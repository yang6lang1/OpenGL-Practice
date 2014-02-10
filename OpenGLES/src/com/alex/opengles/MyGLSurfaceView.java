package com.alex.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView{

	private final float TOUCH_SCALE_FACTOR = 180.0f/320;
	private float mPreviousX;
	private float mPreviousY;
	
	private MyGLRenderer mRenderer;
	
	public MyGLSurfaceView(Context context) {
		super(context);
		
		setEGLContextClientVersion(2);
		
		mRenderer = new MyGLRenderer();
		setRenderer(mRenderer);
		
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		mPreviousX = mPreviousY = 0.0f;
	}

	public boolean onTouchEvent(MotionEvent e){
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()){
		case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
			
			if(y > getHeight() / 2){
				dx *= -1;
			}
			
			if(x < getWidth() / 2){
				dy *= -1;
			}
			
			mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
			this.requestRender();
		}
		
    mPreviousX = x;
    mPreviousY = y;
    return true;
	}
}
