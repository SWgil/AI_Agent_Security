# Claude Code 권한 체계: 무엇이 거절되고, 무엇을 묻고, 무엇을 설정할 수 있는가

- 조사 기준일: 2026-11-18
- 근거: Claude Code 공식 문서(`code.claude.com/docs`). 각 절 끝의 대괄호 약어는 [출처](#출처) 목록을 가리킨다.
- 공식 문서에서 확인하지 못했거나 페이지끼리 서로 다른 내용은 [미확인·불일치 항목](#미확인불일치-항목)에 따로 모았다.

## 요약

- 도구 호출의 허용·질문·거절은 모델이 아니라 Claude Code 실행기(하니스)가 호출 직전에 판정한다. [P]
- 판정 재료는 네 가지다: 권한 규칙(`allow`/`ask`/`deny`), 권한 모드, 훅, 샌드박스.
- 규칙 평가는 항상 deny → ask → allow 순서이고, 어느 설정 파일에서 온 deny든 다른 곳의 allow가 뒤집지 못한다. [P]
- 사용자가 바꿀 수 없는 것은 두 종류다.
  - 제품에 내장된 동작: 보호 경로, 중요 경로 `rm` 확인, 네트워크 경로 읽기 확인, 작업 폴더 신뢰 절차 등.
  - 조직 관리자가 관리형 설정(managed settings)으로 잠근 항목.
- 공식 문서에는 "특정 명령은 무조건 차단"이라는 고정 차단 목록이 없다. 내장된 것은 대부분 "무조건 묻는다"이지 "무조건 거절한다"가 아니다. [PM]

## 1. 판정 흐름

도구 호출 하나가 들어오면 대략 다음 순서로 판정된다. [P][PM][H]

1. `PreToolUse` 훅이 종료 코드 2로 끝나면 권한 규칙을 보기 전에 호출이 중단된다.
2. deny 규칙에 걸리면 거절한다.
3. ask 규칙에 걸리면 사용자에게 묻는다. 훅이 `allow`를 돌려줘도 묻는다.
4. allow 규칙에 걸리면 실행한다.
5. 어느 규칙에도 안 걸리면 현재 권한 모드가 결정한다.
   - 수동 모드: 묻는다.
   - auto 모드: 분류기 모델이 판정한다.
   - `dontAsk` 모드: 거절한다.
   - `bypassPermissions` 모드: 실행한다.
6. 위 결과와 무관하게, [3.1절](#31-제품에-내장된-동작)의 "어떤 모드도 자동 승인하지 않는 동작"은 사용자 확인을 거친다.

## 2. 기본 동작: 설정이 없을 때

### 2.1 시작 모드가 버전과 실행 환경에 따라 다르다

| 실행 환경 | 시작 모드 |
|---|---|
| 대화형 터미널, VS Code (v2.1.283 이상) | `auto` |
| `claude -p`(비대화형), Agent SDK | `default`(수동) |
| `disableAutoMode`가 설정된 세션 | `default`(수동) |

아래 2.2의 표는 수동 모드 기준이다. [PM][SEC]

### 2.2 수동 모드에서 도구별 기본 동작

| 도구 | 기본 동작 | "다시 묻지 않음"의 유지 범위 |
|---|---|---|
| 파일 읽기, Grep 등 읽기 전용 | 작업 디렉터리 안에서는 묻지 않음 | 해당 없음 |
| Bash | 묻는다. 내장 읽기 전용 명령은 예외 | 저장소·명령 단위로 영구 |
| Edit / Write | 묻는다 | 세션이 끝날 때까지 |
| WebFetch | 묻는다. 사전 승인된 문서 도메인은 예외 | 저장소·도메인 단위로 영구 |
| WebSearch | 묻는다 | 저장소 단위로 영구 |
| MCP 도구 | 묻는다 | 문서에서 확인하지 못함 |

- 영구 승인은 git 저장소 루트의 `.claude/settings.local.json`에 allow 규칙으로 저장된다. [P][S]
- 묻지 않고 실행되는 내장 읽기 전용 Bash 명령: `ls`, `cat`, `echo`, `pwd`, `head`, `tail`, `grep`, `find`, `wc`, `which`, `diff`, `stat`, `du`, `cd`, 그리고 `git`의 읽기 전용 형태. [P]
  - 이 목록 자체는 바꿀 수 없다.
  - 다만 ask나 deny 규칙을 추가하면 그 규칙이 우선한다.

### 2.3 수동 모드에서 추가로 묻는 경우

- 완전히 분석할 수 없는 Bash 명령은 실행 전에 묻는다. [SEC]
- 10,000자를 넘는 명령은 항상 묻는다. [P]
- 샌드박스에서 실패한 명령을 샌드박스 밖에서 재시도할 때 묻는다. [SB]

### 2.4 auto 모드에서 분류기가 기본으로 막는 것

auto 모드에서는 별도 분류기 모델이 사용자 대신 각 동작을 검토한다. 요청 범위를 넘어서는 동작, 알 수 없는 인프라를 겨냥한 동작, 악의적 콘텐츠에 유도된 것으로 보이는 동작을 막는다. [PM]

| 구분 | 예시 |
|---|---|
| 기본 차단 | `curl \| bash`, 민감 데이터의 외부 전송, 프로덕션 배포·마이그레이션, 강제 푸시, `git reset --hard`, `git clean -fd`, `terraform destroy`, 승인되지 않은 PR 병합, 실제 자격 증명 출력 |
| 기본 허용 | 작업 디렉터리 안의 파일 작업, lock 파일 기반 의존성 설치, 읽기 전용 HTTP 요청, 현재 저장소 브랜치로의 푸시(`production`, `gh-pages` 같은 배포성 브랜치 제외) |

- 분류기는 권한 규칙 다음에 동작한다. 읽기 전용 동작과 작업 디렉터리 안의 편집은 분류기를 거치지 않고 승인된다.
- auto 모드에 들어가면 지나치게 넓은 allow 규칙은 무시된다: `Bash(*)`, `Bash(python*)` 같은 인터프리터 와일드카드, `Agent` allow 규칙. `Bash(npm test)` 같은 좁은 규칙은 유지된다. [PM][AMC]
- 연속 3회 또는 누적 20회 차단되면 auto 모드가 멈추고 사용자에게 묻는다. 이 임계값은 바꿀 수 없다.
- 분류기가 판정을 내리지 못하면 해당 동작은 차단된다.

## 3. 사용자가 설정할 수 없는 부분

### 3.1 제품에 내장된 동작

**어떤 모드도 자동 승인하지 않는 동작** — auto와 `bypassPermissions`에서도 사용자에게 묻는다. [PM][P]

- 명시적 ask 규칙에 걸린 도구 호출
- `AskUserQuestion`, 그리고 `requiresUserInteraction`으로 표시된 MCP 도구
- 조직이 `ask`로 지정한 커넥터 도구
- 중요 경로에 대한 `rm` / `rmdir`
  - allow 규칙이나 `PreToolUse` 훅의 `allow`로 승인되지 않는다.
  - `bypassPermissions`에서만 별도 설정으로 이 확인을 끌 수 있다.
- 네트워크 경로(UNC, `/net`) 읽기: allow 규칙, 훅의 `allow`, auto 모드 모두 이 확인을 건너뛰지 못한다.

**보호 경로** — 아래 경로에 대한 쓰기는 allow 규칙과 별개로 모드에 따라 처리된다. [PM]

- 디렉터리: `.git`, `.config/git`, `.vscode`, `.idea`, `.husky`, `.cargo`, `.devcontainer`, `.yarn`, `.mvn`, `.claude`(일부 예외 있음)
- 파일: `.bashrc`, `.bash_profile`, `.zshrc`, `.zprofile`, `.profile`, `.envrc`, `.gitconfig`, `.gitmodules`, `.npmrc`, `.yarnrc`, `.mcp.json`, `.claude.json`, `.bazelrc`, `.pre-commit-config.yaml` 등

| 모드 | 보호 경로 쓰기 |
|---|---|
| `default`, `acceptEdits` | 묻는다 |
| `auto` | 분류기가 판정 |
| `dontAsk` | 거절 |
| `bypassPermissions` | 허용 |

**작업 폴더 신뢰 절차** [P][SEC]

- 프로젝트 `.claude/settings.json`의 `permissions.allow`와 `additionalDirectories`는 사용자가 신뢰 대화상자를 수락한 뒤에만 적용된다.
- 같은 파일의 `deny`와 `ask`는 신뢰 여부와 무관하게 즉시 적용된다.
- `claude -p`와 SDK 실행에서는 대화상자가 뜨지 않으므로 프로젝트 allow 규칙이 적용되지 않는다.
- 프로젝트 `.mcp.json`의 MCP 서버는 대화형 세션에서 승인을 받아야 연결된다. `claude -p`는 묻지 않고 연결한다.

**그 밖의 고정 동작**

- 프로젝트 설정 파일에 적은 `defaultMode: "auto"`와 `"bypassPermissions"`는 무시된다. 저장소가 사용자 몰래 권한을 넓히지 못하게 하기 위한 것이다. [PM][S]
- `autoMode` 설정도 프로젝트 설정 파일에서는 읽지 않는다. [AMC]
- Linux와 macOS에서 root 또는 sudo로 실행하면 `--dangerously-skip-permissions`가 거부된다. 인식된 샌드박스 안에서는 예외다. [PM]
- 클라우드 세션은 설정 파일의 `defaultMode: "bypassPermissions"`와 `"dontAsk"`를 무시한다. [PM]
- 내장 읽기 전용 Bash 명령 목록, auto 모드의 일시 중지 임계값(3회/20회)은 조정할 수 없다. [P][PM]
- 훅은 deny·ask 규칙을 뒤집지 못한다. 자세한 내용은 [4.6절](#46-훅). [P][H]

### 3.2 조직 관리자가 잠그는 부분 (관리형 설정)

관리형 설정은 모든 설정 중 우선순위가 가장 높고, 일반 사용자가 덮어쓸 수 없다. [MS][S]

**배포 위치**

| 방식 | 위치 |
|---|---|
| macOS 파일 | `/Library/Application Support/ClaudeCode/managed-settings.json` |
| Linux / WSL 파일 | `/etc/claude-code/managed-settings.json` |
| Windows 파일 | `C:\Program Files\ClaudeCode\managed-settings.json` |
| Windows 레지스트리 | `HKLM\SOFTWARE\Policies\ClaudeCode`의 `Settings` 값 |
| 추가 조각 파일 | `managed-settings.d/*.json` |
| 서버 관리형 | claude.ai 관리자 콘솔 |
| MDM | Jamf, Intune, 그룹 정책 등 |

**관리형 소스에서만 설정할 수 있는 키** [MS]

| 키 | 효과 |
|---|---|
| `allowManagedPermissionRulesOnly` | 관리형 설정의 권한 규칙만 적용. 사용자·프로젝트·로컬 규칙과 `--allowedTools`를 무시하고, 프롬프트의 "항상 허용" 선택지도 숨긴다 |
| `allowManagedHooksOnly` | 실행 가능한 훅을 관리형으로 제한 |
| `allowManagedMcpServersOnly` | 관리형 `allowedMcpServers`만 적용 |
| `managedMcpServers` | 모든 사용자에게 MCP 서버 제공 |
| `strictKnownMarketplaces` / `blockedMarketplaces` | 플러그인 마켓플레이스 허용 목록 / 차단 목록 |
| `disableSideloadFlags` | `--plugin-dir`, `--plugin-url`, `--agents`, `--mcp-config` 거부 |
| `sandbox.network.allowManagedDomainsOnly` | 관리형 허용 도메인만 통과. 그 외 도메인은 묻지 않고 차단 |
| `sandbox.filesystem.allowManagedReadPathsOnly` | 관리형 `allowRead`만 적용 |
| `forceRemoteSettingsRefresh` | 원격 설정을 받아올 때까지 시작을 막음 |

**어느 파일에서든 설정할 수 있지만 관리자가 넣으면 잠금이 되는 키** [P][S]

| 키 | 효과 |
|---|---|
| `permissions.disableBypassPermissionsMode` | `bypassPermissions` 모드 진입 금지 |
| `disableAutoMode` | auto 모드 제거 |
| `allowedMcpServers` / `deniedMcpServers` | 추가 가능한 MCP 서버 허용 목록 / 차단 목록 |
| `permissions.denyInsteadOfPrompting` | 묻는 대신 거절(무인 실행용) |

**참고 사항**

- 관리형 설정이 `allowUnsandboxedCommands: false`나 `allowManagedDomainsOnly`를 지정하면, 저장소 설정으로 샌드박스를 느슨하게 만드는 항목(`excludedCommands`, `allowedDomains`, `allowWrite`)이 무시된다. [SB]
- 관리형 deny 규칙은 `--allowedTools`로도, 훅의 `allow`로도 뒤집을 수 없다. [P]
- 관리형 설정 파일을 파싱할 수 없으면 Claude Code가 시작되지 않는다. [MS]
- 기기에 설치한 `managed-settings.json`과 MDM 프로필은 클라우드 세션에 적용되지 않는다. 서버 관리형 설정만 적용된다. [S][CW]

## 4. 사용자가 설정할 수 있는 부분과 방식

### 4.1 설정 파일과 우선순위

| 우선순위 | 범위 | 위치 |
|---|---|---|
| 1 (최상) | 관리형 | 3.2절 참고 |
| 2 | CLI 인자 | `--settings`, `--permission-mode` 등 |
| 3 | 프로젝트 로컬(개인용) | `.claude/settings.local.json` |
| 4 | 프로젝트 공유(커밋됨) | `.claude/settings.json` |
| 5 | 사용자 | `~/.claude/settings.json` |

- 단일 값 키는 우선순위가 높은 쪽이 이긴다.
- `permissions.allow` / `ask` / `deny` 같은 목록 키는 모든 범위의 항목이 합쳐진다. [S]
- 합쳐진 뒤에도 deny → ask → allow 순서가 적용된다. 사용자 설정의 deny는 프로젝트 설정의 allow를 막고, 로컬 allow는 프로젝트나 관리형 ask를 이기지 못한다. [P][S]
- 대화 중에는 `/permissions` 명령으로 모든 규칙과 출처 파일을 보고, 규칙을 추가·삭제할 수 있다. [P]

### 4.2 권한 규칙

```json
{
  "permissions": {
    "allow": ["Bash(npm run test *)", "Edit(src/**)"],
    "ask":   ["Bash(git push *)"],
    "deny":  ["Read(./.env)", "WebFetch(domain:example.com)", "Agent(Explore)"],
    "additionalDirectories": ["../shared-lib"],
    "defaultMode": "acceptEdits"
  }
}
```

**공통** [P]

- 형식은 `도구` 또는 `도구(지정자)`다.
- deny에 도구 이름만 쓰면 그 도구가 모델의 컨텍스트에서 아예 제거된다.
- deny와 ask에서는 입력 파라미터로도 매칭할 수 있다. 예: `Bash(run_in_background:true)`, `Agent(model:opus)`.

**Bash** [P]

- `*`는 공백을 포함한 임의 문자열에 매칭된다. `Bash(git log *)`는 `git log` 계열, `Bash(git *)`는 모든 git 명령이다.
- `Bash(ls *)`는 `ls`와 `ls -la`에 매칭되고 `lsof`에는 매칭되지 않는다. `Bash(ls*)`는 `lsof`에도 매칭된다.
- 끝에 붙는 `:*`는 ` *`와 같다. `Bash(ls:*)` = `Bash(ls *)`.
- 복합 명령(`&&`, `||`, `;`, `|`, `&`, 줄바꿈)은 하위 명령 전부가 허용되어야 묻지 않고 실행된다. deny와 ask는 `$(...)`나 서브셸 안의 하위 명령에도 적용된다.
- `timeout`, `time`, `nice`, `nohup`, `xargs` 같은 래퍼는 벗겨낸 뒤 매칭한다. `npx`, `docker exec`, `direnv exec`는 벗겨내지 않는다.
- 리디렉션 대상도 검사한다. `> file`은 Edit 규칙과 보호 경로로, `< file`은 Read 규칙으로 확인한다.
- 한계: `Bash(curl *)`를 deny해도 `/usr/bin/curl`이나 `sh -c 'curl ...'`는 막지 못한다. 공식 문서도 인자를 제약하는 Bash 패턴을 "깨지기 쉽다"고 표현하며, 네트워크 통제는 `WebFetch(domain:...)` 규칙과 샌드박스로 하라고 권한다.

**Read / Edit 경로** — gitignore 문법을 쓴다. [P]

| 패턴 | 기준 | 예 |
|---|---|---|
| `//path` | 파일시스템 루트 기준 절대 경로 | `Read(//Users/alice/secrets/**)` |
| `~/path` | 홈 디렉터리 | `Read(~/Documents/*.pdf)` |
| `/path` | 설정 파일 위치 기준 | 프로젝트 설정이면 프로젝트 루트 |
| `path`, `./path` | 현재 디렉터리 | `Read(./.env)` |

- `/Users/alice/file`처럼 슬래시 하나로 시작하면 절대 경로가 아니다.
- `Read` deny는 같은 경로의 Edit과 Write도 막는다.
- `Write(...)`, `Glob(...)`, `Grep(...)` 형태의 경로 규칙은 매칭되지 않는다. `Edit(...)`이나 `Read(...)`를 써야 한다.
- 심볼릭 링크: allow는 요청 경로와 실제 경로가 모두 매칭되어야 하고, deny는 둘 중 하나만 매칭되어도 적용된다.

**WebFetch / MCP / 서브에이전트** [P]

- `WebFetch(domain:example.com)`은 그 호스트만, `WebFetch(domain:*.example.com)`은 하위 도메인만 매칭한다.
- MCP: `mcp__<서버>__<도구>`는 도구 하나, `mcp__<서버>__*` 또는 `mcp__<서버>`는 서버 전체다. deny에 `mcp__*`를 쓰면 모든 MCP 도구가 제거된다. allow에서는 `mcp__*`처럼 서버 이름 없는 와일드카드가 무시된다.
- 서브에이전트: `Agent(Explore)`, `Agent(my-custom-agent)` 형태로 deny할 수 있다.

**작업 디렉터리 범위** [P][SB]

- 기본 쓰기 허용 범위는 작업 디렉터리, `$TMPDIR`, 그리고 `--add-dir` / `/add-dir` / `permissions.additionalDirectories`로 추가한 경로다.
- `permissions.blockReadsOutsideWorkingDirectories`, `permissions.blockWritesOutsideWorkingDirectories`를 켜면 범위 밖 접근이 `bypassPermissions`를 포함한 모든 모드에서 거절된다.
- 파일 도구는 이 범위로 제한되지만, Bash는 샌드박스를 켜지 않으면 사용자 계정이 쓸 수 있는 곳 어디든 쓸 수 있다. [SEC]

### 4.3 권한 모드

| 모드 | 동작 |
|---|---|
| `default` (수동) | 도구를 처음 쓸 때 묻는다. 읽기만 묻지 않는다 |
| `acceptEdits` | 작업 디렉터리 안의 파일 편집과 `mkdir`, `touch`, `mv`, `cp`를 자동 승인 |
| `plan` | 읽기 전용 탐색. 편집 불가 |
| `auto` | 분류기가 사용자 대신 검토 |
| `dontAsk` | 물어야 할 동작을 전부 거절. 사전 승인된 도구와 디렉터리 안 읽기만 실행 |
| `bypassPermissions` | 프롬프트를 건너뛴다. deny 규칙과 3.1절의 강제 확인은 그대로 적용 |

**설정 방법** [PM][CLI]

- 설정 파일: `permissions.defaultMode`
- CLI: `--permission-mode <모드>` (설정 파일의 `defaultMode`보다 우선)
- 대화 중: Shift+Tab으로 `default` → `acceptEdits` → `plan` 순환. `dontAsk`는 순환에 없다.
- `--dangerously-skip-permissions`는 `--permission-mode bypassPermissions`와 같다.
- `--restricted`는 `bypassPermissions`를 거부한다.

### 4.4 auto 모드 분류기 조정

`autoMode` 설정은 사용자 설정(`~/.claude/settings.json`), 관리형 설정, `--settings`에서만 읽는다. [AMC]

| 키 | 용도 |
|---|---|
| `autoMode.environment` | 신뢰하는 저장소·버킷·도메인·서비스를 문장으로 기술 |
| `autoMode.allow` | 기본 차단 규칙의 예외 |
| `autoMode.soft_deny` | 사용자 의도가 명확하면 풀리는 차단 |
| `autoMode.hard_deny` | 사용자가 요청해도 막는 차단 |
| `autoMode.classifyAllShell` | auto·plan 모드에서 Bash allow 규칙을 중지하고 전부 분류기로 보냄 |

- 각 범위의 항목은 합쳐진다. 사용자는 관리형 항목에 추가만 할 수 있고 제거는 못 한다.
- `claude auto-mode defaults`로 기본 규칙을, `claude auto-mode config`로 현재 적용된 설정을 확인한다.

### 4.5 샌드박스

Bash 명령을 OS 수준에서 격리한다. 기본은 꺼져 있고 `/sandbox` 또는 `sandbox.enabled: true`로 켠다. macOS, Linux, WSL2에서 지원하며 네이티브 Windows에서는 격리 없이 실행된다. [SB]

| 키 | 용도 |
|---|---|
| `sandbox.enabled` | 샌드박스 사용 |
| `sandbox.failIfUnavailable` | 샌드박스를 못 쓰면 격리 없이 실행하는 대신 종료 |
| `sandbox.autoAllowBashIfSandboxed` | 샌드박스 안의 Bash를 묻지 않고 실행 |
| `sandbox.excludedCommands` | 샌드박스 밖에서 실행할 명령 |
| `sandbox.allowUnsandboxedCommands` | `false`면 샌드박스 밖 재시도를 금지 |
| `sandbox.filesystem.allowWrite` / `denyWrite` / `allowRead` / `denyRead` | 파일시스템 경로 제어 |
| `sandbox.network.allowedDomains` / `deniedDomains` | 네트워크 도메인 제어 |

**권한 규칙과의 관계** [SB][P]

- 샌드박스 설정과 권한 규칙이 합쳐져 최종 샌드박스 구성이 된다. `Edit` allow는 쓰기 허용, `Read`/`Edit` deny는 접근 차단, `WebFetch(domain:...)`은 도메인 허용에 반영된다.
- 샌드박스 자동 승인 상태에서도 Bash deny 규칙, 중요 경로 `rm` 확인, `Bash(git push *)` 같은 내용 지정 ask 규칙은 그대로 적용된다.
- 샌드박스에서 실패한 명령은 `dangerouslyDisableSandbox` 파라미터로 샌드박스 밖에서 재시도될 수 있다.
  - 수동·`acceptEdits`에서는 묻고, auto에서는 분류기가 판정하고, `dontAsk`에서는 거절한다.
  - ask 규칙 `Bash(dangerouslyDisableSandbox:true)`로 항상 묻게 하거나, `allowUnsandboxedCommands: false`로 아예 막을 수 있다.

### 4.6 훅

훅은 도구 호출마다 실행되는 사용자 스크립트로, 규칙으로 표현하기 어려운 판정을 프로그램으로 내린다. [H][P]

**`PreToolUse` 훅** — `hookSpecificOutput.permissionDecision`으로 결정을 돌려준다.

| 값 | 효과 |
|---|---|
| `allow` | 프롬프트를 건너뛴다. 3.1절의 강제 확인과 deny·ask 규칙은 건너뛰지 못한다 |
| `deny` | 호출을 막고 이유를 모델에 전달 |
| `ask` | 사용자에게 묻는다 |
| `defer` | 턴을 끝내고 나중에 재개. `claude -p`에서만 사용 |

- 훅이 여러 개면 `deny` > `defer` > `ask` > `allow` 순으로 우선한다.
- 종료 코드 2는 차단이다. 권한 규칙보다 먼저 적용되므로 allow 규칙이 있어도 막힌다.
- 종료 코드 1 등 다른 실패는 차단하지 않는다. 정책 강제에는 종료 코드 2를 써야 한다.
- 훅이 시간 초과되어도 차단되지 않고 일반 권한 흐름으로 넘어간다.
- 훅은 권한을 좁힐 수는 있어도 deny·ask 규칙을 넘어 넓힐 수는 없다.

**`PermissionRequest` 훅** — 권한 프롬프트가 뜨려는 시점에 실행되며 `decision.behavior`로 `allow` 또는 `deny`를 돌려준다. 이 훅에서는 종료 코드 2가 차단으로 처리되지 않는다.

### 4.7 CLI 플래그

| 플래그 | 용도 |
|---|---|
| `--allowedTools` | 묻지 않고 실행할 도구(allow 규칙) |
| `--disallowedTools` | deny 규칙. 도구 이름만 쓰면 컨텍스트에서 제거 |
| `--tools` | 존재하는 내장 도구 자체를 제한. MCP 도구에는 영향 없음 |
| `--permission-mode` | 시작 모드 |
| `--settings` | JSON 문자열이나 파일로 설정 주입 |
| `--add-dir` | 작업 디렉터리 추가 |
| `--permission-prompt-tool` | `claude -p`에서 권한 질문을 처리할 MCP 도구 지정 |

## 5. 보안 관점 정리

- **넓히기는 어렵고 좁히기는 쉽게 설계되어 있다.** deny는 어느 범위에서든 이기고, 훅은 차단은 할 수 있어도 deny를 풀지 못하며, 제한적인 값은 낮은 우선순위 범위에서 와도 존중되는 키가 여럿 있다.
- **저장소가 사용자 권한을 넓히는 경로가 막혀 있다.** 프로젝트 설정의 allow는 신뢰 수락 뒤에만 적용되고, `auto`·`bypassPermissions` 시작 모드와 `autoMode` 설정은 프로젝트 파일에서 읽지 않는다. 반면 프로젝트의 deny·ask는 즉시 적용된다.
- **Bash 규칙은 문자열 매칭이라 경계가 되지 못한다.** 인자를 제약하는 패턴은 우회할 수 있으므로, 실제 강제는 샌드박스(파일시스템·네트워크)와 `WebFetch(domain:...)` 규칙으로 해야 한다.
- **`bypassPermissions`도 전부를 풀지는 않는다.** deny 규칙, 중요 경로 `rm` 확인, 네트워크 경로 읽기 확인, 작업 디렉터리 밖 차단 설정은 유지된다. 다만 보호 경로 쓰기는 허용된다.
- **비대화형 실행은 동작이 다르다.** `claude -p`는 신뢰 대화상자가 없어 프로젝트 allow가 적용되지 않는 한편, 프로젝트 `.mcp.json` 서버는 묻지 않고 연결한다.

## 미확인·불일치 항목

공식 문서에서 확인하지 못했거나 페이지 간 표기가 다른 부분이다. 실제 적용 전에 사용 중인 버전에서 확인해야 한다.

| 항목 | 상태 |
|---|---|
| 모델 자체의 요청 거절 | 공식 문서에 설명이 없다. 이 문서는 하니스의 권한 체계만 다룬다 |
| 고정된 명령 차단 목록 | 문서에 없다. 내장 차단은 auto 모드 분류기의 기본 규칙과 3.1절 항목뿐이다 |
| `disableAutoMode`의 위치 | 권한 페이지는 `permissions.disableAutoMode`, 설정 페이지는 최상위 `disableAutoMode`로 표기 |
| `forceLoginMethod`의 범위 | 관리형 설정 페이지는 관리형 전용, 설정 페이지는 "모든 파일"로 표기 |
| `sandbox.autoAllowBashIfSandboxed`, `sandbox.allowUnsandboxedCommands`의 기본값 | 확인하지 못함 |
| MCP 도구 승인의 유지 범위 | 기본 동작 표에 없음 |
| Skill 도구의 allow 규칙 문법 | deny용 `Skill(skill:<이름>)`만 확인 |
| `--allowedTools`와 프로젝트 deny의 관계 | 관리형 deny를 못 뒤집는다는 것만 명시됨 |
| `.claude` 보호 경로의 예외, 보호 파일 전체 목록 | 일부만 확인 |
| 설정 레퍼런스의 키별 타입·기본값 | 색인 표만 확인 |

## 출처

| 약어 | 페이지 |
|---|---|
| [P] | <https://code.claude.com/docs/en/permissions> |
| [PM] | <https://code.claude.com/docs/en/permission-modes> |
| [S] | <https://code.claude.com/docs/en/settings> |
| [SB] | <https://code.claude.com/docs/en/sandboxing> |
| [H] | <https://code.claude.com/docs/en/hooks> |
| [MS] | <https://code.claude.com/docs/en/managed-settings> |
| [SEC] | <https://code.claude.com/docs/en/security> |
| [AMC] | <https://code.claude.com/docs/en/auto-mode-config> |
| [CW] | <https://code.claude.com/docs/en/claude-code-on-the-web> |
| [CLI] | <https://code.claude.com/docs/en/cli-reference> |
