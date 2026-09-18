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
