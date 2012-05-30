package p3.data;

import p3.common.MessageBuilder.MessageType;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Message {
	private MessageType type;
	private String source;
	private Date received;
	private String payload;
	private String state;
	
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String body) {
		this.payload = body;
	}
	
	public String toString(){
		return this.source.concat(this.payload);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
