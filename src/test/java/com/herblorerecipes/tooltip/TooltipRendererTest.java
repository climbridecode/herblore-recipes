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
				"lvl 18: Guthix rest tea [#" + ItemID.CUP_HOT_WATER + "] (1st: #255 - 2nd: #249, #251)\n",
			plain(ItemID.GUAM_LEAF));
	}

	@Test
	public void grimyHerbShowsPasteUnderItsOwnLabel()
	{
		assertEquals(
			"Paste for:\n" +
				"lvl 1: Mox paste (1st: #249)\n" +
				"Secondary for:\n" +
				"lvl 18: Guthix rest tea [#" + ItemID.CUP_HOT_WATER + "] (1st: #255 - 2nd: #249, #251)\n" +
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
	public void pasteItemListsTheHerbsThatMakeIt()
	{
		assertEquals(
			"To make Mox paste:\n" +
				"lvl 1: #249\n" +
				"lvl 1: #" + ItemID.MARENTILL + "\n" +
				"lvl 1: #" + ItemID.TARROMIN + "\n" +
				"lvl 1: #" + ItemID.HARRALANDER + "\n",
			plain(ItemID.MM_MOX_PASTE));
	}

	@Test
	public void pasteItemKeepsTheHigherLevelHerbsDistinguishable()
	{
		String aga = plain(ItemID.MM_AGA_PASTE);

		assertTrue(aga, aga.contains("lvl 60: #" + ItemID.HUASCA + "\n"));
		assertTrue(aga, aga.contains("lvl 60: #" + ItemID.DWARF_WEED + "\n"));
		assertTrue(aga, aga.contains("lvl 1: #" + ItemID.IRIT_LEAF + "\n"));
	}

	@Test
	public void pasteItemHerbsFollowTheLevelToggleButNotTheIngredientToggles()
	{
		TestConfig config = new TestConfig();
		config.level = false;
		config.primaryIngredients = false;
		config.secondaryIngredients = false;

		assertEquals(
			"To make Mox paste:\n" +
				"#249\n" +
				"#" + ItemID.MARENTILL + "\n" +
				"#" + ItemID.TARROMIN + "\n" +
				"#" + ItemID.HARRALANDER + "\n",
			plain(ItemID.MM_MOX_PASTE, config));
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
				"lvl 18: Guthix rest tea [#" + ItemID.CUP_HOT_WATER + "] (1st: #255 - 2nd: #249, #251)\n",
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
	public void potionToggleHidesPotionTooltips()
	{
		TestConfig config = new TestConfig();
		config.potions = false;

		assertFalse(plain(ItemID._4DOSE1ATTACK, config).contains("Requirements for"));
	}

	@Test
	public void pasteToggleHidesThePasteItemTooltipButNotPotions()
	{
		TestConfig config = new TestConfig();
		config.pastes = false;

		assertEquals(null, plain(ItemID.MM_MOX_PASTE, config));
		assertTrue(plain(ItemID._4DOSE1ATTACK, config).contains("Requirements for"));
	}

	@Test
	public void pasteItemTooltipDoesNotNeedThePotionToggle()
	{
		TestConfig config = new TestConfig();
		config.potions = false;

		assertTrue(plain(ItemID.MM_MOX_PASTE, config).startsWith("To make Mox paste:"));
	}

	@Test
	public void herbPasteToggleHidesPasteOnCleanGrimyAndSeedButKeepsTheirOtherSections()
	{
		TestConfig config = new TestConfig();
		config.pastesOnHerbs = false;

		String clean = plain(ItemID.GUAM_LEAF, config);
		assertFalse(clean, clean.contains("Paste for:"));
		assertTrue(clean, clean.contains("Primary for:"));

		String grimy = plain(ItemID.UNIDENTIFIED_GUAM, config);
		assertFalse(grimy, grimy.contains("Paste for:"));
		assertTrue(grimy, grimy.contains("Clean for:"));

		assertEquals(
			"Seed for:\n" +
				"lvl 3: Attack potion (1st: #249 - 2nd: #221)\n",
			plain(ItemID.GUAM_SEED, config));
	}

	@Test
	public void herbPasteToggleDoesNotAffectThePasteItemTooltip()
	{
		TestConfig config = new TestConfig();
		config.pastesOnHerbs = false;

		assertTrue(plain(ItemID.MM_MOX_PASTE, config).startsWith("To make Mox paste:"));
	}

	@Test
	public void herbPasteAlsoNeedsTheHerbFormsOwnToggle()
	{
		TestConfig config = new TestConfig();
		config.primaries = false;

		assertFalse(plain(ItemID.GUAM_LEAF, config).contains("Paste for:"));
		assertTrue(plain(ItemID.UNIDENTIFIED_GUAM, config).contains("Paste for:"));
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
				"lvl 3: Imp repellent [#" + ItemID.II_ANCHOVY_OIL + "] (2nd: Various flowers...)\n",
			plain(ItemID.MARIGOLD));

		TestConfig config = new TestConfig();
		config.impRepellentIngredients = true;
		String detailed = plain(ItemID.MARIGOLD, config);
		assertTrue(detailed, detailed.contains("2nd: #" + ItemID.MARIGOLD));
		assertFalse(detailed, detailed.contains("Various flowers"));
	}

	@Test
	public void potionWithABaseThatIsNotWaterIsTaggedWithIt()
	{
		String cadantine = plain(ItemID.CADANTINE);

		assertTrue(cadantine, cadantine.contains("lvl 80: Bastion potion [#" + ItemID.MYQ4_BLOOD_VIAL + "] (1st: #"
			+ ItemID.CADANTINE + " - 2nd: #" + ItemID.WINE_OF_ZAMORAK + ")\n"));
		assertFalse(cadantine, cadantine.contains("Super defence ["));

		String irit = plain(ItemID.IRIT_LEAF);
		assertTrue(irit, irit.contains("Antidote++ [#" + ItemID.VIAL_COCONUT_MILK + "]"));
		assertFalse(irit, irit.contains("Superantipoison ["));
	}

	@Test
	public void potionTooltipTagsItsOwnBase()
	{
		String bastion = plain(ItemID._4DOSEBASTION);

		assertTrue(bastion, bastion.contains("lvl 80: Bastion potion [#" + ItemID.MYQ4_BLOOD_VIAL + "]"));
	}

	@Test
	public void baseTagCanBeSwitchedOff()
	{
		TestConfig config = new TestConfig();
		config.potionBase = false;

		String cadantine = plain(ItemID.CADANTINE, config);

		assertTrue(cadantine, cadantine.contains("lvl 80: Bastion potion (1st: #"));
		assertFalse(cadantine, cadantine.contains("["));
	}
}
