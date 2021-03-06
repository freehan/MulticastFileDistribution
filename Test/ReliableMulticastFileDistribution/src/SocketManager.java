import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;


public class SocketManager {

	// Import some needed classes

	// Which port should we listen to
	static int port = 5000;
	
	// Which address
	static String group = "225.4.5.6";
	// Which ttl
	static int ttl = 2;

	static MulticastSocket soc;
	
	
	public SocketManager(){
		try {
			soc = new MulticastSocket(this.port);
			soc.joinGroup(InetAddress.getByName(this.group));	
			soc.setTimeToLive(ttl);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("CANNOT CREATE Multicast Socket");
		}
	}
	
	protected void finalize()
	{
		soc.close();
		System.out.println("Multicast Socket closed");
	};
	
	public static int getPort() {
		return port;
	}


	public static void setPort(int port) {
		SocketManager.port = port;
	}


	public static String getGroup() {
		return group;
	}


	public static void setGroup(String group) {
		SocketManager.group = group;
	}


	public static int getTtl() {
		return ttl;
	}


	public static void setTtl(int ttl) {
		SocketManager.ttl = ttl;
	}

	//Helper functions
	
	public static byte[] convertLongToBytes(long l) {

		byte[] b = new byte[8];
		b = java.lang.Long.toString(l).getBytes();
		return b;
	}

	public static long convertBytesToLong(byte[] b) {

		long l = 0L;
		l = java.lang.Long.parseLong(new String(b));
		return l;
	}
	
	public static byte[] intToByteArray(int value) {
	     return  ByteBuffer.allocate(4).putInt(value).array();
	}

	public static byte[] intToByteArray2(int value) {
	    return new byte[] {
	        (byte) (value >> 24),
	        (byte) (value >> 16),
	        (byte) (value >> 8),
	        (byte) value};
	}
	
	public static int byteArrayToInt(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).getInt();
	}
	
	public static int byteArrayToInt2(byte[] bytes) {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	public static byte[] byteArrayConcatination(byte[] a, byte[] b)
	{
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	
	public static boolean sendPacket(byte [] buf){
		if(buf == null)
			return false;
		
		try {
//			soc.setTimeToLive(ttl);
			DatagramPacket pack = new DatagramPacket(buf, buf.length,
					 InetAddress.getByName(group), port);
			soc.send(pack);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Send Data Packet Failed");
			return false;
		}
		
	}
	
	//Standard Packet is 
	public boolean sendStdPacket(int seqNum, byte [] buf){
		byte[] seq = intToByteArray(seqNum);
		byte[] payload = null;
		
		//If buf is null still send seqNum as the payload
		if(buf != null)
			payload = byteArrayConcatination(seq, buf);
		else
			payload = seq;
		
		
		try {
//			soc.setTimeToLive(ttl);
			DatagramPacket pack = new DatagramPacket(payload, payload.length,
					 InetAddress.getByName(group), port);
//			System.out.println("Sending Packet Seq"+ seqNum + "  payload length: " +payload.length);
			soc.send(pack);	
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Send Standard Data Packet Failed");
			return false;
		}
		
	}
	
	public DatagramPacket recievePacket()
	{
		byte buf[] = new byte[1500];//MTU: 1500 byte payload - 20 IP header - 8 UDP header
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			this.soc.receive(pack);
			return pack;
		} catch (IOException e) {
			System.err.println("Receive Data Packet Failed");
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static int getPacketSeqNum(DatagramPacket pack)
	{
		if(pack == null)
		{
			System.err.println("Input Data Packet Is NULL");
			return 0;
		}

		return byteArrayToInt2(pack.getData());
	}
	
	public static byte[] getPacketData(DatagramPacket pack)
	{
		if(pack == null)
		{
			System.err.println("Input Data Packet Is NULL");
			return null;
		}else if(pack.getData().length<=4)
		{
			System.err.println("Input Data Packet has not data payload");
			return null;
		}
		
		byte[] buf = new byte[pack.getLength()-4];
		
		System.arraycopy(pack.getData(), 4, buf, 0, pack.getLength()-4);
		
		return buf;
	}	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SocketManager sender = new SocketManager();
		sender.sendStdPacket(3, null);
		
	}

}
