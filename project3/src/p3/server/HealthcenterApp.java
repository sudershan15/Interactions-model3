package p3.server;

import java.util.Properties;

/**
 * server application
 * 
 * @author Sudershan
 * 
 */
class HealthcenterApp {
	public HealthcenterApp() {
	}

	public static void main(String[] args) {
		/** TODO add arg parsing to set timeouts, port, ... */
		Properties p = new Properties();
		p.setProperty("port", "2100");
		
		String encodingStyle;
		
		// Read encoding style from command line
		encodingStyle = args[0];
				
		Healthcenter h1 = new Healthcenter(p);
		h1.start(encodingStyle);
		
		
	}
}
