package org.exobel.routerkeygen.thomsonGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Stage4 {
	
	/*
	 * Version 1 - Initial dictionary of 55.8 MB ( downloaded 740 times )
	 * Version 2 - Second version with 41.9 MB
	 */
	static final byte [] version = { 0, 2};
	public static void main(String[] args) {
		FileInputStream fis;
		EntryTable entry = new EntryTable(4);
    	System.out.println("Stage3");
		System.out.println("Creating main table and assembling the final file.");
		long begin = System.currentTimeMillis();
		String fileName = "56.dat";
		int progress = 0;
		int c = 0;
		short firstByte;
		byte [] fileData = new  byte [300000];
		byte [] table = new byte[1280];
		//size of the table
		//1282 = 256 * ( 1 + 4) + 2
		// 1byte for header and 4 for address. 2bytes for dictionary version control
		int address = 1282;
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
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("RouterKeygen.dic");
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
