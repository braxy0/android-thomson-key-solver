package org.exobel.routerkeygen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class RouterKeygen extends Activity {

	WifiManager wifi;
	boolean wifi_state;
	ProgressDialog progressDialog;
	ListView lv1;
	KeygenThread calculator;
	List<String> list_key = null;
	BroadcastReceiver scanFinished;
	//BroadcastReceiver stateChanged; TODO
	List<WifiNetwork> vulnerable;
	String router;
	boolean activity_pref = false;
	long begin;
	Handler handler = new Handler() {
          public void handleMessage(Message msg) {
        	  
			if ( msg.what == 0 )
			{
				showDialog(KEY_LIST);
				begin = System.currentTimeMillis()-begin;
				Log.d("RouterKeygen", "Time to solve:" + begin);
				RouterKeygen.this.calculator = new KeygenThread(RouterKeygen.this);

			}
			if ( msg.what == 1 )
			{
				Toast.makeText( RouterKeygen.this , list_key.get(0) , Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if ( wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED 
				|| wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLING )
			wifi_state = true;
		else
			wifi_state = false;
		lv1 = (ListView) findViewById(R.id.ListWifi);
		list_key = new ArrayList<String>();
		lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					router = ((TextView)((RelativeLayout) view).getChildAt(2)).getText().toString();
					
					if ( !vulnerable.get(position).supported )
					{
						  Toast.makeText( RouterKeygen.this , "That essid is not a Thomson one!" , Toast.LENGTH_SHORT).show();
						  return;
					}
					else
						if (vulnerable.get(position).newThomson)
						{
							Toast.makeText( RouterKeygen.this , "That is a new Thomson router which key can not be calculated!" , Toast.LENGTH_SHORT).show();
							  return;
						}
			        begin =  System.currentTimeMillis();
			        if ( RouterKeygen.this.calculator.getState() != Thread.State.NEW )
			        	RouterKeygen.this.calculator = new KeygenThread(RouterKeygen.this);
					RouterKeygen.this.calculator.router = vulnerable.get(position).getEssid().toUpperCase();
					RouterKeygen.this.calculator.folder = folderSelect;
					RouterKeygen.this.calculator.setPriority(Thread.MAX_PRIORITY);
					RouterKeygen.this.calculator.start();
					removeDialog(KEY_LIST);
			}
		});
		
    	scanFinished = new WiFiScanReceiver(this);
        RouterKeygen.this.calculator = new KeygenThread(RouterKeygen.this);

	}
	protected void onSaveInstanceState (Bundle outState){
		
	}

	protected void onRestoreInstanceState (Bundle savedInstanceState){
		
	}
	
    public void onStart() {
    	try{ 
    		super.onStart();
    		registerReceiver(scanFinished, new IntentFilter(
    				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    		getPrefs();
    		if ( !wifi_state && wifi_on )
    			wifi.setWifiEnabled(true);
    		scan();
    		removeDialog(KEY_LIST);
			removeDialog(MANUAL_CALC); 
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @Override
	public void onStop() {
    	try{ 
    		super.onStop();
    		unregisterReceiver(scanFinished);
    		removeDialog(KEY_LIST);
			removeDialog(MANUAL_CALC); 
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
	}
    

	
	public String essidFilter( String essid ) {
		if ( essid.contains("Thomson") && essid.length() == 13 )
			return new String (essid.substring(7));
		if (  essid.contains("SpeedTouch") && essid.length() == 16 )
			return new String ( essid.substring(10));		
		return null;
	}
    
    private static final int KEY_LIST = 1;
    private static final int MANUAL_CALC = 2;
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case KEY_LIST: {
            	Dialog dialog = new Dialog(this);
            	dialog.setContentView(R.layout.results);
            	dialog.setTitle(RouterKeygen.this.router);
          
            	ListView list = (ListView) dialog.findViewById(R.id.list_keys);
            	list.setOnItemClickListener(new OnItemClickListener() {
        			public void onItemClick(AdapterView<?> parent, View view,
        					int position, long id) {
        					Toast.makeText(getApplicationContext(), ((TextView) view).getText() + " was copied to clipboard!",
        							Toast.LENGTH_SHORT).show();
        					ClipboardManager clipboard = 
                                  (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 

                          clipboard.setText(((TextView) view).getText());
                          startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        			}
            	});
            	list.setAdapter(new ArrayAdapter<String>(RouterKeygen.this, android.R.layout.simple_list_item_1,
						list_key));
            	Button share = ( Button ) dialog.findViewById(R.id.bt_share);
            	share.setOnClickListener(new View.OnClickListener(){
             		 public void onClick(View arg0) {
             			Intent i = new Intent(Intent.ACTION_SEND);
             			i.setType("text/plain");
             			i.putExtra(Intent.EXTRA_SUBJECT, router + "Keys");
             			Iterator<String> it = list_key.iterator();
             			String message = "Keys:\n";
             			while ( it.hasNext() )
             				message += it.next() + "\n"; 
             			i.putExtra(Intent.EXTRA_TEXT, message);
             			startActivity(Intent.createChooser(i, "Share Keys"));
                }
           	});
            	
            	return dialog;
            }
            case MANUAL_CALC: {
            	Dialog dialog = new Dialog(this);
            	dialog.setContentView(R.layout.manual);
            	dialog.setTitle("Manual Input");
          
            	final EditText edit = (EditText ) dialog.findViewById(R.id.manual_edittext);
            	
            	Button calc = ( Button ) dialog.findViewById(R.id.bt_manual_calc);
            	calc.setOnClickListener(new View.OnClickListener(){
           		 public void onClick(View arg0) {
                        try
                        {
                        	String essid;
        					router = edit.getText().toString();
        					
        					if ( (essid = essidFilter(router))  == null )
        					{
        						  Toast.makeText( RouterKeygen.this , "That essid is not a Thomson one!" , Toast.LENGTH_SHORT).show();
        						  return;
        					}

        			        if ( RouterKeygen.this.calculator.getState() != Thread.State.NEW )
        			        	RouterKeygen.this.calculator = new KeygenThread(RouterKeygen.this);
        					RouterKeygen.this.calculator.router = essid.toUpperCase();
        					RouterKeygen.this.calculator.folder = folderSelect;
        					RouterKeygen.this.calculator.setPriority(Thread.MAX_PRIORITY);
        					RouterKeygen.this.calculator.start();
        					removeDialog(KEY_LIST);
        					removeDialog(MANUAL_CALC);                      	
                        	
                        } catch (Exception e) {
								e.printStackTrace();
							}
                }
           	});
            	Button cancel = ( Button ) dialog.findViewById(R.id.bt_manual_cancel);
            	cancel.setOnClickListener(new View.OnClickListener(){
              		 public void onClick(View arg0) {
                         try
                         {
                        	 removeDialog(MANUAL_CALC);
                         	
                         	
                         } catch (Exception e) {
 								e.printStackTrace();
 							}
                 }
            	});
         
            	return dialog;
            }
        }
        return null;
    }
    

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wifi, menu);
        return true;
    }
    
    public void scan(){
    	try{
    		if ( wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLED 
    				&& wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING )
    		{
				  Toast.makeText( RouterKeygen.this , "Wifi not activated! Please activate Wifi!", Toast.LENGTH_SHORT).show();
				  return;
    		}
	    	if ( wifi.startScan() )
				  Toast.makeText( RouterKeygen.this , "Scanning Started", Toast.LENGTH_SHORT).show();
			else
				  Toast.makeText( RouterKeygen.this , "Scanning Failed!", Toast.LENGTH_SHORT).show();
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
        	showDialog(MANUAL_CALC);
        	return true;
        case R.id.pref:
        	activity_pref = true;
        	startActivity( new Intent(this , Preferences.class ));
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    

    boolean wifi_on;
    String folderSelect;
	private void getPrefs() {
	    SharedPreferences prefs = PreferenceManager
	                    .getDefaultSharedPreferences(getBaseContext());
	    wifi_on = prefs.getBoolean("wifion", false);
	    folderSelect = prefs.getString("folderSelect","/sdcard/thomson");
	    activity_pref = false;
    }
}