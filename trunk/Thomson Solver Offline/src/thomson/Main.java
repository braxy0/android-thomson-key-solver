/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thomson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	static final String charect[] = {
		"a", "b", "c", "d",
		"e", "f", "0", "1",
		"2", "3", "4", "5",
		"6", "7", "8", "9"
		};

  static final byte[] HEX_CHAR_TABLE = {
    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
  };

  public static String getHexString(byte[] raw)
    throws UnsupportedEncodingException
  {
    byte[] hex = new byte[2 * raw.length];
    int index = 0;

    for (byte b : raw) {
      int v = b & 0xFF;
      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
    }
    return new String(hex, "ASCII");
  }
	static MessageDigest md;
    public static void main(String[] args) throws UnsupportedEncodingException, IOException
    {
    	
        
    	FileManager files = new FileManager();
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        long begin = System.currentTimeMillis();
        byte[] cp = new byte[12];
        byte[] hash = new byte[19];
        byte[] firstByte = new byte[1];
        int sequenceNumber = 0;
        byte [] ret = new byte [5];
    	cp[0] = (byte) (char)'C';
    	cp[1] = (byte) (char)'P';
  
        int offset = 0;
        for(int y = 4; y < 10; y++)
        {
            cp[2] = (byte) Character.forDigit((y / 10), 10);
            cp[3] = (byte) Character.forDigit((y % 10), 10);
            System.out.println("Done .. " + 100*(y-4)/6 + "%");
            for(int w = 1; w <= 52; w++)
            {
                
                cp[4] = (byte) Character.forDigit((w / 10), 10);
                cp[5] = (byte) Character.forDigit((w % 10), 10);
                for(int a = 0; a < unkown.charectbytes.length; a++)
                {
                    cp[6] = unkown.charectbytes[a][0];
                    cp[7] = unkown.charectbytes[a][1];
                    for(int b = 0; b < unkown.charectbytes.length; b++)
                    {
                        cp[8] = unkown.charectbytes[b][0];
                        cp[9] = unkown.charectbytes[b][1];
                        for(int c = 0; c < unkown.charectbytes.length ; c++)
                        {
                            offset += 3;
                            cp[10] = unkown.charectbytes[c][0];
                            cp[11] = unkown.charectbytes[c][1];
                            md.reset();
                            md.update(cp);
                            hash = md.digest();
                            firstByte[0] = hash[17];
                			ret[0] = hash[18];
                			ret[1] = hash[19];
                			ret[2] = (byte) ( (0xFF0000 & sequenceNumber) >> 16) ;
                			ret[3] = (byte) ( (0xFF00 & sequenceNumber) >> 8) ;
                			ret[4] =(byte) (0xFF & sequenceNumber);
                			sequenceNumber++;
                			files.sendFile(getHexString(firstByte)+".dat", ret);
                        }
                    }
                }
            }
        }     
        files.close();
        long time = System.currentTimeMillis() - begin;
        System.out.println("Done .. 100%! It took " + time + " miliseconds.");
    }
    private static class FileManager{
	   	 	Map<String, FileOutput> filesMap;
	   	 	public FileManager(){
			   	filesMap = new HashMap<String, FileOutput>();
		   	 	FileOutput fos;
		        String file = "00.dat";
		        for(int a = 0; a < charect.length; a++)
		        {
		            for(int b = 0; b < charect.length; b++)
		            { 
		            	file = charect[a] + charect[b] + ".dat";
						try {
				            fos = new FileOutput( file);
				            filesMap.put(file, fos);
				        } catch (FileNotFoundException ex) {
				            System.out.println("Error!" + ex);
				            return;
				        }
		            }
		        }
	   	 	}
   	 	
   	 		public void sendFile(String file , byte [] bytes ){
	   	 		if ( !filesMap.containsKey(file) )
	   	 			return;
	   	 		
	   	 		try {
					filesMap.get(file).add(bytes);
				} catch (IOException e) {
					System.out.println("Error!" + e);
		            return;
				}
   	 		}
			
			public void close(){
				Iterator<Entry<String, FileOutput>> it = filesMap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, FileOutput> pairs = (Map.Entry<String, FileOutput>)it.next();
					try {
						pairs.getValue().close();
					} catch (IOException e) {
						System.out.println("Error!" + e);
			            return;
					}
				}
			}
   	 		
  
   	 	private static class FileOutput{
   	 		FileOutputStream fos;
   	 		byte [] buffer;
   	 		int offset;
   	 		public FileOutput( String file ) throws FileNotFoundException{
   	 			fos = new FileOutputStream(file);
   	 			buffer = new byte[10000];
   	 			offset = 0;
   	 		}
   	 		public void add ( byte [] bytes ) throws IOException{
   	 			for ( int i = 0 ; i < bytes.length ; ++i  )
   	 			{
   	 				if ( offset >= buffer.length )
   	 				{
   	 					fos.write(buffer);
   	 					offset = 0;
   	 				}
   	 				buffer[offset] = bytes[i];
   	 				offset++;
   	 			}
   	 		}
   	 		public void close() throws IOException{
   	 			fos.write(buffer,  0 , offset);
   	 			fos.close();
   	 		}
   	 	}
   }
}