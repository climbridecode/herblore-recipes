package com.herblorerecipes.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import lombok.Value;
import net.runelite.api.gameval.ItemID;

/**
 * The Mastering Mixology pastes and the herbs each one is ground from.
 * A herb's grimy and seed forms are not listed here; they are looked up through {@link HerbForms}.
 */
@Getter
public enum Paste
{

	MOX("Mox paste", ItemID.MM_MOX_PASTE, ImmutableList.of(
		herb(ItemID.GUAM_LEAF, 1),
		herb(ItemID.MARENTILL, 1),
		herb(ItemID.TARROMIN, 1),
		herb(ItemID.HARRALANDER, 1))),

	LYE("Lye paste", ItemID.MM_LYE_PASTE, ImmutableList.of(
		herb(ItemID.RANARR_WEED, 1),
		herb(ItemID.TOADFLAX, 1),
		herb(ItemID.AVANTOE, 1),
		herb(ItemID.KWUARM, 1),
		herb(ItemID.SNAPDRAGON, 1))),

	AGA("Aga paste", ItemID.MM_AGA_PASTE, ImmutableList.of(
		herb(ItemID.IRIT_LEAF, 1),
		herb(ItemID.HUASCA, 60),
		herb(ItemID.CADANTINE, 1),
		herb(ItemID.LANTADYME, 1),
		herb(ItemID.DWARF_WEED, 60),
		herb(ItemID.TORSTOL, 1)));

	private final String name;
	private final int itemId;
	private final List<HerbRecipe> herbs;

	Paste(String name, int itemId, List<HerbRecipe> herbs)
	{
		this.name = name;
		this.itemId = itemId;
		this.herbs = herbs;
	}

	private static HerbRecipe herb(int cleanHerbId, int level)
	{
		return new HerbRecipe(cleanHerbId, level);
	}

	/** One way to make the paste: grind this (clean or grimy) herb. */
	@Value
	public static class HerbRecipe
	{
		int herb;
		int level;
	}
}
