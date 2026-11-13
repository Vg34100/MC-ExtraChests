# Extra Chests — current session handoff

Release 1.0.3 was authorized on 2026-11-11, including initial publication with
the remaining per-feature manual checks deferred. The user confirmed PrismMC
startup on 1.20.1 Forge and 26.3 Fabric. Release work stays on `work`; the public
tag will use its filtered `main` snapshot. Current release details are in
[the work-only release note](../../.development/releases/1.0.3.md) and ignored
`build/publishing/1.0.3-publication-report.json`. Historical port evidence follows.

## Port baseline

Updated 2026-10-18. Branch: `feature/multiversion-matrix`. Mod version remains
`1.0.2`; canonical maintained source remains Minecraft `26.1.2`.

## Result

The matrix now discovers 16 targets from `gradle/matrix/*.properties`. The twelve
existing target pins are unchanged. No existing registered IDs were renamed or
removed. No commit, push, tag, upload or publication was performed.

| New target | Java | Compile / installable package | Packaged client | Dedicated server |
| --- | --- | --- | --- | --- |
| 1.20.1 Fabric | 17 | Pass / remapJar | Pass | Not launched |
| 1.20.1 Forge | 17 | Pass / remapJar | Pass | Pass, 8 world assertions |
| 26.3 Fabric | 25 | Pass / jar | Pass | Not launched |
| 26.3 NeoForge | 25 | Pass / jar | Pass | Pass, 8 world assertions |

Final acceptance gate, run once after new-target stabilization:

```text
python build-smart.py matrix:compile packageMatrix --max-workers=2
python scripts/verify-matrix-artifacts.py
```

Both passed for **16/16** targets. Twenty-two publishing-helper regression cases
also pass; the last corrected assertion was resumed individually, not by repeating
the successful cases. These are local mocked safety tests, not platform dry-runs.
The twelve older clients/servers were not relaunched in this session.

## Runtime evidence and limits

Four exact release JARs passed production startup, resource reload, loaded-class
origin and SHA-256 checks. Final aggregate outputs still match those tested hashes.
Proofs are retained under ignored `build/reports/port-16/runtime/<target>.txt`.
Modern clients reached `startedCleanly:true`; legacy clients reached the initial
onboarding-screen initialization after resource reload and stayed stable for three
seconds. This is startup evidence, not a manual title-screen/gameplay inspection.

The two dedicated servers reached `Done`, saved and exited cleanly. Each verified
normal/trapped chest storage at slot 26, paired double-chest states and storage in
both halves, boat/raft storage, and the correct custom boat/raft dropped items.
Logs: `build/reports/gameplay/{1.20.1-forge,26.3-neoforge}.log`.
Recipes, results, ingredients, advancement formats and expected recipe counts are
artifact-checked; both server data reloads loaded without recipe errors.

## Compatibility and standardization

- Added native Forge bootstrap, Architectury event-bus ordering, client subscriber
  and `Item.initializeClient` chest-item renderer. Architectury is still required
  on every loader; Fabric also requires Fabric API. No shared runtime library added.
- Reused `Identifier`; introduced small fresh item/block property factories with
  matching registry IDs. Legacy views remove exactly two unsupported key setters.
  Existing combined registries and deferred dependencies were retained.
- Narrow 1.20.1 deltas reuse the proven legacy view. 26.3 needs two asserted renderer
  substitutions and the recipe-unlocked advancement format change. Details live
  in [compatibility policy](compatibility-policy.md), not a duplicated source tree.
- Canonical datagen stays on 26.1.2; 1.20.1/26.3 omit its runtime entrypoint. Existing
  1.21 legacy behavior is preserved. The unused `RecipeSystem.java` was left alone.
- Fixed the demonstrated self-referential trapped-chest recipes and unlock criteria:
  they now consume/unlock from the matching normal chest plus a tripwire hook.
- Target discovery, packaging, Java 17/21/25 toolchains, future CI and release-smoke
  selection now understand all sixteen targets and the Forge loader.

## Manual checks / retained baseline limitations

- Inspect chest-item locks/orientation, normal/trapped/single/double chest textures,
  chest-boat chest textures and bamboo raft models in an actual world.
- Open the 27/54-slot menus, exercise double-chest merging, trapped-chest redstone
  while opening/closing, ride/place/break boats and rafts, and craft representative
  normal/trapped/boat recipes. Console storage assertions do not prove these UI,
  player interaction or rendering behaviors.
- Test a backed-up existing world and client/server connection. Registry paths were
  preserved, but no real saved-world upgrade or multiplayer UI session was tested.
- The existing legacy pale-oak exclusion remains unchanged; 1.20.1 follows that
  established legacy content inventory. Existing mangrove-chest boat combinations
  remain registered/creative-accessible, but their preexisting missing matching
  mangrove chest block/recipe was not expanded into new content during this port.
- Publication/version decisions remain separate. Previously published 1.0.2 files
  and durable upload receipts were not changed; do not overwrite historical releases.

No further builds or smokes are needed for documentation-only changes. Start any
follow-up with this file and `git status --short`, not a new repository audit.
