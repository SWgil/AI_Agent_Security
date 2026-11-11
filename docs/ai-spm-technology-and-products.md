# AI-SPM(AI 보안 형상 관리): 기술 수준과 상용 제품

> 기준 시점: 2026년 11월 11일 웹 검색으로 확인.
> 출처 상당수가 벤더 자료이거나 벤더가 쓴 비교 글이다. 순위와 기능 목록은 방향성 참고용이며,
> 도입 검토 시에는 각 벤더의 현재 공식 문서를 확인해야 한다.
>
> 용어: AI-SPM은 AI Security Posture Management의 약자다. 국내에서는 "AI 보안 태세 관리" 또는
> "AI 보안 상태 관리"로 더 많이 옮긴다. 이 문서는 저장소의 기존 표기에 맞춰 "형상 관리"를 쓴다.
> 상위 문서: [AI 에이전트 보안 기술의 상용화 현황](ai-agent-security-commercial-landscape.md)

## 1. 요약

- AI-SPM은 조직 내 AI 자산을 찾아 목록화하고, 설정·권한·데이터 경로의 위험을 지속적으로 점검하는 기술이다.
  공격을 실시간으로 막지 않고, 공격당하기 쉬운 상태를 미리 찾아 고친다.
- **클라우드에 배포된 모델·AI 서비스에 대한 점검은 성숙 단계**다. 주요 클라우드 보안 플랫폼(CNAPP)이
  모두 AI-SPM 모듈을 기본 기능으로 제공한다.
- **에이전트에 대한 형상 관리는 2025~2026년에 막 제품화된 단계**다. 에이전트·MCP 서버·도구 연결을
  목록화하고 권한 범위를 분석하는 기능이 추가되고 있으며, 일부 벤더는 이를 "Agent-SPM"으로 구분한다.
- 가장 일관된 도입 조언은 **이미 쓰고 있는 클라우드 보안 플랫폼의 AI 모듈부터 시작하고**, SaaS 에이전트나
  런타임 통제가 필요하면 전문 벤더를 추가하라는 것이다.

## 2. 무엇을 하는 기술인가

| 기능 | 내용 |
|---|---|
| **자산 발견과 AI-BOM** | 모델, 엔드포인트, 학습 데이터, 벡터 DB, AI SDK·라이브러리, 에이전트, MCP 서버를 자동으로 찾아 목록화한다. 보안팀이 모르는 "섀도 AI"가 여기서 드러난다. |
| **설정 오류 점검** | 인증 없이 노출된 모델 엔드포인트, 공개된 학습 데이터 저장소, 노출된 API 키 등을 찾는다. |
| **권한·데이터 경로 분석** | 에이전트와 모델이 접근 가능한 신원·워크로드·데이터를 그래프로 연결해 공격 경로와 피해 범위(blast radius)를 보여준다. |
| **배포 전 점검** | 모델 파일, 에이전트 코드, MCP 서버 설정을 배포 전에 스캔한다. |
| **규정 준수 매핑** | 점검 결과를 OWASP Top 10, NIST AI RMF, EU AI Act 같은 기준에 대응시킨다. |

기술 계보상 AI-SPM은 클라우드 형상 관리(CSPM)와 데이터 형상 관리(DSPM)를 AI 자산으로 확장한 것이다.
그래서 클라우드 API로 자산을 열거하는 에이전트리스 스캔 방식을 그대로 쓰는 제품이 많다.

## 3. 현재 기술 수준

### 성숙한 영역

