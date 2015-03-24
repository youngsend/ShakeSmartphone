package com.example.shakesmartphone;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MeiLogger extends MyLogger implements SensorEventListener {
	
	SensorManager sensorManager;
	Sensor mAcceleration;
	Sensor mGyroscope;
	Sensor mOrientation;
	float[] acceleration = {0,0,0};
	float[] gyroscope ={0,0,0};
	float[] orientation ={0,0,0};
	String log;
	StringBuilder log_perf;
	BufferedWriter out;
	SimpleDateFormat bartDateFormat;
	
	int count = 0;
	
	
	public void run() {
		
		// TODO Auto-generated method stub
		try {
			this.stopFlag = true;
			while(stopFlag){
				try {
				
					Thread.sleep(1000);
					
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			if(!stopFlag)
			{
				sensorManager.unregisterListener(this);
				out.write(log_perf.toString());
				out.flush();
				out.close();
				
			}
		} catch (IOException e) {//open file failed!
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent sensorEvent) {
		switch (sensorEvent.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER: {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			Date date = cal.getTime();
			
			acceleration[0] = sensorEvent.values[0];
			acceleration[1] = sensorEvent.values[1];
			acceleration[2] = sensorEvent.values[2];
			
			log_perf.append(bartDateFormat.format(date))
			.append("\t" + acceleration[0] + " " + acceleration[1] + " " + acceleration[2] + " ")
			.append(gyroscope[0] + " " + gyroscope[1] + " " + gyroscope[2] + " ")
			.append(orientation[0] + " " + orientation[1] + " " + orientation[2] + "\r\n");
			
			if(count++ >= FLUSH_COUNT)
	        {
	        	count = 0;
	        	try {
					out.write(log_perf.toString());
					out.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Log.e("SensorLogger", e1.toString());
				}
	        	//log_perf = new StringBuilder();
	        	log_perf.delete(0, log_perf.length());
	        }
			break;
		}
		case Sensor.TYPE_GYROSCOPE: {
			gyroscope[0] = sensorEvent.values[0];
			gyroscope[1] = sensorEvent.values[1];
			gyroscope[2] = sensorEvent.values[2];
			break;
		}
		case Sensor.TYPE_ORIENTATION: {
			orientation[0] = sensorEvent.values[0];
			orientation[1] = sensorEvent.values[1];
			orientation[2] = sensorEvent.values[2];
			break;
		}
		}
	}
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public MeiLogger(String fileName, int interval, SensorManager s) {
		super(fileName, interval);
		log = "";
		log_perf = new StringBuilder();
		try {
			out = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    bartDateFormat =  new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");  
		sensorManager = s;
		mAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, mAcceleration, SensorManager
				.SENSOR_DELAY_FASTEST);
		mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		sensorManager.registerListener(this, mGyroscope, SensorManager
				.SENSOR_DELAY_FASTEST);
		mOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, mOrientation, SensorManager
				.SENSOR_DELAY_FASTEST);
	}

	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		return null;
	}
}

