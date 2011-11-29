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
		startupBox.setText("Connoisseur");
		final VerticalPanel startupPanel = new VerticalPanel(); // can contain other widgets
		startupPanel.addStyleName("marginPanel"); // interacts with Connfrontend.css
		startupPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		// check to see if storage is supported
		if (storage != null) {
			
			// load dashboard if license key has been submitted before
			if (storage.getLength() > 0) {
				boolean internet = Communicate.hasInternet();
				StorageContainer.initStorage(); // prepares storage for interaction
				Dashboard.loadMenu(Communicate.deserialize(storage.getItem("menu")), "", internet);
				//Dashboard.loadDashboard();
			}
			
			// otherwise load authentication UI in order to receive license key input
			else {
				final Button submitButton = new Button("Submit"); // "Submit" appears on button
				submitButton.addStyleName("myButton"); // interacts with Connfrontend.css
				final HorizontalPanel buttonPanel = new HorizontalPanel(); // used to center button
				buttonPanel.addStyleName("marginlessPanel");
				
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
				buttonPanel.add(submitButton);
				startupPanel.add(buttonPanel);
				
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
						String license = submitLicense.getText();
						String restID = submitRestID.getText();
						boolean result = Communicate.authenticate(restID, license);
						//TODO
				/*		int returnFlag = 0; // so that both tests can be done
						licenseErrorLabel.setText("");
						restErrorLabel.setText("");
						
						// validate license
						String result = FieldVerifier.isValidLicenseKey(license); // from FieldVerifier.java
						if (!result.equals("")) { // error
							licenseErrorLabel.setText("You submitted an invalid license key.");
							submitLicense.selectAll();
							returnFlag = 1;
						}
						
						// validate restID
						result = FieldVerifier.isValidRestaurantID(restID);
						if (!result.equals("")) { // error
							restErrorLabel.setText("You submitted an invalid restaurant ID.");
							submitRestID.selectAll();
							returnFlag = 1;
						}
						
						// don't do anything until the errors are resolved
						if (returnFlag == 1) return;
						*/
//						String json = "{\"menu_id\": \"\", \"restaurant_id\": \""+restID +"\", " +
//							"\"menu_name\": \"menu\", \"ui_profile\": {\"logo_url\": \"http://www.virginialogos.com/Portals/" +
//							"57ad7180-c5e7-49f5-b282-c6475cdb7ee7/Food.jpg\", \"color\": \"black\", \"menu\": \"\", \"profile_id\": " +
//							"\"259fdb7df24a4f6d\", \"template\": \"classy\", \"font\": \"Helvetica\"}, \"restaurant_name\": " +
//							"\"restaurantTest\", \"menuitems\": {\"Drink\": [{\"category\": \"Drink\", \"menuitem_id\": " +
//							"\"24c0206c962a4903\", \"description\": \"\", \"menu\": \"\", \"image\": \"This is a sample menu Item\", " +
//							"\"price\": \"11.0\", \"name\": \"Starter Item 2\"}], \"Appy\": [{\"category\": \"Appy\", \"menuitem_id\": " +
//							"\"6c1bd016d5b54dc9\", \"description\": \"\", \"menu\": \"\" , \"image\": \"This is a sample menu Item\", " +
//							"\"price\": \"10.0\", \"name\": \"Starter Item 1\"}]}}"; 
						
						// clean up
						submitButton.setEnabled(false);
						submitLicense.setEnabled(false);
						submitRestID.setEnabled(false);
						startupBox.hide();
						
						// set up storagestorage.setItem("license", license); // secret key for security
						storage.setItem("restID", restID); // used for almost every call to the backend
						storage.setItem("numMenus", Integer.toString(0));
						StorageContainer.initStorage();
						StorageContainer.addMenu("menu", "");
//						
//						// check for internet connection (affects whether some things load)
//						boolean internet = Communicate.hasInternet();
//						
//						//for testing
//						final Menu m = new Menu("menu");
//						m.setID("236e8690d55248ff");
//						m.setLogo("http://www.virginialogos.com/Portals/57ad7180-c5e7-49f5-b282-c6475cdb7ee7/Food.jpg");
//						m.setRestaurantID(storage.getItem("restID"));
//						m.setColor("black");
//						m.setMenu("null");
//						m.setProfile("259fdb7df24a4f6d");
//						m.setTemplate("classy");
//						m.setFont("Helvetica");
//						m.addCategory("Drink");
//						m.addMenuItem("Drink", "Starter Item 2");
//						m.addCategory("Appy");
//						m.addMenuItem("Appy", "Starter Item 1");
//						Dashboard.loadMenu(m, "", Communicate.hasInternet());
						Communicate.sync("menu", storage.getItem("restID"));
						Dashboard.loadMenu(Communicate.deserialize(json), "firstTime");
						Dashboard.loadDashboard();
					} // end submit
				} // MyHandler
				
				// attach the handler
				final MyHandler handler = new MyHandler();
				submitButton.addClickHandler(handler);
				submitLicense.addKeyUpHandler(handler);
				submitRestID.addKeyUpHandler(handler);
			} // else load authenti=cation UI
			
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
