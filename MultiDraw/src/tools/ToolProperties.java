package tools;

import utils.Protocol;

public class ToolProperties {

	
	private int width;
	private int colorBrush1;
	private int colorBrush2;

	public ToolProperties(int color1, int color2, int width) {
		this.colorBrush1 = color1;
		this.colorBrush2 = color2;
		this.width = width;
	}

	public int getBrushColor(byte type) {
		if(type == Protocol.BRUSH_COLOR_1){
			return colorBrush1;
		} else if(type == Protocol.BRUSH_COLOR_2){
			return colorBrush2;
		} else {
			return -1;
		}
	}

	public void setColor(int clr, byte changeBrushColor) {
		if(changeBrushColor == Protocol.BRUSH_COLOR_1){
			this.colorBrush1 = clr;
		} else if(changeBrushColor == Protocol.BRUSH_COLOR_2){
			this.colorBrush2 = clr;
		}
	}

	public void setBrushWidth(int width) {
		this.width = width;
	}

	public int getBrushSize() {
		return width;
	}
}
