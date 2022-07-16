package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Kibővített ScrollPane, amit megjelenítés után rögtön lehet scroll-olni, anélkül hogy bele kéne kattintani először
 */
public class AutoFocusScrollPane extends ScrollPane {

    public AutoFocusScrollPane(Actor actor, Skin skin) {
        super(actor,skin);
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(AutoFocusScrollPane.this);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                getStage().setScrollFocus(null);
            }
        });
    }
}
