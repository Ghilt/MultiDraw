package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import utils.ImageWrapper;

public class InvisiblePanel extends JPanel implements MouseListener, MouseMotionListener {
	private ImageWrapper bufImage;
	
	// The panel it is covering.
	private PaintPanel paintpanel;

	// Canvas size
	public static final int SIZE_X = 900;
	public static final int SIZE_Y = 780;

	// Mouse pointer coordinates
	private int currentX = 0;
	private int currentY = 0;
	private int previousX = -90000;
	private int previousY = -90000;

	private boolean dragging = false;

	/**
	 * A "canvas" used to draw on.
	 */
	public InvisiblePanel(PaintPanel paintpanel) {
		super();
		this.setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
		this.setOpaque(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.paintpanel = paintpanel;

		// Initializing bufImage
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D
		
		BufferedImage img = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D test = img.createGraphics();
		this.print(test);
		test.dispose();
	}
	
	public void drawLine(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		bufImage.drawLine(previousX, previousY, currentX, currentY, rgb, width);
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		previousX = e.getX();
		previousY = e.getY();
		dragging  = true;
		MouseEvent me = new MouseEvent(paintpanel, // which
			    MouseEvent.MOUSE_PRESSED, // what
			    System.currentTimeMillis(), // when
			    0, // no modifiers
			    e.getX(), e.getY(), // where: at (10, 10}
			    1, // only 1 click 
			    false); // not a popup trigger

		paintpanel.dispatchEvent(me);
	}

	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			currentX = e.getX();
			currentY = e.getY();
			drawLine(previousX, previousY, currentX, currentY, Color.BLACK.getRGB(), 20);
		}
		MouseEvent me = new MouseEvent(paintpanel, // which
			    MouseEvent.MOUSE_DRAGGED, // what
			    System.currentTimeMillis(), // when
			    0, // no modifiers
			    e.getX(), e.getY(), // where: at (10, 10}
			    1, // only 1 click 
			    false); // not a popup trigger

		paintpanel.dispatchEvent(me);
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		
		MouseEvent me = new MouseEvent(paintpanel, // which
			    MouseEvent.MOUSE_RELEASED, // what
			    System.currentTimeMillis(), // when
			    0, // no modifiers
			    e.getX(), e.getY(), // where: at (10, 10}
			    1, // only 1 click 
			    false); // not a popup trigger
		paintpanel.dispatchEvent(me);
		
		repaint();
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
