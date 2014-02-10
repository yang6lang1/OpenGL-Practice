package com.alex.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.alex.opengles.MyGLRenderer;

public class Rectangle {

	private final String vertexShaderCode = 
			"attribute vec3 vertexPosition_modelspace;" +
					"attribute vec3 vertexColor;" +
					"varying vec3 fragmentColor;" +
					"uniform mat4 MVP;" +
					"void main(){" +
					"  gl_Position = MVP * vec4(vertexPosition_modelspace, 1.0);" +
					"  fragmentColor = vertexColor;" +
					"}";

	private final String fragmentShaderCode =
			"varying vec3 fragmentColor;" +
					"void main() {" +
					"  gl_FragColor = vec4(fragmentColor,1);" +
					"}";

	private final int program;
	private int positionHandle;
	private int colorHandle;
	private int mvpHandle;

	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;

	private static final float[] g_vertex_buffer_data = {
		-1.0f,-1.0f,-1.0f,
		-1.0f,-1.0f, 1.0f,
		-1.0f, 1.0f, 1.0f,
		1.0f, 1.0f,-1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f, 1.0f,-1.0f,
		1.0f,-1.0f, 1.0f,
		-1.0f,-1.0f,-1.0f,
		1.0f,-1.0f,-1.0f,
		1.0f, 1.0f,-1.0f,
		1.0f,-1.0f,-1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f, 1.0f, 1.0f,
		-1.0f, 1.0f,-1.0f,
		1.0f,-1.0f, 1.0f,
		-1.0f,-1.0f, 1.0f,
		-1.0f,-1.0f,-1.0f,
		-1.0f, 1.0f, 1.0f,
		-1.0f,-1.0f, 1.0f,
		1.0f,-1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,
		1.0f,-1.0f,-1.0f,
		1.0f, 1.0f,-1.0f,
		1.0f,-1.0f,-1.0f,
		1.0f, 1.0f, 1.0f,
		1.0f,-1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,
		1.0f, 1.0f,-1.0f,
		-1.0f, 1.0f,-1.0f,
		1.0f, 1.0f, 1.0f,
		-1.0f, 1.0f,-1.0f,
		-1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f,
		-1.0f, 1.0f, 1.0f,
		1.0f,-1.0f, 1.0f
	};

	private static float[] g_color_buffer_data = new float[12 * 3 * 3];
	private Random generator = new Random(System.currentTimeMillis());

	public Rectangle(){
		for(int i = 0; i < 12 * 3; i++){
			g_color_buffer_data[3 * i + 0] = generator.nextFloat();
			g_color_buffer_data[3 * i + 1] = generator.nextFloat();
			g_color_buffer_data[3 * i + 2] = generator.nextFloat();
		}
		this.vertexBuffer = ByteBuffer.allocateDirect(g_vertex_buffer_data.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		this.vertexBuffer.put(g_vertex_buffer_data).position(0); 

		this.colorBuffer = ByteBuffer.allocateDirect(g_color_buffer_data.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		this.colorBuffer.put(g_color_buffer_data).position(0); 

		int vertexShader = MyGLRenderer.loadShader(
				GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = MyGLRenderer.loadShader(
				GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		program = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(program);                  // create OpenGL program executables

		positionHandle = GLES20.glGetAttribLocation(program, "vertexPosition_modelspace");
		colorHandle = GLES20.glGetAttribLocation(program, "vertexColor");
		mvpHandle = GLES20.glGetUniformLocation(program, "MVP");
		MyGLRenderer.checkGlError("glGetUniformLocation");
	}

	public void draw(float[] mvpMatrix){
		float[] viewMatrix = new float[16];
		float[] mvp = new float[16];

		Matrix.setLookAtM(viewMatrix, 0, 5f, 4f, -10f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(mvp, 0, mvpMatrix, 0, viewMatrix, 0);

		GLES20.glUseProgram(program);

		this.vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(
				positionHandle, 3,
				GLES20.GL_FLOAT, false, 
				0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(positionHandle);

		for (int i = 0; i < 12*3 ; i++){
			g_color_buffer_data[3*i+0] += 0.005f;
			g_color_buffer_data[3*i+1] += 0.005f;
			g_color_buffer_data[3*i+2] += 0.005f;
			if(g_color_buffer_data[3*i+0] > 1.0f) g_color_buffer_data[3*i+0] = 0.0f;
			if(g_color_buffer_data[3*i+1] > 1.0f) g_color_buffer_data[3*i+1] = 0.0f;
			if(g_color_buffer_data[3*i+2] > 1.0f) g_color_buffer_data[3*i+2] = 0.0f;
		}
		this.colorBuffer = ByteBuffer.allocateDirect(g_color_buffer_data.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		this.colorBuffer.put(g_color_buffer_data).position(0); 

		this.colorBuffer.position(0);
		GLES20.glVertexAttribPointer(
				colorHandle, 3, 
				GLES20.GL_FLOAT, false, 
				0, colorBuffer);
		GLES20.glEnableVertexAttribArray(colorHandle);

		GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvp, 0);
		MyGLRenderer.checkGlError("glUniformMatrix4fv");
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12 * 3);
	}

}
