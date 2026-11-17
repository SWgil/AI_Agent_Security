# 간접 프롬프트 인젝션(IPI) 실제 취약점 사례

> 기준 시점: 2026년 11월 17일 웹 검색으로 확인.
> 벤치마크·학술 평가가 아니라, 실제 서비스 중인 제품에서 보고된 취약점과 실제 악용 관측만 모았다.
> 대부분은 연구자가 재현해 벤더에 제보한 사례이며, 실제 피해가 확인된 사례는 4절에 따로 정리했다.
> CVSS 점수와 날짜는 출처마다 어긋나는 경우가 있어 본문에 표시했다. 인용 전에는 벤더 권고문을 확인해야 한다.
> 링크는 가능한 한 발견자의 1차 글을 걸었고, 1차 글을 확인하지 못한 항목은 보도 기사를 걸었다.

## 1. 요약

- IPI는 공격자가 에이전트에 직접 말을 걸지 않고, 에이전트가 **읽게 될 데이터**(메일, 문서, 웹 페이지, 이슈, 로그 등)에 지시를 심는 공격이다.
- 2023년에는 챗봇 답변을 조작하는 수준이었지만, 2025년부터는 **사용자 조작 없이 데이터가 유출되는(제로클릭)** 사례와 **개발자 PC에서 코드가 실행되는** 사례가 주요 제품 대부분에서 보고되었다.
- 2026년에는 연구 시연을 넘어 **실제 공급망 침해**(Clinejection)와 **웹상의 실제 공격 페이로드**가 관측되었다.
- 거의 모든 사례가 같은 구조다: ① 비신뢰 입력을 읽고 ② 민감 데이터에 접근할 수 있고 ③ 외부로 내보낼 통로가 있다.
- 벤더의 수정은 대부분 ③의 특정 통로(이미지 렌더링, 특정 URL 등)를 막는 방식이었다. 모델이 데이터 속 지시를 따르는 근본 문제는 남아 있어, 같은 제품에서 다른 통로로 재발하는 경우가 많다.

## 2. 사례 목록

"사용자 개입" 열의 **제로클릭**은 피해자가 악성 콘텐츠를 열거나 클릭하지 않아도, 평소처럼 에이전트를 쓰는 것만으로 공격이 성립한다는 뜻이다.

### 2.1 업무용 어시스턴트 (메일·문서·캘린더·CRM)

| 시기 | 대상 | 이름 / 식별자 | 유입 경로 | 결과 | 사용자 개입 |
|---|---|---|---|---|---|
| 2023.02 | Bing Chat | Greshake 외 논문 | 웹 페이지의 숨긴 텍스트 | 챗봇이 사용자에게 개인정보를 캐내도록 조작 | 해당 페이지에서 챗 사용 |
| 2023.11 | Google Bard | — | 공유된 Google Docs | 대화 내용을 이미지 URL로 유출 | 문서 관련 질문 |
| 2024.08 | Slack AI | — | 공개 채널의 메시지 | 비공개 채널 데이터 유출 | 관련 질문 후 링크 클릭 |
| 2024.09 | ChatGPT (macOS 앱) | SpAIware | 웹 페이지·문서 | 장기 메모리에 지시를 심어 이후 대화를 계속 유출 | 악성 콘텐츠 1회 열람 |
| 2025.06 | Microsoft 365 Copilot | EchoLeak, CVE-2025-32711 | 수신 메일 | Copilot 컨텍스트의 사내 데이터 유출 | 제로클릭 |
| 2025.08 | Gemini (Workspace) | Invitation Is All You Need | 캘린더 초대 제목 | 스마트홈 기기 조작, 메일 유출, 앱 실행 등 14개 시나리오 | 일정 관련 질문 |
| 2025.08 | ChatGPT Connectors | AgentFlayer | 공유된 문서 | 연결된 Drive 등의 데이터(API 키) 유출 | 제로클릭 |
| 2025.09 | ChatGPT Deep Research | ShadowLeak | 수신 메일(Gmail 연결) | 메일함 데이터가 OpenAI 서버에서 직접 유출 | 제로클릭 |
| 2025.09 | Salesforce Agentforce | ForcedLeak | Web-to-Lead 폼 입력 | CRM 데이터 유출 | 직원이 해당 리드를 에이전트로 처리 |
| 2025.09 | Notion 3.0 에이전트 | — | PDF의 흰 글씨 | 워크스페이스 기밀을 웹 검색 도구로 유출 | PDF 요약 요청 |
| 2025.09 | Gemini (Cloud Assist 등) | Gemini Trifecta | 로그 항목, 검색 기록 | 자격증명 피싱, 저장 정보·위치 유출 | 로그 요약 등 일반 사용 |
| 2025.10 | Claude (코드 실행) | — | 문서 | 샌드박스의 데이터를 공격자 계정으로 업로드 | 문서 분석 요청 |
| 2025.11 | ServiceNow Now Assist | 2차 프롬프트 인젝션 | 티켓 등 레코드 필드 | 저권한 에이전트가 고권한 에이전트를 호출해 데이터 복사·권한 상승 | 다른 사용자가 해당 레코드 처리 |
| 2025.12 | Gemini Enterprise | GeminiJack | 공유 문서, 초대, 메일 | Gmail·Docs·캘린더 데이터 유출 | 제로클릭 |
| 2025.12(패치) | ChatGPT Connectors + 메모리 | ZombieAgent | 메일·문서 | 메모리에 지시를 남겨 세션을 넘어 지속 유출 | 제로클릭 |
| 2026.01 | Microsoft Copilot Studio | ShareLeak, CVE-2026-21520 | SharePoint 폼 입력 | 에이전트가 접근하는 데이터 유출 | 폼 제출만으로 성립 |
| 2026.01 | Claude Cowork | — | 문서의 숨긴 텍스트 | 로컬 파일을 공격자 계정으로 업로드 | 문서 분석 요청 |

