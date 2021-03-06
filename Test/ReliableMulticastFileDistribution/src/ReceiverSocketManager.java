import java.net.DatagramPacket;


public class ReceiverSocketManager extends SocketManager implements Runnable {

	private FileCombinerPlus fc = null;
	
	private Thread t;
	
	public ReceiverSocketManager(FileCombinerPlus fc) {
		super();
		this.fc = fc;
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void finalize()
	{
		super.finalize();
	
//		try {
//			this.t.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void run() {
		
		while(fc.getNextSeqNum() <= fc.getBlockNum())
		{
			DatagramPacket pack = this.recievePacket();
			
			if(pack == null){
				
				//File Transmission Complete
				if(fc.getNextSeqNum() > fc.getBlockNum())
					break;
				
				System.err.println("Socket Failed to Receive a Packet");
				continue;
			}
			else
			{
				int seqN = SocketManager.getPacketSeqNum(pack);
//				System.out.println("Receive data packet:"+ seqN);
				if(seqN >= fc.getNextSeqNum() && !fc.isWritten(pack))
				{	
					fc.putDatapacketIntoBuffer(pack);
					fc.restart();
				}
				
			}		
		}
		
		System.out.println("Receiver Listener Ended with fc.NextSeqNum:"+fc.getNextSeqNum());
		
//		try {
//			Thread.currentThread().join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//this.finalize();
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileCombinerPlus fc = new FileCombinerPlus("/Users/Freehan/Desktop/Test/copy43422",43422L); //43422/28
		ReceiverSocketManager rsm = new ReceiverSocketManager(fc);
		fc.setSm(rsm);
		
		System.out.println("Main Thread ends");

	}

}
