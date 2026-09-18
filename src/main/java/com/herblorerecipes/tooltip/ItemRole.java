package com.herblorerecipes.tooltip;

/**
 * The kinds of tooltip section an item can have. Declaration order is the order sections appear in a tooltip.
 */
public enum ItemRole
{
	/** The item is a potion: its own recipe. */
	POTION,
	/** The item is a paste: the herbs it can be made from. */
	PASTE_RECIPES,
	/** The item is a herb (any form): the paste it can be ground into. */
	PASTE,
	/** The item is a primary ingredient. */
	PRIMARY,
	/** The item is a secondary ingredient. */
	SECONDARY,
	/** The item is a potion used as the base of another potion. */
	COMPLEX_BASE,
	/** The item is a herb seed. */
	SEED,
	/** The item is an unfinished potion. */
	UNFINISHED,
	/** The item is a grimy herb. */
	GRIMY
}
