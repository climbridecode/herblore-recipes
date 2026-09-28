package com.herblorerecipes;

import com.herblorerecipes.cache.TooltipCache;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.Keybind;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import org.apache.commons.lang3.StringUtils;

public class HerbloreRecipesOverlay extends Overlay implements KeyListener
{

	public final TooltipCache tooltipCache;
	private final Client client;
	private final TooltipManager tooltipManager;
	private final HerbloreRecipesConfig config;
	private final ItemManager itemManager;

	private static final Set<MenuAction> ITEM_ACTIONS = EnumSet.of(
		MenuAction.WIDGET_TARGET_ON_WIDGET,
		MenuAction.WIDGET_TARGET,
		MenuAction.CC_OP,
		MenuAction.CC_OP_LOW_PRIORITY,
		MenuAction.WIDGET_FIRST_OPTION,
		MenuAction.WIDGET_SECOND_OPTION,
		MenuAction.WIDGET_THIRD_OPTION,
		MenuAction.WIDGET_FOURTH_OPTION,
		MenuAction.WIDGET_FIFTH_OPTION
	);

	private boolean boundKeyPressed;

	@Inject
	HerbloreRecipesOverlay(Client client, TooltipManager tooltipManager, HerbloreRecipesConfig config, TooltipCache tooltipCache, ItemManager itemManager)
	{
		setPosition(OverlayPosition.DYNAMIC);
		this.client = client;
		this.tooltipManager = tooltipManager;
		this.config = config;
		this.tooltipCache = tooltipCache;
		this.itemManager = itemManager;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.isMenuOpen())
		{
			return null;
		}

		if (!config.showTooltipOnPrimaries() && !config.showTooltipOnSecondaries() &&
			!config.showTooltipOnPotions() && !config.showTooltipOnUnfinished() &&
			!config.showTooltipOnPrimarySeeds() && !config.showTooltipOnGrimy() &&
			!config.showTooltipOnComplex() && !config.showTooltipOnPastes())
		{
			// plugin is effectively disabled
			return null;
		}

		// check if user wants to use keybind
		if (config.useKeybind())
		{
			// user does want to use keybind
			// check if modifier key is set
			if (config.modifierKey().getKeyCode() == Keybind.NOT_SET.getKeyCode())
			{
				// check modifiers because of ALT/SHIFT/CTRL
				// if modifiers are 0, then return null because Keybind is truly not set.
				// refer to this code in runelite's Keybind.java:
				// 	public static final Keybind CTRL = new Keybind(KeyEvent.VK_UNDEFINED, InputEvent.CTRL_DOWN_MASK);
				//	public static final Keybind ALT = new Keybind(KeyEvent.VK_UNDEFINED, InputEvent.ALT_DOWN_MASK);
				//	public static final Keybind SHIFT = new Keybind(KeyEvent.VK_UNDEFINED, InputEvent.SHIFT_DOWN_MASK);
				if (config.modifierKey().getModifiers() == 0)
				{
					// if keybind isn't set, don't render tooltip
					return null;
				}
			}

			// if here, keybind is set, ensure it's pressed
			// if it isn't pressed, return null
			if (!boundKeyPressed)
			{
				return null;
			}
		}

		final MenuEntry[] menuEntries = client.getMenu().getMenuEntries();
		final int last = menuEntries.length - 1;

		if (last < 0)
		{
			return null;
		}
		final MenuEntry menuEntry = menuEntries[last];

		if (StringUtils.isEmpty(menuEntry.getTarget()) ||
			menuEntry.getOption().contains("View") ||
			menuEntry.getParam0() < 0)
		{
			// These are interface buttons, don't render the overlay.
			return null;
		}

		final MenuAction action = menuEntry.getType();
		if (!ITEM_ACTIONS.contains(action))
		{
			return null;
		}

		final int groupId = WidgetUtil.componentToInterface(menuEntry.getParam1());
		if (isEnabledIn(groupId, action))
		{
			showTooltip(getItemIdFromMenuEntry(menuEntry));
		}
		return null;
	}

	boolean isEnabledIn(int groupId, MenuAction action)
	{
		switch (groupId)
		{
			case InterfaceID.SHARED_BANK:
			case InterfaceID.SHARED_BANK_SIDE:
			case InterfaceID.GIM_SIDEPANEL:
				return config.showTooltipInGroupStorage();

			case InterfaceID.SEED_VAULT:
			case InterfaceID.SEED_VAULT_DEPOSIT:
				return config.showTooltipInSeedVault();

			case InterfaceID.INVENTORY:
			case InterfaceID.BANKSIDE:
				return config.showTooltipInInv();

			case InterfaceID.BANKMAIN:
				// bank placeholders use the low-priority op
				final boolean placeholder = action == MenuAction.CC_OP_LOW_PRIORITY;
				return config.showTooltipInBank() && (!placeholder || config.showTooltipOnPlaceholder());

			default:
				return false;
		}
	}

	private int getItemIdFromMenuEntry(MenuEntry menuEntry)
	{
		int itemId = menuEntry.getItemId();
		if (itemId < 0)
		{
			return -1;
		}

		return itemManager.canonicalize(itemId);
	}

	private void showTooltip(int itemId)
	{
		if (tooltipCache.contains(itemId))
		{
			tooltipManager.add(tooltipCache.get(itemId));
		}
	}


	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (config.useKeybind() && config.modifierKey().matches(e))
		{
			boundKeyPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (config.useKeybind() && config.modifierKey().matches(e))
		{
			boundKeyPressed = false;
		}
	}
}
