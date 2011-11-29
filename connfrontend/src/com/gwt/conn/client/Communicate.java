package com.gwt.conn.client;

import java.util.Date;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.ui.Label;

/*
 * 


 */

public class Communicate {
	static String url = "http://connoisseurmenu.appspot.com/";

	public static String buildQueryString(String[] keys, String[] values) {
		StringBuffer sb = new StringBuffer();
		if (keys.length != values.length) {
			return "ERROR";
		}
		for (int i = 0, n = keys.length; i < n; ++i) {

			if (i > 0) {
				sb.append("&");
			}

			// encode the characters in the name
			String encodedName = URL.encodeComponent(keys[i]);
			sb.append(encodedName);

			sb.append("=");

			// encode the characters in the value
			String encodedValue = URL.encodeComponent(values[i]);
			sb.append(encodedValue);
		}

		return sb.toString();
	}

	public static boolean authenticate(String restID, String license, Label restErrorLabel, Label licenseErrorLabel) {
		int returnFlag = 0; // so that both tests can be done
		licenseErrorLabel.setText("");
		restErrorLabel.setText("");
		
		// validate license
		String result = FieldVerifier.isValidLicenseKey(license); // from FieldVerifier.java
		if (!result.equals("")) { // error
			licenseErrorLabel.setText(result);
			returnFlag = 1;
		}
		
		// validate restID
		result = FieldVerifier.isValidRestaurantID(restID);
		if (!result.equals("")) { // error
			restErrorLabel.setText(result);
			returnFlag = 1;
		}
		
		// don't do anything until the errors are resolved
		if (returnFlag == 1) return false;
		
		//TODO
		
		return true;
	}

