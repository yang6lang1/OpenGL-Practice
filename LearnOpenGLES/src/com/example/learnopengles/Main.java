package com.example.learnopengles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.learnopengles.LearnOpenGLESCH1.LearnOpenGLESCH1Activity;
import com.example.learnopengles.tut02.FirstTriangleActivity;

public class Main extends Activity {

	//private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
					|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
					&& (Build.FINGERPRINT.startsWith("generic")
							|| Build.FINGERPRINT.startsWith("unknown")
							|| Build.MODEL.contains("google_sdk")
							|| Build.MODEL.contains("Emulator")
							|| Build.MODEL.contains("Android SDK built for x86")));
		
		//glSurfaceView = new GLSurfaceView(this);
		
		//final MainRenderer renderer = new MainRenderer(this);
		
		if(supportsEs2){
			//Toast.makeText(this, "This device does support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
			//glSurfaceView.setEGLContextClientVersion(2);
			//glSurfaceView.setRenderer(renderer);
			rendererSet = true;
		}
		else{
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
			return;
		} 
		
		setContentView(R.layout.main); 
		//setContentView(glSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void button1click(View view){
		/*An intent is an object that provides runtime binding between seperate components
		 *(such as two activities)*/
		Intent intent = new Intent(this, LearnOpenGLESCH1Activity.class);
		startActivity(intent);
	}
	
	public void tut02ButtonClick(View view){
		Intent intent = new Intent(this, FirstTriangleActivity.class);
		startActivity(intent);
	}
}
