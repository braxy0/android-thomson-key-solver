package org.exobel.routerkeygen;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class AutoConnectManager extends BroadcastReceiver {
	WifiManager wifi;
	List<String> keys;
	Activity parent;
	WifiNetwork router;
	public AutoConnectManager(WifiManager wifi , List<String> keys , WifiNetwork router ){
		this.wifi = wifi;
		this.keys = keys;
		this.router = router;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		if ( info != null )
		{
			info.getState();
		}
		if ( keys.isEmpty() )
		{
			try{
				parent.unregisterReceiver(this);   
			}catch(Exception e ){}
		}
		WifiConfiguration wc = new WifiConfiguration();
		wc.SSID = "\"" + router.ssid	 + "\"";
		wc.preSharedKey  = "\"" + keys.get(0) +"\"";
		keys.remove(0);
		wc.hiddenSSID = true;
		wc.status = WifiConfiguration.Status.ENABLED;        
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		int res = wifi.addNetwork(wc);
		Log.d("WifiPreference", "add Network returned " + res );
		boolean b = wifi.enableNetwork(res, true);        
		Log.d("WifiPreference", "enableNetwork returned " + b );
	}

}
