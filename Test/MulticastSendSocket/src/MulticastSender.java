import sun.net.*;

import java.io.IOException;
import java.net.*;

public class MulticastSender {

	
	// Import some needed classes

	// Which port should we listen to
	static int port = 5000;
	// Which address
	static String group = "225.4.5.6";
	// Which ttl
	static int ttl = 1;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		try {
			MulticastSocket s;
			// Create the socket but we don't bind it as we are only going to send data
			s = new MulticastSocket();
			// Note that we don't have to join the multicast group if we are only
			// sending data and not receiving
			// Fill the buffer with some data
			byte buf[] = new byte[10];
			for (int i=0; i<buf.length; i++) buf[i] = (byte)i;
			// Create a DatagramPacket 
			DatagramPacket pack = new DatagramPacket(buf, buf.length,
								 InetAddress.getByName(group), port);
			// Do a send. Note that send takes a byte for the ttl and not an int.
			//s.send(pack,(byte)ttl);
			s.setTimeToLive(ttl);
			s.send(pack);
			// And when we have finished sending data close the socket
			s.close();
			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("HIHI");
	
	}

}
