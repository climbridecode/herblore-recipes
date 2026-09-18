package com.herblorerecipes;

import com.herblorerecipes.cache.TooltipCache;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.runelite.api.ItemComposition;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;

/**
 * Builds a {@link TooltipCache} against test doubles and dumps every tooltip it produces.
 * Item names are the numeric item id ("#249"), which is stable across RuneLite updates.
 */
public final class TooltipHarness
{
	private TooltipHarness()
	{
	}

	public static TooltipCache cache(HerbloreRecipesConfig config)
	{
		TooltipCache cache = new TooltipCache(itemManager(), immediateClientThread(), config);
		cache.preloadOnClientThread();
		return cache;
	}

	public static List<String> dump(HerbloreRecipesConfig config)
	{
		return dump(cache(config));
	}

	public static List<String> dump(TooltipCache cache)
	{

		List<String> lines = new ArrayList<>();
		for (int id : allItemIds())
		{
			if (cache.contains(id))
			{
				lines.add("== " + id);
				for (String line : cache.get(id).getText().split("</br>", -1))
				{
					lines.add(line);
				}
			}
		}
		return lines;
	}

	private static ItemManager itemManager()
	{
		ItemManager itemManager = mock(ItemManager.class);
		Map<Integer, ItemComposition> compositions = new HashMap<>();
		when(itemManager.getItemComposition(anyInt())).thenAnswer(invocation ->
		{
			int id = invocation.getArgument(0);
			return compositions.computeIfAbsent(id, i ->
			{
				ItemComposition composition = mock(ItemComposition.class);
				when(composition.getName()).thenReturn("#" + i);
				return composition;
			});
		});
		return itemManager;
	}

	private static ClientThread immediateClientThread()
	{
		ClientThread clientThread = mock(ClientThread.class);
		doAnswer(invocation ->
		{
			((Runnable) invocation.getArgument(0)).run();
			return null;
		}).when(clientThread).invoke(any(Runnable.class));
		return clientThread;
	}

	/** Every item id the game defines, independent of what the plugin knows about. */
	private static Iterable<Integer> allItemIds()
	{
		TreeSet<Integer> ids = new TreeSet<>();
		for (Field field : ItemID.class.getFields())
		{
			if (field.getType() == int.class && Modifier.isStatic(field.getModifiers()))
			{
				try
				{
					int id = field.getInt(null);
					if (id >= 0)
					{
						ids.add(id);
					}
				}
				catch (IllegalAccessException e)
				{
					throw new IllegalStateException(e);
				}
			}
		}
		return ids;
	}
}
