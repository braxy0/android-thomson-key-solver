package org.exobel.routerkeygen;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


public class KeygenThread extends Thread {
	
	MessageDigest md;
	WifiNetwork router;
	RouterKeygen parent;
	boolean stopRequested = false;
	List<String> pwList;
	
	

	public KeygenThread( RouterKeygen par )
	{
		this.parent = par;
		this.pwList = new ArrayList<String>();

	}
	
	@SuppressWarnings("unused")
	private byte[] makeSHA1Hash(byte[] input) {
		md.update(input);
		return md.digest();
	}

}
