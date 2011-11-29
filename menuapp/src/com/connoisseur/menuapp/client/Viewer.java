package com.connoisseur.menuapp.client;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Timer;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;


/** The Viewer Class **
 * 
 * This class contains the code for getting an iframe widget that fills the screen.
 * For the menu app, a rendered menu on the backend is simply viewed. All that is
 * needed is the restaurant ID in order to target the correct restaurant's menu.
 * 
 */

public class Viewer {
	
	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();
	
	/** Shows the restaurant's menu in an iframe. */
	public static void loadViewer() {
		
		//final Frame viewer;
		// if Internet -> getViewer, else load from cache
		if (Communicate.hasInternet() || storage.getItem("cache") == null || storage.getItem("cache").equals("")) {
			Frame viewer = Viewer.getViewer();
	  		RootPanel.get().add(viewer, 0, 0);
		}
		else {
			HTML htmlPanel = new HTML();
			String htmlCache = storage.getItem("cache");
			htmlPanel.setHTML(htmlCache);
			RootPanel.get().add(htmlPanel);
		}
  		
		Timer t = new Timer(){
		      public void run() {
		    	if (Communicate.hasInternet()) {
		    		Communicate.sync("http://connoisseurmenu.appspot.com/?", ""+storage.getItem("restID"));
		    		RootPanel.get().clear();
		    		Frame viewer = Viewer.getViewer();
			  		RootPanel.get().add(viewer, 0, 0);
		        }
		    }
		};
		t.scheduleRepeating(10000);
		
		// storage.setItem("cache", htmlCache);

	} // end loadViewer
	
	/** Called when the previewer needs to be loaded. */
	private static Frame getViewer() {
		// this widget allows web pages to be seen in an HTML iframe
		final Frame frame = new Frame();
		frame.getElement().setAttribute("style", "width:100%; height:100%; border:0");
		frame.setUrl("http://connoisseurmenu.appspot.com/?restaurant_id=" + storage.getItem("restID"));
		
		return frame;
	} // end getViewer

} // Previewer
