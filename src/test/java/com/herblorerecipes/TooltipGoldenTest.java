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
