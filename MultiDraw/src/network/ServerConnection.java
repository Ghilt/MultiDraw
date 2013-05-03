package network;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;

import tools.ToolProperties;
import utils.ImageWrapper;
import utils.Protocol;
import utils.ServerState;

public class ServerConnection extends Thread {
	private String name;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;

	private ImageWrapper image;
	private ToolProperties tp;
	private ServerState state;

	public ServerConnection(Socket s, ImageWrapper image, ServerState state) {
		super();
		this.s = s;
		this.state = state;
		this.image = image;
		this.tp = new ToolProperties(Color.BLACK.getRGB(), 10);
	}

	public void run() {
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String strIn = "";
			while ((strIn = in.readLine()) != null) {
				parseCommand(strIn);
			}
		} catch (IOException e) {
//			this.connections.remove(this);
//			for (ServerConnection cc : connections) {
//				cc.sendUsers();
//			}
			System.out.println("Disconnected: " + s.getInetAddress().getHostAddress());
		}
	}

	private String parseCommand(String strIn) {
		String cmd = strIn.substring(0, strIn.indexOf(" "));
		String[] words;
		words = strIn.split(" ");
		switch (Integer.parseInt(cmd)) {
			case Protocol.ALOHA:
				this.name = words[1];
//				sendUsers();
//				state.setDisabled(true);
				sendImage();
//				state.setDisabled(false);
			break;
			case Protocol.CHAT_MESSAGE:
				writeToAll(strIn);
				break;
			case Protocol.SEND_FILE:
				write(Protocol.SEND_FILE + " ");
				BufferedImage img = receiveImage(Integer.parseInt(words[1]));
				image.insertPicture(img);
				state.setDisabled(true);
				for (ServerConnection cc :  state.getConnections()) {

					cc.sendImage();
				}
				state.setDisabled(false);
				break;
			case Protocol.DRAW_LINE:
//				if (!state.isDisabled()) {
					strIn += " " + tp.getColor() + " " + tp.getBrushWidth();
					writeToAll(strIn);
					if (words.length > 4) {
						int x1, y1, x2, y2;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						image.drawLine(x1, y1, x2, y2, tp.getColor(), tp.getBrushWidth());
					}
//				}
				break;
			case Protocol.DRAW_PEN:
//				if (!state.isDisabled()) {
					strIn += " " + tp.getColor() + " " + 1;
					writeToAll(strIn);
					if (words.length > 4) {
						int x1, y1, x2, y2;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						image.drawLine(x1, y1, x2, y2, tp.getColor(), 1);
					}
//				}
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

	private void sendUsers() {
		String list = Protocol.USERLIST + " ";
		for (ServerConnection cc :  state.getConnections()) {
			list += cc.name + " ";
		}
		writeToAll(list);
	}

	public void sendImage() {
		try {
			// Read image into byte[]
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			
			// Send ALOHA and image size
			write(Protocol.ALOHA + " " + imageInByte.length);

			// Send image
			System.out.println("Server sending image with size: " + imageInByte.length);
			OutputStream os = s.getOutputStream();
			int sizeToSend = 250;
			int totalSent = 0;
			while(totalSent < imageInByte.length){
				if (imageInByte.length - totalSent < sizeToSend)
					sizeToSend = imageInByte.length - totalSent;
				os.write(imageInByte, totalSent, sizeToSend);
				os.flush();
				totalSent += sizeToSend;
				System.out.println(totalSent + " / " + imageInByte.length + " read & bytesread = " + sizeToSend + ". " + (imageInByte.length - totalSent) + " remaining.");
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage receiveImage(int size) {
		BufferedImage im = null;
		try {
			System.out.println("Server receiving image with size: " + size);
			byte[] mybytearray = new byte[size];
			InputStream is = s.getInputStream();
			int totalBytesRead = 0;
			int bytesToRead = 250;
			int bytesRead = 0;
			while (totalBytesRead < size) {
				if (bytesToRead > size - totalBytesRead)
					bytesToRead = size - totalBytesRead;
				bytesRead = is.read(mybytearray, totalBytesRead, bytesToRead);
				totalBytesRead += bytesRead;
				System.out.println(totalBytesRead + " / " + size + " read & bytesread = " + bytesRead + ". " + (size - totalBytesRead) + " remaining.");
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
		for (ServerConnection cc : state.getConnections()) {
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