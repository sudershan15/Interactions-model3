package p3.client;

import p3.common.AppProps;
import p3.common.JsonBuilder;
import p3.common.MessageBuilder;
import p3.common.StateBean;
import p3.common.XmlBuilder;
import p3.common.MessageBuilder.MessageType;
import p3.data.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * console interface to the socket example
 * 
 * @author Sudershan
 * 
 */
public class CustApp {
	private Properties setup;
	private static String encodingStyle; 
	private AppProps appProps;
	StateBean stateBean;
	MessageBuilder builder= new MessageBuilder();
	public CustApp(Properties setup) {
		this.setup = setup;
		appProps = getAppPropsInstance();
	}

	public void run() {
		DataOutputStream dataOp = null;
		DataInputStream dis = null;
		Customer bc = new Customer(setup);
		bc.startSession();
		bc.setName("customer");
		try {
			dis=new DataInputStream(bc.getSocket().getInputStream());
			dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
			encodingStyle = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean forever = true;
		String state ="1";
		while (forever) {
			try {
			
				if("1".equalsIgnoreCase(encodingStyle)) {
					if("1".equals(state)) {
						String msg = formMessage("1");
						System.out.println("Client : "+XmlBuilder.decode(msg));
						dataOp.writeUTF(msg);
						state ="2";
					} else {
						String msgFromServer = dis.readUTF();
						Message receivedMesg = (Message)XmlBuilder.decode(msgFromServer);
						state = receivedMesg.getState();  
						System.out.println("Helpdesk : "+receivedMesg.getPayload());
						state = receivedMesg.getState();
						if("3".equals(state)) {
							String msg = formMessage("3");
							dataOp.writeUTF(msg);
							System.out.println("Client : "+XmlBuilder.decode(msg));
						} else if("5".equals(state)) {
							Properties p = new Properties();
							p.setProperty("host", "127.0.0.1");
							p.setProperty("port", "4100");
							CustApp ca = new CustApp(p);
							ca.runSJSUOne();
							
						}
					} 
				}else if("2".equalsIgnoreCase(encodingStyle)){
					if("1".equals(state)) {
						String msg = formMessage("1");
						System.out.println("Client : "+JsonBuilder.decode(msg, Message.class));
						dataOp.writeUTF(msg);
						state="2";
					}else {
						String msgFromServer = dis.readUTF();
						Message receivedMesg = JsonBuilder.decode(msgFromServer, Message.class);
						state = receivedMesg.getState();  
						System.out.println("Helpdesk : "+receivedMesg.getPayload());
						if("3".equals(state)) {
							String msg = formMessage("3");
							dataOp.writeUTF(msg);
							System.out.println("Client : "+JsonBuilder.decode(msg, Message.class));
						} else if("5".equals(state)) {
							Properties p = new Properties();
							p.setProperty("host", "127.0.0.1");
							p.setProperty("port", "4100");
							CustApp ca = new CustApp(p);
							ca.runSJSUOne();
							
						}
					} 
				}else if("3".equalsIgnoreCase(encodingStyle)){
					if("1".equals(state)) {
						String msg = formMessage("1");
						List<Message> list = builder.decode(msg.getBytes());
						for (Message message : list){
						System.out.println("Client : " +message.getPayload());
						}
						dataOp.writeUTF(msg);
						state="2";
					}else {
						String msgFromServer = dis.readUTF();
						List<Message> list = builder.decode(msgFromServer.getBytes());
						for (Message message : list){
						state = message.getSource();
						System.out.println("Helpdesk : " +message.getPayload());
						}
						if("3".equals(state)) {
							String msg = formMessage("3");
							dataOp.writeUTF(msg);
							
							List<Message> list1 = builder.decode(msg.getBytes());
							for (Message message : list1){
								
								state = message.getSource();  
								System.out.println("Client : "+message.getPayload());
							}
							
						} else if("5".equals(state)) {
							Properties p = new Properties();
							p.setProperty("host", "127.0.0.1");
							p.setProperty("port", "4100");
							CustApp ca = new CustApp(p);
							ca.runSJSUOne();
							
						}
					} 
				}
				
			} catch (Exception e) {
				forever = false;
				e.printStackTrace();
			}
		}

	}
public void runSJSUOne() {
		DataOutputStream dataOp = null;
		DataInputStream dis = null;
		Customer bc = new Customer(setup);
		bc.startSession();
		bc.setName("customer");
		try {
			dis=new DataInputStream(bc.getSocket().getInputStream());
			dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
			encodingStyle = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean forever = true;
		String state ="5";
		while (forever) {
			
			try {
				
				 if("1".equalsIgnoreCase(encodingStyle)){
					 if("5".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("5");
							System.out.println("Client : "+XmlBuilder.decode(msg));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = (Message)XmlBuilder.decode(msgFromServer);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
						}else if("7".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("7");
							System.out.println("Client : "+XmlBuilder.decode(msg));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = (Message)XmlBuilder.decode(msgFromServer);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
						}else if("9".equals(state)) {
							Properties p = new Properties();
							p.setProperty("host", "127.0.0.1");
							p.setProperty("port", "2100");
							CustApp ca = new CustApp(p);
							ca.nf();
							}else{
							String msgFromServer = dis.readUTF();
							Message receivedMesg = (Message)XmlBuilder.decode(msgFromServer);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
							state = receivedMesg.getState();
						}
				 }else if("2".equalsIgnoreCase(encodingStyle)){
					 if("5".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("5");
							System.out.println("Client : "+JsonBuilder.decode(msg, Message.class));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = JsonBuilder.decode(msgFromServer, Message.class);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
					}if("7".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("7");
							System.out.println("Client : "+JsonBuilder.decode(msg, Message.class));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = JsonBuilder.decode(msgFromServer, Message.class);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
					}else if("9".equals(state)) {
						Properties p = new Properties();
						p.setProperty("host", "127.0.0.1");
						p.setProperty("port", "2100");
						CustApp ca = new CustApp(p);
						ca.nf();
						}else{
							String msgFromServer = dis.readUTF();
							Message receivedMesg = JsonBuilder.decode(msgFromServer, Message.class);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
							state = receivedMesg.getState();
							break;
							
						}
				 }else if("3".equalsIgnoreCase(encodingStyle)){
					 if("5".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("5");
							List<Message> list1 = builder.decode(msg.getBytes());
							for (Message message : list1){
								state = message.getSource();  
								System.out.println("Client : "+message.getPayload());
							}
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							List<Message> list2 = builder.decode(msgFromServer.getBytes());
							for (Message message : list2){
								state = message.getSource();  
								System.out.println("Helpdesk : "+message.getPayload());
							}
						}else if("7".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("7");
							List<Message> list1 = builder.decode(msg.getBytes());
							for (Message message : list1){
								
								state = message.getSource();  
								System.out.println("Client : "+message.getPayload());
							}
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							List<Message> list2 = builder.decode(msgFromServer.getBytes());
							for (Message message : list2){
								state = message.getSource();  
								System.out.println("Helpdesk : "+message.getPayload());
							}
						}else if("9".equals(state)) {
							Properties p = new Properties();
							p.setProperty("host", "127.0.0.1");
							p.setProperty("port", "2100");
							CustApp ca = new CustApp(p);
							ca.nf();
							}else{
							String msgFromServer = dis.readUTF();
							List<Message> list2 = builder.decode(msgFromServer.getBytes());
							for (Message message : list2){
								state = message.getSource();  
								System.out.println("Helpdesk : "+message.getPayload());
							}
							break;
						}
				 }

			} catch (Exception e) {
				forever = false;
				e.printStackTrace();
			}
		}
	}
	
	public void nf(){
		DataOutputStream dataOp = null;
		DataInputStream dis = null;
		Customer bc = new Customer(setup);
		bc.startSession();
		bc.setName("customer");
		try {
			dis=new DataInputStream(bc.getSocket().getInputStream());
			dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
			encodingStyle = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean forever = true;
		String state ="9";
		while (forever) {
			
			try {
				 if("1".equalsIgnoreCase(encodingStyle)){
					 if("9".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("9");
							System.out.println("Client : "+XmlBuilder.decode(msg));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = (Message)XmlBuilder.decode(msgFromServer);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
						}else if("11".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("11");
							System.out.println("Client : "+XmlBuilder.decode(msg));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = (Message)XmlBuilder.decode(msgFromServer);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
						}
				 }else if("2".equalsIgnoreCase(encodingStyle)){
					 if("9".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("9");
							System.out.println("Client : "+JsonBuilder.decode(msg, Message.class));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = JsonBuilder.decode(msgFromServer, Message.class);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
					}if("11".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("7");
							System.out.println("Client : "+JsonBuilder.decode(msg, Message.class));
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							Message receivedMesg = JsonBuilder.decode(msgFromServer, Message.class);
							state = receivedMesg.getState();  
							System.out.println("Helpdesk : "+receivedMesg.getPayload());
					}
				 }else if("3".equalsIgnoreCase(encodingStyle)){
					 if("9".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("9");
							List<Message> list1 = builder.decode(msg.getBytes());
							for (Message message : list1){
								state = message.getSource();  
								System.out.println("Client : "+message.getPayload());
							}
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							List<Message> list2 = builder.decode(msgFromServer.getBytes());
							for (Message message : list2){
								state = message.getSource();  
								System.out.println("Helpdesk : "+message.getPayload());
							}
						}else if("11".equals(state)) {
							dataOp = new DataOutputStream(bc.getSocket().getOutputStream());
							String msg = formMessage("11");
							List<Message> list1 = builder.decode(msg.getBytes());
							for (Message message : list1){
								
								state = message.getSource();  
								System.out.println("Client : "+message.getPayload());
							}
							dataOp.writeUTF(msg);
							String msgFromServer = dis.readUTF();
							List<Message> list2 = builder.decode(msgFromServer.getBytes());
							for (Message message : list2){
								state = message.getSource();  
								System.out.println("Helpdesk : "+message.getPayload());
							}
						}
				 }
			}catch (Exception e) {
				forever = false;
				e.printStackTrace();
			}
		}
		
	}
	public AppProps getAppPropsInstance() {
		AppProps appProps = AppProps.getInstance("c:/messageexchanges.properties");
		return appProps;
	}
	
	public String formMessage(String state) throws Exception {
		Message data = new Message();
		// Read payload for the first message
		int stateVal =0;
		String payload = appProps.getStringValue(state);
		data.setPayload(payload);
		data.setSource("");
		data.setType(MessageType.join);
		stateVal = Integer.parseInt(state);
		stateVal++;
		state = Integer.toString(stateVal);
		data.setState(state);
		String msg=null;
		if(encodingStyle.equals("1")){
				msg = XmlBuilder.encode(data);
				
		}else if(encodingStyle.equals("2")){
			msg=JsonBuilder.encode(data);
		}else if(encodingStyle.equals("3")){
			
			msg= builder.encode(MessageType.join, state, payload, null);
		}
		
		return msg;
	}
	
	public static void main(String[] args) {
		Properties p = new Properties();
		p.setProperty("host", "127.0.0.1");
		p.setProperty("port", "2100");

		CustApp ca = new CustApp(p);
		ca.run();
	}
}
