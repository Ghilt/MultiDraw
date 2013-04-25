package mainserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import network.ServerConnection;

public class MultiDrawServer {
	private static ArrayList<ServerConnection> connections;

	public static void main(String[] args) {
		connections = new ArrayList<ServerConnection>();
		try {
			System.out.println("Server commenciated!");
			ServerSocket server = new ServerSocket(30001);
			
			Socket s;
			while ((s = server.accept()) != null) {
				System.out.println("Connected : " + s.getInetAddress().getHostAddress());
				ServerConnection conn = new ServerConnection(s, connections);
				conn.start();
				connections.add(conn);
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

