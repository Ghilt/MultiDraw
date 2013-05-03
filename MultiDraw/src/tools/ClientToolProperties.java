package tools;

import java.awt.Color;

import utils.Protocol;

import network.SendBuffer;

public class ClientToolProperties {
	public static final int PEN_TOOL = 1;
	public static final int BRUSH_TOOL = 2;
	public static final int LINE_TOOL = 3;
	public static final int ERASER_TOOL = 4;
	public static final int BUCKET_TOOL = 5;
	public static final int TEXT_TOOL = 6;
	public static final int RECTANGLE_TOOL = 7;
	public static final int ELLIPSE_TOOL = 8;

	private int width;
	private int color;
	private int tool;
	private SendBuffer buffer;

	public ClientToolProperties(int color, int width) {
		this.color = color;
		this.width = width;
		this.tool = BRUSH_TOOL;
	}
	
	public ClientToolProperties(SendBuffer buffer) {
		this.color = Color.BLACK.getRGB();
		this.width = 10;
		this.tool = BRUSH_TOOL;
		this.buffer = buffer;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		String send = Protocol.CHANGE_BRUSH_COLOR + " " + color;
		buffer.put(send);
		this.color = color;
	}
	
	public void setColor(Color color) {
		int c = color.getRGB();
		String send = Protocol.CHANGE_BRUSH_COLOR + " " + c;
		buffer.put(send);
		this.color = c;
	}

	public void setBrushWidth(int width) {
		String send = Protocol.CHANGE_BRUSH_SIZE + " " + width;
		buffer.put(send);
		this.width = width;
	}

	public int getBrushWidth() {
		return width;
	}
	
	public void setTool(int tool) {
		this.tool = tool;
	}
	
	public int getTool() {
		return tool;
	}

	public void changeTool(int tool) {
		this.tool = tool;
	}
}
