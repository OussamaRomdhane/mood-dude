package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.levels.Tutorial;
import com.eiilo.mood.dude.utils.Level;

public class TutorialCollisionHandler extends CollisionHandler {

    @Override
    public void handleRedBoostCollision(Fixture f) {
        f.setUserData("delete");
        System.out.println("123");
        MD.isHUD = true;
        Tutorial.changeColorInstructionIsShown = true;
        Tutorial.canShowDialog1 = true;
        Level.animatePlayer = false;
        super.handleRedBoostCollision(f);
    }

    @Override
    public void handleBlueBoostCollision(Fixture f) {
        f.setUserData("delete");
        MD.isHUD = true;
        Tutorial.canChangeColorToWalkThrough = true;
        Tutorial.canShowDialog2 = true;
        Level.animatePlayer = false;
        super.handleBlueBoostCollision(f);
    }
}
