/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.managers;

import com.toniasanzo.gamestates.*;

public class GameStateManager {

    private GameState gameState;  // Current game state

    public static final int MENU = 0;
    public static final int PLAY = 333;


    public GameStateManager(){
        setGameState(PLAY);
    }


    public void setGameState(int state){
        if(gameState != null) gameState.dispose();  // Dispose of the current GameState when it's updated

        if(state == MENU) {
            // switch to menu state
            // gameState = new MenuState(this);
        }
        if(state == PLAY){
            // switch to play state
            gameState = new PlayState(this);
        }
    }

    /**
     * Update the current game state
     *
     * @param dt
     */
    public void update(float dt){
        gameState.update(dt);
    }

    /**
     * Draw the current game state
     */
    public void draw(){
        gameState.draw();
    }
}
