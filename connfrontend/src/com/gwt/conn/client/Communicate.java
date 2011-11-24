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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Communicate extends StorageContainer {

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
	 	
	public static void synchronize(String[] menus) {
		// need to maintain a list of menus that were changed during offline mode
		// changes can include:
		// new menu created
		// existing menu edited
		// existing menu deleted
	}
	public static String getSHA(String convertme)  throws NoSuchAlgorithmException, UnsupportedEncodingException {
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