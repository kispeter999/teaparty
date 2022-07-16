package com.mygdx.game;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Két Road közötti kapcsolatot reprezentál, az útkeresés használja (mindig 1 a költsége)
 */
public class RoadConnection implements Connection<Road> {

    Road from;
    Road to;
    int cost;

    public RoadConnection(Road from, Road to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Road getFromNode() {
        return from;
    }

    @Override
    public Road getToNode() {
        return to;
    }
}
