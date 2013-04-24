package networktest;
import gui.PaintPanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class DrawClient extends Thread {
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private PaintPanel panel;

	public DrawClient(String host, int port) {
		try {
			s = new Socket(host, port);
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("No host at this address, shutting down...");
		}
	}

	public void run() {
		System.out.println("Starting Thread " + getName());
		try {
			String strIn;
			while ((strIn = in.readLine()) != null) {
				String[] words = strIn.split(" ");
				int x1, y1, x2, y2;
				x1 = Integer.parseInt(words[1]);
				y1 = Integer.parseInt(words[2]);
				x2 = Integer.parseInt(words[3]);
				y2 = Integer.parseInt(words[4]);
				panel.drawLine(x1, y1, x2, y2);
			}
			in.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String s) {
//		System.out.println(s);
		out.println(s);
		out.flush();
	}
	
	public void setPaintPanel(PaintPanel panel) {
		this.panel = panel;
	}
}