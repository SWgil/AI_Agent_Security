# 화면 조작(GUI) 에이전트 보안의 표준, 규제, 책임과 앱·서비스 제공자 측 대응 — 2026년 11월 18일 기준 현황

보고서 작성자를 위한 읽기 안내:
- 모든 발견 사항에는 URL과 날짜가 붙어 있다. "[search summary]"는 해당 주장이 링크된 페이지의 검색 결과 요약에서 나왔고 페이지 자체는 열어 보지 않았다는 뜻이다. 신뢰도가 더 낮은 것으로 취급한다.
- "[background, not re-verified]"는 해당 항목이 조사자의 사전 지식(2026년 6월 이전)에서 나왔고 이번 세션에서 다시 확인하지 않았다는 뜻이다. URL은 흔히 인용되는 위치이며, 인용하기 전에 확인해야 한다.
- 2026년 자료의 상당수는 2차 자료(업계 전문지, 로펌, 벤더 블로그)이다. 1차 원문(제9연방항소법원 판결문, 중국 표준, 한국 기관 문서, 벤더 약관)은 대부분 확보하지 못했다. 이 점은 항목마다 표시해 두었다.

## 1. GUI·화면 조작 에이전트에 적용되는 프레임워크와 표준

### 핵심
2026년 11월 기준으로 자율 규범 성격의 에이전트 보안 분류 체계(OWASP ASI Top 10, MITRE ATLAS 에이전트 기법, NIST/CAISI 이니셔티브)는 촘촘하게 쌓여 있지만, 화면 조작에 특화된 것은 하나도 없다. "다른 앱의 GUI를 읽고 클릭하는 행위"를 직접 규율하는 표준은 중국 것뿐이다(금융기관의 권한 부여를 요구하는 금융 분야 단체표준 하나와, 준비 중인 강제성 국가표준 하나). 한국에는 초안과 계획(2026년 말 예정인 개인정보보호위원회의 에이전틱 AI 안내서, 시범 운영 중인 금융보안원의 에이전트 레드팀 기준)만 있고 확정된 규칙은 없다.

### 인용 근거

