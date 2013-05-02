package mainclient;
import gui.MultiDrawFrame;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import network.ClientReceiver;
import network.ClientSender;
import network.SendBuffer;
import utils.Controller;

class MultiDrawClient {
	public static void main(String[] args) {
//		try {
//			// Fix look and feel of GUI.
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String myName = "";
		while (myName.length() <= 0)
			myName = JOptionPane.showInputDialog("Enter a nickname");
		System.out.println(myName);
		Socket s = null;
		try {
			s = new Socket("213.113.124.67", 30001);
		} catch (UnknownHostException e) {
			System.err.println("Did not find a host at the specified address.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Connection error.");
			System.exit(1);
		}
		
		SendBuffer buffer = new SendBuffer(10);
		Controller controller = new Controller();
		ClientReceiver receiver = new ClientReceiver(s, controller);
		ClientSender sender = new ClientSender(myName, s, buffer);
		MultiDrawFrame mainframe = new MultiDrawFrame(buffer);
		
		controller.setFrame(mainframe);
		controller.setSender(sender);
		receiver.start();
		sender.start();
	}
}
