# AI 에이전트 보안 기술의 상용화 현황

> 기준 시점: 2026년 11월 11일 웹 검색으로 확인.
> 출처 대부분이 업계 매체·블로그·벤더 자료 같은 2차 자료이며, 서로 날짜나 금액이 어긋나는 경우는
> 본문에 표시했다. 인용 전에는 각 벤더의 공식 발표를 확인해야 한다.
> 링크가 없는 제품명은 이번 검색에서 따로 재확인하지 못한 항목이다.

## 1. 이미 상용화된 분야

| 분야 | 무엇을 막는가 | 대표 제품·기업 |
|---|---|---|
| **AI 방화벽 / 가드레일** | 프롬프트 인젝션, 탈옥, 유해·민감 출력을 입출력 단계에서 필터링 | Lakera Guard(Check Point), Prisma AIRS(Palo Alto), Cisco AI Defense, F5 AI Guardrails(구 CalypsoAI), Azure Prompt Shields, AWS Bedrock Guardrails, Google Model Armor, 오픈소스 Llama Guard·NeMo Guardrails |
| **AI 레드팀 / 자동 취약점 점검** | 배포 전 에이전트·모델을 자동 공격해 취약점 탐지 | Promptfoo(OpenAI가 인수 발표), SPLX(Zscaler), Mindgard, HiddenLayer, 오픈소스 garak·PyRIT |
| **AI 보안 형상 관리(AI-SPM) / 섀도 AI 탐지** | 조직 내 모델·에이전트·연결 데이터의 자산 파악과 설정 오류 점검 | Wiz, Orca, Palo Alto, Zenity, Noma Security |
| **에이전트 실행 격리(샌드박스)** | 코드 실행·브라우저 조작 에이전트가 호스트와 네트워크에 미치는 영향 제한 | E2B·Vercel Sandbox(Firecracker microVM), Modal(gVisor), Daytona, Northflank, Cloudflare |
| **AI용 데이터 유출 방지(DLP)** | 프롬프트와 도구 호출을 통한 기밀·개인정보 유출 | Microsoft Purview, Nightfall, Harmonic, Prompt Security(SentinelOne) |
| **모델·AI 공급망 보안** | 악성 모델 파일, 오염된 의존성, AI-BOM 관리 | Protect AI Guardian(Palo Alto), HiddenLayer, Hugging Face 스캐닝 |
| **관측·감사 로깅** | 에이전트 행동 추적, 사후 분석, 규제 대응 증적 | Langfuse, LangSmith, Datadog LLM Observability |
| **에이전트 신원·권한 관리** | 에이전트에 별도 ID를 부여하고 최소 권한·단기 토큰으로 통제 | Microsoft Entra Agent ID, Okta for AI Agents, Auth0 for AI Agents, CyberArk Secure AI Agents, Astrix(Cisco가 인수 발표), Aembit |
| **에이전트 런타임 탐지·대응(AIDR)** | 도구 호출 순서와 행동 이상을 실시간 감시·차단 | CrowdStrike Falcon Guardian/AIDR, Zenity, Noma Security, Operant AI |

참고:

