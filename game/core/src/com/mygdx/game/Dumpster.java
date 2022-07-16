package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Kibővített Rectangle, ami a kukák egy példányát valósítja meg
 * //A szülő osztályból örökölt X és Y koordinátái alapján egyértelműen azonosíthatóak
 */
public class Dumpster extends Rectangle {

    public static int getPath() { return 0; }
    public static int getCost() { return 150; }

}
