#!/usr/bin/env python3
"""Freeze release hashes and retrieve those exact bytes from public Modrinth."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import runpy
import urllib.request

ROOT = Path(__file__).resolve().parents[1]
WRAPPER = runpy.run_path(str(ROOT / 'build-smart.py'))


def inventory(root=ROOT):
    config = WRAPPER['read_properties'](root / 'gradle/publishing.properties')
    props = WRAPPER['read_properties'](root / 'gradle.properties')
    data = json.loads((root / config['canonical_artifacts_file']).read_text(encoding='utf-8'))
    if data['mod_version'] != props['mod_version'] or data['modrinth_project_id'] != config['modrinth_project_id']:
        raise ValueError('Canonical artifact pins belong to a different release/project')
    matrices = {p.stem: WRAPPER['read_properties'](p) for p in (root / 'gradle/matrix').glob('*.properties')}
    rows = data['artifacts']
    built = data.get('origin', 'modrinth') == 'build'
    if data.get('origin', 'modrinth') not in ('build', 'modrinth'):
        raise ValueError('Unknown release artifact origin')
    targets = {r['target'] for r in rows}
    if len(rows) != len(targets) or not targets <= set(matrices):
        raise ValueError('Canonical artifact pins must name unique registered targets')
    if built and targets != set(matrices):
        raise ValueError('Fresh release pins must cover the complete registered matrix')
    for row in rows:
        pins = matrices[row['target']]
        filename = f"{props['archives_name']}-{pins['loader']}-{pins['minecraft_version']}-{props['mod_version']}.jar"
        if (row['filename'] != filename or row['minecraft_version'] != pins['minecraft_version']
                or row['loader'] != pins['loader'] or row['size'] <= 0):
            raise ValueError('Invalid canonical artifact identity: ' + row['target'])
        if not built:
            url = f"https://cdn.modrinth.com/data/{data['modrinth_project_id']}/versions/{row['modrinth_version_id']}/{filename}"
            if row['url'] != url:
                raise ValueError('Invalid canonical artifact URL: ' + row['target'])
        for algorithm, length in (('sha256', 64), ('sha512', 128), ('sha1', 40)):
            if not re.fullmatch('[0-9a-f]{' + str(length) + '}', row[algorithm]):
                raise ValueError('Invalid canonical digest: ' + row['target'])
        expected_exception = 0
        if row['legacy_json_count'] != expected_exception:
            raise ValueError('Unsupported canonical JSON exception: ' + row['target'])
    resources = data['legacy_json_resources']
    expected_count = 0
    if len(resources) != len(set(resources)) or len(resources) != expected_count:
        raise ValueError('Unsupported legacy JSON resource inventory')
    return data


def record(target, root=ROOT):
    row = next((r for r in inventory(root)['artifacts'] if r['target'] == target), None)
    if row is None:
        raise ValueError(f'No canonical release artifact for {target}; prepare new release pins before publishing')
    return row


def verify_bytes(payload, row):
    if len(payload) != row['size'] or any(hashlib.new(a, payload).hexdigest() != row[a] for a in ('sha256', 'sha512', 'sha1')):
        raise ValueError('Canonical artifact hash/size mismatch: ' + row['filename'])


def verify(artifact, target, root=ROOT):
    row = record(target, root)
    if artifact.name != row['filename']:
        raise ValueError('Wrong canonical artifact filename: ' + artifact.name)
    verify_bytes(artifact.read_bytes(), row)
    return row


def request(url):
    return urllib.request.Request(url, headers={'User-Agent': 'Vg34100/ExtraChests-release-pipeline'})


def freeze(root=ROOT):
    config = WRAPPER['read_properties'](root / 'gradle/publishing.properties')
    props = WRAPPER['read_properties'](root / 'gradle.properties')
    destination = root / config['canonical_artifacts_file']
    if destination.exists():
        raise ValueError('Refusing to overwrite frozen release pins: ' + str(destination))
    rows = []
    for matrix in sorted((root / 'gradle/matrix').glob('*.properties')):
        pins = WRAPPER['read_properties'](matrix)
        filename = f"{props['archives_name']}-{pins['loader']}-{pins['minecraft_version']}-{props['mod_version']}.jar"
        payload = (root / 'build/libs' / matrix.stem / filename).read_bytes()
        rows.append(dict(target=matrix.stem, minecraft_version=pins['minecraft_version'], loader=pins['loader'],
                         filename=filename, size=len(payload), legacy_json_count=0,
                         **{a: hashlib.new(a, payload).hexdigest() for a in ('sha256', 'sha512', 'sha1')}))
    data = dict(origin='build', mod_version=props['mod_version'], modrinth_project_id=config['modrinth_project_id'],
                legacy_json_resources=[], artifacts=rows)
    destination.parent.mkdir(parents=True, exist_ok=True)
    # Exclusive creation protects every historical release, including on retries.
    with destination.open('x', encoding='utf-8') as output:
        output.write(json.dumps(data, indent=2) + '\n')
    inventory(root)
    print(f"FROZEN {props['mod_version']}: {len(rows)} release JAR hashes")


def fetch(root=ROOT, public=False):
    data = inventory(root)
    built = data.get('origin', 'modrinth') == 'build'
    if built and not public:
        for row in data['artifacts']:
            verify(root / 'build/libs' / row['target'] / row['filename'], row['target'], root)
        print(f"FROZEN LOCAL ARTIFACTS PASS ({len(data['artifacts'])}/{len(data['artifacts'])})")
        return
    config = WRAPPER['read_properties'](root / 'gradle/publishing.properties')
    url = f"https://api.modrinth.com/v2/project/{data['modrinth_project_id']}/version"
    with urllib.request.urlopen(request(url), timeout=60) as response:
        versions = {v['id']: v for v in json.load(response)}
    # Validate the complete public inventory before writing any local release JAR.
    for row in data['artifacts']:
        if built:
            number = config['version_number'].format(mod_version=data['mod_version'],
                        minecraft_version=row['minecraft_version'], loader=row['loader'])
            matches = [v for v in versions.values() if v.get('version_number') == number
                       and v.get('game_versions') == [row['minecraft_version']] and v.get('loaders') == [row['loader']]]
            if len(matches) != 1:
                raise ValueError('Missing/ambiguous public release target: ' + row['target'])
            version = matches[0]
        else:
            number = data['mod_version']
            version = versions.get(row['modrinth_version_id'], {})
        files = [f for f in version.get('files', []) if f.get('primary')]
        if (version.get('project_id') != data['modrinth_project_id'] or version.get('version_number') != number
                or version.get('game_versions') != [row['minecraft_version']] or version.get('loaders') != [row['loader']]
                or len(files) != 1):
            raise ValueError('Public canonical target changed/missing: ' + row['target'])
        file = files[0]
        if built:
            row = row.copy()
            row['url'] = f"https://cdn.modrinth.com/data/{data['modrinth_project_id']}/versions/{version['id']}/{row['filename']}"
        if any(file.get(key) != row[key] for key in ('filename', 'url', 'size')) or any(
                file.get('hashes', {}).get(a) != row[a] for a in ('sha1', 'sha512')):
            raise ValueError('Public canonical file changed: ' + row['target'])
        # The URL is discovered only after matching the frozen file hashes.
        if built:
            next(r for r in data['artifacts'] if r['target'] == row['target'])['url'] = row['url']
    for row in data['artifacts']:
        artifact = root / 'build/libs' / row['target'] / row['filename']
        payload = artifact.read_bytes() if artifact.is_file() else b''
        if hashlib.sha256(payload).hexdigest() != row['sha256'] or len(payload) != row['size']:
            with urllib.request.urlopen(request(row['url']), timeout=60) as response:
                payload = response.read()
            verify_bytes(payload, row)
            artifact.parent.mkdir(parents=True, exist_ok=True)
            temporary = artifact.with_suffix('.jar.download')
            temporary.write_bytes(payload)
            temporary.replace(artifact)
        verify_bytes(artifact.read_bytes(), row)
        print(f"CANONICAL {row['target']}: {row['filename']} | SHA-256 {row['sha256']}")
    print(f"CANONICAL MODRINTH AUDIT PASS ({len(data['artifacts'])}/{len(data['artifacts'])}; zero uploads)")


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--freeze', action='store_true', help='Pin newly verified local release JARs; never overwrite')
    parser.add_argument('--public', action='store_true', help='Require and retrieve public Modrinth bytes (GitHub release)')
    args = parser.parse_args()
    if args.freeze and args.public:
        parser.error('--freeze and --public are mutually exclusive')
    freeze() if args.freeze else fetch(public=args.public)
