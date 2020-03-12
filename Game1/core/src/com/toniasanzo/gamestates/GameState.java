/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.gamestates;

import com.toniasanzo.managers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gsm;

    /**
     * Constructor, sets the GameStateManager, (All GameState's should have the same manager), and initializes
     * the current GameState
     *
     * @param gsm GameStateManager, allows GameStates to be changed
     */
    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }

    /**
     * init is called when the GameState first starts up
     */
    public abstract void init();

    /**
     * Update is called during the render() method
     *
     * @param dt Delta Time, time in seconds that has passed between the current and previous render() method calls
     */
    public abstract void update(float dt);

    /**
     * Draw is called during the render() method
     */
    public abstract void draw();

    /**
     * Method for interacting with the "GameKeys"
     */
    public abstract void handleInput();

    /**
     * Method called when we want to switch to another GameState and dispose of the current GameState
     */
    public abstract void dispose();
}
