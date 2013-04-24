package networktest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection extends Thread {
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private ArrayList<ClientConnection> connections;

	public ClientConnection(Socket s, ArrayList<ClientConnection> connections) {
		super();
		this.s = s;
		this.connections = connections;
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String strIn = "";
			while ((strIn = in.readLine()) != null) {
				writeToAll(strIn);
			}
		} catch (IOException e) {
			System.out.println("Disconnected: " + s.getInetAddress().getHostAddress());
		}
	}
	
	public void write(String in) {
		out.println(in);
		out.flush();
	}
	
	public void writeToAll(String msg) {
		for (ClientConnection cc : connections) {
			cc.write(msg);
		}
	}
	
	public void closeConnection() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getHostName() {
		return s.getInetAddress().getHostName();
	}
}