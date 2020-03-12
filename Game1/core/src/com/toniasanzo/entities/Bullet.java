/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends SpaceObjects {

    private float lifeTime;  // Amount of time the bullet should remain active
    private float lifeTimer; // How long the bullet has been alive so far

    private boolean remove;  // Boolean flag, marks when it is appropriate to dispose of the Bullet

    /**
     * Create a bullet based on the current position and angle of the bullet
     *
     * @param x x-pos
     * @param y y-pos
     * @param radians angle
     */
    public Bullet(float x, float y, float radians) {
        this.x = x;
        this.y = y;
        this.radians = radians;
        float speed = 380;
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        width = height = 2;

        lifeTimer = 0;
        lifeTime = 1;
    }

    /**
     * @return If the bullet has passed it's expiration date
     */
    public boolean shouldRemove() { return remove; }


    /**
     * Update the bullet location based on delta time
     *
     * @param dt delta time, time based in seconds since the last render() method call
     */
    public void update(float dt) {
        x += dx * dt;    // Update the x-position
        y += dy * dt;    // Update the y-position
        lifeTimer += dt; // Update the lifeTimer on the bullet

        // Wrap the bullet around the screen
        wrap();

        // Check the bullets expiration date
        if(lifeTimer > lifeTime) remove = true;
    }

    /**
     * Draw the Bullet to the screen
     *
     * @param sr ShapeRenderer used to draw the bullet
     */
    public void draw(ShapeRenderer sr) {
        // Set color to a green-blue
        sr.setColor(0,1,.6f,1);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width / 2, y - height / 2, width);
        sr.end();
    }
}
