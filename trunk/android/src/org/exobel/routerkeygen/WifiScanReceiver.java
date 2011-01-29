package org.exobel.routerkeygen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.widget.Toast;

class WiFiScanReceiver extends BroadcastReceiver {
	  RouterKeygen solver;

	  public WiFiScanReceiver( RouterKeygen wifiDemo) {
	    super();
	    this.solver = wifiDemo;
	  }

	  public void onReceive(Context c, Intent intent) {
	    List<ScanResult> results = solver.wifi.getScanResults();
	    List<WifiNetwork> list = new ArrayList<WifiNetwork>();
	    Set<WifiNetwork> set = new TreeSet<WifiNetwork>();

	    for (int i = 0; i < results.size() - 1; ++i)
	    	for (int j = i+1; j < results.size(); ++j)
		    	if(results.get(i).SSID.equals(results.get(j).SSID))
		    		results.remove(j--);
	    
	    for (ScanResult result : results) {
	    	  set.add(new WifiNetwork(result.SSID, result.BSSID, result.level , result.capabilities));
	    }
	    Iterator<WifiNetwork> it = set.iterator();
	    while( it.hasNext())
	    	list.add(it.next());
	    solver.vulnerable = list;
	    if (  list.isEmpty() )
	    {
			Toast.makeText( solver , solver.getResources().getString(R.string.msg_nowifidetected) ,
					Toast.LENGTH_SHORT).show();
	    }
	    solver.scanResuls.setAdapter(new WifiListAdapter(list , solver)); 
	    try{
		solver.unregisterReceiver(this);   
	    }catch(Exception e ){}
	    
	 }

}
