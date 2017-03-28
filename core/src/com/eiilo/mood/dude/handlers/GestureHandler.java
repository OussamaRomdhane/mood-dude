package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Filter;
import com.eiilo.mood.dude.MainMenu;
import com.eiilo.mood.dude.env.Box2DVars;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.levels.Level1;
import com.eiilo.mood.dude.utils.Level;

import java.util.Map;

public class GestureHandler implements GestureDetector.GestureListener {

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 v = new Vector3(x,y,0);
        Level.mainCamera.unproject(v);

        //System.out.println("okok");

        if (MD.isPaused || PlayerVars.isDead) {
            if (Level.retryButton.getBoundingRectangle().contains(v.x, v.y) ) {
                Level.game.setScreen(new LevelLoaderSimple(Level.levelIndex, Level.game));
                return true;
            }
            if (Level.homeButton.getBoundingRectangle().contains(v.x, v.y) ) {
                Level.game.setScreen(new MainMenu(Level.game));
                return true;
            }
        }

        if (MD.isWon) {
            if (Level.retrySmallButton.getBoundingRectangle().contains(v.x, v.y) ) {
                Level.game.setScreen(new LevelLoaderSimple(Level.levelIndex, Level.game));
                return true;
            }
            if (Level.homeButton.getBoundingRectangle().contains(v.x, v.y) ) {
                Level.game.setScreen(new MainMenu(Level.game));
                return true;
            }
            if (Level.nextButton.getBoundingRectangle().contains(v.x, v.y) ) {
                Level.game.setScreen(new LevelLoader(Level.levelIndex+1, Level.game));
                return true;
            }
        }

        if (Level.pauseSprite.getBoundingRectangle().contains(v.x, v.y) && !MD.isWon && !PlayerVars.isDead) {
            MD.isPaused = true;
            Level.pauseSprite.setX(-100);
            Level.pauseSprite.setY(-100);
            return true;
        }
        if (Level.resumeSprite.getBoundingRectangle().contains(v.x, v.y) && !MD.isWon && !PlayerVars.isDead) {
            MD.isPaused = false;
            Level.resumeSprite.setX(-100);
            Level.resumeSprite.setY(-100);
            return true;
        }

        for (Map.Entry<String, Sprite> es : Level.moodsSprites.entrySet()){
            if(es.getKey().endsWith("_blank")) continue;
            if (es.getValue().getBoundingRectangle().contains(v.x,v.y) && PlayerVars.AVAILABLE_COLORS.contains(es.getKey().toString())){
                String color = es.getKey();
                PlayerVars.CURRENT_COLOR = color;

                if(color == PlayerVars.GREEN_LABEL) {
                    handleGreenBoostPressed();
                }
                else if(color == PlayerVars.BLUE_LABEL) {
                    handleBlueBoostPressed();
                }
                else if(color.equals(PlayerVars.RED_LABEL)) {
                    handleRedBoostPressed();
                }
                else if(color == PlayerVars.YELLOW_LABEL) {
                    handleYellowBoostPressed();
                }
                else if(color == PlayerVars.BEIGE_LABEL) {
                    handleBeigeBoostPressed();
                }

                return true;
            }
        }
        if (Level.ch.isOnGround > 0 && !MD.isPaused && !PlayerVars.isDead) {
            Level.playerBody.applyForceToCenter(0, 50, true);
            PlayerVars.isJumping = true;
            if (MD.soundsOn) Level.jumpSound.play();
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


    public void handleGreenBoostPressed () {
        Filter fd1 = Level.playerBody.getFixtureList().get(0).getFilterData();
        Filter fd2 = Level.playerBody.getFixtureList().get(1).getFilterData();
        Filter fd3 = Level.playerBody.getFixtureList().get(2).getFilterData();
        fd1.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.GREEN_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT  | Box2DVars.WIN_BIT;
        fd2.maskBits = Box2DVars.GREEN_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        fd3.maskBits = Box2DVars.GREEN_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        Level.playerBody.getFixtureList().get(0).setFilterData(fd1);
        Level.playerBody.getFixtureList().get(1).setFilterData(fd2);
        Level.playerBody.getFixtureList().get(2).setFilterData(fd3);
    }
    public void handleRedBoostPressed () {
        Filter fd1 = Level.playerBody.getFixtureList().get(0).getFilterData();
        Filter fd2 = Level.playerBody.getFixtureList().get(1).getFilterData();
        Filter fd3 = Level.playerBody.getFixtureList().get(2).getFilterData();
        fd1.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.RED_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT | Box2DVars.WIN_BIT;
        fd2.maskBits = Box2DVars.RED_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        fd3.maskBits = Box2DVars.RED_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        Level.playerBody.getFixtureList().get(0).setFilterData(fd1);
        Level.playerBody.getFixtureList().get(1).setFilterData(fd2);
        Level.playerBody.getFixtureList().get(2).setFilterData(fd3);
    }
    public void handleBeigeBoostPressed () {
        Filter fd1 = Level.playerBody.getFixtureList().get(0).getFilterData();
        Filter fd2 = Level.playerBody.getFixtureList().get(1).getFilterData();
        Filter fd3 = Level.playerBody.getFixtureList().get(2).getFilterData();
        fd1.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.BEIGE_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT | Box2DVars.WIN_BIT;
        fd2.maskBits = Box2DVars.BEIGE_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        fd3.maskBits = Box2DVars.BEIGE_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        Level.playerBody.getFixtureList().get(0).setFilterData(fd1);
        Level.playerBody.getFixtureList().get(1).setFilterData(fd2);
        Level.playerBody.getFixtureList().get(2).setFilterData(fd3);
    }
    public void handleBlueBoostPressed () {
        Filter fd1 = Level.playerBody.getFixtureList().get(0).getFilterData();
        Filter fd2 = Level.playerBody.getFixtureList().get(1).getFilterData();
        Filter fd3 = Level.playerBody.getFixtureList().get(2).getFilterData();
        fd1.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.BLUE_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT | Box2DVars.WIN_BIT;
        fd2.maskBits = Box2DVars.BLUE_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        fd3.maskBits = Box2DVars.BLUE_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        Level.playerBody.getFixtureList().get(0).setFilterData(fd1);
        Level.playerBody.getFixtureList().get(1).setFilterData(fd2);
        Level.playerBody.getFixtureList().get(2).setFilterData(fd3);
    }
    public void handleYellowBoostPressed () {
        Filter fd1 = Level.playerBody.getFixtureList().get(0).getFilterData();
        Filter fd2 = Level.playerBody.getFixtureList().get(1).getFilterData();
        Filter fd3 = Level.playerBody.getFixtureList().get(2).getFilterData();
        fd1.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.YELLOW_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT | Box2DVars.WIN_BIT;
        fd2.maskBits = Box2DVars.YELLOW_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        fd3.maskBits = Box2DVars.YELLOW_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        Level.playerBody.getFixtureList().get(0).setFilterData(fd1);
        Level.playerBody.getFixtureList().get(1).setFilterData(fd2);
        Level.playerBody.getFixtureList().get(2).setFilterData(fd3);
    }
}
