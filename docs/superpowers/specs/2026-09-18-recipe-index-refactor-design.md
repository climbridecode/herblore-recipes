# Recipe Index Refactor — Design

Date: 2026-09-18
Branch: `refactor/recipe-index`

## Goal

Restructure how the plugin models herblore recipes and builds tooltips so that
adding a feature (like the Mastering Mixology pastes) does not require touching
six parallel maps, three classes and a string-matching special case.

As part of the restructure, fix the Mixology paste tooltip so it works on every
form of a herb.

## Problems in the current code

- **Parallel maps per role.** `Potions` keeps six maps (primary, secondary,
  complex base, unfinished, seed, grimy), each with its own `isX` and `getByX`
  methods, and `TooltipCache.preLoadCache` has a matching branch for each.
- **Pastes are hacked into the potion model.** Pastes are 15 fake `Potion`
  entries (one per herb) detected by `name.contains(" paste")` in
  `Potions.getPastes` and `TooltipCache`.
- **Mutation bug.** `TooltipCache` calls `ps.remove(paste)` on the list returned
  by `Potions.getByPrimary`, which is the list stored in the static map. After
  the first cache build the paste is gone from that herb permanently, so any
  config change (which calls `reset()`) rebuilds herb tooltips without the paste
  section.
- **Paste coverage gaps.** Paste appears as its own section on clean herbs only.
  On grimy herbs it is mixed into "Clean for:" with no paste label. Seeds never
  show it.
- **Multi-pass tooltip assembly.** `Potions` → `TooltipBox` / `TooltipCategory` /
  `TooltipCategoryContent` → `TooltipStringBuilder` → string, all inside
  `TooltipCache` alongside data lookup and config checks. Config gating is split
  between the overlay (seed/unfinished/grimy) and the cache (the rest).
- **No tests** check tooltip content.

## Decisions made

- Approach A: keep the Java `ItemID`-based data, unify the model around item
  forms and roles. (Rejected: JSON data file — loses compile-time `ItemID`
  checking for little gain; minimal patch — leaves the structure that made
  Mixology painful.)
- Paste tooltips appear on **all forms** of a herb: clean, grimy and seed.
- **All existing config keys, names and defaults stay unchanged**, so saved user
  settings keep working. No new paste toggle.
- Regression protection is **characterization tests first**, written against the
  current code before any refactoring.

## Design

### 1. Data model

- `Potion` and the `Potions` enum data are unchanged, except that the 15 fake
  paste entries are deleted.
- New `Paste` enum: `MOX`, `LYE`, `AGA`. Each has its paste item ID and a list
  of herb recipes. A recipe is `(clean herb ID, grimy ID, level)`. Recipes carry
  their own grimy ID because some paste herbs (e.g. Huasca) may not be primary
  ingredients of any potion. Existing levels (including 60 for Huasca and Dwarf
  weed) are preserved.
- New `HerbForms` value groups the IDs that count as the same ingredient: clean,
  grimy, seed and unfinished potion. The index builds it once per herb from
  potion and paste data.
- New `ItemRole` enum: `PRIMARY`, `SECONDARY`, `SEED`, `GRIMY`, `UNFINISHED`,
  `COMPLEX_BASE`, `PASTE`, `POTION`. It replaces the six parallel maps and their
  `isX` / `getByX` methods.
- Open point for the plan: a seed ID for paste-only herbs (Huasca has one in
  game). If `ItemID` defines it, wire it in; otherwise that herb has no seed
  tooltip.

### 2. The index

- `Recipe` is a small read-only view (name, level, primary item, base,
  secondaries). `Potion` and paste recipes both adapt to it, so rendering never
  needs to ask whether something is a paste.
- `RecipeIndex` is built once and immutable. It maps
  `itemId → form → section → List<Recipe>` and exposes `knownIds()` and a way to
  read the sections for an item. Immutable lists make the mutation bug
  impossible.
- The index is built with one generic pass. Each rule is one line: a potion's
  primary is indexed under the herb's clean ID, and likewise for grimy, seed,
  unfinished, secondaries (plus alternate secondaries) and complex bases. Paste
  recipes add a `PASTE` section under the clean, grimy and seed IDs of the herb,
  using `HerbForms`.
- Two special cases remain, each in one visible place:
  - Imp repellent's flowers are indexed as `PRIMARY`, as today.
  - A paste item's own ID gets a "To make <paste>:" section listing every herb
    recipe for it.
- Section order is fixed and matches today's tooltips: potion, paste, primary,
  secondary, complex base, seed, unfinished, grimy.
- `Potions` shrinks to just the enum data. The maps and lookup methods move to
  the index.

