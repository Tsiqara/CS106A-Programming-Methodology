/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	/** private instance variables */
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JButton addButton;
	private JButton deleteButton;
	private JButton lookupButton;
	private JTextField statusTextField;
	private JButton changeStatusButton;
	private JTextField pictureTextField;
	private JButton changePictureButton;
	private JTextField friendTextField;
	private JButton addFriendButton;
	
	private FacePamphletDatabase dataBase;
	private FacePamphletProfile currentProfile;
	private FacePamphletCanvas canvas;
	
	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		
		dataBase = new FacePamphletDatabase();
		
		canvas = new FacePamphletCanvas(); 
		add(canvas);
		
		createUI();
		addActionListeners();
    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used.
     */
    public void actionPerformed(ActionEvent e) {
    	String name = nameTextField.getText();
		if(!name.equals("")){
			if(e.getSource() == addButton){
				addProfile(name);
			}else if(e.getSource() == deleteButton){
				deleteProfile(name);
			}else if(e.getSource() == lookupButton){
				lookupProfile(name);
			}
			
			nameTextField.setText("");
		}
		
		if(e.getSource() == changeStatusButton || e.getSource() == statusTextField){
			String status = statusTextField.getText();
			if(!status.equals("")){
				updateStatus(status);
			}
			
		}else if(e.getSource() == changePictureButton || e.getSource() == pictureTextField){
			String filename = pictureTextField.getText();
			if(!filename.equals("")){
				updatePicture(filename);
			}
		}else if(e.getSource() == addFriendButton || e.getSource() == friendTextField){
			String friend = friendTextField.getText();
			if(!friend.equals("")){
				addFriend(friend);
			}
		}
	}
    
    /**  This method adds profile with given name to data base. 
     * If profile with this name already exists in data base, we display profile and appropriate message on screen.
     * If profile with this name does not exist in data base, we create new profile with given name, 
     * add it to data base and then display new profile and appropriate message on screen. 
     * currentProfile becomes profile with this name.
     *  */
    private void addProfile(String name){
    	if(dataBase.containsProfile(name)){
			currentProfile = dataBase.getProfile(name);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("A profile with the name " + name + " already exists");
		}else{
			currentProfile = new FacePamphletProfile(name);
			dataBase.addProfile(currentProfile);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("New profile created");
		}
    }
    
    /**  This method deletes profile with this name from data base. currentProfile becomes null.
     *  If data base contains profile with this name, we delete it from data base, clear canvas and display message
     *  that profile has been deleted. 
     *  If profile with this name was not in the data base we just show appropriate message.
     * 
     * */
    private void deleteProfile(String name){
    	currentProfile = null;
		if(dataBase.containsProfile(name)){
			dataBase.deleteProfile(name);
			canvas.removeAll();
			canvas.showMessage("Profile of " + name + " deleted");
		}else{
			canvas.showMessage("A profile with the name " + name + " does not exist");
		}
    }
    
    
    /** This method searches for profile with this name in the data base.
     * If data base contains such profile it becomes current profile and is displayed on canvas with relevant message.
     * If there is not such profile in the data base, current profile becomes null, everything is removed from canvas
     * and message is shown that such profile does not exist.
     * */
    private void lookupProfile(String name){
    	if(dataBase.containsProfile(name)){
			currentProfile = dataBase.getProfile(name);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Displaying " + name);
		}else{
			currentProfile = null;
			canvas.removeAll();
			canvas.showMessage("A profile with the name " + name + " does not exist");
		}
    }
    
    /** This method updates status of current profile.
     * If current profile is null, message is displayed that profile must be selected to update its status.
     * Else status of current profile is updated, profile is displayed on canvas again so that changed status is displayed.
     * and appropriate message is shown.
     * text of statusTextField is set to ""  */
    private void updateStatus(String status){
    	if(currentProfile != null){
			currentProfile.setStatus(status);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Status updated to " + status);
		}else{
			canvas.showMessage("Please select a profile to change status");
		}
		statusTextField.setText("");
    }
    
    
    /** If current profile is null, message is displayed that profile must be selected to update picture.
     * Else we try to update picture. If given filename is correct profile picture of current profile is updated,
     * profile is redisplayed to show new picture and message is displayed that picture was updated.
     * If filename is not correct, message is shown that we were unable to open image file. 
     * text of pictureTextField is set to "". */
    private void updatePicture(String filename){
    	if(currentProfile != null){
			GImage image = null; 
			try { 
				image = new GImage(filename);
				currentProfile.setImage(image);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Picture updated");
			} catch (ErrorException ex) { 
				canvas.showMessage("Unable to open image file: " + filename);
			}
		}else{
			canvas.showMessage("Please select a profile to change picture.");
		}
    	pictureTextField.setText("");
    }
    
    
    /** If current profile is null, message is displayed that profile must be selected to add friend.
     * Else if data base does not contain profile with given friend name, message is shown that such profile does not exist.
     * if given friend name equals current profile name, message is displayed that you cannot add yourself as friend.
     * Else  if currentProfile.addFriend(friend) is true, means that friend was not friend of current profile and 
     * now was added as friend. So current profile is displayed on canvas, message is shown and current profile is added
     * to list of friend's friend. if currentProfile.addFriend(friend) is false, 
     * means profile with name friend and current profile were already friend and appropriate message is displayed.
     * 
     * text of friendTextField is set to "".
     * */
    private void addFriend(String friend){
    	if(currentProfile != null){
    		if(!dataBase.containsProfile(friend)){
    			canvas.showMessage(friend + " does not exist");
    		}else if(friend.equals(currentProfile.getName())){
    			canvas.showMessage("You cannot add yourself as your friend");
    		}else{
				if(currentProfile.addFriend(friend)){
					canvas.displayProfile(currentProfile);
					canvas.showMessage(friend + " added as friend");
					dataBase.getProfile(friend).addFriend(currentProfile.getName());
    			}else{
    				canvas.showMessage(currentProfile.getName() + " already has " + friend + " as a friend.");
    			}
			}
    	}else{
    		canvas.showMessage("Please select a profile to add friend");
    	}
		friendTextField.setText("");
    }
    
 
    /** This method creates User Interface. It adds necessary JLabels, JTextFields and JButtons.
     * Also adds actionListeners of TextFields.  */
    private void createUI(){
    	nameLabel = new JLabel("Name");
    	add(nameLabel, NORTH);
    	
    	nameTextField = new JTextField(TEXT_FIELD_SIZE);
    	add(nameTextField, NORTH);
    	
    	addButton = new JButton("Add");
    	add(addButton, NORTH);
    	
    	deleteButton = new JButton("Delete");
    	add(deleteButton, NORTH);
    	
    	lookupButton = new JButton("Lookup");
    	add(lookupButton, NORTH);
    	
    	
    	statusTextField = new JTextField(TEXT_FIELD_SIZE);
    	add(statusTextField, WEST);
    	statusTextField.addActionListener(this);
    	
    	changeStatusButton = new JButton("Change Status");
    	add(changeStatusButton, WEST);
    	
    	add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
    	
    	
    	pictureTextField = new JTextField(TEXT_FIELD_SIZE);
    	add(pictureTextField, WEST);
    	pictureTextField.addActionListener(this);
    	
    	changePictureButton = new JButton("Change Picture");
    	add(changePictureButton, WEST);
    	
    	add(new JLabel(EMPTY_LABEL_TEXT), WEST);
    	
    	friendTextField = new JTextField(TEXT_FIELD_SIZE);
    	add(friendTextField, WEST);
    	friendTextField.addActionListener(this);
    	
    	addFriendButton = new JButton("Add Friend");
    	add(addFriendButton, WEST);
 
    	
    	add(new JLabel(EMPTY_LABEL_TEXT), WEST);
    }

}
