package network;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender extends Thread {
	private Socket s;
	private OutputStream out;
	private SendBuffer buffer;

	public ClientSender(Socket s, SendBuffer buffer) {
		this.s = s;
		this.buffer = buffer;
	}

	public void run() {
		System.out.println("ClientSender running...");
		try {
			out = s.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			while (true) {
				writer.println(buffer.pop());
				writer.flush();
			}
		} catch (IOException e) {
			System.out.println("Connection error, bailing out...");
		}
	}
	
	public void sendFile() {
		try {
			File file = buffer.popFile();
			System.out.println("Client has started sending file: " + file);
	
			int bytesToSend = (int) file.length();
			byte[] mybytearray = new byte[bytesToSend];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(mybytearray, 0, mybytearray.length);
			OutputStream os = s.getOutputStream();
			System.out.println("Trying to sen it all in one go! nbr of bytes " + file.length());
			os.write(mybytearray, 0, mybytearray.length);
			os.flush();
	
			System.out.println("Client has finished sending");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}