# Recipe Index Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the plugin's six parallel role maps, string-matched paste hack and multi-pass tooltip assembly with one immutable `RecipeIndex` and one pure `TooltipRenderer`, and make Mixology pastes show on clean herbs, grimy herbs and seeds.

**Architecture:** `Potions` keeps only the recipe data. `Paste` (new enum) holds the three pastes and their herbs. `RecipeIndex` is built once from both and maps `itemId → sections (role, gate, recipes)`. `TooltipRenderer` turns an item's sections into tooltip text, applying config toggles by each section's `gate`. `TooltipCache` shrinks to a holder that renders every known item on the client thread.

**Tech Stack:** Java 11, Gradle (wrapper), Lombok 1.18.30, RuneLite client API, JUnit 4.12, Mockito 4.11.0 (test only).

**Spec:** `docs/superpowers/specs/2026-09-18-recipe-index-refactor-design.md` (Task 7 records the small places where implementation refined it).

## Global Constraints

- **Branch:** all work happens on `refactor/recipe-index` (already created, spec already committed). Do not push and do not open a PR.
- **Config unchanged:** `HerbloreRecipesConfig.java` is not modified. Same keys, names and defaults. No new paste toggle.
- **Runtime dependencies unchanged.** The only build change is `testImplementation 'org.mockito:mockito-core:4.11.0'`.
- **Java 11** (`options.release.set(11)` in `build.gradle`): no `var`, no records, no switch expressions, no `List.of`. Lombok is available in `src/main` only.
- **Code style:** tabs, braces on their own line, matching the surrounding files.
- **Running Gradle:** no JDK is on `PATH` and `gradlew` is not executable in git. In every shell first run
  `export JAVA_HOME=$HOME/.jdks/temurin-11.0.31`, and always invoke `bash ./gradlew ...` from the repo root.
- **Commits:** end every commit message with the line `Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>`.
- **Golden files:** `src/test/resources/golden/*.txt` are snapshots of every tooltip. They are written with the environment variable `UPDATE_GOLDEN=1` and otherwise compared. **`TooltipGoldenTest` is expected to fail from Task 3 (step 1) until Task 6 regenerates it**, because the intended behaviour change (paste on grimy herbs and seeds) alters the output. In Tasks 3-5 run the test filters given in each step, not the whole suite, unless the step says otherwise.
- **Paste data facts** (verified against the current code): there are 15 fake paste potions in `Potions.java` (4 Mox, 5 Lye, 6 Aga; Huasca and Dwarf weed are level 60, the rest level 1). Every paste herb is also a primary of a real potion, so its grimy and seed IDs come from potion data.

## File Structure

| File | Responsibility |
|---|---|
| `src/main/java/com/herblorerecipes/model/Potions.java` | Modify: recipe data only (fake paste entries, maps and lookup methods removed) |
| `src/main/java/com/herblorerecipes/model/Potion.java` | Modify: drop unused `complexBaseNames()` |
| `src/main/java/com/herblorerecipes/model/Paste.java` | Create: the three pastes and their herbs |
| `src/main/java/com/herblorerecipes/model/Recipe.java` | Create: read-only view of one recipe, adapted from a `Potion` or a paste herb |
| `src/main/java/com/herblorerecipes/model/HerbForms.java` | Create: clean/grimy/seed/unfinished IDs of one herb |
| `src/main/java/com/herblorerecipes/tooltip/ItemRole.java` | Create: section kinds, in display order |
| `src/main/java/com/herblorerecipes/tooltip/Section.java` | Create: one titled group of recipes (role, gate, recipes) |
| `src/main/java/com/herblorerecipes/tooltip/RecipeIndex.java` | Create: immutable `itemId → sections` |
| `src/main/java/com/herblorerecipes/tooltip/TooltipRenderer.java` | Create: pure sections → tooltip text, incl. config gating |
| `src/main/java/com/herblorerecipes/cache/TooltipCache.java` | Rewrite: thin holder of rendered tooltips |
| `src/main/java/com/herblorerecipes/HerbloreRecipesOverlay.java` | Modify: drop per-form checks |
| `src/main/java/com/herblorerecipes/HerbloreRecipesPlugin.java` | Modify: reset on any config change in the group |
| `src/main/java/com/herblorerecipes/model/Tooltip{Box,Category,CategoryContent,StringBuilder}.java` | Delete |
| `src/test/java/com/herblorerecipes/{TestConfig,TooltipHarness,GoldenFile,TooltipGoldenTest,TooltipCacheTest}.java` | Create: test support and cache/golden tests |
| `src/test/java/com/herblorerecipes/model/{PasteTest,HerbFormsTest,RecipeTest}.java` | Create |
| `src/test/java/com/herblorerecipes/tooltip/{RecipeIndexTest,TooltipRendererTest}.java` | Create |
| `src/test/resources/golden/*.txt` | Create: four snapshot files |

---

### Task 1: Characterization tests against the current code

Capture what the plugin does today, before touching it.

**Files:**
- Modify: `build.gradle`
- Create: `src/test/java/com/herblorerecipes/TestConfig.java`
- Create: `src/test/java/com/herblorerecipes/TooltipHarness.java`
- Create: `src/test/java/com/herblorerecipes/GoldenFile.java`
- Create: `src/test/java/com/herblorerecipes/TooltipGoldenTest.java`
- Create: `src/test/resources/golden/{default,no-ingredients,imp-repellent-ingredients,ingredient-tooltips-off}.txt` (generated)

