import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;


public class another {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Date now = new Date();
//		System.out.println(now.getTime());
//
//		byte[] bytes= converLongToBytes(now.getTime());
//		
//		System.out.println(new String(bytes));
//		
//		
//		long value = converBytesToLong(bytes);
//		
//		
//		System.out.println(value);
		
		try {
			FileWriter fw = new FileWriter("/Users/Freehan/Desktop/Test/Test1");
			Random rd = new Random();
			
			for(int i=0;i<100*1000*1000;i++)
			{
				int some = rd.nextInt(255);
				fw.append((char)some);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	public static long toLong(byte[] b)

	{
		long l = 0;

		l = b[0];

		l |= ((long) b[1] << 8);

		l |= ((long) b[2] << 16);

		l |= ((long) b[3] << 24);

		l |= ((long) b[4] << 32);

		l |= ((long) b[5] << 40);

		l |= ((long) b[6] << 48);

		l |= ((long) b[7] << 56);

		return l;
	}

	public static byte[] converLongToBytes(long l) {

		byte[] b = new byte[8];
		b = java.lang.Long.toString(l).getBytes();
		return b;
	}

	public static long converBytesToLong(byte[] b) {

		long l = 0L;
		l = java.lang.Long.parseLong(new String(b));
		return l;
	}


}
