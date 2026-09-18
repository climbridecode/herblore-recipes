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
