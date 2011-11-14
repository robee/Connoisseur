package com.gwt.conn.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Dashboard Class **
 * 
 * This class is similar to a home page. From here, the user can travel to
 * all other parts of the app. That is, from here, the user can create new
 * menus, edit existing menus and schedule when certain menus go live.
 * 
 */

public class Dashboard extends StorageContainer {

	/** This creates an instance of the VisualEditor class. */
	private final VisualEditor visualEditor = GWT.create(VisualEditor.class);
	
	/** This creates an instance of the Scheduler class. */
	private final Scheduler scheduler = GWT.create(Scheduler.class);

	/** Create a remote service proxy to talk to the server-side Greeting service. */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/** The message displayed to the user when the server cannot be reached or returns an error. */
	private static final String SERVER_ERROR =
		"<font color=red>A connection error occurred while attempting to contact the server.<br>" +
		"You've been put into offline mode for the remainder of this session.<br>" +
		"Changes will be pushed to the server when starting your next session.<br>" +
		"Check your network connection, then restart Connoisseur to try again.<br><br></font>";
	
	/** Called by Authenticate.java when everything works. */
	public void startDashboard() {
		final HTML statusLabel = new HTML(); // dynamic HTML
		final DialogBox dashboardBox = new DialogBox();
		dashboardBox.setAnimationEnabled(true);
		
		// send the license to the server
		greetingService.greetServer(storage.getItem("license"),
				new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// show the RPC error message to the user
				dashboardBox.setText("Dashboard - Offline Mode");
/** ///////STATUS LABEL UPDATE??? WTFZZXXZX? */
				//statusLabel.setHTML(???);
				
				final DialogBox errorBox = new DialogBox();
				errorBox.setAnimationEnabled(true);

				final VerticalPanel errorVPanel = new VerticalPanel();
				errorVPanel.addStyleName("marginPanel"); // interacts with Connfrontend.css
				errorVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

				final Button errorButton = new Button("Continue");
				errorButton.addStyleName("myButton");

				final HorizontalPanel errorHPanel = new HorizontalPanel();

				errorVPanel.add(new HTML(SERVER_ERROR));
				errorHPanel.add(errorButton);
				errorVPanel.add(errorHPanel);
				errorBox.setWidget(errorVPanel);
				errorBox.center();
				errorButton.setFocus(true);

				// handler for errorButton
				class ErrorHandler implements ClickHandler, KeyUpHandler {
					/** Fired when the user clicks Continue. */
					public void onClick(ClickEvent event) {
						cont();
					}
					
					/** Fired when the user presses Enter. */
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) cont();
					}

					private void cont() {
						errorButton.setEnabled(false);
						errorBox.hide();
						loadDashboard(dashboardBox, statusLabel); // void method below
					}
				}

