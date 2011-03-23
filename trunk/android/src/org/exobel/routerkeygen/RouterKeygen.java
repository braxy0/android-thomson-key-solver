package org.exobel.routerkeygen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.exobel.routerkeygen.WifiNetwork.TYPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RouterKeygen extends Activity {

	WifiManager wifi;
	boolean wifi_state;
	ListView scanResuls;
	KeygenThread calculator;
	ArrayList<String> list_key = null;
	BroadcastReceiver scanFinished;
	BroadcastReceiver stateChanged;
	ArrayList<WifiNetwork> vulnerable;
	WifiNetwork router;
	long begin;
	static final String TAG = "RouterKeygen";
		final String welcomeScreenShownPref = "welcomeScreenShown";

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		wifi = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
		wifi_state = wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED ||  
		wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLING;
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean welcomeScreenShown = mPrefs.getBoolean( welcomeScreenShownPref, false);

		if (!welcomeScreenShown) {

			String whatsNewTitle = getString(R.string.msg_welcome_title);
			String whatsNewText = getString(R.string.msg_welcome_text);
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(whatsNewTitle).setMessage(whatsNewText).setPositiveButton(
					R.string.bt_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putBoolean(welcomeScreenShownPref, true);
			editor.commit();
		}


		scanResuls = (ListView) findViewById(R.id.ListWifi);
		scanResuls.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				router = vulnerable.get(position);
				if (router.newThomson)
				{
					Toast.makeText( RouterKeygen.this , getString(R.string.msg_newthomson) ,
							Toast.LENGTH_SHORT).show();
					return;
				}
				calcKeys(router);
			}
		});
		stateChanged = new WifiStateReceiver(wifi);
		scanFinished = new WiFiScanReceiver(this);
		if ( savedInstanceState == null  )
			return;
		Boolean warning = (Boolean)savedInstanceState.getSerializable("warning");
		if ( warning != null )
		{
			removeDialog(DIALOG_NATIVE_CALC);
			if ( calculator != null )
				calculator.stopRequested = true;
		}
		ArrayList<WifiNetwork> list_networks =(ArrayList<WifiNetwork>) savedInstanceState.getSerializable("networks");
		if ( list_networks != null )
		{
			vulnerable = list_networks;
			scanResuls.setAdapter(new WifiListAdapter(vulnerable, this));
		}
		WifiNetwork r = (WifiNetwork) savedInstanceState.getSerializable("warning");
		if ( r != null )
		{
			router = r;
		}
		else
			router = new WifiNetwork("","",0,"",this);
		ArrayList<String> list_k =  (ArrayList<String>) savedInstanceState.getSerializable("keys");
		if ( list_k != null )
		{
			list_key = list_k;
		}
	}
	
	protected void onSaveInstanceState (Bundle outState){	
		try {
			if ( calculator instanceof NativeThomson )
			{
				outState.putSerializable("warning", true);
			}
			outState.putSerializable("router", router);
			outState.putSerializable("keys", list_key );
			outState.putSerializable("networks", vulnerable );
		}
		catch(Exception e){}
	}


	public void onStart() {
		super.onStart();
		getPrefs();
		if ( wifiOn )
		{
			if ( !wifi.setWifiEnabled(true))
				Toast.makeText( RouterKeygen.this , getString(R.string.msg_wifibroken),
						Toast.LENGTH_SHORT).show();
			else
				wifi_state = true;
		}
		scan();
	}

	public void onStop() {
		try{ 
			super.onStop();
			unregisterReceiver(scanFinished);
			unregisterReceiver(stateChanged);
			removeDialog(DIALOG_KEY_LIST);
			removeDialog(DIALOG_MANUAL_CALC); 
		}
		catch (Exception e) {}
	}
	ProgressDialog progressDialog;
	private static final int DIALOG_THOMSON3G = 0; 
	private static final int DIALOG_KEY_LIST = 1;
	private static final int DIALOG_MANUAL_CALC = 2;
	private static final int DIALOG_NATIVE_CALC = 3;
	private static final int DIALOG_AUTO_CONNECT = 4;
	protected Dialog onCreateDialog(int id ) {
		switch (id) {
			case DIALOG_THOMSON3G: {
				progressDialog = new ProgressDialog(RouterKeygen.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(getString(R.string.dialog_thomson3g));
				progressDialog.setMessage(getString(R.string.dialog_thomson3g_msg));
				progressDialog.setCancelable(false);
				progressDialog.setProgress(0);
				progressDialog.setButton(getString(R.string.bt_manual_cancel),
						new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if ( RouterKeygen.this.calculator != null )
							RouterKeygen.this.calculator.stopRequested = true;
						removeDialog(DIALOG_THOMSON3G);
					}
				});
				progressDialog.setIndeterminate(false);
				return progressDialog;
			}
			case DIALOG_KEY_LIST: {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle(router.ssid);
			    LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
			    View layout = inflater.inflate(R.layout.results,
			                                   (ViewGroup) findViewById(R.id.layout_root));
			    ListView list = (ListView) layout.findViewById(R.id.list_keys);
				list.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String key = ((TextView)view).getText().toString();
						Toast.makeText(getApplicationContext(), key + " " 							
								+ getString(R.string.msg_copied),
								Toast.LENGTH_SHORT).show();
						ClipboardManager clipboard = 
							(ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
	
						clipboard.setText(key);
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					}
				});
				
				list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_key)); 
				/*
				 * TODO: Auto connect
				 * Still not working as wished though it works +-.
				 
				builder.setPositiveButton(RouterKeygen.this.getResources().getString(R.string.bt_connect),
						new OnClickListener() {	
							public void onClick(DialogInterface dialog, int which) {
								wifi.disconnect();
								AutoConnectManager auto = new AutoConnectManager(wifi, list_key , 
														router , RouterKeygen.this , handler);
								auto.activate();
								registerReceiver( auto, new IntentFilter(
										WifiManager.NETWORK_STATE_CHANGED_ACTION));showDialog(DIALOG_AUTO_CONNECT);
							}
				});*/
				builder.setNeutralButton(RouterKeygen.this.getResources().getString(R.string.bt_share),
							new OnClickListener() {	
								public void onClick(DialogInterface dialog, int which) {
									try
									{
										Intent i = new Intent(Intent.ACTION_SEND);
										i.setType("text/plain");
										i.putExtra(Intent.EXTRA_SUBJECT, router.ssid + getString(R.string.share_msg_begin));
										Iterator<String> it = list_key.iterator();
										String message = router.ssid + getString(R.string.share_msg_begin) + ":\n";
										while ( it.hasNext() )
											message += it.next() + "\n";
										
										i.putExtra(Intent.EXTRA_TEXT, message);
										message = getString(R.string.share_title);
										startActivity(Intent.createChooser(i, message));
									}
									catch(Exception e)
									{
										Toast.makeText( RouterKeygen.this , getString(R.string.msg_err_sendto) , 
												Toast.LENGTH_SHORT).show();
										return;
									}
								}
							});
				builder.setNegativeButton(getString(R.string.bt_save_sd), new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						removeDialog(DIALOG_MANUAL_CALC);
						if ( !Environment.getExternalStorageState().equals("mounted")  && 
							     !Environment.getExternalStorageState().equals("mounted_ro")	)
						{
							Toast.makeText( RouterKeygen.this , getString(R.string.msg_nosdcard),
								Toast.LENGTH_SHORT).show();
							return ;
						}
						try {
							BufferedWriter out = new BufferedWriter(
									new FileWriter(folderSelect + File.separator + router.ssid + ".txt"));
							out.write(router.ssid + " KEYS");
							out.newLine();
							for ( String s : list_key )
							{
								out.write(s);
								out.newLine();
							}
							out.close();
						}
						catch (IOException e)
						{
							Toast.makeText( RouterKeygen.this , getString(R.string.msg_err_saving_key_file),
									Toast.LENGTH_SHORT).show();
							return ;
						}
						Toast.makeText( RouterKeygen.this , router.ssid + ".txt " + getString(R.string.msg_saved_key_file),
								Toast.LENGTH_SHORT).show();
					}
				});
				
				builder.setView(layout);
				return builder.create();
			}
			case DIALOG_MANUAL_CALC: {
				AlertDialog.Builder builder = new Builder(this); 
				final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
				final View layout = inflater.inflate(R.layout.manual_input,
                        (ViewGroup) findViewById(R.id.manual_root));
				builder.setTitle(getString(R.string.menu_manual));
				String[] routers = getResources().getStringArray(R.array.supported_routers);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
										android.R.layout.simple_dropdown_item_1line, routers);
				final AutoCompleteTextView edit = (AutoCompleteTextView) layout.findViewById(R.id.manual_autotext);
				edit.setAdapter(adapter);
				edit.setThreshold(1);
				InputFilter filterSsid = new InputFilter() { 
			        public CharSequence filter(CharSequence source, int start, int end, 
			        		Spanned dest, int dstart, int dend) { 
			        		                for (int i = start; i < end; i++) { 
			        		                        if (!Character.isLetterOrDigit(source.charAt(i)) &&
			        		                        		source.charAt(i) != '-' ) { 
			        		                                return ""; 
			        		                        } 
			        		                } 
			        		                return null; 
			        		        }
	     		};
			    edit.setFilters(new InputFilter[]{filterSsid});
			    if ( manualMac )
			    {
			    	layout.findViewById(R.id.manual_mac_root).setVisibility(View.VISIBLE);
			    	edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				    EditText mac1 = (EditText) layout.findViewById(R.id.input_mac_pair1);
				    EditText mac2 = (EditText) layout.findViewById(R.id.input_mac_pair2);
				    EditText mac3 = (EditText) layout.findViewById(R.id.input_mac_pair3);
				    EditText mac4 = (EditText) layout.findViewById(R.id.input_mac_pair4);
				    EditText mac5 = (EditText) layout.findViewById(R.id.input_mac_pair5);
				    EditText mac6 = (EditText) layout.findViewById(R.id.input_mac_pair6);
	
	        		InputFilter filterMac = new InputFilter() { 
				        public CharSequence filter(CharSequence source, int start, int end, 
				        		Spanned dest, int dstart, int dend) { 
				        		                try{/*TODO:Lazy mode programming, improve in the future*/
				        		                	Integer.parseInt((String) source , 16);
				        		                }
				        		                catch( Exception e){
				        		                	return "";
				        		                }
				        		                return null; 
				        		        }
				        		}; 
				    mac1.setFilters(new InputFilter[]{filterMac});
				    mac2.setFilters(new InputFilter[]{filterMac});
				    mac3.setFilters(new InputFilter[]{filterMac});
				    mac4.setFilters(new InputFilter[]{filterMac});
				    mac5.setFilters(new InputFilter[]{filterMac});
				    mac6.setFilters(new InputFilter[]{filterMac});
			    }
				builder.setNeutralButton(getString(R.string.bt_manual_calc), new OnClickListener() {			
					public void onClick(DialogInterface dialog, int which) {
						String ssid = edit.getText().toString().trim();
						String mac = "";
						if ( manualMac )
						{
						    EditText mac1 = (EditText) layout.findViewById(R.id.input_mac_pair1);
						    EditText mac2 = (EditText) layout.findViewById(R.id.input_mac_pair2);
						    EditText mac3 = (EditText) layout.findViewById(R.id.input_mac_pair3);
						    EditText mac4 = (EditText) layout.findViewById(R.id.input_mac_pair4);
						    EditText mac5 = (EditText) layout.findViewById(R.id.input_mac_pair5);
						    EditText mac6 = (EditText) layout.findViewById(R.id.input_mac_pair6);
						    mac= mac1.getText().toString()+':'+mac2.getText().toString()+':'+
						    	 mac3.getText().toString()+':'+mac4.getText().toString()+':'+
						    	 mac5.getText().toString()+':'+mac6.getText().toString();
					    }
						if ( ssid.equals("") )
							return;
						begin =  System.currentTimeMillis();
						router = new WifiNetwork(ssid, mac , 0 ,"" , RouterKeygen.this);
						calcKeys(router);
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);						
					}
				});
				builder.setNegativeButton(getString(R.string.bt_manual_cancel), new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						removeDialog(DIALOG_MANUAL_CALC);
					}
				});
				
				builder.setView(layout);
				return builder.create();
			}
			case DIALOG_NATIVE_CALC: {
				progressDialog = new ProgressDialog(RouterKeygen.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(RouterKeygen.this.getResources().getString(R.string.dialog_nativecalc));
				progressDialog.setMessage(RouterKeygen.this.getResources().getString(R.string.dialog_nativecalc_msg));
				progressDialog.setCancelable(false);
				progressDialog.setProgress(0);
				progressDialog.setButton(RouterKeygen.this.getResources().getString(R.string.bt_manual_cancel),
						new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if ( RouterKeygen.this.calculator != null )
							RouterKeygen.this.calculator.stopRequested = true;
						removeDialog(DIALOG_THOMSON3G);
					}
				});
				progressDialog.setIndeterminate(false);
				return progressDialog;
			}
			case DIALOG_AUTO_CONNECT:
			{
				progressDialog = new ProgressDialog(RouterKeygen.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMessage("Connecting");
				progressDialog.setMax(list_key.size() + 1);
				progressDialog.setTitle(R.string.msg_dl_dlingdic);
				progressDialog.setCancelable(false);
				progressDialog.setButton(getString(R.string.bt_close), new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						removeDialog(DIALOG_AUTO_CONNECT);
					}
				});
				return progressDialog;
			}
		}
		return null;
	}


	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.wifi, menu);
		return true;
	}

	public void scan(){
		registerReceiver(scanFinished, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		if ( !wifi_state && !wifiOn )
		{
			Toast.makeText( RouterKeygen.this , 
					RouterKeygen.this.getResources().getString(R.string.msg_nowifi),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if ( wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLING )
		{
			registerReceiver(stateChanged, new IntentFilter(
					WifiManager.WIFI_STATE_CHANGED_ACTION));
			Toast.makeText( RouterKeygen.this ,
					RouterKeygen.this.getResources().getString(R.string.msg_wifienabling),
					Toast.LENGTH_SHORT).show();
		}
		else
			if ( wifi.startScan() )
				Toast.makeText( RouterKeygen.this ,
						RouterKeygen.this.getResources().getString(R.string.msg_scanstarted),
						Toast.LENGTH_SHORT).show();
			else
				Toast.makeText( RouterKeygen.this ,
						RouterKeygen.this.getResources().getString(R.string.msg_scanfailed),
						Toast.LENGTH_SHORT).show();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.wifi_scan:
			scan();
			return true;
		case R.id.manual_input:
			showDialog(DIALOG_MANUAL_CALC);
			return true;
		case R.id.pref:
			startActivity( new Intent(this , Preferences.class ));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void calcKeys(WifiNetwork wifi){
		if ( !wifi.supported )
		{
			Toast.makeText( RouterKeygen.this , 
					RouterKeygen.this.getResources().getString(R.string.msg_unspported),
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			switch( wifi.type )
			{
				case THOMSON: RouterKeygen.this.calculator = 
							new ThomsonKeygen(handler,getResources(), folderSelect , thomson3g);
							break;
				case DISCUS: RouterKeygen.this.calculator = 
							new DiscusKeygen(handler,getResources());
							break;
				case EIRCOM: RouterKeygen.this.calculator = 
							new EircomKeygen(handler,getResources());
							break;
				case DLINK: RouterKeygen.this.calculator = 
							new DlinkKeygen(handler,getResources());
							break;
				case VERIZON: RouterKeygen.this.calculator = 
							new VerizonKeygen(handler,getResources());
							break;
				case PIRELLI: RouterKeygen.this.calculator = 
							new PirelliKeygen(handler,getResources());
							break;
				case TELSEY: RouterKeygen.this.calculator = 
							new TelseyKeygen(handler,getResources());
							break;
				case ALICE:	 RouterKeygen.this.calculator = 
							new AliceKeygen(handler,getResources());
							break;
				case COMTREND_4X:	 RouterKeygen.this.calculator = 
							new Wlan4Keygen(handler,getResources());
							break;
				case HUAWEI: RouterKeygen.this.calculator = 
							new HuaweiKeygen(handler,getResources());
							break;
				case WLAN2:	 RouterKeygen.this.calculator = 
							new Wlan2Keygen(handler,getResources());
							break;
				case ONO_WEP: RouterKeygen.this.calculator = 
							new OnoKeygen(handler,getResources());
							break;
				case SKY_V1: RouterKeygen.this.calculator = 
							new SkyV1Keygen(handler,getResources());
							break;	
				case WLAN6: RouterKeygen.this.calculator = 
							new Wlan6Keygen(handler,getResources());
							break;			
			}
		}catch(LinkageError e){
			Toast.makeText( RouterKeygen.this ,
					RouterKeygen.this.getResources().getString(R.string.err_misbuilt_apk), 
					Toast.LENGTH_SHORT).show();
			return;
		}

		RouterKeygen.this.calculator.router = wifi;
		RouterKeygen.this.calculator.setPriority(Thread.MAX_PRIORITY);
		begin =  System.currentTimeMillis();//debugging
		RouterKeygen.this.calculator.start();
		removeDialog(DIALOG_KEY_LIST);
		removeDialog(DIALOG_MANUAL_CALC);
		if (  wifi.type == TYPE.THOMSON && thomson3g )
			showDialog(DIALOG_THOMSON3G);
		removeDialog(DIALOG_KEY_LIST);
	}

	boolean wifiOn;
	boolean thomson3g;
	boolean nativeCalc;
	boolean manualMac;
	String folderSelect;
	final String folderSelectPref = "folderSelect";
	final String wifiOnPref = "wifion";
	final String thomson3gPref = "thomson3g";
	final String nativeCalcPref = "nativethomson";
	final String manualMacPref = "manual_mac";

	private void getPrefs() {
		SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(getBaseContext());
		wifiOn = prefs.getBoolean(wifiOnPref , true);
		thomson3g = prefs.getBoolean(thomson3gPref, false);
		nativeCalc = prefs.getBoolean(nativeCalcPref, true);
		manualMac = prefs.getBoolean(manualMacPref, false);
		folderSelect = prefs.getString(folderSelectPref, 
				Environment.getExternalStorageDirectory().getAbsolutePath());
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if ( thomson3g)
				removeDialog(DIALOG_THOMSON3G);
			if ( nativeCalc )
				removeDialog(DIALOG_NATIVE_CALC);
			if ( msg.what == KeygenThread.RESULTS_READY ) /*Got Keys*/
			{
				begin = System.currentTimeMillis()-begin;
				list_key = RouterKeygen.this.calculator.getResults();
				Log.d(TAG, "Time to solve:" + begin);
				showDialog(DIALOG_KEY_LIST);
				return;
			}
			if ( msg.what == KeygenThread.ERROR_MSG ) 
			{
				if ( nativeCalc && ( calculator instanceof ThomsonKeygen ) )
				{
					if ( ((ThomsonKeygen)calculator).errorDict )
					{
						Toast.makeText( RouterKeygen.this , getString(R.string.msg_startingnativecalc) , 
								Toast.LENGTH_SHORT).show();
						
						WifiNetwork tmp = RouterKeygen.this.calculator.router;
						try{
							RouterKeygen.this.calculator = new NativeThomson(this ,RouterKeygen.this.getResources() );
						}catch(LinkageError e){
							Toast.makeText( RouterKeygen.this ,getString(R.string.err_misbuilt_apk), 
									Toast.LENGTH_SHORT).show();
							return;
						}
						RouterKeygen.this.calculator.router = tmp;
						RouterKeygen.this.calculator.setPriority(Thread.MAX_PRIORITY);
						RouterKeygen.this.calculator.start();
						showDialog(DIALOG_NATIVE_CALC);
						return;
					}

				}
				Toast.makeText( RouterKeygen.this , msg.obj.toString() , Toast.LENGTH_SHORT).show();
				return;
			}
			if ( msg.what == 2 )
			{
				progressDialog.setProgress(progressDialog.getProgress() +1);
				return;
			}
			if ( msg.what == 3 )
			{
				removeDialog(DIALOG_AUTO_CONNECT);
				Toast.makeText( RouterKeygen.this ,msg.obj.toString() , Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
	};

}