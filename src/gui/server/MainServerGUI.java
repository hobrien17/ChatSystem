package gui.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import system.Server;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class MainServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private StyledDocument log;
	private Map<String, Style> stylemap;
	private JTextPane serverLog;
	Server serv;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainServerGUI frame = new MainServerGUI();
					frame.setVisible(true);
					frame.new ServerThread().start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainServerGUI() {
		String inp = JOptionPane.showInputDialog("Please enter a port:", "25000");
		int port;
		try{
			port = Integer.parseInt(inp);
		} catch(NumberFormatException ex) {
			port = 25000;
		}
			
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 466, 327);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStopServer = new JButton("Stop Server");
		btnStopServer.setBounds(5, 271, 456, 29);
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serv.stop();
				System.exit(0);
			}
		});
		contentPane.add(btnStopServer);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 456, 254);
		contentPane.add(scrollPane);
		
		serverLog = new JTextPane();
		scrollPane.setViewportView(serverLog);
		serverLog.setEditable(false);
		log = serverLog.getStyledDocument();
		
		stylemap = new HashMap<>();
		Style red = serverLog.addStyle("red", null);
		Style blue = serverLog.addStyle("blue", null);
		Style green = serverLog.addStyle("green", null);
		Style black = serverLog.addStyle("black", null);
		StyleConstants.setForeground(red, Color.red);
		StyleConstants.setForeground(blue, Color.blue);
		StyleConstants.setForeground(green, Color.decode("#00BF00"));
		StyleConstants.setForeground(black, Color.black);
		stylemap.put("red", red); 
		stylemap.put("blue", blue);
		stylemap.put("green", green);
		stylemap.put("black", black);
		
		serv = new Server(port, this);
	}
	
	public void updateLog(String msg, String style) {
		try{
			log.insertString(log.getLength(), msg + "\n", stylemap.get(style));
		} catch(Exception ex) {}
		//serverLog.append(msg + "\n");
	}
	
	public void showAlert(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	class ServerThread extends Thread {
		public void run() {
			serv.start();
		}
	}

}
