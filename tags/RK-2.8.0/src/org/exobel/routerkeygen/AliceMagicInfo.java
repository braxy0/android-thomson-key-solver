package org.exobel.routerkeygen;

import java.io.Serializable;

public class AliceMagicInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1640975633984337261L;
	String alice;
	boolean supported;
	int [] magic;
	String serial;
	String mac;
	public AliceMagicInfo(String alice, boolean supported, int[] magic,
			String serial, String mac) {
		this.alice = alice;
		this.supported = supported;
		this.magic = magic;
		this.serial = serial;
		this.mac = mac;
	}
}
