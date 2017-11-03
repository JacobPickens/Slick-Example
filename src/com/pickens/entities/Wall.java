package com.pickens.entities;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import com.pickens.main.Main;
import com.pickens.util.Constants;

/*
 *  This is yet another entity like the player class. It has a render, update, and an initializing constructor
 */

public class Wall {

	WallManager wm;
	
	// Both bounding boxes of the top and bottom of the wall excluding the hole for obvious reasons.
	Rectangle top;
	Rectangle bottom;
	
	Random r;
	
	float x, y, holeY, holeSize;
	
	boolean scored = false;
	
	public Wall(float x, float y, float holeY, float holeSize, WallManager wm) {
		this.x = x;
		this.y = y;
		this.holeY = holeY;
		this.holeSize = holeSize;
		this.wm = wm;
		
		r = new Random();
		
		top = new Rectangle(x, y, 8, holeY);
		bottom = new Rectangle(x, holeY+holeSize, 8, Constants.HEIGHT-(holeY+holeSize));
	}
	
	public void render(Graphics g) {
		// Instead of drawing an arbitrary rectangle like I did in the player class, I just drew the literal bounding boxes
		if(Main.score > 12) {
			g.setColor(new Color(r.nextFloat(),r.nextFloat(),r.nextFloat()));
		} else {
			g.setColor(Color.red);
		}
		g.fill(top);
		g.fill(bottom);
	}
	
	public void update(Input i) {
		/*
		 *  Similar to the appliedGravity in the player class, this is just a constant with an incremental scalar
		 *  for gradual speed increase over time
		 */
		float speed = (float) (WallManager.wallSpeed + (Main.speedIncrements*.5));
		
		// Limits speed to TERMINAL_VELOCITY
		if(speed > Constants.TERMINAL_VELOCITY) {
			speed = Constants.TERMINAL_VELOCITY;
		}
		
		x -= speed;
		top.setX(x);
		bottom.setX(x);	
		
		// If the wall is -32 pixels off the screen, its irrelevant and is removed to save space.
		if(x < -32) {
			wm.remove(this);
		}
	}

	public Rectangle getTop() {
		return top;
	}

	public void setTop(Rectangle top) {
		this.top = top;
	}

	public Rectangle getBottom() {
		return bottom;
	}

	public void setBottom(Rectangle bottom) {
		this.bottom = bottom;
	}

}
