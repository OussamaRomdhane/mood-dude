package com.eiilo.mood.dude.handlers;

import static com.eiilo.mood.dude.env.Box2DVars.FOOT_UD;
import static com.eiilo.mood.dude.env.Box2DVars.PLAYER_LABEL;
import static com.eiilo.mood.dude.env.Box2DVars.SIDE_UD;
import static com.eiilo.mood.dude.env.Box2DVars.PLAYER_UD;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.eiilo.mood.dude.env.Box2DVars;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.utils.Credits;
import com.eiilo.mood.dude.utils.Level;

public class CollisionHandler implements ContactListener {

    public int isOnGround = 0;
    public boolean jumping = false;

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        //System.out.println();
        //System.out.println("fa : " + (String) fa.getUserData());
        //System.out.println("fb : " + (String) fb.getUserData());
        //System.out.println();

        if (fa.getUserData() != null)
            if ((fa.getUserData().toString() == "mo" && fb.getUserData().toString() == FOOT_UD) || (fa.getUserData().toString() == FOOT_UD && fb.getUserData().toString() == "mo"))
                System.out.println("is On Ground : " + isOnGround);

        if ((fa.getUserData() != null && fa.getUserData().equals(FOOT_UD)) || (fb.getUserData() != null && fb.getUserData().equals(FOOT_UD))) {

            if (fa.getUserData().toString().startsWith("the-enemy-")) {
                fa.getBody().setUserData("dying");
                fa.getBody().setGravityScale(1);
            }
            else if (fb.getUserData().toString().startsWith("the-enemy-")) {
                fb.getBody().setUserData("dying");
                fb.getBody().setGravityScale(1);
            }

            if (jumping == true) {
                jumping = false;
                PlayerVars.isJumping = false;
            }
            isOnGround ++;
            return;
        }

        if (fa.getUserData().equals(Box2DVars.WIN_LABEL) || fb.getUserData().equals(Box2DVars.WIN_LABEL)){

                MD.isWon = true;
                if (Level.levelIndex == MD.LEVELS_COUNT) {
                    Level.game.setScreen(new Credits (Level.game));
                    return;
                }
                if (MD.getLevel() <= Level.levelIndex) {
                    MD.setLevel(Level.levelIndex+1);
                    MD.lastLevel = MD.getLevel();
                }
                return;

        }

        if (fa.getUserData() == SIDE_UD || fb.getUserData() == SIDE_UD && !(fa.getUserData().equals(Box2DVars.WIN_LABEL) || fb.getUserData().equals(Box2DVars.WIN_LABEL))) {
            PlayerVars.isDying = true;
            PlayerVars.AVAILABLE_COLORS.clear();
            Level.playerBody.setLinearVelocity(0, 0);
            Level.playerBody.applyForceToCenter(new Vector2(-10, 20) ,true);
            Filter fd1 = Level.playerBody.getFixtureList().get(0).getFilterData();
            Filter fd2 = Level.playerBody.getFixtureList().get(1).getFilterData();
            Filter fd3 = Level.playerBody.getFixtureList().get(2).getFilterData();
            fd1.maskBits = 0;
            fd2.maskBits = 0;
            fd3.maskBits = 0;
            Level.playerBody.getFixtureList().get(0).setFilterData(fd1);
            Level.playerBody.getFixtureList().get(1).setFilterData(fd2);
            Level.playerBody.getFixtureList().get(2).setFilterData(fd3);
            return;
        }

