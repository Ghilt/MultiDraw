package network;

import interfaces.Protocol;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import tools.ToolProperties;

public class ServerConnection extends Thread {
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private ArrayList<ServerConnection> connections;
	private ToolProperties tp;

	public ServerConnection(Socket s, ArrayList<ServerConnection> connections) {
		super();
		this.s = s;
		this.connections = connections;
		this.tp = new ToolProperties(Color.BLACK.getRGB(), 10);
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String strIn = "";
			while ((strIn = in.readLine()) != null) {
				parseCommand(strIn);
			}
		} catch (IOException e) {
			System.out.println("Disconnected: "
					+ s.getInetAddress().getHostAddress());
		}
	}

	private String parseCommand(String strIn) {
		String[] words = strIn.split(" ");
		switch (Integer.parseInt(words[0])) {
			case Protocol.CHAT_MESSAGE:
				writeToAll(strIn);
				break;
			case Protocol.DRAW_LINE:
				strIn += " " + tp.getColor() + " " + tp.getBrushSize();
				writeToAll(strIn);
				break;
			case Protocol.CHANGE_BRUSH_COLOR:
				tp.setColor(Integer.parseInt(words[1]));
				break;
			case Protocol.CHANGE_BRUSH_SIZE:
				tp.setBrushWidth(Integer.parseInt(words[1]));
				break;
		}

		return strIn;
	}

	public void write(String in) { // send command to receiver
		out.println(in);
		out.flush();
	}

	public void writeToAll(String msg) {
		for (ServerConnection cc : connections) {
			cc.write(msg);
		}
	}

	public void closeConnection() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getHostName() {
		return s.getInetAddress().getHostName();
	}
}