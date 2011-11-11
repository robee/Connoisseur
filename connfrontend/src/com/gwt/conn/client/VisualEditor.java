package com.gwt.conn.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/** The Visual Editor Class **
 * 
 * 
 * 
 */

public class VisualEditor extends StorageContainer {
	
	/** Called when the visual editor needs to be used. */
	public void loadVisualEditor(final DialogBox dashboardBox, String json) {
		final DialogBox d = new DialogBox();
		final VerticalPanel v = new VerticalPanel();
		final HTML h = new HTML();
		//final Button b = new Button(json);
		h.setHTML(json);
		v.add(h);
		//v.add(b);
		d.setWidget(v);
		d.show();
		dashboardBox.center();
	} // end loadVisualEditor
	
} // VisualEditor
