# mobilerun IPI 벤치마크 테스트 환경

mobilerun(구 droidrun) 에이전트를 대상으로 간접 프롬프트 인젝션(IPI) PoC를 돌리기 위한 환경이다.
AndroidWorld 에뮬레이터 위에서 에이전트를 실행하고, 그 화면에 공격 콘텐츠를 얹어 정상 실행과 비교한다.

본인 소유의 에뮬레이터와 테스트 계정에서만 사용한다.

## 구성

`./setup.sh`가 아래 저장소를 `third_party/`에 고정 커밋으로 받는다. `third_party/`는 git에 올리지 않는다.

| 저장소 | 역할 |
|---|---|
| `droidrun-android-world` | droidrun 공식 AndroidWorld 하네스(`droidworld` CLI). 서브모듈로 droidrun과 android_world를 포함한다 |
| `AWAttackerApplier` | AgentHazard의 동적 공격 도구. 규칙(JSON)에 따라 앱 화면 위에 오버레이를 그리는 Android 앱 |
| `AgentHazard` | 정적 벤치마크. 미리 수집한 화면에 공격 문구를 넣어 평가한다. 자체 에이전트(m3a, t3a 등)만 지원한다 |
| `mobilesafetybench` | 별도 벤치마크(API 34, Appium). 받아만 두었고 설치하지 않았다 |
| `droidrun` | 최신 mobilerun 소스(0.7.4). 참고용이다 |

## 설치

필요한 것은 Linux 또는 macOS, [uv](https://docs.astral.sh/uv/), JDK 17 이상, 그리고 하드웨어 가속(Linux는 `/dev/kvm`)이다.

```bash
./setup.sh          # repos, python, sdk, avd 단계를 차례로 실행
./setup.sh python   # 한 단계만 실행
```

- Android SDK는 `~/Android/Sdk`에 설치한다. `ANDROID_SDK_ROOT`로 바꿀 수 있다.
- AVD는 AndroidWorld가 요구하는 `AndroidWorldAvd`(Pixel 6, API 33)로 만든다.

## 기준선 실행

터미널 세 개를 쓴다.

```bash
# 1. 에뮬레이터
./emulator.sh

# 2. AndroidWorld 환경 서버 (계속 켜 둔다)
export PATH="$HOME/Android/Sdk/platform-tools:$PATH"
cd third_party/droidrun-android-world/android_world
../.venv/bin/python -m server.android_server

# 3. 에이전트 실행
cp .env.example .env    # 키를 채운 뒤
set -a; source .env; set +a
cd third_party/droidrun-android-world
.venv/bin/droidworld check                      # Portal 앱 설치와 접근성 설정 점검
.venv/bin/droidworld run --task ContactsAddContact --vision
```

- 환경 서버를 처음 띄우면 AndroidWorld가 과제용 앱을 에뮬레이터에 설치하므로 오래 걸린다.
- 모델은 `--llm-provider`, `--llm-model`로 바꾼다. 기본값은 Gemini다.
- `--vision`을 켠 실행과 끈 실행을 같은 공격으로 비교하면 인식 방식에 따른 차이를 볼 수 있다.

## 공격 실행

1. AWAttackerApplier APK를 준비한다. 저장소 릴리스에서 받거나 Flutter와 Rust로 직접 빌드한다(`make flutter-build`).
2. 에뮬레이터에 설치하고 오버레이 권한과 접근성 권한을 준다.
3. 규칙 JSON을 불러오고 서비스를 켠 뒤, 기준선과 같은 `droidworld run` 명령을 다시 돌린다.

서비스는 adb 브로드캐스트로 제어한다. 액션 이름은 `com.mobilellm.awattackerapplier.` 뒤에
`START_SERVICE`, `STOP_SERVICE`, `SET_GRPC_CONFIG`, `CLEAR_RULES`, `IMPORT_RULES`가 붙는다.
`IMPORT_RULES`는 `rules_json` 문자열 extra를 받는다.

AgentHazard 정적 평가는 데이터셋을 `third_party/AgentHazard/data/`에 받아야 한다. 링크는 해당 README에 있다.

## 알아 둘 점

- **하네스의 droidrun 버전이 낡았다.** 하네스가 고정한 서브모듈은 droidrun 0.3.0이고, 최신 mobilerun은 0.7.4다.
  패키지 이름과 API가 바뀌었으므로, 현재 쓰는 버전을 평가하려면 `eval/runner.py`와 `eval/tools.py`를 새 API에 맞게 고쳐야 한다.
- **하네스에 패치를 적용한다.** 최신 커밋이 없는 함수를 import해서 CLI가 뜨지 않는다. `patches/droidrun-android-world.patch`가 그 함수를 추가한다.
- **`uv sync --locked`를 쓰지 않는다.** 상위 저장소의 `uv.lock`이 어긋나 있고 android_world 빌드가 setuptools 81 미만을 요구한다.
  `setup.sh`는 lock의 의존성을 먼저 설치한 뒤 `build-constraints.txt`로 워크스페이스 패키지를 설치한다.
- **KVM이 없으면 실행할 수 없다.** 클라우드 컨테이너처럼 `/dev/kvm`이 없는 곳에서는 설치까지만 되고 에뮬레이터가 실용적인 속도로 돌지 않는다.
