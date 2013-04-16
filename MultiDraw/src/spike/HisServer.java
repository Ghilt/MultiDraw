package spike;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class HisServer {

    public static void main(String [] args) {

    args[0] = "8199";
    	
	if (args.length!=1) {
	    System.out.println("Usage: java NIOServer3 port");
	    System.exit(0);
	}

	int port = 0;
	try {
	    port = Integer.parseInt(args[0]);
	} catch(Exception e) {
	    System.out.println("Unrecognized port number!");
	    System.exit(1);
	}
	
	try {
	    ServerSocketChannel server = ServerSocketChannel.open();
	    ServerSocket ss = server.socket();
	    ss.bind(new InetSocketAddress(port));
	    server.configureBlocking(false);

	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    
	    SocketChannel channels[] = new SocketChannel[2];
	    long nr[] = new long[2];
	    SelectionKey keys[] = new SelectionKey[2];

	    Selector selector = Selector.open();
	    server.register(selector,SelectionKey.OP_ACCEPT);


	    while (true) {

		selector.select();

		Set ready = selector.selectedKeys();
		Iterator iterator = ready.iterator();

		while (iterator.hasNext()) {
		    SelectionKey key = (SelectionKey) iterator.next();
		    iterator.remove();

		    if (key.isAcceptable()) {
			SocketChannel channel = server.accept();
			System.out.println("Accept");
			if (channels[0]==null) {
			    channels[0] = channel;
			    channel.configureBlocking(false);
			    keys[0] = channel.register(selector,SelectionKey.OP_WRITE);
			    nr[0] = 0;
			} else {
			    if (channels[1]==null) {
				channels[1] = channel;
				channel.configureBlocking(false);
				keys[1] = channel.register(selector,SelectionKey.OP_WRITE);
				nr[1] = 0;
			    } else {
				channel.close();
			    }
			}
		    } else {
			if (key.isWritable()) {
			    for(int n=0;n<2;n++) {
				if (channels[n]==(SocketChannel)key.channel()) {
				    try {
					buffer.clear();
					buffer.putLong(nr[n]++);
					buffer.flip();
					while(buffer.hasRemaining() && channels[n].write(buffer)!=-1);
				    } catch(IOException e) {
					System.out.println("Channel closed");
					channels[n].close();
					keys[n].cancel();
					channels[n] = null;
				    }
				}
			    }
			}
		    }
		}
	    }

	} catch(IOException e) {
	    e.printStackTrace();
	}

    }

}