### 3. Rendering, config gating, overlay

- One pure `TooltipRenderer` replaces `TooltipBox`, `TooltipCategory`,
  `TooltipCategoryContent`, `TooltipStringBuilder` and the string assembly in
  `TooltipCache`. It takes an item ID, the index, an `IntFunction<String>` for
  item names and a snapshot of the display options, and returns the tooltip
  text or nothing. It does no I/O.
- Section titles and colors live in one table.
- Line formatting is one small function producing
  `lvl N: Name (1st: … - 2nd: …)` from a list of parts. Output is identical to
  today's. The imp repellent "Various flowers..." rule stays as one named rule.
- Config gating happens in one place. Each index entry records the **form** the
  hovered item is (clean herb, grimy, seed, unfinished, secondary, complex base,
  potion), and the visibility toggle is looked up by form. So the paste section
  on a grimy herb follows the grimy toggle, on a seed the seed toggle, on a
  clean herb the primaries toggle, and on a paste item the potions toggle.
- `TooltipCache` becomes a thin holder of `Map<Integer, Tooltip>`, rebuilt on
  the client thread at login and on any config change. `onConfigChanged`
  resets on any change in the `herblorerecipes` group instead of matching an
  eight-key list.
- The overlay drops its per-form `isSeed` / `isUnfinished` / `isGrimy` checks.
  Its keybind and interface-group logic is unchanged.
- **Deliberate behavior change:** the seed toggle now hides only the seed
  section, not the whole tooltip for that item.

### 4. Testing and migration order

1. Add Mockito as a test dependency. Write characterization tests against the
   **current** code, using test doubles for `ItemManager`, `ClientThread` and
   the config. Item names come from reflecting over `ItemID` constants
   (id → `GUAM_LEAF`). Dump tooltips for every known item ID under default
   config and a few toggled configs into golden files under
   `src/test/resources`. Commit before any refactoring.
2. Build the new model (`Paste`, `HerbForms`, `ItemRole`, `Recipe`,
   `RecipeIndex`, `TooltipRenderer`), rewire `TooltipCache`, the overlay and the
   plugin, and delete the old DTOs and parallel maps.
3. Review the golden diff. The only differences should be the intended ones:
   paste on grimy herbs and seeds, a proper "Paste for:" label on grimy, and
   the narrower seed toggle. Update the golden files in a separate commit.
4. Add invariant tests for the new code:
   - Building or resetting the cache twice gives identical output
     (regression test for the mutation bug).
   - Index lists are immutable.
   - Every herb with a paste shows it on its clean, grimy and seed IDs.
   - Every ID in the recipe data resolves to at least one section.
5. Cleanup: update the README if needed and confirm `./gradlew build` passes.
   The `build.gradle` version is left alone unless a bump is requested.

## Risks and open notes

- Mockito version compatibility with RuneLite's `latest.release` client.
- CI (`.github/workflows/build.yml`) runs `./gradlew build.gradle`, which
  looks like it may not run the `build` task or the tests. To be checked in the
  plan; not changed without asking.
- Level 60 for Huasca and Dwarf weed pastes is carried over from the existing
  data, not re-verified against the game.

## Implementation notes

Where the implementation refined the design above:

- There are 15 fake paste potions, not 16 (4 Mox, 5 Lye, 6 Aga).
- A paste herb recipe is `(clean herb ID, level)`, not `(clean, grimy, level)`. Every paste herb (Huasca included) is a
  primary of a real potion, so its grimy and seed IDs come from `HerbForms`. That also gives Huasca a seed tooltip.
- `Recipe` has no base field: the base was computed for every line but never displayed.
- `ItemRole` has an extra value, `PASTE_RECIPES`, for the paste item's own "To make <paste>:" section. A `Section` is
  `(role, gate, recipes)`: `role` decides title and order, `gate` decides which config toggle applies.
- `TooltipRenderer` reads the config interface directly instead of a separate options snapshot.
- Two existing bugs surfaced while snapshotting and are fixed: item 0 ("Dwarf remains") had a bogus tooltip because
  potions without a primary ingredient were indexed under id 0, and stackable nightshade listed Weapon poison++ twice
  because it is both the primary and the alternate primary.
- The characterization snapshots taken before the refactor were order-dependent: the old reset bug (ps.remove(paste) mutating shared static lists) had already stripped the clean-herb paste section for every config except the one whose test ran first. The regenerated snapshots therefore show 45 new "Paste for:" sections in the default and no-ingredients configs (clean herb, grimy and seed) and 30 in the other two, rather than 30 everywhere.
- Mockito 4.11.0 works with the RuneLite client on Java 11. The CI command concern below is unchanged.
