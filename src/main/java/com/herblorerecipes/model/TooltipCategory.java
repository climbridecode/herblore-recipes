package com.herblorerecipes.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TooltipCategory
{

	private String title;
	private List<TooltipCategoryContent> content;

	public TooltipCategory()
	{
		this.content = new ArrayList<>();
	}
}
