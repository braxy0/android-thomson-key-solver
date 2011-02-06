package org.exobel.routerkeygen;

public class NativeThomson extends KeygenThread{

	public NativeThomson(RouterKeygen par) {
		super(par);
	}
	

	static {
		System.loadLibrary("thomson");
    }
    		  
  /** 
   * Native processing without a dictionary.
   */
	public native String[] thomson( byte [] essid );
  
  
	public void run(){
		if ( router == null)
			return;
		if ( router.getEssid().length() != 6 ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_shortessid6));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		byte [] routerESSID = new byte[3];

		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.getEssid().charAt(i), 16) << 4)
					+ Character.digit(router.getEssid().charAt(i + 1), 16));
		String [] results;
		try{
			results = this.thomson(routerESSID);
		}catch (Exception e) {
			pwList.add(parent.getResources().getString(R.string.msg_err_native));
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		if ( stopRequested )
			return;
		for (int i = 0 ; i < results.length ; ++i  )
			pwList.add(results[i]);
		
		if(pwList.toArray().length == 0)
		{
			pwList.add(parent.getResources().getString(R.string.msg_errnomatches));
			parent.list_key = pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		parent.list_key = pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}

}
