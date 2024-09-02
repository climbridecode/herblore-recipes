package com.herblorerecipes.cache;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.herblorerecipes.HerbloreRecipesConfig;
import com.herblorerecipes.model.Potion;
import com.herblorerecipes.model.Potions;
import com.herblorerecipes.model.TooltipBox;
import com.herblorerecipes.model.TooltipCategory;
import com.herblorerecipes.model.TooltipCategoryContent;
import com.herblorerecipes.model.TooltipStringBuilder;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.util.ColorUtil;

@Slf4j
public class TooltipCache
{

	private static final Color GREY = new Color(238, 238, 238);
	private static final Color LIME = new Color(0, 255, 0);
	private static final Color AQUA = new Color(0, 255, 255);
	private static final Color GOLD = new Color(255, 215, 0);
	private static final Color PINK = new Color(236, 128, 255);
	private static final Color PURPLE = new Color(181, 153, 255);
	private static final Color ORANGE = new Color(255, 140, 103);
	private static final Color TURQUOISE = new Color(112, 248, 208);
	private static final String TOOLTIP_PRIMARY_TEXT = colorWrap("Primary", LIME) + colorWrap(" for:", GREY);
	private static final String TOOLTIP_SECONDARY_TEXT = colorWrap("Secondary", AQUA) + colorWrap(" for:", GREY);
	private static final String TOOLTIP_UNF_TEXT = colorWrap("Unfinished", GOLD) + colorWrap(" for:", GREY);
	private static final String TOOLTIP_POTION_TEXT = colorWrap("Requirements", PINK) + colorWrap(" for %s:", GREY);
	private static final String TOOLTIP_BASE_TEXT = colorWrap("Base", PURPLE) + colorWrap(" for:", GREY);
	private static final String TOOLTIP_SEED_TEXT = colorWrap("Seed", ORANGE) + colorWrap(" for:", GREY);
	private static final String TOOLTIP_GRIMY_TEXT = colorWrap("Clean", TURQUOISE) + colorWrap(" for:", GREY);
	private final ItemManager itemManager;
	private final ClientThread clientThread;
	private final HerbloreRecipesConfig config;
	private final Map<Integer, Tooltip> tooltipCache = new HashMap<>();

	@Inject
	public TooltipCache(ItemManager itemManager, ClientThread clientThread, HerbloreRecipesConfig config)
	{
		this.itemManager = itemManager;
		this.clientThread = clientThread;
		this.config = config;
	}

	public void preloadOnClientThread()
	{
		clientThread.invoke(this::preLoadCache);
	}

	private void preLoadCache()
	{
		Set<Integer> allIds = Potions.allIds();

		Stopwatch timer = Stopwatch.createStarted();
		allIds.forEach(id -> {
			TooltipBox tooltip = new TooltipBox();

			// generate tooltip for potions
			// check if it's potion ID
			if (Potions.isPotion(id) && config.showTooltipOnPotions())
			{
				// get Potion instance
				Potion p = Potions.getPotion(id);
				// add Potion category
				tooltip.getCategories().add(tooltipCategory(ImmutableList.of(p), String.format(TOOLTIP_POTION_TEXT, colorWrap(p.getName(), AQUA))));
			}

			// generate tooltip content for primaries
			if (Potions.isPrimary(id) && config.showTooltipOnPrimaries())
			{
				// get Potions for this primary
				List<Potion> ps = Potions.getByPrimary(id);
				tooltip.getCategories().add(tooltipCategory(ps, TOOLTIP_PRIMARY_TEXT));
			}

			// generate tooltip content for secondaries
			if (Potions.isSecondary(id) && config.showTooltipOnSecondaries())
			{
				List<Potion> ps = Potions.getBySecondary(id);
				tooltip.getCategories().add(tooltipCategory(ps, TOOLTIP_SECONDARY_TEXT));
			}

			// generate tooltip content for complex bases
			if (Potions.isComplexBase(id) && config.showTooltipOnComplex())
			{
				List<Potion> ps = Potions.getByComplex(id);
				tooltip.getCategories().add(tooltipCategory(ps, TOOLTIP_BASE_TEXT));
			}

			// tooltip visibility on Seeds, Unfinisheds, and Grimy herbs can be determined at the render layer
			// generate tooltip content for seeds (basically same tooltip as primaries)
			if (Potions.isSeed(id))
			{
				List<Potion> ps = Potions.getBySeed(id);
				tooltip.getCategories().add(tooltipCategory(ps, TOOLTIP_SEED_TEXT));
			}

			// generate tooltip content for unfinished
			if (Potions.isUnfinished(id))
			{
				List<Potion> ps = Potions.getByUnfinished(id);
				tooltip.getCategories().add(tooltipCategory(ps, TOOLTIP_UNF_TEXT));
			}

			if (Potions.isGrimy(id))
			{
				// get Potions for this grimy
				List<Potion> ps = Potions.getByGrimy(id);
				tooltip.getCategories().add(tooltipCategory(ps, TOOLTIP_GRIMY_TEXT));
			}

			if (!tooltip.isEmpty())
			{
				// put actual item in map
				this.tooltipCache.put(id, buildTooltip(tooltip));
			}
		});
		long nanos = timer.stop().elapsed().toNanos();
		log.debug("Tooltip cache was loaded in {}ms.", nanos / 1000000.0);
	}

