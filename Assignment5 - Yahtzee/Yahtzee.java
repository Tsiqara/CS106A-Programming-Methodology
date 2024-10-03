/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;
import java.util.List;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	/** This methods runs the program.
	 * 	Sets size of game window. Inputs number of players(not greater then max_players), their names(stores in array and displays on screen).*/
	public void run() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while(nPlayers > MAX_PLAYERS){
			dialog.print("Maximum number of players is " + MAX_PLAYERS + '\n');
			nPlayers = dialog.readInt("Enter number of players");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
		
	}

	/** This methods runs the yahtzee game.	
	 * We create matrix category x player and set every value to -1, so then we can check if category has already been used.
	 * 	Players play 13 rounds. In scores array we store total scores of each player and in the end find winner and print out relevant message.*/
	private void playGame() {
		int[][] categoryXPlayer = new int[N_CATEGORIES][nPlayers];
		setValuesToMinusOne(categoryXPlayer);
		for(int i = 0; i < 13; i ++){
			playRound(categoryXPlayer);
		}
		int[] scores = new int[nPlayers];
		for(int i = 0; i < scores.length; i ++){
			scores[i] = sumUpAndDisplayScores(i, categoryXPlayer);
		}
		int maxInd = getMaxIndex(scores);
		String winner = playerNames[maxInd];
		display.printMessage("Congratulations, " + winner + ", you're the winner with a total score of "
										+ scores[maxInd] + "!");
	}
	
	/** This method allows each player to play one round.*/
	private void playRound(int[][] categoryXPlayer){
		for(int i = 0; i < categoryXPlayer[0].length; i ++){
			playTurn(i, categoryXPlayer);
		}
	}
	
	/** This method is one player's one turn. Player should roll dice 3 times, then select category and get points for it. 
	 * All along the process relevant messages are printed out. At the end score board is updated.*/
	private void playTurn(int player, int[][] categoryXPlayer){
		display.printMessage(playerNames[player] + "'s turn! Click "+ '"' + "Role Dice" +
							'"' + " button to roll the dice.");
		display.waitForPlayerToClickRoll(player + 1);
		int[] dice = new int[N_DICE];
		dice = rollDice();
		display.displayDice(dice);
		for(int i = 0; i < 2; i ++){
			display.printMessage("Select the dice you wish to re-roll and click " + '"' + "Role Again" + '"');
			display.waitForPlayerToSelectDice();
			display.displayDice(rollDiceAgain(dice));
		}
		
		display.printMessage("Select a category for this roll.");
		int category = display.waitForPlayerToSelectCategory();
		while(categoryXPlayer[category][player] != -1){
			display.printMessage("This category has already been selected! Please, select another category.");
			category = display.waitForPlayerToSelectCategory();
		}
		boolean p = checkCategory(dice, category);
		int score = 0;
		if(p){
			score = getScore(category, dice);
		}
		categoryXPlayer[category][player] = score;
		display.updateScorecard(category, player + 1, score);
	}
	
	/** This method sums up and displays total score of player. If upperScore is unless 63 player gets bonus 35 points.*/
	private int sumUpAndDisplayScores(int player, int[][] categoryXPlayer){
		int sum = 0;
		int upperScore = sumUpAndDisplayUpperScore(player, categoryXPlayer);
		sum += upperScore;
		if(upperScore >= 63){
			sum += upperBonus;
			display.updateScorecard(UPPER_BONUS, player + 1, upperBonus);
		}else{
			display.updateScorecard(UPPER_BONUS, player + 1, 0);
		}
		int lowerScore = sumUpAndDisplayLowerScore(player, categoryXPlayer);
		sum += lowerScore;
		display.updateScorecard(TOTAL, player + 1, sum);
		return sum;
	}
	
	/** This method sums up and displays upper score of player.*/
	private int sumUpAndDisplayUpperScore(int player, int[][] categoryXPlayer){
		int upperScore = 0;
		for(int i = 0; i < UPPER_SCORE; i ++){
			upperScore += categoryXPlayer[i][player];
		}
		display.updateScorecard(UPPER_SCORE, player + 1, upperScore);
		return upperScore;
	}
	
	/** This method sums up and displays lower score of player.*/
	private int sumUpAndDisplayLowerScore(int player, int[][] categoryXPlayer){
		int lowerScore = 0;
		for(int i = THREE_OF_A_KIND; i < TOTAL; i ++){
			lowerScore += categoryXPlayer[i][player];
		}
		display.updateScorecard(LOWER_SCORE, player + 1, lowerScore);
		return lowerScore;
	}
	
	/** This method simulates rolling of dice. For each dice integer in range of [1, 6]  is generated randomly.*/
	private int[] rollDice(){
		int[] dice = new int[N_DICE];
		for(int i = 0; i < dice.length; i ++){
			dice[i] = rgen.nextInt(1, 6);
		}
		return dice;
	}
		
	/** This method rolls selected dices again. Not selected dices stay the same.*/
	private int[] rollDiceAgain(int[] dice){
		for(int i = 0; i < dice.length; i ++){
			if(display.isDieSelected(i)){
				dice[i] = rgen.nextInt(1, 6);
			}
		}
		return dice;
	}
	
	/** If category's criteria are satisfied player gets points according to category.
	 * I call this method only when checkCategory is true, so default cases are only : ONES, TWOS, THREES, FOURS, FIVES and SIXES.
	 * category will not be nonscoring. */
	private int getScore(int category, int[] dice){
		int score = 0;
		switch(category){
			case THREE_OF_A_KIND : 
				score = sumOfDices(dice); 
				break;
			case FOUR_OF_A_KIND : 
				score = sumOfDices(dice);
				break;
			case FULL_HOUSE :
				score = 25;
				break;
			case SMALL_STRAIGHT:
				score = 30;
				break;
			case LARGE_STRAIGHT:
				score = 40;
				break;
			case YAHTZEE:
				score = 50;
				break;
			case CHANCE:
				score = sumOfDices(dice);
				break;
			default:
				score = sumOfNums(dice, category);
				break;
					
		}
		return score;
	}
	
	/** This method counts sum of numbers on every dice.*/
	private int sumOfDices(int[] dice){
		int sum = 0;
		for(int i = 0; i < dice.length; i ++){
			sum += dice[i];
		}
		return sum;
	}
	
	/** This method counts sum of ones, twos, threes, fours, fives or sixes on dices, based on category.*/
	private int sumOfNums(int[] dice, int category){
		int sum = 0;
		for(int i = 0; i < dice.length; i ++){
			if(dice[i] == category){
				sum += dice[i];
			}
		}
		
		return sum;
	}
	
	/** This method sets value of each element of the matrix to -1.*/
	private void setValuesToMinusOne(int[][] categoryXPlayer){
		for(int r = 0; r < categoryXPlayer.length; r ++){
			for(int c = 0; c < categoryXPlayer[0].length; c ++){
				categoryXPlayer[r][c] = -1;
			}
		}
	}
	
	/** This method return index of maximum value of array.*/
	private int getMaxIndex(int[] scores){
		int maxInd = 0;
		for(int i = 1; i < scores.length; i ++){
			if(scores[i] > scores[maxInd]){
				maxInd = i;
			}
		}
		return maxInd;
	}
	
	/** This method checks if player' combination matches requirements of selected category. */
	private boolean checkCategory(int[] dice, int category){
		if((category >= ONES && category <= SIXES) || (category == CHANCE)){
			return true;
		}
		if(category == THREE_OF_A_KIND){
			return checkIfThreeOfAKind(dice);
		}
		if(category == FOUR_OF_A_KIND){
			return checkIfFourOfAKind(dice);
		}
		if(category == FULL_HOUSE){
			return checkIfFullHouse(dice);
		}
		if(category == SMALL_STRAIGHT){
			return checkIfSmallStraight(dice);
		}
		if(category == LARGE_STRAIGHT){
			return checkIfLargeStraight(dice);
		}
		if(category == YAHTZEE){
			return checkIfYahtzee(dice);
		}
		return false;
	}
	
	/** This method checks if combination of our dices is threeOfAKind.
	 *  For each index i I add to arrayList indexes j on which dice[i] == dice[j]
	 *  If for any of them size of such list is unless 3, player has got three of a kind.*/
	private boolean checkIfThreeOfAKind(int[] dice){
		for(int i = 0; i < dice.length; i ++){
			List<Integer> list = new ArrayList<>();
			for(int j = i; j < dice.length; j ++){
				if(dice[i] == dice[j]){
					list.add(j);
				}
			}
			if(list.size() >= 3){
				return true;
			}
		}
		
		return false;
	}
	
	
	/** This method checks if player's combination of dices is four of a kind.
	 * Similar, to checkIfThreeOfAKind, for each index i I create ArrayList and add indexes j. on which
	 * dice[i] == dice[j]. If size of any such ArrayList is unless 4, player has got four of a kind.*/
	private boolean checkIfFourOfAKind(int[] dice){
		for(int i = 0; i < dice.length; i ++){
			List<Integer> list = new ArrayList<>();
			for(int j = i; j < dice.length; j ++){
				if(dice[i] == dice[j]){
					list.add(j);
				}
			}
			if(list.size() >= 4){
				return true;
			}
		}
		return false;
	}
	
	/** This method checks if values of each dice are equal, so checks if player has Yahtzee.*/
	private boolean checkIfYahtzee(int[] dice){
		for(int i = 1; i < dice.length; i ++){
			if(dice[i] != dice[0]){
				return false;
			}
		}
		return true;
	}
	
	/** This method checks if player's combination of dices is Full House.
	 * If player has yahtzee it mean he/she has full house too.
	 * If player does not have yahtzee but has four of a kind, he/she cannot have full house.
	 * If player does not have even three of a kind it is impossible to have full house.
	 * So if answer has not been returned yet, it means player has exactly 3 equal dices,
	 * so we have to check if the other two are equal to each other.*/
	private boolean checkIfFullHouse(int[] dice){
		if(checkIfYahtzee(dice)){
			return true;
		}
		if((checkIfFourOfAKind(dice)) || !checkIfThreeOfAKind(dice)){
			return false;
		}
		
		for(int i = 0; i < dice.length; i ++){
			List<Integer> list1 = new ArrayList<>();
			List<Integer> list2 = new ArrayList<>();
			for(int j = i; j < dice.length; j ++){
				if(dice[i] == dice[j]){
					list1.add(j);
				}else{
					list2.add(j);
				}
			}
			if(dice[list2.get(0)] == dice[list2.get(1)]){
				return true;
			}
		}
		
		return false;
	}
	
	/** This method checks if player's combination of dices is small straight.
	 * I create an ArrayList to which are added different numbers from dice[].( for example {1,2,2,3,1} -> [1,2,3]
	 * This ArrayList is sorted from minimum to maximum values.
	 * If size of list <4 combination of dices cannot be small straight.
	 * If size is exactly 4 , for combination to be small straight, every element of list starting from index = 1, 
	 * should be list.get(i - 1) +1.
	 * If size >4 so =5, small straight can be from index 0 to index 3 or from index 1 to index 4.*/
	private boolean checkIfSmallStraight(int[] dice){
		List<Integer> list = listOfDifferentNumbers(dice);
		sortArrayList(list);
		
		if(list.size() < 4){
			return false;
		}
		
		if(list.size() == 4){
			for(int i = 0; i < list.size() - 1; i ++){
				if(list.get(i+1) != list.get(i) + 1){
					return false;
				}
			}
			return true;
		}
		
		boolean ans = true;
		for(int i = 0; i < 3; i ++){
			if(list.get(i+1) != list.get(i) + 1){
				ans = false;
			}
		}
		if(ans){
			return ans;
		}
		ans = true;
		for(int i = 1; i < 4; i ++){
			if(list.get(i+1) != list.get(i) + 1){
				ans = false;
			}
		}
		
		return ans;
	}
	
	/** This method sorts List(ArrayList). ( minimum --> maximum)*/
	private void sortArrayList(List<Integer> list){
		int t = 0;
		for(int i = 0; i < list.size(); i ++){
			for(int j = i + 1; j < list.size(); j ++){
				if(list.get(j) < list.get(i)){
					t = list.get(i);
					list.set(i, list.get(j));
					list.set(j, t);
				}
			}
		}
	}
	
	/** This method picks out different numbers from array
	 * (values in the list are unique, we only add elements that have not been added before).*/
	private List<Integer> listOfDifferentNumbers(int[] dice){
		List<Integer> list = new ArrayList<>();
		list.add(dice[0]);
		boolean hasBeen = false;
		for(int i = 1; i < dice.length; i ++){
			hasBeen = false;
			for(int j = 0; j < list.size(); j ++){
				if(dice[i] == list.get(j)){
					hasBeen = true;
				}
			}
			if(!hasBeen){
				list.add(dice[i]);
			}
		}
		
		return list;
	}
	
	
	/** This method checks if player's combination of dices is large straight.
	 * Similar, to checking small straight.
	 * We create list of unique values on dices.
	 * If size of list <5, combination cannot be large straight.
	 * If size equals 5, for combination to be large straight,value on each index of list should be
	 * value on previous index + 1. */
	private boolean checkIfLargeStraight(int[] dice){
		List<Integer> list = listOfDifferentNumbers(dice);
		sortArrayList(list);
		
		if(list.size() < 5){
			return false;
		}
		
		for(int i = 0; i < list.size() - 1; i ++){
			if(list.get(i + 1) != list.get(i) + 1){
				return false;
			}
		}
		return true;
	}
	
	
/* Private instance variables */
	private int nPlayers;
	private static final int upperBonus = 35;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
