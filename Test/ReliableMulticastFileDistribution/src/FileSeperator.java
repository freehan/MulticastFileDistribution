import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileSeperator {
	
	String fileName = null;
	long fileSize = 0;
	long blockNum = 0;		//How many blocks could this file be seperated to according to the blockSize;
	long blockSize = 1468L; 	//1500 - 20 (IPv4 header) - 8 (UDP header)  - 4 (SeqNum)  = 1468 (1400 to be safe)
	RandomAccessFile raf = null;
	
	
	public FileSeperator(){
		System.out.println("FileSeperator Default Constructor");
	};
	
	//FOR SENDER WITH COMPLETE FILE
	public FileSeperator(String fileName)
	{
		try {
			this.fileName = fileName;
			raf = new RandomAccessFile(fileName, "r");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		}
	};
	
	//FOR RECEIVER who need FILE COMBINER 
	public FileSeperator(String fileName, long blockNum)
	{
		try {
			this.fileName = fileName;
			this.blockNum = blockNum;
			raf = new RandomAccessFile(fileName, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		}
	};
	
	
	
	protected void finalize()
	{
		try {
			raf.close();
			System.out.println("RandomAccessFile closed");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("RandomAccessFile close failed");
		}
		
	};
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		try {
			raf.close();
			raf = new RandomAccessFile(this.fileName, "r");
		}
		catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("RandomAccessFile raf has not been initialized yet");
			e.printStackTrace();
		}
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	
	public long getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(long blockNum) {
		this.blockNum = blockNum;
	}
	
	public long getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(long blockSize) {
		this.blockSize = blockSize;
	}

	// Calculate Total Number of Blocks could be generated according to the blocksize
	public long getBlockNum(long blockSize){
		if(this.fileSize <= blockSize)
			return 1;
		else{
			if(fileSize % blockSize > 0)
				return fileSize/blockSize + 1;
			else
				return fileSize/blockSize;
		}
	}
	
	public void getFileAttribute(){
		File file = new File(this.fileName);
		this.fileName = file.getName();
		this.fileSize = file.length();		
		this.blockNum = getBlockNum(this.blockSize);
	}
	
	public void getFileAttribute(String fileAndPath){
		File file = new File(fileAndPath);
		this.fileName = file.getName();
		this.fileSize = file.length();	
		this.blockNum = getBlockNum(this.blockSize);
	}
	

	public void printFileAttribute(){
		System.out.println("File Name: " + this.fileName);
		System.out.println("File Size: " + this.fileSize);
		System.out.println("Block Num: " + this.blockNum);
		System.out.println("Block Size: " + this.blockSize);
	}
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	public byte[] getDataBlock(int seqNum){
		
		long blockSizeNeedToRead = this.getBlockSize();;
		long read_poz = 0;
		
		read_poz = this.getBlockSize()*(seqNum-1);
		
		if(blockNum==1)
			blockSizeNeedToRead = this.getFileSize();
		else if(seqNum == this.getBlockNum())
			blockSizeNeedToRead = this.getFileSize() - this.getBlockSize()*(this.getBlockNum()-1);
		
		try{
			int bufferSize = safeLongToInt(blockSizeNeedToRead);
			byte[] buf = new byte[bufferSize];
			
			raf.seek(read_poz);
			raf.read(buf);		
			return buf;
			
		}catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("CANNOT SEEK TO THE READ POSITION");
			return null;
			
		}
	}
	
	
	
	public static void main(String[] args) {
		//FileSeperator fs = new FileSeperator("C:\\Users\\Minhan\\Dropbox\\MasterProject\\design.docx");
		FileSeperator fs = new FileSeperator("/Users/Freehan/Desktop/Test/Test.wmv");
		//fs.setBlockSize((long)50*1024);
		fs.getFileAttribute();
		fs.printFileAttribute();
		
		
		
	}
}
