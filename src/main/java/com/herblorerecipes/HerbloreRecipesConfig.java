package com.herblorerecipes;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("herblorerecipes")
public interface HerbloreRecipesConfig extends Config
{
	String MODIFIER_KEYBIND = "modifierKeybind";
	String USE_KEYBIND = "useKeybind";
	String SHOW_TOOLTIP_SEED_VAULT = "showTooltipSeedVault";
	String SHOW_TOOLTIP_GROUP_STORAGE = "showTooltipGroupStorage";
	String SHOW_TOOLTIP_INVENTORY = "showTooltipInInventory";
	String SHOW_TOOLTIP_ON_PLACEHOLDER = "showTooltipOnPlaceholder";
	String SHOW_TOOLTIP_IN_BANK = "showTooltipInBank";
	String SHOW_POTION_RECIPES = "showPotionRecipes";
	String SHOW_TOOLTIP_ON_PRIMARIES = "showTooltipOnPrimaries";
	String SHOW_TOOLTIP_ON_GRIMY = "showTooltipOnGrimy";
	String SHOW_TOOLTIP_ON_SECONDARIES = "showTooltipOnSecondaries";
	String SHOW_TOOLTIP_ON_UNFINISHED = "showTooltipOnUnfinished";
	String SHOW_TOOLTIP_ON_SEEDS = "showTooltipOnSeeds";
	String SHOW_TOOLTIP_ON_COMPLEX = "showTooltipOnComplex";
	String SHOW_PRIMARY_INGS = "showPrimaryIngredientsInTooltip";
	String SHOW_SECONDARY_INGS = "showSecondaryIngredientsInTooltip";
	String SHOW_HERB_LVL_REQ = "showHerbloreLvlInTooltip";
	String SHOW_IMP_REPELLENT_INGS = "showImpRepellentIngs";

	@ConfigSection(
		name = "Tooltip Visibility",
		description = "Settings for tooltip Tooltip visibility",
		position = 0
	)
	String tooltipSection = "tooltip";

	@ConfigSection(
		name = "Interfaces",
		description = "Configure whether tooltip appears in these interfaces.",
		position = 1
	)
	String interfaceSection = "interface";

	@ConfigSection(
		name = "Tooltip Content",
		description = "Configure tooltip content",
		position = 2
	)
	String tooltipContentSection = "tooltipContent";

	@ConfigSection(
		name = "Keybind",
		description = "Define a custom hotkey to control the display of the Tooltip. Keybind changes are applied when focus is returned to the game.",
		position = 3
	)
	String keybindSection = "keybind";

