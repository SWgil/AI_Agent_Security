# 브라우저 AI 에이전트의 웹 플랫폼·웹사이트 운영자 측면: 표준, 프로토콜, 사이트 측 통제 (2026년 11월 18일 기준 현황)

보고서 작성자를 위한 읽기 규칙:

- **(fetched)** = 이번 세션에서 1차 페이지/문서를 직접 열었고, 해당 서술은 그 본문에서 나온 것이다. **(search summary)** = 검색 결과 요약에서 가져온 것이다. 페이지 자체는 열지 않았으므로 신뢰도가 낮은 것으로 취급하고, 수치를 인용하기 전에 다시 확인해야 한다. **(earlier notes)** = 이미 `화면 조작 에이전트 보안 기술/emerging_defenses.md` 또는 `standards_regulation_appside.md`에 있는 내용이다. 이번 세션에서 정정하거나 확인한 경우에만 다시 적었다.
- 출처 뒤의 날짜는, 알 수 있는 경우 그 문서 자체의 날짜이다.
- 이번 세션에서 직접 읽고 이전 노트를 정정한 사항은 놓치지 않도록 여기에 모아 둔다:
  1. **AP2 mandate 명칭이 바뀌었다.** 현재 명세(v0.2)는 *Checkout Mandate*와 *Payment Mandate*를 정의하며, 각각 *open* 형태와 *closed* 형태가 있다. "Intent Mandate / Cart Mandate"는 v0.1(2025년 9월)의 용어이다. AP2는 FIDO Alliance에 기증되었다.
  2. **MCP 인가(authorization)는** 2026-07-28 개정판에서도 프로토콜 수준에서 **여전히 "OPTIONAL"이다.** 이 개정판이 "HTTP 전송에 OAuth 2.1을 요구한다"는 벤더 측 주장은 명세 본문의 내용과 다르다.
  3. **WebMCP의 진입점은 `document.modelContext`이다.** `navigator.modelContext`(옛 이름)가 아니다.
  4. **Web Bot Auth WG 초안은 -01(2026년 10월 29일)이며**, Cloudflare와 Google이 공동 저자이다.
  5. **Amazon v. Perplexity**: 판결문 전문을 읽었다. 2026년 8월 4일 선고, No. 26-1444. 판결문에 인쇄된 지방법원 사건번호는 3:25-cv-09514-MMC이다(검색 요약 하나는 "25-cv-8431"이라고 했는데, 틀렸거나 다른 식별자이다). 판시는 "authorization"이 아니라 "access" 요건에 관한 것이다.
  6. **ChatGPT Instant Checkout은 2026년 3월에 가맹점 "Apps" 안으로 옮겨졌다.** 이전 노트의 "[SHIPPED (ChatGPT Instant Checkout) / expanding]"은 더 이상 맞지 않는다.

## 1. 웹에서 에이전트를 위한 구조화된 인터페이스(WebMCP, MCP + 인가, A2A, NLWeb, llms.txt)와 AppFunctions / App Intents와의 비교

### 핵심
이제 웹에는 브라우저 수준의 "클릭을 대신하는 구조화된 대안"이 있다. 하나는 WebMCP로, W3C Community Group 초안이며 Chrome에 오리진 트라이얼(origin trial)로 구현되어 있다(M149–M156, 정식 출시 목표는 M157). 다른 하나는 OAuth 2.1 기반 인가 명세로 보호되는 원격 MCP 서버라는 서버 측 경로이다. WebMCP의 보안 모델은 설계상 얇다. 동일 출처(same-origin) 범위 제한과 Permissions Policy 스위치는 브라우저가 강제하지만, 도구 포이즈닝(tool poisoning), 출력 인젝션(output injection), 기만적인 도구에 대한 방어는 명시적으로 에이전트에게 맡겨져 있고, 명세에는 아직 사용자 확인을 위한 프리미티브가 없다.

### 인용 근거

