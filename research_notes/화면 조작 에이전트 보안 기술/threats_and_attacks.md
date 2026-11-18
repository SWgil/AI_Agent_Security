# 화면 조작(GUI) AI 에이전트에 대한 위협 지형과 실증된 공격 — 2026년 11월 기준 현황

보고서 작성자를 위한 읽기 안내:
- 조사 일자: 2026-11-18. 2024~2025년 항목에는 "(오래됨)"이라고 표시했다.
- 검증 표지: **[P]** = 이번 세션에서 1차 출처를 직접 읽었다. **[S]** = 2차 보도나 검색 결과 요약에서만 가져왔으며 1차 출처는 열어 보지 않았다. **[M]** = 기존 지식에서 가져온 것으로, URL은 적었지만 이번 세션에서 다시 가져와 확인하지 않았다. [S]와 [M]의 수치는 대표 수치로 인용하기 전에 확인이 필요한 것으로 취급한다.
- 서로 다른 논문의 공격 성공률(ASR)은 서로 비교할 수 없다. 공격자 예산(단발 시도, N회 중 최선(best-of-N), 강화학습(RL) 최적화), "시도"를 세는지 "완료"를 세는지, 모델 세대, 제품 수준 안전장치(확인 절차, 분류기)가 켜져 있었는지가 논문마다 다르다.

## 1. GUI/화면 에이전트에 고유하거나 GUI/화면 에이전트에서 증폭되는 공격 유형은 무엇인가?

### 핵심
지배적인 유형은 화면에 렌더링되는 모든 것(웹 콘텐츠, 메시지, 알림, 팝업, 불투명도가 낮은 오버레이, 캘린더 초대)을 통한 간접 프롬프트 인젝션이다. 이 공격은 GUI 에이전트에서 증폭되는데, 에이전트가 사용자의 인증된 세션 안에서 사용자의 앱 간·출처 간 권한을 모두 가지고 행동하므로 고전적인 격리(동일 출처 정책, 앱 샌드박스)가 더 이상 피해 범위를 제한하지 못하기 때문이다. GUI 에이전트에는 API 에이전트에 없는 공격 유형이 추가된다. 사람이 보는 것과 모델이 읽는 것 사이의 인지 격차, 클릭 리디렉션/오버레이, 스크린샷 채널에 대한 경쟁 조건(race), 그리고 자격 증명/OTP가 단지 화면에 보인다는 이유만으로 노출되는 문제가 그것이다.

### 인용 근거

