import io
import os
import struct
import sys
import zipfile

import pytest
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import ec

sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

import apkblock  # noqa: E402
import attest  # noqa: E402
import verify  # noqa: E402

CLAIMS = {
    "repository": "alice/notes-app",
    "repository_id": "1001",
    "repository_owner": "alice",
    "repository_owner_id": "2002",
    "repository_visibility": "public",
    "sha": "a" * 40,
    "ref": "refs/heads/main",
    "job_workflow_ref": "issuer/builder/.github/workflows/attested-build.yml@refs/heads/main",
    "runner_environment": "github-hosted",
    "run_id": "42",
}


def _prefixed(b):
    return struct.pack("<I", len(b)) + b


def make_apk(payload=b"hello", cert=b"fake-certificate"):
    """A ZIP with a synthetic signing block shaped like a v2 signature."""
    buf = io.BytesIO()
    with zipfile.ZipFile(buf, "w") as z:
        z.writestr("classes.dex", payload)
    raw = buf.getvalue()
    eocd = raw.rfind(apkblock.EOCD_SIGNATURE)
    (cd_start,) = struct.unpack_from("<I", raw, eocd + 16)

    signed_data = _prefixed(b"") + _prefixed(_prefixed(cert)) + _prefixed(b"")
    signer = _prefixed(signed_data) + _prefixed(b"") + _prefixed(b"")
    v2_value = _prefixed(_prefixed(signer))
    block = apkblock._build_block([(apkblock.V2_PAIR_ID, v2_value)])

    tail = bytearray(raw[eocd:])
    struct.pack_into("<I", tail, 16, cd_start + len(block))
    return raw[:cd_start] + block + raw[cd_start:eocd] + bytes(tail)


@pytest.fixture
def key():
    return ec.generate_private_key(ec.SECP256R1())


def test_block_is_aligned_and_zip_stays_readable(key):
    apk = make_apk()
    attested, _ = attest.attest_apk(apk, CLAIMS, key)
    layout = apkblock.parse(attested)
    assert (layout.central_dir_start - layout.signing_block_start) % 4096 == 0
    assert zipfile.ZipFile(io.BytesIO(attested)).read("classes.dex") == b"hello"


def test_embedding_does_not_change_the_attested_digests(key):
    apk = make_apk()
    attested, _ = attest.attest_apk(apk, CLAIMS, key)
    assert apkblock.content_digest(attested) == apkblock.content_digest(apk)
    assert apkblock.signer_cert_digest(attested) == apkblock.signer_cert_digest(apk)
    assert apkblock.get_pair(attested, apkblock.V2_PAIR_ID) == apkblock.get_pair(
        apk, apkblock.V2_PAIR_ID
    )


def test_verify_returns_source_claims(key):
    attested, _ = attest.attest_apk(make_apk(), CLAIMS, key)
    statement = verify.verify_apk(attested, key.public_key())
    assert statement["source"]["repository"] == "alice/notes-app"
    assert statement["source"]["repositoryId"] == "1001"
    assert statement["source"]["commit"] == "a" * 40


def test_recorded_inputs_are_summarized_in_the_statement(key):
    inputs = {
        "networkHosts": ["repo.maven.apache.org:443", "dl.google.com:443"],
        "artifacts": [{"name": "org.example:lib:1.2/lib-1.2.jar", "sha256": "ab" * 32}],
    }
    attested, _ = attest.attest_apk(make_apk(), CLAIMS, key, inputs=inputs)
    recorded = verify.verify_apk(attested, key.public_key())["inputs"]
    assert recorded["networkHosts"] == ["dl.google.com:443", "repo.maven.apache.org:443"]
    assert recorded["artifactCount"] == 1

    changed = dict(inputs, artifacts=[{"name": "org.example:lib:1.2/lib-1.2.jar", "sha256": "cd" * 32}])
    assert attest.summarize_inputs(changed)["artifactsDigest"] != recorded["artifactsDigest"]


def test_statement_without_recorded_inputs_has_no_inputs_field(key):
    attested, _ = attest.attest_apk(make_apk(), CLAIMS, key)
    assert "inputs" not in verify.verify_apk(attested, key.public_key())


def test_unattested_apk_has_no_attestation(key):
    assert verify.verify_apk(make_apk(), key.public_key()) is None


def test_attesting_twice_replaces_the_statement(key):
    once, _ = attest.attest_apk(make_apk(), CLAIMS, key)
    twice, _ = attest.attest_apk(once, dict(CLAIMS, sha="b" * 40), key)
    pairs = [i for (i, _) in apkblock.parse(twice).pairs]
    assert pairs.count(apkblock.PROVENANCE_PAIR_ID) == 1
    assert verify.verify_apk(twice, key.public_key())["source"]["commit"] == "b" * 40


def test_modified_content_is_rejected(key):
    attested = bytearray(attest.attest_apk(make_apk(), CLAIMS, key)[0])
    attested[attested.index(b"hello")] ^= 1
    with pytest.raises(verify.VerificationError, match="content"):
        verify.verify_apk(bytes(attested), key.public_key())


def test_attestation_moved_to_another_apk_is_rejected(key):
    attested, _ = attest.attest_apk(make_apk(b"hello"), CLAIMS, key)
    envelope = apkblock.get_pair(attested, apkblock.PROVENANCE_PAIR_ID)
    other = apkblock.set_pair(make_apk(b"evil!"), apkblock.PROVENANCE_PAIR_ID, envelope)
    with pytest.raises(verify.VerificationError, match="content"):
        verify.verify_apk(other, key.public_key())


def test_resigned_apk_is_rejected(key):
    attested, _ = attest.attest_apk(make_apk(cert=b"original"), CLAIMS, key)
    envelope = apkblock.get_pair(attested, apkblock.PROVENANCE_PAIR_ID)
    resigned = apkblock.set_pair(
        make_apk(cert=b"attacker"), apkblock.PROVENANCE_PAIR_ID, envelope
    )
    with pytest.raises(verify.VerificationError, match="certificate"):
        verify.verify_apk(resigned, key.public_key())


def test_other_issuer_key_is_rejected(key):
    attested, _ = attest.attest_apk(make_apk(), CLAIMS, key)
    other = ec.generate_private_key(ec.SECP256R1())
    with pytest.raises(verify.VerificationError, match="trusted issuer"):
        verify.verify_apk(attested, other.public_key())


def test_policy_requires_github_hosted_runner():
    with pytest.raises(attest.PolicyError, match="GitHub-hosted"):
        attest.check_policy(
            dict(CLAIMS, runner_environment="self-hosted"),
            "issuer/builder/.github/workflows/attested-build.yml@",
        )


def test_policy_requires_trusted_workflow():
    with pytest.raises(attest.PolicyError, match="trusted workflow"):
        attest.check_policy(
            dict(CLAIMS, job_workflow_ref="alice/notes-app/.github/workflows/ci.yml@refs/heads/main"),
            "issuer/builder/.github/workflows/attested-build.yml@",
        )


def test_policy_accepts_trusted_build():
    attest.check_policy(CLAIMS, "issuer/builder/.github/workflows/attested-build.yml@")


def test_committed_demo_keys_match():
    keys = os.path.join(os.path.dirname(__file__), "..", "keys")
    with open(os.path.join(keys, "mock_samsung_demo_private_key.pem"), "rb") as f:
        private_key = serialization.load_pem_private_key(f.read(), password=None)
    with open(os.path.join(keys, "mock_samsung_public_key.pem"), "rb") as f:
        public_key = serialization.load_pem_public_key(f.read())
    assert attest.key_id(private_key.public_key()) == attest.key_id(public_key)
