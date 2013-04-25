package tools;

import java.awt.Color;

public class ToolProperties {

	
	private int width;
	private int color;

	public ToolProperties(int color, int width) {
		this.color = color;
		this.width = width;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int clr) {
		this.color = clr;
	}
}
