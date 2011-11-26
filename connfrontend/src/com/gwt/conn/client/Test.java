package com.gwt.conn.client;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*
		Restaurant BurgerKing = new Restaurant("Crap Food");
		BurgerKing.addMenu("Lunch");
		BurgerKing.setID("schmilblik");
		BurgerKing.getMenu("Lunch").setID("x005");
		//BurgerKing.addMenu("Dinners");
		BurgerKing.createCategory("Lunch", "Drinks");
		BurgerKing.createCategory("Lunch", "Strippers");
		//BurgerKing.createCategory("Dinners", "Entrees");
		BurgerKing.addMenuItem("Lunch", "Drinks", "Bloody Mary");
		BurgerKing.addMenuItem("Lunch", "Drinks", "Blue Lagoon");
		BurgerKing.addMenuItem("Lunch", "Strippers", "Candy");
		
		// BurgerKing.addMenuItem("Dinners", "Entrees", "Kickass Deep Fried Shrimp");
		
		String serializedMenu = BurgerKing.generateJSON("Lunch");
		System.out.println(serializedMenu);
		*/
		/*
		char [] encoded = null;
		try {
			encoded = Communicate.getSHA1("ROSS IS BALLIN");
		} catch (NoSuchAlgorithmException exc) {
				System.err.println(exc);
		  }
		catch (UnsupportedEncodingException exc) {
			System.err.println(exc);
		}
		String news = "" + encoded;
		System.out.print(encoded);
		*/
		//String createRestResponse = Communicate.createRestaurant(authcode, restName, webService)
		
		// test serializers (see if correct JSON is produced)
		// test HTTP calls
		// test deserializer
		//
		//
		//String newRest = Communicate.createRestaurant("4a0e36be6e7d439f83ef8aa8d3f4a40f", "OMGWTF Restaurant", "http://connoisseurmenu.appspot.com/restaurant/create");
		//String newMenu = Communicate.createMenu("North Korean cousine", "b686d49d8b67424aa1e347613cbb1975", "http://connoisseurmenu.appspot.com/menu/create");
		//System.out.println(newRest);
		/*
		String json = "{\"menu_id\": \"236e8690d55248ff\", \"restaurant_id\": \"b686d49d8b67424aa1e347613cbb1975\", " +
		"\"menu_name\": \"menu\", \"ui_profile\": {\"logo_url\": \"http://www.virginialogos.com/Portals/" +
		"57ad7180-c5e7-49f5-b282-c6475cdb7ee7/Food.jpg\", \"color\": \"black\", \"menu\": null, \"profile_id\": " +
		"\"259fdb7df24a4f6d\", \"template\": \"classy\", \"font\": \"Helvetica\"}, \"restaurant_name\": " +
		"\"restaurantTest\", \"menuitems\": {\"Drink\": [{\"category\": \"Drink\", \"menuitem_id\": " +
		"\"24c0206c962a4903\", \"description\": \"\", \"menu\": null, \"image\": \"This is a sample menu Item\", " +
		"\"price\": 11.0, \"name\": \"Starter Item 2\"}], \"Appy\": [{\"category\": \"Appy\", \"menuitem_id\": " +
		"\"6c1bd016d5b54dc9\", \"description\": \"\", \"menu\": null, \"image\": \"This is a sample menu Item\", " +
		"\"price\": 10.0, \"name\": \"Starter Item 1\"}]}}";
		//System.out.println(json);
		Menu m = Communicate.deserialize(json);
		System.out.println("\n\n");
		System.out.println(m.generateJSON());
		*/
		String getM = Communicate.getMenu("236e8690d55248ff", "b686d49d8b67424aa1e347613cbb1975","http://connoisseurmenu.appspot.com/menu");
		System.out.println(getM);
	}
}
