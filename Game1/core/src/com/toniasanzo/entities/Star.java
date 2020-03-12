package com.toniasanzo.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Star extends SpaceObjects {


    /**
     * Make star with x and y-coordinates
     * @param x x-pos
     * @param y y-pos
     */
    public Star(float x, float y){
        this.x = x; // set x-pos
        this.y = y; // set y-pos

        width = height = 2;
    }

    /**
     * Draw stars
     * @param sr ShapeRenderer, draws stars
     */
    public void draw(ShapeRenderer sr){
        // Stars are a light purple
        sr.setColor(.7f, .2f, .7f, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width/2, y - height/2, width/2);
        sr.end();

    }


}
