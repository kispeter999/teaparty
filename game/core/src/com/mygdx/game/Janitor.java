package com.mygdx.game;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.Rectangle;

/**
 * Kibővített Rectangle, ami a takarítók egy példányát valósítja meg
 * Mivel overlapelhetnek, mindegyiknek egyedi ID-ja van
 */
public class Janitor extends Rectangle {
    final private int id;
    private GraphPath<Road> destination;
    private final int path;
    private int where;

    public Janitor(int id){
        this.id = id+1;
        this.destination = null;
        this.path = 9;
        this.where = 0;
    }

    public int getID() { return id; }

    public GraphPath<Road> getDestination() { return destination; }
    public void setDestination(GraphPath<Road> newDest) { this.destination = newDest; }

    public int getPath() { return path; }

    public int getWhere() {
        return where;
    }
    public void setWhere(int where) { this.where += where; }
}
