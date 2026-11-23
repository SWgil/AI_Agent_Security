# 로컬 서버의 llama.cpp에 브라우저 에이전트 연결하기 (Windows / Linux)

> 확인 기준일: 2026-11-23. 옵션과 명령어는 각 프로젝트 공식 문서에서 확인했다. 빠르게 바뀌므로 적용 전 [출처](#출처)를 다시 확인한다.
> "추정"이라고 표시한 내용은 공식 문서에 근거가 없는 경험칙이다.

## 1. 전제와 요약

- **전제**: 같은 네트워크의 서버에 llama.cpp의 `llama-server`로 Qwen3.8 27B가 이미 떠 있다. 작업 PC(Windows 또는 Linux)에는 모델 서버를 설치하지 않는다.
- **할 일**: 에이전트의 접속 주소를 `http://<서버IP>:<포트>/v1`로 지정한다. `llama-server`는 OpenAI 호환 chat completions API를 제공하므로 Ollama는 필요 없다.
- **연결 전에 확인할 것**: 서버가 외부 접속을 받는지, 컨텍스트 길이가 충분한지, 도구 호출이 켜져 있는지(2장). 연결은 되는데 에이전트가 제대로 동작하지 않는 경우는 대부분 여기서 갈린다.
- **모델**: Qwen3.8 27B는 도구 호출·비전·thinking을 지원하고, Nanobrowser가 로컬 모델로 직접 권장한다. 단순 작업은 기대할 만하지만 긴 다단계 작업은 클라우드 모델보다 실패율이 높다.

## 2. 서버 쪽 점검 (llama-server)

서버를 띄울 때 쓴 옵션을 아래 표와 비교한다.

| 항목 | 옵션 | 기본값 | 확인할 점 |
|---|---|---|---|
| 수신 주소 | `--host` | `127.0.0.1` | 기본값이면 다른 PC에서 접속할 수 없다. `0.0.0.0` 또는 서버의 LAN IP여야 한다. |
| 포트 | `--port` | `9931` (현재 문서 기준) | 실제로 띄운 포트를 쓴다. 공식 Docker 예제는 `8080`을 쓴다. |
| 컨텍스트 길이 | `-c`, `--ctx-size` | `0` = 모델에서 읽음 | 최소 16k(Nanobrowser 요구), 가능하면 32k 이상. 아래 참고. |
| 도구 호출 | `--jinja` | 켜짐 | `--no-jinja`로 꺼 두지 않았는지 확인한다. |
| 비전 | `--mmproj` | `-hf`로 받으면 자동 | `-m`으로 로컬 GGUF를 지정했다면 `--mmproj`를 따로 줘야 스크린샷을 볼 수 있다. 없어도 텍스트(DOM) 기반으로는 동작한다. |
| thinking | `--reasoning on\|off\|auto` | `auto` | 단계마다 느리면 `off`로 끈다. |
| 인증 | `--api-key` | 없음 | 6장 참고. 설정했다면 클라이언트에 같은 값을 넣는다. |
| CORS | `--cors-origins` | `*` | 기본값이면 브라우저 확장에서 바로 접속된다. 6장 참고. |
| 모델 이름 | `-a`, `--alias` | — | API에서 쓸 이름. 클라이언트의 모델 이름과 맞춘다. |
| 응답 제한 시간 | `-to`, `--timeout` | 3600초 | 보통 그대로 둔다. |

각 옵션은 `LLAMA_ARG_<이름>` 환경 변수로도 줄 수 있다(예: `LLAMA_ARG_CTX_SIZE`). API 키만 `LLAMA_API_KEY`다.

### 컨텍스트 길이

- `-c`를 주지 않으면 모델에 기록된 값을 쓴다. Qwen3.8 27B는 최대 256K라서, 명시하지 않으면 메모리를 과도하게 잡거나 적재에 실패할 수 있다. `-c 32768`처럼 명시하는 편이 안전하다(추정).
- 병렬 슬롯(`-np`, 기본 자동)을 쓰면 슬롯 사이의 컨텍스트 배분이 버전과 옵션(`--kv-unified`, `--kv-unified-per-slot`)에 따라 달라진다. 서버 시작 로그의 슬롯별 `n_ctx`로 실제 값을 확인한다. 혼자 쓴다면 `-np 1`이 단순하다.
- KV 캐시를 강하게 양자화(예: `-ctk q4_0`)하면 도구 호출 성능이 크게 떨어진다고 공식 문서가 경고한다.

### 도구 호출 템플릿

공식 function-calling 문서의 네이티브 지원 목록에는 Qwen 2.5까지만 올라 있고 Qwen3 계열은 확인되지 않았다. 이 경우 GGUF에 내장된 채팅 템플릿이 쓰인다. 도구 호출이 깨지면 `GET /props`의 `chat_template`를 확인하고, 필요하면 `--chat-template-file`로 템플릿을 지정한다.

### 참고: 서버 실행 예시

이미 떠 있는 서버를 다시 띄워야 할 때의 예시다. 파일 이름과 키는 환경에 맞게 바꾼다.

```bash
llama-server -m qwen3.8-27b-q4_k_m.gguf --mmproj mmproj-qwen3.8-27b.gguf \
  -c 32768 -np 1 --host 0.0.0.0 --port 8080 \
  --alias qwen3.8-27b --api-key "<임의의 긴 문자열>"
```

## 3. 작업 PC에서 연결 확인

아래에서 `192.168.0.10:8080`은 예시다. 실제 서버 IP와 포트로 바꾼다.

**Windows (PowerShell)** — `curl`이 아니라 `curl.exe`로 쓴다.

```powershell
curl.exe http://192.168.0.10:8080/health
curl.exe http://192.168.0.10:8080/v1/models -H "Authorization: Bearer <키>"
```

**Linux**

```bash
curl http://192.168.0.10:8080/health
curl http://192.168.0.10:8080/v1/models -H "Authorization: Bearer <키>"
```

- `/health`가 `{"status":"ok"}`를 돌려주면 서버가 준비된 것이다. 이 주소는 API 키 없이 열린다.
- 응답이 없으면 서버의 `--host` 값과 방화벽(서버 OS의 해당 포트 인바운드 허용)을 확인한다.
- `--api-key`를 쓰지 않는 서버라면 `-H` 부분은 뺀다.

## 4. 경로 A — Nanobrowser (코드 없이 시작)

Chrome 확장 프로그램이다. 공식 지원 브라우저는 **Chrome과 Edge**뿐이고, Chromium·Brave 등은 "동작할 수 있으나 테스트되지 않음"이다. Windows와 Linux의 절차는 같다.

1. [Chrome Web Store의 Nanobrowser](https://chromewebstore.google.com/detail/nanobrowser/imbddededgmcgfhfpcjmijokokekbkal)에서 **Chrome에 추가**를 누른다. Edge에서는 먼저 "다른 스토어의 확장 허용"을 켠다.
2. **Settings → Providers**에서 **OpenAI 호환(OpenAI-compatible)** 제공자를 추가한다. README는 llama.cpp를 이 제공자로 연결하라고 안내한다.
3. 주소에 `http://192.168.0.10:8080/v1`, 키에 서버의 `--api-key` 값을 넣는다. 입력 칸의 정확한 이름은 README에 없으므로 화면을 따른다. 요청이 거부되면 제공자 카드에 해결 방법이 표시된다.
4. 모델 이름은 서버의 `--alias` 값(또는 `/v1/models`가 돌려주는 이름)을 쓴다.
5. 사이드 패널에서 간단한 작업부터 시도한다. 예: "example.com의 제목을 알려줘".

`llama-server`의 CORS 기본값이 `*`이므로 Ollama의 `OLLAMA_ORIGINS` 같은 추가 설정은 필요 없다. 단, 서버를 `--cors-origins`로 제한했거나 `--tools`·`--agent`·MCP 옵션으로 띄웠다면(이때 CORS가 localhost로 제한된다) 확장의 origin을 허용해야 한다.

## 5. 경로 B — Browser Use (Python 코드로 제어)

Python 3.11 이상이 필요하다.

### 5.1 설치

**Windows (PowerShell)**

```powershell
# uv가 없다면: winget install astral-sh.uv
uv init --python 3.12 my-agent
cd my-agent
uv add "browser-use==1.0.8"
```

**Linux**

```bash
# uv가 없다면: curl -LsSf https://astral.sh/uv/install.sh | sh
uv init --python 3.12 my-agent && cd my-agent
uv add 'browser-use==1.0.8'
```

README는 버전을 `==`로 고정하라고 안내한다. `Browser('chromium')`은 PC에 설치된 Chrome/Chromium/Edge를 쓴다. 하나도 없으면 `uv run browser-use install`로 Chromium을 한 번 내려받는다.

### 5.2 코드

```python
# main.py
from browser_use import Agent, Browser, ChatOpenAI

llm = ChatOpenAI(
    'qwen3.8-27b',                           # 서버의 --alias 값
    base_url='http://192.168.0.10:8080/v1',  # 서버 IP와 포트
    api_key='none',                          # 서버에 --api-key를 걸었다면 그 값
)
agent = Agent(
    'https://example.com 의 제목은 무엇인가?',
    llm=llm,
    browser=Browser('chromium'),
)
print(agent.run_sync().final_result())
```

```
uv run main.py
```

- `ChatOpenAI`가 받는 인자는 `model`, `api_key`, `base_url`, `temperature`, `reasoning_effort`뿐이다. 다른 인자를 주면 `TypeError`가 난다.
- 1.x에서는 `ChatOllama`가 제거되었다. 인터넷의 0.x 예제는 그대로 동작하지 않는다.
- Linux에서 디스플레이가 없으면 자동으로 헤드리스로 동작한다. 명시하려면 `Browser('chromium', headless=...)`를 쓴다.

## 6. 보안 주의사항

모델 서버가 네트워크에 열려 있으므로 같은 PC에서 돌릴 때보다 챙길 것이 많다.

- **API 키를 건다**: `--host 0.0.0.0`에 `--api-key`가 없으면 같은 네트워크의 누구나 모델을 쓸 수 있다.
- **CORS를 좁힌다**: 기본값 `--cors-origins *`에서는 LAN 안의 PC가 방문한 임의의 웹 페이지가 그 브라우저를 통해 서버 API를 호출할 수 있다. API 키를 걸어 두면 키 없는 요청은 막힌다. Nanobrowser를 쓰지 않는다면 `--cors-origins localhost`로 줄여도 된다(Browser Use는 브라우저가 아니라 Python에서 호출하므로 CORS와 무관하다).
- **평문 HTTP**: API 키와 페이지 내용이 암호화 없이 LAN을 지나간다. 신뢰할 수 없는 네트워크라면 `--ssl-key-file`/`--ssl-cert-file`로 TLS를 켜거나, 서버는 `127.0.0.1`에 두고 SSH 터널(`ssh -L 8080:localhost:8080 서버`)로 접속한다.
- **방화벽**: 서버 포트의 인바운드를 작업 PC의 IP로 제한한다.
- **프롬프트 인젝션은 그대로다**: 로컬 모델이라도 에이전트가 읽은 페이지의 숨은 지시를 따를 수 있다. 평소 쓰는 브라우저 프로필(은행·메일 로그인 상태)에서 돌리지 말고 전용 프로필을 쓰며, 결제·메일 발송·비밀번호 입력은 맡기지 않는다.
- **Browser Use의 디버깅 포트**: `Browser('chromium')`은 127.0.0.1에 인증 없는 디버깅 포트를 연다. 여러 사람이 쓰는 Linux 머신에서는 컨테이너 안에서 돌린다.

## 7. 문제 해결

| 증상 | 원인과 조치 |
|---|---|
| 접속 자체가 안 됨(시간 초과, 연결 거부) | 서버 `--host`가 `127.0.0.1`이거나 방화벽이 막고 있다. 3장의 `/health`로 확인한다. |
| 401 Unauthorized | 서버에 `--api-key`가 걸려 있다. 클라이언트에 같은 키를 넣는다. |
| 에이전트가 앞 단계를 잊거나 같은 행동을 반복 | 컨텍스트 부족. 서버의 `-c`와 시작 로그의 슬롯별 `n_ctx`를 확인한다. |
| 도구 호출 형식 오류, 행동 없이 글만 출력 | `--no-jinja` 여부와 `/props`의 `chat_template`를 확인한다. KV 캐시 양자화도 의심한다. |
| 스크린샷을 이해하지 못함 | 서버에 `--mmproj`가 로드되지 않았다. |
| 단계마다 매우 느림 | thinking을 끄거나(`--reasoning off`) `reasoning_effort`를 낮춘다. 다른 사용자가 같은 서버를 쓰고 있는지도 확인한다. |
| Nanobrowser에서만 요청이 거부됨 | 서버의 `--cors-origins` 제한 또는 `--tools`/`--agent` 옵션. 4장 참고. |
| `ImportError: ChatOllama`, `TypeError` | Browser Use 0.x용 예제이거나 지원하지 않는 인자다. 5.2를 따른다. |

## 출처

- [llama.cpp server README](https://github.com/ggml-org/llama.cpp/blob/master/tools/server/README.md) · [Function calling](https://github.com/ggml-org/llama.cpp/blob/master/docs/function-calling.md) · [Multimodal](https://github.com/ggml-org/llama.cpp/blob/master/docs/multimodal.md)
- [Browser Use GitHub](https://github.com/browser-use/browser-use) · [Quickstart](https://docs.browser-use.com/quickstart) · [Supported models](https://docs.browser-use.com/supported-models)
- [Nanobrowser GitHub](https://github.com/nanobrowser/nanobrowser)
- [Ollama 라이브러리: qwen3.8](https://ollama.com/library/qwen3.8) (모델 사양 확인용)
