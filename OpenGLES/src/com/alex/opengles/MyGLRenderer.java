package com.alex.opengles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.alex.shapes.AndroidLesson;
import com.alex.shapes.Cube;
import com.alex.shapes.Rectangle;
import com.alex.shapes.Square;
import com.alex.shapes.Triangle;

public class MyGLRenderer implements GLSurfaceView.Renderer{

	private static final String TAG = "MyGLRenderer";
	private Triangle triangle;
	private Square square;
	private Cube cube;
	private Rectangle rect;
	private AndroidLesson test;

	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	private volatile float mAngle = 0.0f;

	@Override
	public void onDrawFrame(GL10 gl) {
		float[] scratch = new float[16];

		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -10, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		/*
		// Draw square
		square.draw(mMVPMatrix);
		
		// Create a rotation transformation for the triangle
    Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

    // Combine the rotation matrix with the projection and camera view
    // Note that the mMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

		// Draw triangle
		triangle.draw(scratch);*/
		//cube.draw(mMVPMatrix);
		
		rect.draw(mProjectionMatrix);
	
//		Matrix.translateM(mMVPMatrix, 0, 1, 2, 0);
//		test.draw(mMVPMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		GLES20.glViewport(0,0,width,height);

		float ratio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 100);
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LESS);
		
		triangle = new Triangle();
		square = new Square();
		cube = new Cube();
		rect = new Rectangle();
		test = new AndroidLesson();
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

	public float getAngle(){
		return this.mAngle;
	}

	public void setAngle(final float angle){
		this.mAngle = angle;
	}
}
