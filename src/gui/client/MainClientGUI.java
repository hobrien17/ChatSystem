package gui.client;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import system.client.Client;
import system.client.Messenger;
import system.client.ServerReader;
import system.message.Message;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;

public class MainClientGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextArea chatWindow;
	private JButton btnSubmit;
	
	private String username;
	private int port;
	private String host;
	private Client client;
	private Messenger messenger;
	private JScrollPane scrollPane;
	private JScrollBar vertical;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClientGUI frame = new MainClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainClientGUI() {
		username = JOptionPane.showInputDialog("Please enter a username:");
		if (username == null || username.equals("")) {
			username = "Anon";
		}
		
		host = JOptionPane.showInputDialog("Please enter a host to connect to:", "localhost");
		if (host == null) {
			host = "localhost";
		}
		
		String inpPort = JOptionPane.showInputDialog("Please enter a port:", "25000");
		try{
			port = Integer.parseInt(inpPort);
		} catch(NumberFormatException ex) {
			port = 25000;
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 319);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblStatus = new JLabel("Disconnected from Server");
		lblStatus.setBounds(16, 6, 411, 16);
		contentPane.add(lblStatus);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 25, 411, 228);
		contentPane.add(scrollPane);
		
		chatWindow = new JTextArea();
		scrollPane.setViewportView(chatWindow);
		chatWindow.setEditable(false);
		
		vertical = scrollPane.getVerticalScrollBar();
		
		textField = new JTextField();
		textField.setBounds(17, 265, 352, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnSubmit = new JButton(">>>");
		btnSubmit.setBounds(369, 265, 58, 29);
		contentPane.add(btnSubmit);
		
		//create client
		client = new Client(host, port, username, this);
		if(client.start()) {
			messenger = new Messenger(client);
			new ServerReader(client).start();
			lblStatus.setText("Connected as " + username + " to server " + host + " at port " + port);
		}
		else{
			showAlert("Connection failed");
		}
		
		//detect submission
		Action submit = new AbstractAction()
		{
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public void actionPerformed(ActionEvent e)
		    {
		        String text = textField.getText();
		        if (text.equals("LOGOUT")) {
		        	messenger.sendMessage(text, Message.Type.LOGOUT);
		        	System.exit(0);
		        }
		        else {
		        	messenger.sendMessage(text, Message.Type.MESSAGE);
		        }
		        textField.setText("");
		    }
		};
		
		textField.addActionListener(submit);
		btnSubmit.addActionListener(submit);
	}
	
	public void updateChat(String msg) {
		chatWindow.append(msg + "\n");
		vertical.setValue(vertical.getMaximum());
	}
	
	public void sendEndSignal() {
		messenger.active = false;
		textField.setEnabled(false);
		btnSubmit.setEnabled(false);
	}
	
	public void showAlert(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
