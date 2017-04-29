package system;
import java.io.*;
import java.net.*;

import system.message.Message;

/**
 * A thread representing a client's connection to a given server
 *
 */
public class Connection extends Thread {
	Server server;
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String username;
	boolean active = true;
	
	/**
	 * <p>
	 * Creates a new instance of this thread, associated with the given server
	 * </p>
	 * 
	 * @param server
	 * 			the server this connection is connected to
	 */
	public Connection(Server server) {	
		this.server = server;
		socket = server.getSocket();
		try{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
		} catch(Exception ex) {}
	}
	
	/**
	 * <p>
	 * Called when this thread is run
	 * </p>
	 * 
	 * <p>
	 * Scans for messages sent by a client, and distributes them to all
	 * connections
	 * </p>
	 */
	public void run() {
		try {			
			username = ((Message)in.readObject()).toString();
			server.disp(username + " has joined the server", "blue");
			server.messageConnections(new Message(username + " has joined the server", Message.Type.MESSAGE));
			
			while(active) {				
				Message input = (Message)in.readObject();
				
				if (input.getType().equals(Message.Type.LOGOUT)) {
					server.disp(username + " has left the server", "red");
					server.messageConnections(new Message(username + " has left the server", Message.Type.MESSAGE));
					server.users.remove(this);
					active = false;
				}
				else if (input.getType().equals(Message.Type.MESSAGE)) {
					server.disp(input.toString(), "black");
				
					server.messageConnections(input);
				}
			}
		} catch(Exception ex) {
			server.alert(ex);
		}
	}
	
	/**
	 * <p>
	 * Sends a message to the client associated with the current connection
	 * </p>
	 * @param msg
	 * 			the message to send to the client
	 */
	public void sendMessage(Message msg) {
		try{
		out.writeObject(msg);
		}catch(Exception ex) {}
	}
	
}
