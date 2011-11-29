package com.connoisseur.menuapp.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.storage.client.Storage;



public class Communicate {
	
	private static final Storage storage = StorageContainer.getStorage();
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

	public static boolean authenticate(String rest_id, String secret_key) {
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

	public static String sync(String url, String rest_id) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		Request response = null;
		try {
			
			builder.setHeader("Access-Control-Allow-Origin", "*");
			builder.setHeader("Access-Control-Allow-Headers",
					"X-Requested-With");
			
			String callParameters = "restaurant_id=" + rest_id;
			response = builder.sendRequest(callParameters, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
				}

				public void onResponseReceived(Request request,
						Response response) {
						System.out.println("Response: " + response.getText());
						storage.setItem("cache", response.getText());
				}
			});

		} catch (RequestException e) {
			return "Update Unsuccessful";
		}

		return "Sync succussful";
	}
}
