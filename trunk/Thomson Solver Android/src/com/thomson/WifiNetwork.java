package com.thomson;

public class WifiNetwork implements Comparable<WifiNetwork>{
	String ssid;
	String mac;
	String ssidSubpart;
	boolean supported;
	boolean newThomson;
	int level;
	public WifiNetwork(String ssid, String mac,int level){
		this.ssid = ssid;
		this.mac = mac.toUpperCase();
		this.level  = level;
		this.supported =  essidFilter();
	}
	
	public int getLevel(){
//		if ( level >  )
		return level;
	}
	
	public String getEssid(){
		return ssidSubpart;
	}
	
	private boolean essidFilter() {
		if ( ssid.contains("Thomson") && ssid.length() == 13 )
		{
			ssidSubpart = new String (ssid.substring(7));
			if (ssidSubpart.charAt(0) == mac.charAt(9) &&  ssidSubpart.charAt(1) == mac.charAt(10) &&
			      ssidSubpart.charAt(2) == mac.charAt(12) &&  ssidSubpart.charAt(3) == mac.charAt(13) &&	
			      ssidSubpart.charAt(4) == mac.charAt(15) &&  ssidSubpart.charAt(5) == mac.charAt(16))
				newThomson = true;
			return true;
		}
		if (  ssid.contains("SpeedTouch") && ssid.length() == 16 )
		{
			ssidSubpart = new String ( ssid.substring(10));		
			if (ssidSubpart.charAt(0) == mac.charAt(9) &&  ssidSubpart.charAt(1) == mac.charAt(10) &&
				  ssidSubpart.charAt(2) == mac.charAt(12) &&  ssidSubpart.charAt(3) == mac.charAt(13) &&	
				  ssidSubpart.charAt(4) == mac.charAt(15) &&  ssidSubpart.charAt(5) == mac.charAt(16))
				newThomson = true;;
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(WifiNetwork another) {
		if ( another.level == this.level && this.ssid.equals(another.ssid) && this.mac.equals(another.mac) )
			return 0;
		if ( this.supported && !this.newThomson )
			return -1;
		return 1;
	}
	
	
}
