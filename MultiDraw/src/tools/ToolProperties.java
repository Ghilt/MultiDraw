package tools;

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

	public void setColor(int color) {
		this.color = color;
	}

	public void setBrushWidth(int width) {
		this.width = width;
	}

	public int getBrushWidth() {
		return width;
	}
}