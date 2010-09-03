package com.thomson;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ThomsonSolver extends Activity {

	WifiManager wifi;
	ProgressDialog progressDialog;
	TextView tv;
	ListView lv1;
	String lv_arr[]={"Android","iPhone","BlackBerry","AndroidPeople"};
	ThomsonCalc calculator;
	String [] list = null;
	BroadcastReceiver receiver;
	List<ScanResult> vulnerable;
	Handler handler = new Handler() {
          public void handleMessage(Message msg) {
        	  
			if ( msg.what == 0 )
			{
				lv1.setAdapter(new ArrayAdapter<String>(ThomsonSolver.this, android.R.layout.simple_list_item_1,
							list));
				removeDialog(PROGRESSBAR);
			}
			if ( msg.what == 1 )
				  Toast.makeText( ThomsonSolver.this , list[0] , Toast.LENGTH_LONG).show();
			if ( msg.what == 2 )
				  progressDialog.setProgress(progressDialog.getProgress() + 1);
          }
	};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		lv1 = (ListView) findViewById(R.id.ListView01);

		lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					Toast.makeText(getApplicationContext(), ((TextView) view).getText() + " was copied to clipboard!",
							Toast.LENGTH_SHORT).show();
		      
					ClipboardManager clipboard = 
						(ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
	
					clipboard.setText(((TextView) view).getText());
			}
		});
		
		final EditText ed = (EditText) findViewById(R.id.edittext);

        Button calc = (Button) findViewById(R.id.button);
        calc.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				try
				{
					ThomsonSolver.this.calculator = new ThomsonCalc(ThomsonSolver.this);
					ThomsonSolver.this.calculator.router = "Thomson" + ed.getText().toString().toUpperCase();
					ThomsonSolver.this.calculator.start();
					showDialog(PROGRESSBAR);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return;
				}
			}
		});
        
        if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}
 
    public void setList(String[] ret) {
    	this.list = ret;
    }  
    
    private static final int PROGRESSBAR = 0; 
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESSBAR: {
            	progressDialog = new ProgressDialog(ThomsonSolver.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setTitle("Working..");
				progressDialog.setMessage("Calculating Keys...");
				progressDialog.setCancelable(true);
				progressDialog.setProgress(0);
				progressDialog.setIndeterminate(false);
                return progressDialog;
            }
          
        }
        return null;
    }
    
    @Override
	public void onStop() {
		unregisterReceiver(receiver);
	}

    private class WiFiScanReceiver extends BroadcastReceiver {
    	  ThomsonSolver solver;

    	  public WiFiScanReceiver( ThomsonSolver wifiDemo) {
    	    super();
    	    this.solver = wifiDemo;
    	  }

    	  public void onReceive(Context c, Intent intent) {
    	    List<ScanResult> results = solver.wifi.getScanResults();
    	    List<ScanResult> vulnerable = new ArrayList<ScanResult>();
    	    for (ScanResult result : results) {
    	      if (result.SSID.contains("Thomson") || result.SSID.contains("SpeedTouch"))
    	    	  vulnerable.add(result);
    	    }
    	    solver.vulnerable = vulnerable;
      	 }

    }

}