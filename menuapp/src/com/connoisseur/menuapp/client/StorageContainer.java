package com.connoisseur.menuapp.client;

import com.google.gwt.storage.client.Storage;

/** The Storage Container Class **
 * 
 * This class contains useful functions for interacting with the local storage device.
 * For the menu app, the only necessary function is the one for getting the storage device.
 * 
 */

public class StorageContainer {

	/** This is essentially an HTML5 database for storing strings. */
	private static final Storage storage = Storage.getLocalStorageIfSupported();
	
	/** Enforces singleton instance of the actual storage object. */
	public static Storage getStorage() {
		return storage;
	}
	
} // StorageContainer
