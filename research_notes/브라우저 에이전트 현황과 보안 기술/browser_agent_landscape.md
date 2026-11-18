# 브라우저 에이전트 제품 현황 (2026-11-18 기준)

모든 근거에 붙인 검증 표시의 뜻:
- **[opened]** = 이번 세션에서 인용한 페이지를 직접 가져와 읽었다(요약형 가져오기 도구를 거쳤으므로, 따옴표로 인용한 부분이 아니면 표현은 의역이다).
- **[snippet]** = 검색 결과 요약에서만 가져왔다. 페이지 자체는 열지 않았다. 신뢰도가 더 낮다고 보아야 한다.
- **[memory]** = 조사자의 배경지식에 있는 2024~2025년의 과거 사실이다. URL은 공식 발표 페이지이지만 이번 세션에서 열지는 않았다.

전체에 적용되는 단서: 아래의 2026년 사실 대부분은 벤더 게시물을 전달한 2차 언론·블로그에서 나왔다. 실제로 연 1차 벤더 페이지는 Anthropic의 2026-08-26 게시물 두 건과 Microsoft의 2026-05-13 Edge 블로그뿐이다. OpenAI와 Google의 1차 페이지는 열지 않았으므로(help.openai.com / blog.google 을 가져오지 않았다), OpenAI·Google 관련 서술은 간접 전달이다.

---

## Q1. 현재 어떤 브라우저 에이전트가 있고, 각각의 상태(출시·종료, 요금제, 국가, 플랫폼)는 어떠한가?

### 핵심
2025년 중반에서 2026년 11월 사이에 이 분야는 통합되었다. "1세대" 단독 제품 세 가지(OpenAI Operator, Google Project Mariner, ChatGPT Atlas)는 모두 종료되어 범용 에이전트(ChatGPT Work, Gemini Spark/Tasks, Chrome auto browse)에 흡수되었고, 현재 운영 중인 주요 소비자용 브라우저 에이전트는 Perplexity Comet, Claude in Chrome(2026년 8월 정식 출시), Chrome 내장 auto browse이다. 브라우저 제어는 이제 독립 제품이라기보다 더 넓은 "업무 에이전트" 안에 들어 있는 도구 하나인 경우가 대부분이다.

### 인용 근거

