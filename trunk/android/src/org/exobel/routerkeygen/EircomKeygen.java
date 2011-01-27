package org.exobel.routerkeygen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EircomKeygen extends KeygenThread {

	public EircomKeygen(RouterKeygen par) {
		super(par);
	}
	public void run(){
		String mac=  router.getMacEnd();
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nosha1));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		byte [] routerMAC = new byte[4];
		routerMAC[0] = 1;
		for (int i = 0; i < 6; i += 2)
			routerMAC[i / 2 + 1] = (byte) ((Character.digit(mac.charAt(i), 16) << 4)
					+ Character.digit(mac.charAt(i + 1), 16));
		int macDec = ( (0xFF & routerMAC[0]) << 24 ) | ( (0xFF & routerMAC[1])  << 16 ) |
					 ( (0xFF & routerMAC[2])  << 8 ) | (0xFF & routerMAC[3]);
		mac = StringUtils.dectoString(macDec) + "Although your world wonders me, ";
		md.reset();
		md.update(mac.getBytes());
		byte [] pass = new byte[13];
		byte [] hash = md.digest();
		for ( int i = 0 ; i < 13 ; ++i )
			pass[i] = hash[i];
			
		try {
			pwList.add(StringUtils.getHexString(pass));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		parent.list_key =  pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
	
}
