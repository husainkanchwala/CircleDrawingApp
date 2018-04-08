package com.example.husain.assignment3;

/**
 * Created by husain on 2/25/2018.
 */

public class Circle {
    public float xStart ;
    public float yStart;
    public float radius;
    public boolean isMoving = false;

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public Circle (float xStart, float yStart, float radius)
    {
        this.xStart = xStart;
        this.yStart = yStart;
        this.radius = radius;
    }

    public void setxStart(float xStart) {
        this.xStart = xStart;
    }

    public void setyStart(float yStart) {
        this.yStart = yStart;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getxStart() {
        return xStart;
    }

    public float getyStart() {
        return yStart;
    }

    public float getRadius() {
        return radius;
    }
}
