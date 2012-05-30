package p3.client;

import java.net.Socket;
import java.util.Properties;

/**
 * client chat
 * 
 * @author Sudershan
 * 
 */
public class Customer {
	private Properties setup;

	private Socket socket;
	private String name;

	/**
	 * empty constructor
	 */
	public Customer() {
	}

	/**
	 * specify the host and port to connect to
	 */
	public Customer(Properties setup) {
		this.setup = setup;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public Socket getSocket()
	{
		return socket;
	}

	/**
	 * connect to server
	 */
	public void startSession() {
		if (socket != null) {
			return;
		}

		String host = setup.getProperty("host");
		String port = setup.getProperty("port");
		if (host == null || port == null)
			throw new RuntimeException("Missing port and/or host");

		try {
			socket = new Socket(host, Integer.parseInt(port));
			if(port.equals("2100")){
				System.out.println("");
				System.out.println("\t\tHealth Center");
				System.out.println("");
				
			}else if(port.equals("4100")){
				System.out.println("");
				System.out.println("\t\tSJSU Helpdesk");
				System.out.println("");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
