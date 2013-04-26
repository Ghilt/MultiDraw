package mainserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import utils.ImageWrapper;

import network.ServerConnection;

public class MultiDrawServer {
	private static ArrayList<ServerConnection> connections;
	private static ImageWrapper image;
	
	public static void main(String[] args) {
		connections = new ArrayList<ServerConnection>();
		image = new ImageWrapper(900, 780);
		try {
			System.out.println("Server commenciated!");
			ServerSocket server = new ServerSocket(30002);
			
			Socket s;
			while ((s = server.accept()) != null) {
				System.out.println("Connected : " + s.getInetAddress().getHostAddress());
				ServerConnection conn = new ServerConnection(s, connections, image);
				conn.start();
				connections.add(conn);
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