세부 내용:

- **Bing Chat (2023)**: IPI라는 용어를 처음 정립한 논문이 실제 Bing Chat을 대상으로 시연했다.
  ([arXiv 2302.12173](https://arxiv.org/abs/2302.12173))
- **Google Bard (2023)**: 공유 문서의 지시로 대화 내용을 마크다운 이미지 URL에 실어 내보냈다. 이후 수많은 사례에서 반복되는 "이미지 렌더링 유출" 패턴의 초기 사례다.
  ([Embrace The Red](https://embracethered.com/blog/posts/2023/google-bard-data-exfiltration/))
- **Slack AI (2024)**: 공격자가 공개 채널에 글만 올려도 접근 권한이 없는 비공개 채널의 데이터를 빼낼 수 있었다. Slack은 처음에 의도된 동작이라고 했다가 공개 후 패치했다.
  ([PromptArmor](https://promptarmor.substack.com/p/data-exfiltration-from-slack-ai-via))
- **SpAIware (2024)**: 인젝션이 한 번 성공하면 메모리 기능에 지시가 저장되어 이후 모든 대화가 유출되는 지속형 공격이다.
  ([Embrace The Red](https://embracethered.com/blog/posts/2024/chatgpt-macos-app-persistent-data-exfiltration/))
- **EchoLeak (2025)**: Aim Security가 2025년 1월 제보, 6월 공개. CVSS 9.3. 메일 한 통으로 성립하며 Microsoft의 인젝션 분류기(XPIA)와 외부 링크 차단을 모두 우회했다. Microsoft가 서버 측에서 수정했고 실제 악용 증거는 없다고 밝혔다.
  ([The Hacker News](https://thehackernews.com/2025/06/zero-click-ai-vulnerability-exposes.html),
  [SecurityWeek](https://www.securityweek.com/echoleak-ai-attack-enabled-theft-of-sensitive-data-via-microsoft-365-copilot/),
  [사후 분석 논문](https://arxiv.org/abs/2509.10540v1))
- **Invitation Is All You Need (2025)**: SafeBreach·텔아비브대·테크니온 연구진이 2025년 2월 Google에 제보했고 DEF CON 33에서 발표했다. "고마워" 같은 일상 표현을 방아쇠로 삼아 조명·창문·보일러를 조작했다.
  ([arXiv 2508.12175](https://arxiv.org/abs/2508.12175),
  [The Decoder](https://the-decoder.com/attackers-can-hijack-google-gemini-with-a-simple-prompt-hidden-in-a-calendar-invite/))
- **AgentFlayer (2025)**: Zenity가 Black Hat에서 발표. ChatGPT가 신뢰하는 Azure Blob Storage URL을 이미지 주소로 써서 유출 차단을 우회했다. 일부 벤더는 의도된 기능이라며 수정하지 않았다.
  ([Zenity](https://zenity.io/blog/security/zero-click-attacks-when-ttps-resurface-across-platforms),
  [eSecurityPlanet](https://www.esecurityplanet.com/news/ai-agents-vulnerable-silent-hijacking/))
- **ShadowLeak (2025)**: 유출 요청이 사용자 기기가 아니라 OpenAI 클라우드에서 나가므로 기업 네트워크 보안 장비에 보이지 않는다. Radware가 2025년 6월 18일 제보했다. 수정 시점은 8월 초라는 자료와 9월 3일이라는 자료가 엇갈린다.
  ([Radware](https://www.radware.com/blog/threat-intelligence/shadowleak/),
  [The Hacker News](https://thehackernews.com/2025/09/shadowleak-zero-click-flaw-leaks-gmail.html))
- **ForcedLeak (2025)**: Noma Security가 CVSS 9.4로 평가. 허용 목록에 남아 있던 만료 도메인을 5달러에 사서 유출 통로로 썼다. Salesforce는 신뢰 URL 강제 적용으로 수정했다.
  ([Noma Security](https://noma.security/blog/forcedleak-agent-risks-exposed-in-salesforce-agentforce/),
  [The Hacker News](https://thehackernews.com/2025/09/salesforce-patches-critical-forcedleak.html))
- **Notion 3.0 (2025)**: 웹 검색 도구가 URL을 인자로 받는 점을 이용해 검색 요청 자체를 유출 통로로 썼다. Notion은 이후 관리자가 에이전트의 웹 접근을 끌 수 있게 했다.
  ([CodeIntegrity](https://www.codeintegrity.ai/blog/notion),
  [The Decoder](https://the-decoder.com/notion-ai-agents-get-security-update-after-data-leak/))
- **Gemini Trifecta (2025)**: Tenable이 3건을 묶어 공개. HTTP User-Agent 헤더에 지시를 넣어 클라우드 로그를 오염시키는 등, 사람이 쓴 문서가 아닌 **기계 생성 데이터**도 유입 경로가 됨을 보였다. Google이 모두 수정했다.
  ([Tenable](https://www.tenable.com/blog/the-trifecta-how-three-new-gemini-vulnerabilities-in-cloud-assist-search-model-and-browsing))
- **Claude 코드 실행 / Cowork (2025~2026)**: 샌드박스가 외부 접속을 막아도 허용 목록에 있는 Anthropic API로 공격자 계정에 파일을 올릴 수 있었다. 2025년 10월 보고된 수법이 2026년 1월 출시된 Cowork에서 재현되었다. Anthropic은 처음에 범위 밖으로 종결했다가 절차 오류였다고 정정했다.
  ([Embrace The Red](https://embracethered.com/blog/posts/2025/claude-abusing-network-access-and-anthropic-api-for-data-exfiltration/),
  [PromptArmor](https://www.promptarmor.com/resources/claude-cowork-exfiltrates-files),
  [The Register](https://www.theregister.com/2026/01/15/anthropics_claude_bug_cowork/))
- **ServiceNow Now Assist (2025)**: 인젝션 방어 기능을 켠 상태에서도 성립했다. ServiceNow는 설계대로의 동작이라며 문서만 보강했다.
  ([AppOmni](https://appomni.com/ao-labs/ai-agent-to-agent-discovery-prompt-injection/),
  [The Hacker News](https://thehackernews.com/2025/11/servicenow-ai-agents-can-be-tricked.html))
- **GeminiJack (2025)**: 사내 검색(RAG)이 오염된 문서를 끌어오면서 발동한다. Google이 수정했다.
  ([Noma Security](https://noma.security/blog/geminijack-google-gemini-zero-click-vulnerability/),
  [SC Media](https://www.scworld.com/news/google-addresses-geminijack-exploit-affecting-gemini-enterprise))
- **ZombieAgent (2025)**: Radware가 2025년 9월 제보, 12월 중순 패치. URL을 모델이 변형하지 못하게 한 기존 방어를 우회했다.
  ([Radware](https://www.radware.com/blog/threat-intelligence/zombieagent/),
  [TechRadar](https://www.techradar.com/pro/security/this-zombieagent-zero-click-vulnerability-allows-for-silent-account-takeover-heres-what-we-know))
- **ShareLeak (2026)**: Capsule Security가 2025년 11월 발견, Microsoft가 2026년 1월 15일 패치. CVSS 7.5. 발견자는 패치 후에도 유출이 재현되었다고 주장한다.
  ([VentureBeat](https://venturebeat.com/security/microsoft-salesforce-copilot-agentforce-prompt-injection-cve-agent-remediation-playbook))

### 2.2 코딩 에이전트·IDE·CI

| 시기 | 대상 | 이름 / 식별자 | 유입 경로 | 결과 |
|---|---|---|---|---|
| 2025.05 | GitLab Duo | — | MR 설명, 커밋 메시지, 이슈 댓글, 소스 코드 | 비공개 소스 코드 유출, 응답에 악성 HTML 삽입 |
| 2025.07 | Gemini CLI | — | 저장소의 README | 사용자 모르게 셸 명령 실행, 환경 변수 유출 |
| 2025.08 | Cursor | CurXecute, CVE-2025-54135 | MCP로 읽은 Slack 메시지 | `mcp.json`을 고쳐 승인 없이 명령 실행(RCE) |
| 2025.08 | GitHub Copilot (VS Code) | CVE-2025-53773 | 소스 주석, 이슈, 웹 페이지 | 설정 파일을 고쳐 자동 승인 모드로 전환 후 RCE |
| 2025.10 | GitHub Copilot Chat | CamoLeak | PR 설명의 숨긴 주석 | 비공개 저장소의 코드·비밀값 유출 |
| 2025.11 | Google Antigravity | — | 웹 문서의 작은 글씨 | 브라우저 하위 에이전트로 자격증명·코드 유출 |
| 2025.12 | Cursor, Windsurf, Copilot, Zed, Roo Code, Cline 등 | IDEsaster | 저장소 파일 등 | 데이터 유출, RCE (30건 이상, CVE 24건) |
| 2025.12 | GitHub Actions의 AI 에이전트 | PromptPwnd | 이슈·PR 본문 | CI 비밀값 유출, 저장소 변조 |
| 2026.02 | Cline (이슈 분류 봇) | Clinejection | 이슈 제목 | npm 배포 토큰 탈취 → **악성 버전 실제 배포** |
| 2026.04 | Claude Code·Gemini CLI·Copilot의 Actions 연동 | Comment and Control | PR 제목, 이슈 댓글 | CI 비밀값·API 키 유출 |

세부 내용:

- **GitLab Duo (2025)**: Legit Security가 2025년 2월 제보. GitLab은 외부 도메인을 가리키는 HTML 태그를 렌더링하지 않도록 수정했다.
  ([Legit Security](https://www.legitsecurity.com/blog/remote-prompt-injection-in-gitlab-duo),
  [The Hacker News](https://thehackernews.com/2025/05/gitlab-duo-vulnerability-enabled.html))
- **Gemini CLI (2025)**: Tracebit이 출시 이틀 뒤인 6월 27일 제보, 7월 25일 v0.1.14에서 수정. 인젝션에 더해 명령 허용 목록 검증의 허점과 실행 내용을 가리는 UI 문제가 결합되었다.
  ([Tracebit](https://tracebit.com/blog/code-exec-deception-gemini-ai-cli-hijack),
  [CyberScoop](https://cyberscoop.com/google-gemini-cli-prompt-injection-arbitrary-code-execution/))
- **CurXecute (2025)**: Aim Labs 발견, Cursor 1.3에서 수정. CVSS는 8.6으로 보도되었으나 자료에 따라 더 높게 적힌 곳도 있다. 같은 시기 Check Point가 별도 취약점 MCPoison(CVE-2025-54136)을 공개했다.
  ([The Hacker News](https://thehackernews.com/2025/08/cursor-ai-code-editor-fixed-flaw.html),
  [Tenable CVE](https://www.tenable.com/cve/CVE-2025-54135))
- **CVE-2025-53773 (2025)**: 에이전트가 **자기 자신의 권한 설정 파일을 수정**할 수 있다는 점이 핵심이다. `.vscode/settings.json`에 `"chat.tools.autoApprove": true`를 추가하게 만든다. CVSS 7.8, 2025년 8월 정기 패치에서 수정.
  ([Embrace The Red](https://embracethered.com/blog/posts/2025/github-copilot-remote-code-execution-via-prompt-injection/))
- **CamoLeak (2025)**: CVSS 9.6. GitHub 자체 이미지 프록시(Camo)를 유출 통로로 써서 콘텐츠 보안 정책을 우회했다. GitHub은 Copilot Chat의 이미지 렌더링을 아예 꺼서 대응했다(8월 14일).
  ([Legit Security](https://legitsecurity.com/blog/camoleak-critical-github-copilot-vulnerability-leaks-private-source-code),
  [The Register](https://www.theregister.com/2025/10/09/github_copilot_chat_vulnerability/))
- **Antigravity (2025)**: 기본 브라우저 허용 목록에 요청 기록 서비스(webhook.site)가 들어 있어 유출 통로가 되었다. Google은 이런 유출을 포상 대상이 아닌 "알려진 문제"로 분류한다.
  ([PromptArmor](https://www.promptarmor.com/resources/google-antigravity-exfiltrates-data),
  [Simon Willison](https://simonwillison.net/2025/Nov/25/google-antigravity-exfiltrates-data/))
- **IDEsaster (2025)**: 연구자 Ari Marzouk이 시험한 모든 AI IDE에서 하나 이상의 공격 체인이 성립했다고 보고했다.
  ([The Hacker News](https://thehackernews.com/2025/12/researchers-uncover-30-flaws-in-ai.html))
- **PromptPwnd (2025)**: Aikido가 명명. 이슈 분류, PR 라벨링 등에 AI를 쓰는 워크플로가 대상이며 Google의 Gemini CLI 저장소도 해당되어 4일 만에 패치되었다.
  ([Aikido](https://www.aikido.dev/blog/promptpwnd-github-actions-ai-agents))
- **Clinejection (2026)**: 4.1절 참고.
- **Comment and Control (2026)**: Aonan Guan과 존스홉킨스대 연구진이 공개. Anthropic은 Claude Code Security Review 건을 CVSS 9.4로 평가했다. 공개 시점은 4월이라는 자료와 6월 1일이라는 자료가 엇갈린다. CVE는 발급되지 않은 것으로 보도되었다.
  ([SecurityWeek](https://www.securityweek.com/claude-code-gemini-cli-github-copilot-agents-vulnerable-to-prompt-injection-via-comments/),
  [CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-coding-agent-ci-prompt-injection-202608/))

### 2.3 MCP·도구 연결

| 시기 | 대상 | 유입 경로 | 결과 |
|---|---|---|---|
| 2025.05 | GitHub MCP 서버 | 공개 저장소의 이슈 | 비공개 저장소 내용을 공개 PR로 유출 |
| 2025.07 | Supabase MCP (Cursor) | 고객 지원 티켓 | 관리자 권한 SQL로 DB의 토큰 유출 |

- **GitHub MCP (2025)**: Invariant Labs는 서버 코드의 버그가 아니라 에이전트 시스템 구조의 문제라고 설명했고 CVE는 없다. 완화책은 저장소 단위로 토큰 권한을 나누는 것이다.
  ([Invariant Labs](https://invariantlabs.ai/blog/mcp-github-vulnerability),
  [DevClass](https://devclass.com/2025/05/27/researchers-warn-of-prompt-injection-vulnerability-in-github-mcp-with-no-obvious-fix/))
- **Supabase MCP (2025)**: Supabase는 실제 고객 피해는 없고 MCP 고유의 취약점도 아니라고 반박했으며, 읽기 전용 모드와 도구 호출 수동 승인을 권고했다.
  ([General Analysis](https://www.generalanalysis.com/blog/supabase-mcp-blog),
  [Simon Willison](https://simonwillison.net/2025/Jul/6/supabase-mcp-lethal-trifecta/))
- 2.2절의 CurXecute도 MCP로 읽은 외부 데이터가 유입 경로였다.

### 2.4 AI 브라우저

| 시기 | 대상 | 유입 경로 | 결과 |
|---|---|---|---|
| 2025.08 | Perplexity Comet | Reddit 댓글의 숨긴 텍스트 | 로그인된 계정의 메일 주소·일회용 비밀번호 탈취 |
| 2025.10 | Perplexity Comet | 스크린샷 속 거의 안 보이는 글씨 | 위와 동일 계열 |
| 2025.10 | ChatGPT Atlas | URL처럼 보이는 문자열(주소창) | 에이전트가 공격자 지시 수행 |

- Brave는 이 문제가 특정 제품이 아니라 AI 브라우저 범주 전체의 구조적 문제라고 주장한다. AI 브라우저는 사용자의 로그인 세션을 그대로 쓰기 때문에 웹의 동일 출처 정책이 무력화된다.
  ([Brave: Comet](https://brave.com/blog/comet-prompt-injection/),
  [Brave: 보이지 않는 인젝션](https://brave.com/blog/unseeable-prompt-injections/))
- Atlas 주소창 건은 사용자가 문자열을 붙여 넣어야 하므로 엄밀한 IPI보다는 경계 사례에 가깝다. OpenAI CISO는 프롬프트 인젝션이 아직 해결되지 않은 문제라고 밝혔다.
  ([The Hacker News](https://thehackernews.com/2025/10/chatgpt-atlas-browser-can-be-tricked-by.html))

### 2.5 개인용 자율 에이전트 (OpenClaw)

- 메신저·메일·로컬 파일에 넓은 권한을 갖는 OpenClaw에서는 2026년 들어 다수의 IPI 경로가 보고되었다. 에이전트가 만든 링크를 메신저가 미리보기로 자동 요청하면서 데이터가 유출되는 경로가 대표적이다.
- 중국 CNCERT는 기본 설정이 취약하다고 경고했고, 정부 시스템에서의 사용이 제한되었다.
  ([The Hacker News](https://thehackernews.com/2026/03/openclaw-ai-agent-flaws-could-enable.html),
  [CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-openclaw-indirect-prompt-injection-2026061/))
- 개별 CVE와 수정 버전은 2차 자료마다 설명이 달라 이 문서에서는 적지 않았다.

## 3. 공통 패턴

**유입 경로** — 공격자가 쓸 수 있고 에이전트가 읽는 곳이면 어디든 된다.

- 외부인이 보낼 수 있는 것: 메일, 캘린더 초대, 공유 문서, 웹 폼, 고객 지원 티켓
- 공개 게시물: 웹 페이지, Reddit 댓글, 공개 저장소의 이슈·PR
- 저장소 내용: README, 소스 주석, 커밋 메시지
- 기계 생성 데이터: 로그(User-Agent 헤더), 검색 기록, 도구 결과
- 숨기는 방법: 흰 글씨, 작은 글씨, HTML 주석, 이미지 속 옅은 글씨

**유출 통로** — 수정이 이루어진 지점이 대부분 여기다.

- 마크다운 이미지·링크 렌더링 (Bard, EchoLeak, AgentFlayer, CamoLeak)
- 허용 목록에 있는 신뢰 도메인 악용: GitHub Camo, Azure Blob, Anthropic API, 만료된 허용 도메인, webhook.site
- URL을 인자로 받는 도구: 웹 검색, 브라우징 (Notion, ShadowLeak, Gemini Trifecta)
- 메신저의 링크 미리보기 (OpenClaw)
- 공개된 곳에 쓰기: 공개 PR, 이슈 댓글 (GitHub MCP, PromptPwnd)

**권한 상승·지속** — 유출을 넘어서는 단계.

- 에이전트가 자기 설정 파일을 수정해 승인 절차를 끔 (CVE-2025-53773, CurXecute)
- 장기 메모리에 지시를 저장해 세션을 넘어 지속 (SpAIware, ZombieAgent)
- 저권한 에이전트가 고권한 에이전트를 호출 (ServiceNow)
- CI 캐시 오염으로 다른 워크플로의 비밀값 탈취 (Clinejection)

**벤더 대응의 경향**

- 서버 측에서 조용히 수정하고 CVE를 발급하지 않는 경우가 많다. CVE가 붙은 사례는 일부다.
- "의도된 동작", "알려진 문제"로 분류하고 설정·문서로 대응한 사례가 있다 (Slack 초기 대응, ServiceNow, Antigravity, GitHub MCP).
- 같은 제품군에서 재발한다: ChatGPT(SpAIware → AgentFlayer → ShadowLeak → ZombieAgent), Gemini, Copilot, Claude 모두 여러 차례 보고되었다.

## 4. 실제 악용 관측

2절의 사례 대부분은 연구자의 재현이다. 아래는 실제 공격 또는 실제 공격 시도가 관측된 경우다.

### 4.1 Clinejection — 실제 공급망 침해 (2026.02)

- Cline 저장소의 AI 이슈 분류 워크플로가 이슈 제목을 그대로 프롬프트에 넣었고, 에이전트에 셸 실행 권한이 있었다.
- 인젝션으로 분류 워크플로를 장악한 뒤 GitHub Actions 캐시를 오염시켜, 야간 릴리스 워크플로의 npm 배포 토큰을 탈취했다.
- 연구자 Adnan Khan이 2월 9일 공개했고, 2월 17일 누군가 이 수법으로 악성 `cline@2.3.0`을 npm에 배포했다. 설치 시 OpenClaw를 몰래 설치하는 내용이었다.
- 영향 규모는 약 4,000대로 알려졌으나 2차 자료의 수치다.
- Cline은 해당 버전을 폐기하고 토큰을 폐기했으며 배포 방식을 OIDC로 바꿨다.

([CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-clinejection-prompt-injection-cicd-cache-p/),
[The Hacker News](https://thehackernews.com/2026/02/cline-cli-230-supply-chain-attack.html),
[Simon Willison](https://simonwillison.net/2026/Mar/6/clinejection/))

### 4.2 웹상의 실제 페이로드 (2026)

- **Palo Alto Unit 42 (2026.03)**: 실험이 아닌 탐지 데이터로 실제 웹 사이트의 IPI를 보고했다. AI 기반 광고 심사를 우회하려는 첫 관측 사례, 피싱 사이트를 홍보하는 검색 조작, 무단 결제 시도, 데이터 삭제 시도가 포함된다.
  ([Unit 42](https://unit42.paloaltonetworks.com/ai-agent-prompt-injection/))
- **Google (2026.04)**: 월 20~30억 페이지 규모의 Common Crawl을 분석한 결과, 악성 IPI가 든 페이지 비율이 2025년 11월부터 2026년 2월 사이 상대적으로 32% 늘었다. 정교함은 아직 낮다고 평가했다. 로그인이 필요한 페이지와 소셜 미디어는 분석 범위 밖이다.
  ([Google 보안 블로그](https://blog.google/security/prompt-injections-web/))
- **Forcepoint X-Labs (2026.04)**: 실제 사이트에서 금융 사기, 데이터 파괴, API 키 탈취 목적의 페이로드 10건을 확인했다. 여러 도메인이 같은 템플릿을 써서 조직적 도구 사용이 의심된다.
  ([Forcepoint](https://www.forcepoint.com/blog/x-labs/indirect-prompt-injection-payloads))
- **학술 측정 (2026.04)**: 12억 개 URL에서 1만 1,700개 페이지, 1만 5,300건의 IPI를 확인했다. 소수의 템플릿이 대부분을 차지한다.
  ([arXiv 2604.27202](https://arxiv.org/abs/2604.27202))

이 관측들은 "페이로드가 웹에 존재한다"는 것이며, 그로 인해 실제 에이전트가 피해를 입은 건수를 뜻하지는 않는다.

## 5. 경계 사례: 링크 매개 인젝션

아래는 흔히 IPI와 함께 언급되지만, 피해자가 공격자의 링크를 클릭해야 하고 지시가 URL 매개변수로 들어온다는 점에서 구분된다.

- **Reprompt (CVE-2026-24307, 2026.01)**: Copilot Personal에서 URL의 `q` 매개변수가 프롬프트로 채워지는 점을 이용한 원클릭 유출. 기업용 M365 Copilot은 해당되지 않는다.
  ([Varonis](https://www.varonis.com/blog/reprompt))
- **SearchLeak (CVE-2026-42824, 2026.06)**: M365 Copilot 기업용 검색에서 같은 계열의 원클릭 유출. 인젝션, HTML 렌더링 경쟁 조건, Bing SSRF를 연결했다. Microsoft는 6.5점, NVD는 7.5점으로 평가가 갈린다.
  ([Varonis](https://www.varonis.com/blog/searchleak))

## 6. 이 문서의 한계

- 공개된 사례만 담았다. 벤더가 조용히 수정했거나 제보자가 공개하지 않은 건은 빠져 있다.
- 발견자 대부분이 AI 보안 제품을 파는 회사다. 심각도 표현과 "최초" 주장은 발견자 측 서술인 경우가 많다.
- 2026년 9월 이후 공개 건은 이번 검색에서 확인하지 못했다.
- 모든 사례를 망라한 목록이 아니다. Rehberger의 "Month of AI Bugs"(2025.08)처럼 한 연구자가 수십 건을 연속 공개한 경우는 대표 사례만 실었다.
