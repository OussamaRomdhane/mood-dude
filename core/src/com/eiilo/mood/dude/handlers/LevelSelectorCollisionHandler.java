package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.eiilo.mood.dude.utils.LevelSelector;

public class LevelSelectorCollisionHandler implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() == "player" || fb.getUserData() == "player") {
            if (fa.getUserData() == "limits") {
                if (fb.getBody().getLinearVelocity().x > 0){
                    fb.getBody().setLinearVelocity(-1, 0);
                    LevelSelector.facing = "LEFT";
                }
                else {
                    fb.getBody().setLinearVelocity(1, 0);
                    LevelSelector.facing = "RIGHT";
                }
                if (LevelSelector.currentIndex < 4)
                    LevelSelector.currentIndex ++;
                else
                    LevelSelector.currentIndex = 0;
                LevelSelector.currentColor = LevelSelector.colors[LevelSelector.currentIndex];
                return;
            }
            else if (fb.getUserData() == "limits") {
                if (fa.getBody().getLinearVelocity().x > 0){
                    fa.getBody().setLinearVelocity(-1, 0);
                    LevelSelector.facing = "LEFT";
                }
                else {
                    fa.getBody().setLinearVelocity(1, 0);
                    LevelSelector.facing = "RIGHT";
                }
                if (LevelSelector.currentIndex < 4)
                    LevelSelector.currentIndex ++;
                else
                    LevelSelector.currentIndex = 0;
                LevelSelector.currentColor = LevelSelector.colors[LevelSelector.currentIndex];
                return;
            }
            if (fa.getUserData() == "jump") {
                fb.getBody().applyForceToCenter(0, 100, true);
                return;
            }
            else if (fb.getUserData() == "jump") {
                fa.getBody().applyForceToCenter(0, 100, true);
                return;
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
