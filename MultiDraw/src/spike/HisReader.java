package spike;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;



public class HisReader {

    public static void main(String [] args) {

    	args[0] = "";
    	args[1] = "8199";
    	args[2] = "2000";
    	
	if (args.length!=3) {
	    System.out.println("Usage: java NIOReader address port delay");
	    System.exit(0);
	}

	int port = 0;
	try {
	    port = Integer.parseInt(args[1]);
	} catch(Exception e) {
	    System.out.println("Unrecognized port number!");
	    System.exit(1);
	}

	long sleep = 0;
	try {
	    sleep = Integer.parseInt(args[2]);
	} catch(Exception e) {
	    System.out.println("Illegal sleep value !");
	    System.exit(1);
	}
	
	try {
	    InetSocketAddress address = new InetSocketAddress(port);
	    SocketChannel connection = SocketChannel.open(address);
	    ByteBuffer buffer = ByteBuffer.allocate(8);

	    int r = 0;

	    while (r!=-1) {
		if (sleep>0) {
		    try {
			Thread.sleep(sleep);
		    } catch(InterruptedException e) {}
		}
		buffer.clear();
		while (buffer.hasRemaining() && r!=-1) {
		    r = connection.read(buffer);
		}
		if (r!=-1) {
		    buffer.rewind();
		    long l = buffer.getLong();
		    System.out.println(l);
		}
	    }
	    connection.close();

	} catch(IOException e) {
	    e.printStackTrace();
	}

    }

}