package com.herblorerecipes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TooltipCategoryContent
{

	private String potion;
	private String level;
	private String primary;
	private String secondary;
	private String base;

	public TooltipCategoryContent()
	{
		potion = "";
		level = "";
		primary = "";
		secondary = "";
		base = "";
	}
}
