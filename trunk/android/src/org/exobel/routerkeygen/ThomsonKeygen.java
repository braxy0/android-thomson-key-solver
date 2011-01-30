package org.exobel.routerkeygen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Environment;

public class ThomsonKeygen extends KeygenThread {

	/*
	 * Thomson Keygen Working variables. Here to save speed as this
	 *  thread is created much sooner than needed.
	 */
	byte[] cp;
	byte[] hash;
	byte[] entry;
	byte[] table;
	int a, b, c;
	int year;
	int week;
	int sequenceNumber;
	byte[] routerESSID;
	boolean thomson3g;
	int len = 0;
	final String onlineDict = "http://paginas.fe.up.pt/~ei10139/webdic/";
	public ThomsonKeygen(RouterKeygen par ,boolean thomson3g ) {
		super(par);
		this.cp = new byte[12];
		this.hash = new byte[19];
		this.table= new byte[1282];
		this.routerESSID = new byte[3];
		this.thomson3g = thomson3g;
	}

	public void  run(){
		if ( router == null)
			return;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nosha1));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		if ( router.getEssid().length() != 6 ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_shortessid6));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		
		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.getEssid().charAt(i), 16) << 4)
					+ Character.digit(router.getEssid().charAt(i + 1), 16));

		
		if ( !thomson3g )
		{
			if (!localCalc() )
				return;
		}
		else
		{
			if (!internetCalc())
				return;
		}

		if(pwList.toArray().length == 0)
		{
			pwList.add(parent.getResources().getString(R.string.msg_errnomatches));
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
	private boolean internetCalc(){

		ThomsonHttpRetriever client = new ThomsonHttpRetriever();
		InputStream onlineFile =  client.retrieveStream(
									onlineDict +  
									router.getEssid().substring(0, 2).toLowerCase() + "/" + 
									router.getEssid().substring(2, 4).toLowerCase() + ".dat");	
		if ( onlineFile == null )
		{
			pwList.add(parent.getResources().getString(R.string.msg_errthomson3g));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return false;
		}
		this.entry = new byte[2000];
		try {
			len = onlineFile.read(entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		for (int offset = 0; offset < len ; offset += 4 )
		{
			if ( stopRequested )
				return false;

			if ( entry[offset] != routerESSID[2])
				continue;
			sequenceNumber = ( (0xFF & entry[offset + 1]) << 16 ) | 
			( (0xFF & entry[offset + 2])  << 8 ) | (0xFF & entry[offset + 3]) ;
			c = sequenceNumber % 36;
			b = sequenceNumber/36 % 36;
			a = sequenceNumber/(36*36) % 36;
			year = sequenceNumber / ( 36*36*36*52 ) + 4 ;
			week = ( sequenceNumber / ( 36*36*36 ) ) % 52 + 1 ;				
			cp[2] = (byte) Character.forDigit((year / 10), 10);
			cp[3] = (byte) Character.forDigit((year % 10), 10);
			cp[4] = (byte) Character.forDigit((week / 10), 10);
			cp[5] = (byte) Character.forDigit((week % 10), 10);
			cp[6] = charectbytes0[a];
			cp[7] = charectbytes1[a];
			cp[8] = charectbytes0[b];
			cp[9] = charectbytes1[b];
			cp[10] = charectbytes0[c];
			cp[11] = charectbytes1[c];
			md.reset();
			md.update(cp);
			hash = md.digest();
			
			try {
				pwList.add(StringUtils.getHexString(hash).substring(0, 10).toUpperCase());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private boolean localCalc(){

		
		if ( !Environment.getExternalStorageState().equals("mounted")  && 
		     !Environment.getExternalStorageState().equals("mounted_ro")	)
		{
			pwList.add(parent.getResources().getString(R.string.msg_nosdcard));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return false;
		}
		RandomAccessFile fis;
		try {
			fis = new RandomAccessFile(parent.folderSelect + File.separator + "RouterKeygen.dic", "r");
		} catch (FileNotFoundException e2) {
			pwList.add(parent.getResources().getString(R.string.msg_dictnotfound));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return false;
		}
		int version = 0;
		try {
			if ( fis.read(table) == -1 )
			{
				pwList.add(parent.getResources().getString(R.string.msg_errordict));
				parent.list_key =  pwList;
				parent.handler.sendEmptyMessage(1);
				return false;
			}
			version = table[0] << 8 | table[1];
			int totalOffset = 0;
			int offset = 0;
			if ( table[( 0xFF &routerESSID[0] )*5 + 2 ] == routerESSID[0] )
			{
				int i = ( 0xFF &routerESSID[0] )*5 + 2;
				offset =( (0xFF & table[i + 1]) << 24 ) | ( (0xFF & table[i + 2])  << 16 ) |
						( (0xFF & table[i + 3])  << 8 ) | (0xFF & table[i + 4]);
			}
			totalOffset += offset;
			fis.seek(totalOffset);
			if ( fis.read(table,0,1024) == -1 )
			{
				pwList.add(parent.getResources().getString(R.string.msg_errordict));
				parent.list_key =  pwList;
				parent.handler.sendEmptyMessage(1);
				return false;
			}	
			int lenght = 0;
			if ( table[( 0xFF &routerESSID[1] )*4] == routerESSID[1] )
			{
				int i = ( 0xFF &routerESSID[1] )*4;
				offset =( (0xFF & table[i + 1])  << 16 ) |
						( (0xFF & table[i + 2])  << 8 ) | (0xFF & table[i + 3]);
				lenght =  ( (0xFF & table[i + 5])  << 16 ) |
						( (0xFF & table[i + 6])  << 8 ) | (0xFF & table[i + 7]);
				
			}
			totalOffset += offset;
			fis.seek(totalOffset );
			this.entry = new byte[lenght - offset];
			if ( fis.read(entry,0, lenght - offset) == -1 )
			{
				pwList.add(parent.getResources().getString(R.string.msg_errordict));
				parent.list_key =  pwList;
				parent.handler.sendEmptyMessage(1);
				return false;
			}
			lenght -= offset;
			len = lenght;	
		} catch (IOException e1) {}
		if ( version == 1 )
			firstDic();
		if ( version == 2 )
			secondDic();
		return true;
	}
	private void secondDic(){
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		for (int offset = 0; offset < len ; offset += 3 )
		{
			if ( stopRequested )
				return;
			sequenceNumber = ( (0xFF & entry[offset + 0]) << 16 ) | 
			( (0xFF & entry[offset + 1])  << 8 ) | (0xFF & entry[offset + 2]) ;
			c = sequenceNumber % 36;
			b = sequenceNumber/36 % 36;
			a = sequenceNumber/(36*36) % 36;
			year = sequenceNumber / ( 36*36*36*52 ) + 4 ;
			week = ( sequenceNumber / ( 36*36*36 ) ) % 52 + 1 ;				
			cp[2] = (byte) Character.forDigit((year / 10), 10);
			cp[3] = (byte) Character.forDigit((year % 10), 10);
			cp[4] = (byte) Character.forDigit((week / 10), 10);
			cp[5] = (byte) Character.forDigit((week % 10), 10);
			cp[6] = charectbytes0[a];
			cp[7] = charectbytes1[a];
			cp[8] = charectbytes0[b];
			cp[9] = charectbytes1[b];
			cp[10] = charectbytes0[c];
			cp[11] = charectbytes1[c];
			md.reset();
			md.update(cp);
			hash = md.digest();
			if ( hash[19] != routerESSID[2])
				continue;
			if ( hash[18] != routerESSID[1])
				continue;
			if ( hash[17] != routerESSID[0])
				continue;
			
			try {
				pwList.add(StringUtils.getHexString(hash).substring(0, 10).toUpperCase());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	private void firstDic(){
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		for (int offset = 0; offset < len ; offset += 4 )
		{
			if ( stopRequested )
				return;

			if ( entry[offset] != routerESSID[2])
				continue;
			sequenceNumber = ( (0xFF & entry[offset + 1]) << 16 ) | 
			( (0xFF & entry[offset + 2])  << 8 ) | (0xFF & entry[offset + 3]) ;
			c = sequenceNumber % 36;
			b = sequenceNumber/36 % 36;
			a = sequenceNumber/(36*36) % 36;
			year = sequenceNumber / ( 36*36*36*52 ) + 4 ;
			week = ( sequenceNumber / ( 36*36*36 ) ) % 52 + 1 ;				
			cp[2] = (byte) Character.forDigit((year / 10), 10);
			cp[3] = (byte) Character.forDigit((year % 10), 10);
			cp[4] = (byte) Character.forDigit((week / 10), 10);
			cp[5] = (byte) Character.forDigit((week % 10), 10);
			cp[6] = charectbytes0[a];
			cp[7] = charectbytes1[a];
			cp[8] = charectbytes0[b];
			cp[9] = charectbytes1[b];
			cp[10] = charectbytes0[c];
			cp[11] = charectbytes1[c];
			md.reset();
			md.update(cp);
			hash = md.digest();
			
			try {
				pwList.add(StringUtils.getHexString(hash).substring(0, 10).toUpperCase());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
    static byte[] charectbytes0 = {
        '3','3','3','3','3','3',
        '3','3','3','3','4','4',
        '4','4','4','4','4','4',
        '4','4','4','4','4','4',
        '4','5','5','5','5','5',
        '5','5','5','5','5','5',
        };
    
    static byte[] charectbytes1 = {
        '0','1','2','3','4','5',
        '6','7','8','9','1','2',
        '3','4','5','6','7','8',
        '9','A','B','C','D','E',
        'F','0','1','2','3','4',
        '5','6','7','8','9','A',
        };

}
