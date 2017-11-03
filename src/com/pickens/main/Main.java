package com.pickens.main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.pickens.entities.Player;
import com.pickens.entities.WallManager;
import com.pickens.util.Constants;

/*
 *  This is the Main class. It extends a basic game because this game only contains one state.
 *  You can think of a state as like a page of a website or something along those lines.
 *  We just have one state here and that is the Game state. All the other parts of this game
 *  all come together here and interface with each other and the games window.
 */

public class Main extends BasicGame {

	private Player player;
	private WallManager wallManager;
	
	public static int highscore = 0;
	public static int score = 0;
	public static boolean dead = false;
	public static int speedIncrements = 0;
	
	public Main(String title) {
		super(title);
	}

	// This is just a quick method I put together so I didn't have to duplicate code to restart the game
	public void reset() {
		wallManager = new WallManager();
		player = new Player(32, 32, wallManager);
		score = 0;
		speedIncrements = 0;
	}
	
	// This method sets all the variables and puts the game into motion
	@Override
	public void init(GameContainer gc) throws SlickException {
		reset();
	}
	
	// This is where all the art is drawn. It is called roughly 60 times a second. (FPS)
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		player.render(g);
		wallManager.render(g);
		g.setColor(Color.white);
		g.drawString("Highscore: " + highscore, 10, 10);
		g.drawString("Score: " + score, 10, 30);
		
		// If the player has died it will also draw this little menu over top of the frozen game.
		if(dead) {
			g.setColor(Color.green);
			g.fillRect(Constants.WIDTH/2-256/2, Constants.HEIGHT/2-128/2, 256, 128);
			g.setColor(Color.black);
			g.drawString("Dead", Constants.WIDTH/2, (Constants.HEIGHT/2)-15);
			g.drawString("[Click]", Constants.WIDTH/2, (Constants.HEIGHT/2)+15);
		}
	}

	int gameTimeTicker = 0;
	
	// This is where all the game logic will take place. Generally this is also limited to 60 FPS.
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if(!dead) { // If the player isn't dead, update everything. Otherwise, freeze the game.
			player.update(gc.getInput());
			wallManager.update(gc.getInput());
			
			// Handles speeding up of gravity and walls. Check the WallManager class for a more in-depth exp.
			gameTimeTicker++;
			if(gameTimeTicker >= 3*60) {
				gameTimeTicker = 0;
				speedIncrements++;
			}
		} else {
			// Game reset after death
			if(gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				reset();
				dead = false;
			}
		}
	}
	
	public static void main(String[] args) {
		AppGameContainer appgc;
		try {
			appgc = new AppGameContainer(new Main("Anti-Gravity"));
			appgc.setDisplayMode((int)Constants.WIDTH, (int)Constants.HEIGHT, false); // Sets the size of the window
			appgc.setVSync(true);
			appgc.setShowFPS(false);
			appgc.start(); // Starts the window and calls the init() method above
		} catch(SlickException e) {
			e.printStackTrace();
		}
	}

}
