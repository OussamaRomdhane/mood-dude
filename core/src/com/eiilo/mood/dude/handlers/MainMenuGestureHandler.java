package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.eiilo.mood.dude.MainMenu;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.utils.About;
import com.eiilo.mood.dude.utils.LevelSelector;

import sun.applet.Main;

public class MainMenuGestureHandler implements GestureDetector.GestureListener {
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 v = new Vector3(x, y, 0);
        MainMenu.mainCamera.unproject(v);

        if (MainMenu.isDeleting) {
            if (MainMenu.yesNoButtonsSprites.get(0).getBoundingRectangle().contains(v.x, v.y)) {
                MD.resetGame();
                MD.lastLevel = MD.getLevel();
                MainMenu.isDeleting = false;
            }
            else if (MainMenu.yesNoButtonsSprites.get(1).getBoundingRectangle().contains(v.x, v.y)) {
                MainMenu.isDeleting = false;
            }
            return true;
        }

        if (! MainMenu.settingsClicked) {
            if (MainMenu.buttonsSprites.get(2).getBoundingRectangle().contains(v.x, v.y)) {
                MainMenu.settingsClicked = true;
                return true;
            }
        }
        else {
            if (MainMenu.buttonsSettingsSprites.get(0).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel > 0) {
                MainMenu.isDeleting = true;
                return true;
            }
            if (MainMenu.buttonsSettingsSprites.get(1).getBoundingRectangle().contains(v.x, v.y)) {
                if (MD.musicOn) {
                    MD.setMusic(false);
                    MD.musicOn = MD.getMusic();
                    if (MainMenu.game.mainMenuBackgroundMusic.isPlaying()) MainMenu.game.mainMenuBackgroundMusic.stop();
                    return true;
                }
                else {
                    if (MD.soundsOn) {
                        MD.setSound(false);
                        MD.soundsOn = MD.getSound();
                        return true;
                    } else {
                        MD.setMusic(true);
                        MD.musicOn = MD.getMusic();
                        if (! MainMenu.game.mainMenuBackgroundMusic.isPlaying()) MainMenu.game.mainMenuBackgroundMusic.play();
                        MD.setSound(true);
                        MD.soundsOn = MD.getSound();
                        return true;
                    }
                }
            }
        }

        if (MainMenu.buttonsSprites.get(1).getBoundingRectangle().contains(v.x, v.y)) {
            if (MD.getLevel() == 0) {
                MainMenu.game.setScreen(new LevelLoader(0, MainMenu.game));
                return true;
            }
            MainMenu.game.setScreen(new LevelSelector(MainMenu.game, 1));
            return true;
        }

        if (MainMenu.buttonsSprites.get(0).getBoundingRectangle().contains(v.x, v.y)) {
            MainMenu.game.setScreen(new About(MainMenu.game));
            return true;
        }

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
