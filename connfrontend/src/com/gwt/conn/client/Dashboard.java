package com.gwt.conn.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Dashboard Class **
 * 
 * This class is similar to a home page. From here, the user can travel to
 * all other parts of the app. That is, from here, the user can create new
 * menus, edit existing menus and schedule when certain menus go live.
 * 
 * NOTE: Scheduling has not been implemented,
 *       so only one menu per restaurant is
 *       currently supported.
 * 
 */

public class Dashboard {

	/** Create a remote service proxy to talk to the server-side Greeting service. */
	private static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/** Local storage for saving strings, which persist when the app is shut down.  */
	private static final Storage storage = StorageContainer.getStorage();

	/** The message displayed to the user when there is a connection problem. */
	private static final String SERVER_ERROR =
			"<font color=red>A connection error occurred while attempting to contact the server.<br>" +
					"You've been put into offline mode for the remainder of this session.<br>" +
					"All changes you make will be saved locally; so you can work while in<br>" +
					"offline mode, but you must eventually push your changes to the server.<br>" +
					"Check your network connection, then restart Connoisseur to try again.<br><br></font>";

	/** Called whenever a menu needs to be loaded. Parameter "message" is displayed after loading. */
	public static void loadMenu(final Menu menu, String message) {
		// this is used so that buttons don't do anything when clicked
		// if the contents that the button would load are already visible
		// need to use storage to save state of editor when interacting with buttons
		storage.setItem("curDashPage", "vis"); // default to visual editor first
		//storage.setItem("curDashPage", "dat"); // for testing

		// initialize panels for widgets to be placed in
		final VerticalPanel dashboardPan = new VerticalPanel();
		dashboardPan.addStyleName("marginlessPanel"); // interacts with Connfrontend.css
		dashboardPan.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dashboardPan.setSize("100%", "100%");
		final HorizontalPanel buttonPan = new HorizontalPanel();
		buttonPan.addStyleName("marginPanel");

		// initialize buttons and put them into the button panel
		final Button visualButton = new Button("Visual Editor");
		final Button dataButton = new Button("Data Editor");
		visualButton.addStyleName("myButton");
		dataButton.addStyleName("myButton");
		buttonPan.add(visualButton);
		buttonPan.add(dataButton);

		// put the button panel in the dashboard panel
		dashboardPan.add(buttonPan);
		dashboardPan.setCellHeight(buttonPan, "0%");

		// get static instances of all possible editor app contents
		final Frame previewContent = Previewer.getPreviewer(menu);
		final HorizontalPanel visualContent = VisualEditor.getVisualEditor(menu);
		final HorizontalPanel dataContent = DataEditor.getDataEditor(menu);

		// put the dashboard panel in the root panel
		dashboardPan.add(visualContent); // load visual editor by default
		dashboardPan.setCellHeight(visualContent, "100%");
		//dashboardPan.add(dataContent); // load data editor for testing
		//dashboardPan.setCellHeight(dataContent, "100%");
		RootPanel.get().add(dashboardPan, 0, 0);

		// handler for visualButton shows the visual editor
		class VisualHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				String cur = storage.getItem("curDashPage");
				if (cur.equals("dat")) {
					dashboardPan.remove(dataContent);
					cont();
				}
				else if (cur.equals("pre")) {
					dashboardPan.remove(previewContent);
					cont();
				}
			}
			private void cont() {
				storage.setItem("curDashPage", "vis");
				dashboardPan.add(visualContent);
				dashboardPan.setCellHeight(visualContent, "100%");
			}
		}
		final VisualHandler visualHandler = new VisualHandler();
		visualButton.addClickHandler(visualHandler);

		// handler for dataButton shows the data editor
		class DataHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				String cur = storage.getItem("curDashPage");
				if (cur.equals("vis")) {
					dashboardPan.remove(visualContent);
					cont();
				}
				else if (cur.equals("pre")) {
					dashboardPan.remove(previewContent);
					cont();
				}
			}
			private void cont() {
				storage.setItem("curDashPage", "dat");
				dashboardPan.add(dataContent);
				dashboardPan.setCellHeight(dataContent, "100%");
			}
		}
		final DataHandler dataHandler = new DataHandler();
		dataButton.addClickHandler(dataHandler);
