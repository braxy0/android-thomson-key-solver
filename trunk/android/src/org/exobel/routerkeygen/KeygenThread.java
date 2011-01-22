package org.exobel.routerkeygen;

import java.security.MessageDigest;


public class KeygenThread extends Thread {
	
	MessageDigest md;
	WifiNetwork router;
	RouterKeygen parent;
	boolean stopRequested = false;
	
	
	

	public KeygenThread( RouterKeygen par )
	{
		this.parent = par;

	}
	
	@SuppressWarnings("unused")
	private byte[] makeSHA1Hash(byte[] input) {
		md.update(input);
		return md.digest();
	}

}
