package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Kibővített Rectangle, ami a Szemét egy példányát valósítja meg
 */
public class Garbage extends Rectangle {

    private final float offsetX;
    private final float offsetY;

    public Garbage(float x, float y) {
        Random random = new Random();
        this.x = x;
        this.y = y;
        this.offsetX = random.nextInt(36);
        this.offsetY = random.nextInt(40);
    }

    public float getOffsetX() { return offsetX; }

    public float getOffsetY() { return offsetY; }

}