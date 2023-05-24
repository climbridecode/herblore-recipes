package com.herblorerecipes.model;

public class TooltipStringBuilder
{

	private final StringBuilder builder;

	public TooltipStringBuilder()
	{
		this.builder = new StringBuilder();
	}

	public void appendln(String s)
	{
		this.builder.append(s).append("</br>");
	}

	public void append(String s)
	{
		this.builder.append(s);
	}

	@Override
	public String toString()
	{
		return this.builder.toString();
	}
}
