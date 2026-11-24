#!/usr/bin/env python3
"""Recording forward proxy for the trusted builder.

The build runs as a user whose only way out to the network is this proxy, so
every host the build contacts ends up in the log. HTTPS is tunnelled (CONNECT)
without being decrypted: the log has hosts, not contents. Plain HTTP is
refused and logged.
"""

import argparse
import asyncio
import datetime
import urllib.parse


async def _pipe(reader, writer):
    try:
        while data := await reader.read(65536):
            writer.write(data)
            await writer.drain()
    except (ConnectionError, OSError):
        pass
    finally:
        writer.close()


async def _reply(writer, status):
    writer.write(f"HTTP/1.1 {status}\r\nContent-Length: 0\r\nConnection: close\r\n\r\n".encode())
    await writer.drain()
    writer.close()


async def handle(reader, writer, record, deny=()):
    try:
        head = await asyncio.wait_for(reader.readuntil(b"\r\n\r\n"), timeout=30)
    except (asyncio.IncompleteReadError, asyncio.LimitOverrunError, asyncio.TimeoutError, OSError):
        writer.close()
        return
    parts = head.split(b"\r\n", 1)[0].decode("latin-1").split()
    if len(parts) != 3:
        await _reply(writer, "400 Bad Request")
        return
    method, target, _version = parts

    if method != "CONNECT":
        host = urllib.parse.urlsplit(target).netloc or target
        record(f"http://{host}")
        await _reply(writer, "403 Plain HTTP Is Not Allowed")
        return

    host, _, port = target.rpartition(":")
    record(target.lower())
    if target.lower() in deny:
        await _reply(writer, "403 Host Is Denied By The Builder")
        return
    try:
        upstream_reader, upstream_writer = await asyncio.open_connection(host, int(port))
    except (OSError, ValueError):
        await _reply(writer, "502 Bad Gateway")
        return
    writer.write(b"HTTP/1.1 200 Connection Established\r\n\r\n")
    await writer.drain()
    await asyncio.gather(_pipe(reader, upstream_writer), _pipe(upstream_reader, writer))


async def serve(port, log_path, deny=()):
    log = open(log_path, "a", buffering=1)

    def record(target):
        # "<UTC time> <target>"; the time lets a host be matched to a build step.
        now = datetime.datetime.now(datetime.timezone.utc).strftime("%H:%M:%S")
        log.write(f"{now} {target}\n")

    server = await asyncio.start_server(lambda r, w: handle(r, w, record, deny), "127.0.0.1", port)
    async with server:
        await server.serve_forever()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--port", type=int, default=3128)
    parser.add_argument("--log", required=True)
    parser.add_argument("--deny", action="append", default=[], metavar="HOST:PORT",
                        help="refuse (but still record) connections to this target; may be repeated")
    args = parser.parse_args()
    asyncio.run(serve(args.port, args.log, {d.lower() for d in args.deny}))
