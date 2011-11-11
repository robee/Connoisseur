package com.gwt.conn.client;

/** The Field Verifier Class **
 * 
 * This class checks various things about what the user inputs in order to
 * ensure consistency in the storage system.
 * 
 */
public class FieldVerifier extends StorageContainer {

	/** Verifies that the given string is a valid license key. */
	public static String isValidLicenseKey(String name) {
		/** Still need to implement validation with backend. */
		String test = isLongEnough(name);
		if (!test.equals("")) return "You submitted an invalid license key.";
		return ""; // empty string means success!
	}
	
	/** Verifies that the given string is a valid menu name. */
	public static String isValidMenuName(String name) {
		String test = isLongEnough(name);
		if (!test.equals("")) return test;
		test = isAlphanumeric(name);
		if (!test.equals("")) return test;
		test = doesntExistYet(name);
		if (!test.equals("")) return test;
		return ""; // empty string means success!
	}
	
	/** Checks to see if the given string has a length of at least 4. */
	private static String isLongEnough(String name) {
		if (name == null) return "Must be at least 4 characters long.";
		else if (name.length() < 4) return "Must be at least 4 characters long.";
		else return "";
	}
	
	/**
	 * Checks to see if the given string consists entirely of letters, numbers or spaces,
	 * with no spaces at the beginning or end.
	 */
	private static String isAlphanumeric(String name) {
		char str[] = name.toCharArray();
		if (str[0] == ' ' || str[name.length()-1] == ' ') return "Cannot start or end with a space.";
		for (int i=0; i < name.length(); ++i) {
			if (str[i] == ' ') continue; // ignore spaces
			// from ASCII chart
			// 0=48, 9=57
			// a=65, z=90
			// A=97, Z=122
			if (str[i] < 48 || str[i] > 122) return "Alphanumeric characters and spaces only.";
			if (str[i] > 57 && str[i] < 65) return "Alphanumeric characters and spaces only.";
			if (str[i] > 90 && str[i] < 97) return "Alphanumeric characters and spaces only.";
		}
		return "";
	}
	
	/** Checks to see that no other menu has this name already. */
	private static String doesntExistYet(String name) {
		String[] menus = getMenus(); // from StorageContainer.java
		int numMenus = getNumMenus(); // from StorageContainer.java
		for (int i=0; i < numMenus; ++i) {
			String cur = menus[i];
			if (name.equals(cur)) return "A menu by this name already exists.";
		}
		return "";
	}
	
}
