package com.gwt.conn.client;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class Menu {

	// private fields that represent a menu object and menu JSON object
	private String menuName = null;
	private String restaurantID = null;
	private String id = null;
	private String logo_url = null;
	private String color = null;
	private String menu = null;
	private String profile_id = null;
	private String template = null;
	private String font = null;
	private String restaurant_name = null;
	private ArrayList <Category> categories = new ArrayList<Category>();
	
	public void swapCategories(int first, int second) {
		Collections.swap(categories, first, second);
	}
	
	public ArrayList<Category> getCategories() {
		return this.categories;
	}
	
	public Menu (String menuName) {
		this.menuName = menuName;
	}
	
	public String getName() {
		return this.menuName;
	}
	
	public void setRestaurantName(String restName) {
		this.restaurant_name = restName;
	}
	
	public void setRestaurantID (String id) {
		this.restaurantID = id;
	}
	
	public void setID (String id) {
		this.id = id;
	}
	
	public String getLogo() {
		return this.logo_url;
	}
	
	public void setLogo(String logoURL) {
		this.logo_url = logoURL;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	public void setProfile(String profileID) {
		this.profile_id = profileID;
	}
	
	public void setTemplate (String template) {
		this.template = template;
	}
	
	public String getTemplate() {
		return this.template;
	}
	
	public void setFont(String newFont) {
		this.font = newFont;
	}
	
	public String[] getCategoryNames() {
		String [] categoryNames = new String[categories.size()];
		for (int i = 0; i < categoryNames.length; i++) {
			categoryNames[i] = categories.get(i).getTitle();
		}
		return categoryNames;
	}

	public void addCategory(String newCategoryName) {
		Category newCat = new Category (newCategoryName);
		categories.add(newCat);
	}
	
	public void addCategory(Category newCategory) {
		categories.add(newCategory);
	}
	
	public void addMenuItem (String category, String itemName) {
		for (Category c : categories) {
			if (c.getTitle().equalsIgnoreCase(category)) {
				c.addMenuItem(itemName, c.getTitle());
			}
		}
	}
	
	public void deleteMenuItem (String category, String itemName) {
		for (Category c : categories) {
			if (c.getTitle().equalsIgnoreCase(category)) {
				c.deleteMenuItem(itemName);
			}
		}
	}
	
	public void deleteCategory (String category) {
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getTitle().equalsIgnoreCase(category)) {
				categories.remove(i);
				break;
			}
		}
	}
	
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		String commaPrefix = "";
		
		sb.append("{");
		sb.append("\"menu_id\":\"" + this.id + "\",");
		sb.append("\"restaurant_id\":\"" + this.restaurantID + "\",");
		sb.append("\"menu_name\":\"" + this.menuName + "\",");
		sb.append("\"ui_profile\":{");
		sb.append("\"logo_url\":\"" + this.logo_url + "\",");
		sb.append("\"color\":\"" + this.color + "\",");
		sb.append("\"menu\":null,");
		sb.append("\"profile_id\":\"" + this.profile_id + "\",");
		sb.append("\"template\":\"" + this.template + "\",");
		sb.append("\"font\":\"" + this.font + "\"");
		sb.append("},");
		sb.append("\"restaurant_name\":\"" + this.restaurant_name + "\",");
		sb.append("\"menuitems\":{");
		//loop CATEGORY get JSON
		for (Category c : categories) {
			sb.append(commaPrefix);
			commaPrefix = ",";
			sb.append(c.generateJSON());
		}
		sb.append("}}");
		
		return sb.toString();
	}
	
} // Menu
