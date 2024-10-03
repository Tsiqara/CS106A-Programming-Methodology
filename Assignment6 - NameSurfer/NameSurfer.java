/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/*  private instance variables */
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JButton graphButton;
	private JButton clearButton;
	
	private NameSurferGraph graph; 
	
	private NameSurferDataBase data; 
	
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
	    setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
	    
	    data = new NameSurferDataBase(NAMES_DATA_FILE);
	    graph = new NameSurferGraph(); 
	    add(graph);
	    
	    createUI();
	    addActionListeners();
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are clicked.
 * If graph button or Enter key is pressed we get name from text field, search such entry in database,
 * and if found graph is drawn.
 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == graphButton || e.getSource() == nameTextField){
			String name = nameTextField.getText();
			name = nameToAppropriateFormat(name);
			NameSurferEntry entry = data.findEntry(name);
			if(entry != null){
				graph.addEntry(data.findEntry(name));
			}
			
		}else if(e.getSource() == clearButton){
			graph.clear();
		}
		
		nameTextField.setText("");
	}
	
	
/** 
 * This method creates user interface. It places JLabel, JTextField and JButtons at the bottom of window.
 * */
	private void createUI(){
		nameLabel = new JLabel("Name");
		add(nameLabel, SOUTH);
		
		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		add(nameTextField, SOUTH);
		nameTextField.addActionListener(this);
		
		graphButton = new JButton("Graph");
		add(graphButton, SOUTH);
		
		clearButton = new JButton("Clear");
		add(clearButton, SOUTH);
	}
	
	
/** We are told that "Eric" and "ERIC" are the same entry, case is not important.
 * In file names are given in a certain way, so this method transforms entered name into appropriate format.
 * So that, for example "eric" is not incorrect as it would have been perceived 
 * if it was given directly to findEntry method.*/
	private String nameToAppropriateFormat(String name){
		if(name.length() > 0){
			name = name.toLowerCase();
			name = (char)(name.charAt(0) - ('a' - 'A')) + name.substring(1);
		}
		return name;
	}
}
