package com.gwt.conn.client;

import com.google.gwt.storage.client.Storage;

/** The Storage Container Class **
 * 
 * This class contains useful functions for interacting with the local storage device.
 * Functions in this class can be called statically by any other class in this package.
 * 
 */

public class StorageContainer {

	/** This is essentially an HTML5 database for storing strings. */
	private static final Storage storage = Storage.getLocalStorageIfSupported();
	
	/** Number of menus currently in local storage. */
	private static int numMenus;
	
	/** Enforces singleton instance of the actual storage object. */
	public static Storage getStorage() {
		return storage;
	}
	
	/** Removes non-constant data from storage in case of weird overlap issues. */
	public static void initStorage() {
		numMenus = Integer.valueOf(storage.getItem("numMenus"));
	}
	
	/** Called whenever a new menu needs to be added to storage. */
	public static void addMenu(String menuName, String json) {
		// update number of menus
		numMenus++;
		
		// so we can get back the number of menus for each future session
		storage.setItem("numMenus", Integer.toString(numMenus));
		
		// save each menu name in an "array" of strings that can be retrieved by looping
		storage.setItem("menu" + Integer.toString(numMenus), menuName);
		
		// save menu name and associated JSON data
		storage.setItem(menuName, json);
	}
	
	/** Returns a string in JSON format representing a menu. */
	public static String getMenu(String menuName) {
		return storage.getItem(menuName);
	}
	
	/** Returns the total number of menus in storage. */
	public static int getNumMenus() {
		return numMenus;
	}
	
	/** Returns an array of strings representing a list of all menu names. */
	public static String[] getMenus() {
		String[] menus = new String[numMenus];
		for (int i=0; i < numMenus; ++i) {
			menus[i] = storage.getItem("menu" + Integer.toString(i+1));
		}
		return menus;
	}
	
	/** Saves a change that was made by the user while interacting with the visual or data editor. */
	public static void saveChange(Menu menu) {
		//storage.setItem("menu", menu.generateJSON());
		storage.setItem(menu.getName(), menu.generateJSON());
	}
	
} // StorageContainer
