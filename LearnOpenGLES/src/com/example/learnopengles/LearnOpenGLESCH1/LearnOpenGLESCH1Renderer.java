package com.example.learnopengles.LearnOpenGLESCH1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import static android.opengl.GLES20.*;

import android.content.Context;

import android.opengl.GLSurfaceView.Renderer;

public class LearnOpenGLESCH1Renderer implements Renderer{

	private final Context context;
	
	public LearnOpenGLESCH1Renderer(Context context){
		this.context = context;
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0,0,width,height);
	}
 
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 1.0f, 1f);		
	}

}
