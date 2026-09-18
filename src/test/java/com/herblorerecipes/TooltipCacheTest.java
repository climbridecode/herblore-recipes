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
