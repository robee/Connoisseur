package com.gwt.conn.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Authenticate Class **
 * 
 * This authenticator provides the ability to interact with the editor app.
 * The authenticator loads the dashboard after authenticating the user.
 *
 */

public class Authenticate {
	
	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();
	
	/** This is essentially the main method for the editor app. */
	public static void go() {
		storage.clear(); // uncomment to completely reset app
		
		final DialogBox startupBox = new DialogBox(); // movable box that contains widgets
		startupBox.setAnimationEnabled(true);
		final VerticalPanel startupPanel = new VerticalPanel(); // can contain other widgets
		startupPanel.addStyleName("marginPanel"); // interacts with Connfrontend.css
		startupPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		// check to see if storage is supported
		if (storage != null) {
			
			// load dashboard if license key has been submitted before
			if (storage.getLength() > 0) {
				StorageContainer.initStorage(); // prepares storage for interaction
				Dashboard.loadMenu(Communicate.deserialize(storage.getItem("menu")));
				//Dashboard.loadDashboard();
			}
			
			// otherwise load authentication UI in order to receive license key input
			else {
				final Button submitButton = new Button("Submit"); // "Submit" appears on button
				submitButton.addStyleName("myButton"); // interacts with Connfrontend.css
				
				// license widgets
				final Label licenseErrorLabel = new Label(); // dynamic text
				licenseErrorLabel.addStyleName("errorLabel"); // interacts with Connfrontend.css
				final TextBox submitLicense = new TextBox(); // user can input text using this
				submitLicense.setText("license key..."); // default text to be seen on load
				
				// restaurant ID widgets
				final Label restErrorLabel = new Label();
				restErrorLabel.addStyleName("errorLabel");
				final TextBox submitRestID = new TextBox();
				submitRestID.setText("restaurant ID...");
				
				// organize UI
				startupPanel.add(new HTML("Please enter your license key:"));
				startupPanel.add(submitLicense);
				startupPanel.add(licenseErrorLabel);
				startupPanel.add(new HTML("<br>Please enter your restaurant ID:"));
				startupPanel.add(submitRestID);
				startupPanel.add(restErrorLabel);
				startupPanel.add(new HTML("<br>"));
				startupPanel.add(submitButton);
				
				// setup startupBox, which is what will be seen by the user
				startupBox.setWidget(startupPanel); // connects the two widgets
				startupBox.center(); // also shows the box
				
				// focus the cursor on submitLicense when startupBox appears
				submitLicense.setFocus(true);
				submitLicense.selectAll();
				
				// create a handler for the submitButton and submitLicense
				class MyHandler implements ClickHandler, KeyUpHandler {
					/** Fired when the user clicks submit. */
					public void onClick(ClickEvent event) {
						submit();
					}
					
					/** Fired when the user presses Enter in submitLicense. */
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
					}
					
					/** Checks the submitted license key for validity. Loads the dashboard if valid. */
					private void submit() {
						// validate license key
						String license = submitLicense.getText(); // unused for now
						String restID = submitRestID.getText();
						String json = FieldVerifier.isValidLicenseKey(license, restID, "menu"); // from FieldVerifier.java
						if (json.charAt(0) != '{') { // not json
							restErrorLabel.setText("You submitted an invalid restaurant ID.");
							submitLicense.selectAll();
							return;
						}
						
						// clean up
						submitButton.setEnabled(false);
						submitLicense.setEnabled(false);
						submitRestID.setEnabled(false);
						startupBox.hide();
						
						// set up storage
						storage.setItem("license", license); // remember restaurant_id
						storage.setItem("numMenus", Integer.toString(0));
						StorageContainer.initStorage();
						StorageContainer.addMenu("menu", json);
						Dashboard.loadMenu(Communicate.deserialize(json));
						//Dashboard.loadDashboard();
					}
				} // MyHandler
				
				// attach the handler
				final MyHandler handler = new MyHandler();
				submitButton.addClickHandler(handler);
				submitLicense.addKeyUpHandler(handler);
				submitRestID.addKeyUpHandler(handler);
			} // else load authentication UI
			
		} // if storage supported
		
		// storage is not supported, so report error
		else {
			startupPanel.add(new HTML("<font color=red>The app will not function because local<br>" +
					"storage is not supported on this platform.</font>"));
			startupBox.setWidget(startupPanel);
			startupBox.center();
		}
	} // end go
	
} // Authenticate
