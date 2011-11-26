package com.connoisseur.menuapp.client;

/** The Field Verifier Class **
 * 
 * This class checks what the user inputs during authentication in order to
 * ensure an authentic user is using Connoisseur.
 * 
 */

public class FieldVerifier {

	/** Verifies that the given string is a valid license key.
	 *  Returns the empty string "" on success.
	 */
	public static String isValidLicenseKey(String license) {
		String test = isLongEnoughAndNotNull(license);
		if (!test.equals("")) return test;
		test = isAlphanumericPlusSpaces(license);
		if (!test.equals("")) return test;
		return ""; // success!
	}
	
	/** Verifies that the given string is a valid restaurant ID.
	 *  Returns the empty string "" on success.
	 */
	public static String isValidRestaurantID(String restID) {
		String test = isLongEnoughAndNotNull(restID);
		if (!test.equals("")) return test;
		test = isAlphanumericPlusSpaces(restID);
		if (!test.equals("")) return test;
		return "";
	}
	
	/** Checks to see if the given string is not null and has length of at least 4. */
	private static String isLongEnoughAndNotNull(String name) {
		if (name == null) return "Must be at least 4 characters long.";
		else if (name.length() < 4) return "Must be at least 4 characters long.";
		else return "";
	}
	
	/**
	 * Checks to see if the given string consists entirely of letters, numbers or spaces,
	 * with no spaces at the beginning or end.
	 */
	private static String isAlphanumericPlusSpaces(String name) {
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
	
} // FieldVerifier
