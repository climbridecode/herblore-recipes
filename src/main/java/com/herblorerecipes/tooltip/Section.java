package com.herblorerecipes.tooltip;

import com.herblorerecipes.model.Recipe;
import java.util.List;
import lombok.Value;

/** One titled group of recipes in an item's tooltip. */
@Value
public class Section
{

	/** Decides the section's title and its position in the tooltip. */
	ItemRole role;
	/**
	 * The role the hovered item plays (never {@code PASTE}); the config toggle for that role decides whether the
	 * section is shown. {@code PASTE} sections on herbs additionally need the "pastes on herbs" option.
	 */
	ItemRole gate;
	List<Recipe> recipes;
}
