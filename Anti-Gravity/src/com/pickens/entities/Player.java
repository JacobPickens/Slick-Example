package com.pickens.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import com.pickens.main.Main;
import com.pickens.util.Constants;

/*
 *  This is obviously the player class. If you take a quick glance at it you will notice some obvious
 *  similarities between this class and the Wall, WallManager, and Main class. They all contain a
 *  render method, an update method, and (though it isn't noticeable) an init method that takes place in the constructor.
 *  All entities in the game, an entity being something that would be drawn and updated should well, always have an update
 *  and render method that will be fed into the Main class's original render and update method.
 */

public class Player {

	private float x, y, yVel; // Simple coordinates and a y velocity
	
	private int direction; // This is an integer that can be either -1 or 1. It decides the direction of gravity
	
	private WallManager wm; // This is just an instance of all the walls on the map for collision purposes
	private Rectangle bounds; // The bounding box of the player for collision purposes
	
	public Player(float x, float y, WallManager wm) {
		this.x = x;
		this.y = y;
		this.wm = wm;
		
		bounds = new Rectangle(x, y, 32, 32);
		
		direction = -1;
	}
	
	public void render(Graphics g) {
		// Just drawing a red rectangle at the exact position of the player
		g.setColor(Color.red);
		g.fillRect(x, y, 32, 32);
	}
	
	float appliedGravity;
	public void update(Input input) {
		/* 
		 * This variable looks a lot more complicated than it actually is.
		 * In reality it's just a constant gravity value found in the Constants class with a scaled down
		 * value of how much time the game has lasted in order to make gravity stronger over time. 
		 */
		appliedGravity = (float) (Constants.GRAVITY + (Main.speedIncrements*.02));
		
		// If the space bar is pressed, toggle the direction from either negative to positive, thus inverting gravity
		if(input.isKeyPressed(Input.KEY_SPACE)) {
			if(direction == -1) {
				direction = 1;
			} else {
				direction = -1;
			}
		}
		
		//////// Collision and Movement	////////
		
		/*
		 *  This looks complicated too. Here what is happening is we're getting the absolute value of 
		 *  what the players current y velocity is plus what it will be next turn. If this value is less then
		 *  TERMINAL_VELOCITY that is set in the Constants class, then continue with the addition, however,
		 *  if it is greater than TERMINAL_VELOCITY, set yVel to TERMINAL_VELOCITY and multiply it by the current
		 *  direction instead so you can't just move infinitely fast.
		 */
		if(Math.abs(yVel + appliedGravity * direction) < Constants.TERMINAL_VELOCITY) {
			yVel += appliedGravity * direction;
		} else {
			yVel = Constants.TERMINAL_VELOCITY * direction;
		}
		
		/*
		 *  Similarly to what is happening above, we are now predicting where the players y coordinate will be in the
		 *  next update by adding its current velocity to its current position. If the sum is greater than the HEIGHT
		 *  of the window (this would be the bottom of the window) minus the height of the player itself because by
		 *  default when something is drawn in Slick2D its origin is the top left corner of the shape.
		 */
		if(y + yVel >= Constants.HEIGHT-32) {
			/*
			 *  The player is about to fall through the window, so set the players Y coordinate
			 *  to the height of the window minus the height of the player. (Yet again due to its draw origin)
			 *  and then invert its velocity and scale it by a bounce friction to get a fancy little bounce effect.
			 */
			y = Constants.HEIGHT-32;
			yVel = -yVel*Constants.BOUNCE_FRICTION;
		} else if(y + yVel <= 0) {
			/*
			 *  Exactly like above except instead its the top of the screen. Notice how we don't subtract or add anything
			 *  to the new y coordinate because since its the top of the window, the origin will hit before any other
			 *  points.
			 */
			y = 0;
			yVel = -yVel*Constants.BOUNCE_FRICTION;
		} else {
			// Finally if no collision conditions are satisfied just add the yVel to the y as normal
			y += yVel;
		}
		
		bounds.setY(y); // Updating the bounding box with the new y coordinate
		
		// This for loop cycles through every wall that exists
		for(int i = 0; i < wm.getWalls().size(); i++) {
			// Each wall we iterate through we are checking whether or not one of the walls boxes intersects the player's.
			if(bounds.intersects(wm.getWall(i).getTop())) {
				die();
			}
			
			if(bounds.intersects(wm.getWall(i).getBottom())) {
				die();
			}
			
			// Scoring
			// Pretty self-explanatory, if the wall has not yet been scored, and it passes the player, score it.
			if(!wm.getWall(i).scored && wm.getWall(i).getBottom().getX() < x) {
				wm.getWall(i).scored = true;
				Main.score++;
			}
		}
	}
	
	// This method just sets highscores and changes the boolean that makes the ugly death menu pop up
	public void die() {
		Main.dead = true;
		if(Main.score > Main.highscore) {
			Main.highscore = Main.score;
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
