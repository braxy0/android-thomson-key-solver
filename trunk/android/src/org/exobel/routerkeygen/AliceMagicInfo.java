package org.exobel.routerkeygen;

public class AliceMagicInfo {
	String alice;
	boolean supported;
	int [] magic;
	String serial;
	String mac;
	public AliceMagicInfo(String alice, boolean supported, int[] magic,
			String serial, String mac) {
		super();
		this.alice = alice;
		this.supported = supported;
		this.magic = magic;
		this.serial = serial;
		this.mac = mac;
	}
}
