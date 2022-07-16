package com.mygdx.game;

/**
 * Felsorolás az épületek típusainak reprezentálására
 */
public enum Type {
    COASTER1(300,60,8,2,15,3,50, "Wooden Wild_Mouse", 3),
    COASTER2(225,80,6,1,20,2,40, "Bobsleigh Coaster", 4),
    COASTER3(150,95,10,3,25,2,35, "Log Trains", 5),
    COASTER4(125,90,7,2,10,2,30, "Looping Roller", 6),
    COASTER5(400,50,9,3,30,3,55, "Suspended Swinging", 7),
    RESTAURANT1(150,85,25,3,15,0,70,"Pizza",8);

    public int cost;
    public int upkeep;
    public int capacity;
    public int turnTime;
    public int price;
    public int durability;
    public int value; //ez akar egyelőre lenni hogy mennyivel növeli a látogatók jókedélyét/csökkenti az éhségét
    public String name;
    public int path;

    Type(int cost, int upkeep, int capacity, int turnTime, int price, int durability, int value, String name, int path) {
        this.cost = cost;
        this.upkeep = upkeep;
        this.capacity = capacity;
        this.turnTime = turnTime;
        this.price = price;
        this.durability = durability;
        this.value = value;
        this.name = name;
        this.path = path;
    }
}
