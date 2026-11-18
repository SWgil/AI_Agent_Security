# 상용 화면/GUI 조작 AI 에이전트에 실제 배포된 보안 통제 (2026-11-18 기준 현황)

보고서 작성자를 위한 읽기 안내:
- 모든 항목에는 출처와 출처의 날짜를 붙였다. "직접 열람"은 2026-11-18에 해당 페이지를 직접 읽었다는 뜻이고, "검색 요약"은 검색 엔진이 보여 준 페이지 요약만 보았다는 뜻이다(신뢰도가 낮고, 문구가 의역되었을 수 있다).
- 1차 출처 페이지 몇 개는 열람하지 못했다(openai.com과 androidauthority.com은 HTTP 403을 반환했다). 이런 페이지에 기대는 주장에는 표시를 해 두었다.
- 제품별 조사 결과는 핵심 질문 1 아래에 있다. 핵심 질문 2와 3은 그 결과를 다시 쓰고, 집행 계층과 공백에 관한 내용만 덧붙인다.

## 핵심 질문 1 — 벤더들은 어떤 메커니즘을 출시했고, 그것은 어떻게 분류되는가(격리 / 범위 제한 / 사용자 동의 / 모델 수준 / 투명성 및 감사 / 사용 한도 / 데이터 처리)?

### 핵심
2026년 11월 기준으로 주요 벤더는 모두 같은 5계층 패턴(격리, 범위 허용 목록, 로그인·결제 시 사람의 확인 또는 제어권 인수, 두 번째 "비평자/분류기" 모델, 실시간 보기와 로그)을 출시했다. 그러나 격리가 어디에 놓이는지는 크게 다르다. Google/Android(백그라운드 가상 디스플레이 + 특권 권한), Microsoft(별도 OS 계정과 세션), OpenAI/Google 클라우드 에이전트(원격 VM)는 플랫폼 수준에서 격리하는 반면, Anthropic의 데스크톱 computer use와 사용자 프로필 안에서 동작하는 대부분의 브라우저 에이전트는 샌드박스가 없다고 명시하고 권한과 모델 측 검사에 의존한다. 구조화된 인터페이스(AppFunctions, App Intents, MCP/A2A, 중국의 SAEP)는 Google, Apple, Microsoft, Anthropic, ByteDance가 모두 우선 경로로 내세우며, 화면 조작은 대체 수단으로 둔다.

### 인용 근거

#### 1. Google — Android (Gemini 화면 자동화, Computer Control, AppFunctions)

