package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sockets.ConnectionType;

public class ChatGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ConnectionType cType;
	private Socket s;
	private BufferedReader br;
	private PrintWriter bw;
	private char[] privateKey;
	private char[] sharedMessageKey;
	
	private void exchangeKeys() {
		privateKey = encryption.Keys.generateKey();
		System.out.println((cType == ConnectionType.Active ? "Server" : "Client") + ": Generated private key " + new String(privateKey));
		if(cType == ConnectionType.Active) {
			try {
				sharedMessageKey = encryption.Keys.generateKey();
				System.out.println("Server: Generated new shared message key " + new String(sharedMessageKey));
				
				char[] privateEncryptedMessageKey = encryption.Keys.NEncryption(sharedMessageKey, privateKey);
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
				
				char[] sharedKey = encryption.Keys.NDecryption(receivedDecryptedKey, privateKey);
				System.out.println("Client: Shared key is " + new String(sharedKey));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	/**
	 * Create the frame.
	 */
	public ChatGUI(Socket s, ConnectionType cType) {
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		exchangeKeys();
	}

}
