package com.gwt.conn.client;

import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

import java.util.Date;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.commons.codec.binary.Hex;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
		long Seed;
		Date currentDate = new Date();
	    Seed = currentDate.getTime();
	    String hash = null;
	    try {
	    	hash = "" + getSHA1(restID + "67d29607cbb149d79d59cb6279643e74" + Seed);  // message + secret_key + timestamp
	    }
	    catch (NoSuchAlgorithmException exc) {
			System.err.println(exc);
	    }
	    catch (UnsupportedEncodingException exc) {
		System.err.println(exc);
	    }
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody securityMessage = new StringBody(restID);
			StringBody timestamp = new StringBody("" + Seed);
			StringBody messageHash = new StringBody(hash);	// use getSHA (message+securityCode+timestamp);
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
		long Seed;
		Date currentDate = new Date();
	    Seed = currentDate.getTime();
	    String doc = StorageContainer.getMenu(menuName);
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody jsonMenu = new StringBody(doc);	
			StringBody securityMessage = new StringBody(doc);
			StringBody timestamp = new StringBody("" + Seed);
			StringBody messageHash = new StringBody(doc + "67d29607cbb149d79d59cb6279643e74" + Seed);
			StringBody restaurantID = new StringBody(restID);
			
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
		String hash = null;
		long Seed;
		Date currentDate = new Date();
	    Seed = currentDate.getTime();
	    String message = menuName + restID;
	    try {
	    	hash = "" + getSHA1(message + "67d29607cbb149d79d59cb6279643e74" + Seed);  // message + secret_key + timestamp
	    }
	    catch (NoSuchAlgorithmException exc) {
			System.err.println(exc);
	    }
	    catch (UnsupportedEncodingException exc) {
		System.err.println(exc);
	    }
	    try {
			HttpPost httppost = new HttpPost(webService);
			StringBody menu_name = new StringBody(menuName);
			StringBody securityMessage = new StringBody(message);
			StringBody timestamp = new StringBody("" + Seed);
			StringBody messageHash = new StringBody(hash);	// use getSHA (message+securityCode+timestamp);
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
	
	public static String deleteMenu (String menuID, String restID, String webService) {
		StringBuffer sb = new StringBuffer();
		HttpClient client = new DefaultHttpClient();
		String message = menuID + restID;
		long Seed;
		Date currentDate = new Date();
	    Seed = currentDate.getTime();
	    String hash = null;
	    try {
	    	hash = "" + getSHA1(message + "67d29607cbb149d79d59cb6279643e74" + Seed);  // message + secret_key + timestamp
	    }
	    catch (NoSuchAlgorithmException exc) {
			System.err.println(exc);
	    }
	    catch (UnsupportedEncodingException exc) {
		System.err.println(exc);
	    }
	    
		try {
			HttpPost httppost = new HttpPost(webService);
			StringBody menu_id = new StringBody(menuID);
			StringBody securityMessage = new StringBody(message);
			StringBody timestamp = new StringBody("" + Seed);
			StringBody messageHash = new StringBody(hash);	// use getSHA (message+securityCode+timestamp);
			StringBody restaurantID = new StringBody(restID);
			
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
	 public static String getMenu(String menuID, String restID, String webservice) {
		 String json = null;
		 long Seed;
		 Date currentDate = new Date();
		 Seed = currentDate.getTime();
		 String hash = null;
		 try {
		    	hash = "" + getSHA1(menuID + restID + "67d29607cbb149d79d59cb6279643e74" + Seed);  // message + secret_key + timestamp
		 }
		 catch (NoSuchAlgorithmException exc) {
			System.err.println(exc);
		 }
		 catch (UnsupportedEncodingException exc) {
			System.err.println(exc);
		 }
		 StringBuffer sb = new StringBuffer();
		 sb.append("menu_id=" + menuID + "&");
		 sb.append("message=" + menuID + restID + "&");
		 sb.append("timestamp=" + Seed + "&");
		 sb.append("message_hash=" + hash + "&");
		 sb.append("restaurant_id=" + restID);
		 
		 try {
		// Send data
			 String urlString = webservice;
			 urlString += "?" + sb.toString();
			 URL url = new URL(urlString);
			 URLConnection conn = url.openConnection();
	
		// Get the response
			 BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 sb = new StringBuffer();
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
	
	public static String extractJSONValue(String keyValuePair) {
		int start = 0; int  finish = 0;
		start  = keyValuePair.indexOf("\"", 1);
		start = keyValuePair.indexOf("\"", start + 1);
		finish = keyValuePair.indexOf("\"", start + 1);
		return keyValuePair.substring(start + 1, finish);
	}
	
	// get restaurant name
	public static Menu deserialize(String jsonMenu) {
		int start = 0; int finish = 0;
		int categoryEndIndex = 0;
		int nextComma = 0;
		
		String menuID, restID, menuName, val;
		
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias("MenuItem", MenuItem.class);
		
		Category cat;
		Menu menu;
		
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
		
		menu = new Menu(menuName);
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
		start = jsonMenu.indexOf("\"", start + "\"menuitems\"  ".length());	// extract index of the start of category name
		finish = jsonMenu.indexOf("\"", start + 1); 	// index of the closing quotation for the first category name
		
		// this loop is responsible for finding new categories within the JSON string
		String menuItemJson = null;
		for (;;) {
			//System.out.println("start: " + start + ", finish: " + finish);
			val = jsonMenu.substring(start + 1, finish);	// extract category name
			cat = new Category(val);
			categoryEndIndex = jsonMenu.indexOf("]", finish);
			// this loop is responsible for finding all menuItems within the category and adding them into the category
			for (;;) {
				start = jsonMenu.indexOf("{", finish + 1);
				finish = jsonMenu.indexOf("}", start);
				System.out.println(jsonMenu.substring(start, finish + 1));
				menuItemJson = jsonMenu.substring(start, finish + 1);
				MenuItem mi = (MenuItem)xstream.fromXML("{\"MenuItem\":" + menuItemJson + "}");
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
		finish = jsonMenu.indexOf("\"", start + "\"restaurant_name\": \" ".length() );
		val = extractJSONValue(jsonMenu.substring(start, finish + 1));
		menu.setRestaurantName(val);
		return menu;
	}
	 
	public static char[] getSHA1(String convertme)  throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    try {
	    	MessageDigest md = MessageDigest.getInstance("SHA-1"); 
		    byte[] sha1hash = new byte[40];
		    md.update(convertme.getBytes("iso-8859-1"));
		    sha1hash = md.digest();
		    char [] encoded = Hex.encodeHex(sha1hash);
		    return encoded;
	    }
	    catch (NoSuchAlgorithmException none) { System.err.println(none); return null; }
	    catch (UnsupportedEncodingException unsupported) { System.err.println(unsupported); return null; }
	}

}