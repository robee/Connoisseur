package com.gwt.conn.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Authenticate Class **
 * 
 * This class provides the ability to interact with the editor app.
 * The editor loads the dashboard after authenticating the user.
 *
 */

public class Authenticate extends StorageContainer {
	
	/** This creates an instance of the Dashboard class. */
	private final Dashboard dashboard = GWT.create(Dashboard.class);
	
	/** This is essentially the main method for the editor app. */
	public void go() {
		//storage.clear(); // uncomment to completely reset app
		
		final DialogBox startupBox = new DialogBox(); // movable box that contains widgets
		startupBox.setAnimationEnabled(true);
		
		final VerticalPanel startupVPanel = new VerticalPanel(); // can contain other widgets
		startupVPanel.addStyleName("marginPanel"); // interacts with Connfrontend.css
		startupVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		// check to see if storage is supported
		if (storage != null) {
			// load dashboard if license key has been submitted before
			if (storage.getLength() > 0) {
				startup(); // prepares storage for interaction (in StorageContainer.java)
				dashboard.startDashboard(); // gets the rest of the app started
			}
			
			// load authentication UI in order to receive license key input
			else {
				final Label errorLabel = new Label(); // dynamic text
				errorLabel.addStyleName("errorLabel"); // interacts with Connfrontend.css
				
				final Button sendButton = new Button("Submit");
				sendButton.addStyleName("myButton"); // interacts with Connfrontend.css
				
				final TextBox submitField = new TextBox(); // user can input text using this
				submitField.setText("license key..."); // default text to be seen on load
				
				// organize UI
				final HorizontalPanel startupHPanel = new HorizontalPanel();
				startupVPanel.add(new HTML("Please enter your license key:"));
				startupHPanel.add(submitField);
				startupHPanel.add(sendButton);
				startupVPanel.add(startupHPanel);
				startupVPanel.add(errorLabel);
				
				// setup startupBox, which is what will be seen
				startupBox.setWidget(startupVPanel); // connects the two widgets
				startupBox.center(); // also shows the box
				
				// focus the cursor on submitField when startupBox appears
				submitField.setFocus(true);
				submitField.selectAll();
				
				// create a handler for the sendButton and submitField
				class MyHandler implements ClickHandler, KeyUpHandler {
					/** Fired when the user clicks submit. */
					public void onClick(ClickEvent event) {
						submit();
					}
					
					/** Fired when the user presses Enter in submitField. */
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
							submit();
						}
					}
					
					/** Checks the submitted license key for validity. Loads the dashboard if valid. */
					private void submit() {
						// validate license key
						String license = submitField.getText();
						String test = FieldVerifier.isValidLicenseKey(license); // from FieldVerifier.java
						if (!test.equals("")) {
							errorLabel.setText(test);
							return;
						}
						
						// clean up, then load dashboard
						sendButton.setEnabled(false);
						startupBox.hide();
						storage.setItem("license", license); // remember license key
						storage.setItem("numMenus", Integer.toString(0));
						dashboard.startDashboard(); // gets the rest of the app started
					}
				}
				
				// attach the handler
				final MyHandler handler = new MyHandler();
				sendButton.addClickHandler(handler);
				submitField.addKeyUpHandler(handler);
			} // else load authentication UI
		} // if storage supported
		
		// storage is not supported, so report error
		else {
			startupVPanel.add(new HTML("<font color=red>The app will not function because<br>" +
					"storage is not supported on this platform.</font>"));
			startupBox.setWidget(startupVPanel);
			startupBox.center();
		}
	} // end go
	
} // Authenticate
