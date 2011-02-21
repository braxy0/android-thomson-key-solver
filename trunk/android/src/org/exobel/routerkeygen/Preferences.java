
package org.exobel.routerkeygen;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
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
import android.widget.Toast;

public class Preferences extends PreferenceActivity {
	
	// The maximum supported dictionary version
	public static final int MAX_DIC_VERSION = 3;

	ProgressDialog pbarDialog;
	Downloader downloader;
	int myProgress = 0, fileLen;
	long lastt, now = 0, downloadBegin = 0;
	
	byte[] dicVersion = new byte [2];
	static byte[] cfvTable = new byte[18];
	
	
	private static final String PUB_DONATE = 
		"market://details?id=org.exobel.routerkeygen.donate";
	private static final String PUB_DOWNLOAD = 
		"http://dl.dropbox.com/u/7566036/RouterKeygen.dic";
	//"http://android-thomson-key-solver.googlecode.com/files/RouterKeygen.dic";
	private static final String PUB_DIC_CFV =
		"http://dl.dropbox.com/u/7566036/RouterKeygen.cfv";
	
	private static final String folderSelectPref = "folderSelect";

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		findPreference("download").setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference)
					{
						new AlertDialog.Builder(Preferences.this)
							.setTitle(R.string.pref_download)
							.setMessage(R.string.msg_dicislarge)
							.setCancelable(false)
							.setPositiveButton(R.string.bt_yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// Check if we have the latest dictionary version.

									final File myDicFile = new File(PreferenceManager.getDefaultSharedPreferences(getBaseContext())
										.getString(folderSelectPref,
											Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "thomson")
											+ File.separator + "RouterKeygen.dic");
									
									if(myDicFile.exists());
									{
										pbarDialog = new ProgressDialog(Preferences.this);
										pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
										pbarDialog.setMessage(getString(R.string.msg_wait));
										pbarDialog.show();
										  new Thread(new Runnable() {
											    public void run() {

													// TODO: Check if this dic is not corrupt.
													
													// Comparing this version with the online version
													try {
														InputStream is = new FileInputStream(myDicFile);
														URLConnection con = new URL(PUB_DIC_CFV).openConnection();
														DataInputStream dis = new DataInputStream(con.getInputStream());
														if(con.getContentLength() != 18)
															throw new Exception();
														
														dis.read(Preferences.cfvTable);
														
														// Check our version
														is.read(dicVersion);
														
														int thisVersion, onlineVersion;
														thisVersion = dicVersion[0] << 8 | dicVersion[1];
														onlineVersion = cfvTable[0] << 8 | cfvTable[1];
														
														if(thisVersion >= onlineVersion)
														{
															// All is well
															messHand.sendEmptyMessage(6);
															return;
														}
														if(onlineVersion > thisVersion && onlineVersion > MAX_DIC_VERSION)
														{
															// Online version is too advanced
															messHand.sendEmptyMessage(5);
															return;
														}
														messHand.sendEmptyMessage(7);
														
													} 
													catch ( FileNotFoundException e ){
														messHand.sendEmptyMessage(7);
													}
													catch (Exception e)
													{
														messHand.sendEmptyMessage(-1);
														return;
													}
												}
											  }).start();

									}									
					           }
					       })
					       .setNegativeButton(R.string.bt_no, new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					           }
					       })
					       .create()
					       .show();
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
	
	private void checkDownload(){
		pbarDialog = new ProgressDialog(Preferences.this);
		pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pbarDialog.setMessage(getString(R.string.msg_wait));
		pbarDialog.show();
		new Thread(new Runnable() {
		    public void run() {
				try
				{
					String folderSelect = PreferenceManager
										.getDefaultSharedPreferences(getBaseContext()).getString(folderSelectPref, 
												Environment.getExternalStorageDirectory().getAbsolutePath() +
												File.separator + "thomson");
					MessageDigest md = MessageDigest.getInstance("MD5");
					InputStream is = new FileInputStream(Environment.getExternalStorageDirectory().getPath()
														+ File.separator + "DicTemp.dic");
					try {
						is = new DigestInputStream(is, md);
						byte []  buffer = new byte [16384] ; 
						while ( is.read ( buffer )  != -1 );
					}
					finally {
						is.close();
					}
					byte[] digest = md.digest();

					downloadHash = StringUtils.getHexString(digest);

					try {
						URLConnection con = new URL(PUB_DIC_CFV).openConnection();
						DataInputStream dis = new DataInputStream(con.getInputStream());
						if(con.getContentLength() != 18)
							throw new Exception();
						
						dis.read(Preferences.cfvTable);

						for(int i = 0; i < 16; ++i)
						{
							if(digest[i] != cfvTable[i + 2])
							{
								new File(
										Environment.getExternalStorageDirectory().getPath() + File.separator + "DicTemp.dic"
								).delete();
								messHand.sendEmptyMessage(-1);
								return;
							}
						}
						
						if (!renameFile(Environment.getExternalStorageDirectory().getPath() + File.separator + "DicTemp.dic" ,
								folderSelect + File.separator + "RouterKeygen.dic" , true ))
						{
							messHand.sendEmptyMessage(8);
							return;
						}
						messHand.sendEmptyMessage(9);
					}
					catch (Exception e)
					{
						messHand.sendEmptyMessage(-1);
						return;
					}

		 	   }
		 	   catch(Exception e){}
		 	   }
		  }).start();
	}
	
	// Download the dictionary
	private void startDownload(){
		pbarDialog = new ProgressDialog(Preferences.this);
		pbarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pbarDialog.setMessage(getString(R.string.msg_dl_estimating));
		pbarDialog.setMax(100);
		pbarDialog.setTitle(R.string.msg_dl_dlingdic);
		pbarDialog.setCancelable(false);
		pbarDialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				downloader.stopRequested = true;
			}
		});
		pbarDialog.setButton(getString(R.string.bt_pause), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downloader.stopRequested = true;
				pbarDialog.dismiss();
			}
		});
		pbarDialog.setButton2(getString(R.string.bt_manual_cancel), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downloader.deleteTemp = true;
				downloader.stopRequested = true;
				pbarDialog.dismiss();
			}
		});
		pbarDialog.show();
		myProgress = 0;
		downloader = new Downloader(messHand , PUB_DOWNLOAD);
		downloader.start();
		lastt = downloadBegin = System.currentTimeMillis();
	}
	String downloadHash;
	boolean notDownloadDic = false;
	Handler messHand = new Handler() {

		public void handleMessage(Message msg) {
			
			switch(msg.what)
			{
			case -1:
				pbarDialog.dismiss();
				new AlertDialog.Builder(Preferences.this).setTitle(R.string.msg_error)
					.setMessage(R.string.msg_err_unkown).show();
			break;
			case 0:
				pbarDialog.dismiss();
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
				checkDownload();
				break;
			case 4:
				now = System.currentTimeMillis();
				if(now - lastt < 1000 )
					break;
				
				myProgress = msg.arg1;
				fileLen = msg.arg2;
				if ( fileLen == 0 )
					break;
				long kbs =  ((myProgress / (now - downloadBegin))*1000/1024);
				if(kbs == 0)
					break;
				
				double progress =  100 * ( (double)myProgress/ fileLen );
				pbarDialog.setProgress((int) progress);
				pbarDialog.setMessage(getString(R.string.msg_dl_speed) + ": "
						+ kbs + "kb/s\n"
						+ getString(R.string.msg_dl_eta) + ": "
						+ (fileLen - myProgress) / kbs / 1024 + "s");
				lastt = now;
				break;
			case 5:
				pbarDialog.dismiss();
				new AlertDialog.Builder(Preferences.this).setTitle(R.string.msg_error)
					.setMessage(R.string.msg_err_online_too_adv).show();
				break;
			case 6:
				pbarDialog.dismiss();
				Toast.makeText(getBaseContext(),getResources().getString(R.string.msg_dic_updated),
						Toast.LENGTH_SHORT).show();
				break;
			case 7: 
				pbarDialog.dismiss();
				startDownload();
				break;
			case 8: 
				pbarDialog.dismiss();
				Toast.makeText(getBaseContext(),getResources().getString(R.string.pref_msg_err_rename_dic),
						Toast.LENGTH_SHORT).show();
				break;
			case 9: 
				pbarDialog.dismiss();
				Toast.makeText(Preferences.this, R.string.msg_dic_updated_finished, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	 private boolean renameFile(String file, String toFile , boolean saveOld) {

	        File toBeRenamed = new File(file);
	        File newFile = new File(toFile);

	        if (!toBeRenamed.exists() || toBeRenamed.isDirectory())
	            return false;
	        

	        if (newFile.exists() && !newFile.isDirectory() && saveOld) {
	        	if ( !renameFile(toFile,toFile+"_backup" , false) )
	        		Toast.makeText(getBaseContext(),getResources().getString(R.string.pref_msg_err_backup_dic),
						Toast.LENGTH_SHORT).show();
	        	else
	        		toFile +="_backup";
	        }
	        newFile = new File(toFile);

	        //Rename
	        if (!toBeRenamed.renameTo(newFile) )
	           return false;
	       

	        return true;
	    }
	
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

					editor.putString(folderSelectPref,mPath.toString());
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


