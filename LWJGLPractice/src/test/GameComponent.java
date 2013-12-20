package test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

/**
 * Base component for games and other similar graphics intensive
 * applications.
 * 
 * Usage: Create instance of a GameComponent subclass, add to a 
 * Container, and call the GameComponent's start() method. Parent 
 * must be made visible prior to calling start(). Override update() 
 * and draw() for custom game content.
 * 
 * @author removed for forum
 *
 */
public abstract class GameComponent extends Canvas implements Runnable {

  private static final long nanosecondsPerSecond = 1000000000L;
  private static final long millisecondsPerNanosecond = 1000000L;

  protected int frameRate; // how many times per second to (ideally) run the game loop
  private long timePerFrame; // nanoseconds per frame based on framerate

  protected Dimension resolution; // holds the width/height of game's canvas

  private Image drawImage;
  protected Graphics2D drawGraphics; // reference to drawImage's graphics

  protected Color backgroundColor = Color.BLACK;

  protected boolean running;

  // creates GameComponent with default resolution of 800x600 at 60 fps
  public GameComponent() {
	this(new Dimension(800,600), 60);
  }

  // creates GameComponent with passed resolution at 60 fps
  public GameComponent(Dimension resolution) {
	this(resolution, 60);
  }

  // creates GameComponent with passed resolution at passed fps
  public GameComponent(Dimension resolution, int frameRate) {
	this.resolution = resolution;
	this.frameRate = frameRate;

	timePerFrame = nanosecondsPerSecond / frameRate;
	setPreferredSize(resolution);
  }

  // final set up and starts game loop
  public void start() {
	createBufferStrategy(2); // double buffering
	running = true;
	new Thread(this).start(); // start game loop
  }

  @Override 
  public void run() {
	// game loop
	while(running) {
	  long timeBeforeLoop = System.nanoTime(); // time when game loop starts

	  update();
	  draw();
	  render();

	  try { 
		Thread.sleep(calculateSleepTime(timeBeforeLoop)); 
	  } catch (InterruptedException e) {}
	}
  }

  // update everything in the game, should be overridden
  public void update() {}

  // draw everything in the game, should be overridden
  public void draw() {
	if(drawImage == null) 
	  drawImage = createImage((int)resolution.getWidth(), (int)resolution.getHeight());

	drawGraphics = (Graphics2D) drawImage.getGraphics();
	drawGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

	drawGraphics.setColor(backgroundColor); 
	drawGraphics.fillRect(0, 0, getWidth(), getHeight()); // draw background
  }

  // render the graphics, buffer strategy uses volatile images, so deal with possible content loss
  private void render() {
	drawGraphics.dispose();
	BufferStrategy strategy = getBufferStrategy();
	do {
	  do {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.drawImage(drawImage, 0, 0, null);
		g.dispose();
	  } while (strategy.contentsRestored());

	  strategy.show(); // Display the buffer
	} while (strategy.contentsLost()); // Repeat the rendering if the drawing buffer was lost
	Toolkit.getDefaultToolkit().sync();
  }

  // figure out how long the thread should sleep to keep a consistent frame rate
  private long calculateSleepTime(long beforeLoop) {
	long afterLoop = System.nanoTime(); // get time after loop
	long difference = afterLoop - beforeLoop; // calculate the difference
	long timeToSleep = (timePerFrame - difference) / millisecondsPerNanosecond; // calculate time to sleep in milliseconds

	// if you see this in the console when running, it means the game loop isn't keeping up with frame rate
	if(timeToSleep < 0)
	  System.out.println("sleep time is < 0");

	return (timeToSleep > 0) ? timeToSleep : 0; // if timeToSleep is < 0, frame took longer than timePerFrame, so sleep time is 0
  }
}