**WebMCP**
- 명세의 상태 표시줄: "Draft Community Group Report, 13 November 2026". W3C Web Machine Learning Community Group이 발행했다. "W3C 표준이 아니며 W3C 표준 트랙에 있지도 않다"("not a W3C Standard nor is it on the W3C Standards Track"). 편집자: Brandon Walderman(Microsoft), Khushal Sagar(Google), Dominic Farolino(Google), Samuel Dallstream(Microsoft). (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/)
- API 표면: `document.modelContext`(`[SecureContext, SameObject]`, `Navigator`가 아니라 `Document`의 partial interface). `ModelContext`에는 `registerTool(tool, options)`, `getTools(options)`, `executeTool(tool, inputObject, options)`와 이벤트 `toolchange`, `toolactivated`, `toolcancel`이 있다. 등록 옵션에는 `exposedTo`와 `signal`이 포함되고, `getTools`는 `fromOrigins`를 받는다. (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/)
- 도구 주석(annotation, 불리언, 기본값 false): `readOnlyHint`, `untrustedContentHint`(출력에 도구 작성자가 신뢰할 수 없다고 보는 데이터가 들어 있음), `consequentialHint`(중대한 효과, 현실 세계에 미치는 효과, 또는 되돌릴 수 없는 효과), `debugging`. (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/)
- Permissions Policy 기능 이름은 `tools`이고 기본 허용 목록은 `'self'`이다. 설명서(explainer): "기본적으로, 문서가 등록한 도구는 그 문서 자신과 같은 트리 안의 동일 출처 문서에만 노출된다"("By default, tools registered by a document are only exposed to itself, same-origin documents in the same tree"). "기본적으로 WebMCP는 최상위 `Window`와 그 동일 출처 iframe에서 활성화된다"("By default, WebMCP is enabled in top-level `Window`s and its same-origin iframes"). 권한이 비활성화되어 있으면 호출은 `NotAllowedError`로 거부된다. (fetched) — [명세](https://webmachinelearning.github.io/webmcp/); [설명서 저장소](https://github.com/webmachinelearning/webmcp)
- 설명서의 위치 설정: 도구는 자연어 설명과 스키마를 가진 JavaScript 함수이며, MCP 서버 같은 백엔드 통합을 대신하는 클라이언트 측 대안이다. 2025년 8월 13일에 처음 공개되었다. 원저자는 Microsoft(Walderman, Lee, Nolan)와 Google(Bokan, Sagar, Van Opstal) 소속이다. 선언형 폼 기반 API는 "검토 중이지만 명세의 일부는 아니다"("is being explored but is not part of the specification"). (fetched) — [설명서 저장소](https://github.com/webmachinelearning/webmcp)
- 보안/프라이버시 절의 인용: 프롬프트 인젝션은 "에이전트의 동작을 조작하기 위해 악의적인 지시가 도구 메타데이터, 입력, 출력에 삽입되는"("malicious instructions are embedded in tool metadata, inputs, or outputs to manipulate agent behavior") 경우이다. 도구 포이즈닝 = "도구 메타데이터(이름, 설명, 매개변수 설명)에 삽입된 악의적인 지시"("Malicious instructions embedded in tool metadata (name, description, parameter descriptions)"). 출력 인젝션 — "도구의 반환값은 에이전트의 언어 모델이 신뢰할 수 있는 정보로 처리한다."("Tool return values are processed by the agent's language model as trusted information."). "WebMCP 도구가 선언한 의도가 실제 동작과 일치한다는 보장은 없다."("There is no guarantee that a WebMCP tool's declared intent matches its actual behavior."). "사이트는 민감한 사용자 데이터를 뽑아내기 위해 매개변수가 매우 많은 WebMCP 도구를 설계할 수 있다"("Sites can design highly parameterized WebMCP tools to extract sensitive user data"). "기본적으로 WebMCP는 엄격한 동일 출처 격리를 강제한다"("By default, WebMCP enforces strict same origin isolation"). 사생활 보호 브라우징은 "의도치 않게 이 경계를 넘어 정보를 유출할 수 있다"("may inadvertently leak information across this boundary"). (fetched) — [WebMCP 명세, 보안 및 프라이버시 고려사항](https://webmachinelearning.github.io/webmcp/)
- 명세에 열거된 완화책: Permissions Policy로 비활성화. 입력 길이 제한("이 제한이 프롬프트 인젝션 공격을 완전히 해결하지는 못한다", "This restriction would not fully solve prompt injection attacks"). untrusted 주석("페이로드를 한층 강화된 보안 수준으로 다뤄야 한다는 것을 클라이언트에 알리는 신호로 작용한다", "acts as a signal to the client that the payload requires heightened security handling"). consequential 주석("위험도가 높은 도구를 실행하기 전에 사용자 확인 프롬프트를 선택적으로 의무화할 수 있다", "they can selectively enforce mandatory user confirmation prompts before executing high-stakes tools"). 그리고 "에이전트는 보안을 사이트가 주장하는 도구 설명이나 주석에만 의존할 수 없다"("agents cannot rely solely on site-asserted tool descriptions or annotations for security"). 명세는 에이전트가 사용자의 신원과 로그인된 세션을 물려받으며, 악의적인 사이트가 힌트를 빠뜨리거나 거짓으로 표시할 수 있다고 적고 있다. (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/)
- 현재 명세 본문에는 `requestUserInteraction`이나 그 밖의 사용자 확인 메서드가 없다. 설명서는 미해결 질문으로 "도구가 명시적인 사용자 승인을 필요로 할 때 도구가 사용자에게 확인을 요청하는 방법을 모색한다"("Exploring a way for a tool to prompt the user for confirmation when tools require explicit user authorization.")를 들고 있다. 명세는 호출하는 쪽이 사람인지 에이전트인지, 어떤 에이전트인지를 사이트가 알 수 있는 방법을 전혀 기술하지 않는다. (fetched) — [설명서](https://github.com/webmachinelearning/webmcp); [명세](https://webmachinelearning.github.io/webmcp/)
- Chrome 현황: "Intent to Experiment: WebMCP"가 2026년 5월 15일 blink-dev에 게시되었다. 오리진 트라이얼은 Desktop, Android, WebView에서 M149부터 M156까지이고, 정식 출시는 M157로 계획되어 있다. 2026년 5월 18일 Mike Taylor가 LGTM을 주었다(Chrome이 2주 단위 릴리스로 옮겨 가고 있어 기간이 평소보다 길다). TAG 리뷰는 "pending"이다("출시 전에 TAG 리뷰를 요청할 것이다", "We will request TAG review before shipping"). Gecko와 WebKit: "No signal". 명시된 위험에는 "에이전트가 WebMCP 도구를 호출할 때 사이트가 민감한 정보를 소비하고 노출할 수 있다"("Sites may consume and expose sensitive information when their WebMCP tools are called by agents")는 점과 LLM 에이전트가 간접 프롬프트 인젝션에 취약하다는 점이 포함된다. 소비자로 거론된 것은 클라우드 LLM 에이전트가 도구를 발견하고 호출하게 해 주는 Chrome 확장 프로그램, 그리고 온디바이스 모델을 쓰는 Prompt API이다. 에이전트는 "브라우저가 제공할 수도 있고 사이트 작성자가 제공할 수도 있다"("may be provided by the browser or by the site author"). 이 intent에는 자사 에이전트(Gemini in Chrome)가 거론되어 있지 않다. (fetched) — [blink-dev 스레드](https://groups.google.com/a/chromium.org/g/blink-dev/c/gmYffo5WOE8/m/OJxuQRP3AAAJ)
- Chrome 149는 2026년 6월 2일 안정 버전에 도달했다. `navigator.modelContext`는 이전 이름이었고 2026년 7월경 `document.modelContext`로 옮겨졌으며 옛 표기는 Chrome 150에서 지원 중단(deprecated)되었다. 이는 블로그 하나에만 근거한 주장이다. Edge: 출처마다 말이 다르다(플래그 뒤에 있다는 것과, Edge 150용 오리진 트라이얼이 2026년 7월 2일자로 등재되어 2027년 3월 30일에 만료된다는 것). Microsoft의 1차 페이지는 찾지 못했다. Firefox/Safari는 입장을 정하지 않았다. (search summary) — [dev.to](https://dev.to/thousand_miles_ai/webmcp-in-chrome-149-web-pages-get-a-tool-api-for-ai-agents-bfi); [mcpplaygroundonline](https://mcpplaygroundonline.com/blog/what-is-webmcp); [mcpbundles](https://www.mcpbundles.com/guides/webmcp-browser-support)
- 배포 근거: W3C/GS1 워크숍(취리히, 2026년 9월 8–9일)에서 Shopify는 자사 스토어프런트에 WebMCP를 탑재했다고 보고했고, "페이지를 이동할 때마다 등록된 도구가 사라진다"("every navigation tears down registered tools")고 지적했으며, 결제(checkout)를 위해 사용자가 개입하는(user-in-the-loop, elicitation) 프리미티브를 요청했다. W3C는 WebMCP를 포함하게 될 **Agentic Web Working Group** 제안의 헌장(charter)을 논의하고 있다(즉 표준 트랙으로의 이동이다). 이 보고서는 초안으로 표시되어 있다. (fetched) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html)

**MCP 원격 서버와 인가 명세**
- 읽은 현재 개정판: **2026-07-28**(URL과 예시에 `MCP-Protocol-Version: 2026-07-28`). "인가는 MCP 구현에서 **OPTIONAL**이다. 지원하는 경우: HTTP 기반 전송을 사용하는 구현은 이 명세를 **따라야 한다(SHOULD)**"("Authorization is **OPTIONAL** for MCP implementations. When supported: Implementations using an HTTP-based transport **SHOULD** conform to this specification"). STDIO 전송은 자격 증명을 환경에서 가져와야 한다. (fetched) — [MCP 인가 명세](https://modelcontextprotocol.io/specification/latest/basic/authorization)
- OAuth 2.1(draft-ietf-oauth-v2-1-13), RFC 8414, RFC 7591, RFC 8707, RFC 9728, RFC 9207, OAuth Client ID Metadata Documents(draft-ietf-oauth-client-id-metadata-document-00) 위에 만들어졌다. MCP 서버 = OAuth 리소스 서버, MCP 클라이언트 = OAuth 클라이언트. (fetched) — 같은 출처
- 인가를 사용할 때의 요구 수준: 인가 서버는 OAuth 2.1을 구현해야 한다(MUST). MCP 서버는 Protected Resource Metadata(RFC 9728)를 구현해야 하고(MUST) 클라이언트는 이를 발견(discovery)에 사용해야 한다(MUST). 클라이언트와 서버는 Client ID Metadata Documents를 지원해야 한다(SHOULD). Dynamic Client Registration은 "지원 중단되었으며 하위 호환성을 위해 유지된다"("deprecated and retained for backwards compatibility"). 클라이언트는 인가 요청과 토큰 요청에 RFC 8707 `resource` 매개변수를 보내야 한다(MUST). 클라이언트는 RFC 9207에 따라 `iss`를 검증해야 한다(MUST)(향후 개정판에서는 인가 서버가 `iss`를 포함하는 것이 MUST가 될 것으로 예상된다). (fetched) — 같은 출처
- 토큰 규칙: "MCP 서버는 액세스 토큰이 자신을 대상(audience)으로 하여 발급된 것인지 **반드시** 검증해야 한다"("MCP servers **MUST** validate that access tokens were issued specifically for them as the intended audience"). "MCP 서버는 그 밖의 어떤 토큰도 수락하거나 전달해서는 **안 된다**"("MCP servers **MUST NOT** accept or transit any other tokens")(토큰 패스스루 금지). 토큰은 절대 쿼리 문자열에 넣지 않는다. 최소 권한 스코프 선택과 함께 403 `insufficient_scope`를 통한 스텝업(step-up). 보안 고려사항은 대상(audience) 바인딩, 토큰 탈취, 인가 코드 보호, 믹스업(mix-up) 공격과 혼동된 대리인(confused deputy) 공격, 오픈 리디렉션, Client ID Metadata Document 보안을 다룬다. (fetched) — 같은 출처
- 이로써 이전 노트에서 표시해 둔 충돌이 해소된다. CSA의 서술(인가는 선택 사항)은 명세와 일치한다. 2026-07-28이 "HTTP 전송에 OAuth 2.1을 요구한다"는 벤더의 서술은 "HTTP 위에서 인가를 한다면 이 OAuth 2.1 프로파일이어야 한다"는 의미에서만 정확하다. (fetched 대 earlier notes) — [MCP 명세](https://modelcontextprotocol.io/specification/latest/basic/authorization)

**A2A, NLWeb, llms.txt**
- A2A: Google이 2025년 4월에 출시했고 2025년 6월에 Linux Foundation에 기증했다. 한 사이트 표본을 감사한 결과 약 10%의 사이트에서 agent card가 발견되었는데 "대부분 AI/개발자 생태계"("mostly AI/developer ecosystem")였다. AP2는 스스로를 "오픈소스 Agent2Agent (A2A) 프로토콜의 확장"("an extension for the open-source Agent2Agent (A2A) protocol")이라고 설명하며, Universal Commerce Protocol(UCP)의 확장으로도 제공된다. (A2A는 search summary, AP2 문장은 fetched) — [dev.to 개관](https://dev.to/sebastian_chedal/what-mcp-a2a-and-ucp-mean-for-your-website-in-2026-3aij); [ap2-protocol.org](https://ap2-protocol.org/)
- NLWeb(Microsoft, Build 2025년 5월): 모든 NLWeb 인스턴스는 MCP 서버이기도 하다. Wix는 2026년 3월에 "운영 중인 도메인 800만 개"("8 million live domains")에 NLWeb 엔드포인트를 부여했다(기본적으로 인증 뒤에 있다고 보도됨). 배포 수에 대한 공개된 집계는 없다. (search summary) — [VentureBeat](https://venturebeat.com/ai/the-battle-to-ai-enable-the-web-nlweb-and-what-enterprises-need-to-know/); [presenc.ai](https://presenc.ai/research/nlweb-adoption-2026); [hidden-layer 블로그](https://hidden-layer-blogs.pages.dev/post/wix-nlweb-platform-shift)
- llms.txt: 채택 수치는 조사마다 다르다(Fortune 500의 7.4%, 도메인 300,000개 중 10.13%, 다른 감사에서는 49%). Google은 2026년 5월에 이를 Lighthouse의 에이전트 관련 감사 항목에 추가했다. 2025년 7월 Ahrefs 분석은 주요 LLM 제공자 가운데 이를 파싱하는 곳이 없다고 밝혔다. 이것은 콘텐츠 발견을 위한 힌트이지 행위 인터페이스가 아니며 보안상의 의미는 전혀 담고 있지 않다. (search summary, 출처 간 수치가 충돌함) — [ppc.land](https://ppc.land/google-adds-llms-txt-to-lighthouse-as-agentic-web-standards-heat-up/); [limy.ai](https://limy.ai/blog/llms-txt-in-2026-the-full-guide)

**모바일 비교 지점(이전 노트에서 가져온 것이며 여기서 재검증하지 않음)**
- Android AppFunctions: 앱이 함수에 주석을 단다. 호출은 특권/시스템 호출자로 제한된다. 2027년 4월 14일부터 Play 정책은 에이전트를 속이거나 조작하는 AppFunctions를 금지한다. Apple: App Intents는 Siri가 서드파티 앱에서 행동할 수 있는 유일한 경로이며 개발자가 옵트인한다. (earlier notes) — [Play Console 도움말](https://support.google.com/googleplay/android-developer/answer/16585319)

### 추론
- WebMCP 대 AppFunctions/App Intents — 발상은 같고 신뢰의 근거(trust anchor)가 다르다:
  - *누가 호출할 수 있는가*: WebMCP 도구는 페이지에 붙어 있는 에이전트라면 무엇이든(브라우저 내장, 확장 프로그램, 사이트 자체의 에이전트) 호출할 수 있다. 명세에는 호출자 인증이 없다. AppFunctions는 OS 권한으로 호출자를 제한하고, App Intents는 OS 어시스턴트가 호출한다. 웹에는 "특권 시스템 호출자"에 해당하는 문지기가 없다.
  - *누가 도구를 보증하는가*: Android에서 도구는 서명 인증서를 가진 패키지에서 나오며 Play 정책에 따라 심사된다(2027년 4월부터는 명시적인 조작 금지 규칙이 있다). 웹에서 도구는 출처(origin)에서 나오며, 아무도 이를 심사하지 않고, 명세는 설명과 힌트가 사이트의 주장일 뿐이며 거짓일 수 있다고 밝힌다.
  - *범위 경계*: 출처(기본은 동일 출처, 교차 출처는 `exposedTo`/`fromOrigins`와 Permissions Policy를 통해서만) 대 패키지/UID.
  - *확인*: WebMCP도, 직접 읽은 MCP 인가 페이지도 에이전트가 위조할 수 없는 확인 표면을 제공하지 않는다. WebMCP는 사이트가 제공하는 `consequentialHint`만 내놓는다. Android에는 확인을 위한 OS 소유 UI가 있다.
- WebMCP는 프롬프트 인젝션을 없애지 못한다. 그것을 페이지의 픽셀/DOM에서 도구 메타데이터와 도구 출력으로 옮겨 놓을 뿐이다. 이는 이전 노트가 AppFunctions/MCP에 대해 기술한 것과 같은 이동이다. 얻는 것은 참조 모니터(reference monitor)와 출처 정책(예: Chrome의 Agent Origin Sets, 6절)이 결합할 수 있는, 타입이 지정된 행위 공간이다.
- WebMCP 도구는 사용자의 쿠키를 가진 채 페이지 안에서 실행되므로, 사이트가 따로 계측하지 않는 한 WebMCP 호출은 서버 측에서 사용자 자신의 행위와 구별되지 않는다. 따라서 WebMCP는 사이트에 구조화된 인터페이스를 주지만 에이전트 식별은 주지 않는다.
- MCP의 인가 프로파일은 "어떤 클라이언트가 어떤 사용자를 위해 어떤 스코프로 이 서버를 호출할 수 있는가"(대상 바인딩된 토큰, 패스스루 금지)를 해결하지만, 선택 사항이며 모델이 도구 설명이나 결과를 가지고 무엇을 하는지에 대해서는 아무 말도 하지 않는다.

### 공백
- Edge의 실제 WebMCP 상태(플래그인지 오리진 트라이얼인지)는 검증되지 않았다. Microsoft의 1차 출처를 찾지 못했다.
- Gemini in Chrome, Copilot in Edge, ChatGPT Atlas, Comet, Claude in Chrome이 실제 운영 환경에서 WebMCP 도구를 소비하는지는 확인하지 못했다. Chrome intent는 확장 프로그램과 Prompt API만 거론한다.
- `navigator.modelContext`가 Chrome 150에서 제거/지원 중단되었다는 주장은 블로그 하나에 근거한다.
- A2A의 보안 세부 사항(서명된 agent card)과 NLWeb/A2A 채택 현황은 검색 요약 이상으로 조사하지 않았다.
- Apple과 Android의 구조화된 인터페이스는 이번 세션에서 재검증하지 않았다.

## 2. 에이전트 식별: Web Bot Auth, user-agent 문자열, robots.txt, Cloudflare 정책, 그리고 로컬 브라우저의 한계

### 핵심
Web Bot Auth는 이제 IETF 워킹그룹 초안(-01, 2026년 10월 29일, 저자는 Cloudflare + Google)이다. 이 초안이 증명하는 것은 특정 URL에 공개된 키의 보유자가 요청에 서명했다는 사실뿐이며, 누가 에이전트를 운영하는지, 에이전트가 무해한지, 권한을 받았는지는 명시적으로 증명 대상이 아니다. 클라우드에서 호스팅되는 에이전트(OpenAI의 클라우드 브라우저, `Google-Agent` 요청의 일부)에 배포되어 있고 CDN에서 검증되지만, 사용자 자신의 로컬 브라우저를 조작하는 에이전트(Comet, Atlas agent mode, Claude in Chrome, Gemini in Chrome)는 브라우저의 통상적인 트래픽을 보내므로 이것으로 식별되지 않는다. 2026년 9월의 W3C 워크숍은 "주요 에이전트 제공자들 사이에 스스로를 식별하는 문제에 대한 합의가 없다"("no consensus among major agent providers on identifying themselves")고 기록했다.

### 인용 근거

**직접 읽은 초안**
- `draft-ietf-webbotauth-httpsig-protocol-01`, "HTTP Message Signatures for automated traffic", 2026년 10월 29일자, 2027년 5월 2일 만료. 저자는 Thibault Meunier(Cloudflare)와 Sandor Major(Google). Web Bot Auth WG 문서이다. 문서 헤더에는 Standards Track이라고 되어 있다(datatracker 메타데이터에는 아직 의도된 지위가 표시되지 않는다). (fetched) — [datatracker](https://datatracker.ietf.org/doc/draft-ietf-webbotauth-httpsig-protocol/)
- 작동 방식: 헤더 `Signature`, `Signature-Input`, `Signature-Agent`(Structured Fields 딕셔너리이며, 검증자가 레거시인 단순 문자열 형태를 받아들일 수도 있다). 서명은 `@authority`/`@target-uri` 중 적어도 하나와, `signature-agent` 필드가 있으면 그 필드를 포함해야 한다. 매개변수는 `created`, `expires`(`created` 이후 24 h 이하를 권장), `keyid`(JWK thumbprint), `tag="web-bot-auth"`. 키는 `/.well-known/http-message-signatures-directory`에서 HTTPS로 제공되는 JWKS이다(미디어 타입 `application/http-message-signatures-directory+json`). (fetched) — 같은 출처
- 서명이 증명하는 것과 증명하지 않는 것(원문 그대로):
  - "해석된 URL에 대한 유효한 서명은, 그 URL이 공개하는 키의 보유자가 서명 대상 메시지에 서명했음을 증명한다."("A valid signature over a resolved URL proves that a holder of a key that URL publishes signed the covered message.") (§4.1)
  - "누가 Agent를 운영하는지, Agent가 무해한지, 요청이 권한을 받았는지에 대해서는 아무것도 말해 주지 않는다."("It says nothing about who operates the Agent, whether the Agent is benign, or whether the request is authorized.") (§4.1)
  - "그 URL의 운영자가 정직하다거나, 모두가 알고 있는 바로 그 당사자와 동일하다는 것을 말해 주지 않는다."("It does not say that the operator of that URL is honest, or that it is the same party everyone knows about.") (§4.4)
  - "이 프로토콜은 사람 사용자를 인증하지 않는다."("This protocol does not authenticate human users.") (§4.6)
  - "서명되지 않은 요청은 발신자가 사람인지 자동화된 것인지, 또는 식별을 회피하고 있는지를 확정해 주지 않는다."("An unsigned request does not establish whether the sender is human or automated, or whether it is evading identification.") (§6.11)
  - 중첩/전달된 서명에 관하여: "이는 인가, 위임, 동의를 표현하지 않는다."("it does not express authorization, delegation, or consent.") (§5.2.2). 부록 D.2: "Alice의 에이전트가 원격 브라우저에 자신을 대신해 행동할 권한을 주었다는 것을 말해 주지 않는다."("It does not say that Alice's agent authorized the remote browser to act for it.")
  (fetched) — 같은 출처
- 프라이버시(§7.1): "이 프로토콜은 수신자가 동일한 식별자를 담은 요청들을 알아볼 수 있게 한다"("This protocol enables receivers to recognise requests that carry the same identifier"). "식별자가 바뀌지 않으면 키 교체로는 이러한 상관 분석을 막지 못한다"("Key rotation does not prevent this correlation when the identifier remains unchanged"). "이 프로토콜은 그러한 식별을 피해야 하는 에이전트에는 적합하지 않다."("This protocol is unsuitable for agents that need to avoid such identification."). 이 절은 "식별자가 개별 사람과 연결될"("whose identifier would be associated with an individual human") 에이전트도 다룬다. (fetched, 페이지 본문의 마지막 약 2.5%는 읽지 못함) — 같은 출처
- 초안은 사용자 자신의 브라우저에 내장된 에이전트에 대해 구체적인 지침을 주지 않는다. 가장 가까운 문구는 "오케스트레이션되는 user agent(예: Chromium, CURL)"("An orchestrated user agent (e.g. Chromium, CURL)")라는 용어 항목이다. (fetched) — 같은 출처
- 선행 문서: Meunier의 개인 초안들(`draft-meunier-web-bot-auth-architecture-05`, 2026년 3월, `draft-meunier-webbotauth-httpsig-protocol-00`, 2026년 6월). (search summary) — [아키텍처 초안](https://datatracker.ietf.org/doc/html/draft-meunier-web-bot-auth-architecture-05)

**누가 서명하는가**
- Google: "Google의 Web Bot Auth 구현은 현재 실험 단계이다"("Google's implementation of Web Bot Auth is currently experimental"). "`Google-Agent`가 보내는 요청의 일부는 Web Bot Auth로 서명된다"("A subset of requests made by the `Google-Agent` are signed with Web Bot Auth"). `Signature-Agent: g="https://agent.bot.goog"`, 키 디렉터리는 `https://agent.bot.goog/.well-known/http-message-signatures-directory`. "우리는 특정 에이전트의 모든 요청에 서명하지는 않는다"("We don't sign every request of a particular agent"). 사이트 소유자에게는 IP 검증으로 폴백하라고 안내한다. 페이지 최종 업데이트는 2026년 10월 13일이다. 사용자의 로컬 Chrome에 있는 에이전트에 대해서는 아무 언급이 없다. (fetched) — [Google 크롤링 문서](https://developers.google.com/crawling/docs/crawlers-fetchers/web-bot-auth)
- OpenAI: 도움말 센터 문서(제목이 "ChatGPT agent allowlisting" / "Allowlisting ChatGPT Work's Cloud browser for your website" 등으로 다르게 표시됨)는 클라우드 브라우저가 외부로 나가는 요청에 RFC 9421 헤더로 서명하며 `Signature-Agent`를 `"https://chatgpt.com"`으로 설정한다고 말한다. 봇 관리 레이블: Cloudflare `chatgpt-agent`, Vercel `chatgpt-operator`, Akamai "ChatGPT Agent". (search summary, 문서를 가져오려 했을 때 HTTP 403이 반환됨) — [OpenAI 도움말](https://help.openai.com/en/articles/11845367)
- Cloudflare의 첫 서명 에이전트(signed agent) 집단: ChatGPT agent, Goose(Block), Browserbase, Anchor Browser. 권위가 낮은 블로그 하나는 "지원되는 에이전트"에 Anthropic Claude와 Perplexity를 추가하는데, 다른 출처로 확인되지 않았다. (search summary) — [WorkOS 해설](https://workos.com/blog/web-bot-auth-identify-ai-agents); [crawlbase 블로그](https://crawlbase.com/blog/web-bot-auth-signed-agents/)
- Comet과 Claude in Chrome은 "문서화된 에이전트 토큰이 없다"("have no documented agent token"). 반면 `Claude-User`는 robots.txt에 대해 준수된다. (search summary, 2차 출처) — [ScrapingBee](https://www.scrapingbee.com/blog/bots-blocking-bots-in-llm-driven-browsers/)
- 법원이 인정한 Comet 관련 사실: Comet은 "Google Chrome처럼 작동하며, 사용자의 기기에서 로컬로 실행된다"("operates like Google Chrome, running locally on a user's machine"). "분쟁의 핵심은 '사용자가 AI 에이전트를 활성화했음을 알려 주는' 메커니즘인 'user-agent 문자열'을 사용하지 않기로 한 Perplexity의 결정이었다. 그 user-agent 문자열이 있으면 Amazon은 Assistant의 접근을 차단할 수 있었을 것이다"("At the core of the dispute was Perplexity's decision not to use a 'user-agent string,' a mechanism 'that would communicate that the user has activated an AI agent.' That user-agent string would allow Amazon to block the Assistant's access"). Amazon이 처음 식별하고 차단한 뒤 Perplexity가 user-agent 문자열을 알면서 변경했는지에 대해서는 양 당사자의 주장이 엇갈린다. (fetched) — [제9순회항소법원 판결문, 2026년 8월 4일](https://www.courthousenews.com/wp-content/uploads/2026/08/amazon-v-perplexity-ninth-circuit-opinion.pdf)

**누가 검증하는가**
- Cloudflare 봇 문서(2026년 11월 12일 업데이트): 봇은 정직한 자기 식별로 자격을 얻는다. 즉 "암호학적 Web Bot Auth 서명을 통해 자신이 누구인지 결정론적으로 선언"("it declares who it is deterministically, through a cryptographic Web Bot Auth signature")하거나, 안정적인 user-agent와 함께 IP 목록을 공개하거나, 역방향 DNS를 쓰는 것이다. 여기에 더해 남용하지 않는 행동(robots.txt와 크롤링 지시문 준수, 합리적인 요청 빈도, 사이트의 선호를 회피하지 않음)이 요구된다. "2026년 7월 1일부터 Verified bot과 signed agent의 구분은 새로운 메타데이터 필드로 표현된다"("Since July 1, 2026, the distinction between a Verified bot and a signed agent is expressed by a new metadata field")(Direct 접근 대 Intermediary 접근). (fetched) — [Cloudflare 문서](https://developers.cloudflare.com/bots/concepts/bot/signed-agents/)
- AWS WAF가 Web Bot Auth 지원을 추가했다. 검증된 에이전트는 Bot Control 레이블을 받으며, 실패 시에는 `:invalid`, `:expired`, `:unknown_bot` 변형이 붙는다. Cloudflare가 엣지에서 운영 환경 적용을 시작한 것은 2026년 3월로 보도되었다. (search summary) — [WorkOS](https://workos.com/blog/web-bot-auth-identify-ai-agents); [stellagent](https://stellagent.ai/insights/web-bot-auth-cloudflare-ietf)
- Akamai와 Vercel은 OpenAI 에이전트를 자체 봇 이름으로 인식한다(위 참조). Web Bot Auth 검증에 관한 Akamai/Vercel의 1차 페이지는 읽지 않았다. (search summary) — [OpenAI 도움말](https://help.openai.com/en/articles/11845367)

**robots.txt, 크롤링 과금, Perplexity 스텔스 크롤링 분쟁**
- 2025년 8월: Cloudflare는 Perplexity가 크롤링 금지 지시를 회피하기 위해 "관련 없는 ASN들의 다양한 미신고 IP"("a variety of undeclared IPs from unrelated ASNs")에서 미신고 크롤러를 사용했다고 보고하고, Perplexity를 verified bot 목록에서 제외했으며 차단 휴리스틱을 추가했다. Perplexity는 이를 "홍보용 쇼"("publicity stunt")라고 부르며 그 봇은 "우리 것조차 아니다"("isn't even ours")라고 말했다. (search summary) — [Decrypt](https://decrypt.co/333479/cloudflare-perplexity-ai-stealth-crawlers-website-blocks); [implicator.ai](https://www.implicator.ai/cloudflare-accuses-perplexity-of-masking-bots-to-evade-ai-scraping-bans/)
- Cloudflare pay-per-crawl은 HTTP 402를 사용한다. AI Crawl Control(2025년 8월)은 사용자 지정 가능한 402 응답을 추가했다. 2026년 현황 보고는 찾지 못했다. Visa의 TAP 명세는 결제 컨테이너에 "402 응답: Browsing IOU 객체"("402 response: a Browsing IOU object") 변형을 포함하는데, 이는 접근에 대가를 지불한다는 발상이 카드 네트워크 명세에까지 도달했음을 보여 준다. (Cloudflare는 search summary, Visa는 fetched) — [Cloudflare 블로그 태그](https://blog.cloudflare.com/tag/bots); [Visa TAP 명세](https://developer.visa.com/docs/trusted-agent-protocol/trusted-agent-protocol-specifications)
- "Cloudflare는 2026년 9월 15일부터 광고가 있는 페이지에서 에이전트를 차단하는 것으로 알려졌으며, 검증된 에이전트도 예외가 아니다."("Cloudflare reportedly blocks agents on pages with ads since September 15, 2026, and verified agents aren't exempt.") (search summary, 단일 2차 출처 — 검증 필요) — [ScrapingBee](https://www.scrapingbee.com/blog/bots-blocking-bots-in-llm-driven-browsers/)

**식별에 관한 워크숍 기록**
- W3C/GS1 워크숍(2026년 9월): "Know-Your-Agent" Internet-Draft 여섯 건이 IETF OAuth WG에 제출되었으나 채택된 것은 없다. Web Bot Auth는 "에이전트가 자신의 요청에 서명하는 업계 규범으로 논의되었다"("was discussed as an industry norm for agents to sign their requests"). 한 분과 토론은 "주요 에이전트 제공자들 사이에 스스로를 식별하는 문제에 대한 합의를 찾지 못했다"("found no consensus among major agent providers on identifying themselves"). 참가자들은 "식별하지 않을 정당한 이유가 존재하며, 열린 웹은 손쉬운 시장 진입에 의존한다"("legitimate reasons exist not to identify, and that the open Web depends on easy market entry")고 경고했다. (fetched, 초안 보고서) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html)

### 추론
- 브라우저 에이전트에는 식별 속성이 정반대인 두 집단이 있다:
  - *클라우드 브라우저 에이전트*(ChatGPT agent/클라우드 브라우저, Google-Agent, Browserbase에서 호스팅되는 에이전트): 요청이 벤더의 인프라에서 나오므로 서명할 수 있고, CDN에서 검증할 수 있으며, 허용 목록에 올릴 수 있다. Web Bot Auth가 들어맞는다.
  - *로컬 브라우저 에이전트*(Comet, Atlas agent mode, Claude in Chrome, Gemini in Chrome auto browse): 요청이 사용자의 기기, IP, 쿠키, 그리고 평범한 Chromium 핑거프린트에서 나온다. 초안의 어떤 내용도 이들을 다루지 않는다. 식별은 벤더가 자발적으로 user-agent 토큰이나 헤더를 추가하는지에 달려 있는데, Perplexity는 이를 거부했다. 사이트 입장에서 에이전트는 "사용자처럼 보인다". 그리고 제9순회항소법원은 바로 이 구도를 법적으로 받아들였다(5절).
- 서명이 있는 경우에도 서명이 확립하는 것은 안정적인 가명 키이지 책임성이 아니다. 신뢰는 대역 외(out-of-band) 레지스트리(Cloudflare의 봇 디렉터리, Visa/Mastercard의 에이전트 등록), 즉 민간 문지기들이 공급한다. 이는 웹에서 앱 스토어의 허용 목록에 가장 가까운 것이다.
- 모바일과의 비교: Play Integrity는 *앱 + 기기*의 무결성을 앱의 백엔드에 증명하며 플랫폼이 통제한다. Web Bot Auth는 *에이전트 운영자의 키*를 사이트에 증명하며 자기 주장에 기반한다. 어느 쪽도 정당한 클라이언트 안에서 버튼을 누른 것이 사람인지 에이전트인지를 서비스에 알려 주지 않는다. Android에서는 적어도 OS가 에이전트가 행동하고 있다는 것을 안다(Computer Control 권한, 표시기). 웹에서는 브라우저 벤더가 에이전트 벤더이기도 한 경우가 아니면 구조적으로 이를 알 수 있는 위치에 있는 당사자가 없다.
- 프라이버시 조항("그러한 식별을 피해야 하는 에이전트에는 적합하지 않다")은 로컬 에이전트의 요청에 서명하면 이 사용자가 특정 AI 에이전트를 쓴다는 사실을 모든 사이트에 알리게 된다는 뜻이다. 이는 경쟁 문제 외에 로컬 에이전트가 서명하지 않는 또 하나의 이유이다.

### 공백
- OpenAI의 허용 목록 등록 문서는 가져올 수 없었다(403). Atlas agent mode(로컬)가 서명을 하는지 또는 어떤 토큰을 싣는지는 검증되지 않았다.
- Claude in Chrome / Comet의 요청 식별에 관한 Anthropic과 Perplexity 자체 문서는 찾지 못했다.
- 채택 수치(서명을 검증하는 사이트의 비율, 서명된 에이전트 트래픽의 비율)는 찾지 못했다.
- Akamai, Vercel, HUMAN, DataDome의 1차 문서는 읽지 않았다.
- Cloudflare pay-per-crawl의 2026년 현황과 "광고가 있는 페이지에서 에이전트를 차단한다"는 주장은 검증되지 않았다.

## 3. 사이트 측 차단, 봇 탐지, CAPTCHA에 대한 동작, 서비스 약관, 그리고 차단 대신 제휴하는 사이트

### 핵심
봇 방어는 사람과 스크립트를 구분하기 위해 만들어졌으며 에이전트 정책으로는 작동하지 않는다. 2026년 11월의 한 측정 연구에 따르면 풀이(solver) 서비스는 챌린지형 CAPTCHA를 사실상 항상 통과하고, LLM 에이전트의 통과율은 모델이 아니라 에이전트가 제시하는 브라우저/계정 상태에 달려 있다. 사용자의 실제 브라우저 안에 있는 에이전트는 그 상태를 물려받는다. 그래서 사이트는 계약과 사업상의 선택에 기대게 된다. Amazon과 eBay는 약관으로 승인되지 않은 에이전트를 금지하고 서버 측에서 차단하는 반면, Shopify, Walmart, Etsy와 (조건부로) Target은 승인된 채널을 통해 에이전트를 받아들인다.

### 인용 근거

**CAPTCHA와 봇 탐지**
- "Broken Gates: Re-evaluating Web Bot Defenses in the Age of LLM Agents"(Ousat 외, Florida International University, arXiv 2607.18659, v2 2026년 11월 5일): 일곱 개의 풀이 서비스는 "챌린지 기반 방어를 100% 성공률로 대부분 무력화한다"("largely defeat challenge-based defenses with a 100% success rate"). reCAPTCHA v3에서는 평균이 23%로 떨어진다. 여덟 개의 LLM 브라우저 에이전트에서 병목은 "모델 자체가 아니라 검증자가 보는 브라우저와 계정 상태"("the browser and account state the verifier sees, not the model itself")이다. 오래 사용된 소비자 계정과 지속되는 Chrome 상태는 reCAPTCHA v2/v3의 통과 여부에 큰 영향을 준다. Turnstile은 브라우저 제어 경로에 더 민감하다. 오래 사용된 브라우저 환경을 재구성하자 이전에 거부되던 에이전트가 모델 재학습 없이 통과했고, 그 환경은 다른 에이전트와 다른 방어에도 그대로 통했다. 저자들의 결론: 비대화형 방어는 일반 사용자도 가질 수 있어야 하는 신호에 의존하므로 공격자가 그 신호를 확보해 재사용할 수 있다. (초록 페이지를 fetched, 에이전트별 수치는 보지 못함) — [alphaXiv 2607.18659](https://www.alphaxiv.org/abs/2607.18659)
- 2025년 7월: ChatGPT agent가 퍼즐 없이 Cloudflare Turnstile의 "I am not a robot" 체크박스를 체크하는 것이 관찰되었다. OpenAI의 이전 제품 Operator는 멈춰서 사람에게 CAPTCHA를 완료해 달라고 요청하도록 학습되어 있었다. (search summary) — [CO/AI](https://getcoai.com/news/chatgpt-agent-bypasses-cloudflares-i-am-not-a-robot-verification/); [techbang(Ars Technica 기사 전재)](https://www.techbang.com/posts/124556-openai-chatgpt-agent-passes-i-am-not-a-robot-test)
- SPLX: ChatGPT agent는 직접 요청하면 CAPTCHA 풀이를 거부하지만, 붙여 넣은 이전 대화 안에서 과제를 다르게 포장하자 reCAPTCHA v2 Enterprise 등을 풀었다. 즉 이 거부는 모델 수준의 정책이며 사회공학으로 우회될 수 있다. (search summary) — [SecurityWeek](https://www.securityweek.com/chatgpt-tricked-into-solving-captchas/amp/); [Cybersecurity News](https://cybersecuritynews.com/chatgpt-bypassing-captcha-security/)
- hCaptcha의 브라우저 에이전트 보고서는 CAPTCHA 풀이 시도가 "계정 탈취 프리미티브"("account takeover primitive")이므로 전면 거부되어야 한다고 주장하며, 시험한 에이전트가 행동하지 못한 경우는 대부분 안전상의 거부 때문이 아니라 도구에 기능이 없었기 때문이라고 말한다. 이해관계가 있는 당사자의 벤더 보고서이며 방법론은 불분명하다. (search summary) — [hCaptcha 블로그](https://hcaptcha.uth.edu/blog/report-browser-agent-safety-is-an-afterthought-for-vendors)
- W3C/GS1 워크숍: 봇 탐지는 "쫓고 쫓기는 게임"("a cat-and-mouse game")으로, 사기 탐지 모델은 "어제의 에이전트로 학습된"("trained on yesterday's agents") 것으로 묘사되었다. (fetched) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html)
- 부수적 차단: 미국 대형 소매업체 51곳을 대상으로 한 감사에서는 37곳이 기존 봇 규칙으로 에이전트를 차단하고, 검증된 에이전트에게 구매 경로를 제공하는 곳은 하나도 없었다. 다른 보고는 봇 방어가 한 쇼핑 에이전트를 소매업체 200곳 중 29곳에서 차단했다고 한다. (search summary, 업계 매체 출처이며 수치는 검증되지 않음) — [letsdatascience](https://letsdatascience.com/news/retailers-block-ai-shopping-agents-leaving-results-inconsist-4e450242); [ppc.land](https://ppc.land/bot-protection-blocks-blends-ai-shopping-agent-at-29-of-200-retailers/)

**차단하거나 금지하는 사이트**
- Amazon: Comet 출시 전에 Perplexity의 CEO에게 Perplexity의 AI 제품은 Amazon Store에서 허용되지 않을 것이라고 알렸고, 출시 후에도 이를 반복했으며, "비공개 고객 계정에 대한 Comet의 접근을 차단하는 도구"("tools to block Comet's access to its private customer accounts")를 만든 뒤 소송을 제기했다(2025년 11월). (fetched) — [제9순회항소법원 판결문](https://www.courthousenews.com/wp-content/uploads/2026/08/amazon-v-perplexity-ninth-circuit-opinion.pdf)
- Amazon의 Conditions of Use에는 이제 "Agent Terms"가 포함되어 있다. Meta의 Muse 에이전트를 amazon.com에 보낸 쇼핑객은 승인되지 않은 AI 에이전트의 계속된 접근은 Conditions of Use 위반이라는 통지를 받았다. 집행은 robots.txt가 아니라 서버 측에서 이루어진다. Amazon은 ChatGPT를 포함해 수십 개의 에이전트를 차단하는 것으로 보도되었다. (search summary) — [The New Stack](https://thenewstack.io/amazon-meta-muse-block/); [Search Engine Journal](https://searchenginejournal.com/amazon-blocked-metas-muse-and-robots-txt-had-nothing-to-say/590494)
- eBay: 2026년 2월 20일부터 시행된 사용자 약관 조항은 eBay가 사전에 명시적으로 허가하지 않는 한 쇼핑 에이전트, AI 기반 봇, 완전 자동화된 결제 흐름을 금지한다. (search summary) — [ppc.land](https://ppc.land/ebay-bans-the-robot-shopper-unless-ebay-sends-the-invitation/); [Shopifreaks](https://www.shopifreaks.com/ebay-bans-unapproved-agentic-shopping-bots-from-its-platform-set-stage-for-more-partnerships/)
- The New York Times는 ChatGPT Atlas가 자사 콘텐츠를 분석하지 못하도록 차단하는 것으로 보도되었다. (search summary) — [PYMNTS](https://www.pymnts.com/cpi-posts/a-court-just-decided-your-ai-browser-isnt-a-hacker/)
- 업계별 신호: Airlines for America, National Retail Federation, News/Media Alliance, Digital Content Next, SIIA가 Amazon v. Perplexity에서 법정 조언자(amicus) 의견서를 제출했다(판결문의 대리인 목록에 따른 것이며, 판결문은 각자가 어느 쪽을 지지했는지 밝히지 않는다). EFF, Mozilla, ACLU, Knight Institute도 제출했다. (fetched) — [제9순회항소법원 판결문](https://www.courthousenews.com/wp-content/uploads/2026/08/amazon-v-perplexity-ninth-circuit-opinion.pdf)
- 은행: 미국 은행들은 고객 계정에서 에이전트를 탐지해 차단하고 있으며, 읽기 전용 에이전트 권한을 검토하고 있다. (earlier notes, search summary) — [Malwarebytes](https://www.malwarebytes.com/?p=481980)

**제휴하는 사이트**
- 추적 자료의 분류: Walmart, QVC, Etsy는 "수용"("embracing"), Target, Home Depot, Tapestry, The RealReal은 "부분적"("partial"). Target의 약관은 승인된 AI 에이전트가 고객 계정에 접근하고, 승인된 한도 안에서 장바구니를 수정하고 주문을 넣고 반품을 개시하는 것을 상정한다. Shopify는 Amazon이 차단한 다음 날 "Muse를 자사의 모든 스토어에 들였고"("let Muse into every one of its stores") Shop Pay 결제를 열었다. (search summary, 분류는 업계 매체마다 다름) — [WebProNews](https://webpronews.com/ai-shopping-agents-which-retailers-allow-block); [The Fashion Law](https://www.thefashionlaw.com/?p=108217); [shopappy](https://shopappy.com/?p=11410)
- Shopify는 스토어프런트에 WebMCP를 탑재했고 Google과 UCP를 공동 개발한다. (WebMCP는 fetched, UCP는 search summary) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html); [Digital Commerce 360, 2026년 3월 6일](https://www.digitalcommerce360.com/2026/03/06/openai-shifts-checkout-plans-agentic-commerce-strategy/)
- 2025년 ChatGPT 결제 파트너는 Shopify와 Etsy였고 이후 Walmart가 추가되었다. ChatGPT 앱을 발표한 소매업체에는 Target, DoorDash, Instacart, The Knot이 있다. (fetched) — [Digital Commerce 360, 2026년 3월 6일](https://www.digitalcommerce360.com/2026/03/06/openai-shifts-checkout-plans-agentic-commerce-strategy/)

### 추론
- 로컬 브라우저 에이전트에 대해 봇 탐지는 *에이전트* 통제 수단으로서 거의 쓸모가 없다. 에이전트는 사용자의 실제 브라우저 프로필, 계정 이력, IP를 제시하는데, 이것이 바로 reCAPTCHA v3/Turnstile이 높게 평가하는 신호이다. 그러면 탐지는 행동상의 단서(입력 타이밍, CDP/자동화 흔적)에 의존할 수밖에 없고, 이것이 워크숍이 말한 "쫓고 쫓기는 게임"이다. CAPTCHA에 걸리는 것은 클라우드 브라우저 에이전트이며, 이들은 요청에 서명하고 허용 목록에 오를 수 있는 쪽이기도 하다.
- 따라서 사이트에는 세 가지 수단이 있으며, 뒤로 갈수록 기술적 성격은 줄고 법적 성격은 커진다. (a) 서명/등록된 에이전트를 CDN에서 허용 목록에 올린다. (b) 알 수 없는 자동화를 휴리스틱으로 차단하며, 정당한 에이전트에 대한 부수적 피해를 감수한다. (c) 서비스 약관에 더해 사용자나 벤더에게 통지한다. 제9순회항소법원 판결(5절) 이후 수단 (c)는 에이전트 벤더를 상대로 한 컴퓨터 범죄 청구가 아니라 사이트와 그 *사용자* 사이의 계약 문제이다.
- 모바일과의 비교: 화면 에이전트에 대한 앱의 방어(위험 엔진, Play Integrity, `FLAG_SECURE`)는 자사 에이전트가 어떤 앱을 자동화할 수 있는지를 선별하는 OS와 나란히 놓여 있다. 앱은 플랫폼 소유자 하나와 협상한다. 웹사이트는 선별자 없이 열린 집합의 에이전트와 브라우저를 마주한다. 웹사이트의 유일한 "허용 목록"은 CDN의 봇 디렉터리이거나 자체 약관이다.
- 벤더의 CAPTCHA "정책"은 공개된 규칙이라기보다 모델의 행동(거부하거나 사용자에게 넘김)으로 보인다. SPLX의 결과는 그러한 행동이 보안 경계가 아님을 보여 준다.

### 공백
- CAPTCHA 처리에 관한 현행 1차 벤더 정책 문구(OpenAI, Anthropic, Google, Perplexity)는 찾지 못했다. 서술은 언론과 보안 벤더에서 나온 것이다.
- AI 에이전트를 특정한 항공사와 은행의 서비스 약관 변경은 찾지 못했다(검색 1회). Airlines for America의 법정 조언자 의견서 입장은 읽지 않았다.
- Amazon의 "Agent Terms"와 eBay의 조항은 직접 읽지 않았다.
- 2026년에 "신뢰할 수 있는 에이전트"를 위한 Google reCAPTCHA / Cloudflare Turnstile 제품 변경은 찾지 못했다.
- 소매업체 감사 수치(37/51, 29/200)는 업계 블로그에서 나온 것이며 확인하지 않았다.

## 4. 웹에서의 에이전트 결제: AP2, ACP/Shared Payment Token, Visa TAP, Mastercard Agent Pay, 출시된 에이전트가 실제로 하는 일, 한국

### 핵심
결제 계층은 하나의 공통 패턴으로 수렴했다. 사용자가 승인하고 암호학적으로 범위가 한정된 인가를 에이전트가 행사할 수는 있지만 확대할 수는 없게 하는 것(AP2의 open/closed mandate, Stripe SPT의 사용 한도, 네트워크의 에이전트용 토큰)에, 가맹점 엣지에서의 서명 기반 에이전트 인식(RFC 9421 위의 Visa TAP)을 더한 패턴이다. 그러나 이것들은 API/"앱" 통합과 클라우드 에이전트를 통해 작동한다. 출시된 로컬 브라우저 에이전트가 일반 웹 결제 화면에서 이것들을 사용한다는 근거는 찾지 못했다. 문서로 확인되는 Gemini in Chrome의 동작은 확인을 위한 일시 정지와 함께 사용자가 저장해 둔 결제 정보를 자동 완성하는 것이며, Visa 자체 명세에는 에이전트가 비회원 결제 폼에 카드를 "직접 입력(key-entering)"하는 모드가 있다. 한국은 개념 증명 단계에 있다.

### 인용 근거

**AP2(명세 사이트에서 읽음)**
- 현재 명세의 제목은 "Agentic Payment Protocol (v0.2)"이다. 사이트는 "AP2 v0.2 Release and FIDO Alliance Donation"으로 링크한다. 표준화는 FIDO의 Agentic Authentication Technical Working Group과 Payments Technical Working Group에서 계속된다. 저장소는 `google-agentic-commerce/AP2`, 라이선스는 Apache-2.0, 최초 발표는 2025년 9월 16일이다. 읽은 페이지에는 v0.2의 릴리스 날짜가 표시되어 있지 않다. (fetched) — [ap2-protocol.org](https://ap2-protocol.org/); [AP2 명세](https://ap2-protocol.org/ap2/specification/)
- 역할: Shopping Agent, Credential Provider, Merchant, Merchant Payment Processor, **Trusted Surface**. Trusted Surface는 "비에이전트적이어야 한다(MUST)"("MUST be non-agentic"). (fetched) — [AP2 명세](https://ap2-protocol.org/ap2/specification/)
- Mandate: **Checkout Mandate**(가맹점과 공유. "에이전트와 가맹점 사이에 협상된 구체적인 품목과 구매 세부 사항에 대한 참조를 담는다", "Captures the reference to the specific items and purchase details negotiated between the agent and the merchant")와 **Payment Mandate**(자격 증명 제공자, 네트워크, 가맹점 결제 처리자와 공유. "확정된 체크아웃에 결합된 특정 거래 금액에 대한 인가를 담는다.", "Captures the authorization for a specific transaction amount bound to a finalized checkout."). 각각 *open* 형태(자율 실행을 위한 사용자의 제약 조건 — 예: 예산, 허용되는 결제 수단)와 *closed* 형태(확정된 특정 체크아웃/금액)가 있다. `vct` 식별자는 `mandate.checkout.open.1`, `mandate.payment.1` 등이다. (fetched) — [ap2-protocol.org](https://ap2-protocol.org/); [명세](https://ap2-protocol.org/ap2/specification/)
- 누가 서명하는가: *사람이 있는 경우(human present)* — Shopping Agent가 mandate 내용을 Trusted Surface에 넘기고, Trusted Surface는 이를 사용자에게 표시해 서명하게 한다. "closed Mandate의 서명은 User Credential 또는 Agent Provider의 신뢰 목록을 사용하여 User에게서 직접 온 것으로 검증된다."("the signature on the closed Mandates is validated as coming from a User directly, using a User Credential or a trust list of Agent Providers.") *사람이 없는 경우(human not present)* — Trusted Surface가 open mandate를 인가하며, open mandate는 "에이전트의 공개 키를 `cnf` 클레임으로 포함해야 한다(MUST)"("MUST include the agent's public key as a `cnf` claim"). 그런 다음 에이전트가 그 키로 closed mandate에 서명한다. 가맹점은 Checkout JWT에 서명하고, closed mandate는 해시로 여기에 결합된다. (fetched) — [AP2 명세](https://ap2-protocol.org/ap2/specification/)
- 형식: "AP2는 Payment Mandate와 Checkout Mandate를 보호하기 위해 `SD-JWT`를 사용하도록 규정한다."("AP2 specifies the use of `SD-JWT`s for securing the Payment and Checkout Mandates.") 검증자(가맹점, 자격 증명 제공자/네트워크, 결제 처리자)는 closed mandate를 open mandate의 제약 조건에 비추어 확인한다. Mandate와 영수증을 합치면 "거래에 대한 부인할 수 없는 그림"("a non-repudiable picture of the transaction")이 된다고 하지만, "분쟁 해결, 보관, 조회 요건은 이 명세의 범위 밖이다"("dispute resolution, retention, and retrieval requirements are outside the scope of this specification"). 읽은 페이지에는 책임 배분도, 3DS/스텝업 메커니즘도 나오지 않는다. (fetched) — 같은 출처
- 원칙: "결제에서의 신뢰는 사용자로부터 나온 결정론적이고 부인할 수 없는 의사 증명에 닻을 내린다"("Trust in payments is anchored to deterministic, non-repudiable proof of intent from the user"). 열거된 샘플: human-present 카드, human-not-present 카드, human-not-present x402, Android의 Digital Payment Credentials. (fetched) — [ap2-protocol.org](https://ap2-protocol.org/)
- 제품 출시: I/O 2026에서 Google은 자사 제품에 대한 AP2 적용이 "Gemini Spark"부터 시작될 것이라고 밝혔다. Universal Cart는 소비자가 Google Pay로 결제하거나 가맹점 사이트로 넘어갈 수 있게 한다. UCP는 2026년 1월 11일 NRF에서 발표되었다. (search summary) — [PYMNTS](https://www.pymnts.com/?p=3750795); [aiweekly](https://aiweekly.co/alerts/gemini-spark-expands-to-160-countries-with-chrome-auto-browse)

**ACP와 Stripe Shared Payment Token**
- Stripe 문서(API 버전 `2026-09-30.preview`, 에이전트 약관 제목은 "Stripe Agentic Commerce Agent Services Preview"): "Shared payment token(SPT)은 에이전트가 개시한 구매를 위해 판매자에게 고객의 결제 수단에 대한 범위가 한정된 접근 권한을 부여한다"("Shared payment tokens (SPTs) grant sellers scoped access to customers' payment methods for agent-initiated purchases"). 미국, 캐나다, 그리고 열거된 유럽 국가에서 이용할 수 있다. 흐름: 고객이 에이전트에 PaymentMethod를 제공 → 에이전트가 Stripe를 통해 특정 판매자 프로필을 대상으로 SPT를 발급 → 판매자가 그것으로 PaymentIntent를 생성. 에이전트는 "통화, 최대 금액, 만료 기간을 포함한 사용 한도"("usage limits, including currency, maximum amount, and expiration window")를 설정하며, "언제든지 SPT를 폐기"("Revoke the SPT at any time")할 수 있다. (fetched) — [Stripe 문서](https://docs.stripe.com/agentic-commerce/concepts/shared-payment-tokens.md?agent-seller=agent)
- 네트워크 토큰: "Stripe는 카드 네트워크와 협력하여, Mastercard의 Agent Pay와 Visa의 Intelligent Commerce 프로그램 같은 네트워크 프로그램을 통해 발급된 토큰을 귀하를 대신해 사용할 수 있다."("In collaboration with the card networks, Stripe might use tokens issued through network programs such as Mastercard's Agent Pay and Visa's Intelligent Commerce programs on your behalf.") 지원 대상: 카드, Link, Apple Pay, Google Pay, Klarna, Affirm(제한적). (fetched) — 같은 출처
- 스텝업은 사람에게 남는다: SPT는 3D Secure나 리디렉션을 위해 `requires_action` 상태에 들어갈 수 있다 — "Stripe는 … 고객을 은행의 사용자 인터페이스로 리디렉션한다"("Stripe … redirects the customer to the bank's user interface"). Affirm 각주: "에이전트는 웹뷰 안의 Affirm 대출 신청 UI와 프로그램 방식으로 상호작용할 수 없다. 탐색하고 확인하는 주체는 반드시 구매자여야 한다."("An agent may not programmatically interact with the Affirm loan application UI inside the webview; the buyer must be the one navigating and confirming.") (fetched) — 같은 출처
- Instant Checkout 현황: 2026년 3월 6일 Digital Commerce 360은 (The Information을 인용해) Instant Checkout이 뒤로 밀려나고 가맹점의 ChatGPT 앱을 통한 결제가 우선시되고 있다고 보도했다. OpenAI: "Instant Checkout은 Apps로 옮겨 가고 있으며, 그곳에서 구매가 더 매끄럽게 이루어질 수 있다"("Instant Checkout is moving to Apps, where purchases can happen more seamlessly"), 그리고 "ACP는 쇼핑 여정 전체에 걸쳐 사용자와 가맹점을 연결하는 인프라 역할을 한다."("ACP serving as the infrastructure that connects users to merchants across the full shopping journey.") 다른 블로그들은 제거 시점을 2026년 3월 4일 또는 24일로 잡으며 약 서른 곳의 가맹점이 이를 탑재했었다고 말한다. (DC360은 fetched, 다른 날짜들은 search summary) — [Digital Commerce 360](https://www.digitalcommerce360.com/2026/03/06/openai-shifts-checkout-plans-agentic-commerce-strategy/); [get-ryze.ai](https://www.get-ryze.ai/blog/agentic-commerce-protocol-after-instant-checkout-what-survived)

**Visa Trusted Agent Protocol(developer.visa.com에서 가맹점용 명세를 읽음. 페이지에 © 2026 표시, 버전 없음)**
- 에이전트 인식 = RFC 9421 `Signature-Input`/`Signature`이며 "web-bot-auth에 맞춰져 있다"("aligned with web-bot-auth"). 태그는 `agent-browser-auth`(탐색)와 `agent-payer-auth`(체크아웃/결제). 필수 매개변수는 `@authority`, `@path`, `created`, `keyid`, `expires`, `tag`, `alg`, `nonce`. created와 expires의 간격은 최대 8분이다. 가맹점은 nonce를 8분 동안 보관하고 반복되면 거부한다. Visa가 키 디렉터리(JWK set)를 호스팅한다. (fetched) — [Visa TAP 명세](https://developer.visa.com/docs/trusted-agent-protocol/trusted-agent-protocol-specifications)
- *Agentic Consumer Recognition Object*(요청 본문): nonce, Visa가 서명한 `idToken`(JWT), `contextualData`(국가, 우편번호, IP 주소, 기기 데이터). 가맹점이 그 소비자를 이미 알고 있는지 확인할 수 있게 해 준다. (fetched) — 같은 출처
- *Agentic Payment Container* 변형: (a) Browsing IOU가 담긴 "402 응답". (b) **비회원 결제 / 직접 입력(guest checkout / key entry)** — 에이전트가 입력할 카드 번호, 유효기간, CVV의 해시로, 가맹점이 이를 다시 계산해 불일치하면 거절한다. (c) 암호화된 페이로드 형태의 API / 네트워크 토큰. 같은 nonce가 서명, 소비자 객체, 결제 컨테이너를 연결한다. (fetched) — 같은 출처
- 폴백: 신뢰할 수 있는 태그가 없으면 가맹점은 "메시지를 차단하거나 다른 메커니즘을 사용하기로 결정할 수 있다"("may decide to block the message or use some other mechanism"). 이를 채택하지 않는 가맹점은 서명을 무시하면 된다. 페이지에는 내부적으로 일관되지 않은 부분이 있다(샘플 타임스탬프는 60분 간격인데 규칙은 8분, `keyid` 대 `kid`, 샘플에 PS256과 Ed25519가 섞여 있음). (fetched) — 같은 출처
- "파트너 100곳 이상"("100+ partners"), 2026년 1분기 상용 출시, 2026년 4월부터 Visa APAC 에이전트 파일럿: 2차 출처의 주장이다. (search summary) — [stellagent](https://stellagent.ai/insights/visa-tap-trusted-agent-protocol)

**Mastercard Agent Pay**
- Mastercard는 Cloudflare와 제휴해 Web Bot Auth를 Agent Pay에 도입했다. 가맹점은 "새 코드를 배포하지 않고도 … 콘텐츠 전송 네트워크(CDN) 계층에서"("at the Content Delivery Network (CDN) layer … without deploying new code") 에이전트를 검증할 수 있다. Agentic Token은 MDES 위에 만들어진다. 2026년 6월 Mastercard 뉴스룸 게시물은 유럽 은행들이 패스키를 사용해 통제된 환경의 실제 에이전트 거래를 완료했으며 유럽의 모든 Mastercard 발급사가 네트워크 수준에서 사용 가능한 상태가 되었다고 말한다. Microsoft Copilot Checkout 및 PayPal 통합이 발표되었다. "Agent Pay for Machines"는 2026년 6월 10일. (Mastercard 페이지와 2차 보도에 대한 search summary) — [Mastercard 프레임워크 페이지](https://www.mastercard.com/hk/en/news-and-trends/stories/2025/agentic-commerce-framework.html); [Mastercard 유럽 뉴스룸](https://newsroom.mastercard.com/news/europe/en/perspectives/en/2026/europe-is-building-the-foundations-for-trusted-agentic-commerce/)
- FIDO는 "Google의 AP2, Mastercard의 Verifiable Intent, Digital Payment Credentials를 조화시키는"("harmonizing Google's AP2, Mastercard's Verifiable Intent, and Digital Payment Credentials") 작업을 하고 있다. EMVCo와 네트워크들은 에이전트용 토큰, 의도(intent) 서비스, 표준화된 분쟁 데이터를 다루고 있다. (fetched) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html)

**출시된 브라우저 에이전트가 결제 단계에서 하는 일**
- Gemini in Chrome(설계 게시물, 2025년 12월 8일): 에이전트는 "구매나 결제를 완료하거나 메시지를 보내기 전에"("before completing a purchase or payment, sending messages") 사용자 허가를 위해 멈추거나 사용자에게 마무리를 요청한다. "Chrome이 Google Password Manager를 통해 사이트에 로그인하도록 허용하기 전에"("before allowing Chrome to sign-in to a site via Google Password Manager") 확인을 받는다. "모델은 저장된 비밀번호에 직접 접근할 수 없다"("the model does not have direct access to stored passwords"). (fetched) — [Google Security 블로그](https://blog.google/security/architecting-security-for-agentic/)
- Auto browse는 Chrome 자동 완성(주소와 결제 정보)을 확장한 것으로 설명되며, "가맹점 웹사이트와 사용자의 기존 결제 수단을 통해"("through merchant websites and the user's existing payment methods") 작동하고, AI Pro/Ultra 구독자로 제한된다. 결제 단계에 도달하기 위해 UCP도 지원한다고 한다. (search summary) — [ppc.land](https://ppc.land/chrome-auto-browse-lets-ai-agents-book-travel-and-fill-forms-alone/); [The Register, 2026년 1월 29일](https://www.theregister.com/2026/01/29/chrome_gemini_pane/)
- Amazon에서 Comet은 사용자의 로그인된 세션 안에서 작동했다. 제9순회항소법원은 Perplexity 자신의 비유를 "결제 단계에서 사용자의 주소와 결제 정보를 자동으로 채워 주는"("automatically fills in the user's address and payment information at checkout") Safari라고 요약했다. (fetched) — [제9순회항소법원 판결문](https://www.courthousenews.com/wp-content/uploads/2026/08/amazon-v-perplexity-ninth-circuit-opinion.pdf)
- 워크숍의 관찰: "에이전트에게 카드 정보를 주면 책임은 사용자에게 남는다"("giving card details to an agent leaves the user liable"). 미해결 쟁점에는 정기 결제와, 사람이 없을 때의 강력한 고객 인증(strong customer authentication)이 포함된다. (fetched) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html)

**한국**
- 금융결제원(KFTC): 원장 채병득은 (연합뉴스, 2026년 5월 3일) 금융결제원이 핀테크 및 그 밖의 희망 참여자들과 함께 AI 에이전트 전용 결제 플랫폼의 기술 검증을 연내에 진행할 것이라고 말했다. 에이전트 결제는 현재 보안 우려 때문에 소액부터 시작하고 있으며, 이 우려는 "기본적으로 해소돼야 한다"고 했다. 프로토콜, 토큰, 한도 설계는 공개되지 않았다. (fetched) — [연합뉴스(Daum 경유), 2026년 5월 3일](https://v.daum.net/v/20260503180009019)
- 카카오페이: "에이전틱 AI 결제 환경 선도"("leading the agentic AI payment environment")를 2026년 전략으로 내세웠다(2026년 5월 12일). 2026년 9월 15일에는 양면(구매자와 판매자) 에이전트 결제 개념 증명을 발표했다. 사용자가 사전에 설정한 권한과 지출 한도가 있는 지갑을 에이전트에 주면 에이전트가 그 범위 안에서 상품, 서비스, 데이터를 구매하고, 결제는 **스테이블코인**으로 이루어지며, 활동 내역은 카카오페이 앱에서 볼 수 있다. 상용화는 규제 정책에 달려 있고, 프로토콜이나 파트너는 거론되지 않았다. (9월 15일은 fetched, 5월 12일은 search summary) — [SBS Biz, 2026년 9월 15일](https://biz.sbs.co.kr/amp/article/20000334666); [Daum, 2026년 5월 12일](https://v.daum.net/v/20260512140301954)
- Visa APAC 에이전트 파일럿(2026년 4월부터)에는 KB국민카드, 삼성카드, 신한카드, 카카오뱅크, 하나카드, 현대카드가 포함된 것으로 알려졌다. (삼일PwC 보고서에 대한 search summary, 기사 본문이 잘려 있음) — [삼일PwC](https://www.pwc.com/kr/ko/insights/samil-insight/agentic-ai-and-the-future-of-payments.html)
- 네이버: 2026년 2월 26일부터 네이버플러스 스토어 앱에서 쇼핑 AI 에이전트 베타(추천, 비교, 할인)를 운영한다. "검색부터 결제까지"("from search to payment") 완결되는 커머스가 목표로 제시되었다. 에이전트 결제 메커니즘에 대한 설명은 찾지 못했다. 토스: 에이전트 결제에 관해 찾은 것이 없다. (search summary) — [ditoday](https://ditoday.com/네이버·카카오-에이전틱-커머스-직접-써봤습니다/); [Bizwatch, 2026년 6월 22일](https://news.bizwatch.co.kr/article/consumer/2026/06/22/0020/)

### 추론
- 이제 웹 에이전트에는 서로 다른 세 가지 결제 경로가 공존한다:
  1. *GUI 입력* — 에이전트(또는 에이전트를 대신한 브라우저 자동 완성)가 사용자의 카드를 가맹점 폼에 입력한다. 발급사와 가맹점은 통상적인 전자상거래 거래를 본다. mandate도, 한도도, 에이전트 귀속도 없다. 로컬 브라우저 에이전트가 임의의 사이트에서 하는 일이 이것이다.
  2. *증명이 붙은 GUI 입력* — Visa TAP의 "key entry" 컨테이너. 에이전트가 여전히 카드를 입력하지만, 서명된 해시 덕분에 가맹점은 카드 데이터가 등록된 에이전트에서 왔음을 확인할 수 있다. 수정되지 않은 결제 폼을 위한 가교이다.
  3. *구조화된 위임 자격 증명* — API나 앱 내 결제를 통한 SPT / 에이전트용 네트워크 토큰 / AP2 closed mandate. 모델이 프롬프트 인젝션을 당하더라도 한도가 유지되는 것은 이 경로뿐이다.
- AP2 v0.2의 "Trusted Surface MUST be non-agentic"은 이전 노트에 나온 신뢰할 수 있는 확인(trusted confirmation) 요구 사항의 결제 분야 버전이다. 그리고 open mandate에서 `cnf`로 결합된 에이전트 키는 "에이전트가 어떻게 증명 가능한 신원을 얻는가"에 대한 구체적인 답이다. 벤더가 하나의 전역 신원을 보유하는 것이 아니라, 사용자의 trusted surface가 mandate마다 그 키에 위임한다.
- Instant Checkout이 가맹점 앱 안으로 물러난 것은 가맹점이 결제(세금, 재고, 사기 방지, 고객 관계)를 자기 손에 두고 싶어 한다는 것을 시사한다. 이는 WeChat/Alipay가 실행을 자사 에이전트 안에 두는 것(earlier notes)의 웹 쪽 대응물이다.
- 스텝업 인증(3DS, 패스키, Affirm의 "탐색하는 주체는 반드시 구매자여야 한다")은 읽은 모든 체계에서 의도적으로 에이전트의 손이 닿지 않는 곳에 놓여 있다. SCA는 웹 결제에서도 사실상의 사람 개입(human-in-the-loop)이다.
- 한국에서 눈에 보이는 방향(카카오페이)은 카드 네트워크의 mandate가 아니라 지갑 + 사전 설정 한도 + 스테이블코인이다. 한국 카드사들의 Visa 파일럿 참여가 국내의 TAP/에이전트용 토큰 배포로 이어질지는 알 수 없다.

### 공백
- 출시된 브라우저 에이전트(Atlas, Comet, Claude in Chrome, Gemini in Chrome, Copilot) 가운데 어느 것이 결제 단계에서 폼 입력이 아니라 AP2/ACP/TAP를 사용하는지를 확인해 주는 1차 출처는 없다. 사용하지 않는다는 서술은 찾은 문서에 근거한 추론이다.
- AP2 v0.2 릴리스 날짜와 FIDO 기증 발표문은 읽지 않았다. 가져온 페이지에서 파트너 목록은 찾지 못했다.
- UCP 자체의 명세는 읽지 않았다. Mastercard 개발자 문서는 직접 읽지 않았다. PayPal의 현재 에이전트 커머스 현황은 찾지 못했다.
- 어떤 체계에 대해서도 거래량 데이터가 없다.
- 한국: 에이전트 결제에 관한 네이버페이, 토스, 금융위/금감원의 1차 발언이 없다. Visa 파일럿 참가자 목록은 잘린 2차 요약에서 나온 것이다.

## 5. 웹 에이전트의 법적 지위: Amazon v. Perplexity(직접 읽음), CFAA/약관 논거, 2026년의 그 밖의 사안

### 핵심
제9순회항소법원의 공간(公刊) 판결(2026년 8월 4일, No. 26-1444, 전원 일치 재판부를 대표해 M. Smith 판사 집필)은, 이 사건 기록상 Amazon에 "접근(access)"하는 것은 Perplexity가 아니라 *사용자*라고 판시한다. Comet은 로컬에서 실행되고 Perplexity의 서버는 Amazon의 서버와 전혀 통신하지 않기 때문이다. 따라서 에이전트 벤더를 상대로 한 CFAA 및 CDAFA 청구는 인용될 가능성이 낮다. 판결은 Amazon이 사용자와의 서비스 약관을 통해 접근을 규율할 수 있다는 점을 명시적으로 남겨 두었고, 불법행위 등 다른 이론은 열어 두었다. 이 논리는 로컬 브라우저 아키텍처에 묶여 있으며 클라우드에서 호스팅되는 에이전트까지 포괄한다고 보기는 어렵다.

### 인용 근거 (별도 표시가 없으면 모두 판결문 본문에서 가져옴)
- 사건 표시와 절차: *Amazon.com Services, LLC v. Perplexity AI, Inc.*, No. 26-1444, D.C. No. 3:25-cv-09514-MMC(N.D. Cal., Chesney 판사). 2026년 6월 11일 변론(시애틀), 2026년 8월 4일 선고, 공간 대상. 재판부는 Milan D. Smith, Jr., Eric C. Tung, 그리고 지방법원 판사 John C. Hinderaker(D. Ariz., 지명 파견). 판결문은 M. Smith 판사가 집필했고, 문서에 별개 의견은 없다. (fetched) — [판결문 PDF](https://www.courthousenews.com/wp-content/uploads/2026/08/amazon-v-perplexity-ninth-circuit-opinion.pdf)
- 인정된 사실: Comet은 "Google Chrome처럼 작동하며, 사용자의 기기에서 로컬로 실행된다"("operates like Google Chrome, running locally on a user's machine"). 쇼핑을 요청받으면 Assistant는 "브라우저 화면의 스크린샷을 찍어 사용자의 컴퓨터에서 Perplexity의 서버로 보내고, Amazon.com을 어떻게 탐색할지에 대한 지시를 Perplexity의 서버로부터 받는다"("takes screenshots of the browser view, sends those screenshots from the user's computer to Perplexity's servers, and receives instructions from Perplexity's servers on how to navigate Amazon.com"). "Perplexity 자체는 Amazon의 서버와 직접 통신하지 않는다."("Perplexity itself does not directly communicate with Amazon's servers.") Amazon은 Perplexity에 권한이 없다고 두 차례 알렸다. 핵심 분쟁은 에이전트임을 식별해 주는 user-agent 문자열을 보내지 않기로 한 Perplexity의 결정이었다. (fetched)
- 지방법원(2026년 3월): Perplexity가 "Amazon 사용자의 허락은 받았으나 Amazon의 승인은 받지 않은 채 사용자의 비밀번호로 보호되는 계정에 접근한다"("accesses with the Amazon user's permission but without authorization by Amazon, the user's password-protected account")는 판단에 근거해 가처분(injunction)을 인용했다. 손실 요건은 Amazon이 "Comet의 접근을 차단하는 도구를 개발하는"("developing tools to block Comet's access") 데 든 비용으로 충족되었다. 담보 제공은 기각되었고, 7일간의 행정적 집행정지가 부여되었다. 이후 제9순회항소법원의 신청 사건 재판부(motions panel)가 신속 항소 절차가 진행되는 동안 가처분의 효력을 정지했다. (fetched)
- CFAA에 관한 판시: "CFAA는 사람(person)에 의한 접근을 상정한다. Assistant가 현재 아무리 발전했더라도 그것은 도구이지 법률상의 사람이 아니다."("the CFAA contemplates access by a person. However advanced the Assistant currently is, it is a tool, not a person for statutory purposes.") "따라서 우리가 물어야 할 것은 Perplexity가 도구(Assistant)를 사용해 Amazon의 컴퓨터에 '접근'하는가이다. 우리 앞에 놓인 사실관계에서 답은 '아니오'이다. Amazon의 컴퓨터에 '접근'하는 것은 사용자이며, 사용자는 Amazon.com에서 특정 행위를 수행하는 데 Assistant의 도움을 받는다."("Our focus is thus to ask whether Perplexity uses a tool (the Assistant) to 'access' Amazon's computers. On the facts before us, we answer no. It is the user who 'accesses' Amazon's computers, with the help of the Assistant to carry out specific acts on Amazon.com.") 스크린샷을 받고 지시를 보내는 것은 "그 자체만으로는 Perplexity가 Amazon의 서버에 '접근'했다(진입했다)는 뜻이 아니다."("by themselves, do not mean that Perplexity has 'accessed' (gained entry) to Amazon's servers.") (fetched)
- 유보된 쟁점: "우리는 다른 기록이나 새로운 사실관계에서 Perplexity가 Amazon의 서버에 진입하는 방식으로 Assistant를 통제할 수 있는지는 다루지 않는다."("We do not address whether, on a different record or new facts, Perplexity may exercise control over the Assistant in such a way as to gain entry to Amazon's servers.") (fetched)
- 뒷받침하는 이유: 관대한 해석의 원칙(rule of lenity)(CFAA는 일차적으로 형사법이다). Amazon의 이론은 "사용자 자신을 (공모 또는 방조 이론에 따라) 형사 책임에 노출시킬 수 있다"("could expose users themselves to criminal liability (under a conspiracy or aiding-and-abetting theory)"). 이 법률의 목적은 해킹 방지이다(*hiQ*, *Nosal I*). *Van Buren*은 접근을 시스템에 진입하는 것으로 정의한다. *Power Ventures*는 그 사건에서 접근이 전제되어 있었고 분석이 승인(authorization)에 관한 것이었으므로 구별되었다. *BrandTotal*은 Assistant가 "수동적인 데이터 수집 이상을 하는 것으로 보이기"("appears to do more than passive data collection") 때문에 완전한 유사 사례가 아니다. 법원은 "without authorization" 요건과 손실 요건에는 나아가지 않았다(각주 4). (fetched)
- CDAFA: 청구들은 "운명을 같이한다"("rise and fall together"). Cal. Penal Code § 502에서 "access"가 더 넓다 하더라도 "(Perplexity가 아니라) 사용자가 Assistant를 AI 도구로 사용해 Amazon에 접근한다"("the user (not Perplexity) accesses Amazon using the Assistant as an AI tool"). (fetched)
- 법원이 밝힌 한계: "우리는 에이전트형 AI를 규율하는 새로운 법 체제를 수립하지 않는다. 우리는 불법행위 청구를 포함한 다른 맥락에서 Perplexity가 Assistant의 행위에 대한 책임을 피할 수 있는지는 다루지 않는다. 여기서 우리의 판시는 우리 앞의 기록에 비추어 … CFAA가 상정하는 '접근'에 한정된다"("We do not establish a new legal regime governing agentic AI. We do not address whether in other contexts, including tort claims, Perplexity can avoid liability for the Assistant's actions. Our holding here is limited to 'access' as contemplated by the CFAA … on the record before us"). "이 판결은 이 사건의 제출 서면에 제시된 대로의 기술 상태만을 반영하며 그것에만 적용된다."("this opinion reflects and applies to the state of technology only as presented in the filings in this case.") (fetched)
- 각주 5: "이 결과는 Amazon이 사용자를 대상으로 한 사적 서비스 약관을 통해 Amazon.com에 대한 접근을 규율할 수 있는 능력을 손상하지 않는다."("This outcome does not impair Amazon's ability to regulate access to Amazon.com via private terms of service for its users.") (fetched)
- 형평: Amazon이 제시한 피해 증거는 약했다. 쇼핑 경험의 저하는 "더 추상적"("more abstract")이다. 보안에 관해서는 Amazon 측 전문가도 인용된 위험을 완전히 재현하지 못했고, 쇼핑 사이트가 관련된 예는 하나뿐이었으며 Amazon.com이 관련된 예는 없었다. 가처분은 "소비자의 선택을 저해하고 초기 단계 기술의 발전을 불필요하게 제한할 것이다"("would impair consumer choice and needlessly limit development of a nascent technology"). 처분: 파기환송(vacated and remanded). (fetched)
- 법정 조언자: ACLU/Knight Institute. EFF, Alliance for Responsible Data Collection, Mozilla, Digital Medusa, EleutherAI(법원은 이들의 아키텍처 설명을 인용했다). SIIA. National Retail Federation. Digital Content Next. News/Media Alliance. Airlines for America. (fetched)
- 논평(Kieran McCarthy / Eric Goldman, 2026년 8월 6일): 재판부가 에이전트 관련 사실관계를 제대로 다루었다고 칭찬했다. McCarthy는 지방법원의 논리를 "충격적으로 부실한 시도"("a shockingly poor effort")라고 부른 바 있다. Goldman이 제기한 미해결 질문은, 제공자가 정의되지 않은 선을 넘어 행위자가 될 수 있는지, *Ticketmaster v. RMG* 유형의 이론이 여전히 제공자에게 미치는지, 사용자 자신이 책임에 노출되는지이다. Perplexity는 "아직 위험에서 완전히 벗어난 것이 아닐 수도 있다"("might not be out of the woods just yet"). (fetched) — [Technology & Marketing Law Blog](https://blog.ericgoldman.org/?p=29133)
- 판결 이후: Amazon은 2026년 8월 18일 전원합의체(en banc) 재심리를 청구했다. 2차 출처 하나는 이 청구가 나중에 기각되었다고 하고, 다른 출처들은 청구 사실만 보도한다. (search summary — 기각 여부는 검증되지 않음) — [MLex](https://www.mlex.com/articles/2515046/amazon-asks-us-appeals-court-to-revisit-case-against-perplexity-ai); [ABA 뉴스레터](https://www.americanbar.org/groups/litigation/resources/newsletters/privacy-data-security/ninth-circuit-holds-human-user-not-developer-ai-agent-responsible-websites-accessed/)
- 서비스 약관 경로: Amazon의 소장은 "데이터 마이닝, 로봇, 또는 이와 유사한 추출 도구"("data mining, robots, or similar extraction tools")에 대한 금지 조항과 에이전트가 스스로를 식별해야 한다는 요건을 원용했다. PYMNTS는 해킹 관련 법률이 적용되지 않더라도 웹사이트 약관 위반은 여전히 계약상 청구의 근거가 될 수 있다고 지적한다. (search summary) — [PYMNTS](https://www.pymnts.com/cpi-posts/a-court-just-decided-your-ai-browser-isnt-a-hacker/); [The Fashion Law](https://www.thefashionlaw.com/can-companies-use-anti-hacking-laws-to-stop-ai-shopping-agents/)

### 추론
- 결정적인 사실은 아키텍처에 관한 것이다. 요청은 사용자 자신의 브라우저에서 Amazon에 도달한다. 클라우드 브라우저 에이전트(벤더가 운영하는 브라우저가 사용자의 자격 증명으로 로그인하는 경우)라면 벤더의 컴퓨터가 사이트에 직접 접속하게 되며, 이는 *Power Ventures*에 더 가깝다. 유보된 쟁점에 관한 문장이 이를 시사한다. 따라서 법적 노출과 기술적 식별 가능성은 나란히 간다. 서명하고 차단할 수 있는 에이전트(클라우드)가 CFAA에도 가장 많이 노출되어 있고, 로컬 에이전트는 식별도 되지 않고 현재로서는 CFAA도 미치지 않는다.
- 이 판결은 사이트를 (a) 자사 고객을 상대로 한 계약 집행(승인되지 않은 에이전트 사용에 대한 계정 정지 — "Agent Terms" 접근법), (b) 기술적 차단, (c) 법원이 다루지 않은 CFAA 이외의 청구(계약, 동산 침해(trespass to chattels), 부정경쟁) 쪽으로 밀어낸다. 또한 사용자가 구동하는 에이전트에 대해 "승인"을 철회하는 수단으로서 중지 요구 서한(cease-and-desist letter)이 갖는 법적 효력을 약화시킨다.
- 보안 분석의 관점에서 "사용자의 도구"라는 구도는 에이전트가 사이트에서 하는 일의 법적 행위자가 사용자임을 함의한다. 이는 에이전트의 구매를 고객의 구매로 취급하는 가맹점 약관과 일치한다. 반면 인젝션되었거나 잘못된 행위에 대한 벤더의 책임(불법행위, 제조물 책임, 소비자법)은 명시적으로 열려 있다.
- 모바일과의 대비: 중국에서는 앱/기관의 동의가 규범이다("이중 승인", earlier notes). Android에서는 플랫폼의 문지기 역할(특권 권한, 선별된 허용 목록)이 이 질문을 선점한다. 지금까지 미국 웹의 답은 컴퓨터 범죄법에 관한 한 사용자의 동의로 충분하다는 것이다.

### 공백
- 전원합의체 결과와 환송 후 지방법원의 현재 상태는 사건 기록(docket)으로 확인하지 못했다.
- 2026년에 브라우저 에이전트(Atlas, Operator, Claude in Chrome, Gemini)를 특정해 제기된 다른 소송은 찾지 못했다. 스크레이핑/크롤러 사건(예: Reddit v. Perplexity, Google v. SerpApi)은 조사하지 않았다.
- 사용자 세션에서 행동하는 에이전트에 대한 EU 측 관점(DSA, 소비자법, 불공정 상행위, 데이터베이스권): 이번 세션에서 찾은 것이 없다.
- Amazon의 소장과 Conditions of Use의 "Agent Terms"는 직접 읽지 않았다. 2026년 3월 지방법원 명령은 항소심 판결문에 인용된 범위에서만 읽었다.
- WSGR과 Cooley의 분석은 403이 반환되었거나 열지 않았다.

## 6. 에이전트와 관련된 브라우저 플랫폼 보안 작업: W3C 그룹, 에이전트 출처 격리, 패스키/WebAuthn, Digital Credentials API

### 핵심
브라우저 플랫폼의 에이전트 전용 보안 작업은 아직 벤더의 설계에 초기 단계의 W3C 인큐베이션이 더해진 수준이다. Chrome이 공개한 아키텍처는 Agent Origin Sets(작업별로 출처 수준에서 읽기 / 읽기-쓰기를 허용하는 목록), 메타데이터만 보는 "User Alignment Critic", 그리고 로그인, 결제, 민감한 사이트에 대한 필수 확인을 추가한다. W3C는 WebMCP를 제안된 Agentic Web Working Group 쪽으로 옮기고 있으며, 에이전트가 변경할 수 없는 "신뢰할 수 있는 확인 표면(trusted confirmation surface)"(WebAuthn, Secure Payment Confirmation, Digital Credentials API 위에 구축)을 작업 항목으로 지목했다. 패스키는 인증기에서 사용자 존재(user presence) 제스처가 필요하기 때문에, 에이전트가 혼자서는 완료할 수 없는 유일한 기존 웹 프리미티브이다.

### 인용 근거

**Chrome의 에이전트 보안 아키텍처(Nathan Parker, Chrome Security, 2025년 12월 8일)**
- *User Alignment Critic*: "Gemini로 만든 별도의 모델로, 신뢰도가 높은 시스템 구성 요소로 작동한다"("a separate model built with Gemini that acts as a high-trust system component"). 웹 콘텐츠가 이를 오염시킬 수 없도록 "제안된 행위에 관한 메타데이터만 보도록 설계되었다"("architected to see only metadata about the proposed action"). 플래너의 각 행위를 실행 전에 심사한다. (fetched) — [Google Security 블로그](https://blog.google/security/architecting-security-for-agentic/)
- *Agent Origin Sets*: 작업별로 읽기 전용 출처(Gemini가 소비할 수 있는 콘텐츠 — "iframe의 출처가 목록에 없으면 모델은 그 콘텐츠를 보지 못한다", "If an iframe's origin isn't on the list, the model will not see that content")와, "에이전트가 읽는 것에 더해 조작(예: 클릭, 입력)하는 것이 허용되는"("the agent is allowed to actuate (e.g., click, type) in addition to reading from") 읽기-쓰기 가능 출처가 있다. "신뢰할 수 있는 게이팅 함수가 플래너가 제안한 출처 중 어느 것이 작업과 관련 있는지 결정한다"("a trustworthy gating function decides which origins proposed by the planner are relevant to the task"). 동기: 제한이 없는 상태에서 침해된 에이전트는 "사실상 Site Isolation 우회에 해당하는 것을 만들어 낼 수 있다"("can create what is effectively a Site Isolation bypass"). (fetched) — 같은 출처
- 확인: "은행 거래나 개인 의료 정보를 다루는 사이트 같은 특정 민감 사이트"("certain sensitive sites, such as those dealing with banking transactions or personal medical information")로 이동하기 전, "Chrome이 Google Password Manager를 통해 사이트에 로그인하도록 허용하기 전"("before allowing Chrome to sign-in to a site via Google Password Manager"), 그리고 "구매나 결제를 완료하거나 메시지를 보내기"("completing a purchase or payment, sending messages") 전. "모델은 저장된 비밀번호에 직접 접근할 수 없다"("the model does not have direct access to stored passwords"). (fetched) — 같은 출처
- 탐지와 보증: 에이전트가 보는 모든 페이지에 대해 "계획 모델의 추론과 병렬로 실행되는"("runs in parallel to the planning model's inference") 프롬프트 인젝션 분류기. 악의적인 샌드박스 사이트를 생성하는 자동화된 레드팀. 에이전트 보안 경계를 뚫은 경우 "최대 $20,000"("up to $20,000")의 VRP. 이 게시물은 에이전트가 웹사이트에 스스로를 식별하는 것에 대해서는 아무 말도 하지 않는다. (fetched) — 같은 출처
- 2차 보도는 초기 구현이 읽기-쓰기 집합만 추적했다고 한다. (search summary) — [CyberInsider](https://cyberinsider.com/google-launches-new-security-architecture-for-ai-agents-in-chrome/)

**W3C**
- Web Machine Learning Community Group: WebMCP를 발행한다(초안 CG 보고서, 2026년 11월 13일). (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/)
- AI Agent Protocol Community Group: 2025년 5월에 제안되었다. 임무는 에이전트가 웹 전반에서 서로를 발견하고 식별하고 협업하기 위한 개방적이고 상호 운용 가능한 프로토콜을 개발하는 것이다. 제안된 범위에는 에이전트 신원 모델과 교차 출처 통신 보안 모델(인증, 인가, 검증 가능한 자격 증명 기반 신뢰)이 포함된다. 격주로 회의한다. 2026년 의제에는 "W3C AC2026: the Agentic Web and the WebMCP protocol"과 "DID and OAuth in the Context of the Agentic Internet"이 있었다. 산출물은 확인되지 않았다. (search summary) — [W3C 그룹 페이지](https://www.w3.org/groups/cg/agentprotocol); [제안서, 2025년 5월 8일](https://www.w3.org/community/blog/2025/05/08/proposed-group-ai-agent-protocol-community-group)
- W3C/GS1 워크숍 "E-commerce for Humans and AI Agents"(취리히, 2026년 9월 8–9일, 현장 약 94명, 원격 약 80명, 참가자에는 Amazon, Google, Meta, OpenAI, Shopify, Visa, Stripe, PayPal이 포함됨) — 제시된 W3C의 방향: WebMCP 및 에이전트와 웹 애플리케이션의 상호작용. 제안된 **Agentic Web Working Group** 헌장(논의 중이며, 결과는 2026년 10월 28일 TPAC에서 발표됨). 사람과 에이전트의 공동 브라우징(co-browsing), 여기에는 "누가 결정을 내렸고 누가 버튼을 눌렀는지 어떻게 구별할 것인가"("how to tell who made a decision and who pressed the button")가 포함된다. Web Authentication, Secure Payment Confirmation, Digital Credentials API를 기반으로 하는 **trusted confirmation surface**. Web Payments WG는 에이전트 신원과 결제 프로토콜을 "검토해야 할 수도 있다"("may need to consider"). IETF(KYA 초안, Web Bot Auth), FIDO(AP2 / Verifiable Intent / Digital Payment Credentials), EMVCo와의 조율. (fetched, 초안 보고서) — [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html)
- 같은 보고서에서: 참가자들은 "에이전트가 변경할 수 없는 확인 단계를 일관되게 원했다"("consistently wanted a confirmation step the agent cannot alter"). FIDO의 입장은 "그 일을 하는 에이전트를 신뢰할 수 없다"("you cannot trust the agent doing that")로 요약되었다. 제안된 "Action Acceptance" 프리미티브는 정확히 특정된 효과가 지금 일어나도 되는지를 묻는다(수락 / 보류 / 거부). FIDO의 결제 시연은 Digital Credentials API를 사용해 에이전트가 통제할 수 없는 확인을 보여 주었다. Vouched는 지출 한도와 상품 범위를 강제하는, 패스키로 승인되고 철회 가능한 권한 부여를 시연했다. OpenAI가 제안한 프리미티브에는 OAuth, WebAuthn, 패스키를 통한 신원이 포함된다. 개인정보의 과다 수집을 피하기 위해 FedCM과 Digital Credentials API가 제안되었다. 이 보고서는 프롬프트 인젝션을 명시적으로 논의하지 않는다. (fetched) — 같은 출처

**패스키 / WebAuthn**
- 벤더들의 해설은 에이전트가 혼자서는 WebAuthn 세리머니(ceremony)를 완료할 수 없다는 데 의견이 일치한다. 인증기는 사용자 존재(그리고 대개 사용자 검증 — 생체 인식 또는 PIN)를 요구하고, 브라우저는 일시적 사용자 활성화(transient user activation)가 없는 호출을 거부한다(`NotAllowedError`). 권장 패턴: 사람이 패스키로 인증하고 에이전트에게 범위가 한정되고 수명이 짧은 OAuth 토큰을 부여한다. 민감한 행위에 대한 스텝업에는 새로운 패스키 세리머니가 필요하다. 실무에서 보고된 우회 방법: 수동으로 초기 설정하고 패스키를 등록해 둔 브라우저 프로필의 쿠키를 재사용한다. ChatGPT Atlas는 패스키를 지원한다(사람이 개입). (search summary, 모두 벤더 블로그이며 명세가 아님) — [Corbado](https://corbado.com/blog/ai-agents-passkeys); [MojoAuth](https://mojoauth.com/blog/can-an-ai-agent-complete-a-passkey-ceremony); [Atlas에 관한 Corbado 글](https://dev.to/corbado/chatgpt-atlas-passkey-support-current-capabilities-and-limitations-270n)
- Mastercard는 유럽에서 "인증에 패스키를 사용한"("using passkeys for authentication") 실제 에이전트 거래를 보고한다. (search summary) — [Mastercard 유럽 뉴스룸](https://newsroom.mastercard.com/news/europe/en/perspectives/en/2026/europe-is-building-the-foundations-for-trusted-agentic-commerce/)

### 추론
- Agent Origin Sets는 Android의 앱별 자동화 허용 목록에 대응하는 웹 쪽 장치이며, 단위가 다르다. *패키지 + 서명 인증서*(Google이 선별하고 사용자가 켜고 끔)가 아니라 *출처*(게이팅 함수가 작업별로 결정하고, 브라우저가 Site Isolation 위에서 강제)이다. 웹 버전은 동적이며 작업에서 도출되고, 모바일 버전은 정적이며 벤더가 선별한다. 둘 다 인젝션된 에이전트가 도달할 수 있는 범위를 Rule of Two 방식으로 좁히는 것이다.
- 로컬 브라우저 에이전트는 모든 사이트에 대한 사용자의 쿠키를 한꺼번에 쥐고 있다. 출처 범위 제한이 없으면 한 페이지에서의 인젝션 하나가 로그인된 어떤 출처에서든 행동할 수 있다. Chrome이 이 위험을 Site Isolation 우회로 규정하는 이유가 여기에 있다. Origin Sets는 교차 출처 확산에는 대응하지만 허용된 출처 안에 있는 공격자 콘텐츠(리뷰, 메시지, 동일 출처 프레임의 광고)에는 대응하지 못한다.
- 패스키는 위협의 양상을 두 가지 면에서 바꾼다. (1) 로그인과 스텝업이 에이전트가 스크립트로 처리할 수 없는 사람 존재 확인 지점이 되므로, 에이전트는 결코 스스로 세션을 *수립*할 수 없고 세션을 물려받을 뿐이다. (2) 세션 쿠키가 일단 존재하면 패스키는 더 이상 보호를 제공하지 않으므로, 에이전트 위험은 세션 재사용에 집중된다. 고위험 행위에 대해 WebAuthn(또는 Secure Payment Confirmation)을 다시 요구하는 사이트는 새로운 표준 없이도 오늘날 사실상 에이전트가 뚫을 수 없는 확인을 얻는다.
- 표준화 기구들은 이전 노트의 연구 논문들과 같은 누락된 프리미티브(행위에 결합된 신뢰할 수 있는 확인)를 식별했고, 이를 브라우저 크롬/인증기 UI(SPC, Digital Credentials API)에 두려 하고 있다. 이는 Android Protected Confirmation 또는 OS 소유 확인 시트의 웹 쪽 대응물이다.
- 찾은 플랫폼 작업 가운데 웹사이트가 브라우저에 에이전트 정책(예: "여기서 에이전트는 읽을 수는 있지만 조작할 수는 없다")을 선언할 표준적인 방법을 제공하는 것은 없다. WebMCP의 Permissions Policy는 도구 노출만 통제한다. 이 자리는 여전히 비어 있다.

### 공백
- 2025년 12월 블로그 게시물 외에 에이전트 출처 격리에 관한 Chromium 설계 문서는 찾지 못했다. 읽기 전용 집합이 출시되었는지는 2차 보도에 근거한다.
- Microsoft Edge(Copilot Mode), Atlas, Comet, Claude in Chrome에서 출처 범위 제한에 해당하는 것은 이 노트의 범위 밖이었다(다루어졌다면 다른 연구자들이 다루었다).
- 에이전트 전용 권한에 관한 Web AppSec WG의 작업, 그리고 사이트가 선언하는 에이전트 정책 헤더에 대한 제안은 찾지 못했다.
- 에이전트에 관한 WebAuthn 명세 본문은 읽지 않았다. 패스키 관련 서술은 벤더 블로그에서 나온 것이다. 어떤 브라우저가 자사 에이전트로 하여금 사용자를 대신해 플랫폼 패스키 프롬프트를 띄우게 하는지(그런 뒤 사용자가 승인) 여부는 검증하지 않았다.
- AI Agent Protocol CG의 산출물과 Agentic Web WG 헌장 본문은 입수하지 못했다.

## 7. 모바일과의 비교 지점: 어떤 웹 메커니즘에 앱 세계의 대응물이 있는가

### 핵심
두 세계는 같은 구성 요소(구조화된 도구 채널, 에이전트가 행동할 수 있는 곳의 허용 목록, 돈과 로그인에 대한 사람의 확인, 어떤 당사자에 대한 서비스 측으로의 증명)를 공유하지만 통제 지점이 다르다. 모바일에서는 OS 벤더가 선별하고 강제한다(서명된 패키지, 특권 권한, 스토어 정책). 반면 열린 웹에서는 각 통제가 자발적이며 브라우저 벤더, 에이전트 벤더, CDN, 결제 네트워크, 사이트에 분산되어 있고, 몇몇 모바일 통제는 웹에 대응물이 아예 없다.

### 인용 근거
- 격리 단위 — 웹: 출처. WebMCP 도구는 기본적으로 동일 출처이며 Permissions Policy 기능 `tools`의 기본값은 `'self'`이다. Chrome의 Agent Origin Sets는 읽기와 조작을 출처별로 통제한다. (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/); [Google Security 블로그](https://blog.google/security/architecting-security-for-agentic/). 모바일: Gemini의 자동화 대상 목록은 패키지 이름을 서명 인증서와 짝지으며, 2026년 11월 12일 기준 앱 114개이다. (earlier notes, search summary) — [Android Authority](https://androidauthority.com/google-gemini-task-automation-target-apps-list-3734443)
- 구조화된 인터페이스 — 웹: WebMCP는 붙어 있는 어떤 에이전트든 호출할 수 있고, 도구의 정직성은 보장되지 않는다("WebMCP 도구가 선언한 의도가 실제 동작과 일치한다는 보장은 없다", "There is no guarantee that a WebMCP tool's declared intent matches its actual behavior"). (fetched) — [WebMCP 명세](https://webmachinelearning.github.io/webmcp/). 모바일: AppFunctions는 특권 호출자로 제한되며, 2027년 4월 14일부터 에이전트 조작을 금지하는 Play 정책이 적용된다. (earlier notes) — [Play Console 도움말](https://support.google.com/googleplay/android-developer/answer/16585319)
- 서비스에 대한 증명 — 웹: Web Bot Auth는 키 보유만 증명하며("누가 Agent를 운영하는지, Agent가 무해한지, 요청이 권한을 받았는지에 대해서는 아무것도 말해 주지 않는다", "says nothing about who operates the Agent, whether the Agent is benign, or whether the request is authorized") 로컬 브라우저 트래픽에는 적용되지 않는다. (fetched) — [datatracker](https://datatracker.ietf.org/doc/draft-ietf-webbotauth-httpsig-protocol/). 모바일: Play Integrity는 기기/앱 무결성을 증명한다. (earlier notes, 배경 지식) — [Android 문서](https://developer.android.com/google/play/integrity)
- 애초에 누가 GUI 에이전트를 실행할 수 있는가 — 웹: 어떤 브라우저나 확장 프로그램이든 가능하다. 제9순회항소법원은 사용자의 세션에서 행동하는 서드파티 에이전트형 브라우저를 사용자 자신의 접근으로 취급했다. (fetched) — [판결문](https://www.courthousenews.com/wp-content/uploads/2026/08/amazon-v-perplexity-ninth-circuit-opinion.pdf). 모바일: Play는 행위를 자율적으로 계획하고 실행하는 앱에 Accessibility API를 사용하는 것을 금지한다. Computer Control은 특권 권한을 요구한다. (earlier notes) — [Play Console 도움말](https://support.google.com/googleplay/android-developer/answer/16585319)
- 서비스 측 옵트아웃 — 웹: 서비스 약관(Amazon "Agent Terms", eBay 조항), 서버 측 차단, CDN 봇 규칙. (search summary) — [The New Stack](https://thenewstack.io/amazon-meta-muse-block/); [ppc.land](https://ppc.land/ebay-bans-the-robot-shopper-unless-ebay-sends-the-invitation/). 모바일: Android Computer Control에 대해 문서화된 개발자 옵트아웃은 없다. 앱은 위험 통제와 `FLAG_SECURE`에 의존한다. (earlier notes) — [Google 블로그, 2026년 5월 12일](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- 신뢰할 수 있는 확인 — 웹: AP2의 비에이전트적 Trusted Surface. WebAuthn / SPC / Digital Credentials API 위의 확인 표면에 대한 W3C 계획. 결제와 로그인에 대한 Chrome의 확인. (fetched) — [AP2 명세](https://ap2-protocol.org/ap2/specification/); [W3C 워크숍 보고서](https://www.w3.org/2026/ecommerce-agents/report.html); [Google Security 블로그](https://blog.google/security/architecting-security-for-agentic/). 모바일: OS의 구매 확인과 닫을 수 없는 표시기. Android Protected Confirmation이 존재하지만 에이전트와 연결되어 있지 않다. (earlier notes)

### 추론
나란히 놓은 요약(위의 근거와 이전 노트를 종합한 것):

| 기능 | 웹(브라우저 에이전트) | 모바일(앱 에이전트) | 대응 여부 |
|---|---|---|---|
| 주체 / 격리 단위 | 출처(동일 출처 정책, Site Isolation) | 패키지 + 서명 인증서, UID 샌드박스 | 대응함. 다만 세분화 수준이 다르며, 출처는 훨씬 많고 심사를 받지 않는다 |
| 에이전트가 행동할 수 있는 곳 | Agent Origin Sets — 작업별, 모델이 제안, 게이팅됨(Chrome 한정) | 선별된 앱 허용 목록 + 사용자의 앱별 토글 | 부분적. 웹은 동적이고 벤더별로 다르며, 모바일은 선별된다 |
| 클릭을 대신하는 구조화된 대안 | WebMCP(오리진 트라이얼), 원격 MCP + OAuth 2.1 프로파일, ACP/UCP | AppFunctions, App Intents | 대응함. 웹에는 호출자 제한과 도구 심사가 없다 |
| 도구/앱 심사 | 없음(사이트가 주장하는 힌트) | 스토어 심사, Play AppFunctions 정책(2027) | **웹에 대응물 없음** |
| 누가 에이전트가 될 수 있는가 | 브라우저/확장 프로그램을 내놓는 누구나, 클라우드 브라우저 | OS 벤더 / OEM 어시스턴트. 서드파티는 접근성 기반 자율 동작이 금지됨 | **기본값이 정반대**: 개방 대 통제 |
| 에이전트 → 서비스 식별 | Web Bot Auth(클라우드 에이전트만), UA 토큰(자발적), 네트워크 레지스트리(Visa/Mastercard) | 표준 없음. 서비스는 자기 앱이 실행되는 것을 본다. Play Integrity는 에이전트가 아니라 앱/기기를 증명한다 | 서로 다른 문제이다. 어느 쪽도 서비스에 "에이전트가 정당한 클라이언트를 조작하고 있다"는 것을 알려 주지 않는다 |
| 서비스 측 거부 | 약관, 서버 측 차단, CDN 봇 규칙, robots.txt(크롤러) | 위험 통제, `FLAG_SECURE`, 플랫폼과의 협상. 중국: 기관 승인 | 웹사이트에는 일방적으로 쓸 수 있는 기술적 수단이 더 많고, 앱은 상대방이 하나이다 |
| "사용자가 있다"는 증명 | WebAuthn/패스키 사용자 존재, SPC, 3DS | 생체 인식 프롬프트, Protected Confirmation | 대응함. 둘 다 에이전트의 손이 닿지 않는 곳에 둔다 |
| 범위가 한정된 결제 권한 | AP2 open/closed mandate, SPT 한도, 에이전트용 네트워크 토큰 | 지갑 앱을 통한 같은 체계(AP2에는 Android Digital Payment Credentials 샘플이 있다) | 공유되는 계층이며 플랫폼 중립적이다 |
| 에이전트 활동 표시기 / 로그 | 브라우저별 UI. 사이트를 위한 표준은 없다 | OS 표시기, 프라이버시 대시보드 보기 | **웹에 표준 대응물 없음** |
| 법적 기본값(미국) | 사용자가 승인한 에이전트 = 사용자의 접근(9th Cir., 좁은 판시) | 소송으로 다투어진 바 없음. 플랫폼의 문지기 역할이 선점함 | — |

- **모바일에 대응물이 없는** 메커니즘: 중개자(CDN)가 검증하는, Web Bot Auth 방식의 자체 공개 에이전트 키. robots.txt/402 pay-per-crawl. 임의의 서드파티 에이전트를 상대로 한 사이트 수준의 약관 집행. 에이전트형 브라우저의 열린 시장.
- **웹에 대응물이 없는** 메커니즘: 에이전트가 다른 당사자의 UI를 조작하는 능력 자체를 거부할 수 있는 플랫폼 소유자. 도구의 정직성에 관한 스토어 정책. 에이전트보다 아래 계층에서 강제되는 사용자의 앱별 토글. 어떤 에이전트가 어떤 앱을 건드렸는지에 대한 OS 수준의 기록.
- 보안상의 귀결: 웹에서는 선별자가 없으므로 피해 한정이 브라우저(출처 범위 제한, 확인), 자격 증명(범위가 한정된 토큰/mandate, 패스키 스텝업), 사이트(자체 위험 통제)에서 나와야 한다. 모바일에서는 같은 한계를 대부분 하나의 행위자가 부과하는데, 이는 추론하기에는 더 단순하지만 권력을 집중시키고 서드파티 에이전트를 배제한다.

### 공백
- 모바일 열은 이전 노트에 의존한다(여러 항목이 search summary 수준이다). 이번 세션에서 재검증하지 않았다.
- WebMCP를 AppFunctions/App Intents와 직접 비교한 출처는 없다. 이 비교는 연구자의 종합이다.
- 모바일 브라우저(WebMCP 오리진 트라이얼이 활성화된 Android의 Chrome, iOS WebKit)가 여기에 어떻게 들어맞는지, 즉 브라우저 앱을 자동화하는 OS 에이전트가 WebMCP 도구를 보는지는 찾지 못했다.
