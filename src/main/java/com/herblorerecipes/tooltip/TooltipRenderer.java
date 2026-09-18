package com.herblorerecipes.tooltip;

import com.herblorerecipes.HerbloreRecipesConfig;
import com.herblorerecipes.model.Recipe;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import net.runelite.client.util.ColorUtil;

/**
 * Turns an item's {@link Section}s into tooltip text. Pure: it needs only item names and the config.
 */
public final class TooltipRenderer
{

	private static final String LINE_BREAK = "</br>";

	private static final Color GREY = new Color(238, 238, 238);
	private static final Color LIME = new Color(0, 255, 0);
	private static final Color AQUA = new Color(0, 255, 255);
	private static final Color GOLD = new Color(255, 215, 0);
	private static final Color PINK = new Color(236, 128, 255);
	private static final Color PURPLE = new Color(181, 153, 255);
	private static final Color ORANGE = new Color(255, 140, 103);
	private static final Color TURQUOISE = new Color(112, 248, 208);
	private static final Color SALMON = new Color(248, 112, 123);
	private static final Color GREEN = new Color(66, 187, 123);

	private static final String PRIMARY_TITLE = colorWrap("Primary", LIME) + colorWrap(" for:", GREY);
	private static final String SECONDARY_TITLE = colorWrap("Secondary", AQUA) + colorWrap(" for:", GREY);
	private static final String UNFINISHED_TITLE = colorWrap("Unfinished", GOLD) + colorWrap(" for:", GREY);
	private static final String BASE_TITLE = colorWrap("Base", PURPLE) + colorWrap(" for:", GREY);
	private static final String SEED_TITLE = colorWrap("Seed", ORANGE) + colorWrap(" for:", GREY);
	private static final String GRIMY_TITLE = colorWrap("Clean", TURQUOISE) + colorWrap(" for:", GREY);
	private static final String PASTE_TITLE = colorWrap("Paste", SALMON) + colorWrap(" for:", GREY);
	private static final String POTION_TITLE = colorWrap("Requirements", PINK) + colorWrap(" for %s:", GREY);
	private static final String PASTE_RECIPES_TITLE = colorWrap("To make", GREEN) + colorWrap(" %s:", GREY);

	private final RecipeIndex index;
	private final IntFunction<String> itemName;
	private final HerbloreRecipesConfig config;

	public TooltipRenderer(RecipeIndex index, IntFunction<String> itemName, HerbloreRecipesConfig config)
	{
		this.index = index;
		this.itemName = itemName;
		this.config = config;
	}

	/** The tooltip text for an item, or empty if there is nothing to show (or the config hides all of it). */
	public Optional<String> render(int itemId)
	{
		StringBuilder text = new StringBuilder();
		for (Section section : index.sectionsOf(itemId))
		{
			if (!isEnabled(section.getGate()))
			{
				continue;
			}
			text.append(title(section)).append(LINE_BREAK);
			for (Recipe recipe : section.getRecipes())
			{
				String line = section.getRole() == ItemRole.PASTE_RECIPES ? herbLine(recipe) : line(recipe);
				text.append(line).append(LINE_BREAK);
			}
		}
		return text.length() == 0 ? Optional.empty() : Optional.of(text.toString());
	}

	private boolean isEnabled(ItemRole gate)
	{
		switch (gate)
		{
			case POTION:
				return config.showTooltipOnPotions();
			case PRIMARY:
				return config.showTooltipOnPrimaries();
			case SECONDARY:
				return config.showTooltipOnSecondaries();
			case COMPLEX_BASE:
				return config.showTooltipOnComplex();
			case SEED:
				return config.showTooltipOnPrimarySeeds();
			case UNFINISHED:
				return config.showTooltipOnUnfinished();
			case GRIMY:
				return config.showTooltipOnGrimy();
			default:
				throw new IllegalArgumentException(gate + " is not a role an item can have");
		}
	}

	private static String title(Section section)
	{
		switch (section.getRole())
		{
			case POTION:
				return String.format(POTION_TITLE, colorWrap(section.getRecipes().get(0).getName(), AQUA));
			case PASTE_RECIPES:
				return String.format(PASTE_RECIPES_TITLE, colorWrap(section.getRecipes().get(0).getName(), AQUA));
			case PASTE:
				return PASTE_TITLE;
			case PRIMARY:
				return PRIMARY_TITLE;
			case SECONDARY:
				return SECONDARY_TITLE;
			case COMPLEX_BASE:
				return BASE_TITLE;
			case SEED:
				return SEED_TITLE;
			case UNFINISHED:
				return UNFINISHED_TITLE;
			case GRIMY:
				return GRIMY_TITLE;
			default:
				throw new IllegalArgumentException("No title for " + section.getRole());
		}
	}

	/** The herb a paste is made from, e.g. {@code lvl 1: Ranarr weed}. The paste's own name is already in the title. */
	private String herbLine(Recipe recipe)
	{
		return levelPrefix(recipe) + itemName.apply(recipe.getPrimary());
	}

	/** e.g. {@code lvl 3: Attack potion (1st: Guam leaf - 2nd: Eye of newt)} */
	private String line(Recipe recipe)
	{
		StringBuilder line = new StringBuilder(levelPrefix(recipe));
		line.append(recipe.getName());

		List<String> ingredients = new ArrayList<>();
		if (config.showPrimariesInTooltip() && recipe.getPrimary() > 0)
		{
			ingredients.add("1st: " + itemName.apply(recipe.getPrimary()));
		}
		if (config.showSecondariesInTooltip() && !recipe.getSecondaries().isEmpty())
		{
			ingredients.add("2nd: " + secondaries(recipe));
		}
		if (!ingredients.isEmpty())
		{
			line.append(" (").append(String.join(" - ", ingredients)).append(")");
		}
		return line.toString();
	}

	private String levelPrefix(Recipe recipe)
	{
		return config.showHerbloreLvlInTooltip() ? "lvl " + recipe.getLevel() + ": " : "";
	}

	private String secondaries(Recipe recipe)
	{
		if (recipe.isCollapsibleSecondaries() && !config.showImpRepellentIngs())
		{
			return "Various flowers...";
		}
		return recipe.getSecondaries().stream()
			.map(itemName::apply)
			.collect(Collectors.joining(", "));
	}

	private static String colorWrap(String text, Color color)
	{
		return ColorUtil.wrapWithColorTag(text, color);
	}
}
