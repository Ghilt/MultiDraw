package test;

import gui.InvisiblePanel;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import network.ClientSender;
import network.SendBuffer;
import utils.Protocol;

public class ContinuousDrawing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String myName = "testing";
		Socket s = null;
		try {
			s = new Socket("localhost", 30001);
		} catch (UnknownHostException e) {
			System.err.println("Did not find a host at the specified address.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Connection error.");
			System.exit(1);
		}
		
		SendBuffer buffer = new SendBuffer(10);
		ClientSender sender = new ClientSender(myName, s, buffer);
		sender.start();
		
		try {
			Thread.sleep(1000);
			buffer.put(Protocol.ACK + "");
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (true) {
			buffer.put(Protocol.CHANGE_BRUSH_SIZE + " " + (int)(Math.random()*100));
			Color c = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
			buffer.put(Protocol.BRUSH_COLOR_1 + " " + c.getRGB());
			buffer.put(Protocol.DRAW_LINE + " " + (int)(Math.random()*InvisiblePanel.SIZE_X) + " " + (int)(Math.random()*InvisiblePanel.SIZE_Y) + " " + (int)(Math.random()*InvisiblePanel.SIZE_X) + " " + (int)(Math.random()*InvisiblePanel.SIZE_Y) + " " + Protocol.BRUSH_COLOR_1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
