package network;

import java.util.Vector;

public class SendBuffer {
	Vector<String> buffer;
	private int size;
	private byte[] image;
	boolean paused;

	public SendBuffer(int size) {
		paused = false;
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
			while (buffer.size() == 0 || paused) {
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

	public synchronized void unpause() {
		notifyAll();
		paused = false;
	}
	public synchronized void pause() {
		paused = true;
	}
	
}
