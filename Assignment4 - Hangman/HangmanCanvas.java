/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import java.awt.Color;

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		drawScaffold();
		drawWordLabel();
		drawIncorrectLetterLabel();
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		label.setLabel(word);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter, int guessesLeft) {
		if(guessesLeft == 7){
			drawHead();
		}else if(guessesLeft == 6){
			drawBody();
		}else if(guessesLeft == 5){
			drawLeftArm();
		}else if(guessesLeft == 4){
			drawRightArm();
		}else if(guessesLeft == 3){
			drawLeftLeg();
		}else if(guessesLeft == 2){
			drawRightLeg();
		}else if(guessesLeft == 1){
			drawLeftFoot();
		}else if(guessesLeft == 0){
			drawRightFoot();
		}
		
		incorrectLetters += letter;
		mistakes.setLabel(incorrectLetters);
		
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	private static final int X_OFFSET = 56;
	private static final int Y_OFFSET = 50;
	private static final int LABEL_X_OFFSET = 40;
	private static final int LABEL_Y_OFFSET = 600;
	private static final int MISTAKES_Y_OFFSET = 50;
	private GLabel label;
	private GLabel mistakes;
	private String incorrectLetters = "";

	private void drawWordLabel(){
		label = new GLabel("");
		label.setFont("Helvetica-40");
		add(label, LABEL_X_OFFSET, LABEL_Y_OFFSET - label.getAscent());
	}
	
	private void drawIncorrectLetterLabel(){
		mistakes = new GLabel(incorrectLetters);
		mistakes.setFont("Helvetica-20");
		mistakes.setColor(Color.RED);
		add(mistakes, LABEL_X_OFFSET, getHeight() - MISTAKES_Y_OFFSET - mistakes.getDescent());
	}
	
	private void drawScaffold(){
		GLine scaffold = new GLine(X_OFFSET, Y_OFFSET, X_OFFSET, Y_OFFSET + SCAFFOLD_HEIGHT);
		GLine beam = new GLine(X_OFFSET, Y_OFFSET, X_OFFSET + BEAM_LENGTH, Y_OFFSET);
		GLine rope = new GLine(X_OFFSET + BEAM_LENGTH, Y_OFFSET, X_OFFSET + BEAM_LENGTH, Y_OFFSET + ROPE_LENGTH);
		add(scaffold);
		add(beam);
		add(rope);
	}
	
	
	private void drawHead(){
		double x = X_OFFSET + BEAM_LENGTH - HEAD_RADIUS;
		double y = Y_OFFSET + ROPE_LENGTH;
		GOval head = new GOval(x, y, 2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		add(head);
	}
	
	private void drawBody(){
		double x =  X_OFFSET + BEAM_LENGTH;
		double y1 = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS;
		double y2 = y1 + BODY_LENGTH;
		GLine body = new GLine(x, y1, x, y2);
		add(body);
	}
	
	private void drawLeftArm(){
		double x1 =  X_OFFSET + BEAM_LENGTH;
		double x2 =  X_OFFSET + BEAM_LENGTH - UPPER_ARM_LENGTH;
		double y = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
		GLine upperArm = new GLine(x1, y, x2, y);
		GLine lowerArm = new GLine(x2, y, x2, y + LOWER_ARM_LENGTH);
		add(upperArm);
		add(lowerArm);
	}
	
	private void drawRightArm(){
		double x1 =  X_OFFSET + BEAM_LENGTH;
		double x2 =  X_OFFSET + BEAM_LENGTH + UPPER_ARM_LENGTH;
		double y = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
		GLine upperArm = new GLine(x1, y, x2, y);
		GLine lowerArm = new GLine(x2, y, x2, y + LOWER_ARM_LENGTH);
		add(upperArm);
		add(lowerArm);
	}
	
	private void drawLeftLeg(){
		double x1 = getWidth() / 2;
		double x2 = x1 - HIP_WIDTH;
		double y = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH;
		GLine line1 = new GLine(x1, y, x2, y);
		add(line1);
		
		double x = x2;
		double y1 = y;
		double y2 = y1 + LEG_LENGTH;
		GLine line2 = new GLine(x, y1, x, y2);
		add(line2);
	}
	
	private void drawRightLeg(){
		double x1 = getWidth() / 2;
		double x2 = x1 + HIP_WIDTH;
		double y = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH;
		GLine line1 = new GLine(x1, y, x2, y);
		add(line1);
		
		double x = x2;
		double y1 = y;
		double y2 = y1 + LEG_LENGTH;
		GLine line2 = new GLine(x, y1, x, y2);
		add(line2);
	}
	
	private void drawLeftFoot(){
		double x1 = getWidth() / 2 - HIP_WIDTH;
		double x2 = x1 - FOOT_LENGTH;
		double y = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;
		GLine leftFoot = new GLine(x1, y, x2, y);
		add(leftFoot);
	}
	
	private void drawRightFoot(){
		double x1 = getWidth() / 2 + HIP_WIDTH;
		double x2 = x1 + FOOT_LENGTH;
		double y = Y_OFFSET + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;
		GLine rightFoot = new GLine(x1, y, x2, y);
		add(rightFoot);
	}
}