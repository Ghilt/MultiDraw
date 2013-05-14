package test;

import gui.MultiDrawFrame;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


import network.ClientReceiver;
import network.ClientSender;
import network.SendBuffer;
import utils.Controller;

public class MassStartClients {
	public static void main(String[] args) {
		
		for (int i = 0; i < 10; i++) {
			MssThread test = new MssThread("Test" + i);
			test.run();
		}
	
	}
}

class MssThread extends Thread{
	
	public MssThread(String s) {
		super(s);
	}
	
	@Override
	public void run() {
		
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
		Controller controller = new Controller();
		ClientReceiver receiver = new ClientReceiver(s, controller);
		ClientSender sender = new ClientSender(getName(), s, buffer);
		MultiDrawFrame mainframe = new MultiDrawFrame(buffer);
		
		controller.setFrame(mainframe);
		controller.setSender(sender);
		receiver.start();
		sender.start();
		
	}
}