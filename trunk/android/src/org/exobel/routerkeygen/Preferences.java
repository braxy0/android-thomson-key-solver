
package org.exobel.routerkeygen;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Preferences extends PreferenceActivity {

	ProgressDialog pbarDialog;
	Thread myRunnableThread;
	int myProgress = 0, fileLen;
	int byteRead;
	long lastt, now = 0;

	private static final String PUB_DONATE = 
		"market://details?id=org.exobel.routerkeygen.donate";
	private static final String PUB_DOWNLOAD = 
		"http://android-thomson-key-solver.googlecode.com/files/RouterKeygen.dic";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		findPreference("download").setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference)
					{
						pbarDialog = new ProgressDialog( Preferences.this);

						pbarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pbarDialog.setMessage("Estimating download speed and time left...");

						pbarDialog.setMax(100);
						pbarDialog.setTitle("Downloading dictionary");
						pbarDialog.setCancelable(true);
						pbarDialog.setOnDismissListener(new OnDismissListener() {
							public void onDismiss(DialogInterface dialog) {
								myRunnableThread.stop();
							}
						});
						pbarDialog.setButton("Cancel", new OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								myRunnableThread.stop();
								pbarDialog.dismiss();
							}
						});

						myProgress = 0;
						byteRead = 0;
						myRunnableThread = new Thread(myThread);
						myRunnableThread.start();
						return true;
					}
				});
		findPreference("donate").setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(PUB_DONATE));
						try {
							Preferences.this.startActivity(intent);
						} catch (ActivityNotFoundException anfe) {}
						return true;
					}
				});
		findPreference("about").setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						showDialog(DIALOG_ABOUT);
						return true;
					}
				});
		findPreference("folderSelect").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mPath = new File(Environment.getExternalStorageDirectory() + File.separator);
				mChosenFile = File.separator;
				directoryTree.clear();
				showDialog(DIALOG_LOAD_FOLDER);
				return true;
			}
		});
	}

	Handler messHand = new Handler() {

		public void handleMessage(Message msg) {
			
			switch(msg.what)
			{
			case 0:
				new AlertDialog.Builder(Preferences.this).setTitle(R.string.msg_error)
					.setMessage(R.string.msg_nosdcard).show();
				break;
			case 1:
				new AlertDialog.Builder(Preferences.this).setTitle(R.string.msg_error)
					.setMessage(R.string.msg_nomemoryonsdcard).show();
				break;
			case 2:
				pbarDialog.show();
				break;
			case 3:
				pbarDialog.dismiss();
				break;
			case 4:
				now = System.currentTimeMillis();
				if(now - lastt < 4000 )
					break;
				
				long kbs = (myProgress / (now - lastt))*1000/1024;
				if(kbs == 0)
					break;
				
				pbarDialog.setMessage("Download speed: "
						+ kbs + "kb/s"
						+ "\nEstimaded Time Left: "
						+ (fileLen - myProgress) / kbs / 1024 + "s");
				break;
			}
		}
	};
	
	private Runnable myThread = new Runnable() {
		public void run() {
			URL url;
			URLConnection con;
			DataInputStream dis;
			FileOutputStream fos;
			byte[] buf;
			try {
				url = new URL(PUB_DOWNLOAD);
				fos = new FileOutputStream(
						new File(
							Environment.getExternalStorageDirectory().getPath() + File.separator + "DicTemp.dic"));
				con = url.openConnection();
				dis = new DataInputStream(con.getInputStream());
				fileLen = con.getContentLength();
				
				// Checking if external storage has enough memory ...
				android.os.StatFs stat = new android.os.StatFs(Environment.getExternalStorageDirectory().getPath());
				if((long)stat.getBlockSize() * (long)stat.getAvailableBlocks() < fileLen)
					messHand.sendEmptyMessage(1);

				lastt = System.currentTimeMillis();
				buf = new byte[65536];
				messHand.sendEmptyMessage(2);
				while (myProgress < fileLen) {
					try{

						if ((byteRead = dis.read(buf)) != -1)
						{
							fos.write(buf, 0, byteRead);
							myProgress += byteRead;
						}
						else
						{
							dis.close();
							fos.close();
							myProgress = fileLen;
						}
					}
					catch(Exception e)
					{
					}
					messHand.sendEmptyMessage(4);
					pbarDialog.setProgress(100 * myProgress / fileLen);
					Thread.sleep(10);
				}
			Thread.sleep(10);
			messHand.sendEmptyMessage(3);
			}
			catch (FileNotFoundException e)
			{
				messHand.sendEmptyMessage(0);
			}
			catch(Exception e)
			{
			}
		}
	};
	
	private static final String TAG = "ThomsonPreferences";
	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory() + File.separator);
	private String mChosenFile = File.separator;
	Stack<String> directoryTree = new Stack<String>();
	private static final int DIALOG_LOAD_FOLDER = 1000;
	private static final int DIALOG_ABOUT = 1001;

	private void loadFolderList() {
		mPath = new File(Environment.getExternalStorageDirectory() + File.separator + mChosenFile);
		if(mPath.exists()){
			FilenameFilter filter = new FilenameFilter(){
				public boolean accept(File dir, String filename){
					File sel = new File(dir, filename);
					return sel.isDirectory();
				}
			};
			mFileList = mPath.list(filter);
		}
		else{ 
			if ( !directoryTree.empty() )
			{
				mChosenFile = directoryTree.pop();
				loadFolderList();
			}
			else
				mFileList = null;
		}
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);
		switch(id) {
		case DIALOG_LOAD_FOLDER:
		{
			loadFolderList();
			builder.setTitle("Choose your folder");
			if(mFileList == null || mFileList.length == 0) {
				Log.e(TAG, "Showing file picker before loading the file list");
				mFileList = new String[]{"(There are no more directories)"};
				builder.setItems(mFileList, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int which) {}}
				);
			}
			else
				builder.setItems(mFileList, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						directoryTree.push(mChosenFile);
						mChosenFile += File.separator + mFileList[which];
						removeDialog(DIALOG_LOAD_FOLDER);
						showDialog(DIALOG_LOAD_FOLDER);
					}
				});
			if ( !mChosenFile.equals(File.separator))
				builder.setNegativeButton(R.string.bt_choose_back,new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if ( !directoryTree.empty())
							mChosenFile = directoryTree.pop();
						else
							mChosenFile = File.separator;
						removeDialog(DIALOG_LOAD_FOLDER);
						showDialog(DIALOG_LOAD_FOLDER);
					}
				});
			builder.setNeutralButton(R.string.bt_choose,new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences customSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					SharedPreferences.Editor editor = customSharedPreference
					.edit();

					editor.putString("folderSelect",mPath.toString());
					editor.commit();
					String path = mPath.toString();
					mPath = new File(path +  File.separator + "RouterKeygen.dic");
					if ( !mPath.exists() )
						Toast.makeText(getBaseContext(),getResources().getString(R.string.pref_msg_notfound) + " " + path,
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getBaseContext(),mPath.toString() +  " " + getResources().getString(R.string.pref_msg_found),
								Toast.LENGTH_SHORT).show();
				}
			});

			break;
		}
		case DIALOG_ABOUT:
		{
			builder.setTitle(R.string.pref_about); 
			builder.setMessage(R.string.pref_about_desc);
			builder.setNeutralButton(R.string.bt_close, new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(DIALOG_ABOUT);

				}
			});
			break;
		}
		}
		dialog = builder.show();
		return dialog;
	}
};