- **클라우드 AI 서비스 발견**: Azure OpenAI, Microsoft Foundry, Azure ML, Amazon Bedrock, Google Vertex AI에
  배포된 AI 워크로드를 자동·지속적으로 발견하는 기능은 표준이 되었다.
  ([Microsoft Learn](https://learn.microsoft.com/tr-tr/azure/defender-for-cloud/ai-security-posture))
- **AI-BOM**: 코드부터 클라우드까지 AI 구성 요소를 목록화하는 기능을 Wiz, Microsoft, Orca 등이 공통으로 제공한다.
- **공격 경로 분석**: 기존 CNAPP의 보안 그래프에 AI 자산을 노드로 추가해, 노출된 엔드포인트에서 민감
  데이터까지 이어지는 경로를 보여준다.

### 2025~2026년에 추가된 영역: 에이전트 형상 관리

- **에이전트·MCP 발견**: Wiz는 AI-BOM, MCP 발견, 코드·클라우드 전반의 엔드포인트 스캔으로 에이전트를
  찾는 기능을 정식 제공한다고 밝혔다.
  ([Wiz 블로그: 에이전트 보안](https://www.wiz.io/ko-kr/blog/wiz-ai-spm-secures-ai-agents),
  [Wizdom 2025 발표](https://wiz.io/blog/wizdom-product-launches-2025))
- **SaaS·로우코드 에이전트 플랫폼 지원**: Palo Alto의 Agent Posture Management는 12개 에이전트 SaaS·클라우드
  플랫폼의 에이전트를 지속 평가한다.
  ([Prisma AIRS 3.0 발표](https://www.paloaltonetworks.com/blog/2026/03/prisma-airs-3-0/))
- **에이전트 산출물 사전 스캔**: 에이전트 코드, MCP 서버 설정, 스킬 정의, 훅 연동을 배포 전에 검사한다.
- **형상 관리와 런타임의 결합**: 주요 벤더가 형상 관리 결과를 런타임 탐지·대응(AIDR)이나 게이트웨이
  정책으로 연결하는 통합 플랫폼으로 가고 있다.

### 시장에서의 위치

- Gartner에는 AI-SPM 단독 Market Guide가 없고, AI TRiSM(신뢰·위험·보안 관리)의 한 요소로 다뤄진다.
  2025년 AI TRiSM Market Guide는 4개 기술 계층 중 상위 2개(AI 거버넌스, 런타임 검사·집행)가 별도 시장으로
  통합되고 있다고 본다.
  ([Gartner 문서 요약](https://gartner.com/en/documents/6185655))
- 2026년 Gartner Hype Cycle(데이터 보안, AI 거버넌스)에 AI-SPM과 AI TRiSM이 포함되었다는 보도가 있으나
  2차 자료여서 직접 확인하지는 못했다.
  ([Software Strategies Blog](https://softwarestrategiesblog.com/2026/08/10/gartners-2026-ai-governance-hype-cycle-and-what-cisos-must-change-to-control-agentic-ai-and-rogue-agents/))

## 4. 상용 제품

### 4.1 클라우드 보안 플랫폼(CNAPP) 내장형

기존 클라우드 보안 제품에 AI-SPM이 모듈로 들어간 형태다. 이미 해당 플랫폼을 쓰는 조직에 맞는다.

| 제품 | 특징 | 참고 |
|---|---|---|
| **Wiz AI-SPM** | 에이전트리스 AI-BOM, MCP 발견, Security Graph로 에이전트를 신원·워크로드·데이터에 연결해 공격 경로 표시 | 2026년 3월 Google이 Wiz 인수를 완료해 Google Cloud 보안 부문에 편입 |
| **Microsoft Defender for Cloud** | Defender CSPM 플랜에 포함. Azure·AWS·GCP의 AI 워크로드 발견, AI-BOM, 에이전트 대상 권고와 공격 경로 분석 | 2026년 7월 1일부터 Foundry 에이전트·서드파티 클라우드 에이전트의 발견과 형상 관리는 Agent 365 라이선스 필요 |
| **Palo Alto Cortex Cloud AI-SPM + Prisma AIRS** | Cortex Cloud가 클라우드 AI 자산 형상 관리를, Prisma AIRS가 모델 스캔·레드팀·런타임·에이전트 형상 관리를 담당 | Prisma Cloud는 Cortex Cloud로 통합. Prisma AIRS 3.0은 2026년 3월 출시 |
| **Orca Security** | 에이전트리스 사이드 스캐닝으로 AI 모델·서비스 발견, 설정 오류와 노출된 키 탐지 | |
| **CrowdStrike Falcon Cloud Security AI-SPM** | 클라우드·데이터·애플리케이션·권한 형상 관리와 한 플랫폼. 클라우드에 배포된 AI 서비스와 LLM의 설정 오류·취약점 점검 | 런타임은 별도 제품 Falcon AIDR/Guardian |
| **SentinelOne** | AI-SPM, CSPM, 런타임 워크로드 보호, 에이전트 보안 모듈에 2026년 2월 DSPM을 추가 | |
| **Tenable** | 멀티클라우드의 AI 리소스와 학습 데이터 발견, 설정 오류·정책 위반 탐지 | 공식 자료로 재확인하지 못함 |

출처:
[Google의 Wiz 인수 완료](https://securitybrief.co.uk/story/google-closes-wiz-deal-unveils-ai-security-tools),
[Microsoft: Agent 365 전환 안내](https://learn.microsoft.com/en-us/defender-xdr/security-for-ai/transition-agent-security-to-agent-365),
[Microsoft AI 보안 변경 내역](https://learn.microsoft.com/security/security-for-ai/whats-new),
[Prisma AIRS 2026년 8월 릴리스 노트](https://docs.paloaltonetworks.com/content/techdocs/en_US/ai-runtime-security/new-features/by-date/prisma-airs/august-2026),
[Help Net Security: Prisma AIRS 3.0](https://helpnetsecurity.com/2026/03/24/palo-alto-networks-prisma-airs-3-0-closes-visibility-gaps-in-autonomous-ai-systems),
[CrowdStrike 블로그](https://crowdstrike.com/en-us/blog/secure-ai-at-machine-speed),
[SentinelOne 확장 보도](https://meatechwatch.com/2026/02/09/sentinel-one-delivers-end-to-end-ai-security-from-data-to-runtime/),
[Orca 구매 가이드](https://orca.security/resources/blog/best-ai-spm-tools/)

### 4.2 에이전트·AI 전문 벤더

클라우드 인프라보다 에이전트 자체의 구성과 권한에 초점을 둔다. SaaS·로우코드 플랫폼에서 만들어진
에이전트처럼 CNAPP이 보지 못하는 영역을 다룬다.

| 제품 | 특징 |
|---|---|
| **Zenity AISPM** | 에이전트·코파일럿의 설정, 권한, 연동을 배포 전에 평가하고 변경 시에도 가드레일을 유지. 런타임 탐지·대응(AIDR)과 함께 제공. SaaS 전용 |
| **Noma Security** | 모델, 에이전트, 파이프라인, MCP 서버를 대상으로 발견, AI 공급망 스캔, 에이전트 위험 통제. 온프레미스와 SaaS 배포를 모두 지원 |

- Zenity는 2026년 4월 Gartner 보고서에서 AI 에이전트 거버넌스 분야의 "Company to Beat"로 선정되었다고
  밝히고 있다(벤더 측 주장).
- 두 벤더 모두 정가를 공개하지 않는다.
- 출처: [Zenity AISPM 제품 페이지](https://zenity.io/platform/ai-security-posture-management),
  [Zenity: AISPM 설명](https://zenity.io/academy/what-is-ai-security-posture-management),
  [Noma 벤더 정리](https://guptadeepak.com/agent-security/vendor/noma-security/),
  [단독형 vs CNAPP 내장형 비교](https://www.decryptiondigest.com/blog/ai-spm-standalone-vs-cnapp-builtin-noma-wiz-defender-for-cloud)

### 4.3 데이터 보안 기반

데이터 형상 관리(DSPM)에서 출발해, AI가 어떤 민감 데이터에 닿는지를 중심으로 본다.

- **Cyera AI Guardian**: 2025년 8월 출시. AI 자산을 세부 수준까지 목록화하는 AI-SPM과 런타임 정책 집행
  (AI Protect)으로 구성된다. 공개 AI 도구, 내장형 코파일럿, 자체 개발 에이전트를 대상으로 한다.
  출시 당시 AI-SPM은 비공개 베타였다.
  ([Cyera AI-SPM](https://www.cyera.com/platform/ai-spm),
  [Help Net Security](https://www.helpnetsecurity.com/2025/08/04/cyera-unveils-ai-guardian/))

### 4.4 국내 제품

이번 검색에서 안랩, SK쉴더스, 삼성SDS가 AI-SPM이라는 이름으로 출시한 제품은 확인하지 못했다.
삼성SDS는 2026년 위협 전망에서 AI 레드팀과 AI 가드레일 도입, CNAPP 기반 상시 점검을 권고했다.
([보안뉴스](https://m.boannews.com/html//detail.html?idx=142338))
국내 시장은 현재 글로벌 제품의 국내 공급이 중심인 것으로 보이나, 이 부분은 추가 조사가 필요하다.

## 5. 한계

- **열거 가능한 자산만 본다**: AI-SPM은 "자산이 내 계정 안에 있고 API로 열거할 수 있다"는 클라우드 형상
  관리의 전제를 물려받았다. 직원이 브라우저에서 쓰는 외부 AI 도구처럼 이 전제 밖에 있는 사용은 보지 못한다.
  Gartner는 2026년까지 승인되지 않은 AI 거래의 80% 이상이 악의적 공격이 아닌 내부 정책 위반에서
  나올 것으로 예측했다.
  ([Kovrr](https://www.kovrr.com/blog-post/ai-security-posture-management-what-it-covers-and-misses))
- **에이전트 이전에 설계되었다**: 초기 AI-SPM은 자율 에이전트가 주류가 되기 전에 만들어져 에이전트의
  전체 피해 범위(MCP 연결, 도구 연동, 에이전트 간 관계, 도달 가능한 데이터)를 다루지 못한다는 지적이 있다.
  (런타임 벤더인 Straiker의 주장)
  ([Straiker](https://www.straiker.ai/blog/agent-spm-ai-spm-ai-usage-controls-what-do-they-actually-secure))
- **발견이지 통제가 아니다**: AI-SPM은 발견과 우선순위화 계층이며 차단 도구가 아니다. 런타임의 에이전트
  악용이나 도구 연쇄 호출 위험은 놓치기 쉬워, 고위험 발견 사항은 티켓이 아니라 런타임 정책으로 연결해야 한다.
  ([NHI Mgmt Group](https://nhimg.org/articles/ai-spm-is-becoming-a-baseline-control-for-agentic-ai-security/))
- **라이선스와 제품 분할**: 에이전트 관련 기능이 별도 라이선스(예: Microsoft Agent 365)나 여러 하위 제품으로
  나뉘어 있어, 전체 범위를 확보하려면 평가와 비용이 늘어난다.

## 6. 도입 시 선택 기준

| 상황 | 우선 검토 대상 |
|---|---|
| 이미 CNAPP을 쓰고 있고 AI 워크로드가 클라우드에 있다 | 기존 플랫폼의 AI-SPM 모듈 (Wiz, Defender for Cloud, Cortex Cloud, Orca, CrowdStrike) |
| Azure·Microsoft 365 중심이고 Copilot Studio·Foundry 에이전트가 많다 | Defender for Cloud + Agent 365 |
| SaaS·로우코드 플랫폼의 에이전트가 주된 위험이다 | Zenity, Noma, Prisma AIRS Agent Posture Management |
| AI가 접근하는 민감 데이터 통제가 핵심이다 | Cyera 등 데이터 보안 기반 제품 |
| 온프레미스 배포가 필요하다 | Noma (온프레미스 지원) |

어느 경우든 AI-SPM만으로는 실행 중 공격을 막지 못하므로, 가드레일과 런타임 탐지·대응을 함께 구성해야 한다.

## 7. 참고한 비교·개요 자료

- [Orca: Best AI-SPM Tools in 2026](https://orca.security/resources/blog/best-ai-spm-tools/) (벤더 작성)
- [Top 12 AI-SPM Tools of 2026](https://guptadeepak.com/tools/top-10-ai-spm-tools-2026/)
- [Guideflow: 9 AI-SPM platforms compared](https://www.guideflow.com/blog/best-ai-security-posture-management-tools)
- [Mindgard: What Is AI-SPM?](https://mindgard.ai/blog/what-is-ai-security-posture-management) (벤더 작성)
- [Vectra: AI-SPM explained](https://vectra.ai/topics/ai-security-posture-management) (벤더 작성)
- [SentinelOne: AI-SPM이란](https://www.sentinelone.com/ko/cybersecurity-101/cybersecurity/what-is-ai-spm/) (한국어, 벤더 작성)
