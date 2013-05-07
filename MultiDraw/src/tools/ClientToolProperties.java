package tools;

import gui.ColorButton;

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
	public static final int COLORPICKER_TOOL = 9;

	private int width;
	private int color1;
	private int color2;
	private int tool;
	private SendBuffer buffer;
	private ColorButton colorButton1;
	private ColorButton colorButton2;
	
	public ClientToolProperties(SendBuffer buffer) {
		this.color1 = Color.BLACK.getRGB();
		this.color2 = Color.WHITE.getRGB();
		this.width = 10;
		this.tool = BRUSH_TOOL;
		this.buffer = buffer;
	}

	public int getColor(int type) {
		if (type == Protocol.BRUSH_COLOR_1) {
			return color1;
		} else if (type == Protocol.BRUSH_COLOR_2) {
			return color2;
		}
		return -1;
	}

	public void setColor(int color, int type) {
		if (type == Protocol.BRUSH_COLOR_1) {
			this.color1 = color;
			String send = Protocol.BRUSH_COLOR_1 + " " + color;
			colorButton1.setBackground(new Color(color));
			buffer.put(send);
		} else if (type == Protocol.BRUSH_COLOR_2) {
			this.color2 = color;
			String send = Protocol.BRUSH_COLOR_2 + " " + color;
			colorButton2.setBackground(new Color(color));
			buffer.put(send);
		}
	}
	
	public void setColor(Color color, int type) {
		if (type == Protocol.BRUSH_COLOR_1) {
			this.color1 = color.getRGB();
			String send = Protocol.BRUSH_COLOR_1 + " " + color.getRGB();
			colorButton1.setBackground(color);
			buffer.put(send);
		} else if (type == Protocol.BRUSH_COLOR_2) {
			this.color2 = color.getRGB();
			String send = Protocol.BRUSH_COLOR_2 + " " + color.getRGB();
			colorButton2.setBackground(color);
			buffer.put(send);
		}
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

	public void setColorbuttons(ColorButton colorButton1,ColorButton colorButton2) {
		this.colorButton1 = colorButton1;
		this.colorButton2 = colorButton2;
	}
}
