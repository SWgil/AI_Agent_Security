#!/usr/bin/env python3
"""List what a build pulled in from outside the repository.

Reads the build user's Gradle home (which starts empty, so everything in it
was fetched by this build) and the recording proxy's log, and writes

    {"networkHosts": [...], "artifacts": [{"name": ..., "sha256": ...}, ...]}

Files fetched by other means than Gradle's dependency resolution show up only
as a host in networkHosts; their contents are not hashed.
"""

import argparse
import hashlib
import json
import pathlib


def _sha256(path):
    h = hashlib.sha256()
    with open(path, "rb") as f:
        for chunk in iter(lambda: f.read(1 << 20), b""):
            h.update(chunk)
    return h.hexdigest()


def collect(gradle_home, hosts_log, toolchain=()):
    gradle_home = pathlib.Path(gradle_home)
    artifacts = []

    # caches/modules-2/files-2.1/<group>/<name>/<version>/<sha1>/<file>
    modules = gradle_home / "caches" / "modules-2" / "files-2.1"
    for path in sorted(modules.glob("*/*/*/*/*")):
        if path.is_file():
            group, name, version, _sha1, filename = path.relative_to(modules).parts
            artifacts.append({"name": f"{group}:{name}:{version}/{filename}", "sha256": _sha256(path)})

    # Tools the builder supplied itself, such as the Gradle distribution.
    for path in map(pathlib.Path, toolchain):
        artifacts.append({"name": f"toolchain/{path.name}", "sha256": _sha256(path)})

    hosts = set()
    log = pathlib.Path(hosts_log)
    if log.exists():
        hosts = {line.strip() for line in log.read_text().splitlines() if line.strip()}
    return {"networkHosts": sorted(hosts), "artifacts": artifacts}


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--gradle-home", required=True)
    parser.add_argument("--hosts-log", required=True)
    parser.add_argument("--toolchain", action="append", default=[],
                        help="file the builder supplied to the build; may be repeated")
    parser.add_argument("--out", required=True)
    args = parser.parse_args()
    inputs = collect(args.gradle_home, args.hosts_log, args.toolchain)
    with open(args.out, "w") as f:
        json.dump(inputs, f, indent=1)
    print(f"{len(inputs['artifacts'])} artifacts, hosts: {', '.join(inputs['networkHosts']) or '(none)'}")