	public boolean contains(int id)
	{
		try
		{
			return this.tooltipCache.containsKey(id);
		}
		catch (NullPointerException e)
		{
			// sanity check, this shouldn't happen as id can't be null
			log.debug("NPE thrown, id: {}. e: {}", id, e);
		}
		return false;
	}

	public Tooltip get(int id)
	{
		return this.tooltipCache.get(id);
	}

	private static String colorWrap(String text, Color color)
	{
		return ColorUtil.wrapWithColorTag(text, color);
	}

	private String itemName(int itemId)
	{
		return itemManager.getItemComposition(itemId).getName();
	}

	private String itemNames(Collection<Integer> items)
	{
		return items.stream()
			.map(this::itemName)
			.collect(Collectors.joining(", "));
	}

	private TooltipCategory tooltipCategory(List<Potion> potions, String title)
	{
		TooltipCategory category = new TooltipCategory();
		category.setTitle(title);

		potions.forEach(p -> {
			TooltipCategoryContent content = new TooltipCategoryContent();
			// prepare potion
			content.setPotion(p.getName());
			// prepare level line
			content.setLevel(String.valueOf(p.getLevel()));
			// prepare primary ingredient line
			if (p.getPrimary() > 0)
			{
				content.setPrimary(itemName(p.getPrimary()));
			}
			// prepare potion base line
			if (p.hasComplexBase())
			{
				// potion has complex base
				content.setBase(p.complexBaseNames());
			}
			else
			{
				// potion necessarily has basic base
				content.setBase(itemName(p.getBasicBase()));
			}
			// prepare secondary ingredient(s) line
			if (p.getSecondaries() != null)
			{
				// there are secondaries to consider
				content.setSecondary(itemNames(p.getSecondaries()));
				// consider imp repellent bc there are so many secondaries
				if (p.getIds().contains(ItemID.IMP_REPELLENT))
				{
					if (!config.showImpRepellentIngs())
					{
						// only show this simple string for now...
						content.setSecondary("Various flowers...");
					}
				}
			}

			category.getContent().add(content);
		});

		return category;
	}

	private Tooltip buildTooltip(TooltipBox tooltip)
	{
		TooltipStringBuilder builder = new TooltipStringBuilder();
		tooltip.getCategories().forEach(category -> {
			builder.appendln(category.getTitle());
			category.getContent()
				.forEach(content ->
				{
					// check if level config is enabled
					if (config.showHerbloreLvlInTooltip())
					{
						builder.append(String.format("lvl %s: ", content.getLevel()));
					}

					// always append potion name
					builder.append(content.getPotion());

					// check if 1st ingredients OR 2nd config ingredients are enabled
					if (config.showPrimariesInTooltip() || config.showSecondariesInTooltip())
					{
						// if 1st ingredients are enabled, build primary String
						String primary = "";
						if (config.showPrimariesInTooltip())
						{
							primary = content.getPrimary();
						}
						// if 2nd ingredients are enabled, build secondary string
						String secondary = "";
						if (config.showSecondariesInTooltip())
						{
							secondary = content.getSecondary();

						}
						String combined = "";
						// if primary is empty, then combined is only secondary
						if (primary.isEmpty() && !secondary.isEmpty())
						{
							combined = "2nd: " + secondary;
						}
						// if secondary is empty, then combined is only primary
						if (secondary.isEmpty() && !primary.isEmpty())
						{
							combined = "1st: " + primary;
						}
						// if primary and secondary aren't empty, concat them with a comma
						if (!primary.isEmpty() && !secondary.isEmpty())
						{
							combined = "1st: " + primary + " - 2nd: " + secondary;
						}
						String ingredients = "%s";
						// if primary or secondary are not empty, build parenthesis
						if (!primary.isEmpty() || !secondary.isEmpty())
						{
							ingredients = " (%s)";
						}
						builder.append(String.format(ingredients, combined));
					}
					// append </br> line break
					builder.appendln("");
				});
		});
		return new Tooltip(builder.toString());
	}

	private void invalidate()
	{
		this.tooltipCache.clear();
	}

	public void reset()
	{
		invalidate();
		preloadOnClientThread();
	}
}
