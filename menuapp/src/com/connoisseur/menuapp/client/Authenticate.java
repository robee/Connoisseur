package com.connoisseur.menuapp.client;

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
 * This authenticator provides the ability to interact with the menu app upon
 * successful authentication. The Viewer is mounted after authenticating.
 *
 */

public class Authenticate {
	
	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();
	
	/** This is essentially the main method for the menu app. */
	public static void go() {
		storage.clear(); // uncomment to completely reset app
		
		final DialogBox startupBox = new DialogBox(); // movable box that contains widgets
		startupBox.setAnimationEnabled(true);
		final VerticalPanel startupPanel = new VerticalPanel(); // can contain other widgets
		startupPanel.addStyleName("marginPanel"); // interacts with Menuapp.css
		startupPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		// check to see if storage is supported
		if (storage != null) {
			
			// load viewer if license key and restID have been submitted before
			if (storage.getLength() > 0) {
				Viewer.loadViewer();
			}
			
			// otherwise load authentication UI in order to receive input
			else {
				final Button submitButton = new Button("Submit"); // "Submit" appears on button
				submitButton.addStyleName("myButton"); // interacts with Menuapp.css
				final HorizontalPanel buttonPanel = new HorizontalPanel(); // used to center button
				buttonPanel.addStyleName("marginlessPanel");
				
				// license widgets
				final Label licenseErrorLabel = new Label(); // dynamic text
				licenseErrorLabel.addStyleName("errorLabel"); // interacts with Menuapp.css
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
				
				// create a handler for submitButton, submitLicense and submitRestID
				class MyHandler implements ClickHandler, KeyUpHandler {
					/** Fired when the user clicks submit. */
					public void onClick(ClickEvent event) {
						submit();
					}
					
					/** Fired when the user presses Enter in a submit field. */
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
					}
					
					/** Checks the submitted license key and restaurant ID for validity. Loads Viewer if valid. */
					private void submit() {
						String license = submitLicense.getText(); // unused for now
						String restID = submitRestID.getText(); // not sure how to validate yet
						int returnFlag = 0; // so that both tests can be done
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
						
						// clean up
						submitButton.setEnabled(false);
						submitLicense.setEnabled(false);
						submitRestID.setEnabled(false);
						startupBox.hide();
						
						// set up storage
						storage.setItem("license", license); // secret key for security
						storage.setItem("restID", restID); // used for almost every call to the backend
						
						// show menu
						Viewer.loadViewer();
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
