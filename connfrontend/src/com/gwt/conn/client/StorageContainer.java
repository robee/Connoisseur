package com.gwt.conn.client;

import com.google.gwt.storage.client.Storage;

/** The Storage Container Class **
 * 
 * This class contains the Storage API, which functions like a database.
 * Most classes in this package inherit this class in order to communicate
 * with storage at any time.
 * 
 */

public class StorageContainer {

	/** This is essentially an HTML5 database for storing strings. */
	protected final static Storage storage = Storage.getLocalStorageIfSupported();
	
	/** Number of menus currently in local storage. */
	private static int numMenus;
	
	/** Removes non-constant data from storage in case of weird overlap issues. */
	protected void startup() {
		numMenus = Integer.valueOf(storage.getItem("numMenus"));
	}
	
	/** Called whenever a new menu needs to be added to storage. */
	protected void addMenu(String menuName) {
		numMenus++;
		storage.setItem("numMenus", Integer.toString(numMenus));
		storage.setItem("menu" + Integer.toString(numMenus), menuName);
		//storage.setItem(menuName, menuName + "!"); // remember menu name and associated JSON data
		storage.setItem(menuName, Communicate.getMenu(menuName, ""));
	}
	
	/** Returns a string in JSON format representing a menu. */
	protected static String getMenu(String menuName) {
		return storage.getItem(menuName);
	}
	
	/** Returns the total number of menus in storage. */
	protected static int getNumMenus() {
		//return Integer.valueOf(storage.getItem("numMenus"));
		return numMenus;
	}
	
	/** Returns an array of strings representing a list of all menu names. */
	protected static String[] getMenus() {
		String[] menus = new String[numMenus];
		for (int i=0; i < numMenus; ++i) {
			menus[i] = storage.getItem("menu" + Integer.toString(i+1));
		}
		return menus;
	}
	
	/** 
	 * Checks to see if anything needs to be pushed to the server
	 * due to potential changes made during offline mode.
	 */
	protected void synchronizeWithBackend() {
		// get list of menus that need to be updated on backend
		//Communicate.synchronize(listOfMenuNamesNeedingUpdates);
	} // end synchronizeWithBackend
	
} // StorageContainer
