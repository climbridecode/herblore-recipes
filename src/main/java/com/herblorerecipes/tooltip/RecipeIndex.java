package com.herblorerecipes.tooltip;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.herblorerecipes.model.HerbForms;
import com.herblorerecipes.model.Paste;
import com.herblorerecipes.model.Potion;
import com.herblorerecipes.model.Potions;
import com.herblorerecipes.model.Recipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Value;
import net.runelite.api.gameval.ItemID;

/**
 * Immutable lookup from an item id to everything its tooltip should say about it.
 */
public final class RecipeIndex
{

	private final Map<Integer, List<Section>> sections;

	private RecipeIndex(Map<Integer, List<Section>> sections)
	{
		this.sections = sections;
	}

	public static RecipeIndex build()
	{
		List<Potion> potions = Arrays.stream(Potions.values()).map(p -> p.potion).collect(Collectors.toList());
		Map<Integer, HerbForms> herbs = HerbForms.fromPotions(potions);
		Builder builder = new Builder();

		Set<Integer> potionIdsSeen = new HashSet<>();
		for (Potion potion : potions)
		{
			Recipe recipe = Recipe.of(potion);

			// an item id is a potion of the first recipe that lists it
			for (int id : potion.getIds())
			{
				if (potionIdsSeen.add(id))
				{
					builder.add(id, ItemRole.POTION, ItemRole.POTION, recipe);
				}
			}

			if (isImpRepellent(potion))
			{
				// imp repellent is the one potion with many primaries (flowers), which are stored as its secondaries
				potion.getSecondaries().forEach(flower -> builder.add(flower, ItemRole.PRIMARY, ItemRole.PRIMARY, recipe));
			}
			else
			{
				builder.add(potion.getPrimary(), ItemRole.PRIMARY, ItemRole.PRIMARY, recipe);
				builder.add(potion.getPrimaryAlt(), ItemRole.PRIMARY, ItemRole.PRIMARY, recipe);
				builder.add(potion.getGrimyHerb(), ItemRole.GRIMY, ItemRole.GRIMY, recipe);
			}
			builder.add(potion.getPrimarySeed(), ItemRole.SEED, ItemRole.SEED, recipe);
			builder.add(potion.getUnfinishedPotion(), ItemRole.UNFINISHED, ItemRole.UNFINISHED, recipe);

			if (potion.hasComplexBase())
			{
				potion.getComplexBase().forEach(base -> base.getIds()
					.forEach(baseId -> builder.add(baseId, ItemRole.COMPLEX_BASE, ItemRole.COMPLEX_BASE, recipe)));
			}
		}

		// Alternate secondaries are listed after the regular ones, so they need their own pass.
		for (Potion potion : potions)
		{
			if (potion.getSecondaries() != null && !isImpRepellent(potion))
			{
				potion.getSecondaries().forEach(id -> builder.add(id, ItemRole.SECONDARY, ItemRole.SECONDARY, Recipe.of(potion)));
			}
		}
		for (Potion potion : potions)
		{
			if (potion.getSecondariesAlt() != null)
			{
				potion.getSecondariesAlt().forEach(id -> builder.add(id, ItemRole.SECONDARY, ItemRole.SECONDARY, Recipe.of(potion)));
			}
		}

		for (Paste paste : Paste.values())
		{
			for (Paste.HerbRecipe herbRecipe : paste.getHerbs())
			{
				Recipe recipe = Recipe.of(paste, herbRecipe);
				builder.add(paste.getItemId(), ItemRole.PASTE_RECIPES, ItemRole.PASTE_RECIPES, recipe);

				// the paste shows on every form of the herb, and follows that form's own config toggle
				builder.add(herbRecipe.getHerb(), ItemRole.PASTE, ItemRole.PRIMARY, recipe);
				HerbForms forms = herbs.get(herbRecipe.getHerb());
				if (forms != null)
				{
					builder.add(forms.getGrimy(), ItemRole.PASTE, ItemRole.GRIMY, recipe);
					builder.add(forms.getSeed(), ItemRole.PASTE, ItemRole.SEED, recipe);
				}
			}
		}

		return builder.build();
	}

	private static boolean isImpRepellent(Potion potion)
	{
		return potion.getIds().contains(ItemID.II_IMP_REPELLENT);
	}

	/** Every item id that has at least one section. */
	public Set<Integer> knownIds()
	{
		return sections.keySet();
	}

	/** The sections for an item, in display order. Empty if the item is not part of any recipe. */
	public List<Section> sectionsOf(int itemId)
	{
		return sections.getOrDefault(itemId, ImmutableList.of());
	}

	private static final class Builder
	{

		@Value
		private static class Key
		{
			ItemRole role;
			ItemRole gate;
		}

		private final Map<Integer, Map<Key, List<Recipe>>> recipesById = new HashMap<>();

		/** Item id 0 means "not set" in the potion data, so it is never indexed. */
		void add(int itemId, ItemRole role, ItemRole gate, Recipe recipe)
		{
			if (itemId <= 0)
			{
				return;
			}
			List<Recipe> recipes = recipesById
				.computeIfAbsent(itemId, id -> new LinkedHashMap<>())
				.computeIfAbsent(new Key(role, gate), key -> new ArrayList<>());
			if (!recipes.contains(recipe))
			{
				recipes.add(recipe);
			}
		}

		RecipeIndex build()
		{
			ImmutableMap.Builder<Integer, List<Section>> built = ImmutableMap.builder();
			recipesById.forEach((id, byKey) -> built.put(id, byKey.entrySet().stream()
				.map(entry -> new Section(entry.getKey().getRole(), entry.getKey().getGate(), ImmutableList.copyOf(entry.getValue())))
				.sorted(Comparator.comparing(Section::getRole))
				.collect(ImmutableList.toImmutableList())));
			return new RecipeIndex(built.build());
		}
	}
}
