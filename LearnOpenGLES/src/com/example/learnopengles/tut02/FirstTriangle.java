package com.example.learnopengles.tut02;

import static android.opengl.GLES20.*;

import com.example.learnopengles.Constants;
import com.example.learnopengles.data.VertexArray;

public class FirstTriangle {

	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int COLOR_COMPONENT_COUNT = 3;
	
	private static final int TOTAL_COMPONENT_COUNT = 
			POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT;
	
	private static final int STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;

	private final float[] triangles;
		
	private final VertexArray vertexArray;
	
	public FirstTriangle(){
		triangles = new float[] {
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.0f, 1.0f, 0.0f
			};
		vertexArray = new VertexArray(triangles);
	}
	
	public void bindData(FirstTriangleProgram triangleProgram){
		int dataOffset = 0;
		
		vertexArray.setVertexAttribPointer(dataOffset, 
				triangleProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		dataOffset += POSITION_COMPONENT_COUNT;
		
//		triangleProgram.setColor(triangles[dataOffset], triangles[dataOffset+1], triangles[dataOffset+2]);
	}
	
	public void draw(){
		glDrawArrays(GL_TRIANGLES, 0, 3);
	}
}
