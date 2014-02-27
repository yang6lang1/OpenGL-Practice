package com.particles.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class ParticlesActivity extends Activity {

	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*This code tests if the current device is an emulator build, and if it is,
		 * we assume that it supports OpenGL ES 2.0.*/
		ActivityManager activityManager =
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 
				&& (Build.FINGERPRINT.startsWith("generic")
						|| Build.FINGERPRINT.startsWith("unknown")
						|| Build.MODEL.contains("google_sdk")
						|| Build.MODEL.contains("Emulator")
						|| Build.MODEL.contains("Android SDK built for x86")));

		glSurfaceView = new GLSurfaceView(this);

		final MyGLRenderer airHockyRenderer = new MyGLRenderer(this);
		
		if (supportsEs2) {
			//An activity is an Android Context, so we pass in a reference to this
			glSurfaceView.setEGLContextClientVersion(2);
			glSurfaceView.setRenderer(airHockyRenderer);
			rendererSet = true; 
		} 
		else {
			//This shows a message on the screen
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
			return; 
		}
		
		setContentView(glSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onPause(){
		super.onPause();
		if(rendererSet){
			glSurfaceView.onPause();
		}
	}
	
	public void onResume(){
		super.onResume();
		
		if(rendererSet){
			glSurfaceView.onResume();
		}
	}
}
