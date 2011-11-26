package com.gwt.conn.client;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Visual Editor Class **
 * 
 * The Visual Editor allows the user to modify visual aspects of the restaurant menu
 * including template, background, logo, font, and colors.
 * 
 */

public class VisualEditor {
	
	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();
	
	/** Called when the visual editor needs to be used. */
	public static HorizontalPanel getVisualEditor(final Menu menu) {
		return new HorizontalPanel();
	} // end getVisualEditor
	
} // VisualEditor
