package com.connoisseur.menuapp.server;
//UNUSED
import com.connoisseur.menuapp.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/** The server side implementation of the RPC service. */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		String test = ServerFieldVerifier.isValidLicenseKey(input); // verify that the input is valid
		if (!test.equals("")) {
			// if the input is not valid, throw an IllegalArgumentException back to the client
			throw new IllegalArgumentException(test);
		}
		
		// escape data from the client to avoid cross-site script vulnerabilities
		input = escapeHtml(input);

		return "";
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
