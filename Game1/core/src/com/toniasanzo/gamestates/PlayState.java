/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.toniasanzo.Game1;
import com.toniasanzo.entities.*;
import com.toniasanzo.managers.*;

import java.util.ArrayList;

public class PlayState extends GameState {
    private static final int NUM_STARS = 22; // Number of Stars

    private ShapeRenderer sr;                // Used for drawing SpaceObjects
    private Player player;                   // The Player
    private ArrayList<Bullet> bullets;       // The Bullets
    private ArrayList<Asteroid> asteroids;   // The Asteroids
    private ArrayList<Particle> particles;   // The Particles
    private ArrayList<Star> stars;           // The Stars

    private int level;                       // What level we are currently on
    private int totalAsteroids;              // Total Asteroids in level
    private int numAsteroidsLeft;            // Asteroids left



    /**
     * Links the PlayState to the GameState Manager
     *
     * @param gsm GameStateManager, allows switching of GameStates
     */
    public PlayState(GameStateManager gsm){
        super(gsm);
    }

    /**
     * init is called when the GameState first starts up
     */
    public void init(){
        //Set up the renderer
        sr = new ShapeRenderer();

        // Create Bullet(list)
        bullets = new ArrayList<Bullet>();

        // Create Player
        player = new Player(bullets);

        // Create Asteroid(list)
        asteroids = new ArrayList<Asteroid>();

        //Create Particle(list)
        particles = new ArrayList<Particle>();

        //Create Stars(list)
        stars = new ArrayList<Star>();

        level = 1;
        // Create Asteroids
        spawnAsteroids();
        spawnStars();
    }

    /**
     * Generate stars
     */
    private void spawnStars(){
        stars.clear();

        // Spawn Stars in a loop
        for(int i = 0; i < NUM_STARS; i++){
            stars.add(new Star(MathUtils.random(Game1.WIDTH), MathUtils.random(Game1.HEIGHT)));
        }
    }

    /**
     * Generate asteroids
     */
    private void spawnAsteroids(){
        asteroids.clear();

        int numToSpawn = 4 + level - 1;    // Number of large asteroids to spawn
        numAsteroidsLeft = totalAsteroids; // numAsteroidsLeft equals totalASteroids during init()

        // Spawn Asteroids in loop
        for(int i = 0; i < numToSpawn; i++) {
            float x, y, dist;

            // Generate random x and y-coordinate for asteroid
            do {
                x = MathUtils.random(Game1.WIDTH);
                y = MathUtils.random(Game1.HEIGHT);

                float dx = x - player.getx();
                float dy = y - player.gety();
                dist = (float) Math.sqrt(dx * dx + dy * dy);

                // If the asteroid is too close to the player pick another X and Y point for the asteroid
            } while(dist < 100);


            asteroids.add(new Asteroid(x, y, Asteroid.LARGE));

        }
    }

    /**
     * Create 6 particles as x and y-coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void createParticles(float x, float y){ for(int i = 0; i < 6; i++){ particles.add(new Particle(x, y)); } }

    /**
     * Split Asteroids into smaller asteroids
     * @param a asteroid to split
     */
    private void splitAsteroids(Asteroid a){
        createParticles(a.getx(), a.gety());
        numAsteroidsLeft--;
        // Split Large Asteroid into medium Asteroids
        if(a.getType() == Asteroid.LARGE){
            particles.add(new Particle(a.getx(), a.gety()));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
        }

        // Split Medium Asteroid into small Asteroids
        if(a.getType() == Asteroid.MEDIUM){
            particles.add(new Particle(a.getx(), a.gety()));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));

        }
    }


    /**
     * Update is called during the render() method
     *
     * @param dt Delta Time, time in seconds that has passed between the current and previous render() method calls
     */
    public void update(float dt){
        // get user input
        handleInput();

        // Update level once when completed
        if(asteroids.size() == 0){
            level++;
            spawnAsteroids();
            spawnStars();
        }

        // update player
        player.update(dt);
        if(player.isDead()){
            player.reset();
            return;
        }

        // update player bullets
        for(int i = 0; i < bullets.size(); i++){

            // Update the bullet status
            bullets.get(i).update(dt);

            // Remove expired bullets
            if(bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        // update asteroids
        for(int i = 0; i < asteroids.size(); i++){

            // update the asteroid's statuses
            asteroids.get(i).update(dt);
        }

        // update particles
        for(int i = 0; i < particles.size(); i++){
            particles.get(i).update(dt);
            if(particles.get(i).shouldRemove()){
                particles.remove(i);
                i--;
            }
        }

        // check collisions
        checkCollisions();
    }

    /**
     * Check SpaceObject collisions
     */
    private void checkCollisions(){

        // player-asteroid collision
        if(!player.isHit()) {
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid a = asteroids.get(i);
                if (a.intersects(player) || player.intersects(a)) {
                    player.hit();
                    asteroids.remove(i);
                    i--;
                    splitAsteroids(a);
                    break;
                }
            }

            // bullet-asteroid collision
            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                for (int j = 0; j < asteroids.size(); j++) {
                    Asteroid a = asteroids.get(j);

                    // Checks to see if bullet hits asteroid
                    if (a.contains(b.getx(), b.gety())) {
                        bullets.remove(i);
                        i--;
                        asteroids.remove(j);
                        j--;
                        splitAsteroids(a);
                        break;
                    }
                }
            }

            // asteroid-asteroid collision
            for(int i = 0; i < asteroids.size(); i++){
                Asteroid a1 = asteroids.get(i);
                for(int j = 0; j < asteroids.size(); j++) {
                    if(i == j) continue; // Don't check if an asteroid collides with itself
                    Asteroid a2 = asteroids.get(j);

                    // Update the direction of the asteroids
                    if(a1.intersects(a2) || a2.intersects(a1)){
                        float a1x = a1.getdx(), a1y = a1.getdy(), a1m = a1.getMass();
                        a1.bounce(a2.getdx(),a2.getdy(), a2.getMass());
                        a2.bounce(a1x, a1y, a1m);
                    }

                }
            }
        }
    }

    /**
     * Draw is called during the render() method
     */
    public void draw(){
        // draw player
        player.draw(sr);

        // draw bullets
        for(int i = 0; i < bullets.size(); i++) bullets.get(i).draw(sr);

        // draw asteroids
        for(int i = 0; i < asteroids.size(); i++) asteroids.get(i).draw(sr);

        // draw particles
        for(int i = 0; i < particles.size(); i++) particles.get(i).draw(sr);

        // draw stars
        for(int i = 0; i < stars.size(); i++) stars.get(i).draw(sr);
    }

    /**
     * Method for interacting with the "GameKeys"
     */
    public void handleInput(){
        player.setLeft(GameKeys.isDown(GameKeys.LEFT));
        player.setRight(GameKeys.isDown(GameKeys.RIGHT));
        player.setUp(GameKeys.isDown(GameKeys.UP));
        if (GameKeys.isPressed(GameKeys.SPACE)) player.shoot();
    }

    /**
     * Method called when we want to switch to another GameState and dispose of the current GameState
     */
    public void dispose(){}
}
