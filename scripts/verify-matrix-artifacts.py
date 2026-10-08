#!/usr/bin/env python3
"""Verify Extra Chests' installable matrix JARs, not gameplay behavior."""
import json
from pathlib import Path
import re
import struct
from zipfile import BadZipFile, ZipFile

try:
    import tomllib
except ImportError:  # The established wrapper also supports Python 3.10.
    tomllib = None

ROOT = Path(__file__).resolve().parents[1]
MOD_ID = 'extrachests'
PACKAGE = 'net/vg/extrachests/'


def properties(path):
    return {key.strip(): value.strip() for line in path.read_text().splitlines()
            if '=' in line and not line.lstrip().startswith('#')
            for key, value in [line.split('=', 1)]}


def quoted(text, key):
    match = re.search(rf'^\s*{re.escape(key)}\s*=\s*"([^"\n]*)"', text, re.MULTILINE)
    assert match, f'Missing metadata field: {key}'
    return match.group(1)


def release_jar(matrix):
    """Select exactly one expected release; reject classifiers and stale versions."""
    jars = [p for p in (ROOT / 'build/libs' / matrix.stem).glob('*.jar')
            if not p.name.endswith(('-dev.jar', '-sources.jar', '-javadoc.jar',
                                    '-dev-shadow.jar', '-raw.jar'))]
    assert len(jars) == 1, f'Expected one release JAR: {jars}'
    root_pins = properties(ROOT / 'gradle.properties')
    minecraft, loader = matrix.stem.rsplit('-', 1)
    expected = f"{root_pins['archives_name']}-{loader}-{minecraft}-{root_pins['mod_version']}.jar"
    assert jars[0].name == expected, f'Wrong/stale release artifact: {jars[0]} (expected {expected})'
    return jars[0]


