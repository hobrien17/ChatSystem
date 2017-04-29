package system;

import java.net.*; 
import java.util.*;

import gui.server.MainServerGUI;
import system.message.Message;

/**
 * A class used to create a chatroom server
 *
 */
public class Server {
	
	private Socket socket;
	int port;
	ServerSocket serverSocket;
	MainServerGUI gui;
	
	List<Connection> users;
	
	public boolean active;
	
	/**
	 * <p>
	 * Creates a new server at the given port
	 * </p>
	 * 
	 * @param gui
	 * 			the GUI being used
	 * @param port
	 * 			the port to run the server from
	 */
	public Server(int port, MainServerGUI gui) {
		setup(port, gui);		
	}
	
	/**
	 * <p>
	 * Creates a new server at the given port
	 * </p>
	 * 
	 * @param port
	 * 			the port to run the server from
	 */
	public Server(int port) {
		setup(port, null);
	}
	
	/**
	 * Sets up the server
	 * 
	 * @param gui
	 * 			the GUI being used (use null if no GUI is being used)
	 * @param port
	 * 			the port to run the server from
	 */
	void setup(int port, MainServerGUI gui) {
		try {
			this.gui = gui;
			this.port = port;
			serverSocket = new ServerSocket(port);
			users = new ArrayList<>();
			disp("Server started successfully", "green");
		} catch (Exception ex) {
			alert(ex);
		}
		
	}

	/**
	 * <p>
	 * Returns the server's socket
	 * </p>
	 * 
	 * @return the socket the server is connected to
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * <p>
	 * Starts the server and listens for connections
	 * </p>
	 */
	public void start() {

		try {
			active = true;
			while(active) {
				socket = serverSocket.accept();
				Connection c = new Connection(this);
				users.add(c);
				c.start();
			}
		} catch(Exception ex) {
			alert(ex);
		}
	}
	
	public void stop() {
		try{
			messageConnections(new Message("Server has shut down", Message.Type.END));
			active = false;
			for (Connection c : users) {
				c.active = false;
			}
			socket.close();
		} catch(Exception ex) {
			alert(ex);
		}
	}
	
	/**
	 * <p>
	 * Invokes the sendMessage method on all connections
	 * </p>
	 * 
	 * @param msg
	 * 			the message to send to all connections
	 */
	public void messageConnections(Message msg) {
		System.out.println("Sending message: " + msg);
		for (Connection c : users) {
			c.sendMessage(msg);
		}
	}
	
	public void removeConnection(Connection cn) {
		users.remove(cn);
	}
	
	/**
	 * <p>
	 * Displays the given string
	 * </p>
	 * 
	 * @param msg
	 * 			The message to be displayed
	 */
	void disp(String msg, String style) {
		if (gui != null) {
			gui.updateLog(msg, style);
		}
		else {
			System.out.println(msg);
		}
		
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
		if (gui != null) {
			gui.showAlert(msg);
		}
		else {
			System.out.println(msg);
		}
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
		else {
			System.out.println("Exception: " + ex + "\n" + ex.getStackTrace());
		}
	}

	public static void main(String[] args) {
		Server server = new Server(25000);
		server.start();
	}

}
