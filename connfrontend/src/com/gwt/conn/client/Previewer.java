package com.gwt.conn.client;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.http.client.URL;

/** The Previewer Class **
 * 
 * This class contains the code for getting an iframe widget that fills the screen.
 * The HTML page rendered depends upon the JSON object saved in storage as "menu".
 * 
 */

public class Previewer {
	
	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();
	
	/** Called when the previewer needs to be loaded. */
	public static Frame getPreviewer(final Menu menu) {
		// this widget allows web pages to be seen in an HTML iframe
		final Frame frame = new Frame();
		frame.getElement().setAttribute("style", "width:100%; height:100%; border:0");
		frame.setUrl("http://connoisseurmenu.appspot.com/preview?restaurant_id=" + storage.getItem("license")
			+ "&doc=" + URL.encode(storage.getItem(menu.getName()))); // encoded for HTTP
		return frame;
	}

} // Previewer
