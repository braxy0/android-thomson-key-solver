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
		
		FileInputStream fis;
		try {
			fis = new FileInputStream("/sdcard/auxtable.dat");
		} catch (FileNotFoundException e2) {
			ret =  new String[]{"Aux Table not found on SDCard!"};
			parent.list_key = ret;
			parent.handler.sendEmptyMessage(1);
			return;
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

		byte[] cp = new byte[12];
		byte[] hash = new byte[19];
		byte[] week = new byte[3 * 36 * 36 * 36];
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		int offset = 0;
		int progress = 0;
		for (int y = 4; y <= 10; y++)
		{
			for (int w = 1; w <= 52; w++)
			{
				try {
					fis.read(week);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				// Proper progress bar
				if (progress < 100f*(w + 52*(y-4))/364f)
				{
					parent.handler.sendEmptyMessage(2);
					progress++;
				}

				if ( stopRequested )
					return;
				
				offset = 0;
				for (int i = 0; i < 36*36*36; i++)
				{
					
					offset += 3;
					if (week[offset - 3 + 0] != routerESSID[0])
						continue;
					
					if (week[offset - 3 + 1] != routerESSID[1])
						continue;
					
					if (week[offset - 3 + 2] != routerESSID[2])
						continue;
					
					int a, b, c;
					c = i % 36;
					b = i/36 % 36;
					a = i/(36*36) % 36;
					
					cp[2] = (byte) Character.forDigit((y / 10), 10);
					cp[3] = (byte) Character.forDigit((y % 10), 10);
					cp[4] = (byte) Character.forDigit((w / 10), 10);
					cp[5] = (byte) Character.forDigit((w % 10), 10);
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
