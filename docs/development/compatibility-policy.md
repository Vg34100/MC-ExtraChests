# Compatibility Policy

## Decision Order

Use the first mechanism that fits.

### 1. Shared source

If the same code/resource works, leave it shared.

### 2. Local Stonecutter condition

Use for small API drift:

- import/package move
- method rename
- one changed argument
- short alternate call
- annotation/interface difference

### 3. Narrow deterministic replacement

Use only for truly mechanical drift.

Requirements:

- narrow scope
- deterministic
- behavior-preserving
- expected matches can be asserted

Avoid broad regex/package rewrites.

### 4. Parsed resource/data transform

Use when behavior is shared but serialized schema changed.

Examples:

- recipe representation
- loader metadata
- model/item-definition schema
- advancement/data-component formats

Parse structured data where practical and verify processed output.

### 5. Separate compatibility implementation

Use a real alternate file when the implementation or lifecycle materially differs.

Examples:

- renderer architecture
- menu/screen lifecycle
- entity render-state model
- fundamentally different mixin injection strategy

Keep the compatibility area small.

## Loader Differences

Minecraft-version differences and loader differences are separate axes.

Prefer Fabric-specific source for Fabric-only behavior and NeoForge-specific source for NeoForge-only behavior.

## Mixins

Compilation does not prove mixin correctness.

Verify:

- target class
- target method/descriptor
- injection point
- actual dispatch path
- whether the mixin should exist on the target

## Optional Integrations

Optional means optional.

- no accidental required metadata
- no accidental bundling
- dev runtimes may attach compatible optional mods
- test present/absent states when integration behavior changes

## Runtime ABI

When runtime libraries/loaders differ:

1. identify actual runtime versions
2. compare good/bad combinations
3. pin the narrow dependency when proven
4. do not contort application source around an unproven loader/library issue

## Durable Boundary Log

Record only proven reusable boundaries:

```text
Boundary:
Affected targets:
Affected files/system:
Mechanism:
Reason:
Validation:
```

Raw migration diaries do not belong here.

## Proven Extra Chests boundaries

- **1.20.1 vs 1.21:** reuse the existing legacy view. Only identifier construction,
  boat-model RGBA arguments, creative stack consumption and serialized data drift
  need extra handling (`gradle/compat/1.20.1.gradle`). Recipes/loot/advancements and
  block/item tags use plural directories; result `id` becomes `item`, and advancement
  item predicates use arrays. Mixin Java level must come from the target pin.
- **Legacy item rendering:** Fabric uses `BuiltinItemRendererRegistry`; Forge
  uses `Item.initializeClient`/`IClientItemExtensions`; legacy NeoForge uses its
  `RegisterClientExtensionsEvent`. Retain the proven 3-D chest model orientation
  and full chest-boat/raft models, not a flat item texture or bare boat model.
- **26.3 rendering:** submit the chest model and breaking overlay separately;
  the water-patch submission no longer takes a trailing overlay argument. Two
  exact, asserted renderer substitutions live in `gradle/compat/26.3.gradle`.
  Recipe-unlocked criteria use `recipes` arrays, not a single `recipe`.
- **Registration:** shared `Identifier` owns naming; `ModProperties` creates fresh
  block/item properties with matching registered IDs. The legacy view removes
  exactly its two unsupported key setters. Forge must register its Architectury
  event bus before common registration. Keep saved IDs and deferred lookups intact.
- **Early NeoForge:** retain the existing 26.1/26.1.1 repair. Do not copy or broaden
  another mod's fix; 26.3 excludes that obsolete mixin/config entirely. Its standalone
  node uses Architectury's compile-only transformation mode, not a runtime transformer.
