package com.alex.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import android.opengl.GLES20;

import com.alex.opengles.MyGLRenderer;

public class Cube {

	private final String vertexShaderCode =
					"attribute vec3 a_color;" +
					"attribute vec3 a_position;" +
					"varying vec3 fragmentColor;" +
					"uniform mat4 mvp;" +
					"void main() {" +
					"  gl_Position = mvp * vec4(a_position,1);" +
					"fragmentColor = a_color;"+
					"}";

	private final String fragmentShaderCode =
					"precision mediump float;"+
					"varying vec3 fragmentColor;" +
					"void main() {" +
					"  gl_FragColor = vec4(fragmentColor,1);" +
					"}";

	private final FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private final ShortBuffer drawListBuffer;
	private final int program;
	private int positionHandle;
	private int colorHandle;
	private int mvpHandle;

	private float[] cubeCoords = {
			1.0f, 1.0f, 1.0f,			//A	-	0
			-1.0f, 1.0f, 1.0f,		//B	-	1	
			-1.0f, -1.0f, 1.0f,		//C	-	2
			1.0f, -1.0f, 1.0f,		//D	-	3
			1.0f, 1.0f, -1.0f,		//E	-	4
			-1.0f, 1.0f, -1.0f,		//F	-	5
			-1.0f, -1.0f, -1.0f,	//G	-	6
			1.0f, -1.0f, -1.0f		//H	-	7
	};

	private float[] colorCoords;

	private final short drawOrder[]	= {
			0,	1,	3,	1,	2,	3,		//ABD BCD
			0,	3,	4,	4,	3,	7,		//ADE EDH
			5,	1,	4,	1,	0,	4,		//FBE BAE
			1,	5,	6,	6,	2,	1,		//BFG GCB
			2,	6,	3,	3,	6,	7,		//CGD DGH
			4,	7,	6,	4,	6,	5			//EHG EGF
	};

	public static final int COORDS_PER_VERTEX = 3;
	private Random generator = new Random(System.currentTimeMillis());

	public Cube(){
		ByteBuffer bb = ByteBuffer.allocateDirect(cubeCoords.length * 4)
				.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(cubeCoords).position(0);

		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder).position(0);

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

		colorCoords = new float[12*3*3];
		for(int i = 0; i < 12*3; i++){
			colorCoords[3*i +0] = generator.nextFloat();
			colorCoords[3*i +1] = generator.nextFloat();
			colorCoords[3*i +2] = generator.nextFloat();
		}
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer cb = ByteBuffer.allocateDirect(colorCoords.length * 4)
				.order(ByteOrder.nativeOrder());
		colorBuffer = cb.asFloatBuffer();
		colorBuffer.put(colorCoords).position(0);
		positionHandle = GLES20.glGetAttribLocation(program, "a_position");
		colorHandle = GLES20.glGetAttribLocation(program, "a_color");
		mvpHandle = GLES20.glGetAttribLocation(program, "mvp");
	}

	public void draw(float[] mvpMatrix){
		// Add program to OpenGL environment
		GLES20.glUseProgram(program);

		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(
				positionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				COORDS_PER_VERTEX * 4, vertexBuffer);
		GLES20.glEnableVertexAttribArray(positionHandle);

//
//		for (int i = 0; i < 12*3 ; i++){
//			colorCoords[3*i+0] += 0.005f;
//			colorCoords[3*i+1] += 0.005f;
//			colorCoords[3*i+2] += 0.005f;
//			if(colorCoords[3*i+0] > 1.0f) colorCoords[3*i+0] = 0.0f;
//			if(colorCoords[3*i+1] > 1.0f) colorCoords[3*i+1] = 0.0f;
//			if(colorCoords[3*i+2] > 1.0f) colorCoords[3*i+2] = 0.0f;
//		}
//		ByteBuffer cb = ByteBuffer.allocateDirect(
//				colorCoords.length * 4);
//		cb.order(ByteOrder.nativeOrder());
//		colorBuffer = cb.asFloatBuffer();
//		colorBuffer.put(colorCoords);
//		colorBuffer.position(0);
		GLES20.glVertexAttribPointer(
				colorHandle, 3,
				GLES20.GL_FLOAT, false,
				3 * 4, colorBuffer);
		GLES20.glEnableVertexAttribArray(colorHandle);
		
		GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
		MyGLRenderer.checkGlError("glUniformMatrix4fv");

		// Draw the cube
		GLES20.glDrawElements(
				GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(colorHandle);
	}
}