	@ConfigItem(
		position = 0,
		keyName = SHOW_POTION_RECIPES,
		name = "Show Tooltip on Potions (Recipes)",
		section = tooltipSection,
		description = "Configure whether tooltips appear on potions - NOTE: Tooltips will still appear on potions that are bases for other potions e.g. Super attack potion for Divine super attack etc..."
	)
	default boolean showTooltipOnPotions()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = SHOW_TOOLTIP_ON_PRIMARIES,
		name = "Show Tooltip on Primaries",
		section = tooltipSection,
		description = "Toggle recipe tooltip on primary ingredients."
	)
	default boolean showTooltipOnPrimaries()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = SHOW_TOOLTIP_ON_COMPLEX,
		name = "Show Tooltip on Complex Bases",
		section = tooltipSection,
		description = "Toggle recipe tooltip on Complex potion bases e.g. Hovering over a Super combat potion will show a tooltip as it's a base for Divine super combat potion."
	)
	default boolean showTooltipOnComplex()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = SHOW_TOOLTIP_ON_GRIMY,
		name = "Show Tooltip on Grimy herbs",
		section = tooltipSection,
		description = "Whether or not tooltip should be displayed on Grimy herbs."
	)
	default boolean showTooltipOnGrimy()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = SHOW_TOOLTIP_ON_SECONDARIES,
		name = "Show Tooltip on Secondaries",
		section = tooltipSection,
		description = "Toggle recipe tooltip on secondary ingredients."
	)
	default boolean showTooltipOnSecondaries()
	{
		return true;
	}

	@ConfigItem(
		position = 5,
		keyName = SHOW_TOOLTIP_ON_UNFINISHED,
		name = "Show Tooltip on Unfinished Potions",
		section = tooltipSection,
		description = "Toggle recipe tooltip on unfinished potions."
	)
	default boolean showTooltipOnUnfinished()
	{
		return true;
	}

	@ConfigItem(
		position = 6,
		keyName = SHOW_TOOLTIP_ON_SEEDS,
		name = "Show Tooltip on Seeds",
		section = tooltipSection,
		description = "Toggle recipe tooltip on seeds."
	)
	default boolean showTooltipOnSeeds()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = SHOW_TOOLTIP_IN_BANK,
		name = "Show Tooltip in Bank",
		section = interfaceSection,
		description = "Configure whether the tooltip appears on herblore items in the bank"
	)
	default boolean showTooltipInBank()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = SHOW_TOOLTIP_ON_PLACEHOLDER,
		name = "Show Tooltip on Placeholders",
		section = interfaceSection,
		description = "Configure whether the tooltips appears on herblore items placeholders"
	)
	default boolean showTooltipOnPlaceholder()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = SHOW_TOOLTIP_INVENTORY,
		name = "Show Tooltip in Inventory",
		section = interfaceSection,
		description = "Configure whether the tooltip appears on herblore items in the inventory"
	)
	default boolean showTooltipInInv()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = SHOW_TOOLTIP_SEED_VAULT,
		name = "Show Tooltip in Seed Vault",
		section = interfaceSection,
		description = "Configure whether the tooltip appears on herblore items in the seed vault"
	)
	default boolean showTooltipInSeedVault()
	{
		return true;
	}

	@ConfigItem(
		position = 5,
		keyName = SHOW_TOOLTIP_GROUP_STORAGE,
		name = "Show Tooltip in Group Storage",
		section = interfaceSection,
		description = "Configure whether the tooltip appears on herblore items in group storage"
	)
	default boolean showTooltipInGroupStorage()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = SHOW_PRIMARY_INGS,
		name = "Show Primary ingredients",
		section = tooltipContentSection,
		description = "Configure whether tooltip shows primary ingredients"
	)
	default boolean showPrimariesInTooltip()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = SHOW_SECONDARY_INGS,
		name = "Show Secondary ingredients",
		section = tooltipContentSection,
		description = "Configure whether tooltip shows secondary ingredients"
	)
	default boolean showSecondariesInTooltip()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = SHOW_HERB_LVL_REQ,
		name = "Show Herblore lvl requirement",
		section = tooltipContentSection,
		description = "Configure whether tooltip shows herblore level requirement"
	)
	default boolean showHerbloreLvlInTooltip()
	{
		return true;
	}
	
	@ConfigItem(
		position = 4,
		keyName = SHOW_IMP_REPELLENT_INGS,
		name = "Show Imp Repellent ingredients",
		section = tooltipContentSection,
		description = "Configure whether Imp Repellent tooltip shows ALL ingredients"
	)
	default boolean showImpRepellentIngs()
	{
		return false;
	}


	@ConfigItem(
		position = 1,
		keyName = USE_KEYBIND,
		name = "Use Tooltip Keybind",
		section = keybindSection,
		description = "Configure whether to press a key to show Herblore tooltips."
	)
	default boolean useKeybind()
	{
		return false;
	}

	@ConfigItem(
		position = 2,
		keyName = MODIFIER_KEYBIND,
		name = "Show Tooltip Keybind",
		section = keybindSection,
		description = "Only show Tooltips when this Hotkey is pressed."
	)
	default Keybind modifierKey()
	{
		return Keybind.NOT_SET;
	}
}
