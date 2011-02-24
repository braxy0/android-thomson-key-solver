package org.exobel.routerkeygen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
 * 
; The following creates an array for easy lookup of the Passphrase.
; 00 = A, 01 = B, 02 = C, etc until Z is reached, 
; then we kick back to A: 19 = Z, 1a = A, 1b = B
; The 8 hex values for the Passphrase can be found in the hash
; at these locations:
; Position 3,7,11,15,19,23,27,31 - an example using 1's and 0's,
; where the 1's are the Passphrase hex values:
; 00110011001100110011001100110011
; So if the above MD5SUM = 00990011001100110011001100110011
; Then via the array-lookup, the Passphrase is:XRRRRRRR
; X=99, R=11
 */
public class SkyV1Keygen extends KeygenThread{

	public SkyV1Keygen(RouterKeygen par) {
		super(par);
	}

	final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public void run(){
		if ( router == null)
			return;
		if ( router.getMac().length() != 12 ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_nomac));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nomd5));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		md.reset();
		md.update(router.getMac().getBytes());
		byte [] hash = md.digest();
		String key ="";
		int index = hash[1] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[3] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[5] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[7] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[9] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[11] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[13] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[15] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		
		pwList.add(key);
		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
}
