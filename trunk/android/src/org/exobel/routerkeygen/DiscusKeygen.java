package org.exobel.routerkeygen;

public class DiscusKeygen extends KeygenThread {

	final int essidConst = 0xD0EC31;
	public DiscusKeygen(RouterKeygen par) {
		super(par);
	}
	
	public void run(){
		byte [] routerESSID = new byte [3];
		
		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.getEssid().charAt(i), 16) << 4)
					+ Character.digit(router.getEssid().charAt(i + 1), 16));
		
		int routerEssid =( (0xFF & routerESSID[0])  << 16 ) |
		( (0xFF & routerESSID[1])  << 8 ) | (0xFF & routerESSID[2]);
		int result  = ( routerEssid - essidConst )>>2;
		pwList.add("YW0" + Integer.toString(result));
		parent.list_key =  pwList;
		parent.handler.sendEmptyMessage(0);
		return;
		
	}

}
