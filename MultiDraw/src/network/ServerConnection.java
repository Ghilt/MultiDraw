package network;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tools.ToolProperties;
import utils.ImageWrapper;
import utils.Protocol;

public class ServerConnection extends Thread {
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private ArrayList<ServerConnection> connections;
	private ImageWrapper image;
	private ToolProperties tp;

	public ServerConnection(Socket s, ArrayList<ServerConnection> connections,
			ImageWrapper image) {
		super();
		this.s = s;
		this.image = image;
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
		String cmd = strIn.substring(0, strIn.indexOf(" "));
		String[] words;
		words = strIn.split(" ");
		switch (Integer.parseInt(cmd)) {
			case Protocol.ALOHA:
				sendImage();
			break;
			case Protocol.CHAT_MESSAGE:
				writeToAll(strIn);
				break;
			case Protocol.SEND_FILE:
				write(Protocol.SEND_FILE + " ");
				BufferedImage img = receiveImage(Integer.parseInt(words[1]));
				image.insertPicture(img);
				for (ServerConnection cc : connections) {
					cc.sendImage();
				}
				break;
			case Protocol.DRAW_LINE:
				if(!image.disabled){
					strIn += " " + tp.getColor() + " " + tp.getBrushSize();
					writeToAll(strIn);
					if (words.length > 4) {
						int x1, y1, x2, y2;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						image.drawLine(x1, y1, x2, y2, tp.getColor(), tp.getBrushSize());
					}
				}
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

	public void sendImage() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();

			write(Protocol.ALOHA + " " + imageInByte.length);

			OutputStream os = s.getOutputStream();
			System.out
					.println("Trying to sen it all(BufferedImage) in one go! nbr of bytes ");
			os.write(imageInByte, 0, imageInByte.length);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage receiveImage(int size) {
		BufferedImage im = null;
		try {
			System.out.println("Starting to receive image with size: " + size);
			byte[] mybytearray = new byte[size];
			InputStream is = s.getInputStream();
			int totalBytesRead = 0;
			int bytesRead = 0;

			while (totalBytesRead < size && bytesRead != -1) {
				bytesRead = is.read(mybytearray, totalBytesRead,
						mybytearray.length - totalBytesRead);
				totalBytesRead += bytesRead;
				// System.out.println(totalBytesRead + " / " + size +
				// " read & bytesread = " + bytesRead);
			}

			InputStream in = new ByteArrayInputStream(mybytearray);
			im = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return im;
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