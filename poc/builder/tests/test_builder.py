import asyncio
import hashlib
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

import collect_inputs  # noqa: E402
import record_proxy  # noqa: E402


def _run_through_proxy(request, deny_target=False):
    """Send one request to the proxy; return (response bytes, recorded lines)."""

    async def scenario():
        async def echo(reader, writer):
            writer.write(b"echo:" + await reader.read(5))
            await writer.drain()
            writer.close()

        target = await asyncio.start_server(echo, "127.0.0.1", 0)
        target_port = target.sockets[0].getsockname()[1]
        recorded = []
        deny = {f"127.0.0.1:{target_port}"} if deny_target else set()
        proxy = await asyncio.start_server(
            lambda r, w: record_proxy.handle(r, w, recorded.append, deny), "127.0.0.1", 0
        )
        reader, writer = await asyncio.open_connection("127.0.0.1", proxy.sockets[0].getsockname()[1])
        writer.write(request(target_port))
        await writer.drain()
        response = await asyncio.wait_for(reader.read(-1), timeout=5)
        writer.close()
        proxy.close()
        target.close()
        return response, recorded, target_port

    return asyncio.run(scenario())


def test_connect_is_tunnelled_and_recorded():
    response, recorded, port = _run_through_proxy(
        lambda p: f"CONNECT 127.0.0.1:{p} HTTP/1.1\r\nHost: x\r\n\r\nhello".encode()
    )
    assert response == b"HTTP/1.1 200 Connection Established\r\n\r\necho:hello"
    assert recorded == [f"127.0.0.1:{port}"]


def test_denied_target_is_refused_and_recorded():
    response, recorded, port = _run_through_proxy(
        lambda p: f"CONNECT 127.0.0.1:{p} HTTP/1.1\r\n\r\nhello".encode(), deny_target=True
    )
    assert response.startswith(b"HTTP/1.1 403")
    assert recorded == [f"127.0.0.1:{port}"]


def test_plain_http_is_refused_and_recorded():
    response, recorded, _ = _run_through_proxy(
        lambda p: b"GET http://example.test/payload.bin HTTP/1.1\r\nHost: example.test\r\n\r\n"
    )
    assert response.startswith(b"HTTP/1.1 403")
    assert recorded == ["http://example.test"]


def test_unreachable_target_is_still_recorded():
    response, recorded, _ = _run_through_proxy(
        lambda p: b"CONNECT 127.0.0.1:1 HTTP/1.1\r\n\r\n"
    )
    assert response.startswith(b"HTTP/1.1 502")
    assert recorded == ["127.0.0.1:1"]


def test_collect_lists_gradle_downloads_and_hosts(tmp_path):
    jar = tmp_path / "caches/modules-2/files-2.1/org.example/lib/1.2/abc123/lib-1.2.jar"
    jar.parent.mkdir(parents=True)
    jar.write_bytes(b"jar-bytes")
    dist = tmp_path / "gradle-8.14.3-bin.zip"
    dist.write_bytes(b"zip-bytes")
    log = tmp_path / "hosts.log"
    log.write_text(
        "01:02:03 repo.maven.apache.org:443\n01:02:04 dl.google.com:443\n01:02:09 repo.maven.apache.org:443\n"
    )

    inputs = collect_inputs.collect(tmp_path, log, [dist])
    assert inputs["networkHosts"] == ["dl.google.com:443", "repo.maven.apache.org:443"]
    assert inputs["artifacts"] == [
        {"name": "org.example:lib:1.2/lib-1.2.jar", "sha256": hashlib.sha256(b"jar-bytes").hexdigest()},
        {"name": "toolchain/gradle-8.14.3-bin.zip", "sha256": hashlib.sha256(b"zip-bytes").hexdigest()},
    ]


def test_collect_without_downloads_or_log(tmp_path):
    assert collect_inputs.collect(tmp_path, tmp_path / "missing.log") == {"networkHosts": [], "artifacts": []}
