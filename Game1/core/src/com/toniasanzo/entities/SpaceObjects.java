/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.entities;

import com.toniasanzo.Game1;

public class SpaceObjects {

    protected float x;             // x - position of the SpaceObject
    protected float y;             // y - position of the SpaceObject

    protected float dx;            // dx - direction the SpaceObject is traveling [horizontally]
    protected float dy;            // dy - direction the SpaceObject is traveling [vertically]

    protected float radians;       // radians - the angle SpaceObject's facing

    protected float speed;         // speed - how fast the SpaceObject's going

    protected float rotationSpeed; // rotationSpeed - how fast the SpaceObject

    protected int width;           // width - the width of the SpaceObject
    protected int height;          // height - the height of the SpaceObject

    protected float[] shapex;      // An array of vertices
    protected float[] shapey;      // An array of vertices

    /**
     * If the SpaceObject goes out one end of the screen it will wrap to the other end
     */
    protected void wrap(){
        if(x < 0) x = Game1.WIDTH;
        if(x > Game1.WIDTH) x = 0;
        if(y < 0) y = Game1.HEIGHT;
        if(y > Game1.HEIGHT) y = 0;
    }

    public float getx(){ return x; }             // get x-coordinate
    public float gety(){ return y; }             // get y-coordinate

    public float[] getShapex(){ return shapex; } // get shapex
    public float[] getShapey(){ return shapey; } // get shapey


    /**
     * Check if polygons intersect using th contains() method
     * @param other polygon object you want to check
     * @return true if polygon's intersect
     */
    public boolean intersects(SpaceObjects other){
        float[] sx = other.getShapex();
        float[] sy = other.getShapey();
        for(int i = 0; i < sx.length; i++){
            if(contains(sx[i], sy[i])){ return true; }
        }
        return false;
    }

    /**
     * Check if a polygon intersects a point, using the Even-Odd Winding Rule
     * @param x point x-pos
     * @param y piont y-pos
     * @return true if point is within the polygon
     */
    public boolean contains(float x, float y){
        boolean b = false;
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
           // Even-Odd Winding Rule
           if((shapey[i] > y) != (shapey[j] > y) && (x < (shapex[j] - shapex[i]) * (y - shapey[i]) /
                   (shapey[j] - shapey[i]) + shapex[i])){
               b = !b;
           }
        }
        return b;
    }
}
