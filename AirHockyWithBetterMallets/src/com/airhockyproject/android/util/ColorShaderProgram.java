package com.airhockyproject.android.util;

import static android.opengl.GLES20.*;

import android.content.Context;

import com.airhockyproject.android.R;
import com.airhockyproject.android.programs.ShaderProgram;

public class ColorShaderProgram extends ShaderProgram{
	//Uniform locations
	private final int uMatrixLocation;
	private final int uColorLocation;
	
	//Attribute locations
	private final int aPositionLocation;
	
	public ColorShaderProgram(Context context){
		super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
		//Retrieve uniform locations for the shader program.
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		//Retrieve attribute locations for the shader program.
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
	}
	
	public void setUniforms(float[] matrix, float r, float g, float b){
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform4f(uColorLocation, r, g, b, 1f);
	}

	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}
}
