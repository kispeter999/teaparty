package com.mygdx.game;

/**
 * Felsorolás a nehézségi szintek reprezentálására
 */
public enum Difficulty {
    EASY(0,0),
    MEDIUM(5,5),
    HARD(10,10);

    public int modifierCost;
    public int modifierTime;

    Difficulty(int mC, int mT) {
        this.modifierCost = mC;
        this.modifierTime = mT;
    }

    public Difficulty next() {
        switch(this) {
            case EASY: return MEDIUM;
            case MEDIUM: return HARD;
            default: return EASY;
        }
    }
}
