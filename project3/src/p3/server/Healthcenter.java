package p3.server;

import p3.common.MessageBuilder;
import p3.common.MessageBuilder.MessageType;
import p3.common.AppProps;
import p3.common.JsonBuilder;
import p3.common.StateBean;
import p3.common.XmlBuilder;
import p3.data.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * server to manage incoming clients
 * 
 * @author Sudershan
 * 
 */
public class Healthcenter {
	private Properties setup;
	private ServerSocket socket;
	private long idCounter = 1;
	private boolean forever = true;
	private ArrayList<SessionHandler> connections = new ArrayList<SessionHandler>();
	private String encodingStyle;
	private AppProps appProps;
	Socket clientSocketAtServer = null;
	StateBean stateBean = StateBean.getInstance();
	MessageBuilder builder= new MessageBuilder();
	public Healthcenter() {
	}

	/**
	 * construct a new server listening on the specified port
	 */
	public Healthcenter(Properties setup) {
		this.setup = setup;
		appProps = getAppPropsInstance();
	}
	/**
	 * start monitoring socket for new connections
	 */
	public void start(String encodingStyle) {
		// Assign input encoding style to member variable
		this.encodingStyle = encodingStyle;
		if (setup == null)
			throw new RuntimeException("Missing configuration properties");
		try {
			int port = Integer.parseInt(setup.getProperty("port"));
			socket = new ServerSocket(port);
			System.out.println("\t\tHealth Center Helpdesk");
			System.out.println("");
			System.out.println("");
			while (forever) {
				Socket s = socket.accept();
				if (!forever) {
					break;
				}
				System.out.println("Client Enters health center");
				System.out.println("");
				System.out.println("");
				System.out.flush();
				SessionHandler sh = new SessionHandler(s, idCounter++);
				connections.add(sh);
				// Sending encoding style to client
				sh.sendEncodingStyle(encodingStyle);
				sh.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void stopSessions() {
		for (SessionHandler sh : connections) {
			sh.stopSession();
		}

		connections = null;
		forever = false;
	}

	/**
	 * 
	 * @author gash
	 * 
	 */
	public class SessionHandler extends Thread {
		private Socket connection;
		private long id;
		private String name;
		private long lastContact;
		private boolean forever = true;
		private int timeout = 10 * 1000; // 10 seconds
		DataInputStream is = null;
		DataOutputStream os =null;
		Message data = null;
		
		public SessionHandler(Socket connection, long id) {
			this.connection = connection;
			this.id = id;
		}

		/**
		 * stops session on next timeout cycle
		 */
		public void stopSession() {
			forever = false;
			if (connection != null) {
				try {
					removeSession();
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			connection = null;
		}

		public long getSessionId() {
			return id;
		}

		public long getLastContact() {
			return lastContact;
		}

		public void setTimeOut(int v) {
			timeout = v;
		}

		public void setSessionName(String n) {
			name = n;
		}

		public String getSessionName() {
			return name;
		}

		/**
		 * process incoming data
		 */
		public void run() {
			try {
				connection.setSoTimeout(timeout);
				is = new DataInputStream(connection.getInputStream());
				os = new DataOutputStream(connection.getOutputStream());				
					while(forever) {
						try {
							
							if("1".equalsIgnoreCase(encodingStyle)) {
								String msgFromClient = is.readUTF();
								Message receivedMesg = (Message)XmlBuilder.decode(msgFromClient);
								System.out.println("Client : "+receivedMesg.getPayload());
								String state = receivedMesg.getState();  
								if("2".equals(state)) {
									String msg = formMessage("2");
									System.out.println("Helpdesk : "+ XmlBuilder.decode(msg));
									os.writeUTF(msg);
								}else if("4".equals(state)) {
									String msg = formMessage("4");
									System.out.println("Helpdesk : "+XmlBuilder.decode(msg));
									os.writeUTF(msg);
								}else if("10".equals(state)) {
									String msg = formMessage("10");
									System.out.println("Helpdesk : "+XmlBuilder.decode(msg));
									os.writeUTF(msg);
								} 
			
							}else if("2".equalsIgnoreCase(encodingStyle)){
								String msgFromClient = is.readUTF();
								Message receivedMesg = JsonBuilder.decode(msgFromClient, Message.class);
								System.out.println("Client : "+receivedMesg.getPayload());
								String state = receivedMesg.getState();  
								if("2".equals(state)) {
									String msg = formMessage("2");
									System.out.println("Helpdesk : "+JsonBuilder.decode(msg, Message.class));
									os.writeUTF(msg);
								} else if("4".equals(state)) {
									String msg = formMessage("4");
									System.out.println("Helpdesk : "+JsonBuilder.decode(msg, Message.class));
									os.writeUTF(msg);
								} else if("10".equals(state)) {
									String msg = formMessage("10");
									System.out.println("Helpdesk : "+JsonBuilder.decode(msg, Message.class));
									os.writeUTF(msg);
								} 
							}else if("3".equalsIgnoreCase(encodingStyle)){
								String msgFromClient = is.readUTF();
								String state=null;
								List<Message> list = builder.decode(msgFromClient.getBytes());
								for (Message message : list){
									System.out.println("Client : " +message.getPayload());
									state = message.getSource();  
								}
								
								if("2".equals(state)) {
									String msg = formMessage("2");
									List<Message> list1 = builder.decode(msg.getBytes());
									for (Message message : list1){
										System.out.println("Helpdesk : "+message.getPayload());
										state = message.getSource();  
									}os.writeUTF(msg);
								} else if("4".equals(state)) {
									String msg = formMessage("4");
									List<Message> list2 = builder.decode(msg.getBytes());
									for (Message message : list2){
										System.out.println("Helpdesk : "+message.getPayload());
										state = message.getSource();  
									}os.writeUTF(msg);
								} else if("10".equals(state)) {
									String msg = formMessage("10");
									List<Message> list2 = builder.decode(msg.getBytes());
									for (Message message : list2){
										System.out.println("Helpdesk : "+message.getPayload());
										state = message.getSource();  
									}os.writeUTF(msg);
								} 
							}
							
						} catch (Exception e) {
							
						}
					}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					System.out.println("Session " + (name == null ? "" : name)
							+ " [" + id + "] exiting");
					System.out.flush();
					stopSession();
				} catch (Exception re) {
					re.printStackTrace();
				}
			}
		}

		/**
		 * send message to all connections
		 * 
		 * @param msg
		 *            String
		 * @throws Exception
		 */
		private synchronized void sendEncodingStyle(String encodingStyle) throws Exception {
			for (SessionHandler sh : connections) {
				
				DataOutputStream dos=new DataOutputStream(sh.connection.getOutputStream());
				dos.writeUTF(encodingStyle);
			}
		}
		
		/**
		 * remove connection
		 * 
		 * @param msg
		 *            String
		 * @throws Exception
		 */
		private synchronized void removeSession() throws Exception {
			connections.remove(this);
		}

	} // class SessionHandler

	/**
	 * 
	 * @author gash
	 * 
	 */
	public class MonitorSessions extends Thread {
		private boolean forever = true;

		private long interval;

		private long idleTime;

		/**
		 * create a new monitor
		 * 
		 * @param interval
		 *            long how often to check
		 * @param idleness
		 *            long what is considered idle
		 */
		public MonitorSessions(long interval, long idleness) {
			this.interval = interval;
			this.idleTime = idleness;
		}

		/**
		 * stop monitoring on the next interval
		 */
		public void stopMonitoring() {
			forever = false;
		}

		/**
		 * ran in the thread to monitor for idle threads
		 */
		public void run() {
			while (forever) {
				try {
					long idle = System.currentTimeMillis() - idleTime;
					Thread.sleep(interval);
					if (!forever) {
						break;
					}

					for (SessionHandler sh : connections) {
						if (sh.getLastContact() < idle) {
							System.out
									.println("MonitorSessions stopping session "
											+ sh.getSessionId());

							sh.stopSession();
							connections.remove(sh);
						}
					}
				} catch (Exception e) {
					break;
				}
			}
		}
	} // class MonitorSessions

	public AppProps getAppPropsInstance() {
		//call this function once globally only..
		AppProps appProps = AppProps.getInstance("c:/messageexchanges.properties");

		return appProps;
	}
	
	public String formMessage(String state) {
			Message data = new Message();
			int stateVal =0;
		// Read payload for the first message
		String payload = appProps.getStringValue(state);
		data.setPayload(payload);
		data.setSource("");
		stateVal = Integer.parseInt(state);
		stateVal++;
		state = Integer.toString(stateVal);
		data.setState(state);
		String msg =null;
		if("1".equalsIgnoreCase(encodingStyle)){
			msg = XmlBuilder.encode(data);
		}else if("2".equalsIgnoreCase(encodingStyle)){
			msg=JsonBuilder.encode(data);
		}else if("3".equalsIgnoreCase(encodingStyle)){
			msg= builder.encode(MessageType.join, state, payload, null);
	}
		return msg;
	}
}
