# Framework Seed Status

The standardized framework has been adapted for Extra Chests 1.0.2.

Project identities, Client + Server environments, production dependencies,
artifact checks, semantic duplicate skips, and release-only task wiring are
configured. Project-specific facts live in `docs/development/distribution.md`.

Acceptance: twelve packaged/verified JARs, four representative packaged-client
smoke passes, both twelve-target publishing dry-runs, and an exact twelve-JAR
GitHub bundle. The single preflight was completed through targeted resumes for
installer transport failures and release-tool wiring fixes. Mod source and
dependency pins were unchanged.

Maintain the project-specific facts in:

- `scripts/verify-matrix-artifacts.py`
- `gradle/publishing.properties`
- `build-smart.py`
- `scripts/smoke-release-client.py`
- `scripts/publish-release.py`
- `.github/workflows/release.yml`

Use `prompts/adapt-seeded-framework.md`.

Future releases follow `docs/development/publishing.md`; avoid repeating passed
gates when only unrelated documentation changes.
