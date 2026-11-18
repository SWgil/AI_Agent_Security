# 브라우저 에이전트("에이전틱 브라우저"): 보안 아키텍처와 공개된 취약점 — 2026-11-18 기준 현황

보고서 작성자를 위한 읽기 안내:
- 범위: 웹 브라우저만 조작하는 에이전트. 이 파일은 `../화면 조작 에이전트 보안 기술/deployed_defenses.md`와 `threats_and_attacks.md`를 더 깊이 파고든 후속 문서다. 그 문서들에서 이미 다룬 항목은 내가 검증하거나 바로잡거나 확장한 경우에만 다시 적었다.
- 모든 항목에 붙인 검증 표시: **[P]** = 2026-11-18에 해당 페이지/PDF 자체를 직접 읽었다(1차 출처 또는 연구자 본인의 글). **[P-press]** = 언론/2차 기사 자체를 직접 읽었다(검색 스니펫만 본 것이 아니다). **[S]** = 검색 엔진 요약만 보았고 페이지는 열지 않았다(신뢰도가 낮고, 문구가 의역일 수 있다).
- 전혀 열 수 없었던 것: 모든 `openai.com` 및 `help.openai.com` URL(HTTP 403), Dark Reading과 TechRepublic(403), Perplexity의 "security into Comet" 블로그 글 두 편(404), LayerX 자체 블로그(도메인이 이제 Akamai 제품 페이지로 리디렉션된다), Cato의 HashJack 글(본문이 비어 있음), Microsoft Learn의 Edge Copilot Mode 페이지(404), Chrome Enterprise 정책 목록(탐색 메뉴만 표시됨). OpenAI 페이지를 브라우저 user-agent로, 그리고 웹 아카이브를 통해 가져오려고 한 번 시도했으나 세션의 권한 시스템이 차단했고, 더 시도하지 않았다. 따라서 OpenAI에 관한 모든 주장은 OpenAI를 인용한 언론 보도 또는 OpenAI 페이지의 검색 요약에 근거한다.
- 작성자가 알아야 할 제품 상태 변화: **ChatGPT Atlas는 종료되었고(2026년 7월 9일 발표, 지원 종료일 2026년 8월 9일)** **"ChatGPT agent"는 더 이상 제공되지 않는다.** 둘 다 ChatGPT Work, ChatGPT 데스크톱 앱의 내장 브라우저, Chrome 확장 프로그램으로 대체되었다. **Edge "Copilot Mode"** 는 별도 모드로서는 이름이 바뀌었거나 종료된 것으로 보도되었다(기사 제목만 확인). **LayerX** 는 Akamai에 인수된 것으로 보인다(블로그 URL이 Akamai로 리디렉션된다).

## 1. OpenAI — ChatGPT Atlas agent mode / ChatGPT agent / 후속 제품

### 핵심
OpenAI의 브라우저 에이전트 보안 모델은 다음과 같았다. 사용자가 선택하는 로그아웃 모드(쿠키 없음, 세션 없음), 일부 민감한 사이트에서 탭이 포커스를 잃으면 일시 정지하는 "watch mode", 중대한 결과를 낳는 행동 전의 확인, 로그인 시 사용자가 직접 넘겨받아 조작하는 방식, 강화학습으로 훈련한 자동 공격자를 바탕으로 적대적 훈련을 거친 에이전트 모델(2025년 12월), 그리고 agent mode를 통째로 끄는 선택형 Lockdown Mode다. OpenAI는 Atlas의 공격 성공률 지표를 한 번도 공개하지 않았고 제품 자체도 2026년 8월에 종료되었다. 그래서 2026년의 상태는 "통제 수단이 ChatGPT Work / 데스크톱 앱 내장 브라우저 / Chrome 확장 프로그램으로 옮겨 갔다"이며, 이들의 브라우저 관련 보안 문서는 내가 읽을 수 없었다.