#### OpenAI
- (과거) Operator는 2025-01-23에 미국 ChatGPT Pro 사용자를 대상으로 한 리서치 프리뷰로 출시되었고, OpenAI가 호스팅하는 원격 브라우저에서 Computer-Using Agent (CUA) 모델을 실행했다. [memory] — [OpenAI](https://openai.com/index/introducing-operator/)
- (과거) Operator는 2025년 중반에 ChatGPT agent로 흡수되었다. 별도의 Operator 사이트는 2025-08-01에 지원 중단되었고 2025-08-31에 접속이 종료되었다. [snippet] — [usecarly.com 요약](https://www.usecarly.com/blog/what-is-chatgpt-agent/)
- (과거) ChatGPT agent는 2025-07-17에 출시되었다(시각 브라우저, 텍스트 브라우저, 터미널을 갖춘 가상 컴퓨터). [memory] — [OpenAI](https://openai.com/index/introducing-chatgpt-agent/)
- ChatGPT agent(agent mode)는 종료되었다. OpenAI 도움말 페이지에 "ChatGPT agent는 더 이상 제공되지 않는다"("ChatGPT agent is no longer available")고 적혀 있다고 한다(제3자가 2026-10-26에 확인). 해당 글은 종료 날짜를 밝히지 않는다. [opened] — [omidsaffari.com, 2026-10](https://omidsaffari.com/blog/chatgpt-agent-retired). 다른 안내 글은 "2026년 8월 초에 사전 지원 중단 공지 없이"("in early August 2026, with no advance deprecation notice") 제거되었다고 한다. [snippet] — [같은 글 및 관련 안내 글의 검색 요약](https://www.usecarly.com/blog/what-is-chatgpt-agent/). **상충/불확실: 정확한 종료 날짜는 1차 출처로 확인하지 못했다.**
- (과거) ChatGPT 사이드바와 agent mode를 갖춘 Chromium 기반 브라우저 ChatGPT Atlas는 2025-10-21에 출시되었다. [opened] — [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/); [tidbits, 2026-07-13](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/)
- Atlas 종료는 2026-07-09에 ChatGPT Work와 함께 발표되었다("단독 Atlas 브라우저의 단계적 종료를 시작한다"("We'll begin sunsetting the standalone Atlas browser")). [opened] — [tidbits](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/); [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- OpenAI 도움말 문서 "Evolving Atlas into ChatGPT for browser-based agentic work"에 따르면 Atlas는 "2026년 8월 9일에 작동을 멈출 예정"("scheduled to stop working on August 9, 2026")이었다. 북마크·탭·방문 기록·세션은 자동으로 이전되지 않으며, 브라우저 에이전트 기능은 ChatGPT와 Codex로 옮겨 간다. [opened, OpenAI 도움말 문서를 전달한 2차 자료] — [letsdatascience.com](https://letsdatascience.com/news/openai-shutters-standalone-chatgpt-atlas-desktop-browser-0cc642c2). Atlas는 292일 동안 존속했다. [opened] — [No Hacks 현황 가이드, 2026-10-31 갱신](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- ChatGPT Work(2026-07-09 출시)가 현재의 OpenAI 에이전트이다. "몇 시간 동안"("for hours") 실행되는 다단계 사무 작업, 예약 작업, 플러그인(Slack, Teams, Drive, SharePoint 등), 통합 데스크톱 앱(Mac 및 Windows)의 내장 브라우저, 데스크톱 Computer Use(백그라운드에서 사용자 기기의 클릭·입력·파일 이동), 그리고 Chrome 사이드바에서 ChatGPT를 실행하는 개편된 Chrome 확장 프로그램을 제공한다. 웹·모바일 배포: 2026-07-09에 Pro, Enterprise, Edu, 며칠 안에 Plus와 Business. 데스크톱 앱의 각 모드는 무료를 포함한 모든 등급에서 제공된다. [opened] — [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Work 안에서는 "클라우드 브라우저"가 웹사이트 단계를 처리한다. 사용자가 자리를 떠난 뒤에도 계속 작업하고, 자체 세션을 가지며("노트북에서 브라우저에 로그인해도 클라우드 브라우저에는 로그인되지 않는다"("A browser login on your laptop does not sign in the cloud browser")), 로그인·입력·확인이 필요하면 일시 중지한다. 지원 지역의 유료 요금제에서 제공된다(Free/Go 제외). [opened] — [omidsaffari.com](https://omidsaffari.com/blog/chatgpt-agent-retired)
- 2026-10-15부터 ChatGPT Chrome 확장 프로그램은 Work 모드에서 탭을 넘겨받아 조작할 수 있다. Atlas 종료가 발표되었을 때 이 확장 프로그램의 사용자는 약 200만 명이었다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- 새 데스크톱 앱은 Chat, ChatGPT Work, Codex를 하나로 합쳤다. Electron 기반이며 약 1.45 GB이다. [opened] — [tidbits](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/)

#### Google
- (과거) Project Mariner는 2024년 12월에 Chrome 확장 프로그램 형태의 연구용 프로토타입으로 발표되었고, 2025년 5월에 AI Ultra 구독자용 클라우드 VM으로 옮겨 갔다. [memory] — [TechCrunch 2025-05-20](https://techcrunch.com/2025/05/20/google-rolls-out-project-mariner-its-web-browsing-ai-agent/)
- Project Mariner는 2026년 5월 초에 종료되었다(검색 요약은 2026-05-04라고 하며, Android Authority의 2026-05-06 기사는 정확한 날짜를 밝히지 않는다). Google의 랜딩 페이지는 이 기술이 "다른 제품 속에서 이어질 것"("will live on in other products")이라고 했다. 팀 인력은 2026년 3월에 다른 곳으로 재배치되었다(Wired 보도, AA가 인용). 기능은 Gemini API와 Gemini Agent로 넘어간다. Mariner는 스크린샷을 자주 찍고 Chrome에서 클릭·입력하는 방식으로 동작했으며, 자원을 많이 쓰고 느리고 오류가 잦다는 평가를 받았다. [opened] — [Android Authority 2026-05-06](https://www.androidauthority.com/google-project-mariner-shutdown-3664323/)
- Chrome "auto browse"(Gemini 3)는 2026-01-28에 미국의 Google AI Pro 및 AI Ultra 구독자를 대상으로 데스크톱에서 출시되었다. 스크롤, 클릭, 입력, 페이지 이동을 수행한다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026); "1월에 데스크톱 프리뷰"로 교차 확인됨 [opened] — [Android Authority 2026-05-12](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/). No Hacks 페이지는 내부적으로 일관되지 않는다(타임라인은 "모든 사용자"라고 하고 본문은 Pro/Ultra 전용이라고 한다).
- Google 관리자 도움말 페이지에 따르면 auto browse는 관리형(기업용) Chrome 사용자에게는 제공되지 않는다. [snippet] — [Google Chrome Enterprise 도움말](https://support.google.com/chrome/a/answer/16291696)
- Android용 auto browse: 2026-05-12 발표, 2026년 6월 말부터 배포. AI Pro/Ultra, 미국 한정, Android 12 이상, RAM 4 GB 이상, 영어(미국). 구매 단계와 Google Password Manager 자격 증명 사용 단계에서는 사용자가 직접 넘겨받아야 한다. [opened] — [Android Authority 2026-05-12](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/). Gemini in Chrome은 2026-08-18에 미국의 모든 Android 사용자에게 제공되었지만 auto browse는 유료·미국 한정으로 남았다. iPhone/iPad의 Gemini in Chrome에서는 제공되지 않는다. [snippet] — [unite.ai](https://www.unite.ai/gemini-in-chrome-opens-to-all-u-s-android-users-as-auto-browse-goes-mobile/); [Google 블로그](https://blog.google/products-and-platforms/products/chrome/bringing-chrome-ai-to-android/)
- Gemini Spark(개인용 백그라운드 에이전트)는 2026년 5월 미국에서 AI Ultra 대상으로 처음 공개되었고, 2026-07-31의 약 1주일 전에 미국 AI Pro로 확대되었으며, 2026-07-30에 "160개 이상의 신규 국가"("over 160 new countries")의 유료 구독자로 확대되었다. Google은 2026-07-30에 Spark가 권한을 받으면 Chrome에 로그인된 계정과 저장된 비밀번호를 사용할 수 있다는 점도 발표했다(Chrome의 "auto browse", 미국). 결제와 민감한 동작에는 사람의 확인이 필요하다. [opened] — [Thurrott 2026-07-31](https://www.thurrott.com/a-i/339900/googles-gemini-spark-agent-expands-to-more-countries-and-adds-new-chrome-features). Spark는 EEA, 영국, 스위스, 나이지리아에서는 제공되지 않았다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- 2026-10-13: Gemini 앱에 AI Pro 및 Ultra 대상의 Chat/Task 전환 토글(Gemini 4 Argon)이 추가되었다. 2026-11-17: Google의 Gemini 블로그는 task mode가 미국, 일본, 한국, 캐나다, 호주, 인도의 "수억 명"("hundreds of millions of people")에게 무료로 제공되며 Gemini 4 Hydrogen(2026-11-12 공개)으로 구동된다고 밝혔다. 업체에 전화 걸기, Google Pay 결제(Mastercard/Visa와의 AP2), 문자 발송, 이메일 전달은 미국에 먼저 제공된다. 연결 가능한 앱: 60개 이상. 무료 작업의 사용 한도는 Google 게시물에 명시되어 있지 않다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- DigitalToday가 전달한 The Verge(2026-11-17) 보도는 이를 Gemini Spark가 "Tasks"로 이름을 바꾸고 6개국에서 무료로 제공되는 것으로 설명한다. [opened] — [DigitalToday 2026-11-18](https://www.digitaltoday.co.kr/en/view/127105/google-revamps-ai-agent-gemini-spark-into-tasks-adds-support-in-south-korea). 한 블로그는 무료 계정에 5시간마다 초기화되는 연산 예산과 주간 상한이 있다고 주장한다. [snippet, 미검증] — [검색 요약](https://www.fdaytalk.com/free-gemini-ai-agent/). **상충: 한 검색 요약은 ppc.land가 Google 게시물이 무료 사용자에 대해 아무 언급도 하지 않는다고 보았다고 했으나, 직접 연 ppc.land 페이지는 그 반대로 말한다(Google 게시물이 "수억 명"에게 무료라고 한다). 직접 연 페이지를 따른다.**
- 요금제 가격(Google 문서, 2026-10-04): AI Plus 월 $4.99, AI Pro $19.99, AI Ultra $99.99 또는 $199.99. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- 별도의 기업용 "Gemini agent"가 Google Cloud "Gemini at Work 2026"(2026-10-08)에서 공개되었으며, 비공개 프리뷰 상태이고 가격은 없다. [snippet] — [wikidocs](https://wikidocs.net/blog/@openwiki/32950/)
- Gemini 컴퓨터 사용 모델: Gemini 2.5 Computer Use 모델은 2025-10-07에 발표되었다(브라우저 중심, Gemini API의 `computer_use` 도구) [memory] — [Google](https://blog.google/innovation-and-ai/models-and-research/google-deepmind/gemini-computer-use-model/). Computer Use는 2026년 1월에 Gemini 3 Pro/Flash 프리뷰에 들어갔고, 2026년 6월에는 브라우저·모바일·데스크톱 환경을 포괄하는 Gemini 3.5 Flash의 내장 도구가 되었으며, 문서에서는 여전히 Preview로 표시된다. [snippet] — [Android Authority](https://www.androidauthority.com/gemini-3-5-flash-computer-use-model-3681381/); [Neowin](https://www.neowin.net/news/google-adds-built-in-computer-control-to-gemini-35-flash/). 한 제3자 집계 사이트는 정식 출시(GA)라고 주장하지만 확인되지 않았다.

#### Anthropic
- (과거) computer use 도구는 2024년 10월에 공개 베타로 출시되었다(Claude 3.5 Sonnet). 스크린샷을 입력받고 마우스·키보드 동작을 출력하며, 실행 환경은 개발자가 호스팅한다. [memory] — [Anthropic](https://www.anthropic.com/news/3-5-models-and-computer-use)
- (과거) Claude for Chrome: 2025년 8월 Max 구독자 대상 제한적 프리뷰, 2025년 12월 Pro, Team, Enterprise로 확대. Anthropic은 파일럿 당시 프롬프트 인젝션 성공률이 23.6% -> 11.2%로 떨어졌다고 보고했다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- 2026-08-12: Claude in Chrome 사이드 패널이 완전한 Cowork 클라이언트가 되었다(세션, 스킬, 커넥터를 데스크톱·웹·모바일과 공유). Max와 Team에 먼저, Pro에는 나중에 제공되었다. [snippet] — [The New Stack](https://thenewstack.io/claude-chrome-cowork-sessions/)
- 2026-08-26: Claude in Chrome이 "모든 유료 Claude 요금제에서 정식 제공"("generally available on every paid Claude plan")되었다. 사용자의 기존 로그인을 사용하고, 여러 탭에 걸쳐 동작하며, 안전한 동작은 자동 승인하고(Claude Code auto mode와 같은 메커니즘이며 끌 수 있다), 안전 분류기가 각 동작을 원래 요청과 대조해 검사한다. Chrome 데스크톱 전용이다: "다른 Chromium 브라우저나 모바일에서는 아직 실행되지 않는다"("does not run on other Chromium browsers or on mobile yet"). 기업 관리자는 승인된 도메인으로 사용을 제한할 수 있다. [opened, 1차 자료] — [Anthropic 2026-08-26](https://claude.com/blog/claude-in-chrome-generally-available)
- 같은 게시물의 프롬프트 인젝션 수치(간략히만 적는다. 보안은 다른 문서에서 다룬다): 추가 안전장치를 적용하기 전, 더 강한 레드팀 공격에 대해 Opus 4.5에서는 성공률 17.6%, Opus 5에서는 3.8%였다. Opus 4.8부터 적용된 프로브 + 분류기를 쓰면 Sonnet 5 / Opus 5 / Mythos 5에서는 성공한 공격이 없었고 Fable 5에서는 0.3%였다. Anthropic은 인젝션이 "여전히 움직이는 표적"("remains a moving target")이라고 말한다. [opened, 1차 자료] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available)
- 2026-08-26: Cowork에 내장 브라우저가 생겼다(macOS, Windows, Linux 베타의 Claude 데스크톱 앱 사이드 패널). Pro, Max, Ultra, Team 대상이며 Enterprise는 관리자가 활성화한다. 사용자의 브라우저와 분리되어 있다("Claude는 사용자의 탭, 북마크, 비밀번호를 결코 보지 않는다"("Claude never sees your tabs, bookmarks, or passwords")). 로그인은 Chrome/Edge/Firefox에서 사이트별로 가져올 수 있고, 은행·이메일·SSO 사이트는 기본적으로 제외된다. Claude in Chrome이 설치되어 있으면 그것이 Cowork의 기본 브라우저로 유지된다. [opened, 1차 자료] — [Anthropic 2026-08-26](https://claude.com/resources/articles/cowork-built-in-browser)

#### Microsoft
- (과거) Edge Copilot Mode는 2025년 7월에 출시되었고, Copilot Actions와 Journeys는 2025년 10월에 프리뷰로 공개되었다(미국, 제한적 프리뷰). [snippet] — [Windows Central](https://www.windowscentral.com/microsoft/windows-11/microsoft-edge-copilot-actions-journeys-available-2025)
- 2026-05-13: "Copilot Mode를 종료한다"("we're retiring Copilot Mode"). 기능은 일반 Edge에 내장된다. Copilot Actions는 "미국 한정 Microsoft 365 Premium 구독자를 위한 Edge 데스크톱의 Browse with Copilot"("Browse with Copilot on Edge desktop for Microsoft 365 Premium Subscribers in the U.S. only")이 되었고 사용 한도가 적용된다. Vision, Voice, 다중 탭 컨텍스트, 장기 기억은 데스크톱과 모바일의 모든 Copilot 제공 시장에서 쓸 수 있다. Journeys는 데스크톱에서는 영어권 시장 한정, 모바일에서는 미국 한정이다. [opened, 1차 자료] — [Microsoft Edge 블로그 2026-05-13](https://blogs.windows.com/msedgedev/2026/05/13/new-updates-to-edge-across-desktop-and-mobile/)
- Edge 150(2026년 7월)은 2027-03-30까지 진행되는 WebMCP 오리진 트라이얼을 열었다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)

#### Perplexity Comet
- Comet: 2025년 7월 데스크톱(처음에는 Max 전용), 2025년 10월 모두에게 무료, 2025-11-20 Android, 2026-03-18 iOS(03-11에서 연기). 모바일 앱은 무료이며 일부 에이전트 기능을 갖춘 어시스턴트를 포함한다. Pro 월 $20와 Max 월 $200는 한도를 높여 준다. [snippet] — [Thurrott](https://www.thurrott.com/mobile/333936/perplexity-launches-its-comet-browser-on-ios-and-ipados); [TechCrunch 2025-10-02](https://www.techcrunch.com/2025/10/02/perplexitys-comet-ai-browser-now-free-max-users-get-new-background-assistant/)
- 2026년 3월: macOS/Windows에서 MDM 무인 배포를 지원하는 Comet 기업용 출시. Amazon v. Perplexity: 2026-03-10 예비적 금지명령, 항소 중 집행 정지, 2026-08-04 제9연방항소법원이 취소. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)

#### 기타 브라우저
- Dia / Arc (The Browser Company): Atlassian의 인수가 2025-09-04에 약 $610M 규모로 발표되어 2025-10-20에 완료되었다(공시 자료: 현금 약 $488M와 귀속 조건부 주식). Dia는 macOS 14 이상 Apple Silicon용으로 정식 출시되었고 14일 체험이 딸린 유료 등급이 있다. Arc는 "Chromium 업데이트만 받는다"("receives Chromium updates only"). [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026). **Windows에 관한 상충: No Hacks(2026-10-31 갱신)는 Windows가 베타 대기자 명단 단계라고 하고, 다른 리뷰는 Dia가 2026-10-07에 온전한 에이전트 경험 없이 Windows에 출시되었다고 한다** [snippet] — [wetheflywheel](https://wetheflywheel.com/en/radar/dia-browser-review/). 리뷰어들은 Dia를 "완전하지는 않은"("not fully") 에이전트형으로 분류한다.
- Opera Neon: 2025년 9월 발표, 2025년 12월 월 $19.90로 공개. 2026년 2월 Intelligent Mode. 2026-08-14 무료 요금제 추가. 에이전트는 Neon Do / Neon Make / ODRA. MCP 서버로 노출할 수 있다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Brave: "AI browsing"(Leo 에이전트 모드)이 2025-12-10에 Nightly에서 플래그 뒤에 숨겨진 초기 테스트로 들어갔다. 쿠키와 로그인 상태가 일반 프로필과 분리된 격리 프로필에서 동작한다. Brave의 게시물은 2026-05-05에 갱신되어 모든 릴리스 채널에서 초기 테스트 중이라고 밝혔다. [snippet] — [Brave 블로그](https://brave.com/blog/ai-browsing/). **상충: No Hacks는 Brave의 에이전트 기능을 "None"(없음)으로 적고 있다** [opened] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026).
- Mozilla Firefox: Firefox 148(2026-02-24)에 AI 킬 스위치. 2026-08-18 "Smart Window" 베타. Firefox 155(2026-09-01)가 이를 미국, 캐나다, 프랑스의 모든 사용자에게 열었다. 웹을 인식하는 채팅 + 탭 관리 기능이며, 클릭하고 입력하는 에이전트는 아니다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Samsung: Windows용 "Samsung Browser"가 2026년 3월 말에 Perplexity 기반 "Browsing Assist"와 함께 출시되었다(Galaxy Android에서도 제공). 에이전트 기능은 한국과 미국으로 한정된다고 한다. [snippet + opened 3차 자료] — [Samsung Mobile Press](https://samsungmobilepress.com/articles/samsung-browser-windows-agentic-ai); [Perplexity 블로그](https://hub-prod.perplexity.ai/hub/blog/perplexity-apis-power-ai-browsing-on-samsung-devices); [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Genspark 브라우저(2025년 5월. Autopilot Mode, Super Agent, MCP 스토어)와 Fellou(편집 가능한 워크플로 계획을 갖춘 완전한 에이전트로, 로그인된 세션을 사용한다)가 단독 에이전트형 브라우저로 존재한다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- 중국: Alibaba Quark, ByteDance Doubao, Meituan의 "Tabbit"은 모두 브라우저가 아니라 AI 에이전트로 마케팅된다. 2026년 6월의 한 실사용 리뷰는 Tabbit이 양식 작성·가격 비교·데이터 내보내기에서 가장 강하다고 평가했다. Doubao 전문가용 에디션에는 권한을 부여하면 PC의 앱, 브라우저, 파일을 조작하는 "office task mode"(사무 작업 모드)가 추가되었다. [snippet] — [36Kr](https://www.36kr.com/p/3855958190127752)
- Manus (Butterfly Effect): 클라우드 가상 컴퓨터 + 브라우저. Meta가 2025년 12월 말에 인수하기로 합의했다($2B 초과로 보도). Caixin(2026-08-12)은 이 거래가 무산되어 Manus가 독립 운영을 재개한다고 보도한다. [snippet] — [Caixin](https://www.caixinglobal.com/2026-08-12/tech-brief-aug-12-manus-to-resume-independent-operations-as-meta-deal-unwinds-102473330.html); [SiliconANGLE 2025-12-29](https://siliconangle.com/2025/12/29/meta-platforms-buys-manus-bolster-agentic-ai-skillset/). **상충: 2026년 초의 자료들은 이 거래가 완료된 것으로 서술한다.**

#### 개발자 / 인프라 계층 (간략)
- Amazon Nova Act는 2025-12-02에 UI 워크플로 에이전트 플릿을 위한 AWS 서비스로 정식 출시되었다(US East). 시간당 과금이다. [snippet] — [AWS](https://aws.amazon.com/blogs/aws/build-reliable-ai-agents-for-ui-workflow-automation-with-amazon-nova-act-now-generally-available)
- Browserbase: 누적 투자 유치 약 $67.5M(Series B). Stagehand v4.0.0은 2026-08-10(MIT). Playwright MCP(Microsoft) v0.0.79는 2026-08-06(Apache-2.0). Chrome DevTools MCP는 CDP(네트워크, 콘솔, 스크립트 평가)를 노출한다. Browser Use: 시드 약 $17M(2025). [snippet, 제3자 집계 사이트] — [Contrary Research](https://research.contrary.com/company/browserbase); [nikiforovall 블로그](https://nikiforovall.blog/ai/2026/07/05/browser-automation-agents-ecosystem.html)
- WebMCP(사이트가 브라우저 내 에이전트에게 도구를 노출하는 방식)는 Edge 150에서 오리진 트라이얼 중이고 Brave에서는 실험 단계이다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)

### 추론
- "단독 제품으로서의 브라우저 에이전트"는 모델 벤더들에게서 상업적으로 대체로 실패했다(Operator 약 7개월, Mariner 약 17개월, Atlas 약 10개월). 그러나 그 기능은 범용 에이전트 안의 도구로서 어디에나 남아 있다. 보고서는 2026년 11월의 상태를 "에이전트형 브라우저라는 범주의 승자"가 아니라 "기능으로서의 브라우저 제어"로 서술해야 한다.
- 2026년 11월 기준으로 도달 범위가 가장 넓은 브라우저 조작 가능 에이전트는 아마도 Gemini Tasks(무료, 6개국)와 ChatGPT Work의 클라우드 브라우저(유료)일 것이며, 둘 다 브라우저 제품이 아니다.
- 벤더의 명칭 변경이 잦으므로(Spark -> Tasks, Copilot Actions -> Browse with Copilot, Claude for Chrome -> Claude in Chrome, agent mode -> Work), 보고서는 두 이름을 모두 적어야 한다.

### 공백
- OpenAI의 1차 페이지는 하나도 열지 않았다. ChatGPT agent의 정확한 종료 날짜는 알 수 없다. Atlas가 실제로 2026-08-09에 멈췄는지는 HUMAN/No Hacks의 2차 서술에 의존한다.
- "Tasks"가 Spark의 공식 명칭 변경인지, 그리고 무료 등급의 한도가 어떠한지는 Google 자체 게시물로 확인하지 못했다.
- 2026년 11월 기준으로 Chrome auto browse가 데스크톱에서 미국 밖(한국 포함)에 제공되는지: 확대를 언급한 출처를 찾지 못했고, 모든 출처가 미국 한정이라고 한다.
- Copilot Studio computer use, Windows agent workspace / Windows의 Copilot Actions: 이번 세션에서 2026년 출처를 찾지 못했다.
- Arc Search, Genspark/Fellou의 현재 상태, Quark의 세부 사항, Kakao: 신뢰할 만한 2026년 데이터를 찾지 못했다.
- 2026년 5월 이후 Brave AI browsing의 안정 버전 출시 여부는 알 수 없다.

---

## Q2. 아키텍처 분류: 브라우저는 어디에서 실행되고, 누구의 세션을 쓰며, 모델은 무엇을 관찰하는가?

### 핵심
네 가지 분류가 공존하며, 이제 여러 벤더가 두 가지를 동시에 제공한다(세션 공유 모드와 격리 모드). 이것이 2026년에 보안과 관련해 가장 중요한 제품상의 사실이다. Anthropic(Claude in Chrome 대 Cowork 내장 브라우저), OpenAI(Chrome 확장 프로그램 대 클라우드 브라우저), Google(Chrome auto browse 대 Tasks용 사용자별 클라우드 VM)은 각각 에이전트를 사용자의 로그인된 브라우저 안에서 실행할 수도 있고, 자체 세션을 가진 별도 환경에서 실행할 수도 있게 한다.

### 인용 근거

| 분류 | 제품 (2026년 11월) | 브라우저 위치 | 사용하는 세션 | 근거 |
|---|---|---|---|---|
| (a) 사용자가 쓰던 주류 브라우저 안의 확장 프로그램 / 내장 에이전트 | Claude in Chrome; ChatGPT Chrome 확장 프로그램(2026-10-15부터 Work 모드 탭 넘겨받기); Chrome auto browse(Chrome 데스크톱 및 Android에 내장); Edge Browse with Copilot | 사용자 기기, 사용자 프로필 | 사용자의 실제 로그인된 세션 | 아래 |
| (b) 에이전트가 내장된 AI 네이티브 브라우저 | Perplexity Comet; Opera Neon; Dia(부분적 에이전트형); Genspark; Fellou; Samsung Browser Browsing Assist; (종료) ChatGPT Atlas; Brave AI browsing(격리 프로필 변형) | 사용자 기기 | 해당 브라우저에 있는 사용자의 세션(Brave: 별도 프로필) | 아래 |
| (c) 벤더가 운영하는 원격/클라우드 브라우저 또는 VM | ChatGPT Work 클라우드 브라우저; Gemini Tasks/Spark(사용자별 클라우드 VM); Manus; (종료) Operator, ChatGPT agent, Project Mariner(2025년 버전) | 벤더 클라우드 | 에이전트 자체 세션. 사용자가 그 안에서 로그인하거나 자격 증명을 넘겨주어야 한다 | 아래 |
| (c') 데스크톱 에이전트가 구동하는 격리된 로컬 브라우저 | Claude Cowork 내장 브라우저; ChatGPT 데스크톱 앱 내장 브라우저 | 사용자 기기, 사용자의 브라우저와 분리 | 분리됨. 선택적으로 사이트별 로그인 가져오기 | 아래 |
| (d) 모델 API + 개발자가 운영하는 브라우저 | Anthropic computer use 도구; Gemini computer_use 도구(3.5 Flash); OpenAI CUA/computer-use API; Nova Act; Browser Use; Stagehand/Browserbase; Playwright MCP; Chrome DevTools MCP | 개발자 인프라 또는 클라우드 브라우저 서비스 | 개발자가 마련한 것 | 아래 |

- Claude in Chrome은 Claude에게 "사용자의 기존 로그인을 사용해"("using your existing logins") 접근할 수 있게 한다. 현재 페이지를 보고, 텍스트를 읽고 입력하고, 클릭하고, 페이지를 이동하고, 양식을 채울 수 있다. Anthropic의 게시물은 스크린샷인지 DOM인지 명시하지 않으며, 페이지 내용이 도구 결과를 통해 들어온다고만 말한다. [opened, 1차 자료] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available)
- Cowork 내장 브라우저: "Claude는 사용자의 탭, 북마크, 비밀번호를 결코 보지 않는다"("Claude never sees your tabs, bookmarks, or passwords"). 로그인은 사이트별로 가져온다. 은행·이메일·SSO는 기본적으로 제외된다. 데스크톱 앱에서 실행되며, 데스크톱 앱이 온라인인 동안에는 웹·휴대전화에서 조작할 수 있다. [opened, 1차 자료] — [Anthropic](https://claude.com/resources/articles/cowork-built-in-browser)
- ChatGPT Work 클라우드 브라우저는 자체 세션을 유지하며 노트북의 로그인을 물려받지 않는다. 사용자가 자리를 떠난 뒤에도 계속 동작한다. [opened] — [omidsaffari.com](https://omidsaffari.com/blog/chatgpt-agent-retired). ChatGPT 데스크톱 앱에도 내장 브라우저가 있고, OS 수준의 Computer Use가 별도로 있다. [opened] — [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Gemini Tasks: 각 사용자의 작업은 Google Cloud "Agent Compute Engine"의 하드웨어 격리된 전용 클라우드 VM에서 실행된다. 미국에서는 Google Password Manager 자격 증명으로 사이트에 로그인하고 Google Pay로 결제할 수 있다. 중요한 단계 전에는 일시 중지한다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/); [DigitalToday](https://www.digitaltoday.co.kr/en/view/127105/google-revamps-ai-agent-gemini-spark-into-tasks-adds-support-in-south-korea)
- Chrome auto browse(데스크톱, 미국)는 저장된 로그인·비밀번호를 사용할 수 있지만 결제 단계는 사용자에게 되돌려준다. Spark는 권한을 받으면 Chrome에 로그인된 계정을 사용할 수 있다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/); [Thurrott](https://www.thurrott.com/a-i/339900/googles-gemini-spark-agent-expands-to-more-countries-and-adds-new-chrome-features)
- Project Mariner는 잦은 스크린샷으로 관찰하고 Chrome에서 클릭·입력으로 동작했다. [opened] — [Android Authority](https://www.androidauthority.com/google-project-mariner-shutdown-3664323/)
- Brave AI browsing은 격리 프로필을 사용한다. 쿠키, 로그인 상태, 캐시는 프로필 사이를 넘나들지 않는다. [snippet] — [Brave](https://brave.com/blog/ai-browsing/)
- Samsung Browsing Assist는 전용 단일 테넌트 Perplexity 클러스터에서 Perplexity의 Agent/Search API를 호출하며, 데이터를 전혀 보관하지 않는다고 주장한다(벤더 주장). [snippet] — [Perplexity](https://hub-prod.perplexity.ai/hub/blog/perplexity-apis-power-ai-browsing-on-samsung-devices)
- Chrome DevTools MCP는 에이전트에게 프로토콜 수준의 접근(네트워크 요청, 콘솔, 스크립트 평가)을 제공한다. 즉 스크린샷으로 보는 것 이상이다. [snippet] — [Browserbase 블로그](https://browserbase.com/blog/best-mcp-servers-for-browser-automation)
- Android "task automation"(2026-11-10, 영국·인도·일본으로 확대)은 기기 내 가상 휴대전화에서 실행되며, Tasks에 쓰이는 클라우드 VM과는 다르다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)

### 추론
- 분류별 관찰 방식(일반적인 경향이며, 위에서 출처로 뒷받침된 것은 일부뿐이다): API 형태의 computer-use 도구(Anthropic, Gemini, CUA)는 스크린샷 기반이다. 확장 프로그램·AI 네이티브 브라우저는 페이지 텍스트/DOM/접근성 트리를 스크린샷과 결합한다. 개발자 프레임워크(Playwright MCP, Stagehand, Browser Use)는 접근성 트리/DOM 스냅숏에 기댄다. 이번 세션에서 Comet, Atlas, auto browse, Neon의 관찰 방식을 확인해 주는 출처는 열지 않았으므로, 보고서는 이 부분을 조심스럽게 표현해야 한다.
- 보안상의 절충이 이제 사용자가 직접 고르는 설정으로 드러나 있다(예: Cowork의 "Preferred browser", ChatGPT의 Chat 대 확장 프로그램 대 클라우드 브라우저). 격리 환경은 피해 범위(blast radius)를 제한하지만 자격 증명을 넘겨주어야 하고, 프로필 내 에이전트는 로그인된 모든 세션을 물려받는다.
- 벤더들은 고위험 사이트를 기본적으로 세션 공유에서 제외하고(Anthropic: 은행·이메일·SSO), 결제·자격 증명 사용에는 사람의 확인을 조건으로 건다(Google, OpenAI).

### 공백
- Comet, Atlas, Chrome auto browse, Edge Browse with Copilot이 모델에 정확히 무엇을 넣는지(스크린샷 대 DOM 대 접근성 트리) 밝힌 벤더 문서는 열지 못했다.
- ChatGPT 데스크톱 내장 브라우저가 OS 브라우저의 쿠키를 공유하는지: 찾지 못했다.

---

## Q3. 능력 수준: 벤치마크, 신뢰성, 사용자가 실제로 하는 일

### 핵심
고전적인 벤치마크에서 2026년의 시스템들은 포화 수준이거나 그 이상이며, 인용되는 인간 기준선도 넘어섰다(WebVoyager 약 99%, Online-Mind2Web 90~97%, OSWorld-Verified 약 85% 대 인간 약 72%). 그러나 더 어렵게 갱신된 벤치마크(OSWorld 2.0 약 71~78%, WebArena 약 74%, ClawBench 33%, HealthAdminBench 52%)와 언론 리뷰에서는 여전히 오류가 잦다. 실제 에이전트 트래픽은 검색·상품 탐색에 집중되어 있고, 결제 단계에 도달하는 것은 약 2%뿐이다.

### 인용 근거
- Steel.dev 집계 리더보드(최종 갱신 2026-10-11)의 상위 항목: WebVoyager — browser-control (Fable 5) 99.19%, Alumnium 98.5%, Surfer 2 97.1%; Online-Mind2Web — Browser Use Cloud (bu-max) 97.0%, GPT-5.4 Native Computer Use 93.0%, ABP + Claude Opus 4.6 90.53%; WebArena — WebTactix (DeepSeek v3.2) 74.3%, OpAgent 71.6%, ColorBrowserAgent 71.2%; OSWorld — Qwen3.8-Max 86.1%, Claude Mythos Preview 85.4%, Claude Mythos 5 85.0%; OSWorld 2.0 — Claude Fable 5.1 77.9%, Simular Sai 73.0%, GPT-6 Astra 72.6%; BrowseComp — GPT-5.6 Sol Ultra 92.2%, GPT-6 Astra 91.5%, Kimi K3 91.2%; ClawBench — Claude Sonnet 4.6 33.3%; HealthAdminBench — Claude Mythos 5 (browser-use) 51.9%. [opened; 집계 사이트이며 많은 점수가 자체 보고이다] — [leaderboard.steel.dev](https://leaderboard.steel.dev/)
- 다른 집계 사이트들은 OSWorld-Verified 1위를 다르게 적는다. Yutori(2026-08-07 갱신)는 "Intelligence-Indeed Agent" 90.2%를 올려 두었고, BenchLM(2026-10-15)은 Qwen3.8 Max 86.1%, Claude Fable 5 85%, Claude Mythos 5 85%를 올려 두었다. 인간 기준선은 보통 약 72%(72.36%)로 인용된다. [snippet] — [Yutori](https://yutori.com/leaderboards); [BenchLM](https://benchlm.ai/benchmarks/osWorldVerified)
- "컴퓨터 사용 에이전트가 OSWorld에서 85%에 도달했다. 2년 전의 12%에서 오른 것이다."("Computer-use agents hit 85% on OSWorld, up from 12% in two years.") [snippet, 기사 제목] — [cryptobriefing](https://cryptobriefing.com/computer-use-agents-85-percent-osworld-benchmark/)
- OSWorld 2.0은 더 어려운 별도의 프로토콜이다. Claude Opus 5(2026-07-24 출시)는 약 70.6%로 보고되었다(BenchLM, 2026-08-22). [snippet] — [BenchLM](https://www.benchlm.ai/benchmarks/osworld2)
- Online-Mind2Web = 실제 운영 중인 136개 사이트에 걸친 300개 과제. [snippet] — [Yutori](https://yutori.com/leaderboards)
- 리뷰에 나타난 신뢰성: 한국 언론이 전달한 바에 따르면, Gemini Spark는 Google 서비스와의 통합에서는 호평을 받았지만 사용자 개입이 필요한 오류(The Verge)와 참석자 누락·외부 예약 미완료(Wired)로 비판을 받았다. [opened] — [Daum/유미's 픽 2026-08-01](https://v.daum.net/v/20260801074138008). Google 스스로도 Tasks의 실수에 대해 "모두 없애지는 못했다"("we haven't eliminated all of them")고 말한다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- 속도: Claude in Chrome의 "Quick Mode"(2026년 3월)는 3배 빠르다고 소개된다. ChatGPT Work 작업은 사람이 지켜보지 않아도 몇 시간씩 실행될 수 있다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026); [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- 에이전트가 실제 환경에서 하는 일(HUMAN Security 네트워크 텔레메트리): 2026년 5월 — 상품 및 검색 경로가 에이전트 트래픽의 76.4%, 인증 5.6%, 결제 2.4%. 전자상거래, 미디어, 여행이 대부분을 차지한다. 2026년 7월에는 미디어 43.5%가 전자상거래 42.0%를 앞질렀다. 연간 보고서: 에이전트 트래픽은 전년 대비 +7,851%, 에이전트 활동의 2.3%가 결제 단계에서 발생. [snippet] — [HUMAN 2026년 5월](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-may-2026-financial-services-agentic-traffic-continues-to-climb-more-than-doubling-this-month/); [HUMAN 2026년 7월](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-july-2026-publishers-claim-highest-share-of-agentic-traffic/); [HUMAN 2026 벤치마크 보고서](https://www.humansecurity.com/2026-state-of-ai-traffic-cyberthreat-benchmark-report/)
- 벤더가 제시하는 사용 사례: 커넥터가 없는 내부 대시보드·레거시 시스템·협력업체 포털, 양식 작성, 이메일 답장 초안 작성(Anthropic) [opened] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available); 백그라운드에서의 청구서 수집과 조사(Cowork 브라우저) [opened] — [Anthropic](https://claude.com/resources/articles/cowork-built-in-browser); 아파트 방문 예약, 항공편 조사, 주차장 검색(Google) [opened] — [Thurrott](https://www.thurrott.com/a-i/339900/googles-gemini-spark-agent-expands-to-more-countries-and-adds-new-chrome-features); [Android Authority](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/)

### 추론
- 벤치마크 수치는 대부분 벤더가 스스로 보고한 것을 집계 사이트가 모은 것이며 하네스가 서로 다르다. WebVoyager/Online-Mind2Web의 상위 항목은 순수 모델이 아니라 에이전트 스캐폴드이다. 보고서는 이 수치들을 "보고된" 값으로 제시하고 벤치마크 간 비교를 피해야 한다.
- WebVoyager 약 99%와 실제 에이전트 트래픽 중 결제 도달 약 2% 사이의 간극은, 실제 사용이 여전히 읽기·비교 작업 위주이고 거래는 사람의 확인을 거쳐야 진행된다는 점을 시사한다.

### 공백
- 공식 리더보드(os-world.github.io, Online-Mind2Web HAL)와 모델 시스템 카드는 열지 않았다. 벤더 시스템 카드의 수치는 하나도 검증하지 못했다.
- 소비자 제품(Comet, auto browse, Work)의 일반적인 작업 소요 시간이나 성공률에 관한 신뢰할 만한 공개 데이터는 찾지 못했다.
- 리더보드에 나오는 모델 이름(GPT-6 Astra, Gemini 4, Qwen3.8, Fable 5.1 등)은 조사자의 배경지식보다 나중의 것이어서, 집계 사이트에 표시된 그대로 적었다.

---

## Q4. 보급과 시장 사실

### 핵심
에이전트형 브라우저는 브라우저 시장 점유율에서 여전히 반올림 오차 수준이며(StatCounter 상위 6위 안에 하나도 없다. Chrome 약 68%), 감사를 거친 사용자 수를 공개하는 벤더는 없다. 관찰 가능한 가장 좋은 대리 지표는 HUMAN Security의 에이전트 트래픽 점유율인데, 여기서 Comet은 연중 약 40~48%를 유지하고, Claude의 확장 프로그램은 약 22~24%로 올랐으며, Atlas는 약 21%에서 서비스 종료까지 떨어졌다.

### 인용 근거
- StatCounter 기반 2026년 7월 현황: 전 세계 상위 6위 안에 AI 네이티브 브라우저는 없다. Chrome 약 68%, Firefox 3.3%. [snippet] — [dupple.com](https://dupple.com/learn/best-ai-browser)
- HUMAN Security가 관찰한 에이전트 트래픽의 월별 점유율: 2026년 4월 — Comet 48.12%, Atlas 21.33%, Claude Chrome 확장 프로그램 17.33%, ChatGPT Agent 8.55%; 5월 — Comet 47%, Atlas 20.3%; 6월 — Comet 47.6%, Claude 20.8%, Atlas 16.5%; 7월 — Comet 47.1%, Claude 24%, Atlas 15.5%; 8월 — Comet 40.7%, Claude 22%, Codex 첫 등장 7.3%, 전체 에이전트 트래픽은 +27%로 새 최고치. [snippet; 8월 페이지는 열려고 했을 때 HTTP 403을 반환했다] — [HUMAN 4월](https://humansecurity.com/learn/blog/state-of-agentic-traffic-april-26); [HUMAN 6월](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-june-2026-browser-agent-tooling-for-developers-is-catching-on-fast/); [HUMAN 8월](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-august-2026-agentic-traffic-grows-27-reaches-new-high-as-codex-debuts-strongly/); 5월 수치는 [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)를 통해서도 [opened]. 단서: HUMAN이 보호하는 사이트만 대상이며, 식별은 일부 user-agent에 의존한다.
- Comet 사용자 수는 서로 엇갈린다: 2026년 1분기 MAU 약 1,800만(제3자 추적 사이트), MAU 300만(Similarweb 기반), "2026년 초까지 사용자 1,000만"("10M users by early 2026")(리뷰 사이트). Perplexity는 검증된 수치를 공개한 적이 없다. 2026년 초까지 모바일 다운로드는 100만 초과. [snippet] — [presenc.ai](https://presenc.ai/research/comet-usage-statistics-2026); [aitoolanalysis](https://aitoolanalysis.com/comet-browser-review/)
- OpenAI는 Atlas의 사용량이나 비용 수치를 공개하지 않았다. [opened] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- 규모 비교를 위한 맥락: 활성 사용자 ChatGPT 약 11억, Gemini 약 9억, Claude 약 2억 4,500만(TidBITS가 2026년 7월에 인용). Codex는 주간 사용자 500만 초과이며 그중 100만 초과가 소프트웨어 개발 이외의 사용자이다. [opened] — [tidbits](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/); [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Google은 무료 Tasks가 "수억 명"("hundreds of millions of people")에게 도달한다고 표현한다. Chrome 사용자 기반은 약 30억. Android auto browse의 목표는 2026년 말까지 기기 2억 대이다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/); [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- 가격 현황: Comet 무료(Pro $20, Max $200). Opera Neon 월 $19.90에 2026-08-14부터 무료 요금제 추가. Claude in Chrome은 모든 유료 Claude 요금제. Chrome auto browse는 AI Pro($19.99) 또는 Ultra 필요. Gemini Tasks는 6개국에서 무료. Browse with Copilot은 Microsoft 365 Premium 필요(미국). ChatGPT Work 클라우드 브라우저는 유료 요금제(Free/Go 제외)에서 Codex 방식의 사용 크레딧으로 제공. Brave Leo Premium $14.99. [opened/snippet, Q1에 인용된 대로]
- 종료·전환: Operator(2025년 8월), Project Mariner(2026년 5월), Copilot Mode 브랜드(2026년 5월), ChatGPT Atlas(2026년 8월), ChatGPT agent mode(2026년 8~10월까지), Arc(유지보수만), Dia는 Atlassian에 매각, Manus-Meta 거래 무산(2026년 8월). [Q1에 인용된 대로]
- Mariner 종료에 대한 Android Authority의 해석: Claude Code와 OpenClaw 같은 코딩·파일 에이전트가 웹을 클릭하며 돌아다니는 에이전트보다 더 인기를 얻었다. [opened] — [Android Authority](https://www.androidauthority.com/google-project-mariner-shutdown-3664323/)
- 기업: MDM을 지원하는 Comet 기업용(2026년 3월). Claude in Chrome의 관리자 도메인 허용 목록. Chrome auto browse는 관리형 사용자에게 제공되지 않음. Samsung Browsing Assist는 10억 대 초과 기기에 도달한다고 주장(Perplexity의 주장). [위에 인용된 대로]

### 추론
- 시장은 "새 브라우저"에 거는 방식에서 기존 강자(Chrome, Edge, Samsung)와 어시스턴트 앱을 통한 배포로 옮겨 갔다. Comet을 제외한 단독 AI 브라우저는 측정 가능한 점유율을 보여 주지 못했다.
- 무료 배포가 2026년의 추세이며(Comet 2025년 10월, Neon 2026년 8월, Gemini Tasks 2026년 11월), 이는 보안 성숙도와 무관하게 노출되는 사용자 집단이 빠르게 늘어남을 뜻한다.

### 공백
- 어떤 제품에 대해서도 감사를 거친 사용자 수가 없다. 이름이 알려진 주목할 만한 기업 도입 사례도 찾지 못했다.
- HUMAN 데이터는 직접 열지 못했고(403), 수치는 검색 요약에서 가져왔다.

---

## Q5. 한국 내 제공 여부와 한국 제품

### 핵심
한국에는 어시스턴트 수준의 기능이 먼저 들어왔고(Gemini in Chrome, 2026년 4월) 에이전트는 그 뒤에 들어왔다. Gemini Spark가 2026-07-30에 유료 사용자에게, 무료 Gemini Tasks가 2026-11-17에 제공되었다(한국은 6개 출시국 중 하나이다). 반면 Chrome auto browse와 결제·전화 기능은 여전히 미국 한정이다. 국내 사업자(Naver Whale)는 지금까지 어시스턴트 기능만 출시했고 에이전트 기능은 "연내"로 약속만 한 상태이다.

### 인용 근거
- Gemini in Chrome(요약, 비교, 초안 작성. Gemini 3.1)은 2026년 4월에 한국과 다른 APAC 6개 시장에 배포되었다(한국 언론 보도 2026-04-21 전후). auto browse는 포함되지 않았다. [snippet] — [Asia Economy](https://www.asiae.co.kr/en/article/2026042108250717018); [Thurrott](https://www.thurrott.com/?p=335143)
- Gemini Spark는 2026-07-30에 한국에서 Google AI Pro/Ultra 구독자를 대상으로 출시되었고, 한국어와 영어는 단계적으로 확대된다. 사용자는 반복 작업 지시를 한국어로 내릴 수 있다. 업계의 시각: 성패는 한국어 작업 이해도, 정확성, 개인정보에 대한 신뢰에 달려 있다. [opened] — [Daum (아이뉴스 계열 '유미's 픽') 2026-08-01](https://v.daum.net/v/20260801074138008). **상충: 다른 한국 출처는 7월 중순이며 월 KRW 119,000의 AI Ultra 전용이라고 한다** [snippet] — [ditoday](https://ditoday.com/%ea%b5%ac%ea%b8%80-%ec%a0%9c%eb%af%b8%eb%82%98%ec%9d%b4-%ec%8a%a4%ed%8c%8c%ed%81%ac-%ea%b5%ad%eb%82%b4-%ec%b6%9c%ec%8b%9c-24%ec%8b%9c%ea%b0%84-%ea%b0%9c%ec%9d%b8-%ec%97%90%ec%9d%b4%ec%a0%84/)
- 2026-11-17: Tasks가 한국, 미국, 캐나다, 일본, 호주, 인도에서 무료. 전화 걸기, Google Pay, Password Manager 로그인, 문자·이메일을 통한 지시는 미국 한정이다. [opened] — [DigitalToday 2026-11-18](https://www.digitaltoday.co.kr/en/view/127105/google-revamps-ai-agent-gemini-spark-into-tasks-adds-support-in-south-korea); [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- Google 한국 블로그(2026-05-19)는 Android용 Gemini in Chrome을 다루면서 Android용 auto browse가 6월로 예정되어 있다고 언급했다(미국). [snippet] — [Google 한국 블로그](https://blog.google/intl/ko-kr/products/android-play-hardware/bringing-chrome-ai-to-android-kr/)
- Samsung Browser의 에이전트 기능(Perplexity 기반)은 한국과 미국에서 제공된다고 한다. [opened, 3차 자료] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Naver: AI탭이 2026-06-26에 모든 사용자에게 출시되어 2026-07-15까지 사용자 1,000만 명을 넘었다. Naver는 연내에 부동산, Whale, 건강 에이전트를 도입하겠다고 밝혔다. Whale "AI 챗"(페이지 요약·번역, 질의응답, 탭 정리, Naver Calendar)은 2026-10-01에 정식 출시되었다. "Whale AI 브라우저"는 2026년 안에 내놓겠다고 약속한 상태이다. [snippet] — [Asia Economy 2026-07-15](https://view.asiae.co.kr/article/2026071509404690281); [CEO Score Daily 2026-10-01](https://www.ceoscoredaily.com/page/view/2026100111144358607); [NewsTomato](https://newstomato.com/ReadNews.aspx?no=1313642)
- Claude in Chrome은 모든 유료 Claude 요금제에서 제공되며, 게시물에는 국가 제한이 명시되어 있지 않다. [opened] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available)

### 추론
- 2026년 11월의 한국 사용자가 현실적으로 쓸 수 있는 브라우저 에이전트는 Gemini Tasks(무료, 클라우드 VM), Claude in Chrome / Cowork 브라우저(유료), Comet(무료), ChatGPT Work(유료), Opera Neon, Samsung Browsing Assist이다. Chrome auto browse와 Edge Browse with Copilot은 쓸 수 없다.
- 한국어 품질은 벤더들이 문서화하지 않았다. 한국어를 명시적으로 단계 적용하고 있다고 보도된 곳은 Google뿐이다.

### 공백
- Kakao의 브라우저 에이전트 기능에 관한 출처가 없다. Whale의 에이전트 기능이 2026년 11월 기준으로 출시되었는지도 확인하지 못했다.
- Comet/ChatGPT Work/Claude의 한국어 지원에 관한 언급은 찾지 못했다.
- Samsung의 한국 내 제공 여부는 2차 출처에만 근거한다.

---

## Q6. 모바일: 브라우저 에이전트 작업은 브라우저 앱 안에서 이루어지는가, OS 화면 제어로 이루어지는가?

### 핵심
휴대전화에서 출시된 제품들은 브라우저 에이전트 작업을 브라우저 앱 안에서(Android/iOS의 Comet, Android의 Chrome auto browse, Samsung Browser) 또는 벤더 클라우드에서(Gemini Tasks, ChatGPT Work) 수행하며, 사용자가 실제로 쓰고 있는 휴대전화의 화면을 제어하는 방식이 아니다. Google의 별도 기능인 Android "task automation"은 기기 내 가상 휴대전화에서 실행된다. 확장 프로그램 기반 에이전트(Claude in Chrome)는 모바일에 존재하지 않는다.

### 인용 근거
- Android의 Chrome auto browse는 Chrome 앱의 기능이며(AI Pro/Ultra, 미국, Android 12 이상, RAM 4 GB, en-US), 구매와 저장된 자격 증명 단계에서는 제어권을 되돌려준다. iOS에는 없다. [opened] — [Android Authority](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/); [snippet] — [unite.ai](https://www.unite.ai/gemini-in-chrome-opens-to-all-u-s-android-users-as-auto-browse-goes-mobile/)
- Comet Android(2025-11-20)와 iOS(2026-03-18)는 어시스턴트가 내장된 무료 브라우저로, 어시스턴트가 탭 내용을 보고 사용자 대신 탐색하고 작업을 자동화할 수 있다. 모바일에서는 확장 프로그램을 지원하지 않는다. [snippet] — [Thurrott](https://www.thurrott.com/mobile/333936/perplexity-launches-its-comet-browser-on-ios-and-ipados); [Engadget](https://engadget.com/apps/perplexitys-comet-ai-browser-is-now-available-for-iphones-183947569.html)
- Claude in Chrome은 "모바일에서는 아직 ... 실행되지 않는다"("does not run ... on mobile yet"). 모바일 앱은 데스크톱 Chrome이나 데스크톱 앱 내장 브라우저에서 실행되는 세션을 이어 가거나 조작할 수 있다. [opened] — [Anthropic 정식 출시 게시물](https://claude.com/blog/claude-in-chrome-generally-available); [Anthropic Cowork 브라우저](https://claude.com/resources/articles/cowork-built-in-browser)
- 검토한 출처들에서 Atlas는 데스크톱 전용이었다. 종료 이후 모바일 사용자는 웹·모바일의 ChatGPT Work 클라우드 브라우저를 통해 브라우저 에이전트 작업을 이용한다. [opened] — [letsdatascience](https://letsdatascience.com/news/openai-shutters-standalone-chatgpt-atlas-desktop-browser-0cc642c2); [omidsaffari.com](https://omidsaffari.com/blog/chatgpt-agent-retired)
- Gemini Tasks는 Gemini 앱·웹사이트에서 사용하며 사용자별 클라우드 VM에서 실행된다. 반면 Android task automation(2026-11-10)은 기기 내 가상 휴대전화에서 실행된다. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- Edge 모바일에는 Copilot 다중 탭 추론, Vision, Voice, Journeys(미국)가 있지만 Browse with Copilot(동작 수행)은 Edge 데스크톱 전용이다. [opened, 1차 자료] — [Microsoft](https://blogs.windows.com/msedgedev/2026/05/13/new-updates-to-edge-across-desktop-and-mobile/)
- Gemini 3.5 Flash computer use는 개발자를 위해 모바일 환경을 지원한다. [snippet] — [Neowin](https://www.neowin.net/news/google-adds-built-in-computer-control-to-gemini-35-flash/)

### 추론
- 모바일 브라우저 에이전트는 해당 브라우저 앱의 쿠키·로그인을 물려받는 앱 내 기능(분류 b)이거나, 별도 세션을 가진 클라우드 에이전트(분류 c)이다. OS 수준의 화면 제어는 샌드박스 형태(가상 휴대전화)이거나 개발자 API로만 존재한다.
- iOS는 제약이 가장 심한 환경이다. 그곳에서 브라우저 내 에이전트를 제공하는 것은 Comet뿐이다.

### 공백
- Comet 모바일이나 Chrome Android 에이전트가 페이지를 어떻게 관찰하는지에 관한 기술적 세부 사항은 찾지 못했다.
- Atlas가 모바일 버전을 출시한 적이 있는지: 검토한 출처들은 데스크톱만 언급한다. 확정적으로 검증하지는 못했다.
- 한 출처는 Android auto browse를 "OS 수준"("at the OS level")이라고 설명하고(No Hacks), 다른 출처는 Chrome 앱의 기능이라고 한다(Android Authority). 해소하지 못했다.

---

## Q7. 2024년부터 2026년 11월까지 시장이 움직인 과정 (타임라인)

### 핵심
2024~25년은 스크린샷 기반 "computer use"와 단독 에이전트 제품의 출시 단계였다. 2025년 말은 AI 브라우저 붐이었다. 2026년은 범용 업무 에이전트로의 통합, 무료 배포, 이중 아키텍처(프로필 내 대 격리)의 해였다.

### 인용 근거
- 2024-10 Anthropic computer use 베타; 2024-12 Project Mariner 프로토타입; 2025-01-23 Operator; 2025-05 클라우드 VM의 Mariner(AI Ultra), Genspark 브라우저; 2025-07 Comet, Edge Copilot Mode, ChatGPT agent(07-17); 2025-08 Claude for Chrome 파일럿, Operator 종료; 2025-09 Opera Neon, Atlassian-TBC 거래(10-20 완료); 2025-10 Comet 무료화, Gemini 2.5 Computer Use, ChatGPT Atlas(10-21), Copilot Actions 프리뷰; 2025-11 Comet Android; 2025-12 Nova Act 정식 출시, Brave AI browsing Nightly, Claude in Chrome을 Pro/Team/Enterprise로 확대, Meta-Manus 거래. [memory + Q1에 인용된 출처]
- 2026-01-28 Chrome auto browse(미국, 유료); 2026-03 Comet iOS, Comet 기업용, Perplexity를 탑재한 Windows용 Samsung Browser, Mariner 팀 재배치; 2026-04 한국/APAC에 Gemini in Chrome; 2026-05 Mariner 종료, Copilot Mode 종료(05-13), Gemini Spark(미국), Android용 auto browse 발표; 2026-07-09 ChatGPT Work + Atlas 단계적 종료 발표; 2026-07-30 Spark가 한국을 포함한 160개 이상 국가로 확대; 2026-08-09 Atlas 작동 중단; 2026-08-12/26 Claude in Chrome이 Cowork 클라이언트가 된 뒤 정식 출시, Cowork 내장 브라우저; 2026-08-14 Neon 무료 요금제; 2026-08-18 미국의 모든 Android에 Gemini in Chrome; 2026-09-01 Firefox Smart Window; 2026-10-13 Gemini Chat/Task 토글; 2026-10-15 ChatGPT 확장 프로그램의 탭 넘겨받기; 2026-11-10 Android task automation 확대; 2026-11-17 한국을 포함한 6개국에서 Gemini Tasks 무료. [Q1~Q6에 인용된 출처]

### 추론
- 변화의 방향: (1) 단독 제품 -> 내장 기능; (2) 유료 프리뷰 -> 무료 대규모 배포; (3) 동작별 승인 -> 분류기가 걸러 주는 자율 실행(Anthropic). 다만 결제·자격 증명에는 사람의 확인을 유지한다(Google, OpenAI); (4) 에이전트 결제 프로토콜(AP2, ChatGPT Wallet 2026-11-13)이 함께 등장하고 있다.

### 공백
- 2026년 사건의 날짜는 대부분 2차 출처에 의존한다. 위 항목별 표시를 참고하라.
