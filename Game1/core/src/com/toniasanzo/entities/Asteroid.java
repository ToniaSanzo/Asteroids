/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends SpaceObjects {
    private int type;
    private boolean prevColl;           // asteroid's been colliding
    private boolean currColl;           // asteroid's colliding
    public static final int SMALL = 0;  // Small asteroid
    public static final int MEDIUM = 1; // Medium asteroid
    public static final int LARGE = 2;  // Large asteroid

    private int numPoints; // Help generate random asteroids
    private float[] dists; // Help generate random asteroids

    private boolean remove; // Flags when to remove the asteroid


    /**
     * Construct the asteroid
     * @param x x-pos
     * @param y y-pos
     * @param type size
     */
    public Asteroid(float x, float y, int type) {
        prevColl = false;
        currColl = false;

        this.x = x;
        this.y = y;
        this.type = type;

        // Small asteroid dimensions and speed
        if(type == SMALL){
            numPoints = 8;
            width = height = 12;
            speed = MathUtils.random(70, 100);
        }
        // Medium asteroid dimensions and speed
        else if(type == MEDIUM){
            numPoints = 10;
            width = height = 20;
            speed = MathUtils.random(50, 60);
        }
        // Large asteroids dimensions and speed
        else if(type == LARGE) {
            numPoints = 12;
            width = height = 40;
            speed = MathUtils.random(20, 30);
        }

        // Asteroids spin
        rotationSpeed = MathUtils.random(-1, 1);

        // Random direction the Asteroid travels in
        radians = MathUtils.random(2 * 3.1415f);
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        // Holds all the X and Y coordinates of the points within the radius and radius/2 of the asteroid
        shapex = new float[numPoints];
        shapey = new float[numPoints];
        dists = new float[numPoints];

        // Generate the random coordinates within the radius and the radius/2
        int radius = width/2;
        for(int  i = 0; i < numPoints; i++){
            dists[i] = MathUtils.random(radius / 2, radius);
        }

        // Set's the vertices of the Asteroid Polygon
        setShape();
    }

    private void setShape() {
        float angle = 0;
        // Set all the shapex and shapey coordinates
        for(int i = 0; i < numPoints; i++) {
            shapex[i] = x + MathUtils.cos(angle + radians) * dists[i];
            shapey[i] = y + MathUtils.sin(angle + radians) * dists[i];
            angle += 2 * 3.1415f / numPoints;
        }
    }

    /**
     * @return Current size of the Asteroid
     */
    public int getType() { return type; }

    /**
     * @return x-axis vector
     */
    public float getdx() { return dx; }

    /**
     * @return y-axis vector
     */
    public float getdy() { return dy; }

    /**
     * @return the "mass" of the object
     */
    public float getMass() { return width/2f; }

    /**
     * asteroid-collision bounce
     * @param dx2 x-axis vector of asteroid2
     * @param dy2 y-axis vector of asteroid2
     * @param m2 "mass" of asteroid2
     */
    public void bounce(float dx2, float dy2, float m2){
        currColl = true;
        if(!prevColl) {
            float m1 = width / 2f;
            prevColl = true;
            // change asteroid spin
            rotationSpeed = MathUtils.random(-1, 1);
            // 1-D elastic collision applied to x & y-axis
            dx = (((m1 - m2)/(m1 + m2)) * dx) + (((2f * m2)/(m1 + m2)) * dx2);
            dy = (((m1 - m2)/(m1 + m2)) * dy) + (((2f * m2)/(m1 + m2)) * dy2);
        }
    }


    /**
     * Update the Asteroids information depending on the amount of time that has passed
     * @param dt Delta Time, time since the last render loop
     */
    public void update(float dt){
        prevColl = currColl;
        currColl = false;

        // Position gets updated in a straight line
        x += dx * dt;
        y += dy * dt;

        // Aesthetic of rotation
        radians += rotationSpeed * dt;
        setShape();

        // Wrap around screen
        wrap();
    }

    /**
     * Draw Asteroid
     * @param sr ShapeRenderer used to draw the Asteroid polygon
     */
    public void draw(ShapeRenderer sr){
        sr.setColor(1,1,1,1);
        sr.begin(ShapeRenderer.ShapeType.Line);

        // draw asteroids
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            sr.line(shapex[i], shapey[i], shapex [j], shapey[j]);
        }

        sr.end();
    }
}
