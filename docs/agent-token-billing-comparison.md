# Claude · Codex · Gemini 에이전트의 토큰 과금: 입력·출력·캐시 단가

> 기준 시점: 2026년 11월 16일 각 사 공식 가격 페이지 열람으로 확인. 가격은 모두 미국 달러(USD) 정가다.
> 가격은 자주 바뀐다. 예산 산정에는 각 표 아래에 달린 공식 페이지를 다시 확인해야 한다.
>
> 범위: Claude Code(Anthropic), Codex(OpenAI), Gemini CLI(Google) 세 에이전트가 쓰는 모델의
> **입력 토큰, 출력 토큰, 캐시 토큰** 단가만 다룬다. 웹 검색 같은 도구 요금과 구독료는 다루지 않는다.
>
> 단위: 별도 표기가 없으면 **토큰 100만 개(MTok)당 달러**다.

## 1. 요약

- 세 에이전트 모두 과금 요소는 **입력, 출력, 캐시** 세 가지이고, 모델마다 단가가 다르다.
- **출력은 입력의 5배**가 기본이다. 세 회사의 최신 주력 모델이 모두 이 비율을 쓴다.
- **캐시 읽기는 입력의 2.5~10%** 다. 에이전트는 같은 맥락을 매 요청마다 다시 보내므로 실제 비용에서 캐시 읽기 비중이 크다.
- **캐시 쓰기 과금 방식이 회사마다 다르다.**
  - Claude: 캐시에 쓸 때 입력의 1.25배(5분 보관) 또는 2배(1시간 보관)를 낸다.
  - OpenAI API: GPT-6·5.6 계열에 "30m write"(입력의 1.25배)와 "12h write"(2.5배) 단가가 표시되어 있다. Codex 크레딧에는 캐시 쓰기 요금이 없다.
  - Gemini: Gemini 4 계열은 캐시 쓰기 요금과 저장 요금이 없다. 3.x 이하 모델은 캐시 저장 요금을 시간당으로 낸다.
- 구독으로 쓰면(Claude Pro·Max, ChatGPT Plus·Pro) 토큰 단가가 직접 청구되지 않고 사용 한도에서 차감된다.
  한도를 넘거나 API 키로 쓰면 아래 단가가 그대로 적용된다.

### 주력 모델 비교

| 에이전트 | 모델 | 입력 | 출력 | 캐시 읽기 | 캐시 쓰기 |
|---|---|---|---|---|---|
| Claude Code | Claude Fable 5.2 | $8 | $40 | $0.20 | $10 (5분) / $16 (1시간) |
| Claude Code | Claude Opus 5.5 | $4 | $20 | $0.20 | $5 / $8 |
| Claude Code | Claude Sonnet 5.5 | $2 | $10 | $0.10 | $2.50 / $4 |
| Claude Code | Claude Haiku 5.5 | $0.10 | $0.50 | $0.01 | $0.125 / $0.20 |
| Codex | GPT-6 Astra | $10 | $50 | $1.00 | $12.50 (30m) / $25 (12h) |
| Codex | GPT-6.1 Sol | $2 | $10 | $0.10 | $2.50 / $5 |
| Codex | GPT-6.1 Terra | $1 | $5 | $0.05 | $1.25 / $2.50 |
| Codex | GPT-6 Luna | $0.10 | $0.50 | $0.01 | $0.125 / $0.25 |
| Gemini CLI | Gemini 4 Carbon | $2 | $10 | $0.10 | 없음 |
| Gemini CLI | Gemini 4 Hydrogen | $1.50 | $7.50 | $0.075 | 없음 |
| Gemini CLI | Gemini 3.8 Flash | $0.75 | $3.75 | $0.075 | 없음 (저장 $0.50/시간) |

