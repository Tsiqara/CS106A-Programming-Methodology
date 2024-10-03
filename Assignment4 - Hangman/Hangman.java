/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.io.*;
import java.awt.*;

public class Hangman extends ConsoleProgram {
	
	//Instance variables
	private HangmanCanvas canvas;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private String word = "";
	private String currentWord = "";
	private int numberOfGuessesLeft = 8;
	
	
	/**  This method initializes the game. word is randomly chosen from HangmanLexicon.txt,
	 *   currentWord is created with as many hyphens as size of word.
	 *   We set size and add canvas to console. */
	public void init() {
		HangmanLexicon lexicon = new HangmanLexicon();
		int n = rgen.nextInt(lexicon.getWordCount());
		word = lexicon.getWord(n);
		for(int i = 0; i < word.length(); i ++){
			currentWord += "-";
		}
		setSize(800,800);
		canvas = new HangmanCanvas(); 
		add(canvas); 
	}

	/** This method runs the program - calls playHangman() method. canvas is reseted before every game. */
    public void run() {
    	canvas.reset();
		println("Welcome to Hangman!");
		playHangman();

	}

    /* This method represents whole process of game. Until end of the game we printout current state 
     * and display currentWord on canvas, require guess from player, check if it is appropriate, if so,
     *	check if it is correct. */
    private void playHangman(){
    	while(true){
    		printCurrentState();
    		canvas.displayWord(currentWord);
			String guess = readLine("Your guess: ");
			
			if(!inappropriateInput(guess)){
				char ch = guess.charAt(0);
				checkGuess(ch);
			}
			
			if(playerWon() || playerLost()){
				break;
			}
		}
    	
    }

    /* Checks if player's input was inappropriate and if so prints out relevant message */
    private boolean inappropriateInput(String str){
    	if(str.length() != 1){
			println("Inappropriate input! You should enter 1 letter.");
			return true;
		}else{
			char ch = str.charAt(0);
			if(!isLetter(ch)){
				println("You should enter letter!");
				return true;
			}
		}
    	
    	return false;
			
    }
    
    /*  If currentWord is same as word, player won the game, this method prints out relevant message. */
    private boolean playerWon(){
    	if(currentWord.equals(word)){
			println("You guessed the word: " + word);
			println("You win.");
			canvas.displayWord(currentWord);
			return true;
		}
    	return false;
    }
    
    /*  If player has no more guesses left, he lost,so this method prints out relevant message
     *  and word that player was trying to guess. */
    private boolean playerLost(){
    	if(numberOfGuessesLeft == 0){
			println("You're completely hung.");
			println("The word was: " + word);
			println("You lose.");
			return true;
		}
    	
    	return false;
    }
    
    /**  This method checks and prints out if player's guess was correct. 
     *   If guess was incorrect numberOfGuessesLeft is reduced, next body part appears on scaffold 
     *   and incorrect letter is displayed red at the bottom of the window.*/
    private void checkGuess(char ch){
    	ch = toUpperCase(ch); // It does not matter if inputed letter was lowerCase or upperCase
		if(wordContainsGuess(word, ch)){
			println("That guess is correct.");
			currentWord = fillCurrentWord(currentWord, ch);
		}else{
			println("There are no " + ch +"'s in the word.");
			numberOfGuessesLeft --;
			canvas.noteIncorrectGuess(ch, numberOfGuessesLeft);
		}
    }
    
    /*Prints out what letters have been guessed so far(unguessed letters are indicated by hyphens) 
    and how many guesses player has left.. */
    private void printCurrentState(){
    	println("The word now looks like this: " + currentWord);
		println("You have " + numberOfGuessesLeft + " guesses left.");
    }
    
    /* This method replaces hyphens with guessed letter in currentWord at all places where word contains it.*/
    private String fillCurrentWord(String str,char ch){
    	int startIndex = 0;
		while(true){
			int ind = word.indexOf(ch, startIndex);
			if(ind == -1){
				break;
			}
			str = str.substring(0, ind) + ch + str.substring(ind + 1);
			startIndex = ind + 1; 
		}
		return str;
    }
    
    // This method return if word contains player's guess at least once.
    private boolean wordContainsGuess(String str, char ch){
    	return str.indexOf(ch) != -1;
    }
    
    // This method turns lowerCase letters to upperCase
    private char toUpperCase(char ch){
    	if(ch >= 'a' && ch <= 'z'){
    		ch -= ('a' - 'A');
    	}
    	return ch;
    }
    
    // This method checks if character is English alphabet letter.
	private boolean isLetter(char ch) {
		if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
			return true;
		}
		return false;
	}

}
