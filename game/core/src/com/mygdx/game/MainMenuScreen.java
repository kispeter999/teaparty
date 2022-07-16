package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * A kezdőképernyőt / főnemüt megvalósító osztály, innen érhetőek el a beállítások, a kilépés és az új játék kezdése
 */
public class MainMenuScreen implements Screen {

    final private GameMain game;
    final private Stage stage;

    public MainMenuScreen(GameMain gam) {
        game = gam;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        game.startMusic();
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton newGame = new TextButton("New Game", skin); //új játék
        TextButton settings = new TextButton("Settings", skin); //beállítások
        TextButton exit = new TextButton("Exit", skin); //kilépés

        table.add(newGame).width(200).height(60).pad(10);
        table.row().height(50);
        table.add(settings).width(200).height(60).pad(10);
        table.row().height(50);
        table.add(exit).width(200).height(60).pad(10);

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingScreen(game));
                dispose();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public static boolean isNewGamePresent(){
        return true;
    }
    public boolean musicLoaded() { return game.getMusic();}
}
