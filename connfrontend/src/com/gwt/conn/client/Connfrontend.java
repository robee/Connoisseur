package com.gwt.conn.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/** Entry point classes define <code>onModuleLoad()</code>. */
public class Connfrontend implements EntryPoint {
	
	/** Create an instance of the editor class in order to get started. */
	private final Authenticate authenticate = GWT.create(Authenticate.class);
	
	/** This is the entry point method. */
	public void onModuleLoad() {
		authenticate.go();
	}
	
}
