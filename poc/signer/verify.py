#!/usr/bin/env python3
"""Verify the provenance attestation embedded in an APK.

Reference implementation of the check the Android verifier app performs.
"""

import argparse
import base64
import json
import os
import sys

from cryptography.exceptions import InvalidSignature
from cryptography.hazmat.primitives import hashes, serialization
from cryptography.hazmat.primitives.asymmetric import ec

import apkblock


class VerificationError(Exception):
    pass


def verify_apk(apk_bytes, public_key):
    """Return the provenance statement, or None if the APK carries none.

    Raises VerificationError if an attestation is present but does not hold.
    """
    try:
        envelope_bytes = apkblock.get_pair(apk_bytes, apkblock.PROVENANCE_PAIR_ID)
    except apkblock.ApkFormatError:
        return None
    if envelope_bytes is None:
        return None

    try:
        envelope = json.loads(envelope_bytes)
        payload = base64.b64decode(envelope["statement"])
        signature = base64.b64decode(envelope["signature"])
    except (ValueError, KeyError, TypeError) as e:
        raise VerificationError(f"malformed attestation: {e}")

    try:
        public_key.verify(signature, payload, ec.ECDSA(hashes.SHA256()))
    except InvalidSignature:
        raise VerificationError("attestation is not signed by the trusted issuer key")

    statement = json.loads(payload)
    subject = statement["subject"]
    if subject["contentDigest"] != "sha256:" + apkblock.content_digest(apk_bytes):
        raise VerificationError("APK content does not match the attestation")
    if subject["signerCertDigest"] != "sha256:" + apkblock.signer_cert_digest(apk_bytes):
        raise VerificationError("APK signing certificate does not match the attestation")
    return statement


def main(argv=None):
    here = os.path.dirname(os.path.abspath(__file__))
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("apk")
    parser.add_argument(
        "--pubkey", default=os.path.join(here, "keys", "mock_samsung_public_key.pem")
    )
    args = parser.parse_args(argv)

    with open(args.pubkey, "rb") as f:
        public_key = serialization.load_pem_public_key(f.read())
    with open(args.apk, "rb") as f:
        apk_bytes = f.read()

    try:
        statement = verify_apk(apk_bytes, public_key)
    except VerificationError as e:
        print(f"INVALID: {e}")
        return 2
    if statement is None:
        print("NO ATTESTATION")
        return 1
    source = statement["source"]
    print(f"VERIFIED: built from {source['repository']} at {source['commit']}")
    print(json.dumps(statement, indent=2))
    return 0


if __name__ == "__main__":
    sys.exit(main())
