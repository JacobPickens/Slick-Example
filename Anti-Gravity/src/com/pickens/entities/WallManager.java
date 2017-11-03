package com.pickens.entities;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.pickens.util.Constants;

/*
 *  This class is a bit more interesting. You may notice that this class has a render, update, and init method,
 *  however, this class isn't rendered... technically. This class is used to batch together all the instances of
 *  walls and render them all in one call. otherwise your code would look something like this:
 *  
 *  wall1.render(g);
 *  wall2.render(g);
 *  wall3.render(g);
 *  ...
 *  
 *  This would be incredibly inefficient if not downright impossible. Instead we just put them in a list and 
 *  render them all in one call to the manager's render method below.
 */

public class WallManager {

	public static float wallSpeed = 2; // Speed the walls move towards the player
	public static float spawnTime = 2; // time in seconds
	public static float holeSize = 200; // Size of the hole in the walls
	
	ArrayList<Wall> walls; // A list that will hold every wall on the screen
	
	Random r; // Used for random generation of numbers
	
	public WallManager() {
		walls = new ArrayList<Wall>();
		r = new Random();
	}
	
	public void render(Graphics g) {
		// For loop that loops through every wall in the walls list and renders it to the screen.
		for(Wall w:walls) {
			w.render(g);
		}
	}
	
	int spawnTicker = 0;
	public void update(Input input) {
		// Just like the for loop above but instead this is for the updating of the walls.
		for(int i = 0; i < walls.size(); i++) {
			walls.get(i).update(input);
		}
		
		/*
		 * This is where the walls are spawned. The spawnTicker is increased every update. So since this is constantly
		 * being called, and the game runs at about 60 FPS its safe to say that every increment of 60 is one second, and 
		 * so that is why we multiply spawn time by 60. I do this for speedIncrements in the Main class as well.
		 */
		spawnTicker++;
		if(spawnTicker >= spawnTime*60) {
			spawnTicker = 0;
			// Randomly generated a hole position on the wall and spawns it slightly off-screen for it to move into view
			add(new Wall(Constants.WIDTH, 0, r.nextInt((int) (Constants.HEIGHT-holeSize)), holeSize, this));
		}
	}
	
	public void add(Wall w) {
		walls.add(w);
	}
	
	public void remove(Wall w) {
		walls.remove(w);
	}
	
	public Wall getWall(int i) {
		return walls.get(i);
	}
	
	public ArrayList<Wall> getWalls() {
		return walls;
	}

}
