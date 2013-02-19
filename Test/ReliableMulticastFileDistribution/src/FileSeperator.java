import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileSeperator {
	
	String fileName = null;
	long fileSize = 0;
	long blockNum = 0;
	long blockSize = 543; //According to optimal UDP packet size
	RandomAccessFile raf = null;
	
	public FileSeperator(){};
	
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

	
	public long getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(long blockSize) {
		this.blockSize = blockSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileSeperator fs = new FileSeperator("C:\\Users\\Minhan\\Dropbox\\MasterProject\\design.docx");
		fs.getFileAttribute();
		fs.printFileAttribute();
		
	}
}
