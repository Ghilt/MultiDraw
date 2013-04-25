package interfaces;

public interface Protocol {	
	// Chat commands
	public static final byte CHAT_MESSAGE = 1;
	
	// Paint commands
	public static final byte DRAW_LINE = 10;
	public static final byte CHANGE_BRUSH_COLOR = 11;
	public static final byte CHANGE_BRUSH_SIZE = 12;
}
