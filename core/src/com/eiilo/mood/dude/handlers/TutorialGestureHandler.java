package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.math.Vector3;
import com.eiilo.mood.dude.levels.Tutorial;
import com.eiilo.mood.dude.utils.Level;

public class TutorialGestureHandler extends GestureHandler {

    @Override
    public void handleRedBoostPressed() {
        if (Tutorial.canShowDialog1) {
            Tutorial.changeColorInstructionIsShown = false;
            Tutorial.canWalkOnColorInstructionIsShown = true;
        }
        super.handleRedBoostPressed();
    }

    @Override
    public void handleGreenBoostPressed() {
        if (Tutorial.canShowDialog2) {
            Tutorial.canChangeColorToWalkThrough = false;
            Tutorial.canWalkThrough = true;
        }
        super.handleGreenBoostPressed();
    }

    @Override
    public void handleBlueBoostPressed() {
        if (Tutorial.canShowDialog2) {
            Tutorial.canChangeColorToWalkThrough = false;
            Tutorial.canWalkThrough = true;
        }
        super.handleBlueBoostPressed();
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 v3 = new Vector3(x, y, 0);
        Level.mainCamera.unproject(v3);
        if ( (Tutorial.canWalkThrough || Tutorial.canWalkOnColorInstructionIsShown) && Tutorial.btnOkay.getBoundingRectangle().contains(v3.x, v3.y)) {
            if (Tutorial.canWalkThrough) {
                Tutorial.canWalkThrough = false;
                Tutorial.canShowDialog2 = false;
                Level.animatePlayer = true;
                return true;
            }
            if (Tutorial.canWalkOnColorInstructionIsShown) {
                Tutorial.canWalkOnColorInstructionIsShown = false;
                Tutorial.canShowDialog1 = false;
                Level.animatePlayer = true;
                return true;
            }
        }
        return super.touchDown(x, y, pointer, button);
    }
}
