package com.particles.android.programs;

import static android.opengl.GLES20.*;
import com.particles.android.R;

import android.content.Context;

public class SkyboxShaderProgram extends ShaderProgram{

	private final int uMatrixLocation;
	private final int uTextureUnitLocation;

	private final int aPositionLocation;
	
	public SkyboxShaderProgram(Context context){
		super(context, R.raw.skybox_vertex_shader, R.raw.skybox_fragment_shader);
		
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
	}
	
	public void setUniforms(float[] matrix, int textureId){
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);
    glUniform1i(uTextureUnitLocation, 0);
	}
	
	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}
}