Android Computer Control 프레임워크(개발자 문서, 직접 열람. 페이지에 "Preview" 표시가 있고 게시일은 나와 있지 않다):
- 누가 에이전트가 될 수 있는가: 이 프레임워크는 "OEM이 사전 탑재한 AI 어시스턴트가 선택된 앱에서 작업 자동화를 수행할 수 있게 한다". "어시스턴트 앱은 특권 권한인 `ACCESS_COMPUTER_CONTROL`을 보유해야 한다." — [Android Developers: Android Computer Control](https://developer.android.com/ai/computer-control)
- 격리: 이 프레임워크는 "OEM 사전 탑재 어시스턴트가 안전한 백그라운드 가상 디스플레이에서 대상 앱을 실행하고 조작할 수 있게 한다". "시스템은 캐스팅과 비슷하게 가상 기기에서 대상 앱을 실행한다." — [Android Developers](https://developer.android.com/ai/computer-control)
- 세션 한도: 한 세션은 "순차 실행을 위한 최대 여섯 개의 대상 앱 집합"을 다룬다. "시스템은 한 시점에 활성 세션 하나만 허용한다." — [Android Developers](https://developer.android.com/ai/computer-control)
- 동의: "어시스턴트 앱이 세션을 요청하면 프레임워크가 암묵적으로 시스템 대화상자를 띄워" 요청된 대상 앱의 자동화를 허용할지 사용자에게 묻는다. 대상 앱을 처음 건드릴 때 자동으로 표시된다. — [Android Developers](https://developer.android.com/ai/computer-control)
- 제어권 인수: "어시스턴트는 수동 개입이 필요할 때 사용자에게 제어권을 넘기거나, 사용자가 직접 제어권 인수를 요청하게 할 수 있다. 예를 들어 거래 확인에 유용하다." — [Android Developers](https://developer.android.com/ai/computer-control)
- 앱 쪽의 옵트인은 필요 없다("대상 앱을 통합하기 위해 추가로 변경할 것은 없다"). "AI 어시스턴트는 자동화할 대상 앱 집합을 제한하기로 선택할 수 있다." "현재 Computer Control 프레임워크는 제한된 대상 앱 집합과만 상호작용할 수 있다." 이 페이지에는 앱 쪽의 옵트아웃 메커니즘이 문서화되어 있지 않다. — [Android Developers](https://developer.android.com/ai/computer-control)
- 코드 분석에서 나온 구현 세부(검색 요약. 기사는 열람 시 403): 각 세션은 사용자 화면과 분리된 신뢰 가상 디스플레이 하나와 가상 입력 장치를 사용한다. 신뢰 디스플레이는 별도의 상호작용 가능한 디스플레이로 미러링할 수 있다. Google 또는 OEM이 신뢰하는 앱만 이 권한을 받는다. 사용자는 한 세션에 대해서만 또는 이후 모든 세션에 대해 승인한다. — [Android Authority: Computer Control feature](https://www.androidauthority.com/android-computer-control-feature-3603862/) (2025)

실제 출시된 Gemini 화면 자동화(Google 도움말 페이지, 2026-11-18 직접 열람. 날짜 표기가 없는 운영 중 페이지):
- 제공 범위: Pixel 10 및 Pixel 11 시리즈, Galaxy S26 시리즈, Z Flip 8, Z Fold 8, Tab S12 시리즈. 인도, 일본, 한국, 영국, 미국. 영어, 일본어, 한국어. "만 18세 이상". 개인 계정 전용("직장, 학교 또는 감독 대상 Google 계정으로 로그인하면 사용할 수 없다"). 베타. — [Pixel Phone Help: Ask Gemini to handle your multi-step tasks in select Android apps](https://support.google.com/pixelphone/answer/16940971?hl=en)
- 앱별 동의는 세 가지 상태("Always allow", "Ask every time", "Do not allow")로 나뉘며, Gemini 앱 > Settings > Screen automation에서 설정한다. 이 권한은 "Gemini가 앱의 화면 콘텐츠를 읽고 상호작용할 수 있게 한다". — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- 격리와 실시간 보기: Gemini는 "기기 내 가상 휴대전화 환경(an on-device virtual phone environment)"에서 작업하고 백그라운드에서 계속 진행한다. "View progress"로 진행 상황을 실시간으로 볼 수 있고, 채팅 또는 실시간 업데이트 알림에서 "Stop task"를 누를 수 있다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- "Take control"은 Pixel 10과 Galaxy S26에서는 제공되지 않는다(중지만 가능). Z Flip 8 / Z Fold 8에서는 제어권 인수 요청 후 10분 안에 사용자가 넘겨받지 않으면 Gemini가 중지한다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- 확인: Gemini는 특정 동작(메시지 전송, 배송 주소 변경) 전에 검토를 요청하는 것을 "목표로 한다(aims to)". 마지막 단계(승차 확정, 주문 완료)는 사용자가 수행한다. 결제 정보 입력, 거래 완료, 로그인 또는 계정 생성에서는 Gemini가 사용자에게 제어권 인수를 요청할 수 있다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- 사용 한도: 일일 요청 한도와 동시 작업 한도가 있다. 수치는 공개되어 있지 않다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- 벤더의 경고: 사용자는 "작업 중 Gemini의 행동에 대해, 실수와 구매 같은 예기치 않은 결과를 포함해 책임을 진다". 프롬프트 인젝션에 대한 명시적 경고가 있다(앱 콘텐츠, 이메일, 웹사이트에 숨겨진 지시가 주문, 주소 변경, 메시지 전송을 일으킬 수 있다). "로그인 정보, 결제 정보 또는 그 밖의 민감한 정보를 채팅에 직접 입력하지 말 것". 긴급 상황이나 민감한 작업에는 사용을 피하라고 한다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- 데이터: 자동화 중에 Gemini는 앱에 보이는 모든 것이 담길 수 있는 스크린샷을 찍는다(Google 도움말 페이지들의 검색 요약. 직접 열람한 Pixel 페이지 자체는 데이터 처리를 별도 페이지로 미룬다). — [Googlebook Help: multi-step tasks in select apps](https://support.google.com/googlebook/answer/18429222?hl=en)
- 최초 출시, 2026년 3월 12일(직접 열람): Galaxy S26 시리즈가 먼저였다. 출시 시점 앱은 Lyft, Uber, Grubhub, DoorDash, Uber Eats, Starbucks였다. Gemini는 주문을 넣기 전에 멈추고 강한 진동으로 알린다. 한 테스트에서는 프리뷰 버전이 휴대전화를 전체 화면 보기에 가두어 강제 재부팅으로만 빠져나올 수 있었다. — [9to5Google, 2026-03-12](https://9to5google.com/2026/03/12/gemini-android-app-automation-galaxy-s26-rollout/)

서버에서 내려받는 대상 앱 허용 목록:
- 2026년 11월 12일 기준으로 목록에는 안정 버전 Android 17에서 114개 앱이 있고 테스트 중인 앱이 20개 더 있었다(합계 134개). Google은 이 목록을 자사 서버에 두고 휴대전화가 내려받는다. 각 항목은 앱 ID와 서명 인증서를 짝지어 둔다. Google은 언제든 목록을 바꿀 수 있다. 범주는 음식/식료품, 승차, 여행, 티켓, 쇼핑, 업무용 앱(Word, Excel, Outlook, Teams, Slack)이다. 은행 앱은 언급되지 않는다. — [tech-ish, 2026-11-16](https://tech-ish.com/2026/11/16/gemini-task-automation-galaxy-apps/) (직접 열람. 2차 매체). 제목으로만 교차 확인됨: [Android Authority: "Exclusive: Google's hidden list of 134 apps that support Gemini task automation"](https://www.androidauthority.com/google-gemini-task-automation-target-apps-list-3734443/) (열람 시 403).

Google이 밝힌 Android용 Gemini Intelligence 보안 프레임워크(Google 블로그, 2026년 5월 12일, 직접 열람):
- "Gemini는 사용자가 작업을 허용한 앱에만 접근할 수 있고, 기기의 나머지 부분에는 접근할 수 없다". 앱별 자동화 토글은 "올해 안에" 제공하겠다고 약속했다. — [Google blog, Kleidermacher, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- "Gemini 어시스턴트는 사용자가 시킬 때에만 작업 자동화를 시작한다". "Gemini는 사용자를 대신해 구매하기 전에 사용자 확인을 요구하도록 설계되어 있다." — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- 투명성: "View progress"와 함께, 작업이 실행되는 동안 화면 상단에 "닫을 수 없는(that you can't dismiss)" 알림 칩이 표시된다. — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- 감사: Google은 Android Privacy Dashboard를 "곧(soon)" 확장해 지난 24시간 동안 어떤 AI 어시스턴트가 활동했고 어떤 앱을 사용했는지 보여 주겠다고 했다(2026년 5월 시점의 미래 시제). — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- 모델 수준: Google은 "Gemini가 사용자를 대신해 행동할 때를 위한 새로운 안전장치를 Android에 구축하고 있다"고 하며 Chrome auto browse의 보호 장치에 견준다(기술적 세부는 없다). Private Compute Core, Private AI Compute, protected KVM은 Magic Cue 같은 주변 데이터(ambient data) 기능과 관련해 언급되며, 화면 자동화를 특정해 언급한 것은 아니다. — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- Android 17 QPR2 Beta 4에는 설치·실행 중인 AI 에이전트를 추적하는 프라이버시 대시보드와 Private Compute Core용 "Data logging" 메뉴가 들어 있다고 한다(검색 요약. 출시 전 코드 분석). — [Android Authority: Android 17 QPR2 Beta 4 agent dashboard](https://androidauthority.com/android-17-qpr2-beta-4-agent-dashboard-3704775)

AppFunctions(구조화된 대안):
- 다른 앱의 함수를 실행하려면 호출자에게 `android.permission.EXECUTE_APP_FUNCTIONS`가 필요하다(레퍼런스 버전에 따라 `_TRUSTED` / `_SYSTEM` 변형이 나타난다). 앱은 자기 자신의 함수는 언제나 실행할 수 있다. — [Android Developers: AppFunctionManager reference](https://developer.android.com/reference/android/app/appfunctions/AppFunctionManager) (검색 요약)
- 2026년 5월자 zh-TW 개발자 페이지는 AppFunctions와 Gemini의 통합이 아직 신뢰할 수 있는 테스터 대상의 비공개 사전 출시 단계라고 했다(검색 요약, 의역). — [Android Developers: AppFunctions overview](https://developer.android.google.cn/ai/appfunctions?hl=zh-tw)
- Gemini 화면 자동화는 "Connected Apps로 제공되는 앱에서는 동작하지 않는다". 즉 구조화된 통합이 있는 곳에서는 Google이 GUI를 우회한다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)

#### 2. Google — Gemini in Chrome (auto browse), Gemini Agent, Project Mariner

아키텍처 게시글(Chrome 보안팀, 2025년 12월 8일, 직접 열람):
- User Alignment Critic: "Gemini로 만든 별도의 모델로, 고신뢰 시스템 구성 요소로 동작한다". 이 모델은 "계획이 끝난 뒤에 실행되어 제안된 각 동작을 다시 점검"하며, "제안된 동작에 관한 메타데이터만(only metadata about the proposed action)" 보므로 페이지 콘텐츠가 이 모델을 직접 오염시킬 수 없다. 거부권을 행사할 수 있고, 실패가 반복되면 제어권이 사용자에게 돌아간다. — [Google: Architecting Security for Agentic Capabilities in Chrome, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Agent Origin Sets: "읽기 전용 출처(read-only origins)는 Gemini가 콘텐츠를 소비하도록 허용된 출처다". "읽기·쓰기 가능 출처(read-writable origins)는 에이전트가 동작(예: 클릭, 입력)을 수행하도록 허용된 출처다". "신뢰할 수 있는 게이팅 함수가 플래너가 제안한 출처 중 어느 것이 작업과 관련 있는지 결정한다". 플래너는 "게이팅 함수의 승인 없이는 새 출처를 추가할 수 없다". 모델이 생성한 URL은 알려진 공개 URL의 결정론적 목록과 대조해 검사한다. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- 프롬프트 인젝션 분류기는 "자신이 보는 모든 페이지에서 간접 프롬프트 인젝션을 검사"하며, 계획 수립과 병렬로 동작하고 동작을 차단할 수 있다. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- 확인: 은행/의료 사이트로 이동하기 전(결정론적 목록), Chrome이 Google Password Manager를 통해 로그인하기 전(모델은 비밀번호에 직접 접근하지 못한다), 구매·결제·메시지 전송 전에 확인을 받는다. 각 단계의 작업 로그가 남고, 일시 중지/제어권 인수/중지가 가능하다. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- 생성된 악성 샌드박스 사이트를 이용한 자동 레드팀. 에이전트 보안 경계를 뚫는 경우에 대해 "최대 $20,000"의 버그 바운티를 건다. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- 스스로 밝힌 한계: "우리는 처음에 읽기·쓰기 가능 집합만 추적하는 더 단순한 버전의 출처 게이팅을 구현했다(We've initially implemented a simpler version of origin gating that just tracks the read-writeable set)". 분류기는 "모든 것을 잡아낼 수는 없다". — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)

Chrome 도움말 페이지(2026-11-18 직접 열람. 운영 중 페이지):
- 이용 자격: 만 18세 이상, 미국, 유료 AI 요금제(Ultra/Pro/Plus), Safe Browsing Enhanced 또는 Standard. 시크릿 모드에서는 안 되고 학교 계정에서도 안 된다. 직장/학교 계정의 접근은 "관리자가 사용 설정해야 한다". — [Chrome Help: auto browse](https://support.google.com/chrome/answer/16821166?hl=en)
- 작업을 시작하기 전, 그리고 "커뮤니케이션 전송", "사용자 데이터 수정", "웹 양식 제출", "일정 예약", 매우 민감한 금융·건강 데이터가 있는 사이트에 대해 확인을 받는다. 금융 거래 완료, 서비스 약관 동의, 계정 생성에서는 사용자에게 제어권 인수를 요청할 수 있다. 로그인에서는 일시 중지한다. — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- "Google은 사용자의 비밀번호를 Gemini in Chrome이나 다른 사이트와 공유하지 않는다." — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- 로컬 브라우저에서는 "Gemini가 사용자가 로그인한 사이트를 포함해 사용자와 똑같은 사이트에 모두 접근할 수 있다(Gemini has access to all the same sites that you do, including sites you're signed into)". 원격 브라우저는 기기를 닫은 뒤에도 계속 진행할 수 있다. 방문한 사이트는 동작 아이콘과 함께 Chrome 방문 기록에 나타나고, 채팅은 Gemini Apps Activity에 남는다. — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- "사용자는 작업 중 Gemini의 행동에 대해, 실수와 구매 같은 예기치 않은 결과를 포함해 책임을 진다". 안전장치는 "모든 위험에 대한 보호를 보장하지 않는다". "실험적 기능"이다. — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)

클라우드 VM 에이전트:
- Project Mariner는 2025년 5월에 클라우드 VM으로 옮겨 갔다(작업마다 클라우드 VM의 전용 브라우저에서 실행). Gemini Agent 모드는 채팅 옆에 Google VM의 실시간 브라우저를 보여 준다. Google이 2026년 5월 4일 Project Mariner를 종료하고 Gemini Agent에 통합했다는 보도가 있으나, 다른 페이지들은 여전히 운영 중인 것으로 기술한다. 상태에 관한 정보가 서로 충돌한다(검색 요약만 확인). — [TechCrunch, 2025-05-20](https://techcrunch.com/2025/05/20/google-rolls-out-project-mariner-its-web-browsing-ai-agent/); [WebProNews의 종료 보도](https://www.webpronews.com/google-shuts-down-project-mariner-as-browser-agents-give-way-to-smarter-ai-workflows/)

#### 3. Samsung — One UI 9

- "AI assistant activity"(Samsung 지원 페이지, 2026년 9월 9일 갱신, 직접 열람): Settings > Security and privacy > AI assistant activity > 어시스턴트 선택(예: Google). "Last 24 hours" / "Last 7 days"로 나뉜 타임스탬프 타임라인이 있다. Samsung은 이를 "중앙 집중식 감사 원장(centralized audit ledger)"이라고 부른다. 로그는 에이전트형 작업이 실행될 때에만 채워진다(예: Gemini가 Samsung Reminder에 항목을 만드는 경우. Notes, Clock, Gallery, Calendar, Reminder가 언급된다). 이 페이지는 열람만 설명하며, 삭제·내보내기·일시 중지·차단 제어는 없다. — [Samsung Support (IN): How to use AI assistant activity](https://www.samsung.com/in/support/mobile-devices/how-to-use-ai-assistant-activity-feature-in-galaxy-devices/)
- Galaxy Z Fold8 / Fold8 Ultra / Flip8의 One UI 9에서 처음 선보였다(검색 요약). — [Android Authority: One UI 9 AI Assistant Activity dashboard](https://www.androidauthority.com/samsung-one-ui-9-ai-assistant-activity-dashboard-3690155/)
- Samsung은 One UI 9이 Knox를 기반으로 한다고 말한다. 이 대시보드나 에이전트 제어를 Knox 기능으로 설명하는 출처는 찾지 못했다(검색 요약). — [Lowyat.net, 2026](https://www.lowyat.net/2026/399365/samsung-one-ui-9-galaxy-ai-privacy/)
- Galaxy에서 화면을 조작하는 에이전트는 Android Computer Control을 거치는 Google의 Gemini이므로, 격리와 동의는 위에서 다룬 Google의 메커니즘이다. S26 사용자는 작업을 중지할 수는 있지만 제어권을 넘겨받을 수는 없다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)

#### 4. Apple — Siri AI (iOS 27)

- 2026년 6월 8일 발표: 화면 인식(화면에 있는 내용에 대해 답하기)과 앱 간 동작. 기기 내 Apple Foundation Models에서 실행되고 필요하면 Private Cloud Compute에서 실행되며, 여기서는 "개인 데이터가 저장되지 않고 Apple이나 다른 누구도 접근할 수 없다". 시스템 오케스트레이터가 기기 내 Spotlight 색인과 "App Toolbox"를 사용한다. 베타는 "올해 안에", 영어 먼저. 초기에는 EU의 iPhone/iPad에서 제공되지 않고, 중국에서는 제공되지 않는다. — [Apple의 발표를 옮겨 실은 iClarified, 2026-06-08](https://www.iclarified.com/101113/apple-introduces-siri-ai-with-personal-context-and-onscreen-awareness) (직접 열람. 2차 출처)
- 앱 동작은 외부 모델 벤더에 원본 스크린샷을 보내는 방식이 아니라 개발자가 선언한 인터페이스(App Intents, 화면 인식 / 뷰 주석 API)를 거친다(1차 출처가 아닌 블로그의 검색 요약. 다른 한 매체는 모델이 "사람처럼 화면을 읽는다"고 주장해 서로 충돌한다). — [mer.vin, 2026-06](https://mer.vin/2026/06/apple-wwdc26-siri-ai-and-gemini-backed-foundation-models-on-device-and-private-cloud-compute/)

#### 5. OpenAI — Operator / ChatGPT agent / Atlas

- Operator 출시(2025년 1월. OpenAI 게시글의 검색 요약, openai.com은 열람 불가): 원격 브라우저에서 실행된다. 제어권 인수 모드(takeover mode)에서는 "로그인 자격 증명이나 결제 정보처럼 민감한 정보를 브라우저에 입력할 때 Operator가 사용자에게 제어권을 넘겨받으라고 요청"하며, 이 모드에서는 사용자가 입력한 내용을 수집하거나 스크린샷으로 찍지 않는다. 중요한 동작(주문 제출, 이메일 전송) 전에 확인을 받는다. 이메일이나 금융 서비스 같은 민감한 사이트에서는 watch mode가 적용된다. 은행 거래와 고위험 결정은 거절하도록 훈련되었다. "모니터 모델(monitor model)"이 의심스러운 행동을 감시하고 작업을 일시 중지할 수 있다. "완벽한 시스템은 없다". — [OpenAI: Introducing Operator](https://openai.com/index/introducing-operator/)
- ChatGPT agent(2025년 7월. 검색 요약): 가상 컴퓨터를 쓴다. 중대한 결과를 낳는 동작 전에 허락을 구한다. 사용자는 끼어들거나, 브라우저를 넘겨받거나, 중지할 수 있다. 로그인하려면 사용자가 가상 브라우저의 제어권을 넘겨받아야 한다. — [OpenAI: Introducing ChatGPT agent](https://openai.com/index/introducing-chatgpt-agent/); [OpenAI Help: ChatGPT agent](https://help.openai.com/en/articles/11752874-chatgpt-agent)
- Atlas(2025년 10월): 로그아웃 모드(에이전트가 사용자의 자격 증명 없이 동작). 민감한 사이트에서의 Watch Mode는 사용자가 탭을 떠나면 일시 중지한다. — [OpenAI CISO Dane Stuckey를 인용한 Simon Willison, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- Atlas 강화(2025년 12월): 다단계 인젝션을 찾도록 강화 학습으로 엔드투엔드 훈련한 LLM 기반 자동 공격자. 적대적으로 훈련한 에이전트 모델을 모든 Atlas 사용자에게 배포했다. 프롬프트 인젝션은 "완전히 해결될 가능성이 낮은(unlikely to ever be fully solved)" 장기 과제로 기술된다(검색 요약). — [OpenAI: Hardening Atlas against prompt injection](https://openai.com/index/hardening-atlas-against-prompt-injection); [Fortune, 2025-12-23](https://fortune.com/2025/12/23/openai-ai-browser-prompt-injections-cybersecurity-hackers/)
- Lockdown Mode와 "Elevated Risk" 라벨(2026년 2월 13일. 검색 요약): 고위험 사용자를 위한 선택 설정으로, 인젝션에 의한 유출을 줄이기 위해 네트워크를 쓰는 도구를 비활성화하거나 제한한다(브라우징은 캐시된 콘텐츠로 제한되고, 2차 요약에 따르면 Deep Research는 비활성화된다). 라벨은 ChatGPT, Atlas, Codex에 적용된다. 2026년 6월 4일 업데이트는 개인 계정과 셀프서브 Business 계정으로 배포 중이라고 밝힌다. — [OpenAI: Introducing Lockdown Mode and Elevated Risk labels](https://openai.com/index/introducing-lockdown-mode-and-elevated-risk-labels-in-chatgpt/); [Help Net Security, 2026-02-16](https://helpnetsecurity.com/2026/02/16/chatgpt-lockdown-mode-elevated-risk)

#### 6. Anthropic — computer use 도구, Claude in Chrome, Cowork computer use

computer use 도구, API(문서, 2026-11-18 직접 열람):
- 강제가 아니라 권고다: "최소 권한을 가진 전용 가상 머신 또는 컨테이너 사용". 로그인 정보를 주지 말 것. "인터넷 접근을 도메인 허용 목록으로 제한". "현실 세계에 의미 있는 결과를 낳을 수 있는 결정은 사람에게 확인을 요청". — [Claude Platform docs: computer use tool](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- 내장 기능: "분류기가 스크린샷처럼 도구가 반환하는 내용을 자동으로 검사해 프롬프트 인젝션 가능성을 표시한다... 분류기는 모델이 그 지시에 따라 행동하기 전에 지시가 정말 사용자에게서 온 것인지 확인하도록 유도한다". 옵트아웃은 지원팀을 통해서만 할 수 있다. — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- 데이터: "스크린샷과 파일이 Anthropic이 아니라 사용자의 환경에서 캡처·저장되는 클라이언트 측 도구"다(ZDR 적용 가능). — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)

Claude in Chrome(도움말 페이지, 2026-11-18 직접 열람, "Updated today"):
- 권한 모드는 세 가지다. Manual("Manually approve", 이전 명칭 "Ask before acting"). Auto("Automatically approve" — Claude가 각 동작의 안전성을 검토해 안전하지 않은 동작을 차단한다. Cowork 사이드 패널의 기본값). Skip("Skip all approvals", 이전 명칭 "Act without asking" — 동작을 검사하는 것이 아무것도 없다). — [Claude Help: Manage Claude in Chrome permissions](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- 사이트 수준 동의: "Allow this action" / "Always allow actions on this site" / "Decline". 승인된 사이트 목록과 권한 이력은 검토하고 취소할 수 있다. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- 어떤 모드에서든 항상 묻는 동작: 권한 설정 변경, 인가 부여, 민감할 수 있는 정보 입력, 영구 삭제, 파일 다운로드. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- 권한과 무관하게 금지되는 동작: 구매 또는 금융 거래, 계정 생성, 신용카드/신분증 데이터 취급, 금융 상품 매매, 시스템 파일 수정, "이메일이나 웹 콘텐츠에 담긴 지시를 수행하는 것(completing instructions from emails or web content)", 봇 검사 우회. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Team과 Enterprise에서는 관리자 허용 목록/차단 목록을 쓸 수 있다. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- 차단되는 사이트 범주: 도움말 센터의 버전끼리 서로 충돌한다. 이전 버전은 금융 서비스, 은행, 투자/트레이딩, 암호화폐 거래소, 성인 및 불법 복제 콘텐츠를 차단 대상으로 나열한다. 새 버전은 Claude가 금융 사이트에 앞서 허락을 구하고 성인 사이트와 알려진 불법 복제 사이트만 차단한다고 한다(검색 요약. 해소되지 않음). — [Claude Help: Use Claude in Chrome safely](https://support.claude.com/en/articles/12902428-use-claude-in-chrome-safely)

공개된 공격 성공률:
- 파일럿, 2025년 8월: 테스트 케이스 123개 / 시나리오 29개. 자율 모드에서 완화책이 없을 때 공격 성공률 23.6%, 완화책이 있을 때 11.2%(검색 요약). — [Anthropic: Piloting Claude for Chrome, 2025-08-26](https://www.anthropic.com/news/claude-for-chrome)
- 2025년 11월 24일(직접 열람), 내부의 적응형 "Best-of-N" 공격자, 환경당 100회 시도. 모델만 / 기존 안전장치 / 개선된 안전장치 순으로: Sonnet 4 13.8% / 10.7% / 2.0%, Sonnet 4.5 13.8% / 10.8% / 1.9%, Haiku 4.5 12.0% / 11.1% / 2.4%, Opus 4.5 8.0% / 7.6% / 1.4%. 방어책: 인젝션이 삽입된 모의 웹 콘텐츠에 대한 RL 훈련, 신뢰할 수 없는 모든 콘텐츠(숨겨진 텍스트, 조작된 이미지, 기만적인 UI)를 검사하는 분류기, 레드팀. "1%의 공격 성공률은 상당한 개선이기는 하지만 여전히 의미 있는 위험이다(A 1% attack success rate—while a significant improvement—still represents meaningful risk)." — [Anthropic: prompt injection defenses, 2025-11-24](https://www.anthropic.com/research/prompt-injection-defenses)

데스크톱의 Cowork computer use(도움말 페이지, 2026-11-18 직접 열람, "Updated yesterday". 베타 표시. Pro/Max/Ultra 요금제 전용):
- 도구 우선순위: 커넥터가 먼저, 그다음 브라우저, 화면 상호작용은 최후의 수단이다. — [Claude Help: Let Claude use your computer in Cowork](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- "Claude는 각 애플리케이션에 접근하기 전에 묻는다". "일부 민감한 앱(투자 및 트레이딩 플랫폼, 암호화폐)은 기본적으로 차단된다". 사용자의 앱 차단 목록에 있는 앱은 요청이 자동으로 거부된다. — [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- "computer use에는 Claude와 사용자의 애플리케이션 사이에 샌드박스가 없다(Computer use has no sandbox between Claude and your applications)." — [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- 자동 동작 검사가 프롬프트 인젝션의 징후를 살핀다. 이 검사는 최신 스크린샷을 보며, "Anthropic은 검사 기록에 그 사본을 보관할 수 있다(Anthropic can keep a copy of it in its record of the check)". 가드레일은 "절대적이지 않다(aren't absolute)". 은행, 의료, 정부 앱은 대상에서 빼 두라고 권고한다. — [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)

#### 7. Microsoft — Windows agent workspace, Windows의 MCP, Edge

Experimental agentic features(지원 페이지, 직접 열람. 2025년 10월 16일 게시, 2025년 11월 17일과 12월 5일 갱신):
- "experimental agentic features 설정은 기본적으로 꺼져 있다". "기기의 관리자 사용자만 켤 수 있다". 한번 켜면 모든 사용자에게 적용된다. — [Microsoft Support: Experimental agentic features](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- "각 에이전트는 사용자의 개인 사용자 계정과 구별되는 자체 계정으로 동작한다". "agent workspace는 별도의 Windows 세션에서 실행된다". "각 에이전트는 자체 workspace와 자체 권한을 가진다". — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- 알려진 폴더 접근(Documents, Downloads, Desktop, Videos, Pictures, Music). 빌드 26100.7344 이상에서는 에이전트별로 Allow Always / Ask every time / Never allow를 정한다. — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- 원칙: "에이전트의 모든 동작은 관찰 가능하며 사용자가 한 동작과 구별할 수 있다". 권한은 "세분화되고, 구체적이며, 시간 제한이 있다". "Windows는 변조 방지 감사 로그로 이 동작들을 검증할 수 있어야 한다(Windows should be able to verify these actions with a tamper-evident audit log)"(원칙일 뿐이며 구현은 기술되어 있지 않다). 언급된 위험: "교차 프롬프트 인젝션(cross-prompt injection, XPIA)". — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Copilot Actions 앱은 로그인한 사용자의 계정이 아니라 에이전트 계정으로 실행된다(검색 요약). — [Microsoft Learn: Securing AI agents on Windows](https://learn.microsoft.com/en-us/windows/security/book/operating-system-agentic-security)
- 2026년 11월 문서는 에이전트 보안을 "Agent identity"와 "Agent governance"로 재구성한다. Microsoft Execution Containers(MXC)는 "에이전트가 실행되는 동안 파일 및 네트워크 접근을 통제하는 데 도움이 되는 정책 기반 런타임 경계를 제공한다". — [Microsoft Learn: Agent governance, ms.date 2026-11-04](https://learn.microsoft.com/en-us/windows/security/application-security/agent-governance); [Microsoft Learn: Application and agent security, ms.date 2026-11-14](https://learn.microsoft.com/en-us/windows/security/application-security/)

Windows의 MCP(검색 요약):
- Windows 기기 내 레지스트리의 에이전트 커넥터는 동의, 거버넌스, 감사, 격리(containment) 집행을 처리하는 MCP 프록시 뒤에 놓인다. 서버가 등록되려면 코드 서명, 패키징 ID, 선언된 기능이 필요하다. — [Windows Experience Blog, 2025-11-18](https://blogs.windows.com/windowsexperience/2025/11/18/preparing-for-whats-next/)
- 기본적으로 이런 서버는 "에이전트 세션에서 실행되고, 별도의 환경에 안전하게 격리되며, 승인된 리소스에만 접근이 허용될 수 있다". 테스트용 토글인 "Reduce protections for agent connectors"를 켜면 이 보호가 사라진다. — [Microsoft Learn: MCP containment](https://learn.microsoft.com/en-nz/windows/ai/mcp/servers/mcp-containment)

Edge Copilot Mode Actions(Microsoft 게시글의 검색 요약, 2025년 10월 23일):
- 기본값인 "Balanced Mode"는 선별된 사이트 목록을 허용한다. "Strict Mode"는 모든 사이트에 대해 승인을 요구한다. 페이지 데이터는 신뢰할 수 없는 것으로 표시되고 Azure Prompt Shields로 걸러진다. 숨겨진 지시와 작업 이탈(task drift)을 검사하고 위험이 높으면 확인을 받는다. edge:// 페이지와는 상호작용하지 않고 Edge 밖으로는 접근하지 못한다. — [Microsoft Edge Blog: Considerations for Safe Agentic Browsing, 2025-10-23](https://blogs.windows.com/msedgedev/2025/10/23/considerations-for-safe-agentic-browsing/)

#### 8. 그 밖의 에이전트형 브라우저

- Brave AI browsing(블로그, 2026년 5월 5일 갱신. 검색 요약): 자체 쿠키, 로그인, 캐시를 가진 별도의 브라우저 프로필을 쓴다. 두 번째 모델인 "alignment checker"가 시스템 프롬프트, 사용자 프롬프트, 작업 모델의 응답을 받는다. 내부 페이지, HTTPS가 아닌 페이지, Safe Browsing이 경고한 사이트에는 접근하지 못한다. 옵트인 기능 플래그다. Brave는 이 검사기가 "모든 위험을 제거할 수는 없으며(cannot eliminate all risks)" 작업 모델의 출력을 통해 검사기 자체가 공격 대상이 될 수 있다고 밝힌다. — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- Perplexity Comet: BrowseSafe 콘텐츠 분류기(Perplexity는 약 91%의 탐지율을 주장한다. 레드팀은 인코딩 기반 인젝션에 약하다는 점을 발견했다). 도구 권한은 기본적으로 제한된다. 민감한 동작에는 확인을 받는다. Perplexity는 자사 탐지기가 2025년 9월부터 2026년 9월 사이에 115만 건의 인젝션을 표시했다고 보고한다(검색 요약). — [Perplexity: BrowseSafe++](https://perplexity.ai/hub/blog/browsesafe-plus-plus); [arXiv 2511.20597](https://arxiv.org/html/2511.20597v1); [eSecurity Planet: Red-teaming BrowseSafe](https://www.esecurityplanet.com/threats/red-teaming-browsesafe-exposes-ai-browser-guardrail-gaps/)

#### 9. 중국 — Doubao 폰과 규제

- 1세대(Nubia M153, 2025년 12월): 화면 읽기와 모의 탭을 썼다. 며칠 만에 WeChat 로그인이 실패했고 Taobao와 은행 앱이 위험 통제 검사를 발동했다. ByteDance는 금융 및 일부 민감한 조작을 철회했다. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657) (직접 열람)
- 2세대(Nubia NaviX Ultra, 2026년 9월 16일 판매 개시): "프로토콜 우선, 모의 조작은 대체 수단, 사람의 확인". (a) MCP/A2A가 있는 앱은 작업을 스스로 실행한다. (b) 프로토콜은 없지만 모의 탭에 동의한 앱은 SAEP에 따라 조작하되, 로그인·결제·추첨/출석 체크 페이지는 금지 구역이고 결제는 사용자가 한다. (c) 거부한 앱은 사용자를 위해 열어 주기만 한다. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)
- SAEP(Screen Automation Operation Declaration Protocol, Doubao가 출시 약 이틀 전에 공개): 앱이 페이지, 업무 의도, 조작 유형별로 경계를 선언한다. 시스템은 에이전트 신원, 보안 기준선, 앱 선언, 사용자 인가를 결합한다. 30일의 고지 기간이 있으며 그동안 서드파티 앱은 기본적으로 조작하지 않는다. 명시적 거부는 항상 존중된다. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)
- ICBC, China Merchants Bank, Alipay를 포함해 155개 앱이 Doubao 폰의 접근을 거부했다고 한다(검색 요약. 원 매체는 열람하지 않음). — [Jiemian](https://m.jiemian.com/article/15104952.html)
- 실사용 테스트: 결제와 실명 인증 단계는 사용자에게 넘어간다(검색 요약). — [Yicai](https://www.yicai.com/news/103366765.html)
- 규제: 에이전트 단말 기기의 망 접속(入網) 관리에 관한 MIIT 판공청 통지(工信厅信管〔2026〕42号, 2026년 11월 6일)는 적응 테스트, 권한 호출, 망 접속 테스트, OTA 업그레이드를 다룬다. 언론 보도에 따르면 앱 배포 플랫폼은 개발자가 에이전트 호출에 동의하는지를 전자 서명으로 선언하게 해야 하고, 에이전트는 동의하지 않은 앱을 강제로 호출해서는 안 된다(검색 요약. 전문은 읽지 않음). — [Xinhua, 2026-11-06](https://www.news.cn/20261106/b56a8a3cb14e40a69d997b37b39cc99a/c.html); [IT之家](https://ithome.com/1/023/059.htm)
- 언론이 보도한 표준(검색 요약, 원문과 대조 검증하지 않음): 2025년 12월의 "이중 인가(dual authorization)" 지침(사용자와 앱 양쪽의 동의). 금융 분야 에이전트 보안에 관한 단체 표준 T/BFIA 070-2026(2026년 8월 27일). 에이전트 애플리케이션 보안에 관한 강제성 국가 표준 계획(2026년 7월). — [Cover News](https://cbgc.scol.com.cn/news/7944927)
- Alipay의 에이전트는 이중 경로를 쓴다. 통합된 미니 프로그램에는 MCP를, 그 밖에는 GUI 모의 탭을 쓰며, 결제와 의료 보험 조작에는 일회성 확인을 받는다(논평 기사의 검색 요약). — [Huxiu](https://www.huxiu.com/article/4894229.html?f=rss)

#### 분류 매트릭스(위의 조사 결과에서 도출)

| 분류 | 출시된 사례 |
|---|---|
| 격리 | Android 백그라운드 가상 디스플레이; Windows 에이전트 계정 + 별도 세션, MXC; OpenAI/Gemini Agent 클라우드 VM/원격 브라우저; Brave 별도 프로필; Anthropic API: VM은 권고일 뿐; Cowork 데스크톱: 없음 |
| 범위 제한 | Google 서버 허용 목록(114개 앱, 인증서 고정), 세션당 최대 6개 앱; Chrome origin sets; Edge 선별 사이트 목록; Claude 차단 앱/사이트 + 관리자 목록; SAEP 페이지 수준 선언 |
| 사용자 동의 | Android 시스템 대화상자 + 앱별 Always/Ask/Never; Claude 사이트별/앱별; Windows 에이전트별 폴더 접근; 구매/전송 전 확인; 로그인/결제 시 제어권 인수(Google, OpenAI, Doubao) |
| 모델 수준 | Chrome User Alignment Critic + 인젝션 분류기; Brave alignment checker; Anthropic RL 훈련 + 분류기; OpenAI 모니터 모델 + 적대적 훈련; Perplexity BrowseSafe; Edge Prompt Shields |
| 투명성/감사 | Android 닫을 수 없는 칩 + View progress; Samsung AI assistant activity(24시간/7일); Chrome 작업 로그 + 방문 기록 아이콘; Claude 권한 이력; Windows 감사 로그 원칙 |
| 사용 한도 | Android: 세션 하나, 앱 여섯 개; Gemini 일일/동시 한도(수치 미공개); 만 18세 이상, 개인 계정 전용 |
| 데이터 처리 | Anthropic API 스크린샷은 클라이언트 측에 남음; Cowork 검사는 스크린샷을 보관할 수 있음; OpenAI 제어권 인수 중 캡처 없음; Apple PCC 저장 없음; Chrome 비밀번호는 모델과 공유되지 않음 |

### 추론
- 조사한 휴대전화 설계 가운데 화면 제어 능력 자체가 일반 앱이 얻을 수 없는 플랫폼 권한인 것은 Google의 Android 설계뿐이다. 중국의 1세대 설계는 앱 쪽의 동의 없이 OEM 제휴를 통해 그에 맞먹는 권한을 얻었고, 바로 이것이 앱 벤더의 반발과 SAEP/이중 인가라는 대응을 촉발했다.
- 업계는 "구조화된 인터페이스 먼저, GUI는 마지막"으로 수렴했다. Anthropic은 우선순위를 명시적으로 밝히고, Google은 Connected Apps에 대해 화면 자동화를 비활성화하며, Doubao 2세대는 "프로토콜 우선"이고, Apple은 App Intents를 거친다.
- 결제와 로그인은 거의 한결같이 모델을 신뢰하는 대신 사람에게 제어권을 되돌려주는 방식으로 처리된다. 이는 벤더들이 되돌릴 수 없는 동작에 대해서는 모델 수준 방어가 충분하다고 보지 않음을 시사한다.
- 서버에서 내려받는 인증서 고정 허용 목록은 Google에게 앱별 원격 킬 스위치를 주고, 재패키징된 앱이 대상 앱을 사칭하는 것을 막는다. 다만 후자는 Google의 설명이 아니라 2차 매체의 해석이다.

### 공백
- Gemini 화면 자동화의 정확한 일일/동시 한도 수치는 공개되어 있지 않다.
- Gemini 화면 자동화의 스크린샷 보관 및 사람 검토 정책: 직접 열람한 도움말 페이지는 내가 읽지 않은 다른 페이지로 미룬다.
- Android 17 Privacy Dashboard의 AI 활동 보기가 안정 버전에 출시되었는지 여부: Google의 2026년 5월 게시글은 "곧(soon)"이라고 하며, 베타 코드에서 목격된 사례만 찾았다.
- 약속된 앱별 자동화 토글이 기존 Gemini 앱의 앱별 권한과 다른 것인지 불분명하다.
- Knox 고유의 에이전트 보호나 Bixby가 서드파티 GUI를 조작하는 것에 관한 Samsung 출처는 찾지 못했다.
- Apple: Apple의 1차 페이지는 읽지 못했다. Siri 앱 동작의 확인 프롬프트나 권한 모델을 설명하는 출처가 없다. 데이터 처리에서 Gemini가 맡는 역할은 검증되지 않았다.
- OpenAI의 1차 페이지는 열람할 수 없었다(403). ChatGPT agent의 현재 상태는 논란이 있다. 2차 출처들은 "ChatGPT Work"로 대체되어 종료되었다고 한다. "30일 보관" 주장은 검증되지 않았다.
- Copilot Studio computer use(허용 목록, 자격 증명 처리)와 Opera Neon: 아무것도 확보하지 못했다.
- Honor, Xiaomi, OPPO, vivo: 이들의 민감 앱 처리에 관한 신뢰할 만한 1차 문서는 찾지 못했다. MIIT 통지와 중국 표준들의 전문은 읽지 않았다.

## 핵심 질문 2 — 어떤 통제가 OS/플랫폼에 의해 집행되고, 어떤 통제가 에이전트 자체의 모델이나 앱에 의해 집행되는가(즉 모델이 완전히 장악되어도 살아남는 통제는 무엇인가)?

### 핵심
모델이 완전히 탈취되어도 유지되는 통제는 플랫폼이 집행하는 통제다. Android의 특권 권한, 가상 디스플레이, 시스템 동의 대화상자, 세션 상한이 그렇고, Windows의 별도 에이전트 계정, 세션, ACL이 그렇다. 클라우드 VM 격리, Chrome의 결정론적 출처 게이팅과 민감 사이트 목록과 비밀번호 관리자 중개, Brave의 별도 프로필 격리도 여기에 든다. "결제 전 중지", 은행 작업 거절, 비평자 모델, 인젝션 분류기는 모델 또는 앱 수준의 통제이며 벤더 스스로 이를 확률적이라고 설명한다.

### 인용 근거
- Android 플랫폼 집행: OEM 사전 탑재 어시스턴트로 제한된 특권 권한, 시스템이 띄우는 동의 대화상자, 세션 하나와 앱 여섯 개, 가상 디스플레이에서 실행되는 대상 앱. — [Android Developers](https://developer.android.com/ai/computer-control)
- Android 에이전트 앱 집행: 허용 목록은 어시스턴트의 선택이며("AI 어시스턴트는 자동화할 대상 앱 집합을 제한하기로 선택할 수 있다(An AI assistant may choose to limit the set of target apps it automates)"), Google 서버에서 내려받는다. — [Android Developers](https://developer.android.com/ai/computer-control); [tech-ish, 2026-11-16](https://tech-ish.com/2026/11/16/gemini-task-automation-galaxy-apps/)
- Android 모델 수준이며 명시적으로 느슨한 통제: Gemini는 특정 동작 전에 검토를 요청하는 것을 "목표로 한다(aims to)". 도움말 페이지는 "승인되지 않은 구매를 완료"할 수 있다고 경고한다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Chrome: 게이팅 함수와 URL 검사는 결정론적이거나 페이지 콘텐츠로부터 격리된 것으로 기술된다. 비평자는 "메타데이터만" 본다. 모델은 저장된 비밀번호에 직접 접근하지 못한다. 민감 사이트 확인은 결정론적 목록을 사용한다. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Chrome에 남는 노출: 로컬 브라우저에서는 "Gemini가 사용자가 로그인한 사이트를 포함해 사용자와 똑같은 사이트에 모두 접근할 수 있다." — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- Windows OS 집행: 별도 계정, 별도 세션, 에이전트별 폴더 권한, 관리자만 가능한 활성화. — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Windows MCP: 기본적으로 프록시가 중개하는 동의와 에이전트 세션 내 격리. — [Microsoft Learn: MCP containment](https://learn.microsoft.com/en-nz/windows/ai/mcp/servers/mcp-containment)
- Anthropic 데스크톱: "computer use에는 Claude와 사용자의 애플리케이션 사이에 샌드박스가 없다". Skip 모드에서는 "동작을 검사하는 것이 아무것도 없다(nothing checks its actions)". — [Claude Help: Cowork computer use](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork); [Claude Help: permissions](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Anthropic API: 격리는 개발자의 책임이다(VM/컨테이너, 도메인 허용 목록은 권고 사항이다). — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- Brave: 프로필 격리는 구조적이다. alignment checker는 작업 모델의 출력을 통해 공격받을 수 있다. — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- Doubao 2세대: 경계는 앱 선언(SAEP)에서 나오며, 에이전트 신원 및 사용자 인가와 결합되어 시스템이 평가한다. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)

### 추론
- 모델이 장악되어도 살아남는 것: 누가 에이전트가 될 수 있는가(Android 권한, Windows 관리자 토글), 어떤 앱이나 출처에 닿을 수 있는가(모델 밖에서 검사되는 허용 목록, 출처 게이팅, Edge 사이트 목록, 관리자 차단 목록), 에이전트가 어떤 자격 증명을 쥐는가(비밀번호 관리자 중개, 로그아웃 모드, 별도 프로필/계정/VM), 그리고 가시성(닫을 수 없는 칩, OS 수준 로그).
- 살아남지 못하는 것: 모델이 결정하는 동작별 확인, 작업 거절, 비평자/정렬 모델과 분류기(두 번째 모델은 위험을 줄이지만 없애지는 못한다), 그리고 Android의 "결제에서 중지" 동작. 마지막 것은 프레임워크의 규칙이 아니라 에이전트의 동작으로 보인다. 프레임워크는 제어권을 넘기는 기본 기능(primitive)만 제공한다.
- 허용된 앱이나 읽기·쓰기 가능 출처 안에서는, 결제/자격 증명 입력에 이르기 전까지 탈취된 모델이 할 수 있는 일을 제한하는 출시된 플랫폼 통제가 없다. 범위 제한은 피해 반경을 한정하지만 범위 안의 오용(예: 배송 주소 변경, 메시지 전송)은 막지 못한다.
- Android의 가상 디스플레이는 기기의 나머지 부분을 에이전트로부터 보호하고 사용자의 포그라운드 세션을 간섭으로부터 보호한다. 그러나 대상 앱은 여전히 사용자의 로그인된 데이터로 실행되므로, 이것은 도달 범위의 격리이지 권한의 격리가 아니다.
- 어시스턴트 앱이 쥐고 있는 클라이언트 측 허용 목록은 그 앱의 무결성만큼만 강하다. Android에서는 어시스턴트가 특권을 가진 사전 탑재 앱이라는 점이 이를 뒷받침한다.

### 공백
- Android가 Computer Control 세션 안에서 은행 앱이나 FLAG_SECURE 창 같은 범주를 OS 수준에서 차단하는지는 읽은 출처에 문서화되어 있지 않다.
- Samsung과 Android의 활동 로그를 OS가 기록하는지 어시스턴트가 자체 보고하는지는 명시되어 있지 않다.
- Microsoft의 변조 방지 감사 로그는 선언된 원칙이다. 출시된 구현에 관한 문서는 찾지 못했다.
- OpenAI의 watch mode와 확인이 어떻게 집행되는지(모델인지 하니스인지)는 내가 접근할 수 있었던 자료에 문서화되어 있지 않다.

## 핵심 질문 3 — 문서화된 공백이나 벤더의 단서 조항은 무엇인가?

### 핵심
모든 벤더가 자사 문서에서 프롬프트 인젝션이 해결되지 않았다고 밝히고 남은 책임을 사용자에게 넘긴다. 공개된 잔여 공격 성공률은 적응형 공격에서 여전히 1–2% 안팎이며, 발표된 통제 가운데 몇몇(Android AI 활동 대시보드, Windows 변조 방지 로그, Chrome의 완전한 출처 게이팅)은 찾은 출처에서 부분적으로만 구현되었거나 아직 출시 전이었다.

### 인용 근거
- Google Android: 사용자는 "작업 중 Gemini의 행동에 대해, 실수와 구매 같은 예기치 않은 결과를 포함해 책임을 진다". 확인은 Gemini가 "목표로 하는(aims to)" 것이다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Google Android: Pixel 10과 Galaxy S26에서는 제어권 인수가 없고 중지만 있다. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Google Android: 프리뷰 버그로 휴대전화가 강제 재부팅 전까지 전체 화면에 갇혔다. — [9to5Google, 2026-03-12](https://9to5google.com/2026/03/12/gemini-android-app-automation-galaxy-s26-rollout/)
- Google Android: Computer Control은 OEM 사전 탑재 어시스턴트로 제한된 "Preview"이므로 서드파티 에이전트는 이 보호된 경로를 쓸 수 없다. — [Android Developers](https://developer.android.com/ai/computer-control)
- Chrome: 처음에는 "읽기·쓰기 가능 집합만 추적하는 더 단순한 버전의 출처 게이팅"만 구현되었다. 안전장치는 "모든 위험에 대한 보호를 보장하지 않는다". — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/); [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- Samsung: Samsung 자체 페이지에 따르면 활동 페이지는 열람 전용이다. 일시 중지/닫기 제어가 있다는 제3자의 주장은 확인되지 않았다. — [Samsung Support](https://www.samsung.com/in/support/mobile-devices/how-to-use-ai-assistant-activity-feature-in-galaxy-devices/)
- OpenAI: 프롬프트 인젝션은 "완전히 해결될 가능성이 낮다(is unlikely to ever be fully solved)"(검색 요약). — [Fortune, 2025-12-23](https://fortune.com/2025/12/23/openai-ai-browser-prompt-injections-cybersecurity-hackers/)
- OpenAI Atlas: 독립적인 테스트에서 Watch Mode가 GitHub나 온라인 뱅킹 사이트에서 발동하지 않았다. — [Simon Willison, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- Anthropic: "1%의 공격 성공률은... 여전히 의미 있는 위험이다". 데스크톱 가드레일은 "절대적이지 않다". 샌드박스가 없다. 동작 검사는 스크린샷을 보관할 수 있다. — [Anthropic, 2025-11-24](https://www.anthropic.com/research/prompt-injection-defenses); [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- Anthropic API: "어떤 상황에서는 Claude가 콘텐츠에서 발견한 명령을, 그것이 사용자의 지시와 충돌하더라도 따른다(In some circumstances, Claude follows commands found in content even when they conflict with your instructions)." — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- Microsoft: 기능은 "실험적"이고, 기기 보안에 영향을 줄 수 있으며, 더 엄격한 에이전트 계정 규칙은 이후 릴리스로 미뤄져 있다. XPIA와 환각이 위험으로 언급된다. — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Brave: alignment checker는 "모든 위험을 제거할 수는 없다". — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- Perplexity: 제3자 레드팀이 BrowseSafe가 인코딩된 인젝션에 약하다는 점을 발견했다(검색 요약). — [eSecurity Planet](https://www.esecurityplanet.com/threats/red-teaming-browsesafe-exposes-ai-browser-guardrail-gaps/)
- 중국: SAEP는 30일의 고지 기간이 지나면 거부하지 않은 앱을 위험 등급에 따라 점진적으로 개방한다. 즉 앱 입장에서는 옵트인이 아니라 옵트아웃이다. 거래 시나리오는 대부분 조회 또는 수동 확인에서 멈춘다. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)

### 추론
- 벤더의 공격 성공률 수치는 내부 벤치마크에서 자체 보고한 것이며 벤더 간에 비교할 수 없다. 찾은 출처 가운데 모델별 수치를 공개하는 곳은 Anthropic뿐이다.
- 책임 문구("사용자가 책임을 진다")는 Google에서 일관되게 쓰이며, 확인 UX가 보안뿐 아니라 법적 역할도 하고 있음을 내비친다.
- 감사 기능은 동작 기능보다 뒤처진다. 화면 자동화가 출시되고(2026년 3월) 몇 달이 지나서야 활동 로그가 나왔거나(Samsung, 2026년 9월) 여전히 출시 전이었다(Android 대시보드).
- Android Computer Control에는 앱 개발자를 위한 문서화된 옵트아웃이 없으며, 이는 중국이 택한 앱 동의 방향(SAEP, MIIT 통지)과 대조된다. 이것이 지역 간 설계의 가장 큰 차이다.

### 공백
- Android 화면 자동화의 인젝션 저항성을 독립적으로 측정한 자료는 찾지 못했다.
- 에이전트의 지출 상한이나 거래 한도를 문서화한 벤더는 없다.
- 제3자 분석은 Android에서 어떤 동작이 확인이 필요한 것으로 분류되는지 Google이 공개하지 않았다고 지적한다. 예시를 넘어 그것을 열거한 Google 출처는 찾지 못했다.
- 위의 주장 가운데 몇몇은 직접 열람한 페이지가 아니라 검색 요약에 근거한다(본문에 표시). 문구 그대로 인용하기 전에 다시 검증해야 한다.
