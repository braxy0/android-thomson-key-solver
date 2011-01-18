package thomson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Stage2 {
	public static void main(String[] args){
		
		FileInputStream fis;
		FileOutputManager files = new FileOutputManager();
		String file = "00.dat";
		System.out.println("Ordering Entries in the Dictionary");
		long begin = System.currentTimeMillis();
        for(int a = 0; a < AlphabetCodes.charect.length; a++)
        {
            for(int b = 0; b < AlphabetCodes.charect.length; b++)
            { 
            	file = AlphabetCodes.charect[a] + AlphabetCodes.charect[b] + ".dat";
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException e) {
		            System.out.println("Error!" + e);
					return;
				}
				Set<DictEntry> entries = new TreeSet<DictEntry>();
				byte [] entry = new  byte [5];
				int count = 0;
				byte [] fileData = new  byte [300000];
				try {
					count = fis.read(fileData);
					fis.close();
				} catch (IOException e) {
					System.out.println("Error!" + e);
					return;
				}
				files.addFile(file);
				int offset = 0;
				while ( offset < count)
				{
					entry[0] = fileData[offset + 0];
					entry[1] = fileData[offset + 1];
					entry[2] = fileData[offset + 2];
					entry[3] = fileData[offset + 3];
					entry[4] = fileData[offset + 4];
					entries.add(new DictEntry(entry));
					offset += 5;
				}
				Iterator<DictEntry> it = entries.iterator();
				while ( it.hasNext() )
						files.sendFile( file, it.next().toFile());
					//	System.out.println(it.next());
				count /= 5;
				System.out.println("Counted " + count + " entries in " + file);
            }
        }
        files.close();
        long time = System.currentTimeMillis() - begin;
        System.out.println("Done .. 100%! It took " + time + " miliseconds.");
	}
	private static class DictEntry implements Comparable<DictEntry>{
		byte [] hash;
		int number;
		
		public DictEntry(byte [] entry ){
			hash = new byte [2];
			hash[0] =  entry[0];
			hash[1] =  entry[1];
			number = ( (0xFF & (int)entry[2]) << 16 ) | 
					 ( (0xFF & (int)entry[3])  << 8 ) |
					   (0xFF & (int)entry[4]) ;

		}

		@Override
		public int compareTo(DictEntry o) {
			// TODO Auto-generated method stub
			if ( ( this.hash[0] == o.hash[0] &&  this.hash[1] == o.hash[1] ) ||
					( this.hash[0] > o.hash[0] ))
				return 1;
			else
				if ( this.hash[0] < o.hash[0] )
					return -1;
				else
					if ( this.hash[1] >= o.hash[1] )
						return 1;
					else
						if ( this.hash[1] < o.hash[1] )
							return -1;
			return 0;
		}
		
		public String toString(){
			try {
				return Stage1.getHexString(hash) + " " + number;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "null";	
		}
		
		public byte [] toFile(){
			byte [] entry = new  byte [5];
			entry[0] = hash[0];
			entry[1] = hash[1];
			entry[2] = (byte) ( (0xFF0000 & number) >> 16) ;
			entry[3] = (byte) ( (0xFF00 & number) >> 8) ;
			entry[4] =(byte) (0xFF & number);
			return entry;
		}
	}
}
