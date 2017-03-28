package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.Screen;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.levels.Level1;
import com.eiilo.mood.dude.levels.Level10;
import com.eiilo.mood.dude.levels.Level11;
import com.eiilo.mood.dude.levels.Level12;
import com.eiilo.mood.dude.levels.Level13;
import com.eiilo.mood.dude.levels.Level14;
import com.eiilo.mood.dude.levels.Level15;
import com.eiilo.mood.dude.levels.Level16;
import com.eiilo.mood.dude.levels.Level17;
import com.eiilo.mood.dude.levels.Level18;
import com.eiilo.mood.dude.levels.Level2;
import com.eiilo.mood.dude.levels.Level3;
import com.eiilo.mood.dude.levels.Level4;
import com.eiilo.mood.dude.levels.Level5;
import com.eiilo.mood.dude.levels.Level6;
import com.eiilo.mood.dude.levels.Level7;
import com.eiilo.mood.dude.levels.Level8;
import com.eiilo.mood.dude.levels.Level9;
import com.eiilo.mood.dude.levels.Tutorial;

public class LevelLoaderSimple implements Screen {

    private int levelIndex;
    private MoodDude game;

    public LevelLoaderSimple (int levelIndex, MoodDude game){
        this.levelIndex = levelIndex;
        this.game = game;

    }

    @Override
    public void show() {
        switch (levelIndex){
            case 0 :
                game.setScreen(new Tutorial(game));
                break;
            case 1 :
                game.setScreen(new Level1(game));
                break;
            case 2 :
                game.setScreen(new Level2(game));
                break;
            case 3 :
                game.setScreen(new Level3(game));
                break;
            case 4 :
                game.setScreen(new Level4(game));
                break;
            case 5 :
                game.setScreen(new Level5(game));
                break;
            case 6 :
                game.setScreen(new Level6(game));
                break;
            case 7 :
                game.setScreen(new Level7(game));
                break;
            case 8 :
                game.setScreen(new Level8(game));
                break;
            case 9 :
                game.setScreen(new Level9(game));
                break;
            case 10 :
                game.setScreen(new Level10(game));
                break;
            case 11 :
                game.setScreen(new Level11(game));
                break;
            case 12 :
                game.setScreen(new Level12(game));
                break;
            case 13 :
                game.setScreen(new Level13(game));
                break;
            case 14 :
                game.setScreen(new Level14(game));
                break;
            case 15 :
                game.setScreen(new Level15(game));
                break;
            case 16 :
                game.setScreen(new Level16(game));
                break;
            case 17 :
                game.setScreen(new Level17(game));
                break;
            case 18 :
                game.setScreen(new Level18(game));
                break;
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }

}
