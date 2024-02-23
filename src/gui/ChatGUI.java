package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sockets.ConnectionType;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.CompoundBorder;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ConnectionType cType;
	private Socket s;
	private BufferedReader br;
	private PrintWriter bw;
	private int[] privateKey;
	private int[] sharedMessageKey;
	private JTextPane chatPanel;
	
	private void addTextToChatPanel(String text) {
		chatPanel.setText(chatPanel.getText() + text + "\n");
	}
	
	private void startMessageListener() {
		new Thread(() -> {
			while(true) {
				try {
					char[] message = br.readLine().toCharArray();
					message = encryption.Keys.NDecryption(message, sharedMessageKey);
					addTextToChatPanel(new String(message));
				}catch(NullPointerException | SocketException e) {
					chatPanel.setEnabled(false);
					addTextToChatPanel("Disconnected");
					break;
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void exchangeKeys() {
		privateKey = encryption.Keys.generateKey();
		System.out.println((cType == ConnectionType.Active ? "Server" : "Client") + ": Generated private key " + new String(encryption.Keys.keyToChar(privateKey)));
		if(cType == ConnectionType.Active) {
			try {
				sharedMessageKey = encryption.Keys.generateKey();
				System.out.println("Server: Generated new shared message key " + new String(encryption.Keys.keyToChar(sharedMessageKey)));
				
				char[] privateEncryptedMessageKey = encryption.Keys.NEncryption(encryption.Keys.keyToChar(sharedMessageKey), privateKey);
				System.out.println("Server: Sending encrypted shared key " + new String(privateEncryptedMessageKey));
				
				bw.println(new String(privateEncryptedMessageKey));
	
				char[] receivedEncryptedMessageKey;
				receivedEncryptedMessageKey = br.readLine().toCharArray();
				System.out.println("Server: Received encrypted shared key " + new String(receivedEncryptedMessageKey));
				
				char[] decryptedReceivedMessageKey = encryption.Keys.NDecryption(receivedEncryptedMessageKey, privateKey);
				bw.println(new String(decryptedReceivedMessageKey));
				System.out.println("Server: Sending decrypted shared key " + new String(decryptedReceivedMessageKey));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			try {
				char[] encryptedMessageKey;
				encryptedMessageKey = br.readLine().toCharArray();
				System.out.println("Client: Received encrypted key " + new String(encryptedMessageKey));
				
				char[] privateEncryptedMessageKey = encryption.Keys.NEncryption(encryptedMessageKey, privateKey);
				bw.println(new String(privateEncryptedMessageKey));
				System.out.println("Client: Sending encrypted key " + new String(privateEncryptedMessageKey));
				
				char[] receivedDecryptedKey = br.readLine().toCharArray();
				System.out.println("Client: Received the decrypted key " + new String(receivedDecryptedKey));
				
				sharedMessageKey = encryption.Keys.keyToInt(encryption.Keys.NDecryption(receivedDecryptedKey, privateKey));
				System.out.println("Client: Shared key is " + new String(encryption.Keys.keyToChar(sharedMessageKey)));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		startMessageListener();
	}
	
	
	/**
	 * Create the frame.
	 */
	public ChatGUI(Socket s, ConnectionType cType) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					s.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.s = s;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.cType = cType;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 465, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea message = new JTextArea();
		message.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		message.setBounds(10, 224, 347, 27);
		contentPane.add(message);
		
		JButton sendMessage = new JButton("Invia");
		sendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(message.getText().strip() != "") {
					bw.println(new String(encryption.Keys.NEncryption((s.getInetAddress() + ":" + s.getLocalPort() + ": " + message.getText()).toCharArray(), sharedMessageKey)));
					addTextToChatPanel(s.getInetAddress() + ":" + s.getLocalPort() + ": " + message.getText());
					message.setText("");					
				}
			}
		});
		sendMessage.setBounds(367, 223, 67, 27);
		contentPane.add(sendMessage);
		
		chatPanel = new JTextPane();
		chatPanel.setEditable(false);
		chatPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		chatPanel.setBounds(10, 11, 424, 201);
		contentPane.add(chatPanel);
		
		message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					sendMessage.doClick();
				}
			}
		});
		
		exchangeKeys();
	}
}
