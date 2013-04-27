package utils;

public interface Protocol {	
	// MISC commands
	public static final byte ALOHA = 1;
	public static final byte SEND_FILE = 2;
	public static final byte CHAT_MESSAGE = 3;
	public static final byte USERLIST= 4;
	public static final byte DISABLE= 6;
	public static final byte DISABLE_ACK= 7;
	public static final byte ENABLE= 8;
	public static final byte ENABLE_ACK = 9;
	
	// Paint commands
	public static final byte DRAW_LINE = 10;
	public static final byte CHANGE_BRUSH_COLOR = 11;
	public static final byte CHANGE_BRUSH_SIZE = 12;
}
