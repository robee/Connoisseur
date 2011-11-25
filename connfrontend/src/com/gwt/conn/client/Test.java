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
		String encoded = null;
		try {
			encoded = Communicate.getSHA1("ROSS IS BALLIN");
		} catch (NoSuchAlgorithmException exc) {
				System.err.println(exc);
		  }
		catch (UnsupportedEncodingException exc) {
			System.err.println(exc);
		}
		System.out.println(encoded);
		//String createRestResponse = Communicate.createRestaurant(authcode, restName, webService)
		
		// test serializers (see if correct JSON is produced)
		// test HTTP calls
		// test deserializer
		//
		// 
	}

}
