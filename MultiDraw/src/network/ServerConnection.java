package network;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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


	private ToolProperties tp;
	private ServerState state;

	public ServerConnection(Socket s, ServerState state) {
		super();
		this.s = s;
		this.state = state;
		this.tp = new ToolProperties(Color.BLACK.getRGB(),Color.WHITE.getRGB(), 10);
	}

	public void run() {
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String strIn = "";
			while ((strIn = in.readLine()) != null) {
				parseCommand(strIn);
			}
			disconnection();
		} catch (IOException e) {
			disconnection();
		}
	}
	
	private void disconnection() {
		if (this.s != null && !this.s.isClosed() && this.s.isConnected()) {
			try {
				this.s.close();
			} catch (IOException e) { /* IGNORE */ }
		}
		this.state.removeConnection(this);
		for (ServerConnection cc : state.getConnections()) {
			cc.sendUsers();
		}
		System.out.println("Disconnected: " + s.getInetAddress().getHostAddress());
	}

	private void parseCommand(String strIn) {
		if (!strIn.contains(" ")) 
			return;
		String cmd = strIn.substring(0, strIn.indexOf(" "));
		String[] words;
		ImageWrapper image = state.getImgWrapper();
		words = strIn.split(" ");
		switch (Integer.parseInt(cmd)) {
			case Protocol.ALOHA:
				if (words.length > 1) {
					this.name = words[1];
					sendUsers();
					state.setDisabled(true);
					sendImage();
					state.setDisabled(false);
					String send = Protocol.CHAT_MESSAGE +" " + name + " Connected";
					writeToAll(send);
				}
				break;
			case Protocol.CHAT_MESSAGE:
				if (words.length > 1) {
					String text = strIn.substring(strIn.indexOf(" ")).trim();
					String send = Protocol.CHAT_MESSAGE + " " + name + " " + text;
					if (text.charAt(0) == '/') {
						handleSlashCommand(text);
						write(send);
					} else {
						writeToAll(send);
					}
				}
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
				if (!state.isDisabled()) {
					if (words.length > 5) {
						int x1, y1, x2, y2, color, width;
						byte brushType;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						brushType = Byte.parseByte(words[5]);
						color = tp.getColor(brushType);
						width = tp.getBrushWidth();
						image.drawLine(x1, y1, x2, y2, color, width);
						
						String strOut = Protocol.DRAW_LINE + " " + 
										x1 + " " +  y1 + " " +
										x2 + " " +  y2 + " " +
										color + " " + width;
						writeToAll(strOut);
					}
				}
				break;
			case Protocol.DRAW_PEN:
				if (!state.isDisabled()) {
					if (words.length > 5) {
						int x1, y1, x2, y2, color;
						byte brushType;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						brushType = Byte.parseByte(words[5]);
						color = tp.getColor(brushType);
						image.drawLine(x1, y1, x2, y2, color, 1);

						String strOut = Protocol.DRAW_LINE + " " + 
										x1 + " " +  y1 + " " +
										x2 + " " +  y2 + " " +
										color + " " + 1;
						writeToAll(strOut);
					}
				}
				break;
			case Protocol.ERASE:
				if (!state.isDisabled()) {
					if (words.length > 4) {
						int x1, y1, x2, y2, color, width;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						color = Color.WHITE.getRGB();
						width = tp.getBrushWidth();
						image.drawLine(x1, y1, x2, y2, color, width);
						
						String strOut = Protocol.ERASE + " " + 
										x1 + " " +  y1 + " " +
										x2 + " " +  y2 + " " +
										width;
						writeToAll(strOut);
					}
				}
				break;
			case Protocol.DRAW_RECTANGLE:
				if (!state.isDisabled()) {
					if (words.length > 5) {
						int x1, y1, x2, y2, color;
						byte brushType;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						brushType = Byte.parseByte(words[5]);
						color = tp.getColor(brushType);
						image.drawRectangle(x1, y1, x2, y2, color);
						
						String strOut = Protocol.DRAW_RECTANGLE + " " + 
										x1 + " " +  y1 + " " +
										x2 + " " +  y2 + " " +
										color;
						writeToAll(strOut);
					}
				}
				break;
			case Protocol.DRAW_ELLIPSE:
				if (!state.isDisabled()) {
					if (words.length > 5) {
						int x1, y1, x2, y2, color;
						byte brushType;
						x1 = Integer.parseInt(words[1]);
						y1 = Integer.parseInt(words[2]);
						x2 = Integer.parseInt(words[3]);
						y2 = Integer.parseInt(words[4]);
						brushType = Byte.parseByte(words[5]);
						color = tp.getColor(brushType);
						image.drawEllipse(x1, y1, x2, y2, color);
						
						String strOut = Protocol.DRAW_ELLIPSE + " " + 
										x1 + " " +  y1 + " " +
										x2 + " " +  y2 + " " +
										color;
						writeToAll(strOut);
					}
				}
				break;
			case Protocol.DRAW_TEXT:
				if (!state.isDisabled()) {
					if (words.length > 3) {
						int x, y, color, width;
						char cToDraw;
						byte brushType;
						x = Integer.parseInt(words[1]);
						y = Integer.parseInt(words[2]);
						cToDraw = Protocol.interpretAsChar(words[3]);
						brushType = Byte.parseByte(words[4]);
						color = tp.getColor(brushType);
						width = tp.getBrushWidth();
						image.drawText(x, y, cToDraw, color, width);
						
						String strOut = Protocol.DRAW_TEXT + " " + 
										x + " " + 
										y + " " +
										words[3] + " " +
										color + " " + width;
						writeToAll(strOut);
					}
				}
				break;
			case Protocol.BRUSH_COLOR_1:
				tp.setColor(Integer.parseInt(words[1]), Protocol.BRUSH_COLOR_1);
				break;
			case Protocol.BRUSH_COLOR_2:
				tp.setColor(Integer.parseInt(words[1]), Protocol.BRUSH_COLOR_2);
				break;
			case Protocol.CHANGE_BRUSH_SIZE:
				tp.setBrushWidth(Integer.parseInt(words[1]));
				break;
		}
	}

	private void handleSlashCommand(String text) {
		String[] s = text.split(" ");
		if (s[0].equals("/sudokick")) {
			for (ServerConnection c : state.getConnections()) {
				if (c.name.toLowerCase().equals(s[1].toLowerCase())) {
					c.write(Protocol.CHAT_MESSAGE + " SERVER You were kicked by " + name);
					c.disconnection();
				}
			}
		} else if (s[0].equals("/blur")) {
			String value = "10";
			if(s.length > 1){
				value = s[1];
			}
			state.getImgWrapper().blur(Integer.parseInt(value));
			writeToAll(Protocol.BLUR + " " + value);
		}
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
			ImageIO.write(state.getImgWrapper().getImage(), "PNG", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			
			// Send ALOHA and image size
			write(Protocol.ALOHA + " " + imageInByte.length);
			
			String waitForAck = in.readLine();
			
			if (waitForAck == null) {
				disconnection();
				System.err.println("Error: Expected ACK, received: " + waitForAck);
				return;
			}

			// Send image
			//System.out.println("Server sending image with size: " + imageInByte.length);
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
			int sizeToSend = 500;
			int totalSent = 0;
			while(totalSent < imageInByte.length){
				if (imageInByte.length - totalSent < sizeToSend)
					sizeToSend = imageInByte.length - totalSent;
				bos.write(imageInByte, totalSent, sizeToSend);
				bos.flush();
				totalSent += sizeToSend;
				//System.out.println(totalSent + " / " + imageInByte.length + " sent. " + (imageInByte.length - totalSent) + " remaining.");
			}
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage receiveImage(int size) {
		BufferedImage im = null;
		try {
			//System.out.println("Server receiving image with size: " + size);
			byte[] mybytearray = new byte[size];
			BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
			int totalBytesRead = 0;
			int bytesToRead = 250;
			int bytesRead = 0;
			while (totalBytesRead < size) {
				if (bytesToRead > size - totalBytesRead)
					bytesToRead = size - totalBytesRead;
				bytesRead = bis.read(mybytearray, totalBytesRead, bytesToRead);
				totalBytesRead += bytesRead;
				//System.out.println(totalBytesRead + " / " + size + " read & bytesread = " + bytesRead + ". " + (size - totalBytesRead) + " remaining.");
			}
			InputStream in = new ByteArrayInputStream(mybytearray);
			im = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return im;
	}

	public void write(String in) { // send command to receiver
		if (out != null) {
			out.println(in);
			out.flush();
		} else {
			System.err.println("Error, outstream was null");
			disconnection();
		}
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
