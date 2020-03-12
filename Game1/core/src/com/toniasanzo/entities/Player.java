/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.toniasanzo.Game1;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Player extends SpaceObjects {

    private final int MAX_BULLETS = 4;      // Player has a maximum of 4 bullets
    private ArrayList<Bullet> bullets;      // An ArrayList of the Bullet's in the PlayState

    private float[] flamex;                 // An array of vertices used to create the flame
    private float[] flamey;                 // An array of vertices used to create the flame

    private boolean left;                   // rotate to the left if the left key is pressed
    private boolean right;                  // rotate to the right if the right key is pressed
    private boolean up;                     // accelerate the Player forward

    private float maxSpeed;                 // Player speed limit
    private float acceleration;             // How fast the player accelerates
    private float deceleration;             // Slows the player down when they are not accelerating
    private float acceleratingTimer;        // Used for to cosmetically change the length of the flame on acceleration

    private boolean hit;                    // Player's hit state
    private boolean dead;                   // Player's dead state

    private float hitTimer;                 // Amount of Time the Player has been hit
    private float hitTime;                  // The time it takes after getting hit and getting into the dead state
    private Line2D.Float[] hitLines;        // Line's used to show the Player exploding
    private Point2D.Float[] hitLinesVector; // Directions the hitLine's travel

    public Player(ArrayList<Bullet> bullets) {

        this.bullets = bullets;

        x = Game1.WIDTH/2;     // Set the Player's position to the center of the screen
        y = Game1.HEIGHT/2;    // Set the Player's position to the center of the screen

        maxSpeed = 300;        // Speed limit set to 300pixels/second
        acceleration = 220;    // Acceleration set to 200pixels/second
        deceleration = 14;     // Deceleration/Friction set to 10pixels/seconds

        shapex = new float[4]; // Player's a 4 vertex polygon
        shapey = new float[4]; // Player's a 4 vertex polygon
        flamex = new float[3]; // Player's flame 3 vertex polygon
        flamey = new float[3]; // Player's flame 3 vertex polygon

        // Angle and rotation speed
        radians = 3.1415f / 2;
        rotationSpeed = 3;

        // Hit state and hit values
        hit = false;
        hitTimer = 0;
        hitTime = 2;
    }


    /**
     * Set the vertices of the Player polygon
     */
    private void setShape(){
        // 0 - index of the vertex at the top of the polygon, MathUtils is used because they have cos and sin lookup
        //     tables
        shapex[0] = x + MathUtils.cos(radians) * 8;
        shapey[0] = y + MathUtils.sin(radians) * 8;

        // 1 - index of the vertex at the bottom left of the polygon
        shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f /5) * 8;
        shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f /5) * 8;

        // 2 - index of the vertex at the center bottom of the polygon
        shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
        shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;

        // 3 - index of the vertex at the bottom right of the polygon
        shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
        shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f /5) * 8;
    }


    /**
     * Set the vertices of the Player's Flame polygon
     */
    private void setFlame(){
        flamex[0] = x + MathUtils.cos(radians - 5 * 3.1415f / 6) * 5;
        flamey[0] = y + MathUtils.sin(radians - 5 * 3.1415f / 6) * 5;

        flamex[1] = x + MathUtils.cos(radians - 3.1415f) * (6 + acceleratingTimer * 50);
        flamey[1] = y + MathUtils.sin(radians - 3.1415f) * (6 + acceleratingTimer * 50);

        flamex[2] = x + MathUtils.cos(radians + 5 * 3.1415f / 6) * 5;
        flamey[2] = y + MathUtils.sin(radians + 5 * 3.1415f / 6) * 5;
    }

    public void setLeft(boolean b){ left = b; }   // Left is updated based on user interaction
    public void setRight(boolean b){ right = b; } // Right is updated based on user interaction
    public void setUp(boolean b){ up = b; }       // Up is updated based on user interaction


    /**
     * Add a Bullet to the Bullet (list), otherwise exit
     */
    public void shoot(){
        // If the arraylist is already filled with the maximum number of bullets the method does not do anything
        if(bullets.size() == MAX_BULLETS) return;
        bullets.add(new Bullet(x, y, radians)); // Add a Bullet to the PlayState ArrayList otherwise
    }


    /**
     * Handle all the keyboard input within update
     *
     * @param dt Delta Time, time passed since the last render event
     */
    public void update(float dt) {

        //Check if hit
        if(hit){
            hitTimer += dt;
            if(hitTimer > hitTime) {
                dead = true;
                hitTimer = 0;
            }
            for(int i = 0; i < hitLines.length; i++){
                hitLines[i].setLine(
                        hitLines[i].x1 + hitLinesVector[i].x * 10 * dt,
                        hitLines[i].y1 + hitLinesVector[i].y * 10 * dt,
                        hitLines[i].x2 + hitLinesVector[i].x * 10 * dt,
                        hitLines[i].y2 + hitLinesVector[i].y * 10 * dt
                        );
            }
            return;
        }

        // Rotate the Player based on the Keyboard input
        if(left) {
            radians += rotationSpeed * dt;
        }
        else if(right) {
            radians -= rotationSpeed * dt;
        }

        // Acceleration, move forward a set amount based on the current angle of the Player polygon
        if(up){
            dx += MathUtils.cos(radians) * acceleration * dt;
            dy += MathUtils.sin(radians) * acceleration * dt;
            acceleratingTimer += dt;
            if(acceleratingTimer > 0.1f) acceleratingTimer = 0;
        }
        else{ acceleratingTimer = 0; }

        // Deceleration
        float vec = (float) Math.sqrt(dx * dx + dy * dy);

        // When the Player is accelerating make sure to decelerate the Player
        if(vec > 0){
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }

        // Cap the Player's movement at the Max Speed
        if(vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }

        // set position
        x += dx * dt;
        y += dy * dt;

        // set shape
        setShape();

        // If the up button's pressed set the flame
        if(up) setFlame();

        // screen wrap, if the player goes out one side of the screen they'll wrap to the other side
        wrap();

    }

    public void draw(ShapeRenderer sr){
        // Set the Player object to yellow
        sr.setColor(1, 1, 0, 1);


        // All the drawing happens within the sr.begin and sr.end methods
        sr.begin(ShapeRenderer.ShapeType.Line);

        // Check if hit
        if(hit) {
            for(int i = 0; i < hitLines.length; i++){
                sr.line(
                        hitLines[i].x1,
                        hitLines[i].y1,
                        hitLines[i].x2,
                        hitLines[i].y2
                );
            }
            sr.end();
            return;
        }

        // draw ship
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            sr.line(shapex[i], shapey[i], shapex [j], shapey[j]);
        }

        // draw flames, change the color from white
        if(up){
            sr.setColor(1,.3f,.2f,1);
            for(int i =0, j = flamex.length - 1; i < flamey.length; j = i++){
                sr.line(flamex[i], flamey[i], flamex[j], flamey[j]);
            }
        }

        sr.end();
    }


    public void hit(){

        // Return if Player's already hit
        if(hit) return;

        // Player's state updated to true, stop the player from moving
        hit = true;
        dx = dy = 0;
        left = right = up = false;

        // Make explosions
        hitLines = new Line2D.Float[4];
        for(int i = 0, j = hitLines.length -1; i < hitLines.length; j = i++){
            hitLines[i] = new Line2D.Float(shapex[i], shapey[i], shapex[j], shapey[j]);
        }
        hitLinesVector = new Point2D.Float[4];
        hitLinesVector[0] = new Point2D.Float(
                MathUtils.cos(radians + 1.5f),
                MathUtils.sin(radians + 1.5f)
        );
        hitLinesVector[1] = new Point2D.Float(
                MathUtils.cos(radians - 1.5f),
                MathUtils.sin(radians - 1.5f)
        );
        hitLinesVector[2] = new Point2D.Float(
                MathUtils.cos(radians - 2.9f),
                MathUtils.sin(radians - 2.9f)
        );
        hitLinesVector[3] = new Point2D.Float(
                MathUtils.cos(radians + 2.9f),
                MathUtils.sin(radians + 2.9f)
        );
    }

    /**
     * @return true if the player's hit
     */
    public boolean isHit(){ return hit; }

    /**
     * @return true if the player's dead
     */
    public boolean isDead(){ return dead; }


    /**
     * Reset the Player's position to the center of the screen, and remove hit and dead flags
     */
    public void reset(){
        x = Game1.WIDTH / 2;
        y = Game1.HEIGHT / 2;
        setShape();
        hit = dead = false;
    }
}
