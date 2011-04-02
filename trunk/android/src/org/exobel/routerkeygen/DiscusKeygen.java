package org.exobel.routerkeygen;

import android.content.res.Resources;
import android.os.Handler;

public class DiscusKeygen extends KeygenThread {

	public DiscusKeygen(Handler h, Resources res) {
		super(h, res);
	}

	final int essidConst = 0xD0EC31;

	public void run(){
		byte [] routerESSID = new byte [3];
		
		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.getSSIDsubpart().charAt(i), 16) << 4)
					+ Character.digit(router.getSSIDsubpart().charAt(i + 1), 16));
		
		int routerEssid =( (0xFF & routerESSID[0])  << 16 ) |
		( (0xFF & routerESSID[1])  << 8 ) | (0xFF & routerESSID[2]);
		int result  = ( routerEssid - essidConst )>>2;
		pwList.add("YW0" + Integer.toString(result));
		handler.sendEmptyMessage(RESULTS_READY);
		return;
		
	}

}
