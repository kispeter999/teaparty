package com.mygdx.game;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.Rectangle;

/**
 * Kibővített Rectangle, ami a szerelők egy példányát valósítja meg
 * Mivel overlapelhetnek, mindegyiknek egyedi ID-ja van
 */
public class RepairMan extends Rectangle {
    private final int id;
    private int timeRemaining;
    private GraphPath<Road> destination;
    private final int path;
    private int where;

    public RepairMan(int id) {
        this.id = id;
        this.timeRemaining = 0;
        this.destination = null;
        this.path = 10;
        this.where = 0;
    }

    public int getID() { return id; }

    public int getTR() { return timeRemaining; }
    public void setTR(int amount) { this.timeRemaining += amount; }

    public GraphPath<Road> getDestination() { return destination; }
    public void setDestination(GraphPath<Road> newDest) { this.destination = newDest; }

    public int getPath() { return path; }

    public int getWhere() {
        return where;
    }
    public void setWhere(int where) { this.where += where; }
}