        if (fa.getUserData().equals(Box2DVars.BEIGE_BOOST_LABEL) || fa.getUserData().equals(Box2DVars.RED_BOOST_LABEL)
                || fa.getUserData().equals(Box2DVars.BLUE_BOOST_LABEL) || fa.getUserData().equals(Box2DVars.BEIGE_BOOST_LABEL)
                || fa.getUserData().equals(Box2DVars.YELLOW_BOOST_LABEL)){
            String tmp = (String) fa.getUserData();
            Level.boostsAreTaken.remove(tmp);
            Level.boostsAreTaken.put(tmp, true);

            if (tmp == Box2DVars.BEIGE_BOOST_LABEL)
                handleBeigeBoostCollision (fa);
            else if (tmp == Box2DVars.RED_BOOST_LABEL)
                handleRedBoostCollision(fa);
            else if (tmp == Box2DVars.BLUE_BOOST_LABEL)
                handleBlueBoostCollision(fa);
            else if (tmp == Box2DVars.YELLOW_BOOST_LABEL)
                handleYellowBoostCollision(fa);

            return;
        }
        if (fb.getUserData() != null) {
            if (fb.getUserData().equals(Box2DVars.BEIGE_BOOST_LABEL) || fb.getUserData().equals(Box2DVars.RED_BOOST_LABEL)
                    || fb.getUserData().equals(Box2DVars.BLUE_BOOST_LABEL) || fb.getUserData().equals(Box2DVars.BEIGE_BOOST_LABEL)
                    || fb.getUserData().equals(Box2DVars.YELLOW_BOOST_LABEL)){
                String tmp = (String) fb.getUserData();
                Level.boostsAreTaken.remove(tmp);
                Level.boostsAreTaken.put(tmp, true);

                if (tmp == Box2DVars.BEIGE_BOOST_LABEL)
                    handleBeigeBoostCollision(fb);
                else if (tmp == Box2DVars.RED_BOOST_LABEL)
                    handleRedBoostCollision(fb);
                else if (tmp == Box2DVars.BLUE_BOOST_LABEL)
                    handleBlueBoostCollision(fb);
                else if (tmp == Box2DVars.YELLOW_BOOST_LABEL)
                    handleYellowBoostCollision(fb);

                //System.out.println("yo : " + Level.boostsAreTaken.get(tmp));
                return;
            }
            else if (fb.getUserData().toString().startsWith("the-enemy-")) {

                int enemyNumber = Integer.parseInt(fb.getUserData().toString().split("-")[2]);

                if (fa.getUserData().equals("limit-end")) {
                    fb.getBody().setLinearVelocity(-1, 0);
                    Level.enemyIsFacing.set(enemyNumber, "left");
                }
                else if (fa.getUserData().equals("limit-begin")) {
                    fb.getBody().setLinearVelocity(1, 0);
                    Level.enemyIsFacing.set(enemyNumber, "right");
                }

            }
            else if (fa.getUserData().toString().startsWith("the-enemy-")) {

                int enemyNumber = Integer.parseInt(fa.getUserData().toString().split("-")[2]);

                if (fb.getUserData().equals("limit-end")) {
                    fa.getBody().setLinearVelocity(-1, 0);
                    Level.enemyIsFacing.set(enemyNumber, "left");
                }
                else if (fb.getUserData().equals("limit-begin")) {
                    fa.getBody().setLinearVelocity(1, 0);
                    Level.enemyIsFacing.set(enemyNumber, "right");
                }

            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals(FOOT_UD)) {
            isOnGround --;
        }
        if (fb.getUserData() != null && fb.getUserData().equals(FOOT_UD)) {
            isOnGround --;
        }
        if (PlayerVars.isJumping) {
            jumping = true;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void handleBeigeBoostCollision(Fixture f) {
        PlayerVars.AVAILABLE_COLORS.add(PlayerVars.BEIGE_LABEL);
        Level.moodSound.play();
    }

    public void handleRedBoostCollision(Fixture f) {
        PlayerVars.AVAILABLE_COLORS.add(PlayerVars.RED_LABEL);
        Level.moodSound.play();
    }

    public void handleBlueBoostCollision(Fixture f) {
        PlayerVars.AVAILABLE_COLORS.add(PlayerVars.BLUE_LABEL);
        Level.moodSound.play();
    }

    public void handleYellowBoostCollision(Fixture f) {
        PlayerVars.AVAILABLE_COLORS.add(PlayerVars.YELLOW_LABEL);
        Level.moodSound.play();
    }

}
