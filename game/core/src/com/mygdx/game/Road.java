package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Kibővített Rectangle, ami az utak egy példányát valósítja meg
 * A szülő osztályból örökölt X és Y koordináták alapján egyértelműen azonosíthatóak
 */
public class Road extends Rectangle {

    private State state = State.WORKING;
    private boolean hidden = false; //épületek alatt 1-1 rejtett Road van az útkereséshez; ez mutatja hogy olyan-e
    private int index; //szintén útkeresés

    public static int getPath() { return 2; }
    public static int getCost() { return 20; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public int getIndex() { return index; }
    public void setIndex(int index){ this.index = index; }

    public boolean isHidden() { return hidden; }
    public void setHidden() { this.hidden = true; }

}
