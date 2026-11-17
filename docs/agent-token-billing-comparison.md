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
  - OpenAI API: GPT-5.6 이후 모델은 캐시에 쓸 때 입력의 1.25배(30분 보관) 또는 2.5배(12시간 보관)를 낸다. Codex 크레딧에는 캐시 쓰기 요금이 없다.
  - Gemini: Gemini 4 계열은 캐시 쓰기 요금과 저장 요금이 없다. 3.x 이하 모델은 명시적 캐시를 만들면 저장 요금을 시간당으로 낸다.
- **캐시 쓰기 단가는 입력 단가에 더해지는 것이 아니라 입력 단가를 대신한다.** 입력 토큰 하나는 일반 입력, 캐시 쓰기,
  캐시 읽기 중 하나로만 청구된다. 회사별 적용 규칙은 [5절](#5-입력-단가와-캐시-쓰기-단가의-적용-규칙)에 정리했다.
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

### 5분과 1시간은 누가 정하는가

캐시 보관 시간(TTL)은 요청을 보내는 쪽이 요청마다 지정한다. Claude Code에서는 Claude Code가 자동으로 고르고, 사용자가 설정으로 바꿀 수 있다.

| 요청 종류 | 구독 포함 사용량 안 | 사용 크레딧, API 키, 클라우드 제공사 |
|---|---|---|
| 메인 대화 | 1시간 | 5분 |
| 그 밖의 요청(하위 에이전트, 컨텍스트 압축 등) | 5분 | 5분 |

- 구독 포함 사용량 안에서는 토큰별 청구가 없으므로 1시간 캐시 쓰기 단가(2배)를 따로 내지 않는다.
  한도를 넘어 사용 크레딧으로 넘어가면 Claude Code가 메인 대화를 5분으로 낮춘다.
- 사용자가 바꾸려면 `promptCacheTtl` 설정이나 `CLAUDE_CODE_PROMPT_CACHE_TTL` 환경 변수에 `5m` 또는 `1h`를 넣는다.
  하위 에이전트 등은 `subagentPromptCacheTtl` 설정이나 `CLAUDE_CODE_SUBAGENT_PROMPT_CACHE_TTL`로 따로 정한다.
- API를 직접 호출할 때는 `cache_control`의 `ttl` 필드에 `"1h"`를 넣어야 1시간이 되고, 넣지 않으면 5분이다.
- 캐시를 읽을 때마다 보관 시간이 추가 요금 없이 다시 시작된다.

출처: [Claude Code 프롬프트 캐싱](https://code.claude.com/docs/en/prompt-caching.md),
[Claude Code 환경 변수](https://code.claude.com/docs/en/env-vars.md),
[Claude API 프롬프트 캐싱](https://platform.claude.com/docs/en/build-with-claude/prompt-caching.md)

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
- **캐시 쓰기**: GPT-5.6 이후 모델은 캐시에 쓰는 토큰을 입력의 1.25배(30분 보관, 기본값) 또는 2.5배(12시간 보관)로 청구한다.
  GPT-5.5 이전 모델에는 캐시 쓰기 요금이 없다. 적용 조건은 [5.2절](#52-openai-codex)에 있다.

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
| Vertex AI | 토큰별 청구. 글로벌 엔드포인트의 Gemini 4 단가는 Gemini API와 같고, 그 밖의 리전은 약 10% 비싸다 ([Vertex AI 모델 가격](https://cloud.google.com/model-garden/pricing)) |

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
- **Gemini 3.x 이하**: 캐시된 입력은 입력 단가의 10%다. 명시적 캐시를 만들어 보관하는 동안 저장 요금을 토큰 100만 개·시간당으로 낸다.
- 적용 조건은 [5.3절](#53-google-gemini-cli)에 있다.

### 단가를 바꾸는 조건

- **Batch·Flex**: 표준의 50%.
- **Priority**: 표준의 약 1.8배(Gemini 4 Hydrogen $2.70/$13.50).
- **긴 컨텍스트 할증**: Gemini 3.1 Pro Preview와 2.5 Pro에만 있다(20만 토큰 초과). Gemini 4 계열에는 없다.

## 5. 입력 단가와 캐시 쓰기 단가의 적용 규칙

세 회사 모두 **입력 토큰 하나를 한 가지 단가로만 청구한다.** 캐시 쓰기 단가는 입력 단가에 얹는 추가 요금이 아니다.

| 토큰의 상태 | Claude | OpenAI (GPT-5.6 이후) | Gemini 4 |
|---|---|---|---|
| 캐시와 무관한 토큰 | 입력 단가 (1배) | 입력 단가 (1배) | 입력 단가 (1배) |
| 이번 요청에서 처음 캐시에 저장되는 토큰 | 캐시 쓰기 단가 (1.25배 또는 2배) | 캐시 쓰기 단가 (1.25배 또는 2.5배) | 입력 단가 (1배). 쓰기 요금 없음 |
| 이전 요청에서 저장된 것을 다시 쓰는 토큰 | 캐시 읽기 단가 (0.025~0.1배) | 캐시된 입력 단가 (0.05~0.1배) | 캐시된 입력 단가 (0.05배) |
| 캐시가 켜지는 방식 | 요청에 `cache_control`을 넣어야 켜짐. Claude Code는 기본으로 켬 | 기본으로 켜짐 | 기본으로 켜짐 |
| 캐시 가능한 최소 길이 | 모델별 512~4,096토큰 | 1,024토큰 | 4,096토큰 |

같은 앞부분(prefix)을 두 번 보낼 때 그 앞부분에 드는 비용을 입력 단가의 배수로 나타내면 다음과 같다.

| 경우 | 첫 요청 | 둘째 요청 | 합계 | 캐시 없이 두 번 |
|---|---|---|---|---|
| Claude Sonnet 5.5 (5분 캐시) | 1.25배 | 0.05배 | 1.30배 | 2배 |
| Claude Sonnet 5.5 (1시간 캐시) | 2배 | 0.05배 | 2.05배 | 2배 |
| OpenAI gpt-6.1-sol (30분 캐시) | 1.25배 | 0.05배 | 1.30배 | 2배 |
| Codex 크레딧, GPT-6.1 Sol | 1배 | 0.05배 | 1.05배 | 2배 |
| Gemini 4 Carbon | 1배 | 0.05배 | 1.05배 | 2배 |

1시간·12시간 캐시는 세 번 이상 재사용해야 이득이 난다. 에이전트는 같은 앞부분을 수십 번 다시 보내므로 실제로는 캐시 읽기 단가가 비용을 좌우한다.

### 5.1 Anthropic (Claude Code)

- **토큰 분류**: API 응답의 사용량이 세 필드로 나뉘고, 전체 입력은 셋의 합이다.
  - `input_tokens`: 마지막 캐시 지점 뒤에 있는 토큰. 입력 단가로 청구된다.
  - `cache_creation_input_tokens`: 이번에 캐시에 새로 쓴 토큰. 쓰기 단가로 청구된다.
  - `cache_read_input_tokens`: 캐시에서 읽은 토큰. 읽기 단가로 청구된다.
- **켜는 방법**: Messages API는 요청에 `cache_control`을 넣어야 캐싱한다. 최상위에 한 번 넣으면 자동으로 마지막 블록까지 캐싱하고,
  블록마다 직접 넣으면 지점을 최대 4개까지 정할 수 있다. Claude Code는 사용자가 끄지 않는 한 항상 캐싱한다.
- **캐시 범위**: 도구 정의, 시스템 프롬프트, 메시지 순서로 앞부분 전체가 캐시된다. 앞부분이 한 글자라도 바뀌면 그 뒤가 전부 다시 쓰기 단가로 청구된다.
- **최소 길이**: 모델에 따라 512, 1,024, 2,048, 4,096토큰이다. 이보다 짧으면 오류 없이 캐싱 없이 처리된다.
- **출력 토큰**: 캐시되지 않고 할인도 없다. 이전 턴의 사고 블록은 다음 턴에 입력으로 들어가면서 캐시 대상이 된다.
- **Claude Code에서 캐시가 깨져 다시 쓰기가 발생하는 경우**: 모델 전환, 대부분 모델에서의 노력 수준 변경, 빠른 모드 전환,
  MCP 서버 연결·해제, 컨텍스트 압축, 보관 시간을 넘긴 대기, Claude Code 업그레이드.
  세션 중 CLAUDE.md 편집, 권한 모드 변경, 하위 에이전트 실행은 캐시를 깨지 않는다.
- **확인 방법**: `/usage`가 캐시 읽기·쓰기 토큰 수와 "입력 토큰 중 캐시에서 온 비율"을 보여 준다.
- **배치 API**: 50% 할인이 캐시 배수와 함께 적용된다. 배치에서는 캐시 적중이 보장되지 않는다.

출처: [Claude API 프롬프트 캐싱](https://platform.claude.com/docs/en/build-with-claude/prompt-caching.md),
[Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing),
[Claude Code 프롬프트 캐싱](https://code.claude.com/docs/en/prompt-caching.md),
[Claude Code 비용 문서](https://code.claude.com/docs/en/costs)

### 5.2 OpenAI (Codex)

- **쓰기 단가는 입력 단가를 대신한다.** 공식 가이드가 "입력 위에 얹는 추가 요금이 아니라 전체 쓰기 단가"라고 명시한다.
  한 번 쓰고 한 번 전부 재사용하면 일반 입력 비용의 1.35배이고, 캐시 없이 두 번 처리하면 2배라는 예시도 실려 있다.
- **토큰 분류** (GPT-5.6 이후):
  - 아직 캐시되지 않은 앞부분 중 캐시 지점까지: 쓰기 단가.
  - 이미 캐시된 앞부분: 캐시된 입력 단가. 대부분 입력의 10%, GPT-6.1 Sol과 Terra는 5%.
  - 마지막 캐시 지점 뒤의 내용: 입력 단가. 쓰기 요금 없음.
- **기본으로 켜져 있고 기본값에서도 쓰기 요금이 나온다.** 캐시 관련 매개변수를 넣지 않으면 마지막 메시지 끝에 캐시 지점이 자동으로 놓이고,
  캐시되지 않은 앞부분이 1.25배로 청구된다.
- **보관 시간 선택**: `prompt_cache_options.ttl`에 `"30m"`(기본값) 또는 `"12h"`를 넣는다. `"12h"`는 Responses API에서만 쓸 수 있다.
  `mode`를 `"explicit"`으로 하고 캐시 지점을 두지 않으면 캐싱도 쓰기 요금도 생기지 않는다.
- **읽을 때 갱신**: 캐시를 읽으면 30분 보관이 무료로 연장된다. 12시간 보관은 읽어도 다시 시작되지 않는다.
- **GPT-5.5 이전 모델**: 캐시 쓰기 요금이 없다. 처음 보낼 때는 입력 단가, 적중하면 캐시된 입력 단가다.
- **사용량 필드**: `input_tokens` 안에 `cached_tokens`와 `cache_write_tokens`가 겹치지 않게 들어 있다.
  일반 입력 토큰 수는 `input_tokens`에서 이 둘을 뺀 값이다.
- **Codex**:
  - ChatGPT 로그인(크레딧): 캐시 쓰기 요금이 없다. 처음 보내는 토큰은 입력 크레딧 단가로 차감된다.
  - API 키: API 가격을 따르므로 GPT-5.6 이후 모델에서 쓰기 단가가 적용된다.
  - Codex CLI가 요청하는 보관 시간은 문서에 없다. 소스 코드상으로는 캐시 옵션을 보내지 않아 API 기본값(30분, 1.25배)이 적용되는 것으로 보인다.
    이는 문서가 아니라 [저장소 코드](https://github.com/openai/codex)를 읽은 결과다.

출처: [OpenAI 프롬프트 캐싱 가이드](https://developers.openai.com/api/docs/guides/prompt-caching),
[OpenAI API 가격](https://developers.openai.com/api/docs/pricing),
[Responses API 레퍼런스](https://developers.openai.com/api/reference/resources/responses/methods/create),
[Codex 가격](https://learn.chatgpt.com/docs/pricing)

### 5.3 Google (Gemini CLI)

Gemini에는 캐싱 방식이 두 가지 있다.

| 방식 | 지원 모델 | 처음 보낼 때 | 재사용할 때 | 저장 요금 | 할인 보장 |
|---|---|---|---|---|---|
| 암시적 캐싱 | Gemini 2.5 이후 전부. 기본으로 켜짐 | 입력 단가 | 캐시된 입력 단가 | 없음 | 없음. 적중하면 자동 할인 |
| 명시적 캐싱 | Gemini 3.x, 2.5. Gemini 4는 미지원 | 입력 단가 | 캐시된 입력 단가 | 토큰 100만 개·시간당 부과 | 있음 |

- **처음 보낼 때의 단가**: Gemini API 문서에는 명시되어 있지 않다. Vertex AI 문서가 "암시적·명시적 캐시를 만드는 데 쓴 입력 토큰은
  표준 입력 단가로 청구되고, 암시적 캐싱에는 별도 저장 요금이 없다"고 적고 있다.
- **암시적 캐싱은 적중이 보장되지 않는다.** 공통 내용을 프롬프트 앞쪽에 두고 짧은 간격으로 보내야 적중 확률이 오른다.
- **Gemini 4의 다중 턴 조건**: 새 요청이 2,050토큰 이상을 추가해야 직전 요청 전체가 캐시된다.
  그보다 적게 추가하면 직전 요청의 마지막 2,050토큰이 표준 입력 단가로 다시 처리된다.
- **명시적 캐싱**: API 호출로 캐시를 만들고 이후 요청에서 참조한다. 보관 시간을 지정하지 않으면 1시간이고, 그 시간만큼 저장 요금이 붙는다.
  명시적 캐시를 쓰는 요청에는 암시적 캐싱이 적용되지 않는다.
- **사용량 필드**: `promptTokenCount`가 전체 프롬프트 크기이고, 그중 캐시된 부분이 `cachedContentTokenCount`다.
- **Gemini CLI**: Gemini API 키 또는 Vertex AI로 인증할 때만 토큰 캐싱이 동작한다.
  Google 계정 로그인(Code Assist)에서는 지원하지 않는다. 캐시된 토큰 수는 `/stats`로 볼 수 있다.
  이 설명은 2025년 12월 1일자 문서이고, 암시적·명시적 중 어느 방식인지와 Gemini 4에서의 동작은 적혀 있지 않다.
- **Vertex AI의 예정된 변경**: 2027년 1월 12일부터 Gemini 4 Hydrogen 이후 모델에 캐시 쓰기 요금(기본 입력의 1.25배)이 생긴다.
  가격표에는 Hydrogen 캐시 쓰기 $1.875가 올라와 있고, Carbon과 Argon은 "N/A"다. Gemini API 쪽 문서에는 이런 예고가 없다.

출처: [Gemini API 캐싱 가이드](https://aistudio.google.com/docs/caching),
[Gemini API 가격](https://aistudio.google.com/docs/pricing),
[Vertex AI 컨텍스트 캐시 개요](https://docs.cloud.google.com/gemini-enterprise-agent-platform/models/context-cache/context-cache-overview),
[Vertex AI 모델 가격](https://cloud.google.com/model-garden/pricing),
[Gemini CLI 토큰 캐싱](https://geminicli.com/docs/cli/token-caching/),
[generateContent API 레퍼런스](https://aistudio.google.com/api-reference/generate-content)

## 6. 세 에이전트의 차이

| 항목 | Claude Code | Codex | Gemini CLI |
|---|---|---|---|
| 출력/입력 비율 (최신 주력 모델) | 5배 | 5배 | 5배 |
| 캐시 읽기/입력 비율 | 2.5~10% | 5~10% | 5~10% |
| 캐시 쓰기 요금 | 입력의 1.25배(5분), 2배(1시간) | API(GPT-5.6 이후): 1.25배(30분), 2.5배(12시간). 크레딧: 없음 | Gemini 4: 없음 |
| 캐시 저장 요금 | 없음 | 없음 | Gemini 3.x 이하 명시적 캐시만 시간당 부과 |
| 캐시가 켜지는 방식 | Claude Code가 기본으로 켬 | 기본으로 켜짐 | API 키·Vertex AI 인증에서만 동작 |
| 긴 입력 할증 | Haiku 5.5만 10만 토큰 초과 시 5배 | 27.2만 토큰 초과 시 입력 2배, 출력 1.5배 | Gemini 4 없음. 3.1 Pro는 20만 토큰 초과 시 입력 2배 |
| 사고 토큰 | 출력으로 청구 | 이 문서에서 확인하지 않음 | 출력 단가에 포함 |
| 구독 사용 시 단위 | 5시간·주간 한도 | 5시간당 메시지 수, 초과분은 크레딧 | 일일 요청 수 |

## 7. 확인이 필요한 항목

- **Codex 크레딧의 달러 가격**: 공식 페이지에 없다. $0.04는 API 단가와 맞춰 본 추정이다.
- **Codex CLI가 요청하는 캐시 보관 시간**: 공식 문서에 없다. 소스 코드로만 확인했다.
- **Gemini API에서 캐시를 처음 만들 때의 단가**: Gemini API 문서에 없어 Vertex AI 문서의 설명을 따랐다.
- **Gemini CLI의 캐싱 방식**: 문서가 2025년 12월자이고 Gemini 4에서의 동작을 설명하지 않는다.
- **Claude 배치 API의 캐시 쓰기·읽기 단가**: 할인이 함께 적용된다고만 적혀 있고 금액 표는 없다.
- **Codex에서 고를 수 있는 모델**: ChatGPT 로그인 시 GPT-6.1 Sol·Terra, GPT-6 Sol·Luna·Astra가 한도 표에 나온다.
  API 키로는 키에 허용된 API 모델을 따른다고만 적혀 있다.
- **Gemini 2.5 Flash, 2.5 Flash-Lite 단가**: 요약으로만 읽었다.
- **Vertex AI 경유 Gemini 단가**: Gemini 4와 3.8 Flash, 3.1 Pro의 글로벌 단가가 Gemini API와 같다는 것만 확인했다. 글로벌이 아닌 리전은 약 10% 비싸다.
- **Claude 가격 페이지 뒷부분**: 모델 표, 배치, 빠른 모드, 긴 컨텍스트 절까지 원문으로 읽었고 그 뒤는 사용하지 않았다.

## 8. 출처

### Anthropic

- [Claude API 가격](https://platform.claude.com/docs/en/about-claude/pricing)
- [Claude Code 비용 관리](https://code.claude.com/docs/en/costs)
- [Pro·Max 요금제에서 Claude Code 사용](https://support.claude.com/en/articles/11145838-using-claude-code-with-your-pro-or-max-plan)
- [유료 요금제의 사용 크레딧](https://support.claude.com/en/articles/12429409-extra-usage-for-paid-claude-plans)
- [Claude API 프롬프트 캐싱](https://platform.claude.com/docs/en/build-with-claude/prompt-caching.md)
- [Claude Code 프롬프트 캐싱](https://code.claude.com/docs/en/prompt-caching.md)
- [Claude Code 환경 변수](https://code.claude.com/docs/en/env-vars.md)

### OpenAI

- [Codex 가격](https://learn.chatgpt.com/docs/pricing)
- [OpenAI API 가격](https://developers.openai.com/api/docs/pricing)
- [OpenAI 프롬프트 캐싱 가이드](https://developers.openai.com/api/docs/guides/prompt-caching)
- [Responses API 레퍼런스](https://developers.openai.com/api/reference/resources/responses/methods/create)
- [Codex 저장소](https://github.com/openai/codex) (보관 시간 기본값 확인용, 공식 문서 아님)

### Google

- [Gemini API 가격](https://aistudio.google.com/docs/pricing)
- [Gemini CLI 할당량·가격](https://geminicli.com/docs/resources/quota-and-pricing/)
- [Gemini CLI README](https://github.com/google-gemini/gemini-cli/blob/main/README.md)
- [Gemini API 캐싱 가이드](https://aistudio.google.com/docs/caching)
- [generateContent API 레퍼런스](https://aistudio.google.com/api-reference/generate-content)
- [Gemini CLI 토큰 캐싱](https://geminicli.com/docs/cli/token-caching/)
- [Vertex AI 컨텍스트 캐시 개요](https://docs.cloud.google.com/gemini-enterprise-agent-platform/models/context-cache/context-cache-overview)
- [Vertex AI 모델 가격](https://cloud.google.com/model-garden/pricing)
