package com.herblorerecipes;

import com.herblorerecipes.cache.TooltipCache;
import com.herblorerecipes.model.Potions;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.Keybind;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import org.apache.commons.lang3.StringUtils;

public class HerbloreRecipesOverlay extends Overlay implements KeyListener
{

	private static final int INVENTORY_ITEM_WIDGETID = WidgetInfo.INVENTORY.getPackedId();
	private static final int BANK_ITEM_WIDGETID = WidgetInfo.BANK_ITEM_CONTAINER.getPackedId();
	private static final int BANKED_INVENTORY_ITEM_WIDGETID = WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId();
	private static final int SEED_VAULT_INVENTORY_WIDGETID = WidgetInfo.SEED_VAULT_INVENTORY_ITEMS_CONTAINER.getPackedId();
	private static final int SEED_VAULT_WIDGETID = WidgetInfo.SEED_VAULT_ITEM_CONTAINER.getPackedId();
	private static final int GROUP_STORAGE_ITEM_WIDGETID = WidgetInfo.GROUP_STORAGE_ITEM_CONTAINER.getPackedId();
	public final TooltipCache tooltipCache;
	private final Client client;
	private final TooltipManager tooltipManager;
	private final HerbloreRecipesConfig config;
	private final ItemManager itemManager;

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
			!config.showTooltipOnSeeds() && !config.showTooltipOnGrimy() &&
			!config.showTooltipOnComplex())
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
				// if keybind isn't set, but user wants to use keybind, don't render tooltip
				return null;
			}

			// if here, keybind is set, ensure it's pressed
			// if it isn't pressed, return null
			if (!boundKeyPressed)
			{
				return null;
			}
		}

		final MenuEntry[] menuEntries = client.getMenuEntries();
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
		final int widgetId = menuEntry.getParam1();
		final int groupId = WidgetInfo.TO_GROUP(widgetId);

		switch (action)
		{
			case WIDGET_USE_ON_ITEM:
			case WIDGET_TARGET:
			case CC_OP:
			case ITEM_USE:
			case ITEM_FIRST_OPTION:
			case ITEM_SECOND_OPTION:
			case ITEM_THIRD_OPTION:
			case ITEM_FOURTH_OPTION:
			case CC_OP_LOW_PRIORITY:
			case ITEM_FIFTH_OPTION:
				switch (groupId)
				{
					case WidgetID.BANK_INVENTORY_GROUP_ID:
					case WidgetID.SEED_VAULT_GROUP_ID:
					case WidgetID.SEED_VAULT_INVENTORY_GROUP_ID:
						if (!config.showTooltipInSeedVault())
						{
							return null;
						}
					case WidgetID.GROUP_STORAGE_GROUP_ID:
						if (!config.showTooltipInGroupStorage())
						{
							return null;
						}
					case WidgetID.INVENTORY_GROUP_ID:
						if (!config.showTooltipInInv())
						{
							return null;
						}
					case WidgetID.BANK_GROUP_ID:
						if (!config.showTooltipOnPlaceholder() && action == MenuAction.CC_OP_LOW_PRIORITY)
						{
							// item is bank placeholder - return null
							return null;
						}
						if (!config.showTooltipInBank())
						{
							return null;
						}

						ItemContainer container = getContainer(widgetId);
						if (container == null)
						{
							return null;
						}


						Item item = container.getItem(menuEntry.getParam0());
						if (item == null)
						{
							return null;
						}

						int itemId = itemManager.canonicalize(item.getId());


						if (Potions.isSeed(itemId) && !config.showTooltipOnSeeds())
						{
							return null;
						}

						if (Potions.isUnfinished(itemId) && !config.showTooltipOnUnfinished())
						{
							return null;
						}

						if (Potions.isGrimy(itemId) && !config.showTooltipOnGrimy())
						{
							return null;
						}

						if (tooltipCache.contains(itemId))
						{
							tooltipManager.add(tooltipCache.get(itemId));
						}
				}
		}
		return null;
	}

	private ItemContainer getContainer(int widgetId)
	{
		if (widgetId == INVENTORY_ITEM_WIDGETID || widgetId == BANKED_INVENTORY_ITEM_WIDGETID || widgetId == SEED_VAULT_INVENTORY_WIDGETID)
		{
			return client.getItemContainer(InventoryID.INVENTORY);
		}
		else if (widgetId == BANK_ITEM_WIDGETID)
		{
			return client.getItemContainer(InventoryID.BANK);
		}
		else if (widgetId == SEED_VAULT_WIDGETID)
		{
			return client.getItemContainer(InventoryID.SEED_VAULT);
		}
		else if (widgetId == GROUP_STORAGE_ITEM_WIDGETID)
		{
			return client.getItemContainer(InventoryID.GROUP_STORAGE);
		}
		return null;
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
