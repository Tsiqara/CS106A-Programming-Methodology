/*
 * File: Breakout.java
 * -------------------
 * Name: Mariam Tsikarishvili
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	/* Private instance variable*/
	private GRect paddle;
	
/** Minimum offset of paddle from right and left walls */
	private static final int  PADDLE_X_OFFSET = 5;
	
/** The number of milliseconds to pause on each animation cycle */
	private static final int PAUSE_TIME = 15;

	/* Private instance variable*/
	private GOval ball;
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Private instance variable*/
	private GLabel click_label;
	
/** X and Y Velocities of ball */
	private double vx = rgen.nextDouble(1.0, 3.0);
	private double vy = 3;
	
	
/**  Initializes Breakout program. */	
	public void init(){
		drawBricks();
		drawPaddle();
		drawClickLabel();
		// Waits until mouse is clicked to start the game, click_label is removed and ball is drawn.
		waitForClick();
		remove(click_label);
		drawBall();
	}

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		addMouseListeners();
		if(rgen.nextBoolean()){
			vx = -vx;
		}
		int numLives = NTURNS;
		int numBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
		
		while(true){
			numLives = checkCollisionWithWalls(numLives);
			moveBall();
			numBricks = checkCollision(numBricks);
			if(gameEnded(numLives, numBricks)){
				break;
			}
			
		}
	
	}
	
	
/** This method moves ball. */
	private void moveBall(){
		ball.move(vx, vy);
		pause(PAUSE_TIME);
			
	}
	
/** Draws click_label in the center of canvas. */
	private void drawClickLabel(){
		click_label = new GLabel("Click to start game ");
		click_label.setFont("Helvetica-20");
		double x = (WIDTH - click_label.getWidth()) / 2;
		double y = (HEIGHT + click_label.getAscent()) / 2;
		add(click_label, x, y);
		
	}
	
	
/** Checks if ball collided with the brick and if so reduces the total number of bricks on canvas. */
	private int checkCollision(int numberOfBricks){
		GObject collider = getCollidingObject();
		if(collider != null){
			vy = - vy;
			if(collider != paddle){
				remove(collider);
				numberOfBricks --;
				
			}else{
				ball.setLocation(ball.getX() ,paddle.getY() - 2 * BALL_RADIUS);
			}
		}
		return numberOfBricks;	
	}
	
/**  Checks if ball collided with any object on canvas. */
	private GObject getCollidingObject(){
		if(getElementAt(ball.getX(), ball.getY()) != null){
			return getElementAt(ball.getX(), ball.getY());
		}
		if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null){
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		}
		if(getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null){
			return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		}
		if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != null){
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		}
		
		return null;
	}
	
/** Checks if game ended and prints out relevant message. 
 * Game ended if player has no lives left and he/she loses of player wins if he/she breaks all bricks. */
	private boolean gameEnded(int numLives, int numberOfBricks){
		if(numLives == 0){
			removeAllAndDrawLabel("GAME OVER!", "Helvetica-40", Color.RED);
			return true;
		}
		if(numberOfBricks == 0){
			removeAllAndDrawLabel("CONGRATULATIONS! YOU WON!!!", "Helvetica-20", Color.GREEN);
			return true;
		}
		
		return false;
	}
	
/** Removes all objects from canvas, sets background color to red in case of lose and color green in case of win. 
 * 	It also creates and adds to canvas label with information for player( he/she lost or won). */
	private void removeAllAndDrawLabel(String text, String font, Color color){
		removeAll();
		setBackground(color);
		GLabel label = new GLabel(text);
		label.setFont(font);
		double x = (WIDTH - label.getWidth()) / 2;
		double y = (HEIGHT + label.getAscent()) / 2;
		add(label, x, y);
	}
	
/** Checks collision  with walls. If ball collided with left or right wall we change vx to -vx.
 *  If collided with top wall we change vy to -vy.
 * If ball collided with bottom wall player loses 1 life and ball starts again from center(number of bricks is unchanged).*/
	private int checkCollisionWithWalls(int numberOfLives){
		if(ball.getX() + ball.getWidth() + vx >= WIDTH){
			ball.setLocation(WIDTH - 2 * BALL_RADIUS, ball.getY());
			pause(PAUSE_TIME);
			vx = -vx;
		}
		if(ball.getX() + vx <= 0){
			ball.setLocation(0, ball.getY());
			pause(PAUSE_TIME);
			vx = -vx;
		}
		if(ball.getY() + vy <= 0){
			ball.setLocation(ball.getX(), 0);
			pause(PAUSE_TIME);
			vy = -vy;
		}
		
		if(ball.getY() + ball.getHeight() + vy >= HEIGHT){
			numberOfLives --;
			ball.setLocation(WIDTH / 2.0 - BALL_RADIUS, HEIGHT / 2.0 - BALL_RADIUS);
			pause(2 *PAUSE_TIME);
		}
		return numberOfLives;
	}
	
/** Creates ball and places it in the center of canvas. */
	private void drawBall(){
		ball = new GOval(WIDTH / 2.0 - BALL_RADIUS, HEIGHT / 2.0 - BALL_RADIUS, 
								2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		ball.setFillColor(Color.BLACK);
		add(ball);
	}
	
/** Moves paddle horizontally according to mouse's moves. Also controls paddle not to cross borders. */
	public void mouseMoved(MouseEvent e){
		if(e.getX() + PADDLE_WIDTH <= WIDTH && e.getX() >= 0){
			paddle.move(e.getX() - paddle.getX(), 0);
		}
		else if(e.getX() + PADDLE_WIDTH > WIDTH){
			paddle.setLocation(WIDTH - PADDLE_WIDTH - PADDLE_X_OFFSET, paddle.getY());
		}else{
			paddle.setLocation(PADDLE_X_OFFSET, paddle.getY());
		}
	}
	
/** Draws black paddle in the middle at PADDLE_Y_OFFSET. */ 
	private void drawPaddle(){
		paddle = new GRect((WIDTH - PADDLE_WIDTH) / 2.0, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT, 
												PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		paddle.setColor(Color.BLACK);
		add(paddle);
	}
	
/** Draws all bricks one row at a time. */
	private void drawBricks(){
		double x =  (WIDTH - (BRICK_WIDTH * NBRICKS_PER_ROW + BRICK_SEP * (NBRICKS_PER_ROW - 1))) / 2;
		double y = BRICK_Y_OFFSET;
		for(int i = 0; i < NBRICK_ROWS; i ++){
			Color color = getRowColor(i);
			drawBrickRow(x, y, color);
			y += (BRICK_HEIGHT + BRICK_SEP);
		
		}
		
	}
	
/**  Draws row of bricks. */
	private void drawBrickRow(double x, double y, Color color){
		for(int i = 0; i < NBRICKS_PER_ROW; i ++){
			GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
			brick.setFillColor(color);
			brick.setColor(color);
			add(brick);
			x += (BRICK_WIDTH + BRICK_SEP);
		}
	}
	
/** Returns what color the row should be. */
	private Color getRowColor(int n){
		if(n % 10 == 0 || n % 10 == 1){
			return Color.RED;
		}
		if(n % 10 == 2 || n % 10 == 3){
			return Color.ORANGE;
		}
		if(n % 10 == 4 || n % 10 == 5){
			return Color.YELLOW;
		}
		if(n % 10 == 6 || n % 10 == 7){
			return Color.GREEN;
		}
			return Color.CYAN;
	}

}
