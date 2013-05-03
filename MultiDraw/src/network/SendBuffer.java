package network;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import utils.Protocol;

public class SendBuffer {
	Vector<String> buffer;
	private int size;
	private byte[] image;

	public SendBuffer(int size) {
		this.size = size;
		buffer = new Vector<String>(size);
	}

	public synchronized void put(String s) {
		try {
			while (buffer.size() >= size) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		buffer.add(s);
		notifyAll();
	}

	public synchronized String pop() {
		try {
			while (buffer.size() == 0) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String cmd = buffer.remove(0);
		notifyAll();
		return cmd;
	}

	public byte[] popImage() {
		byte[] img = image;
		image = null;
		return img;
	}

	public void putImage(byte[] imageInByte) {
		image = imageInByte;
	}
	
	public void putImportedImage(File f) {
		try {
			System.out.println("File loaded, sending to server");
			BufferedImage img = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "PNG", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			putImage(imageInByte);
			put(Protocol.SEND_FILE + " " + imageInByte.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
