package com.airhockyproject.android;


import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockyproject.android.objects.Mallet;
import com.airhockyproject.android.objects.Table;
import com.airhockyproject.android.util.ColorShaderProgram;
import com.airhockyproject.android.util.MatrixHelper;
import com.airhockyproject.android.util.TextureHelper;
import com.airhockyproject.android.util.TextureShaderProgram;

public class MyGLRenderer implements Renderer{

	private final Context context;
	
	private float[]	projectionMatrix = new float[16];
	private float[] modelMatrix = new float[16];

	private Table table;
	private Mallet mallet;
	
	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;
	
	private int texture;

	public MyGLRenderer(Context context){
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		//Draw the table
		textureProgram.useProgram();
		textureProgram.setUniforms(projectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();
		
		//Draw the mallets
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		mallet.bindData(colorProgram);
		mallet.draw();

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);	

		MatrixHelper.perspectiveM(projectionMatrix, 45, 
				(float) width / (float) height, 1f, 10f);

		//create model matrix that translate our table into view range(-1, -10)
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, 0f, 0f, -2.7f);
		rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

		final float[] temp = new float[16];
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
	}

	/**This happens the first time the app is run, and it may also be called
	 * when the device wakes up or when the user switches back to our activity.*/
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Some good-looking colors
		//glClearColor(1.0f,0.91f,0.38f,1.0f);//Mustard Addiction
		//glClearColor(0.769f,0.063f,0.173f,1.0f);//Plascon Fuscia Fizz
		//glClearColor(0.690f,0.902f,0.796f,1.0f);//Taubmans Sherbet Fizz
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		table = new Table();
		mallet = new Mallet();
		
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
	}

}