				// attach the handler
				final ErrorHandler errorHandler = new ErrorHandler();
				errorButton.addClickHandler(errorHandler);
			} // end onFailure

			public void onSuccess(String result) {
				synchronizeWithBackend(); // implemented in StorageContainer.java
				dashboardBox.setText("Dashboard");
				statusLabel.setHTML(result);
				loadDashboard(dashboardBox, statusLabel); // void method below
			}
		}); // end greetServer
	} // end startDashboard
	
	/** Called when the dashboard needs to be loaded. */
	private void loadDashboard(final DialogBox dashboardBox, final HTML statusLabel) {
		final VerticalPanel dashboardVPanel = new VerticalPanel();
		dashboardVPanel.addStyleName("marginPanel"); // interacts with Connfrontend.css
		dashboardVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		// addStyleName makes buttons look pretty (interacts with Connfrontend.css)
		final Button newButton = new Button("New");
		final Button editButton = new Button("Edit");
		final Button scheduleButton = new Button("Schedule");
		newButton.addStyleName("myButton");
		editButton.addStyleName("myButton");
		scheduleButton.addStyleName("myButton");

		// setup vertical panel
		dashboardVPanel.add(statusLabel);
		dashboardVPanel.add(newButton);
		dashboardVPanel.add(editButton);
		dashboardVPanel.add(scheduleButton);
		dashboardBox.setWidget(dashboardVPanel);
		dashboardBox.center();

		// handler for newButton
		class NewHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				dashboardBox.hide();
				newMenu(dashboardBox); // void method below
			}
		}

		// handler for editButton
		class EditHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				dashboardBox.hide();
				getMenus(dashboardBox); // void method below
			}
		}

		// handler for scheduleButton
		class ScheduleHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				dashboardBox.hide();
				scheduler.loadScheduler(dashboardBox); // implemented in Scheduler.java
			}
		}

		// attach the handlers to their respective buttons
		final NewHandler newHandler = new NewHandler();
		final EditHandler editHandler = new EditHandler();
		final ScheduleHandler scheduleHandler = new ScheduleHandler();
		newButton.addClickHandler(newHandler);
		editButton.addClickHandler(editHandler);
		scheduleButton.addClickHandler(scheduleHandler);
	} // end loadDashboard
	
	/** Called when the user wants to create a new menu. */
	private void newMenu(final DialogBox dashboardBox) {
		// declarations
		final DialogBox submitBox = new DialogBox();
		submitBox.setAnimationEnabled(true);
		
		final VerticalPanel submitVPanel = new VerticalPanel();
		submitVPanel.addStyleName("marginPanel");
		submitVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		final Label errorLabel = new Label();
		errorLabel.addStyleName("errorLabel");
		
		final Button sendButton = new Button("Submit");
		sendButton.addStyleName("myButton");
		
		final Button cancelButton = new Button("Cancel");
		sendButton.addStyleName("myButton");
		
		final TextBox submitField = new TextBox(); // user can input text using this
		submitField.setText("menu name..."); // default text to be seen on load
		
		final HorizontalPanel submitHPanel = new HorizontalPanel();
		
		// organize UI
		submitVPanel.add(new HTML("Please enter a name for your new menu:"));
		submitHPanel.add(submitField);
		submitHPanel.add(sendButton);
		submitHPanel.add(cancelButton);
		submitVPanel.add(submitHPanel);
		submitVPanel.add(errorLabel);
		submitBox.setWidget(submitVPanel);
		submitBox.center();
		submitField.setFocus(true);
		submitField.selectAll();
		
		// handler for the sendButton and submitField
		class SubmitHandler implements ClickHandler, KeyUpHandler {
			/** Fired when the user clicks submit. */
			public void onClick(ClickEvent event) {
				submit();
			}
			
			/** Fired when the user presses Enter in submitField. */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
			}
			
			/**
			 * Checks the submitted menu name for validity.
			 * If valid, loads a new menu object and starts the visual editor.
			 */
			private void submit() {
				// validate menu name
				String menuName = submitField.getText();
				String test = FieldVerifier.isValidMenuName(menuName);
				if (!test.equals("")) {
					errorLabel.setText(test);
					return;
				}

				addMenu(menuName); // to storage (implemented in StorageContainer.java)
				//String json = createDefaultJSON(); // get default JSON object
				String json = getMenu(menuName);
				sendButton.setEnabled(false);
				submitBox.hide();
				visualEditor.loadVisualEditor(dashboardBox, json); // start editing
			}
		}
		
		// handler for the cancel button
		class CancelHandler implements ClickHandler {
			/** Fired when the user clicks cancel. */
			public void onClick(ClickEvent event) {
				submitBox.hide();
				dashboardBox.center();
			}
		}
		
		// attach the handlers
		final SubmitHandler submitHandler = new SubmitHandler();
		final CancelHandler cancelHandler = new CancelHandler();
		sendButton.addClickHandler(submitHandler);
		submitField.addKeyUpHandler(submitHandler);
		cancelButton.addClickHandler(cancelHandler);
	} // end newMenu
	
	/** Called when the user wants to edit an existing menu. */
	private void getMenus(final DialogBox dashboardBox) {
		// declarations
		final DialogBox editBox = new DialogBox();
		editBox.setAnimationEnabled(true);

		final VerticalPanel editVPanel = new VerticalPanel();
		editVPanel.addStyleName("marginPanel");
		editVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		final Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("myButton");
		
		// from StorageContainer.java
		int numMenus = getNumMenus();
		final String[] menus = getMenus();
		
		// organize UI
		for (int i=0; i < numMenus; ++i) {
			final String menuName = menus[i];
			final Button button = new Button(menuName);
			button.addStyleName("myButton");
			editVPanel.add(button);
			
			// handler for the buttons
			class ButtonHandler extends StorageContainer implements ClickHandler {
				/** Fired when the user clicks cancel. */
				public void onClick(ClickEvent event) {
					editBox.hide();
					String json = getMenu(menuName);
					visualEditor.loadVisualEditor(dashboardBox, json); // start editing
				}
			}
			
			// attach handler
			final ButtonHandler buttonHandler = new ButtonHandler();
			button.addClickHandler(buttonHandler);
		}

		editVPanel.add(cancelButton);
		editBox.setWidget(editVPanel);
		editBox.center();

		// handler for the cancel button
		class CancelHandler implements ClickHandler {
			/** Fired when the user clicks cancel. */
			public void onClick(ClickEvent event) {
				editBox.hide();
				dashboardBox.center();
			}
		}

		// attach cancel handler
		final CancelHandler cancelHandler = new CancelHandler();
		cancelButton.addClickHandler(cancelHandler);
	} // end getMenus
	
} // Dashboard
