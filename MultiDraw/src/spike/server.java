package spike;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class server {
	public static void main(String[] args) {
		System.out.println("Starting server");
		ServerSocketChannel server = null;
		try {
			server = ServerSocketChannel.open();
			ServerSocket socket = server.socket();
			SocketAddress address = new InetSocketAddress(8199);
			socket.bind(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true){
			try {
				SocketChannel cc = server.accept();
				System.out.println("Accepted  " + cc.toString());
				ClientConnection k = new ClientConnection("SameName", cc);
				k.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}


class ClientConnection extends Thread{
	
	private SocketChannel connection;
	private ByteBuffer buf;

	public ClientConnection(String name, SocketChannel connection) {
		super(name);
		this.connection = connection;
		this.buf = ByteBuffer.allocate(16);
	}
	
	public void run(){
		System.out.println("Started thread for " + getName());
		try {
			buf.clear();
			System.out.println(getName() + " : Buffer Cleared");
			buf.putInt(5);
			buf.putInt(5);
			buf.putInt(5);
			buf.putInt((int) Math.random()*50);
			
			while (buf.hasRemaining() && connection.write(buf)!=-1);
			
			System.out.println(getName() + " : Wrote buffer");
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}