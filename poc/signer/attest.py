#!/usr/bin/env python3
"""Mock "Samsung" provenance issuer for the PoC.

Runs inside the trusted build workflow. It
  1. asks GitHub Actions for an OIDC token that identifies this build,
  2. verifies that token against GitHub's public keys,
  3. checks the build policy (GitHub-hosted runner, trusted workflow),
  4. signs a provenance statement with the mock issuer key, and
  5. embeds the statement in the APK Signing Block.

In a real deployment steps 2-4 would run on the issuer's server, which would
hold the signing key. Here they run in the workflow so the PoC needs no server.
"""

import argparse
import base64
import datetime
import hashlib
import json
import os
import sys
import urllib.request

import jwt
from cryptography.hazmat.primitives import hashes, serialization
from cryptography.hazmat.primitives.asymmetric import ec

import apkblock

GITHUB_OIDC_ISSUER = "https://token.actions.githubusercontent.com"
GITHUB_OIDC_JWKS = GITHUB_OIDC_ISSUER + "/.well-known/jwks"
AUDIENCE = "mock-samsung-provenance"
STATEMENT_TYPE = "mock-samsung-provenance/v1"
ISSUER_NAME = "Mock Samsung Provenance CA (PoC, not affiliated with Samsung)"


class PolicyError(Exception):
    pass


def fetch_oidc_token(audience=AUDIENCE):
    url = os.environ.get("ACTIONS_ID_TOKEN_REQUEST_URL")
    bearer = os.environ.get("ACTIONS_ID_TOKEN_REQUEST_TOKEN")
    if not url or not bearer:
        raise PolicyError(
            "no OIDC token endpoint: the job needs 'permissions: id-token: write'"
        )
    request = urllib.request.Request(
        f"{url}&audience={audience}", headers={"Authorization": f"Bearer {bearer}"}
    )
    with urllib.request.urlopen(request, timeout=30) as response:
        return json.load(response)["value"]


def verify_oidc_token(token, audience=AUDIENCE):
    signing_key = jwt.PyJWKClient(GITHUB_OIDC_JWKS).get_signing_key_from_jwt(token)
    return jwt.decode(
        token,
        signing_key.key,
        algorithms=["RS256"],
        audience=audience,
        issuer=GITHUB_OIDC_ISSUER,
    )


def check_policy(claims, trusted_workflow_prefix):
    if claims.get("runner_environment") != "github-hosted":
        raise PolicyError(
            f"build did not run on a GitHub-hosted runner: {claims.get('runner_environment')!r}"
        )
    workflow_ref = claims.get("job_workflow_ref", "")
    if not workflow_ref.startswith(trusted_workflow_prefix):
        raise PolicyError(
            f"build did not run in the trusted workflow: {workflow_ref!r} "
            f"does not start with {trusted_workflow_prefix!r}"
        )


def build_statement(claims, apk_bytes, now=None):
    now = now or datetime.datetime.now(datetime.timezone.utc)
    server = claims.get("iss_server_url", "https://github.com")
    return {
        "type": STATEMENT_TYPE,
        "issuer": ISSUER_NAME,
        "issuedAt": now.strftime("%Y-%m-%dT%H:%M:%SZ"),
        "subject": {
            "contentDigest": "sha256:" + apkblock.content_digest(apk_bytes),
            "signerCertDigest": "sha256:" + apkblock.signer_cert_digest(apk_bytes),
        },
        "source": {
            "repository": claims["repository"],
            "repositoryId": str(claims["repository_id"]),
            "owner": claims["repository_owner"],
            "ownerId": str(claims["repository_owner_id"]),
            "visibility": claims.get("repository_visibility", ""),
            "commit": claims["sha"],
            "ref": claims.get("ref", ""),
        },
        "build": {
            "workflowRef": claims.get("job_workflow_ref", ""),
            "runnerEnvironment": claims.get("runner_environment", ""),
            "runUrl": f"{server}/{claims['repository']}/actions/runs/{claims.get('run_id', '')}",
        },
    }


def key_id(public_key):
    der = public_key.public_bytes(
        serialization.Encoding.DER, serialization.PublicFormat.SubjectPublicKeyInfo
    )
    return hashlib.sha256(der).hexdigest()[:16]


def sign_statement(statement, private_key):
    payload = json.dumps(statement, sort_keys=True, separators=(",", ":")).encode()
    signature = private_key.sign(payload, ec.ECDSA(hashes.SHA256()))
    envelope = {
        "v": 1,
        "keyId": key_id(private_key.public_key()),
        "statement": base64.b64encode(payload).decode(),
        "signature": base64.b64encode(signature).decode(),
    }
    return json.dumps(envelope, separators=(",", ":")).encode()


def attest_apk(apk_bytes, claims, private_key, now=None):
    """Return the APK with a signed provenance statement embedded."""
    statement = build_statement(claims, apk_bytes, now)
    envelope = sign_statement(statement, private_key)
    return apkblock.set_pair(apk_bytes, apkblock.PROVENANCE_PAIR_ID, envelope), statement


def load_private_key(path):
    pem = os.environ.get("MOCK_SAMSUNG_KEY_PEM")
    if pem:
        source = "MOCK_SAMSUNG_KEY_PEM"
        data = pem.encode()
    else:
        source = path
        with open(path, "rb") as f:
            data = f.read()
    return serialization.load_pem_private_key(data, password=None), source


def main(argv=None):
    here = os.path.dirname(os.path.abspath(__file__))
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("apk", help="signed APK to attest")
    parser.add_argument("--out", help="output path (default: overwrite the input)")
    parser.add_argument(
        "--key",
        default=os.path.join(here, "keys", "mock_samsung_demo_private_key.pem"),
        help="issuer private key (PEM); MOCK_SAMSUNG_KEY_PEM overrides it",
    )
    parser.add_argument(
        "--trusted-workflow-prefix",
        help="required prefix of the job_workflow_ref claim, "
        "e.g. owner/repo/.github/workflows/attested-build.yml@",
    )
    parser.add_argument(
        "--insecure-claims-file",
        help="LOCAL TESTING ONLY: read build claims from a JSON file instead of "
        "verifying a GitHub OIDC token",
    )
    args = parser.parse_args(argv)

    try:
        if args.insecure_claims_file:
            print("WARNING: using unverified claims from a file (local testing only)")
            with open(args.insecure_claims_file) as f:
                claims = json.load(f)
        else:
            if not args.trusted_workflow_prefix:
                parser.error("--trusted-workflow-prefix is required")
            claims = verify_oidc_token(fetch_oidc_token())
            check_policy(claims, args.trusted_workflow_prefix)

        with open(args.apk, "rb") as f:
            apk_bytes = f.read()
        private_key, key_source = load_private_key(args.key)
        attested, statement = attest_apk(apk_bytes, claims, private_key)
    except (PolicyError, apkblock.ApkFormatError, jwt.PyJWTError) as e:
        print(f"attestation refused: {e}", file=sys.stderr)
        return 1

    out = args.out or args.apk
    with open(out, "wb") as f:
        f.write(attested)
    print(f"issuer key: {key_source} (keyId {key_id(private_key.public_key())})")
    print(json.dumps(statement, indent=2))
    print(f"wrote {out}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
