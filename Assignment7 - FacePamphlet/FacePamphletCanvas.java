/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/** private instance variables */
	private GLabel messageLabel;
	private GLabel nameLabel;
	private GObject picture;
	private GLabel statusLabel;
	private GLabel friendsLabel;
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		messageLabel = new GLabel("");
		messageLabel.setFont(MESSAGE_FONT);
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		messageLabel.setLabel(msg);
		add(messageLabel, (getWidth() - messageLabel.getWidth()) / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);	
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		displayName(profile);
		displayPicture(profile);
		displayStatus(profile);
		displayFriends(profile);
	}
	
	/** This methods displays profile's name on canvas at the coordinates demanded by instructions.*/
	private void displayName(FacePamphletProfile profile) {
		String name = profile.getName();
		nameLabel = new GLabel(name);
		nameLabel.setColor(Color.BLUE);
		nameLabel.setFont(PROFILE_NAME_FONT);
		add(nameLabel, LEFT_MARGIN, TOP_MARGIN + messageLabel.getAscent());
	
	}

	
	/** This methods displays picture on canvas.
	 * If profile does not have profile picture, GRect is drawn with message "No Image" in the center of it.
	 * If profile has picture, it is displayed.*/
	private void displayPicture(FacePamphletProfile profile) {
		if(profile.getImage() == null){
			picture = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(picture, LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN);
			
			GLabel pictureLabel = new GLabel("No Image");
			pictureLabel.setFont(PROFILE_IMAGE_FONT);
			double x = LEFT_MARGIN +(picture.getWidth() - pictureLabel.getWidth()) / 2;
			double y = picture.getY() + (picture.getHeight() - pictureLabel.getHeight()) / 2 
					+ pictureLabel.getAscent();
			add(pictureLabel, x, y);
		}else{
			picture = profile.getImage();
			((GImage) picture).setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(picture, LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN);
		}
		
	}

	/** This method displays status on canvas.
	 * If profile does not have status, message "No current status" is displayed.
	 * If profile has status it is displayed below picture(distance between bottom of picture 
	 * and top of status label them STATUS_MARGIN) 
	 * */
	private void displayStatus(FacePamphletProfile profile) {
		String str = "";
		if(profile.getStatus().equals("")){
			str = "No current status";
		}else{
			str = profile.getName() + " is " + profile.getStatus();
		}
		
		statusLabel = new GLabel(str);
		statusLabel.setFont(PROFILE_STATUS_FONT);
		add(statusLabel, LEFT_MARGIN , picture.getY() + picture.getHeight() + STATUS_MARGIN + statusLabel.getAscent());
		
	}

	/** This method displays friends.
	 * At top label "Friends: and then list of profiles friends. */ 
	private void displayFriends(FacePamphletProfile profile) {
		double x = getWidth() / 2;
		double y = TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN;
		
		friendsLabel = new GLabel("Friends: ");
		friendsLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friendsLabel, x, y);
		
		
		Iterator<String> it = profile.getFriends();
		while(it.hasNext()){
			String friend = it.next();
			GLabel label = new GLabel(friend);
			label.setFont(PROFILE_FRIEND_FONT);
			y += label.getHeight();
			add(label, x, y);
		}
	}

	
}
