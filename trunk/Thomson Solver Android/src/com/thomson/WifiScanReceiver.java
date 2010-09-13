package com.thomson;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.widget.ArrayAdapter;
import android.widget.Toast;


class WiFiScanReceiver extends BroadcastReceiver {
	  ThomsonSolver solver;

	  public WiFiScanReceiver( ThomsonSolver wifiDemo) {
	    super();
	    this.solver = wifiDemo;
	  }

	  public void onReceive(Context c, Intent intent) {
	    List<ScanResult> results = solver.wifi.getScanResults();
	    List<String> list = new ArrayList<String>();
	    for (ScanResult result : results) {
	    	  list.add(result.SSID);
	    }
		  Toast.makeText( solver , "Scanning Finished!", Toast.LENGTH_SHORT).show();

	    solver.vulnerable = results;
	    solver.lv1.setAdapter(new ArrayAdapter<String>( solver, android.R.layout.simple_list_item_1, list
				));
	 }

}
