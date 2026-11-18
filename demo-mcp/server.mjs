// 데모용 원격 MCP 서버 (Streamable HTTP, 무상태, 인증 없음, 의존성 없음).
// 실제 기능은 없고, 커넥터 연결 확인용 `noop` 도구 하나만 노출한다.
import { createServer } from "node:http";

const PORT = Number(process.env.PORT) || 3000;
const SUPPORTED_VERSIONS = ["2025-11-25", "2025-06-18", "2025-03-26"];
const MAX_BODY_BYTES = 1024 * 1024;

const TOOLS = [
  {
    name: "noop",
    description: "아무 일도 하지 않고 'ok'를 돌려주는 연결 확인용 도구.",
    inputSchema: { type: "object", properties: {}, additionalProperties: false },
    annotations: { readOnlyHint: true },
  },
];

const ok = (id, result) => ({ jsonrpc: "2.0", id, result });
const fail = (id, code, message) => ({ jsonrpc: "2.0", id, error: { code, message } });

// 요청이면 응답 객체를, 알림(id 없음)이면 null을 돌려준다.
function handle(msg) {
  if (!msg || msg.jsonrpc !== "2.0" || typeof msg.method !== "string") {
    return fail(msg?.id ?? null, -32600, "Invalid Request");
  }
  if (msg.id === undefined) return null;

  switch (msg.method) {
    case "initialize": {
      const requested = msg.params?.protocolVersion;
      return ok(msg.id, {
        protocolVersion: SUPPORTED_VERSIONS.includes(requested) ? requested : SUPPORTED_VERSIONS[0],
        capabilities: { tools: {} },
        serverInfo: { name: "demo-mcp", version: "0.1.0" },
      });
    }
    case "ping":
      return ok(msg.id, {});
    case "tools/list":
      return ok(msg.id, { tools: TOOLS });
    case "tools/call":
      if (msg.params?.name !== "noop") {
        return fail(msg.id, -32602, `Unknown tool: ${msg.params?.name}`);
      }
      return ok(msg.id, { content: [{ type: "text", text: "ok" }] });
    default:
      return fail(msg.id, -32601, `Method not found: ${msg.method}`);
  }
}

function send(res, status, body) {
  if (body === undefined) return res.writeHead(status).end();
  res.writeHead(status, { "content-type": "application/json" }).end(JSON.stringify(body));
}

createServer((req, res) => {
  const { pathname } = new URL(req.url, "http://localhost");

  if (pathname === "/" && req.method === "GET") {
    return send(res, 200, { name: "demo-mcp", endpoint: "/mcp" });
  }
  if (pathname !== "/mcp") return send(res, 404, { error: "not found" });
  if (req.method !== "POST") {
    res.setHeader("allow", "POST");
    return send(res, 405, { error: "method not allowed" });
  }

  const chunks = [];
  let size = 0;
  req.on("data", (chunk) => {
    size += chunk.length;
    if (size > MAX_BODY_BYTES) {
      send(res, 413, fail(null, -32600, "Request too large"));
      req.destroy();
      return;
    }
    chunks.push(chunk);
  });
  req.on("end", () => {
    if (res.writableEnded) return;
    let payload;
    try {
      payload = JSON.parse(Buffer.concat(chunks).toString("utf8"));
    } catch {
      return send(res, 400, fail(null, -32700, "Parse error"));
    }
    if (Array.isArray(payload)) {
      const replies = payload.map(handle).filter(Boolean);
      return replies.length ? send(res, 200, replies) : send(res, 202);
    }
    const reply = handle(payload);
    return reply ? send(res, 200, reply) : send(res, 202);
  });
}).listen(PORT, () => {
  console.log(`demo-mcp listening on :${PORT} (endpoint: /mcp)`);
});