**Interfaces:**
- Produces:
  - `TestConfig implements HerbloreRecipesConfig` with public boolean fields `potions, primaries, secondaries, complex, grimy, unfinished, seeds, level, primaryIngredients, secondaryIngredients, impRepellentIngredients` (defaults equal the real config's).
  - `TooltipHarness.cache(HerbloreRecipesConfig): TooltipCache` (built and preloaded against test doubles), `TooltipHarness.dump(HerbloreRecipesConfig): List<String>`, `TooltipHarness.dump(TooltipCache): List<String>`. Item names are `"#<itemId>"`.
  - `GoldenFile.check(String name, List<String> actual)`.
- Consumes: the existing `TooltipCache(ItemManager, ClientThread, HerbloreRecipesConfig)` constructor, `preloadOnClientThread()`, `contains(int)`, `get(int).getText()`. **These signatures must not change in later tasks**, since this harness is reused unchanged against the new code.

- [ ] **Step 1: Add Mockito as a test dependency**

In `build.gradle`, directly under the `testImplementation 'junit:junit:4.12'` line add:

```groovy
    testImplementation 'org.mockito:mockito-core:4.11.0'
```

- [ ] **Step 2: Create the test support and golden test**

`src/test/java/com/herblorerecipes/TestConfig.java`:

```java
package com.herblorerecipes;

/** A {@link HerbloreRecipesConfig} whose display options can be flipped field by field. Defaults match the real config. */
public class TestConfig implements HerbloreRecipesConfig
{

	public boolean potions = true;
	public boolean primaries = true;
	public boolean secondaries = true;
	public boolean complex = true;
	public boolean grimy = true;
	public boolean unfinished = true;
	public boolean seeds = true;
	public boolean level = true;
	public boolean primaryIngredients = true;
	public boolean secondaryIngredients = true;
	public boolean impRepellentIngredients = false;

	@Override
	public boolean showTooltipOnPotions()
	{
		return potions;
	}

	@Override
	public boolean showTooltipOnPrimaries()
	{
		return primaries;
	}

	@Override
	public boolean showTooltipOnSecondaries()
	{
		return secondaries;
	}

	@Override
	public boolean showTooltipOnComplex()
	{
		return complex;
	}

	@Override
	public boolean showTooltipOnGrimy()
	{
		return grimy;
	}

	@Override
	public boolean showTooltipOnUnfinished()
	{
		return unfinished;
	}

	@Override
	public boolean showTooltipOnPrimarySeeds()
	{
		return seeds;
	}

	@Override
	public boolean showHerbloreLvlInTooltip()
	{
		return level;
	}

	@Override
	public boolean showPrimariesInTooltip()
	{
		return primaryIngredients;
	}

	@Override
	public boolean showSecondariesInTooltip()
	{
		return secondaryIngredients;
	}

	@Override
	public boolean showImpRepellentIngs()
	{
		return impRepellentIngredients;
	}
}
```

`src/test/java/com/herblorerecipes/TooltipHarness.java`:

```java
package com.herblorerecipes;

import com.herblorerecipes.cache.TooltipCache;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.runelite.api.ItemComposition;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;

/**
 * Builds a {@link TooltipCache} against test doubles and dumps every tooltip it produces.
 * Item names are the numeric item id ("#249"), which is stable across RuneLite updates.
 */
public final class TooltipHarness
{
	private TooltipHarness()
	{
	}

	public static TooltipCache cache(HerbloreRecipesConfig config)
	{
		TooltipCache cache = new TooltipCache(itemManager(), immediateClientThread(), config);
		cache.preloadOnClientThread();
		return cache;
	}

	public static List<String> dump(HerbloreRecipesConfig config)
	{
		return dump(cache(config));
	}

	public static List<String> dump(TooltipCache cache)
	{

		List<String> lines = new ArrayList<>();
		for (int id : allItemIds())
		{
			if (cache.contains(id))
			{
				lines.add("== " + id);
				for (String line : cache.get(id).getText().split("</br>", -1))
				{
					lines.add(line);
				}
			}
		}
		return lines;
	}

	private static ItemManager itemManager()
	{
		ItemManager itemManager = mock(ItemManager.class);
		Map<Integer, ItemComposition> compositions = new HashMap<>();
		when(itemManager.getItemComposition(anyInt())).thenAnswer(invocation ->
		{
			int id = invocation.getArgument(0);
			return compositions.computeIfAbsent(id, i ->
			{
				ItemComposition composition = mock(ItemComposition.class);
				when(composition.getName()).thenReturn("#" + i);
				return composition;
			});
		});
		return itemManager;
	}

	private static ClientThread immediateClientThread()
	{
		ClientThread clientThread = mock(ClientThread.class);
		doAnswer(invocation ->
		{
			((Runnable) invocation.getArgument(0)).run();
			return null;
		}).when(clientThread).invoke(any(Runnable.class));
		return clientThread;
	}

	/** Every item id the game defines, independent of what the plugin knows about. */
	private static Iterable<Integer> allItemIds()
	{
		TreeSet<Integer> ids = new TreeSet<>();
		for (Field field : ItemID.class.getFields())
		{
			if (field.getType() == int.class && Modifier.isStatic(field.getModifiers()))
			{
				try
				{
					int id = field.getInt(null);
					if (id >= 0)
					{
						ids.add(id);
					}
				}
				catch (IllegalAccessException e)
				{
					throw new IllegalStateException(e);
				}
			}
		}
		return ids;
	}
}
```

`src/test/java/com/herblorerecipes/GoldenFile.java`:

```java
package com.herblorerecipes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Compares output against a checked-in file. Run the tests with the environment variable
 * UPDATE_GOLDEN=1 to (re)write the files instead of comparing, then review the diff in git.
 */
final class GoldenFile
{

	private static final Path DIR = Paths.get("src/test/resources/golden");

	private GoldenFile()
	{
	}

	static void check(String name, List<String> actual) throws IOException
	{
		Path file = DIR.resolve(name + ".txt");
		if (System.getenv("UPDATE_GOLDEN") != null)
		{
			Files.createDirectories(DIR);
			Files.write(file, actual);
			return;
		}
		if (!Files.exists(file))
		{
			fail("Missing golden file " + file + ". Run the tests with UPDATE_GOLDEN=1 to create it.");
		}

		List<String> expected = Files.readAllLines(file);
		for (int i = 0; i < Math.min(expected.size(), actual.size()); i++)
		{
			if (!expected.get(i).equals(actual.get(i)))
			{
				fail(String.format("%s differs at line %d (run with UPDATE_GOLDEN=1 to accept the change):%n  expected: %s%n  actual:   %s",
					name, i + 1, expected.get(i), actual.get(i)));
			}
		}
		assertEquals(name + ": number of lines", expected.size(), actual.size());
	}
}
```

`src/test/java/com/herblorerecipes/TooltipGoldenTest.java`:

```java
package com.herblorerecipes;

import java.io.IOException;
import org.junit.Test;

/**
 * Snapshot of every tooltip the plugin can show, under a few configs. Item names are numeric ids ("#249").
 */
public class TooltipGoldenTest
{

	@Test
	public void defaultConfig() throws IOException
	{
		GoldenFile.check("default", TooltipHarness.dump(new TestConfig()));
	}

	@Test
	public void noIngredientsOrLevels() throws IOException
	{
		TestConfig config = new TestConfig();
		config.level = false;
		config.primaryIngredients = false;
		config.secondaryIngredients = false;
		GoldenFile.check("no-ingredients", TooltipHarness.dump(config));
	}

	@Test
	public void impRepellentIngredientsShown() throws IOException
	{
		TestConfig config = new TestConfig();
		config.impRepellentIngredients = true;
		GoldenFile.check("imp-repellent-ingredients", TooltipHarness.dump(config));
	}

	@Test
	public void ingredientTooltipsOff() throws IOException
	{
		TestConfig config = new TestConfig();
		config.potions = false;
		config.primaries = false;
		config.secondaries = false;
		config.complex = false;
		GoldenFile.check("ingredient-tooltips-off", TooltipHarness.dump(config));
	}
}
```

- [ ] **Step 3: Run the test to verify it fails for lack of golden files**

Run: `bash ./gradlew test --tests '*TooltipGoldenTest'`
Expected: 4 tests FAIL with `Missing golden file src/test/resources/golden/default.txt. Run the tests with UPDATE_GOLDEN=1 to create it.` (and likewise for the other three).

- [ ] **Step 4: Generate the golden files from the current (unchanged) code**

Run: `UPDATE_GOLDEN=1 bash ./gradlew test --tests '*TooltipGoldenTest'`
Expected: `BUILD SUCCESSFUL`; four files exist in `src/test/resources/golden/`.

- [ ] **Step 5: Run again to verify the snapshots are deterministic**

Run: `bash ./gradlew test --tests '*TooltipGoldenTest' --rerun-tasks`
Expected: `BUILD SUCCESSFUL`, 4 tests pass.

- [ ] **Step 6: Sanity-check the snapshot contents**

Run:
```bash
grep -c '^== ' src/test/resources/golden/default.txt
grep -A2 '^== 0$' src/test/resources/golden/default.txt
grep -B1 -A1 'Mox paste' src/test/resources/golden/default.txt | head -12
```
Expected: `477` blocks; a `== 0` block whose title is `Primary for:` and lists mixes (this is a bug in the current code: item 0 is a real item, "Dwarf remains", that should have no tooltip; Task 3 fixes it); `Mox paste` lines appear under `Paste for:` and `Clean for:` headings. `ingredient-tooltips-off.txt` should have 54 blocks.

- [ ] **Step 7: Prove the paste-after-reset bug on the current code (do not commit this file)**

Create `src/test/java/com/herblorerecipes/ResetBug.java`:

```java
package com.herblorerecipes;

import com.herblorerecipes.cache.TooltipCache;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ResetBug
{
	@Test
	public void pasteSurvivesReset()
	{
		TooltipCache cache = TooltipHarness.cache(new TestConfig());
		assertTrue(cache.get(ItemID.GUAM_LEAF).getText().contains("Mox paste"));
		cache.reset();
		assertTrue("paste lost after reset", cache.get(ItemID.GUAM_LEAF).getText().contains("Mox paste"));
	}
}
```

Run: `bash ./gradlew test --tests '*ResetBug'`
Expected: FAIL with `paste lost after reset` (the first assertion passes, the second fails). That is the `ps.remove(paste)` mutation bug. Then delete the file: `rm src/test/java/com/herblorerecipes/ResetBug.java`.

- [ ] **Step 8: Commit**

```bash
git add build.gradle src/test
git commit -m "$(cat <<'EOF'
Add characterization tests for current tooltip output

Snapshot every tooltip under four configs, using Mockito test doubles
for ItemManager and ClientThread, before restructuring the plugin.

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

---

### Task 2: Data types `Paste`, `Recipe`, `HerbForms`

**Files:**
- Create: `src/main/java/com/herblorerecipes/model/Paste.java`
- Create: `src/main/java/com/herblorerecipes/model/Recipe.java`
- Create: `src/main/java/com/herblorerecipes/model/HerbForms.java`
- Test: `src/test/java/com/herblorerecipes/model/PasteTest.java`
- Test: `src/test/java/com/herblorerecipes/model/HerbFormsTest.java`
- Test: `src/test/java/com/herblorerecipes/model/RecipeTest.java`

**Interfaces:**
- Produces:
  - `enum Paste { MOX, LYE, AGA }` with `getName(): String`, `getItemId(): int`, `getHerbs(): List<Paste.HerbRecipe>`; `Paste.HerbRecipe` has `getHerb(): int` (clean herb item id) and `getLevel(): int`.
  - `Recipe(String name, int level, int primary, Set<Integer> secondaries, boolean collapsibleSecondaries)` (Lombok `@Value`), with `Recipe.of(Potion)` and `Recipe.of(Paste, Paste.HerbRecipe)`. `primary` is 0 when there is none; `secondaries` is never null.
  - `HerbForms(int clean, int grimy, int seed, int unfinished)` (Lombok `@Value`; 0 = none) with `static Map<Integer, HerbForms> fromPotions(Collection<Potion>)`, keyed by clean herb item id, only for primaries that have at least one other form.

- [ ] **Step 1: Write the failing tests**

`src/test/java/com/herblorerecipes/model/PasteTest.java`:

```java
package com.herblorerecipes.model;

import java.util.HashSet;
import java.util.Set;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PasteTest
{

	@Test
	public void eachPasteListsItsHerbs()
	{
		assertEquals(4, Paste.MOX.getHerbs().size());
		assertEquals(5, Paste.LYE.getHerbs().size());
		assertEquals(6, Paste.AGA.getHerbs().size());
	}

	@Test
	public void noHerbMakesTwoPastes()
	{
		Set<Integer> herbs = new HashSet<>();
		int total = 0;
		for (Paste paste : Paste.values())
		{
			for (Paste.HerbRecipe recipe : paste.getHerbs())
			{
				herbs.add(recipe.getHerb());
				total++;
			}
		}
		assertEquals(total, herbs.size());
	}

	@Test
	public void onlyHuascaAndDwarfWeedNeedLevel60()
	{
		for (Paste paste : Paste.values())
		{
			for (Paste.HerbRecipe recipe : paste.getHerbs())
			{
				boolean high = recipe.getHerb() == ItemID.HUASCA || recipe.getHerb() == ItemID.DWARF_WEED;
				assertEquals(paste.getName() + " from " + recipe.getHerb(), high ? 60 : 1, recipe.getLevel());
			}
		}
	}
}
```

`src/test/java/com/herblorerecipes/model/HerbFormsTest.java`:

```java
package com.herblorerecipes.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class HerbFormsTest
{

	private static final List<Potion> POTIONS = Arrays.stream(Potions.values()).map(p -> p.potion).collect(Collectors.toList());
	private static final Map<Integer, HerbForms> HERBS = HerbForms.fromPotions(POTIONS);

	@Test
	public void groupsAllFormsOfAHerb()
	{
		assertEquals(new HerbForms(ItemID.GUAM_LEAF, ItemID.UNIDENTIFIED_GUAM, ItemID.GUAM_SEED, ItemID.GUAMVIAL),
			HERBS.get(ItemID.GUAM_LEAF));
	}

	@Test
	public void mergesFormsSpreadAcrossPotions()
	{
		// torstol is the primary of several potions, and only some of them name its grimy, seed and unfinished forms
		assertEquals(new HerbForms(ItemID.TORSTOL, ItemID.UNIDENTIFIED_TORSTOL, ItemID.TORSTOL_SEED, ItemID.TORSTOLVIAL),
			HERBS.get(ItemID.TORSTOL));
	}

	@Test
	public void ingredientsWithNoOtherFormsAreNotHerbs()
	{
		assertNull(HERBS.get(ItemID.UNICORN_HORN_DUST));
	}
}
```

`src/test/java/com/herblorerecipes/model/RecipeTest.java`:

```java
package com.herblorerecipes.model;

import java.util.Collections;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RecipeTest
{

	@Test
	public void potionRecipeCopiesItsIngredients()
	{
		Recipe recipe = Recipe.of(Potions.ATTACK_POTION.potion);

		assertEquals("Attack potion", recipe.getName());
		assertEquals(3, recipe.getLevel());
		assertEquals(ItemID.GUAM_LEAF, recipe.getPrimary());
		assertEquals(Collections.singleton(ItemID.EYE_OF_NEWT), recipe.getSecondaries());
		assertFalse(recipe.isCollapsibleSecondaries());
	}

	@Test
	public void potionWithoutSecondariesHasAnEmptySetNotNull()
	{
		assertTrue(Recipe.of(Potions.SUPER_COMBAT_POTION.potion).getSecondaries().isEmpty());
	}

	@Test
	public void impRepellentHasNoPrimaryAndCollapsibleSecondaries()
	{
		Recipe recipe = Recipe.of(Potions.IMP_REPELLENT.potion);

		assertEquals(0, recipe.getPrimary());
		assertTrue(recipe.getSecondaries().contains(ItemID.MARIGOLD));
		assertTrue(recipe.isCollapsibleSecondaries());
	}

	@Test
	public void pasteRecipeIsNamedAfterThePasteAndUsesTheHerbAsPrimary()
	{
		Recipe recipe = Recipe.of(Paste.AGA, Paste.AGA.getHerbs().get(1));

		assertEquals("Aga paste", recipe.getName());
		assertEquals(60, recipe.getLevel());
		assertEquals(ItemID.HUASCA, recipe.getPrimary());
		assertTrue(recipe.getSecondaries().isEmpty());
		assertFalse(recipe.isCollapsibleSecondaries());
	}
}
```

- [ ] **Step 2: Run to verify they fail**

Run: `bash ./gradlew test --tests '*PasteTest' --tests '*HerbFormsTest' --tests '*RecipeTest'`
Expected: `compileTestJava FAILED` with `cannot find symbol ... class Paste` (and `Recipe`, `HerbForms`).

- [ ] **Step 3: Write the implementation**

`src/main/java/com/herblorerecipes/model/Paste.java`:

```java
package com.herblorerecipes.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import lombok.Value;
import net.runelite.api.gameval.ItemID;

/**
 * The Mastering Mixology pastes and the herbs each one is ground from.
 * A herb's grimy and seed forms are not listed here; they are looked up through {@link HerbForms}.
 */
@Getter
public enum Paste
{

	MOX("Mox paste", ItemID.MM_MOX_PASTE, ImmutableList.of(
		herb(ItemID.GUAM_LEAF, 1),
		herb(ItemID.MARENTILL, 1),
		herb(ItemID.TARROMIN, 1),
		herb(ItemID.HARRALANDER, 1))),

	LYE("Lye paste", ItemID.MM_LYE_PASTE, ImmutableList.of(
		herb(ItemID.RANARR_WEED, 1),
		herb(ItemID.TOADFLAX, 1),
		herb(ItemID.AVANTOE, 1),
		herb(ItemID.KWUARM, 1),
		herb(ItemID.SNAPDRAGON, 1))),

	AGA("Aga paste", ItemID.MM_AGA_PASTE, ImmutableList.of(
		herb(ItemID.IRIT_LEAF, 1),
		herb(ItemID.HUASCA, 60),
		herb(ItemID.CADANTINE, 1),
		herb(ItemID.LANTADYME, 1),
		herb(ItemID.DWARF_WEED, 60),
		herb(ItemID.TORSTOL, 1)));

	private final String name;
	private final int itemId;
	private final List<HerbRecipe> herbs;

	Paste(String name, int itemId, List<HerbRecipe> herbs)
	{
		this.name = name;
		this.itemId = itemId;
		this.herbs = herbs;
	}

	private static HerbRecipe herb(int cleanHerbId, int level)
	{
		return new HerbRecipe(cleanHerbId, level);
	}

	/** One way to make the paste: grind this (clean or grimy) herb. */
	@Value
	public static class HerbRecipe
	{
		int herb;
		int level;
	}
}
```

`src/main/java/com/herblorerecipes/model/Recipe.java`:

```java
package com.herblorerecipes.model;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.Value;
import net.runelite.api.gameval.ItemID;

/**
 * What a tooltip line needs to know about one way of making something. Both {@link Potion}s and
 * {@link Paste} recipes adapt to this, so rendering never has to care which one it is showing.
 */
@Value
public class Recipe
{

	String name;
	int level;
	/** Item id of the single primary ingredient, or 0 when there is none. */
	int primary;
	/** Never null; empty when the recipe has no secondaries. */
	Set<Integer> secondaries;
	/** True when the secondaries are so numerous they can be summarised (imp repellent's flowers). */
	boolean collapsibleSecondaries;

	public static Recipe of(Potion potion)
	{
		Set<Integer> secondaries = potion.getSecondaries() == null ? ImmutableSet.of() : potion.getSecondaries();
		boolean collapsible = potion.getIds().contains(ItemID.II_IMP_REPELLENT);
		return new Recipe(potion.getName(), potion.getLevel(), potion.getPrimary(), secondaries, collapsible);
	}

	public static Recipe of(Paste paste, Paste.HerbRecipe herb)
	{
		return new Recipe(paste.getName(), herb.getLevel(), herb.getHerb(), ImmutableSet.of(), false);
	}
}
```

`src/main/java/com/herblorerecipes/model/HerbForms.java`:

```java
package com.herblorerecipes.model;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Value;

/** The item ids that count as "the same herb": clean, grimy, seed and unfinished potion. 0 means none. */
@Value
public class HerbForms
{

	int clean;
	int grimy;
	int seed;
	int unfinished;

	/** Groups the forms of every herb, keyed by clean herb item id, from the potions that use it as a primary. */
	public static Map<Integer, HerbForms> fromPotions(Collection<Potion> potions)
	{
		Map<Integer, HerbForms> forms = new LinkedHashMap<>();
		for (Potion potion : potions)
		{
			if (potion.getPrimary() <= 0)
			{
				continue;
			}
			HerbForms found = new HerbForms(potion.getPrimary(), potion.getGrimyHerb(), potion.getPrimarySeed(), potion.getUnfinishedPotion());
			if (found.grimy <= 0 && found.seed <= 0 && found.unfinished <= 0)
			{
				// not a herb: an ingredient with no other forms
				continue;
			}
			forms.merge(found.clean, found, HerbForms::combine);
		}
		return ImmutableMap.copyOf(forms);
	}

	private HerbForms combine(HerbForms other)
	{
		return new HerbForms(clean, firstSet(grimy, other.grimy), firstSet(seed, other.seed), firstSet(unfinished, other.unfinished));
	}

	private static int firstSet(int a, int b)
	{
		return a > 0 ? a : b;
	}
}
```

- [ ] **Step 4: Run to verify they pass, and that nothing else moved**

Run: `bash ./gradlew test`
Expected: `BUILD SUCCESSFUL`. All tests pass, including `TooltipGoldenTest` (the old code path is untouched).

- [ ] **Step 5: Commit**

```bash
git add src
git commit -m "$(cat <<'EOF'
Add Paste, Recipe and HerbForms model types

Paste holds the three Mixology pastes and their herbs. Recipe is a
read-only view that potions and paste recipes both adapt to. HerbForms
groups the clean, grimy, seed and unfinished ids of a herb.

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

---

### Task 3: `RecipeIndex`

Also removes the 15 fake paste potions, because the index now represents pastes through `Paste`. **From this task's step 1, `TooltipGoldenTest` is expected to fail** (see Global Constraints).

**Files:**
- Modify: `src/main/java/com/herblorerecipes/model/Potions.java` (delete the paste block)
- Create: `src/main/java/com/herblorerecipes/tooltip/ItemRole.java`
- Create: `src/main/java/com/herblorerecipes/tooltip/Section.java`
- Create: `src/main/java/com/herblorerecipes/tooltip/RecipeIndex.java`
- Test: `src/test/java/com/herblorerecipes/tooltip/RecipeIndexTest.java`

**Interfaces:**
- Consumes: `Potions.values()[i].potion`, `Paste`, `Recipe`, `HerbForms` from Task 2.
- Produces:
  - `enum ItemRole { POTION, PASTE_RECIPES, PASTE, PRIMARY, SECONDARY, COMPLEX_BASE, SEED, UNFINISHED, GRIMY }`, where declaration order is display order.
  - `Section(ItemRole role, ItemRole gate, List<Recipe> recipes)` (Lombok `@Value`). `role` decides title and position; `gate` decides which config toggle applies (`POTION, PRIMARY, SECONDARY, COMPLEX_BASE, SEED, UNFINISHED` or `GRIMY`, never `PASTE`/`PASTE_RECIPES`).
  - `RecipeIndex.build(): RecipeIndex`, `knownIds(): Set<Integer>`, `sectionsOf(int itemId): List<Section>` (empty list for unknown ids; everything immutable).

- [ ] **Step 1: Delete the fake paste potions from `Potions.java`**

Run:
```bash
sed -i '/^\t\/\/ MIXOLOGY PASTES$/,/^\t\/\/ NEW POTIONS (2025)$/{/^\t\/\/ NEW POTIONS (2025)$/!d}' src/main/java/com/herblorerecipes/model/Potions.java
grep -c 'name("[A-Za-z]* paste")' src/main/java/com/herblorerecipes/model/Potions.java
grep -n -B4 'NEW POTIONS (2025)' src/main/java/com/herblorerecipes/model/Potions.java
```
Expected: `0` matches, and `// NEW POTIONS (2025)` still present, directly after a blank line that follows the previous potion's `.build()),`.

- [ ] **Step 2: Write the failing tests**

`src/test/java/com/herblorerecipes/tooltip/RecipeIndexTest.java`:

```java
package com.herblorerecipes.tooltip;

import com.herblorerecipes.model.HerbForms;
import com.herblorerecipes.model.Paste;
import com.herblorerecipes.model.Potion;
import com.herblorerecipes.model.Potions;
import com.herblorerecipes.model.Recipe;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RecipeIndexTest
{

	private static final RecipeIndex INDEX = RecipeIndex.build();
	private static final List<Potion> POTIONS = Arrays.stream(Potions.values()).map(p -> p.potion).collect(Collectors.toList());

	private static Optional<Section> section(int itemId, ItemRole role)
	{
		return INDEX.sectionsOf(itemId).stream().filter(s -> s.getRole() == role).findFirst();
	}

	private static List<String> names(Section section)
	{
		return section.getRecipes().stream().map(Recipe::getName).collect(Collectors.toList());
	}

	private static void assertPasteSection(int itemId, ItemRole gate, Paste paste)
	{
		Section section = section(itemId, ItemRole.PASTE)
			.orElseThrow(() -> new AssertionError("no paste section on item " + itemId + " for " + paste));
		assertEquals("gate on item " + itemId, gate, section.getGate());
		assertEquals("recipes on item " + itemId, Collections.singletonList(paste.getName()), names(section));
	}

	@Test
	public void herbShowsItsPasteOnCleanGrimyAndSeedForms()
	{
		Map<Integer, HerbForms> herbs = HerbForms.fromPotions(POTIONS);
		for (Paste paste : Paste.values())
		{
			for (Paste.HerbRecipe recipe : paste.getHerbs())
			{
				HerbForms forms = herbs.get(recipe.getHerb());
				assertNotNull("no forms for herb " + recipe.getHerb(), forms);
				assertPasteSection(forms.getClean(), ItemRole.PRIMARY, paste);
				assertPasteSection(forms.getGrimy(), ItemRole.GRIMY, paste);
				assertPasteSection(forms.getSeed(), ItemRole.SEED, paste);
			}
		}
	}

	@Test
	public void pasteItemListsEveryHerbThatMakesIt()
	{
		Section section = section(ItemID.MM_MOX_PASTE, ItemRole.PASTE_RECIPES).get();

		assertEquals(ItemRole.POTION, section.getGate());
		assertEquals(Arrays.asList(ItemID.GUAM_LEAF, ItemID.MARENTILL, ItemID.TARROMIN, ItemID.HARRALANDER),
			section.getRecipes().stream().map(Recipe::getPrimary).collect(Collectors.toList()));
	}

	@Test
	public void pasteIsNotMixedIntoTheHerbsPotions()
	{
		Section primary = section(ItemID.GUAM_LEAF, ItemRole.PRIMARY).get();

		assertTrue(names(primary).contains("Attack potion"));
		assertFalse(names(primary).contains("Mox paste"));
	}

	@Test
	public void sectionsAppearInDisplayOrder()
	{
		List<ItemRole> roles = INDEX.sectionsOf(ItemID.GUAM_LEAF).stream().map(Section::getRole).collect(Collectors.toList());

		assertEquals(Arrays.asList(ItemRole.PASTE, ItemRole.PRIMARY, ItemRole.SECONDARY), roles);
	}

	@Test
	public void potionItemsShowTheirOwnRecipe()
	{
		Section section = section(ItemID._4DOSE1ATTACK, ItemRole.POTION).get();

		assertEquals(Collections.singletonList("Attack potion"), names(section));
	}

	@Test
	public void impRepellentFlowersArePrimaries()
	{
		Section section = section(ItemID.MARIGOLD, ItemRole.PRIMARY).get();

		assertEquals(Collections.singletonList("Imp repellent"), names(section));
		assertTrue(section.getRecipes().get(0).isCollapsibleSecondaries());
		assertFalse(section(ItemID.MARIGOLD, ItemRole.SECONDARY).isPresent());
	}

	@Test
	public void aRecipeIsNotListedTwiceInOneSection()
	{
		// weapon poison++ names the same item as both its primary and alternate primary
		Section section = section(ItemID.STACKABLE_NIGHTSHADE, ItemRole.PRIMARY).get();

		assertEquals(1, names(section).stream().filter("Weapon poison++"::equals).count());
	}

	@Test
	public void unsetIngredientsAreNotIndexed()
	{
		// potions without a primary ingredient leave it as 0, which is also the id of a real item
		assertTrue(INDEX.sectionsOf(0).isEmpty());
		assertFalse(INDEX.knownIds().contains(0));
	}

	@Test
	public void everyIdInThePotionDataIsIndexed()
	{
		for (Potion potion : POTIONS)
		{
			potion.getIds().forEach(id -> assertIndexed(potion, id));
			assertIndexed(potion, potion.getPrimary());
			assertIndexed(potion, potion.getPrimaryAlt());
			assertIndexed(potion, potion.getGrimyHerb());
			assertIndexed(potion, potion.getPrimarySeed());
			assertIndexed(potion, potion.getUnfinishedPotion());
			if (potion.getSecondaries() != null)
			{
				potion.getSecondaries().forEach(id -> assertIndexed(potion, id));
			}
			if (potion.getSecondariesAlt() != null)
			{
				potion.getSecondariesAlt().forEach(id -> assertIndexed(potion, id));
			}
			if (potion.hasComplexBase())
			{
				potion.getComplexBase().forEach(base -> base.getIds().forEach(id -> assertIndexed(potion, id)));
			}
		}
	}

	private static void assertIndexed(Potion potion, int itemId)
	{
		if (itemId > 0)
		{
			assertFalse(potion.getName() + " uses item " + itemId + " which has no section", INDEX.sectionsOf(itemId).isEmpty());
		}
	}

	@Test
	public void noSectionIsEmpty()
	{
		for (int id : INDEX.knownIds())
		{
			assertFalse("item " + id, INDEX.sectionsOf(id).isEmpty());
			INDEX.sectionsOf(id).forEach(section -> assertFalse("item " + id + " " + section.getRole(), section.getRecipes().isEmpty()));
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void recipeListsCannotBeModified()
	{
		INDEX.sectionsOf(ItemID.GUAM_LEAF).get(0).getRecipes().clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void sectionListsCannotBeModified()
	{
		INDEX.sectionsOf(ItemID.GUAM_LEAF).clear();
	}
}
```

- [ ] **Step 3: Run to verify they fail**

Run: `bash ./gradlew test --tests '*RecipeIndexTest'`
Expected: `compileTestJava FAILED`, `cannot find symbol ... class RecipeIndex` (and `ItemRole`, `Section`).

- [ ] **Step 4: Write the implementation**

`src/main/java/com/herblorerecipes/tooltip/ItemRole.java`:

```java
package com.herblorerecipes.tooltip;

/**
 * The kinds of tooltip section an item can have. Declaration order is the order sections appear in a tooltip.
 */
public enum ItemRole
{
	/** The item is a potion: its own recipe. */
	POTION,
	/** The item is a paste: the herbs it can be made from. */
	PASTE_RECIPES,
	/** The item is a herb (any form): the paste it can be ground into. */
	PASTE,
	/** The item is a primary ingredient. */
	PRIMARY,
	/** The item is a secondary ingredient. */
	SECONDARY,
	/** The item is a potion used as the base of another potion. */
	COMPLEX_BASE,
	/** The item is a herb seed. */
	SEED,
	/** The item is an unfinished potion. */
	UNFINISHED,
	/** The item is a grimy herb. */
	GRIMY
}
```

`src/main/java/com/herblorerecipes/tooltip/Section.java`:

```java
package com.herblorerecipes.tooltip;

import com.herblorerecipes.model.Recipe;
import java.util.List;
import lombok.Value;

/** One titled group of recipes in an item's tooltip. */
@Value
public class Section
{

	/** Decides the section's title and its position in the tooltip. */
	ItemRole role;
	/** The role the hovered item plays; the config toggle for that role decides whether the section is shown. */
	ItemRole gate;
	List<Recipe> recipes;
}
```

`src/main/java/com/herblorerecipes/tooltip/RecipeIndex.java`:

```java
package com.herblorerecipes.tooltip;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.herblorerecipes.model.HerbForms;
import com.herblorerecipes.model.Paste;
import com.herblorerecipes.model.Potion;
import com.herblorerecipes.model.Potions;
import com.herblorerecipes.model.Recipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Value;
import net.runelite.api.gameval.ItemID;

/**
 * Immutable lookup from an item id to everything its tooltip should say about it.
 */
public final class RecipeIndex
{

	private final Map<Integer, List<Section>> sections;

	private RecipeIndex(Map<Integer, List<Section>> sections)
	{
		this.sections = sections;
	}

	public static RecipeIndex build()
	{
		List<Potion> potions = Arrays.stream(Potions.values()).map(p -> p.potion).collect(Collectors.toList());
		Map<Integer, HerbForms> herbs = HerbForms.fromPotions(potions);
		Builder builder = new Builder();

		Set<Integer> potionIdsSeen = new HashSet<>();
		for (Potion potion : potions)
		{
			Recipe recipe = Recipe.of(potion);

			// an item id is a potion of the first recipe that lists it
			for (int id : potion.getIds())
			{
				if (potionIdsSeen.add(id))
				{
					builder.add(id, ItemRole.POTION, ItemRole.POTION, recipe);
				}
			}

			if (isImpRepellent(potion))
			{
				// imp repellent is the one potion with many primaries (flowers), which are stored as its secondaries
				potion.getSecondaries().forEach(flower -> builder.add(flower, ItemRole.PRIMARY, ItemRole.PRIMARY, recipe));
			}
			else
			{
				builder.add(potion.getPrimary(), ItemRole.PRIMARY, ItemRole.PRIMARY, recipe);
				builder.add(potion.getPrimaryAlt(), ItemRole.PRIMARY, ItemRole.PRIMARY, recipe);
				builder.add(potion.getGrimyHerb(), ItemRole.GRIMY, ItemRole.GRIMY, recipe);
			}
			builder.add(potion.getPrimarySeed(), ItemRole.SEED, ItemRole.SEED, recipe);
			builder.add(potion.getUnfinishedPotion(), ItemRole.UNFINISHED, ItemRole.UNFINISHED, recipe);

			if (potion.hasComplexBase())
			{
				potion.getComplexBase().forEach(base -> base.getIds()
					.forEach(baseId -> builder.add(baseId, ItemRole.COMPLEX_BASE, ItemRole.COMPLEX_BASE, recipe)));
			}
		}

		// Alternate secondaries are listed after the regular ones, so they need their own pass.
		for (Potion potion : potions)
		{
			if (potion.getSecondaries() != null && !isImpRepellent(potion))
			{
				potion.getSecondaries().forEach(id -> builder.add(id, ItemRole.SECONDARY, ItemRole.SECONDARY, Recipe.of(potion)));
			}
		}
		for (Potion potion : potions)
		{
			if (potion.getSecondariesAlt() != null)
			{
				potion.getSecondariesAlt().forEach(id -> builder.add(id, ItemRole.SECONDARY, ItemRole.SECONDARY, Recipe.of(potion)));
			}
		}

		for (Paste paste : Paste.values())
		{
			for (Paste.HerbRecipe herbRecipe : paste.getHerbs())
			{
				Recipe recipe = Recipe.of(paste, herbRecipe);
				builder.add(paste.getItemId(), ItemRole.PASTE_RECIPES, ItemRole.POTION, recipe);

				// the paste shows on every form of the herb, and follows that form's own config toggle
				builder.add(herbRecipe.getHerb(), ItemRole.PASTE, ItemRole.PRIMARY, recipe);
				HerbForms forms = herbs.get(herbRecipe.getHerb());
				if (forms != null)
				{
					builder.add(forms.getGrimy(), ItemRole.PASTE, ItemRole.GRIMY, recipe);
					builder.add(forms.getSeed(), ItemRole.PASTE, ItemRole.SEED, recipe);
				}
			}
		}

		return builder.build();
	}

	private static boolean isImpRepellent(Potion potion)
	{
		return potion.getIds().contains(ItemID.II_IMP_REPELLENT);
	}

	/** Every item id that has at least one section. */
	public Set<Integer> knownIds()
	{
		return sections.keySet();
	}

	/** The sections for an item, in display order. Empty if the item is not part of any recipe. */
	public List<Section> sectionsOf(int itemId)
	{
		return sections.getOrDefault(itemId, ImmutableList.of());
	}

	private static final class Builder
	{

		@Value
		private static class Key
		{
			ItemRole role;
			ItemRole gate;
		}

		private final Map<Integer, Map<Key, List<Recipe>>> recipesById = new HashMap<>();

		/** Item id 0 means "not set" in the potion data, so it is never indexed. */
		void add(int itemId, ItemRole role, ItemRole gate, Recipe recipe)
		{
			if (itemId <= 0)
			{
				return;
			}
			List<Recipe> recipes = recipesById
				.computeIfAbsent(itemId, id -> new LinkedHashMap<>())
				.computeIfAbsent(new Key(role, gate), key -> new ArrayList<>());
			if (!recipes.contains(recipe))
			{
				recipes.add(recipe);
			}
		}

		RecipeIndex build()
		{
			ImmutableMap.Builder<Integer, List<Section>> built = ImmutableMap.builder();
			recipesById.forEach((id, byKey) -> built.put(id, byKey.entrySet().stream()
				.map(entry -> new Section(entry.getKey().getRole(), entry.getKey().getGate(), ImmutableList.copyOf(entry.getValue())))
				.sorted(Comparator.comparing(Section::getRole))
				.collect(ImmutableList.toImmutableList())));
			return new RecipeIndex(built.build());
		}
	}
}
```

- [ ] **Step 5: Run to verify they pass**

Run: `bash ./gradlew test --tests '*RecipeIndexTest' --tests '*PasteTest' --tests '*HerbFormsTest' --tests '*RecipeTest'`
Expected: `BUILD SUCCESSFUL`, all pass. (`TooltipGoldenTest` is now red as documented; do not run it.)

- [ ] **Step 6: Commit**

```bash
git add src
git commit -m "$(cat <<'EOF'
Add RecipeIndex and replace fake paste potions with Paste

RecipeIndex maps every item id to its tooltip sections in one generic
pass. Unset ingredients (id 0) are no longer indexed, and a recipe is
never listed twice in a section. TooltipGoldenTest is expected to fail
until the goldens are regenerated after the switch-over.

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

---

### Task 4: `TooltipRenderer`

**Files:**
- Create: `src/main/java/com/herblorerecipes/tooltip/TooltipRenderer.java`
- Test: `src/test/java/com/herblorerecipes/tooltip/TooltipRendererTest.java`

**Interfaces:**
- Consumes: `RecipeIndex`, `Section`, `ItemRole`, `Recipe` (Task 3/2); `TestConfig` (Task 1); `HerbloreRecipesConfig` (unchanged).
- Produces: `new TooltipRenderer(RecipeIndex index, IntFunction<String> itemName, HerbloreRecipesConfig config)` and `render(int itemId): Optional<String>`. The text uses `</br>` line breaks and the same colour tags as before.

- [ ] **Step 1: Write the failing tests**

`src/test/java/com/herblorerecipes/tooltip/TooltipRendererTest.java`:

```java
package com.herblorerecipes.tooltip;

import com.herblorerecipes.TestConfig;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TooltipRendererTest
{

	private static final RecipeIndex INDEX = RecipeIndex.build();

	private static String name(int itemId)
	{
		return "#" + itemId;
	}

	/** Renders with the given config and returns the text without colour tags, one line per line break. */
	private static String plain(int itemId, TestConfig config)
	{
		return new TooltipRenderer(INDEX, TooltipRendererTest::name, config).render(itemId)
			.map(text -> text.replace("</br>", "\n").replaceAll("<[^>]*>", ""))
			.orElse(null);
	}

	private static String plain(int itemId)
	{
		return plain(itemId, new TestConfig());
	}

	@Test
	public void cleanHerbShowsPasteThenPotions()
	{
		assertEquals(
			"Paste for:\n" +
				"lvl 1: Mox paste (1st: #249)\n" +
				"Primary for:\n" +
				"lvl 3: Attack potion (1st: #249 - 2nd: #221)\n" +
				"lvl 19: Guam tar (1st: #249 - 2nd: #1939)\n" +
				"Secondary for:\n" +
				"lvl 18: Guthix rest tea (1st: #255 - 2nd: #249, #251)\n",
			plain(ItemID.GUAM_LEAF));
	}

	@Test
	public void grimyHerbShowsPasteUnderItsOwnLabel()
	{
		assertEquals(
			"Paste for:\n" +
				"lvl 1: Mox paste (1st: #249)\n" +
				"Secondary for:\n" +
				"lvl 18: Guthix rest tea (1st: #255 - 2nd: #249, #251)\n" +
				"Clean for:\n" +
				"lvl 3: Attack potion (1st: #249 - 2nd: #221)\n" +
				"lvl 19: Guam tar (1st: #249 - 2nd: #1939)\n",
			plain(ItemID.UNIDENTIFIED_GUAM));
	}

	@Test
	public void seedShowsPaste()
	{
		assertEquals(
			"Paste for:\n" +
				"lvl 1: Mox paste (1st: #249)\n" +
				"Seed for:\n" +
				"lvl 3: Attack potion (1st: #249 - 2nd: #221)\n",
			plain(ItemID.GUAM_SEED));
	}

	@Test
	public void pasteItemShowsWhatMakesIt()
	{
		assertEquals(
			"To make Mox paste:\n" +
				"lvl 1: Mox paste (1st: #249)\n" +
				"lvl 1: Mox paste (1st: #" + ItemID.MARENTILL + ")\n" +
				"lvl 1: Mox paste (1st: #" + ItemID.TARROMIN + ")\n" +
				"lvl 1: Mox paste (1st: #" + ItemID.HARRALANDER + ")\n",
			plain(ItemID.MM_MOX_PASTE));
	}

	@Test
	public void potionShowsItsRequirementsThenWhatItIsABaseFor()
	{
		assertEquals(
			"Requirements for Attack potion:\n" +
				"lvl 3: Attack potion (1st: #249 - 2nd: #221)\n" +
				"Base for:\n" +
				"lvl 4: Attack mix (2nd: #11324)\n",
			plain(ItemID._4DOSE1ATTACK));
	}

	@Test
	public void unknownItemHasNoTooltip()
	{
		assertEquals(null, plain(ItemID.COINS));
	}

	@Test
	public void seedToggleHidesTheSeedAndItsPasteButNotOtherForms()
	{
		TestConfig config = new TestConfig();
		config.seeds = false;

		assertEquals(null, plain(ItemID.GUAM_SEED, config));
		assertTrue(plain(ItemID.GUAM_LEAF, config).contains("Paste for:"));
		assertTrue(plain(ItemID.UNIDENTIFIED_GUAM, config).contains("Clean for:"));
	}

	@Test
	public void grimyToggleHidesOnlyTheGrimySections()
	{
		TestConfig config = new TestConfig();
		config.grimy = false;

		assertEquals(
			"Secondary for:\n" +
				"lvl 18: Guthix rest tea (1st: #255 - 2nd: #249, #251)\n",
			plain(ItemID.UNIDENTIFIED_GUAM, config));
	}

	@Test
	public void primaryToggleHidesPasteOnCleanHerbs()
	{
		TestConfig config = new TestConfig();
		config.primaries = false;

		assertFalse(plain(ItemID.GUAM_LEAF, config).contains("Paste for:"));
		assertFalse(plain(ItemID.GUAM_LEAF, config).contains("Primary for:"));
	}

	@Test
	public void potionToggleHidesPotionAndPasteItemTooltips()
	{
		TestConfig config = new TestConfig();
		config.potions = false;

		assertFalse(plain(ItemID._4DOSE1ATTACK, config).contains("Requirements for"));
		assertEquals(null, plain(ItemID.MM_MOX_PASTE, config));
	}

	@Test
	public void contentTogglesTrimTheLines()
	{
		TestConfig config = new TestConfig();
		config.level = false;
		config.primaryIngredients = false;
		assertEquals(
			"Requirements for Attack potion:\n" +
				"Attack potion (2nd: #221)\n" +
				"Base for:\n" +
				"Attack mix (2nd: #11324)\n",
			plain(ItemID._4DOSE1ATTACK, config));

		config.secondaryIngredients = false;
		assertEquals(
			"Requirements for Attack potion:\n" +
				"Attack potion\n" +
				"Base for:\n" +
				"Attack mix\n",
			plain(ItemID._4DOSE1ATTACK, config));
	}

	@Test
	public void impRepellentFlowersAreSummarisedUnlessAsked()
	{
		assertEquals(
			"Primary for:\n" +
				"lvl 3: Imp repellent (2nd: Various flowers...)\n",
			plain(ItemID.MARIGOLD));

		TestConfig config = new TestConfig();
		config.impRepellentIngredients = true;
		String detailed = plain(ItemID.MARIGOLD, config);
		assertTrue(detailed, detailed.contains("2nd: #" + ItemID.MARIGOLD));
		assertFalse(detailed, detailed.contains("Various flowers"));
	}
}
```

- [ ] **Step 2: Run to verify they fail**

Run: `bash ./gradlew test --tests '*TooltipRendererTest'`
Expected: `compileTestJava FAILED`, `cannot find symbol ... class TooltipRenderer`.

- [ ] **Step 3: Write the implementation**

`src/main/java/com/herblorerecipes/tooltip/TooltipRenderer.java`:

```java
package com.herblorerecipes.tooltip;

import com.herblorerecipes.HerbloreRecipesConfig;
import com.herblorerecipes.model.Recipe;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import net.runelite.client.util.ColorUtil;

/**
 * Turns an item's {@link Section}s into tooltip text. Pure: it needs only item names and the config.
 */
public final class TooltipRenderer
{

	private static final String LINE_BREAK = "</br>";

	private static final Color GREY = new Color(238, 238, 238);
	private static final Color LIME = new Color(0, 255, 0);
	private static final Color AQUA = new Color(0, 255, 255);
	private static final Color GOLD = new Color(255, 215, 0);
	private static final Color PINK = new Color(236, 128, 255);
	private static final Color PURPLE = new Color(181, 153, 255);
	private static final Color ORANGE = new Color(255, 140, 103);
	private static final Color TURQUOISE = new Color(112, 248, 208);
	private static final Color SALMON = new Color(248, 112, 123);
	private static final Color GREEN = new Color(66, 187, 123);

	private static final String PRIMARY_TITLE = colorWrap("Primary", LIME) + colorWrap(" for:", GREY);
	private static final String SECONDARY_TITLE = colorWrap("Secondary", AQUA) + colorWrap(" for:", GREY);
	private static final String UNFINISHED_TITLE = colorWrap("Unfinished", GOLD) + colorWrap(" for:", GREY);
	private static final String BASE_TITLE = colorWrap("Base", PURPLE) + colorWrap(" for:", GREY);
	private static final String SEED_TITLE = colorWrap("Seed", ORANGE) + colorWrap(" for:", GREY);
	private static final String GRIMY_TITLE = colorWrap("Clean", TURQUOISE) + colorWrap(" for:", GREY);
	private static final String PASTE_TITLE = colorWrap("Paste", SALMON) + colorWrap(" for:", GREY);
	private static final String POTION_TITLE = colorWrap("Requirements", PINK) + colorWrap(" for %s:", GREY);
	private static final String PASTE_RECIPES_TITLE = colorWrap("To make", GREEN) + colorWrap(" %s:", GREY);

	private final RecipeIndex index;
	private final IntFunction<String> itemName;
	private final HerbloreRecipesConfig config;

	public TooltipRenderer(RecipeIndex index, IntFunction<String> itemName, HerbloreRecipesConfig config)
	{
		this.index = index;
		this.itemName = itemName;
		this.config = config;
	}

	/** The tooltip text for an item, or empty if there is nothing to show (or the config hides all of it). */
	public Optional<String> render(int itemId)
	{
		StringBuilder text = new StringBuilder();
		for (Section section : index.sectionsOf(itemId))
		{
			if (!isEnabled(section.getGate()))
			{
				continue;
			}
			text.append(title(section)).append(LINE_BREAK);
			for (Recipe recipe : section.getRecipes())
			{
				text.append(line(recipe)).append(LINE_BREAK);
			}
		}
		return text.length() == 0 ? Optional.empty() : Optional.of(text.toString());
	}

	private boolean isEnabled(ItemRole gate)
	{
		switch (gate)
		{
			case POTION:
				return config.showTooltipOnPotions();
			case PRIMARY:
				return config.showTooltipOnPrimaries();
			case SECONDARY:
				return config.showTooltipOnSecondaries();
			case COMPLEX_BASE:
				return config.showTooltipOnComplex();
			case SEED:
				return config.showTooltipOnPrimarySeeds();
			case UNFINISHED:
				return config.showTooltipOnUnfinished();
			case GRIMY:
				return config.showTooltipOnGrimy();
			default:
				throw new IllegalArgumentException(gate + " is not a role an item can have");
		}
	}

	private static String title(Section section)
	{
		switch (section.getRole())
		{
			case POTION:
				return String.format(POTION_TITLE, colorWrap(section.getRecipes().get(0).getName(), AQUA));
			case PASTE_RECIPES:
				return String.format(PASTE_RECIPES_TITLE, colorWrap(section.getRecipes().get(0).getName(), AQUA));
			case PASTE:
				return PASTE_TITLE;
			case PRIMARY:
				return PRIMARY_TITLE;
			case SECONDARY:
				return SECONDARY_TITLE;
			case COMPLEX_BASE:
				return BASE_TITLE;
			case SEED:
				return SEED_TITLE;
			case UNFINISHED:
				return UNFINISHED_TITLE;
			case GRIMY:
				return GRIMY_TITLE;
			default:
				throw new IllegalArgumentException("No title for " + section.getRole());
		}
	}

	/** e.g. {@code lvl 3: Attack potion (1st: Guam leaf - 2nd: Eye of newt)} */
	private String line(Recipe recipe)
	{
		StringBuilder line = new StringBuilder();
		if (config.showHerbloreLvlInTooltip())
		{
			line.append("lvl ").append(recipe.getLevel()).append(": ");
		}
		line.append(recipe.getName());

		List<String> ingredients = new ArrayList<>();
		if (config.showPrimariesInTooltip() && recipe.getPrimary() > 0)
		{
			ingredients.add("1st: " + itemName.apply(recipe.getPrimary()));
		}
		if (config.showSecondariesInTooltip() && !recipe.getSecondaries().isEmpty())
		{
			ingredients.add("2nd: " + secondaries(recipe));
		}
		if (!ingredients.isEmpty())
		{
			line.append(" (").append(String.join(" - ", ingredients)).append(")");
		}
		return line.toString();
	}

	private String secondaries(Recipe recipe)
	{
		if (recipe.isCollapsibleSecondaries() && !config.showImpRepellentIngs())
		{
			return "Various flowers...";
		}
		return recipe.getSecondaries().stream()
			.map(itemName::apply)
			.collect(Collectors.joining(", "));
	}

	private static String colorWrap(String text, Color color)
	{
		return ColorUtil.wrapWithColorTag(text, color);
	}
}
```

- [ ] **Step 4: Run to verify they pass**

Run: `bash ./gradlew test --tests '*TooltipRendererTest' --tests '*RecipeIndexTest'`
Expected: `BUILD SUCCESSFUL`, all pass.

- [ ] **Step 5: Commit**

```bash
git add src
git commit -m "$(cat <<'EOF'
Add TooltipRenderer

Turns an item's sections into tooltip text. Config toggles are applied
per section by the form the hovered item is, so the paste section
follows the toggle of the herb form it appears on.

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

---

### Task 5: Switch the plugin over and delete the old code

**Files:**
- Rewrite: `src/main/java/com/herblorerecipes/cache/TooltipCache.java`
- Modify: `src/main/java/com/herblorerecipes/HerbloreRecipesOverlay.java`
- Modify: `src/main/java/com/herblorerecipes/HerbloreRecipesPlugin.java`
- Modify: `src/main/java/com/herblorerecipes/model/Potions.java`
- Modify: `src/main/java/com/herblorerecipes/model/Potion.java`
- Delete: `src/main/java/com/herblorerecipes/model/{TooltipBox,TooltipCategory,TooltipCategoryContent,TooltipStringBuilder}.java`
- Test: `src/test/java/com/herblorerecipes/TooltipCacheTest.java`

**Interfaces:**
- Consumes: `RecipeIndex.build()`, `knownIds()`, `TooltipRenderer` (Tasks 3-4); `TooltipHarness` (Task 1).
- Produces: `TooltipCache` keeps exactly its public API: constructor `(ItemManager, ClientThread, HerbloreRecipesConfig)`, `preloadOnClientThread()`, `contains(int)`, `get(int): Tooltip`, `reset()`.

- [ ] **Step 1: Write the cache test**

`src/test/java/com/herblorerecipes/TooltipCacheTest.java`:

```java
package com.herblorerecipes;

import com.herblorerecipes.cache.TooltipCache;
import java.util.List;
import net.runelite.api.gameval.ItemID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TooltipCacheTest
{

	@Test
	public void pasteSurvivesAConfigReset()
	{
		TooltipCache cache = TooltipHarness.cache(new TestConfig());
		assertTrue(cache.get(ItemID.GUAM_LEAF).getText().contains("Mox paste"));

		cache.reset();

		assertTrue(cache.get(ItemID.GUAM_LEAF).getText().contains("Mox paste"));
	}

	@Test
	public void rebuildingGivesIdenticalTooltips()
	{
		TooltipCache cache = TooltipHarness.cache(new TestConfig());
		List<String> first = TooltipHarness.dump(cache);

		cache.reset();

		assertEquals(first, TooltipHarness.dump(cache));
	}

	@Test
	public void resetPicksUpConfigChanges()
	{
		TestConfig config = new TestConfig();
		TooltipCache cache = TooltipHarness.cache(config);
		assertTrue(cache.contains(ItemID.GUAM_LEAF));

		config.primaries = false;
		config.secondaries = false;
		config.seeds = false;
		config.grimy = false;
		config.unfinished = false;
		config.potions = false;
		config.complex = false;
		cache.reset();

		assertFalse(cache.contains(ItemID.GUAM_LEAF));
	}
}
```

- [ ] **Step 2: Run to verify it fails against the old cache**

Run: `bash ./gradlew test --tests '*TooltipCacheTest'`
Expected: FAIL. `pasteSurvivesAConfigReset` fails (the old cache no longer has paste data since Task 3, and it also had the mutation bug). `rebuildingGivesIdenticalTooltips` may pass or fail; ignore it here.

- [ ] **Step 3: Replace `TooltipCache`**

Overwrite `src/main/java/com/herblorerecipes/cache/TooltipCache.java`:

```java
package com.herblorerecipes.cache;

import com.google.common.base.Stopwatch;
import com.herblorerecipes.HerbloreRecipesConfig;
import com.herblorerecipes.tooltip.RecipeIndex;
import com.herblorerecipes.tooltip.TooltipRenderer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.tooltip.Tooltip;

/**
 * Holds the rendered tooltip for every item. Item names are only available on the client thread,
 * so the whole cache is rebuilt there, at login and whenever the config changes.
 */
@Slf4j
public class TooltipCache
{

	private static final RecipeIndex INDEX = RecipeIndex.build();

	private final ItemManager itemManager;
	private final ClientThread clientThread;
	private final HerbloreRecipesConfig config;
	private volatile Map<Integer, Tooltip> tooltips = Collections.emptyMap();

	@Inject
	public TooltipCache(ItemManager itemManager, ClientThread clientThread, HerbloreRecipesConfig config)
	{
		this.itemManager = itemManager;
		this.clientThread = clientThread;
		this.config = config;
	}

	public void preloadOnClientThread()
	{
		clientThread.invoke(this::preLoadCache);
	}

	private void preLoadCache()
	{
		Stopwatch timer = Stopwatch.createStarted();
		TooltipRenderer renderer = new TooltipRenderer(INDEX, this::itemName, config);
		Map<Integer, Tooltip> built = new HashMap<>();
		for (int id : INDEX.knownIds())
		{
			renderer.render(id).ifPresent(text -> built.put(id, new Tooltip(text)));
		}
		tooltips = built;
		log.debug("Tooltip cache was loaded in {}ms.", timer.stop().elapsed().toNanos() / 1000000.0);
	}

	public boolean contains(int id)
	{
		return tooltips.containsKey(id);
	}

	public Tooltip get(int id)
	{
		return tooltips.get(id);
	}

	public void reset()
	{
		preloadOnClientThread();
	}

	private String itemName(int itemId)
	{
		return itemManager.getItemComposition(itemId).getName();
	}
}
```

- [ ] **Step 4: Update the overlay**

In `src/main/java/com/herblorerecipes/HerbloreRecipesOverlay.java`:

1. Delete the line `import com.herblorerecipes.model.Potions;`.
2. Replace the method

```java
	private void showTooltip(int widgetId, MenuEntry menuEntry)
	{
		int itemId = getItemIdFromMenuEntry(menuEntry);

		if (Potions.isSeed(itemId) && !config.showTooltipOnPrimarySeeds())
		{
			return;
		}

		if (Potions.isUnfinished(itemId) && !config.showTooltipOnUnfinished())
		{
			return;
		}

		if (Potions.isGrimy(itemId) && !config.showTooltipOnGrimy())
		{
			return;
		}

		showTooltip(itemId);
	}
```

with

```java
	private void showTooltip(int widgetId, MenuEntry menuEntry)
	{
		showTooltip(getItemIdFromMenuEntry(menuEntry));
	}
```

Nothing else in the overlay changes. The renderer now applies the seed, unfinished and grimy toggles per section.

- [ ] **Step 5: Update the plugin**

Overwrite `src/main/java/com/herblorerecipes/HerbloreRecipesPlugin.java`:

```java
package com.herblorerecipes;

import com.google.inject.Provides;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Herblore Recipes",
	description = "Hover over a herblore ingredient or potion in your inventory or bank to see which potions can be made with it or that potion's recipe",
	tags = {"recipes", "herblore", "herb"}
)
public class HerbloreRecipesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HerbloreRecipesConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private HerbloreRecipesOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		keyManager.registerKeyListener(overlay);
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(overlay);
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (Objects.requireNonNull(gameStateChanged.getGameState()) == GameState.LOGGED_IN)
		{
			overlay.tooltipCache.preloadOnClientThread();
		}
	}

	@Provides
	HerbloreRecipesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HerbloreRecipesConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if ("herblorerecipes".equals(event.getGroup()))
		{
			overlay.tooltipCache.reset();
		}
	}
}
```

(Diff from before: the eight `import static ...HerbloreRecipesConfig.SHOW_*` lines are gone, and `onConfigChanged` resets the cache on any change in the `herblorerecipes` group.)

- [ ] **Step 6: Strip `Potions` down to the data**

In `src/main/java/com/herblorerecipes/model/Potions.java`:

1. Replace the import block with only:

```java
import com.google.common.collect.ImmutableSet;
import net.runelite.api.gameval.ItemID;
```

2. Delete everything from the line `private static final Map<Integer, List<Potion>> primaryToPotion = new HashMap<>();` to the end of the file, and end the file with:

```java
	public final Potion potion;

	Potions(Potion potion)
	{
		this.potion = potion;
	}
}
```

(The enum's last constant already ends with `;` before the deleted block.)

- [ ] **Step 7: Remove dead code from `Potion` and delete the old DTOs**

In `src/main/java/com/herblorerecipes/model/Potion.java` delete the `complexBaseNames()` method and the `import java.util.stream.Collectors;` line (keep `hasComplexBase()`).

Run:
```bash
git rm src/main/java/com/herblorerecipes/model/TooltipBox.java \
       src/main/java/com/herblorerecipes/model/TooltipCategory.java \
       src/main/java/com/herblorerecipes/model/TooltipCategoryContent.java \
       src/main/java/com/herblorerecipes/model/TooltipStringBuilder.java
```

- [ ] **Step 8: Compile and run everything except the golden test's expected failures**

Run: `bash ./gradlew test 2>&1 | grep -E 'FAILED|BUILD|tests completed'`
Expected: `BUILD FAILED` with **only** the four `TooltipGoldenTest` tests failing (`defaultConfig`, `noIngredientsOrLevels`, `impRepellentIngredientsShown`, `ingredientTooltipsOff`). `TooltipCacheTest`, `RecipeIndexTest`, `TooltipRendererTest`, `PasteTest`, `HerbFormsTest` and `RecipeTest` must all pass. If anything else fails, stop and fix it.

Also run: `grep -rn 'TooltipBox\|TooltipCategory\|TooltipStringBuilder\|getByPrimary\|isGrimy\|getPastes' src` and expect no output.

- [ ] **Step 9: Commit**

```bash
git add -A src
git commit -m "$(cat <<'EOF'
Switch the plugin to RecipeIndex and TooltipRenderer

TooltipCache now just renders every known item on the client thread and
swaps in the new map, so a config reset can no longer lose data. The
overlay's per-form checks and the six parallel role maps are gone, and
the old tooltip DTO classes are deleted.

TooltipGoldenTest fails until the goldens are regenerated (next commit).

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

---

### Task 6: Review and accept the golden diff

**Files:**
- Modify: `src/test/resources/golden/*.txt` (regenerated)

- [ ] **Step 1: Confirm the golden test fails at the first difference**

Run: `bash ./gradlew test --tests '*TooltipGoldenTest'`
Expected: 4 FAILED, e.g. `default differs at line N (run with UPDATE_GOLDEN=1 ...)`.

- [ ] **Step 2: Regenerate the snapshots**

Run: `UPDATE_GOLDEN=1 bash ./gradlew test --tests '*TooltipGoldenTest' --rerun-tasks`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Review the diff. It must contain only the intended changes**

Run:
```bash
git diff --stat src/test/resources/golden
for f in default no-ingredients imp-repellent-ingredients ingredient-tooltips-off; do
  echo "=== $f: blocks before/after: $(git show HEAD:src/test/resources/golden/$f.txt | grep -c '^== ') / $(grep -c '^== ' src/test/resources/golden/$f.txt)"
  echo "-- added lines that do not mention paste (expect none):"
  git diff -U0 src/test/resources/golden/$f.txt | grep '^+' | grep -v '^+++' | grep -vi 'paste'
  echo "-- removed lines that do not mention paste:"
  git diff -U0 src/test/resources/golden/$f.txt | grep '^-' | grep -v '^---' | grep -vi 'paste'
done
```

Expected, exactly:
- Block counts: `default`, `no-ingredients`, `imp-repellent-ingredients`: 477 → 476 (the bogus `== 0` block is gone). `ingredient-tooltips-off`: 54 → 54.
- **Added lines that do not mention paste:** none, in every file.
- **Removed lines that do not mention paste:** only (a) the whole former `== 0` block (a `Primary for:` list of mixes and a few potions) in the first three files, and (b) the single duplicate `Weapon poison++` line (`lvl 82: Weapon poison++ (1st: #27790 - 2nd: #6018)` or plain `Weapon poison++`) in the first three files. Nothing in `ingredient-tooltips-off`.
- Paste changes, per file: 15 paste lines removed from `Clean for:` lists (grimy herbs), and 30 new `Paste for:` sections added (15 on grimy herbs, 15 on seeds), each followed by one paste line.

If anything else differs, **stop and report it instead of accepting it**.

- [ ] **Step 4: Run the whole suite**

Run: `bash ./gradlew test`
Expected: `BUILD SUCCESSFUL`, all tests pass.

- [ ] **Step 5: Commit the regenerated snapshots**

```bash
git add src/test/resources/golden
git commit -m "$(cat <<'EOF'
Update tooltip snapshots for intended behaviour changes

- Mixology paste now shows on grimy herbs and herb seeds (as "Paste for:"),
  not mixed into "Clean for:"
- Item 0 (Dwarf remains) no longer gets a bogus "Primary for:" tooltip
- Weapon poison++ is no longer listed twice on stackable nightshade

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

---

### Task 7: Final verification, spec notes, README

**Files:**
- Modify: `docs/superpowers/specs/2026-09-18-recipe-index-refactor-design.md`
- Modify: `README.md`

- [ ] **Step 1: Correct the spec where the implementation refined it**

Run:
```bash
sed -i 's/Pastes are 16 fake/Pastes are 15 fake/; s/the 16 fake/the 15 fake/' docs/superpowers/specs/2026-09-18-recipe-index-refactor-design.md
grep -n '16' docs/superpowers/specs/2026-09-18-recipe-index-refactor-design.md
```
Expected: no line still saying 16 pastes. Then append this section to the end of the spec:

```markdown

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
- Mockito 4.11.0 works with the RuneLite client on Java 11.
```

- [ ] **Step 2: Mention pastes in the README**

In `README.md`, directly after the paragraph that ends `![Seeds](https://i.imgur.com/uz6xISY.png "Seeds")` and the line after it (`Feature added thanks to this [raised issue](...issues/3).`), insert:

```markdown

Mastering Mixology pastes are covered too: herbs (clean, grimy or as seeds) show which paste they can be ground into,  
and the paste itself lists the herbs that make it.  
```

- [ ] **Step 3: Full build**

Run: `bash ./gradlew build 2>&1 | tail -15`
Expected: `BUILD SUCCESSFUL`; the test task ran and passed.

- [ ] **Step 4: Check the CI command without changing anything**

Run: `grep -n 'gradlew' .github/workflows/build.yml`
Expected: the line `./gradlew build`, which compiles and runs the tests. (An earlier draft of this plan expected `./gradlew build.gradle`; that was a misreading caused by the workflow file lacking a trailing newline.) Do **not** edit the workflow.

- [ ] **Step 5: Commit**

```bash
git add README.md docs
git commit -m "$(cat <<'EOF'
Record implementation notes in the spec and mention pastes in the README

Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
EOF
)"
```

- [ ] **Step 6: Report to the user (do not push or open a PR)**

Summarise: the commit list (`git log --oneline main..HEAD`), the golden diff categories from Task 6, and ask them to check it in a real client, which automated tests cannot do: run `bash ./gradlew run` (needs a display), then hover in inventory and bank: a clean herb, a grimy herb, a seed, a Mox/Lye/Aga paste, a potion, an unfinished potion and a secondary ingredient. Toggle a config option and confirm the herb tooltips keep their "Paste for:" section (this was the reset bug).

Then use `superpowers:finishing-a-development-branch`.
