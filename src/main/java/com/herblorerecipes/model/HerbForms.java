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
