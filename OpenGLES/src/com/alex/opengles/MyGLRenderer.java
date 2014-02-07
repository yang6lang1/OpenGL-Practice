package com.alex.opengles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.alex.shapes.Square;
import com.alex.shapes.Triangle;

public class MyGLRenderer implements GLSurfaceView.Renderer{

	private Triangle triangle;
	private Square square;
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		square.draw();
		triangle.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(width > height){
			GLES20.glViewport((width - height)/2, 0, height, height);
		}else{
			GLES20.glViewport(0, (height-width)/2, width, width);	
		}
		
//		GLES20.glViewport(0,0,width,height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		triangle = new Triangle();
	  square = new Square();
	}

	public static int loadShader(int type, String shaderCode){

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}
}
