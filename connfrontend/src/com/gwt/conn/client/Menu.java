package com.gwt.conn.client;

import java.util.ArrayList;

public class Menu {

	private String menuName;
	private String restaurantID;
	private String id;
	private ArrayList <Category> Categories = new ArrayList<Category>();
	
	public Menu (String menuName) {
		this.menuName = menuName;
	}
	
	public String getName() {
		return this.menuName;
	}
	
	public void setRestaurantID (String id) {
		this.restaurantID = id;
	}
	
	public void setID (String id) {
		this.id = id;
	}
	
	public String[] getCategoryNames() {
		String [] categoryNames = new String[Categories.size()];
		for (int i = 0; i < categoryNames.length; i++) {
			categoryNames[i] = Categories.get(i).getTitle();
		}
		return categoryNames;
	}
	
	public void createCategory(String newCategoryName) {
		Category newCat = new Category (newCategoryName);
		Categories.add(newCat);
	}
	public void addMenuItem (String category, String itemName) {
		for (Category c : Categories) {
			if (c.getTitle().equalsIgnoreCase(category)) {
				c.addMenuItem(itemName);
			}
		}
	}
	public void deleteMenuItem (String category, String itemName) {
		for (Category c : Categories) {
			if (c.getTitle().equalsIgnoreCase(category)) {
				c.deleteMenuItem(itemName);
			}
		}
	}
	public void deleteCategory (String category) {
		for (int i = 0; i < Categories.size(); i++) {
			if (Categories.get(i).getTitle().equalsIgnoreCase(category)) {
				Categories.remove(i);
				break;
			}
		}
	}
	
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		String commaPrefix = "";
		
		sb.append("{");
		sb.append("\"menu_id\": \"" + this.id + "\", ");
		sb.append("\"restaurant_id\": \"" + this.restaurantID + "\", ");
		sb.append("\"menu_name\": \"" + this.menuName + "\", ");
		sb.append("\"ui_profile\": {");
		sb.append("\"logo_url\": \"" + null + "\", ");
		sb.append("\"color\": \"" + null + "\", ");
		sb.append("\"menu\": \"" + null + "\", ");
		sb.append("\"profile_id\": \"" + null + "\", ");
		sb.append("\"template\": \"" + null + "\", ");
		sb.append("\"font\": \"" + null + "\"");
		sb.append("}, ");
		sb.append("\"restaurant_name\": \"" + null + "\", ");
		sb.append("\"menuitems\": {");
		//loop CATEGORY get JSON
		for (Category c : Categories) {
			sb.append(commaPrefix);
			commaPrefix = ",";
			sb.append(c.generateJSON());
		}
		sb.append("}}");
		
		return sb.toString();
	}	
}