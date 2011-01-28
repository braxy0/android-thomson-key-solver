package org.exobel.routerkeygen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AliceKeygen extends KeygenThread {

	public AliceKeygen(RouterKeygen par) {
		super(par);
	}
	
	private String preInitCharset [] = {
			 "0","1","2","3","4","5","6","7","8","9","a","b","c",
			 "d","e","f","g","h","i","j","k","l","m","n","o","p",
			 "q","r","s","t","u","v","w","x","y","z","0","1","2",
			 "3","4","5","6","7","8","9","a","b","c","d","e","f","g",
			 "h","i","j","k","l","m","n","o","p","q","r","s","t","u",
			 "v","w","x","y","z","0","1","2","3","4","5","6","7","8",
			 "9","a","b","c","d","e","f","g","h","i","j","k","l","m",
			 "n","o","p","q","r","s","t","u","v","w","x","y","z","0",
			 "1","2","3","4","5","6","7","8","9","a","b","c","d","e",
			 "f","g","h","i","j","k","l","m","n","o","p","q","r","s",
			 "t","u","v","w","x","y","z","0","1","2","3","4","5","6",
			 "7","8","9","a","b","c","d","e","f","g","h","i","j","k",
			 "l","m","n","o","p","q","r","s","t","u","v","w","x","y",
			 "z","0","1","2","3","4","5","6","7","8","9","a","b","c",
			 "d","e","f","g","h","i","j","k","l","m","n","o","p","q",
			 "r","s","t","u","v","w","x","y","z","0","1","2","3","4",
			 "5","6","7","8","9","a","b","c","d","e","f","g","h","i",
			 "j","k","l","m","n","o","p","q","r","s","t","u","v","W",
			 "x","y","z","0","1","2","3"};
	 
	 private byte specialSeq[/*32*/]= {
		0x64, (byte) 0xC6, (byte) 0xDD, (byte) 0xE3, 
		(byte) 0xE5, 0x79, (byte) 0xB6, (byte) 0xD9, 
		(byte) 0x86, (byte) 0x96, (byte) 0x8D, 0x34, 
		0x45, (byte) 0xD2, 0x3B, 0x15, 
		(byte) 0xCA, (byte) 0xAF, 0x12, (byte) 0x84, 
		0x02, (byte) 0xAC, 0x56, 0x00, 
		0x05, (byte) 0xCE, 0x20, 0x75, 
		(byte) 0x91, 0x3F, (byte) 0xDC, (byte) 0xE8};
	
	
	public void run() {

		if ( router == null)
			return;
		try {
			md = MessageDigest.getInstance("SHA256");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nosha256));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		
		byte [] mac = new byte[6];
		for (int i = 0; i < 12; i += 2)
			mac[i / 2] = (byte) ((Character.digit(router.getMac().charAt(i), 16) << 4)
					+ Character.digit(router.getMac().charAt(i + 1), 16));
		String serialStr = "";
		int Q = 0, k = 0 ;
		if (  router.ssid.contains("Alice-96") )
		{
			Q = 96017051;
			k = 13;
			serialStr  = "69102X";
			return;
		}
		else if (  router.ssid.contains("Alice-93") 	)
		{
			Q = 92398366;
			k = 13;
			serialStr  = "69101X";
		}
		else if ( router.ssid.contains("Alice-56")	)
		{
			Q = 54808800;
			k = 13;
			serialStr  = "67902X";
		}
		else if ( router.ssid.contains("Alice-55") )
		{
			Q = 55164449;
			k = 8;
			serialStr  = "67904X";
		}
		else if ( router.ssid.contains("Alice-54") )
		{
			Q = 52420689;
			k = 8;
			serialStr  = "67903X";	
		}
		else if ( router.ssid.contains("Alice-48") )
		{
			Q = 47896103;
			k = 8;	
			serialStr  = "67903X";
		}
		else if ( router.ssid.contains("Alice-46") )
		{
			Q = 39015145;
			k = 13;	
			serialStr = "67902X";
		}
		else if ( router.ssid.contains("Alice-37") )
		{
			Q = 33175048;
			k = 13;	
			serialStr  = "67902X";
		}
		
		
		if ( Q == 0 || k == 0 )
		{
			pwList.add(parent.getResources().getString(R.string.msg_erralicenotsupported));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		int serial = ( Integer.valueOf(router.getEssid()) - Q ) / k;
		String tmp = Integer.toString(serial);
		for (int i = 0; i < 7 - tmp.length(); i++){
			serialStr += "0";
		}
		serialStr += tmp;
		
		md.reset();
		md.update(specialSeq);
		try {
			md.update(serialStr.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		md.update(mac);
		String key = "";
		byte [] hash = md.digest();		
		for ( int i = 0 ; i < 24 ; ++i )
		{
			key += preInitCharset[hash[i] & 0xFF];
		}
		pwList.add(key);/*For pre AGPF 4.5.0sx*/
		
		
		/*For post AGPF 4.5.0sx*/
		String macEth = router.getMac().substring(0,6);
		int extraNumber = 0;
		while ( extraNumber <= 9 )
		{
			String calc = Integer.toHexString(Integer.valueOf(
					extraNumber + router.getEssid()) ).toUpperCase();
			if ( macEth.charAt(5) == calc.charAt(0))
			{
				macEth += calc.substring(1);
				break;
			}
			extraNumber++;
		}
		if ( macEth.equals(router.getMac().substring(0,6)) )
		{
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(0);
			return;
		}
		
		
		
		for (int i = 0; i < 12; i += 2)
			mac[i / 2] = (byte) ((Character.digit(macEth.charAt(i), 16) << 4)
					+ Character.digit(macEth.charAt(i + 1), 16));
		md.reset();
		md.update(specialSeq);
		try {
			md.update(serialStr.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		md.update(mac);
		key = "";
		hash = md.digest();
		for ( int i = 0 ; i < 24 ; ++i )
			key += preInitCharset[hash[i] & 0xFF];
		pwList.add(key);
		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
}
