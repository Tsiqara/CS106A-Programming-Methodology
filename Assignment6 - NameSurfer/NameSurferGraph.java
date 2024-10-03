/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

/* NameSurferEntrys are stored here. */
	private ArrayList<NameSurferEntry> list = new ArrayList<>();
	
/** This variable shows how many pixels are proportional to one rank. 
 * It changes according to window size, so is updated every time update() method is called.*/
	private double coefficient = 0;
	
/** offset between vertical lines is equal to getWidth() / NDECADES.
 * It changes according to window size, so it's value changes every time update() method is called.*/
	private double offsetBetweenVerticalLines = 0;
	
	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class and updates display.
	*/
	public void clear() {
		list.removeAll(list);
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	*/
	public void addEntry(NameSurferEntry entry) {
		list.add(entry);
		update();
	}
	
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	* Updates values of coefficient and offsetBetweenVerticalLines.
	*/
	public void update() {
		removeAll();
		coefficient = (getHeight() - 2.0 * GRAPH_MARGIN_SIZE) / MAX_RANK;
		offsetBetweenVerticalLines = getWidth() / NDECADES;
		drawGrid();
		drawGraphics();
	}
	
	
/** This method draws graphic for every NameSurferEntry in the list.*/
	private void drawGraphics(){
		for(int i = 0; i < list.size(); i ++){
			drawGraph(list.get(i), getColor(i));
		}
	}
	
/** Colors of graph change in sequence: black, red, blue, yellow, black, red...
 * This method return which color graph should be.*/
	private Color getColor(int n){
		Color color;
		if(n % 4 == 0){
			color = Color.BLACK;
		}else if(n % 4 == 1){
			color = Color.RED;
		}else if(n % 4 == 2){
			color = Color.BLUE;
		}else{
			color = Color.YELLOW;
		}
		return color;
	}
	
/** This method draws graphic of given color for certain NameSurferEntry.
 * It draws lines connecting decades, labels with name and rank in each decade.*/
	private void drawGraph(NameSurferEntry entry, Color color){
		String name = entry.getName();
		String labelText;
		
		double x1 = 0;
		double x2 = x1 + offsetBetweenVerticalLines;
		double y1 = 0;
		double y2 = 0;
		
		for(int i = 0; i < NDECADES; i ++){	
			labelText = name + " " + entry.getRank(i);
			
			if(entry.getRank(i) == 0){
				y1 = getHeight() - GRAPH_MARGIN_SIZE;
				labelText = name +" *";
			}else{
				y1 = GRAPH_MARGIN_SIZE + coefficient * entry.getRank(i);				
			}
			
			if(i != NDECADES - 1){
				y2 = GRAPH_MARGIN_SIZE + coefficient * entry.getRank(i + 1);
				
				if(entry.getRank(i + 1) == 0){
					y2 = getHeight() - GRAPH_MARGIN_SIZE;
				}
				
				GLine line = new GLine(x1, y1, x2, y2);
				line.setColor(color);
				add(line);
			}
			
			GLabel label = new GLabel(labelText);
			label.setColor(color);
			add(label, x1, y1);
			
			x1 += offsetBetweenVerticalLines;
			x2 += offsetBetweenVerticalLines;
		}
			
	}
	
/** This method draws grid: horizontal lines, vertical lines and labels of decades.*/
	private void drawGrid(){
		drawHorizontalLines();
		drawVerticalLines();
		drawLabels();
	}
	
/** This method draws 2 horizontal lines GRAPH_MARGIN_SIZE pixels away from the top and bottom of window. */
	private void drawHorizontalLines(){
		GLine line1 = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		add(line1);
		
		GLine line2 = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(line2);
	}
	
/** This method draws vertical lines to separate decades.*/
	private void drawVerticalLines(){
		double x = 0;
		double y1 = 0;
		double y2 = getHeight();
		
		for(int i = 0; i < NDECADES; i ++){
			GLine line = new GLine(x, y1, x, y2);
			add(line);
			x += offsetBetweenVerticalLines;
		}
	}
	
/** This method draws labels at the bottom of window. one label per decade indicating which decade it is.*/
	private void drawLabels(){
		double offsetBetweenLabels = getWidth() / NDECADES;
		double Offset = 2;
		
		int decade = START_DECADE;
		double x = Offset;
		double y = getHeight() - Offset;

		for(int i = 0; i < NDECADES; i ++){
			String year = Integer.toString(decade);
			GLabel label = new GLabel(year);
			add(label, x, y);
			
			x += offsetBetweenLabels;
			decade += 10;
		}
	}
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
