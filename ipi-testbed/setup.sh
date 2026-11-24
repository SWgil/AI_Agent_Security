#!/usr/bin/env bash
# mobilerun(구 droidrun) IPI 벤치마크 테스트 환경 설치 스크립트.
# 여러 번 실행해도 안전하다. 단계별 실행: ./setup.sh [repos|python|sdk|avd|all]
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TP="$ROOT/third_party"
export ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$HOME/Android/Sdk}"
export ANDROID_HOME="$ANDROID_SDK_ROOT"
CMDLINE_TOOLS_ZIP="commandlinetools-linux-11076708_latest.zip"
AVD_NAME="AndroidWorldAvd"
SYS_IMAGE="system-images;android-33;google_apis;$( [ "$(uname -m)" = aarch64 ] && echo arm64-v8a || echo x86_64 )"

# 저장소와 고정 커밋. 재현성을 위해 설치 시점의 HEAD를 고정했다.
REPOS=(
  "droidrun-android-world https://github.com/droidrun/droidrun-android-world 5090130394bc93a6e2bd8069e4c2d6b9c05aa112"
  "droidrun               https://github.com/droidrun/droidrun               e34004270a9d7b68824b94a3a5ba6698cefea69d"
  "AgentHazard            https://github.com/Zsbyqx20/AgentHazard            659ca72b386b4286919a7fd8814d7ca48fc8eec3"
  "AWAttackerApplier      https://github.com/Zsbyqx20/AWAttackerApplier      781e2e6b29a6b54ec1529f96943949c7b68b195b"
  "mobilesafetybench      https://github.com/jylee425/mobilesafetybench      d775aa475f4ec3524c0502489b167673c2087282"
)

step_repos() {
  mkdir -p "$TP"
  for entry in "${REPOS[@]}"; do
    read -r name url sha <<<"$entry"
    if [ ! -d "$TP/$name/.git" ]; then
      git init -q "$TP/$name"
      git -C "$TP/$name" remote add origin "$url"
    fi
    if [ "$(git -C "$TP/$name" rev-parse HEAD 2>/dev/null)" != "$sha" ]; then
      git -C "$TP/$name" fetch -q --depth 1 origin "$sha"
      git -C "$TP/$name" checkout -q FETCH_HEAD
    fi
    echo "[repos] $name @ ${sha:0:10}"
  done
  # 하네스가 고정한 droidrun, android_world 서브모듈
  git -C "$TP/droidrun-android-world" submodule update --init --depth 1
}

step_python() {
  command -v uv >/dev/null || { echo "uv가 필요하다: https://docs.astral.sh/uv/"; exit 1; }
  local h="$TP/droidrun-android-world"
  # 하네스 최신 커밋은 없는 함수를 import해서 CLI가 뜨지 않는다. 패치로 보완한다.
  git -C "$h" apply --check "$ROOT/patches/droidrun-android-world.patch" 2>/dev/null \
    && git -C "$h" apply "$ROOT/patches/droidrun-android-world.patch"
  # `uv sync --locked`는 쓰지 못한다. 상위 저장소의 uv.lock이 pyproject와 어긋나 있고,
  # android_world의 setup.py는 setuptools 81에서 제거된 pkg_resources를 쓴다.
  # 그래서 lock에 고정된 의존성을 먼저 설치하고, 워크스페이스 패키지는
  # setuptools<81 빌드 제약으로 따로 설치한다.
  local req; req="$(mktemp)"
  (
    cd "$h"
    uv venv -q --python 3.11 --allow-existing .venv
    uv export -q --frozen --no-emit-workspace --no-hashes -o "$req"
    uv pip install -q -r "$req"
    uv pip install -q --no-deps -b "$ROOT/build-constraints.txt" -e android_world -e droidrun -e .
    .venv/bin/droidworld version
  )
  rm -f "$req"
  (cd "$TP/AgentHazard" && uv sync -q)
  echo "[python] droidworld: $TP/droidrun-android-world/.venv/bin/droidworld"
}

step_sdk() {
  command -v java >/dev/null || { echo "JDK 17 이상이 필요하다"; exit 1; }
  local bin="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin"
  if [ ! -x "$bin/sdkmanager" ]; then
    mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools"
    local tmp; tmp="$(mktemp -d)"
    curl -fsSL -o "$tmp/clt.zip" "https://dl.google.com/android/repository/$CMDLINE_TOOLS_ZIP"
    unzip -q "$tmp/clt.zip" -d "$tmp"
    rm -rf "$ANDROID_SDK_ROOT/cmdline-tools/latest"
    mv "$tmp/cmdline-tools" "$ANDROID_SDK_ROOT/cmdline-tools/latest"
    rm -rf "$tmp"
  fi
  yes | "$bin/sdkmanager" --licenses >/dev/null || true
  "$bin/sdkmanager" "platform-tools" "emulator" "platforms;android-33" "$SYS_IMAGE"
  echo "[sdk] $ANDROID_SDK_ROOT"
}

step_avd() {
  local bin="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin"
  if "$bin/avdmanager" list avd -c | grep -qx "$AVD_NAME"; then
    echo "[avd] $AVD_NAME 이미 있음"
  else
    echo no | "$bin/avdmanager" create avd -n "$AVD_NAME" -k "$SYS_IMAGE" -d pixel_6
    echo "[avd] $AVD_NAME 생성"
  fi
}

case "${1:-all}" in
  repos)  step_repos ;;
  python) step_python ;;
  sdk)    step_sdk ;;
  avd)    step_avd ;;
  all)    step_repos; step_python; step_sdk; step_avd ;;
  *) echo "usage: $0 [repos|python|sdk|avd|all]"; exit 2 ;;
esac
