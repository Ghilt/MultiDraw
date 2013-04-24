package servermain;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import networktest.ClientConnection;

public class MultiDrawServer {
	private static ArrayList<ClientConnection> connections;

	public static void main(String[] args) {
		connections = new ArrayList<ClientConnection>();
		try {
			System.out.println("Server commenciated!");
			ServerSocket server = new ServerSocket(30001);
			
			Socket s;
			while ((s = server.accept()) != null) {
				System.out.println("Connected : " + s.getInetAddress().getHostAddress());
				ClientConnection cc = new ClientConnection(s, connections);
				cc.start();
				connections.add(cc);
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

