package network;
import gui.PaintPanel;
import interfaces.Protocol;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import mainclient.Controller;

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
				String cmd = strIn.substring(0, strIn.indexOf(" "));
				String[] words;
				switch (Integer.parseInt(cmd)) {
				case Protocol.DRAW_LINE:
					words = strIn.split(" ");
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
				case Protocol.CHANGE_BRUSH_COLOR:
					words = strIn.split(" ");
					if (words.length > 1) {
						int rgb;
						rgb = Integer.parseInt(words[1]);
						controller.setBrushColor(new Color(rgb));
					}
					break;
				case Protocol.CHAT_MESSAGE:
					String msg = strIn.substring(strIn.indexOf(" "));
					controller.putChatMessage(msg);
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