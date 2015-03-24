package com.example.shakesmartphone;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	Button buttonstart;
	Button buttonnext;
	TextView showshakemethod;
	String gesture = "";
	String shakeHandleWay = "";
	//boolean next = false;
	int i,j;
	String[] gestures;
	String[] shakeHandleWays;
	String fileName;
	SensorEventListener mySensorListener;
	public static final int FLUSH_COUNT = 1000;
	int interval = 1;
	SensorManager mySensorManager;
	boolean meichecked = false;
	EditText editText;
	MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showshakemethod = new TextView(this);
		Resources res = getResources();
		gestures = res.getStringArray(R.array.gestures);
		shakeHandleWays = res.getStringArray(R.array.shake_handle_way);
		// remember use fragment_main rather than activity_main
		setContentView(R.layout.fragment_main);
		i = 0;
		j = 0;
		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.beep_sound);
		
		View.OnClickListener mylistener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.button_start: {
					if(buttonstart.getText() == "STOP") {
						buttonstart.setText("START");
						buttonstart.setTextColor(android.graphics.Color.BLACK);
						mediaPlayer.start();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mediaPlayer.start();
						//stop logging
						Intent intent=new Intent();
						intent.setClass(MainActivity.this, LogService.class);
						stopService(intent);
					}
					else {//buttonstart.getText() == "START"
						buttonstart.setText("STOP");
						buttonstart.setTextColor(android.graphics.Color.RED);
						mediaPlayer.start();
						//start logging acceleration and gyroscope
						editText = (EditText)findViewById(R.id.user_name);
						String username = editText.getText().toString();
						
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, LogService.class);
						meichecked = true;
						intent.putExtra("Mei", meichecked);
						intent.putExtra("Gesture", i);
						intent.putExtra("Shake", j/4);
						intent.putExtra("Handle", j%4);
						intent.putExtra("Username", username);
						startService(intent);
						//mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
					}
					break;
				}
				case R.id.button_next: {
					if(i == 2 && j == 15) { //every body gesture has four handling fashions and four shaking fashions.
						showshakemethod.setText("Successfully Complete This Time!");
						i = 0;
						j = 0;
					}
					else if (j == 15) {
						j = 0;
						i++;
						gesture = gestures[i];
						shakeHandleWay = shakeHandleWays[j];
						showshakemethod = (TextView)findViewById(R.id.show_shake_method);
						showshakemethod.setText(gesture.concat(shakeHandleWay));
					}
					else {
						j++;
						gesture = gestures[i];
						shakeHandleWay = shakeHandleWays[j];
						showshakemethod = (TextView)findViewById(R.id.show_shake_method);
						showshakemethod.setText(gesture.concat(shakeHandleWay));
					}
					break;
				}
				default:
					break;
				}
			}
		};
		gesture = gestures[0];
		shakeHandleWay = shakeHandleWays[0];
		showshakemethod = (TextView)findViewById(R.id.show_shake_method);
		showshakemethod.setText(gesture.concat(shakeHandleWay));
		buttonstart = (Button)findViewById(R.id.button_start);
		buttonnext = (Button)findViewById(R.id.button_next);
		buttonstart.setOnClickListener(mylistener);
		buttonnext.setOnClickListener(mylistener);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
