package com.gwt.conn.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/** Used for storing buttonRow position dynamically. */
public class ButtonRow {
	private HorizontalPanel buttonRow; // a row of buttons
	private VerticalPanel page; // can modify categories or menu items through this page
	private int posn; // position in ArrayList
	public ButtonRow(HorizontalPanel buttonPanel, VerticalPanel pagePanel, int i) {
		buttonRow = buttonPanel;
		page = pagePanel;
		posn = i;
	}
	public HorizontalPanel getButtonRow() {
		return buttonRow;
	}
	public VerticalPanel getContentPage() {
		return page;
	}
	public int getPosition() {
		return posn;
	}
	public void setPosition(int i) {
		posn = i;
	}
} // ButtonRow