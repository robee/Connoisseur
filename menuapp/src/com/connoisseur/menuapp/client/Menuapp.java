package com.connoisseur.menuapp.client;

import com.google.gwt.core.client.EntryPoint;

/** Entry point classes define <code>onModuleLoad()</code>. */
public class Menuapp implements EntryPoint {
	
	/** This is the entry point method. */
	public void onModuleLoad() {
		Authenticate.go();
	}
	
}
