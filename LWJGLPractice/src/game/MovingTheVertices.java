package game;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import utility.Log;
import utility.TXTReader;


public class MovingTheVertices {

  // Whether to enable VSync in hardware.
  public static final boolean VSYNC = true;
  public static final String vertexShaderFilePath = "Data/tut03/vertexShader.txt";
  public static final String fragShaderFilePath = "Data/tut03/fragmentShader.txt";

  // Width and height of our window
  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;

  // Whether to use fullscreen mode
  public static final boolean FULLSCREEN = true;

  // Whether our game loop is running
  protected boolean running = false;

  private long startTime = 0;
  private long elapsedTime = 0;
  private long lastTime = 0;
  
  public static void main(String[] args) throws LWJGLException {
	new MovingTheVertices().start();
  }

  // Start our game
  public void start() throws LWJGLException {
	// Set up our display 
	Display.setTitle("Display example"); //title of our window
	Display.setResizable(true); //whether our window is resizable
	Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); //resolution of our display
	Display.setVSyncEnabled(VSYNC); //whether hardware VSync is enabled
	Display.setFullscreen(FULLSCREEN); //whether fullscreen is enabled

	//create and show our display
	Display.create();

	// Create our OpenGL context and initialize any resources
	create();

	// Call this before running to set up our initial size
	resize();

	running = true;
	startTime = this.getTime();
	elapsedTime = 0;
	lastTime = startTime;
	// While we're still running and the user hasn't closed the window... 
	while (running && !Display.isCloseRequested()) {
	  // If the game was resized, we need to update our projection
	  if (Display.wasResized())
		resize();

	  update();
	  // Render the game
	  render();

	  // Flip the buffers and sync to 60 FPS
	  Display.update();
	  Display.sync(60);

	  elapsedTime = this.getTime()-startTime;
	}

