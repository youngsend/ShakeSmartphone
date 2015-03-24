package com.example.shakesmartphone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public abstract class MyLogger extends Thread {
	public String logFileName;
	public int logInterval; // ms
	public boolean stopFlag;
	public String log="";
	public static final String TAG="MyLogger";
	public static final int FLUSH_COUNT = 1000;
	public MyLogger(String logFileName, int interval){
		super();
		this.logFileName = logFileName;
		this.logInterval = interval;
		this.stopFlag =true;
		String a[] = this.logFileName.split("/");
		int length = a.length;
		String filePath = "";
		if(length>=3){
			for(int i=2;i<length-2;i++)
			{
				filePath += (a[i] + "/");
			}
			filePath += a[length-2];
		}
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			File sdcardDir = Environment.getExternalStorageDirectory();
			String path = sdcardDir.getPath();
			filePath = path + "/" +filePath;
			File logtxt = new File(filePath);
			if(!logtxt.exists())
			{
				logtxt.mkdirs();
				Log.v("asasdasdasda", filePath);
			}
		}
	}
	public void startLog()
	{
		this.start();
	}
	public void stopLog()
	{
		this.stopFlag = false;
	}
	
	public abstract String getLogValue();
	
	public void run() {
		try {
			this.stopFlag = true;
		    BufferedWriter out = new BufferedWriter(new FileWriter(this.logFileName));
		    SimpleDateFormat bartDateFormat =  
			new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");  
		    int count = 0;
			while(stopFlag){
				try {
					if(count++ == FLUSH_COUNT)
					{
						out.write(log);
						out.flush();
						log = "";
						count = 0;
					}
					Thread.sleep(this.logInterval);
					long timeInMillis = System.currentTimeMillis();
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(timeInMillis);
					Date date = cal.getTime();
					log+=bartDateFormat.format(date)+"\t"+ this.getLogValue() + "\r\n";
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			if(!stopFlag)
			{
				out.write(log);
				out.flush();
				out.close();
			}
		} catch (IOException e) {//open file failed!
			e.printStackTrace();
		}

	}

}