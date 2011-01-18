package com.thomson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
		ArrayList<String> pwList = new ArrayList<String>();

		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		if ( router == null)
			return;
		
		if ( router.length() != 6 ) 
		{
			ret =  new String[]{"Invalid ESSID! It must have 6 characters."};
			parent.list_key = ret;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		
		byte[] routerESSID = new byte[3];
		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.charAt(i), 16) << 4)
					+ Character.digit(router.charAt(i + 1), 16));

		
		FileInputStream fis;
		try {
			fis = new FileInputStream("/sdcard/thomson/" + router.substring(0,2) + ".dat");
		} catch (FileNotFoundException e2) {
			ret =  new String[]{"Aux File not found on SDCard!" };
			parent.list_key = ret;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		int bytesRead = 0;
		byte[] cp = new byte[12];
		byte[] hash = new byte[19];
		byte[] entry = new byte[300000];
		short [] essid = new short[2];
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		int a, b, c;
		int year;
		int week;
		int sequenceNumber;
		try {
			if ( ( bytesRead = fis.read(entry) ) == -1 )
				return;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for( int offset = 0 ; offset < bytesRead ; offset += 5 )
		{
			if ( stopRequested )
				return;
			
			essid[0] = (short) (0xFF &  (short)entry[offset + 0]);
			essid[1] = (short) (0xFF & (short)entry[offset + 1]);
			if (essid[0] != routerESSID[1])
				continue;
			
			if (essid[1] != routerESSID[2])
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
			ret =  new String[]{"No matches were found! Try another ESSID"};
			parent.list_key = ret;
			parent.handler.sendEmptyMessage(1);
			return;
		}

		ret = new String[pwList.size()];
		int i = 0;
		for(String s: pwList)
			ret[i++] = s;
		parent.list_key = ret;
		parent.handler.sendEmptyMessage(0);
		return;
		
	}
	
	public byte[] makeSHA1Hash(byte[] input) {
		md.update(input);
		return md.digest();
	}
	

}
