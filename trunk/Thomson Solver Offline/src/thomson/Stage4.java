package thomson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Stage4 {
	static final byte [] version = { 0, 1};
	public static void main(String[] args) {
		FileInputStream fis;
		FileOutputManager files = new FileOutputManager();
		EntryTable entry = new EntryTable(4);
		System.out.println("Creating main tables and assembling final final.");
		long begin = System.currentTimeMillis();
		String fileName = "56.dat";
		int progress = 0;
		int c = 0;
		short firstByte;
		byte [] fileData = new  byte [300000];
		byte [] table = new byte[1280];
		//byte [] outputData = new  byte [300000];
		//size of the table
		//1280 = 256 * ( 1 + 4)
		// 1byte for header and 4 for address
		int address = 1280;
		for(int a = 0; a < AlphabetCodes.charect.length; a++)
        {
            for(int b = 0; b < AlphabetCodes.charect.length; b++)
            {
           	fileName = AlphabetCodes.charect[a] + AlphabetCodes.charect[b] + ".dat";
			firstByte = (short) (( AlphabetCodes.charectCode[a] << 4 ) | AlphabetCodes.charectCode[b]);
            entry.addEntry(firstByte, address);
           	try {
					fis = new FileInputStream(fileName);
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
				if ( count == -1 )
				{
					System.out.println("Error while reading " + fileName + "!");
					return;
				}
				address +=count;
			}
		}
		
		try {
			entry.printAll();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("thomson.dic");
		} catch (FileNotFoundException e) {
			System.out.println("Error!" + e);
			return;
		}
		try {
			fos.write(version);
		} catch (IOException e) {
			System.out.println("Error!" + e);
			return;
		}
		entry.toFile(table);
		try {
			fos.write(table);
		} catch (IOException e) {
			System.out.println("Error!" + e);
			return;
		}
		System.out.println("Initial table finished. Assembling in a single file!");
		for(int a = 0; a < AlphabetCodes.charect.length; a++)
        {
            for(int b = 0; b < AlphabetCodes.charect.length; b++ , c++)
            {
           	fileName = AlphabetCodes.charect[a] + AlphabetCodes.charect[b] + ".dat";
           	try {
					fis = new FileInputStream(fileName);
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
				if ( count == -1 )
				{
					System.out.println("Error while reading " + fileName + "!");
					return;
				}
				try {
					fos.write(fileData , 0 , count);
				} catch (IOException e) {
					System.out.println("Error!" + e);
					return;
				}
				progress = (c *100)>>8;
				System.out.println("File " + fileName + " processed " +
			           "  Total done: " + progress + "% " );
			}
		}
		try {
			fos.close();
		} catch (IOException e) {
			System.out.println("Error!" + e);
			return;
		}
		long time = System.currentTimeMillis() - begin;
        System.out.println("Done .. 100%! It took " + time + " miliseconds.");
	}

}
