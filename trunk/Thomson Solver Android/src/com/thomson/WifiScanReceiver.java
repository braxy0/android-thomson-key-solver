package com.thomson;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

class WiFiScanReceiver extends BroadcastReceiver {
	  ThomsonSolver solver;

	  public WiFiScanReceiver( ThomsonSolver wifiDemo) {
	    super();
	    this.solver = wifiDemo;
	  }

	  public void onReceive(Context c, Intent intent) {
	    List<ScanResult> results = solver.wifi.getScanResults();
	    List<WifiNetwork> list = new ArrayList<WifiNetwork>();
	    for (ScanResult result : results) {
	    	  list.add(new WifiNetwork(result.SSID, result.BSSID, result.level));
	    }
	    solver.vulnerable = list;
	    WifiListAdapter adap= new WifiListAdapter(list , solver);
	    solver.lv1.setAdapter(adap); 
	 }

}
