package de.tomgrill.gdxtesting.menuTests;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MainMenuScreen;
import org.junit.Assert;
import org.junit.Test;

public class MainMenuTest {
    @Test
    public void testMainMenuButtons(){
        Assert.assertTrue("Test hogy rendesen meghívhatóak e a methodok ", MainMenuScreen.isNewGamePresent());
    }
}
