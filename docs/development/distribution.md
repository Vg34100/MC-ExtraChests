# Extra Chests distribution

The publication plan is derived from `gradle/matrix/*.properties`, loader
metadata, `gradle.properties`, and `gradle/publishing.properties`.

- All three loaders require Architectury API. Fabric also requires Fabric API.
- Both client and server need the mod. NeoForge common registration is not a
  client-only entrypoint; only rendering is gated to the client.
- 1.20.1 uses Java 17; 1.21/1.21.1 use Java 21. Both use `remapJar` (not the
  `-dev.jar`). Current targets use Java 25 and
  `jar`. Modern matrix files inherit the build's Java 25 default.
- `gradle/distribution.gradle` only attaches packaging, staged production
  smoke, and publishing tasks. It does not change compatibility source views.
- Legacy Fabric uses PortableMC because its established Loom 1.7 has no modern
  production-client task. Modern Fabric uses Loom's production-client task;
  Forge/NeoForge use PortableMC. All load the exact staged release JAR.
- Production staging reads `modImplementation` for legacy Fabric/Forge and
  `implementation` for NeoForge on both generations; using Fabric's legacy
  configuration on NeoForge omits its required Architectury JAR.
- The artifact verifier checks chest blocks/entities, item and boat renderers,
  textures, recipes, loot, and loader mixins. Vanilla chest recipe/axe-tag
  overrides are intentional. Legacy views exclude pale oak, modern item
  definitions, and the development-only Fabric datagen implementation. 26.3
  likewise omits datagen and its entrypoint; generation stays on canonical 26.1.2.

`release_smoke_targets` in `gradle/publishing.properties` selects the four
representative release tests without changing pinned Minecraft dependencies.

Publication skips existing semantic version/Minecraft/loader nodes, even when
their historical filenames or version numbers differ. An explicitly targeted
JAR takes precedence over broad platform compatibility labels. A mislabelled
CurseForge file must be checked against its actual JAR metadata before skipping
it. Existing public files are preserved; new uploads keep exact-artifact
receipts under ignored `build/publishing/` for safe resume.

Local release preflight packages with Java 17/21/25 and freezes all sixteen JARs
in `gradle/canonical/<version>.json`. GitHub Actions retrieves the identical public
Modrinth bytes, verifies their hashes, and publishes only those sixteen installable
JARs. It never rebuilds replacements or uploads to Modrinth/CurseForge.

The 16-target release is 1.0.3; see [session status](session-summary.md).
The previously published 1.0.2 artifacts/receipts remain historical and untouched.
