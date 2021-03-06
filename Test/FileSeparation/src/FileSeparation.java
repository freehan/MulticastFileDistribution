import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;



public class FileSeparation {

	String fileName = null;
	long fileSize = 0;
	long blockNum = 0;
	
	public FileSeparation(){};
	
	private void getFileAttribute(String fileAndPath){
		File file = new File(fileAndPath);
		fileName = file.getName();
		fileSize = file.length();
	}
	
	private long getBlockNum(long blockSize){
		if(this.fileSize <= blockSize)
			return 1;
		else{
			if(fileSize % blockSize > 0)
				return fileSize/blockSize + 1;
			else
				return fileSize/blockSize;
		}
		
	}
	
	private String generateSeparatorFileName(String fileAndPath, int currentBlock){
		return fileAndPath + ".part" + currentBlock;
	}
	
	private boolean writeFile(String fileAndPath, String fileSeparateName, long blockSize, long beginPos){
		RandomAccessFile raf = null;
		FileOutputStream fos = null;
		byte[] bt = new byte[1024];
		long writeByte = 0;
		int len = 0;
		try{
			raf = new RandomAccessFile(fileAndPath, "r");
			raf.seek(beginPos);
			fos = new FileOutputStream(fileSeparateName);
			while((len = raf.read(bt))>0)
			{
				if(writeByte < blockSize)
				{
					writeByte = writeByte + len;
					
					if(writeByte <= blockSize)
						fos.write(bt,0,len);
					else
					{
						len = len-(int)(writeByte - blockSize);
						fos.write(bt,0,len);
					}
				}else
					break;
			}
			
			fos.close();
			raf.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			try{
				if(fos != null)
					fos.close();
				if(raf != null)
					raf.close();
				
			}catch(Exception ex){
				ex.printStackTrace();	
			}
			return false;
		}
		return true;
	}
	
	public boolean separateFile(String fileAndPath, long blockSize)
	{
		getFileAttribute(fileAndPath);
		this.blockNum = getBlockNum(blockSize);		//Calculate Number of Part after separation
		
		System.out.println("FileSize="+this.fileSize);
		System.out.println("blockNum="+this.blockNum);
		System.out.println("blockSize="+blockSize);
		
		
		if(this.blockNum == 1)					
			blockSize = this.fileSize;
		
				
		long writeSize = 0;			//amount of data written at the time
		long writeTotal = 0;		//total amount of data written 
		String fileCurrentNameAndPath = null;
		
		for(int i=1; i<=blockNum; i++){
			if(i<blockNum)
				writeSize = blockSize; 		//amount of data written this time
			else
				writeSize = this.fileSize - writeTotal;		//write the remaining data
			
			if(blockNum==1)
				fileCurrentNameAndPath = fileAndPath + ".bak";
			else
				fileCurrentNameAndPath = generateSeparatorFileName(fileAndPath, i);
			
			if(!writeFile(fileAndPath, fileCurrentNameAndPath, writeSize, writeTotal))
				return false;
			
			writeTotal = writeTotal + writeSize;
			
		}
			
		return true;
	}
	
	
	public static void main(String[] args) {
//		System.out.println("Hellow World!");
		FileSeparation separator = new FileSeparation();
		String fileAndPath = "/Users/Freehan/Desktop/test/1.txt";
		long blockSize = 20*1024;
		if(separator.separateFile(fileAndPath, blockSize))
			System.out.println("Success");
		else
			System.out.println("Fail");
	}
	

}