Codex 행은 API 키로 쓸 때의 달러 단가다. ChatGPT 구독으로 쓸 때의 크레딧 단가는 [3.1절](#31-chatgpt-로그인으로-쓸-때-codex-크레딧)에 있다.
Gemini 4 Carbon과 3.8 Flash는 가격 인상이 예정되어 있다([4절](#4-gemini-cli-google)).

## 2. Claude Code (Anthropic)

### 과금 경로

| 사용 방식 | 토큰 과금 |
|---|---|
| 구독(Pro, Max, Ultra, Team) | 5시간·주간 사용 한도에서 차감. 토큰별 청구 없음 |
| 구독 한도 초과 후 "사용 크레딧" | 아래 API 표준 단가로 청구 |
| API 키, Enterprise 사용량제, Bedrock·Vertex·Foundry | 아래 단가로 토큰별 청구 |

출처: [Claude Code 비용 문서](https://code.claude.com/docs/en/costs),
[Pro·Max 사용 안내](https://support.claude.com/en/articles/11145838-using-claude-code-with-your-pro-or-max-plan),
[사용 크레딧](https://support.claude.com/en/articles/12429409-extra-usage-for-paid-claude-plans)

### 모델별 단가

| 모델 | 입력 | 출력 | 캐시 쓰기 (5분) | 캐시 쓰기 (1시간) | 캐시 읽기 |
|---|---|---|---|---|---|
| Claude Fable 5.2 | $8 | $40 | $10 | $16 | $0.20 |
| Claude Mythos 5.2 (제한 제공) | $8 | $40 | $10 | $16 | $0.20 |
| Claude Fable 5.1 | $10 | $50 | $12.50 | $20 | $0.25 |
| Claude Mythos 5.1 (제한 제공) | $10 | $50 | $12.50 | $20 | $0.25 |
| Claude Fable 5 | $10 | $50 | $12.50 | $20 | $1 |
| Claude Mythos 5 (제한 제공) | $10 | $50 | $12.50 | $20 | $1 |
| Claude Opus 5.5 | $4 | $20 | $5 | $8 | $0.20 |
| Claude Opus 5 / 4.8 / 4.7 / 4.6 / 4.5 | $5 | $25 | $6.25 | $10 | $0.50 |
| Claude Opus 4.1 / 4 (종료, 일부 클라우드만 제공) | $15 | $75 | $18.75 | $30 | $1.50 |
| Claude Sonnet 5.5 | $2 | $10 | $2.50 | $4 | $0.10 |
| Claude Sonnet 5 | $2 | $10 | $2.50 | $4 | $0.20 |
| Claude Sonnet 4.6 | $3 | $15 | $3.75 | $6 | $0.30 |
| Claude Sonnet 4.5 (지원 중단 예정) | $3 | $15 | $3.75 | $6 | $0.30 |
| Claude Sonnet 4 (종료, 일부 클라우드만 제공) | $3 | $15 | $3.75 | $6 | $0.30 |
| Claude Haiku 5.5 (프롬프트 10만 토큰 이하) | $0.10 | $0.50 | $0.125 | $0.20 | $0.01 |
| Claude Haiku 5.5 (프롬프트 10만 토큰 초과) | $0.50 | $2.50 | $0.625 | $1 | $0.05 |
| Claude Haiku 4.5 | $1 | $5 | $1.25 | $2 | $0.10 |

출처: [Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing)

### 캐시 과금 규칙

- **캐시 쓰기**: 5분 보관은 입력 단가의 1.25배, 1시간 보관은 2배다.
- **캐시 읽기**: 모델에 따라 입력 단가 대비 비율이 다르다.
  - Fable·Mythos 5.2와 5.1: 2.5%
  - Opus 5.5, Sonnet 5.5: 5%
  - 그 밖의 모델: 10%
- Haiku 5.5의 10만 토큰 기준에는 캐시 읽기·쓰기 토큰도 포함된다.

### 단가를 바꾸는 조건

- **긴 컨텍스트**: Claude 4.6 이후 모델은 100만 토큰 컨텍스트를 표준 단가로 쓴다. Haiku 5.5만 위 표처럼 단가가 오른다.
- **사고(thinking) 토큰**: 출력 토큰으로 청구된다. ([Claude Code 비용 문서](https://code.claude.com/docs/en/costs))
- **배치 API**: 입력·출력 50% 할인.
- **빠른 모드** (입력/출력): Fable 5.2와 Mythos 5.2 $16/$80, Opus 5.5 $8/$40, Opus 5와 4.8 $10/$50.
- **미국 내 추론 지정**: 모든 항목에 1.1배.

## 3. Codex (OpenAI)

### 과금 경로

| 사용 방식 | 토큰 과금 |
|---|---|
| ChatGPT 구독(Plus, Pro, Business) | 5시간당 메시지 한도에서 차감. 토큰별 청구 없음 |
| 구독 한도 초과 후 크레딧 구매, Enterprise 유연 요금제 | 3.1절의 크레딧 단가로 차감 |
| API 키 | 3.2절의 달러 단가로 토큰별 청구 |

출처: [Codex 가격](https://learn.chatgpt.com/docs/pricing)

### 3.1 ChatGPT 로그인으로 쓸 때: Codex 크레딧

단위는 **토큰 100만 개당 크레딧**이고 표준 속도 기준이다.

| 모델 | 입력 | 캐시된 입력 | 출력 |
|---|---|---|---|
| GPT-6 Astra | 250 | 25 | 1,250 |
| GPT-6.1 Sol | 50 | 2.5 | 250 |
| GPT-6.1 Terra | 25 | 1.25 | 125 |
| GPT-6 Sol | 50 | 5 | 250 |
| GPT-6 Luna | 2.5 | 0.25 | 12.5 |
| GPT-5.6 Sol | 100 | 10 | 500 |
| GPT-5.6 Terra | 50 | 5 | 300 |
| GPT-5.6 Luna | 5 | 0.5 | 30 |

- **캐시 쓰기 요금이 없다.** 페이지에 "Codex credit billing has no separate cache-write charge"라고 적혀 있다.
- **크레딧 1개의 달러 가격은 페이지에 없다.** 요금제나 계약에 따라 다르다고만 안내한다.
  위 크레딧 수에 $0.04를 곱하면 모든 행이 3.2절의 API 달러 단가와 일치한다. 이는 계산으로 얻은 추정이고 공식 수치가 아니다.
- **빠른 모드**: 구매한 크레딧에서는 Fast 2배, Ultrafast 6배로 차감된다. 구독 포함 사용량에서는 Fast 2.5배, Ultrafast 8배다.
- GPT-5.5는 2026년 10월 14일에 ChatGPT 로그인용 Codex에서 종료되었다.

출처: [Codex 가격](https://learn.chatgpt.com/docs/pricing)

### 3.2 API 키로 쓸 때: OpenAI API 단가

API 키로 쓰면 그 키에서 쓸 수 있는 API 모델을 모두 고를 수 있다. 표준(Standard) 등급 기준이다.

**GPT-6·5.6 계열** (입력 27.2만 토큰 이하)

| 모델 | 입력 | 출력 | 캐시된 입력 | 캐시 쓰기 (30m) | 캐시 쓰기 (12h) |
|---|---|---|---|---|---|
| gpt-6-astra | $10 | $50 | $1.00 | $12.50 | $25 |
| gpt-6.1-sol | $2 | $10 | $0.10 | $2.50 | $5 |
| gpt-6.1-terra | $1 | $5 | $0.05 | $1.25 | $2.50 |
| gpt-6-sol | $2 | $10 | $0.20 | $2.50 | $5 |
| gpt-6-luna | $0.10 | $0.50 | $0.01 | $0.125 | $0.25 |
| gpt-5.6-sol | $4 | $20 | $0.40 | $5 | $10 |
| gpt-5.6-terra | $2 | $12 | $0.20 | $2.50 | $5 |
| gpt-5.6-luna | $0.20 | $1.20 | $0.02 | $0.25 | $0.50 |

**GPT-6·5.6 계열** (입력 27.2만 토큰 초과 시)

| 모델 | 입력 | 출력 | 캐시된 입력 | 캐시 쓰기 (30m) | 캐시 쓰기 (12h) |
|---|---|---|---|---|---|
| gpt-6-astra | $20 | $75 | $2.00 | $25 | $50 |
| gpt-6.1-sol | $4 | $15 | $0.20 | $5 | $10 |
| gpt-6.1-terra | $2 | $7.50 | $0.10 | $2.50 | $5 |
| gpt-6-sol | $4 | $15 | $0.40 | $5 | $10 |
| gpt-6-luna | $0.20 | $0.75 | $0.02 | $0.25 | $0.50 |
| gpt-5.6-sol | $8 | $30 | $0.80 | $10 | $20 |
| gpt-5.6-terra | $4 | $18 | $0.40 | $5 | $10 |
| gpt-5.6-luna | $0.40 | $1.80 | $0.04 | $0.50 | $1.00 |

**이전 세대 모델** (캐시 쓰기 단가 표시 없음)

| 모델 | 입력 | 출력 | 캐시된 입력 |
|---|---|---|---|
| gpt-5.5 | $5 | $30 | $0.50 |
| gpt-5.4 | $2.50 | $15 | $0.25 |
| gpt-5.4-mini | $0.75 | $4.50 | $0.075 |
| gpt-5.4-nano | $0.20 | $1.25 | $0.02 |
| gpt-5.3-codex | $1.75 | $14 | $0.175 |
| gpt-5.2 | $1.75 | $14 | $0.175 |
| gpt-5.1 / gpt-5 | $1.25 | $10 | $0.125 |
| gpt-5-mini | $0.25 | $2 | $0.025 |
| gpt-5-nano | $0.05 | $0.40 | $0.005 |

gpt-5.5와 gpt-5.4는 27.2만 토큰 초과 시 입력 2배, 출력 1.5배다(gpt-5.5 $10/$45, gpt-5.4 $5/$22.50).

출처: [OpenAI API 가격](https://developers.openai.com/api/docs/pricing)

### 캐시 과금 규칙

- **캐시된 입력**: GPT-6.1 계열은 입력의 5%, 그 밖의 GPT-6·5.x 계열은 10%다.
- **캐시 쓰기**: GPT-6·5.6 계열에는 "30m write"(입력의 1.25배)와 "12h write"(2.5배) 열이 있다.
  페이지 본문에서 이 열의 적용 조건 설명은 확인하지 못했다. 이전 세대 모델에는 이 열이 비어 있다.

### 단가를 바꾸는 조건

- **Batch·Flex**: 표준의 50%.
- **Fast 등급**: 대부분 표준의 2배(gpt-6-astra $20/$100). gpt-6-astra와 gpt-6.1-sol의 Ultrafast는 6배.
- **지역 데이터 레지던시 엔드포인트**: 2026년 3월 5일 이후 출시 모델에 10% 가산.
- **gpt-5.6-sol 가격은 프로모션가**다. API 가격 페이지는 2026년 11월 30일까지, Codex 가격 페이지는 "최소 11월 21일까지"로 적어 날짜가 다르다.

## 4. Gemini CLI (Google)

### 과금 경로

| 사용 방식 | 토큰 과금 |
|---|---|
| Code Assist Standard·Enterprise 라이선스 | 일일 모델 요청 수 한도(1,500회·2,000회)에서 차감. 토큰별 청구 없음 |
| 무료 Gemini API 키 | 하루 250회 요청, Flash 모델만. 무료 |
| 유료 Gemini API 키 | 아래 단가로 토큰별 청구 |
| Vertex AI | 토큰별 청구. Vertex AI 단가는 이 문서에서 따로 확인하지 않았다 |

개인 무료 사용자와 Google AI Pro·Ultra 구독자는 2026년 6월 18일에 Antigravity CLI로 이전되었다.

출처: [Gemini CLI 할당량·가격](https://geminicli.com/docs/resources/quota-and-pricing/),
[Gemini CLI README](https://github.com/google-gemini/gemini-cli/blob/main/README.md)

### 모델별 단가

유료 표준(Standard) 등급 기준이다. 출력 단가에는 사고 토큰이 포함된다.

| 모델 | 입력 | 출력 | 캐시된 입력 | 캐시 저장 (MTok·시간당) | 무료 등급 |
|---|---|---|---|---|---|
| Gemini 4 Carbon | $2.00 | $10.00 | $0.10 | 없음 | 없음 |
| Gemini 4 Argon | $2.00 | $10.00 | $0.10 | 없음 | 없음 |
| Gemini 4 Hydrogen | $1.50 | $7.50 | $0.075 | 없음 | 있음 |
| Gemini 3.8 Flash | $0.75 | $3.75 | $0.075 | $0.50 | 있음 |
| Gemini 3.6 Flash | $0.75 | $3.75 | $0.075 | $0.50 | 있음 |
| Gemini 3.5 Flash-Lite | $0.30 | $2.50 | $0.03 | $1.00 | 있음 (캐시 제외) |
| Gemini 3.1 Pro Preview (20만 토큰 이하) | $2.00 | $12.00 | $0.20 | $4.50 | 없음 |
| Gemini 3.1 Pro Preview (20만 토큰 초과) | $4.00 | $18.00 | $0.40 | $4.50 | 없음 |
| Gemini 3.1 Flash-Lite | $0.25 | $1.50 | $0.025 | $1.00 | 있음 (캐시 제외) |
| Gemini 3 Flash Preview | $0.50 | $3.00 | $0.05 | $1.00 | 있음 |
| Gemini 2.5 Pro (20만 토큰 이하, 지원 중단 예정) | $1.25 | $10.00 | $0.125 | $4.50 | 있음 (캐시 제외) |
| Gemini 2.5 Pro (20만 토큰 초과) | $2.50 | $15.00 | $0.25 | $4.50 | — |
| Gemini 2.5 Flash (지원 중단 예정) | $0.30 | $2.50 | $0.03 | $1.00 | 있음 (캐시 제외) |
| Gemini 2.5 Flash-Lite | $0.10 | $0.40 | $0.01 | $1.00 | 있음 (캐시 제외) |

- 3.1 Flash-Lite, 3 Flash Preview, 2.5 Flash, 2.5 Flash-Lite는 오디오 입력 단가가 따로 있고 더 비싸다. 표는 텍스트 입력 기준이다.
- Gemini 2.5 Flash와 2.5 Flash-Lite 행은 페이지 뒷부분을 요약으로만 읽은 수치다. 인용 전에 원문 확인이 필요하다.

출처: [Gemini API 가격](https://aistudio.google.com/docs/pricing)

### 예정된 가격 변경

| 모델 | 변경일 | 입력 | 출력 | 캐시된 입력 | 캐시 저장 |
|---|---|---|---|---|---|
| Gemini 4 Carbon, Argon | 2026-12-01 | $2 → $4 | $10 → $20 | $0.10 → $0.20 | — |
| Gemini 3.8 Flash, 3.6 Flash | 2027-01-01 | $0.75 → $1.50 | $3.75 → $7.50 | $0.075 → $0.15 | $0.50 → $1.00 |

2026년 12월 1일부터 Gemini 2.5 Pro 요청은 3.8 Flash로, 2.5 Flash 요청은 3.5 Flash-Lite로 자동 전환되어 그 모델 단가로 청구된다.

### 캐시 과금 규칙

- **Gemini 4 계열**: 암시적 캐싱만 지원한다. 캐시된 입력은 입력 단가의 5%이고, 캐시 쓰기 요금과 저장 요금이 없다.
- **Gemini 3.x 이하**: 캐시된 입력은 입력 단가의 10%다. 캐시를 보관하는 동안 저장 요금을 토큰 100만 개·시간당으로 낸다.

### 단가를 바꾸는 조건

- **Batch·Flex**: 표준의 50%.
- **Priority**: 표준의 약 1.8배(Gemini 4 Hydrogen $2.70/$13.50).
- **긴 컨텍스트 할증**: Gemini 3.1 Pro Preview와 2.5 Pro에만 있다(20만 토큰 초과). Gemini 4 계열에는 없다.

## 5. 세 에이전트의 차이

| 항목 | Claude Code | Codex | Gemini CLI |
|---|---|---|---|
| 출력/입력 비율 (최신 주력 모델) | 5배 | 5배 | 5배 |
| 캐시 읽기/입력 비율 | 2.5~10% | 5~10% | 5~10% |
| 캐시 쓰기 요금 | 입력의 1.25배(5분), 2배(1시간) | API: 1.25배(30m), 2.5배(12h). 크레딧: 없음 | Gemini 4: 없음 |
| 캐시 저장 요금 | 없음 | 없음 | Gemini 3.x 이하만 시간당 부과 |
| 긴 입력 할증 | Haiku 5.5만 10만 토큰 초과 시 5배 | 27.2만 토큰 초과 시 입력 2배, 출력 1.5배 | Gemini 4 없음. 3.1 Pro는 20만 토큰 초과 시 입력 2배 |
| 사고 토큰 | 출력으로 청구 | 이 문서에서 확인하지 않음 | 출력 단가에 포함 |
| 구독 사용 시 단위 | 5시간·주간 한도 | 5시간당 메시지 수, 초과분은 크레딧 | 일일 요청 수 |

## 6. 확인이 필요한 항목

- **Codex 크레딧의 달러 가격**: 공식 페이지에 없다. $0.04는 API 단가와 맞춰 본 추정이다.
- **OpenAI "30m write", "12h write"**: 가격표에 열로 표시되어 있으나 적용 조건을 본문에서 확인하지 못했다.
- **Codex에서 고를 수 있는 모델**: ChatGPT 로그인 시 GPT-6.1 Sol·Terra, GPT-6 Sol·Luna·Astra가 한도 표에 나온다.
  API 키로는 키에 허용된 API 모델을 따른다고만 적혀 있다.
- **Gemini 2.5 Flash, 2.5 Flash-Lite 단가**: 요약으로만 읽었다.
- **Vertex AI 경유 Gemini 단가**: 확인하지 않았다.
- **Claude 가격 페이지 뒷부분**: 모델 표, 배치, 빠른 모드, 긴 컨텍스트 절까지 원문으로 읽었고 그 뒤는 사용하지 않았다.

## 7. 출처

### Anthropic

- [Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing)
- [Claude Code 비용 관리](https://code.claude.com/docs/en/costs)
- [Pro·Max 요금제에서 Claude Code 사용](https://support.claude.com/en/articles/11145838-using-claude-code-with-your-pro-or-max-plan)
- [유료 요금제의 사용 크레딧](https://support.claude.com/en/articles/12429409-extra-usage-for-paid-claude-plans)

### OpenAI

- [Codex 가격](https://learn.chatgpt.com/docs/pricing)
- [OpenAI API 가격](https://developers.openai.com/api/docs/pricing)

### Google

- [Gemini API 가격](https://aistudio.google.com/docs/pricing)
- [Gemini CLI 할당량·가격](https://geminicli.com/docs/resources/quota-and-pricing/)
- [Gemini CLI README](https://github.com/google-gemini/gemini-cli/blob/main/README.md)
