package com.airhockyproject.android;


import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockyproject.android.util.LoggerConfig;
import com.airhockyproject.android.util.MatrixHelper;
import com.airhockyproject.android.util.ShaderHelper;
import com.airhockyproject.android.util.TextResourceReader;

public class MyGLRenderer implements Renderer{

	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int COLOR_COMPONENT_COUNT = 3;
	private static final int BYTES_PER_FLOAT = 4; 
	private static final int STRIDE = 
			(POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	private static final String A_COLOR = "a_Color";
	private static final String A_POSITION = "a_Position";
	private static final String U_MATRIX = "u_Matrix";
	//FloatBuffer will be used to store data in native memory(no automatic garbage collection)
	private final FloatBuffer vertexData;
	private float[]	projectionMatrix = new float[16];
	private float[] modelMatrix = new float[16];

	private final Context context;
	private int program;
	private int aPositionLocation;
	private int aColorLocation;
	private int uMatrixLocation;

	public MyGLRenderer(Context context){
		this.context = context;
		//vertex attribute array
		float[] tableVerticesWithTriangles = {
				//Order of coordinates: X, Y, R, G, B
				// Triangle Fan
				0f,    0f,   1f,   1f,   1f,
				-0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
				0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
				0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
				-0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
				-0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
				// Line 1
				-0.5f, 0f, 1f, 0f, 0f,
				0.5f, 0f, 1f, 0f, 0f,
				// Mallets
				0f, -0.4f, 0f, 0f, 1f,
				0f,  0.4f, 1f, 0f, 0f
		};

		vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT) 
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexData.put(tableVerticesWithTriangles);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

		glDrawArrays(GL_TRIANGLE_FAN, 0, 2 * 3);

		glDrawArrays(GL_LINES, 6, 2);

		glDrawArrays(GL_POINTS, 8, 1);

		glDrawArrays(GL_POINTS, 9, 1);
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

		String vertexShaderSource = TextResourceReader 
				.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader 
				.readTextFileFromResource(context, R.raw.simple_fragment_shader);

		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

		if(LoggerConfig.ON){
			ShaderHelper.validateProgram(program);
		}

		glUseProgram(program);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aColorLocation = glGetAttribLocation(program, A_COLOR);
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

		vertexData.position(0);
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, 
				false, STRIDE, vertexData);
		glEnableVertexAttribArray(aPositionLocation);

		vertexData.position(POSITION_COMPONENT_COUNT);
		glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, 
				false, STRIDE, vertexData);
		glEnableVertexAttribArray(aColorLocation);
	}

}
