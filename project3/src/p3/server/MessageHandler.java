package p3.server;


public interface MessageHandler {
	void process(byte[] msg);
}
