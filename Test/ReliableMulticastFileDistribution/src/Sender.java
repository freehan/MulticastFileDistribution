
public class Sender {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		FileSeperator sep = new FileSeperator("/Users/Freehan/Desktop/Test/1.txt");
		
		FileSeperator sep = new FileSeperator("C:\\Users\\Minhan\\Desktop\\IE9-Windows7-x64-enu.exe");
		sep.getFileAttribute();
		sep.printFileAttribute();
		
//		SocketManager Sender = new SocketManager();
//		
//		
//		int seqNum=1;
//		while(seqNum<=sep.getBlockNum())
//		{
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			byte[] buf = sep.getDataBlock(seqNum);
//			
//			Sender.sendStdPacket(seqNum, buf);
//			
//			seqNum++;
//		}
//		Sender.finalize();		
	}

}
