package com.alex.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.alex.opengles.MyGLRenderer;

public class AndroidLesson {

	private final String vertexShaderCode =
			"uniform mat4 u_MVPMatrix;      \n"    
					+ "attribute vec4 a_Position;     \n"     
					+ "attribute vec4 a_Color;        \n"     

	  + "varying vec4 v_Color;          \n"     

	  + "void main()                    \n"     
	  + "{                              \n"
	  + "   v_Color = a_Color;          \n"    
	  + "   gl_Position = u_MVPMatrix   \n"     
	  + "               * a_Position;   \n"     
	  + "}                              \n";    

	private final String fragmentShaderCode =
			"precision mediump float;       \n"     // Set the default precision to medium. We don't need 
			// as high of a precision in the fragment shader.
			+ "varying vec4 v_Color;          \n"     
			+ "void main()                    \n"     
			+ "{                              \n"
			+ "   gl_FragColor = v_Color;     \n"    
			+ "} ";

	private final float[] triangle1VerticesData = {
			// X, Y, Z,
			// R, G, B, A
			-0.5f, -0.25f, 0.0f,
			1.0f, 0.0f, 0.0f, 1.0f,

			0.5f, -0.25f, 0.0f,
			0.0f, 0.0f, 1.0f, 1.0f,

			0.0f, 0.559016994f, 0.0f,
			0.0f, 1.0f, 0.0f, 1.0f};

	private final FloatBuffer mTriangle1Vertices;
	//	private final FloatBuffer mTriangle2Vertices;
	//	private final FloatBuffer mTriangle3Vertices;

	private int positionHandle;
	private int colorHandle;
	private int matrixHandle;
	private final int program;

	public AndroidLesson(){
		this.mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		this.mTriangle1Vertices.put(triangle1VerticesData).position(0);		

		// prepare shaders and OpenGL program
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
		positionHandle = GLES20.glGetAttribLocation(program,"a_Position");
		colorHandle = GLES20.glGetAttribLocation(program,"a_Color");
		matrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
	}

	public void draw(float[] mvpMatrix){
		GLES20.glUseProgram(program);
		
		this.mTriangle1Vertices.position(0);
		GLES20.glVertexAttribPointer(
				positionHandle, 3, 
				GLES20.GL_FLOAT, false, 
				7 * 4, mTriangle1Vertices);
		GLES20.glEnableVertexAttribArray(positionHandle);

		this.mTriangle1Vertices.position(3);
		GLES20.glVertexAttribPointer(
				colorHandle, 4, 
				GLES20.GL_FLOAT, false, 
				7 * 4, mTriangle1Vertices);
		GLES20.glEnableVertexAttribArray(colorHandle);
		
		GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix,0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}

}
