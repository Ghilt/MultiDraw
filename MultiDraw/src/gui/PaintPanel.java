package gui;

import interfaces.Protocol;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import network.SendBuffer;

public class PaintPanel extends JPanel implements MouseListener, MouseMotionListener {
	// Stores underlying image. (Maybe we can have a Vector of BufferedImages,
	// then we could probably implement Ctrl+Z etc.)
	private BufferedImage bufImage;

	// Temporary storage for convolution filters (blur etc.)
	private BufferedImage bufDest;

	// Canvas size
	private static final int SIZE_X = 900;
	private static final int SIZE_Y = 780;

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
	private Stroke stroke;
	
	// Buffer for outgoing commands
	private SendBuffer buffer;


	/**
	 * A "canvas" used to draw on.
	 */
	public PaintPanel(SendBuffer buffer) {
		
		

		
		
		setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.buffer = buffer;
		
		// Initializing bufImage
		bufImage = (BufferedImage) createImage(SIZE_X, SIZE_Y);
		bufImage = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gc = bufImage.createGraphics();

		// Fill background with white color
		gc.setColor(Color.WHITE);
		gc.fillRect(0, 0, SIZE_X, SIZE_Y);
		// Initializing stroke
		stroke = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D
		
		// Dunno if this does anything.. I hoped it would create antialiasing but it doesn't.. :(
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	
		// re-draw bufImage
		g2.drawImage(bufImage, null, 0, 0);
	}

	public void blur() {
		float[] blurKernel = { 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f,
				1 / 9f, 1 / 9f, 1 / 9f };

		BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
		bufImage = blur.filter(bufImage, bufDest);
	}

	private void drawStuff(MouseEvent e) {
		// Set mouse coordinates
		currentX = e.getX();
		currentY = e.getY();

		// Need something better than -90000 here... Ugly.
		if (previousX == -90000 && previousY == -90000) {
			previousX = currentX;
			previousY = currentY;
		}

		Graphics2D g2 = bufImage.createGraphics();
		g2.setStroke(stroke);

		// Right-click for eraser, don't know if values are the same for all mice, seems weird.
		switch (e.getModifiers()) {
			case 4:
				g2.setColor(Color.WHITE);
				break;
			case 16:
				g2.setColor(brushColor);
				break;
		}
		
		// Check if we are waiting a shape to be drawn (works only for lines for the moment)
		if (waitForShape) {
			if (p1 == null) {
				p1 = new Point(e.getX(), e.getY());
			} else if (p2 == null) {
				p2 = new Point(e.getX(), e.getY());
			
				waitForShape = false;
				g2.drawLine(p1.x, p1.y, p2.x, p2.y);
				p1 = null;
				p2 = null;
			}
		} else {
			// TOOLS
			g2.drawLine(previousX, previousY, currentX, currentY); // Brush tool
		}

		// Set previous coordinates
		previousX = currentX;
		previousY = currentY;

		repaint();
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
		String send = Protocol.CHANGE_BRUSH_SIZE + " " 
				+ size;
		buffer.put(send);
	}
	
	public void sendChangeBrushcolorCommandToserver(Color color){
		String send = Protocol.CHANGE_BRUSH_COLOR + " " 
				+ color.getRGB();
		buffer.put(send);
	}

	// returns stroke
	public Stroke getStroke() {
		return stroke;
	}

	// sets stroke
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
	
	// experimental
	public void drawLine() {
		waitForShape = true;
	}

	public void drawLine(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
		Graphics2D g2 = bufImage.createGraphics();
		g2.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g2.setColor(new Color(rgb));
		g2.drawLine(previousX, previousY, currentX, currentY);
		repaint();
	}

	public void mousePressed(MouseEvent e) {
//		drawStuff(e);
		// Check if we are waiting a shape to be drawn (works only for lines for the moment)
		if (waitForShape) {
			if (p1 == null) {
				p1 = new Point(e.getX(), e.getY());
				previousX = p1.x;
				previousY = p1.y;
			} else if (p2 == null) {
				p2 = new Point(e.getX(), e.getY());
				waitForShape = false;
				drawBrush(e);
				p1 = null;
				p2 = null;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
//		drawStuff(e);
		if (!waitForShape) {
			drawBrush(e);
		}
	}

	/*
	 * This method doesn't actually draw anything, it just puts a draw command into the SendBuffer.
	 */
	private void drawBrush(MouseEvent e) {
		// Set mouse coordinates
		currentX = e.getX();
		currentY = e.getY();
		
		// Need something better than -90000 here... Ugly.
		if (previousX == -90000 && previousY == -90000) {
			previousX = currentX;
			previousY = currentY;
		}
		
		String send = Protocol.DRAW_LINE + " " 
					+ previousX + " " 
					+ previousY + " " 
					+ currentX + " " 
					+ currentY;
		
		buffer.put(send);

		// Set previous coordinates
		previousX = currentX;
		previousY = currentY;
	}

	public void mouseReleased(MouseEvent e) {
		// Resetting previous coordinates
		if (!waitForShape) {
			previousX = -90000;
			previousY = -90000;
		}
	}

	public void mouseMoved   (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}

	public void insertPicture(File f) {
		BufferedImage img = null;
		try {
			System.out.println("pic ahead");
		    img = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Graphics2D gc = bufImage.createGraphics();
		gc.drawImage(img, null, 0, 0);
		repaint();
		
		buffer.put(Protocol.SEND_FILE + " " + f.length());
		buffer.bufferFile(f);
	
		
	}

	public SendBuffer getBuffer() {
		return buffer;
	}

}
