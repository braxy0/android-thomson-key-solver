package com.thomson;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	boolean wifi_state;
	ProgressDialog progressDialog;
	ListView lv1;
	ThomsonCalc calculator;
	String [] list_key = null;
	BroadcastReceiver receiver;
	List<ScanResult> vulnerable;
	String router;
	
	Handler handler = new Handler() {
          public void handleMessage(Message msg) {
        	  
			if ( msg.what == 0 )
			{
				removeDialog(PROGRESSBAR);
				showDialog(KEY_LIST);
			}
			if ( msg.what == 1 )
			{
				removeDialog(PROGRESSBAR);
				Toast.makeText( ThomsonSolver.this , list_key[0] , Toast.LENGTH_SHORT).show();
			}
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
					String essid;
					router = ((TextView) view).getText().toString();
					
					if ( (essid = essidFilter(router))  == null )
					{
						  Toast.makeText( ThomsonSolver.this , "That essid is not a Thomson one!" , Toast.LENGTH_SHORT).show();
						  return;
					}
					ThomsonSolver.this.calculator = new ThomsonCalc(ThomsonSolver.this);
					ThomsonSolver.this.calculator.router = essid.toUpperCase();
					ThomsonSolver.this.calculator.setPriority(Thread.MAX_PRIORITY);
					ThomsonSolver.this.calculator.start();
					removeDialog(KEY_LIST);
					showDialog(PROGRESSBAR);
			}
		});
		
     
        if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}
	
	public String essidFilter( String essid ) {
		if ( essid.contains("Thomson") && essid.length() == 13 )
			return new String (essid.substring(7));
		if (  essid.contains("SpeedTouch") && essid.length() == 16 )
			return new String ( essid.substring(10));		
		return null;
	}
 
    
    private static final int PROGRESSBAR = 0; 
    private static final int KEY_LIST = 1;
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESSBAR: {
            	progressDialog = new ProgressDialog(ThomsonSolver.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setTitle("Working..");
				progressDialog.setMessage("Calculating Keys...");
				progressDialog.setCancelable(false);
				progressDialog.setProgress(0);
				progressDialog.setButton("Cancel", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
                                ThomsonSolver.this.calculator.stopRequested = true;
                				removeDialog(PROGRESSBAR);
					}
        });
				progressDialog.setIndeterminate(false);
                return progressDialog;
            }
            case KEY_LIST: {
            	Dialog dialog = new Dialog(this);

            	dialog.setContentView(R.layout.results);
            	dialog.setTitle(ThomsonSolver.this.router);
          
            	ListView list = (ListView) dialog.findViewById(R.id.list_keys);
            	list.setOnItemClickListener(new OnItemClickListener() {
        			public void onItemClick(AdapterView<?> parent, View view,
        					int position, long id) {
        					Toast.makeText(getApplicationContext(), ((TextView) view).getText() + " was copied to clipboard!",
        							Toast.LENGTH_SHORT).show();
        					ClipboardManager clipboard = 
                                  (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 

                          clipboard.setText(((TextView) view).getText());
        			}
        		});
            	list.setAdapter(new ArrayAdapter<String>(ThomsonSolver.this, android.R.layout.simple_list_item_1,
						list_key));
            	return dialog;
            }
          
        }
        return null;
    }
    
    @Override
	public void onStop() {
    	super.onStop();
		unregisterReceiver(receiver);
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wifi, menu);
        return true;
    }
    
    public void scan(){
    	try{
    		if ( !wifi.isWifiEnabled() )
    		{
				  Toast.makeText( ThomsonSolver.this , "Wifi not activated!", Toast.LENGTH_SHORT).show();
				  return;
    		}
	    	if ( wifi.startScan() )
				  Toast.makeText( ThomsonSolver.this , "Scanning Started", Toast.LENGTH_SHORT).show();
			else
				  Toast.makeText( ThomsonSolver.this , "Scanning Failed!", Toast.LENGTH_SHORT).show();
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.wifi_scan:
            scan();
			return true;
        case R.id.manual_input:
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private class WiFiScanReceiver extends BroadcastReceiver {
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
    	    solver.vulnerable = results;
    	    lv1.setAdapter(new ArrayAdapter<String>(ThomsonSolver.this, android.R.layout.simple_list_item_1, list
					));
      	 }

    }

}