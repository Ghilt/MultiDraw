package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	// Stores underlying image. (Maybe we can have a Vector of BufferedImages, then we could probably implement Ctrl+Z etc.)
	private BufferedImage bufImage;
	
	// Temporary storage for convolution filters
	private BufferedImage bufDest;

	// Canvas size
	private static final int SIZE_X = 900;
	private static final int SIZE_Y = 700;

	// Mouse pointer coordinates
	private int currentX = 0;
	private int currentY = 0;
	private int previousX = -90000;
	private int previousY = -90000;

	// Current brush properties
	private Color brushColor = Color.BLACK;
	private BasicStroke stroke;

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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (bufImage == null) {
			// Initializing bufImage
			bufImage = (BufferedImage)createImage(getWidth(), getHeight());
			bufImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D gc = bufImage.createGraphics();

			// Fill background with white color
			gc.setColor(Color.WHITE);
			gc.fillRect(0, 0, getWidth(), getHeight());
		}
		
		if (stroke == null) {
			// Initializing stroke
			stroke = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
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
	
	private void drawBrush(Graphics2D g2) {
		g2.drawLine(previousX, previousY, currentX, currentY);
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
		
		// TOOLS
		drawBrush(g2); // Brush tool
		
		// Set previous coordinates
		previousX = currentX;
		previousY = currentY;
		
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		drawStuff(e);
	}

	public void mouseDragged(MouseEvent e) {
		drawStuff(e);
	}

	public void mouseReleased(MouseEvent e) {
		// Resetting previous coordinates
		previousX = -90000;
		previousY = -90000;
	}

	public void mouseMoved   (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
}