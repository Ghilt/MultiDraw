package tools;

import utils.Protocol;

public class ToolProperties {
	private int width;
	private int color1;
	private int color2;

	public ToolProperties(int color1, int color2, int width) {
		this.color1 = color1;
		this.color2 = color2;
		this.width = width;
	}

	public int getColor(byte type) {
		if (type == Protocol.BRUSH_COLOR_1) {
			return color1;
		} else if (type == Protocol.BRUSH_COLOR_2) {
			return color2;
		}
		return -1;
	}

	public void setColor(int color, byte type) {
		if (type == Protocol.BRUSH_COLOR_1) {
			this.color1 = color;
		} else if (type == Protocol.BRUSH_COLOR_2) {
			this.color2 = color;
		}
	}

	public void setBrushWidth(int width) {
		this.width = width;
	}

	public int getBrushWidth() {
		return width;
	}
}
