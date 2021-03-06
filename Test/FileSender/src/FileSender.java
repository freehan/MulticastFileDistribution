import sun.net.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class FileSender {

	// Import some needed classes

	// Which port should we listen to
	static int port = 5000;
	// Which address
	static String group = "225.4.5.6";
	// Which ttl
	static int ttl = 1;
	
	public FileSender(){};
	
	public FileSender(int port, String group, int ttl)
	{
		this.port = port;
		this.group = group;
		this.ttl = ttl;
	};
	
	
	
	
	public static boolean sendPackage(byte buf[])
	{
		try {
			MulticastSocket s;
			// Create the socket but we don't bind it as we are only going to send data
			s = new MulticastSocket();
			// Note that we don't have to join the multicast group if we are only
			// sending data and not receiving
				
			// Create a DatagramPacket 
			DatagramPacket pack = new DatagramPacket(buf, buf.length,
								 InetAddress.getByName(group), port);
			// Do a send. Note that send takes a byte for the ttl and not an int.
			//s.send(pack,(byte)ttl);
			s.setTimeToLive(ttl);

			s.send(pack);
			// And when we have finished sending data close the socket
			s.close();
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public static byte[] intToByteArray(int value) {
	     return  ByteBuffer.allocate(4).putInt(value).array();
	}

	
	public static void main(String[] args) {


		FileSender fs = new FileSender();
		
		
		
		
		try {
			byte[] tmp = intToByteArray(10);
			fs.sendPackage(tmp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
