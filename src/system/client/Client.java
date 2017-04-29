package system.client;

import java.io.*;
import java.net.*;

import gui.client.MainClientGUI;
import system.message.Message;

/**
 * A class representing a client in the chat room
 * 
 */
public class Client {
	
	String host;
	int port;
	InetAddress address;
	Socket socket;
	
	String username;
	MainClientGUI gui;
	
	ObjectInputStream in;
	ObjectOutputStream out;
	
	/**
	 * <p>
	 * Creates a new instance of this class, connected to the server denoted by
	 * the given host and port.
	 * </p>
	 * 
	 * @param host
	 * 			the host address of the server the client is connected to
	 * @param port
	 * 			the port number of the server the client is connected to
	 * @param username
	 * 			the client's username
	 */
	public Client(String host, int port, String username) {
		setup(host, port, username, null);		
	}
	
	public Client(String host, int port, String username, MainClientGUI gui) {
		setup(host, port, username, gui);		
	}
	
	void setup(String host, int port, String username, MainClientGUI gui) {
		try{
			this.host = host;
			this.port = port;
			this.username = username;
			this.gui = gui;
			address = InetAddress.getByName(host);

		} catch(Exception ex) {
			alert(ex);
		}
	}
	
	/**
	 * <p>
	 * Initializes the input and output streams of the class. Returns a boolean
	 * based on whether the setup was successful or not.
	 * </p>
	 * 
	 * @return true if the streams are successfully setup, otherwise false
	 */
	public boolean start() {
		try {
	
			socket = new Socket(address, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			//send the username to the server
			out.writeObject(new Message(username, Message.Type.USERNAME));
			
		} catch(Exception ex) {
			alert(ex);
			return false;
		}
		return true;
	}
	
	
	/**
	 * <p>
	 * Returns the socket associated with the client.
	 * </p>
	 * 
	 * @return the socket the client is connected to
	 */
	public Socket getSocket() {
		return socket;
	}
	
	public String getUsername() {
		return username;
	}

	/**
	 * <p>
	 * Closes the socket associated with the client.
	 * </p>
	 */
	void closeSocket() {
		try {
			socket.close();
			alert("Client deactivated");
		} catch(Exception ex) {}
	}
	
	/**
	 * <p>
	 * Displays the given string
	 * </p>
	 * 
	 * @param msg
	 * 			The message to be displayed
	 */
	void disp(String msg) {
		if(gui != null) {
			gui.updateChat(msg.toString());
		}
		System.out.println(msg);
	}
	
	/**
	 * <p>
	 * Displays the given message as a string
	 * </p>
	 * 
	 * @param msg
	 * 			The message to be displayed
	 */
	void disp(Message msg) {
		if(gui != null) {
			gui.updateChat(msg.toString());
		}
		System.out.println(msg);
	}
	
	/**
	 * <p>
	 * Displays the given string - if a GUI is being used, displays it as
	 * a popup window
	 * </p>
	 * 
	 * @param msg
	 * 			The message to be displayed
	 */
	void alert(String msg) {
		if(gui != null) {
			gui.showAlert(msg);
		}
		System.out.println(msg);
	}
	
	/**
	 * <p>
	 * Displays the given exception as a string - if a GUI is being used,
	 * displays it as a popup window
	 * </p>
	 * 
	 * @param ex
	 * 			The exception to be displayed
	 */
	void alert(Exception ex) {
		if (gui != null) {
			gui.showAlert("Error: " + ex);
		}
		System.out.println("Exception: " + ex + "\n" + ex.getStackTrace());
	}

	public static void main(String[] args) {
		Client client = new Client("localhost", 25000, "Anon");
		if (client.start()) {
			new Messenger(client).start();
			new ServerReader(client).start();
		}
		else {
			client.alert("Connection failed!");
		}

	}

}
