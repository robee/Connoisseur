package com.gwt.conn.client;

import java.util.ArrayList;

public class Category {
	private String title;
	private ArrayList <MenuEntry> menuEntries= new ArrayList<MenuEntry>();
	
	public Category (String title) {
		this.title = title;
	}
	
	public void setTitle (String newTitle) {
		this.title = newTitle;
	}
	public String getTitle() {
		return this.title;
	}
	
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		String commaPrefix = "";
		
		sb.append("\"" + getTitle() + "\": [");
		for (MenuEntry me : menuEntries) {
			sb.append(commaPrefix);
			commaPrefix = ",";
			sb.append(me.generateJSON());
		}
		sb.append("]");
		return sb.toString();
	}
}