package com.gwt.conn.client;

public class MenuItem {
	
	private String category = null;
	private String menuitem_id = null;
	private String description = null;
	private String menu = null;
	private String image = null;
	private String price = null;
	private String name = null;
	
	public MenuItem (String title) {
		this.name = title;
	}
	public MenuItem (String title, String price) {
		this.name = title;
		this.price = price;
	}
	public MenuItem (String title, String price, String description) {
		this.name = title;
		this.price = price;
		this.description = description;
	}
	
	public MenuItem (String category, String menuitem_id, String description, String menu, String image, String price, String name) {
		this.category = category;
		this.menuitem_id = menuitem_id;
		this.description = description;
		this.menu = menu;
		this.image = image;
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"category\": \"" + this.category + "\", ");
		sb.append("\"menuitem_id\": \"" + this.menuitem_id + "\", ");
		sb.append("\"description\": \"" + this.description + "\", ");
		sb.append("\"menu\": \"" + this.menu + "\", ");
		sb.append("\"image\": \"" + this.image + "\", ");
		sb.append("\"price\": \"" + this.price + "\", ");
		sb.append("\"name\": \"" + this.name + "\"");
		sb.append("}");
		return sb.toString();
	}
}