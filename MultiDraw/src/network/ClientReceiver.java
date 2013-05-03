package network;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import utils.Controller;
import utils.Protocol;

public class ClientReceiver extends Thread {
	private Socket s;
	private BufferedReader in;
	private Controller controller;

	public ClientReceiver(Socket s, Controller controller) {
		this.controller = controller;
		try {
			this.s = s;
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Connection error, shutting down...");
		}
	}

	public void run() {
		System.out.println("ClientReceiver running...");
		try {
			String strIn = "";
			while ((strIn = in.readLine()) != null) {
				System.out.println("Client received strIn: " + strIn);
				String cmd = strIn.substring(0, strIn.indexOf(" "));
				String[] words;
				words = strIn.split(" ");
				switch (Integer.parseInt(cmd)) {
					case Protocol.ALOHA:
						receiveImage(Integer.parseInt(words[1]));
						break;
					case Protocol.USERLIST:
						controller.updateUsersList(words);
						break;
					case Protocol.DRAW_LINE:
						if (words.length > 4) {
							int x1, y1, x2, y2, rgb, width;
							x1 = Integer.parseInt(words[1]);
							y1 = Integer.parseInt(words[2]);
							x2 = Integer.parseInt(words[3]);
							y2 = Integer.parseInt(words[4]);
							rgb = Integer.parseInt(words[5]);
							width = Integer.parseInt(words[6]);
							controller.drawLine(x1, y1, x2, y2, rgb, width);
						}
						break;
					case Protocol.DRAW_PEN:
						if (words.length > 4) {
							int x1, y1, x2, y2, rgb, width;
							x1 = Integer.parseInt(words[1]);
							y1 = Integer.parseInt(words[2]);
							x2 = Integer.parseInt(words[3]);
							y2 = Integer.parseInt(words[4]);
							rgb = Integer.parseInt(words[5]);
							width = Integer.parseInt(words[6]);
							controller.drawLine(x1, y1, x2, y2, rgb, width);
						}
						break;
					case Protocol.CHAT_MESSAGE:
						String msg = strIn.substring(strIn.indexOf(" "));
						controller.putChatMessage(msg);
						break;
					case Protocol.SEND_FILE:
						controller.sendImage();
						break;
				}
			}
			in.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveImage(int size) {
		try {
			System.out.println("Client receiving image with size: " + size);
			byte[] mybytearray = new byte[size];
			InputStream is = s.getInputStream();
			int pos = 0;
		    do {
		        pos += is.read(mybytearray, pos, size-pos);
		        System.out.println(pos + " / " + size + " read");
		    } while (pos < size);
			InputStream in = new ByteArrayInputStream(mybytearray);
			BufferedImage image = ImageIO.read(in);
			controller.insertImage(image);
			System.out.println("Filetransfer ended received");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}