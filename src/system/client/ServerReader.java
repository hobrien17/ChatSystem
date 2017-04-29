package system.client;

import java.io.ObjectInputStream;

import system.message.Message;

/**
 * A thread used to scan for and display any messages given by the server
 *
 */
public class ServerReader extends Thread {

	Client client;
	ObjectInputStream in;
	
	/**
	 * <p>
	 * Creates a new instance of this thread, associated with the given client
	 * </p>
	 * @param client
	 * 			the client the class is associated with - this is connected
	 * 			to the class' input stream
	 */
	public ServerReader(Client client) {
		this.client = client;
		try {
			in = client.in;
		} catch(Exception ex) {}
	}
	
	/**
	 * <p>
	 * Called when this thread is run
	 * </p>
	 * 
	 * <p>
	 * Receives and displays any messages given by the server
	 * </p>
	 */
	public void run() {
		boolean active = true;
		while(active) {
			try {
				Message msg = (Message)in.readObject();
				if (msg.getType().equals(Message.Type.MESSAGE)) {
					client.disp(msg);
				}
				else if (msg.getType().equals(Message.Type.END)) {
					client.alert(msg.toString());
					active = false;
					client.gui.sendEndSignal();
				}
			} catch(Exception ex) {}
		}
	}

}
