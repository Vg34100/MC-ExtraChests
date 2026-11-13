# Build and test matrix

Extra Chests discovers its 16 targets from `gradle/matrix/*.properties`. Each writes
its installable JAR to `build/libs/<target>/` and uses `runs/<target>/<client|server>/`.
Canonical source stays on 26.1.2. Current evidence is in [session status](session-summary.md).

1.20.1 Fabric/Forge use Java 17 and Architectury 9. 1.21/1.21.1 Fabric/NeoForge
use Java 21 and Architectury 13. All are remapped legacy builds. 26.x uses Java 25
and ordinary `jar` output. Their `runClient`/`runServer` tasks inherit the matching toolchain.
Do not configure `JavaExec.executable` as well: IntelliJ will reject mismatched `executable` and
`javaLauncher` toolchains. The generated Gradle task under `<target> > Tasks > loom` is the
intended IntelliJ launch entry point. See the legacy-backport section in
[Stonecutter multi-version migration](stonecutter-multiversion-migration.md).

Keep Extra Chests' proven early-NeoForge Architectury repair on its existing pins;
do not broaden it for 26.3 or copy a reference mod's patch for consistency.

For the reusable conversion procedure and the NeoForge platform-wiring pitfalls, see
[Stonecutter multi-version migration](stonecutter-multiversion-migration.md).

Use the smart wrapper from the repository root:

```text
python build-smart.py matrix:compile
python build-smart.py matrix:package
python build-smart.py matrix:server
python build-smart.py matrix
```

`matrix:compile` and `matrix:package` cover all 16 targets. `matrix:server` generates
launch configurations without starting games. During a port, test only the new
compatibility boundaries first, then run the aggregate gate once:

```text
python build-smart.py matrix:compile packageMatrix --max-workers=2
python scripts/verify-matrix-artifacts.py
```

Use `smoke-release-client:<target>` for exact-JAR startup/hash/origin/resource-reload
evidence, not just development classes. Use `smoke-server:<target>` for ready-line
and owned-process shutdown evidence. The optional world assertions and manual
feature checklist are described in [validation](validation-and-release.md) and
[session status](session-summary.md). Automated startup is not a visual gameplay test.
