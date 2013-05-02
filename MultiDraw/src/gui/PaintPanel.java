package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import network.SendBuffer;
import utils.ImageWrapper;
import utils.Protocol;

public class PaintPanel extends JPanel implements MouseListener, MouseMotionListener {
	private ImageWrapper bufImage;

	// Canvas size
	public static final int SIZE_X = 900;
	public static final int SIZE_Y = 780;

	// Mouse pointer coordinates
	private int currentX = 0;
	private int currentY = 0;
	private int previousX = -90000;
	private int previousY = -90000;

	// Mouse pressed coordinates
	private Point p1 = null;
	private Point p2 = null;
	private boolean waitForShape = false; // when drawing shapes we are waiting
											// for mouse press

	// Current brush properties
	private Color brushColor = Color.BLACK;
	private int brushWidth = 10;
	
	// Current selected tool
	private String currentTool = "PEN";

	// Buffer for outgoing commands
	private SendBuffer buffer;


	/**
	 * A "canvas" used to draw on.
	 */
	public PaintPanel(SendBuffer buffer) {
		setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.buffer = buffer;

		// Initializing bufImage
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		bufImage.setWhiteBackground();
		repaint();
	}

	// change tools handler
	public void changeTool(String tool) {
		
		//reset the color if the last tool was an eraser
		if (currentTool == "ERASER" && tool != "ERASER") {
			sendChangeBrushcolorCommandToserver(brushColor);
		} else if (waitForShape && tool.split("_")[0] != "SHAPE") {
			waitForShape = false;
		}
		
		if (tool.equals("PEN")) {

		} else if (tool.equals("BRUSH")) {

		} else if (tool.equals("BUCKET")) {

		} else if (tool.equals("ERASER")) {
			sendChangeBrushcolorCommandToserver(Color.WHITE);
			
		} else if (tool.equals("TEXT")) {
			
		} else if (tool.equals("SHAPE_LINE")) {
			waitForShape = true;
			
		} else if (tool.equals("SHAPE_RECTANGLE")) {
			
		} else if (tool.equals("SHAPE_ELLIPSE")) {
			
		} else {
			System.out.println("Unable to recognize tool in changeTool method");
		}
		
		currentTool = tool;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D
		g2.drawImage(bufImage, null, 0, 0);
	}
	
	// returns brushColor
	public int getBrushWidth() {
		return this.brushWidth;
	}
	
	// returns brushColor
	public void setBrushWidth(int width) {
		this.brushWidth = width;
		sendChangeBrushSizeCommandToserver(width);
	}

	// returns brushColor
	public Color getBrushColor() {
		return this.brushColor;
	}

	// sets brushColor
	public void setBrushColor(Color color) {
		brushColor = color;
	}

	public void sendChangeBrushSizeCommandToserver(int size) {
		String send = Protocol.CHANGE_BRUSH_SIZE + " " + size;
		buffer.put(send);
	}

	public void sendChangeBrushcolorCommandToserver(Color color) {
		String send = Protocol.CHANGE_BRUSH_COLOR + " " + color.getRGB();
		buffer.put(send);
	}

	public void sendFileForInsertingtoSever(File f) {
		try {
			System.out.println("File loaded, sending to server");
			BufferedImage img = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "PNG", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			buffer.putImage(imageInByte);
			buffer.put(Protocol.SEND_FILE + " " + imageInByte.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// experimental
	public void enableStraightLine() {
		waitForShape = true;
	}

	public void drawLine(int previousX, int previousY, int currentX,
			int currentY, int rgb, int width) {
		bufImage.drawLine(previousX, previousY, currentX, currentY, rgb, width);
		repaint();
	}

	public void insertPicture(File file) {
		bufImage.insertPicture(file);
		repaint();
	}

	public void insertPicture(BufferedImage img) {
		bufImage.insertPicture(img);
		repaint();
	}

	/*
	 * This method doesn't actually draw anything, it just puts a draw command
	 * into the SendBuffer.
	 */
	public void drawBrush(MouseEvent e) {
		// Set mouse coordinates
		currentX = e.getX();
		currentY = e.getY();

		// Need something better than -90000 here... Ugly.
		if (previousX == -90000 && previousY == -90000) {
			previousX = currentX;
			previousY = currentY;
		}

		String send = Protocol.DRAW_LINE + " " + previousX + " " + previousY
				+ " " + currentX + " " + currentY;

		buffer.put(send);

		// Set previous coordinates
		previousX = currentX;
		previousY = currentY;
	}
	
	/*
	 * This method doesn't actually draw anything, it just puts a draw command
	 * into the SendBuffer.
	 */
	public void sendDrawLine(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
		String send = Protocol.DRAW_LINE + " " + 
					  previousX + " " + 
					  previousY + " " + 
					  currentX + " " + 
					  currentY;

		buffer.put(send);
	}

	public void mousePressed(MouseEvent e) {
		// drawStuff(e);
		// Check if we are waiting a shape to be drawn (works only for lines for
		// the moment)
		if (waitForShape) {
			if (p1 == null) {
				p1 = new Point(e.getX(), e.getY());
				previousX = p1.x;
				previousY = p1.y;
			} else if (p2 == null) {
				p2 = new Point(e.getX(), e.getY());
				drawBrush(e);
				p1 = null;
				p2 = null;
			}
		} else {
			drawBrush(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		// drawStuff(e);
		if (!waitForShape) {
			drawBrush(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		// Resetting previous coordinates
		if (!waitForShape) {
			previousX = -90000;
			previousY = -90000;
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

}
