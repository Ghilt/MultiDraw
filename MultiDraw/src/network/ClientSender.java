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
				writer.println(buffer.pop());
				writer.flush();
			}
		} catch (IOException e) {
			System.out.println("Connection error, bailing out...");
		}
	}	
	
	public void sendImage() {
		try {
			// Send image
			byte[] imageInByte = buffer.popImage();
			System.out.println("Client sending image with size: " + imageInByte.length);
			OutputStream os = s.getOutputStream();
			int sizeToSend = 250;
			int totalSent = 0;
			while (totalSent < imageInByte.length) {
				if (imageInByte.length - totalSent < sizeToSend)
					sizeToSend = imageInByte.length - totalSent;
				os.write(imageInByte, totalSent, sizeToSend);
				os.flush();
				totalSent += sizeToSend;
				System.out.println(totalSent + " / " + imageInByte.length + " read & bytesread = " + sizeToSend + ". " + (imageInByte.length - totalSent) + " remaining.");
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getClientName() {
		return clientName;
	}
}