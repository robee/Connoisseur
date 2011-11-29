package com.gwt.conn.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
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

	/** Declare these as global for convenience. */
	private static final HorizontalPanel visEditorPan = new HorizontalPanel();
	private static final VerticalPanel navigationPan = new VerticalPanel();
	private static final VerticalPanel nullPage = new VerticalPanel();
	private static final HTML testLabel = new HTML();

	/** Called when the data editor needs to be loaded. */
	public static HorizontalPanel getVisualEditor(final Menu menu, final Frame previewer, final boolean internet) {
		// testing
		testLabel.setText(storage.getItem("menu"));
		//RootPanel.get().add(testLabel, 0, 500);

		// add styles to global widgets
		visEditorPan.addStyleName("marginlessPanel");
		navigationPan.addStyleName("marginPanel");
		nullPage.addStyleName("marginlessPanel");

		// visual editor has a navigation pane that the user can modify menu fields in
		// nullPage contains an error message saying that no internet connection exists
		final VerticalPanel errorPan = new VerticalPanel();
		errorPan.add(new HTML("<b>Offline Mode</b><br>No internet connection.<br>Reset Connoisseur to try again."));
		nullPage.add(errorPan);
		visEditorPan.add(navigationPan);
		visEditorPan.setCellWidth(navigationPan, "33.3%");

		// if there is internet, load the previewer
		if (internet) {
			visEditorPan.add(previewer);
			visEditorPan.setCellWidth(previewer, "66.7%");
		}
		else { // otherwise load the null page
			visEditorPan.add(nullPage);
			visEditorPan.setCellWidth(nullPage, "66.7%");
		}

		// first, construct a horizontal panel wherein the menu's logo text or image URL can be modified
		final Label logoErrorLabel = new Label();
		logoErrorLabel.addStyleName("errorLabel");
		final Button logoSendButton = new Button("Update");
		logoSendButton.addStyleName("myButton");
		final TextBox logoSubmitField = new TextBox(); // user can input text using this
		logoSubmitField.setText(menu.getLogo()); // default text to be seen on load
		navigationPan.add(new HTML("Restaurant Name (regular text) or<br>Logo Image (URL starting with http://)"));
		navigationPan.add(logoSubmitField);
		navigationPan.add(logoErrorLabel);
		navigationPan.add(logoSendButton);
		navigationPan.add(new HTML("<br>"));

		// add handler for image update button
		class LogoHandler implements ClickHandler, KeyUpHandler {
			// fired when the user clicks submit
			public void onClick(ClickEvent event) {
				submit();
			}

			// fired when the user presses Enter in submitField
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) submit();
			}

			// checks the submitted URL for validity (starts with http://)
			private void submit() {
				// check if anything was changed
				String logo = logoSubmitField.getText();
				if (logo.equals(menu.getLogo())) return;

				// check for validity
				String test = FieldVerifier.isValidURL(logo);
				if (!test.equals("")) {
					test = FieldVerifier.isValidName(logo, new String[0]);
					if (!test.equals("")) {
						logoErrorLabel.setText(test);
						logoSubmitField.selectAll();
						return;
					}
				}
				else test = "url";

				// propagate changes
				logoErrorLabel.setText("");
				if (test.equals("url")) menu.setLogo(escapeHtml(logo));
				else menu.setLogo(logo);
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}

			// escape an html string to prevent cross-site script vulnerabilities
			private String escapeHtml(String url) {
				return url.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			}
		} // LogoHandler
		final LogoHandler logoHandler = new LogoHandler();
		logoSendButton.addClickHandler(logoHandler);
		logoSubmitField.addKeyUpHandler(logoHandler);

		// second, construct a drop down box for editing the menu's color scheme
		final ListBox colorList = new ListBox();
		colorList.addItem("black"); // 0
		colorList.addItem("white"); // 1
		colorList.addItem("red"); // 2
		navigationPan.add(new HTML("Color Scheme:"));
		navigationPan.add(colorList);
		navigationPan.add(new HTML("<br>"));

		// add handler for color change
		class ColorHandler implements ChangeHandler {
			// fired when the user changes the selected value of the list
			public void onChange(ChangeEvent event) {
				// get old color
				String oldColorName = menu.getColor();
				int oldColor = 0; // default to black
				if (oldColorName.equals("white")) oldColor = 1;
				else if (oldColorName.equals("red")) oldColor = 2;
				
				// get new color
				int newColor = colorList.getSelectedIndex();
				
				// update JSON
				if (oldColor == newColor) return;
				else if (newColor == 0) menu.setColor("black");
				else if (newColor == 1) menu.setColor("white");
				else if (newColor == 2) menu.setColor("red");
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
		} // ColorHandler
		ColorHandler colorHandler = new ColorHandler();
		colorList.addChangeHandler(colorHandler);
		
		// second, construct a drop down box for editing the menu's template
		final ListBox tempList = new ListBox();
		tempList.addItem("classy"); // 0
		tempList.addItem("modern"); // 1
		navigationPan.add(new HTML("Template:"));
		navigationPan.add(tempList);
		navigationPan.add(new HTML("<br>"));

		// add handler for template change
		class TemplateHandler implements ChangeHandler {
			// fired when the user changes the selected value of the list
			public void onChange(ChangeEvent event) {
				// get old template
				String oldTemplateName = menu.getTemplate();
				int oldTemp = 0; // default to classy
				if (oldTemplateName.equals("modern")) oldTemp = 1;

				// get new template
				int newTemp = tempList.getSelectedIndex();

				// update JSON
				if (oldTemp == newTemp) return;
				else if (newTemp == 0) menu.setTemplate("classy");
				else if (newTemp == 1) menu.setTemplate("modern");
				StorageContainer.saveChange(menu);
				testLabel.setText(storage.getItem("menu"));
			}
		} // TemplateHandler
		TemplateHandler tempHandler = new TemplateHandler();
		tempList.addChangeHandler(tempHandler);
		
		// finally, if there's an internet connection, construct a refresh previewer button
		if (internet) {
			// finally, construct a refresh button button for the previewer
			final Button refreshButton = new Button("Refresh Preview");
			refreshButton.addStyleName("myButton");
			navigationPan.add(refreshButton);

			// add handler for refresh button
			class RefreshHandler implements ClickHandler {
				// fired when the user clicks go back
				public void onClick(ClickEvent event) {
					// check for internet connection again in case it was lost
					boolean internetStill = Communicate.hasInternet();
					if (internetStill) {
						previewer.setUrl("http://connoisseurmenu.appspot.com/preview?restaurant_id=" +
								storage.getItem("restID") + "&doc=" + URL.encode(storage.getItem(menu.getName())));
					}
					else {
						visEditorPan.remove(previewer);
						visEditorPan.add(nullPage);
						visEditorPan.setCellWidth(nullPage, "66.7%");
						navigationPan.remove(refreshButton);
						Dashboard.showNoInternetError();
					}
				}
			} // RefreshHandler
			final RefreshHandler refreshHandler = new RefreshHandler();
			refreshButton.addClickHandler(refreshHandler);
		}
		
		return visEditorPan;
	} // end getVisualEditor
	
} // VisualEditor
