# 스마트폰의 AI 에이전트 보안과 개인정보 노출 점검

> 기준 시점: 2026년 11월 11일 웹 검색으로 확인.
> Android 17과 One UI 8.5 관련 내용은 Google·Samsung 공식 자료와 2차 보도가 섞여 있다.
> 6장(아이디어 검토)은 조사 결과가 아니라 이 문서 작성자의 분석이며, 구현 가능성에 대한 부분은
> 실제 API 문서로 검증하지 않았다.
>
> 관련 문서: [AI 에이전트 보안 기술의 상용화 현황](ai-agent-security-commercial-landscape.md),
> [AI-SPM: 기술 수준과 상용 제품](ai-spm-technology-and-products.md)

## 1. 요약

- 스마트폰에는 AI-SPM이라는 이름의 솔루션이 없다. 대신 OS 권한 체계, 에이전트 전용 통제(AppFunctions),
  삼성 Knox의 데이터 보호가 그 역할 일부를 나눠 맡고 있다.
- 2026년 들어 폰의 에이전트는 **여러 앱을 넘나들며 작업을 대신 수행**하는 단계에 들어섰다
  (Gemini 앱 자동화, One UI 8.5의 에이전트 기능, Perplexity 기반 Bixby).
- 현재 제공되거나 예고된 가시성 기능은 모두 **"에이전트가 무엇을 했는가"를 보여주는 사후 기록**이다.
  **"에이전트가 무엇에 접근할 수 있는가"를 미리 보여주는 기능은 찾지 못했다.**
- 개인용으로 에이전트가 접근 가능한 개인정보를 스캔·관리해 주는 제품도 찾지 못했다. 기업용(Lookout)과
  연구 프로토타입만 있다. 이 공백이 6장에서 검토하는 아이디어의 자리다.

## 2. 폰에서 동작하는 에이전트의 현황

| 에이전트 | 내용 |
|---|---|
| **Gemini (Android)** | AppFunctions로 앱이 공개한 기능을 호출하거나 UI 자동화로 앱을 직접 조작한다. Google 개발자 블로그는 Gemini 연동이 신뢰된 테스터 대상 비공개 프리뷰라고 밝혔다. |
| **Galaxy AI / Bixby (One UI 8.5)** | 2026년 3월 Galaxy S26에 탑재되어 출시, 5월부터 기존 기기에 배포. Perplexity 기반 Bixby, 여러 앱에 걸친 연쇄 동작, 습관·위치·일정을 분석해 행동을 제안하는 Now Nudge가 포함되었다. |
| **서드파티 에이전트 앱** | Claude, ChatGPT 같은 앱은 일반 앱과 같은 샌드박스와 권한 안에서 동작한다. 다른 앱의 기능을 호출하려면 승인된 에이전트에만 주어지는 권한이 필요하다. |

