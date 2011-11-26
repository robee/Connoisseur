package com.gwt.conn.client;
// OBSOLETE?
public class MenuEntry {
	
	private String category;
	private String title;
	private double price;
	private String description;
	private String id;
	private String image;
	
	public MenuEntry (String title) {
		this.title = title;
	}
	public MenuEntry (String title, double price) {
		this.title = title;
		this.price = price;
	}
	public MenuEntry (String title, double price, String description) {
		this.title = title;
		this.price = price;
		this.description = description;
	}
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"category\": \"" + this.category + "\", ");
		sb.append("\"menuitem_id\": \"" + this.id + "\", ");
		sb.append("\"description\": \"" + this.description + "\", ");
		sb.append("\"menu\": null, ");
		sb.append("\"image\": \"" + this.image + "\", ");
		sb.append("\"price\": \"" + this.price + "\", ");
		sb.append("\"name\": \"" + this.title + "\"");
		sb.append("}");
		return sb.toString();
	}
}