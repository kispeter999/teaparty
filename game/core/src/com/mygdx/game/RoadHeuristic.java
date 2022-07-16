package com.mygdx.game;

import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 * Heurisztika az útkereséshez
 */
public class RoadHeuristic implements Heuristic<Road> {

    @Override
    public float estimate(Road currentCity, Road goalCity) {
        return 1;
    } //dummy
}
