package utils;

public abstract class Protocol {	
	// Misc commands
	public static final byte ALOHA = 1;
	public static final byte SEND_FILE = 2;
	public static final byte CHAT_MESSAGE = 3;
	public static final byte USERLIST = 4;
	public static final byte ACK = 5;
	
	// Paint commands
	public static final byte DRAW_LINE = 10;
	public static final byte DRAW_PEN = 11;
	public static final byte DRAW_RECTANGLE = 12;
	public static final byte DRAW_ELLIPSE = 13;
	public static final byte ERASE = 14;
	public static final byte DRAW_TEXT = 15;
	
	// Tool properties
	public static final byte BRUSH_COLOR_1 = 20;
	public static final byte BRUSH_COLOR_2 = 21;
	public static final byte CHANGE_BRUSH_SIZE = 22;
	public static final byte CHAR_SPACE = 30;
	
	
	
	
public static char interpretAsChar(String in) {
		
		if(in.length() == 1){
			return in.charAt(0);
		}
		
		char c = 0;
		byte code = Byte.parseByte(in);
		
		switch (code) {
		case Protocol.CHAR_SPACE:
			c = ' ';
			break;
		default:
			break;
		}
		
		return c;
	}
}
