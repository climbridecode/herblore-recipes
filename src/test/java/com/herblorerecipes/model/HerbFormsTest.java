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
