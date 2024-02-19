package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sockets.Client;
import sockets.ConnectionType;
import sockets.Server;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ConnectFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField ipToConnectTo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectFrame frame = new ConnectFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void startChatGUI(Socket s, ConnectionType c) {
		new ChatGUI(s, c).setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dispose();
	}

	/**
	 * Create the frame.
	 */
	public ConnectFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton connectBtn = new JButton("Connect");
		JButton waitForConnectionBtn = new JButton("Wait for connection");
		waitForConnectionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Server events
				ipToConnectTo.setEnabled(false);
				connectBtn.setEnabled(false);
				waitForConnectionBtn.setEnabled(false);
				
				Server ss = new Server();
				new Thread(() -> {
					Socket s = ss.start();
					startChatGUI(s, ConnectionType.Passive);
				}).start();
			}
		});
		waitForConnectionBtn.setBounds(98, 168, 150, 23);
		contentPane.add(waitForConnectionBtn);
		
		ipToConnectTo = new JTextField();
		ipToConnectTo.setToolTipText("IP to connect to...");
		ipToConnectTo.setBounds(55, 79, 174, 20);
		contentPane.add(ipToConnectTo);
		ipToConnectTo.setColumns(10);
		
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Client events
				ipToConnectTo.setEnabled(false);
				connectBtn.setEnabled(false);
				waitForConnectionBtn.setEnabled(false);
				
				Client c = new Client();
				new Thread(() -> {
					Socket s = c.start(ipToConnectTo.getText());					
					startChatGUI(s, ConnectionType.Active);
				}).start();;
				
			}
		});
		connectBtn.setBounds(239, 78, 89, 23);
		contentPane.add(connectBtn);
		
		JLabel lblNewLabel = new JLabel("IP to connect to");
		lblNewLabel.setBounds(98, 54, 89, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("OR");
		lblNewLabel_1.setBounds(159, 130, 28, 14);
		contentPane.add(lblNewLabel_1);
	}
}
