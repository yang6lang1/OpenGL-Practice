package com.airhockyproject.android.objects;

import java.util.List;

import com.airhockyproject.android.data.VertexArray;
import com.airhockyproject.android.objects.ObjectBuilder.DrawCommand;
import com.airhockyproject.android.objects.ObjectBuilder.GeneratedData;
import com.airhockyproject.android.util.ColorShaderProgram;
import com.airhockyproject.android.util.Geometry.Cylinder;
import com.airhockyproject.android.util.Geometry.Point;

public class Puck {
	private static final int POSITION_COMPONENT_COUNT = 3;

	public final float radius, height;

	private final VertexArray vertexArray;
	private final List<DrawCommand> drawList;

	public Puck(float radius, float height, int numPointsAroundPuck){
		GeneratedData generatedData = ObjectBuilder.createPuck(
				new Cylinder(new Point(0f,0f,0f),radius, height), 
				numPointsAroundPuck);

		this.radius = radius;
		this.height = height;

		vertexArray = new VertexArray(generatedData.vertexData);
		drawList = generatedData.drawList;
	}

	public void bindData(ColorShaderProgram colorProgram){
		vertexArray.setVertexAttribPointer(0,
				colorProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT, 0);
	}

	public void draw() {
		for (DrawCommand drawCommand : drawList) { 
			drawCommand.draw();
		} 
	}
}
