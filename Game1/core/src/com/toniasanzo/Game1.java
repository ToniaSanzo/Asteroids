/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.toniasanzo.managers.*;


public class Game1 extends ApplicationAdapter {

	public static int WIDTH;              // Width of the Game Universe
	public static int HEIGHT;             // Height of the Game Universe

	public static OrthographicCamera cam; // Camera used to view the game universe

	private GameStateManager gsm;

	/**
	 * Create is a one time method, that get's called when the game starts up
	 */
	@Override
	public void create (){
		WIDTH = Gdx.graphics.getWidth();   // Set the class variable WIDTH on startup
		HEIGHT = Gdx.graphics.getHeight(); // Set the class variable HEIGHT on startup


		cam = new OrthographicCamera(WIDTH, HEIGHT);    // Specify dimensions of Camera

		cam.translate(WIDTH/2, HEIGHT/2);         // Move Camera Right: WIDTH/2, Move Camera Up: Height/2

		cam.update();                                   // In order to see the updates made by cam.translate(X,Y); we
		                                                // call cam.update();

		Gdx.input.setInputProcessor(new GameInputProcessor());

		gsm = new GameStateManager();
	}

	/**
	 * Game loop method, update game logic and draw images to user
	 */
	@Override
	public void render (){

		// Clear screen to dark purple
		Gdx.gl.glClearColor(.2f, 0, .2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime()); // Passes the amount of time in Seconds that passed between the current
		                                         // render and previous render, essentially your libgdx clock
		gsm.draw();


		GameKeys.update();  // User input, updates GameKeys
	}


	/**
	 * Called when user resize's the window for the game
	 *
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height){}

	/**
	 * Method pause's the game (used mostly in Android, to deal with calls)
	 */
	@Override
	public void pause(){}

	/**
	 * Method resume's the game after it's been paused
	 */
	@Override
	public void resume(){}

	/**
	 * One time method, called when the application exits
	 */
	@Override
	public void dispose (){}
}