### 인용 근거
Atlas 설계(2025년 10월):
- [P-press] OpenAI CISO Dane Stuckey(Simon Willison이 2025년 10월 22일 전문 인용): 로그아웃 모드에서는 에이전트가 "사용자의 자격 증명에 접근하지 않고(without access to your credentials)" 작업한다. "로그인 모드(logged in mode)"는 "매우 신뢰하는 사이트에서 범위가 잘 정해진 행동에 가장 적합하다(most appropriate for well-scoped actions on very trusted sites)". watch mode는 "에이전트가 작업하는 모습을 지켜보도록 탭을 활성 상태로 두어야 하며", "민감한 정보가 있는 탭에서 벗어나면 에이전트가 일시 정지한다". "프롬프트 인젝션은 여전히 미해결의 최전선 보안 문제다(prompt injection remains a frontier, unsolved security problem)". — [Simon Willison, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- [P-press] watch mode에 대한 독립 확인: Willison이 GitHub와 온라인 뱅킹 사이트에서 시험했으나 "둘 다 'watch mode'를 작동시키지 않는 것 같았다". "내가 다른 애플리케이션으로 전환한 뒤에도 Atlas는 계속 탐색했다." — [Simon Willison, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- [S] OpenAI 도움말 센터 문구(검색 요약, 페이지는 403): 로그아웃 모드에서 에이전트는 "기존 쿠키를 전혀 사용하지 않으며, 사용자의 구체적인 승인 없이는 어떤 온라인 계정에도 로그인되지 않는다". 에이전트는 "금융 기관 같은 특정 민감 사이트에서 행동할 때 사용자가 지켜보고 있는지 확인하기 위해 일시 정지한다". OpenAI의 Atlas 관련 글에서 watch mode 대상으로 명시된 사이트 범주는 내가 찾은 한 금융 기관뿐이다. 2025년 1월의 Operator 글은 "이메일 또는 금융 서비스"를 명시했다. — [OpenAI Help: Atlas agent mode](https://help.openai.com/en/articles/12628199-using-ask-chatgpt-sidebar-and-chatgpt-agent-on-atlas)
- [P] 로그인/로그아웃 전환에 대한 학술적 관찰: Atlas Agent Mode에서 개인화된 페이지에 접근하려면 "먼저 '로그인' 상태로 전환해야 했고, 이는 지속되는 설정이다"(시험 시기 2026년 1월 말~2월 초). — [Roesner & Kohlbrenner, ICLR 2026 workshop paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [S] ChatGPT agent(클라우드 가상 브라우저): "작업에 로그인이 필요하면 에이전트가 일시 정지하고 사용자에게 가상 브라우저를 직접 조작하도록 요청한다". 같은 도움말 글은 이제 ChatGPT agent가 "더 이상 제공되지 않는다"고 밝히며 ChatGPT Work와 별도의 클라우드 브라우저 안내서를 가리킨다. — [OpenAI Help: ChatGPT agent](https://help.openai.com/en/articles/11752874)

2025년 12월 강화 업데이트:
- [P-press] OpenAI는 "강화학습으로 훈련한" "LLM 기반 자동 공격자"를 만들었다. 이 공격자는 "수십(심지어 수백) 단계에 걸쳐 전개되는 정교하고 긴 호흡의 유해한 작업 흐름을 에이전트가 실행하도록 유도할 수 있다". 이 업데이트로 "새로 적대적 훈련을 거친 모델과 더 강력한 시스템 안전장치"가 배포되었다. OpenAI 인용: "우리는 프롬프트 인젝션을 장기적인 AI 보안 과제로 본다". "프롬프트 인젝션은 웹상의 사기나 사회공학과 마찬가지로 완전히 '해결'될 가능성이 낮다(Prompt injection, much like scams and social engineering on the web, is unlikely to ever be fully 'solved')". "'내 이메일을 검토하고 필요한 조치는 무엇이든 취하라' 같은 지나치게 포괄적인 프롬프트"는 피하라. "재량 범위가 넓으면 안전장치가 있더라도 숨겨진 콘텐츠나 악성 콘텐츠가 에이전트에 영향을 미치기 쉬워진다." 기사에 지표는 없다. — [eWeek, 2025-12-23](https://www.eweek.com/news/openai-chatgpt-atlas-security/)
- [P-press] 공격자는 대상 에이전트의 전체 추론 및 행동 기록을 돌려주는 시뮬레이터에서 인젝션을 시험하고("counterfactual rollout") 여러 라운드에 걸쳐 다듬는다. 공개된 예시(심어 둔 이메일 때문에, 사용자가 부재중 자동 응답을 요청했는데 에이전트가 사직서를 보내는 사례)는 가상의/내부 사례로 설명된다. — [CyberScoop, 2025-12-30](https://cyberscoop.com/openai-chatgpt-atlas-prompt-injection-browser-agent-security-update-head-of-preparedness/)
- [S] 대변인은 이 업데이트가 인젝션 성공을 측정 가능한 수준으로 줄였는지에 대해 답변을 거부했다(앞선 노트에 이미 있음). — [TechCrunch, 2025-12-22](https://techcrunch.com/2025/12/22/openai-says-ai-browsers-may-always-be-vulnerable-to-prompt-injection-attacks/)

Lockdown Mode와 Elevated Risk 라벨:
- [P-press] "보안 의식이 매우 높은 사용자를 위한 선택형 고급 보안 설정"으로 출시(2026년 2월 13일). 처음에는 ChatGPT Enterprise, Edu, Healthcare, Teachers 대상이었다. "실시간 네트워크 요청은 OpenAI가 통제하는 네트워크 밖으로 나가지 않으며, 브라우징은 캐시된 콘텐츠로 제한된다". 관리자는 Workspace Settings에서 전용 역할을 만들어 이를 켜고, 어떤 앱/행동을 계속 사용할 수 있게 할지 고를 수 있다. "Elevated Risk" 라벨은 ChatGPT, ChatGPT Atlas, Codex의 기능에 붙으며, OpenAI는 "보안 기술의 발전이" 위험을 "충분히 완화했다고 판단하면 'Elevated Risk' 라벨을 제거하겠다"고 밝힌다. — [Help Net Security, 2026-02-16](https://www.helpnetsecurity.com/2026/02/16/chatgpt-lockdown-mode-elevated-risk/)
- [S] 현재 도움말 센터 문구(검색 요약, 페이지는 403): 로그인한 개인 계정(Free, Go, Plus, Pro)과 셀프서비스 Business 워크스페이스에서 사용할 수 있으며 점진적으로 배포된다. "Agent mode는 비활성화된다". Deep research도 비활성화된다. Canvas가 생성한 코드에는 네트워크 접근을 승인할 수 없다. Finances in ChatGPT와 쇼핑 에이전트 기능은 사용할 수 없다. 발표문은 2026년 6월 4일에 갱신되었다. — [OpenAI Help: Lockdown Mode](https://help.openai.com/en/articles/20001061-lockdown-mode); [OpenAI announcement](https://openai.com/index/introducing-lockdown-mode-and-elevated-risk-labels-in-chatgpt/)

종료와 후속 제품:
- [P-press] 2026년 7월 9일: OpenAI는 ChatGPT Work를 발표하고 Atlas를 단계적으로 종료하기 시작했으며, Codex 데스크톱 앱을 내장 브라우저가 있는 통합 ChatGPT 데스크톱 앱으로 합쳤다. Chrome 확장 프로그램은 ChatGPT가 Chrome의 사이드 패널에서 동작하도록 업데이트되고 있다. ChatGPT Work에 대해 설명된 통제 수단: 진행 상황을 따라가며 특정 행동을 승인하는 기능, 연결된 도구가 관련된 중요한 행동을 실행 전에 OpenAI의 최고 성능 모델이 검토하는 "auto-review", 엔터프라이즈 관리자가 누가 접근할 수 있는지와 어떤 도구를 연결하는지, 어떤 행동을 허용하는지, 그리고 "클라우드 환경의 브라우저 및 네트워크 접근"을 관리하는 기능, ChatGPT Work의 대화와 행동을 조직 전체에서 볼 수 있게 하는 Compliance API. 기사는 내장 브라우저나 Chrome 확장 프로그램의 보안 세부 사항을 전혀 제시하지 않는다. — [PPC Land, 2026-07](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- [S] 지원 종료일 2026년 8월 9일은 OpenAI의 James Sun이 X에서 확인했다. Atlas는 지원이 종료되면 보안 업데이트를 받지 않는다. — [Cybernews](https://cybernews.com/ai-news/openai-shutters-atlas-ai/); [The Next Web, 2026-07-10](https://thenextweb.com/news/openai-chatgpt-atlas-browser-shutdown-superapp)

공개된 지표:
- [P-press] 찾을 수 있었던 OpenAI의 인젝션 수치는 GPT-5.5 시스템 카드(2026년 4월 23일)에 실린 커넥터 대상 모델 수준 "robustness score" 하나뿐이다. 0.963으로, GPT-5.4-thinking의 0.998에서 낮아졌다. 안전장치 켬/끔 구분이 없고 브라우저 표면도 없다. — [VentureBeat, 2026-06-01](https://venturebeat.com/security/anthropic-browser-agent-hijacked-31-percent-before-safeguards-engaged)

### 추론
- OpenAI의 구조적(모델에 의존하지 않는) 브라우저 통제 수단은 로그아웃 모드와 Lockdown Mode뿐이었다. 둘 다 로그인된 에이전트를 제약하는 것이 아니라 권한을 제거하는 방식(세션 없음 / 에이전트 없음)으로 작동한다. watch mode와 확인 절차는 모델이나 하니스가 "민감한" 페이지를 알아보는 데 의존하며, 유일한 독립 시험에서는 watch mode가 은행 사이트에서 작동하지 않았다.
- Lockdown Mode가 agent mode를 아예 꺼 버린다는 사실은, OpenAI가 고위험 사용자에 대해서는 로그인된 에이전트를 방어 가능하다고 보지 않는다는 암묵적 진술이다.
- Atlas가 사라졌으므로, 공개된 Atlas 관련 취약점(6절)은 단종된 제품을 설명한다. 이제 그것들의 의미는 설계상의 교훈이자, ChatGPT Work의 브라우저에 재사용된 같은 에이전트 스택에 대한 증거라는 데 있다.

### 공백
- OpenAI의 1차 문서는 읽지 못했다(모든 경로에서 403). "rapid response loop"라는 문구, watch mode 대상 사이트 범주의 전체 목록, 메모리 제어의 세부 사항("page visibility", 사이트별 메모리 제외), Atlas에서 정확히 어떤 행동에 확인이 필요했는지는 검증되지 않았다.
- 2025년 12월 업데이트 전이든 후든, OpenAI가 공개한 Atlas agent mode 공격 성공률은 없다.
- ChatGPT 데스크톱 앱 내장 브라우저와 ChatGPT Chrome 확장 프로그램의 보안 모델(프로필, 쿠키, 확인 절차, 관리자 정책): 찾지 못했다. Chrome 통합에 관한 TechRepublic 기사(제목 "...but There's a Security Trade-Off")는 403을 반환했다.
- LayerX의 "Tainted Memories"와 NeuralTrust의 옴니박스 문제가 Atlas 종료 전에 수정된 적이 있는지: 출처를 찾지 못했다.

## 2. Anthropic — Claude in Chrome (2026년 권한 모델과 공개된 수치)

### 핵심
앞서 있던 상충은 해소되었다. 현재 도움말 페이지는 성인 사이트와 알려진 불법 복제 사이트만 기본 차단되고 금융 사이트는 먼저 묻는 방식이라고 밝힌다(이전 버전과 2026년 1월의 Wiz 검토는 뱅킹을 차단 대상으로 적고 있으므로 정책이 완화된 것이다). 직접 읽은 Anthropic의 자체 시스템 카드는 "auto mode"를 켠 상태에서 Opus 5.5(2026년 9월)는 브라우저 환경 110개 중 공격 성공 0건, Opus 5(2026년 7월)는 0/129를 보고하며, 안전장치 없는 모델 자체는 시도당 각각 0.09%와 3.84%다. 그러나 같은 확장 프로그램에서 2026년에 서로 다른 경계 버그 네 건이 공개되었고, 이 수치들은 그것을 측정하지 않는다.

### 인용 근거
권한 모델과 사이트 범주:
- [P] 기본 차단 범주: "성인 콘텐츠 웹사이트"와 "알려진 불법 복제 콘텐츠 사이트". "Claude는 금융 사이트에 접근하기 전에 허락을 구한다." 나열된 위험: 프롬프트 인젝션("위험은 0이 아니다(The risk is not zero)"), 활성 탭에 보이는 모든 것을 담는 스크린샷, 규제 대상 데이터(HIPAA 활성화 조직에서는 기본적으로 꺼져 있고 BAA 적용 대상이 아니다). 권고: 민감한 작업에는 "Manually approve"를 쓰고, 뱅킹/의료/정부 로그인이 없는 별도 브라우저 프로필을 쓰며, 세션이 기록에 저장되므로 민감한 페이지에서는 사이드 패널을 열지 말 것. 페이지에는 "Updated yesterday"라고 표시된다. — [Claude Help: Use Claude in Chrome safely](https://support.claude.com/en/articles/12902428-use-claude-in-chrome-safely)
- [P] 같은 도움말 페이지에는 Claude Opus 4.8이 알려진 기법들을 결합한 내부 시험에서 공격 성공률을 "0.08% 미만"으로 낮춘다는 문장이 아직 남아 있다. Opus 5/5.5 카드보다 앞선 예전 수치다. — [Claude Help](https://support.claude.com/en/articles/12902428-use-claude-in-chrome-safely)
- [P-press] 비교용 예전 정책: Wiz의 2025년 검토는 Claude in Chrome을 "뱅킹 및 성인 사이트를 차단하고 사이트 단위 권한을 사용한다"고 적으며 아키텍처 수준의 격리는 없다고 한다. — [Wiz, 2026-01-16](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review)
- 세 가지 모드(Manual / Auto / Skip), 항상 묻는 행동, 금지된 행동은 앞선 노트에 있으며 다시 읽지 않았다. — [Claude Help: permissions](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)

관리자 통제:
- [P] Team/Enterprise: "Enable for your team" 토글(Organization settings > Claude in Chrome). 허용할 사이트의 허용 목록. "Claude의 기본 차단 범주에 더해" 적용되는 차단 목록. Cowork 사이드 패널 활성화(Enterprise 소유자). "1Password for Claude" 통합, 기본적으로 꺼짐(macOS). Enterprise 사용자 지정 역할의 역할별 기능 설정. 확장 프로그램을 한 조직에 고정하는 Chrome 엔터프라이즈 정책 `forceLoginOrgUUID`. 기본값: Team은 기본 활성화, Enterprise는 이미 비활성화해 두지 않았다면 기본으로 켜짐, HIPAA 활성화 Enterprise는 Owner가 켜고 특정 사이트를 허용하기 전까지 꺼짐. 집행 방식의 세부: 차단된 URL을 직접 입력하면 페이지는 여전히 로드되지만, 배너가 표시되고 그 페이지에서는 Claude의 도구가 비활성화된다. 제로 데이터 보존(Zero data retention)은 지원되지 않는다. — [Claude Help: Claude in Chrome admin controls](https://support.claude.com/en/articles/13065128-claude-in-chrome-admin-controls)
- [S] Claude in Chrome 대화 기록이 2026년 9월 18일 Compliance API의 로컬 세션 엔드포인트에 추가되었다고 한다(베타, Enterprise). 제3자 기사이며 Anthropic 문서와 대조해 검증하지 않았다. — [IntuitionLabs](https://intuitionlabs.ai/articles/claude-chrome-compliance-api-pharma)

시스템 카드 수치(PDF를 직접 읽음):
- [P] Opus 5.5 시스템 카드(2026년 9월 22일), §5.2.2.3 Browser use, Table 5.2.2.3.A(89–91쪽): 비공개로 따로 둔(held-out) 환경 110개, 전문 레드팀이 만든 공격으로 "먼저 Claude Opus 4.7을 상대로 시험하며 생성한 뒤 평가 대상 모델에 적용"했고, 시나리오당 10회 시도, Claude Cowork 제품 하니스에서 실행, 프로그램에 의한 성공 판정. 안전장치 없는 경우의 공격 성공률(시도 기준 / 시나리오 기준)과 auto mode를 켠 경우: Opus 5.5 0.09% (1/110) → 0% (0/110), Fable 5.1 2.55% (6/110) → 0%, Opus 5 3.64% (15/110) → 0%, Sonnet 5 0.37% (3/110) → 0%. "auto mode를 켠 상태에서는 110개 시나리오 전체에서 Claude Opus 5.5에 대해서도, 우리가 평가한 다른 어떤 모델에 대해서도 성공한 공격이 없었다(With auto mode enabled, across all 110 scenarios, no attack succeeded against Claude Opus 5.5 or against any other model we evaluated)." "안전장치 없음" 구성은 "Claude Cowork 제품에는 존재하지 않는다"(프로브는 항상 동작한다). Opus 5.5의 단 한 건의 성공은 "폴백 이후 Claude Opus 4.8이 실행한 것이며, 폴백은 시도의 8%에서 발생했다". — [Claude Opus 5.5 System Card](https://anthropic.com/claude-opus-5-5-system-card)
- [P] 같은 카드, 안전장치의 정의: "프롬프트 인젝션 프로브는 ... 모델이 도구 결과에 따라 행동하기 전에 그 결과를 검사한다". auto mode는 "이 프롬프트 인젝션 프로브를, 위험할 수 있는 도구 호출을 차단하는 분류기와 짝지은 것이다 ... 이 안전장치들은 서로 다른 지점에서 작동한다. 전자는 모델로 들어오는 데이터에, 후자는 모델이 수행하는 행동에 작용한다. 따라서 공격이 성공하려면 둘을 각각 따로 무력화해야 한다(so an attack would have to defeat both independently to succeed)." — [Claude Opus 5.5 System Card](https://anthropic.com/claude-opus-5-5-system-card)
- [P] 같은 카드, 브라우저 공격에 대한 단서: "추가 안전장치가 없으면 이 공격들은 우리의 최신 모델에도 여전히 성공한다는 것을 확인했다". 그리고 정적 벤치마크에 대해서는: "알려진 공격의 고정된 데이터셋은 잘못된 안도감을 줄 수 있다(Fixed datasets of known attacks can provide a false sense of security)". 브라우저 세트는 전이된 공격(Opus 4.7을 상대로 최적화)으로, 공격자가 시험 대상 모델을 상대로 최적화하는 코딩 및 GUI 평가와 다르다. — [Claude Opus 5.5 System Card](https://anthropic.com/claude-opus-5-5-system-card)
- [P] 같은 카드, 대조용 다른 표면: Gray Swan IPI 벤치마크, 제품 보호 장치 없는 모델: Opus 5.5 0.1% (k=1), 0.7% (k=10), 1.0% (k=15), 이에 비해 Opus 5는 0.4% / 3.6% / 4.8%. k=15에서 설정별로는 GUI 컴퓨터 사용 2.8%, 코딩 0.5%, 도구 사용 0.4%. 적응형 Shade 공격자를 쓴 GUI 컴퓨터 사용(14개 사례, 각 200회 시도): Opus 5.5는 안전장치 없이 0.07%(시나리오 1/14), 프로브가 있으면 0.04%. 적응형 Shade 공격자를 쓴 코딩: Opus 5.5는 프로브가 있는 상태에서 11.13%(Fable 5.1 8.70%, Opus 5 19.53%, Sonnet 5 15.76%). 성공은 모두 Opus 4.8 폴백이 처리한 요청에서 나왔고, 그 경우의 성공률은 85.73%였다. — [Claude Opus 5.5 System Card](https://anthropic.com/claude-opus-5-5-system-card)
- [P] Opus 5 시스템 카드(2026년 7월 24일), §5.2.2.4, 환경 129개, 10회 시도, Cowork 하니스, 중간 수준의 thinking effort. 안전장치 없음 → auto mode 켬: Opus 5 3.84% (11/129) → 0% (0/129), Opus 4.8 11.15% (26/129) → 0.0% (0/129), Sonnet 5 0.47% (5/129) → 0%, Mythos 5 7.80% (29/129) → 0%, Fable 5 14.75% (43/129) → 0.25% (3/129). — [Claude Opus 5 System Card](https://www.anthropic.com/claude-opus-5-system-card)
- [P-press] VentureBeat가 보도한 Opus 4.8 시스템 카드(2026년 5월 28일): 브라우저 표면, 비공개 환경 129개, 10회 시도. 안전장치 없이 시도당 31.5%, 안전장치가 있으면 0.5%. Sonnet 4.6은 안전장치 없이 50.7%. 이 수치는 이후 Opus 5 카드가 Opus 4.8에 대해 보고하는 11.15%와 다른데, 뒤의 카드가 Cowork 하니스로 바꾸었기 때문이다("이전 시스템 카드와 달리, 이제 우리는 Claude Cowork 제품 하니스에서 실행되는 모델을 평가한다"). — [VentureBeat, 2026-06-01](https://venturebeat.com/security/anthropic-browser-agent-hijacked-31-percent-before-safeguards-engaged); [Claude Opus 5 System Card](https://www.anthropic.com/claude-opus-5-system-card)

확장 프로그램 아키텍처에 대한 독립 관찰:
- [P] Claude for Chrome은 "다른 것들과 아키텍처가 근본적으로 다르다. Chrome용 브라우저 확장 프로그램으로 구현되어 있다(모든 도메인의 콘텐츠에 접근하는 권한을 가진다)". 일곱 개 에이전트 중 페이지에 JavaScript를 주입하는 유일한 에이전트였고, 교차 출처(cross-origin) iframe 안에도 주입했다. 또한 (Atlas agent mode와 함께) DOM을 통해 마스킹된 비밀번호 필드의 내용을 읽은 두 에이전트 중 하나였다. 저자들의 사용자 권고는 이 제품을 따로 지목한다. Claude for Chrome은 "특별히 사용하지 말 것을 권고한다(is specifically advised against)". 논문 작성 시점까지 Anthropic은 제보에 답하지 않았다. — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf); [project page, updated 2026-04-15](https://agent-security.cs.washington.edu/agentic_browsers_sop.html)

### 추론
- 연속된 세 카드(2026년 5월 → 7월 → 9월)에 걸친 추세: 모델 자체의 브라우저 공격 성공률(ASR)은 수십 퍼센트에서 0.1% 미만으로 떨어졌고, 안전장치를 켠 ASR은 0.5%에서 0/110으로 떨어졌다. 그러나 브라우저 시험 세트는 고정되어 있고, 예전 모델에서 전이된 것이며, 규모가 작다(110개 시나리오 × 10회 시도). 0/1,100이라는 결과는 시도당 비율의 상한을 대략 0.3%(95%)로 잡아 줄 뿐 0으로 잡아 주지 않으며, 새로운 적응형 공격자에 대해서는 아무것도 말해 주지 않는다.
- 이 수치는 "auto mode"에 대한 것이다. "Skip all approvals" 모드에서는 행동 분류기가 루프에 들어 있지 않고(Cowork에서 항상 켜져 있는 프로브만 있다), 기존 사이드 패널에서는 구성이 다를 수 있다. 카드는 이 모드들을 보고하지 않는다.
- Opus 4.8로의 폴백 메커니즘은 새로 문서화된 약한 고리다. Opus 5.5의 유일한 브라우저 성공과 모든 코딩 성공이 폴백이 처리한 요청에서 발생했다.
- Anthropic이 측정한 위협(페이지 콘텐츠 → 모델)은 2026년의 확장 프로그램 경계 버그(다른 확장 프로그램이나 웹 출처 → 확장 프로그램 메시징, 6절)와 직교한다. 모든 도메인 권한, DOM 읽기, JS 주입을 가진 확장 프로그램은 그 경계가 무너질 경우 조사한 설계 가운데 피해 반경이 가장 크다.

### 공백
- 권한 페이지(모드, 금지된 행동)는 이번 세션에서 다시 읽지 않았고 앞선 노트에서 가져왔다.
- ClaudeBleed의 "privileged mode" 우회나 Manifold의 발견에 대한 Anthropic의 입장은 찾지 못했다.
- Compliance API/감사 로그가 Claude in Chrome의 행동을 포함하는지는 Anthropic 문서로 검증하지 못했다.
- 시스템 카드는 Manual 또는 Skip 모드의 브라우저 ASR이나, Cowork 하니스 밖에서 실행되는 확장 프로그램의 브라우저 ASR을 보고하지 않는다.

## 3. Google Chrome — 2025년 12월 설계가 2026년 동안 어떻게 발전했는가

### 핵심
auto browse는 2026년 1월 28일 미국에서 미리보기로 출시되었고(Gemini 3, AI Pro/Ultra), 선택적으로 Google Password Manager 로그인을 쓸 수 있으며 구매나 소셜 게시 전에 일시 정지한다. 2026년 4월에는 새 정책 세 개와 함께 Workspace/엔터프라이즈에 도달했고, 관리 대상 사용자에게는 기본적으로 꺼져 있다. 읽기/쓰기 Agent Origin Set 게이팅이 완전한 형태로 출시되었다고 밝히는 2026년의 Google 발표물은 찾지 못했고, 공개된 효과 데이터도 없다. 2026년 초의 독립 시험에서는 Gemini in Chrome이 여전히 교차 출처 iframe 콘텐츠를 읽는 것으로 나타났다.

### 인용 근거
- [P] 출시 글(2026년 1월 28일): auto browse는 "사용자를 대신해 여러 단계의 번거로운 일을 처리한다". 허락을 받으면 로그인에 Google Password Manager를 쓸 수 있다. 구매나 소셜 미디어 게시 같은 민감한 행동 전에는 확인을 위해 일시 정지한다. Chrome은 에이전트 커머스를 위한 Google의 Universal Commerce Protocol(UCP)을 지원할 예정이다. 미국에서 Google AI Pro 및 Ultra 구독자를 대상으로 Windows, macOS, Chromebook Plus에서 미리보기로 제공된다. 보안은 "엄격한 보안 기준(rigorous security standards)"과 "완전히 새로운 방어책(entirely new defenses)"이라고만 설명된다. — [Google: Gemini 3 auto browse, 2026-01-28](https://blog.google/products-and-platforms/products/chrome/gemini-3-auto-browse/)
- [P] 엔터프라이즈 관리자 페이지(운영 중, ©2026): 정책 `GeminiSettings`, `GenAiDefaultSettings`(0 허용 및 모델 개선에 사용 / 1 모델 개선 없이 허용 / 2 허용 안 함), `GeminiActOnWebSettings`(auto browse를 제어), `GeminiActOnWebAllowedForURLs`와 `GeminiActOnWebBlockedForURLs`(auto browse용 URL 허용/차단 목록), `GeminiSparkSettings`(사용자가 Chrome을 Gemini Spark에 연결할 수 있게 하며, Gemini Spark는 auto browse를 호출할 수 있다). auto browse는 "관리 대상 사용자에게 기본적으로 꺼져 있다(off by default for managed users)". 대상 에디션에는 Business Standard/Plus, Enterprise Standard/Plus, Gemini Education Premium, Google AI Pro for Education, AI Ultra Access, AI Expanded Access가 포함된다. 프롬프트, 탭 컨텍스트, 응답은 허락 없이 도메인 밖의 모델 훈련에 쓰이지 않는다. 기존 Workspace 데이터 보호 통제가 적용된다. 이 페이지는 auto browse의 로깅/감사나 DLP를 다루지 않는다. — [Chrome Enterprise Help: Gemini in Chrome](https://support.google.com/chrome/a/answer/16291696)
- [S] Chrome Enterprise 릴리스 노트: auto browse는 Chrome 144에서 일부 비엔터프라이즈 사용자에게 제공되었고, `GeminiActOnWebSettings`는 출시 시점부터 사용할 수 있었다. 미국의 대상 Workspace 사용자에 대한 엔터프라이즈 제공은 2026년 4월 22일 Cloud Next에서, Chrome Enterprise Premium의 확장된 섀도 IT 탐지와 함께 발표되었다. — [Chrome Enterprise release notes](https://support.google.com/chrome/a/answer/7679408); [TechCrunch, 2026-04-22](https://techcrunch.com/2026/04/22/google-turns-chrome-into-an-ai-coworker-for-the-workplace/)
- [P] 독립적인 동작 확인(2026년 1월 말~2월 초): Chrome with Gemini는 요약할 때 동일 출처 및 교차 출처 iframe 콘텐츠에 접근했다. 다른 탭은 사용자 허락이 있어야만 포함할 수 있었다. 마스킹된 입력이나 숨겨진 요소는 읽지 않았다. 단순한 시험에서는 페이지에 삽입된 지시를 따르지 않았다. 텍스트 입력은 1월 말 auto browse 업데이트 이후에야 가능해졌다. 저자들은 인젝션이 성공하면 Chrome with Gemini(auto browse)에 교차 출처 데이터 탈취와 행동 위조의 전제 조건이 존재한다고 결론짓는다. Google은 제보에 "사려 깊게 응답했다". — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf); [project page](https://agent-security.cs.washington.edu/agentic_browsers_sop.html)
- [P] Gemini 사이드 패널 경계 버그, CVE-2026-0628(High): 기본 권한만 가진 확장 프로그램이 `declarativeNetRequest`를 사용해, Chrome의 Gemini 패널 안에 로드된 Gemini 웹 앱에 JavaScript를 주입하고 패널의 권한을 물려받았다(카메라/마이크, HTTPS 탭의 스크린샷, 로컬 파일/디렉터리 읽기, 신뢰받는 패널 안의 피싱 UI). 2025년 10월 23일 제보, 2026년 1월 초 수정, 2026년 3월 2일 공개. — [Unit 42, 2026-03-02](https://unit42.paloaltonetworks.com/gemini-live-in-chrome-hijacking/)
- [P-press] 수정 버전 143.0.7499.192, CVSS 8.8, 포상금 $7,000. — [CSA research note, 2026-09-22](https://labs.cloudsecurityalliance.org/research/csa-research-note-bragjack-browser-ai-agent-hijack-20260922/)
- [S] VRP: 문서화된 규칙은 여전히, 에이전트 보안 경계 침해(악의적 행동, 데이터 탈취 또는 우회로 이어지는 간접 인젝션)에 최대 $20,000를 지급한다는 2025년 12월의 발표뿐이다. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/); [eWeek](https://www.eweek.com/news/google-bounty-chrome-ai-security-breach/)
- [P-press] Gemini 3.x 모델 카드에는 인젝션 수치가 없다. Google의 Frontier Safety Framework 보고는 프롬프트 인젝션을 하나의 영역으로 다루지 않는다. — [VentureBeat, 2026-06-01](https://venturebeat.com/security/anthropic-browser-agent-hijacked-31-percent-before-safeguards-engaged)
- [P-press] 소규모 수동 시험(Berkeley 캡스톤, 2026년 6월): Gemini는 프롬프트 인젝션 항목에서 0.78점을 받았고(다섯 에이전트: Claude 1.00, Atlas 0.95, Copilot 0.81, Gemini 0.78, Comet 0.67), 인젝션 한 건이 성공해 "계층적 프롬프트 인젝션 방어를 갖췄다는 Google의 주장과 모순된다". — [CLTC AgentWatch white paper, 2026-06](https://cltc.berkeley.edu/wp-content/uploads/2026/06/AgentWatch.pdf)

### 추론
- Chrome의 엔터프라이즈 통제 표면(켬/끔 정책과 URL 허용/차단 목록)은 이제 Edge의 승인 사이트 목록이나 Claude의 허용/차단 목록과 종류 면에서 동등하다. 차이는 Chrome의 것이 확장 프로그램이 받아 오는 조직 설정이 아니라 브라우저 바이너리가 집행하는 브라우저 정책이라는 점이다.
- 교차 출처 iframe 결과는, 2026년 초 기준으로 출처 게이팅이 실제로는 여전히 에이전트가 무엇을 읽을 수 있는지보다 어디서 행동할 수 있는지(읽기-쓰기 집합)를 더 제한했음을 시사한다. 이는 처음에는 읽기-쓰기 가능 집합만 추적했다는 Google 자신의 2025년 12월 진술과 일치한다.
- "Gemini Spark"가 auto browse를 호출한다는 것은 같은 에이전트로 들어가는 두 번째 진입점이 있다는 뜻이다. 정책은 둘 다 다루어야 한다.

### 공백
- 읽기 전용 출처 집합 / 완전한 게이팅이 출시되었는지, User Alignment Critic에 변화가 있었는지, 측정된 효과가 있는지에 대한 2026년 Google 1차 출처를 찾지 못했다. 이는 표적 검색을 두 번 한 뒤의 증거 부재이지, 아무것도 발표되지 않았다는 확인이 아니다.
- 에이전트 관련 버그에 대한 공식 Chrome VRP 규칙 페이지(등급, 인젝션만 있는 제보가 대상이 되는지)는 찾지 못했다.
- `GeminiActOnWeb*`의 정책 값 정의와 기본값은 읽지 못했다(정책 목록 페이지가 렌더링되지 않았다).
- auto browse의 확인 절차를 프롬프트 인젝션으로 우회했다는 연구자의 공개 제보는 찾지 못했다.
- 결제 흐름의 세부 사항(UCP, 누가 확인하는지, 가상 카드)은 깊이 조사하지 않았다.

## 4. Edge, Comet, Brave, Opera Neon, Dia — 문서화된 설계와 그 차이

### 핵심
제품들은 세 가지 격리 철학으로 나뉜다. Brave는 프로필로 격리하고(에이전트는 사용자의 실제 세션을 결코 보지 못한다) 콘텐츠를 보지 않는 정렬 검사기(alignment checker)를 추가한다. Edge는 사이트 목록으로 제약하고 기능을 제거한다(다운로드 없음, 자동 완성 없음, 비밀번호 없음, 권한 부여 없음). Comet, Atlas, Chrome, Claude는 사용자의 실제 프로필 안에서 실행되며 분류기와 확인 절차, 그리고 취약점 공개 이후 추가된 몇 가지 하드코딩된 차단에 의존한다. Opera와 Dia는 1차 문서가 가장 빈약하다.

### 인용 근거
Microsoft Edge:
- [P] Copilot Mode Actions 미리보기(2025년 10월 23일): Balanced Mode(기본값)는 Actions를 선별된 사이트 목록으로 제한하고 사용자가 다른 사이트를 승인할 수 있다. Strict Mode는 모든 사이트에 승인을 요구한다. 방어책: Azure Prompt Shields, 둘 다 "시험 중"인 "Spotlighting"과 "Task Tracker"(이탈 탐지), 사이트가 작업과 무관해 보일 때 묻는 관련성 검사, 이메일·뱅킹·건강 맥락에서 멈추고 묻기, 에이전트가 우회할 수 없는 SmartScreen 차단 페이지. 강제 제한: `edge://` 페이지나 탭 콘텐츠 바깥의 UI와 상호작용 불가, 외부 앱 실행(프로토콜 핸들러) 불가, 다운로드 불가, 파일/디렉터리 선택기 불가, Edge 밖의 데이터나 앱에 접근 불가, 컨텍스트 메뉴 불가, 사이트 권한(예: 카메라) 부여 불가, 탭 오디오 음소거. "모델이 비밀번호를 포함한 양식 자동 완성 데이터를 사용하는 기능을 비활성화했다(disabled the ability for the model to use form fill data, including passwords)". — [Microsoft Edge Blog, 2025-10-23](https://blogs.windows.com/msedgedev/2025/10/23/considerations-for-safe-agentic-browsing/)
- [P] Edge for Business(2025년 11월 18일): "Copilot Mode는 자동으로 활성화되지 않는다". Agent Mode는 IT 부서가 승인 사이트 목록과 함께 별도로 활성화해야 하며 "Agent Mode는 이 사이트들로 제한된다". "DLP와 사용 권한 제한 같은 기존 데이터 보호 정책을 준수한다". "저장된 비밀번호나 결제 수단에도 접근하지 않으며" "민감한 행동에서는 사용자의 명시적 개입을 위해 일시 정지한다". Microsoft 365 Copilot 라이선스가 필요하다. 비공개 미리보기 상태이며 공개 미리보기는 2026년 2월로 계획되었다. — [Microsoft Edge Blog, 2025-11-18](https://blogs.windows.com/msedgedev/2025/11/18/edge-for-business-presents-the-worlds-first-secure-enterprise-ai-browser/)
- [P] 독립 시험(2026년 초): Edge with Copilot(에이전트가 아닌 사이드 패널)은 iframe 콘텐츠를 모델에 전혀 노출하지 않았고 이는 아키텍처에 따른 것으로 보이지만, "여러 교차 출처 탭에 자유롭게 접근하는 유일한 브라우저 에이전트"였다. 페이지에서 행동하지는 않았다. — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [S] 2026년 명칭: 행동 기능은 Edge 152+에서 "Copilot Cowork"로 나타나며 정책 `CopilotCoworkToolActionsEnabled`가 있다(제3자 튜토리얼). 또 TechRepublic의 기사 제목은 Microsoft가 별도 모드로서의 "Copilot Mode"를 종료했다고 한다. 둘 다 Microsoft 문서로 확인하지 못했다. — [ElevenForum tutorial](https://www.elevenforum.com/t/enable-or-disable-copilot-cowork-to-automatically-take-actions-in-microsoft-edge-on-windows-11.48048/); [TechRepublic](https://techrepublic.com/article/news-microsoft-retires-copilot-mode-edge)

Perplexity Comet:
- [P] BrowseSafe(2025년 12월 2일): 웹 콘텐츠를 반환하는 도구는 신뢰할 수 없는 것으로 취급하고, 호출마다 병렬 탐지 파이프라인을 시작한다. 미세 조정한 분류기(기반 모델 Qwen-30B-A3B)가 콘텐츠를 검사하고, 불확실한 경우는 더 느린 최상위(frontier) LLM으로 넘긴다. 탐지된 사례는 재훈련에 쓰인다. 사용자 확인 및 도구 정책 집행과 함께 쓰인다(세부 내용은 없음). 보고된 F1은 약 0.91이며, GPT-5/Sonnet 4.5는 약 0.85, PromptGuard-2는 약 0.35다. 인정한 약점: 다국어 공격은 균형 정확도를 76.0%로 떨어뜨린다. 간접적/가정적 표현. 보이는 요소(바닥글, 표 셀)에 있는 인젝션은 주석/속성에 있는 것보다 탐지하기 어렵다. 무해한 교란 요소 세 개가 정확도를 90% 이상에서 약 81%로 떨어뜨린다. — [Perplexity: BrowseSafe, 2025-12-02](https://www.perplexity.ai/hub/blog/browsesafe)
- [P] 취약점 공개 이후의 강제 경계: Zenity의 제보 이후 Perplexity는 "에이전트가 `file://` 경로에 접근하지 못하도록 막는 코드 수준의 강제 경계를 추가했고"(사용자는 여전히 열 수 있다) 에이전트가 확인 절차를 더 엄격하게 따르도록 했다. `view-source:file:///` 우회 때문에 두 번째 패치가 필요했다(2026년 2월 11일). — [Zenity Labs, 2026-03-03](https://labs.zenity.io/p/perplexedbrowser-perplexity-s-agent-browser-can-leak-your-personal-pc-local-files)
- [P] 독립 시험(2026년 초): Comet은 동일 출처 및 교차 출처 iframe 콘텐츠를 읽었고, 보이는 사용자 입력을 읽었으며, 사용자 요청이 있으면 입력을 하되 "비밀번호는 입력하지 않았고", 마스킹된 입력은 읽지 않았으며, 탭/출처를 넘나들며 대화 기록을 공유했다("과거 대화와 방문한 페이지"를 바탕으로 답했다). — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [S] 제보에 대한 Perplexity의 태도: CometJacking은 보안 영향 없음으로 분류했다(LayerX, 2025년 10월). SquareX의 "숨겨진 MCP API"(`chrome.perplexity.mcp.addStdioServer`, 2025년 11월)는 "완전히 거짓(entirely false)"이라고 했으나, 조용한 업데이트 이후 PoC는 "Local MCP is not enabled"를 반환하게 되었다. BioShocking 제보는 완화 조치 없이 종결되었다. — [SecurityWeek on SquareX dispute](https://www.securityweek.com/squarex-and-perplexity-quarrel-over-alleged-comet-browser-vulnerability/); [CSA note on BioShocking](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/)
- [S] Perplexity는 2025년 10월 글에서 4계층 방어(1계층은 실시간 분류)를 설명하며, 출시 전에 Trail of Bits에 감사를 의뢰했다. 해당 글들은 404를 반환했고 2~4계층은 읽지 못했다. — [Perplexity: Mitigating prompt injection in Comet](https://hub-prod.perplexity.ai/hub/blog/mitigating-prompt-injection-in-comet)

Brave:
- [P] AI browsing(2025년 12월 10일 게시, 초기 시험을 위해 Nightly에서 모든 릴리스 채널로 옮겨 간 2026년 5월 5일에 갱신): 격리된 프로필에서 실행된다. 쿠키, 로그인 상태, 캐시는 일반 프로필에서 넘어오지 않는다. 정렬 검사기(두 번째 모델)는 시스템 프롬프트, 사용자 프롬프트, 작업 모델의 응답을 보며 원본 페이지 콘텐츠와는 "방화벽으로 분리"되어 있다. `brave://` 페이지, HTTPS가 아닌 페이지, Chrome Web Store, Safe Browsing이 경고한 사이트에는 접근할 수 없다. 사용자가 수동으로 시작해야 한다. 경고가 붙은 행동에는 명시적 허락이 필요하다. Brave는 사이트별 권한 프롬프트를 의도적으로 피한다. 메모리 제안은 사용자에게 표시되고 되돌릴 수 있다. AI는 자신의 세션 로그를 삭제할 수 없다. HackerOne 포상금 제도가 있으며, 초기 출시 기간에는 대상 범위에 드는 AI browsing 문제에 보상을 두 배로 준다. 인정한 사항: 가드레일은 "위험을 줄이는 데 도움이 될 수 있지만 없애지는 못한다". 신뢰할 수 없는 콘텐츠가 검사기를 무력화하는 데 쓰일 수 있다. "아직 진행 중인 작업(a work in progress)"이다. — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- [P] 독립 시험: Brave Leo(에이전트가 아닌 어시스턴트, 2026년 초)는 모델에 iframe 콘텐츠도, 숨겨진 요소도, 사용자 입력도 주지 않았고 페이지에서 행동할 수 없었다. — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [S] Black Hat USA 2026에서 Brave의 Artem Chaikin은 자신이 분석한 모든 브라우저가 프롬프트 인젝션에 취약했으며 완벽한 해법은 알려져 있지 않다고 말했다. — [Dark Reading](https://www.darkreading.com/application-security/no-perfect-fix-ai-browser-prompt-injection-flaws) (가져오기 시 403)

Opera Neon과 Dia:
- [P-press] Opera Neon: "Tasks"는 독립된 미니 브라우저다. 뱅킹 같은 고위험 페이지를 기본으로 차단한다. Dia: 도구 제한, 비밀번호와 민감한 UI를 AI에게 숨김, URL에서 지시를 제거. — [Wiz, 2026-01-16](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review)
- [P] Opera Neon에서 처음 공개된 결함은 단순한 숨김 텍스트 인젝션(불투명도 0인 span)으로, 사용자의 Opera 계정 이메일이 교차 출처로 유출되었다. 2025년 10월 21일 수정(6절). — [Brave, 2025-10-31](https://brave.com/blog/prompt-injection-flaw-opera-neon/)
- [S] Dia: Atlassian이 The Browser Company를 미화 6억 1,000만 달러(US$610M)에 인수하기로 합의했다. LayerX와 Akamai 백서는 Dia의 위험 중 하나로 간접 프롬프트 인젝션을 든다. Dia가 Chromium 샌드박스/사이트 격리를 비활성화한다는 Seraphic의 주장은 검증되지 않았다. — [TechRadar](https://www.techradar.com/pro/another-chrome-challenger-bites-the-dust-atlassian-is-buying-the-browser-company-for-usd610m); [Akamai white paper](https://akamai.com/resources/white-paper/dia-browser-risks-and-vulnerabilities)

### 추론
- 설계 비교(도출한 것): 

| 제품 | 에이전트 실행 위치 | 사용자의 실제 세션을 보는가 | 사이트 범위 제한 | 두 번째 모델 검사 | 자격 증명/자동 완성 |
|---|---|---|---|---|---|
| Chrome auto browse | 기본 프로필(또는 원격 브라우저) | 예 | 출처 집합 + 민감 사이트 목록, 관리자 URL 목록 | User Alignment Critic(메타데이터만) + 인젝션 분류기 | 사용자 확인 후 Password Manager가 입력, 모델은 비밀번호를 보지 못함 |
| Edge Agent Mode | 기본 프로필 | 예 | 선별된 목록(소비자용) / 관리자 승인 목록(기업용) | Prompt Shields, task tracker는 시험 중 | 양식 자동 완성, 비밀번호, 결제 수단 비활성화 |
| Claude in Chrome | 기본 프로필의 확장 프로그램 | 예 | 사이트별 사용자 승인, 관리자 허용/차단 목록, 기본 차단 범주 | 입력에 대한 프로브 + 도구 호출 분류기(auto mode) | 정책으로 금지, DOM 읽기로는 마스킹된 필드를 여전히 볼 수 있음 |
| Comet | 기본 프로필 | 예 | 기본적으로 없음, 2026년 1월부터 file:// 강제 차단 | BrowseSafe 분류기 + LLM 보조 검사 | 선택형 1Password 통합, 비밀번호는 입력하지 않음 |
| Atlas(종료됨) | 기본 프로필, 선택형 로그아웃 모드 | 선택 사항 | 일부 민감 사이트에서 watch mode | 적대적 훈련 모델 + 모니터 | 로그인은 사용자가 넘겨받음 |
| Brave AI browsing | 별도 프로필 | 아니오(사용자가 거기서 로그인하지 않는 한) | HTTPS만, 내부 페이지 불가 | 정렬 검사기(페이지 콘텐츠 없음) | 넘어오는 것 없음 |
| Opera Neon | 작업별 미니 브라우저 | 불분명 | 뱅킹 기본 차단 | 문서화되지 않음 | 문서화되지 않음 |
| Dia | 기본 프로필 | 예 | 도구 제한 | 문서화되지 않음 | 비밀번호를 모델에게 숨김 |

- Edge는 기능 부류 전체(다운로드, 권한 부여, 프로토콜 핸들러)를 코드에서 에이전트로부터 제거하는 유일한 주류 설계다. 이것이 Zenity가 주장하는 "강제 경계(hard boundary)" 접근이다.
- Brave가 사이트별 프롬프트를 쓰지 않기로 한 것과 Anthropic이 그것에 의존하는 것은 확인 피로에 대한 정반대의 베팅이다.

### 공백
- 2026년 Edge 에이전트 정책, 감사, Purview 통합에 관한 Microsoft Learn 문서는 읽을 수 없었다. 현재 제품 명칭은 확인되지 않았다.
- 확인 규칙, 자격 증명 처리, 엔터프라이즈 통제에 대한 Perplexity 자체 설명은 읽을 수 없었다(404).
- Opera나 The Browser Company/Atlassian의 1차 보안 문서는 읽지 못했다. 위 표의 Dia와 Neon 행은 2차 검토에 근거한다.
- Fellou, Genspark, Sigma: 벤더의 보안 문서를 찾지 못했다. 이들은 LayerX에 응답하지 않았다.

## 5. 브라우저 고유의 격리 선택지와 그 절충

### 핵심
배치 위치는 네 가지다. 기본 프로필, 별도 프로필, 로그아웃 상태, 원격 클라우드 브라우저. 얻는 보안은 잃는 유용성과 거의 정확히 같다. 에이전트의 가치가 사용자의 인증된 세션에서 나오기 때문이다. 기본 프로필 설계 안에서 결정적인 세부 사항은 모델이 교차 출처 프레임 콘텐츠를 받는지, DOM 접근인지 스크린샷 접근인지, 입력/행동을 할 수 있는지, 그리고 자격 증명을 모델 바깥의 브라우저 코드가 입력하는지다.

### 인용 근거
- [P] 핵심 구조적 발견: "가장 제한이 약한 경우 ... 악성 웹사이트가 프롬프트 인젝션에 성공할 수 있다면, 브라우저 에이전트를 이용해 동일 출처 정책을 우회할 수 있다(it can leverage the browser agent to circumvent the same-origin policy)". 저자들의 요약은 동일 출처 정책이 에이전트의 인젝션 방어만큼만 강해진다는 것이다. ChatGPT Atlas agent mode에서 양방향 모두 완전한 개념 증명을 했다(바깥 페이지가 교차 출처 프레임에서 훔침, 광고 같은 삽입 프레임이 바깥 페이지에서 훔침). — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [P] 제품별 결과(Table 1–3, 시험 시기 2026년 1월 말~2월 초). 교차 출처 iframe을 읽음: Atlas agent mode, Chrome with Gemini, Claude for Chrome, Comet — 예. Brave Leo, Edge Copilot, Firefox AI Mode, agent mode가 아닌 Atlas — 아니오. 다른 탭: Brave, Chrome, Claude는 사용자의 명시적 허락이 있을 때만. Edge는 자유롭게. Atlas agent mode와 Firefox는 아니오. Comet은 동일 출처만. 마스킹된(비밀번호) 입력을 읽을 수 있음: Atlas agent mode(로그인된 경우)와 Claude for Chrome뿐. 숨겨진 HTML 요소를 읽음: Atlas(두 모드 모두)와 Claude. JavaScript를 주입함: Claude for Chrome뿐. 탭/출처를 넘나들며 대화 기록을 공유함: Atlas, Chrome with Gemini, Comet. — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [P] 아키텍처 수준의 제한 대 모델 수준의 제한: Firefox AI Mode는 바깥 프레임의 텍스트를 담은, 사용자에게 보이는 생성된 프롬프트만 전달하며 "삽입된 프레임의 존재조차 배제한다". 이와 달리 Atlas의 비에이전트 모드 프레임 제한은 초기 실행에서 프레임 콘텐츠가 가끔 새어 나왔기 때문에 모델 가드레일처럼 보였다. — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [P] 교차 출처 메모리 오염에는 인젝션이 필요 없다. 출처를 넘나들며 대화 기록을 모으는 에이전트(Atlas, Gemini, Comet)는 한 사이트의 거짓 콘텐츠를 다른 사이트에 관한 이후 답변으로 옮길 수 있다. — [Roesner & Kohlbrenner paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [P] 별도 프로필: Brave의 에이전트 프로필은 일반 프로필과 쿠키, 로그인, 캐시를 공유하지 않는다. — [Brave: AI browsing](https://brave.com/blog/ai-browsing/). Anthropic은 같은 것을 사용자 실천 사항으로 권고하지만("뱅킹, 의료, 정부 계정에 접근할 수 없는 별도 브라우저 프로필을 사용하라") 강제하지는 않는다. — [Claude Help](https://support.claude.com/en/articles/12902428-use-claude-in-chrome-safely)
- [P-press] 로그아웃 모드(Atlas): 에이전트에게 자격 증명이 없다. OpenAI는 로그인 모드를 "매우 신뢰하는 사이트에서 범위가 잘 정해진 행동"용으로 자리매김했다. — [Simon Willison, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- [P] 원격 브라우저: Chrome의 도움말 페이지에 따르면 원격 브라우저는 기기를 닫은 뒤에도 작업을 계속할 수 있는 반면, 로컬 브라우저에서 Gemini는 "사용자가 로그인한 사이트를 포함해 사용자와 똑같은 사이트에 모두 접근할 수 있다"(앞선 노트). — [Chrome Help: auto browse](https://support.google.com/chrome/answer/16821166?hl=en)
- [P] 모델에게서 떼어 놓은 자격 증명과 자동 완성: Edge는 "모델이 비밀번호를 포함한 양식 자동 완성 데이터를 사용하는 기능"을 비활성화했고, Edge for Business에서는 "저장된 비밀번호나 결제 수단에 접근하지 않는다". Chrome은 사용자 허락을 받아 Google Password Manager로 입력한다. — [Edge Blog, 2025-10-23](https://blogs.windows.com/msedgedev/2025/10/23/considerations-for-safe-agentic-browsing/); [Edge Blog, 2025-11-18](https://blogs.windows.com/msedgedev/2025/11/18/edge-for-business-presents-the-worlds-first-secure-enterprise-ai-browser/); [Google, 2026-01-28](https://blog.google/products-and-platforms/products/chrome/gemini-3-auto-browse/)
- [P-press] 반례: Guardio의 2025년 8월 시험에서 Comet은 일부 실행에서 가짜 쇼핑몰에 사용자의 저장된 주소와 카드 정보를 묻지 않고 자동 입력했다. — [Guardio Labs "Scamlexity", 2025-08-20](https://guard.io/labs/scamlexity-we-put-agentic-ai-browsers-to-the-test-they-clicked-they-paid-they-failed)
- [P] 비밀번호 관리자 통합은 관리자가 통제하는 기능으로 존재한다. "1Password for Claude", 기본적으로 꺼짐. — [Claude Help: admin controls](https://support.claude.com/en/articles/13065128-claude-in-chrome-admin-controls)
- [P] `file://`과 로컬 접근: Comet의 에이전트는 코드 수준 차단(2026년 1월 23일)과 `view-source:file://`에 대한 두 번째 패치(2026년 2월 11일)가 있기 전까지, 웹 페이지에서 로컬 파일로 유도되어 URL 매개변수를 통해 파일을 유출할 수 있었다. Chrome의 Gemini 패널 버그는 주입된 코드에 로컬 파일 및 디렉터리 읽기를 허용했다. Edge는 파일 선택기, 다운로드, 그리고 Edge 바깥의 모든 것을 차단한다. — [Zenity Labs](https://labs.zenity.io/p/perplexedbrowser-perplexity-s-agent-browser-can-leak-your-personal-pc-local-files); [Unit 42](https://unit42.paloaltonetworks.com/gemini-live-in-chrome-hijacking/); [Edge Blog](https://blogs.windows.com/msedgedev/2025/10/23/considerations-for-safe-agentic-browsing/)
- [P-press] 확장 프로그램 / 특권 채널 경계: Forever Security가 조사한 다섯 제품 모두에서, 에이전트는 설치된 다른 확장 프로그램이 사칭하거나 변조할 수 있는 웹 출처 또는 확장 프로그램 채널에서 명령을 받는다(`declarativeNetRequest`를 이용한 헤더 제거/리디렉션, 보호가 약한 신뢰 대상 테스트 서브도메인, 내부 플래그에 대한 경쟁 상태, 콘텐츠 스크립트 제한 누락, 또는 `isTrusted`가 아닌 합성 클릭의 수용을 통해). — [CSA research note on BragJack, 2026-09-22](https://labs.cloudsecurityalliance.org/research/csa-research-note-bragjack-browser-ai-agent-hijack-20260922/); [BleepingComputer, 2026-09-19](https://www.bleepingcomputer.com/news/security/bragjack-attacks-hijack-ai-browser-agents-through-malicious-extensions/)
- [S] 로컬 명령 실행 채널: SquareX는 Comet의 내장 확장 프로그램이 로컬 stdio 서버를 시작할 수 있는 MCP API를 노출한다고 보고했다. Perplexity는 사용자 동의가 필요하고 로컬 MCP가 활성화되어 있어야 한다고 말한다. — [SecurityWeek](https://www.securityweek.com/squarex-and-perplexity-quarrel-over-alleged-comet-browser-vulnerability/)
- [P-press] Zenity의 관점: "강제 경계는 에이전틱 AI 보안에서 유일하게 신뢰할 수 있는 예방 계층이다(Hard boundaries are the only reliable prevention layer in agentic AI security)". 먼저 강제 한계(파일 시스템, 네트워크 송신, 자격 증명, 코드 실행)를 정의하고, 모델 바깥에서 집행한 다음, 유연한 가드레일을 추가하라. — [Zenity, 2026-03-13](https://zenity.io/blog/security/hard-boundaries-agentic-ai)

### 추론
- 절충 요약: 기본 프로필 = 유용성은 전부 누리지만 SOP가 사실상 모델에 위임된다. 별도 프로필 / 로그아웃 = 기존 세션에 대해 SOP와 동등한 보호를 얻지만, 사용자가 에이전트 컨텍스트 안에서 다시 로그인하지 않는 한 에이전트는 계정에 묶인 작업을 할 수 없다(다시 로그인하면 그 사이트들에 한해 노출이 되살아난다). 원격 클라우드 브라우저 = 로컬 기기, 파일, 다른 세션을 보호하지만, 사용자가 원격에서 로그인한 세션은 여전히 노출되고 자격 증명이 벤더가 호스팅하는 브라우저를 거친다.
- 스크린샷만으로 인식하면 인젝션이 읽을 수 있는 것이 제한되지만(마스킹된 필드 없음, 숨겨진 DOM 없음) 에이전트가 시각 전용 인젝션에 노출된다. DOM 접근은 그 반대다. 둘을 섞은 제품(Claude, Atlas)은 두 위험을 모두 진다.
- 내가 읽은 어떤 벤더 문서에도 패스키는 언급되지 않았다. 자격 증명 중개는 비밀번호, 양식 자동 완성, 결제 수단에 대해서만 설명된다.
- 에이전트의 로컬 네트워크(RFC1918/localhost) 접근은 읽은 어떤 출처에서도 다루지 않는다. 차단된다고 문서화된 것은 `file://`과 내부 `chrome://` 유형 페이지뿐이다.

### 공백
- 에이전트가 페이지를 조작할 때 패스키/WebAuthn 프롬프트를 어떻게 처리하는지 문서화한 벤더는 없다.
- 에이전트의 localhost/인트라넷 주소 접근이나 Private Network Access와의 상호작용에 대한 출처가 없다.
- Chrome "원격 브라우저"의 쿠키/세션 범위(어떤 쿠키가 어떻게 복사되는지)는 내가 읽은 자료에 문서화되어 있지 않다.
- Claude in Chrome ↔ Claude Desktop/Claude Code 사이 네이티브 메시징 호스트의 신뢰 문제는 관리자 토글 이상으로 조사하지 않았다.
- Roesner & Kohlbrenner는 Brave Leo와 Edge Copilot을 비에이전트 형태로 시험했다. Brave AI browsing과 Edge Agent Mode는 독립적인 SOP 시험의 대상이 되지 않았다.

## 6. 출시된 브라우저 에이전트에서 공개된 취약점의 날짜별 타임라인, 2025–2026

### 핵심
2025년 2월부터 2026년 9월 사이에 출시된 브라우저 에이전트를 대상으로 연구자들의 서로 다른 취약점 공개가 최소 스무 건 있었다. 이들은 여섯 가지 근본 원인 부류로 나뉘며, 2025년 말부터 무게 중심이 "페이지 텍스트가 지시로 취급됨"에서 에이전트 주변의 전통적인 경계 버그(확장 프로그램 메시징, 신뢰 대상 출처, 로컬 파일 스킴)로 옮겨 갔다. CVE와 포상금이 있는 곳도 바로 그쪽이다. 실제 환경에서의 악용이나 사용자 피해 사례 가운데 1차 출처로 뒷받침되는 것은 찾지 못했다.

### 인용 근거
타임라인(따로 밝히지 않는 한 날짜 = 공개일, "부류" 문자는 아래에 정의):

| 날짜 | 제품 | 이름 / 연구자 | 기법 → 영향 | 벤더 대응 | 부류 | 표시 / 출처 |
|---|---|---|---|---|---|---|
| 2025년 2월 | OpenAI Operator | Johann Rehberger | GitHub 페이지의 숨겨진 지시 → 사용자 입력 없이 인증된 페이지에서 데이터 유출 | 검증되지 않음 | A | [P-press] [Wiz](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review) |
| 2025년 8월 20일 | Comet | "Scamlexity", Guardio | 가짜 Walmart 쇼핑몰: 일부 실행에서 에이전트가 구매하고 저장된 카드를 자동 입력함. 피싱 이메일: 에이전트가 링크를 열고 가짜 은행 로그인 입력을 도움. 숨김 텍스트가 있는 "PromptFix" 가짜 캡차 → 파일 다운로드 | 벤더 대응 보고된 바 없음 | E (+A) | [P-press] [Guardio](https://guard.io/labs/scamlexity-we-put-agentic-ai-browsers-to-the-test-they-clicked-they-paid-they-failed) |
| 2025년 8월 20일 (제보 7월 25일) | Comet | Brave | 숨겨진 Reddit 댓글 → Gmail에서 OTP 읽기 → 계정 탈취 | 7월 27일과 8월 13일 수정, 이후 Brave는 완전히 완화되지 않았다고 밝힘 | A | (앞선 노트) [Brave](https://brave.com/blog/comet-prompt-injection/) |
| 2025년 9월 | Gemini(브라우징 도구 포함) | "Gemini Trifecta", Tenable | 브라우징 도구를 통한 유출을 포함한 결함 세 건 | Google이 수정 | A | [S] [Wiz](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review) |
| 2025년 10월 | Comet | "CometJacking", LayerX | 조작된 URL 매개변수가 에이전트로 하여금 메모리/커넥터를 조회하고 Base64로 인코딩한 메일/캘린더를 유출하게 함 | Perplexity: 보안 영향 없음 | B | [P-press] [Wiz](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review) |
| 2025년 10월 21일 (제보 10월 1일) | Comet | Brave | 이미지 속 거의 보이지 않는 텍스트. 사용자가 페이지를 스크린샷으로 찍으면 추출된 텍스트가 지시로서 LLM에 전달됨 | 설명 없음 | A (시각) | [P] [Brave](https://brave.com/blog/unseeable-prompt-injections/) |
| 2025년 10월 21일 (제보 8월 20일) | Fellou | Brave | 페이지로 이동하기만 해도 보이는 텍스트가 지시로서 LLM에 전송됨 | 설명 없음 | A | [P] [Brave](https://brave.com/blog/unseeable-prompt-injections/) |
| 2025년 10월 31일 (제보 10월 14일) | Opera Neon | Brave | 불투명도 0인 span. 요약 요청 → 에이전트가 auth.opera.com을 방문해 계정 이메일을 교차 출처로 유출 | 10월 17일 "Not Applicable"로 종결, 10월 20일 재개, 10월 21일 수정, Opera 글 10월 23일 | A | [P] [Brave](https://brave.com/blog/prompt-injection-flaw-opera-neon/) |
| 2025년 10월 | Atlas | NeuralTrust | 옴니박스에서 URL 파싱에 실패하는 URL 모양 문자열이 신뢰받는 사용자 프롬프트로 실행됨 | 수정 여부 불명 | B | [S] [The Register](https://theregister.com/2025/10/27/openai_atlas_prompt_injection) |
| 2025년 10월 27일 | Atlas / ChatGPT | "Tainted Memories", LayerX | CSRF로 ChatGPT Memory에 숨겨진 지시를 기록함. 기기와 브라우저를 넘어 지속됨. PoC에서는 생성된 코드가 원격 페이로드를 가져오게 됨 | OpenAI에 제보, 수정 여부는 밝혀지지 않음 | C | [P] [LayerX via Akamai](https://www.akamai.com/blog/security/2025/oct/layerx-identifies-vulnerability-in-new-chatgpt-atlas-browser) |
| 2025년 10월 27일 | Atlas | LayerX 피싱 시험 | Atlas는 실제 유포 중인 악성 페이지 103개 중 6개를 막음(5.8%). Edge는 53%, Chrome은 47%. 앞선 시험에서 Comet과 Genspark는 7% | — | E | [P] [LayerX via Akamai](https://www.akamai.com/blog/security/2025/oct/layerx-identifies-vulnerability-in-new-chatgpt-atlas-browser) |
| 2025년 11월 (제보 11월 4일) | Comet | SquareX | 내장 확장 프로그램의 MCP API가 로컬 명령을 시작할 수 있음. 시연은 사이드로드한 유사 확장 프로그램을 사용 | Perplexity: "완전히 거짓(entirely false)". 조용한 업데이트로 PoC 무력화 | D/F | [S] [SecurityWeek](https://www.securityweek.com/squarex-and-perplexity-quarrel-over-alleged-comet-browser-vulnerability/) |
| 2025년 11월 | 여러 AI 브라우저 어시스턴트 | "HashJack", Cato | 정상 사이트 URL의 프래그먼트(# 뒤)에 넣은 지시 | 벤더별 대응은 검증되지 않음 | B | [P-press] [Wiz](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review) |
| 2025년 12월 | OpenAI Operator | "Task Injection"(Google 연구자) | 가짜 하위 작업(예: 다운로드를 유발하는 캡차)이 사용자 목표의 일부로 취급됨 | 검증되지 않음 | A | [P-press] [Wiz](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review) |
| 2026년 3월 2일 (제보 2025년 10월 23일) | Chrome의 Gemini 패널 | CVE-2026-0628, Unit 42 (Gal Weizman) | 확장 프로그램이 declarativeNetRequest로 특권 패널 안의 Gemini 앱에 JS를 주입 → 카메라/마이크, 탭 스크린샷, 로컬 파일 | 2026년 1월 초 수정(Chrome 143.0.7499.192), $7,000 | D (+F) | [P] [Unit 42](https://unit42.paloaltonetworks.com/gemini-live-in-chrome-hijacking/) |
| 2026년 3월 3일 (제보 2025년 10월 22일) | Comet | "PleaseFix"/"PerplexedBrowser", Zenity | 빈 줄 아래에 지시를 넣고 가짜 버튼과 가짜 system-reminder 블록을 둔 캘린더 초대("intent collision"). 에이전트가 로컬 파일을 읽고 URL 매개변수로 유출 | P1/치명적 등급. 2026년 1월 23일 `file://` 강제 차단. 1월 27일 `view-source:file://` 우회 발견. 2월 11일 패치. 2월 13일 검증 | A → F | [P] [Zenity Labs](https://labs.zenity.io/p/perplexedbrowser-perplexity-s-agent-browser-can-leak-your-personal-pc-local-files) |
| 2026년 3월 3일 | Comet + 1Password | Zenity(두 번째 경로) | 승인된 1Password 통합을 악용해 자격 증명을 가져옴 | 설명 없음 | F | [S] (앞선 노트) [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-pleasefix-agentic-browser-exploits-2026032/) |
| 2026년 3월 24일 | BrowserOS | "WebPromptTrap", Cato | 조작된 AI 요약이 GitHub 인가 흐름을 유도함 | 릴리스에서 수정 | A | [S] (앞선 노트) [Cato](https://www.catonetworks.com/blog/webprompttrap-new-indirect-prompt-injection-vulnerability/) |
| 2026년 3월 26일 (제보 2025년 12월 27일) | Claude in Chrome | "ShadowPrompt", Koi (Oren Yomtov) | 확장 프로그램이 모든 `*.claude.ai` 서브도메인의 프롬프트를 수용 + `a-cdn.claude.ai`의 Arkose 캡차 컴포넌트에 있는 DOM XSS → 방문한 어떤 페이지든 프롬프트를 몰래 주입 | 확장 프로그램 v1.0.41이 정확한 출처를 강제. Arkose XSS는 2026년 2월 19일 수정 | D | [P-press] [The Hacker News](https://thehackernews.com/2026/03/claude-extension-flaw-enabled-zero.html) |
| 2026년 5월 7–8일 (제보 4월 27일) | Claude in Chrome | "ClaudeBleed", LayerX | 확장 프로그램이 실행 컨텍스트가 아니라 claude.ai 출처를 신뢰함. 권한이 전혀 없는 확장 프로그램이 claude.ai에 메인 월드 스크립트를 주입해 프롬프트를 보냄. 승인 메시지 재전송과 DOM 조작으로 확인 절차 우회 → Gmail/Drive/GitHub 데이터, 메일 발송 | Anthropic은 "standard" 모드에서 원격 명령을 차단하는 검사를 추가(다른 보도에 따르면 v1.0.70). LayerX: 사용자에게 알리지 않고 확장 프로그램을 "privileged" 모드로 전환하면 우회 가능 | D | [P-press] [SecurityWeek, 2026-05-08](https://www.securityweek.com/vulnerability-in-claude-extension-for-chrome-exposes-ai-agent-to-takeover/) |
| 2026년 6월 24일 (제보 2025년 10월~2026년 1월) | Atlas, Comet, Claude, Fellou, Genspark, Sigma | "BioShocking", LayerX | 거짓 전제에 보상을 주는 게임/퍼즐 구도로 에이전트가 안전 규칙을 제쳐 두게 한 뒤, 인증된 GitHub에서 자격 증명을 복사하게 함 | OpenAI 수정(2025년 10월 30일 공개). Anthropic 패치는 LayerX가 효과 없다고 판단. Perplexity는 종결. 나머지는 무응답 | A (논리 수준) | [S] [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/); [eSecurity Planet](https://www.esecurityplanet.com/threats/bioshocking-ai-ai-browser-vulnerability-lets-attackers-bypass-guardrails/) |
| 2026년 7월 20일경 (제보 5월 21일) | Claude in Chrome | Manifold Security | 작업을 활성화하는 클릭의 출처를 검증하지 않음. 이후 여덟 차례 릴리스에 걸쳐 여전히 악용 가능하다고 함 | Anthropic의 입장은 찾지 못함 | D | [S] (앞선 노트) [IANS](https://www.iansresearch.com/resources/all-blogs/post/security-blog/2026/07/20/unpatched-claude-for-chrome-flaw-could-give-attackers-access-to-gmail--calendar) |
| 2026년 9월 16일 | Chrome Gemini, Comet, Edge Copilot, Opera Neon, Claude in Chrome | "BragJack", Forever Security (Gal Weizman) | 평범한 확장 프로그램 하나가 각 에이전트를 탈취함. Chrome(CVE-2026-0628, 위와 같음). Comet — 보호가 약한 신뢰 대상 테스트 서브도메인 때문에 콘텐츠 스크립트가 에이전트 확장 프로그램에 메시지를 보낼 수 있음 → 스크린샷, 방문 기록, 행동. Edge — 내부 플래그에 대한 경쟁 상태와 합성 클릭 → 인증된 사이트에서의 행동(CVE-2026-55945). Neon — Opera의 신뢰 대상 도메인에 콘텐츠 스크립트 제한이 없음 → 이메일 읽기. Claude — 합성(`isTrusted`가 아닌) 클릭이 미리 정의된 작업을 시작함 | 다섯 곳 모두 PoC를 "인정하고 조치했다(acknowledged and remediated)". Edge 수정 버전 150.0.4078.48. 포상금 합계 약 $20,000(Chrome $7,000, Comet $7,000, Anthropic $600). Perplexity, Opera, Anthropic은 권고문을 내지 않음 | D | [P-press] [CSA note](https://labs.cloudsecurityalliance.org/research/csa-research-note-bragjack-browser-ai-agent-hijack-20260922/); [BleepingComputer](https://www.bleepingcomputer.com/news/security/bragjack-attacks-hijack-ai-browser-agents-through-malicious-extensions/) |

근본 원인 부류:
- **A. 콘텐츠가 지시로 취급됨**(페이지 텍스트, 숨김 텍스트, 이미지, 초대, 퍼즐): Operator(2025년 2월, 12월), Comet(Brave ×2, PleaseFix의 진입점), Fellou, Opera Neon, BrowserOS, BioShocking, Gemini Trifecta.
- **B. URL / 옴니박스 / 매개변수 파싱**: CometJacking(쿼리 매개변수), Atlas 옴니박스(URL 파싱에 실패하면 프롬프트로 넘어감), HashJack(프래그먼트).
- **C. 메모리 지속 / CSRF**: Tainted Memories, 교차 출처 대화 메모리 오염 개념(Roesner & Kohlbrenner).
- **D. 확장 프로그램 / 신뢰 대상 출처 경계**: CVE-2026-0628, ShadowPrompt, ClaudeBleed, Manifold, BragJack(다섯 제품), SquareX MCP API.
- **E. 피싱 / 사기 취약성**: Scamlexity, LayerX의 피싱 차단 비교.
- **F. 로컬 파일 또는 비밀번호 관리자 접근**: PleaseFix의 `file://`과 `view-source:file://`, 1Password 경로, Gemini 패널의 로컬 파일 읽기.

- [P] SOP 연구의 취약점 제보 처리(일곱 벤더에 통지, 발표 60일 이상 전): Brave, Google, Microsoft는 "사려 깊게 응답했다". OpenAI와 Firefox는 처음부터 끝까지 완결된 프롬프트 인젝션이 없다는 이유로 받아들이지 않았다. 작성 시점까지 Anthropic의 답은 없었다. — [UW project page, updated 2026-04-15](https://agent-security.cs.washington.edu/agentic_browsers_sop.html)

### 추론
- 부류 A의 발견에는 대부분 모델/분류기 업데이트로 대응했고 다시 뚫리는 일이 잦았다. 부류 D와 F의 발견에는 코드 변경, 버전 번호, CVE, 포상금으로 대응했다. 업계는 후자만을 고전적 의미의 "취약점"으로 취급하며, 이는 OpenAI가 작동하는 인젝션이 없는 제보를 받아들이지 않은 이유도 설명한다.
- 2026년에 조사된 확장 프로그램형 또는 패널형 통합은 모두 같은 방식으로 실패했다. 명령 채널을 실행 컨텍스트가 아니라 웹 출처로 인증한 것이다. 이는 모델 품질과 무관한 설계 단일 문화(design monoculture, CSA 노트의 용어) 문제다.
- 수정의 질은 벤더마다 달랐다. Google과 Microsoft는 CVE와 버전이 명시된 수정을 냈다. Perplexity는 치명적인 항목은 수정했지만 다른 것들은 반박하거나 종결했다. Anthropic은 두 연구팀이 불완전하다고 평가한 수정을 내놓았고 가장 적은 포상금을 지급했다. 작은 벤더들(Fellou, Genspark, Sigma)은 응답하지 않았다.
- Comet의 `file://` 사례는 부류 A에서 부류 F로 이어지는 완전한 연쇄를 보여 주며, 결정론적 차단조차 두 번째 반복이 필요했음을 보여 준다(스킴 접두사 우회).

### 공백
- 열어 보지 못한 1차 글: Forever Security(BragJack), Koi(ShadowPrompt), CometJacking/ClaudeBleed/BioShocking에 대한 LayerX 자체 글(사이트가 이제 Akamai로 리디렉션된다), NeuralTrust, Cato HashJack, SquareX, Embrace The Red, Manifold, Opera의 보안 블로그. [S] 또는 [P-press]로 표시한 행은 그대로 인용하기 전에 다시 검증해야 한다.
- BioShocking의 발표일(2026년 6월 24일 대 6월 30일)과 제품 수(여섯 개 대 다섯 개 더하기 하나)가 2차 출처마다 엇갈린다.
- Anthropic의 BragJack 포상금은 $600로도, $500로도 보도되었다. 합계도 약 $20,000와 $20,500로 엇갈린다.
- HashJack의 벤더별 수정 날짜, 그리고 OpenAI가 옴니박스와 메모리 CSRF 문제를 수정했는지는 확인되지 않았다.
- CVE-2026-55945는 Microsoft의 권고문과 대조하지 않았다.
- 어떤 항목에 대해서도 1차 출처로 뒷받침되는 실제 환경에서의 악용이나 피해는 찾지 못했다. 그런 취지의 애그리게이터 주장은 사용하지 않았다.

## 7. 운영 환경의 안전장치를 켠 브라우저 에이전트에 대한 독립 평가

### 핵심
안전장치를 켠 상태의 출시된 브라우저 에이전트에 대한 독립 평가는 빈약하다. 일곱 제품에 대한 학술적 기능/SOP 연구 하나(정성적, 완전한 익스플로잇 한 건), 다섯 제품에 대한 학생 팀의 수동 채점표 하나, 보안 회사들이 경쟁 벤더 제품을 대상으로 한 시험들, 그리고 Anthropic 자체의 하니스 내 수치가 전부다. 운영 중인 에이전틱 브라우저에 대한 독립적이고 적응형이며 통계적 검정력을 갖춘 벤치마크는 내가 찾은 범위에는 없다.

### 인용 근거
- [P] Roesner & Kohlbrenner(UW, ICLR 2026 Workshop on Agents in the Wild, 시험 시기 2026년 1월 말~2월 초, 최신 안정 버전, macOS): 두 도메인으로 구성한 테스트 사이트에서 Brave Leo, Atlas(agent mode 사용/미사용), Chrome with Gemini, Claude for Chrome, Edge with Copilot, Firefox AI Mode, Comet을 대상으로 수동 시험. 결과: Atlas agent mode에서 완전한 교차 출처 탈취 PoC. Chrome(auto browse), Claude, Comet에는 전제 조건이 존재하며, 여기서는 단순한 "페이지가 시키는 대로 하라" 인젝션이 "모델 가드레일 때문에" 거부되었다. 저자들은 자신들의 목표가 "정교한 프롬프트 인젝션을 개발하는 것이 아니었다"고 밝힌다. — [paper](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf)
- [P] AgentWatch(UC Berkeley CLTC 백서, 2026년 6월, 석사 캡스톤, 저자 여섯 명): 깨끗한 사용자 프로필을 사용해 Atlas, Claude, Comet, Copilot, Gemini를 대상으로 사람 시험자가 수동으로 프롬프트를 입력. 작업별 0–1 채점 기준. 종합 점수(0–100): Claude 97.3, Atlas 93.6, Gemini 92.9, Comet 86.1, Copilot 77.7. 프롬프트 인젝션 항목: Claude 1.00, Atlas 0.95, Copilot 0.81, Gemini 0.78, Comet 0.67. 밝힌 한계: 수동 시험이라 라운드 수가 적고 "통계적 유의성이 떨어질 수 있는 결과"이며, 평가자 간 편차가 있고, 모델이 고정되어 있지 않다. Atlas는 뻔한 인젝션에는 저항했지만 "간접 프롬프트 인젝션 시나리오에서 약점"을 보였다. — [CLTC AgentWatch](https://cltc.berkeley.edu/wp-content/uploads/2026/06/AgentWatch.pdf)
- [P] LayerX(보안 벤더)의 피싱 노출 시험, 2025년 10월: 실제 유포 중인 악성 페이지 103개. Atlas는 97개를 통과시켰고(5.8% 차단), Edge는 53%, Chrome은 47%를 차단했다. 앞선 실행에서 Comet과 Genspark는 7%. 이는 브라우저 셸에 내장된 피싱 방지 기능을 측정한 것이지 에이전트 탈취를 측정한 것이 아니다. — [LayerX via Akamai, 2025-10-27](https://www.akamai.com/blog/security/2025/oct/layerx-identifies-vulnerability-in-new-chatgpt-atlas-browser)
- [P-press] Guardio Scamlexity(2025년 8월): Comet을 대상으로 한 세 가지 시나리오를 "여러 번" 실행했고 결과는 엇갈렸다. 비율은 공개되지 않았다. — [Guardio](https://guard.io/labs/scamlexity-we-put-agentic-ai-browsers-to-the-test-they-clicked-they-paid-they-failed)
- [P] 운영 하니스에서의 벤더 자체 평가(대조용): Anthropic, auto mode를 켠 Cowork 하니스, 시나리오 0/110(Opus 5.5). 공격은 계약한 전문 레드팀이 작성했고 Opus 4.7에서 전이한 것이다. — [Opus 5.5 System Card](https://anthropic.com/claude-opus-5-5-system-card)
- [P] Perplexity의 BrowseSafe-Bench는 탐지기를 평가하며(F1 약 0.91), 에이전트의 종단 간 장악을 평가하지 않는다. — [Perplexity](https://www.perplexity.ai/hub/blog/browsesafe)
- [P-press] 벤더 간 공개 관행: 안전장치가 있는 경우와 없는 경우의 표면별 인젝션 비율을 공개하는 곳은 Anthropic뿐이다. OpenAI는 커넥터 robustness score 하나를 제시하고, Google은 아무것도 제시하지 않는다. — [VentureBeat, 2026-06-01](https://venturebeat.com/security/anthropic-browser-agent-hijacked-31-percent-before-safeguards-engaged)
- (앞선 노트에서 가져온 것으로, 출시된 브라우저가 아니라 모델 자체 또는 API 하니스 대상이다. RedTeamCUA에서 Operator는 검사가 있으면 7.57%, 없으면 30.89%. SIR은 Opus 4.8에서 24%. Saban 0/140.) — `threats_and_attacks.md` 참조.

### 추론
- 아직 측정되지 않은 것: (1) 안전장치를 켠 출시 버전 Chrome auto browse, Edge Agent Mode, Comet, Brave AI browsing에 대한 적응형 공격자. (2) 현실적인 주의력 조건(승인 피로, watch mode 중의 방심)에서 사람의 확인이 내는 효과. (3) 권한 모드별(Manual/Auto/Skip, Balanced/Strict) 종단 간 비율. (4) 긴 호흡의 공격과 메모리를 매개로 한 공격. (5) 대규모의 비영어 및 시각 전용 인젝션(BrowseSafe 자체 수치도 다국어에서 76%로 떨어진다). (6) 페이지 콘텐츠 인젝션을 중심으로 만든 벤치마크가 전혀 다루지 않는 공격 부류 B–F.
- 두 독립 연구는 벤더 수치와 강조점은 다르지만 실질은 다르지 않다. 둘 다 상위 제품이 단순한 인젝션을 거부한다는 것을 확인했고, 둘 다 그 시험 바깥에서 남은 약점을 확인했다(UW 연구에서는 아키텍처, AgentWatch에서는 간접적 표현).
- 서비스 약관, 봇 탐지, 빠른 제품 교체(Atlas는 열 달 만에 종료)는 출시된 브라우저에 대한 재현 가능한 제3자 벤치마킹을 어렵게 한다. 이것이 공백의 구조적 이유다.

### 공백
- 자동 또는 적응형 공격자를 사용해 운영 환경의 안전장치를 갖춘 여러 출시 에이전틱 브라우저를 벤치마크한 2026년 arXiv 논문은 찾지 못했다. 검색은 두 번 했고, 없다는 것이 증명된 것은 아니다.
- AgentWatch의 항목별 작업 수는 추출하지 않았다. 소수점 수치는 거친 값으로 다루어야 한다.
- Brave의 Black Hat USA 2026 발표(Chaikin)에는 브라우저 간 시험 결과가 담겨 있을 수 있다. 찾은 유일한 기사는 403을 반환했다.

## 8. 엔터프라이즈 통제와 애널리스트 권고

### 핵심
2026년 말이 되면 주요 벤더는 저마다 자사 브라우저 에이전트에 켬/끔 스위치와 사이트 허용/차단 목록을 제공하며, 엔터프라이즈 우선 제품은 기본값이 꺼짐이다(관리 대상 사용자의 Chrome auto browse, Edge Copilot/Agent Mode, HIPAA 테넌트의 Claude). 그러나 행동 수준의 감사 로깅과 DLP 통합이 명확히 문서화된 것은 Edge(DLP)와 OpenAI의 ChatGPT Work(Compliance API)뿐이다. AI 브라우저를 차단하라는 Gartner의 2025년 12월 권고는 내가 찾을 수 있었던 어떤 자료에서도 철회되지 않았다.

### 인용 근거
- [P] Chrome Enterprise: `GeminiSettings`, `GenAiDefaultSettings`, `GeminiActOnWebSettings`, `GeminiActOnWebAllowedForURLs`, `GeminiActOnWebBlockedForURLs`, `GeminiSparkSettings`. auto browse는 "관리 대상 사용자에게 기본적으로 꺼져 있다". Workspace 데이터 보호 통제가 적용된다. auto browse의 로깅/감사와 DLP는 이 페이지에 설명되어 있지 않다. — [Chrome Enterprise Help](https://support.google.com/chrome/a/answer/16291696)
- [P] Edge for Business: Copilot Mode는 자동으로 활성화되지 않는다. Agent Mode는 관리자의 승인 사이트 목록과 함께 별도로 활성화한다. DLP와 사용 권한 제한을 준수한다(보호된 페이지에 접근할 수 없다고 사용자에게 알린다). 저장된 비밀번호나 결제 수단을 쓰지 않는다. Microsoft 365 Copilot 라이선스가 필요하다. Microsoft 365 관리 센터의 Edge 관리 서비스를 통해 관리한다. — [Edge Blog, 2025-11-18](https://blogs.windows.com/msedgedev/2025/11/18/edge-for-business-presents-the-worlds-first-secure-enterprise-ai-browser/)
- [P] Claude Team/Enterprise: 조직 토글. 허용 목록과 차단 목록. 역할별 기능 설정(Enterprise 사용자 지정 역할). `forceLoginOrgUUID` Chrome 정책. Chrome 관리 도구를 통한 관리형 배포. Team과 Enterprise는 기본으로 켜짐(HIPAA 테넌트는 꺼짐). ZDR 미지원. 직접 입력한 차단 URL은 여전히 로드되지만 거기서는 Claude의 도구가 비활성화된다. — [Claude Help: admin controls](https://support.claude.com/en/articles/13065128-claude-in-chrome-admin-controls)
- [P-press] OpenAI: Lockdown Mode는 관리자가 전용 역할을 통해 지정한다. ChatGPT Work 관리자는 접근 권한, 연결된 도구, 허용되는 행동, 클라우드 환경의 브라우저/네트워크 접근, 지출 한도를 통제하고, "대화와 행동"을 포괄하는 Compliance API를 받는다. — [Help Net Security, 2026-02-16](https://www.helpnetsecurity.com/2026/02/16/chatgpt-lockdown-mode-elevated-risk/); [PPC Land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- [P-press] Gartner, "Cybersecurity Must Block AI Browsers for Now"(Dennis Xu, Evgeny Mirolyubov, John Watts, 2025년 12월 초): "AI 브라우저의 기본 설정은 보안보다 사용자 경험을 우선한다(Default AI browser settings prioritize user experience over security)". 거론된 위험에는 "간접 프롬프트 인젝션이 유발하는 에이전트의 악의적 행동"과, 피싱 사이트로 유도될 경우의 "자격 증명의 추가적인 유출과 남용"이 포함된다. 사이드바 데이터는 "클라우드 기반 AI 백엔드로 전송되는 경우가 많다". 권고: 백엔드가 너무 위험하다고 판단되면 설치를 차단하라. 평가 후 승인한다면, 사용자에게 "보고 있는 모든 것이 AI 서비스 백엔드로 전송될 수 있다"는 점을 교육하고, 에이전트의 능력을 제한하고(예: 이메일 금지), 데이터 보존을 막는 설정을 사용하라. 금지 사용 사례의 긴 목록과 전체 기기에 대한 모니터링을 예상하라. — [The Register, 2025-12-08](https://www.theregister.com/2025/12/08/gartner_recommends_ai_browser_ban/)
- [S] 2026년에 수정된 Gartner의 입장은 찾지 못했다. 이 글은 2026년 2월에 같은 논지로 일본어로 다시 게재되었고, 2026년 10월의 한 2차 요약은 위험 허용도가 낮은 조직은 "장기적으로 AI 브라우저를 차단해야 할 수도 있다"고 Gartner가 밝힌다고 전한다. — [ITmedia, 2026-02-20](https://atmarkit.itmedia.co.jp/ait/articles/2602/20/news005.html); [SecurePrivacy guide, 2026-11](https://secureprivacy.ai/blog/ai-browser-security-governance-guide)
- [P-press] Wiz가 기록한 반대 견해: 전면 차단은 "Department of No"(무조건 안 된다고 하는 부서) 문화를 낳을 위험이 있다. 권장하는 방식은 브라우징 컨텍스트를 주요 자격 증명과 격리하고, 확인 절차를 켜 두며, 사용을 위험이 낮은 작업으로 제한하는 것이다. — [Wiz, 2026-01-16](https://www.wiz.io/blog/agentic-browser-security-2025-year-end-review)
- [P] 학계의 사용자 권고(2026년 4월): "보안 모델이 아직 정립되지 않았으므로" 주의해서 사용하라. Brave, Edge, Firefox의 어시스턴트는 에이전트 기능이 더 적지만 현재로서는 보안이 더 강하다. — [UW project page](https://agent-security.cs.washington.edu/agentic_browsers_sop.html)

### 추론
- 2026년 11월에 실제로 취할 수 있는 엔터프라이즈 태세는 다음과 같다. 에이전트는 기본적으로 끈다. 그룹별로 켠다. 승인 사이트 목록으로 제한한다. 자격 증명과 결제 데이터를 손이 닿지 않는 곳에 둔다. 제한을 모델이 아니라 브라우저 바이너리나 테넌트 정책이 집행하는 벤더를 우선한다.
- 악성이거나 침해된 확장 프로그램은 이제 브라우저 에이전트에 대한 일급 위협이다(6절의 부류 D). 따라서 확장 프로그램 허용 목록 관리는 에이전트 토글과 같은 정책 묶음에 속한다. 내가 읽은 벤더 관리자 페이지 가운데 이 연결을 짓는 것은 없다.
- 에이전트 기능이 Chrome, Edge, 그리고 기존 브라우저용 확장 프로그램 안으로 들어갔기 때문에, 설치 프로그램을 차단하는 방식의 "AI 브라우저 차단"은 더 이상 이 위험을 막지 못한다. 통제는 브라우저 정책과 확장 프로그램 관리를 통해 이루어져야 한다.

### 공백
- Gartner의 원문과 2026년의 개정 여부는 읽지 못했다(유료). 진술은 언론 보도에서 가져왔다.
- Chrome auto browse, Edge Agent Mode, Claude in Chrome의 행동 수준 감사 로그: 읽은 페이지에 문서화되어 있지 않다(Chrome의 방문 기록 아이콘과 Claude Compliance API의 적용 범위가 유일한 단서이며, 후자는 제3자에게서 나온 것이다).
- (사용자 행동이 아니라) 에이전트 행동을 검사하는 제3자 DLP/SSE/엔터프라이즈 브라우저 제품에 관한 출처는 살펴보지 않았다.
- Chrome Enterprise Premium의 에이전트 전용 보호 기능(섀도 IT 탐지 이외)은 검증하지 못했다.
