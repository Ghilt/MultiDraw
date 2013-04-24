package network;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread {
	private Socket s;
	private PrintWriter out;
	private SendBuffer buffer;

	public ClientSender(Socket s, SendBuffer buffer) {
		this.s = s;
		this.buffer = buffer;
	}

	public void run() {
		System.out.println("ClientSender running...");
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			while (true) {
				out.println(buffer.pop());
				out.flush();
			}
		} catch (IOException e) {
			System.out.println("Connection error, bailing out...");
		}
	}
}