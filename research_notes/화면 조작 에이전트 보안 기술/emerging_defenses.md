# 화면/GUI 조작 에이전트를 위한 신흥 및 미래 보안 기술 (2026-11-18 기준 현황)

이 문서 전체에서 쓰는 표기 규칙은 다음과 같다.

- 성숙도 태그: **[RESEARCH]** = 논문/프로토타입 단계에 그친다. **[PILOT]** = 프리뷰, 실험적 기능, 실제 배포 사례가 있는 표준 초안이다. **[SHIPPED]** = 제품에서 정식으로 제공된다.
- 검증 태그: **(fetched)** = 이번 세션에서 1차 출처 페이지를 직접 읽었다. **(search summary)** = 페이지 전문이 아니라 검색 결과 요약에서 가져왔으므로, 수치는 공개 전에 다시 확인해야 한다. **(prior knowledge)** = 잘 알려진 출처이지만 이번 세션에서 다시 열어 보지는 않았다.
- 검색 결과 중 일부는 품질이 고르지 않은 2차 블로그였다. 이런 출처는 항목마다 따로 표시했다. 1차 출처와 연결할 수 없었던 주장은 공백 절에 두었다.

## 1. 구조적/증명 가능한 보장을 갖는 아키텍처 방어, 그리고 관측이 스크린샷이고 행동이 탭일 때도 그 보장이 유지되는가

### 핵심
구조적 방어(dual-LLM, CaMeL, IFC/테인트 추적, 정책 모니터)는 적응형 공격자를 상대로도 버틴다고 설득력 있게 주장할 수 있는 유일한 계열이다. 그러나 이들은 구조화된 도구 호출을 전제로 설계되었다. 컴퓨터 사용으로 옮긴 본격적인 시도는 하나뿐인데(단발 계획, 2026년 1~3월), 이 시도는 제어 흐름 무결성만 보장하고, 작업 성능의 대략 3분의 1에서 절반을 잃으며, 브라우저/데스크톱에만 적용되고, 조작된 픽셀을 통한 "분기 조종(branch steering)"에는 여전히 뚫린다. 모바일에서는 이에 상응하는 것이 시연된 적이 없으며, 이 계열 가운데 소비자용 화면 조작 에이전트에 출시된 것은 없다.

### 인용 근거

