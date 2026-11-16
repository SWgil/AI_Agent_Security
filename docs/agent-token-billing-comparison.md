# AI 에이전트 토큰 사용량 과금 방식 비교

> 기준 시점: 2026년 11월 16일 웹 검색·공식 페이지 열람으로 확인. 가격은 모두 미국 달러(USD) 정가다.
> 가격과 요금제 이름은 몇 달 단위로 바뀐다. 계약·예산 산정에는 각 행에 달린 공식 페이지를 다시 확인해야 한다.
>
> 표기: 출처는 각 항목 옆에 링크로 달았다. 공식 페이지를 열지 못해 제3자 자료에 의존한 수치는 **(2차 출처)**,
> 어떤 자료로도 확정하지 못한 항목은 **(미확인)** 으로 표시했다. 미확인 항목은 [7절](#7-확인하지-못한-항목)에 모았다.
>
> 단위: MTok = 토큰 100만 개. "입력/출력"은 입력 토큰 단가/출력 토큰 단가를 뜻한다.

## 1. 요약

- 에이전트 제품의 과금은 **토큰을 그대로 청구하는 방식**과 **토큰을 다른 단위(크레딧·한도·작업 수·액션)로 감싸는 방식**으로 나뉜다.
  2026년 들어 코딩 에이전트는 전자로 수렴하고 있다. GitHub Copilot은 6월 1일에 "프리미엄 요청"을 폐지하고
  토큰 기반 크레딧으로 전환했고, Cursor·Devin도 외부 모델 사용분을 API 정가로 청구한다.
- 구독형 제품(Claude Code, Codex, Cursor, Devin)은 **포함 사용량의 크기를 토큰이나 금액으로 공개하지 않는 경우가 많다.**
  "Pro의 5배" 같은 배수나 "5시간당 메시지 수 범위"로만 알려 주고, 한도를 넘으면 API 단가로 종량 과금한다.
- 엔터프라이즈 에이전트(Copilot Studio, Agentforce)는 **액션·대화 건수 단위**가 기본이고, 추론 모델을 쓸 때만
  토큰이 과금 단위로 드러난다.
- 에이전트는 토큰 외에도 **도구 호출(웹 검색 1,000회당 $10~14), 샌드박스·런타임 시간, 메모리 저장**이 별도 청구된다.
  토큰 단가만 비교하면 실제 비용을 낮게 잡게 된다.
- 입력·출력·캐시 토큰의 단가 차이가 크다. 출력은 입력의 약 5배이고, 캐시 읽기는 입력의 2.5~10% 수준이다.
  에이전트는 같은 맥락을 반복 전송하므로 캐시 적중 여부가 비용을 크게 좌우한다.

## 2. 과금 방식의 유형

| 유형 | 과금 단위 | 토큰과의 관계 | 해당 제품 |
|---|---|---|---|
| **A. 토큰 종량제** | 입력·출력·캐시 토큰 | 토큰 수 × 모델 단가 그대로 | Claude API, OpenAI API, Gemini API, Bolt, Aider·Cline 등 BYOK 에이전트, OpenRouter |
| **B. 금액 환산 크레딧** | 달러에 고정된 크레딧 | 토큰 비용을 크레딧으로 환산 (환산율 공개) | GitHub Copilot, OpenAI Codex 크레딧, JetBrains AI, v0, Replit, OpenCode |
| **C. 구독 한도 + 초과분 종량** | 시간 창(5시간·주간)별 한도 | 한도 크기 비공개, 초과분은 API 단가 | Claude Code(구독), Codex(ChatGPT 구독), Cursor, Devin/Windsurf, Antigravity |
| **D. 추상 크레딧** | 제품 고유 크레딧 | 토큰 환산율 비공개, 모델별 배수만 공개 | Kiro, Manus, Genspark, Perplexity, Lovable |
| **E. 요청·작업 건수** | 요청 수, 작업 수 | 토큰과 무관 | Jules, Gemini CLI(일일 요청 수), Amazon Q Developer |
| **F. 액션·대화 건수** | 액션, 대화, 메시지 | 대체로 무관. 추론 모델만 토큰 가산 | Copilot Studio, Salesforce Agentforce, ServiceNow |
| **G. 토큰 + 인프라 사용량** | 토큰 + vCPU·메모리·세션 시간 | 모델 토큰은 별도 청구 | Claude Managed Agents, Bedrock AgentCore, Google Agent Platform, Foundry Agent Service |

BYOK(Bring Your Own Key)는 사용자가 모델 제공사의 API 키를 직접 넣어 쓰는 방식이다.

## 3. 종합 비교표

### 3.1 모델 제공사의 에이전트

| 제품 | 과금 단위 | 요금제와 포함 사용량 | 한도 초과 시 | 출처 |
|---|---|---|---|---|
| **Claude Code** (구독) | 5시간·주간 사용 한도. 채팅·Claude Code·Cowork가 한도를 공유 | Pro $20/월(기준 1배), Max 5x $100/월, Ultra $200/월(주간 10배, 5시간 한도 없음), Team 표준 좌석 $25·프리미엄 $125. 한도의 토큰 수는 비공개 | 진행 중인 단계를 마친 뒤 중단. "사용 크레딧"을 켜면 API 표준 단가로 계속 사용. 월 지출 상한 설정 가능 | [요금제](https://claude.com/pricing), [Pro·Max 사용 안내](https://support.claude.com/en/articles/11145838-using-claude-code-with-your-pro-or-max-plan), [사용 크레딧](https://support.claude.com/en/articles/12429409-extra-usage-for-paid-claude-plans) |
| **Claude Code** (API 키·Enterprise 사용량제) | 토큰 | 포함량 없음. Enterprise는 좌석 $20/월(연간, 20석 이상) + 사용량을 API 단가로 청구 | 조직·그룹·구성원별 지출 한도 | [비용 문서](https://code.claude.com/docs/en/costs), [Team·Enterprise 안내](https://support.claude.com/en/articles/11845131-use-claude-code-with-your-team-or-enterprise-plan) |
| **Claude Managed Agents** | 토큰 + 세션 시간 | 모델 토큰 단가 + 실행 중인 세션 1시간당 $0.08. 대기 시간은 무료 | 종량제 | [API 가격](https://platform.claude.com/docs/en/about-claude/pricing) |
| **OpenAI Codex** (ChatGPT 구독) | 5시간당 메시지 수(주간 한도 병행). 초과분은 토큰 기반 크레딧 | Plus $20/월, Pro $100·$200·$500/월, Business $25/인/월. 예: Plus에서 GPT-6.1 Sol은 5시간당 15~160개 메시지 | 현재 턴을 마친 뒤 중단. 크레딧 구매 또는 API 키로 전환 | [Codex 가격](https://learn.chatgpt.com/docs/pricing) |
| **OpenAI Codex** (API 키) | 토큰 | 포함량 없음. API 단가 적용. 클라우드 기능(GitHub 코드 리뷰, Slack) 제외 | 종량제 | [Codex 가격](https://learn.chatgpt.com/docs/pricing), [API 가격](https://developers.openai.com/api/docs/pricing) |
| **Gemini CLI** | 일일 모델 요청 수 | Code Assist Standard 1,500회/일, Enterprise 2,000회/일, 무료 API 키 250회/일. 개인 무료·AI Pro/Ultra 사용자는 2026-06-18에 Antigravity CLI로 이전 | 유료 Gemini API 키 또는 Vertex AI로 전환해 토큰 종량 과금 | [할당량·가격](https://geminicli.com/docs/resources/quota-and-pricing/), [README](https://github.com/google-gemini/gemini-cli/blob/main/README.md) |
| **Google Antigravity** | 주간 한도(유료는 5시간 창) + AI 크레딧 | 개인 무료, Google AI Pro $19.99/월, Ultra $99.99/월, 기업 $30/좌석/월부터. 한도 수치 비공개 | "AI 크레딧 초과 사용" 설정을 켜면 구매한 크레딧에서 차감 | [가격](https://antigravity.google/pricing), [요금제 문서](https://antigravity.google/docs/plans), [Google AI 요금제](https://one.google.com/about/google-ai-plans/) |
| **Jules** | 작업 수(24시간 단위) | 무료 15건/일(동시 3건), Pro 100건/일, Ultra 300건/일. 토큰 계량 없음 | 창이 초기화될 때까지 새 작업 불가. 유료 초과분 없음 | [사용 한도](https://jules.google/docs/usage-limits/) |

### 3.2 코딩 에이전트 (IDE·CLI)

| 제품 | 과금 단위 | 요금제와 포함 사용량 | 한도 초과 시 | 출처 |
|---|---|---|---|---|
| **GitHub Copilot** | GitHub AI 크레딧 (1크레딧 = $0.01). 입력·출력·캐시 토큰을 모델 단가로 계산해 환산 | Pro $10/월(1,500크레딧), Pro+ $39/월(7,000), Max $100/월(20,000), Business $19/좌석(1,900, 조직 합산), Enterprise $39/좌석(3,900). 인라인 자동완성은 크레딧 차감 없음 | 크레딧당 $0.01로 추가 사용. 달러 예산 설정 가능. 조직이 유료 초과를 끄면 다음 주기까지 차단 | [사용량 기반 과금](https://docs.github.com/en/copilot/concepts/billing/organizations-and-enterprises/usage-based-billing), [요금제](https://docs.github.com/en/copilot/get-started/plans), [모델별 단가](https://docs.github.com/en/copilot/reference/copilot-billing/models-and-pricing), [전환 공지](https://github.blog/changelog/2026/04/27/github-copilot-is-moving-to-usage-based-billing/) |
| **Cursor** | 자체 모델용 주간 사용 풀 + 외부 모델용 선불 달러 크레딧(토큰 과금) | Plus $8/월(기준), Premium $30/월(4배), Super $100/월(15배), Ultra $200/월(50배). 풀 크기는 배수로만 공개. 외부 모델(Claude, GPT)은 풀에 포함되지 않고 첫 요청부터 API 정가 | 주간 초기화 대기, 선불 크레딧 사용, 상위 요금제 전환 중 선택 | [가격 문서](https://cursor.com/docs/account/pricing), [가격](https://cursor.com/pricing), [사용 한도](https://cursor.com/help/models-and-usage/usage-limits) |
| **Devin / Windsurf** (Cognition) | 일간·주간 할당량 + 달러 표시 종량 크레딧. Enterprise만 ACU | Pro $20/월, Max $200/월, Teams 좌석 $40/월(최소 $80). 할당량 크기 비공개. Windsurf는 Devin Desktop으로 통합 | 선불 크레딧에서 "각 실행의 기반 모델 비용"만큼 달러로 차감 | [가격](https://devin.ai/pricing), [셀프서비스 과금](https://docs.devin.ai/admin/billing/self-serve), [요금제 개편 공지](https://cognition.com/blog/new-self-serve-plans-for-devin), [Windsurf 요금 변경](https://devin.ai/blog/windsurf-pricing-plans) |
| **Kiro** (AWS) | 크레딧(0.01 단위 계량) × 모델 배수. 토큰 환산율 비공개 | Free 50크레딧, Pro $20/월(1,000), Pro+ $40(2,000), Pro Max $100(5,000), Power $200(10,000). 배수 예: Auto 1.0배, Claude Opus 5.5 2.0배, Haiku 5.5 0.06배 | 추가 크레딧 1개당 $0.04(선불 팩). 소진 시 사용 중단 | [가격](https://kiro.dev/pricing/), [모델 배수](https://kiro.dev/docs/models/), [추가 크레딧](https://kiro.dev/docs/billing/add-on-credits/) |
| **Amazon Q Developer** | 에이전트 요청 수 | Free 50회/월, Pro $19/인/월. 2027-04-30 지원 종료 예정, 후속 제품은 Kiro | 요금제 한도 내 제공 | [가격](https://aws.amazon.com/q/developer/pricing/), [지원 종료 공지](https://aws.amazon.com/blogs/devops/amazon-q-developer-end-of-support-announcement/) |
| **Replit Agent** | 노력 기반(effort-based) 과금. 요청 복잡도에 따라 체크포인트 단위로 청구 | Core $20/월(크레딧 $20 포함), Pro $100/월(크레딧 $100 포함). 외부 모델 호출은 제공사 API 단가로 크레딧에서 차감 | 누적 비용이 월 크레딧을 넘으면 청구. 예산 상한 설정 가능 | [AI 과금](https://docs.replit.com/billing/ai-billing), [가격](https://replit.com/pricing), [사용량 과금](https://docs.replit.com/billing/about-usage-based-billing) |
| **JetBrains AI / Junie** | AI 크레딧 (1크레딧 = $1). 소비량은 토큰 기반 | AI Free 30일당 3크레딧, AI Pro $10(10크레딧) 또는 $20(20크레딧), AI Ultimate $30(35) 또는 $60(70). 두 가격 세트의 구분은 **(미확인)** | 충전 크레딧(12개월 유효) | [라이선스 문서](https://www.jetbrains.com/help/ai-assistant/licensing-and-subscriptions.html) |
| **Cline** | 토큰 (BYOK 또는 Cline 제공사 크레딧) | 확장 프로그램 무료. Cline 제공사는 모델 제공사 가격 그대로 청구. ClinePass $9.99/월은 오픈 가중치 모델 정액제 | 종량제 | [가격](https://cline.bot/pricing), [제공사 공지](https://cline.bot/blog/cline-provider-now-runs-on-vercel), [ClinePass](https://docs.cline.bot/getting-started/clinepass) |
| **Aider** | 토큰 (BYOK) | 오픈소스(Apache-2.0). 자체 요금제 없음. 제공사에 직접 지불 | 종량제 | [저장소](https://github.com/Aider-AI/aider) |
| **OpenCode** | 토큰 (BYOK 또는 자체 게이트웨이 선불 크레딧) | 게이트웨이는 모델을 원가에 제공하고 크레딧 구매 시 4.6% + $0.31 수수료. Go 구독 $10/월은 모델별 월 달러 한도 | Go 한도 초과 시 종량 잔액으로 전환(설정 시) | [게이트웨이 문서](https://opencode.ai/docs/zen/), [Go 구독](https://opencode.ai/docs/go/) |
| **Roo Code** | — | 2026-05-15 서비스 종료, 저장소 보관 처리 | — | [저장소](https://github.com/RooCodeInc/Roo-Code) |

ACU(Agent Compute Unit)는 Devin이 토큰·컴퓨팅·VM 사용을 합쳐 계량하는 단위다. 현재는 Enterprise 계약에만 쓰이고
공개 단가는 없다. ([Devin 사용량 문서](https://docs.devin.ai/desktop/accounts/usage))

### 3.3 범용·엔터프라이즈 에이전트

| 제품 | 과금 단위 | 요금과 환산 | 한도 초과 시 | 출처 |
|---|---|---|---|---|
| **Microsoft Copilot Studio** | Copilot 크레딧 | 선불 25,000크레딧 $200/월, 종량제 크레딧당 $0.01. 생성형 답변 2크레딧, 에이전트 액션 5크레딧, 테넌트 그래프 그라운딩 10크레딧. 추론 모델은 과금 토큰 1,000개당 10크레딧 가산 | 선불 용량의 125%에서 에이전트 차단. 종량제를 연결하면 초과분 청구 | [가격](https://www.microsoft.com/en-us/microsoft-365-copilot/pricing/copilot-studio), [과금 요율](https://learn.microsoft.com/en-us/microsoft-copilot-studio/requirements-messages-management), [라이선스](https://learn.microsoft.com/en-us/microsoft-copilot-studio/billing-licensing) |
| **Salesforce Agentforce** | Flex 크레딧(액션당) 또는 대화 건수 | 100,000크레딧 $500. 액션 1건 20크레딧($0.10), 음성 액션 30크레딧. 고객 응대는 대화당 $2 선택 가능. 사용자 라이선스형은 $125/인/월부터 | 계약 단가로 월별 후불 청구 | [가격](https://www.salesforce.com/agentforce/pricing/), [요율표 PDF](https://www.salesforce.com/en-us/wp-content/uploads/sites/4/documents/white-papers/Rate-Card-Flex-Credits.pdf) |
| **ServiceNow Now Assist** | 어시스트(assist) | 스킬 실행 시 소비, 테넌트 단위 풀. 공개 단가 없음 **(2차 출처)** | 비공개 | [커뮤니티 글](https://www.servicenow.com/community/upgrades-and-patching-forum/servicenow-ai-native-licensing-in-2026-a-practical-guide-to/m-p/3565858) |
| **Manus** | 크레딧 | Pro $20/월에 4,000크레딧. 크레딧은 LLM 토큰, 가상 머신, 외부 API 호출을 합쳐 차감. 토큰 환산율 비공개 | 추가 크레딧 구매 | [크레딧 안내](https://help.manus.im/en/articles/11711097-how-do-credits-work) |
| **Perplexity (Comet)** | 크레딧 (100크레딧 = $1) | Max $200/월에 10,000크레딧. Computer 작업과 Comet 어시스턴트의 Work 모드에서 차감. 일반 검색은 무료 | 이월 없음 | [크레딧 안내](https://www.perplexity.ai/help-center/en/articles/13838041-how-credits-work-on-perplexity), [Max 요금제](https://www.perplexity.ai/help-center/en/articles/11680686-perplexity-max) |
| **Genspark** | 크레딧 | Plus $24.99/월(10,000크레딧부터), Pro $249.99/월(125,000크레딧부터) | 비공개 | [멤버십 안내](https://www.genspark.ai/helpcenter/membership-plans) |
| **Lovable** | 크레딧 | Plan 모드 메시지당 1크레딧, Build 모드는 사용량 기반(예시 0.5~2크레딧). 크레딧 가치는 Pro $0.25, Business $0.50 | 충전 크레딧 | [가격](https://lovable.dev/pricing), [요금제·크레딧 문서](https://docs.lovable.dev/introduction/plans-and-credits) |
| **Bolt** | 토큰 | Free 월 100만 토큰, Pro $25/월(1,000만 토큰부터). 미사용 토큰은 2개월까지 이월 | 토큰 추가 구매 | [가격](https://bolt.new/pricing) |
| **v0** (Vercel) | 달러 표시 크레딧을 토큰 단가로 차감 | Plus $30/인/월(크레딧 $30 포함). v0 Pro 모델 $2/$10, v0 Max $5/$25 (MTok당 입력/출력) | 크레딧 추가 구매 | [가격](https://v0.app/pricing) |
| **OpenRouter** | 토큰 (제공사 정가 그대로) | 요청별 수수료 없음. 크레딧 구매 시 5.5%(Standard) 수수료. BYOK는 월 $25,000까지 수수료 면제, 이후 5% | 종량제 | [가격](https://openrouter.ai/pricing) |

### 3.4 에이전트 실행 플랫폼

이 범주는 모델 토큰 비용을 별도로 내고, 에이전트를 돌리는 인프라 사용량을 추가로 낸다.

| 플랫폼 | 토큰 외 과금 항목 | 출처 |
|---|---|---|
| **Claude Managed Agents** | 세션 실행 시간 $0.08/시간(밀리초 단위 계량, 대기 중 무료) | [API 가격](https://platform.claude.com/docs/en/about-claude/pricing) |
| **Amazon Bedrock AgentCore** | 런타임 $0.0895/vCPU-시간, $0.00945/GB-시간. 게이트웨이 호출 1,000회당 $0.005. 장기 메모리 레코드 1,000개당 월 $0.75. 오케스트레이션 자체는 무료 | [AgentCore 가격](https://aws.amazon.com/bedrock/agentcore/pricing/), [Agents Classic 안내](https://docs.aws.amazon.com/bedrock/latest/userguide/agents-classic-maintenance-mode.html) |
| **Google Gemini Enterprise Agent Platform** (구 Vertex AI Agent Engine) | 컴퓨팅 $0.085/vCPU-시간, 메모리 $0.009/GiB-시간, 스토리지 $0.30/GiB-월 | [SKU 통합 안내 PDF](https://services.google.com/fh/files/emails/b_502770571_gemini_enterprise_agent_platform_pricing.pdf) |
| **Microsoft Foundry Agent Service** | 에이전트 생성·실행은 추가 요금 없음. 파일 검색 저장소, 코드 인터프리터 세션, 웹 검색, 호스팅 에이전트의 vCPU·메모리는 별도. 단가는 **(미확인)** | [가격](https://azure.microsoft.com/en-us/pricing/details/foundry-agent-service/), [개요](https://learn.microsoft.com/en-us/azure/foundry/agents/overview) |
| **OpenAI Agents SDK / AgentKit** | SDK·Agent Builder 자체 요금 없음. 내장 도구 요금만 추가 ([5절](#5-토큰-외-추가-과금) 참고) | [API 가격](https://developers.openai.com/api/docs/pricing) |

## 4. 기반 모델의 토큰 단가

위 제품 대부분이 한도 초과분이나 종량 사용분을 아래 단가로 청구한다. 단위는 MTok당 달러다.

### Anthropic Claude API

| 모델 | 입력 | 출력 | 캐시 쓰기(5분) | 캐시 읽기 |
|---|---|---|---|---|
| Fable 5.2 | $8 | $40 | $10 | $0.20 |
| Opus 5.5 | $4 | $20 | $5 | $0.20 |
| Sonnet 5.5 | $2 | $10 | $2.50 | $0.10 |
| Haiku 5.5 (프롬프트 10만 토큰 이하) | $0.10 | $0.50 | $0.125 | $0.01 |

- 캐시 쓰기는 입력 단가의 1.25배(5분) 또는 2배(1시간)다. 캐시 읽기는 모델에 따라 입력 단가의 2.5~10%다.
- 배치 API는 입력·출력 50% 할인이고 캐싱과 중복 적용된다.
- 100만 토큰 컨텍스트에 추가 요금이 없다. Haiku 5.5만 10만 토큰 초과 시 단가가 오른다.
- 사고(thinking) 토큰은 출력 토큰으로 청구된다. ([Claude Code 비용 문서](https://code.claude.com/docs/en/costs))

출처: [Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing)

### OpenAI API

| 모델 | 입력 | 캐시된 입력 | 출력 | 27.2만 토큰 초과 시 (입력/출력) |
|---|---|---|---|---|
| gpt-6-astra | $10 | $1.00 | $50 | $20 / $75 |
| gpt-6.1-sol | $2 | $0.10 | $10 | $4 / $15 |
| gpt-6.1-terra | $1 | $0.05 | $5 | $2 / $7.50 |
| gpt-6-luna | $0.10 | $0.01 | $0.50 | $0.20 / $0.75 |

- 캐시 쓰기 요금이 따로 없다.
- Batch·Flex는 표준의 50%, Fast 등급은 2배다.
- Codex 크레딧은 같은 구조를 크레딧으로 옮긴 것이다. 예: GPT-6.1 Sol은 MTok당 입력 50, 캐시 2.5, 출력 250크레딧.
  ([Codex 가격](https://learn.chatgpt.com/docs/pricing)) 크레딧 1개의 달러 가격은 공식 페이지에서 확인하지 못했다.
  $0.04라는 추정이 있고 API 단가와 계산이 맞는다 **(2차 출처: [morphllm](https://www.morphllm.com/codex-pricing))**.

출처: [OpenAI API 가격](https://developers.openai.com/api/docs/pricing)

### Google Gemini API

| 모델 | 입력 | 출력(사고 포함) | 컨텍스트 캐시 | 예정된 변경 |
|---|---|---|---|---|
| Gemini 4 Carbon | $2.00 | $10.00 | $0.10 | 2026-12-01부터 $4 / $20 / $0.20 |
| Gemini 4 Hydrogen | $1.50 | $7.50 | $0.075 | — |
| Gemini 3.8 Flash | $0.75 | $3.75 | $0.075 | 2027-01-01부터 $1.50 / $7.50 / $0.15 |
| Gemini 3.5 Flash-Lite | $0.30 | $2.50 | $0.03 | — |

- Batch·Flex는 표준의 50%, Priority는 1.8배다.
- Gemini 4 계열에는 20만 토큰 초과 구간 할증이 없다. Gemini 3.1 Pro에는 있다($2→$4 입력).
- Carbon 모델명은 가격 페이지 열람 결과가 "Carbon"과 "Argon"으로 엇갈렸다. GitHub Copilot의 모델 단가표가
  같은 가격과 같은 인상 일정으로 "Gemini 4 Carbon"을 싣고 있어 이 이름을 썼다.
  ([Copilot 모델 단가](https://docs.github.com/en/copilot/reference/copilot-billing/models-and-pricing))

출처: [Gemini API 가격](https://aistudio.google.com/docs/pricing)

## 5. 토큰 외 추가 과금

| 항목 | Anthropic | OpenAI | Google |
|---|---|---|---|
| 웹 검색 | 1,000회당 $10 + 토큰 | 1,000회당 $10 + 검색 내용 토큰 | 월 5,000회 무료 후 1,000회당 $14 |
| 코드 실행 | 조직당 월 1,550시간 무료 후 컨테이너 시간당 $0.05 (웹 검색·웹 페치와 함께 쓰면 무료) | 컨테이너 20분 세션당 $0.03(1GB)~$1.92(64GB) | 도구 요금 없음, 토큰만 청구 |
| 파일 검색 | — | 1,000회당 $2.50 + 저장소 $0.10/GB/일 | 도구 요금 없음, 임베딩 $0.15/MTok |
| 컴퓨터 사용 | 토큰만 청구(도구 정의 약 4,500토큰 + 스크린샷) | 별도 가격 표기 없음 | 토큰만 청구 |

출처: [Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing),
[OpenAI API 가격](https://developers.openai.com/api/docs/pricing),
[Gemini API 가격](https://aistudio.google.com/docs/pricing)

그 밖에 제품별로 붙는 추가 요금은 다음과 같다.

- **GitHub Copilot 코드 리뷰·클라우드 에이전트**: AI 크레딧과 별개로 GitHub Actions 분을 소비한다.
  리뷰 1건의 평균 크레딧 비용은 Lite $0.25, Balanced $1.50, Max $10로 안내된다.
  ([코드 리뷰](https://docs.github.com/en/copilot/concepts/agents/code-review),
  [클라우드 에이전트](https://docs.github.com/en/copilot/concepts/agents/cloud-agent/about-cloud-agent))
- **Cursor Teams·Enterprise**: 외부 모델에 API 가격과 별도로 MTok당 $0.25의 "Cursor Token Rate"가 붙는다.
  BYOK에도 적용되고 개인 요금제에는 적용되지 않는다. ([가격 문서](https://cursor.com/docs/account/pricing))
- **빠른 모드**: Claude Fable 5.2 Fast는 $16/$80으로 표준의 2배다. OpenAI Fast 등급도 2배다.
  Codex 구독 한도에서는 Fast가 2.5배, Ultrafast가 최대 8배로 차감된다.
  ([Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing), [Codex 가격](https://learn.chatgpt.com/docs/pricing))

## 6. 비교에서 드러나는 점

- **같은 모델이면 토큰 단가는 어디서나 거의 같다.** Claude Sonnet 5.5는 Claude API, GitHub Copilot, Cursor, OpenCode에서
  모두 MTok당 $2/$10이다. 제품 간 차이는 단가가 아니라 포함 사용량, 수수료, 부가 요금에서 난다.
- **구독 한도는 비교가 어렵다.** Claude Code, Cursor, Devin은 포함 사용량을 배수로만 공개한다.
  Anthropic이 공개한 참고치는 API 과금 기준 개발자 1인당 활성일 평균 약 $13, 월 $150~250이다.
  ([Claude Code 비용 문서](https://code.claude.com/docs/en/costs))
- **에이전트 구조가 토큰 소비를 키운다.** Claude Code의 에이전트 팀은 표준 세션의 약 7배 토큰을 쓰고,
  하위 에이전트·예약 작업·컨텍스트 압축은 각각 별도 요청을 보낸다.
  ([Claude Code 비용 문서](https://code.claude.com/docs/en/costs))
  Perplexity는 작업 1건의 비용을 15크레딧(단순 조회)에서 15,000크레딧 이상(앱 제작)까지로 안내한다.
  ([크레딧 안내](https://www.perplexity.ai/help-center/en/articles/13838041-how-credits-work-on-perplexity))
- **지출 통제 수단은 제품마다 다르다.** 종량 과금이 붙는 제품은 대부분 월 지출 상한이나 예산을 제공한다
  (Claude 사용 크레딧 상한, Copilot 달러 예산, Replit 예산 상한, Kiro의 선불 팩 상한).
  Copilot Business·Enterprise는 유료 초과 사용이 기본으로 켜져 있어 도입 시 설정을 확인해야 한다.
  ([Copilot 사용량 기반 과금](https://docs.github.com/en/copilot/concepts/billing/organizations-and-enterprises/usage-based-billing))
- **에이전트가 외부 입력에 따라 스스로 도구를 호출하고 반복 실행하므로, 비용 상한이 없는 종량 과금은
  의도치 않은 과다 청구로 이어질 수 있다.** 이는 조사한 가격 자료가 아니라 과금 구조에서 끌어낸 해석이다.

## 7. 확인하지 못한 항목

- **OpenAI 고객센터(help.openai.com)**: 모든 문서가 403으로 열리지 않았다. Codex 크레딧의 달러 가격,
  ChatGPT agent의 요금제별 한도를 확정하지 못했다. 검색 결과 요약에 따르면 ChatGPT agent는 종료되고
  "ChatGPT Work"로 대체되었다고 한다 **(2차 출처)**.
- **Claude·Cursor·Devin·Antigravity의 구독 한도 크기**: 벤더가 토큰 수나 금액으로 공개하지 않는다.
- **Devin ACU 단가, 토큰→ACU 환산표**: 공개 수치가 없다.
- **ServiceNow**: 공식 가격 페이지가 열리지 않아 커뮤니티 글에만 의존했다.
- **Manus 요금제 전체 가격, Gemini Enterprise 좌석 가격, Foundry Agent Service 도구 단가**: 공식 페이지에서 수치를 읽지 못했다.
- **Salesforce 액션 1건이 포괄하는 토큰 수**: 제3자 글은 액션당 10,000토큰까지라고 하나 공식 문서로 확인하지 못했다
  **(2차 출처: [Jitendra Zaa](https://www.jitendrazaa.com/blog/salesforce/salesforce-agentforce-credits-cost-model-complete-guide-2026/))**.
- **Jules의 모델 표기**: 문서가 Gemini 2.5 Pro·3 Pro를 언급해 최신 상태가 아닐 수 있다.
- 조사는 페이지 내용을 요약해 주는 도구로 읽었다. 수치를 인용하기 전에 원문을 다시 여는 것이 안전하다.

## 8. 출처 목록

### Anthropic

- [Claude 요금제](https://claude.com/pricing)
- [Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing)
- [Claude Code 비용 관리](https://code.claude.com/docs/en/costs)
- [Pro·Max 요금제에서 Claude Code 사용](https://support.claude.com/en/articles/11145838-using-claude-code-with-your-pro-or-max-plan)
- [Team·Enterprise 요금제에서 Claude Code 사용](https://support.claude.com/en/articles/11845131-use-claude-code-with-your-team-or-enterprise-plan)
- [유료 요금제의 사용 크레딧](https://support.claude.com/en/articles/12429409-extra-usage-for-paid-claude-plans)
- [Claude 요금제로 Agent SDK 사용](https://support.claude.com/en/articles/15036540-use-the-claude-agent-sdk-with-your-claude-plan)
- [Max·Ultra·Team의 월간 API 크레딧](https://support.claude.com/en/articles/17154008-monthly-api-credits-for-max-ultra-and-team-plans)

### OpenAI

- [Codex 가격](https://learn.chatgpt.com/docs/pricing)
- [OpenAI API 가격](https://developers.openai.com/api/docs/pricing)
- [ChatGPT 요금제](https://chatgpt.com/pricing)

### Google

- [Gemini API 가격](https://aistudio.google.com/docs/pricing)
- [Gemini CLI 할당량·가격](https://geminicli.com/docs/resources/quota-and-pricing/)
- [Gemini CLI README](https://github.com/google-gemini/gemini-cli/blob/main/README.md)
- [Antigravity 가격](https://antigravity.google/pricing), [요금제 문서](https://antigravity.google/docs/plans)
- [Google AI 요금제](https://one.google.com/about/google-ai-plans/)
- [Jules 사용 한도](https://jules.google/docs/usage-limits/)
- [Gemini Enterprise Agent Platform SKU 통합 안내](https://services.google.com/fh/files/emails/b_502770571_gemini_enterprise_agent_platform_pricing.pdf)

### 코딩 에이전트

- GitHub Copilot: [사용량 기반 과금](https://docs.github.com/en/copilot/concepts/billing/organizations-and-enterprises/usage-based-billing),
  [요금제](https://docs.github.com/en/copilot/get-started/plans),
  [모델별 단가](https://docs.github.com/en/copilot/reference/copilot-billing/models-and-pricing),
  [개인 과금](https://docs.github.com/en/copilot/concepts/billing-and-usage/individuals/billing),
  [전환 공지](https://github.blog/changelog/2026/04/27/github-copilot-is-moving-to-usage-based-billing/),
  [Flex 할당·Max 요금제 공지](https://github.blog/news-insights/company-news/github-copilot-individual-plans-introducing-flex-allotments-in-pro-and-pro-and-a-new-max-plan/),
  [코드 리뷰](https://docs.github.com/en/copilot/concepts/agents/code-review),
  [클라우드 에이전트](https://docs.github.com/en/copilot/concepts/agents/cloud-agent/about-cloud-agent)
- Cursor: [가격](https://cursor.com/pricing), [가격 문서](https://cursor.com/docs/account/pricing),
  [사용 한도](https://cursor.com/help/models-and-usage/usage-limits)
- Devin / Windsurf: [가격](https://devin.ai/pricing), [셀프서비스 과금](https://docs.devin.ai/admin/billing/self-serve),
  [사용량 문서](https://docs.devin.ai/desktop/accounts/usage), [모델 단가](https://docs.devin.ai/desktop/models),
  [요금제 개편 공지](https://cognition.com/blog/new-self-serve-plans-for-devin),
  [Windsurf 요금 변경](https://devin.ai/blog/windsurf-pricing-plans)
- Kiro: [가격](https://kiro.dev/pricing/), [모델 배수](https://kiro.dev/docs/models/),
  [추가 크레딧](https://kiro.dev/docs/billing/add-on-credits/)
- Amazon Q Developer: [가격](https://aws.amazon.com/q/developer/pricing/),
  [지원 종료 공지](https://aws.amazon.com/blogs/devops/amazon-q-developer-end-of-support-announcement/)
- Replit: [가격](https://replit.com/pricing), [AI 과금](https://docs.replit.com/billing/ai-billing),
  [사용량 과금](https://docs.replit.com/billing/about-usage-based-billing), [요금제](https://docs.replit.com/billing/plans)
- JetBrains AI: [라이선스·구독](https://www.jetbrains.com/help/ai-assistant/licensing-and-subscriptions.html)
- Cline: [가격](https://cline.bot/pricing), [ClinePass](https://docs.cline.bot/getting-started/clinepass),
  [제공사 공지](https://cline.bot/blog/cline-provider-now-runs-on-vercel)
- [Aider 저장소](https://github.com/Aider-AI/aider)
- OpenCode: [게이트웨이](https://opencode.ai/docs/zen/), [Go 구독](https://opencode.ai/docs/go/)
- [Roo Code 저장소(보관됨)](https://github.com/RooCodeInc/Roo-Code)

### 범용·엔터프라이즈 에이전트와 플랫폼

- Microsoft: [Copilot Studio 가격](https://www.microsoft.com/en-us/microsoft-365-copilot/pricing/copilot-studio),
  [과금 요율](https://learn.microsoft.com/en-us/microsoft-copilot-studio/requirements-messages-management),
  [라이선스](https://learn.microsoft.com/en-us/microsoft-copilot-studio/billing-licensing),
  [종량제 미터](https://learn.microsoft.com/en-us/power-platform/admin/pay-as-you-go-meters),
  [Foundry Agent Service 가격](https://azure.microsoft.com/en-us/pricing/details/foundry-agent-service/),
  [Foundry 에이전트 개요](https://learn.microsoft.com/en-us/azure/foundry/agents/overview)
- Salesforce: [Agentforce 가격](https://www.salesforce.com/agentforce/pricing/),
  [Flex 크레딧 요율표](https://www.salesforce.com/en-us/wp-content/uploads/sites/4/documents/white-papers/Rate-Card-Flex-Credits.pdf)
- AWS: [AgentCore 가격](https://aws.amazon.com/bedrock/agentcore/pricing/),
  [Bedrock Agents Classic 유지보수 모드](https://docs.aws.amazon.com/bedrock/latest/userguide/agents-classic-maintenance-mode.html)
- [Manus 크레딧 안내](https://help.manus.im/en/articles/11711097-how-do-credits-work)
- Perplexity: [크레딧 안내](https://www.perplexity.ai/help-center/en/articles/13838041-how-credits-work-on-perplexity),
  [Max 요금제](https://www.perplexity.ai/help-center/en/articles/11680686-perplexity-max)
- [Genspark 멤버십](https://www.genspark.ai/helpcenter/membership-plans)
- Lovable: [가격](https://lovable.dev/pricing), [요금제·크레딧](https://docs.lovable.dev/introduction/plans-and-credits)
- [Bolt 가격](https://bolt.new/pricing)
- [v0 가격](https://v0.app/pricing)
- [OpenRouter 가격](https://openrouter.ai/pricing)

### 2차 출처 (공식 페이지로 확인하지 못한 수치에만 사용)

- [morphllm: Codex pricing](https://www.morphllm.com/codex-pricing)
- [ServiceNow 커뮤니티: AI-native licensing in 2026](https://www.servicenow.com/community/upgrades-and-patching-forum/servicenow-ai-native-licensing-in-2026-a-practical-guide-to/m-p/3565858) (사용자 작성)
- [Jitendra Zaa: Agentforce credits cost model](https://www.jitendrazaa.com/blog/salesforce/salesforce-agentforce-credits-cost-model-complete-guide-2026/)
