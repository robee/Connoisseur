package com.gwt.conn.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/** The Previewer Class **
 * 
 * This class renders the menu that is currently being edited in a
 * frame that fills the entire screen. It also loads a quit button
 * with an "x" in it so that the user can leave the previewer.
 * 
 */

public class Previewer extends StorageContainer {

	/** Called when the previewer needs to be used. */
	public void loadPreviewer(final DialogBox box) {
		// this widget allows web pages to be seen in an HTML iframe
		final Frame frame = new Frame();
		frame.getElement().setAttribute("style", "width:100%; height:100%; border:0");

		// this button closes the previewer
		final Button quitButton = new Button("x");
		quitButton.addStyleName("myButton");
		/*
		// validate menu id
		String menuid = storage.getItem("menuid");
		// still need to implement validation with server
		if (!FieldVerifier.isValidName(menuid)) {
			//errorLabel.setText("You submitted an invalid menu id.");
			return;
		}
		*/
		frame.setUrl("http://connoisseurmenu.appspot.com");
		RootPanel.get().add(frame, 0, 0);
		RootPanel.get().add(quitButton, 0, 0);

		// handler for quitButton
		class QuitHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				RootPanel.get().remove(frame);
				RootPanel.get().remove(quitButton);
				box.center();
			}
		}

		// attach the handler
		final QuitHandler quitHandler = new QuitHandler();
		quitButton.addClickHandler(quitHandler);
	} // end loadPreviewer

} // Previewer
