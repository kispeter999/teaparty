package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Kibővített Rectangle, ami az adott épülettípus ("type" változó) egy példányát valósítja meg
 * A szülő osztályból örökölt X és Y koordináták alapján egyértelműen azonosíthatóak
 */
public class Building extends Rectangle {
    private final Type type; //épület típusa
    private Array<Visitor> queue; //sor
    private int turnsPlayed;

    public Building(Type type) {
        this.type = type;
        this.queue = new Array<Visitor>();
        this.turnsPlayed = 0;
    }

    public Type getType() { return type; }
    public void addToQueue(Visitor vis) { this.queue.add(vis); }
    public void removeFromQueue(Visitor vis) { this.queue.removeValue(vis, false);}
    public Array<Visitor> getQueue() { return this.queue; }
    public void resetTurnsPlayed() { this.turnsPlayed = 0; }
    public void incTurnsPlayed() { this.turnsPlayed += 1; }
    public int getTurnsPlayed() { return turnsPlayed; }
}
