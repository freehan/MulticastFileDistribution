import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.StringTokenizer;


public class FileCombination {
	
	String srcDirectory = null;
	String[] separatedFiles;
	String[][] separatedFilesAndSize;
	int fileNum=0;
	String fileRealName="";
	
	
	public FileCombination(){};
	
	public FileCombination(String directory){this.srcDirectory = directory;};

	
	private String getRealName(String sFileName){
		StringTokenizer st = new StringTokenizer(sFileName,".");
		return st.nextToken()+"."+st.nextToken();
	}
	
	private long getFileSize(String fileName){
		String fullName = this.srcDirectory + fileName;
		return (new File(fullName).length());	
	}
	
	private void getFileAttribute(String directory)
	{
		File file = new File(directory);
		this.separatedFiles = new String[file.list().length];
		this.separatedFiles = file.list();
		this.separatedFilesAndSize = new String[separatedFiles.length][2];
		//Arrays.sort(separatedFiles);	//XIA:Serious Trouble Here!!!!!!
		//after sorting:1-11 part1 part10 part11 part2 part3 ....part9
		//
		//
		this.fileNum = separatedFiles.length;
		
		for(int i=0; i<this.fileNum; i++){
			separatedFilesAndSize[i][0] = separatedFiles[i];
			separatedFilesAndSize[i][1] = String.valueOf(getFileSize(separatedFiles[i]));
		}
		
		this.fileRealName = getRealName(separatedFiles[this.fileNum-1]);
		
	}
	
	public boolean combineFile()
	{
		RandomAccessFile raf = null;
		long alreadyWrite = 0;
		FileInputStream fis = null;
		int len = 0;
		byte[] bt = new byte[1024];
		
		try{
			raf = new RandomAccessFile(srcDirectory+fileRealName, "rw");
			for(int i=0; i<fileNum;i++)
			{
				if(!separatedFilesAndSize[i][0].equals(".DS_Store"))
				{
					System.out.println(separatedFilesAndSize[i][0]);
					raf.seek(alreadyWrite);
					fis = new FileInputStream(srcDirectory+separatedFilesAndSize[i][0]);
					while((len = fis.read(bt))>0)
					{
						raf.write(bt,0,len);
					}
					fis.close();
					alreadyWrite = alreadyWrite + Long.parseLong(separatedFilesAndSize[i][1]);
				}
			}
			
			raf.close();
		}catch(Exception ex){
			ex.printStackTrace();
			try{
				if(raf!=null)
					raf.close();
				if(fis!=null)
					fis.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return false;
		}
		return true;
	}
	
	public static void main(String[] args)
	{
		FileCombination fc = new FileCombination("/Users/Freehan/Desktop/Test/");
		fc.getFileAttribute(fc.srcDirectory);
		boolean result = fc.combineFile();
		if(result)
			System.out.println("success");
		else
			System.out.println("fail");

		
	}
}
