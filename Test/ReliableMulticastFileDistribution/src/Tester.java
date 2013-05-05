import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.Date;
import java.util.Arrays;


public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SocketManager sm = new SocketManager();
		FileSeperator fs = new FileSeperator("/Users/Freehan/Desktop/Test/Test");
		fs.getFileAttribute();
		//fs.printFileAttribute();
		
//TEST FOR SENDER RECOVERY LISTENER
//		int testSqnNum = -3;
//		Date now = new Date();
//		byte[] timeStamp = SocketManager.convertLongToBytes(now.getTime());
		
		
		
//		sm.sendStdPacket(testSqnNum, timeStamp);
		
		
//		System.out.println("Waiting");
//		DatagramPacket pack = sm.recievePacket();
//		System.out.println("SeqNum:" + SocketManager.getPacketSeqNum(pack));
//		
//		pack = sm.recievePacket();
//		System.out.println("SeqNum:" + SocketManager.getPacketSeqNum(pack));
		
		
//		byte[] buf1 = fs.getDataBlock(Math.abs(testSqnNum));
//		byte[] buf2 = SocketManager.getPacketData(pack);
//		System.out.println(buf1.length);
//		System.out.println(buf2.length);
		
		
//		if(Arrays.equals(buf1, buf2))
//			System.out.println("The Same");
		
		
		
		
		int seqN = 1;
		byte[] data = fs.getDataBlock(seqN);
		sm.sendStdPacket(seqN, data);	
	}

}
