package org.exobel.routerkeygen;

import java.io.Serializable;

public class WifiNetwork implements Comparable<WifiNetwork>, Serializable{
	
	private static final long serialVersionUID = 1L;
	String ssid;
	String mac;
	String ssidSubpart;
	boolean supported;
	boolean newThomson;
	int level;
	
	TYPE type;
	static enum TYPE {THOMSON , DLINK , DISCUS , VERIZON , EIRCOM};
	public WifiNetwork(String ssid, String mac, int level){
		this.ssid = ssid;
		this.mac = mac.toUpperCase();
		this.level  = level;
		this.newThomson = false;
		this.supported =  essidFilter();
	}
	
	public int getLevel(){
		return level;
	}
	
	public String getEssid(){
		return ssidSubpart;
	}
	
	public String getMacEnd(){
		return mac.substring(9, 11) + mac.substring(12, 14) + mac.substring(15, 17);
	}
	
	public String getMac(){
		return  mac.substring(0, 2) +  mac.substring(3, 5) +  
		 		mac.substring(6, 8) + mac.substring(9, 11) + 
				mac.substring(12, 14) + mac.substring(15, 17);
	}
	
	private boolean essidFilter() {
		if ( ( ssid.contains("BTHomeHub") && ssid.length() == 15 )  ||
		     ( ssid.contains("Thomson") && ssid.length() == 13 )    ||
		     ( ssid.contains("SpeedTouch") && ssid.length() == 16 ) ||
		     ( ssid.contains("O2Wireless") && ssid.length() == 16 ) ||
		     ( ssid.contains("Orange") && ssid.length() == 12 ) || 
		     ( ssid.contains("Infinitum") && ssid.length() == 15 )  ||
		     ( ssid.contains("BigPond") && ssid.length() == 13 )  ||
		     ( ssid.contains("Otenet") && ssid.length() == 12 ) ||
		     ( ssid.contains("BBox") && ssid.length() == 10 ) ||
		     ( ssid.contains("DMax") && ssid.length() == 10 ))
		{
			ssidSubpart = new String (ssid.substring(ssid.length()-6));
			if ( !mac.equals("") )
				if ( ssidSubpart.equals(getMacEnd()) )
					newThomson = true;
			type = TYPE.THOMSON;
			return true;
		}
		if (  ssid.contains("DLink-") && ssid.length() == 12 )
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-6));
			type = TYPE.DLINK;
			return true;
		}
		if ( ( ssid.contains("Discus-") && ssid.length() == 13 ) ||
			( ssid.contains("Discus--") && ssid.length() == 14 )	)
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-6));
			type = TYPE.DISCUS;
			return true;
		}
		if ( ( ssid.contains("Eircom") && ssid.length() >= 14 ) ||
		     ( ssid.contains("eircom") && ssid.length() >= 14 )	)
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-8));
			if ( mac.equals("") )
				calcEircomMAC();
			type = TYPE.EIRCOM;
			return true;
		}
		if ( ssid.length() == 5  && ( mac.contains("00:1F:90") || 
				mac.contains("00:18:01") || mac.contains("00:20:E0") ||
				mac.contains("00:0F:B3") || mac.contains("00:1E:A7") ||
				mac.contains("00:15:05") || mac.contains("00:24:7B") ) )
		{
			ssidSubpart = ssid;
			type = TYPE.VERIZON;
			return true;
		}
		return false;
	}
	
	public void calcEircomMAC(){
		String end = Integer.toHexString( Integer.parseInt(ssidSubpart, 8) ^ 0x000fcc );
		this.mac = "00:0F:CC" +  ":" + end.substring(0,2)+ ":" +
					end.substring(2,4)+ ":" + end.substring(4,6);
	}

	public int compareTo(WifiNetwork another) {
		if ( another.level == this.level && this.ssid.equals(another.ssid) && this.mac.equals(another.mac) )
			return 0;
		if ( this.supported && !this.newThomson )
			return -1;
		return 1;
	}
	
	
}
