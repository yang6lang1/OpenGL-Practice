package com.example.learnopengles.utils;

import static android.opengl.GLES20.*;
import android.util.Log;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";

	public static int compileVertexShader(String shaderCode){
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}

	public static int compileFragmentShader(String shaderCode){
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}

	private static int compileShader(int type, String shaderCode){
		final int shaderObjectId = glCreateShader(type);

		//check if shader has been successfully created
		if(shaderObjectId == 0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Could not create new shader.");
			}

			return 0;
		}

		//Upload shader source code into shader object
		glShaderSource(shaderObjectId, shaderCode);
		glCompileShader(shaderObjectId);

		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

		if(LoggerConfig.ON){
			Log.v(TAG, "Results of compiling source:\n"+shaderCode+"\n:"
					+glGetShaderInfoLog(shaderObjectId));
		}
		
		if(compileStatus[0] == 0){
			//if fail, we delete the shader object
			glDeleteShader(shaderObjectId);
			
			if(LoggerConfig.ON){
				Log.w(TAG, "Compilation of shader failed.");
			}
			
			return 0;
		}
		
		return shaderObjectId;
	}
	
	public static int linkProgram(int vertexShaderId, int fragmentShaderId){
		final int programObjectId = glCreateProgram();
		
		if(programObjectId == 0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Could not create new program.");
			}
			
			return 0;
		}
		
		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);
		
		glLinkProgram(programObjectId);
		
		final int[] linkStatus = new int[1];
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
		if(LoggerConfig.ON){
			Log.v(TAG, "Results of linking program:\n"
					+ glGetProgramInfoLog(programObjectId));
		}
		
		if(linkStatus[0] == 0){
			glDeleteProgram(programObjectId);
			if(LoggerConfig.ON){
				Log.w(TAG, "Linking of program failed.");
			}
			return 0;
		}
		
		return programObjectId;
	}
	
	/**Validate if the current program might be inefficient, failing to run, and so on.*/
	public static boolean validateProgram(int programObjectId){
		glValidateProgram(programObjectId);
		
		final int[] validateStatus = new int[1];
		glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
		Log.v(TAG, "Results of validating program: " + validateStatus[0]
				+ "\nLog:" + glGetProgramInfoLog(programObjectId));
		
		return validateStatus[0] != 0;
	} 
	
	public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
		int program;
		
		//Compile the shaders
		int vertexShader = compileVertexShader(vertexShaderSource);
		int fragmentShader = compileFragmentShader(fragmentShaderSource);
		
		//Link them into a shader program
		program = linkProgram(vertexShader, fragmentShader);
		
		if(LoggerConfig.ON){
			validateProgram(program);
		}
		
		return program;
	}
}
