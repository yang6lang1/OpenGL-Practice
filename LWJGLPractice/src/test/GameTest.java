package test;


import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.JFrame;

public class GameTest extends GameComponent {
	private static final int NUM_CIRCLES = 5;
	private static final Circle[] circles = new Circle[NUM_CIRCLES];

	private static Random random = new Random();

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		GameComponent game = new GameTest();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		game.start();
	}

	public GameTest() {
		//		Thread timerAccuracyThread = new Thread(new Runnable() {
		//			public void run() {
		//				while (true) {
		//					try {
		//						Thread.sleep(Long.MAX_VALUE);
		//					} catch (Exception e) {
		//					}
		//				}
		//			}
		//		});
		//		timerAccuracyThread.setDaemon(true);
		//		timerAccuracyThread.start();

		//		new Thread() {
		//			{ 
		//				setDaemon(true); 
		//				start(); 
		//			}
		//			public void run() {
		//				while(true) {
		//					try {
		//						Thread.sleep(Integer.MAX_VALUE);
		//					}
		//					catch(Throwable t){
		//
		//					} 
		//				} 
		//			}
		//		};

		// create some random circles
		for(int i=0; i<circles.length; i++) {
			//Color color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256),random.nextInt(256));
		  	Color color = new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), 125);
			int radius = random.nextInt(100) + 30;
			int x = random.nextInt(800-radius);
			int y = random.nextInt(600-radius);
			circles[i] = new Circle(x, y, radius, color);
		}
	}

	@Override public void update() {
		super.update();

		for(Circle circle : circles)
			circle.update();
	}

	@Override public void draw() {
		super.draw();

		for(Circle circle : circles)
			circle.draw();
	}

	class Circle {
		private int x, y, width, height, xVelocity, yVelocity;
		private Color color;

		public Circle(int x, int y, int radius, Color color) {
			this.x = x;
			this.y = y;
			width = height = radius;
			this.color = color;
			xVelocity = random.nextInt(6) + 4;
			//yVelocity = random.nextInt(6) + 4;
		}

		public void update() {
			if(x < 0 || x > getWidth()-width) // check left/right bounds
				xVelocity *= -1; // reverse horizontal velocity
			if(y < 0 || y > getHeight()-height) // check top/bottom bounds
				yVelocity *= -1; // reverse vertical velocity

			x += xVelocity;
			y += yVelocity;
		}

		public void draw() {
			drawGraphics.setColor(color);
			Shape circle = new Ellipse2D.Float(x, y, width, height);
			drawGraphics.fill(circle);
		}
	}
}