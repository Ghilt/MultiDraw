package network;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import utils.Protocol;

public class ClientSender extends Thread {
	private String clientName;
	private Socket s;
	private OutputStream out;
	private SendBuffer buffer;

	public ClientSender(String myName, Socket s, SendBuffer buffer) {
		this.clientName = myName;
		this.s = s;
		this.buffer = buffer;
		buffer.put(Protocol.ALOHA + " " + myName);
	}

	public void run() {
		System.out.println("ClientSender running...");
		try {
			out = s.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			while (true) {
				if(!buffer.isDisabled()){
					writer.println(buffer.pop());
					writer.flush();
				}
			}
		} catch (IOException e) {
			System.out.println("Connection error, bailing out...");
		}
	}	
	
	public void sendImage() {
		try {
			byte[] image = buffer.popImage();
			System.out.println("Client has started sending image");
			OutputStream os = s.getOutputStream();
			System.out.println("Trying to sen it all(BufferedImage) in one go! nbr of bytes ");
			os.write(image, 0, image.length);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getClientName() {
		return clientName;
	}

	public SendBuffer getBuffer() {
		return buffer;
	}
}