**OWASP**
- OWASP Top 10 for Agentic Applications(ASI01–ASI10)는 2025년 12월 9일 OWASP GenAI Security Project가 100명 이상의 기여자와 함께 공개했다. ASI01 = Agent Goal Hijack(에이전트가 읽는 콘텐츠를 통한 간접 프롬프트 인젝션), ASI02 = Tool Misuse, ASI10 = Rogue Agents이다. 이 문서는 "최소 행위 권한(least agency)" 원칙(범위가 정해진 작업에 최소한의 자율성만 부여한다)을 도입한다. [search summary] — [Cycode 요약](https://cycode.com/blog/owasp-top-10-agentic-applications/), [TechRepublic](https://www.techrepublic.com/article/news-owasp-ai-agent-risk-list/)
- OWASP Top 10 for LLM Applications의 2026년판이 2026년 8월 3일 공개되었고 에이전틱 목록 및 NIST/ATLAS/CWE와 상호 연결되어 있다고 보도되었으며, OWASP가 "Agent Control Standard"를 기증받았다고도 전해진다. 출처는 CSA Labs의 AI 보조 리서치 노트이며 OWASP 페이지와 대조해 검증하지 못했다. [search summary] — [CSA Labs 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-owasp-genai-top10-2026-agent-control-stand/)
- OWASP "Agentic AI – Threats and Mitigations" 분류 체계(2025년 2월)가 Top 10보다 먼저 나왔다. [background, not re-verified] — [OWASP GenAI](https://genai.owasp.org/resource/agentic-ai-threats-and-mitigations/)

**NIST / CAISI / NCCoE**
- NIST의 Center for AI Standards and Innovation(CAISI)은 AI Agent Standards Initiative를 출범시켰다(2026년 2월 17일 보도). 에이전트 보안, 신원·인가, 상호운용성, 테스트에 초점을 두며, 모든 산출물은 자율 규범이다. [search summary] — [Pillsbury 뉴스레터](https://www.pillsburylaw.com/en/news-and-insights/nist-ai-agent-standards.html), [CSA Labs 노트, 2026년 3월](https://labs.cloudsecurityalliance.org/research/csa-research-note-nist-caisi-ai-agent-standards-compliance-2/)
- AI 에이전트 시스템 보안에 관한 CAISI의 RFI(위협 모델, 감독, 보안 개발, 모니터링·로깅·사고 대응): 의견 제출 기한은 2026년 3월 9일이었고, 분야별 의견 청취 세션(금융, 보건, 교육)은 2026년 4월에 열렸다. — [Pillsbury 뉴스레터](https://www.pillsburylaw.com/en/news-and-insights/nist-ai-agent-standards.html)
- NCCoE 개념 문서 "Accelerating the Adoption of Software and AI Agent Identity and Authorization"이 2026년 2월 5일 발간되었고 의견 제출 기한은 2026년 4월 2일이었다. OAuth 2.0, SPIFFE/SPIRE, MCP를 사용하는 실증 프로젝트를 제안한다. 개념 문서이지 표준이 아니다. [search summary] — [CSA Labs 노트](https://labs.cloudsecurityalliance.org/research/csa-research-note-nist-ai-agent-standards-initiative-2026040/), [Pillsbury](https://www.pillsburylaw.com/en/news-and-insights/nist-ai-agent-standards.html)
- AI 에이전트 하이재킹 평가 강화에 관한 US AISI(현 CAISI)의 기술 블로그(AgentDojo 기반, 2025년 1월). [background, not re-verified] — [NIST](https://www.nist.gov/news-events/news/2025/01/technical-blog-strengthening-ai-agent-hijacking-evaluations)

**MITRE ATLAS**
- ATLAS는 2025년 10월 Zenity Labs와의 협업을 통해, 그리고 2026년 첫 업데이트에서 다시 에이전트 중심 기법을 추가했다. 이름이 확인된 기법으로는 AI Agent Tool Invocation(AML.T0053), 도구 호출을 통한 유출(AML.T0086), 도구 호출을 통한 데이터 파괴(AML.T0101), AI Agent Tool Poisoning(AML.T0110)이 있다. "Publish Poisoned AI Agent Tool"은 v5.4.0(2026년 2월)에서 추가되었다. 기법 ID는 ATLAS 사이트가 아니라 벤더 블로그에서 가져온 것이다. [search summary] — [Zenity](https://zenity.io/blog/current-events/zenitys-contributions-to-mitre-atlas-first-2026-update), [Akto](https://www.akto.io/blog/mitre-atlas-agentic-ai), [Speakeasy](https://www.speakeasy.com/blog/mitre-atlas-explained)
- "computer use"나 GUI 조작을 이름으로 내건 ATLAS 기법은 찾지 못했다. 한 벤더는 브라우저 계층·AI 브라우저 기법이 존재한다고 말한다. 전체 기법 수는 벤더 글마다 다르다(84 / 120 / 170). [search summary] — [Akamai](https://www.akamai.com/blog/ai/mitre-atlas), [Armosec](https://www.armosec.io/blog/mitre-atlas-for-ai-agent-attack-detection/)
- CSA Labs의 초안 노트(2026년 3월 27일)는 ATLAS가 오케스트레이터 하이재킹, 도구 체인 오염, MCP 서버 악용을 여전히 충분히 다루지 못한다고 주장한다. [search summary] — [CSA Labs](https://labs.cloudsecurityalliance.org/research/csa-research-note-atlas-agentic-gap-analysis-20260327/)

**CSA / ETSI / ISO / ITU**
- CSA MAESTRO, 7계층으로 구성된 에이전틱 위협 모델링 프레임워크(2025년 2월). [background, not re-verified] — [CSA 블로그](https://cloudsecurityalliance.org/blog/2025/02/06/agentic-ai-threat-modeling-framework-maestro)
- CSA Labs의 백서 초안 "Agentic Transaction Security Framework v1"(2026년 3월 27일, 상태: 초안)은 에이전트에게 위임된 결제 권한을 다룬다. [search summary] — [CSA Labs](https://labs.cloudsecurityalliance.org/agentic/agentic-transaction-security-framework-v1/)
- ETSI TS 104 223 "Securing AI — Baseline Cyber Security Requirements for AI Models and Systems"(2025년 4월)는 일반적인 AI 기준선이며 에이전트에 특화된 것이 아니다. [background, not re-verified] — [ETSI](https://www.etsi.org/deliver/etsi_ts/104200_104299/104223/)
- ITU-T SG21 총회(제네바, 2026년 7월)는 CAICT가 주도한 에이전트 상호운용성 표준 ITU-T F.748.93의 발간을 승인하고 신규 과제를 개설했다: F.ACAIA-B(에이전트 평가 벤치마크), FSTR.CMCLAIA(에이전트 분류 및 능력 수준), TR.AAI(에이전트 인프라 프레임워크). — [通信世界, Tencent News 경유, 2026년 8월 4일](https://news.qq.com/rain/a/20260804A0DCL400)

**중국**
- 《智能体技术金融应用安全要求》는 금융 분야 에이전트 보안에 관한 중국 최초의 단체표준으로 소개되며, 2026년 9월 11일 "최근 발표되었다"고 보도되었다. 北京国家金融科技认证中心이 주도하고 PSBC, ICBC, UnionPay, Bank of China, BoCom, Huaxia Bank, Huawei, Ant, Volcano Engine(ByteDance), Tencent Cloud가 참여했다. 인용된 핵심 조항은 다음과 같다: 제3자 온디바이스 에이전트는 "未经金融机构授权，不得利用系统权限自动化读取、操作金融应用软件GUI界面". 마이크, 스크린샷, 화면 녹화, 화면 공유로 얻은 데이터는 피호출 측의 보안 정책을 따라야 한다. 다섯 개 영역은 초기화 및 입력, 모델 추론 및 의사결정, 신원 인증 및 조작, 데이터 보안 및 프라이버시, 리스크 통제 및 컴플라이언스이다. 업계에서는 이를 줄여 "双重授权"(이용자 + 기관)이라고 부른다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)
- 2026년 5월: CAC, NDRC, MIIT가 공동으로 《智能体规范应用与创新发展实施意见》를 발표하여 에이전트 권한 관리와 행위 통제의 강화를 요구했다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)
- 2026년 7월: 국가 지도성 기술문서 《人工智能 智能体互联》 GB/Z 185.1–185.7—2026이 발표되었다. 같은 시기에 국가표준위원회는 강제성 국가표준 《智能体应用安全基本要求》의 제정 계획을 하달했다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)
- 2026년 7월 15일: CAC가 온디바이스 생성형 AI 서비스 등록(备案) 목록을 새로 공개했으며, 휴대폰 측 서비스 7개가 포함되었다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)
- China Software Industry Association의 단체표준 《移动互联网服务可访问性安全要求》는 접근성 서비스를 이용해 "화면 읽기 + 모의 클릭"을 하는 에이전트를 대상으로 한다. CAICT의 Taier 단말 연구소는 모바일 에이전트 위험 점검 도구 "M-ARC"를 공개했다. 검색 요약에서만 확인했으며 1차 페이지는 열어 보지 않았다. [search summary] — [M-ARC에 관한 CAICT GMA 페이지](https://gma.caict.ac.cn/en/plat/cttl-t/cttl-terminal-labs-of-caict-release-the-mobile-agent-risk-audit-tool-M-ARC), [CAICT AI 단말 보안 표준화](https://gma.caict.ac.cn/en/plat/cttl-t/caict-promote-the-standardization-of-ai-terminal-security-together-with-the-industry), [ifanr](https://www.ifanr.com/1634513)
- 중국이 AI 단말 등급 분류에 관한 국가표준을 공개했다(2026년 5월 8일 보도). [search summary, title only] — [CGTN](https://news.cgtn.com/news/2026-05-08/China-unveils-new-national-standards-for-AI-terminal-grading-1MZ5v5f8BJS/p.html)

**한국**
- 개인정보보호위원회(PIPC)는 2026년 9월 23일 AI 프라이버시 민관 정책협의회 제2차 전체회의를 에이전틱 AI를 주제로 열었다. 7~8월에 AI 개발사·도입사 15곳을 대상으로 한 조사에서 프롬프트 인젝션과 과도한 권한이 최대 위험으로 꼽혔다. 남은 쟁점은 에이전트가 단독으로 해도 되는 작업과 사람의 승인이 필요한 작업의 구분, 개발자·배포자 등 사이의 책임 배분, 에이전트 로그와 메모리의 보관 및 삭제이다. 개인정보보호위원회는 2026년 말까지 (가칭) "에이전틱 AI 개인정보 처리 안내서"를 마련할 계획이며, 일률적으로 규제를 강화하기보다 위험 기반으로 접근한다. 업계는 기술 중립성을 요청했다. — [MTN, 2026년 9월 23일](https://news.mtn.co.kr/news-detail/2026092316363993251)
- 보도된 개인정보보호위원회의 방향: AI에 부여한 권한이 어떻게 쓰이는지를 기업이 지속적으로 관리하고 입증할 수 있게 한다. 관련 행사에서 Meta와 Toss가 자사의 안전장치를 발표했다. [search summary] — [MTN, 2026년 9월 29일](https://news.mtn.co.kr/news-detail/2026092915195124670), [Byline Network, 2026년 9월](https://byline.network/2026/09/29-564/)
- 개인정보보호위원회는 2026년 2월 2일 제2기 AI 프라이버시 민관 정책협의회를 출범시켰다. 2025년의 "생성형 AI 개발·활용을 위한 개인정보 처리 안내서"가 선행 안내서이다. [search summary] — [ZDNet Korea, 2026년 2월 2일](https://zdnet.co.kr/view/?no=20260202130115), [Byline, 2025년 8월](https://byline.network/2025/08/6-277/)
- 금융보안원(FSI)은 2026년 10월 7일 "AI 모델 위협 점검 기준 및 AI 에이전트 보안 평가기준(안)"을 발표했다. AI 레드티밍 기준에 점검 항목 17개를 추가하여 모델 계층(탈옥, 시스템 프롬프트 유출)에서 실행 계층으로 범위를 넓혔다. 명시된 영역은 실행 권한 및 격리, 도구 실행 및 승인 통제, 작업 범위 및 결과 검증, 자율 실행의 제한 및 중단 능력이다. 시범 검증은 2026년 말까지 진행되며 최종 기준은 "2026년 AI 레드팀 리포트"에 실릴 예정이다. 금융보안원장 박상원: "AI 에이전트는 판단에 그치지 않고 직접 행동한다는 점에서 기존 AI와는 또 다른 보안 리스크를 수반한다". 검색 요약은 이를 "6개 분야 17개 항목"이라고 설명했으나, 열어 본 기사에는 네 개 영역이 나열되어 있다. — [파이낸셜뉴스, 2026년 10월 7일](https://www.fnnews.com/news/202610071821048238)
- 금융위원회는 2026년 11월 5일 "AI 시대 침해위협에 대응하기 위한 全금융권 보안체계 혁신방안"을 발표했다(11월 30일까지 외부 접점 점검, 2026년 12월 31일까지 내부통제 전수 조사, 2027년 1분기에 "보안 내부통제 가이드라인"). 이것은 AI를 활용한 금융권 대상 공격에 관한 것이지, 소비자용 에이전트가 은행 앱을 조작하는 문제에 관한 것이 아니다. [search summary] — [법률신문](https://www.lawtimes.co.kr/news/articleView.html?idxno=229265)
- AI 기본법과 그 시행령은 2026년 1월 22일 시행되었다. 개정 시행령이 2026년 7월 14일 국무회의를 통과했다(2026년 7월 21일 시행으로 보도되었으나 날짜는 보도마다 다르다). 2026년 11월 16일 자 보도에 따르면 무엇이 고영향 AI에 해당하는지를 두고 현장에 혼란이 있고, AI가 직접 행동할 때 책임을 귀속시키고 피해를 구제하는 절차가 부족하다는 비판이 있다. [search summary] — [법률신문](https://www.lawtimes.co.kr/news/articleView.html?idxno=223727), [디지털데일리, 2026년 7월 20일](https://www.ddaily.co.kr/page/view/2026072017153663628), [ZDNet Korea, 2026년 11월 16일](https://zdnet.co.kr/view/?no=20261116162921)

### 추론
- 국제 프레임워크들은 GUI 에이전트를 "도구를 가진 에이전트"의 한 사례로 취급한다. 화면 특유의 위험(시각적 프롬프트 인젝션, 화면에 함께 보이는 제3자의 데이터, 제3자 앱을 그 동의 없이 조작하는 행위)은 ASI01/ASI02 유형의 범주를 통해 간접적으로만 다루어진다. "조작되는 앱도 동의해야 한다"는 것을 성문 규범으로 만든 관할은 중국 하나뿐이다.
- 한국의 접근은 지금까지 프라이버시 주도(개인정보보호위원회)와 금융 분야 레드티밍(금융보안원)이다. 중국의 "이중 권한 부여" 규칙이나 KISA의 에이전트 전용 가이드에 해당하는 것은 찾지 못했다.

### 공백
- AI 에이전트·온디바이스 화면 에이전트에 특화된 KISA나 과기정통부 문서는 찾지 못했다(검색은 한 번뿐이었고, 없다는 사실이 확인된 것은 아니다).
- AI 기본법 시행령에 에이전트 관련 조항이 있는지는 확인하지 않았다. 살펴본 자료에서는 그런 조항이 보도되지 않았다.
- 중국 표준의 1차 원문(조항 번호, 발표일, 표준 번호)과 《实施意见》의 1차 원문은 확보하지 못했다. 세부 내용은 신문 기사 한 건에 의존한다.
- ISO/IEC의 에이전트 전용 작업(SC 42)과 ETSI의 에이전트 전용 산출물: 이번 세션에서는 찾지 못했다.
- 금융보안원의 17개 항목 전체와 "6개 분야"라는 주장은 검증되지 않았다.
- OWASP 2026 LLM Top 10과 "Agent Control Standard"에 관한 주장은 AI 보조로 작성된 CSA Labs 노트에 의존한다.

## 2. 에이전트가 이용자를 대신해 행동할 때의 규제와 책임

### 핵심
에이전트의 실수나 인젝션으로 인한 행동에 대한 책임은 어디에서도 정리되지 않았다. 유일한 항소심 판결(제9연방항소법원, 2026년 8월, Amazon v. Perplexity)은 컴퓨터 접근 법률의 적용에서 에이전트를 이용자의 도구로 취급했지만 일반 원칙을 세우는 것은 명시적으로 거부했다. 결제 법규(Reg E/Z, PSD2/PSR)는 여전히 사람인 지급인을 전제하며, 벤더 약관은 에이전트가 개시한 거래의 책임을 이용자에게 떠넘긴다.

### 인용 근거

**Amazon v. Perplexity (Comet)**
- Amazon은 Comet의 에이전트가 이용자의 Amazon 계정에 로그인해 쇼핑하는 것을 문제 삼아 2025년 11월 연방 CFAA와 캘리포니아주 CDAFA에 근거해 Perplexity를 제소했다. — [Retail Insight Network, 2026년 8월](https://www.retail-insight-network.com/newsletters/us-court-overturns-amazon-injunction-perplexity)
- 2026년 3월(보도에 따라 3월 6일, 9일 또는 10일): Maxine Chesney 판사(N.D. Cal.)는 Comet의 에이전트가 Amazon의 비밀번호 보호 영역에 접근하지 못하게 하는 예비적 금지명령을 내렸다. 접근이 "Amazon 이용자의 허락은 받았지만 Amazon의 권한 부여는 없이(with the Amazon user's permission, but without authorization by Amazon)" 이루어졌다고 판단했다. US$1bn의 담보 제공을 요구한 Perplexity의 신청은 기각했고, 항소를 위해 명령의 효력을 7일간 정지했다. [search summary] — [GeekWire](https://geekwire.com/2026/judge-blocks-perplexitys-ai-bot-from-shopping-on-amazon-in-early-test-of-agentic-commerce/), [Bloomberg Law](https://news.bgov.com/ip-law/amazon-wins-court-order-blocking-perplexitys-ai-shopping-bots), [The Fashion Law](https://www.thefashionlaw.com/court-blocks-perplexity-from-accessing-amazon-systems-in-agentic-ai-lawsuit/)
- 2026년 8월(기사 날짜는 2026년 8월 5일이며 판결일은 명시되지 않았다): 항소심 계속 중 금지명령의 효력을 정지해 두었던 제9연방항소법원의 3인 재판부가 금지명령을 취소했다. 재판부는 현재의 기록상 Amazon에 접근하는 주체는 Perplexity가 아니라 이용자이므로 Amazon이 CFAA와 CDAFA 청구에서 승소할 가능성이 낮다고 보았다: "Perplexity의 AI Assistant는 이용자의 지시에 따라 행동하는 도구로서 작동한다(Perplexity's AI Assistant operates as a tool acting on the user's instructions)." 금지명령의 다른 요소들도 Perplexity에 유리했다. 재판부는 판단을 해당 기록에 한정했으며, 이 판단이 "에이전틱 AI에 관한 더 넓은 법 원칙을 세우는 것이 아니다(does not set broader legal principles regarding agentic AI)"라고 밝혔다. 사건은 N.D. Cal.로 환송된다. — [Retail Insight Network](https://www.retail-insight-network.com/newsletters/us-court-overturns-amazon-injunction-perplexity)
- Amazon은 판단에 동의하지 않으며 대응 방안을 검토 중이라고 밝혔다(재심리 또는 연방대법원 심리 가능). 본안 소송은 계속된다. [search summary] — [eWeek](https://www.eweek.com/news/perplexity-ai-shopping-agent-returns-amazon/)

**미국 — 결제 및 소비자 법규**
- Regulation E(12 CFR 1005.2(m))는 무권한 이체를 소비자가 아닌 자가 "실제 권한 없이(without actual authority)" 개시하고 소비자에게 이익이 없는 이체로 정의한다. Regulation Z의 카드 관련 기준에는 "묵시적 또는 표현적 권한(implied or apparent authority)"이 포함된다. 논평가들은 하이재킹된 에이전트가 여전히 "실제 권한"을 가지는지에 관한 CFPB의 규칙·지침도 법원 판결도 없다고 전한다. 제3의 범주인 "권한 있는 접근, 의도하지 않은 거래"는 규정에 반영되어 있지 않다. [search summary] — [SuperMoney](https://www.supermoney.com/ai-agent-unauthorized-payment-liability), [JD Supra](https://www.jdsupra.com/legalnews/when-ai-clicks-pay-the-emerging-4898967/)
- ITIF(2026년 3월 23일)는 현행 Reg E대로라면 에이전틱 구매에서 소비자가 이의 제기 권리를 갖지 못하게 된다고 주장하며 CFPB에 조치를 촉구했다. [search summary] — [ITIF](https://itif.org/publications/2026/03/23/agentic-commerce-is-coming-but-regulation-meant-for-humans-will-slow-it-down/)
- 미국 은행들은 고객 계좌에서 활동하는 AI 에이전트를 탐지해 접근을 차단하고 있으며, 고객이 로그인 자격 증명을 AI에 넘기는 것에 반대한다고 보도되었다. "읽기 전용" 에이전트 권한(잔액·거래 내역 조회만 가능하고 실행은 불가)이 검토되고 있다. [search summary] — [Malwarebytes](https://www.malwarebytes.com/?p=481980)

**벤더 및 가맹점 약관**
- OpenAI: 한 분석에 따르면 OpenAI의 Service Terms(Section 14)는 에이전틱 기능이 이용자를 대신해 수행한 거래의 책임을 이용자에게 지우며, OpenAI는 결제·주문 분쟁의 대부분을 가맹점으로 돌린다. 이 약관이 법정 권리나 카드 회원의 권리를 무효로 할 수는 없다. 1차 약관은 읽지 않았다. [search summary] — [ConductAtlas](https://conductatlas.com/blog/visa-chatgpt-ai-agents-payment-governance-2026/), [The Neuron](https://www.theneuron.ai/news/chatgpt-wallet-purchase-goes-wrong/)
- 가맹점: 주요 가맹점들이 권한을 받은 AI 에이전트의 구매를 고객의 구매로 간주하도록 약관을 고치기 시작했다. Target은 자사 AI 어시스턴트의 구매에 대해 고객이 책임을 진다고 규정한 것으로 보도되었다. [search summary] — [Chargeflow](https://www.chargeflow.io/blog/agentic-commerce-regulation-what-merchants-need-to-know), [Tech News Day](https://technewsday.com/?p=50524)
- 일부 플랫폼은 그 대신 한도가 있는 구매 보호를 제공한다. 한 논평가는 책임을 이용자에게 전부 전가하는 것이 비양심적 계약(unconscionable)에 해당할 수 있다고 주장한다(주장일 뿐 법은 아니다). [search summary] — [HackerNoon](https://hackernoon.com/who-pays-when-an-ai-agent-makes-the-wrong-purchase)
- Google: "Gemini는 이용자를 대신해 구매하기 전에 이용자의 확인을 요구하도록 설계되어 있다(Gemini is designed to require user confirmation before making purchases on your behalf)." — [Google 블로그, 2026년 5월 12일](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- WeChat: 《腾讯微信软件许可及服务协议》는 Tencent가 개발하거나 권한을 부여하지 않은 제3자 소프트웨어로 WeChat을 제어하거나 방해하는 것을 금지한다(자동화된 세션을 로그아웃시키는 근거). — [36Kr, 2026년 6월 4일](https://www.36kr.com/p/3838411102534533)

**EU**
- AI Act Digital Omnibus: 2026년 5월 7일 잠정 합의. Regulation (EU) 2026/1744가 2026년 7월 24일 공포되어 2026년 7월 27일 발효되었다. Annex III의 독립형 고위험 의무는 이제 2027년 12월 2일부터, Annex I의 제품 내장형 의무는 2028년 8월 2일부터 적용된다. [search summary] — [Gibson Dunn](https://www.gibsondunn.com/eu-ai-act-omnibus-agreement-postponed-high-risk-deadlines-and-other-key-changes/), [Stephenson Harwood](https://www.stephensonharwood.com/post/102msaw/eu-digital-omnibus-deal-simplification-of-ai-act-and-postponed-deadlines/), [Future AGI](https://futureagi.com/blog/eu-ai-act-timeline-digital-omnibus-2027)
- PSD3/PSR: 시점에 관해 자료가 서로 어긋난다(2025년 11월 정치적 합의 대 2026년 4월 23일 최종 합의, 2026년 4월 조문 공개, 2026년 말 또는 2027년부터 적용). PSR은 2요소 SCA를 유지하되 위험 기반 취급을 늘리고, 사기 방지 통제가 미흡한 경우와 사칭 사기에 대한 PSP의 책임을 확대한다. [search summary] — [DLA Piper, 2026년 3월](https://www.dlapiper.com/en/insights/publications/2026/03/psd3-and-psr), [IBS Intelligence](https://ibsintelligence.com/ibsi-news/final-psd3-texts-advance-eu-payments-reform/), [PwC Ireland](https://pwc.ie/industries/banking/insights/payment-services-directive-3.html)
- CSA Labs 초안(2026년 3월 27일): PSD2의 SCA는 사람인 지급인을 전제하기 때문에 "자율 운영과 구조적 긴장을 만든다(create[s] structural tension with autonomous operation)". PSD2도 PSD3/PSR도 에이전틱 결제를 명시적으로 다루지 않는다. 동의 모델을 에이전트에게 위임된 권한에 맞게 고쳐 쓸 수 있을 것이다. 분석가의 견해이며 규제 당국의 견해가 아니다. [search summary] — [CSA Labs](https://labs.cloudsecurityalliance.org/agentic/agentic-transaction-security-framework-v1/)

**중국**
- 규제 방향은 권한 관리와 "이중 권한 부여" 쪽이다(1절 참조). 에이전트 제공자는 온디바이스 생성형 AI 서비스를 CAC에 등록해야 하기도 한다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)

**한국**
- 개인정보보호위원회는 에이전트에 의한 개인정보 처리에 대해 개발자, 배포 기업 등 가운데 누가 책임을 지는지를 아직 정하는 중이다. 안내서는 2026년 말 예정이다. — [MTN, 2026년 9월 23일](https://news.mtn.co.kr/news-detail/2026092316363993251)
- 가명·익명 처리된 데이터로는 충분하지 않은 경우 적법하게 수집한 개인정보를 개인정보보호위원회의 심의를 거쳐 AI 개발에 쓸 수 있게 하는 특례 조항이 2027년 3월 9일 시행된다. — [MTN, 2026년 9월 23일](https://news.mtn.co.kr/news-detail/2026092316363993251)
- 2026년 11월 16일 세미나 보도: AI가 직접 행동해 사고를 일으켰을 때 책임을 귀속시키고 배상하는 절차가 한국에 없다는 비판이 나왔다. [search summary] — [ZDNet Korea](https://zdnet.co.kr/view/?no=20261116162921)

### 추론
- 제9연방항소법원의 "이용자의 도구"라는 틀은 양쪽으로 작용한다. 플랫폼이 에이전트 벤더를 상대로 제기하는 컴퓨터 접근 청구를 약화시키는 한편, 에이전트의 구매를 이용자 자신의 행위로 취급하는 것을 뒷받침한다. 이는 거래를 이용자의 것으로 돌리는 벤더·가맹점 약관과 맞아떨어진다. 프롬프트 인젝션으로 일어난 구매는 아직 다투어진 적 없는 사안으로 남아 있다.
- 유럽의 SCA(그리고 한국의 추가 인증 관행) 때문에 화면 조작 에이전트는 보통 결제 인증을 스스로 끝낼 수 없다. 이 추가 인증 단계가 사실상의 사람 개입(human-in-the-loop) 통제 장치이다. 카드 네트워크가 제공하는 토큰화된 에이전트 자격 증명(3절)은 이를 우회하려는 업계의 경로이지 법의 변화가 아니다.
- 한국의 전자금융거래법(제9조, 사고에 대한 금융회사의 책임. 접근매체를 대여하거나 위임하는 것과 같은 이용자의 중대한 과실이 있으면 손해가 이용자에게 넘어간다)과 개인정보 보호법(제3자의 채팅 메시지나 계좌 정보가 담긴 스크린샷을 업로드하는 에이전트 벤더는 동의한 적 없는 사람들의 데이터를 처리하는 것이다)은 명백한 압력 지점이다. 그러나 두 법률 가운데 어느 하나라도 화면 에이전트에 적용한 한국 규제 당국의 입장, 판례, 로펌 분석은 찾지 못했다. 이것은 조사자의 해석이며 [background, not re-verified], 출처가 있는 발견 사항이 아니다.

### 공백
- 제9연방항소법원 판결문 자체(날짜, 재판부, 사건 번호)와 환송 후 지방법원의 현재 진행 상황은 확보하지 못했다. 모든 세부 내용은 업계 전문지에서 가져왔다.
- ChatGPT agent, Gemini agent mode / Gemini 앱 자동화, Claude in Chrome / computer use, Samsung Galaxy AI의 1차 약관은 읽지 않았다. Anthropic, Google, Samsung의 에이전트 관련 책임 조항을 인용한 자료는 찾지 못했다.
- 에이전트가 개시한 구매에 특화된 FTC나 CFPB의 입장은 찾지 못했다(논평가들은 그런 것이 없다고 말한다).
- AI Act가 범용 에이전트를 어떻게 분류하는지에 관한 집행위원회·AI Office의 지침은 찾지 못했다. 에이전트가 개시한 결제와 SCA에 관한 EBA의 입장도 찾지 못했다. 영국의 입장은 조사하지 않았다.
- 한국: 에이전트가 개시한 이체나 에이전트의 착오 결제를 전자금융거래법상 어떻게 취급하는지에 관해 금융위·금감원에서 나온 것은 찾지 못했다.
- 제3자 UI를 조작하는 에이전트가 관련된 다른 소송: Amazon v. Perplexity 외에는 찾지 못했다.

## 3. 제3자 앱과 서비스가 자사 UI를 조작하는 에이전트를 방어하거나 수용하는 방식

### 핵심
앱들은 처음에 기존 리스크 통제(비정상 로그인에 따른 로그아웃, 기능 차단)로 대응했고, 이후 플랫폼들은 "이용자 동의면 충분하다"는 입장에서 허용 목록과 앱 측 동의로 옮겨 갔다. Android의 Computer Control은 서명된 허용 목록에 있는 앱만 자동화하고, Google Play는 제3자 앱이 Accessibility API를 자율 행동에 쓰는 것을 금지하며, 중국에서는 거부 목록과 기관의 권한 부여가 규범으로 자리 잡고 있다. 결제는 에이전트가 카드 번호를 입력하게 두는 방식이 아니라 별도의 암호학적 신원 계층(Web Bot Auth + Visa TAP / Mastercard Agent Pay)으로 처리되고 있다.

### 인용 근거

**실제로 이루어진 앱 측 차단(중국)**
- Doubao 폰(Nubia M153)이 2025년 12월 판매를 시작한 지 일주일도 지나지 않아 Taobao, Alipay, Meituan, Pinduoduo가 제한을 걸었다. 주류 앱 23개 가운데 8개가 정상적으로 실행되지 않았다고 한다. 어시스턴트가 자신의 WeChat을 조작한 이용자들은 "登录环境存在异常" 팝업을 보고 강제 로그아웃되었으며, Doubao는 WeChat 조작 기능을 철회했다. — [36Kr, 2026년 6월 4일](https://www.36kr.com/p/3838411102534533)
- WeChat 관계자는 Doubao만을 겨냥한 조치는 취하지 않았다고 말했다. 이상 현상은 자동화가 기존 리스크 통제를 건드려 생겼을 가능성이 크다. — [CBNData, 2026년 8월 4일](https://www.cbndata.com/information/295610)
- 2025년 12월: 이용자들이 은행 앱 로그인 이상과 결제 차단을 신고했다. 2025년 12월 6일 어시스턴트는 금융 앱 조작 기능을 내렸다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)
- Doubao의 "Screen Automation Operation Declaration Protocol"(屏幕自动化操作声明协议, SAEP): 2026년 10월 14일 Doubao가 진행 상황과 거부 목록을 공개했다고 한다. WeChat, Meituan, Alipay를 포함해 150개 이상의 앱이 거부했다. 2세대 Doubao는 SAEP에 30일의 공시 기간을 결합해 개발자에게 거부할 권리를 준다고 한다. 검색 요약에서만 확인했으며, 열어 본 세 페이지에는 이 내용이 없었다. 미검증. [search summary] — [Huxiu](https://www.huxiu.com/article/4894229.html?f=rss), [Jiemian](https://www.jiemian.com/article/14862992.html)
- 보도된 WeChat의 에이전트 정책: 개발자 동의가 첫째 관문, 이용자의 권한 부여가 둘째 관문이고, 셋째로 고위험 행위(모멘트 게시, 대량 메시지 발송)는 의도적으로 제공하지 않는다. 시나리오를 넘나드는 데이터 호출은 기본적으로 꺼져 있다. 별도의 "AI专属卡"가 결제 권한을 격리한다. Alipay: AI 버전으로 전환하면 기본적으로 모든 미니프로그램 서비스에 권한이 부여되지만, 결제와 의료보험 관련 행위는 사용할 때마다 확인이 필요하다. 공식 문서와 대조해 검증하지 못했다. [search summary] — [Huxiu](https://www.huxiu.com/article/4892936.html?f=rss), [人人都是产品经理](https://www.woshipm.com/ai/6407830.html)

**Android / Google Play**
- Google Play의 Accessibility API 정책은 이 API를 "행동이나 결정을 자율적으로 개시하고, 계획하고, 실행하는 앱(an app that autonomously initiates, plans, and executes actions or decisions)"에 쓰는 것을 금지한다. 핵심 기능이 장애인을 직접 지원하는 앱만 `isAccessibilityTool`을 선언할 수 있고, 그 밖의 앱은 사용 사실을 공개하고 동의를 받아야 한다. — [Play Console 고객센터](https://support.google.com/googleplay/android-developer/answer/16585319) (2026년 11월 열람)
- 자율 행동에 관한 문구는 2025년 10월 정책 업데이트에서 "명확화"로 도입되었고 2026년 1월 개발자 뉴스레터에서 다시 언급되었다. [search summary] — [Android 개발자 뉴스레터, 2026년 1월](https://developer.android.com/newsletter/play-monthly/2026/content/january)
- Play 정책: 2027년 4월 14일부터 개발자는 자신의 AppFunctions가 AI 에이전트를 속이거나 조종하지 않고, 보안 통제를 우회하지 않으며, 다른 앱을 방해하지 않도록 해야 한다. — [Play Console 고객센터](https://support.google.com/googleplay/android-developer/answer/16585319)
- Advanced Protection Mode: Android Canary 2602는 접근성 도구로 분류되지 않은 앱의 AccessibilityService 사용을 차단하는 기능을 시험하고 있다. Malwarebytes(2026년 3월)는 APM에서 비밀번호 관리자와 자동화 앱이 이 API를 쓸 수 없게 된다고 전한다. [search summary] — [Malwarebytes, 2026년 3월](https://www.malwarebytes.com/blog/mobile/2026/03/google-cracks-down-on-android-apps-abusing-accessibility)
- Computer Control: 백그라운드 앱 자동화를 위한 Android 프레임워크이다. `ACCESS_COMPUTER_CONTROL` 권한(높은 권한을 가진 신뢰된 앱으로 제한된다)이 필요하고, 여기에 더해 세션마다 또는 모든 세션에 대한 이용자의 승인이 필요하다. Gemini의 작업 자동화가 이 위에서 동작한다. [search summary] — [Android Authority, 2025년](https://www.androidauthority.com/android-computer-control-feature-3603862/), [Android Authority](https://www.androidauthority.com/gemini-automation-control-other-apps-3637733/)
- Gemini의 자동화 대상 목록은 각 앱의 패키지 이름을 서명 인증서와 짝지어 둔다(공식 Play 빌드만 해당한다). 2026년 11월 12일 기준으로 안정판 Android 17 QPR2에 114개 앱이 들어 있으며, 이틀 전의 106개에서 늘었다. 페이지가 403을 반환해 세부 내용은 검색 요약에서 가져왔다. [search summary] — [Android Authority, 2026년 11월 12일](https://androidauthority.com/google-gemini-task-automation-target-apps-list-3734443)
- Google이 밝힌 안전장치(2026년 5월 12일): Gemini는 "이용자가 작업을 허용한 앱에만 접근할 수 있고 기기의 나머지에는 접근할 수 없다(can only access the apps you allow it to work in, and not the rest of your device)". 설정의 앱별 자동화 토글은 "올해 안(later this year)"에 제공된다. 자동화는 "이용자가 지시할 때에만(only when you tell it to)" 시작된다. 닫을 수 없는 알림 칩과 "View progress"가 있다. Privacy Dashboard는 지난 24시간 동안 어떤 AI 어시스턴트가 활동했고 어떤 앱을 사용했는지 보여 주게 된다. 구매에는 확인이 필요하다. 개발자용 옵트아웃은 설명되어 있지 않다. — [Google 블로그](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- 앱 분해 분석(teardown)에서 나온 문자열은 앱별로 어떤 어시스턴트가 그 앱을 자동화할 수 있는지 나열하는 설정 화면이 있음을 시사한다(추정이며 발표된 것은 아니다). [search summary] — [Nerdschalk](https://nerdschalk.com/android-17-is-adding-a-settings-screen-to-manage-which-apps-ai-assistants-can-automate-apk-teardown/)
- 화면 에이전트를 상대로 쓸 수 있는, 오래전부터 있던 앱 측 통제 수단: `FLAG_SECURE`(창의 스크린샷·녹화를 차단한다), `accessibilityDataSensitive`(Android 14+, 접근성 도구가 아닌 서비스에 뷰를 숨긴다), Play Integrity API(기기·앱 증명). [background, not re-verified] — [Android 문서: FLAG_SECURE](https://developer.android.com/security/fraud-prevention/activities), [Android 14 accessibilityDataSensitive](https://developer.android.com/about/versions/14/features#accessibility-data-sensitive), [Play Integrity](https://developer.android.com/google/play/integrity)

**Apple**
- WWDC 2026: SiriKit은 지원 중단 예정(deprecated)이 되었고, App Intents가 Siri가 제3자 앱 안에서 행동하는 유일한 경로가 되었다. 개발자가 지원을 추가해야 한다(옵트인). 한 개발자 블로그는 제3자 앱이 다른 앱의 인텐트를 직접 호출하는 API가 없다고 전한다. iOS 27.3 베타(2026년 11월 3일)는 화면 인식(on-screen awareness)을 포함한 개발자 프리뷰를 열었다. "Extensions"는 설치된 앱의 에이전트가 Siri와 함께 동작하게 해 준다. Apple의 MCP 지원은 확인된 바 없다. 대부분 블로그·유출 정보이며 Apple 문서가 아니다. [search summary] — [MacRumors, 2026년 3월 29일](https://www.macrumors.com/2026/03/29/ios-27-siri-app-with-extensions-rumor/), [dev.to WWDC 2026 노트](https://dev.to/malik_chohra/wwdc-2026-app-intents-foundation-models-and-what-rn-devs-should-ship-3o8a), [Let's Data Science](https://letsdatascience.com/news/apple-opens-siri-ai-to-all-developers-in-ios-273-beta-5e07c890), [AppleInsider, 2025년 9월 22일](https://appleinsider.com/articles/25/09/22/ios-26-could-get-a-major-ai-boost-with-the-model-context-protocol)

**웹: 에이전트 신원과 결제 인증**
- Web Bot Auth(Cloudflare 제안, 2025년 5월)는 HTTP Message Signatures로 에이전트 트래픽을 인증한다. `Signature-Input`에는 `keyid`, `alg`(Ed25519), `created`/`expires`, `nonce`, `tag`가 담긴다. 공개키는 well-known 디렉터리에 게시된다. 요청은 유효 시간이 제한되고 재전송할 수 없다. — [Cloudflare 블로그, 2025년 10월 24일, 2026년 7월 15일 갱신](https://blog.cloudflare.com/secure-agentic-commerce/)
- Visa Trusted Agent Protocol(2025년 10월 14일 발표)과 Mastercard Agent Pay(2025년 4월 29일 출시)는 Web Bot Auth를 기반으로 한다. 에이전트가 등록하고 Visa와 Mastercard가 각각 키 디렉터리를 운영한다. 태그로 탐색과 결제를 구분한다(`agent-browser-auth`, `agent-payer-auth`). 가맹점은 에이전트를 소비자 신원에 연결하고 예상되는 결제 수단(네트워크 토큰, 브라우저 사용 방식의 비회원 결제, 소액 결제)을 알릴 수 있다. Cloudflare는 봇 방어를 유지하면서 등록된 에이전트를 받아들이는 관리형 규칙 집합을 계획하고 있다. — [Cloudflare 블로그](https://blog.cloudflare.com/secure-agentic-commerce/), [Digital Transactions](https://www.digitaltransactions.net/visa-launches-trusted-agent-an-agentic-commerce-protocol/)
- 2026년 현황: Mastercard의 첫 실거래 에이전틱 결제가 홍콩에서 이루어졌다고 2026년 3월 말 보도되었다. "Agent Pay for Machines"는 2026년 6월 10일. American Express가 Web Bot Auth를 사용할 예정이다. 두 네트워크 모두 Google의 Universal Commerce Protocol에 참여한다. 2차 자료뿐이다. [search summary] — [eco.com 비교](https://eco.com/support/en/articles/15192003-mastercard-agent-pay-vs-visa-trusted-agent-2026-compared), [Digital Applied, 2026년 8월 6일](https://www.digitalapplied.com/blog/agent-checkout-authentication-card-networks-2026)
- StepFun과 협력한 Alipay: 작업은 Alipay 자체 에이전트 "阿宝"에 넘겨진다. 결제와 권한 부여 단계에는 여전히 이용자 본인의 확인이 필요하다. — [CBNData, 2026년 8월 4일](https://www.cbndata.com/information/295610)

**한국**
- 한국의 은행들은 자체 에이전트를 만들고 있다(KakaoBank는 "AI native bank" 전환을 선언했고, Hana Bank의 로드맵은 모든 거래를 에이전트가 처리하게 하는 것이다). 금융보안원은 금융권을 겨냥한 AI 에이전트 기반 공격 시도를 탐지했다고 밝혔다. 금융감독원 자료: 2026년 8월까지 20개 은행이 실시한 모의해킹 78건 가운데 9건이 AI 에이전트를 사용했다. [search summary for bank items] — [한국경제, 2026년 10월](https://www.hankyung.com/article/202610018778Y), [SBS Biz](https://biz.sbs.co.kr/amp/article/20000335477), [파이낸셜뉴스, 2026년 10월 7일](https://www.fnnews.com/news/202610071821048238)

### 추론
- 세 개의 방어 계층이 보인다. (a) 앱 수준의 휴리스틱(리스크 엔진, 증명, 보안 창)으로, 사기 방지의 부수 효과로 에이전트를 차단한다. (b) OS·플랫폼의 문지기 역할(Android의 특권 권한 + 서명된 허용 목록, iOS의 인텐트 전용, 제3자의 접근성 기반 자율 동작에 대한 Play의 금지). (c) 명시적 선언 체계(중국의 이중 권한 부여, Doubao의 거부 목록, Web Bot Auth 서명).
- Play의 접근성 규칙과 특권 권한인 `ACCESS_COMPUTER_CONTROL`을 합치면, 범용 제3자 화면 에이전트는 순정 Android에서 공인된 배포 경로가 없다. 화면 조작은 사실상 OS 벤더·OEM의 어시스턴트 몫으로 남겨져 있다. 이것은 보안 문제인 만큼이나 경쟁 문제이기도 하다.
- Android의 허용 목록은 공개된 개발자 플래그를 통해 옵트인하는 방식이 아니라 Google이 선별한다(문서화된 플래그는 없다). 따라서 앱 개발자의 영향력은 간접적이다(FLAG_SECURE, 리스크 통제, 또는 협상).

### 공백
- 한국의 은행·결제 앱(Toss, KakaoPay, 은행 앱)이 Gemini/Galaxy AI의 화면 자동화를 탐지하거나 차단하는지, 또는 한국 금융 앱이 Gemini의 허용 목록에 있는지에 관한 자료는 찾지 못했다.
- 은행·결제·메신저 앱이 Gemini의 114개 앱 목록에 들어 있는지는 알 수 없다(페이지 차단).
- Android Computer Control에 대한 개발자용 옵트아웃·옵트인 신호는 문서화된 것이 없다. Web Bot Auth와 검증되지 않은 SAEP 외에 GUI 에이전트를 위한 robots.txt 유사 표준은 찾지 못했다.
- Akamai나 다른 CDN의 에이전트 검증 프로그램: 조사하지 않았다.
- SAEP의 세부 내용과 WeChat/Alipay의 에이전트 규칙은 1차 자료로 확인해야 한다.
- FLAG_SECURE와 `accessibilityDataSensitive`가 Computer Control의 가상 디스플레이에서 어떻게 동작하는지: 자료를 찾지 못했다.

## 4. 기업 관점: MDM/EMM, Knox, DLP, 감사

### 핵심
2026년 말 기준으로 온디바이스 화면 에이전트에 대한 기업용 통제는 거친 수준으로만 존재한다. Android 17은 관리자가 관리 대상 기기에서 AI 자동화를 끌 수 있게 하고 Work Profile을 개인 프로필의 에이전트로부터 격리하며, Knox Service Plugin은 Galaxy AI의 클라우드 처리를 제한할 수 있다. 세밀한 정책, 감사 로깅, 클라우드 모델로 전송되는 스크린샷에 대한 DLP는 찾은 자료에 문서화되어 있지 않다.

### 인용 근거
- Android 17 기업용 게시글(2026년 6월 16일): IT 관리자는 "관리 대상 기기에서 AI 자동화를 완전히 끌(disable AI automation entirely on managed devices)" 수 있다. 개인 프로필의 에이전트는 Work Profile 앱에 접근하거나 이를 자동화할 수 없다. 세부 내용은 Gemini for Work 고객센터 문서로 미루어져 있다. 정책·API 이름은 제시되지 않았다. 한 커뮤니티 구성원은 EMM 벤더들이 새로운 Android 통제 기능을 노출하는 데 자주 뒤처진다고 지적했다. — [Android Enterprise Community](https://www.androidenterprise.community/product-updates/android-17-brings-intelligent-experiences-and-advanced-safeguards-for-enterprise-2705)
- Samsung(2026년 5월 1일): Knox Service Plugin은 클라우드 처리의 관리와 비활성화를 포함해 Galaxy AI 설정에 대한 통제권을 IT 부서에 준다. [search summary] — [Samsung Knox 블로그](https://www.samsungknox.com/en/blog/galaxy-ai-for-business)
- Google이 계획 중인, 지난 24시간 동안 어떤 AI 어시스턴트가 활동했고 어떤 앱을 사용했는지 보여 주는 Privacy Dashboard 화면은 이용자용 로그이지 기업용 감사 추적이 아니다. — [Google 블로그, 2026년 5월 12일](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- Google은 Android AI 보안 아키텍처의 핵심 부분이 오픈소스이고, 바이너리 투명성을 갖추었으며, 제3자 감사를 받았다고 말한다. Private Compute Core / Private AI Compute / protected KVM은 Magic Cue 같은 앰비언트 기능에 대해 언급된 것이지 UI 자동화에 대해 언급된 것이 아니다. — [Google 블로그, 2026년 5월 12일](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- 에이전트 로깅·감사에 대한 기대는 규제 당국의 초안에 나타난다. NIST RFI의 주제에는 모니터링, 로깅, 사고 대응이 포함된다. 개인정보보호위원회는 AI에 부여한 권한이 어떻게 쓰이는지를 기업이 관리하고 입증할 수 있기를 원하며, 에이전트 로그와 메모리의 보관·삭제 규칙을 검토하고 있다. — [Pillsbury](https://www.pillsburylaw.com/en/news-and-insights/nist-ai-agent-standards.html), [MTN, 2026년 9월 23일](https://news.mtn.co.kr/news-detail/2026092316363993251)
- 중국의 금융 단체표준은 스크린샷·화면 녹화 데이터의 수집이 피호출 측의 보안 정책을 따르도록 요구한다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)

### 추론
- 현재의 기업용 통제는 이분법적이다(자동화 켜기/끄기, 프로필 격리). 에이전트에 대한 앱별 허용/거부, 업로드 전 화면 캡처의 가림 처리, SIEM으로 내보낼 수 있는 변조 탐지 가능한 행동 로그는 아직 채워지지 않은 제품 수요이다.

### 공백
- 화면 자동화에 대한 Knox, Intune, Workspace ONE, Android Management API의 정책 이름은 찾지 못했다. 에이전트 스크린샷을 다루는 벤더의 DLP 문서도 찾지 못했다.
- 구속력 있는 규제 가운데 온디바이스 에이전트에 특화된 감사 로그 요건은 찾지 못했다.

## 5. 추세: 통제권을 대가로 한 구조화된 에이전트 인터페이스, 그리고 그 배후의 사업상 갈등

### 핵심
그렇다. 2026년 동안 무게 중심은 초대받지 않은 GUI 조작에서 앱이 내어 주는 구조화된 인터페이스(MCP, A2A, AppFunctions, App Intents)로 옮겨 갔고, GUI 조작은 주로 OS 벤더가 허용 목록을 선별하는 곳에서 살아남았다. 그 동인은 보안인 만큼이나 이용자 진입점, 트래픽, 리스크 관리에 대한 통제이기도 하다.

### 인용 근거
- 2세대 Doubao 폰은 더 이상 이용자의 권한 부여 후에 화면을 읽고 클릭을 모의하지 않는다. 앱이 MCP 서비스를 제공하고 제어를 허용해야 한다. ByteDance의 앱 수십 개가 프로토콜 호출을 지원한다. 외부 슈퍼앱 가운데 어느 곳이 개방했는지는 공개되지 않았다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927), [CBNData, 2026년 8월 4일](https://www.cbndata.com/information/295610)
- StepFun의 STEPX Neo는 무엇을 노출할지를 앱이 결정하는 "GUI-MCP" 프로토콜을 사용한다. — [封面新闻, 2026년 9월 11일](https://cbgc.scol.com.cn/news/7944927)
- WeChat은 Huawei, Honor, Xiaomi, OPPO, vivo와 A2A 어시스턴트 기능에서 협력했다(Honor가 2026년 6월 처음). 메시지 발송, 음성 통화, 영상 통화만 노출된다. 휴대폰 어시스턴트가 구조화된 지시를 보내면 WeChat이 직접 실행하며, 실행 권한과 리스크 통제 권한은 WeChat이 계속 쥔다. — [36Kr, 2026년 6월 4일](https://www.36kr.com/p/3838411102534533), [封面新闻](https://cbgc.scol.com.cn/news/7944927)
- Alipay는 외부 에이전트가 자사 UI를 조작하게 두지 않고 에이전트 작업을 자체 에이전트로 넘긴다. — [CBNData, 2026년 8월 4일](https://www.cbndata.com/information/295610)
- Android: AppFunctions는 모의 화면 제어를, OS가 식별하고 제한할 수 있는 선언적이고 구조화된 작업으로 대체한다. Android 17은 런타임 등록, 액티비티 범위 함수, 더 세밀한 접근·탐색 통제를 추가한다. AppFunctions에 대한 Play 정책은 2027년 4월 14일부터 적용된다. 한 논평은 대부분의 에이전트가 AppFunctions라는 "우리(cage)" 밖에 남아 있다고 지적한다. [search summary for first item] — [remio.ai](https://www.remio.ai/post/google-android-app-functions-built-a-security-cage-but-most-ai-agents-remain-out), [Play Console 고객센터](https://support.google.com/googleplay/android-developer/answer/16585319), [Android Developers Blog, 2026년 2월](https://android-developers.googleblog.com/2026/02/the-intelligent-os-making-ai-agents.html)
- Galaxy S26에서의 Google/Samsung: 시스템이 권한을 열고 앱이 협조한다. Gemini는 선별된 목록의 제3자 앱을 화면 읽기를 통해 조작한다. — [封面新闻](https://cbgc.scol.com.cn/news/7944927), [Android Authority, 2026년 11월 12일](https://androidauthority.com/google-gemini-task-automation-target-apps-list-3734443)
- Apple: App Intents 전용, 개발자 옵트인(3절 참조). — [dev.to](https://dev.to/malik_chohra/wwdc-2026-app-intents-foundation-models-and-what-rn-devs-should-ship-3o8a)
- 중국 언론의 사업적 해석: AI 폰과 슈퍼앱 사이의 "진입점 전쟁"이다. 기술 표준이 있다고 해서 상업적 합의가 굴러가는 것은 아니며 중국이 Google의 A2A(2026년까지 150개 이상의 조직이 A2A에 참여)를 그대로 베끼지는 않을 것이라는 논평이 있다. — [36Kr, 2026년 6월 4일](https://www.36kr.com/p/3838411102534533), [CBNData](https://www.cbndata.com/information/295610)
- 미국의 유사 사례: Amazon은 Perplexity의 에이전트를 로그인 상태의 쇼핑에서 배제하려고 소송을 벌였고, 그러는 동안 카드 네트워크와 Google(UCP)은 공인된 에이전트 커머스 채널을 구축하고 있다. — [Retail Insight Network](https://www.retail-insight-network.com/newsletters/us-court-overturns-amazon-injunction-perplexity), [eco.com](https://eco.com/support/en/articles/15192003-mastercard-agent-pay-vs-visa-trusted-agent-2026-compared)

### 추론
- 앱 제공자는 좁고 구조화된 기능 집합(WeChat: 세 가지 행위)을 내어 주는 대신 실행, 리스크 통제, 광고 지면, 고객 관계를 계속 쥔다. GUI 조작은 에이전트 인터페이스가 없는 롱테일 앱을 위한 대비책으로 남는다. 따라서 화면 에이전트를 위한 보안 기술은 여전히 필요하지만, 개방된 접근이 아니라 플랫폼이 선별하는 허용 목록과 앱 동의 체제 아래에서 필요하다.
- 결과는 법적 환경에 따라 갈린다. 미국에서는 항소법원이 (잠정적으로) 이용자가 권한을 준 에이전트의 손을 들어 주었고, 중국에서는 표준이 앱·기관의 동의 쪽에 섰다. 한국은 아직 선택하지 않았다.

### 공백
- 얼마나 많은 한국 앱이 Galaxy AI/Gemini에 AppFunctions/MCP를 노출하는지, 또는 제3자 화면 에이전트에 대한 Kakao/Naver/Toss의 입장이 무엇인지에 관한 데이터는 없다.
- 주요 앱의 AppFunctions 및 App Intents 채택 수치는 찾지 못했다.
- Doubao의 MCP 전용 접근과 SAEP 거부 목록이 공존하는지(일부 GUI 조작이 유지되는가?)는 불분명하다. 자료마다 강조점이 어긋난다.
