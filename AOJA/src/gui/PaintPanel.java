package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PaintPanel extends JPanel implements MouseListener, MouseMotionListener {
	// stores underlying image
	private BufferedImage bufImage = null;
	
	// Temp storage for convolution filters
	private BufferedImage bufSrc = null;
	private BufferedImage bufDest = null;

	// size of paint area
	private static final int SIZE_X = 900;
	private static final int SIZE_Y = 700;

	// Brush types
	public static final int OVAL = 0;
	public static final int RECT = 1;

	// Mouse pointer coordinates
	private int currentStartX = 0;
	private int currentStartY = 0;

	// Current brush properties
	private int brushType = OVAL;
	private int brushSize = 6;
	private Color brushColor = Color.BLACK;

	/**
	 * A "canvas" used to draw on.
	 */
	public PaintPanel() {    	
		setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
		this.setBorder(BorderFactory.createEmptyBorder());
		this.addMouseListener(this); 
		this.addMouseMotionListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;  // downcast to Graphics2D
		if (bufImage == null) {
			// First time we're painting. Initialize bufImage
			bufImage = (BufferedImage)createImage(getWidth(), getHeight());
			Graphics2D gc = bufImage.createGraphics();

			// Fill background with white color
			gc.setColor(Color.WHITE);
			gc.fillRect(0, 0, getWidth(), getHeight());
		}

		// re-draw bufImage
		g2.drawImage(bufImage, null, 0, 0); 
	}

	public void blur() {
		float[] blurKernel = {
	        1/9f, 1/9f, 1/9f,
	        1/9f, 1/9f, 1/9f,
	        1/9f, 1/9f, 1/9f
	    };
	
	    BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
	    bufImage = blur.filter(bufImage, bufDest);
	}

	/**
	 * @param g2
	 * @param x
	 * @param y
	 */
	private void drawBrush(Graphics2D g2, int x, int y) {
		switch (brushType) {
		case OVAL:
			g2.fillOval(x - (brushSize / 2), y - (brushSize / 2), brushSize, brushSize);
			break;
		case RECT:
			g2.fillRect(x - (brushSize / 2), y - (brushSize / 2), brushSize, brushSize);
			break;
		default: // This should never happen
			g2.fillOval(x - (brushSize / 2), y - (brushSize / 2), brushSize, brushSize);
		}

	}

	public void mousePressed(MouseEvent e) {
		// Set coordinates, need to remember these for other features
		currentStartX = e.getX();
		currentStartY = e.getY();
		Graphics2D g2 = bufImage.createGraphics();
		switch(e.getModifiers()) {
		case 4:
			g2.setColor(Color.WHITE);
			break;
		case 16:
			g2.setColor(brushColor);
			break;
	}
		drawBrush(g2, currentStartX, currentStartY);
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		// Set coordinates, need to remember these for other features
		currentStartX = e.getX();
		currentStartY = e.getY();
		Graphics2D g2 = bufImage.createGraphics();
		switch(e.getModifiers()) {
			case 4:
				g2.setColor(Color.WHITE);
				break;
			case 16:
				g2.setColor(brushColor);
				break;
		}
		drawBrush(g2, currentStartX, currentStartY);
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		repaint();
	}

	public void mouseMoved   (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
}