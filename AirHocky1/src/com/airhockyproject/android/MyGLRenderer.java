package com.airhockyproject.android;


import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockyproject.android.util.LoggerConfig;
import com.airhockyproject.android.util.ShaderHelper;
import com.airhockyproject.android.util.TextResourceReader;

public class MyGLRenderer implements Renderer{

	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int BYTES_PER_FLOAT = 4; 
	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	//FloatBuffer will be used to store data in native memory(no automatic garbage collection)
	private final FloatBuffer vertexData;

	private final Context context;
	private int program;
	private int uColorLocation;
	private int aPositionLocation;

	public MyGLRenderer(Context context){
		//vertex attribute array
		float[] tableVerticesWithTriangles = {
				-0.5f, -0.5f,		// Triangle 1
				0.5f,  0.5f,
				-0.5f,  0.5f,

				-0.5f, -0.5f,		// Triangle 2
				0.5f, -0.5f,
				0.5f,  0.5f,

				-0.5f, 0f,			// Line 1
				0.5f, 0f,

				0f, -0.25f,			// Mallets
				0f, 0.25f
		};

		vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT) 
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexData.put(tableVerticesWithTriangles);

		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);
	
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
		glDrawArrays(GL_TRIANGLES, 0, 2 * 3);
		
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_LINES, 6, 2);

		glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
		glDrawArrays(GL_POINTS, 8, 1);

		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_POINTS, 9, 1);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0, 0, width, height);	
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

		uColorLocation = glGetUniformLocation(program, U_COLOR);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		vertexData.position(0);
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, 
				false, 0, vertexData);
		glEnableVertexAttribArray(aPositionLocation);
	}

}
