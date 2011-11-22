package com.gwt.conn.client;

public class Menu {

	private String menuName;
	private String restaurantID;
	private String id;
	
	public Menu (String menuName) {
		this.menuName = menuName;
	}
	
	public String getName() {
		return this.menuName;
	}
	public String generateJSON() {
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
	
}
