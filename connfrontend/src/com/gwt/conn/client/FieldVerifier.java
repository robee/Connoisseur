package com.gwt.conn.client;

/** The Field Verifier Class **
 * 
 * This class checks various things about what the user inputs in general in order to
 * ensure consistency and cleanliness in the storage system.
 * 
 */
public class FieldVerifier {

	/** Verifies that the given string is a valid license key.
	 *  Returns the default json object for the app if valid.
	 *  Returns an error otherwise.
	 */
	public static String isValidLicenseKey(String license, String restID, String menuName) {
		String test = isLongEnoughAndNotNull(restID);
		if (!test.equals("")) return "You submitted an invalid license key.";
		return test;
		//return Communicate.createMenu(menuName, restID, "http://connoisseurmenu.appspot.com/menu/create");
	}
	
	/** Verifies that the given string is a valid menu name. */
	public static String isValidMenuName(String name) {
		String test = isLongEnoughAndNotNull(name);
		if (!test.equals("")) return test;
		test = isAlphanumericPlusSpaces(name);
		if (!test.equals("")) return test;
		test = menuDoesntExistYet(name);
		if (!test.equals("")) return test;
		return ""; // empty string means success!
	}
	
	/** Verifies that the given string is a valid menu or category name. */
	public static String isValidName(String name, String[] names) {
		String test = isLongEnoughAndNotNull(name);
		if (!test.equals("")) return test;
		test = isLessThanOrEqualTo16Chars(name);
		if (!test.equals("")) return test;
		test = isAlphanumericPlusSpaces(name);
		if (!test.equals("")) return test;
		test = nameDoesntExistYet(name, names);
		if (!test.equals("")) return test;
		return ""; // empty string means success!
	}
	
	/** Verifies that the given string is a valid menu item name. */
	public static String isValidItemName(String name, String[] names) {
		String test = isLongEnoughAndNotNull(name);
		if (!test.equals("")) return test;
		test = isLessThanOrEqualTo26Chars(name);
		if (!test.equals("")) return test;
		test = isAlphanumericPlusSpaces(name);
		if (!test.equals("")) return test;
		test = nameDoesntExistYet(name, names);
		if (!test.equals("")) return test;
		return ""; // empty string means success!
	}
	
	/** Checks to see if the given string has a length of at least 4. */
	private static String isLongEnoughAndNotNull(String name) {
		if (name == null) return "Must be at least 4 characters long.";
		if (name.length() < 4) return "Must be at least 4 characters long.";
		return "";
	}
	
	/** Checks to see if the given string has a length of at most 16. */
	private static String isLessThanOrEqualTo16Chars(String name) {
		if (name.length() > 16) return "Must be at most 16 characters long.";
		return "";
	}
	
	/** Checks to see if the given string has a length of at most 26. */
	private static String isLessThanOrEqualTo26Chars(String name) {
		if (name.length() > 26) return "Must be at most 26 characters long.";
		return "";
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
			if (str[i] < 48 || str[i] > 122) return "Alphanumeric characters or spaces only.";
			if (str[i] > 57 && str[i] < 65) return "Alphanumeric characters or spaces only.";
			if (str[i] > 90 && str[i] < 97) return "Alphanumeric characters or spaces only.";
		}
		return "";
	}
	
	/** Checks to see that no other menu in storage has this name already. */
	private static String menuDoesntExistYet(String name) {
		String[] menus = StorageContainer.getMenus(); // from StorageContainer.java
		int numMenus = StorageContainer.getNumMenus(); // from StorageContainer.java
		for (int i=0; i < numMenus; ++i) {
			String cur = menus[i];
			if (name.equals(cur)) return "A menu by this name already exists.";
		}
		return "";
	}
	
	/** Checks to see that no other menu in storage has this name already. */
	private static String nameDoesntExistYet(String name, String[] names) {
		for (int i=0; i < names.length; ++i) {
			if (name.equalsIgnoreCase(names[i])) return "An item by this name already exists.";
		}
		return "";
	}
	
	/** A valid URL starts with http:// and is not null. */
	public static String isValidURL(String url) {
		String test = isLongEnoughAndNotNull(url);
		if (!test.equals("")) return test;
		if (!url.startsWith("http://")) return "URLs must start with \"http://\".";
		return "";
	}
	
	/** A valid price is a real number. */
	public static String isValidPrice(String price) {
		if (price == null) return "";
		char str[] = price.toCharArray();
		if (str[0] == '.' || str[price.length()-1] == '.') return "Cannot start or end with a decimal point.";
		int numDecimals = 0;
		for (int i=0; i < price.length(); ++i) {
			if (str[i] == '.') { // count decimals
				numDecimals++;
				continue;
			}
			// from ASCII chart
			// 0=48, 9=57
			if (str[i] < 48 || str[i] > 57) return "Numbers or decimal points only.";
		}
		if (numDecimals > 1) return "There cannot be more than one decimal point.";
		return "";
	}
	
	public static String isValidDescription(String desc) {
		char str[] = desc.toCharArray();
		for (int i=0; i < desc.length(); ++i) {
			if (str[i] == '&') return "Character '&' is not allowed.";
			if (str[i] == ';') return "Character ';' is not allowed.";
			if (str[i] == '#') return "Character '#' is not allowed.";
		}
		return "";
	}
	
} // FieldVerifier
