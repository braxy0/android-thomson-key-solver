package org.exobel.routerkeygen.thomsonGenerator;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class StageWebdic {

    public static void main(String[] args){
		FileOutputManager files = new FileOutputManager();
		FileInputStream fis;
		String prefixfileName = "56";
		String suffixfileName = "00";
		byte [] fileData = new byte [300000];
		byte [] outputData = new byte [2];
		long begin = System.currentTimeMillis();  
		System.out.println("Stage3");
		System.out.println("Refactoring dat files for webdic.");
		(new File("webdic")).mkdir();
		int progress = 0;
		for(int a = 0; a < AlphabetCodes.charect.length; a++)
        {
           for(int b = 0; b < AlphabetCodes.charect.length; b++ )
            {
        	   prefixfileName = AlphabetCodes.charect[a] + AlphabetCodes.charect[b];

               	(new File("webdic" + File.separator + prefixfileName)).mkdir();
            	try {
					fis = new FileInputStream(prefixfileName + ".dat");
				} catch (FileNotFoundException e) {
		            System.out.println("Error!" + e);
					return;
				}
				int count = 0;
				try {
					count = fis.read(fileData);
					fis.close();
				} catch (IOException e) {
					System.out.println("Error!" + e);
					return;
				}
				for(int c = 0; c < AlphabetCodes.charect.length; c++)
				{
				    for(int d = 0; d < AlphabetCodes.charect.length; d++)
				    {
				    	suffixfileName = AlphabetCodes.charect[c] + AlphabetCodes.charect[d];
				    	files.addFile("webdic" + File.separator + prefixfileName + File.separator + suffixfileName + ".dat");
				    }
				}

				int offset= 0;
				while (offset < count ){
					outputData[0] = fileData[offset + 2];
					outputData[1] = fileData[offset + 3];
					try {
						files.sendFile("webdic" + File.separator + prefixfileName + File.separator + 
								AlphabetCodes.getHexString(fileData[offset + 0]) + ".dat",outputData );
					} catch (UnsupportedEncodingException e) {}
					offset += 4;

				}
				files.close();
				files.clearAll();
            }
           progress++;
           System.out.print("Proccessed ");
           System.out.print(( progress * 100 )>> 4);
           System.out.println("%");
        }
		long time = System.currentTimeMillis() - begin;
        System.out.println("Done .. 100%! It took " + time + " miliseconds.");
    }
    
}
    
				    