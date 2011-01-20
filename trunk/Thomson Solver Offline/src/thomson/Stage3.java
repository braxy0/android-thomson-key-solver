package thomson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;


public class Stage3 {

	public static void main(String[] args) {
		FileInputStream fis;
		FileOutputManager files = new FileOutputManager();
		TableEntry entry = new TableEntry();
		System.out.println("Creating secondary tables.");
		long begin = System.currentTimeMillis();
		String fileName = "56.dat";
		int progress = 0;
		int c = 0;
		byte [] fileData = new  byte [300000];
		byte [] outputData = new  byte [300000];
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
					files.addFile(fileName);
				} catch (IOException e) {
					System.out.println("Error!" + e);
					return;
				}
				byte currentEntry;
				byte tmp;
				int offset= 0;
				int address = 1024;//size of the table
				//1024 = 256 * ( 1 + 3)
				// 1byte for header and 3 for address
				currentEntry = fileData[offset + 0];
				entry.addEntry((short) (0xFF & currentEntry), address );
				outputData[address + 0] = fileData[offset + 1];
				outputData[address + 1] = fileData[offset + 2];
				outputData[address + 2] = fileData[offset + 3];
				outputData[address + 3] = fileData[offset + 4];
				address += 4;
				offset += 5;
				while (offset < count ){
					tmp = fileData[offset + 0];
					if ( tmp != currentEntry )
					{ 
						currentEntry = tmp;
						entry.addEntry((short) (0xFF & currentEntry), address );
					}
					outputData[address + 0] = fileData[offset + 1];
					outputData[address + 1] = fileData[offset + 2];
					outputData[address + 2] = fileData[offset + 3];
					outputData[address + 3] = fileData[offset + 4];
					offset += 5;
					address += 4;
				}
				entry.toFile(outputData);
				files.sendFile(fileName, outputData , address);
				progress = (c *100)>>8;
				System.out.println("File " + fileName + " processed " +
				           "  Total done: " + progress + "% " );
			}
		}
        files.close();
		long time = System.currentTimeMillis() - begin;
        System.out.println("Done .. 100%! It took " + time + " miliseconds.");
	}
	private static class TableEntry{
		Map<Short , Integer > map;
		public TableEntry(){
			map = new TreeMap<Short, Integer>();
		}
		
		public void addEntry( short secondByte , int offset){
			map.put(secondByte, offset);
		}
		
		public void toFile(byte [] outputData ){
			Iterator<Entry<Short, Integer>> it = map.entrySet().iterator();
			Entry<Short, Integer> entry;
			int offset = 0;
			while (it.hasNext()){
				entry = it.next();
				outputData[offset + 0] = (byte) (0xFF & entry.getKey());
				outputData[offset + 1] = (byte) ( (0xFF0000 & entry.getValue()) >> 16) ;
				outputData[offset + 2] = (byte) ( (0xFF00 & entry.getValue()) >> 8) ;
				outputData[offset + 3] =(byte) (0xFF & entry.getValue());
				offset += 4;
			}
		}
		
		@SuppressWarnings("unused")
		public void printAll() throws UnsupportedEncodingException{
			Iterator<Entry<Short, Integer>> it = map.entrySet().iterator();
			Entry<Short, Integer> entry;
			while (it.hasNext()){
				entry = it.next();
				System.out.println(AlphabetCodes.getHexString((byte) (0xFF &entry.getKey())) + 
						": " + entry.getValue());
			}
		}
	}
}
