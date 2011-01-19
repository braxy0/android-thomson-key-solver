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
	    List<String> list = new ArrayList<String>();
	    List<Integer> strength = new ArrayList<Integer>();
	    for (ScanResult result : results) {
	    	  list.add(result.SSID);
	    	  strength.add(result.level);
	    }
		//  Toast.makeText( solver , "Scanning Finished!", Toast.LENGTH_SHORT).show();

	    solver.vulnerable = results;
	    WifiListAdapter adap= new WifiListAdapter(list , solver);
	    adap.setStrenght(strength);
	    solver.lv1.setAdapter(adap); 
	 }

}
