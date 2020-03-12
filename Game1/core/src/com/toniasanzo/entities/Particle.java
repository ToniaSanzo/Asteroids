/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Particle extends SpaceObjects {
    private float timer;    // Time that it's been hit
    private float time;     // Time until expiration
    private boolean remove; // Should particle be disposed

    /**
     * Set Starting position
     * @param x x-pos
     * @param y y-pos
     */
    public Particle(float x, float y){

        this.x = x;
        this.y = y;
        width = height = 2;

        // move in a random direction
        speed = 4;
        radians = MathUtils.random(2 * 3.1415f);
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        timer = 0;
        time = 1;
    }

    /**
     * @return true if the particle has expired
     */
    public boolean shouldRemove() { return remove;}

    /**
     * Update the time the particle has existed
     * @param dt Delta Time, time since the last render() method
     */
    public void update(float dt){
        timer += dt;
        if(timer > time) remove = true;

        // Update Particle position
        x += dx;
        y += dy;
    }

    /**
     * Draw Particle
     * @param sr ShapeRenderer draws Particle
     */
    public void draw(ShapeRenderer sr){
        sr.setColor(1,1,1,1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width/2, y - height/2, width);
        sr.end();
    }
}
