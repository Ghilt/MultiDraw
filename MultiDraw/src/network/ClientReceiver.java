package network;
import gui.PaintPanel;

import interfaces.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientReceiver extends Thread {
	private Socket s;
	private BufferedReader in;
	private PaintPanel panel;

	public ClientReceiver(Socket s , PaintPanel panel) {
		
		this.panel = panel;
		
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
				String[] words = strIn.split(" ");
				switch (Integer.parseInt(words[0])) {
				case Protocol.DRAW_LINE:
					if (words.length > 4) {
						int x1, y1, x2, y2;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						panel.drawLine(x1, y1, x2, y2);
					}
					break;
				}
			}
			in.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}