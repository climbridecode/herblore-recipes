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
	public void potionRecipeRecordsABaseThatIsNotWater()
	{
		assertEquals(ItemID.MYQ4_BLOOD_VIAL, Recipe.of(Potions.BASTION_POTION.potion).getBase());
		assertEquals(ItemID.VIAL_COCONUT_MILK, Recipe.of(Potions.ANTIDOTE_PLUS2.potion).getBase());
	}

	@Test
	public void waterBasedPotionRecipeHasNoBase()
	{
		assertEquals(0, Recipe.of(Potions.ATTACK_POTION.potion).getBase());
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
		assertEquals(0, recipe.getBase());
		assertTrue(recipe.getSecondaries().isEmpty());
		assertFalse(recipe.isCollapsibleSecondaries());
	}
}
