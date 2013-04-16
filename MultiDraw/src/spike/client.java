package spike;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class client {


	public static void main(String[] args) {
		
		SocketAddress address = new InetSocketAddress(8199);
		try {
			SocketChannel channel = SocketChannel.open(address);
			System.out.println("Succesfully opened channel");
			ByteBuffer buf = ByteBuffer.allocate(4);
			
			while(true){
				buf.clear();
				System.out.println("Buffer Cleared");
				channel.read(buf);
//				while(buf.hasRemaining() && channel.read(buf)!=-1);
				buf.rewind();
				System.out.println(buf.getInt());
//				System.out.println(buf.getInt(4));
			}
			

			
		
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