- **샌드박스 격리 수준**: 비교 자료들은 E2B가 샌드박스마다 전용 커널을 갖는 Firecracker microVM,
  Modal이 gVisor를 쓴다는 데 일치한다. Daytona는 자료마다 설명이 달라(기본 컨테이너 vs VM 스냅샷)
  도입 전 공식 문서 확인이 필요하다.
  ([Upstash 비교](https://upstash.com/blog/ai-agent-sandbox-providers-compared-2026),
  [LogRocket 비교](https://blog.logrocket.com/comparing-ai-agent-sandbox-platforms-e2b-modal-daytona-and-more/),
  [Northflank: E2B vs Modal](https://northflank.com/blog/e2b-vs-modal))
- **에이전트 신원 관리**는 2026년 상반기에 주요 제품이 정식 출시되면서 초기 단계를 벗어났다.
  - Microsoft Entra Agent ID는 2026년 봄 Agent 365와 함께 정식 출시된 것으로 보도되었다
    (출처에 따라 4월 30일 또는 5월 1일).
    ([Microsoft Learn: Agent ID 변경 내역](https://learn.microsoft.com/en-za/entra/agent-id/whats-new-agent-id))
  - Okta for AI Agents는 얼리 액세스 발표 당시 2026년 4월 30일 정식 출시를 예고했다.
    ([Okta 블로그](https://www.okta.com/blog/ai/okta-ai-agents-early-access-announcement/))
  - Auth0 for AI Agents는 2025년 10월 정식 출시라는 자료와 2026년에도 개발자 프리뷰라는 자료가 엇갈린다.
    ([GA 기록](https://guptadeepak.com/ciam-compass/changelog/auth0-for-ai-agents-ga/),
    [비교 글](https://www.decryptiondigest.com/blog/auth0-entra-agent-id-okta-xaa-ai-agent-authorization-comparison))
  - CyberArk Secure AI Agents는 2025년 11월 정식 출시로 정리되어 있다.
    ([CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-okta-ai-agent-iam-framework-enterprise-gap/))
- **AIDR**: CrowdStrike는 2026년 9월 1일 Fal.Con에서 에이전트 탐지·런타임 탐지·대응을 묶은
  Falcon Guardian을 발표했다. 프롬프트 공격 99% 탐지율은 자체 벤치마크 수치다.
  ([CrowdStrike 보도자료](https://www.crowdstrike.com/en-us/press-releases/crowdstrike-unveils-falcon-guardian-ai-agent-security),
  [CrowdStrike AIDR 블로그](https://www.crowdstrike.com/en-us/blog/aidr-how-crowdstrike-is-defining-next-era-of-cybersecurity/),
  [SiliconANGLE](https://siliconangle.com/2026/09/01/ai-detection-response-emerges-security-category-falcon/))
- **Zenity**는 2026년 8월 1억 2,500만 달러 시리즈 C를 유치했다.
  ([Fortune](https://fortune.com/2026/08/03/softbank-hitachi-lg-back-zenitys-125-million-round-to-police-ai-agents/),
  [SecurityWeek](https://www.securityweek.com/zenity-raises-125-million-in-series-c-funding/amp/))
  **Noma Security**는 1억 달러 시리즈 B로 누적 1억 3,200만 달러를 조달했다.
  ([SecurityWeek](https://www.securityweek.com/noma-security-raises-100-million-for-ai-security-platform/amp/))

## 2. 초기 상용화 단계

제품은 나왔지만 표준과 시장이 아직 형성 중인 분야다.

- **MCP·도구 보안**: MCP 게이트웨이, 서버 스캐닝, 도구 오염(tool poisoning) 탐지.
  - Docker MCP Gateway는 오픈소스이며 서버마다 별도 컨테이너에서 실행한다.
  - Cloudflare MCP server portals는 Zero Trust 기반 관리형 서비스로, 한 자료는 2026년 9월 24일
    정식 출시로 적고 있다(그 이전 자료는 오픈 베타로 기술).
  - Kong은 AI Gateway 안에서 엔터프라이즈 MCP 게이트웨이를 유료로 제공한다.
  - Snyk은 Invariant Labs를 인수해 MCP-Scan을 Snyk Agent Scan으로 이어가고 있다(게이트웨이가 아닌 스캐너).
  - 출처: [Maxim 게이트웨이 비교](https://www.getmaxim.ai/articles/mcp-gateway-comparison-2026-bifrost-docker-microsoft-kong-cloudflare/),
    [Aembit: MCP 보안 플랫폼 9선](https://aembit.io/blog/top-9-mcp-security-platforms-and-gateways-in-2026/),
    [Speakeasy 비교](https://www.speakeasy.com/blog/best-mcp-gateways-for-enterprise-2026)
- **사람 승인(Human-in-the-loop) 정책 엔진**: 결제·삭제·외부 전송 같은 고위험 행동에만
  승인을 요구하는 정책 계층으로, 에이전트 프레임워크와 신원 관리·가드레일 제품에 기능으로 들어가고 있다.
- **에이전트 간 신뢰·인증**
  - A2A 프로토콜은 Google이 2025년 4월 발표해 6월 Linux Foundation에 기증했고, 2026년 v1.0에 도달한
    것으로 정리되어 있다. Agent Card에 OAuth 2.0·OIDC·API 키·mTLS 방식을 선언하며 카드 서명(JWS)을 지원한다.
    ([Wiz: A2A 프로토콜](https://www.wiz.io/academy/ai-security/a2a-protocol),
    [Tyk: A2A 보안 가이드](https://tyk.io/learning-center/a2a-security-the-developers-complete-guide/))
  - Web Bot Auth는 HTTP Message Signatures(RFC 9421) 기반으로 에이전트가 요청에 서명해 신원을 증명하는
    방식이다. Cloudflare가 "signed agents"로 제품화했지만 IETF 규격은 아직 초안이다.
    ([Cloudflare 문서: Web Bot Auth](https://developers.cloudflare.com/bots/reference/bot-verification/web-bot-auth/),
    [Cloudflare 문서: Signed agents](https://developers.cloudflare.com/bots/concepts/bot/signed-agents),
    [Cloudflare 블로그](https://blog.cloudflare.com/web-bot-auth))
- **에이전트 결제·커머스 보안**: 에이전트가 사용자 대신 결제할 때의 신원·위임 증명.
  - Visa Trusted Agent Protocol(2025년 10월 14일 발표)은 가맹점이 신뢰할 수 있는 에이전트와 악성 봇을
    구분하게 하며, Visa Intelligent Commerce의 일부로 주요 PSP에 적용 중인 것으로 보도되었다.
    ([Digital Transactions](https://www.digitaltransactions.net/visa-launches-trusted-agent-an-agentic-commerce-protocol/))
  - Mastercard Agent Pay(2025년 4월 29일 발표)는 토큰화 기반 Agentic Token을 발급하고, 결제 전
    에이전트 등록·검증을 요구한다.
  - Google AP2(2025년 9월 발표)는 서명된 "Mandate"로 사용자 승인을 기록하며 60곳 이상이 참여했다.
    Visa의 AP2 참여 여부는 출처마다 다르다.
  - 출처: [CIO: 에이전트 결제 대비](https://www.cio.com/article/4137893/agentic-payments-are-coming-is-your-company-ready.html),
    [Agent Pay vs Trusted Agent 비교](https://eco.com/support/en/articles/15192003-mastercard-agent-pay-vs-visa-trusted-agent-2026-compared)

## 3. 곧 상용화될 것으로 보이는 분야

- **구조적 프롬프트 인젝션 방어**: 필터링이 아니라 설계로 막는 방식이다. Google DeepMind의 CaMeL은
  사용자 요청에서 제어 흐름과 데이터 흐름을 분리하고 명시적 보안 정책을 강제하며, 모델 자체는 바꾸지 않는다.
  다만 이번 검색에서 2026년 기준 기업 도입 사례는 찾지 못했고, 보안 정책을 사용자가 정의해야 한다는 점과
  Dual-LLM 구조의 성능 비용이 한계로 지적된다.
  ([CaMeL 논문](https://arxiv.org/pdf/2503.18813),
  [InfoQ 해설](https://www.infoq.com/news/2025/04/deepmind-camel-promt-injection))
- **메모리·RAG 오염 방어**: 장기 메모리에 심어진 악성 지시를 탐지하고 출처를 추적한다.
  현재 가장 구체적인 구현은 오픈소스인 OWASP Agent Memory Guard로, 에이전트와 메모리 저장소 사이에서
  모든 읽기·쓰기를 검사하고 정상 상태로의 롤백을 지원한다. 상용 제품보다는 오픈소스와 연구가 앞서 있다.
  ([Help Net Security](https://helpnetsecurity.com/2026/06/01/owasp-agent-memory-guard/),
  [연구 논문](https://arxiv.org/abs/2601.05504v1))
- **기밀 컴퓨팅 기반 에이전트 실행**: TEE 안에서 추론과 도구 실행을 수행해 데이터와 가중치를 보호한다.
  Fortanix Confidential AI 같은 상용 제품과 Phala/NEAR AI의 오픈소스 스택이 있으나, 에이전트 전용으로는
  아직 연구 단계에 가깝다. TEE는 데이터를 보호할 뿐 프롬프트 인젝션을 막지는 못한다.
  ([Fortanix Confidential AI](https://www.fortanix.com/platform/confidential-ai))

## 4. 시장 흐름

### 인수 통합

Forrester는 주요 벤더 8곳이 약 18개월 동안 AI 보안 스타트업에 20억 달러 이상을 썼다고 집계했다.
금액은 발표·언론 추정치와 SEC 공시 수치가 달라 둘 다 적었다.

| 인수자 | 대상 | 시점 | 금액 |
|---|---|---|---|
| Cisco | Robust Intelligence | 2024년 | 이번 검색에서 재확인하지 못함 |
| Palo Alto Networks | Protect AI | 2025년 7월 완료 | 공시 6억 3,450만 달러 (추정 6억 5,000만) |
| Snyk | Invariant Labs | 2025년 6월 완료 | 비공개 |
| SentinelOne | Prompt Security | 2025년 8월 발표, 9월 완료 | 공시 1억 6,020만 달러 (추정 2억 5,000만) |
| F5 | CalypsoAI | 2025년 9월 | 공시 1억 4,520만 달러 (발표 1억 8,000만) |
| CrowdStrike | Pangea | 2025년 9월 | 공시 2억 2,270만 달러 (보도 2억 6,000만) |
| Check Point | Lakera | 2025년 9월 발표, 10월 완료 | 공시 2억 180만 달러 (보도 약 3억) |
| Zscaler | SPLX | 2025년 10월 완료 | 공시 4,060만 달러 |
| OpenAI | Promptfoo | 2026년 3월 9일 발표 | 비공개, 완료 여부 불명확 |
| Cisco | Astrix Security | 2026년 5월 4일 발표 | 비공개 (보도 2억 5,000만~4억 달러) |
| Palo Alto Networks | Portkey, Koi | 2026년 | 재확인하지 못함 |

출처:
[Forrester](https://www.forrester.com/blogs/securing-ais-ma-feeding-frenzy-is-on),
[인수 통합 추적 목록](https://guptadeepak.com/agent-security/deals/),
[Infosecurity Magazine](https://www.infosecurity-magazine.com/news-features/cybersecurity-ma-crowdstrike),
[Bloomberg: OpenAI·Promptfoo](https://www.bloomberg.com/news/articles/2026-03-09/openai-buying-ai-security-startup-promptfoo-to-safeguard-ai-agents),
[Security Boulevard: Cisco·Astrix](https://securityboulevard.com/2026/05/cisco-fortifies-ai-defenses-with-acquisition-of-astrix-security/)

### 기준 프레임워크

- **OWASP Top 10 for Agentic Applications 2026**: 2025년 12월 공개. 자율 에이전트 고유의 위험 10가지
  (ASI01~ASI10)를 정리했다.
  ([OWASP 문서](https://genai.owasp.org/resource/owasp-top-10-for-agentic-applications-for-2026/),
  [TechRepublic](https://www.techrepublic.com/article/news-owasp-ai-agent-risk-list/))
- **OWASP Top 10 for LLM Applications 2026**: 2026년 8~9월 공개. NIST, MITRE ATLAS, CWE, Agentic Top 10과의
  매핑을 확장했고, 같은 시기에 Agent Control Standard가 기증되었다.
  ([OWASP 문서](https://genai.owasp.org/resource/owasp-genai-llm-top-10-2026/),
  [CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-owasp-genai-top10-2026-agent-control-stand/))
- MITRE ATLAS, NIST AI RMF, ISO 42001도 제품 분류의 기준으로 쓰인다.

### 규제

- **EU AI Act**: Digital Omnibus로 고위험 의무 적용이 연기되었다. 2026년 5월 7일 3자 협의 합의 기준으로
  Annex III(독립형) 시스템은 2027년 12월 2일, Annex I(제품 내장형)은 2028년 8월 2일부터 적용된다.
  제50조 투명성 의무는 2026년 8월부터 그대로 적용된다. 최종 채택 절차 완료 여부는 확인하지 못했다.
  ([Cooley](https://cdp.cooley.com/digital-ai-omnibus-delays-key-deadlines-introduces-new-rules/),
  [OneTrust](https://www.onetrust.com/blog/eu-digital-omnibus-proposes-delay-of-ai-compliance-deadlines/),
  [Tech Policy Press](https://techpolicy.press/eus-ai-act-delays-let-highrisk-systems-dodge-oversight))
- **한국 AI 기본법**: 2026년 1월 22일 시행. 과태료(최대 3,000만 원)는 최소 1년 이상 계도기간을 두고
  운영하되, 의무 자체는 시행일부터 발생한다. 시행령 발효 시점은 자료마다 설명이 달라
  국가법령정보센터 확인이 필요하다.
  ([머니투데이](https://www.mt.co.kr/tech/2025/12/31/2025123009504430416),
  [뉴시스](https://mobile.newsis.com/view/NISX20260121_0003484769),
  [법무법인 세종 뉴스레터](https://shinkim.com/kor/media/newsletter/3114))
