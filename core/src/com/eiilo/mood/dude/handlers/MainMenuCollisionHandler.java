package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.eiilo.mood.dude.MainMenu;

public class MainMenuCollisionHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (((String) fa.getUserData()).startsWith("player")) {
            MainMenu.bodies.remove(fa.getUserData());
            fa.setUserData("delete");
            return;
        }
        if (((String) fb.getUserData()).startsWith("player")) {
            MainMenu.bodies.remove(fb.getUserData());
            fb.setUserData("delete");
            return;
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
