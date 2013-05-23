import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.TIMEOUT;


public class Sender {

	//in seconds
	public static final int TIME_CONSTRAINT = 50;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		FileSeperator sep = new FileSeperator("/Users/Freehan/Desktop/Test/1.txt");
		
		FileSeperator sep = new FileSeperator("/Users/Freehan/Desktop/Test/Test100");
		sep.getFileAttribute();
		sep.printFileAttribute();
		
		SocketManager Sender = new SocketManager();
		SenderRecoveryListener srl = new SenderRecoveryListener(Sender, sep, 0);
		
		Random r = new Random();
		
		
		//Calculate Transmission Rate 
		double size = sep.getBlockNum();
		double rate = size / TIME_CONSTRAINT; //blocks per second
		double iteration = 1*1000*1000*1000 / rate;   //nanosecond per block
		long iterTime = Math.round(iteration);
		
		System.out.println("Iteration: "+ iteration + " iterTime: " + iterTime);
		
		long lastTime = System.nanoTime();
		
		int seqNum=1;
		while(seqNum<=sep.getBlockNum())
		{
			
			try {
				long elapse = System.nanoTime() - lastTime;
				lastTime = System.nanoTime();
				TimeUnit.NANOSECONDS.sleep(iterTime-elapse);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Test Recovery Request
//			if(!Arrays.asList(a).contains(seqNum))
//			if(r.nextInt(100000)> 1)
//			{
				byte[] buf = sep.getDataBlock(seqNum);
			
				Sender.sendStdPacket(seqNum, buf);
//			}
			seqNum++;
			
			//transmission rate control
		}
		
		try {
			
			System.out.println("Finish Sending. Wait for 10 seconds and terminate");
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Sender.finalize();		
	}

}
