package com.connoisseur.menuapp.client;
/* DISABLED FOR NOW
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
*/
public class Communicate {
	
	 /**
	  * @param endpoint - The URL of the server. http://connoisseurmenu.appspot.com/menu
	  * @param requestParameters - all the request parameters (Example: "param1=val1&param2=val2"). Note: This method will add the question mark (?) to the request - DO NOT add it yourself
	  * menu_id         : the id for the menu you want to get
      * message         : used for security, in this case it will be the menu_id and the restaurant_id concated
      * timestamp       : used for security as a way to randomize the message_hash
      * message_hash    : its a hash like this hashlib.sha1( message+secret_key+timestamp ).hexdigest()
      * restaurant_id   : need to know who is trying to do this
	  * @return - JSON string containing items in the menu
	  * HTTP GET
	  */
/*	 public static String getMenu(String menuID, String restID, String webservice) {
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
*/
} // Communicate