	public static boolean hasInternet() {
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
		builder.setHeader("Access-Control-Allow-Origin", "*");
		try {
			Request response = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
				}

				public void onResponseReceived(Request request,
						Response response) {
				}
			});

		} catch (RequestException e) {
			return false;
		}

		return true;
	}

	public static String updateMenu(String doc) {
		StorageContainer.saveChange(deserialize(doc));
		return "Update Successful";
	}

	public static String GetMenu(String menuName) {
		return StorageContainer.getMenu(menuName);
	}

	public static String sync(String menuName, String restID) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url
				+ "menu/update");

		try {

			builder.setHeader("Access-Control-Allow-Origin", "*");
			builder.setHeader("Access-Control-Allow-Headers",
					"X-Requested-With");
			String doc = StorageContainer.getMenu(menuName);

			String timestamp = new Date().getTime() + "";
			String secret_key = ""; // TODO get secret key from storage
			String message = doc + secret_key + timestamp;
			String hash = "";
			hash = "" + getSHA1(message + secret_key + timestamp);

			String[] keys = { "doc", "message", "timestamp", "message_hash",
					"restaurant_id" };
			String[] values = { doc, doc, timestamp, hash, restID };

			String postData = buildQueryString(keys, values);

			Request response = builder.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable exception) {
				}

				public void onResponseReceived(Request request,
						Response response) {
				}
			});

		} catch (RequestException e) {
			return "Update Unsuccessful";
		}

		return "Update Successful";
	}

	// public static String updateMenu(String menuName, String restID,
	// String webService) { // should return Update successful string
	// // push/update associated json string of given menu in backend
	//
	// RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	//
	// try {
	// builder.setHeader("Content-Type",
	// "application/x-www-form-urlencoded");
	// String doc = StorageContainer.getMenu(menuName);
	// String timestamp = new Date().getTime() + "";
	// String secret_key = ""; // TODO get secret key from storage
	// String message = doc + secret_key + timestamp;
	// String hash = "";
	// try {
	// hash = "" + getSHA1(message + secret_key + timestamp); // message
	// // +
	// // secret_key
	// // +
	// // timestamp
	// } catch (NoSuchAlgorithmException exc) {
	// System.err.println(exc);
	// } catch (UnsupportedEncodingException exc) {
	// System.err.println(exc);
	// }
	//
	// String[] keys = { "doc", "message", "timestamp", "message_hash",
	// "restaurant_id" };
	// String[] values = { doc, doc, timestamp, hash, restID };
	//
	// String postData = buildQueryString(keys, values);
	//
	// Request response = builder.sendRequest(postData,
	// new RequestCallback() {
	// public void onError(Request request, Throwable exception) {
	// }
	//
	// public void onResponseReceived(Request request, Response response) {
	// Menu menu_to_save = deserialize(response.getText());
	// StorageContainer.saveChange(menu_to_save);
	// }
	//
	// });
	//
	// } catch (RequestException e) {
	// Window.alert("Failed to send the request: " + e.getMessage());
	// }
	//
	// return "Update Successful";
	//
	// }
	//
	// public static String getMenu(String menuID, String restID, String
	// webservice) {
	// return "";
	//
	//
	// String json = null;
	// long Seed;
	// Date currentDate = new Date();
	// Seed = currentDate.getTime();
	// String hash = null;
	// try {
	// hash = ""
	// + getSHA1(menuID + restID
	// + "67d29607cbb149d79d59cb6279643e74" + Seed); // message
	// // +
	// // secret_key
	// // +
	// // timestamp
	// } catch (NoSuchAlgorithmException exc) {
	// System.err.println(exc);
	// } catch (UnsupportedEncodingException exc) {
	// System.err.println(exc);
	// }
	// StringBuffer sb = new StringBuffer();
	// sb.append("menu_id=" + menuID + "&");
	// sb.append("message=" + menuID + restID + "&");
	// sb.append("timestamp=" + Seed + "&");
	// sb.append("message_hash=" + hash + "&");
	// sb.append("restaurant_id=" + restID);
	//
	// try {
	// // Send data
	// String urlString = webservice;
	// urlString += "?" + sb.toString();
	// URL url = new URL(urlString);
	// URLConnection conn = url.openConnection();
	//
	// // Get the response
	// BufferedReader rd = new BufferedReader(new InputStreamReader(
	// conn.getInputStream()));
	// sb = new StringBuffer();
	// String line;
	// while ((line = rd.readLine()) != null) {
	// sb.append(line);
	// }
	// rd.close();
	// json = sb.toString();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return json;
	// }

	// public static String createRestaurant (String authcode, String restName,
	// String webService) {
	// StringBuffer sb = new StringBuffer();
	//
	// HttpClient client = new DefaultHttpClient();
	// try {
	// HttpPost httppost = new HttpPost(webService);
	// StringBody auth_code = new StringBody(authcode); // retrieve from
	// storage?
	// StringBody name = new StringBody(restName);
	//
	// // message_hash : its a hash like this hashlib.sha1(
	// message+secret_key+timestamp ).hexdigest()
	// MultipartEntity reqEntity = new MultipartEntity();
	// reqEntity.addPart("auth_code", auth_code);
	// reqEntity.addPart("name", name);
	//
	// httppost.setEntity(reqEntity);
	// HttpResponse response = client.execute(httppost);
	// HttpEntity resEntity = response.getEntity();
	//
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(resEntity.getContent()));
	// String line;
	// while ((line = rd.readLine()) != null) {
	// sb.append(line);
	// }
	// rd.close();
	// }
	// catch (Exception err) {
	// System.err.println(err);
	// }
	// finally {
	// try { client.getConnectionManager().shutdown();
	// return sb.toString();
	// } catch (Exception e) { System.err.println(e); }
	// }
	// return null;
	// }

	// // returns message "Restaurant Successfully Deleted"
	// public static String deleteRestaurant (String restID, String webService)
	// {
	// StringBuffer sb = new StringBuffer();
	// HttpClient client = new DefaultHttpClient();
	// long Seed;
	// Date currentDate = new Date();
	// Seed = currentDate.getTime();
	// String hash = null;
	// try {
	// hash = "" + getSHA1(restID + "67d29607cbb149d79d59cb6279643e74" + Seed);
	// // message + secret_key + timestamp
	// }
	// catch (NoSuchAlgorithmException exc) {
	// System.err.println(exc);
	// }
	// catch (UnsupportedEncodingException exc) {
	// System.err.println(exc);
	// }
	// try {
	// HttpPost httppost = new HttpPost(webService);
	// StringBody securityMessage = new StringBody(restID);
	// StringBody timestamp = new StringBody("" + Seed);
	// StringBody messageHash = new StringBody(hash); // use getSHA
	// (message+securityCode+timestamp);
	// StringBody restaurantID = new StringBody(restID);
	// // message_hash : its a hash like this hashlib.sha1(
	// message+secret_key+timestamp ).hexdigest()
	// MultipartEntity reqEntity = new MultipartEntity();
	// reqEntity.addPart("message", securityMessage);
	// reqEntity.addPart("timestamp", timestamp);
	// reqEntity.addPart("message_hash", messageHash);
	// reqEntity.addPart("restaurant_id", restaurantID);
	//
	// httppost.setEntity(reqEntity);
	// HttpResponse response = client.execute(httppost);
	// HttpEntity resEntity = response.getEntity();
	//
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(resEntity.getContent()));
	// String line;
	// while ((line = rd.readLine()) != null) {
	// sb.append(line);
	// }
	// rd.close();
	// }
	// catch (Exception err) {
	// System.err.println(err);
	// }
	// finally {
	// try { client.getConnectionManager().shutdown();
	// return sb.toString();
	// } catch (Exception e) { System.err.println(e); }
	// }
	// return null;
	// }
	//

	// public static String createMenu(String menuName, String restID, String
	// webService) {
	// StringBuffer sb = new StringBuffer();
	// HttpClient client = new DefaultHttpClient();
	// String hash = null;
	// long Seed;
	// Date currentDate = new Date();
	// Seed = currentDate.getTime();
	// String message = menuName + restID;
	// try {
	// hash = ""
	// + getSHA1(message + "67d29607cbb149d79d59cb6279643e74"
	// + Seed); // message + secret_key + timestamp
	// } catch (NoSuchAlgorithmException exc) {
	// System.err.println(exc);
	// } catch (UnsupportedEncodingException exc) {
	// System.err.println(exc);
	// }
	// try {
	// HttpPost httppost = new HttpPost(webService);
	// StringBody menu_name = new StringBody(menuName);
	// StringBody securityMessage = new StringBody(message);
	// StringBody timestamp = new StringBody("" + Seed);
	// StringBody messageHash = new StringBody(hash); // use getSHA
	// // (message+securityCode+timestamp);
	// StringBody restaurantID = new StringBody(restID);
	// // message_hash : its a hash like this hashlib.sha1(
	// // message+secret_key+timestamp ).hexdigest()
	// MultipartEntity reqEntity = new MultipartEntity();
	// reqEntity.addPart("menu_name", menu_name);
	// reqEntity.addPart("message", securityMessage);
	// reqEntity.addPart("timestamp", timestamp);
	// reqEntity.addPart("message_hash", messageHash);
	// reqEntity.addPart("restaurant_id", restaurantID);
	//
	// httppost.setEntity(reqEntity);
	// HttpResponse response = client.execute(httppost);
	// HttpEntity resEntity = response.getEntity();
	//
	// BufferedReader rd = new BufferedReader(new InputStreamReader(
	// resEntity.getContent()));
	// String line;
	// while ((line = rd.readLine()) != null) {
	// sb.append(line);
	// }
	// rd.close();
	// } catch (Exception err) {
	// System.err.println(err);
	// } finally {
	// try {
	// client.getConnectionManager().shutdown();
	// return sb.toString();
	// } catch (Exception e) {
	// System.err.println(e);
	// }
	// }
	// return null;
	// }

	// public static String deleteMenu (String menuID, String restID, String
	// webService) {
	// StringBuffer sb = new StringBuffer();
	// HttpClient clienct = new DefaultHttpClient();
	// String message = menuID + restID;
	// long Seed;
	// Date currentDate = new Date();
	// Seed = currentDate.getTime();
	// String hash = null;
	// try {
	// hash = "" + getSHA1(message + "67d29607cbb149d79d59cb6279643e74" + Seed);
	// // message + secret_key + timestamp
	// }
	// catch (NoSuchAlgorithmException exc) {
	// System.err.println(exc);
	// }
	// catch (UnsupportedEncodingException exc) {
	// System.err.println(exc);
	// }
	//
	// try {
	// HttpPost httppost = new HttpPost(webService);
	// StringBody menu_id = new StringBody(menuID);
	// StringBody securityMessage = new StringBody(message);
	// StringBody timestamp = new StringBody("" + Seed);
	// StringBody messageHash = new StringBody(hash); // use getSHA
	// (message+securityCode+timestamp);
	// StringBody restaurantID = new StringBody(restID);
	//
	// MultipartEntity reqEntity = new MultipartEntity();
	// reqEntity.addPart("menu_id", menu_id);
	// reqEntity.addPart("message", securityMessage);
	// reqEntity.addPart("timestamp", timestamp);
	// reqEntity.addPart("message_hash", messageHash);
	// reqEntity.addPart("restaurant_id", restaurantID);
	//
	// httppost.setEntity(reqEntity);
	// HttpResponse response = client.execute(httppost);
	// HttpEntity resEntity = response.getEntity();
	//
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(resEntity.getContent()));
	// String line;
	// while ((line = rd.readLine()) != null) {
	// sb.append(line);
	// }
	// rd.close();
	// }
	// catch (Exception err) {
	// System.err.println(err);
	// }
	// finally {
	// try { client.getConnectionManager().shutdown();
	// return sb.toString();
	// } catch (Exception e) { System.err.println(e); }
	// }
	// return null;
	// }

	/*
	 * @param endpoint - The URL of the server.
	 * http://connoisseurmenu.appspot.com/menu
	 * 
	 * @param requestParameters - all the request parameters (Example:
	 * "param1=val1&param2=val2"). Note: This method will add the question mark
	 * (?) to the request - DO NOT add it yourself menu_id : the id for the menu
	 * you want to get message : used for security, in this case it will be the
	 * menu_id and the restaurant_id concated timestamp : used for security as a
	 * way to randomize the message_hash message_hash : its a hash like this
	 * hashlib.sha1( message+secret_key+timestamp ).hexdigest() restaurant_id :
	 * need to know who is trying to do this
	 * 
	 * @return - JSON string containing items in the menu HTTP GET
	 */

	// need a method that would return the value of a JSON string, i.e.
	// "menu": "xxx", the method would return xxx;

	public static String extractJSONValue(String keyValuePair) {
		int start = 0;
		int finish = 0;
		start = keyValuePair.indexOf("\"", 1);
		start = keyValuePair.indexOf("\"", start + 1);
		finish = keyValuePair.indexOf("\"", start + 1);
		return keyValuePair.substring(start + 1, finish);
	}

	private static MenuItem parseJSONMenuItem(String menuItem) {
		String category = null;
		String menuitem_id = null;
		String description = null;
		String menu = null;
		String image = null;
		String price = null;
		String name = null;
		int start = 0;
		int finish = 0;

		System.out.println(menuItem);

		start = menuItem.indexOf("\"category\"");
		finish = menuItem.indexOf("\"", start + "\"category\": \" ".length());
		category = extractJSONValue(menuItem.substring(start, finish + 1));

		start = menuItem.indexOf("\"menuitem_id\"");
		finish = menuItem
				.indexOf("\"", start + "\"menuitem_id\": \" ".length());
		menuitem_id = extractJSONValue(menuItem.substring(start, finish + 1));

		start = menuItem.indexOf("\"description\"");
		finish = menuItem
				.indexOf("\"", start + "\"description\": \" ".length());
		description = extractJSONValue(menuItem.substring(start, finish + 1));

		start = menuItem.indexOf("\"menu\"");
		finish = menuItem.indexOf("\"", start + "\"menu\": \" ".length());
		menu = extractJSONValue(menuItem.substring(start, finish + 1));

		start = menuItem.indexOf("\"image\"");
		finish = menuItem.indexOf("\"", start + "\"image\": \" ".length());
		image = extractJSONValue(menuItem.substring(start, finish + 1));

		start = menuItem.indexOf("\"price\"");
		finish = menuItem.indexOf("\"", start + "\"price\": \" ".length());
		price = extractJSONValue(menuItem.substring(start, finish + 1));

		start = menuItem.indexOf("\"name\"");
		finish = menuItem.indexOf("\"", start + "\"name\": \" ".length());
		name = extractJSONValue(menuItem.substring(start, finish + 1));

		return (new MenuItem(category, menuitem_id, description, menu, image,
				price, name));
	}

	// get restaurant name
	public static Menu deserialize(String jsonMenu) {
		int start = 0;
		int finish = 0;
		int categoryEndIndex = 0;
		int nextComma = 0;

		String menuID, restID, menuName, val;

		Category cat;
		Menu menu;

		if (jsonMenu.charAt(0) != '{') {
			return null;
		}
		// parse menu

		// menu id
		start = jsonMenu.indexOf("\"menu_id\"");
		finish = jsonMenu.indexOf("\"", start + "\"menu_id\": \" ".length());
		menuID = extractJSONValue(jsonMenu.substring(start, finish + 1));
		// rest id
		start = jsonMenu.indexOf("\"restaurant_id\"");
		finish = jsonMenu.indexOf("\"",
				start + "\"restaurant_id\": \" ".length());
		restID = extractJSONValue(jsonMenu.substring(start, finish + 1));
		// menu name
		start = jsonMenu.indexOf("\"menu_name\"");
		finish = jsonMenu.indexOf("\"", start + "\"menu_name\": \" ".length());
		menuName = extractJSONValue(jsonMenu.substring(start, finish + 1));

		menu = new Menu(menuName);
		menu.setID(menuID);
		menu.setRestaurantID(restID);
		// logo url
		start = jsonMenu.indexOf("\"logo_url\"");
		finish = jsonMenu.indexOf("\"", start + "\"logo_url\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setLogo(val);
		// color
		start = jsonMenu.indexOf("\"color\"");
		finish = jsonMenu.indexOf("\"", start + "\"color\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setColor(val);
		// menu
		start = jsonMenu.indexOf("\"menu\"");
		finish = jsonMenu.indexOf("\"", start + "\"menu\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setMenu(val);
		// profile_id
		start = jsonMenu.indexOf("\"profile_id\"");
		finish = jsonMenu.indexOf("\"", start + "\"profile_id\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setProfile(val);
		// template
		start = jsonMenu.indexOf("\"template\"");
		finish = jsonMenu.indexOf("\"", start + "\"template\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setTemplate(val);
		// font
		start = jsonMenu.indexOf("\"font\"");
		finish = jsonMenu.indexOf("\"", start + "\"font\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setFont(val);

		start = jsonMenu.indexOf("\"menuitems\""); // find where menuitems
		// starts
		start = jsonMenu.indexOf("\"", start + "\"menuitems\" ".length()); // extract
		// index
		// of
		// the
		// start
		// of
		// category
		// name
		finish = jsonMenu.indexOf("\"", start + 1); // index of the closing
		// quotation for the first
		// category name

		// this loop is responsible for finding new categories within the JSON
		// string
		String menuItemJson = null;
		MenuItem mi = null;
		for (;;) {

			val = jsonMenu.substring(start + 1, finish); // extract category
															// name

			// System.out.println("start: " + start + ", finish: " + finish);

			cat = new Category(val);
			categoryEndIndex = jsonMenu.indexOf("]", finish);
			// this loop is responsible for finding all menuItems within the
			// category and adding them into the category
			for (;;) {
				start = jsonMenu.indexOf("{", finish + 1);
				finish = jsonMenu.indexOf("}", start);
				menuItemJson = jsonMenu.substring(start, finish + 1);

				// JSONValue jsonValue = JSONParser.parseStrict(menuItemJson);
				// JSONArray iCont = jsonValue.isArray();

				mi = parseJSONMenuItem(menuItemJson);
				// mi = new MenuItem(iCont.get(0).toString(),
				// iCont.get(1).toString(), iCont.get(2).toString(),
				// iCont.get(3).toString(), iCont.get(4).toString(),
				// iCont.get(5).toString(), iCont.get(6).toString());
				// mi = (MenuItem)xstream.fromXML("{\"MenuItem\":" +
				// menuItemJson + "}");

				cat.addMenuItem(mi);
				nextComma = jsonMenu.indexOf(",", finish);
				if (nextComma == -1 || nextComma > categoryEndIndex) {
					break;
				}
			}
			menu.addCategory(cat);
			nextComma = jsonMenu.indexOf(",", categoryEndIndex);
			if (nextComma == -1) {
				break;
			}
			start = jsonMenu.indexOf("\"", nextComma);
			finish = jsonMenu.indexOf("\"", start + 1);
		}
		// font
		start = jsonMenu.indexOf("\"restaurant_name\"");
		finish = jsonMenu.indexOf("\"",
				start + "\"restaurant_name\": \" ".length());
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setRestaurantName(val);
		return menu;
	}

	public static String getSHA1(String convertme) {
		Sha1 hashFunction = new Sha1();
		String hash = hashFunction.hex_sha1(convertme);
		return hash;
	}
	// public static char[] getSHA1(String convertme)
	// throws NoSuchAlgorithmException, UnsupportedEncodingException {
	// try {
	// MessageDigest md = MessageDigest.getInstance("SHA-1");
	// byte[] sha1hash = new byte[40];
	// md.update(convertme.getBytes("iso-8859-1"));
	// sha1hash = md.digest();
	// char[] encoded = Hex.encodeHex(sha1hash);
	// return encoded;
	// } catch (NoSuchAlgorithmException none) {
	// System.err.println(none);
	// return null;
	// } catch (UnsupportedEncodingException unsupported) {
	// System.err.println(unsupported);
	// return null;
	// }
	// }

}