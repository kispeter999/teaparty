package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * //Kibővített Rectangle, ami a zöldterületek egy példányát valósítja meg
 * //A szülő osztályból örökölt X és Y koordinátái alapján egyértelműen azonosíthatóak
 */
public class Green extends Rectangle {

    private final int textureID;

    public Green(){
        Random random = new Random();
        this.textureID = random.nextInt(3);
    }

    public int getPath() { return textureID; }
    public static int getCost() { return 150; }

}
