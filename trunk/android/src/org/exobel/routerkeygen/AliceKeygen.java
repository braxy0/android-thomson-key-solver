package org.exobel.routerkeygen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AliceKeygen extends KeygenThread {

	public AliceKeygen(RouterKeygen par) {
		super(par);
	}
	
	final private String preInitCharset =
			 "0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvWxyz0123";
	 
	 private byte specialSeq[/*32*/]= {
		0x64, (byte) 0xC6, (byte) 0xDD, (byte) 0xE3, 
		(byte) 0xE5, 0x79, (byte) 0xB6, (byte) 0xD9, 
		(byte) 0x86, (byte) 0x96, (byte) 0x8D, 0x34, 
		0x45, (byte) 0xD2, 0x3B, 0x15, 
		(byte) 0xCA, (byte) 0xAF, 0x12, (byte) 0x84, 
		0x02, (byte) 0xAC, 0x56, 0x00, 
		0x05, (byte) 0xCE, 0x20, 0x75, 
		(byte) 0x91, 0x3F, (byte) 0xDC, (byte) 0xE8};
	
	
	public void run() {

		if ( router == null)
			return;
		if ( router.supportedAlice == null )
		{
			pwList.add(parent.getResources().getString(R.string.msg_erralicenotsupported));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		if ( router.supportedAlice.isEmpty() )
		{
			pwList.add(parent.getResources().getString(R.string.msg_erralicenotsupported));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		
		try {
			md = MessageDigest.getInstance("SHA256");
		} catch (NoSuchAlgorithmException e1) {
			pwList.add(parent.getResources().getString(R.string.msg_nosha256));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		for ( int j = 0 ; j <router.supportedAlice.size() ; ++j )
		{/*For pre AGPF 4.5.0sx*/
			String serialStr = router.supportedAlice.get(j).serial + "X";
			int Q = router.supportedAlice.get(j).magic[0];
			int k = router.supportedAlice.get(j).magic[1] ;
			int serial = ( Integer.valueOf(router.getEssid()) - Q ) / k;
			String tmp = Integer.toString(serial);
			for (int i = 0; i < 7 - tmp.length(); i++){
				serialStr += "0";
			}
			serialStr += tmp;
			
			byte [] mac = new byte[6];
			String key = "";
			byte [] hash;		
			
			if (  router.getMac().length() == 12 ) {
					
				
				for (int i = 0; i < 12; i += 2)
					mac[i / 2] = (byte) ((Character.digit(router.getMac().charAt(i), 16) << 4)
							+ Character.digit(router.getMac().charAt(i + 1), 16));
	
				md.reset();
				md.update(specialSeq);
				try {
					md.update(serialStr.getBytes("ASCII"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				md.update(mac);
				hash = md.digest();
				for ( int i = 0 ; i < 24 ; ++i )
				{
					key += preInitCharset.charAt(hash[i] & 0xFF);
				}
				if ( !pwList.contains(key)  ) 
					pwList.add(key);
			}
			
			/*For post AGPF 4.5.0sx*/
			String macEth = router.getMac().substring(0,6);
			int extraNumber = 0;
			while ( extraNumber <= 9 )
			{
				String calc = Integer.toHexString(Integer.valueOf(
						extraNumber + router.getEssid()) ).toUpperCase();
				if ( macEth.charAt(5) == calc.charAt(0))
				{
					macEth += calc.substring(1);
					break;
				}
				extraNumber++;
			}
			if ( macEth.equals(router.getMac().substring(0,6)) )
			{
				parent.list_key = pwList;
				parent.handler.sendEmptyMessage(0);
				return;
			}
			
			
			
			for (int i = 0; i < 12; i += 2)
				mac[i / 2] = (byte) ((Character.digit(macEth.charAt(i), 16) << 4)
						+ Character.digit(macEth.charAt(i + 1), 16));
			md.reset();
			md.update(specialSeq);
			try {
				md.update(serialStr.getBytes("ASCII"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			md.update(mac);
			key = "";
			hash = md.digest();
			for ( int i = 0 ; i < 24 ; ++i )
				key += preInitCharset.charAt(hash[i] & 0xFF);
			if ( !pwList.contains(key)  ) 
				pwList.add(key);
		}
		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
}
