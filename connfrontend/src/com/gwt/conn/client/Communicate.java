package com.gwt.conn.client;

public class Communicate extends StorageContainer {

	public static void sendMenuToBackend(String menuName) {
		// push/update associated json string of given menu in backend
		String json = getJSON(menuName);
	}
	
	public static String getMenuFromBackend(String menuName) {
		// send menuName to backend
		// expect to get back JSON string
		String json = menuName + "!";
		return json;
	}

	public static void synchronize(String[] menus) {
		// need to maintain a list of menus that were changed during offline mode
		// changes can include:
		// new menu created
		// existing menu edited
		// existing menu deleted
	}

}