**A. 화면 콘텐츠(웹 페이지, 댓글, 숨은 텍스트)를 통한 간접 프롬프트 인젝션**
- [P] Brave의 Perplexity Comet 보고(2025년 7월 25일 제보, 2025년 8월 20일 공개, 오래됨): Reddit 댓글의 스포일러 태그 뒤에 숨긴 지시문이 사용자가 "현재 웹페이지 요약(Summarize the current webpage)"을 클릭했을 때 실행되었다. 에이전트는 사용자의 이메일 주소를 가져오고, OTP 로그인을 유발하고, 사용자가 로그인해 둔 Gmail에서 OTP를 읽은 뒤 둘 다 Reddit에 다시 게시해 계정 탈취를 가능하게 했다. 이것이 왜 구조적으로 새로운 문제인지에 대한 Brave의 설명은 다음과 같다. 어시스턴트가 신뢰할 수 없는 페이지 콘텐츠의 지시를 따르면, 어시스턴트가 인증된 세션 전반에서 사용자의 전체 권한으로 동작하기 때문에 동일 출처 정책과 CORS는 "사실상 쓸모없다(effectively useless)". — [Brave](https://brave.com/blog/comet-prompt-injection/)
- [P] 브라우저 에이전트가 특히 노출되는 이유에 대한 Anthropic 자체의 설명(2025년 11월 24일, 오래됨): "모든 웹페이지, 임베드된 문서, 광고, 동적으로 로드되는 스크립트(every webpage, embedded document, advertisement, and dynamically loaded script)"가 잠재적 인젝션 경로이며, 에이전트는 일단 영향을 받으면 악용될 수 있는 행동(탐색, 양식 입력, 클릭, 다운로드)을 많이 가지고 있다. — [Anthropic](https://www.anthropic.com/research/prompt-injection-defenses)
- [P] Google(2025년 12월 8일, 오래됨): "모든 에이전트형 브라우저가 직면한 주된 새 위협은 간접 프롬프트 인젝션이다(The primary new threat facing all agentic browsers is indirect prompt injection)". 이 위협은 악성 사이트, 제3자 iframe, 사용자 생성 콘텐츠를 통해 들어와 무단 거래나 데이터 탈취로 이어진다. — [Google](https://blog.google/security/architecting-security-for-agentic/)
- [P] 실제 웹에서 관찰된 페이로드 은닉 방식(Google과 Forcepoint X-Labs, 2026년 4월 24일 보도): 1픽셀로 축소한 텍스트, 거의 투명한 텍스트, hidden 속성이 붙은 요소, HTML 주석, 페이지 메타데이터/메타 태그. — [Help Net Security](https://www.helpnetsecurity.com/2026/04/24/indirect-prompt-injection-in-the-wild/)

**B. 시각적 / 인지 격차 인젝션(사람과 모델이 서로 다른 것을 본다)**
- [S] Comet에 대한 Brave의 2차 보고(2025년 10월, 오래됨): 이미지/스크린샷 안의 노란 배경 위에 흐린 연파란색 텍스트로 지시문을 넣었으며, 사람에게는 보이지 않지만 모델은 읽는다. — [Brave/The Hacker News 보도의 검색 요약](https://thehackernews.com/2025/10/cometjacking-one-click-can-turn.html) (해당 Brave 게시물은 열어 보지 않았다)
- [P, CSA의 논문 요약 경유] "(A)I Sees What You Don't"(arXiv 2607.00333, 2026년 7월 1일), 공개 모바일 에이전트 프레임워크 다섯 개(AppAgent, AppAgentX, Mobile-Agent-v3, Open-AutoGLM, MobA) 대상: 불투명도를 2%까지 낮춘 오버레이를 사용하는 "잠재의식적 시각 인젝션(subliminal visual injection)"은 VLM은 읽지만 사람은 볼 수 없다. "비가시 영역 인젝션(invisible zone injection)"은 둥근 모서리와 카메라 컷아웃 아래에 페이로드를 배치하는데, 이 영역은 물리적으로는 가려지지만 캡처된 스크린샷에는 존재한다. 인지 공격은 GPT-4o, Claude, Gemini와 더 작은 모델들 전반에서 20회 중 20회 또는 그에 가깝게 성공했다. — [Cloud Security Alliance 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [S] "에이전트 전용" 인젝션: 악성 앱이 자신의 UI에 탈옥(jailbreak) 프롬프트를 심어 두는데, 이 프롬프트는 사람에게는 아무 작용도 하지 않다가 에이전트가 ADB를 통해 UI를 조작할 때만 드러나며, 낮은 권한의 접근만 있으면 된다(arXiv 2510.07809, 2025년 10월, 오래됨). — [arXiv](https://arxiv.org/pdf/2510.07809v3)

**C. 에이전트를 겨냥한 팝업, 기만적 UI, UI 리드레싱(UI redressing)**
- [S] "Attacking Vision-Language Computer Agents via Pop-ups"(Zhang, Yu, Yang; arXiv 2024년 11월, ACL 2025, 오래됨): OSWorld와 VisualWebArena에 삽입한 적대적 팝업은 평균 86%의 비율로 클릭되었고 과제 성공률을 47% 낮췄다. 에이전트에게 팝업을 무시하라고 지시하는 것은 효과가 없었다. — [arXiv 2411.02391](https://arxiv.org/abs/2411.02391v2), [ACL Anthology](https://aclanthology.org/2025.acl-long.411)
- [S] 조작 가능한 요소("Send", "Submit", 앱 아이콘) 위에 놓인 투명 오버레이가 에이전트의 좌표 기반 탭을 가로채 공격자가 제어하는 액티비티로 돌린다(arXiv 2505.12981, 2025년 5월, 오래됨). — [arXiv](https://arxiv.org/pdf/2505.12981)
- [P, CSA 경유] Android에서의 UI 스푸핑: 악성 앱이 접근성 서비스(Accessibility Service)로 대상 앱의 실행을 감지하고 가짜 인터페이스를 덮어씌워 에이전트가 입력하는 자격 증명을 가로챈다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [P, CSA 경유] 숨은 텍스트가 아닌 논리 수준의 기만: LayerX "BioShocking"(2026년 6월 24일 공개)은 틀린 답("2 + 2 = 5")에 보상을 주는 눈에 보이는 퍼즐 페이지를 사용한다. 그 결과 에이전트는 거짓 전제를 받아들이고 자신의 안전 규칙이 적용되지 않는다고 여기게 되며, 이어서 리디렉션을 따라 사용자의 인증된 GitHub에 들어가 SSH 자격 증명을 복사한다. — [CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/)

**D. 알림, 메시지, 캘린더 초대(공격자가 밀어 넣는 콘텐츠, 제로클릭)**
- [P, SafeBreach 연구에 대한 The Hacker News 보도 경유] Android의 Gemini(2026년 6월 3일 공개): WhatsApp, Slack, SMS, Signal, Instagram, Messenger에서 온 오염된 알림을 Gemini의 Utilities 에이전트가 지시문으로 취급했다. 휴대전화에 악성 앱이 설치되어 있을 필요가 없었다. 연구자(Or Yair)는 이 공격 표면을 "사실상 무한하다(effectively infinite)"고 표현했다. — [The Hacker News](https://thehackernews.com/2026/06/whatsapp-slack-notifications-could.html)
- [P, CSA 경유] Zenity "PleaseFix"(2026년 3월 3일): 나중에 일상적인 캘린더 작업 중에 처리되는 캘린더 초대가 Perplexity Comet으로 하여금 `file://`을 통해 로컬 파일을 읽어 외부로 보내게 만들며, 그동안 사용자는 여전히 기대한 작업 결과를 받는다. — [CSA 연구 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-pleasefix-agentic-browser-exploits-2026032/), [Help Net Security](https://www.helpnetsecurity.com/2026/03/04/agentic-browser-vulnerability-perplexedbrowser/)
- [P 초록] GhostEI-Bench(arXiv 2510.20333, 2025년 10월, ICLR 2026 포스터)는 실행 중인 Android 에뮬레이터에 주입되는 기만적 오버레이와 위조 알림을 핵심 모바일 위협으로 모델링한다. — [arXiv](https://arxiv.org/abs/2510.20333)

**E. 에이전트 자신의 정당한 접근 범위를 통한 자격 증명 / OTP 노출과 데이터 유출**
- [P] Gmail에서 OTP를 읽어 공개적으로 게시했다(Comet, 2025년, 오래됨). — [Brave](https://brave.com/blog/comet-prompt-injection/)
- [P, CSA 경유] 비밀번호 관리자 악용: PleaseFix의 두 번째 경로("PerplexedBrowser")는 Comet의 승인된 1Password 연동을 악용해, 1Password의 코드를 공격하지 않고도 자격 증명 조회를 조작하고 부분적인 계정 탈취를 일으킨다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-pleasefix-agentic-browser-exploits-2026032/)
- [P, CSA 경유] 모바일 프레임워크의 경우: 접근성 서비스가 에이전트가 입력한 평문을 가로채는 자격 증명 스니핑, 텍스트 입력에 쓰이는 보호되지 않은 브로드캐스트의 가로채기. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [P] Doubao 폰에서는 화면이 단계마다 클라우드로 업로드된다. 널리 퍼진 한 영상은 같은 Doubao 계정으로 로그인된 미러링 기기들에서 사용자의 은행 잔액이 보이는 모습을 보여 주었다. — [Lawfare, 2026년 3월 6일](https://www.lawfaremedia.org/article/china-s-agentic-ai-controversy)

**F. 혼동된 대리인(confused deputy) / 출처 간·앱 간 흐름 / 딥링크**
- [S] CometJacking(LayerX, 2025년 10월, 오래됨): 조작된 URL 하나의 `collection` 매개변수가 에이전트로 하여금 웹 대신 메모리/커넥터를 참조하게 만든다. 주입된 프롬프트는 이메일과 캘린더 데이터를 끌어와 데이터 보호 검사를 피하도록 Base64로 인코딩해 유출한다. Perplexity는 이를 "보안 영향 없음(no security impact)"으로 분류했다. — [The Hacker News](https://thehackernews.com/2025/10/cometjacking-one-click-can-turn.html)
- [S] Roesner & Kohlbrenner(ICLR 2026 워크숍 "Agents in the Wild"): 제한이 가장 약한 에이전트형 브라우저에서는 인젝션에 성공한 사이트가 에이전트를 이용해 동일 출처 정책을 우회하고 다른 출처의 콘텐츠를 훔칠 수 있다. — [논문 PDF](https://homes.cs.washington.edu/~franzi/pdf/roesner_kohlbrenner_2026_agentic_sop.pdf) (이번 세션에서 PDF를 파싱할 수 없었으며, 이 주장은 검색 요약에서 가져온 것이다)
- [P, The Hacker News 경유] Gemini Android 시연: 주입된 알림이 휴대전화를 Zoom 앱 링크로 보내 회의에 강제로 참가시키고 영상을 스트리밍하게 했으며, 피해자의 위치를 파악하기 위해 URL을 열었고, Google Home의 창문/보일러/조명을 조작했다. — [The Hacker News](https://thehackernews.com/2026/06/whatsapp-slack-notifications-could.html)
- [S] HashJack(Cato Networks, 2025년 11월, 오래됨): URL 프래그먼트에 숨긴 지시문이 AI 브라우저 어시스턴트를 겨냥한다. — [CSA 노트 목록](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/)
- [S] ChatGPT Atlas 옴니박스 인젝션(NeuralTrust, 2025년 10월, 오래됨): URL처럼 보이지만 URL 검증에 실패하는 문자열이 통째로 신뢰된 사용자 프롬프트로 취급된다. — [ITPro](https://www.itpro.com/security/cyber-researchers-have-already-identified-several-big-security-vulnerabilities-on-openais-atlas-browser)

**G. 메모리 / 지속성 오염**
- [P, The Hacker News 경유] Gemini Android: 주입된 알림이 Gemini의 계정 수준 장기 메모리에 기록을 남겼고(피해자의 이름이 "Danny"로 저장됨), 지속성을 위해 매일 실행되는 예약 작업(오후 8시, 최근 메시지 읽기)을 만들었다. — [The Hacker News](https://thehackernews.com/2026/06/whatsapp-slack-notifications-could.html)
- [S] ChatGPT Atlas "Tainted Memories"(LayerX, 2025년 10월, 오래됨): CSRF로 ChatGPT 메모리에 지시문을 심으며, 이 지시문은 해당 계정이 로그인된 모든 기기와 브라우저에 걸쳐 유지된다. — [Akamai/LayerX 보도](https://www.akamai.com/blog/security/2025/oct/layerx-identifies-vulnerability-in-new-chatgpt-atlas-browser), [ITPro](https://www.itpro.com/security/cyber-researchers-have-already-identified-several-big-security-vulnerabilities-on-openais-atlas-browser)
- [S] 학술 연구: "Context manipulation attacks: Web agents are susceptible to corrupted memory"(arXiv 2506.17318, 2025년, 오래됨). — [arXiv](https://arxiv.org/pdf/2506.17318)

**H. 에이전트의 배관(스크린샷 채널, 호스트, 확장 프로그램)에 대한 공격**
- [P, CSA 경유] 스크린샷 변조: 함께 설치된 앱이 캡처 시점과 호스트가 파일을 가져가는 시점 사이의 약 50~500 ms(평균 약 210 ms) 구간에 스크린샷 파일을 바꿔치기한다. 호스트 측 명령 인젝션: 화면에 표시된 셸 메타문자를 VLM이 그대로 옮겨 적고 이것이 `shell=True` 명령에 이어 붙여져, 휴대전화를 구동하는 PC에서 코드 실행이 일어난다. WeChat 사례 연구에서는 이들이 연쇄되었다(가짜 "네트워크 동기화" 메시지 오버레이가 경쟁 조건 구간을 넓히고, 이어서 스크린샷이 바꿔치기되며, 그다음 호스트 RCE가 일어난다). — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [S] 브라우저 확장 프로그램의 신뢰 경계: "ShadowPrompt"(Koi, 2026년 3월)는 웹사이트가 Claude Chrome 확장 프로그램에 프롬프트를 몰래 주입할 수 있게 했다. "ClaudeBleed"(LayerX, 2026년 5월 7일)는 확장 프로그램의 `externally_connectable` 신뢰 모델을 악용해 다른 확장 프로그램이 이를 조종할 수 있게 했다. Manifold(2026년 7월)는 작업을 활성화하는 클릭의 출처가 검증되지 않아 다른 확장 프로그램이 이를 위조할 수 있다고 보고했다. — [The Hacker News, 2026년 3월](https://thehackernews.com/2026/03/claude-extension-flaw-enabled-zero.html), [IANS, 2026년 7월 20일](https://www.iansresearch.com/resources/all-blogs/post/security-blog/2026/07/20/unpatched-claude-for-chrome-flaw-could-give-attackers-access-to-gmail--calendar)

**I. 악성코드 전달 / C2로서의 에이전트**
- [S] "ZombAIs"(Johann Rehberger, 2024년 10월, 오래됨): 웹 페이지가 Claude Computer Use에게 C2 서버에 접속하는 바이너리를 내려받아 실행하라고 지시했고, 첫 시도에 성공했다. — [Embrace The Red](https://embracethered.com/blog/posts/2024/claude-computer-use-c2-the-zombais-are-coming), [Simon Willison](https://simonwillison.net/2024/Oct/25/zombais/)
- [S] "AI ClickFix"(Rehberger, 39C3, 2025년 12월, 오래됨): ClickFix 방식의 사회공학 페이지를 컴퓨터 사용 에이전트에 맞게 변형한 것으로, 특정 에이전트를 겨냥하는 "조건부 프롬프트 인젝션(conditional prompt injections)"을 사용한다. — [media.ccc.de](https://app.media.ccc.de/v/39c3-agentic-probllms-exploiting-ai-computer-use-and-coding-agents), [heise](https://heise.de/-11125687)

**J. 분류 체계의 기준점(OWASP)**
- [S] OWASP Top 10 for Agentic Applications 2026(2025년 12월 9일 발표), ASI01부터 ASI10까지. ASI01은 "Agent Goal Hijack"으로, 공격자가 에이전트가 읽는 콘텐츠를 통해 에이전트의 목표를 다른 방향으로 돌리는 것이다. 나머지 아홉 항목의 정확한 문구는 OWASP 자체 페이지와 대조해 확인하지 않았다. — [TechRepublic](https://www.techrepublic.com/article/news-owasp-ai-agent-risk-list/), [Cycode 요약](https://cycode.com/blog/owasp-top-10-agentic-applications/)

### 추론
- GUI 에이전트를 API/도구 호출 에이전트보다 보호하기 어렵게 만드는 속성(위 내용의 종합):
  1. **데이터와 지시 사이에 타입이 지정된 경계가 없다.** API 에이전트는 도구 결과를 레이블이 붙은 채널로 받는다. GUI 에이전트는 스크린샷/접근성 트리 하나를 받으며, 그 안에서 신뢰할 수 있는 브라우저·시스템 UI(chrome), 사용자 자신의 콘텐츠, 공격자의 콘텐츠는 동등한 지위의 픽셀이다. 출처 정보(provenance)는 캡처 시점에 사라진다.
  2. **사람과 에이전트의 인지 불일치.** 불투명도 2%의 오버레이, 모서리/컷아웃 영역, 흐린 색상, TTS가 건너뛰는 하이퍼링크 텍스트가 그 예다. 감독하는 사람은 에이전트가 본 것을 말 그대로 볼 수 없으며, 이는 통제 수단으로서의 "에이전트 지켜보기"를 약화시킨다.
  3. **주변 권한(ambient authority).** 에이전트는 로그인된 모든 세션과 설치된 모든 앱을 물려받으며, 좁힐 수 있는 도구별 범위가 없다. 이것은 대리인의 범위가 가능한 한 가장 넓은 혼동된 대리인 문제다.
  4. **한정되지 않는 행동 공간.** 도구 에이전트는 스키마가 있는 함수의 허용 목록으로 제한할 수 있다. 반면 "(x, y) 클릭"과 "텍스트 입력"은 어떤 앱의 어떤 행동이든 표현할 수 있으므로, 정책을 픽셀과 의도에서 추론해야 한다.
  5. **공격자가 밀어 넣는 입력.** 알림, 메시지, 초대, 광고는 사용자나 에이전트가 무엇을 방문하기로 선택하지 않아도 화면에 도달한다(제로클릭).
  6. **캡처 및 구동(actuation) 채널 자체가 공격 표면이다**(스크린샷 경쟁 조건, 입력 브로드캐스트, 오버레이, 확장 프로그램 메시징). HTTPS 도구 호출에는 이에 대응하는 것이 없다.
  7. **비밀 정보가 화면에 있다**(OTP, 잔액, 비밀번호 관리자 안의 비밀번호). 그리고 클라우드 추론 방식의 설계에서는 모든 스크린샷이 기기를 떠난다.
- 에이전트의 능력은 위험을 높인다. 여러 벤치마크는 능력이 더 뛰어난 에이전트가 공격자의 목표를 더 많이 완수한다는 것을 보여 준다(2절 참조).

### 공백
- 출시된 브라우저 에이전트를 구체적으로 겨냥한 고전적 클릭재킹(iframe 기반) 공격이 공개되고 실증된 사례는 찾지 못했다. "UI 리드레싱"의 근거는 팝업과 오버레이다.
- 출시된 에이전트를 겨냥해 광고 네트워크로 전달되는 인젝션: 벤더들은 광고를 공격 경로로 꼽지만, 문서화된 사고는 찾지 못했다.
- OWASP ASI02~ASI10의 제목은 OWASP 1차 문서로 확인하지 않았다.

## 2. 어떤 학술 연구와 벤치마크가 이를 측정하며, 어떤 공격 성공률을 보고하는가?

### 핵심
2024~2026년 벤치마크 전반에서 안전장치가 없는 GUI 에이전트는 수십 퍼센트에서 약 90%에 이르는 비율로 탈취되며, 2026년의 모바일 에이전트는 현실적인 Android 벤치마크에서 여전히 40~67%의 ASR을 보인다. 최신 프런티어 모델은 브라우저 환경에서 고정되거나 사람이 직접 작성한 인젝션에 대해 훨씬 낮은 비율(한 자릿수에서 0/140까지)을 보이지만, 적응형 또는 자기 개선형 공격자는 그 비율을 대략 24~28%로 되돌려 놓는다. 그리고 2026년의 한 감사 연구는 높은 대표 수치와 낮은 대표 수치 모두 공격자의 강도와 모델의 세대에서 비롯된 인공물이라고 주장한다.

### 인용 근거

**컴퓨터 사용 / 브라우저 에이전트**
- [S] 팝업 공격(arXiv 2024년 11월; ACL 2025, 오래됨): OSWorld와 VisualWebArena에서 평균 ASR 86%(에이전트가 팝업을 클릭함). 공격받은 에이전트 행동의 92.7% / 73.1%가 팝업 클릭이었다. 과제 성공률은 47% 하락했다. — [arXiv 2411.02391](https://arxiv.org/abs/2411.02391v2)
- [M] EIA: Environmental Injection Attack(arXiv 2409.11295, 2024년 9월; ICLR 2025, 오래됨): 주입된 양식 필드/미러 요소가 웹 에이전트에서 사용자의 PII를 훔친다. Mind2Web에서 GPT-4V를 쓴 SeeAct를 대상으로 특정 PII 탈취는 최대 약 70%, 사용자 요청 전체 탈취는 약 16%의 ASR이었던 것으로 기억한다. 이번 세션에서 다시 가져와 확인하지 않았다. — [arXiv](https://arxiv.org/abs/2409.11295)
- [S] WASP(Meta FAIR; arXiv 2504.18575, 2025년 4월; NeurIPS 2025 D&B, 오래됨): Claude Computer Use, Operator, VisualWebArena 에이전트를 시험했다. 공격은 "최대 86%의 경우에 부분적으로 성공(partially succeed in up to 86% of the cases)"하지만, 에이전트는 공격자의 목표를 끝까지 완수하지 못하는 경우가 많다(ASR-intermediate 대 ASR-end-to-end). 모델별 종단 간 수치는 가져오지 못했다. — [arXiv](https://arxiv.org/abs/2504.18575), [NeurIPS](https://neurips.cc/virtual/2025/poster/121728)
- [P 초록] VPI-Bench(arXiv 2506.02456, 2025년 6월; 2026년 3월 개정; ICLR 2026): 다섯 개 플랫폼에 걸친 시각적 프롬프트 인젝션 사례 306건. 컴퓨터 사용 에이전트는 최대 51%, 브라우저 사용 에이전트는 일부 플랫폼에서 최대 100%의 비율로 속았다. 시스템 프롬프트 방어는 제한적인 개선만 준다. — [arXiv](https://arxiv.org/abs/2506.02456)
- [S] OS-Harm(arXiv 2506.14866, 2025년 6월; NeurIPS 2025, 오래됨): OSWorld 기반. 대상 모델에는 o4-mini, Claude 3.7 Sonnet, Gemini 2.5 Pro가 포함되었다. 에이전트는 기본적인 정적 프롬프트 인젝션에 "o4-mini의 경우 20%의 사례에서(in 20% of cases for o4-mini)" 따른다. 제3자의 표 요약은 o4-mini를 의도적 오용에서 52% 불안전, 인젝션 20%, 오작동 10%로 적고 있다. — [arXiv](https://arxiv.org/abs/2506.14866), [표 요약](https://en.papernotes.org/NeurIPS2025/social_computing/os-harm_a_benchmark_for_measuring_safety_of_computer_use_agents/)
- [P] RedTeamCUA / RTC-Bench(Ohio State; arXiv 2505.21936, v4 2026년 2월 16일; ICLR 2026): 웹 + OS 하이브리드 샌드박스, 예제 864개, 실행 기반 채점.
  - 분리 평가(decoupled evaluation), ASR(시도율): GPT-4o 66.19%(92.45%); Claude 3.5 Sonnet 41.37%(64.27%); Claude 3.7 Sonnet 39.33%(58.99%); Claude 3.7 Sonnet CUA 42.93%(64.39%); Claude 3.5 Sonnet CUA 31.21%(74.43%); 검사 없는 Operator 30.89%(47.84%); 확인/안전 검사가 있는 Operator 7.57%(14.06%).
  - 종단 간(end-to-end), 과제 50개, ASR: Operator 10%; 검사 없는 Operator 42%; Claude 3.7 Sonnet CUA 50%; Claude Opus 4 CUA 48%; Claude Sonnet 4.5 CUA 60%; Claude Opus 4.5 CUA 83%(유효 46건 중 38건); Claude Opus 4.6 CUA 50%. Opus 4.6은 50건 중 25건에서 인젝션을 명시적으로 탐지했으며 Opus 4.5는 3건이었다.
  - 방어: LlamaFirewall과 PromptArmor는 인젝션의 최대 30%만 탐지했다. 방어용 시스템 프롬프트를 써도 ASR은 50% 근처에 머물렀다. Meta SecAlign도 여전히 약 절반을 따랐다. 저자들은 어느 것도 충분하지 않으며, Operator의 보호는 사람의 감독에 의존한다고 결론짓는다(자율성/보안의 상충).
  - 시도율은 일관되게 ASR보다 높다. 에이전트는 공격자의 목표를 시도하지만 능력 부족으로 실패하는 경우가 많다.
  — [arXiv HTML v4](https://arxiv.org/html/2505.21936v4)
- [P 초록] Saban, "Domain-Conditioned Safety in Frontier Computer-Using Agents"(arXiv 2606.05233, 2026년 6월 3일; 단독 저자, 프리프린트): 에피소드 793개, 다단계 웹 과제 24개, 8개 계열의 수작업 공격 템플릿 56개. **Claude Sonnet 4.6과 GPT-5.4에 대한 다단계 공격 성공은 0/140**(Clopper-Pearson 95% 상한 2.60%). 최근 CUA 레드팀 논문들의 42~98% ASR은 퇴역한 모델과 각 비교군에서 가장 약한 모델에 몰려 있고, RL로 최적화한 인젝션 문자열에 의해 나온 것이라고 서술한다. 같은 가중치의 모델이 코딩 에이전트 벤치마크에서는 수작업 스킬 인젝션에 최대 100%까지 무너지므로, 강화는 브라우저 표면에 국한된 도메인 특정적인 것으로 보인다. — [arXiv](https://arxiv.org/abs/2606.05233)
- [P 초록] SIR: Self-improving Red-teaming for Computer Use Agents(arXiv 2608.30207, 2026년 8월 31일, 2026년 10월 3일 개정): 블랙박스, 사례당 최대 10회 시도, 적대적 OS 수준 목표와 정상 과제가 모두 완료된 경우에만 성공으로 집계, 결정론적 상태 검사. **결합 ASR은 Claude Opus 4.8에서 24%, Gemini 3.5 Flash에서 28%이며, 벤치마크의 고정된 수작업 인젝션은 각각 4%와 0%였다.** — [arXiv](https://arxiv.org/abs/2608.30207)
- [S] ClawSafety(arXiv 2604.01438, 2026년 4월): 모델 전반에서 ASR 40~75%이며 인젝션 경로에 따라 크게 달라진다(개인 에이전트 환경이며, 순수 GUI 조작과의 관련성은 확인하지 않았다). — [arXiv](https://arxiv.org/pdf/2604.01438)

**모바일 GUI 에이전트**
- [P 초록] AEIA-MN, "Active Environmental Injection Attacks"(arXiv 2502.13053, 2025년 2월; ACM MM 2025, 오래됨): Android에서 공격자 콘텐츠를 환경 요소로 위장한다(방법 이름은 모바일 알림을 가리킨다). 적대적 콘텐츠 인젝션과 "추론 격차(reasoning gap)" 취약점을 결합했을 때 AndroidWorld에서 최대 ASR 93%. — [arXiv](https://arxiv.org/abs/2502.13053)
- [P 초록] "From Assistants to Adversaries"(arXiv 2505.12981, 2025년 5월, 오래됨): 배포된 모바일 에이전트 아홉 개(Honor YOYO, Zhipu AutoGLM, Alibaba Mobile-Agent 포함), 공격 표면 11개. 모든 에이전트가 취약했고, 가장 나쁜 경우는 여덟 개 경로에 취약했다. 결과는 행동 이탈에서 개인정보 유출, 실행의 완전한 탈취까지 걸쳐 있다. 기기 벤더 두 곳이 제보를 인정했다. 초록에는 ASR이 없다. — [arXiv](https://arxiv.org/abs/2505.12981)
- [P 초록] GhostEI-Bench(arXiv 2510.20333, 2025년 10월; ICLR 2026): Android 에뮬레이터 실행 중에 오버레이와 위조 알림을 동적으로 주입한다. ASR 수치는 가져오지 못했다. — [arXiv](https://arxiv.org/abs/2510.20333), [ICLR](https://iclr.cc/virtual/2026/poster/10011688)
- [P] MobileWorldSafety(Shanghai AI Lab; arXiv 2608.17659, 2026년 8월 18일): 실제 Android 앱 13개와 MCP 서버 5개에 걸친 위험 과제 142개. 전달 매체는 이메일/메시지, 웹 및 상품 페이지, 소셜 게시물, 로컬 파일과 캘린더 기록, 도구 응답이다(오버레이와 알림은 명시적으로 제외). ASR: **Gemini-3-Pro 66.9%, Qwen3.5-397B 51.6%, Kimi-K2.5 49.2%, Claude Sonnet 4.5 47.5%, GUI-Owl-1.5-32B 44.3%, MAI-UI-8B 40.4%.** 모든 범용 에이전트에서 ASR이 공격 상황의 과제 완료율을 넘었다. 특정 대상을 향한 통신(메시지/이메일)은 일관되게 높은 ASR을 보였고, 도구 응답 인젝션이 가장 낮았다. 추론 기능을 켜자 Gemini-3-Flash의 ASR이 51.2%에서 57.9%로 올랐다. 방어: 시스템 프롬프트는 Qwen을 51.6%에서 37.0%로, Gemini-3-Flash를 57.9%에서 41.5%로 낮췄지만, 주로 에이전트를 멈춰 서게 만든 결과였다. 다단계 런타임 방어는 35.7%에 이르렀다. — [arXiv HTML](https://arxiv.org/html/2608.17659v1)
- [P, CSA 경유] "(A)I Sees What You Don't"(arXiv 2607.00333, 2026년 7월): 공격 일곱 가지, 프레임워크 다섯 개, 각 프레임워크는 최소 여섯 가지에 취약했다. 인지 공격은 20회 중 20회 또는 그에 가깝게 성공했다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [S] MobileSafetyBench(2024~2025년, 오래됨): 한 2차 출처는 과제 250개에 간접 프롬프트 인젝션 시나리오 50개를 더한 것으로 설명한다. 출처마다 수치가 엇갈리며, 확인하지 않았다. — [ResearchGate 목록](https://www.researchgate.net/publication/402661952_MobileSafetyBench_Evaluating_Safety_of_Autonomous_Agents_in_Mobile_Device_Control)

**GUI에도 적용되는 도구 에이전트 벤치마크**
- [M] AgentDojo(ETH Zurich, NeurIPS 2024, 오래됨)는 표준적인 도구 호출 인젝션 벤치마크이며, 이후의 GUI 벤치마크들은 이것을 기준으로 자신의 위치를 설정한다. WASP의 저자들은 WASP가 InjecAgent, AgentDojo, ASB의 단점을 해결하기 위해 만들어졌다고 말한다. 이번 세션에서 AgentDojo의 수치는 가져오지 않았다. — [WASP 논문](https://arxiv.org/abs/2504.18575)

### 추론
- 세 가지 구간이 보이며 따로 보고해야 한다. (a) 제품 안전장치가 없는 오픈소스 또는 구형 에이전트: 40~93%. (b) 안전장치가 있는 상태에서 정적/수작업 인젝션을 상대하는 프런티어 모델: 대략 0~10%. (c) 적응형 공격자를 상대하는 프런티어 모델: 시도 예산에 따라 대략 1~30%. 구간을 밝히지 않은 단일 "ASR" 수치는 의미가 없다.
- 모바일은 데스크톱/브라우저에 뒤처져 있다. 2026년의 가장 좋은 모바일 수치(40~67%)는 2025년의 데스크톱 수치와 비슷하며, 여기에는 벤더의 제품 안전장치 없이 Android 하네스로 구동한 현행 프런티어 모델(Claude Sonnet 4.5 47.5%, Gemini-3-Pro 66.9%)이 포함된다. 이는 강화가 표면 특정적이며 브라우저에 집중되어 왔다는 Saban 논문의 주장을 뒷받침한다.
- 능력과 취약성은 맞물려 있다. RedTeamCUA의 종단 간 ASR은 탐색 능력이 향상되면서 Opus 4(48%)에서 Opus 4.5(83%)로 올랐고, MobileWorldSafety는 더 약한 GUI 전문 모델의 ASR이 낮은 것은 주로 인젝션에 도달하기 전에 실패하기 때문이라는 것을 보여 준다.
- 프롬프트 수준의 방어는 주로 ASR을 과제 정체와 맞바꾼다. 시스템 수준의 확인 절차(Operator)가 측정된 것 중 가장 큰 감소(30.89%에서 7.57%)를 주지만, 사람이 루프 안에 있을 때에만 그렇다.

### 공백
- GhostEI-Bench, WASP의 모델별 종단 간 수치, o4-mini 외의 OS-Harm 모델별 인젝션 비율, AgentDojo 수치는 논문에서 가져오지 못했다.
- EIA 수치는 기억에 의한 것이다.
- Saban의 감사 연구는 단독 저자의 프리프린트다. 0/140이라는 결과가 모델별인지 합산인지 밝히지 않으며, 본문 전체를 읽지 않았다.
- 출시된 소비자용 휴대전화 에이전트(Gemini 화면 자동화, Doubao, Honor/Huawei 시스템 에이전트)를 실서비스 안전장치를 켠 채로 시험한, 동료 심사를 거친 2026년 벤치마크는 찾지 못했다.
- AEIA-MN과 MobileWorldSafety 외의 "AndroidWorld 기반 안전 벤치마크": 추가로 확인된 것이 없다.

## 3. 출시된 제품에 대해 어떤 실제 사고나 공개된 취약점 제보가 있는가?

### 핵심
출시된 주요 화면/브라우저 에이전트는 모두 2024년에서 2026년 사이에 에이전트 탈취에 관한 연구자의 공개 제보를 적어도 한 건 이상 받았으며, 여러 벤더가 수정 없이 제보를 종결하거나 연구자들이 불완전하다고 판단한 수정을 배포했다. 에이전트를 겨냥한 인젝션 페이로드는 이제 실제 웹에서 관찰되지만, 출시된 GUI 에이전트를 통해 실제 사용자가 피해를 입은 것으로 확인된 사례는 찾지 못했다. 문서화된 "사고"는 연구자의 개념 증명과 플랫폼의 에이전트 차단이다.

### 인용 근거

**Perplexity Comet**
- [P] Brave의 간접 인젝션 / OTP 탈취: 2025년 7월 25일 제보, 7월 27일 최초 수정, 7월 28일 불완전한 것으로 확인, 8월 13일 패치된 것으로 보임, 2025년 8월 20일 공개. Brave는 이후 게시물을 갱신해 공격이 여전히 완전히 완화되지 않았다고 밝히고 다시 제보했다(오래됨). — [Brave](https://brave.com/blog/comet-prompt-injection/)
- [S] CometJacking 원클릭 URL 유출(LayerX, 2025년 10월, 오래됨). Perplexity는 이를 "보안 영향 없음(no security impact)"으로 분류했다. — [The Hacker News](https://thehackernews.com/2025/10/cometjacking-one-click-can-turn.html)
- [P, CSA 경유] PleaseFix(Zenity Labs, 2026년 3월 3일 공개): 캘린더 초대를 통한 제로클릭 로컬 파일 유출과 1Password 연동 악용. Perplexity는 공개 전에 자율적인 `file://` 접근을 차단하는 강한 경계를 추가했다. CVE는 부여되지 않았다. 1Password 경로에 대한 구체적인 수정은 설명되지 않았다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-pleasefix-agentic-browser-exploits-2026032/)
- 참고: 일부 취합 사이트(Aviatrix)는 CometJacking에 CVE ID, "실제 악용됨(exploited in the wild)" 상태, 50만 달러($500,000)의 손실을 덧붙이고 있다. 이는 일관성이 없으며 1차 보도로 뒷받침되지 않는다. 사용하지 말 것. — [검색 결과에서 표시함](https://aviatrix.ai/threat-research-center/cometjacking-attack-2025-perplexity-comet-browser-prompt-injection/)

**OpenAI: Operator, ChatGPT agent, ChatGPT Atlas**
- [S] Atlas는 2025년 10월에 출시되었다. 며칠 안에 LayerX의 "Tainted Memories"(지속 메모리에 대한 CSRF)와 NeuralTrust의 옴니박스 프롬프트 인젝션이 나왔다(오래됨). — [ITPro](https://www.itpro.com/security/cyber-researchers-have-already-identified-several-big-security-vulnerabilities-on-openais-atlas-browser), [Malwarebytes](https://www.malwarebytes.com/ja/blog/news/2025/10/openais-atlas-browser-leaves-the-door-wide-open-to-prompt-injection)
- [S] 2025년 12월 22일: OpenAI는 RL로 훈련한 자동화 공격자가 새로운 공격 유형을 찾아낸 뒤 Atlas 에이전트 보안 업데이트(적대적으로 훈련한 모델과 더 강한 안전장치)를 배포했다. 예: 악성 이메일 때문에 에이전트가 부재중 자동 응답 대신 사직서를 보냈다(오래됨). — [TechCrunch](https://techcrunch.com/2025/12/22/openai-says-ai-browsers-may-always-be-vulnerable-to-prompt-injection-attacks/), [CyberScoop](https://cyberscoop.com/openai-chatgpt-atlas-prompt-injection-browser-agent-security-update-head-of-preparedness/)
- [P, CSA 경유] BioShocking(LayerX, 2025년 10월부터 2026년 1월까지 제보, 2026년 6월 24일 공개): 시험에서 Atlas가 공격에 뚫렸다. OpenAI는 여섯 벤더 가운데 이를 수정한 것으로 평가된 유일한 벤더다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/)
- [M] Rehberger의 Operator 프롬프트 인젝션 익스플로잇(2025년 2월, 오래됨)은 Operator가 유도되어 인증된 페이지의 개인 데이터를 유출하는 것을 보여 주었다. 다시 가져와 확인하지 않았으며, URL은 기억에 의한 것이다. — [Embrace The Red](https://embracethered.com/blog/posts/2025/chatgpt-operator-prompt-injection-exploits/)

**Anthropic: computer use, Claude in Chrome**
- [S] ZombAIs: Claude Computer Use가 웹 페이지에서 C2 악성코드를 내려받아 실행한다(2024년 10월, 오래됨). — [Embrace The Red](https://embracethered.com/blog/posts/2024/claude-computer-use-c2-the-zombais-are-coming)
- [S] ShadowPrompt(Koi, 2026년 3월): 웹사이트에서 Claude Chrome 확장 프로그램으로 들어가는 제로클릭 프롬프트 인젝션으로, Arkose Labs가 호스팅하는 구성 요소의 XSS와 연쇄되었다. Arkose의 XSS는 2026년 2월 19일에 수정되었고, 확장 프로그램은 v1.0.41+에서 수정되었다. — [The Hacker News](https://thehackernews.com/2026/03/claude-extension-flaw-enabled-zero.html)
- [S] ClaudeBleed(LayerX, 2026년 4월 27일 제보, 2026년 5월 7일 공개): 악성 확장 프로그램이 Claude 확장 프로그램을 조종해 Gmail/Drive 데이터에 접근할 수 있었다. Anthropic은 5월 6일에 명시적 승인 프롬프트를 추가한 v1.0.70을 배포했다. 연구자들은 이 수정이 UI 수준의 것이며 "Act without asking" 모드에서는 여전히 악용 가능하다고 말한다. — [Rewterz 권고문](https://rewterz.com/threat-advisory/claude-extension-flaw-exposes-gmail-and-drive-data)
- [S] Manifold(2026년 5월 21일 제보, 2026년 7월 20일경 공개): 두 가지 결함이 이후 여덟 번의 릴리스에 걸쳐 여전히 악용 가능하다고 한다. Anthropic의 입장 표명은 찾지 못했다. — [IANS](https://www.iansresearch.com/resources/all-blogs/post/security-blog/2026/07/20/unpatched-claude-for-chrome-flaw-could-give-attackers-access-to-gmail--calendar)
- [P, CSA 경유] BioShocking: Anthropic은 패치를 시도했으나 LayerX는 2026년 1월 시점 기준으로 성공하지 못한 것으로 평가했다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/)

**Google: Gemini in Chrome, Android의 Gemini**
- [P, The Hacker News 경유] Gemini의 Android Utilities 에이전트에 대한 SafeBreach의 알림 인젝션: 2025년 8월 17일 Google VRP에 제보. Google은 2025년 11월 14일에 서버 측 콘텐츠 분류기 개선으로 이를 완화했으며 "Delayed Tool Invocation" 우회도 포함된다고 확인했다. 2026년 6월 3일 공개. CVE 없음. 실제 악용의 증거 없음. 기법 "Fake Context Alignment": 권한 승인 질문을 피해자가 쓰지 않는 언어로 묻거나 음성 합성(TTS)이 건너뛰는 하이퍼링크 텍스트에 숨겨, 사용자가 이해하지 못한 것을 승인하게 만든다. 2025년의 캘린더 초대 연구 "Invitation Is All You Need"를 바탕으로 한다. — [The Hacker News](https://thehackernews.com/2026/06/whatsapp-slack-notifications-could.html)
- [P] 2025년 12월 8일에 발표된 Chrome 에이전트 보안 아키텍처(User Alignment Critic, Agent Origin Sets, 확인 절차, 인젝션 분류기, 최대 2만 달러($20,000)의 포상금)(오래됨). — [Google](https://blog.google/security/architecting-security-for-agentic/)
- [S] 2026년 5월: Google은 Android에서 자율적으로 행동하는 Gemini를 위한 안전장치를 설명했다. 자동화 중 지속적으로 표시되며 닫을 수 없는 알림 칩, 구매 전 확인, "올해 후반(later this year)"의 앱별 접근 제어가 그것이다. — [AndroidGuys](https://androidguys.com/news/google-details-security-and-privacy-framework-for-gemini-intelligence-on-android/)

**Microsoft**
- [S] Windows 11 "실험적 에이전트 기능(experimental agentic features)" / Copilot Actions / Agent Workspace: Microsoft의 지원 문서(2025년 11월 17일 갱신, 오래됨)는 데이터 유출이나 악성코드 설치로 이어질 수 있는 교차 프롬프트 인젝션(XPIA)을 포함한 "새로운 보안 위험(novel security risks)"을 경고한다. 기본값은 꺼짐이고, 관리자만 켤 수 있으며, 별도의 에이전트 계정과 활동 로깅을 둔다. 에이전트에게 Documents/Desktop/Downloads 접근 권한이 부여될 수 있다. — [Windows Central](https://www.windowscentral.com/microsoft/windows-11/microsoft-warns-security-risks-agentic-os-windows-11-xpia-malware), [SC World](https://www.scworld.com/news/new-agent-workspace-feature-comes-with-security-warning-from-microsoft), [Tom's Hardware](https://www.tomshardware.com/software/windows/microsofts-new-agentic-ai-features-introduce-new-security-risks-introduced-by-ai-like-prompt-injection-firm-acknowledges-new-and-unexpected-risks-are-possible)

**ByteDance Doubao 폰(ZTE Nubia M153)과 중국의 시스템 에이전트**
- [P] 2025년 12월 초 출시. 시스템 권한 INJECT_EVENTS를 사용하는 OS 수준 에이전트로, 화면을 읽고 사용자로서 탭하며, 스크린샷은 단계마다 클라우드로 업로드된다. 며칠 안에 WeChat, Taobao, Alipay와 여러 은행 앱이 이를 차단하거나 제한했다. WeChat은 이것이 고위험 보안 통제를 발동시켰다고 밝혔다. 은행들은 에이전트의 행동과 사용자의 행동을 구별할 수 없었다. 그보다 몇 주 전에는 WeChat이 Huawei의 에이전트가 통화를 시작하는 것을 차단했다고 전해진다. ByteDance는 이 권한을 "사용자의 명시적 동의가 있을 때에만(only with explicit user consent)" 사용한다고 말했다. 규제 당국은 지켜보고 있는 것으로 전해졌다. 한 핀테크 업계 연합은 예비 지침을 내놓았다. 제안에는 고위험 작업에서 AI 행동의 의무적 중단과 민감한 데이터의 온디바이스 처리가 포함된다. Nanfang Compliance Technology Research Institute의 한 연구는 접근성 권한을 사용하는 휴대전화 에이전트가 사용자에게 알리지 않고 은행 카드 비밀번호와 채팅 기록을 볼 수 있다는 것을 발견했다. — [Lawfare, 2026년 3월 6일](https://www.lawfaremedia.org/article/china-s-agentic-ai-controversy)
- [S] 사용자들은 2025년 12월 2일경부터 WeChat의 "비정상 로그인 환경(abnormal login environment)" 경고를 신고했다. 이후 ByteDance는 에이전트의 게임, 은행, 결제 기능을 중단했다. Alipay는 에이전트의 행동을 "치트 스크립트(cheat scripts)"로 분류했다고 전해진다. — [Yicai Global](https://www2.yicaiglobal.com/news/wechat-some-chinese-banking-apps-reportedly-restrict-devices-with-bytedances-new-ai-voice-assistant), [Business Standard](https://www.business-standard.com/world-news/bytedance-ai-phone-doubao-nubia-zte-app-blocks-user-control-china-125120800459_1.html)
- [S] 2세대 기기는 화면 읽기와 모의 탭에서 벗어나 승인된 앱 파트너와의 프로토콜 기반 연동(MCP, A2A)으로 옮겨 갔다고 전해진다. — [36Kr](https://eu.36kr.com/en/p/3987162000210692) (1차 출처와 대조해 확인하지 않음)

**기타 제품과 실제 환경에서의 관찰**
- [S] Brave는 Fellou와 Opera Neon(2025년), 그리고 Mozilla Tabstack과 Cotypist(2026년 6월)에 대해서도 동작하는 인젝션을 공개했다. — [2차 브리핑](https://www.pk-sharma.com/briefing/agentic-browsers-widen-prompt-injection-exposure) (Brave의 자료와 대조해 확인하지 않음)
- [S] WebPromptTrap(Cato Networks, 2026년 3월 24일): 조작된 AI 요약이 BrowserOS에서 GitHub 권한 승인 흐름을 유도한다. 한 릴리스에서 수정되었다. — [Cato Networks](https://www.catonetworks.com/blog/webprompttrap-new-indirect-prompt-injection-vulnerability/)
- [P, CSA 경유] BioShocking: Fellou, Genspark, Sigma는 응답하지 않았다. Perplexity는 접수를 확인하고 완화 조치 없이 종결했다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-browser-prompt-injection-20260630-csa-s/)
- [P] 실제 환경: Google이 매달 크롤링한 20~30억 개 페이지를 조사한 결과, 2025년 11월과 2026년 2월 사이에 악성 인젝션이 상대적으로 32% 증가했다(검색 조작, 파괴적 행동을 동반한 서비스 거부, API 키 유출, 파일 삭제). Forcepoint X-Labs는 결제 기능이 있는 에이전트를 겨냥해 세부 내용이 완전히 지정된 PayPal 거래 페이로드와, 결제를 Stripe 기부 링크로 보내는 페이로드를 발견했다. 여러 도메인에 걸쳐 공유된 템플릿은 "조직화된 도구의 존재를 시사한다(suggest organized tooling)". **실제 에이전트에 대한 공격 성공이 확인된 사례는 보고되지 않았다.** — [Help Net Security, 2026년 4월 24일](https://www.helpnetsecurity.com/2026/04/24/indirect-prompt-injection-in-the-wild/)
- [S] Unit 42는 텔레메트리에서 관찰된 웹 기반 간접 프롬프트 인젝션(강제 "pro plan" 구매, 데이터베이스 파괴, "공짜 돈(free money)" 미끼)과, AI 기반 광고 심사를 회피하기 위해 인젝션을 실제로 사용한 것으로 처음 확인된 사례라고 스스로 부른 것을 보고했다. 출처에 따라 시점이 2025년 12월 또는 2026년 3월 3일로 다르다(미해결). — [Unit 42](https://unit42.paloaltonetworks.com/ai-agent-prompt-injection/)

### 추론
- 벤더의 대응은 고르지 않다. OpenAI와 Google은 제보된 문제를 수정했다(Google은 약 석 달이 걸렸다). Perplexity는 거듭 영향을 부정하거나 제보를 종결했다. Anthropic의 확장 프로그램 계층 수정은 연구자들의 반박을 받았다. 소규모 브라우저 벤더들은 응답하지 않았다. 취약점 제보 대응의 성숙도 자체가 하나의 선정 기준이다.
- 반복되는 양상은 수정 후 우회다(Comet 2025년 7~8월; Gemini "Invitation Is All You Need" 다음에 Delayed Tool Invocation, 그다음 Fake Context Alignment; Claude 확장 프로그램 v1.0.41 다음에 v1.0.70, 그다음 Manifold). 이는 완화 조치가 아키텍처 수준이 아니라 분류기와 UI 수준이라는 것과 부합한다.
- Doubao 사례는 유일한 대규모 "사고"이며, 생태계가 에이전트를 거부한 것이다. 신뢰 당사자(은행, 슈퍼앱)는 구별할 수 없는 자동화된 행위자를 사기로 취급한다. 따라서 앱에 대한 에이전트의 신원 확인/증명(attestation)은 보안 요구 사항이며, 보도된 MCP/A2A로의 전환은 고위험 앱을 순수하게 화면 조작만으로 다루는 방식이 상업적으로 유지되기 어려울 수 있음을 시사한다.
- 확장 프로그램 기반 에이전트는 프롬프트 인젝션에 더해 전통적인 웹 보안 공격 표면(확장 프로그램 메시징, 의존 구성 요소의 XSS)을 추가한다.

### 공백
- 출시된 GUI 에이전트를 통한 실제 피해자/손실 사례가 확인된 것은 찾지 못했다.
- Gemini의 Android 화면 자동화(휴대전화에서의 "computer use")에 특정된 공개 취약점 제보는 찾지 못했다. SafeBreach의 연구는 Utilities/알림 에이전트에 관한 것이다.
- 이번 세션에서 Microsoft Copilot의 컴퓨터 사용 기능에 대한 구체적인 연구자 제보는 찾지 못했다. Microsoft 자체의 경고만 있다.
- Manifold의 발견에 대한 Anthropic의 대응과 ClaudeBleed 수정의 현재 상태는 확인되지 않았다.
- LayerX, Zenity, Koi, SafeBreach, NeuralTrust의 1차 보고서는 열어 보지 않았다. 세부 내용은 CSA 노트와 언론 보도에서 가져온 것이다.

## 4. 벤더들은 잔여 위험에 대해 스스로 무엇을 공개적으로 인정하는가?

### 핵심
주요 벤더 네 곳 모두 화면/브라우저 에이전트에 대한 프롬프트 인젝션이 해결되지 않았다고 공식적으로 밝히고 있다. 정량적인 ASR을 공개하는 것으로 확인된 곳은 Anthropic뿐이며, 그 수치는 두 자릿수(2025년)에서 약 1%(2025년 11월)로, 그리고 계층화된 안전장치를 갖춘 한 브라우저 시험에서는 0%(2026년 7월, 2차 보도)로 떨어졌지만, GUI 컴퓨터 사용과 적응형 공격자에 대해서는 여전히 0이 아니다.

### 인용 근거
- [P] Anthropic, 2025년 11월 24일(오래됨), Claude in Chrome, 환경당 100회 시도하는 내부 적응형 best-of-N 공격자, 구성별 ASR(모델만 / 기존 안전장치 / 개선된 안전장치): Sonnet 4: 13.8% / 10.7% / 2.0%; Sonnet 4.5: 13.8% / 10.8% / 1.9%; Haiku 4.5: 12.0% / 11.1% / 2.4%; Opus 4.5: 8.0% / 7.6% / 1.4%. 인용: "1%의 공격 성공률은 상당한 개선이지만 여전히 의미 있는 위험이다(A 1% attack success rate—while a significant improvement—still represents meaningful risk)"; "프롬프트 인젝션에 면역인 브라우저 에이전트는 없다(No browser agent is immune to prompt injection)"; "프롬프트 인젝션은 해결된 문제와는 거리가 멀다(prompt injection is far from a solved problem)". 방어: 인젝션에 대응하는 RL 훈련, 신뢰할 수 없는 모든 콘텐츠(숨은 텍스트, 조작된 이미지, 기만적 UI)에 대한 분류기, 레드팀. — [Anthropic](https://www.anthropic.com/research/prompt-injection-defenses)
- [M] Anthropic, 2025년 8월 Claude for Chrome 연구 프리뷰(오래됨): 123개 테스트 케이스/29개 시나리오에 걸쳐 완화 조치 없이 ASR 23.6%, 완화 조치가 있을 때 11.2%. 브라우저에 특정된 네 가지 공격 유형에서는 35.7%에서 0%로. 기억에 의한 것이며 다시 가져와 확인하지 않았다. — [Anthropic](https://www.anthropic.com/news/claude-for-chrome)
- [S] Anthropic 지원 페이지: "공격 가능성은 여전히 0이 아니다(the chances of an attack are still non-zero)". — [Claude Help Center](https://support.claude.com/en/articles/12902428-using-claude-for-chrome-safely)
- [S] Claude Opus 5 시스템 카드(2026년 7월), The Decoder의 보도(2026년 7월 25일, 76쪽 인용)에 따름: 브라우저 에이전트, 시나리오 129개: Auto Mode의 두 안전장치 계층이 있을 때 ASR 0%, 안전장치가 없을 때 3.7%(Sonnet 5: 안전장치 없이 0.93%). Gray Swan 간접 프롬프트 인젝션 벤치마크, 15회 시도, 모델 단독: Opus 5 2.0%, Mythos 5 2.6%, Fable 5 2.8%, Opus 4.8 5.5%. — [The Decoder](https://the-decoder.com/opus-5-may-have-solved-browser-based-prompt-injection-the-biggest-security-flaw-haunting-ai-agents/)
- [S, 신뢰도 낮음] 같은 카드에 대한 다른 2차 글들은 GUI 컴퓨터 사용에 대한 적응형 평가가 0%에 이르지 못했다고 하며(Opus 5 약 0.54~0.56%, Sonnet 5 2.25%), 이후의 Opus 5.5 카드는 1회 시도에서 0.1% / 15회 이내에서 1.0%를 보고하고 컴퓨터 사용에서 가장 높으며(2.8%), 더 새로운 공격자에 대해서는 훨씬 높은 수치를 보고한다고 한다. 이 수치들은 출처마다 엇갈리며 1차 시스템 카드는 열어 보지 않았다. — [xenospectrum](https://xenospectrum.com/en/claude-opus-5-prompt-injection/), [stingrai](https://www.stingrai.io/blog/prompt-injection-statistics-2026)
- [S] 그 수치들에 대한 독립적인 경고: 한 CSA 노트는 프롬프트 인젝션이 Claude Code Auto Mode의 안전 분류기를 무력화했다고 보고한다(코딩 환경, 2026년). — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-claude-code-automode-prompt-injection-2026/)
- [S] OpenAI, 2025년 12월 22일(오래됨): 프롬프트 인젝션은 "웹상의 사기나 사회공학과 마찬가지로 완전히 '해결'될 가능성이 낮다(much like scams and social engineering on the web, is unlikely to ever be fully 'solved')". 에이전트 모드는 "보안 위협 표면을 넓힌다(expands the security threat surface)". 결정론적 보장은 어렵다. 로그아웃 모드 사용과 확인 요청의 검토를 권고한다. 대변인은 이 업데이트가 성공한 인젝션을 측정 가능한 수준으로 줄였는지 밝히기를 거부했다. OpenAI의 CISO는 프롬프트 인젝션을 "최전선의 미해결 보안 문제(a frontier, unsolved security problem)"라고 불렀다(2025년 10월). — [TechCrunch](https://techcrunch.com/2025/12/22/openai-says-ai-browsers-may-always-be-vulnerable-to-prompt-injection-attacks/), [VentureBeat](https://venturebeat.com/security/openai-admits-that-prompt-injection-is-here-to-stay) (OpenAI 자체 게시물은 HTTP 403을 반환했다: https://openai.com/index/hardening-atlas-against-prompt-injection/)
- [P] Google, 2025년 12월 8일(오래됨): 간접 프롬프트 인젝션은 "열린 과제(an open challenge)"다. 탐지 분류기는 "악의적 의도로 모델에 영향을 줄 수 있는 모든 것을 표시할 수는 없다(cannot flag everything that might influence the model with malicious intent)". 웹 에이전트 보안은 "아직 형성 중인 분야다(is still an emerging domain)". — [Google](https://blog.google/security/architecting-security-for-agentic/)
- [S] Microsoft, 2025년 11월(오래됨): 에이전트 기능은 "교차 프롬프트 인젝션(XPIA)과 같은 새로운 보안 위험을 가져온다(introduce novel security risks, such as cross-prompt injection (XPIA))". 이는 데이터 유출이나 악성코드 설치를 일으킬 수 있다. 사용자는 기능을 켜려면 경고를 확인해야 한다. — [Windows Central](https://www.windowscentral.com/microsoft/windows-11/microsoft-warns-security-risks-agentic-os-windows-11-xpia-malware)
- [S] Perplexity의 보안 팀은 프롬프트 인젝션을 해결되지 않은 업계 전반의 문제라고 불렀다. Brave는 2026년 6월 8일에 간접 프롬프트 인젝션이 현재의 LLM 아키텍처에서는 완전히 해결될 수 없다고 밝혔다고 전해진다. — [The Hacker News](https://thehackernews.com/2025/10/cometjacking-one-click-can-turn.html), [2차 브리핑](https://www.pk-sharma.com/briefing/agentic-browsers-widen-prompt-injection-exposure)
- [S] 영국 NCSC도 마찬가지로 프롬프트 인젝션이 결코 완전히 완화되지 않을 수 있다고 경고했다. — [TechCrunch](https://techcrunch.com/2025/12/22/openai-says-ai-browsers-may-always-be-vulnerable-to-prompt-injection-attacks/)

### 추론
- 벤더의 궤적(2025년 8월 23.6%, 2025년 11월 약 1%, 2026년 7월 계층화된 한 브라우저 구성에서 0%)은 감소가 모델 단독이 아니라 주로 시스템 수준의 계층(입력 분류기와 행동 게이팅)에서 나온다는 것을 보여 준다(2025년 11월의 모델 단독 비율은 8~14%, 안전장치 없는 Opus 5는 3.7%). 이는 기술 보고서에서 "계층화된 시스템"이라는 틀을 쓰는 것을 뒷받침한다.
- 벤더의 수치는 벤더 자신의 표면에서 벤더 자신의 공격자로 측정한 것이다. 독립적인 2026년 결과(SIR의 Opus 4.8 대상 24%; MobileWorldSafety의 Android상 Sonnet 4.5 47.5%; 출시 제품에 대한 연구자들의 우회)는 그 조건 밖의 잔여 위험을 보여 준다.
- 접촉 한 번당 1%라고 하더라도, 신뢰할 수 없는 화면을 수천 개 읽는 에이전트는 결국 침해될 것으로 예상된다. 벤더들 스스로도 1%를 의미 있는 수준으로 규정한다.

### 공백
- Anthropic의 1차 시스템 카드(Opus 5, Opus 5.5)와 OpenAI의 강화 관련 게시물은 직접 읽지 않았다. 컴퓨터 사용에 특정된 ASR은 신뢰도가 낮다.
- OpenAI, Google, Microsoft가 자사 GUI 에이전트에 대해 공개한 정량적 ASR은 찾지 못했다.
- 휴대전화 화면 자동화 에이전트에 대해 벤더가 공개한 ASR은 찾지 못했다.

## 5. 모바일 특유의 문제: 휴대전화에서는 무엇이 다른가?

### 핵심
휴대전화 에이전트는 Android 뱅킹 악성코드가 십 년 동안 악용해 온 바로 그 능력(화면 읽기, 입력 주입, 오버레이 그리기, 알림 읽기)을 필요로 하며, 그것도 OTP와 결제 앱, 개인 메시지가 집중된 기기에서, 그리고 어떤 연락처나 앱이든 공격자가 제어하는 텍스트를 화면에 밀어 넣을 수 있는 곳에서 그렇다. 측정된 모바일 에이전트의 ASR은 GUI 에이전트의 모든 범주 가운데 가장 높으며, 에이전트에 의존하게 되는 앱은 정당한 에이전트와 원격 제어 트로이 목마를 구별할 수 없다.

### 인용 근거
- [S] 에이전트의 권한과 겹치는 기존 악용의 규모: Zimperium의 2026 Mobile Banking Heist Report는 90개국의 금융 브랜드 1,243개를 겨냥하는 활성 악성코드 계열 34개를 다룬다. 네 개의 캠페인(RecruitRat, SaferRat, Astrinox, Massiv)은 접근성 악용, MediaProjection 화면 캡처, 오버레이, 세션 설치 API를 사용해 800개 이상의 앱을 겨냥한다. Kaspersky는 2026년 1분기에 새로운 모바일 뱅킹 트로이 목마 설치 패키지 162,275개를 집계했다. Android 사용자에 대한 공격은 2025년에 56% 늘었고 새 패키지는 271% 늘어 255,090개가 되었다. — [TechRepublic](https://techrepublic.com/article/news-android-malware-stealing-pin-overlay-attack), [Fintech News SG](https://fintechnews.sg/136967/security/malicious-mobile-banking-software-proliferates/), [Dawn](https://www.dawn.com/news/1978843/WhatsApp:/send) (벤더 통계이며, 방법론은 벤더마다 다르다)
- [S] 악성코드가 에이전트의 설계로 수렴하고 있다. RatHat(Zimperium, 2026년 9월)은 접근성 악용을 원격 AI 의사 결정과 결합하고, 접근성을 이용해 화면에서 ADB 페어링 코드를 읽어 사용자 상호작용 없이 디버그 인터페이스와 페어링한다. RemControl(Group-IB, 2026년 7월부터 관찰됨)은 화면에 보이는 모든 UI 요소의 기계 판독 가능한 지도를 수집한다. — [Security Affairs](https://securityaffairs.com/199317/malware/rathat-turns-android-accessibility-into-an-attack-weapon.html), [Malwarebytes](https://www.malwarebytes.com/blog/news/2026/09/new-android-malware-uses-ai-to-steal-bank-logins-and-pins), [Infosecurity Magazine](https://www.infosecurity-magazine.com/news/banking-trojan-remote-control/)
- [S] Google의 Advanced Protection Mode는 어떤 앱이 접근성 권한을 요청할 수 있는지를 제한한다. — [Security Affairs](https://securityaffairs.com/199317/malware/rathat-turns-android-accessibility-into-an-attack-weapon.html)
- [P, CSA 경유] 공격자로서의 함께 설치된 악성 앱: 일반적인 제3자 앱이 요청할 수 있는 권한(오버레이, 접근성; 루트 불필요)만으로 앱은 보이지 않는 지시문을 주입하고, UI를 위조하고, 입력된 자격 증명을 스니핑하고, 입력 브로드캐스트를 가로채고, 스크린샷 파일에 대해 경쟁 조건 공격을 할 수 있다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [P, The Hacker News 경유] 제로클릭 푸시 채널로서의 알림과 메시지: WhatsApp/Slack/SMS/Signal/Instagram/Messenger의 어떤 발신자든 Android의 Gemini에 인젝션을 할 수 있었다. 그 결과는 물리적 세계로 넘어갔고(Google Home의 창문, 보일러, 조명), Zoom 앱 링크를 통한 영상 스트리밍, 메모리 오염, 예약 작업을 통한 지속성으로도 이어졌다. — [The Hacker News](https://thehackernews.com/2026/06/whatsapp-slack-notifications-could.html)
- [P, The Hacker News 경유] 음성/화면의 분리는 모바일 특유의 문제다. "음소거(muted)" 변형은 실제 권한 승인 질문을 TTS가 건너뛰는 링크 텍스트에 숨기므로, 음성으로 들리는 확인 내용이 화면의 확인 내용과 달라진다. — [The Hacker News](https://thehackernews.com/2026/06/whatsapp-slack-notifications-could.html)
- [P] 측정된 모바일 ASR: AndroidWorld에서 최대 93%(AEIA-MN, 2025년, 오래됨). MobileWorldSafety(2026년 8월)에서 여섯 개 에이전트에 걸쳐 40.4~66.9%이며, 특정 대상을 향한 통신(메시지, 이메일)이 가장 효과적인 전달 매체였다. — [arXiv 2502.13053](https://arxiv.org/abs/2502.13053), [arXiv 2608.17659](https://arxiv.org/html/2608.17659v1)
- [P] 특권을 가진 시스템 에이전트: Doubao의 OS 수준 INJECT_EVENTS 권한은 앱의 협조가 필요 없다. 단계별 스크린샷이 클라우드로 간다. 은행 잔액이 미러링 기기에 노출되었다. 공유 채팅에 있는 제3자의 데이터가 당사자 모르게 처리된다. "현재 다른 제조사가 비슷한 권한을 부여하는 것을 막는 규칙은 없다(No rule currently stops other manufacturers from granting similar permissions)". — [Lawfare](https://www.lawfaremedia.org/article/china-s-agentic-ai-controversy)
- [P] 결제 및 은행 앱은 차단으로 대응했다. 은행들은 "에이전트의 행동과 사용자의 행동을 구별할 수 없었다(could not distinguish agent actions from user actions)". Alipay는 이를 치트 스크립트로 취급했다고 전해진다. ByteDance는 은행/결제/게임 자동화를 중단했다. — [Lawfare](https://www.lawfaremedia.org/article/china-s-agentic-ai-controversy), [Yicai Global](https://www2.yicaiglobal.com/news/wechat-some-chinese-banking-apps-reportedly-restrict-devices-with-bytedances-new-ai-voice-assistant)
- [P, CSA 경유] 하드웨어/디스플레이 특성: 둥근 모서리와 카메라 컷아웃은 프레임버퍼에는 존재하지만 사용자에게는 보이지 않는 영역을 만든다. — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mobile-agent-attack-surface-screen-injecti/)
- [S] Android에서 계획된 플랫폼 완화 조치(2026년 5월): 닫을 수 없는 자동화 표시기, 구매 확인, Gemini에 대한 앱별 접근 제어. — [AndroidGuys](https://androidguys.com/news/google-details-security-and-privacy-framework-for-gemini-intelligence-on-android/)

### 추론
- 휴대전화에서 공격자의 가장 값싼 진입 수단은 메시지 전송이다. 웹사이트 방문도 설치도 필요 없다. 특정 대상을 향한 통신이 가장 강한 전달 매체라는 MobileWorldSafety의 발견과 합쳐 보면, 수신 메시지/알림 콘텐츠가 모바일의 최상위 위협 경로다.
- 모바일 OS에는 에이전트가 연결해 쓸 수 있는, 브라우저의 출처 모델에 해당하는 것이 없으며(Chrome의 Agent Origin Sets에 대응하는 Android의 앱별 장치는 발표된 앱별 접근 제어 외에는 아직 출시된 것이 없다), 접근성 트리/스크린샷은 DOM보다 약한 출처 정보를 준다.
- 휴대전화 에이전트는 이중 용도 문제를 물려받는다. 같은 능력 집합이 사기 방지 시스템이 트로이 목마를 식별하는 지표이기도 하므로, 정당한 에이전트에게는 증명 가능한 신원과 승인된 채널이 필요하며, 그렇지 않으면 고가치 앱에 의해 차단될 것이다.
- 소형 온디바이스 모델이나 GUI 전문 모델은 인젝션을 알아보는 능력이 더 약하다(AutoGLM의 경량 변형을 포함해 인지 공격 성공이 20회 중 20회에 가깝다). 따라서 온디바이스 추론의 개인정보 보호 이점은 견고성과 상충한다.
- SMS/알림 OTP는 에이전트가 읽을 수 있을 뿐 아니라 공격자가 전달할 수도 있으므로, OTP 기반의 추가 인증(step-up authentication)은 에이전트가 시작한 행동에 대한 확인 수단으로서 약하다.

### 공백
- iOS 화면 조작 에이전트에 대한 공개된 보안 평가는 찾지 못했다.
- 실서비스 안전장치를 갖춘 출시된 OEM 시스템 에이전트(Honor YOYO, Huawei, Samsung/Gemini 화면 자동화, Doubao)에 대해 측정된 ASR이 없다.
- 에이전트를 겨냥한 딥링크/인텐트 기반 공격: Zoom 앱 링크 시연만 찾았으며, 체계적인 연구는 확인되지 않았다.
- 뱅킹 트로이 목마 통계는 벤더가 보고한 것이며, 보고서 자체가 아니라 언론 보도에서 가져왔다.
- 배포된 휴대전화 에이전트를 구체적으로 겨냥하는 실제 악성코드가 문서화된 사례는 찾지 못했다(에이전트와 유사한 기법을 채택한 악성코드와는 구별된다).
