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
					  currentY;

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
					  currentY;

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

	public void mousePressed(MouseEvent e) {
		switch (tp.getTool()) {
			case ClientToolProperties.BRUSH_TOOL:
				previousX = e.getX();
				previousY = e.getY();
				sendDrawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(), tp.getBrushWidth());
				break;
			case ClientToolProperties.PEN_TOOL:
				previousX = e.getX();
				previousY = e.getY();
				sendDrawPen(previousX, previousY, e.getX(), e.getY(), tp.getColor(), 1);
				break;
			case ClientToolProperties.LINE_TOOL:
				previousX = e.getX();
				previousY = e.getY();
				break;
		}
	}

	public void mouseDragged(MouseEvent e) {
		switch (tp.getTool()) {
			case ClientToolProperties.BRUSH_TOOL:
				sendDrawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(), tp.getBrushWidth());
				previousX = e.getX();
				previousY = e.getY();
				break;
			case ClientToolProperties.PEN_TOOL:
				sendDrawPen(previousX, previousY, e.getX(), e.getY(), tp.getColor(), 1);
				previousX = e.getX();
				previousY = e.getY();
				break;
			case ClientToolProperties.LINE_TOOL:
				drawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(), tp.getBrushWidth());
				break;
		}
	}

	public void mouseReleased(MouseEvent e) {
		switch (tp.getTool()) {
			case ClientToolProperties.LINE_TOOL:
				sendDrawLine(previousX, previousY, e.getX(), e.getY(), tp.getColor(), tp.getBrushWidth());
				bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
				repaint();
				break;
		}
		
		// Reset previous
		previousX = -90000;
		previousY = -90000;
	}

	public void mouseMoved(MouseEvent e) {
		switch (tp.getTool()) {
			case ClientToolProperties.BRUSH_TOOL:
				drawLine(e.getX(), e.getY(), e.getX(), e.getY(), tp.getColor(), tp.getBrushWidth());
				break;
			case ClientToolProperties.LINE_TOOL:
				drawLine(e.getX(), e.getY(), e.getX(), e.getY(), tp.getColor(), tp.getBrushWidth());
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
	
	
	
	

	/*
	 * This method doesn't actually draw anything, it just puts a draw command
	 * into the SendBuffer.
	 */
//	public void drawBrush(MouseEvent e) {
//		// Set mouse coordinates
//		currentX = e.getX();
//		currentY = e.getY();
//
//		// Need something better than -90000 here... Ugly.
//		if (previousX == -90000 && previousY == -90000) {
//			previousX = currentX;
//			previousY = currentY;
//		}
//
//		String send = Protocol.DRAW_LINE + " " + previousX + " " + previousY + " " + currentX + " " + currentY;
//
//		buffer.put(send);
//
//		// Set previous coordinates
//		previousX = currentX;
//		previousY = currentY;
//	}

}