def inspect(matrix):
    pins = properties(matrix)
    assert matrix.stem == f"{pins['minecraft_version']}-{pins['loader']}", 'Target/property mismatch'
    artifact = release_jar(matrix)
    legacy = pins.get('loom_generation', 'current') == 'legacy'
    java = pins.get('java_version', '25')
    root_pins = properties(ROOT / 'gradle.properties')
    with ZipFile(artifact) as jar:
        entries = jar.namelist()
        files = set(entries)
        assert len(entries) == len(files), 'Duplicate archive entries'
        read_json = lambda name: json.loads(jar.read(name))
        required_classes = (
            'Extrachests', 'block/ModChestBlock', 'block/ModTrappedChestBlock',
            'blockentity/ModChestBlockEntity', 'blockentity/ModTrappedChestBlockEntity',
            'registry/ModChestRegistries', 'registry/ModChestBoatRegistries',
            'client/ExtraChestsClient', 'client/renderer/ModChestRenderer',
            'client/renderer/ModTrappedChestRenderer', 'client/renderer/ModChestBoatRenderer',
        )
        for name in required_classes:
            assert PACKAGE + name + '.class' in files, f'Missing mod class: {name}'
        classes = [name for name in files if name.endswith('.class')]
        assert classes and all(name.startswith(PACKAGE) for name in classes), 'Bundled dependency classes'
        # These vanilla overrides are part of Extra Chests' own baseline.
        vanilla_data = {'data/minecraft/recipe/chest.json', 'data/minecraft/tags/block/mineable/axe.json'}
        assert all(name.split('/')[1] == MOD_ID or name in vanilla_data for name in files
                   if name.startswith(('assets/', 'data/')) and len(name.split('/')) > 2
                   and not name.endswith('/')), 'Bundled external resources/datapack'
        for name in classes:
            bytecode = jar.read(name)
            assert bytecode[:4] == b'\xca\xfe\xba\xbe', f'Invalid class: {name}'
            assert struct.unpack('>H', bytecode[6:8])[0] == int(java) + 44, f'Wrong class level: {name}'
        for name in files:
            if name.endswith('.json'):
                read_json(name)
        mixins = read_json('extrachests.mixins.json')
        assert mixins['required'] is True
        assert mixins['compatibilityLevel'] == 'JAVA_' + java
        # The canonical baseline registers no injections; dormant helpers stay dormant.
        assert mixins['client'] == [] and mixins['mixins'] == [], 'Unexpected active mixins'
        assert mixins['package'] == 'net.vg.extrachests.mixin'
        for side in ('mixins', 'client', 'server'):
            for mixin in mixins.get(side, []):
                name = (mixins['package'] + '.' + mixin).replace('.', '/') + '.class'
                assert name in files, f'Missing mixin class: {name}'
        if pins['loader'] == 'fabric':
            assert 'META-INF/neoforge.mods.toml' not in files, 'Wrong loader metadata'
            meta = read_json('fabric.mod.json')
            assert meta['id'] == MOD_ID and meta['version'] == root_pins['mod_version']
            assert meta['depends']['minecraft'] == pins['minecraft_version']
            assert set(meta['depends']) == {'java', 'minecraft', 'fabricloader', 'architectury', 'fabric-api'}
            assert meta['depends']['java'] == '>=' + java
            for dep, pin in (('fabricloader', 'fabric_loader_version'),
                             ('architectury', 'architectury_api_version')):
                assert meta['depends'][dep] == '>=' + pins[pin], f'Wrong dependency: {dep}'
            assert not meta.get('suggests') and not meta.get('recommends'), 'Unexpected integrations'
            assert meta['mixins'] == ['extrachests.mixins.json']
            assert meta['environment'] == '*'
            assert meta['entrypoints'] == {
                'main': ['net.vg.extrachests.fabric.ExtrachestsFabric'],
                'client': ['net.vg.extrachests.fabric.client.ExtrachestsFabricClient'],
                'fabric-datagen': ['net.vg.extrachests.fabric.data.ModDataGenerator'],
            }
            for kind, names in meta['entrypoints'].items():
                # The legacy view intentionally excludes modern datagen; it is
                # not a runtime entrypoint or a production dependency.
                if kind == 'fabric-datagen' and legacy:
                    continue
                for name in names:
                    assert name.replace('.', '/') + '.class' in files, f'Missing entrypoint: {name}'
            assert meta['icon'] in files
            assert 'accessWidener' not in meta
            if legacy:
                assert b'net/minecraft/class_' in jar.read(PACKAGE + 'block/ModChestBlock.class'), 'Unremapped Fabric release'

        else:
            assert 'fabric.mod.json' not in files, 'Wrong loader metadata'
            meta = jar.read('META-INF/neoforge.mods.toml').decode()
            assert quoted(meta, 'modId') == MOD_ID
            assert quoted(meta, 'version') == root_pins['mod_version']
            assert PACKAGE + 'neoforge/ExtrachestsNeoForge.class' in files
            assert PACKAGE + 'neoforge/ExtraChestsNeoForgeClient.class' in files
            blocks = re.findall(r'\[\[dependencies\.extrachests\]\](.*?)(?=\n\[|\Z)', meta, re.DOTALL)
            deps = {quoted(block, 'modId'): block for block in blocks}
            assert set(deps) == {'minecraft', 'neoforge', 'architectury'}
            assert quoted(deps['minecraft'], 'versionRange') == '[' + pins['minecraft_version'] + ']'
            for dep, pin in (('neoforge', 'neoforge_version'), ('architectury', 'architectury_api_version')):
                assert quoted(deps[dep], 'versionRange') == '[' + pins[pin] + ',)'
            assert all(quoted(deps[dep], 'type') == 'required' for dep in ('minecraft', 'neoforge', 'architectury'))
            assert all(quoted(block, 'side') == 'BOTH' for block in deps.values())
            if tomllib is not None:
                tomllib.loads(meta)
            icon_field = 'logoFile' if legacy else 'iconFile'
            assert quoted(meta, icon_field) in files
            configs = re.findall(r'^config\s*=\s*"([^"]+)"', meta, re.MULTILINE)
            assert configs == (['extrachests.mixins.json'] if legacy else
                               ['extrachests.mixins.json', 'extrachests-neoforge-compat.mixins.json'])
            if not legacy:
                compat = read_json(configs[1])
                assert compat['required'] and compat['compatibilityLevel'] == 'JAVA_' + java
                assert compat['plugin'] == 'net.vg.extrachests.neoforge.mixin.EarlyNeoForgeArchitecturyCompatPlugin'
                assert compat['mixins'] == ['EarlyNeoForgeArchitecturyCompatMixin']
                for name in [compat['plugin'], compat['package'] + '.' + compat['mixins'][0]]:
                    assert name.replace('.', '/') + '.class' in files
        if legacy:
            for name in ('entity/LegacyChestBoat', 'item/LegacyChestBoatItem', 'client/renderer/LegacyChestItemRenderer'):
                assert PACKAGE + name + '.class' in files
            assert not any('/items/' in name or 'pale_oak' in name for name in files), 'Modern-only legacy resources'
            for wood in ('spruce', 'bamboo', 'birch'):
                for suffix in ('_chest', '_trapped_chest'):
                    assert read_json(f'assets/extrachests/models/item/{wood}{suffix}.json')['parent'] == 'minecraft:item/chest'
        else:
            assert 'assets/extrachests/items/pale_oak_chest.json' in files
            assert 'assets/extrachests/items/bamboo_raft_spruce_chest.json' in files
        assert 'assets/extrachests/lang/en_us.json' in files
        for wood in ('spruce', 'bamboo', 'birch'):
            for suffix in ('_chest', '_trapped_chest'):
                for prefix in ('blockstates', 'models/block', 'models/item' if legacy else 'items'):
                    name = f'assets/extrachests/{prefix}/{wood}{suffix}.json'
                    assert name in files, f'Missing chest resource: {name}'
                assert f'data/extrachests/recipe/{wood}{suffix}.json' in files
                assert f'data/extrachests/loot_table/blocks/{wood}{suffix}.json' in files
            for texture in ('', '_left', '_right', '_trapped', '_trapped_left', '_trapped_right'):
                image = f'assets/extrachests/textures/entity/chest/{wood}{texture}.png'
                assert image in files and jar.read(image).startswith(b'\x89PNG\r\n\x1a\n')
        assert 'assets/extrachests/models/item/bamboo_raft_spruce_chest.json' in files
        assert 'assets/extrachests/textures/entity/chest_boat/bamboo_spruce.png' in files
        assert not any(name.endswith(('.jar', '.java')) for name in files), 'Embedded JAR or source archive'
        for name in ('fabric.mod.json', 'META-INF/neoforge.mods.toml', 'extrachests.mixins.json', 'extrachests-neoforge-compat.mixins.json'):
            if name in files:
                assert b'${' not in jar.read(name), f'Unexpanded metadata: {name}'
    print(f'{matrix.stem}: release metadata, classes, mixins and resources OK')
    return artifact


if __name__ == '__main__':
    matrices = sorted((ROOT / 'gradle/matrix').glob('*.properties'))
    assert matrices, 'No matrix properties found'
    for matrix in matrices:
        try:
            inspect(matrix)
        except (AssertionError, KeyError, ValueError, OSError, BadZipFile) as error:
            raise SystemExit(f'{matrix.stem}: {error}') from error
    print(f'ARTIFACT VERIFICATION PASS ({len(matrices)}/{len(matrices)})')
