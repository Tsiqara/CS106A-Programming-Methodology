/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HangmanLexicon {

	private ArrayList<String> list;
	
	// This is the HangmanLexicon constructor 
	public HangmanLexicon() { 
		try {
			BufferedReader reader = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			list = new ArrayList<String>();
			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				list.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		try{
			return list.size();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return 0;
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		try{
			return list.get(index - 1);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return "Word at such index does not exists";
	}
}
