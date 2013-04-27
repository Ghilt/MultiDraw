package network;

import java.util.Vector;

public class SendBuffer {
	Vector<String> buffer;
	private int size;
	private byte[] image;
	private boolean disabled;

	public SendBuffer(int size) {
		disabled = false;
		this.size = size;
		buffer = new Vector<String>(size);
	}

	public synchronized void put(String s) {
		if(!disabled){
			
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

	public void disable(boolean b) {
		this.disabled = b;
		
		
	}

	public boolean isDisabled() {
		return disabled;
	}
	
}
