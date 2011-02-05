package org.exobel.routerkeygen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WlanKeygen extends KeygenThread {

	public WlanKeygen(RouterKeygen par) {
		super(par);
	}
	final String magic = "bcgbghgg";
	public void run(){
		if ( router == null)
			return;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nomd5));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		if ( router.getMac().length() != 12 ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_errpirelli));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		String macMod = router.getMac().substring(0,8) + router.getEssid();
		md.reset();
		try {
			md.update(magic.getBytes("ASCII"));
			md.update(macMod.getBytes("ASCII"));
			md.update(router.getMac().getBytes("ASCII"));
			byte [] hash = md.digest();
			pwList.add(StringUtils.getHexString(hash).substring(0,20).toUpperCase());
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(0);
			return;
		} catch (UnsupportedEncodingException e) {}
		
	}

}
