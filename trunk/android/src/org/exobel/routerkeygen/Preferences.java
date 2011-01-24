
package org.exobel.routerkeygen;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Stack;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.widget.Toast;
 
public class Preferences extends PreferenceActivity {
	
	 private static final String PUB_DONATE = 
         "market://details?id=org.exobel.routerkeygen.donate";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.layout.preferences);
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
                                        	    mPath = new File(Environment.getExternalStorageDirectory() 
                                        	    				+ File.separator);
                                                mChosenFile = File.separator;
                                                directoryTree.clear();
                                                showDialog(DIALOG_LOAD_FOLDER);
                                                return true;
                                        }
 
                                });

        }
        private static final String TAG = "ThomsonPreferences";
        private String[] mFileList;
        private File mPath = new File(Environment.getExternalStorageDirectory() + File.separator);
        private String mChosenFile = File.separator;
        Stack<String> directoryTree = new Stack<String>();
        private static final int DIALOG_LOAD_FOLDER = 1000;
        private static final int DIALOG_ABOUT = 1001;

        private void loadFolderList(){
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
        
        protected Dialog onCreateDialog(int id){
        	Dialog dialog = null;
        	AlertDialog.Builder builder = new Builder(this);
        	switch(id){
        		case DIALOG_LOAD_FOLDER:
        				loadFolderList();
        				builder.setTitle("Choose your folder");
        				if(mFileList == null || mFileList.length == 0){
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
		                        Toast.makeText(getBaseContext(),mPath.toString(),
                                        Toast.LENGTH_SHORT).show();
									}
						});
			            
			            break;
        		case DIALOG_ABOUT:
        				builder.setTitle("About Router Keygen"); 
        				builder.setMessage(R.string.pref_about_desc);builder.setNeutralButton(R.string.bt_close, new OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
										removeDialog(DIALOG_ABOUT);
										
									}
						});
        				break;
        	}
        	dialog = builder.show();
        	return dialog;
         } 
}