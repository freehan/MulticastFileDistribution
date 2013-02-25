import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class MulticastSocketManager {

	/**
	 * @param args
	 */
		
	public static byte[] intToByteArray(int i) throws Exception { 
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); 
		DataOutputStream out = new DataOutputStream(buf); 
		System.out.println("i:" + i); 
		out.writeInt(i); 
		byte[] b = buf.toByteArray(); 
		System.out.println("i:" + b); 
		out.close(); 
		buf.close(); 
		return b; 
	} 
	
	public static byte[] longToByteArray(long i) throws Exception { 
		ByteArrayOutputStream buf = new ByteArrayOutputStream(); 
		DataOutputStream out = new DataOutputStream(buf); 
		System.out.println("i:" + i); 
		out.writeLong(i);
		byte[] b = buf.toByteArray(); 
		System.out.println("i:" + b); 
		out.close(); 
		buf.close(); 
		return b; 
	} 
	
	public static int ByteArrayToInt(byte b[]) throws Exception { 
		int temp = 0; 
		ByteArrayInputStream buf = new ByteArrayInputStream(b); 
		
		
		temp = buf.read();
		return temp;
	}
	
	public static long ByteArrayToLong(byte b[]) throws Exception { 
		long temp = 0; 
		ByteArrayInputStream buf = new ByteArrayInputStream(b); 

		temp = buf.read();
		return buf.read(); 
	}
	
	public static byte[] toByteArray(int value) {
	     return  ByteBuffer.allocate(4).putInt(value).array();
	}

	public static byte[] toByteArray2(int value) {
	    return new byte[] {
	        (byte) (value >> 24),
	        (byte) (value >> 16),
	        (byte) (value >> 8),
	        (byte) value};
	}

	public static int fromByteArray(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).getInt();
	}

	public static int fromByteArray2(byte[] bytes) {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println("check");
//		
//
//		byte[] buf = new byte[1000];
//		
//		
//		byte[] tmp = intToByteArray(1);
//
//
//		byte[] tmp2 = toByteArray2(-2123123123);
//		System.out.println(fromByteArray2(tmp));
		
		RandomAccessFile raf1 = new RandomAccessFile("/Users/Freehan/Desktop/Test/test.txt", "rw");
		RandomAccessFile raf2 = new RandomAccessFile("/Users/Freehan/Desktop/Test/test.txt", "rw");
		
		
		
	}


}

