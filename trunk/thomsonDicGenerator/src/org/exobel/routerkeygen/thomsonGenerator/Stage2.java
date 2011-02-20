package org.exobel.routerkeygen.thomsonGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class Stage2 {
	public static void main(String[] args){
		FileInputStream fis;
		FileOutputManager files = new FileOutputManager();
		String file = "56.dat";
    	System.out.println("Stage2");
		System.out.println("Ordering Entries in the Dictionary");
		long begin = System.currentTimeMillis();
		int progress = 0;
		int c = 0;
		byte [] fileData = new  byte [300000];
		ArrayList<DictEntry> entries = new ArrayList<DictEntry>();
       for(int a = 0; a < AlphabetCodes.charect.length; a++)
        {
            for(int b = 0; b < AlphabetCodes.charect.length; b++, c++)
            { 
           	    file = AlphabetCodes.charect[a] + AlphabetCodes.charect[b] + ".dat";
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException e) {
		            System.out.println("Error!" + e);
					return;
				}
				byte [] entry = new  byte [5];
				int count = 0;
				try {
					count = fis.read(fileData);
					fis.close();
				} catch (IOException e) {
					System.out.println("Error!" + e);
					return;
				}
				files.addFile(file);
				int offset = 0;
				int lastn = 0;
				while ( offset < count)
				{
					entry[0] = fileData[offset + 0];
					//entry[1] = fileData[offset + 1];
					entry[2] = fileData[offset + 2];
					entry[3] = fileData[offset + 3];
					entry[4] = fileData[offset + 4];
					int number = ( (0xFF & entry[2]) << 16 ) | 
								 ( (0xFF & entry[3])  << 8 ) |
								   (0xFF & entry[4]) ;
					int diff = number - lastn;
					if(diff > 2097152) // 2 ^ 21
						System.out.println("Ooops, larger than 2 ^ 21!");
					if(diff > 1048576) // 2 ^ 20
						System.out.println("Ooops, larger than 2 ^ 20!");
					if(diff > 524288) // 2 ^ 19
						System.out.println("Ooops, larger than 2 ^ 19!");
					if(diff > 262144) // 2 ^ 18
						System.out.println("Ooops, larger than 2 ^ 18!");
					if(diff > 131072) // 2 ^ 17
						System.out.println("Ooops, larger than 2 ^ 17!");
					if(diff > 65536) // 2 ^ 16
						System.out.println("Ooops, larger than 2 ^ 16!");
					if(diff > 4096) // 2 ^ 12
					{
						System.out.println("Ooops, larger than 2 ^ 12!");
						diff = 4080;
					}
					
					if((diff >> 4) == 255) // 2 ^ 12
						System.out.println("Warning, FF exists!");
					//System.out.println("[ " + (number - lastn) + "  == " + ((number - lastn) >> 4) + " ]");
					lastn = number;
					entry[1] = (byte) (diff  >> 4);
					entries.add(new DictEntry(entry));
					offset += 5;
				}
				Iterator<DictEntry> it = entries.iterator();
				DictEntry dict_old = it.next(), dict_now ,tmp;
				Stack<DictEntry> pot = new Stack<DictEntry>();
				pot.push(dict_old);
				int aux1, aux2;
				while ( it.hasNext() ){
					dict_now = it.next();
					if ( dict_old.hash[0] == dict_now.hash[0] )
					{
						pot.push(dict_now);
					}
					else
					{
						aux1 = pot.peek().number;	
						tmp = pot.pop();
						files.sendFile( file, tmp.toFile() , 4);
						while ( !pot.empty() )
						{
							tmp = pot.pop();
							aux2 = tmp.number;
							tmp.number -= aux1;
							if ( tmp.number > 0xFFFF ){
								System.out.println("OMG");
								return;
							}
							aux1 = aux2;
							files.sendFile( file, tmp.toFile() , 4);
						}
						pot.push(dict_now);
					}
					dict_old = dict_now;
				}
				aux1 = pot.peek().number;	
				tmp = pot.pop();
				files.sendFile( file, tmp.toFile() , 4);
				while ( !pot.empty())
				{
					tmp = pot.pop();
					aux2 = tmp.number;
					tmp.number -= aux1;
					if ( tmp.number > 0xFFFF ){
						System.out.println("OMG");
						return;
					}
					aux1 = aux2;
					files.sendFile( file, tmp.toFile() , 4);
				}
				
				
				entries.clear();
				count /= 5;
				progress = (c *100)>>8;
				System.out.println("Counted " + count + " entries in " + file +
						           "  Total done: " + progress + "%");
            }
        }
        files.close();
        long time = System.currentTimeMillis() - begin;
        System.out.println("Done .. 100%! It took " + time + " miliseconds.");
	}
	private static class DictEntry { //implements Comparable<DictEntry>{
		short [] hash;
		int number;
		
		public DictEntry(byte [] entry ){
			hash = new short [1];
			hash[0] = (short) (0xFF & entry[0]);
			// hash[1] = (short) (0xFF & entry[1]); // discarded, average 220 possibilities
			number = entry[1];
			//number = ( (0xFF & entry[2]) << 16 ) | 
			//		 ( (0xFF & entry[3])  << 8 ) |
			//		   (0xFF & entry[4]) ;

		}

		/*
		@Override
		public int compareTo(DictEntry o) {
			if ( this.hash[0] > o.hash[0] )
				return 1;
			if ( this.hash[0] == o.hash[0] && this.number < o.number )
				return 1;
			return -1;
		}/**/
		
		public String toString(){
			try {
				return AlphabetCodes.getHexString(hash) + " " + number;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "null";	
		}
		
		public byte [] toFile(){
			byte [] entry = new  byte [4];
			entry[0] = (byte) (0xFF & hash[0]);
			entry[1] = (byte) (0xFF & hash[1]);
			//entry[2] = (byte) ( (0xFF0000 & number) >> 16) ;
			entry[2] = (byte) ( (0xFF00 & number) >> 8) ;
			entry[3] =(byte) (0xFF & number);
			return entry;
		}
	}
}
