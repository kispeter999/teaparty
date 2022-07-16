package com.mygdx.game;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Kibővített Rectangle, ami a látogatók egy példányát valósítja meg
 * Mivel overlapelhetnek, mindegyiknek egyedi ID-ja van
 */
public class Visitor extends Rectangle {
    private final int id;
    private int attitude;
    private int hunger;
    private GraphPath<Road> destination;
    private Type destinationType;
    private boolean hasGarbage;
    private final float offsetX;
    private final float offsetY;
    private final int path;
    private int where;
    private boolean onWayToBuilding;
    private boolean inQueue;

    public Visitor(int id) {
        Random random = new Random();
        this.id = id;
        this.attitude = random.nextInt(30) + 70; //70-99
        this.hunger = random.nextInt(30) + 70; //70-99
        this.destination = null;
        this.destinationType = null;
        this.hasGarbage = false;
        this.offsetX = random.nextInt(40);
        this.offsetY = random.nextInt(40);
        this.path = random.nextInt(2);
        this.where = 0;
        this.onWayToBuilding = false;
        this.inQueue = false;
    }

    public int getID() { return id; }

    public int getAttitude() { return attitude; }
    public void setAttitude(int amount) { this.attitude += amount; }

    public int getHunger() { return hunger; }
    public void setHunger(int amount) { this.hunger += amount; }

    public boolean getGarbage() { return hasGarbage; }
    public void setGarbage(boolean has) { this.hasGarbage = has; }

    public GraphPath<Road> getDestination() { return destination; }
    public void setDestination(GraphPath<Road> newDest) { this.destination = newDest; }

    public Type getDestinationType() { return destinationType; }
    public void setDestinationType(Type newType) { this.destinationType = newType; }

    public float getOffsetX() { return offsetX; }

    public float getOffsetY() { return offsetY; }

    public int getPath() { return path; }

    public int getWhere() {
        return where;
    }
    public void setWhere(int where) { this.where += where; }

    public boolean getOnWayToBuilding() { return onWayToBuilding; }
    public void toggleOnWayToBuilding() { this.onWayToBuilding = !this.onWayToBuilding; }

    public boolean getInQueue() { return inQueue; }
    public void toggleInQueue() { this.inQueue = !this.inQueue; }
}