	// Dispose any resources and destroy our window
	dispose();
	Display.destroy();
  }

  /**Update the program*/
  private void update() {
	//Log.p(""+elapsedTime);
  }

  /**Retrieve the current time in milliseconds
   * */
  private long getTime(){
	return (Sys.getTime() *1000) / Sys.getTimerResolution();
  }

  private long getDelta(){
	long time = getTime();
	long delta = time - lastTime;
	lastTime = time;

	return delta;
  }

  // Exit our game loop and close the window
  public void exit() {
	running = false;
  }

  // Called to setup our game and context
  protected void create() {
	// 2D games generally won't require depth testing 
	glDisable(GL_DEPTH_TEST);

	// Enable blending
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	// Set clear to transparent black
	glClearColor(0f,0f,0f,0f);

	// ... initialize resources here ...
	init();
  }

  // Called to render our game
  protected void render() {
	// Clear the screen
	glClear(GL_COLOR_BUFFER_BIT);

	// ... render our game here ...
	draw();
  }

  // Called to resize our game
  protected void resize() {
	if(Display.getWidth() > Display.getHeight()){
	  int offset = (int)((Display.getWidth()-Display.getHeight())/2);
	  glViewport(offset, 0,  Display.getHeight(), Display.getHeight());
	}
	else{
	  int offset = (int)((Display.getHeight()-Display.getWidth())/2);
	  glViewport(0, offset,  Display.getWidth(), Display.getWidth());
	}

	// ... update our projection matrices here ...
  }

  // Called to destroy our game upon exiting
  protected void dispose() {
	// ... dispose of any textures, etc ...
  }

  public int programObject = 0;

  private void init() {
	initializeProgram();
	initializeVertexBuffer();

	vao = glGenVertexArrays();
	glBindVertexArray(vao);
  }

  /**This is the triangle data we want to transfer to OpenGL*/
  public static final float[] vertexPositions = { 
	0.0f,    0.5f, 0.0f, 1.0f,
	0.5f, -0.366f, 0.0f, 1.0f,
	-0.5f, -0.366f, 0.0f, 1.0f,
	//	-1f,    0f, 0.0f, 1.0f,
	//	-0.5f, 0f, 0.0f, 1.0f,
	//	-0.5f, 0.5f, 0.0f, 1.0f,
	//	1.0f,    0.0f, 0.0f, 1.0f,
	//	0.0f,    1.0f, 0.0f, 1.0f,
	//	0.0f,    0.0f, 1.0f, 1.0f,
	//	1.0f,    0.0f, 0.0f, 1.0f,
	//	0.0f,    1.0f, 0.0f, 1.0f,
	//	0.0f,    0.0f, 1.0f, 1.0f
  };

  /**
   * Even though we have this data, OpenGL cannot use it directly. Therefore, the first step is to 
   * allocate some memory that OpenGL can see , and fill that memory with our data. This is done 
   * with something called a buffer object.</br></br>
   * 
   * The following code is the buffer object initialization:
   * */
  private void initializeVertexBuffer() {
	positionBufferObject = glGenBuffers();

	glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
	FloatBuffer buf = BufferUtils.createFloatBuffer(vertexPositions.length);
	for(float i : vertexPositions){
	  buf.put(i);
	}
	buf.position(0);

	glBufferData(GL_ARRAY_BUFFER, buf , GL_STREAM_DRAW);

	glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

  /**A program object in OpenGL contains code for all of the shaders to be used for rendering*/
  private void initializeProgram() {
	List<Integer> shaderList = new ArrayList<Integer>();

	shaderList.add(loadShader(GL_VERTEX_SHADER, vertexShaderFilePath));
	shaderList.add(loadShader(GL_FRAGMENT_SHADER, fragShaderFilePath));


	programObject = createProgram(shaderList);

	for(Integer entry : shaderList){
	  glDeleteShader(entry);
	}
  }

  public int positionBufferObject = 0;
  public int vao = 0;

  private void draw(){

	float[] offset = {0.0f,0.0f};
	offset = computePositionOffsets(offset[0], offset[1]);
	 
	adjustVertexData(offset[0], offset[1]);

	glClearColor(0f, 0f, 0f, 0f);
	glClear(GL_COLOR_BUFFER_BIT);

	glUseProgram(programObject);

	glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
	glEnableVertexAttribArray(0);
	//	glEnableVertexAttribArray(1);
	glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
	//	glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 96);

	glDrawArrays(GL_TRIANGLES, 0, 3);
	//	glDrawArrays(GL_TRIANGLES, 3, 3);

	glDisableVertexAttribArray(0);
	//	glDisableVertexAttribArray(1);
	glUseProgram(0);

  }

  private float[] computePositionOffsets(float fXOffset, float fYOffset){
	final float fLoopDuration = 5.0f;
	final float fScale = 3.14159f * 2.0f / fLoopDuration;

	float fElapsedTime = elapsedTime/ 1000.0f;

	float fCurrTimeThroughLoop = fElapsedTime % fLoopDuration;
	fXOffset = (float)Math.cos(fCurrTimeThroughLoop * fScale) * 0.5f;
	fYOffset = (float)Math.sin(fCurrTimeThroughLoop * fScale) * 0.5f;
	
	float[] output = {fXOffset,fYOffset};
	return output;
  }

  private void adjustVertexData(float fXOffset, float fYOffset){
	float[] fNewData = new float[vertexPositions.length];
	fNewData = vertexPositions.clone();

	for(int i = 0; i < vertexPositions.length; i += 4){
	  fNewData[i] += fXOffset;
	  fNewData[i+1] += fYOffset;
	}

	glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);

	FloatBuffer buf = BufferUtils.createFloatBuffer(fNewData.length);
	for(float i : fNewData){
	  buf.put(i);
	}
	buf.position(0);

	glBufferSubData(GL_ARRAY_BUFFER, 0, buf);

	glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

  private int loadShader(int eShaderType, final String filename){
	String shaderData = "";
	try {
	  TXTReader reader = new TXTReader(filename);
	  shaderData = reader.getInputAsOneStringRetainLineBreaker();
	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	} catch (URISyntaxException e) {
	  e.printStackTrace();
	}

	int shader = glCreateShader(eShaderType);
	final String strShaderData = shaderData;
	glShaderSource(shader, strShaderData);

	glCompileShader(shader);
	if(glGetShaderi(shader,GL_COMPILE_STATUS) == GL_FALSE){
	  String strInfoLog = glGetShaderInfoLog(shader,glGetShaderi(shader, GL_INFO_LOG_LENGTH));

	  String strShaderType = null;
	  switch(eShaderType){
	  case GL_VERTEX_SHADER: strShaderType = "vertex"; break;
	  case GL_GEOMETRY_SHADER: strShaderType = "geometry"; break;
	  case GL_FRAGMENT_SHADER: strShaderType = "fragment"; break;
	  }

	  Log.p("Compile failure in %s shader:\n"+strShaderType+ "\n"+strInfoLog);
	}

	return shader;
  }

  private int createProgram(final List<Integer> shaderList){
	int program = glCreateProgram();

	for(int i = 0; i< shaderList.size(); i++){
	  glAttachShader(program, shaderList.get(i));
	}

	glLinkProgram(program);

	if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE){
	  String strInfoLog = new String();
	  strInfoLog = glGetProgramInfoLog(program,  glGetProgrami(program, GL_INFO_LOG_LENGTH));
	  Log.p("Linker failure: \n"+ strInfoLog);
	}

	for(int i = 0; i< shaderList.size(); i++){
	  glDetachShader(program, shaderList.get(i));
	}

	return program;
  }
}