package com.airhockyproject.android;


import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockyproject.android.objects.Mallet;
import com.airhockyproject.android.objects.Puck;
import com.airhockyproject.android.objects.Table;
import com.airhockyproject.android.util.ColorShaderProgram;
import com.airhockyproject.android.util.Geometry;
import com.airhockyproject.android.util.Geometry.Plane;
import com.airhockyproject.android.util.Geometry.Point;
import com.airhockyproject.android.util.Geometry.Ray;
import com.airhockyproject.android.util.Geometry.Sphere;
import com.airhockyproject.android.util.Geometry.Vector;
import com.airhockyproject.android.util.MatrixHelper;
import com.airhockyproject.android.util.TextureHelper;
import com.airhockyproject.android.util.TextureShaderProgram;

public class MyGLRenderer implements Renderer {
	private final Context context;

	private final float[] projectionMatrix = new float[16];
	private final float[] modelMatrix = new float[16];
	private final float[] viewMatrix = new float[16];
	private final float[] viewProjectionMatrix = new float[16];
	private final float[] invertedViewProjectionMatrix = new float[16];
	private final float[] modelViewProjectionMatrix = new float[16];

	private Table table;
	private Mallet mallet;
	private Puck puck;

	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;

	private int texture;

	private boolean malletPressed = false;
	private Point blueMalletPosition;
	private Point previousBlueMalletPosition;
	private Point puckPosition;
	private Vector puckVector;

	private final float leftBound = -0.5f;
	private final float rightBound = 0.5f;
	private final float farBound = -0.8f;
	private final float nearBound = 0.8f;

	public MyGLRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LESS);

		table = new Table();
		mallet = new Mallet(0.08f, 0.15f, 32);
		puck = new Puck(0.06f, 0.02f, 32);
		blueMalletPosition = new Point(0f, mallet.height/2f, 0.4f);
		puckPosition = new Point(0f, puck.height / 2f, 0f);
		puckVector = new Vector(0f, 0f, 0f);

		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);

		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);
		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
				/ (float) height, 1f, 10f);
		setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
	}


	@Override
	public void onDrawFrame(GL10 glUnused) {
		// Clear the rendering surface.
		glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Multiply the view and projection matrices together.
		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

		// Draw the table.
		positionTableInScene();
		textureProgram.useProgram();
		textureProgram.setUniforms(modelViewProjectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();

		// Draw the mallets.
		positionObjectInScene(0f, mallet.height / 2f, -0.4f);
		colorProgram.useProgram();
		colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
		mallet.bindData(colorProgram);
		mallet.draw();

		positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z);
		colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
		// Note that we don't have to define the object data twice -- we just
		// draw the same mallet again but in a different position and with a
		// different color.
		mallet.draw();

		// Draw the puck.
		puckPosition = puckPosition.translate(puckVector);
		if(puckPosition.x < leftBound + puck.radius || puckPosition.x > rightBound - puck.radius){
			puckVector = new Vector(-puckVector.x, puckVector.y, puckVector.z);
			puckVector = puckVector.scale(0.9f);
		}
		if (puckPosition.z < farBound + puck.radius || puckPosition.z > nearBound - puck.radius) {
			puckVector = new Vector(puckVector.x, puckVector.y, -puckVector.z); 
			puckVector = puckVector.scale(0.9f);
		}
		puckPosition = new Point(
				clamp(puckPosition.x, leftBound + puck.radius, rightBound - puck.radius),
				puckPosition.y,
				clamp(puckPosition.z, farBound + puck.radius, nearBound - puck.radius));
		
		positionObjectInScene(puckPosition.x, puckPosition.y, puckPosition.z);
		colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
		puck.bindData(colorProgram);
		puck.draw();
		//Damping friction:
		puckVector = puckVector.scale(0.99f);
	}

	private void positionTableInScene() {
		// The table is defined in terms of X & Y coordinates, so we rotate it
		// 90 degrees to lie flat on the XZ plane.
		setIdentityM(modelMatrix, 0);
		rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
		multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
				0, modelMatrix, 0);
	}

	// The mallets and the puck are positioned on the same plane as the table.
	private void positionObjectInScene(float x, float y, float z) {
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, x, y, z);
		multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
				0, modelMatrix, 0);
	}

	public void handleTouchPress(float normalizedX, float normalizedY){
		Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);

		//now test if the ray intersects with the mallet by creating a bounding sphere that wraps the mallet
		Sphere malletBoundingSphere = new Sphere(new Point(
				blueMalletPosition.x,
				blueMalletPosition.y,
				blueMalletPosition.z),
				mallet.height / 2f);

		//if the ray intersects, then malletPressed = true;
		malletPressed = Geometry.intersects(malletBoundingSphere, ray);
	}

	public void handleTouchDrag(float normalizedX, float normalizedY){
		if(malletPressed){
			Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
			//define a plane representing our air hocky table
			Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 1, 0));
			Point touchedPoint = Geometry.intersectionPoint(ray, plane);
			previousBlueMalletPosition = blueMalletPosition;
			blueMalletPosition = new Point(
					clamp(touchedPoint.x, leftBound + mallet.radius, rightBound - mallet.radius),
					mallet.height / 2f,
					clamp(touchedPoint.z, 0f + mallet.radius, nearBound - mallet.radius));

			float distance = Geometry.vectorBetween(blueMalletPosition, puckPosition).length();
			if(distance < (puck.radius + mallet.radius)){
				// The mallet has struck the puck. Now send the puck flying 
				// based on the mallet velocity.
				puckVector = Geometry.vectorBetween(previousBlueMalletPosition, blueMalletPosition);
			}
		}
	}

	private Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY){
		// We'll convert these normalized device coordinates into world-space
		// coordinates. We'll pick a point on the near and far planes, and draw a 
		// line between them. To do this transform, we need to first multiply by 
		// the inverse matrix, and then we need to undo the perspective divide.
		final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
		final float[] farPointNdc = {normalizedX, normalizedY, 1, 1};

		final float[] nearPointWorld = new float[4];
		final float[] farPointWorld = new float[4];

		multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
		multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

		divideByW(nearPointWorld);
		divideByW(farPointWorld);

		Point nearPointRay = new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
		Point farPointRay = new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

		return new Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
	}

	private void divideByW(float[] vector){
		vector[0] /= vector[3];
		vector[1] /= vector[3];
		vector[2] /= vector[3];
	}

	private float clamp(float value, float min, float max){
		return Math.min(max, Math.max(value, min));
	}
}