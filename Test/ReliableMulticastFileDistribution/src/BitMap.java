
public class BitMap {



	volatile private byte[] bitmap = null;
	private int bitNum = 0; 

	public BitMap(int blockNum)
	{
		this.bitNum = blockNum;

		if(blockNum <= 8)
			bitmap = new byte[1];
		else{
			int byteNum = blockNum/8;
			if(blockNum%8 > 0)
				byteNum++;
			bitmap = new byte[byteNum];
		}
	}

	//IMPORTANT NOTICE:
	//pos stands for bitmap index
	//bitmap index starts from [1] to [bitNum]
	//bitmap actual implementation stores the bitmap with index from [0] to [bitNum]
	//So,the input [pos] is actually trying to visit [pos-1]
	public synchronized void setBitTrue(int pos){
		if(pos > bitNum || pos <= 0){
			System.err.println("BitMap Index out of bitmap boundary!");
			return;
		}

		pos = pos-1; //pos starts from 1, index starts from 0 
		int byteIndex = pos / 8;
		int bitIndex = pos % 8;
		byte insertVal = (byte) (1<<bitIndex);

		bitmap[byteIndex] = (byte) (bitmap[byteIndex] | insertVal);

	}
	
	public synchronized void setBitFalse(int pos){
		if(pos > bitNum || pos <= 0){
			System.err.println("BitMap Index out of bitmap boundary!");
			return;
		}

		pos = pos-1; //pos starts from 1, index starts from 0 
		int byteIndex = pos / 8;
		int bitIndex = pos % 8;
		byte insertVal = (byte) (1<<bitIndex);
		
		insertVal = (byte)(~insertVal); // Bitwise Invert

		bitmap[byteIndex] = (byte) (bitmap[byteIndex] & insertVal);

	}

	public synchronized boolean checkBit(int pos){

		if(pos > bitNum || pos <= 0){
			System.err.println("BitMap Index out of bitmap boundary!");
			return false;
		}
		
		pos = pos-1; //pos starts from 1, index starts from 0
		int byteIndex = pos / 8;
		int bitIndex = pos % 8;
		byte insertVal = (byte) (1<<bitIndex);

		if((bitmap[byteIndex]&insertVal) == insertVal){
			return true;
		}else
			return false;

	}
	
	
	//Return the next false bit starting from startPos. 
	//e.g. 10000, then 2 digit is the nextFalseBit 
	//when return bitNum+1, it means from startPos to bitNum, every bit is true. 
	public synchronized int findNextFalseBit(int startPos)
	{
		if(startPos > bitNum || startPos <= 0){
			System.err.println("BitMap Index out of bitmap boundary!");
			return 0;
		}
		
		while(startPos <= bitNum&&checkBit(startPos))
			startPos++;
//		startPos--;
		return startPos;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BitMap bm = new BitMap(37);
		bm.setBitTrue(4);
		bm.setBitTrue(8);
		bm.setBitTrue(9);
		bm.setBitTrue(10);
		bm.setBitTrue(12);
		bm.setBitTrue(15);
		bm.setBitTrue(37);
		
		int index = bm.findNextFalseBit(37);
		
		System.out.println(index);
//		for(int i=1;i<=37;i++)
//		{
//			System.out.println("Pos" + i + ": " + bm.checkBit(i));
//			
//		}

		
	}

}
