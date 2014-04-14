package com.particles.android;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.particles.android.objects.ParticleShooter;
import com.particles.android.objects.ParticleSystem;
import com.particles.android.objects.Skybox;
import com.particles.android.programs.ParticleShaderProgram;
import com.particles.android.programs.SkyboxShaderProgram;
import com.particles.android.util.Geometry.Point;
import com.particles.android.util.Geometry.Vector;
import com.particles.android.util.MatrixHelper;
import com.particles.android.util.TextureHelper;

public class MyGLRenderer implements Renderer {
	private final Context context;

	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];
	private final float[] viewProjectionMatrix = new float[16];

	private ParticleShaderProgram particleProgram; 
	private SkyboxShaderProgram skyboxProgram;
	private ParticleSystem particleSystem;
	private Skybox skybox;
	private ParticleShooter redParticleShooter; 
	private ParticleShooter greenParticleShooter; 
	private ParticleShooter blueParticleShooter; 
	private long globalStartTime;
	private int particleTexture;
	private int skyboxTexture;

	public MyGLRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glEnable(GL_BLEND);
    glBlendFunc(GL_ONE, GL_ONE);
    
		particleProgram = new ParticleShaderProgram(context); 
		particleSystem = new ParticleSystem(10000); 
		globalStartTime = System.nanoTime();
		particleTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture);

		final Vector particleDirection = new Vector(0f, 0.5f, 0f);
		final float angleVarianceInDegrees = 5f;
		final float speedVariance = 1f;
		
		redParticleShooter = new ParticleShooter( new Point(-1f, 0f, 0f), particleDirection,
				Color.rgb(255, 50, 5), angleVarianceInDegrees, speedVariance);

		greenParticleShooter = new ParticleShooter( new Point(0f, 0f, 0f), particleDirection,
				Color.rgb(25, 255, 25), angleVarianceInDegrees, speedVariance);

		blueParticleShooter = new ParticleShooter( new Point(1f, 0f, 0f), particleDirection,
				Color.rgb(5, 50, 255), angleVarianceInDegrees, speedVariance);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);

		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
				/ (float) height, 1f, 10f);
		setIdentityM(viewMatrix, 0);
		translateM(viewMatrix, 0, 0f, -1.5f, -5f);
		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
				viewMatrix, 0);
	}


	@Override
	public void onDrawFrame(GL10 glUnused) {
		// Clear the rendering surface.
		glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
		
    redParticleShooter.addParticles(particleSystem, currentTime, 5);
    greenParticleShooter.addParticles(particleSystem, currentTime, 5);
    blueParticleShooter.addParticles(particleSystem, currentTime, 5);
    
    particleProgram.useProgram();
    particleProgram.setUniforms(viewProjectionMatrix, currentTime, particleTexture);
    particleSystem.bindData(particleProgram);
    particleSystem.draw();

	}
}