package com.herblorerecipes.model;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Potion
{

	private int level;
	private String name;
	private int basicBase;
	private Set<Potion> complexBase;
	private int unfinishedPotion;
	private int primary;
	private int primaryAlt;
	private int primarySeed;
	private int grimyHerb;
	private Set<Integer> secondaries;
	private Set<Integer> secondariesAlt;
	private Set<Integer> ids;

	public boolean hasComplexBase()
	{
		return complexBase != null;
	}

	public String complexBaseNames()
	{
		return complexBase.stream().map(Potion::getName).collect(Collectors.joining(", "));
	}
}
