package com.ruiyuan.airHockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class AirHockeyActivity extends ActionBarActivity {
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	final AirHockeyRender airHockeyRender = new AirHockeyRender(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		final ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT
						.startsWith("generic")
						|| Build.FINGERPRINT.startsWith("unknown")
						|| Build.MODEL.contains("google_sdk")
						|| Build.MODEL.contains("Emulator") || Build.MODEL
							.contains("Android SDK built for x86")));
		if (supportsEs2) {

			glSurfaceView.setEGLContextClientVersion(2);
			glSurfaceView.setRenderer(airHockeyRender);
			rendererSet = true;

		} else {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0",
					Toast.LENGTH_LONG).show();
			return;

		}
		glSurfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event!=null){
					final float normalizedX = 
							(event.getX()/(float)v.getWidth())*2-1;
					final float normalizedY = 
							-((event.getY()/(float)v.getHeight())*2-1);
					
					if(event.getAction()==MotionEvent.ACTION_DOWN){
						glSurfaceView.queueEvent(new Runnable() {
							
							@Override
							public void run() {
								airHockeyRender.handleRouchPress(normalizedX,normalizedY);
								
							}
						});
					}else if(event.getAction()==MotionEvent.ACTION_MOVE){
						glSurfaceView.queueEvent(new Runnable() {
							
							@Override
							public void run() {
								airHockeyRender.handleTouchDrag(normalizedX,normalizedY);
							}
						});
					}
					return true;
				}else{
					return false;
				}
				
				
			}
		});
		
		
		setContentView(glSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_open_gl_project, menu);
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (rendererSet) {
			glSurfaceView.onPause();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (rendererSet) {
			glSurfaceView.onResume();
		}
	}

}
