package org.exobel.routerkeygen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiStateReceiver extends BroadcastReceiver {

	WifiManager wifi;
	public WifiStateReceiver(WifiManager wifi){
		this.wifi = wifi;
	}
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if ( wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED  )
		{
			wifi.startScan();
    		arg0.unregisterReceiver(this);
    		Toast.makeText( arg0 ,
					arg0.getResources().getString(R.string.msg_scanstarted),
					Toast.LENGTH_SHORT).show();
    		return;
		}
		if ( wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING )
		{
			Toast.makeText( arg0 , 
					  arg0.getResources().getString(R.string.msg_nowifi),
					  Toast.LENGTH_SHORT).show();
			return;
		}
	}

}