/*
		// check for internet connection
		String test = Communicate.getMenu("", storage.getItem("restID"), "http://connoisseurmenu.appspot.com/menu");

		// no internet connection, so report error
		if (test.charAt(0) != '{') {
			// put everything in a popup dialog box
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
				public void onClick(ClickEvent event) { // button clicked
					cont();
				}
				public void onKeyUp(KeyUpEvent event) { // ENTER key
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) cont();
				}
				private void cont() {
					errorButton.setEnabled(false);
					errorBox.hide();
				}
			}
			final ErrorHandler errorHandler = new ErrorHandler();
			errorButton.addClickHandler(errorHandler);
		}

		// internet connection detected, so attach synchronize button to the dashboard
		else {
			// attach a preview button
			final Button previewButton = new Button("Preview");
			previewButton.addStyleName("myButton");
			buttonPan.add(previewButton);
			class PreviewHandler implements ClickHandler {
				public void onClick(ClickEvent event) {
					String cur = storage.getItem("curDashPage");
					if (cur.equals("vis")) {
						dashboardPan.remove(visualContent);
						cont();
					}
					else if (cur.equals("dat")) {
						dashboardPan.remove(dataContent);
						cont();
					}
				}
				private void cont() {
					storage.setItem("curDashPage", "pre");
					dashboardPan.add(previewContent);
					dashboardPan.setCellHeight(previewContent, "100%");
				}
			}
			final PreviewHandler previewHandler = new PreviewHandler();
			previewButton.addClickHandler(previewHandler);

			// attach a push-to-server button
			final Button pushButton = new Button("Synchronize");
			pushButton.addStyleName("myButton");
			buttonPan.add(pushButton);
			class PushHandler implements ClickHandler {
				public void onClick(ClickEvent event) {
					String result = Communicate.updateMenu(menu.getName(), storage.getItem("license"),
							"http://connoisseurmenu.appspot.com/menu/update");
					if (result.equals("Update Successful")) {
						// get menu because new information should be inserted after updating
						String json = Communicate.getMenu("", storage.getItem("restID"),
								"http://connoisseurmenu.appspot.com/menu");
					//	String json = Communicate.getMenu(menu.getMenuID(), storage.getItem("restID"),
					//			"http://connoisseurmenu.appspot.com/menu");
						storage.setItem(menu.getName(), json);

						// reset UI and deserialize to accommodate new information
						RootPanel.get().remove(dashboardPan);
						loadMenu(Communicate.deserialize(json), "menuUpdated");
					}
					else {
						// report error in popup dialog box
					}
				}
			}
			final PushHandler pushHandler = new PushHandler();
			pushButton.addClickHandler(pushHandler);
		} // end onSuccess
*/
	} // end startDashboard

/* ************************************* UNUSED/DEPRECATED CODE BELOW ************************************* */

	/** Called by Authenticate.java when successfully authenticated. (multiple menus version) */
	public void loadDashboard() {
		final HTML statusLabel = new HTML(); // dynamic HTML
		final DialogBox dashboardBox = new DialogBox();
		dashboardBox.setAnimationEnabled(true);

		// send the license to the server
		greetingService.greetServer(storage.getItem("license"),
				new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// show the RPC error message to the user
				dashboardBox.setText("Dashboard - Offline Mode");
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
					// Fired when the user clicks Continue.
					public void onClick(ClickEvent event) {
						cont();
					}

					// Fired when the user presses Enter.
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) cont();
					}

					private void cont() {
						errorButton.setEnabled(false);
						errorBox.hide();
						finishLoad(dashboardBox, statusLabel); // void method below
					}
				}

				// attach the handler
				final ErrorHandler errorHandler = new ErrorHandler();
				errorButton.addClickHandler(errorHandler);
			} // end onFailure

			public void onSuccess(String result) {
				//StorageContainer.synchronizeWithBackend(); // implemented in StorageContainer.java
				dashboardBox.setText("Dashboard");
				statusLabel.setHTML(result);
				finishLoad(dashboardBox, statusLabel); // void method below
			}
		}); // end greetServer
	} // end loadDashboard

	/** Called when the dashboard needs to be loaded. */
	private void finishLoad(final DialogBox dashboardBox, final HTML statusLabel) {
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
				//scheduler.loadScheduler(dashboardBox); // implemented in Scheduler.java
			}
		}

		// attach the handlers to their respective buttons
		final NewHandler newHandler = new NewHandler();
		final EditHandler editHandler = new EditHandler();
		final ScheduleHandler scheduleHandler = new ScheduleHandler();
		newButton.addClickHandler(newHandler);
		editButton.addClickHandler(editHandler);
		scheduleButton.addClickHandler(scheduleHandler);
	} // end finishLoad

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
					submitField.selectAll();
					return;
				}

				String json = "";//createDefaultJSON(); // get default JSON object
				StorageContainer.addMenu(menuName, json); // to storage
				sendButton.setEnabled(false);
				submitBox.hide();
				//visualEditor.loadVisualEditor(dashboardBox, json); // start editing
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
		int numMenus = StorageContainer.getNumMenus();
		final String[] menus = StorageContainer.getMenus();

		// organize UI
		for (int i=0; i < numMenus; ++i) {
			final String menuName = menus[i];
			final Button button = new Button(menuName);
			button.addStyleName("myButton");
			editVPanel.add(button);

			// handler for the buttons
			class ButtonHandler implements ClickHandler {
				/** Fired when the user clicks cancel. */
				public void onClick(ClickEvent event) {
					editBox.hide();
					//String json = StorageContainer.getMenu(menuName);
					//visualEditor.loadVisualEditor(dashboardBox, json); // start editing
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
