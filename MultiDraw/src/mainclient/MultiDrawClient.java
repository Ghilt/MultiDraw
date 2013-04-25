package mainclient;
import gui.MultiDrawFrame;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.UIManager;

import network.ClientReceiver;
import network.ClientSender;
import network.SendBuffer;

class MultiDrawClient {
	public static void main(String[] args) {
//		try {
//			// Fix look and feel of GUI.
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		Socket s = null;
		try {

			s = new Socket("localhost", 30002);


		} catch (UnknownHostException e) {
			System.out.println("Did not find a host at the specified address.");
		} catch (IOException e) {
			System.out.println("Connection error.");
		}
		
		SendBuffer buffer = new SendBuffer(10);
		Controller controller = new Controller();
		ClientReceiver receiver = new ClientReceiver(s, controller);
		ClientSender sender = new ClientSender(s, buffer);
		MultiDrawFrame mainframe = new MultiDrawFrame(buffer);
		
		controller.setFrame(mainframe);
		receiver.start();
		sender.start();
	}
}
