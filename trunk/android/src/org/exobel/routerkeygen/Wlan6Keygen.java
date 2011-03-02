package org.exobel.routerkeygen;

public class Wlan6Keygen extends KeygenThread {

	public Wlan6Keygen(RouterKeygen par) {
		super(par);
	}
	
	
	public void run()
	{
		if ( router == null )
			return;
		if ( router.getMac().equals("") ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_nomac));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		String ssidStr = router.getEssid();
		String macStr = router.mac;
		char [] ESSID = {
				'W', 'L', 'A', 'N', '1', '2','3', '4', '5','6'
		    };
		char [] BSSID = {
		        '1', '1', ':', '2', '2', ':', '3', '3', ':', '4', 
		    '4', ':', '5', '5', ':', '6', '6'
		    };
		ESSID[4] = ssidStr.charAt(0);
		ESSID[5] = ssidStr.charAt(1);
		ESSID[6] = ssidStr.charAt(2);
		ESSID[7] = ssidStr.charAt(3);
		ESSID[8] = ssidStr.charAt(4);
		ESSID[9] = ssidStr.charAt(5);
		BSSID[15] = macStr.charAt(15);
		BSSID[16] = macStr.charAt(16);
		int k = 4;
		while (k <= 9)
		{ 
		    if( ESSID[k] >= 'A')
		        ESSID[k] = (char)(ESSID[k] - 55);
		    k++;
		}
		k = 15;
		while (k <= 16)
		{
		    if(BSSID[k] >= 'A' )
		        BSSID[k] = (char)(BSSID[k] - 55);
		    k++;	
		}
		for ( int i = 0; i < 10 ; ++i )
		{
			/*Do not change the order of this instructions*/
			int aux = i + ( ESSID[7] & 0xf ) +  ( BSSID[15] & 0xf ) + ( BSSID[16] & 0xf );
			int aux1 = ( ESSID[5] & 0xf ) + ( ESSID[6] & 0xf ) + ( ESSID[8] & 0xf ) + ( ESSID[9] & 0xf );
			int second = aux ^ ( ESSID[9] & 0xf );
			int sixth = aux ^ ( ESSID[8] & 0xf );
			int tenth = aux ^ ( ESSID[7] & 0xf );
			int third = aux1 ^ ( ESSID[6] & 0xf );
			int seventh = aux1 ^  ( BSSID[15] & 0xf );
			int eleventh = aux1 ^ ( BSSID[16] & 0xf );
			int fourth =  ( BSSID[15] & 0xf ) ^ ( ESSID[9] & 0xf );
			int eighth = ( BSSID[16] & 0xf ) ^ ( ESSID[8] & 0xf );
			int twelfth = aux ^ aux1;
			int fifth = second ^ eighth;
			int ninth = seventh ^ eleventh;
			int thirteenth = third ^ tenth;
			int first = twelfth ^ sixth;
			String key = Integer.toHexString(Integer.valueOf(first & 0xf)) + Integer.toHexString(Integer.valueOf(second & 0xf)) +
						Integer.toHexString(Integer.valueOf(third & 0xf)) + Integer.toHexString(Integer.valueOf(fourth & 0xf)) +
						Integer.toHexString(Integer.valueOf(fifth & 0xf)) + Integer.toHexString(Integer.valueOf(sixth & 0xf)) +
						Integer.toHexString(Integer.valueOf(seventh & 0xf)) + Integer.toHexString(Integer.valueOf(eighth & 0xf)) +
						Integer.toHexString(Integer.valueOf(ninth & 0xf)) + Integer.toHexString(Integer.valueOf(tenth & 0xf)) + 
						Integer.toHexString(Integer.valueOf(eleventh & 0xf)) + Integer.toHexString(Integer.valueOf(twelfth & 0xf)) +
						Integer.toHexString(Integer.valueOf(thirteenth & 0xf));
			
			pwList.add(key.toUpperCase());
		}
		parent.list_key =  pwList;
		parent.handler.sendEmptyMessage(0);
		if ( ( ESSID[4] != macStr.charAt(10) ) || ( ESSID[5] != macStr.charAt(12) ) ||( ESSID[6] != macStr.charAt(13) ) )
		{
			pwList.clear();
			pwList.add(parent.getResources().getString(R.string.msg_err_essid_no_match));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
		}
		return;
	}


}
