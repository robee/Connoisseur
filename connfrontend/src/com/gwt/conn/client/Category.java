package com.gwt.conn.client;

import java.util.ArrayList;
import java.util.Collections;

public class Category {
	private String title;
	private ArrayList <MenuItem> menuEntries = new ArrayList<MenuItem>();
	
	public Category (String title) {
		this.title = title;
	}
	
	public void swapMenuItems(int first, int second) {
		Collections.swap(menuEntries, first, second);
	}
	
	public void setTitle (String newTitle) {
		this.title = newTitle;
		for (MenuItem me : menuEntries) {
			me.setCategory(newTitle);
		}
	}
	public String getTitle() {
		return this.title;
	}
	
	public void addMenuItem(String itemName, String catName) {
		MenuItem mi = new MenuItem(itemName);
		mi.setCategory(catName);
		menuEntries.add(mi);
	}
	
	public void addMenuItem(MenuItem mi) {
		menuEntries.add(mi);
	}
	
	public void deleteMenuItem (String itemName) {
		for (int i = 0; i < menuEntries.size(); i++) {
			if (menuEntries.get(i).getName().equalsIgnoreCase(itemName)) {
				menuEntries.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<MenuItem> getMenuItems() {
		return menuEntries;
	}
	
	public String[] getMenuItemNames() {
		String[] menuItemNames = new String[menuEntries.size()];
		for (int i = 0; i < menuItemNames.length; i++) {
			menuItemNames[i] = menuEntries.get(i).getName();
		}
		return menuItemNames;
	}
	
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		String commaPrefix = "";
		
		sb.append("\"" + getTitle() + "\":[");
		for (MenuItem me : menuEntries) {
			sb.append(commaPrefix);
			commaPrefix = ",";
			sb.append(me.generateJSON());
		}
		sb.append("]");
		return sb.toString();
	}
}