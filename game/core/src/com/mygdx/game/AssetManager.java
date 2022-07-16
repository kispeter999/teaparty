package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

/**
 * Külön osztályba kiszervezve minden gyakran használatos resource követhetőség és performance végett
 */
public class AssetManager {

    public ArrayList<Texture> gameTextures = new ArrayList<>();
    public ArrayList<Image> menuTextures = new ArrayList<>();
    public ArrayList<Texture> visitorTextures = new ArrayList<>();
    public ArrayList<Texture> greenTextures = new ArrayList<>();

    public Texture ground = new Texture(Gdx.files.internal("dirtTile.jpg"));
    public Pixmap pm = new Pixmap(Gdx.files.internal("demolish.png"));
    public Image demolishTexture = new Image(new Texture("redx.png"));
    public Image repairManTexture = new Image(new Texture("repairman_icon.png"));
    public Image janitorTexture = new Image(new Texture("janitor_icon.png"));
    public Texture blockedSmall = new Texture(Gdx.files.internal("blocked1x1.png"));
    public Texture blockedLarge = new Texture(Gdx.files.internal("blocked2x2.png"));
    public Texture okaySmall = new Texture(Gdx.files.internal("okay1x1.png"));
    public Texture okayLarge = new Texture(Gdx.files.internal("okay2x2.png"));
    public Texture trash = new Texture(Gdx.files.internal("garbage.png"));
    //debug public Texture debug = new Texture(Gdx.files.internal("redx.png"));

    public AssetManager() {
        gameTextures.add(new Texture("game/bin.png"));
        gameTextures.add(new Texture("game/bush.png"));
        gameTextures.add(new Texture("game/road.png"));
        gameTextures.add(new Texture("game/Wooden_Wild_Mouse_RCT2_Icon.png"));
        gameTextures.add(new Texture("game/Bobsleigh_Coaster_RCT2_Icon.png"));
        gameTextures.add(new Texture("game/Log_Trains_RCT2_Icon.png"));
        gameTextures.add(new Texture("game/Looping_Roller_Coaster_RCT2_Icon.png"));
        gameTextures.add(new Texture("game/Suspended_Swinging_Cars_RCT2_Icon.png"));
        gameTextures.add(new Texture("game/pizza.png"));
        gameTextures.add(new Texture("game/janitor.png"));
        gameTextures.add(new Texture("repairman.png"));

        menuTextures.add(new Image(new Texture("menu/bin.png")));
        menuTextures.add(new Image(new Texture("menu/bush.png")));
        menuTextures.add(new Image(new Texture("menu/road.png")));
        menuTextures.add(new Image(new Texture("menu/Wooden_Wild_Mouse_RCT2_Icon.png")));
        menuTextures.add(new Image(new Texture("menu/Bobsleigh_Coaster_RCT2_Icon.png")));
        menuTextures.add(new Image(new Texture("menu/Log_Trains_RCT2_Icon.png")));
        menuTextures.add(new Image(new Texture("menu/Looping_Roller_Coaster_RCT2_Icon.png")));
        menuTextures.add(new Image(new Texture("menu/Suspended_Swinging_Cars_RCT2_Icon.png")));
        menuTextures.add(new Image(new Texture("menu/pizza.png")));

        visitorTextures.add(new Texture(Gdx.files.internal("visitor1.png")));
        visitorTextures.add(new Texture(Gdx.files.internal("visitor2.png")));


        greenTextures.add(new Texture(Gdx.files.internal("game/bush.png")));
        greenTextures.add(new Texture(Gdx.files.internal("game/cherry.png")));
        greenTextures.add(new Texture(Gdx.files.internal("game/morebush.png")));
    }

}
