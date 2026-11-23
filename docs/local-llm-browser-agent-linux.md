# Linux에서 로컬 LLM으로 브라우저 에이전트 시작하기

> 확인 기준일: 2026-11-23. 명령어는 각 프로젝트 공식 문서에서 확인한 것이며, 설치 전 [출처](#출처)를 다시 확인한다.
> Windows 절차는 [local-llm-browser-agent-windows.md](local-llm-browser-agent-windows.md)를 본다. 모델 선택 근거(Qwen3.8 27B)와 하드웨어 추정은 그 문서의 2장과 같다.

## 1. 요약

- **구성**: Ollama(systemd 서비스) + 브라우저 에이전트(Nanobrowser 또는 Browser Use).
- **모델**: `qwen3.8:27b` (18GB, tools·vision·thinking 지원). VRAM 24GB급 GPU를 권한다(추정).
- **Windows와 다른 점**: 환경 변수를 systemd override로 설정하고, GPU 드라이버를 직접 확인해야 하며, 디스플레이가 없는 서버에서도 Browser Use를 헤드리스로 돌릴 수 있다.
- **가장 흔한 실패 원인**: 컨텍스트 길이 기본값(VRAM 24GiB 미만에서 4k)이 너무 작다. 반드시 늘린다(3장).

## 2. Ollama 설치

### 2.1 GPU 드라이버 확인

| GPU | 요구 사항 |
|---|---|
| NVIDIA | compute capability 5.0 이상, 드라이버 550 이상(5.0~6.2 카드는 570 이상). `nvidia-smi`로 확인한다. |
| AMD Radeon | ROCm v7 드라이버. `amdgpu-install`로 설치한다. 일부 배포판은 `ollama` 사용자를 `render` 그룹에 추가해야 한다. |

### 2.2 설치와 모델 받기

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

스크립트를 파이프로 바로 실행하기 꺼려지면 먼저 내려받아 내용을 읽은 뒤 실행하거나, 공식 문서의 수동 설치 절차를 따른다.

```bash
sudo systemctl status ollama     # 서비스 동작 확인
ollama pull qwen3.8:27b          # 18GB
ollama run qwen3.8:27b "안녕"    # 동작 확인
ollama ps                        # PROCESSOR 열이 100% GPU인지 확인
```

| 작업 | 명령 |
|---|---|
| 로그 보기 | `journalctl -e -u ollama` |
| 업데이트 | 설치 스크립트를 다시 실행 |
| 모델 저장 위치 | `/usr/share/ollama/.ollama/models` |

## 3. 컨텍스트 길이와 환경 변수 설정 (필수)

Ollama의 기본 컨텍스트는 VRAM에 따라 4k(24GiB 미만) / 32k(24~48GiB) / 256k(48GiB 이상)로 정해진다. Ollama 문서는 에이전트 작업에 64000 토큰 이상을 권하고, Nanobrowser는 최소 16k를 요구한다. VRAM 24GB라면 16k~32k에서 시작해 `ollama ps`를 보며 올린다.

Linux에서는 Ollama가 systemd 서비스로 돌기 때문에 셸의 `export`가 적용되지 않는다. override 파일로 설정한다.

```bash
sudo systemctl edit ollama.service
```

편집기가 열리면 다음을 넣고 저장한다. 변수마다 `Environment` 줄을 하나씩 쓴다.

```ini
[Service]
Environment="OLLAMA_CONTEXT_LENGTH=32000"
Environment="OLLAMA_ORIGINS=chrome-extension://*"
```

```bash
sudo systemctl daemon-reload
sudo systemctl restart ollama
```

`OLLAMA_ORIGINS`는 Nanobrowser(4장)를 쓸 때만 필요하다. Browser Use만 쓴다면 빼도 된다.

## 4. 경로 A — Nanobrowser (데스크톱, 코드 없이 시작)

화면이 있는 데스크톱 Linux에서 쓴다.

1. **Chrome 또는 Edge**를 준비한다. Nanobrowser가 공식 지원하는 브라우저는 이 둘뿐이다. Chromium, Brave 등 다른 Chromium 계열은 "동작할 수 있으나 테스트되지 않음"으로 안내되어 있다.
2. [Chrome Web Store의 Nanobrowser](https://chromewebstore.google.com/detail/nanobrowser/imbddededgmcgfhfpcjmijokokekbkal)에서 **Chrome에 추가**를 누른다.
3. 3장의 `OLLAMA_ORIGINS`를 설정하고 Ollama를 재시작한다. 빠뜨리면 확장의 요청이 거부된다.
4. 확장 설정에서 제공자로 **Ollama**를 추가하고(기본 주소 `http://localhost:11434`) 모델로 `qwen3.8:27b`를 선택한다.
5. 사이드 패널에서 간단한 작업부터 시도한다.

## 5. 경로 B — Browser Use (Python, 서버에서도 가능)

### 5.1 설치

Python 3.11 이상이 필요하다.

```bash
# uv가 없다면: curl -LsSf https://astral.sh/uv/install.sh | sh
uv init --python 3.12 my-agent && cd my-agent
uv add 'browser-use==1.0.8'
```

pip을 쓰는 경우:

```bash
python3 -m venv .venv && source .venv/bin/activate
pip install 'browser-use==1.0.8'
```

README는 버전을 `==`로 고정하라고 안내한다(확인 시점에 README는 1.0.8, 퀵스타트 문서는 1.0.6을 예로 들고 있었다).

### 5.2 브라우저 준비

`Browser('chromium')`은 시스템에 설치된 Chrome/Chromium/Edge를 찾아 쓴다. 하나도 없으면 한 번만 다음을 실행해 Chromium을 내려받는다.

```bash
uv run browser-use install
```

### 5.3 코드

1.x에서는 `ChatOllama`가 제거되었다. Ollama의 OpenAI 호환 엔드포인트에 `ChatOpenAI`로 연결한다.

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

```bash
uv run main.py
```

### 5.4 디스플레이가 없는 서버

- README에 따르면 `Browser('chromium')`은 디스플레이가 없는 환경(Docker, CI)에서 자동으로 헤드리스로 동작한다. 명시하려면 `headless=` 매개변수를 쓴다. 그 밖에 `executable_path=`, `user_data_dir=`도 받는다.
- 저장소에 `docker` 폴더와 Dockerfile이 있다. 컨테이너에서 GPU로 Ollama를 돌리려면 `nvidia-container-toolkit`이 필요하다.
- root로 실행할 때의 샌드박스 플래그에 대해서는 공식 문서에 안내가 없다. 일반 사용자 계정으로 실행하는 편이 안전하다.
- GPU 서버와 에이전트를 다른 머신에 둘 때는 Ollama를 외부에 열지 말고 SSH 터널을 쓴다: `ssh -L 11434:localhost:11434 gpu-server`

## 6. 보안 주의사항

로컬 모델을 써도 프롬프트 인젝션 위험은 그대로다. Windows 문서 6장의 내용(전용 프로필, 민감한 작업은 사람이 확인, 공식 모델 태그 사용)에 더해 Linux에서는 다음을 챙긴다.

- **`OLLAMA_HOST=0.0.0.0` 금지**: Ollama FAQ의 예시에 이 값이 나오지만, 인증이 없는 API가 네트워크 전체에 열린다. 기본값(`127.0.0.1:11434`)을 유지하고 원격 접근은 SSH 터널로 한다.
- **공용 머신 주의**: Browser Use의 `Browser('chromium')`은 127.0.0.1에 인증 없는 디버깅 포트를 연다. 같은 머신의 다른 로컬 사용자가 그 브라우저에 붙을 수 있으므로, 다중 사용자 서버에서는 컨테이너 안에서 돌린다.
- **격리 실행**: 에이전트 전용 사용자 계정이나 컨테이너에서 돌리면 인젝션이 성공해도 피해 범위가 줄어든다.
- **`OLLAMA_ORIGINS`는 좁게**: `*` 대신 `chrome-extension://*`만 허용한다.

## 7. 문제 해결

| 증상 | 원인과 조치 |
|---|---|
| 절전에서 깨어난 뒤 갑자기 느려짐 | 재개 후 NVIDIA GPU를 못 찾아 CPU로 떨어진 것이다. `sudo rmmod nvidia_uvm && sudo modprobe nvidia_uvm` |
| 환경 변수를 줬는데 적용되지 않음 | 셸에서 `export`한 경우다. 3장의 systemd override로 설정하고 재시작한다. |
| 에이전트가 앞 단계를 잊거나 같은 행동 반복 | 컨텍스트 부족. `OLLAMA_CONTEXT_LENGTH`를 확인한다. |
| Nanobrowser 연결 거부(403) | `OLLAMA_ORIGINS` 미설정 또는 서비스 미재시작. |
| AMD GPU를 인식하지 못함 | ROCm v7 설치 여부와 `render` 그룹을 확인한다. SELinux 환경의 컨테이너는 `sudo setsebool container_use_devices=1` |
| 브라우저를 찾지 못함 | `uv run browser-use install`로 Chromium을 받거나 `executable_path=`로 경로를 지정한다. |
| `ImportError: ChatOllama` | Browser Use 0.x용 예제다. 5.3의 `ChatOpenAI` 방식을 쓴다. |

## 출처

- [Ollama 문서: Linux](https://docs.ollama.com/linux) · [GPU](https://docs.ollama.com/gpu) · [Context length](https://docs.ollama.com/context-length) · [FAQ](https://docs.ollama.com/faq)
- [Ollama 라이브러리: qwen3.8](https://ollama.com/library/qwen3.8)
- [Browser Use GitHub](https://github.com/browser-use/browser-use) · [Quickstart](https://docs.browser-use.com/quickstart) · [Supported models](https://docs.browser-use.com/supported-models)
- [Nanobrowser GitHub](https://github.com/nanobrowser/nanobrowser)
