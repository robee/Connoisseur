package com.gwt.conn.client;

import java.util.ArrayList;

public class Restaurant {
	
	private String name;
	private String id;
	private ArrayList <Menu> Menus = new ArrayList<Menu>();
	
	public String getName() {
		return this.name;
	}
	
	//public void setName(String name) {
	//	this.name = name;
	//}
	
	public void setID(String id) {
		this.id = id; 
	}
	
	public String getID() {
		return this.id;
	}
	
	public Restaurant (String name) {
		this.name = name;
	}
	
	public void addMenu(Menu menu) {
		Menus.add(menu);
	}	
	public String generateJSON(String menuName) {
		for (Menu menu : Menus) {
			if (menu.getName().equalsIgnoreCase(menuName)) {
				return menu.generateJSON();
			}
		}
		return null;
	}
	
	// TEST REMOVE
	public void deleteMenu (String menuName) {
		for (Menu menu : Menus) {
			if (menu.getName().equalsIgnoreCase(menuName)) {
				Menus.remove(menu);								
				break;
			}
		}
	}
}