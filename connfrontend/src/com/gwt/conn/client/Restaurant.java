package com.gwt.conn.client;

import java.util.ArrayList;


// desired functions:

// 1 menu per rest for now

// after deserialization, save the resulting objects' into into the data storage

// getCategoryNames list, array of strings			check

// createCategory method for Menu (takes string)	check



// swap categories method, fed in 2 indices			DO THIS

// delete menuItem name passed in					check

// delete Category 									check

// when changes are made to the menu, objects should be re-serialized and saved locally

// getMenu 

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
	public Menu getMenu (String menuName) {
		for (Menu m : Menus) {
			if(m.getName().equalsIgnoreCase(menuName)) {
				return m;
			}
		}
		return null;
	}
	
	public String generateJSON(String menuName) {
		for (Menu menu : Menus) {
			if (menu.getName().equalsIgnoreCase(menuName)) {
				return menu.generateJSON();
			}
		}
		return null;
	}
	public void addMenuItem(String menu, String category, String itemName) {
		for (Menu m : Menus) {
			if(m.getName().equalsIgnoreCase(menu)) {
				m.addMenuItem(category, itemName);
			}
		}
	}
	public void deleteMenuItem (String menu, String category, String itemName) {
		for (Menu m : Menus) {
			if (m.getName().equalsIgnoreCase(menu)) {
				m.deleteMenuItem(category, itemName);
			}
		}
	}
	public String[] getCategoryNames (String menuName) {
		for (Menu menu : Menus) {
			if (menu.getName().equalsIgnoreCase(menuName)) {
				return menu.getCategoryNames();
			}
		}
		return null;
	}
	
	public void deleteCategory (String menuName, String category) {
		for (Menu menu : Menus) {
			if (menu.getName().equalsIgnoreCase(menuName)) {
				menu.deleteCategory(category);
			}
		}
	}
	
	public void createCategory (String menuName, String newCategoryName) {
		for (Menu menu : Menus) {
			if (menu.getName().equalsIgnoreCase(menuName)) {
				menu.addCategory(newCategoryName);
			}
		}
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