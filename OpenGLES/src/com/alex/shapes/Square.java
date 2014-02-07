package com.alex.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.alex.opengles.MyGLRenderer;

public class Square {

  private FloatBuffer vertexBuffer;
  private ShortBuffer drawListBuffer;

	private final String vertexShaderCode =
			"attribute vec4 vPosition;" +
					"void main() {" +
					"  gl_Position = vPosition;" +
					"}";

	private final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";

  private final int mProgram;
  private int mPositionHandle;
  private int mColorHandle;
  private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

  // number of coordinates per vertex in this array
  private static final int COORDS_PER_VERTEX = 3;
  private static float squareCoords[] = {
          -0.5f,  0.5f, 0.0f,   // top left
          -0.5f, -0.5f, 0.0f,   // bottom left
           0.5f, -0.5f, 0.0f,   // bottom right
           0.5f,  0.5f, 0.0f }; // top right

  private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
  float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };
  
  public Square() {
      // initialize vertex byte buffer for shape coordinates
      ByteBuffer bb = ByteBuffer.allocateDirect(
      // (# of coordinate values * 4 bytes per float)
              squareCoords.length * 4);
      bb.order(ByteOrder.nativeOrder());
      vertexBuffer = bb.asFloatBuffer();
      vertexBuffer.put(squareCoords);
      vertexBuffer.position(0);

      // initialize byte buffer for the draw list
      ByteBuffer dlb = ByteBuffer.allocateDirect(
      // (# of coordinate values * 2 bytes per short)
              drawOrder.length * 2);
      dlb.order(ByteOrder.nativeOrder());
      drawListBuffer = dlb.asShortBuffer();
      drawListBuffer.put(drawOrder);
      drawListBuffer.position(0);
      
      // prepare shaders and OpenGL program
      int vertexShader = MyGLRenderer.loadShader(
              GLES20.GL_VERTEX_SHADER,
              vertexShaderCode);
      int fragmentShader = MyGLRenderer.loadShader(
              GLES20.GL_FRAGMENT_SHADER,
              fragmentShaderCode);

      mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
      GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
      GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
      GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
  }
  
  public void draw(){

    // Add program to OpenGL environment
    GLES20.glUseProgram(mProgram);

    // get handle to vertex shader's vPosition member
    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

    // Enable a handle to the triangle vertices
    GLES20.glEnableVertexAttribArray(mPositionHandle);

    // Prepare the triangle coordinate data
    GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer);

    // get handle to fragment shader's vColor member
    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

    // Set color for drawing the triangle
    GLES20.glUniform4fv(mColorHandle, 1, color, 0);

    // Draw the square
    GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.length,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

    // Disable vertex array
    GLES20.glDisableVertexAttribArray(mPositionHandle);

  }
}