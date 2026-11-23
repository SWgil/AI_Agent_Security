"""APK Signing Block helpers for the provenance PoC.

An APK that carries a v2/v3 signature is laid out as:

    [ZIP entries][APK Signing Block][Central Directory][End of Central Directory]

The signing block is a list of (id, value) pairs. Verifiers ignore ids they do
not know, and the v2/v3 signature does not cover the signing block itself, so
an extra pair can be added after signing without invalidating the signature.
The provenance attestation is stored in such a pair.
"""

import hashlib
import struct

EOCD_SIGNATURE = b"PK\x05\x06"
EOCD_MIN_SIZE = 22
SIGNING_BLOCK_MAGIC = b"APK Sig Block 42"

PROVENANCE_PAIR_ID = 0x50524F56  # "PROV"
PADDING_PAIR_ID = 0x42726577
V2_PAIR_ID = 0x7109871A
V3_PAIR_ID = 0xF05368C0

BLOCK_ALIGNMENT = 4096


class ApkFormatError(Exception):
    pass


class ApkLayout:
    def __init__(self, signing_block_start, central_dir_start, eocd_start, pairs):
        self.signing_block_start = signing_block_start
        self.central_dir_start = central_dir_start
        self.eocd_start = eocd_start
        self.pairs = pairs  # list of (id, bytes), in file order


def _find_eocd(data):
    lowest = max(0, len(data) - EOCD_MIN_SIZE - 0xFFFF)
    pos = len(data) - EOCD_MIN_SIZE
    while pos >= lowest:
        if data[pos:pos + 4] == EOCD_SIGNATURE:
            (comment_len,) = struct.unpack_from("<H", data, pos + 20)
            if pos + EOCD_MIN_SIZE + comment_len == len(data):
                return pos
        pos -= 1
    raise ApkFormatError("ZIP end-of-central-directory record not found")


def parse(data):
    """Locate the signing block and return an ApkLayout."""
    eocd = _find_eocd(data)
    (cd_size, cd_start) = struct.unpack_from("<II", data, eocd + 12)
    if cd_start == 0xFFFFFFFF:
        raise ApkFormatError("ZIP64 archives are not supported")
    if cd_start + cd_size != eocd:
        raise ApkFormatError("central directory does not end at the EOCD record")
    if cd_start < 32 or data[cd_start - 16:cd_start] != SIGNING_BLOCK_MAGIC:
        raise ApkFormatError("no APK Signing Block (the APK needs a v2 or v3 signature)")
    (size_footer,) = struct.unpack_from("<Q", data, cd_start - 24)
    block_start = cd_start - size_footer - 8
    if block_start < 0:
        raise ApkFormatError("signing block size is out of range")
    (size_header,) = struct.unpack_from("<Q", data, block_start)
    if size_header != size_footer:
        raise ApkFormatError("signing block header and footer sizes differ")

    pairs = []
    pos = block_start + 8
    end = cd_start - 24
    while pos < end:
        if pos + 12 > end:
            raise ApkFormatError("truncated signing block pair")
        (pair_len, pair_id) = struct.unpack_from("<QI", data, pos)
        if pair_len < 4 or pos + 8 + pair_len > end:
            raise ApkFormatError("signing block pair length is out of range")
        pairs.append((pair_id, data[pos + 12:pos + 8 + pair_len]))
        pos += 8 + pair_len
    return ApkLayout(block_start, cd_start, eocd, pairs)


def _build_block(pairs):
    pairs = [(i, v) for (i, v) in pairs if i != PADDING_PAIR_ID]
    body = b"".join(struct.pack("<QI", len(v) + 4, i) + v for (i, v) in pairs)
    total = 8 + len(body) + 24
    if total % BLOCK_ALIGNMENT:
        pad = (BLOCK_ALIGNMENT - (total + 12) % BLOCK_ALIGNMENT) % BLOCK_ALIGNMENT
        body += struct.pack("<QI", pad + 4, PADDING_PAIR_ID) + bytes(pad)
    size = len(body) + 24
    return struct.pack("<Q", size) + body + struct.pack("<Q", size) + SIGNING_BLOCK_MAGIC


def get_pair(data, pair_id):
    for (i, value) in parse(data).pairs:
        if i == pair_id:
            return value
    return None


def set_pair(data, pair_id, value):
    """Return a copy of the APK with the given pair added or replaced."""
    layout = parse(data)
    pairs = [(i, v) for (i, v) in layout.pairs if i != pair_id]
    pairs.append((pair_id, value))
    block = _build_block(pairs)
    new_cd_start = layout.signing_block_start + len(block)
    eocd = bytearray(data[layout.eocd_start:])
    struct.pack_into("<I", eocd, 16, new_cd_start)
    return (
        data[:layout.signing_block_start]
        + block
        + data[layout.central_dir_start:layout.eocd_start]
        + bytes(eocd)
    )


def content_digest(data):
    """SHA-256 over everything except the signing block.

    Covers the ZIP entries, the central directory and the EOCD record. The
    central directory offset inside the EOCD is zeroed because it moves when
    the signing block changes size. Each section is length-prefixed so the
    section boundaries are part of the digest.
    """
    layout = parse(data)
    eocd = bytearray(data[layout.eocd_start:])
    struct.pack_into("<I", eocd, 16, 0)
    h = hashlib.sha256()
    for section in (
        data[:layout.signing_block_start],
        data[layout.central_dir_start:layout.eocd_start],
        bytes(eocd),
    ):
        h.update(struct.pack("<Q", len(section)))
        h.update(section)
    return h.hexdigest()


def _read_prefixed(buf, pos):
    if pos + 4 > len(buf):
        raise ApkFormatError("truncated length-prefixed field")
    (length,) = struct.unpack_from("<I", buf, pos)
    if pos + 4 + length > len(buf):
        raise ApkFormatError("length-prefixed field is out of range")
    return buf[pos + 4:pos + 4 + length], pos + 4 + length


def signer_cert_digest(data):
    """SHA-256 of the first signer's certificate (v3 block if present, else v2).

    This is read from the block without checking the APK signature. Android
    checks that signature itself when the APK is installed.
    """
    pairs = dict(parse(data).pairs)
    block = pairs.get(V3_PAIR_ID) or pairs.get(V2_PAIR_ID)
    if block is None:
        raise ApkFormatError("no v2 or v3 signature in the signing block")
    signers, _ = _read_prefixed(block, 0)
    signer, _ = _read_prefixed(signers, 0)
    signed_data, _ = _read_prefixed(signer, 0)
    _digests, pos = _read_prefixed(signed_data, 0)
    certificates, _ = _read_prefixed(signed_data, pos)
    certificate, _ = _read_prefixed(certificates, 0)
    return hashlib.sha256(certificate).hexdigest()
