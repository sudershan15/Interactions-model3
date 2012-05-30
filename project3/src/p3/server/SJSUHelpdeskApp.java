package p3.server;

import java.util.Properties;

/**
 * server application
 * 
 * @author Sudershan
 * 
 */
class SJSUHelpdeskApp {
	public SJSUHelpdeskApp() {
	}

	public static void main(String[] args) {
		/** TODO add arg parsing to set timeouts, port, ... */
		Properties p = new Properties();
		p.setProperty("port", "4100");
		String encodingStyle;
	// Read encoding style from command line
		encodingStyle = args[0];
		SJSUHelpdesk h2 = new SJSUHelpdesk(p);
		h2.start(encodingStyle);
	}
}
