package com.example.shakesmartphone;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.shakesmartphone.IRemoteService;
import com.example.shakesmartphone.IRemoteService.Stub;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;


public class LogService extends Service {
	int interval=1;
	boolean started = false;
	boolean meichecked=false;
	MeiLogger meil;
	PowerManager.WakeLock wlock;
	NotificationManager mNM;
	int gesture;
	int shake;
	int handle;
	String username;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wlock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MeiLogger");
		wlock.acquire();
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (meichecked)
			meil.stopLog();
		wlock.release();
		destroyNotification();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
	@Override
	public void onStart(Intent intent, int startId) {
		if(intent != null && !this.started){
			interval = intent.getIntExtra("Interval", 1000);
			meichecked = intent.getBooleanExtra("Mei", false);
			gesture = intent.getIntExtra("Gesture", 0);
			shake = intent.getIntExtra("Shake", 0);
			handle = intent.getIntExtra("Handle", 0);
			username = intent.getStringExtra("Username");
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String curDateStr = formatter.format(curDate);
			String filename = Integer.toString(gesture)+"_"+Integer.toString(shake)+"_"+Integer.toString(handle);
			if(meichecked) {
				meil = new MeiLogger("/sdcard/Log/Mei/"+username+"/"+filename+".txt", interval, (SensorManager)this
						.getSystemService(SENSOR_SERVICE));
				meil.start();
			}
		}
		super.onStart(intent, startId);
	}
	@SuppressWarnings("deprecation")
	private void showNotification() {
        Notification notification = new Notification(R.drawable.strawberry, "MeiLogger is Logging...",
                System.currentTimeMillis());
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
        		getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "MeiLogger",
        		"Logging...", pendingIntent);
        mNM.notify("NOTIFICATION",0, notification);
    }
    private void destroyNotification() {
        mNM.cancel("NOTIFICATION", 0);
    }
    
    private final IRemoteService.Stub mBinder=new Stub() {
		public String request(String message) throws RemoteException {
			return "Hello "+message;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}


}
