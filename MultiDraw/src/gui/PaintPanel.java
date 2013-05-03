package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import utils.ImageWrapper;

public class PaintPanel extends JPanel {
	private ImageWrapper bufImage;

	// Canvas size
	public static final int SIZE_X = 900;
	public static final int SIZE_Y = 780;

	/**
	 * A "canvas" used to draw on.
	 */
	public PaintPanel() {
		setPreferredSize(new Dimension(SIZE_X, SIZE_Y));

		// Initializing bufImage
		bufImage = new ImageWrapper(SIZE_X, SIZE_Y);
		bufImage.setWhiteBackground();
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D
		g2.drawImage(bufImage, null, 0, 0);
	}

	public void drawLine(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
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
}