package com.thomson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ThomsonCalc extends Thread {
	
	MessageDigest md;
	String [] ret;
	String router;
	ThomsonSolver parent;
	boolean stopRequested = false;
	// Working variables. Here to save speed as this
	// thread is created much sooner than needed.
	byte[] cp;
	byte[] hash;
	byte[] entry;
	int a, b, c;
	int year;
	int week;
	int sequenceNumber;
	byte[] routerESSID;
	public ThomsonCalc( ThomsonSolver par )
	{
		this.parent = par;
		this.cp = new byte[12];
		this.hash = new byte[19];
		this.entry = new byte[300000];
		this.routerESSID = new byte[2];
	}
	public void  run()
	{
		List<String> pwList = new ArrayList<String>();

		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		if ( router == null)
			return;
		
		if ( router.length() != 6 ) 
		{
			pwList.add(new String("Invalid ESSID! It must have 6 characters." ));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		//the fisrt byte is not considered
		
		for (int i = 2; i < 6; i += 2)
			routerESSID[i / 2 - 1] = (byte) ((Character.digit(router.charAt(i), 16) << 4)
					+ Character.digit(router.charAt(i + 1), 16));

		
		FileInputStream fis;
	
		try {
			fis = new FileInputStream("/sdcard/thomson/" + router.substring(0,2) + ".dat");
		} catch (FileNotFoundException e2) {
			pwList.add(new String("Dictionary not found on SDCard!" ));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		//TODO add size of table detection
		try {
			if ( fis.read(entry) == -1 )
			{
				pwList.add(new String("Error reading the dictionary!" ));
				parent.list_key =  pwList;
				parent.handler.sendEmptyMessage(1);
				return;
			}	
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int offset = 0;
		int len=0;
		if ( entry[( 0xFF &routerESSID[0] )<<2] == routerESSID[0] )
		{
			int i = ( 0xFF &routerESSID[0] )<<2;
			offset =( (0xFF & entry[i + 1]) << 16 ) | 
					( (0xFF & entry[i + 2])  << 8 ) | (0xFF & entry[i + 3]);
			len =   ( (0xFF & entry[i + 5]) << 16 ) | 
					( (0xFF & entry[i + 6])  << 8 ) | (0xFF & entry[i + 7]);
		}
		else /*When the total of entries is not 256, the condition above may fail
		 		, if that happens we do a linear search*/
			for( int i = 0 ; i < 1024 ; i += 4 )
			{
				if (entry[i + 0] == routerESSID[0])
				{
					offset =( (0xFF & entry[i + 1]) << 16 ) | 
							( (0xFF & entry[i + 2])  << 8 ) | (0xFF & entry[i + 3]);
					len =   ( (0xFF & entry[i + 5]) << 16 ) | 
							( (0xFF & entry[i + 6])  << 8 ) | (0xFF & entry[i + 7]);
					break;
				}
			}
		/* routerESSID[0] not found in the table*/
		if ( offset == 0 )
		{
			pwList.add( new String("No matches were found! Try another ESSID"));
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		for(  ; offset < len ; offset += 4 )
		{
			if ( stopRequested )
				return;

			if ( entry[offset] != routerESSID[1])
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
			cp[6] = unkown.charectbytes0[a];
			cp[7] = unkown.charectbytes1[a];
			cp[8] = unkown.charectbytes0[b];
			cp[9] = unkown.charectbytes1[b];
			cp[10] = unkown.charectbytes0[c];
			cp[11] = unkown.charectbytes1[c];
			md.reset();
			md.update(cp);
			hash = md.digest();

			try {
				pwList.add(StringUtils.getHexString(hash).substring(0, 10).toUpperCase());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(pwList.toArray().length == 0)
		{
			pwList.add( new String("No matches were found! Try another ESSID"));
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}

		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
		
	}
	
	public byte[] makeSHA1Hash(byte[] input) {
		md.update(input);
		return md.digest();
	}
	

}
