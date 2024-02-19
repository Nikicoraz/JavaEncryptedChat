package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
	public Socket start() {
		try {
			ServerSocket ss = new ServerSocket(42069);
			Socket s = ss.accept();
			ss.close();
			return s;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
