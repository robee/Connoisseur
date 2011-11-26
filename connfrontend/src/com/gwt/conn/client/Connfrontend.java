package com.gwt.conn.client;

import com.google.gwt.core.client.EntryPoint;

/** Entry point classes define <code>onModuleLoad()</code>. */
public class Connfrontend implements EntryPoint {
	
	/** This is the entry point method. */
	public void onModuleLoad() {
		Authenticate.go();
	}
	
}
