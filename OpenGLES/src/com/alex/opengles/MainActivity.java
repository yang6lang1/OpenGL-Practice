package com.alex.opengles;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private GLSurfaceView myGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myGLView = new MyGLSurfaceView(this);

		setContentView(myGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume()
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
		myGLView.onResume();
	}

	@Override
	protected void onPause()
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
		myGLView.onPause();
	}
}
