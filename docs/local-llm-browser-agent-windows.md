# Windows에서 로컬 LLM으로 브라우저 에이전트 시작하기

> 확인 기준일: 2026-11-23. 버전·명령어는 각 프로젝트 공식 문서에서 확인한 것이며, 빠르게 바뀌므로 설치 전 [출처](#출처)를 다시 확인한다.
> "추정"이라고 표시한 수치는 공식 문서가 아닌 일반적인 경험칙이다.

## 1. 요약

- **구성**: Ollama(로컬 모델 서버) + 브라우저 에이전트(Nanobrowser 또는 Browser Use).
- **모델**: `qwen3.8:27b`로 가능하다. 도구 호출·비전·thinking을 모두 지원하고, Nanobrowser가 로컬 모델로 직접 권장하는 모델이다.
- **가장 흔한 실패 원인**: Ollama의 기본 컨텍스트 길이가 너무 작다. 반드시 늘려야 한다(3장).
- **보안**: 로컬 모델이라도 프롬프트 인젝션 위험은 그대로다. 전용 브라우저 프로필에서 시작한다(6장).

## 2. Qwen3.8 27B로 가능한가

가능하다. 근거는 다음과 같다.

| 항목 | 내용 | 출처 |
|---|---|---|
| 공개 | 2026-08-14 | Wikipedia |
| Ollama 태그 | `qwen3.8:27b` (= `latest`), 18GB | Ollama 라이브러리 |
| 컨텍스트 창 | 최대 256K | Ollama 라이브러리 |
| 기능 | tools, vision, thinking (입력: 텍스트·이미지) | Ollama 라이브러리 |
| 양자화 변형 | `27b-q4_K_M` 18GB, `27b-q8_0` 30GB, `27b-bf16` 56GB | Ollama 라이브러리 |
| 에이전트 적합성 | Nanobrowser README: "We currently recommend Qwen3.8 27B. Give them a context window of 16k tokens or more." | Nanobrowser |

### 하드웨어 (추정)

모델 파일이 18GB이고, 컨텍스트를 늘릴수록 메모리가 더 필요하다.

| GPU VRAM | 예상 |
|---|---|
| 24GB (RTX 3090/4090 등) | 기본 태그(18GB)를 GPU에 모두 올릴 수 있다. 컨텍스트 16k~32k 정도가 현실적이다. |
| 32GB 이상 | 컨텍스트 64k까지 여유가 있다. |
| 16GB 이하 | 일부가 CPU/RAM으로 넘어가 매우 느려진다. 단계마다 수십 초 이상 걸릴 수 있어 에이전트 용도로는 권하지 않는다. |

실제 적재 상태는 `ollama ps`의 `PROCESSOR` 열로 확인한다. `100% GPU`가 아니면 컨텍스트를 줄이거나 더 작은 모델을 쓴다.

### 한계

- 단순 작업(검색, 페이지 요약, 폼 한두 개 입력)은 기대할 만하다. 긴 다단계 작업은 상용 클라우드 모델보다 실패율이 높다.
- BrowserOS 문서도 로컬 모델은 에이전트 모드에 아직 부족할 수 있다고 안내한다.
- thinking이 기본으로 켜져 있어 단계마다 지연이 생긴다. 느리면 요청 단위로 끄거나 `reasoning_effort`를 낮춘다.

## 3. 공통 준비: Ollama 설치와 설정

### 3.1 설치와 모델 받기

1. <https://ollama.com/download> 에서 Windows 설치 파일을 받아 설치한다.
2. PowerShell에서 모델을 받는다(18GB).

```powershell
ollama pull qwen3.8:27b
ollama run qwen3.8:27b "안녕"   # 동작 확인
```

### 3.2 컨텍스트 길이 늘리기 (필수)

Ollama의 기본 컨텍스트는 VRAM에 따라 정해진다.

| VRAM | 기본 컨텍스트 |
|---|---|
| 24GiB 미만 | 4k |
| 24~48GiB | 32k |
| 48GiB 이상 | 256k |

브라우저 에이전트는 페이지 DOM과 도구 정의를 매 단계 넣으므로 4k로는 동작하지 않는다. Ollama 문서는 에이전트·웹 검색 작업에 **64000 토큰 이상**을 권하고, Nanobrowser는 최소 16k를 요구한다. VRAM이 24GB라면 16k~32k에서 시작해 `ollama ps`로 GPU 적재를 확인하며 올린다.

설정 방법은 둘 중 하나를 쓴다.

- **Ollama 앱**: 설정(Settings)에서 컨텍스트 길이 슬라이더를 조정한다.
- **환경 변수**: `OLLAMA_CONTEXT_LENGTH=32000`

### 3.3 Windows에서 Ollama 환경 변수 설정

1. 작업 표시줄에서 Ollama를 종료한다.
2. 설정(Windows 11) 또는 제어판(Windows 10)에서 "환경 변수"를 검색해 **계정의 환경 변수 편집**을 연다.
3. 변수를 추가하고 확인을 누른다.
4. 시작 메뉴에서 Ollama를 다시 실행한다.

| 변수 | 값 | 용도 |
|---|---|---|
| `OLLAMA_CONTEXT_LENGTH` | `32000` (여유가 있으면 `64000`) | 컨텍스트 길이 |
| `OLLAMA_ORIGINS` | `chrome-extension://*` | 브라우저 확장(Nanobrowser)의 접근 허용. 4장 A 경로에서만 필요 |

## 4. 경로 A — Nanobrowser (코드 없이 시작)

Chrome 확장 프로그램이라 가장 빠르게 써 볼 수 있다.

1. [Chrome Web Store의 Nanobrowser](https://chromewebstore.google.com/detail/nanobrowser/imbddededgmcgfhfpcjmijokokekbkal)에서 **Chrome에 추가**를 누른다. Edge에서는 먼저 "다른 스토어의 확장 허용"을 켠다.
2. 3.3의 `OLLAMA_ORIGINS`를 설정하고 Ollama를 재시작한다. 이 설정이 없으면 확장의 요청이 거부된다.
3. 확장 설정에서 제공자로 **Ollama**를 추가한다. Ollama의 기본 주소는 `http://localhost:11434`이다. 연결이 거부되면 제공자 카드에 해결 방법이 표시된다.
4. 모델로 `qwen3.8:27b`를 선택한다.
5. 사이드 패널에서 간단한 작업부터 시도한다. 예: "example.com의 제목을 알려줘".

## 5. 경로 B — Browser Use (Python 코드로 제어)

자동화 스크립트를 직접 만들거나 실험을 반복할 때 적합하다. Python 3.11 이상이 필요하다.

### 5.1 설치 (PowerShell)

```powershell
# uv가 없다면: winget install astral-sh.uv
uv init --python 3.12 my-agent
cd my-agent
uv add "browser-use==1.0.8"
```

pip을 쓰는 경우:

```powershell
python -m venv .venv
.venv\Scripts\Activate.ps1
pip install "browser-use==1.0.8"
```

README는 버전을 `==`로 고정하라고 안내한다. `Browser('chromium')`은 PC에 설치된 Chrome/Chromium/Edge를 새 인스턴스로 띄운다.

### 5.2 코드

1.x에서는 `ChatOllama`가 제거되었다(가져오면 `ImportError`). Ollama의 OpenAI 호환 엔드포인트에 `ChatOpenAI`로 연결한다. 인터넷의 0.x용 예제는 그대로 동작하지 않으니 주의한다.

```python
# main.py
from browser_use import Agent, Browser, ChatOpenAI

llm = ChatOpenAI(
    'qwen3.8:27b',
    base_url='http://localhost:11434/v1',
    api_key='ollama',  # 임의의 값. Ollama는 키를 검사하지 않는다
)
agent = Agent(
    'https://example.com 의 제목은 무엇인가?',
    llm=llm,
    browser=Browser('chromium'),
)
print(agent.run_sync().final_result())
```

```powershell
uv run main.py
```

컨텍스트 길이는 코드가 아니라 Ollama 쪽 설정(3.2)을 따른다.

## 6. 보안 주의사항

로컬 모델은 페이지 내용을 외부로 보내지 않는다는 이점이 있지만, 에이전트가 읽은 웹 페이지의 숨은 지시를 따르는 프롬프트 인젝션은 막아 주지 않는다.

- **전용 프로필 사용**: 평소 쓰는 Chrome 프로필(은행·메일 로그인 상태)에서 돌리지 않는다. Nanobrowser는 별도 Chrome 프로필에 설치하고, Browser Use는 기본값인 새 브라우저 인스턴스를 그대로 쓴다.
- **민감한 작업은 사람이 확인**: 결제, 메일 발송, 비밀번호 입력은 에이전트에 맡기지 않는다.
- **Ollama 노출 범위 유지**: `OLLAMA_HOST`를 `0.0.0.0`으로 바꾸지 않는다. 기본값(localhost)이면 외부에서 접근할 수 없다.
- **`OLLAMA_ORIGINS`는 필요한 만큼만**: `*` 대신 `chrome-extension://*`처럼 좁게 준다.
- **비공식 모델 주의**: Ollama에는 제3자가 올린 변형 모델(예: "Uncensored")도 있다. 공식 라이브러리의 `qwen3.8` 태그를 쓴다.

## 7. 문제 해결

| 증상 | 원인과 조치 |
|---|---|
| 에이전트가 앞 단계를 잊거나 엉뚱한 행동을 반복 | 컨텍스트 부족. 3.2 설정을 확인한다. |
| Nanobrowser에서 연결 거부(403) | `OLLAMA_ORIGINS` 미설정 또는 Ollama 미재시작. |
| 응답이 매우 느림 | `ollama ps`에서 CPU 비중 확인. 컨텍스트를 줄이거나 thinking을 끈다. |
| `ImportError: ChatOllama` | Browser Use 0.x용 예제다. 5.2의 `ChatOpenAI` 방식을 쓴다. |
| 도구 호출 형식 오류 | 도구 호출을 지원하는 모델인지 확인한다. `qwen3.8:27b`는 지원한다. |

## 출처

- [Ollama 라이브러리: qwen3.8](https://ollama.com/library/qwen3.8) · [태그 목록](https://ollama.com/library/qwen3.8/tags)
- [Wikipedia: Qwen](https://en.wikipedia.org/wiki/Qwen)
- [Ollama 문서: Context length](https://docs.ollama.com/context-length) · [FAQ](https://docs.ollama.com/faq)
- [Browser Use 문서: Supported models](https://docs.browser-use.com/supported-models) · [GitHub](https://github.com/browser-use/browser-use)
- [Nanobrowser GitHub](https://github.com/nanobrowser/nanobrowser)
- [BrowserOS 문서: Local models](https://docs.browseros.com/features/local-models)
