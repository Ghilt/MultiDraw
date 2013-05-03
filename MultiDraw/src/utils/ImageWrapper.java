package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageWrapper extends BufferedImage {
	private int sizeX;
	private int sizeY;
	private Graphics2D g2;

	// Temporary storage for convolution filters (blur etc.)
	private BufferedImage bufDest;

	public ImageWrapper(int sizeX, int sizeY) {
		super(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.g2 = createGraphics();
	}
	
	public void setWhiteBackground() {
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.sizeX, this.sizeY);
	}
	
	public void drawLine(int previousX, int previousY, int currentX, int currentY, int rgb, int width) {
		g2.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g2.setColor(new Color(rgb));
		g2.drawLine(previousX, previousY, currentX, currentY);
	}

	public void insertPicture(File f) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (img != null)
			g2.drawImage(img, null, 0, 0);
	}

	public void insertPicture(BufferedImage img) {
		g2.drawImage(img, null, 0, 0);
	}

	public void blur() {
		float[] blurKernel = { 
			1/9f, 1/9f, 1/9f,
			1/9f, 1/9f, 1/9f,
			1/9f, 1/9f, 1/9f
		};

		BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
		blur.filter(this, bufDest);
	}
}
