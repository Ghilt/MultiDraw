package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class SendBuffer {
	Vector<String> buffer;
	private int size;
	private File file;

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

	public void bufferFile(File f) {
		this.file = f;
		
	}

	public File getFile() {
		return file;
	}

	
}
