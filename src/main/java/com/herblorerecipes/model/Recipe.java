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
	/** Item id of the base when it is not a vial of water (blood, coconut milk...), or 0 when it is water or unknown. */
	int base;
	/** Never null; empty when the recipe has no secondaries. */
	Set<Integer> secondaries;
	/** True when the secondaries are so numerous they can be summarised (imp repellent's flowers). */
	boolean collapsibleSecondaries;

	public static Recipe of(Potion potion)
	{
		Set<Integer> secondaries = potion.getSecondaries() == null ? ImmutableSet.of() : potion.getSecondaries();
		boolean collapsible = potion.getIds().contains(ItemID.II_IMP_REPELLENT);
		int base = potion.getBasicBase() == ItemID.VIAL_WATER ? 0 : potion.getBasicBase();
		return new Recipe(potion.getName(), potion.getLevel(), potion.getPrimary(), base, secondaries, collapsible);
	}

	public static Recipe of(Paste paste, Paste.HerbRecipe herb)
	{
		return new Recipe(paste.getName(), herb.getLevel(), herb.getHerb(), 0, ImmutableSet.of(), false);
	}
}
