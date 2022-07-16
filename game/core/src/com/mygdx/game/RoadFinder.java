package com.mygdx.game;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Útkeresés
 */
public class RoadFinder implements IndexedGraph<Road> {

    RoadHeuristic cityHeuristic = new RoadHeuristic();
    Array<Road> roads = new Array<>();
    Array<Connection<Road>> roadConnections = new Array<>();
    ObjectMap<Road, Array<Connection<Road>>> roadsMap = new ObjectMap<>();

    private int lastNodeIndex = 0;

    public void addRoad(Road road){
        road.setIndex(lastNodeIndex);
        lastNodeIndex++;
        roads.add(road);
    }

    public void remRoad(Road road) {
        roads.removeValue(road,false);
        //borzasztó, nem is értem miért, de csak úgy működik ha KÉTSZER futtatom le a loopokat, vagy pár benne marad
        for (Connection<Road> curr : roadConnections) {
            if (curr.getToNode() == road) {
                roadConnections.removeValue(curr,false);
            }
        }
        for (Connection<Road> curr : roadConnections) {
            if (curr.getToNode() == road) {
                roadConnections.removeValue(curr,false);
            }
        }
        for (Connection<Road> curr : roadConnections) {
            if (curr.getFromNode() == road) {
                roadConnections.removeValue(curr,false);
            }
        }
        for (Connection<Road> curr : roadConnections) {
            if (curr.getFromNode() == road) {
                roadConnections.removeValue(curr,false);
            }
        }
        roadsMap.remove(road);
        for (ObjectMap.Entries<Road,Array<Connection<Road>>> iter = roadsMap.entries(); iter.hasNext(); ) {
            ObjectMap.Entry<Road, Array<Connection<Road>>> currConnections = iter.next();
            for (Connection<Road> curr : currConnections.value) {
                if (curr.getFromNode() == road || curr.getToNode() == road) {
                    currConnections.value.removeValue(curr,false);
                }
            }
        }
    }

    public void connectRoads(Road from, Road to, int cost){
        RoadConnection newConnection = new RoadConnection(from, to, cost);
        if(!roadsMap.containsKey(from)){
            roadsMap.put(from, new Array<Connection<Road>>()); //ez a warning átverés, ha hallgatsz rá nem indul a program
        }
        roadsMap.get(from).add(newConnection);
    }

    public GraphPath<Road> findPath(Road start, Road goal){
        GraphPath<Road> roadPath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(start, goal, cityHeuristic, roadPath);
        if (roadPath.getCount() > 0) {
            return roadPath;
        } else {
            return null;
        }
    }


    @Override
    public int getIndex(Road node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return lastNodeIndex;
    }

    @Override
    public Array<Connection<Road>> getConnections(Road fromNode) {
        if(roadsMap.containsKey(fromNode)){
            return roadsMap.get(fromNode);
        }
        return new Array<>(0);
    }
}