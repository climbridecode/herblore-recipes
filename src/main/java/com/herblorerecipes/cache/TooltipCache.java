package com.herblorerecipes.cache;

import com.google.common.base.Stopwatch;
import com.herblorerecipes.HerbloreRecipesConfig;
import com.herblorerecipes.tooltip.RecipeIndex;
import com.herblorerecipes.tooltip.TooltipRenderer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.tooltip.Tooltip;

/**
 * Holds the rendered tooltip for every item. Item names are only available on the client thread,
 * so the whole cache is rebuilt there, at login and whenever the config changes.
 */
@Slf4j
public class TooltipCache
{

	private static final RecipeIndex INDEX = RecipeIndex.build();

	private final ItemManager itemManager;
	private final ClientThread clientThread;
	private final HerbloreRecipesConfig config;
	private volatile Map<Integer, Tooltip> tooltips = Collections.emptyMap();

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
		Stopwatch timer = Stopwatch.createStarted();
		TooltipRenderer renderer = new TooltipRenderer(INDEX, this::itemName, config);
		Map<Integer, Tooltip> built = new HashMap<>();
		for (int id : INDEX.knownIds())
		{
			renderer.render(id).ifPresent(text -> built.put(id, new Tooltip(text)));
		}
		tooltips = built;
		log.debug("Tooltip cache was loaded in {}ms.", timer.stop().elapsed().toNanos() / 1000000.0);
	}

	public boolean contains(int id)
	{
		return tooltips.containsKey(id);
	}

	public Tooltip get(int id)
	{
		return tooltips.get(id);
	}

	public void reset()
	{
		preloadOnClientThread();
	}

	private String itemName(int itemId)
	{
		return itemManager.getItemComposition(itemId).getName();
	}
}
