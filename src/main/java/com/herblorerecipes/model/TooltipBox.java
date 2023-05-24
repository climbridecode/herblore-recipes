package com.herblorerecipes.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TooltipBox
{

	private List<TooltipCategory> categories;

	public TooltipBox()
	{
		categories = new ArrayList<>();
	}

	public boolean isEmpty()
	{
		return categories.isEmpty();
	}
}
