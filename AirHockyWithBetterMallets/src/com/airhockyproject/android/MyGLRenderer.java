package com.airhockyproject.android;


import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockyproject.android.objects.Mallet;
import com.airhockyproject.android.objects.Puck;
import com.airhockyproject.android.objects.Table;
import com.airhockyproject.android.util.ColorShaderProgram;
import com.airhockyproject.android.util.MatrixHelper;
import com.airhockyproject.android.util.TextureHelper;
import com.airhockyproject.android.util.TextureShaderProgram;

public class MyGLRenderer implements Renderer {
  private final Context context;

  private final float[] projectionMatrix = new float[16];
  private final float[] modelMatrix = new float[16];
  private final float[] viewMatrix = new float[16];
  private final float[] viewProjectionMatrix = new float[16];
  private final float[] modelViewProjectionMatrix = new float[16];

  private Table table;
  private Mallet mallet;
  private Puck puck;

  private TextureShaderProgram textureProgram;
  private ColorShaderProgram colorProgram;

  private int texture;

  public MyGLRenderer(Context context) {
      this.context = context;
  }

  @Override
  public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
      glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

      table = new Table();
      mallet = new Mallet(0.08f, 0.15f, 32);
      puck = new Puck(0.06f, 0.02f, 32);

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
      glClear(GL_COLOR_BUFFER_BIT);
      
      // Multiply the view and projection matrices together.
      multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

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

      positionObjectInScene(0f, mallet.height / 2f, 0.4f);
      colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
      // Note that we don't have to define the object data twice -- we just
      // draw the same mallet again but in a different position and with a
      // different color.
      mallet.draw();

      // Draw the puck.
      positionObjectInScene(0f, puck.height / 2f, 0f);
      colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
      puck.bindData(colorProgram);
      puck.draw();
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
}