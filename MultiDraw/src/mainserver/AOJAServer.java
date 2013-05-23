package mainserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.ServerConnection;
import utils.ServerState;

public class AOJAServer {

	
	public static void main(String[] args) {
	
		ServerState state = new ServerState();
		
		if (args.length < 1) {
			System.err.println("Usage: MultiDrawServer <port>");
			System.exit(1);
		}
		
		try {
			System.out.println("Server commenciated!");
			ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
			
			Socket s;
			while ((s = server.accept()) != null) {
				System.out.println("Connected : " + s.getInetAddress().getHostAddress());
				ServerConnection conn = new ServerConnection(s, state);
				conn.start();
				state.addConnection(conn);
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

