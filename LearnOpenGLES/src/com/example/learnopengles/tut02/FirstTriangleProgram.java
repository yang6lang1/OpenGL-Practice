package com.example.learnopengles.tut02;

import static android.opengl.GLES20.*;
import android.content.Context;

import com.example.learnopengles.R;
import com.example.learnopengles.programs.ShaderProgram;

public class FirstTriangleProgram extends ShaderProgram{

	private final int uColorLocation;
	
	private final int aPositionLocation;
	
	public FirstTriangleProgram(Context context) {
		super(context, R.raw.tut02_vertex_shader, R.raw.tut02_fragment_shader);
		
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
	}
	
	/**Supply the color array consists of 3 float numbers: r, g, b*/
	public void setColor(float[] color){
		glUniform3f(uColorLocation, color[0], color[1], color[2]);
	}
	
	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}

}
