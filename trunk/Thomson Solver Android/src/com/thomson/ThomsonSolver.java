package com.thomson;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	ProgressDialog progressDialog;
	TextView tv;
	ListView lv1;
	String lv_arr[]={"Android","iPhone","BlackBerry","AndroidPeople"};
	ThomsonCalc calculator;
	String [] list = null;

	Handler handler = new Handler() {
          public void handleMessage(Message msg) {
        	  
			if ( msg.what == 0 )
			{
				lv1.setAdapter(new ArrayAdapter<String>(ThomsonSolver.this, android.R.layout.simple_list_item_1,
							list));
				removeDialog(DIALOG1_KEY);
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
					//progressDialog = ProgressDialog.show(ThomsonSolver.this, "Working..", "Calculating Keys", true,
                      //      false);
					showDialog(DIALOG1_KEY);
				}
				catch(Exception e)
				{
					
					return;
				}
			}
		});
	}
 
    public void setList(String[] ret) {
    	this.list = ret;
    }  
    
    private static final int DIALOG1_KEY = 0; 
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG1_KEY: {
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


}