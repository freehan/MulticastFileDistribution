import java.net.DatagramPacket;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class SenderRecoveryListener implements Runnable {

	private Thread t = null;
	private volatile HashSet<Integer> hs = null;
	private SocketManager sm = null;
	private FileSeperator fs = null;

	private final int poolSize = 4; //Resend 4
	private final ExecutorService resendTaskPool;


	//For Sender Recovery Listener
	public SenderRecoveryListener(SocketManager socket, FileSeperator seperator, int unused)
	{
		fs = seperator;
		sm = socket;
		hs = new HashSet<Integer>();
		resendTaskPool = Executors.newFixedThreadPool(poolSize);

		t = new Thread(this);
		t.setDaemon(true);
		t.start();

	}

	//For Receiver Recovery Listener
	public SenderRecoveryListener(SocketManager socket, FileSeperator seperator)
	{
		fs = seperator;
		sm = socket;
		hs = new HashSet<Integer>();
		resendTaskPool = Executors.newFixedThreadPool(poolSize);

	}
	
	public void startResendTask(int seqN, long timeElapse)
	{
		resendTaskPool.execute(new ResendPacketTask(sm,fs,seqN,timeElapse));
	}



	protected void finalize()
	{
		resendTaskPool.shutdownNow();
	}

	@Override
	public void run() {
		try{
			while(true)
			{
				DatagramPacket pack = sm.recievePacket();
				if(pack == null)
					break;
				int seqN = SocketManager.getPacketSeqNum(pack);
	//			System.out.println("Listener Received Packet SeqN: "+ seqN);
	
				if(seqN>0){
					if(hsContains(seqN))
						hsRemove(seqN);
	
				}
				else if(seqN < 0){
					//Calculate TimeElapse
					Date now = new Date();
					byte[] timestamp = SocketManager.getPacketData(pack);
					long timeElapse = now.getTime() - SocketManager.convertBytesToLong(timestamp);
					System.out.println("Recovery Request Packet:"+ seqN+" TimeElpase:"+timeElapse);
		
					
					seqN = Math.abs(seqN);
	
					//if Have not start a resend packet task
					if(!hsContains(seqN))
					{
						hsAdd(seqN);
						resendTaskPool.execute(new ResendPacketTask(sm, fs, seqN, timeElapse));
	
					}			
				}
			}
		}catch(Exception e)
		{
			System.err.println("Recovery Listener Exception. Listener Shut Down");
			e.printStackTrace();
			
		}
		
		System.out.println("Sender Recovery Listener Listener Shutting Down");
		
		resendTaskPool.shutdownNow();
		
//		try {
//			Thread.currentThread().join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	public boolean hsContains(int seq)
	{
		synchronized(hs)
		{
			return hs.contains(seq);
		}
	}

	public  boolean hsAdd(int seq)
	{
		synchronized(hs)
		{
			return hs.add(seq);
		}
	}

	public boolean hsRemove(int seq)
	{
		synchronized(hs)
		{
			return hs.remove(seq);
		}
	}
	
	public void hsContainsAndRemove(int seq)
	{
		synchronized(hs)
		{
			if(hs.contains(seq))
				hs.remove(seq);
		}
		
	}




	class ResendPacketTask implements Runnable{

		Thread t = null;
		int seqN = 0;
		SocketManager sm = null;
		FileSeperator fs = null;
		long timeElapse = 0;

		public ResendPacketTask(SocketManager socket, FileSeperator seperator, int seqNum, long timeElapse)
		{
			sm = socket;
			fs = seperator;
			seqN = seqNum;
			this.timeElapse = timeElapse;
		}


		@Override
		public void run() {

//			System.out.println("RecoveryListener Starts");
			
			Random r = new Random();
			
			
			//Random Backoff
			try {
				if(this.timeElapse <= 0 ||this.timeElapse >= 1500) //this might indicate the time is not synchronized
				{
					int timer = r.nextInt(40);
					TimeUnit.MILLISECONDS.sleep(timer+5);
				}else
				{
					//Have not implemented yet
					//random timer setting  
					// Random Timer Range from 2*timeElapse - 10*timeElpse
					int timer = r.nextInt((int)this.timeElapse*8);
					
					System.out.println("Random Backoff:"+ timer);
					TimeUnit.MILLISECONDS.sleep(timer+(int)this.timeElapse*2);
					
				}
				//				else
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("ResendPacketTask Thread for SeqNum: "+ seqN +" got interrupted!");
				e.printStackTrace();
			}

			if(hsContains(seqN))
			{
				hsRemove(seqN);
				byte[] buf = fs.getDataBlock(seqN);
				sm.sendStdPacket(seqN, buf);

				System.out.println("Send Recovery DataPacket SeqN: " + seqN);
			}
		}
	}

	/**
	 * @param args
	 */	
	public static void main(String[] args) {
		FileSeperator fs = new FileSeperator("/Users/Freehan/Desktop/Test/Test");
		fs.getFileAttribute();
		fs.printFileAttribute();
		SocketManager Sender = new SocketManager();
//		ExecutorService listener = Executors.newSingleThreadExecutor();
//		listener.execute(new SenderRecoveryListener(Sender, fs, 0));
//		try {
//			TimeUnit.SECONDS.sleep(1);	
//			listener.shutdownNow();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		SenderRecoveryListener srl = new SenderRecoveryListener(Sender, fs, 0);
		
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		srl.finalize();
		
	}


}
