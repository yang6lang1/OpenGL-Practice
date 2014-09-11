package com.example.learnopengles.tut02;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.learnopengles.R;
import com.example.learnopengles.utils.LoggerConfig;
import com.example.learnopengles.utils.ShaderHelper;
import com.example.learnopengles.utils.TextResourceReader;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.widget.Toast;

public class FirstTriangleRenderer implements Renderer{

	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	private static final int POSITION_COMPONENT_COUNT =2;
	private static final int BYTES_PER_FLOAT= 4;
	
	private final FloatBuffer vertexData;
	private Context context;
	private int program;
	private int uColorLocation;
	private int aPositionLocation;
	
//	private FirstTriangle triangle;
//	private FirstTriangleProgram triangleProgram;
	
	public FirstTriangleRenderer(Context context){
		this.context = context;
		
		float[] triangle = {
				-1.0f, -1.0f,
				1.0f, -1.0f,
				0.0f, 1.0f
			};
		vertexData = ByteBuffer
        .allocateDirect(triangle.length * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();
		vertexData.put(triangle);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//red triangle
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		
//		triangleProgram.useProgram();
//		triangleProgram.setColor(1.0f, 0.0f, 0.0f);
//		triangle.bindData(triangleProgram);
//		triangle.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		glViewport(0,0,width,height);
	}
 
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);		
		
		//Load and link vertex/fragment shader programs
		String vertexShaderSource = TextResourceReader
        .readTextFileFromResource(context, R.raw.tut02_vertex_shader);
    String fragmentShaderSource = TextResourceReader
        .readTextFileFromResource(context, R.raw.tut02_fragment_shader);

    int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
    int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

    program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

    if (LoggerConfig.ON) {
        ShaderHelper.validateProgram(program);
    }

    //use program
    glUseProgram(program);

    uColorLocation = glGetUniformLocation(program, U_COLOR);
    aPositionLocation = glGetAttribLocation(program, A_POSITION);
    
    vertexData.position(0);
    glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, 
        false, 0, vertexData);
    glEnableVertexAttribArray(aPositionLocation);
//		triangle = new FirstTriangle();
//		triangleProgram = new FirstTriangleProgram(context);
	}

}
