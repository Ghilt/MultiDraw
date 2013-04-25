package network;
import gui.PaintPanel;
import interfaces.Protocol;

import java.awt.Color;
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
				System.out.println("string in: " + strIn);
				String[] words = strIn.split(" ");
				switch (Integer.parseInt(words[0])) {
				case Protocol.DRAW_LINE:
					if (words.length > 4) {
						
						int x1, y1, x2, y2, rgb;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						rgb = Integer.parseInt(words[5]);
						panel.drawLine(x1, y1, x2, y2, rgb);
					}
					break;
				case Protocol.CHANGE_BRUSH_COLOR:
					if (words.length > 1) {
						System.out.println("in changecolor in client receiver");
						int rgb;
						rgb = Integer.parseInt(words[1]);
						panel.setBrushColor(new Color(rgb));
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