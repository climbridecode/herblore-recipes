package com.herblorerecipes;

import net.runelite.api.MenuAction;
import net.runelite.api.gameval.InterfaceID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HerbloreRecipesOverlayTest
{

	private final TestConfig config = new TestConfig();
	private final HerbloreRecipesOverlay overlay = new HerbloreRecipesOverlay(null, null, config, null, null);

	@Test
	public void enabledInEveryKnownInterfaceByDefault()
	{
		int[] groups = {
			InterfaceID.INVENTORY, InterfaceID.BANKSIDE, InterfaceID.BANKMAIN,
			InterfaceID.SEED_VAULT, InterfaceID.SEED_VAULT_DEPOSIT,
			InterfaceID.SHARED_BANK, InterfaceID.SHARED_BANK_SIDE, InterfaceID.GIM_SIDEPANEL,
		};
		for (int group : groups)
		{
			assertTrue("group " + group, overlay.isEnabledIn(group, MenuAction.CC_OP));
		}
	}

	@Test
	public void disabledInUnknownInterfaces()
	{
		assertFalse(overlay.isEnabledIn(InterfaceID.EQUIPMENT_SIDE, MenuAction.CC_OP));
	}

	@Test
	public void inventoryToggleCoversInventoryAndBankInventory()
	{
		config.inInventory = false;

		assertFalse(overlay.isEnabledIn(InterfaceID.INVENTORY, MenuAction.CC_OP));
		assertFalse(overlay.isEnabledIn(InterfaceID.BANKSIDE, MenuAction.CC_OP));
		assertTrue(overlay.isEnabledIn(InterfaceID.BANKMAIN, MenuAction.CC_OP));
	}

	@Test
	public void seedVaultToggleCoversVaultAndItsInventory()
	{
		config.inSeedVault = false;

		assertFalse(overlay.isEnabledIn(InterfaceID.SEED_VAULT, MenuAction.CC_OP));
		assertFalse(overlay.isEnabledIn(InterfaceID.SEED_VAULT_DEPOSIT, MenuAction.CC_OP));
	}

	@Test
	public void groupStorageToggleCoversAllGroupStorageInterfaces()
	{
		config.inGroupStorage = false;

		assertFalse(overlay.isEnabledIn(InterfaceID.SHARED_BANK, MenuAction.CC_OP));
		assertFalse(overlay.isEnabledIn(InterfaceID.SHARED_BANK_SIDE, MenuAction.CC_OP));
		assertFalse(overlay.isEnabledIn(InterfaceID.GIM_SIDEPANEL, MenuAction.CC_OP));
	}

	@Test
	public void bankToggleCoversItemsAndPlaceholders()
	{
		config.inBank = false;

		assertFalse(overlay.isEnabledIn(InterfaceID.BANKMAIN, MenuAction.CC_OP));
		assertFalse(overlay.isEnabledIn(InterfaceID.BANKMAIN, MenuAction.CC_OP_LOW_PRIORITY));
	}

	@Test
	public void placeholderToggleOnlyHidesPlaceholders()
	{
		config.onPlaceholder = false;

		assertTrue(overlay.isEnabledIn(InterfaceID.BANKMAIN, MenuAction.CC_OP));
		assertFalse(overlay.isEnabledIn(InterfaceID.BANKMAIN, MenuAction.CC_OP_LOW_PRIORITY));
	}

	@Test
	public void placeholderToggleDoesNotAffectOtherInterfaces()
	{
		config.onPlaceholder = false;

		assertTrue(overlay.isEnabledIn(InterfaceID.INVENTORY, MenuAction.CC_OP_LOW_PRIORITY));
	}
}
