package network;

import interfaces.Protocol;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	private boolean uglyAs;

	public ServerConnection(Socket s, ArrayList<ServerConnection> connections) {
		super();
		this.s = s;
		this.connections = connections;
		this.tp = new ToolProperties(Color.BLACK.getRGB(), 10);
	}

	@Override
	public void run() {
		uglyAs = true;
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String strIn = "";
			while ((strIn = in.readLine()) != null && uglyAs) {
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
		case Protocol.SEND_FILE:
			try {
				fileTransferProcedure(Integer.parseInt(words[1]));
			} catch (IOException e) {
				e.printStackTrace();
			}
//			uglyAs = false;
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

	private void fileTransferProcedure(int size) throws IOException {
			write(Protocol.SEND_FILE + " " +size);
		    byte[] mybytearray = new byte[size];
		    InputStream is = s.getInputStream();
		    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("s.png"));
		    int totalBytesRead = 0;
		    int bytesRead = 0;
		    while (totalBytesRead < size && bytesRead != -1) {
		    	bytesRead = is.read(mybytearray, totalBytesRead, mybytearray.length - totalBytesRead);
		    	bos.write(mybytearray, totalBytesRead, bytesRead);
		    	totalBytesRead += bytesRead;
		    }
		    bos.close();
//		    s.close();
		    System.out.println("Filetransfer ended received");
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