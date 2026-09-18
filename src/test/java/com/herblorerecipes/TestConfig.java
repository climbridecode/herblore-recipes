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
