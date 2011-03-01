package org.exobel.routerkeygen;

public class VerizonKeygen extends KeygenThread {

	public VerizonKeygen(RouterKeygen par) {
		super(par);
	}

	
	public void run(){
		String ssid = router.ssid;
		if ( ssid.length() != 5 )
		{
			pwList.add(parent.getResources().getString(R.string.msg_shortessid5));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		char [] inverse = new char[5];
		inverse[0] = ssid.charAt(4);
		inverse[1] = ssid.charAt(3);
        inverse[2] = ssid.charAt(2);
		inverse[3] = ssid.charAt(1);
		inverse[4] = ssid.charAt(0);
		
		int result = 0;
		try{
			result = Integer.valueOf(String.copyValueOf(inverse), 36);
		}catch(NumberFormatException e){
			pwList.add(parent.getResources().getString(R.string.msg_err_verizon_ssid));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
	    if ( !router.mac.equals(""))
	    {
	    	pwList.add(router.mac.substring(3,5) + router.mac.substring(6,8) + 
	    					Integer.toHexString(result).toUpperCase());
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(0);
			return;
	    }
	    else
	    {
	    	pwList.add("1801" + Integer.toHexString(result).toUpperCase());
	    	pwList.add("1F90" + Integer.toHexString(result).toUpperCase());
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(0);
			return;
	    }
	}
}
