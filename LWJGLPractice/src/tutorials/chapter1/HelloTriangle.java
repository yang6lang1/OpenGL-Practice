package tutorials.chapter1;

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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import utility.Log;

public class HelloTriangle {

  // Whether to enable VSync in hardware.
  public static final boolean VSYNC = true;

  // Width and height of our window
  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;

  // Whether to use fullscreen mode
  public static final boolean FULLSCREEN = false;

  // Whether our game loop is running
  protected boolean running = false;

  public static void main(String[] args) throws LWJGLException {
	new HelloTriangle().start();
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

	// While we're still running and the user hasn't closed the window... 
	while (running && !Display.isCloseRequested()) {
	  // If the game was resized, we need to update our projection
	  if (Display.wasResized())
		resize();

	  // Render the game
	  render();

	  // Flip the buffers and sync to 60 FPS
	  Display.update();
	  Display.sync(60);
	}

	// Dispose any resources and destroy our window
	dispose();
	Display.destroy();
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
  
  //A shader is nothing more than a program that runs on the GPU.
  /**Vertex shaders, as the name implies, operate on vertices.*/
  public static final String strVertexShader = 	"#version 330\n"+
	  "layout(location = 0) in vec4 position;\n"+
	  "void main()\n"+
	  "{\n"+
	  "   gl_Position = position;\n"+
	  "}\n";
  
  /**A fragment shader is used to compute the output color(s) of a fragment. */
  public static final String strFragmentShader = "#version 330\n"+
	  "out vec4 outputColor;\n"+
	  "void main()\n"+
	  "{\n"+
	  "   outputColor = vec4(1.0f, 1.0f, 1.0f, 1f);\n"+
	  "}\n";

  private void init() {
	initializeProgram();
	initializeVertexBuffer();

	vao = glGenVertexArrays();
	glBindVertexArray(vao);
  }

  /**This is the triangle data we want to transfer to OpenGL*/
  public static final float[] vertexPositions = {
		0.75f, 0.75f, 0.0f, 1.0f,
		0.75f, -0.75f, 0.0f, 1.0f,
		-0.75f, -0.75f, 0.0f, 1.0f,
		-0.75f, 0.75f, 0.0f, 1.0f,
		0f, .75f, 0f, 1f,
		0f, -.75f, 0f, 1f
	};
  
  /**
   * Even though we have this data, OpenGL cannot use it directly. Therefore, the first step is to 
   * allocate some memory that OpenGL can see , and fill that memory with our data. This is done 
   * with something called a buffer object.</br></br>
   * 
   * The following code is the buffer object initialization:
   * */
  private void initializeVertexBuffer() {
	//create the buffer object, storing the handle to the object in the global variable
	//positionBufferObject. Though the object now exists, it does not own any memory yet.
	positionBufferObject = glGenBuffers();

	//bind the newly-create buffer object to the GL_ARRAY_BUFFER binding target.
	//objects in OpenGL usually have to be bound to the context in order for them
	//to do anything
	glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
	FloatBuffer buf = BufferUtils.createFloatBuffer(vertexPositions.length);
	for(float i : vertexPositions){
	  buf.put(i);
	}
	buf.position(0);
	
	//1. It allocates memory for the buffer currently bound to GL_ARRAY_BUFFER
	//2. Copy data from our memory array to allocate for this buffer object
	//	 in other words: create and initialize a buffer object's data store
	glBufferData(GL_ARRAY_BUFFER, buf , GL_STATIC_DRAW);
	
	//this is simply cleanup
	//By binding the buffer object 0 to GL_ARRAY_BUFFER, 
	//we cause the buffer object previously bound to that target to become unbound from it. 
	//This is not strictly necessary, as any later binds to this target will simply
	//unbind what is already there. But unless you have very strict controll over your rendering, 
	//it is usually a good idea to unbind the objects you bind.
	glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

  /**A program object in OpenGL contains code for all of the shaders to be used for rendering*/
  private void initializeProgram() {
	List<Integer> shaderList = new ArrayList<Integer>();

	shaderList.add(createShader(GL_VERTEX_SHADER, strVertexShader));
	shaderList.add(createShader(GL_FRAGMENT_SHADER, strFragmentShader));

	
	programObject = createProgram(shaderList);
	
	for(Integer entry : shaderList){
	  glDeleteShader(entry);
	}
  }

  public int positionBufferObject = 0;
  public int vao = 0;
  
  private void draw(){
	
	//clear the screen.
	//glClearColor is one of those state setting functions; it sets the color that will be used
	//when clearing the screen
	glClearColor(0f, 0f, 0f, 0f);
	//glClear does not set OpenGL state; it causes the screen to be cleared.
	//The GL_COLOR_BUFFER_BIT parameter means that the clear call will affect the color buffer, 
	//causing it to be cleared to the current clearing color we set in the previous function.
	glClear(GL_COLOR_BUFFER_BIT);
	
	//This sets the current shader program to be used by all subsequent rendering commands
	glUseProgram(programObject);
	
	//set state calls
	//These commands set up the coordinates of the triangle to be rendered.
	//They tell OpenGL the location in memory that the positions of the triangle will come from
	//The first function simply says that we are going to use this buffer object.
	glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
	glEnableVertexAttribArray(0);
	/* (Important)
	 * glVertexAttribPointer, despite having the word "Pointer" in it, does not deal with pointers.
	 * Instead, it deals with buffer objects.
	 * 
	 * When rendering, OpenGL pulls vertex data from arrays stored in buffer objects. What we need to 
	 * tell OpenGL is what format our vertex array data in the buffer object is stored in. That is,
	 * we need to tell OpenGL how to interpret the array of data stored in the buffer.
	 * 
	 * (reference to vertexPositions array)
	 * glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset);
	 * index: Vertex attribute index
	 * size: how many of these values represent a single pieve of data. In this case, that is 4.
	 * type: the base type of the value. In this case, GL_FLOAT, which = 32-bit floating-point value
	 * normalized:
	 * stride: this specifies the spacing between each set of values. In our case, it's 0.
	 * buffer_buffer_offset: this specifies that byte offset from the value in the buffer object is at the front.
	 * 
	 * This function describes where the data for an vertex attribute comes from
	 * */
	glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
	
	//A rendering function. It uses the current state to generate a stream of vertices that will form triangles
	//OpenGL can interpret a list of vertices in a variety of different ways. 
	//The way OpenGL interprets vertex lists is given by the draw command:
	glDrawArrays(GL_TRIANGLES, 0, 6);
//	glDrawArrays(GL_TRIANGLES, 3, 3);
	
	//cleanup work, undoing some of the setup that was done for the purposes of rendering
	glDisableVertexAttribArray(0);
	glUseProgram(0);
	
	//This is a FreeGLUT command, not an OpenGL command.
	//glutSwapBuffers is the function that causes the image we are rendering to be displayed to the user.
	//I can replace this with my own method
//	glutSwapBuffers();
//	glutPostRedisplay();
  }

  /**Compiles a shader into shader object
   * */
  private int createShader(int eShaderType, final String strShaderFile){
	//1. create the shader object of a particular type (vertex or fragment)
	int shader = glCreateShader(eShaderType);
	final String strFileData = strShaderFile;
	//Retrieve the string from strShaderFile and feed into the shader object
	glShaderSource(shader, strFileData);

	glCompileShader(shader)	;

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