package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import tools.ClientToolProperties;
import utils.ImageWrapper;
import utils.Protocol;

public class InvisiblePanel extends JPanel implements MouseListener, MouseMotionListener {
	private SendBuffer buffer;
	private ImageWrapper bufImage;

	// Canvas size
	public static final int SIZE_X = 900;
	public static final int SIZE_Y = 780;

	// Mouse pointer coordinates
	private int previousX = -90000;
	private int previousY = -90000;
	
	private int colorType = Protocol.BRUSH_COLOR_1;
	
	// Information about currently selected tool
	private ClientToolProperties tp;

	/**
	 * A "canvas" used to draw on.
	 */
	public InvisiblePanel(SendBuffer buffer, ClientToolProperties tp) {
		super();
		this.setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
		this.setOpaque(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.tp = tp;
		this.buffer = buffer;

		// Initializing bufImage
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D

		g2.drawImage(bufImage, null, 0, 0);
	}
	
	public void drawLine(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		bufImage.drawLine(previousX, previousY, currentX, currentY, rgb, width);
		repaint();
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
					  currentY + " " +
					  colorType;

		buffer.put(send);
	}
	
	/*
	 * This method doesn't actually draw anything, it just puts a draw command
	 * into the SendBuffer.
	 */
	public void sendDrawPen(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
		String send = Protocol.DRAW_PEN + " " + 
					  previousX + " " + 
					  previousY + " " + 
					  currentX + " " + 
					  currentY + " " +
					  colorType;

		buffer.put(send);
	}

	public void mousePressed(MouseEvent e) {
		// Clear top layer
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		repaint();
		
		colorType = Protocol.BRUSH_COLOR_1;
		if (e.getButton() == 2 || e.getButton() == 3) { // getButton returnerar olika beroende på mus.. MouseEvent.BUTTON2 är inte högerklick för mig t.ex.
			colorType = Protocol.BRUSH_COLOR_2;
		}
		previousX = e.getX();
		previousY = e.getY();
		switch (tp.getTool()) {
			case ClientToolProperties.BRUSH_TOOL:
				sendDrawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(colorType), tp.getBrushWidth());
				break;
			case ClientToolProperties.PEN_TOOL:
				sendDrawPen(previousX, previousY, e.getX(), e.getY(), tp.getColor(colorType), 1);
				break;
			case ClientToolProperties.LINE_TOOL:
				break;
		}
	}

	public void mouseDragged(MouseEvent e) {
		switch (tp.getTool()) {
			case ClientToolProperties.BRUSH_TOOL:
				if (previousX != -90000) {
					sendDrawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(colorType), tp.getBrushWidth());
				}
				previousX = e.getX();
				previousY = e.getY();
				break;
			case ClientToolProperties.PEN_TOOL:
				if (previousX != -90000) {
					sendDrawPen(previousX, previousY, e.getX(), e.getY(), tp.getColor(colorType), 1);
				}
				previousX = e.getX();
				previousY = e.getY();
				break;
			case ClientToolProperties.LINE_TOOL:
				if (previousX != -90000) {
					drawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(colorType), tp.getBrushWidth());
				}
				break;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (previousX != -90000) {
			switch (tp.getTool()) {
				case ClientToolProperties.LINE_TOOL:
					sendDrawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(colorType), tp.getBrushWidth());
					bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
					repaint();
					break;
			}
		}
		
		// Reset previous
		previousX = -90000;
		previousY = -90000;
		colorType = Protocol.BRUSH_COLOR_1;
	}

	public void mouseMoved(MouseEvent e) {
		switch (tp.getTool()) {
			case ClientToolProperties.BRUSH_TOOL:
				drawLine(e.getX(), e.getY(), e.getX(), e.getY(), tp.getColor(Protocol.BRUSH_COLOR_1), tp.getBrushWidth());
				break;
			case ClientToolProperties.LINE_TOOL:
				drawLine(e.getX(), e.getY(), e.getX(), e.getY(), tp.getColor(Protocol.BRUSH_COLOR_1), tp.getBrushWidth());
				break;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		// Clear top layer
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
	}
}
