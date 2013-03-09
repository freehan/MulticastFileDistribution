import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class FileCombiner extends FileSeperator implements Runnable{

	volatile private int lastSeqNum;
	private HashMap<Integer,DatagramPacket> packBuff;
	private Thread t;
	
	public FileCombiner(){};
	
	public FileCombiner(String FileName, long blockNum){
		super(FileName, blockNum);
		
		this.lastSeqNum = 1;
		this.packBuff = new HashMap<Integer,DatagramPacket>();
		
		t = new Thread(this);
		t.start();
	};
	
	synchronized public void putPack(int seqNum, DatagramPacket pack)
	{
		packBuff.put(seqNum, pack);
	}
	
	synchronized public DatagramPacket getPack(int seqNum)
	{
		DatagramPacket re = packBuff.remove(seqNum);
		return re;
	}
	
	public void restart(){
		t.interrupt();
	}
	
	@Override
	public void run() {
		System.out.println("start running");
		while(this.lastSeqNum<=this.blockNum)
		{
			
			DatagramPacket pack = getPack(this.lastSeqNum);
			if(pack == null)
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			else
			{
				System.out.println("Writing Packet");
				this.writeDataPacketToFile(pack);
				//this.lastSeqNum++;
			}
			
		}
		
		System.out.println("Finish Writing File");
	}
	
	
	public int getLastSeqNum() {
		return lastSeqNum;
	}

	public void setLastSeqNum(int lastSeqNum) {
		this.lastSeqNum = lastSeqNum;
	}
	
	
	
	public boolean writeByteToFile(byte[] buf, int offset, int length){
		
		try {
			this.raf.write(buf, offset, length);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Write Byte to file failed!");
			return false;			
		}
	}
	
	public boolean writeDataPacketToFile(DatagramPacket pack)
	{
		this.lastSeqNum++;
		return writeByteToFile(pack.getData(), 4, pack.getLength()-4);
		 
		
	}
	

	public static void main(String[] args) {

//		FileCombiner fc = new FileCombiner("/Users/Freehan/Desktop/Test/test.txt", (long)200);		
//		SocketManager Receiver = new SocketManager();
//		
//		System.out.println("Start Listening");
//		for(int i=0;i<200;i++){
//			DatagramPacket pack = Receiver.recievePacket();
//			if(pack == null){
//				System.err.println("Socket Failed to Receive a Packet");
//				continue;
//			}
//			System.out.println(Receiver.getPacketSeqNum(pack));
////			System.out.println("Packet Payload Size:"+ pack.getLength());
//			
//			fc.writeDataPacketToFile(pack);
//			System.out.println("LastSeqNum"+fc.getLastSeqNum());
//			
//		}
//		
//		System.out.println(fc.getLastSeqNum());
		
		FileCombiner fc = new FileCombiner("C:\\Users\\Minhan\\Desktop\\test\\test.out",(long)66877);
		SocketManager Receiver = new SocketManager();
		
		System.out.println("Start Listening");
		
		
		int index = 1;
		
		while(index <= fc.getBlockNum()){
			DatagramPacket pack = Receiver.recievePacket();
			if(pack == null){
				System.err.println("Socket Failed to Receive a Packet");
				continue;
			}
			else
			{
				int seqN = SocketManager.getPacketSeqNum(pack);
				fc.putPack(seqN, pack);
				if(fc.getLastSeqNum()+1 == seqN)
					fc.restart();
				
				System.out.println("Receive Seq# " + seqN);
				index++;
			}		
		}
		
	}

	

}
