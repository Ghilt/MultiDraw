package mainclient;

import java.awt.Color;

import gui.MultiDrawFrame;

public class Controller {
	private MultiDrawFrame frame;
	
	public Controller() {
		
	}
	
	public void setFrame(MultiDrawFrame frame) {
		this.frame = frame;
	}

	public void setBrushColor(Color color) {
		frame.getPaintPanel().setBrushColor(color);
	}

	public void putChatMessage(String msg) {
		frame.getChatPanel().append(msg + "\n");
	}

	public void drawLine(int x1, int y1, int x2, int y2, int rgb, int width) {
		frame.getPaintPanel().drawLine(x1, y1, x2, y2, rgb, width);
	}
}