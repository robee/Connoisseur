package com.gwt.conn.server;

/**
 * It was necessary to make an alternate version of this class for the server
 * in order to get around an error caused by inheriting StorageContainer.
 */
public class FieldVerifier {

	/** Verifies that the given string is a valid license key. */
	public static String isValidLicenseKey(String name) {
		/** Still need to implement validation with backend. */
		String test = isLongEnough(name);
		if (!test.equals("")) return "You submitted an invalid license key.";
		return ""; // empty string means success!
	}
	
	/** Checks to see if the given string has a length of at least 4. */
	private static String isLongEnough(String name) {
		if (name == null) return "Must be at least 4 characters long.";
		else if (name.length() < 4) return "Must be at least 4 characters long.";
		else return "";
	}

}