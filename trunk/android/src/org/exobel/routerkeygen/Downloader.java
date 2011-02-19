package org.exobel.routerkeygen;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class Downloader extends Thread{
	Handler messHand;
	String urlDownload;	
	boolean stopRequested;
	public void run() {
		URL url;
		URLConnection con;
		DataInputStream dis;
		FileOutputStream fos;
		int myProgress = 0, fileLen;
		int byteRead;
		byte[] buf;
		try {
			url = new URL(urlDownload);
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
				catch(Exception e){}
				messHand.sendMessage(Message.obtain(messHand, 4, myProgress, fileLen));
				Thread.sleep(10);
				if ( stopRequested )
					return;
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

	public Downloader(Handler messHand, String urlDownload) {
		this.messHand = messHand;
		this.urlDownload = urlDownload;
	}

}