**기초 패턴**
- Dual-LLM 패턴(권한 있는 LLM은 신뢰할 수 없는 콘텐츠를 절대 보지 않고, 격리된 LLM은 그 콘텐츠를 보지만 도구가 없다)은 Simon Willison이 2023년 4월 25일에 제안했다. **[RESEARCH/패턴]** (prior knowledge) — [Willison, "The Dual LLM pattern"](https://simonwillison.net/2023/Apr/25/dual-llm-pattern/)
- CaMeL(Debenedetti, Shumailov, Tramèr, Carlini 외, Google DeepMind/ETH, 2025년 3월)은 신뢰할 수 있는 사용자 질의에서 제어 흐름과 데이터 흐름을 추출해 프로그램으로 만들고, 이를 전용 인터프리터에서 실행하며, 데이터에 케이퍼빌리티(capability)를 붙여 정책이 허가되지 않은 흐름을 차단할 수 있게 한다. **[RESEARCH]** (설계에 대해서는 prior knowledge) — [arXiv:2503.18813](https://arxiv.org/abs/2503.18813). 보고된 비용: o3를 쓴 AgentDojo에서 공격을 받는 상황의 작업 성공률이 77%이고 방어가 없을 때는 84%이다. 이는 2026년의 한 체계화 논문에 다시 기술된 수치이다 (fetched) — [arXiv:2606.26479](https://arxiv.org/html/2606.26479v1)
- "Design Patterns for Securing LLM Agents against Prompt Injections"(Beurer-Kellner, Tramèr 외, arXiv 2025년 6월)는 신뢰할 수 없는 데이터를 제어 흐름에서 격리하는 여섯 가지 패턴(action-selector, plan-then-execute, map-reduce, dual-LLM, code-then-execute, context-minimization 포함)을 설명한다. 중심 원칙은, 에이전트가 신뢰할 수 없는 입력을 일단 받아들였다면 그 입력이 중대한 결과를 낳는 행동을 유발할 수 없도록 에이전트를 제약해야 한다는 것이다. 저자들은 이 패턴들이 능력을 내주고 안전을 얻는 것이며 범용 에이전트는 이런 방식으로 보호하기 어렵다고 밝히고, 따라서 애플리케이션별 설계가 실용적인 길이라고 말한다. **[RESEARCH/지침]** (search summary) — [arXiv:2506.08837](https://arxiv.org/html/2506.08837v3)
- Meta "Agents Rule of Two"(2025년 10월 31일): 견고성 연구가 인젝션을 안정적으로 탐지하고 거부할 수 있게 되기 전까지, 에이전트는 한 세션 안에서 [A] 신뢰할 수 없는 입력 처리, [B] 민감한 시스템/개인 데이터에 대한 접근, [C] 상태 변경 또는 외부와의 통신 가운데 최대 두 가지만 가져야 한다. 세 가지가 모두 필요하다면 사람의 감독이 있어야 한다. Meta는 프롬프트 인젝션을 "모든 LLM의 근본적이고 해결되지 않은 약점(a fundamental, unsolved weakness in all LLMs)"이라고 부르며, 이 규칙이 "결승선(a finish line)"은 아니라고 말한다. **[지침; Meta 내부에서 채택 — 범위는 확인되지 않음]** — [Meta AI blog](https://ai.meta.com/blog/practical-ai-agent-security/) (search summary); 세부 내용과 비판은 [Willison, 2 Nov 2025](https://simonwillison.net/2025/Nov/2/new-prompt-injection-papers/) (fetched)
- Willison의 평가: 그는 이 규칙이 "매우 마음에 든다(like[s] this a lot)"고 하며, 믿을 만한 방어가 없는 상황에서 구할 수 있는 최선의 실용적 지침이라고 본다. 그는 Meta의 도식이 A+C 조합을 "안전(safe)"이라고 표기한 것에 이의를 제기했고(피해는 여전히 가능하다), Meta는 교집합의 표기를 "더 낮은 위험(lower risk)"으로 바꾸었다. (fetched) — [Willison, 2 Nov 2025](https://simonwillison.net/2025/Nov/2/new-prompt-injection-papers/)
- Rule of Two는 Willison이 2025년 6월에 제시한 "치명적 삼중주(lethal trifecta)"(개인 데이터 + 신뢰할 수 없는 콘텐츠 + 외부 통신)를 바탕으로 한다. (prior knowledge) — [Willison, "The lethal trifecta"](https://simonwillison.net/2025/Jun/16/the-lethal-trifecta/)
- 비판: Rule of Two는 에이전트별/세션별 규칙이어서, 그것만으로는 침해가 서로 연결된 다중 에이전트 시스템 전체로 번지는 것을 막지 못한다(2차 논평, Ken Huang). — [Substack](https://kenhuangus.substack.com/p/the-rule-of-two-vs-reality-why-metas)

**정보 흐름 제어와 테인트 추적**
- FIDES(Costa 외, Microsoft, arXiv 2025년 5월): 기밀성 및 무결성 레이블을 추적하고, 정책을 결정론적으로 강제하며, 정보를 선택적으로 숨기거나 드러내는 프리미티브를 추가한 플래너이다. 정책을 켠 상태에서는 허가되지 않은 행동이나 데이터 흐름을 시도한 AgentDojo 인젝션 공격을 모두 막았다. **[RESEARCH -> PILOT]** (search summary) — [arXiv:2505.23643](https://arxiv.org/html/2505.23643v2)
- FIDES는 Microsoft Agent Framework에 실험적 IFC 미들웨어로 들어가고 있다(`agent_framework.security`, agent-framework-core v1.3.0 기준으로 보고됨). **[PILOT, 실험적]** (search summary) — [Microsoft Learn](https://learn.microsoft.com/en-us/agent-framework/agents/security); [GitHub discussion #5624](https://github.com/microsoft/agent-framework/discussions/5624)
- RTBAS(arXiv 2025년 2월): 도구 기반 에이전트를 위한 테인트 추적 IFC이며, 신뢰할 수 없는 데이터가 도구 호출에 영향을 주었는지를 판정하는 데 두 가지 "의존성 스크리너(dependency screeners)"(LM-as-judge와 어텐션 기반 살리언시)를 쓴다. 즉 CaMeL/FIDES와 달리 전파 단계 자체가 확률적이다. **[RESEARCH]** (search summary) — [arXiv:2502.08966](https://arxiv.org/abs/2502.08966)

**정책 / 가드레일 언어(참조 모니터)**
- Progent(arXiv 2025년 4월): 에이전트와 도구 사이에 놓이는 참조 모니터로, 세분화된 권한 정책(어떤 도구를, 어떤 인자로, 어떤 조건에서)을 강제한다. 원 논문은 AgentDojo ASR이 39.9% -> 1.0%로 줄었다고 보고한다. **[RESEARCH]** (search summary; 수치는 직접 읽은 2606.26479에 다시 기술되어 있음) — [arXiv:2504.11703](https://arxiv.org/abs/2504.11703)
- AgentSpec(Wang, Poskitt, Sun; arXiv 2025년 3월): 에이전트를 런타임에 제약하기 위한 트리거/술어/강제 규칙의 DSL이다. **[RESEARCH]** (prior knowledge; 이번 세션의 검색에서는 이에 관한 결과가 나오지 않았다 — ID를 확인할 것) — [arXiv:2503.18666](https://arxiv.org/abs/2503.18666)
- 2026년 6월의 독립적인(동료 심사를 거치지 않은 소규모) 재현 연구는 Dual-LLM, CaMeL, FIDES, Progent, Conseca, RTBAS, FORGE를 "대역 외(out-of-band)" 방어로 체계화하고, "이들 모두가 정적 벤치마크에서만 검증되었다(every one of them is validated only on static benchmarks)"고 지적했다. 이 연구가 Progent를 상대로 직접 손으로 작성한 방어 인지형 공격은 ASR을 높이지 못했다(평균이 표준 공격 4.2% 대 적응형 2.6%, 방어가 없을 때 25.8%). 그러나 공격을 받는 상황의 유용성은 45.2%에서 약 26~27%로 떨어졌고, 방어를 적용한 실행에는 LLM 호출이 약 15배 더 필요했다. 저자들은 이를 "증명이 아니라 예비적인 데이터 포인트(a preliminary data point, not a proof)"라고 부른다(7B 모델, 스위트당 8개 작업, 공격 템플릿 하나). (fetched) — [arXiv:2606.26479](https://arxiv.org/html/2606.26479v1)

**컴퓨터 사용 에이전트로의 적용**
- "CaMeLs Can Use Computers Too"(Foerster, Blanchard, Nikolić, Shumailov, Zhang, Mullins, Papernot, Tramèr, Zhao; arXiv 2601.09923, v2는 2026년 3월 6일자)는 dual-LLM/CaMeL 개념을 CUA로 옮기려는 주된 시도이다. **[RESEARCH]** (fetched) — [arXiv:2601.09923v2](https://arxiv.org/html/2601.09923v2)
  - 설계: *단발 계획(Single-Shot Planning)* — 권한 있는 플래너(P-LLM)가 화면을 전혀 보기 전에 "조건 분기를 포함한 완전한 실행 그래프(a complete execution graph with conditional branches)"를 내놓는다. 격리된 비전 모델(Q-VLM)은 관측-검증-행동(Observe-Verify-Act) 루프 안에서 좁은 범위의 도구(`summarize_screenshot`, `find`, `find_element_by_text`, `verify_hypothesis`, `check_done`)를 통해 스크린샷/DOM을 처리한다.
  - 주장하는 보장: "임의의 명령 인젝션에 대한 증명 가능한 제어 흐름 무결성 보장(provable control flow integrity guarantees against arbitrary instruction injections)". 주장하지 않는 것: 데이터 흐름 보안 — "데이터 흐름은 여전히 취약하다(the data flow remains vulnerable)"; "침해된 Q-VLM은 거짓 좌표나 요약을 반환해 계획을 악의적인 분기로 조종할 수 있다(a compromised Q-VLM could return false coordinates or summaries to steer the plan into malicious branches)".
  - 이 논문이 정의하는 새로운 공격 유형: *분기 조종(Branch Steering)* — 화면에 보이는 것을 조작해(광고 안의 가짜 쿠키 배너, 그래디언트로 최적화한 픽셀) 사전 승인된 계획의 정당한 분기가 공격자의 뜻대로 움직이게 한다. 픽셀 공격은 "스크린샷 기반 Q-VLM 검증과 DOM 보강 Q-VLM 검증을 모두 회피한다(evades both screenshot-based and DOM-enhanced Q-VLM verification)".
  - 시험한 완화책(DOM 일관성 검사, DOM을 읽는 모델과 스크린샷을 읽는 모델 사이의 다중 모달 합의)은 "경험적인 심층 방어를 제공하지만 핵심인 Dual-LLM 제어 흐름 보호를 넘어서는 형식적 보장은 제공하지 않는다(provide empirical defense-in-depth but do not offer formal guarantees beyond the core Dual-LLM control flow protection)". HTML5 광고는 DOM 검사를 우회하고, 다중 모달 합의는 토큰 사용량을 최대 x6.57까지 늘린다.
  - 유용성: 초록은 OSWorld에서 프런티어 모델 성능의 "최대 57%(up to 57%)"를 유지하고 소형 오픈소스 모델에서는 최대 +19%를 얻는다고 주장한다. 예를 들어 전체 작업에서 UI-TARS-1.5-7B 단독은 24.4%이고 GPT-5 플래너를 붙이면 29.0%이다. 본문은 pass@5 수치(각 CUA가 이전에 해결한 작업의 65~68%)도 쓰는데, 이는 오라클 선택을 가정한다. 기준이 서로 다르므로 섞어 쓰면 안 된다. 토큰 오버헤드는 CaMeL 방식 구성에서 x1.88, FIDES 방식 변형에서 x29.60이다.
  - 명시된 한계: 브라우저 기반 작업만 다룬다. 명세가 부족한 작업은 분기를 많이 만들게 한다. CUA 도구에 대한 의미론적 보안 정책은 "미해결 문제로 남아 있다(remain an open question)". 모바일/Android 평가는 없다.
  - 배포 아이디어: 독점 모델이 계획을 세우고, 오픈소스 모델이 로컬에서 인지를 수행한다(이렇게 하면 스크린샷도 기기 안에 머문다).
- 모바일에 한정하면, 2026년 8월 Radboud의 논문 "Not an A11y"는 다음을 권고한다(평가하지는 않는다). "관측된 모든 UI 요소에 대해, 그 출처 애플리케이션을 포함한(for every observed UI element, including its source application)" 에이전트 측 출처 추적, 데이터/명령 분리, 알림/웹페이지/숨겨진 노드를 데이터로 취급하기, 예상치 못한 앱 간 행동(예: 요약 중에 설정 열기)에 대한 정책 검사, 접근성 트리와 스크린샷 사이의 일관성 검사가 그것이다. 이 논문은 비전을 "신뢰할 수 있는 보안 경계가 아니라 보조적인 맥락으로 보아야 한다(should be viewed as supplementary context rather than a reliable security boundary)"고 경고하고, "프롬프트 수준의 구분자는 제한적인 보호만 제공할 수 있다(prompt-level delimiters may provide limited protection)"고도 경고한다. **[RESEARCH, 권고에 그침]** (fetched) — [arXiv:2608.08939](https://arxiv.org/html/2608.08939v1)
  - 같은 논문의 근거 자료: Gemma/Qwen 백엔드를 쓰는 오픈소스 Android 에이전트(MobileRun v0.6.0, Mobile-Use v3.3.0)가 알림, 오버레이, 숨겨진 접근성 메타데이터, 캘린더/메모/연락처 필드를 통해 탈취되었고, 종합 ASR은 프레임워크/모델에 따라 0.15에서 0.82 사이였다. 오염된 플래너 "AppCards"는 ASR 1.0에 도달했다. 대상은 오픈소스 프레임워크이며 벤더 제품이 아니다.
- 방어가 무엇을 처리해야 하는지를 규정하는 2026년의 추가 모바일 공격/벤치마크 연구(제목만 확인함): MIRAGE(사용자 생성 콘텐츠 영역을 통한 인젝션, 2026년 5월) [arXiv:2605.28116](https://arxiv.org/abs/2605.28116); Android GUI 에이전트에 대한 행동 재결합(action-rebinding) 공격 [arXiv:2601.12349](https://arxiv.org/pdf/2601.12349); UI 비동기화("When Agents See Differently") [arXiv:2609.16732](https://arxiv.org/pdf/2609.16732); MobileWorldSafety [arXiv:2608.17659](https://arxiv.org/pdf/2608.17659); DIBench [arXiv:2610.06898](https://arxiv.org/pdf/2610.06898); 모바일 에이전트를 위한 OS-Sentinel 하이브리드 검증(2025년 10월) [arXiv:2510.24411](https://arxiv.org/pdf/2510.24411). (search summary; 내용은 읽지 않음)

### 추론
- GUI 에이전트가 구조적으로 더 어려운 이유: CaMeL/FIDES/Progent는 (a) 인자가 의미를 담는 타입이 있는 행동 공간("send_email(to=...)")과 (b) 출처를 식별할 수 있는 데이터에 의존한다. (x, y) 위치의 탭은 화면을 해석하기 전까지 의미가 없고, 스크린샷은 신뢰할 수 있는 시스템 UI(chrome), 사용자 자신의 데이터, 공격자가 통제하는 콘텐츠가 뒤섞인 하나의 덩어리이다. 따라서 IFC가 필요로 하는 세밀도로 레이블을 붙일 수 없고, "인자에 대한 정책"은 좌표에 대한 정책으로 퇴화한다.
- 그 결과 현실적인 적용 방식은 다음과 같다. (1) 관측하기 전에 계획을 세우고(단발 계획) 줄어든 적응성을 감수한다. (2) 행동 공간을 픽셀에서 의미론적 행동(접근성 노드 + 소유 앱/패키지, 또는 구조화된 앱 함수 — 7절 참조)으로 끌어올려 참조 모니터와 테인트 레이블이 결합할 대상을 만든다. (3) 픽셀에 레이블을 붙이려 하지 않고, 작업별로 Rule of Two 방식의 케이퍼빌리티 제거로 가둔다.
- 모바일에서는 OS가 웹에는 없는 이점을 갖는다. Android/iOS는 각 창과 각 접근성 노드를 어느 패키지가 그렸는지 알고 있으므로, 요소별 출처(GUI에 대한 IFC의 전제 조건)를 퍼스트파티 에이전트가 기술적으로 이용할 수 있다. 이를 모델에 보안 레이블로 노출하는 논문이나 제품은 찾지 못했다. 이것은 추론이며 보고된 설계가 아니다.
- Rule of Two를 휴대폰 에이전트에 적용하면 곤란해진다. 범용 휴대폰 에이전트는 거의 언제나 A(어떤 앱 화면에든 서드파티 콘텐츠가 들어 있을 수 있다), B(휴대폰은 사용자의 가장 민감한 기기이다), C(탭을 한다)를 모두 갖는다. 따라서 이 규칙은 C에 사람을 개입시키거나(human-in-the-loop) 작업별로 A/B를 좁히는 것(앱 허용 목록)을 뜻하게 되고, 이는 벤더들이 출시하는 것과 일치한다(3절).
- GUI 에이전트에 대한 성숙도 순위는 대략 다음과 같다. Rule of Two / 설계 패턴(설계 규율로서 오늘 쓸 수 있음) > 의미론적 행동에 대한 정책 모니터(실현 가능하나 모바일에서 평가되지 않음) > 단발 계획(연구 단계, 브라우저 한정) > 픽셀에 대한 완전한 IFC(알려진 해법 없음).

### 공백
- AndroidWorld나 다른 어떤 모바일 벤치마크에서도 CaMeL 방식 계획, FIDES, Progent를 평가한 것은 찾지 못했다.
- 대역 외 방어에 대한 강력한 적응형 공격 평가(Attacker-Moves-Second 규모)는 존재하지 않는다. 찾은 것은 소규모이고 동료 심사를 거치지 않은 것 하나뿐이다.
- Invariant Labs의 가드레일 언어와 Google의 Conseca/FORGE는 출처에서 이름이 언급되었으나 이번 세션에서 조사하지 않았다.
- 출시된 컴퓨터 사용 제품 가운데 내부적으로 plan-then-execute나 권한 분리를 쓰는 것이 있는지는, 내가 찾은 범위에서는 공개적으로 문서화되어 있지 않다.
- "새니타이징 방어로서의 set-of-marks / 접근성 트리 필터링": 이 아이디어는 권고(교차 모달 일관성 검사)로만 찾았고, 적응형 공격자를 견딘다는 것을 보인 엄밀한 논문은 없다. CaMeL-CUA 결과는 견디지 못함을 시사한다(픽셀 공격이 두 모달리티를 모두 통과한다).

## 2. 모델 및 탐지 수준의 방어와 그 한계

### 핵심
적대적 학습, 분류기, 가드 모델, 프롬프트 수준의 기법은 알려진 공격의 성공률을 낮추며, 벤더들이 실제로 출시하는 것도 이것이다. 그러나 2025~2026년의 증거에 따르면 이런 종류의 공개된 방어는 모두 적응하는 공격자에게 무너진다. 이제는 벤더들 스스로 이 문제가 이 계층에서 완전히 해결되지는 않을 것이라고 말한다.

### 인용 근거
- "The Attacker Moves Second"(Nasr, Carlini 외; OpenAI, Anthropic, Google DeepMind에 걸친 저자 14명; arXiv 2025년 10월 10일)는 공개된 탈옥/프롬프트 인젝션 방어 12개를 적응형 공격(그래디언트, RL, 탐색, 사람에 의한 레드팀)으로 시험했고, 대부분이 0에 가까운 수치를 보고했던 것과 달리 대부분에서 90%를 넘는 ASR을 얻었다. (search summary + 직접 읽은 Willison 요약) — [arXiv:2510.09023](https://arxiv.org/abs/2510.09023); [Willison](https://simonwillison.net/2025/Nov/2/new-prompt-injection-papers/)
  - 예: Spotlighting 28%(정적) -> 99%(적응형); RPO 0% -> 99%. 깨진 방어에는 Spotlighting, Prompt Sandwiching, RPO(프롬프트 기법)와 Data Sentinel, MELON(비밀 지식/카나리 탐지)이 포함된다. 사람에 의한 레드팀(참가자 약 500명, 상금 총액 $20,000)은 모든 방어를 무너뜨렸다(100%). 그래디언트 기법은 자동화 공격 가운데 효과가 가장 낮았고, LLM이 생성한 후보를 쓰는 탐색과 RL이 더 잘 작동했다.
  - 방법론적 결론: 고정된 공격 문자열 집합으로 평가하는 것은 타당하지 않다. 방어는 그 방어를 아는 공격자를 상대로 시험해야 한다.
- Willison은 신뢰할 수 있는 방어가 곧 나올 것이라는 이 논문의 낙관에 동의하지 않는다. (fetched) — [Willison, 2 Nov 2025](https://simonwillison.net/2025/Nov/2/new-prompt-injection-papers/)
- OpenAI(2025년 12월 22일, ChatGPT Atlas에 관해)는 프롬프트 인젝션이 "완전히 해결될 가능성은 낮다(unlikely to ever be fully solved)"고 밝혔다. OpenAI는 브라우저 에이전트에 대한 인젝션을 찾아내는 RL 학습 LLM "자동화된 공격자(automated attacker)"를 만들었고, 그 발견 결과를 상대로 적대적 학습을 한다. 발견된 익스플로잇 하나는 에이전트가 부재중 자동 응답 대신 사직서를 보내게 만들었다. OpenAI는 이 업데이트가 인젝션 성공을 측정 가능할 만큼 줄였는지 밝히기를 거부했다. **[SHIPPED: 적대적 학습 + 자동화된 레드팀 루프]** (search summary) — [OpenAI](https://openai.com/index/hardening-atlas-against-prompt-injection/); [TechCrunch 22 Dec 2025](https://techcrunch.com/2025/12/22/openai-says-ai-browsers-may-always-be-vulnerable-to-prompt-injection-attacks/)
- Anthropic의 브라우저 에이전트 수치(Claude for Chrome 연구 프리뷰, 2025년 8월): 완화책이 없을 때 공격 성공률 23.6%, 자율 모드에서 안전장치를 적용했을 때 11.2%이다. **[SHIPPED 완화책, 잔여 위험 공개]** (prior knowledge; 다시 가져오지 않음) — [Anthropic](https://www.anthropic.com/news/claude-for-chrome)
- Google의 Gemini API 컴퓨터 사용 문서에는 이 기능에 오류와 보안 취약점이 있을 수 있다는 프리뷰 경고가 실려 있으며, 숨겨진 적대적 명령을 탐지하는 옵트인 방식의 스크린샷 검사를 제공한다. **[PILOT]** (search summary) — [Gemini API docs](https://ai.google.dev/gemini-api/docs/computer-use.md.txt)
- Google의 2026년 5월 Android 게시글은 Gemini가 사용자를 대신해 행동할 때 프롬프트 인젝션에 대한 새로운 플랫폼 보호 기능이 작동한다고 말하며, 이를 Chrome의 auto browse에 있는 보호 기능에 견준다. 메커니즘이나 측정된 비율은 제시하지 않는다. **[SHIPPED/출시 진행 중, 문서화되지 않음]** (fetched, 폴란드어판) — [Google blog, 12 May 2026](https://blog.google/intl/pl-pl/nowosci-produktowe/android-chrome-play/przyszlosc-androida-oparta-na-agentach-tworzenie-gemini-intelligence-na-fundamencie-bezpieczenstwa-i-prywatnosci/)
- GUI 에이전트에 한정하면, 두 번째 모달리티를 통한 탐지는 견고하지 않다. 그래디언트로 최적화한 배너 픽셀은 스크린샷 기반 검증 모델과 DOM 기반 검증 모델을 모두 회피했다 (fetched) — [arXiv:2601.09923v2](https://arxiv.org/html/2601.09923v2); 그리고 접근성 트리 에이전트에 비전을 추가한 것은 엇갈린 효과를 냈다(Gemma 평균 ASR 0.89 -> 0.62, 여전히 높음) (fetched) — [arXiv:2608.08939](https://arxiv.org/html/2608.08939v1)
- MIRAGE의 부록은 스크린샷이 수정되지 않은 진짜 모바일 UI인지를 묻는 경량 VLM 분류기를 시험한다. 그 효과는 스니펫에서 확인되지 않는다. **[RESEARCH]** (search summary) — [arXiv:2605.28116](https://arxiv.org/html/2605.28116v1)
- Google DeepMind가 Gemini 방어에 관해 직접 기술한 내용(적대적 미세 조정과 적응형 평가)은 "모델 강화는 도움이 되지만 심층 방어가 필요하다"는 점에 대한 표준 참고 문헌이다. (prior knowledge; 다시 가져오지 않음) — [arXiv:2505.14534](https://arxiv.org/abs/2505.14534)

### 추론
- 이 방어들은 확률적이다. 공격자의 비용을 바꿀 뿐 가능성 자체를 없애지 못한다. 화면 에이전트의 경우 인젝션은 렌더링된 텍스트, 이미지, 오버레이, 알림, 숨겨진 접근성 메타데이터가 될 수 있으므로, 분류기가 다루어야 하는 입력 공간이 텍스트 도구의 경우보다 크다. 또 픽셀 수준의 적대적 예제는 텍스트 분류기가 전혀 보지 못하는 채널을 하나 더한다.
- 미래 아키텍처에서 이 계층의 현실적인 역할은, 이 계층이 실패했을 때 피해를 한정하는 구조적 계층 아래에서 잡음을 줄이는 것(흔한 공격을 막고 HITL 프롬프트를 줄이는 것)이다. 행동이 결제나 데이터 삭제일 때는 한 자릿수의 잔여 ASR도 용납할 수 없다. 공격자는 대규모로 재시도하기 때문이다.
- 벤더의 ASR 수치는 벤더 자신의 공격 집합을 상대로 측정한 것이다. "Attacker Moves Second"에 따르면 이 수치는 견고성의 상한으로 읽어야 하며 보장으로 읽어서는 안 된다.

### 공백
- 출시된 모바일 에이전트(Android의 Gemini, Apple, Samsung)의 인젝션 방어에 대해 공개된 적응형 공격 평가는 찾지 못했다.
- 2025년 이후 Anthropic과 Google의 브라우저/컴퓨터 사용 공격 성공률 수치는 이번 세션에서 가져오지 못했다(검색 결과가 없었다). 위의 Anthropic 수치는 2025년 8월 게시글에 대한 기억에서 나온 것이다.
- OpenAI는 Atlas 강화의 전후 비율을 공개하지 않았다(TechCrunch에 따름).
- 같은 그룹이 낸 "The Attacker Moves Second"의 2026년 후속 연구는 찾지 못했다. 더 새로운 학습 기반 방어(예: 명령 계층 학습, SecAlign 방식)가 비슷한 공격을 견디는지는 여기서 확인되지 않았다.

## 3. 에이전트를 위해 제안된 OS/플랫폼 수준 모델

### 핵심
플랫폼 벤더들은 알아볼 수 있는 한 묶음의 통제 수단으로 수렴하고 있다. 에이전트에 대한 앱별 허용 목록, 구매에 대한 필수 확인, 닫을 수 없는 "에이전트가 동작 중" 표시기, 자동화를 위한 격리 실행, 활동 로그, 기밀 컴퓨팅 백엔드가 그것이며, Google은 2026년에 Android에서 이것의 한 형태를 출시하기 시작했다. 더 야심 찬 아이디어(작업 범위로 한정된 의도 권한, 행동 바인딩을 갖추고 에이전트가 위조할 수 없는 신뢰 확인, 기기 내 스크린샷 선택적 가림)는 아직 연구 단계이다.

### 인용 근거

**Google이 Android에 대해 발표한 내용(플랫폼 모델에 가장 가까운 것)**
- Dave Kleidermacher(플랫폼 보안 및 프라이버시 담당 VP)는 2026년 5월 12일, Android의 "Gemini Intelligence"가 사용자 통제, 데이터 보호, 투명성 위에 만들어졌다고 설명한다. 자동화는 사용자가 요청할 때만 시작된다. Gemini는 사용자가 허용한 앱에만 접근할 수 있다. 앱별 자동화 설정은 "올해 안에(later this year)" 제공된다. 구매 전에 사용자 확인이 필요하다. 동의 대화상자에는 "항상 허용 / 이번만 / 허용 안 함(Always allow / Only this time / Don't allow)"이 있다. 자동 완성(Autofill) 연결은 선택 사항이다. **[SHIPPED/출시 진행 중]** (fetched, 폴란드어판; 영어 원문 URL은 찾지 못함) — [Google blog](https://blog.google/intl/pl-pl/nowosci-produktowe/android-chrome-play/przyszlosc-androida-oparta-na-agentach-tworzenie-gemini-intelligence-na-fundamencie-bezpieczenstwa-i-prywatnosci/)
- 같은 게시글의 투명성 관련 내용: 에이전트 행동의 실시간 "진행 상황 보기(view progress)", 자동화가 실행되는 동안 닫을 수 없는 화면 상단의 상시 표시기, 그리고 프라이버시 대시보드가 지난 24시간 동안 어떤 AI 어시스턴트가 활성화되었고 어떤 앱을 사용했는지 보여 줄 예정이라는 것이다. **[SHIPPED / 발표됨]** (fetched) — 같은 출처
- 같은 게시글의 데이터 보호 관련 내용: 주변(ambient) 데이터를 위한 Private Compute Core, Private AI Compute, 보호된 KVM(pKVM) 가상 머신이 있으며, 하드웨어/프로세스/서버 격리를 갖춘다. 핵심 구성 요소는 오픈소스이고, 바이너리 투명성이 있으며, 외부 감사를 받았다고 설명된다. **[SHIPPED 인프라]** (fetched) — 같은 출처
- Google은 이러한 관행이 서드파티를 포함해 Android의 모든 AI 어시스턴트를 위한 업계 표준이 되기를 바란다고 말한다. (fetched) — 같은 출처
- 2026년 3월의 한 언론 보도는 Gemini의 앱 자동화가 대상 앱을 "안전한 가상 창에서(in a secure, virtual window)" 실행하며 기기의 나머지 부분에는 접근할 수 없다고 전한다. **[언론 보도에 따르면 SHIPPED; Google의 1차 기술 문서는 찾지 못함]** (search summary, 2차 자료) — [article](https://tdp-frontend.pages.dev/article/2026-03-12-googles-ai-automation-raises-privacy-questions-ahead-of-australian-rollout-faa0f494)
- 미확인: 2차 사이트들(borncity.com, secnews.gr, digitaltrends.com; 2026년 9월)은 작업마다 컨테이너화된 가상 창을 두고 AI 활동 "블랙박스" 로그를 갖춘 "Android Halo"/"AISeal" 설계를 설명한다. 이 이름이나 세부 내용을 Google의 1차 출처와 대조해 확인할 수 없었다. 같은 기사들 가운데 하나는 내가 검증할 수 없었던 모델명을 인용한다. 소문 수준으로 취급할 것. — [borncity](https://borncity.com/news/android-sandbox-google-begrenzt-autonome-ki-agenten-direkt-im-betriebssystem/); [digitaltrends](https://digitaltrends.com/phones/google-is-building-a-black-box-for-android-ai-but-the-agents-arent-inside-yet/)
- Android는 에이전트에 두 가지 경로를 제공한다. 하나는 AppFunctions(앱이 함수에 `@AppFunction` 애너테이션을 달고, 에이전트가 이를 찾아 호출한다. 호출은 특권/시스템 호출자로 제한되며 그 밖의 호출자는 `SecurityException`을 받는다)이고, 다른 하나는 이를 채택하지 않은 앱을 위한 UI 자동화 폴백이다. Google은 Android 17에서 둘 다 확대할 계획이다. **[PILOT -> 제한된 앱/기기에 대해 SHIPPED]** (search summary, 2차 자료) — [shreyaspatil.dev](https://blog.shreyaspatil.dev/the-future-of-android-apps-with-appfunctions/); [FoneArena roadmap coverage](https://www.fonearena.com/blog/476208/google-android-roadmap-ai-agents-app-automation.html)

**에이전트가 위조할 수 없는 신뢰 확인 경로**
- 기존 프리미티브: Android Protected Confirmation은 TEE 안의 Trusted UI에서 프롬프트를 렌더링하고, 사용자가 바로 그 메시지를 확인했다는 서명된 진술을 반환한다. 높은 보증 수준의 거래를 위해 설계된 것이지 에이전트를 위한 것이 아니며, 지원하는 하드웨어에서만 쓸 수 있다. **[SHIPPED 프리미티브, 에이전트와 연결되어 있지 않음]** (prior knowledge; 다시 가져오지 않음) — [Android developers](https://developer.android.com/privacy-and-security/security-android-protected-confirmation)
- "Approve What, Exactly? Effect Binding and Trusted Rendering for Agent Step-Up Authorization"(arXiv 2026년 10월)은 승인 흐름에 대한 공격(AP2, MCP elicitation, 레이블 위조)을 연구한다. 신뢰할 수 있는 카드 표시는 위조된 서술(forged narration) 공격의 성공률을 0.54에서 0.03으로 낮추었다. 두 번째 기기는 호스트 내 신뢰 확인에 비해 측정 가능한 이점을 더하지 못했다. 승인 플러딩은 모델링된 승인자를 상대로 여전히 0.45~0.60의 성공률을 보였다. 이 논문은 에이전트가 질문을 렌더링하고 응답 채널에 쓸 수도 있는 "채널 내(in-channel)" 확인을 구분한다. **[RESEARCH]** (search summary; 모델링된 승인자이며 사용자 연구가 아님) — [arXiv:2610.21063](https://arxiv.org/pdf/2610.21063)
- "The Verifiable Action Card"(arXiv 2026년 9월, 브라우저 에이전트): 사용자에게 보여 주는 확인은 모델이 생성한 것이 아니라 확정된(resolved) 행동에서 도출되어 신뢰할 수 있는 표면에 렌더링되어야 한다. 구조적으로 탐지된 커밋만 확인 대상이 되며, 눈에 띄는 카드는 위험이 높은 행동에만 쓴다. **[RESEARCH]** (search summary) — [arXiv:2609.18411](https://arxiv.org/pdf/2609.18411)
- "Loopjacking"(arXiv 2026년 9월): 사람은 한 가지 작업을 승인하지만 시스템은 다른 작업을 실행하는 공격으로, 여러 에이전트 프레임워크에서 재현되었다. 얼마나 널리 퍼져 있는지는 추정되지 않았다. **[RESEARCH, 공격]** (search summary) — [arXiv:2609.21081](https://arxiv.org/pdf/2609.21081)
- 암호화폐 에이전트 지갑에서 나온 TEE 측 서명 패턴: 서명 서비스(MPC 또는 TEE)가 서명을 내주기 전에 정책 결정 기록을 확인하고, 최종 승인은 신뢰할 수 있는 기기에서 사용자가 증명한(user-attested) 프롬프트를 거치게 한다. **[서베이에 따르면 일부 지갑에서 SHIPPED]** (search summary) — [arXiv:2601.04583](https://arxiv.org/html/2601.04583v1)

**스크린샷의 비공개 유지: 기밀 추론과 가림 처리**
- 기밀 클라우드 추론: Apple Private Cloud Compute(상태를 저장하지 않고, 증명되며, 이미지를 공개적으로 검사할 수 있음)가 참조 설계이다 (prior knowledge) — [Apple Security Research](https://security.apple.com/blog/private-cloud-compute/); Google은 Private AI Compute와 pKVM을 Gemini Intelligence 보호의 일부로 든다 (fetched) — [Google blog, 12 May 2026](https://blog.google/intl/pl-pl/nowosci-produktowe/android-chrome-play/przyszlosc-androida-oparta-na-agentach-tworzenie-gemini-intelligence-na-fundamencie-bezpieczenstwa-i-prywatnosci/). **[SHIPPED 인프라; 에이전트 스크린샷이 구체적으로 그 안에서만 처리되는지는 직접 읽은 게시글에 명시되어 있지 않음]**
- 업로드 전 기기 내 가림 처리(redaction), 모두 **[RESEARCH]** (search summary; 수치는 확인하지 않음):
  - MaskClaw(2026년 5월): 에지 측 "프라이버시 중재자(privacy arbiter)"가 무엇이든 기기를 떠나기 전에 스크린샷마다 허용 / 마스킹 / 사용자에게 묻기를 결정하고, 사용자의 수정에서 규칙을 학습한다. 벤치마크는 P-GUI-Evo이며 샘플은 832개이다. 기준선들은 "과도하게 확인하거나, 과도하게 마스킹하거나, 원본 화면을 유출한다(over-confirm, over-mask, or leak raw screens)". — [arXiv:2605.28646](https://arxiv.org/pdf/2605.28646)
  - CAPED(2026년 6월): "부수적인 시각적 프라이버시 노출(incidental visual privacy exposure)"(작업과 무관한 민감 콘텐츠)을 겨냥한 휴대폰 측 업로드 전 계층이다. 일반적인 영역 마스킹은 에이전트에 필요한 단서를 파괴하므로 마스킹은 선택적이어야 한다고 주장한다. — [arXiv:2606.12666](https://arxiv.org/pdf/2606.12666)
  - GUIGuard: 로컬 모듈이 민감한 요소를 탐지해 등급을 매기고, 원격 모델이 보기 전에 픽셀을 검게 가리고 접근성 트리 텍스트를 익명화한다. — [review](https://www.themoonlight.io/review/guiguard-toward-a-general-framework-for-privacy-preserving-gui-agents)
  - PriMobiBench(2026년 9월): 클라우드 VLM이 모바일 에이전트의 스크린샷 궤적에서 무엇을 추출할 수 있는지 측정한다. 완화책은 민감하면서 동시에 작업과 무관한 요소만 픽셀화한다. 합성 벤치마크이다. — [arXiv:2609.13873](https://www.alphaxiv.org/abs/2609.13873)
- CaMeL-CUA 저자들이 제안한 분할 배포: 클라우드 모델은 화면을 보지 않고 계획을 세우고, 로컬 오픈소스 모델이 인지를 수행한다. **[RESEARCH]** (fetched) — [arXiv:2601.09923v2](https://arxiv.org/html/2601.09923v2)

**특권 에이전트의 위험과 OS 수준 하니스**
- 연구에 따르면 사전 설치된/특권 에이전트는 비공개 시스템 API를 호출하고 입력 이벤트를 직접 주입할 수 있어, 권한 모델이 보호해야 하는 경계가 넓어진다. 오픈소스 OS 수준 하니스(AOHP, 2026년 6월)가 이 설계 공간을 탐색한다. **[RESEARCH]** (search summary) — [arXiv:2606.23449](https://arxiv.org/pdf/2606.23449)
- 시스템 수준 모바일 에이전트는 앱의 자동화/봇 탐지도 무력화한다("ActReal", 2026년 8월). 즉 앱은 OS 에이전트와 사용자를 안정적으로 구별할 수 없다. **[RESEARCH]** (search summary, 제목 수준) — [arXiv:2608.30038](https://arxiv.org/pdf/2608.30038)
- 접근성 API를 통한 서드파티 "컴퓨터 사용"은 그 자체가 공격 표면이다. 접근성은 "공격자의 영향을 받는 신뢰 경계(an attacker-influenced trust boundary)"이며, 이 논문은 플랫폼 변경을 제안하기보다 제한된 접근성 접근에 관한 선행 연구를 가리킨다. (fetched) — [arXiv:2608.08939](https://arxiv.org/html/2608.08939v1)

### 추론
- 앱 허용 목록 + 구매 확인 + 격리된 창의 조합은 Rule of Two의 실용적인 구현이다. 허용 목록은 B를 좁히고, 가상 창은 A를 좁히며(다른 앱의 알림/오버레이가 시야에 들어오지 않음), 확인은 C의 관문이 된다. 이것은 이미 제품으로 넘어오고 있는 가장 중요한 "미래" 패턴이다.
- 작업별 가상 디스플레이는 "Not an A11y"에서 측정된 여러 공격 벡터(앱 간 오버레이, 알림)를 제거할 가능성이 높지만, 앱 내 콘텐츠(웹뷰, 사용자 생성 콘텐츠, 대상 앱 내부의 숨겨진 접근성 노드)는 제거하지 못한다. 이에 대한 측정 결과는 찾지 못했다.
- 신뢰 확인은 필요하지만 충분하지 않다. 연구에 따르면 어려운 부분은 하드웨어 신뢰 루트가 아니라 *바인딩*(사용자가 보는 것이 실행될 것과 같다)과 *양*(플러딩/피로)이다. 에이전트가 작성한 텍스트를 표시하는 TEE 프롬프트는 의미 면에서 여전히 위조할 수 있다. 모바일은 앱과 에이전트가 그 위에 덧그릴 수 없는 표면을 OS가 소유하고 있으므로 이 점에서 유리한 위치에 있다.
- 가림 처리와 인젝션 방어는 서로 맞물린다. 프라이버시 마스킹에 쓰는 기기 내 요소 분할은 출처 레이블과 관측 필터링에 필요한 것과 같은 장치이다. 두 목적을 함께 수행하는 단일한 기기 내 "화면 방화벽"은 그럴듯한 방향이다(추론이며, 읽은 출처에서 제안된 것은 아니다).
- OS 수준의 "에이전트 전용 신원/계정"(에이전트를 위한 별도의 Android 사용자/프로필 또는 uid와 그 자체의 권한 부여)은 허용 목록의 자연스러운 확장이지만, 이것이 출시되었다고 설명하는 1차 출처는 찾지 못했다.

### 공백
- Gemini 자동화를 위한 Android의 가상 디스플레이/샌드박스 메커니즘에 관한 1차 기술 문서는 찾지 못했다. "Android Halo/AISeal" 주장은 확인되지 않았다.
- 화면 조작 에이전트에 대한 Apple과 Samsung의 2026년 플랫폼 모델은 이번 세션에서 조사하지 않았다.
- Android Protected Confirmation(또는 TEE에서 렌더링되는 어떤 프롬프트든)이 제품에서 에이전트 행동 확인에 실제로 쓰이는 것을 보여 주는 출처는 없다.
- OS 기능으로서의 "의도 기반 / 작업 범위 권한": 벤더의 앱별 설정과 연구 정책(Progent, Conseca)으로만 찾았다. 명시된 작업으로 권한 부여의 범위를 한정하는 OS 권한 모델은 찾지 못했다.
- 선택적 가림 처리가 대규모에서 에이전트의 작업 성공률을 유지하는지에 관한 사용자 연구는 없다. 결과는 소규모 또는 합성 벤치마크에서 나온 것이다.

## 4. 에이전트 신원, 인가, 위임

### 핵심
신원과 위임은 표준화 활동이 가장 활발한 영역이다. IETF에는 이제 Web Bot Auth 워킹그룹 초안과 여러 OAuth/에이전트 인증 초안이 있고, 결제 네트워크들은 서로 경쟁하지만 겹쳐 쓸 수 있는 맨데이트/토큰 체계를 갖고 있다. 이들 거의 전부는 에이전트가 자신의 자격 증명을 가지고 HTTP API를 통해 서비스에 도달한다고 가정한다. 이는 사용자가 로그인해 둔 앱을 탭으로 조작하는 기기 내 GUI 에이전트가 하지 않는 바로 그 일이다. 따라서 이 메커니즘들은 주로, 인가나 결제의 순간에 에이전트가 GUI를 떠나 구조화된 채널로 옮겨 갈 이유를 줌으로써 도움이 된다.

### 인용 근거

**웹에서 에이전트 식별하기**
- IETF Web Bot Auth: `draft-ietf-webbotauth-httpsig-protocol`(WG 초안, -00은 2026년 9월, -01은 2026년 10월)은 자동화된 에이전트가 HTTP Message Signatures로 HTTP 요청에 서명할 수 있게 한다. `Signature-Agent` 헤더는 키를 게시하는 HTTPS URL을 가리킨다. 위조 가능한 User-Agent 문자열, 그리고 확장되지 않는 IP 허용 목록/공유 비밀이 그 동기이다. **[PILOT: 표준 트랙 초안; 그 이전에는 Meunier(Cloudflare)의 개인 초안들]** (search summary) — [datatracker](https://datatracker.ietf.org/doc/html/draft-ietf-webbotauth-httpsig-protocol); [architecture draft, Mar 2026](https://datatracker.ietf.org/doc/html/draft-meunier-web-bot-auth-architecture)
- 초안들은 유효한 서명이 누가 에이전트를 운영하는지, 그것이 선의인지, 인가를 받았는지를 입증하지 않으며 그런 판단은 오리진의 정책으로 남는다고 명시한다. 또 에이전트가 한 신원을 버리고 다른 신원으로 갈아타는 것도 막지 못한다고 명시한다. (search summary) — [draft-ietf-webbotauth-httpsig-protocol-00](https://datatracker.ietf.org/doc/html/draft-ietf-webbotauth-httpsig-protocol-00)

**에이전트를 위한 OAuth / 신원**
- `draft-klrc-aiagent-auth-03`(2026년 7월, Informational; 저자는 AWS, Zscaler, Ping Identity, OpenAI, Okta 소속): WIMSE(워크로드 신원; SPIFFE 방식 식별자)와 OAuth 2.0을 기반으로 한 에이전트 인증/인가 모범 사례이다. 인증, 인가, 감사, 위임을 위해 에이전트가 고유하게 식별될 것을 요구하며, WIMSE 식별자를 주된 에이전트 식별자로 삼는다. **[PILOT: 개인 초안]** (search summary) — [datatracker](https://datatracker.ietf.org/doc/html/draft-klrc-aiagent-auth-03)
- "OAuth 2.0 Extension: On-Behalf-Of User Authorization for AI Agents"는 인가 코드 흐름에 매개변수를 추가해, 에이전트의 신원이 동의 절차를 거쳐 액세스 토큰까지 전달되게 한다. 리비전 -02(2025년 8월)는 인용한 글이 작성된 시점에 OAuth WG에 채택되지 않은 상태였다. **[RESEARCH/초안]** (search summary, 2차 자료) — [WorkOS explainer](https://workos.com/blog/oauth-on-behalf-of-ai-agents)
- `draft-sharif-openid-agent-identity`(-00은 2026년 3월, -01은 2026년 8월): 자율 에이전트를 위한 OpenID Connect 클레임(신원, 소유권, 신뢰 상태, 인가된 케이퍼빌리티)이다. **[초안, 개인]** (search summary) — [datatracker](https://datatracker.ietf.org/doc/draft-sharif-openid-agent-identity/)
- OpenID Foundation은 에이전트형 AI의 신원 관리에 관한 백서를 발간했으며, 이 백서는 현재의 자원과 에이전트 인증, 인가, 신원을 위한 전략적 의제를 다룬다. **[지침]** (search summary; 날짜는 검색 결과에서 확인되지 않음 — 2025년 10월로 기억한다) — [mirror](https://aitopics.org/doc/arxivorg:84F023AD)
- MCP 인가: 2026년 5월의 CSA 노트는 사양이 OAuth 2.1 프레임워크를 정의하지만 인가를 선택 사항으로 표시한다고 말한다. 한 벤더 블로그는 2026-07-28 리비전이 HTTP 전송에 대해 OAuth 2.1, 리소스 인디케이터(RFC 8707), 오디언스 검증을 요구한다고 말한다. **[SHIPPED 사양; 요구 수준에 대해 출처 간 상충 — 공식 변경 이력을 확인할 것]** (search summary) — [CSA note](https://labs.cloudsecurityalliance.org/research/csa-research-note-mcp-security-crisis-20260504/); [WorkOS](https://workos.com/blog/mcp-supply-chain-security)

**결제: 검증 가능한 의도와 범위가 한정된 자격 증명**
- Google AP2(2025년 9월 발표): 검증 가능한 자격 증명(verifiable credentials) 위에 만든 서명된 "맨데이트(mandates)"를 쓰는 개방형 프로토콜이다. Intent Mandate는 에이전트가 구매해도 되는 조건을 담고(사람이 자리에 없는 경우용), Cart Mandate는 정확한 장바구니와 가격에 대한 사용자의 명시적 인가이며(사람이 자리에 있는 경우), Payment Mandate는 에이전트의 관여와 사용자의 현존 여부를 네트워크/발급사에 알린다. **[PILOT: 개방형 사양, 파트너 생태계; 프로덕션 거래량은 알 수 없음]** (search summary) — [ap2-protocol.org](https://ap2-protocol.org/); [core concepts](https://ap2-protocol.org/topics/core-concepts); [Google Cloud announcement](https://cloud.google.com/blog/products/ai-machine-learning/announcing-agents-to-payments-ap2-protocol/?hl=en)
  - 세부 사항에서 출처들이 상충한다. 한 해설 글은 "Checkout Mandate + Payment Mandate"라는 명칭을 쓴다. 사람이 자리에 없는 흐름에서 어떤 맨데이트에 서명하는지, 그리고 사양 버전(사람이 자리에 없는 경우를 정의하는 "v0.2"; "2026년 2월" 릴리스)에 대해 출처마다 다르다. 해결되지 않았다. 사양 저장소를 확인할 것.
- OpenAI/Stripe Agentic Commerce Protocol(ACP), Apache-2.0, 2025년 9월 29일: 에이전트와 가맹점 사이의 체크아웃이다. Stripe의 Shared Payment Token은 저장된 결제 수단에 대한 제한된 참조로, 사용 한도와 만료 시점이 있고 PAN을 결코 드러내지 않는다. 한 2차 출처는 각 토큰의 범위가 판매자 하나, 통화, 최대 금액, 만료 시점으로 한정된다고 말한다. **[SHIPPED (ChatGPT Instant Checkout) / 확대 중]** (search summary) — [Stripe docs](https://docs.stripe.com/agentic-commerce/concepts/shared-payment-tokens)
- Mastercard Agent Pay(2025년 4월 29일 발표): 등록/검증된 에이전트가 MDES 토큰화를 확장한 "Agentic Tokens"를 써서 거래한다. **[PILOT -> SHIPPED; "미국 출시 완료, 전 세계는 2026년 중반"은 단일 2차 출처의 주장임]** (search summary, 2차 자료) — [eco.com comparison](https://eco.com/support/en/articles/15192003-mastercard-agent-pay-vs-visa-trusted-agent-2026-compared)
- Visa Trusted Agent Protocol(2025년 10월 14일 공개): 가맹점이 신뢰할 수 있는 에이전트를 알아보고 악성 봇과 구별하도록 돕는다. 서명된 HTTP 요청 위에 만들어졌다(Web Bot Auth 계보). 이것이 에이전트를 식별하기만 하는지 결제까지 인가하는지에 대해서는 2차 출처들의 말이 엇갈린다. **[PILOT]** (search summary, 2차 자료) — [eco.com comparison](https://eco.com/support/en/articles/15192003-mastercard-agent-pay-vs-visa-trusted-agent-2026-compared)
- 이 프로토콜들의 승인 흐름에 대한 연구 비판(레이블 위조, 위조된 서술, 플러딩) — 3절 참조. — [arXiv:2610.21063](https://arxiv.org/pdf/2610.21063)

### 추론
- 사용자의 저장된 카드를 웹 체크아웃에 입력하거나 로그인된 앱에서 "결제"를 탭하는 GUI 에이전트는 이 가운데 아무것도 쓰지 않는다. 가맹점은 사용자의 세션을 보고, 발급사는 평범한 카드 등록(card-present-on-file) 거래를 보며, 서명된 맨데이트도, 에이전트 신원도, 지출 한도도 없다. 따라서 표준들은 *유인의 기울기*를 만든다. 탐색은 시각적인 방식으로 남더라도, 위험이 큰 마지막 단계(인증, 결제)는 구조화되고 암호학적으로 귀속을 밝힐 수 있는 채널로 옮겨 간다. 이 하이브리드가 결제의 최종 상태가 될 가능성이 높다.
- 모델이 아닌 다른 주체가 강제하는 거래 한도(가맹점/금액/만료로 범위가 한정된 토큰; 발급사 측의 에이전트형 토큰 통제)는 이 보고서 전체에서 프롬프트 인젝션에 영향을 받지 않는 몇 안 되는 방어에 속한다. 인젝션을 당한 에이전트는 토큰의 범위를 넘어설 수 없을 뿐이다. 이것은 돈의 계층에서 "피해 반경을 한정하는" 것이다.
- Web Bot Auth는 안정적인 키를 가진 클라우드 호스팅 에이전트를 위해 설계되었다. 사용자 자신의 브라우저/앱을 구동하는 기기 내 에이전트의 경우 누가 키를 보유하는지(OS 벤더인가? 에이전트 벤더인가? 기기별 증명인가?)는 내가 읽은 범위에서 명시되어 있지 않다. 이는 프라이버시상의 절충(사이트가 사용자가 에이전트를 쓰고 있음을 알게 된다)도 불러온다.
- 에이전트 전용 계정(에이전트가 사용자로서가 아니라 범위가 축소된 위임 주체로 로그인하는 것)은 사이트 측이 위임 토큰을 채택해야 가능하다. 오늘날 GUI 에이전트는 사용자의 전체 세션을 물려받으며, 이것이 "주변 권한(ambient authority)"의 뿌리이다.

### 공백
- AP2, Visa TAP, Mastercard Agent Pay의 원문은 가져오지 않았다. 서술은 검색 요약과 2차 해설 글에 근거하며, 이들은 곳곳에서 상충한다.
- 채택 데이터(2026년 11월 기준으로 얼마나 많은 가맹점/사이트가 Web Bot Auth 서명을 검증하거나 AP2 맨데이트를 받아들이는지)는 찾지 못했다.
- 기기 내 모바일 에이전트가 에이전트 신원 키를 어떻게 얻거나 증명하는지에 관한 출처는 찾지 못했다.
- W3C의 작업(예: AI 에이전트 프로토콜 커뮤니티 그룹, 에이전트를 위한 검증 가능한 자격 증명)은 조사하지 않았다.
- OpenID Foundation 백서의 날짜와 구체적인 내용은 이번 세션에서 확인하지 않았다.

## 5. 런타임 모니터링, 감사, 복구

### 핵심
사람의 확인은 가장 널리 출시된 최후의 방어선이며, 2026년의 연구 합의는 그것이 빠르게 약화된다는 것이다(피로, 플러딩, 위조된 프롬프트). 제안된 해법은 위험에 비례하는 프롬프트, 행동에 바인딩된 신뢰 렌더링, 복구를 염두에 둔 설계이다. 그러나 진정한 실행 취소는 소수의 행동에 대해서만 존재하고, 변조를 감지할 수 있는(tamper-evident) 에이전트 로그는 제품이 아니라 제안에 머물러 있다.

### 인용 근거
- 승인 피로: 전해 들은 보도에 따르면 사용자가 Claude Code 권한 프롬프트의 93%를 승인한다는 내용이 Anthropic(2026년 3월)의 것으로 전해진다. (search summary, 2차 자료; Anthropic의 게시글과 대조해 확인하지 않음) — [changkun.de blog](https://changkun.de/blog/ideas/human-in-the-loop-agents/)
- IANS가 보도한 한 시뮬레이션 연구에서는 사람이 악의적인 코딩 에이전트 명령의 약 3분의 1을 승인했다. 결론은 승인이 주된 안전장치가 되어서는 안 된다는 것이다. (search summary, 2차 자료) — [IANS](https://www.ians.com/news/approval-fatigue-the-hidden-risk-of-human-oversight-of-ai-agents)
- 2026년 Singapore Consensus 문서는 Perplexity와 Anthropic이 모두 승인 피로를 핵심 취약점으로 꼽는다고 적는다. (search summary) — [arXiv:2608.14611](https://arxiv.org/pdf/2608.14611)
- 승인 플러딩은 신뢰 렌더링이 있어도 모델링된 승인자를 상대로 0.45~0.60의 비율로 성공했다. 신뢰할 수 있는 카드 표시는 위조된 서술의 성공률을 0.54 -> 0.03으로 줄였다. **[RESEARCH]** (search summary) — [arXiv:2610.21063](https://arxiv.org/pdf/2610.21063)
- 위험에 비례하는 확인: 구조적으로 탐지된 커밋 행동만 확인하고, 눈에 띄는 카드는 위험이 높은 행동에만 쓴다. **[RESEARCH]** (search summary) — [arXiv:2609.18411](https://arxiv.org/pdf/2609.18411)
- 승인 탈취("Loopjacking"): 승인된 작업과 실행된 작업이 다르다. **[RESEARCH, 공격]** (search summary) — [arXiv:2609.21081](https://arxiv.org/pdf/2609.21081)
- GUI 에이전트의 가역성: "Task-State Governance: A Virtual Machine for Verifiable and Recoverable GUI Agent Execution"(arXiv 2026년 10월)은 가역적 행동, 보상 가능한(compensatable) 행동, 비가역적 행동을 구분하고, "완전한 가역성이라는 위험한 환상(the dangerous illusion of complete reversibility)"을 경고한다. **[RESEARCH]** (search summary) — [arXiv:2610.20100](https://arxiv.org/pdf/2610.20100)
- IBM Research STRATUS: 클라우드 운영 환경에서, 체크포인트와 함께 에이전트의 각 행동에 대한 실행 취소 연산자를 둔다("undo-and-retry"). **[RESEARCH]** (search summary) — [IBM Research](https://research.ibm.com/blog/undo-agent-for-cloud)
- "Infrastructure for AI Agents"(Chan 외, 2025년 1월)는 에이전트 인프라 프리미티브의 하나로 롤백 인프라를 제안하고, 롤백 요청을 모니터링하고 승인하는 일이 "사람 또는 AI의 노동 면에서 자원 집약적일 수 있다(could be resource intensive in terms of human or AI labour)"고 지적한다. **[RESEARCH/입장 논문]** (search summary) — [arXiv:2501.10114](https://arxiv.org/pdf/2501.10114)
- "Parallax"(2026년 4월)는 추론과 행동을 분리하고, 비가역적인 외부 행동에 대해 사람의 승인을 요구하는 것을 최상위 정책으로 삼자고 주장한다. **[RESEARCH]** (search summary) — [arXiv:2604.12986](https://arxiv.org/pdf/2604.12986)
- Android에서 출시된 투명성 기능: 실시간 진행 상황 보기, 닫을 수 없는 표시기, 그리고 프라이버시 대시보드에 예정된 24시간 어시스턴트 활동 보기이다. **[SHIPPED / 발표됨]** (fetched) — [Google blog, 12 May 2026](https://blog.google/intl/pl-pl/nowosci-produktowe/android-chrome-play/przyszlosc-androida-oparta-na-agentach-tworzenie-gemini-intelligence-na-fundamencie-bezpieczenstwa-i-prywatnosci/)
- 모바일 에이전트를 위한 하이브리드 런타임 검증(명시적인 시스템 수준 위반을 잡는 형식 검증기와 맥락적 위험을 판단하는 VLM 심판의 결합): OS-Sentinel, 2025년 10월. **[RESEARCH]** (search summary, 제목/초록 수준) — [arXiv:2510.24411](https://arxiv.org/pdf/2510.24411)
- 가드레일이 실제로 실행되었다는 TEE 증명("Proof-of-Guardrail", 2026년 3월)과 결제를 위한 에이전트 실행의 TEE 증명. **[RESEARCH]** (search summary, 제목 수준) — [arXiv:2603.05786](https://arxiv.org/html/2603.05786v2); [arXiv:2511.15712](https://arxiv.org/abs/2511.15712)

### 추론
- 휴대폰 에이전트에서는 비가역적 행동의 집합이 크고 흔하다. 메시지 보내기, 결제하기, 게시하기, 삭제하기, 보안 설정 바꾸기가 그렇다. 가역성은 행동별로 분류해야 하고, 그 분류는 인젝션을 당한 모델에서 나와서는 안 된다. 픽셀 행동에서는 이 분류 자체가 어렵다(어느 탭이 "커밋"인가?). 이는 의미론적 행동 계층을 지지하는 또 하나의 논거이다.
- 작업별 가상 디스플레이/샌드박스는 로컬 상태 스냅샷을 가능해 보이게 한다(샌드박스 안의 앱 상태를 롤백한다). 그러나 기기를 떠나는 효과(이미 보낸 송금)는 기껏해야 보상 가능할 뿐이다. 현실적인 복구 설계는 지연 커밋("보내기 취소" 시간 창), 지출 한도, 로그에서 얻는 사후 분쟁 증거를 결합한다.
- 로그는 책임 분쟁(이것을 인가한 것은 사용자인가 에이전트인가?)의 증거가 된다. 그래서 변조 감지 가능성과 서명된 맨데이트(AP2)에 대한 바인딩이 중요하다. OS 프라이버시 대시보드 로그는 출발점이지만, 설명된 바로는 변조를 감지할 수 있는 감사 기록이 아니라 사용자에게 보여 주는 활동 보기이다.
- 에이전트 행동에 대한 이상 탐지(예: "작업은 페이지 요약인데 에이전트가 설정을 열었다")는 제어 흐름 무결성의 GUI 판이다. 이는 앱/패키지 전환에 대한 정책 검사로 구현할 수 있으며, OS는 픽셀을 이해하지 못할 때에도 이 전환을 안정적으로 관측할 수 있다.

### 공백
- 모바일/GUI 에이전트에 한정한 확인 피로의 실사용자 연구는 찾지 못했다. 수치는 코딩 에이전트나 시뮬레이션에서 나온 것이며 일부는 전해 들은 것이다.
- 소비자용 화면 에이전트에 출시된 변조 감지 가능한(해시 체인/증명 기반) 행동 로그는 찾지 못했다.
- GUI 에이전트의 행동 이상 탐지: 이번 세션에서 강력한 1차 평가를 찾지 못했다.
- "93%" 수치는 Anthropic의 원 발표와 대조해 검증해야 한다.

## 6. 다중 에이전트 및 장기(long-horizon) 문제

### 핵심
영속 메모리, 플래너 힌트, 스킬/도구 마켓플레이스, 에이전트 간 채널은 일회성 인젝션을 지속적인 침해로 바꾼다. 2026년에는 대규모의 실제 마켓플레이스 오염이 있었고 메모리 오염 논문이 쏟아져 나왔다. 반면 방어(출처 태그가 붙은 메모리, 쓰기 시점 검사, 서명/검사된 스킬, MCP의 OAuth 의무화)는 초기 단계이며 유용성 비용을 수반한다.

### 인용 근거
- 모바일에서의 플래너 수준 오염: 오염된 MobileRun "AppCards"(접근성 트리를 거치지 않고 계획 단계에 주입되는 앱별 안내)는 추론 구성에서 시험한 두 모델 모두에 대해 ASR 1.0을 달성했다. **[RESEARCH, 공격]** (fetched) — [arXiv:2608.08939](https://arxiv.org/html/2608.08939v1)
- 같은 논문은 다중 에이전트 파이프라인에서 "콘텐츠에서 도출된 하위 목표는 신뢰할 수 없는 것으로 표시된 채로 남아야 한다(content-derived subgoals should remain marked as untrusted)"고 권고하고, 실행기가 행동을 독립적으로 검증할 것을 권고한다. (fetched) — 같은 출처
- OWASP의 에이전트형 Top 10은 메모리 오염을 ASI06으로 올리고 다섯 계층을 든다. 입력 검열, 출처 정보를 갖춘 메모리 새니타이징, 신뢰를 고려한 검색, 행동 모니터링, 포렌식이다. 참조 구현인 "Agent Memory Guard"는 메모리 읽기/쓰기를 검사한다(출시일은 일관되지 않게 보고됨). **[지침 / 초기 도구]** (search summary, 2차 자료) — [Kiteworks summary](https://kiteworks.substack.com/p/ai-agent-memory-poisoning-owasp-defense)
- 경험적 절충: 신뢰 기반 메모리 방어는 오염을 막았지만 장기 메모리의 이점을 없앴다. **[RESEARCH]** (search summary) — [arXiv:2601.05504](https://arxiv.org/pdf/2601.05504)
- 2026년의 메모리 오염 탐지/감사/벤치마크 연구: MemAudit(사후 인과 귀속, 2026년 5월) [arXiv:2605.23723](https://arxiv.org/pdf/2605.23723); MemSentry(2026년 9월) [arXiv:2609.08747](https://arxiv.org/pdf/2609.08747); MemForm 벤치마크(2026년 10월) [arXiv:2610.14431](https://arxiv.org/pdf/2610.14431). **[RESEARCH]** (제목만)
- 스킬 공급망: CSA 연구 노트(2026년 6월 24일)는 연구자들이 2026년 2월에서 5월 사이에 ClawHub(OpenClaw의 마켓플레이스)에서 1,184개가 넘는 악성 스킬을 확인했다고 보고하고, 코드 스캐너로는 스킬 마켓플레이스 오염을 막을 수 없다고 주장하는 2026년 5월의 연구(arXiv:2605.11418)를 인용한다. **[실제 사고]** (search summary; 기반 연구는 읽지 않음) — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-ai-skill-supply-chain-attacks-20260624-csa/)
- MCP 도구 오염(도구 설명에 숨겨진 명령)은 2025년 4월 Invariant Labs가 처음 보였다. CSA의 2026년 5월 노트는 MCP의 문제를 체계적인 설계 결함이라고 부른다. (search summary) — [CSA](https://labs.cloudsecurityalliance.org/research/csa-research-note-mcp-security-crisis-20260504/)
- AppFunctions(Android의 구조화된 에이전트 인터페이스)에는 그 자체로 보고된 위험 유형이 있다. 도구 출력을 통한 프라이버시 유출, 지나치게 넓은 함수 노출을 통한 격리 우회, 악의적인 도구 설명을 통한 LLM 무결성 공격이다. (search summary; 연구를 인용하는 커뮤니티 GitHub 프로젝트에서 나온 것 — 2차 자료) — [appfunction-guard](https://github.com/yangyang2857-rgb/appfunction-guard)
- Rule of Two는 그것만으로는 에이전트 간 전파를 가두지 못한다. (2차 논평) — [Substack](https://kenhuangus.substack.com/p/the-rule-of-two-vs-reality-why-metas)

### 추론
- 화면 에이전트에서 "공급망"에는 코드처럼 보이지 않는 것들이 포함된다. 앱별 플레이북/스킬, 캐시된 UI 맵, 학습된 단축 경로, 앱이 제공하는 함수 설명이 그렇다. 각각은 플래너가 화면 콘텐츠보다 더 신뢰하는 텍스트이므로, 이를 오염시키면 관측 수준의 방어를 완전히 우회한다(AppCard 결과가 이를 보여 준다). 이것들에는 코드에 적용하는 무결성 조치, 곧 서명, 출처 관리, 검토, 최소 권한이 필요하다.
- GUI에서 구조화된 인터페이스로 옮겨 가도 인젝션은 없어지지 않는다. 인젝션이 도구 설명과 도구 출력으로 자리를 옮길 뿐이다(MCP 도구 오염; AppFunctions의 위험 유형). 얻는 것은 그때부터 구조적 방어(1절)를 적용할 수 있다는 점이다.
- 메모리 출처 관리는 테인트 추적의 장기 판이다. 신뢰할 수 없는 화면 콘텐츠를 처리하는 동안 기록된 메모리는 낮은 무결성 레이블을 달아야 하고 결코 명령으로 승격되어서는 안 된다. 출시된 휴대폰 어시스턴트에서 이렇게 한다는 증거는 찾지 못했다.

### 공백
- A2A 프로토콜 보안(에이전트 카드, 서명, 인증)은 이번 세션에서 조사하지 않았다.
- 출시된 모바일 어시스턴트(Gemini, Siri, Galaxy AI)에 한정한 메모리 오염 연구는 찾지 못했다.
- 1,184라는 수치와 arXiv:2605.11418의 주장은 CSA의 노트를 거쳐 온 것이다. 원문은 읽지 않았다.
- MCP 사양에서 OAuth의 현재 규범적 지위(HTTP에 대해 선택인지 필수인지)는 출처 간에 상충한다.

## 7. 예상되는 방향과 해결되지 않은 문제

### 핵심
신뢰할 만한 합의는 다음과 같다. (a) 프롬프트 인젝션은 모델 안에서 해결되지 않을 것이므로, 보안은 침해된 에이전트가 할 수 있는 일을 한정하는 시스템 설계에서 나와야 한다. (b) 에이전트는 구조화된 인터페이스가 있는 곳에서는 그쪽으로 옮겨 가고, GUI 조작은 더 엄격한 격리 아래에서 롱테일 폴백으로 남을 것이다. 아무도 갖고 있지 못한 것은, 범용 화면 에이전트의 유용성을 유지하면서 화면의 일부를 통제하는 적응형 공격자에 대한 보장을 제공하는 방어이다.

### 인용 근거
- 벤더의 인정: Meta — 프롬프트 인젝션은 "모든 LLM의 근본적이고 해결되지 않은 약점(a fundamental, unsolved weakness in all LLMs)"이다 [Meta, 31 Oct 2025](https://ai.meta.com/blog/practical-ai-agent-security/); OpenAI — "완전히 해결될 가능성은 낮다(unlikely to ever be fully solved)" [OpenAI, Dec 2025](https://openai.com/index/hardening-atlas-against-prompt-injection/). (search summary)
- OpenAI, Anthropic, DeepMind의 연구자들은 기존의 탐지/프롬프트 방어가 적응형 공격에서 실패한다는 것을 공동으로 보였다. — [arXiv:2510.09023](https://arxiv.org/abs/2510.09023)
- Willison: 신뢰할 수 있는 방어가 곧 나올 것이라고 낙관하지 않는다. Rule of Two가 구할 수 있는 최선의 실용적 지침이다. (fetched) — [Willison, 2 Nov 2025](https://simonwillison.net/2025/Nov/2/new-prompt-injection-papers/)
- 설계 패턴 논문의 저자들(Tramèr 포함): 범용 에이전트는 안전하게 만들기 어렵다. 능력을 의도적으로 제한한 애플리케이션별 에이전트가 실용적인 길이며, 시스템 수준의 설계는 모델이 취약하더라도 에이전트를 안전하게 만들 수 있다. (search summary) — [arXiv:2506.08837](https://arxiv.org/html/2506.08837v3)
- CaMeL-CUA 저자들(Shumailov, Tramèr, Papernot 외): UI 워크플로는 사전 계획이 가능할 만큼 구조적으로 예측 가능하다. 그러나 데이터 흐름 공격(분기 조종)과 CUA 도구에 대한 의미론적 정책은 미해결로 남아 있다. (fetched) — [arXiv:2601.09923v2](https://arxiv.org/html/2601.09923v2)
- Android의 플랫폼 방향은 명시적으로 하이브리드이다. 앱이 채택한 곳에서는 구조화된 AppFunctions를 쓰고, UI 자동화를 폴백으로 두며, 둘 다 Android 17에서 확대된다. (search summary, 2차 자료) — [FoneArena](https://www.fonearena.com/blog/476208/google-android-roadmap-ai-agents-app-automation.html)
- 결제는 에이전트가 양식에 카드 번호를 입력하는 방식이 아니라 구조화되고 서명된 채널(AP2 맨데이트, ACP의 범위가 한정된 토큰, 네트워크의 에이전트형 토큰)로 옮겨 가고 있다. (search summary) — [ap2-protocol.org](https://ap2-protocol.org/); [Stripe](https://docs.stripe.com/agentic-commerce/concepts/shared-payment-tokens)
- 결정론적인 대역 외 강제는 대역 내 방어보다 적응형 공격자에게 더 어려운 표적이라는 가설이 있다(입증된 것은 아니다). 이는 유용성과 연산 비용을 치른다(한 재현 연구에서 LLM 호출이 약 15배). (fetched) — [arXiv:2606.26479](https://arxiv.org/html/2606.26479v1)

### 추론
- 예상되는 궤적(나의 종합): 단기(지금 출시되고 있는 것) — 격리와 확인이다. 앱 허용 목록, 격리된 작업 창, 구매에 대한 필수 확인, 눈에 보이는 표시기, 활동 로그, 강화된 모델이 여기에 든다. 중기 — 의미론적 행동 계층(앱 함수, 패키지 출처를 갖춘 접근성 노드 수준의 행동)이 참조 모니터, 테인트 레이블, 그리고 위험에 적응하고 행동에 바인딩된 신뢰 확인이 작동할 수 있게 한다. 신원/맨데이트 프로토콜이 인가와 결제 단계를 넘겨받는다. 장기/연구 — 관측 전 계획 아키텍처와 GUI를 위한 IFC, 클라우드 계획과 결합한 기기 내 인지, 서명된 의도에 묶인 변조 감지 가능한 로그이다.
- "GUI에서 구조화된 인터페이스로"의 배경에 있는 경제 논리: 1절의 모든 구조적 방어와 4절의 모든 위임 메커니즘에는 타입이 있는 행동과 귀속을 밝힐 수 있는 주체가 필요하다. GUI 조작이 계속 남는 이유는 앱/사이트의 구조화된 인터페이스 채택이 느리고 일부 제공자는 이를 노출하기를 거부할 것이기 때문이다. 그래서 GUI 조작의 롱테일은 가장 약한 보장과 가장 비협조적인 상대방이 겹치는 바로 그 지점이다.
- 해결되지 않은 문제를 화면 에이전트에 대한 중요도 순으로 대략 나열하면 다음과 같다.
  1. 관측이 픽셀일 때의 (제어 흐름만이 아닌) 데이터 흐름 무결성: 분기 조종, 위조된 UI, 허용된 앱 안에서 정당해 보이지만 공격자가 작성한 콘텐츠.
  2. 강제 가능한 신뢰 레이블로서 모델에 노출되는 화면 영역 수준의 출처(특히 신뢰할 수 있는 앱 안의 웹뷰와 사용자 생성 콘텐츠).
  3. 유용성 비용: 단발 계획은 능력의 큰 부분을 잃고, 신뢰 기반 메모리 방어는 메모리의 가치를 지운다.
  4. 의미를 유지하는 확인: 사용자가 "허용"을 탭하도록 길들이지 않으면서 바인딩, 플러딩 방지, 위험 보정을 갖추는 것.
  5. 사용자 자신의 로그인된 세션을 통해 행동하는 에이전트(주변 권한)를 위한 신원과 위임. 여기에는 기기 내 에이전트가 프라이버시를 해치지 않고 증명 가능한 신원을 얻는 방법이 포함된다.
  6. 비가역적 행동과 책임: 인젝션을 당한 에이전트가 결제하거나 전송했을 때 누가 손실을 부담하는가, 어떤 증거가 인정되는가.
  7. 평가: 모바일 에이전트를 위한 표준 적응형 공격 벤치마크가 없다. 벤더의 수치는 스스로 고른 공격을 상대로 한 것이다.
  8. 접근성 API 위에 만든 서드파티 에이전트. 이들은 퍼스트파티의 격리를 전혀 받지 않으면서 기기에 대한 거의 전면적인 권한을 얻는다.
  9. 코드가 아닌 산출물(스킬, 앱 안내 카드, 도구 설명, 메모리)의 공급망.

### 공백
- "GUI 대 구조화된 인터페이스"를 예측으로 다룬 Schneier, Carlini, Tramèr, Shumailov의 2026년 직접 발언은 가져오지 못했다. 위의 방향은 그들의 논문과 플랫폼 로드맵에서 추론한 것이며, 인용된 전망에서 나온 것이 아니다.
- Android에서 에이전트 행동의 어느 정도 비율이 AppFunctions를 거치고 어느 정도가 UI 자동화를 거치는지에 관한 정량 데이터는 없다.
- Apple의 방향(App Intents 기반 에이전트 대 화면 조작)은 이번 세션에서 조사하지 않았다.
- 규제 측면의 동인(EU AI Act, 에이전트가 개시한 거래에 대한 결제 규제)은 이 노트의 범위 밖이며 조사하지 않았다.
