package com.herblorerecipes;

import com.google.inject.Provides;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Herblore Recipes",
	description = "Hover over a herblore ingredient or potion in your inventory or bank to see which potions can be made with it or that potion's recipe",
	tags = {"recipes", "herblore", "herb"}
)
public class HerbloreRecipesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HerbloreRecipesConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private HerbloreRecipesOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		keyManager.registerKeyListener(overlay);
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(overlay);
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (Objects.requireNonNull(gameStateChanged.getGameState()) == GameState.LOGGED_IN)
		{
			overlay.tooltipCache.preloadOnClientThread();
		}
	}

	@Provides
	HerbloreRecipesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HerbloreRecipesConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if ("herblorerecipes".equals(event.getGroup()))
		{
			overlay.tooltipCache.reset();
		}
	}
}
