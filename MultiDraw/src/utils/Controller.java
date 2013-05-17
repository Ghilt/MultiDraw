package utils;

import gui.MultiDrawFrame;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import network.ClientSender;

public class Controller {
	private MultiDrawFrame frame;
	private ClientSender sender;
	
	public Controller() {
		
	}
	
	public void setFrame(MultiDrawFrame frame) {
		this.frame = frame;
	}

	public void setBrushColor(Color color, int type) {
		frame.getToolProperties().setColor(color, type);
	}
	
	public void putChatMessage(String msg) {
		StyledDocument doc = frame.getChatPanel().getStyledDocument();
		try {
			doc.insertString(doc.getLength(), msg + "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void drawLine(int x1, int y1, int x2, int y2, int rgb, int width) {
		frame.getPaintPanel().drawLine(x1, y1, x2, y2, rgb, width);
	}

	public void drawRectangle(int x1, int y1, int x2, int y2, int rgb) {
		frame.getPaintPanel().drawRectangle(x1, y1, x2, y2, rgb);
	}

	public void drawEllipse(int x1, int y1, int x2, int y2, int rgb) {
		frame.getPaintPanel().drawEllipse(x1, y1, x2, y2, rgb);
	}

	public void drawText(int x, int y, char c, int rgb) {
		frame.getPaintPanel().drawText(x, y, c, rgb);
	}
	
	public void sendImage() {
		sender.sendImage();
	}

	public void setSender(ClientSender sender) {
		this.sender = sender;
	}

	public void insertImage(BufferedImage img) {
		frame.getPaintPanel().insertPicture(img);
	}

	public void updateUsersList(String[] words) {
		frame.updateUsersList(words);
	}

	public void sendAck() {
		sender.sendAck();
	}
}
