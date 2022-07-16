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
 * A beállításokat tartalmezó menüt megvalósító osztály
 */
public class SettingScreen implements Screen {

    final private GameMain game;
    final private Stage stage;

    public SettingScreen(GameMain gam) {
        game = gam;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {
        final Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        final TextButton music = new TextButton("Music:  " + (game.getMusic() ? "On" : "Off"), skin); //zene
        final TextButton sfx = new TextButton("Effects:  " + (game.getSfx() ? "On" : "Off"), skin); //hangeffektek
        final TextButton theme = new TextButton("Theme:  " + (game.getTheme().name()), skin); //theme
        final TextButton diff = new TextButton("Difficulty:  " + (game.getDiff().name()), skin); //nehézség
        final TextButton back = new TextButton("Main menu", skin); //vissza a főmenübe

        table.add(music).width(200).height(60).pad(10);
        table.row();
        table.add(sfx).width(200).height(60).pad(10);
        table.row();
        table.add(theme).width(200).height(60).pad(10);
        table.row();
        table.add(diff).width(200).height(60).pad(10);
        table.row();
        table.add(back).width(200).height(60).pad(10);


        music.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setMusic();
                music.getLabel().setText("Music: " + (game.getMusic() ? "On" : "Off"));
                game.toggleMusic();
            }
        });

        sfx.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSfx();
                sfx.getLabel().setText("Effects: " + (game.getSfx() ? "On" : "Off"));
            }
        });

        theme.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setTheme();
                theme.getLabel().setText("Theme: " + (game.getTheme().name()));
            }
        });

        diff.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setDiff();
                diff.getLabel().setText("Difficulty: " + (game.getDiff().name()));
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
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
}
