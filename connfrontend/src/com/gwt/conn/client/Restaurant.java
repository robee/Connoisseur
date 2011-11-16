package com.gwt.conn.client;

import java.util.ArrayList;

public class Restaurant {
	
	private String name;
	private String id;
	private ArrayList <Menu> menus;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setID(String id) {
		this.id = id; 
	}
	
	public String getID() {
		return this.id;
	}
	
	public Restaurant (String name) {
		this.name = name;
	}
	
}


