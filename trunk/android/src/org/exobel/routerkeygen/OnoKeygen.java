package org.exobel.routerkeygen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*
 * The algorithm for the type of network
 * whose SSID must be in the form of [pP]1XXXXXX0000X
 * where X means a digit.
 * Algorithm:
 * Adding +1 to the last digit and use the resulting 
 * string as the passphrase for WEP key generation.
 * Use the first of the 64 bit keys and the 128 bit one
 * as possible keys.
 * Credit:
 *  pulido from http://foro.elhacker.net
 *  http://foro.elhacker.net/hacking_wireless/desencriptando_wep_por_defecto_de_las_redes_ono_wifi_instantaneamente-t160928.0.html
 * */
public class OnoKeygen extends KeygenThread {

	public OnoKeygen(RouterKeygen par) {
		super(par);
	}

	public void run(){
		if ( router == null)
			return;
		if ( router.ssid.length() != 13 ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_shortessid6));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		String val = router.ssid.substring(0,12)+ 
					Integer.toString(Integer.parseInt(router.ssid.substring(12))+1);
		int [] pseed = new int[4];
		pseed[0] = 0;
		pseed[1] = 0;
		pseed[2] = 0;
		pseed[3] = 0;
		int randNumber = 0;
		String key = "";
		for (int i = 0; i < val.length(); i++)
		{
			pseed[i%4] ^= (int) val.charAt(i);
		}
		randNumber = pseed[0] | (pseed[1] << 8) | (pseed[2] << 16) | (pseed[3] << 24);
		short tmp = 0;
		for (int j = 0; j < 5; j++)
		{
			randNumber = (randNumber * 0x343fd + 0x269ec3) & 0xffffffff;
			tmp = (short) ((randNumber >> 16) & 0xff);
			key += getHexString(tmp).toUpperCase();
		}
		pwList.add(key);
		key = "";
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nomd5));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		md.reset();
		md.update(padto64(val).getBytes());
		byte [] hash = md.digest();
		for ( int i = 0 ; i < 13 ; ++i )
			key += getHexString((short)hash[i]);
		pwList.add(key.toUpperCase());
		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
	
	
	private String padto64( String val ){
		if ( val.equals("") )
			return ""; 
		String ret = "";
		for ( int i = 0; i < ( 1 + (64 / (val.length())) ) ; ++i)
			ret += val;
		return ret.substring(0,64);
	}
	
	public static String getHexString(short raw)
	{
		byte[] hex = new byte[2];
		int v = raw & 0xFF;
		hex[0] = HEX_CHAR_TABLE[v >>> 4];
		hex[1] = HEX_CHAR_TABLE[v & 0xF];
		try {
			return new String(hex, "ASCII");
		} catch (UnsupportedEncodingException e) {}
		return "";
	}
	static final byte[] HEX_CHAR_TABLE = {
	    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
	    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
	    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
	    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
	  };
}
