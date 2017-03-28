package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.eiilo.mood.dude.MainMenu;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.utils.About;

/**
 * Created by Oussama on 9/17/2015.
 */
public class AboutGestureHandler implements GestureDetector.GestureListener {

    private MoodDude game;

    public AboutGestureHandler (MoodDude game) {
        this.game = game;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 v = new Vector3(x, y, 0);
        About.camera.unproject(v);

        if ( About.homeBtnSprite.getBoundingRectangle().contains(v.x, v.y) ) {
            game.setScreen(new MainMenu(game));
            return true;
        }

        if ( About.eiiloSprite.getBoundingRectangle().contains(v.x, v.y) ) {
            Gdx.net.openURI("http://eiilo.com");
            return true;
        }

        if ( About.kenneySprite.getBoundingRectangle().contains(v.x, v.y) ) {
            Gdx.net.openURI("http://kenney.nl");
            return true;
        }

        if ( About.music1Sprite.getBoundingRectangle().contains(v.x, v.y) ) {
            Gdx.net.openURI("http://www.matthewpablo.com");
            return true;
        }

        if ( About.music2Sprite.getBoundingRectangle().contains(v.x, v.y) ) {
            Gdx.net.openURI("https://soundcloud.com/kevin-9-1/life-of-riley");
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
