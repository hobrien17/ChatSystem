package system.client;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import system.message.Message;

/**
 * A thread used to send messages to the server
 *
 */
public class Messenger extends Thread {
	Client client;
	ObjectOutputStream out;
	DateFormat dateFormat;
	public boolean active;
	
	/**
	 * <p>
	 * Creates a new instance of this thread, associated with the given client
	 * </p>
	 * 
	 * @param client
	 * 			the client the class is associated with - this is connected
	 * 			to the class' output stream
	 */
	public Messenger(Client client) {
		dateFormat = new SimpleDateFormat("HH:mm:ss");
		this.client = client;
		try {
			out = client.out;
		} catch(Exception ex) {}
	}
	
	/**
	 * <p>
	 * Called when this thread is run
	 * </p>
	 * 
	 * <p>
	 * Scans for messages inputted by the user
	 * </p>
	 */
	public void run() {
		active = true;
		Scanner scan = new Scanner(System.in);
		
		while(active) {
			client.disp(">>>");
			String msg = scan.nextLine();
			
			switch(msg.toUpperCase()) {
			case "LOGOUT": 
				active = false;
				break;
			default:
				sendMessage(msg, Message.Type.MESSAGE);	
			}
		}
		scan.close();
	}
	
	/**
	 * <p>
	 * Sends a message to the server
	 * </p>
	 * @param msg
	 * 			the message to be sent to the server
	 */
	public void sendMessage(String msg, Message.Type type) {
		try{
			Date date = new Date();
			String newmsg = String.format("%s [%s]: %s", client.username, dateFormat.format(date), msg.toString());
			out.writeObject(new Message(newmsg, type));
		}
		catch(Exception ex) {
			client.alert(ex);
		}
	}
}
