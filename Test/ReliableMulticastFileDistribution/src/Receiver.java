import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;


public class Receiver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SocketManager Receiver = new SocketManager();
		
		
		try {
			RandomAccessFile raf = new RandomAccessFile("/Users/Freehan/Desktop/Test/test.wmv", "rw");
			System.out.println("Start Listening");
			for(int i=0;i<200;i++){
				DatagramPacket pack = Receiver.recievePacket();
				if(pack == null){
					System.err.println("Socket Failed to Receive a Packet");
					continue;
				}
				System.out.println(Receiver.getPacketSeqNum(pack));
				System.out.println("Packet Payload Size:"+ pack.getLength());
				
				
				raf.write(pack.getData(), 4, pack.getLength()-4);
				
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			System.err.println("FileNotFoundException");
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Write throws IOException");
			
			
			e.printStackTrace();
		}
	}

}