출처:
[Android 개발자 블로그: Android 17](https://developer.android.com/blog/posts/android-17-is-here),
[InfoQ: AppFunctions](https://www.infoq.com/news/2026/03/android-appfunctions-agents),
[Beebom: One UI 8.5 출시](https://gadgets.beebom.com/news/samsung-releases-stable-one-ui-8-5-update-with-galaxy-s26-series),
[TechCabal: One UI 8.5](https://techcabal.com/?p=187591)

## 3. 이미 들어 있는 보호 장치

### 3.1 Android

Google은 2026년 5월 12일 에이전트 보안 원칙을 "명시적 사용자 통제, 포괄적 데이터 보호, 운영 투명성"으로
정리해 발표했다.
([Google 블로그](https://blog.google/security/android-gemini-intelligence-security-privacy/))

| 구분 | 내용 | 상태 |
|---|---|---|
| 앱 단위 접근 범위 | Gemini는 사용자가 허용한 앱에서만 동작하고 기기의 나머지에는 접근하지 못한다 | 설계 원칙으로 기술 |
| 앱별 자동화 허용 설정 | 설정에서 앱별로 Gemini 자동화를 켤 수 있다 | 예고 ("올해 안") |
| 구매 전 확인 | 대신 구매하기 전에 사용자 확인을 요구하도록 설계 | 설계 원칙으로 기술 |
| 진행 상황 표시 | 자동화 과정을 실시간으로 볼 수 있고, 다른 화면으로 가도 닫을 수 없는 알림 칩이 유지된다 | 현재형으로 기술 |
| Privacy Dashboard 확장 | 최근 24시간 동안 활성화된 AI 어시스턴트와 그것이 사용한 앱을 표시 | 예고 ("곧"), 일정 미공개 |
| 프롬프트 인젝션 방어 | Chrome 자동 브라우징의 보호 기능을 본뜬 안전장치를 Android에 구축 중 | 개발 중 |
| 주변 데이터 격리 | Magic Cue 같은 선제적 기능의 데이터는 Private Compute Core, Private AI Compute, pKVM으로 보호 | 현재형으로 기술 |
| AppFunctions 실행 권한 | 다른 앱의 기능을 찾거나 실행하려면 승인된 에이전트에만 주어지는 `EXECUTE_APP_FUNCTIONS` 권한이 필요 | 2차 보도 |
| 기존 권한 체계 | 앱 샌드박스, Permission Manager, Privacy Dashboard(카메라·마이크·위치 접근 기록), Play의 데이터 보안 섹션 | 제공 중 |

- 이 보호 장치가 **서드파티 에이전트에도 강제되는지는 확인되지 않았다.** Google 블로그는 "플랫폼의 모든
  AI 어시스턴트를 위한 업계 표준으로 만들겠다"는 의지만 밝혔다. Privacy Dashboard가 Gemini 외 에이전트도
  표시할 것이라는 설명은 Android Authority 기자의 추정이다.
- 출처: [Android Authority: Privacy Dashboard](https://www.androidauthority.com/android-privacy-dashboard-ai-assistant-activity-logs-3665767/),
  [Android Police: 에이전트 권한](https://www.androidpolice.com/android-ai-agent-permissions-cage-built-before-agents-arrive/),
  [Android 개인정보 보호 안내](https://www.android.com/intl/en_in/safety/privacy/)

### 3.2 Samsung

- **Personal Data Engine + KEEP + Knox Vault**: 개인화 AI에 쓰이는 데이터를 기기 안에서 처리하고,
  앱별로 분리된 암호화 저장소(KEEP)와 하드웨어 보안 영역(Knox Vault)에 보관한다.
- **처리 위치 선택**: Galaxy AI가 데이터를 기기 내에서만 처리할지 클라우드를 쓸지 사용자가 정할 수 있다.
- **Knox Matrix**: 연결된 Galaxy 기기들의 보안 상태를 확인하고, 문제가 있는 기기는 클라우드 연결 서비스에서 차단한다.
- **Privacy Alert**: One UI 8.5에서 위험한 앱 권한을 알리고, 사진·문서 공유 전에 민감 정보 포함 가능성을
  경고하는 기능이 보도되었다(2차 보도).
- 출처: [Samsung One UI (한국)](https://www.samsung.com/sec/one-ui),
  [KEEP 발표 보도](https://www.channelnews.com.au/samsung-unveils-knox-enhanced-protection-and-quantum-resistant-encryption-for-galaxy-ai/),
  [SecurityWeek](https://www.securityweek.com/samsung-announces-security-improvements-for-galaxy-smartphones/amp/)

### 3.3 기업 관리 기기

- **Knox Service Plugin**: 관리자가 Galaxy AI 전체를 차단하거나 기능별로 끌 수 있고(Knox 3.11 이상),
  "기기 내에서만 데이터 처리 허용" 정책을 적용할 수 있다. Knox Manage에서도 같은 정책을 쓴다.
  ([Samsung Knox 문서: Galaxy AI 데이터 처리](https://docs.samsungknox.com/admin/knox-platform-for-enterprise/knox-service-plugin/configure-advanced-policies/data-processing-for-galaxy-ai/),
  [고급 제한 정책](https://docs.samsungknox.com/admin/knox-platform-for-enterprise/knox-service-plugin/configure-advanced-policies/advanced-restriction-policies/))
- **Android Enterprise**: 개인 프로필의 에이전트는 업무 프로필의 앱을 자동화할 수 없다. 관리자는 완전 관리
  기기에서 AI 자동화를 끌 수 있고, AppFunctions 정책으로 LLM에 기기 상태가 전달되지 않게 할 수 있다.
  ([Android Enterprise: Android 17](https://developer.android.com/work/versions/android-17),
  [Bayton: Android 17 기업용 기능](https://bayton.org/android/android-17-enterprise-features/))
- **Lookout AI Visibility & Governance**(2026년 4월): 직원 폰의 미승인 AI 앱과 앱에 내장된 AI SDK를 찾고
  에이전트 동작을 모니터링한다. 모바일판 AI-SPM에 가장 가깝다.
  ([Lookout](https://security.lookout.com/blog/lookout-ai-visibility-governance))

### 3.4 iOS (비교)

iOS 27은 앱이 App Intents로 기능을 공개하면 시스템 수준 오케스트레이터가 이를 호출하는 구조다.
Apple은 대부분의 처리를 기기 내에서 하고 무거운 작업은 Private Cloud Compute로 보낸다고 설명한다.
개별 에이전트에 대한 사용자 측 권한 통제의 세부 내용은 찾지 못했다.
([Apple WWDC 2026 세션](https://developer-mdn.apple.com/videos/play/wwdc2026/8011/),
[NowSecure: iOS 27 공격 표면](https://nowsecure.com/blog/2026/08/05/what-appsec-teams-need-to-know-about-app-intents-siri-ai-and-the-new-ios-27-attack-surface/))

## 4. 남아 있는 위험

- **권한 안에서의 오용**: 프롬프트 인젝션에 속은 에이전트가 정당하게 받은 권한을 잘못 쓰는 것은 권한
  체계로 막지 못한다.
  ([Approov](https://approov.io/blog/android-17-android-is-becoming-an-agent-are-you-ready), 모바일 보안 벤더)
- **화면을 통한 노출**: 화면 캡처 기반 에이전트는 작업과 무관한 민감 정보까지 클라우드 모델로 보낸다.
  PriMobiBench는 모델이 스크린샷에서 민감 정보를 최대 82.5% 성공률로 추출하고, 누적된 스크린샷으로
  사용자 프로필을 약 70% 수준으로 추론한다고 보고했다.
  ([PriMobiBench](https://www.alphaxiv.org/abs/2609.13873))
- **환경 주입**: 가짜 알림이나 팝업으로 에이전트를 속이는 공격이 에뮬레이터 실험에서 개인정보 유출로 이어졌다.
  ([GhostEI-Bench](https://arxiv.org/pdf/2510.20333v1))
- **높은 시스템 권한**: 화면을 대신 조작하는 에이전트는 높은 시스템 권한으로 동작하며, 단순한 프롬프트
  인젝션에 대한 체계적 방어가 없다는 분석이 있다.
  ([Positive Technologies](https://dbugs.ptsecurity.com/news/ai-powered-smartphone-the-next-challenge-for-cybersecurity-20260320))
- **앱에 숨은 AI**: 승인된 앱이 업데이트로 AI 처리나 외부 모델 호출을 조용히 추가할 수 있다.
  ([NHI Mgmt Group](https://nhimg.org/glossary/mobile-shadow-ai/))
- **기본값 문제**: Gemini가 메시지 앱 등에 기본으로 연결되는 변경이 2025년에 논란이 되었다. 사용자가
  모르는 사이 에이전트의 접근 범위가 넓어질 수 있음을 보여준 사례다.
  ([Malwarebytes](https://www.malwarebytes.com/blog/news/2025/07/no-thanks-google-lets-its-gemini-ai-access-your-apps-including-messages))

## 5. "에이전트가 접근 가능한 개인정보를 스캔·관리하는 기능"은 이미 있는가

찾고자 한 것: 개인 사용자가 자기 폰에서, 에이전트가 접근할 수 있는 개인정보가 무엇인지 스캔해 보고
관리할 수 있게 돕는 기능.

**결론: 이번 조사에서는 그대로 해당하는 제품이나 OS 기능을 찾지 못했다.** 가까운 것들은 아래와 같고,
각각 어긋나는 지점이 있다.

| 가까운 것 | 하는 일 | 어긋나는 지점 |
|---|---|---|
| Android Permission Manager | 앱별·권한별 허용 현황 조회와 회수 | 에이전트를 구분하지 않고, 권한 종류만 보여줄 뿐 그 뒤에 있는 실제 데이터의 민감도는 모른다 |
| Android Privacy Dashboard | 카메라·마이크·위치 접근 기록 | 사후 기록이며 센서 중심 |
| Privacy Dashboard의 AI 확장 (예고) | 최근 24시간 AI 어시스턴트 활동과 사용한 앱 | 사후 기록이며 미출시. "접근 가능한 범위"는 보여주지 않는다 |
| Gemini 연결 앱 설정 | Gemini가 연결된 앱을 켜고 끔 | Gemini 한정 |
| Samsung Privacy Alert | 위험한 권한과 공유 전 민감 정보 경고 | 에이전트 관점의 점검이 아니다 |
| Lookout AI Visibility & Governance | 폰의 AI 앱·SDK 발견, 에이전트 모니터링 | 기업 보안팀용. 개인 데이터가 아니라 회사 정책 관점 |
| AudAgent (연구) | 에이전트의 실제 동작을 개인정보 처리방침이나 사용자 선호와 대조 | 연구·개발자용 도구 |
| PrivacyAssist (연구) | 앱이 받은 권한과 공개한 데이터 수집 내용의 불일치 탐지 | 에이전트 전용이 아닌 연구 프로토타입 |
| AgentPrivArena (연구) | 에이전트 실행 중 개인정보 침해 감시·평가 | 평가용 프레임워크 |
| PrivAuto, CAPED (연구) | 클라우드로 보내기 전 화면 데이터에서 민감 정보 제거 | 노출 범위 점검이 아니라 전송 시점의 차단 |

출처:
[AudAgent](https://arxiv.org/abs/2511.07441),
[RIT 보도](https://www.rit.edu/news/new-privacy-tool-helps-detect-when-ai-agents-become-double-agents),
[PrivacyAssist](https://arxiv.org/pdf/2604.23248),
[AgentPrivArena](https://arxiv.org/abs/2610.06454),
[PrivAuto](https://conf.researchr.org/details/icse-2026/uise-2026-papers/10/PrivAuto-Practical-Insights-of-On-Device-Privacy-Preserving-Agentic-Systems-for-Mobi),
[CAPED](https://arxiv.org/abs/2606.12666),
[에이전트 권한 요청 방식 연구](https://arxiv.org/pdf/2607.13718)

공백을 한 문장으로 정리하면, **현재의 통제는 "권한 단위의 허용 목록"과 "사후 활동 기록"이고,
"데이터 단위의 사전 노출 점검"이 비어 있다.** 기업용 AI-SPM이 클라우드에서 하는 일(자산 발견 → 접근 경로
분석 → 위험 우선순위화)을 개인 기기에서 해 주는 것이 없다.

검색으로 찾지 못했다는 것이 존재하지 않는다는 증명은 아니다. 특히 삼성·Google의 미공개 로드맵,
국내 통신사·보안 업체의 앱, 중국 제조사의 기능은 확인하지 못했다.

## 6. 아이디어 검토: 개인용 에이전트 노출 점검

> 이 장은 조사 결과가 아니라 분석이다.

### 6.1 무엇을 해 주는 기능인가

기업용 AI-SPM의 개인 기기판이다. 가칭 "에이전트 노출 점검".

1. **에이전트 목록화**: 폰에 설치된 에이전트(시스템 어시스턴트, 서드파티 에이전트 앱, AI 기능이 내장된 앱)를 찾는다.
2. **접근 범위 지도**: 각 에이전트가 닿을 수 있는 경로를 모은다. 런타임 권한(연락처, 사진, 문자, 위치 등),
   접근성 서비스와 알림 접근, 화면 캡처, AppFunctions로 호출 가능한 앱 기능, 연결된 계정과 클라우드 커넥터.
3. **데이터 민감도 분류**: 그 경로 뒤에 실제로 어떤 개인정보가 있는지 기기 안에서 분류한다. 예를 들어
   사진 권한 뒤에 신분증·여권 사진이 있는지, 메모에 계좌번호나 비밀번호가 있는지, 문자에 인증번호가 있는지.
4. **위험 평가와 권고**: "이 에이전트는 신분증 사진 12장에 접근할 수 있고, 처리는 클라우드에서 이뤄진다"처럼
   구체적으로 보여주고, 권한 회수·범위 축소·보안 폴더 이동 같은 조치를 제안한다.
5. **변화 감시**: 새 에이전트 설치, 권한 확대, 앱 업데이트로 인한 AI 기능 추가를 감지해 알린다.

### 6.2 왜 가치가 있는가

- **권한 화면은 사용자의 질문에 답하지 않는다.** 사용자가 알고 싶은 것은 "사진 권한이 켜져 있는가"가 아니라
  "내 여권 사진을 이 에이전트가 볼 수 있는가"다. 기존 통제는 앞의 질문에만 답한다.
- **에이전트는 접근 범위를 실제로 쓴다.** 일반 앱은 받은 권한의 일부만 정해진 용도로 쓰지만, 에이전트는
  지시나 주입된 명령에 따라 닿을 수 있는 모든 것을 읽고 외부로 보낼 수 있다. 과도한 권한의 실제 위험이 커진다.
- **사후 기록으로는 늦다.** 예고된 Privacy Dashboard 확장은 이미 일어난 접근을 보여준다. 유출은 되돌릴 수 없다.
- **시기가 맞다.** OS 차원의 에이전트 통제가 2026년에 막 들어오는 중이고, 이 영역은 아직 비어 있다.

### 6.3 어려운 점

- **스캐너 자체가 가장 위험한 앱이 된다.** 민감 데이터를 분류하려면 스캐너가 모든 데이터를 읽어야 한다.
  서드파티 앱이 이 권한을 갖는 것은 그 자체로 위험이므로, 분류는 반드시 기기 안에서 끝나야 하고
  결과가 기기를 떠나지 않아야 한다.
- **서드파티 앱으로는 보이는 범위가 좁다.** 다른 앱의 권한 상태나 접근성 서비스 사용 여부는 조회할 수 있을
  것으로 보이지만, 설치된 앱 전체를 조회하는 권한은 Play 정책상 제한되고, 다른 앱 내부 데이터나
  AppFunctions 등록 내용은 일반 앱이 볼 수 없을 가능성이 높다. (API 문서로 검증하지 않은 추정이다.)
- **클라우드 쪽은 폰에서 보이지 않는다.** 에이전트에 연결한 메일·드라이브·캘린더 커넥터는 서버 간 연결이라
  폰을 스캔해서는 알 수 없다. 계정별 연결 현황 API가 필요하다.
- **화면을 읽는 에이전트는 경계가 없다.** UI 자동화 에이전트는 화면에 뜨는 것은 무엇이든 볼 수 있어,
  "접근 가능한 데이터"를 정적으로 열거하기 어렵다. 이 경우는 앱 단위 허용 범위로 근사할 수밖에 없다.
- **경고 피로**: 민감 정보는 어디에나 있어, 우선순위화 없이 나열하면 사용자가 무시하게 된다.
- **잘못된 안심**: "점검 결과 이상 없음"이 프롬프트 인젝션 같은 런타임 위험까지 없다는 뜻으로 읽힐 수 있다.

### 6.4 누가 만들어야 하는가

위 어려움 대부분이 **OS·제조사가 만들면 해소된다.** 시스템 구성 요소는 모든 앱의 권한과 AppFunctions
등록 내용을 볼 수 있고, 민감 데이터 분류를 격리된 영역에서 수행할 수 있다.

- **삼성이 특히 유리하다.** Personal Data Engine이 이미 기기 안에서 개인 데이터를 분석하고 있고,
  KEEP·Knox Vault라는 격리 저장소와 "보안 및 개인정보 보호" 대시보드, Privacy Alert라는 알림 경로가 있다.
  기존 부품을 "에이전트 관점의 노출 점검"으로 엮는 일에 가깝다.
- **Google**은 Privacy Dashboard를 사후 기록에서 사전 점검으로 확장하는 방향이 자연스럽다.
- **서드파티 보안 앱**은 권한·접근성 기반의 얕은 점검까지만 가능할 것으로 보인다. 그래도 "어떤 에이전트가
  어떤 종류의 데이터에 닿는가"를 한 화면에 모아 주는 것만으로 지금보다는 낫다.

### 6.5 판단

- 조사 범위 안에서는 **비어 있는 영역이고, 필요성도 분명하다.** 기업 쪽에서는 같은 문제가 AI-SPM이라는
  시장을 만들었고, 연구 쪽에서도 에이전트 감사 도구가 2026년에 여럿 나왔다.
- 다만 **독립 앱보다는 OS·제조사 기능으로 적합하다.** 깊은 점검에 필요한 접근 권한과 격리 환경을
  플랫폼만 안전하게 제공할 수 있기 때문이다.
- 차별점은 "권한 목록"이 아니라 **"데이터 단위로 보여주는 것"**에 있다. 권한 조회만으로는 기존
  Permission Manager와 다르지 않다.
- 사전 점검은 런타임 방어를 대체하지 않는다. 구매·전송 전 확인, 프롬프트 인젝션 방어, 활동 기록과
  함께 있어야 한다.

### 6.6 더 확인해야 할 것

- Android 17의 AppFunctions·에이전트 권한 API 문서: 서드파티 앱이 조회할 수 있는 범위
- 삼성 One UI 8.5 이후의 에이전트 권한 설정 화면과 Personal Data Engine의 공개 범위
- Play 정책상 다른 앱의 권한 상태를 조회하는 보안 앱의 허용 조건
- 유사 기능의 특허·선행 제품 조사(국내 통신사, 중국 제조사 포함)
