import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


public class Test {

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
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("check");
		
		byte[] buf = new byte[1000];
		
		
		byte[] tmp = intToByteArray(1);

		
		
	}


}


