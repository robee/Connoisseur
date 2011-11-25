package com.gwt.conn.client;

import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Communicate {

	public static String createRestaurant (String authcode, String restName, String webService) {
		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody auth_code = new StringBody(authcode);	// retrieve from storage?
			StringBody name = new StringBody(restName);
			
			// message_hash     : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("auth_code", auth_code);
			reqEntity.addPart("name", name);
			
			httppost.setEntity(reqEntity);
			HttpResponse response = client.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		}
			catch (Exception err) {
        	System.err.println(err);
        }
			finally {
	           try { client.getConnectionManager().shutdown(); 
	           		 return sb.toString();
	           } catch (Exception e) { System.err.println(e); }
			}
			return null;
	}
	// returns message "Restaurant Successfully Deleted"
	public static String deleteRestaurant (String restID, String webService) {
		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody securityMessage = null;
			StringBody timestamp = null;
			StringBody messageHash = null;	// use getSHA (message+securityCode+timestamp);
			StringBody restaurantID = new StringBody(restID);
			// message_hash     : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("message", securityMessage);
			reqEntity.addPart("timestamp", timestamp);
			reqEntity.addPart("message_hash", messageHash);
			reqEntity.addPart("restaurant_id", restaurantID);
			
			httppost.setEntity(reqEntity);
			HttpResponse response = client.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		}
			catch (Exception err) {
        	System.err.println(err);
        }
			finally {
	           try { client.getConnectionManager().shutdown(); 
	           		 return sb.toString();
	           } catch (Exception e) { System.err.println(e); }
			}
			return null;
	}
		
	public static String updateMenu(String menuName, String restID, String webService) {   // should return Update successful string
		// push/update associated json string of given menu in backend
		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody jsonMenu = null;	// retrieve from storage?
			StringBody securityMessage = null;
			StringBody timestamp = null;
			StringBody messageHash = null;	// use getSHA (message+securityCode+timestamp);
			StringBody restaurantID = new StringBody(restID);
			// message_hash     : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("doc", jsonMenu);
			reqEntity.addPart("message", securityMessage);
			reqEntity.addPart("timestamp", timestamp);
			reqEntity.addPart("message_hash", messageHash);
			reqEntity.addPart("restaurant_id", restaurantID);
			
			httppost.setEntity(reqEntity);
			HttpResponse response = client.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		}
			catch (Exception err) {
        	System.err.println(err);
        }
			finally {
	           try { client.getConnectionManager().shutdown(); 
	           		 return sb.toString();
	           } catch (Exception e) { System.err.println(e); }
			}
			return null;
	}
	
	public static String createMenu (String menuName, String restID, String webService) {
		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody menu_name = new StringBody(menuName);
			StringBody securityMessage = null;
			StringBody timestamp = null;
			StringBody messageHash = null;	// use getSHA (message+securityCode+timestamp);
			StringBody restaurantID = new StringBody(restID);
			// message_hash     : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("menu_name", menu_name);
			reqEntity.addPart("message", securityMessage);
			reqEntity.addPart("timestamp", timestamp);
			reqEntity.addPart("message_hash", messageHash);
			reqEntity.addPart("restaurant_id", restaurantID);
			
			httppost.setEntity(reqEntity);
			HttpResponse response = client.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		}
			catch (Exception err) {
        	System.err.println(err);
        }
			finally {
	           try { client.getConnectionManager().shutdown(); 
	           		 return sb.toString();
	           } catch (Exception e) { System.err.println(e); }
			}
			return null;
	}
	
	// all of the operations except getMenu are done through HTTP POST
	// There will be only 1 method dealing with all POST requests, the calls will be distinguished by the  
	// web service parameter
	public static String deleteMenu (String menuID, String restID, String webService) {
		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody menu_id = new StringBody(menuID);
			StringBody securityMessage = null;
			StringBody timestamp = null;
			StringBody messageHash = null;	// use getSHA (message+securityCode+timestamp);
			StringBody restaurantID = new StringBody(restID);
			// message_hash     : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("menu_id", menu_id);
			reqEntity.addPart("message", securityMessage);
			reqEntity.addPart("timestamp", timestamp);
			reqEntity.addPart("message_hash", messageHash);
			reqEntity.addPart("restaurant_id", restaurantID);
			
			httppost.setEntity(reqEntity);
			HttpResponse response = client.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		}
			catch (Exception err) {
        	System.err.println(err);
        }
			finally {
	           try { client.getConnectionManager().shutdown(); 
	           		 return sb.toString();
	           } catch (Exception e) { System.err.println(e); }
			}
			return null;
	}
	
	 /* @param endpoint - The URL of the server. http://connoisseurmenu.appspot.com/menu
	 *  @param requestParameters - all the request parameters (Example: "param1=val1&param2=val2"). Note: This method will add the question mark (?) to the request - DO NOT add it yourself
	 *  menu_id         : the id for the menu you want to get
        message         : used for security, in this case it will be the menu_id and the restaurant_id concated
        timestamp       : used for security as a way to randomize the message_hash
        message_hash    : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
        restaurant_id   : need to know who is trying to do this
	 *  @return - JSON string containing items in the menu
	 *  HTTP GET
	 */
	 public static String getMenu(String webservice, String requestParameters) {
		 String json = null;
		 try {
		// Send data
			 String urlString = webservice;
			 if (requestParameters != null && requestParameters.length() > 0) {
				 urlString += "?" + requestParameters;
			 }
			 URL url = new URL(urlString);
			 URLConnection conn = url.openConnection();
	
		// Get the response
			 BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 StringBuffer sb = new StringBuffer();
			 String line;
			 while ((line = rd.readLine()) != null) {
				 sb.append(line);
			 }
			 rd.close();
			 json = sb.toString();
		 } catch (Exception e){
			 e.printStackTrace();
		 }
	 return json;
	 }
	
	 // need a method that would return the value of a JSON string, i.e. 
	 // "menu": "xxx", the method would return xxx; 
	
	public String extractJSONValue(String keyValuePair) {
		int start = 0; int  finish = 0;
		start  = keyValuePair.indexOf("\"", 1);
		start = keyValuePair.indexOf("\"", start + 1);
		finish = keyValuePair.indexOf("\"", start + 1);
		return keyValuePair.substring(start + 1, finish);
	}
	 
	public Menu deserialize(String jsonMenu) {
		int start = 0; int finish = 0;
		int categoryEndIndex = 0;
		int nextComma = 0;
		String menuID, restID, menuName, val;
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias(null, MenuItem.class);
		
		if (jsonMenu.charAt(0) != '{') {
			return null;
		}
		// parse menu
		
		//menu id
		start = jsonMenu.indexOf("\"menu_id\"");
		finish = jsonMenu.indexOf("\"", start + "\"menu_id\": \" ".length() );
		menuID = extractJSONValue(jsonMenu.substring(start, finish + 1));
		// rest id
		start = jsonMenu.indexOf("\"restaurant_id\"");
		finish = jsonMenu.indexOf("\"", start + "\"restaurant_id\": \" ".length() );
		restID = extractJSONValue(jsonMenu.substring(start, finish + 1));
		//menu name
		start = jsonMenu.indexOf("\"menu_name\"");
		finish = jsonMenu.indexOf("\"", start + "\"menu_name\": \" ".length() );
		menuName = extractJSONValue(jsonMenu.substring(start, finish + 1));
		
		Menu menu = new Menu(menuName);
		menu.setID(menuID);
		menu.setRestaurantID(restID);
		// logo url
		start = jsonMenu.indexOf("\"logo_url\"");
		finish = jsonMenu.indexOf("\"", start + "\"logo_url\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setLogoURL(val);
		// color
		start = jsonMenu.indexOf("\"color\"");
		finish = jsonMenu.indexOf("\"", start + "\"color\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setColor(val);
		// menu
		start = jsonMenu.indexOf("\"menu\"");
		finish = jsonMenu.indexOf("\"", start + "\"menu\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setMenu(val);
		// profile_id
		start = jsonMenu.indexOf("\"profile_id\"");
		finish = jsonMenu.indexOf("\"", start + "\"profile_id\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setProfile(val);
		// template
		start = jsonMenu.indexOf("\"template\"");
		finish = jsonMenu.indexOf("\"", start + "\"template\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setTemplate(val);
		// font 
		start = jsonMenu.indexOf("\"font\"");
		finish = jsonMenu.indexOf("\"", start + "\"font\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setFont(val);
		
		start = jsonMenu.indexOf("\"menuitems\"");	// find where menuitems starts
		start = jsonMenu.indexOf("\"", start + "\"menuitems\" ".length());	// extract index of the start of category name
		finish = jsonMenu.indexOf("\"", start); 	// index of the closing quotation for the first category name
		
		// this loop is responsible for finding new categories within the JSON string
		for (;;) {
			val = jsonMenu.substring(start + 1, finish);	// extract category name
			Category cat = new Category(val);
			categoryEndIndex = jsonMenu.indexOf("]", finish);
			// this loop is responsible for finding all menuItems within the category and adding them into the category
			for (;;) {
				start = jsonMenu.indexOf("{", finish + 1);
				finish = jsonMenu.indexOf("}", start);
				MenuItem mi = (MenuItem)xstream.fromXML(jsonMenu.substring(start, finish + 1));
				cat.addMenuItem(mi);	
				nextComma = jsonMenu.indexOf(",", finish);
				if (nextComma == -1 || nextComma > categoryEndIndex) {
					break;
				}
			}
			menu.addCategory(val);
			nextComma = jsonMenu.indexOf(",", categoryEndIndex);
			if (nextComma == -1) {
				break; 
			}
			start = jsonMenu.indexOf("\"", nextComma);
			finish = jsonMenu.indexOf("\"", start + 1);
		}
		return menu;
	}
	 
	public static void synchronize(ArrayList<Menu> menus) {
		// need to maintain a list of menus that were changed during offline mode
		// changes can include:
		// new menu created
		// existing menu edited
		// existing menu deleted
	}
	public static String getSHA1(String convertme)  throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    try {
	    	MessageDigest md = MessageDigest.getInstance("sha-1"); 
		    byte[] sha1hash = new byte[40];
		    md.update(convertme.getBytes("iso-8859-1"));
		    sha1hash = md.digest();
		    return sha1hash.toString();
	    }
	    catch (NoSuchAlgorithmException none) { System.err.println(none); return null; }
	    catch (UnsupportedEncodingException unsupported) { System.err.println(unsupported); return null; }
	}
	
}