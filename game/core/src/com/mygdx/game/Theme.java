package com.mygdx.game;

/**
 * Felsorolás a thímsz reprezentációjára
 */
public enum Theme {
    THEME1,
    THEME2,
    THEME3;

    public Theme next() {
        switch(this) {
            case THEME1: return THEME2;
            case THEME2: return THEME3;
            default: return THEME1;
        }
    }
}
