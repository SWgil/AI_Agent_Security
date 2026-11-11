# AI 에이전트 보안 기술의 상용화 현황

> 기준 시점: 2026년 중반까지의 공개 정보. 웹 검색으로 최신 상태를 재확인한 자료가 아니며,
> 기업명과 인수 현황은 변동이 빠르므로 인용 전에 최신 여부를 확인해야 한다.

## 1. 이미 상용화된 분야

| 분야 | 무엇을 막는가 | 대표 제품·기업 |
|---|---|---|
| **AI 방화벽 / 가드레일** | 프롬프트 인젝션, 탈옥, 유해·민감 출력을 입출력 단계에서 필터링 | Lakera Guard(Check Point), Prisma AIRS(Palo Alto), Cisco AI Defense, Azure Prompt Shields, AWS Bedrock Guardrails, Google Model Armor, 오픈소스 Llama Guard·NeMo Guardrails |
| **AI 레드팀 / 자동 취약점 점검** | 배포 전 에이전트·모델을 자동 공격해 취약점 탐지 | Promptfoo, Mindgard, HiddenLayer, SPLX(Zscaler), 오픈소스 garak·PyRIT |
| **AI 보안 형상 관리(AI-SPM) / 섀도 AI 탐지** | 조직 내 모델·에이전트·연결 데이터의 자산 파악과 설정 오류 점검 | Wiz, Orca, Palo Alto, Zenity, Noma Security |
| **에이전트 실행 격리(샌드박스)** | 코드 실행·브라우저 조작 에이전트가 호스트와 네트워크에 미치는 영향 제한 | E2B, Modal, Daytona, Firecracker·gVisor 기반 microVM |
| **AI용 데이터 유출 방지(DLP)** | 프롬프트와 도구 호출을 통한 기밀·개인정보 유출 | Microsoft Purview, Nightfall, Harmonic, Prompt Security(SentinelOne) |
| **모델·AI 공급망 보안** | 악성 모델 파일, 오염된 의존성, AI-BOM 관리 | Protect AI Guardian, HiddenLayer, Hugging Face 스캐닝 |
| **관측·감사 로깅** | 에이전트 행동 추적, 사후 분석, 규제 대응 증적 | Langfuse, LangSmith, Datadog LLM Observability |

## 2. 초기 상용화 단계

제품은 나왔지만 표준과 시장이 아직 형성 중인 분야다.

- **에이전트 신원·권한 관리**: 에이전트에 별도 ID를 부여하고 최소 권한·단기 토큰으로 통제한다.
  Microsoft Entra Agent ID, Okta/Auth0 for AI Agents, CyberArk, Astrix, Aembit 등이 있다.
- **MCP·도구 보안**: MCP 게이트웨이, 서버 스캐닝, 도구 오염(tool poisoning) 탐지.
  Snyk(Invariant Labs의 mcp-scan), Docker MCP Gateway, Cloudflare, Kong 등이 제품을 내놓았다.
- **에이전트 런타임 탐지·대응**: 도구 호출 순서와 행동 이상을 실시간 감시하고 차단하며,
  EDR의 에이전트 버전에 해당한다. Zenity, Noma, Operant AI와 CrowdStrike·SentinelOne 같은
  기존 EDR 벤더가 진입했다.
- **사람 승인(Human-in-the-loop) 정책 엔진**: 결제·삭제·외부 전송 같은 고위험 행동에만
  승인을 요구하는 정책 계층으로, 에이전트 프레임워크와 신원 관리 제품에 기능으로 들어가고 있다.

## 3. 곧 상용화될 것으로 보이는 분야

- **구조적 프롬프트 인젝션 방어**: 필터링이 아니라 설계로 막는 방식이다. 신뢰할 수 없는
  데이터가 제어 흐름에 영향을 주지 못하게 하는 정보 흐름 제어, Dual-LLM,
  Google DeepMind의 CaMeL 계열이 연구에서 제품으로 넘어오는 중이다.
- **에이전트 간 신뢰·인증**: A2A 프로토콜, 웹에서 에이전트를 식별하는 Web Bot Auth,
  서명된 에이전트 카드 등 멀티 에이전트 환경의 상호 인증.
- **에이전트 결제·커머스 보안**: 에이전트가 사용자 대신 결제할 때의 위임 증명.
  Visa Trusted Agent Protocol, Mastercard Agent Pay, Google AP2가 발표되어 시범 적용 중이다.
- **메모리·RAG 오염 방어**: 장기 메모리와 지식베이스에 심어진 악성 지시를 탐지하고 출처를 추적한다.
- **기밀 컴퓨팅 기반 에이전트 실행**: TEE 안에서 추론과 도구 실행을 수행해 데이터와 가중치를 보호한다.

## 4. 시장 흐름

- **대형 보안 벤더의 인수 통합**: Protect AI→Palo Alto, Robust Intelligence→Cisco,
  Lakera→Check Point, CalypsoAI→F5, Pangea→CrowdStrike처럼 독립 스타트업이 플랫폼에 흡수되고 있다.
- **기준 프레임워크**: OWASP LLM Top 10과 Agentic Top 10, MITRE ATLAS, NIST AI RMF,
  ISO 42001이 제품 분류의 기준으로 쓰인다.
- **규제**: EU AI Act와 한국 AI 기본법(2026년 1월 시행)이 거버넌스·감사 제품 수요를 끌어올리고 있다.
