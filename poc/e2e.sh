#!/usr/bin/env bash
# Local end-to-end check: build the sample APK, attest it with unverified
# local claims, and confirm that
#   - Android's own signature check still passes after embedding,
#   - the Python and Kotlin verifiers both accept the attested APK,
#   - both reject a tampered copy.
# Needs ANDROID_HOME (with build-tools) and Python with pyjwt + cryptography.
set -euo pipefail
cd "$(dirname "$0")"

apksigner=$(ls "$ANDROID_HOME"/build-tools/*/apksigner | sort | tail -1)
out=$(mktemp -d)
trap 'rm -rf "$out"' EXIT

(cd android && ./gradlew -q :sample:assembleRelease)
apk=android/sample/build/outputs/apk/release/sample-release.apk

echo "== unattested APK"
python3 signer/verify.py "$apk" && exit 1 || [ $? -eq 1 ]

echo "== attest"
python3 signer/attest.py "$apk" --out "$out/attested.apk" \
    --insecure-claims-file signer/local-claims.example.json >/dev/null

echo "== Android signature check on the attested APK"
"$apksigner" verify --verbose "$out/attested.apk" | grep "v2 scheme.*: true"

echo "== Python verifier"
python3 signer/verify.py "$out/attested.apk" | head -1

echo "== tampered copy"
python3 - "$out/attested.apk" "$out/tampered.apk" <<'PY'
import sys
data = bytearray(open(sys.argv[1], "rb").read())
data[len(data) // 3] ^= 1
open(sys.argv[2], "wb").write(data)
PY
python3 signer/verify.py "$out/tampered.apk" && exit 1 || [ $? -eq 2 ]

echo "== Kotlin verifier (accepts attested, rejects tampered)"
(cd android && ./gradlew -q :provenance-core:test -PattestedApk="$out/attested.apk")
echo "ALL OK"
