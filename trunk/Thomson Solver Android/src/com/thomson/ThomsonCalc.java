package com.thomson;

import java.io.File;
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

	public ThomsonCalc( ThomsonSolver par )
	{
		this.parent = par;
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
		byte[] routerESSID = new byte[2];
		for (int i = 2; i < 6; i += 2)
			routerESSID[i / 2 - 1] = (byte) ((Character.digit(router.charAt(i), 16) << 4)
					+ Character.digit(router.charAt(i + 1), 16));

		
		FileInputStream fis;
		int len=0;
		try {
			len = (int)(new File("/sdcard/thomson/" + router.substring(0,2) + ".dat").length());
			fis = new FileInputStream("/sdcard/thomson/" + router.substring(0,2) + ".dat");
		} catch (FileNotFoundException e2) {
			pwList.add(new String("Dictionary not found on SDCard!" ));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		byte[] cp = new byte[12];
		byte[] hash = new byte[19];
		byte[] entry = new byte[len];
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		int a, b, c;
		int year;
		int week;
		int sequenceNumber;
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
		for( int offset = 0 ; offset < len ; offset += 5 )
		{
			if ( stopRequested )
				return;

			if (entry[offset + 0] != routerESSID[0])
				continue;
			
			if (entry[offset + 1] != routerESSID[1])
				continue;
			sequenceNumber = ( (0xFF & (int)entry[offset + 2]) << 16 ) | 
							( (0xFF & (int)entry[offset + 3])  << 8 ) | (0xFF & (int)entry[offset + 4]) ;
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
