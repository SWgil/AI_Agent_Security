#!/usr/bin/env bash
# AndroidWorld용 에뮬레이터를 띄우고 부팅 완료까지 기다린다.
# 창 없이 띄우려면: HEADLESS=1 ./emulator.sh
set -euo pipefail
export ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$HOME/Android/Sdk}"
ADB="$ANDROID_SDK_ROOT/platform-tools/adb"
LOG="${EMULATOR_LOG:-/tmp/androidworld-emulator.log}"

args=(-avd AndroidWorldAvd -no-snapshot -grpc 8554)   # -grpc 8554는 AndroidWorld 필수
[ "${HEADLESS:-0}" = 1 ] && args+=(-no-window -no-audio -no-boot-anim -gpu swiftshader_indirect)
if [ "$(uname -s)" = Linux ] && [ ! -w /dev/kvm ]; then
  echo "경고: /dev/kvm이 없다. 소프트웨어 에뮬레이션은 벤치마크를 돌릴 수 없을 만큼 느리다." >&2
  export ANDROID_I_WANT_MY_TCG=yes
  args+=(-accel off)
fi

nohup "$ANDROID_SDK_ROOT/emulator/emulator" "${args[@]}" >"$LOG" 2>&1 &
echo "에뮬레이터 시작 (pid $!, 로그 $LOG). 부팅 대기 중..."
"$ADB" wait-for-device
until [ "$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = 1 ]; do sleep 5; done
"$ADB" devices
