package com.example.learnopengles.tut02;

import static android.opengl.GLES20.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.widget.Toast;

public class FirstTriangleRenderer implements Renderer{

	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	private static final int POSITION_COMPONENT_COUNT = 3;
	
	private final FloatBuffer vertexData;

	private Context context;

	private FirstTriangleProgram triangleProgran;
	
	public FirstTriangleRenderer(Context context){
		this.context = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		triangleProgram.useProgram();
		triangleProgram.setColor(1.0f, 0.0f, 0.0f);
		triangle.bindData(triangleProgram);
		triangle.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0,0,width,height);
	}
 
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 1f);		
		//Toast.makeText(context, "This device does support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
		triangle = new FirstTriangle();
		triangleProgram = new FirstTriangleProgram(context);
	}

}
