# demo-mcp

기능이 없는 데모용 원격 MCP 서버. Claude 커스텀 커넥터가 어떻게 붙는지 확인하는 용도다.

- 전송 방식: Streamable HTTP (무상태, JSON 응답)
- 엔드포인트: `POST /mcp`
- 인증: 없음
- 도구: `noop` 하나 (아무 일도 하지 않고 `ok` 반환)
- 의존성: 없음 (Node 18 이상)

## 로컬 실행

```bash
cd demo-mcp
npm start            # 기본 포트 3000, PORT 환경변수로 변경 가능
```

```bash
curl -s localhost:3000/mcp -H 'content-type: application/json' \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

## Claude에 커넥터로 붙이기

1. 이 디렉터리를 공개 HTTPS로 접근 가능한 곳에 배포한다. 시작 명령은 `npm start`이고, 호스팅이 주는 `PORT`를 그대로 쓴다.
2. <https://claude.ai/customize/connectors> 에서 커스텀 커넥터를 추가하고 URL에 `https://<배포 주소>/mcp` 를 넣는다.
3. 새 대화를 열어 커넥터를 켜고 `noop` 도구를 호출해 본다. 모바일 앱에서도 같은 계정이면 보인다.

## 주의

인증이 없으므로 URL을 아는 누구나 호출할 수 있다. 도구가 아무 일도 하지 않아 데모로는 무방하지만, 실제 기능을 넣을 때는 OAuth 등 인증을 먼저 붙여야 한다.
