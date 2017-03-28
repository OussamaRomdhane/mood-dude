package com.eiilo.mood.dude.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.handlers.CollisionHandler;
import com.eiilo.mood.dude.handlers.GestureHandler;
import com.eiilo.mood.dude.handlers.TutorialCollisionHandler;
import com.eiilo.mood.dude.handlers.TutorialGestureHandler;
import com.eiilo.mood.dude.utils.Level;

import static com.eiilo.mood.dude.env.MD.isPaused;
import static com.eiilo.mood.dude.env.MD.isWon;
import static com.eiilo.mood.dude.env.PlayerVars.isDead;

public class Tutorial extends Level {

    public static boolean changeColorInstructionIsShown;
    public static boolean canWalkOnColorInstructionIsShown;
    public static boolean canWalkThrough;
    public static boolean canChangeColorToWalkThrough;

    public static boolean canShowDialog1;
    public static boolean canShowDialog2;

    Texture tapToJump, changeColor, here, canChangeColor, passingThroughWalls;
    private static Texture btnOkayTexture;
    public static Sprite btnOkay;

    public Tutorial(MoodDude game) {
        super(game, new Vector2(30,70), 0);
        tapToJump = new Texture(Gdx.files.internal("ui_media/tap_to_jump.png"));
        changeColor = new Texture(Gdx.files.internal("ui_media/change_color.png"));
        here = new Texture(Gdx.files.internal("ui_media/here.png"));
        canChangeColor = new Texture(Gdx.files.internal("ui_media/mood_change.png"));
        btnOkayTexture = new Texture(Gdx.files.internal("ui_media/okayBtn.png"));
        passingThroughWalls = new Texture(Gdx.files.internal("ui_media/pass_through.png"));
        btnOkay = new Sprite(btnOkayTexture);
        btnOkay.setPosition(-150, -150);

        changeColorInstructionIsShown = false;
        canWalkOnColorInstructionIsShown = false;
        canWalkThrough = false;
        canChangeColorToWalkThrough = false;

        canShowDialog1 = false;
        canShowDialog2 = false;
    }

    @Override
    public void show(){
        super.show();
    }

    @Override
    public void render(float delta){
        super.render(delta);

        Array<Body> cBodies = new Array<Body>();
        world.getBodies(cBodies);

        for (Body b : cBodies){
            if (b.getFixtureList().get(0).getUserData() == "delete") world.destroyBody(b);
        }

        if (!isPaused && !isDead && !isWon && !changeColorInstructionIsShown && !canWalkOnColorInstructionIsShown && !canChangeColorToWalkThrough && !canWalkThrough)
            world.step(delta, 6, 2);
    }

    @Override
    public void renderBeforePlayer() {
        super.renderBeforePlayer();
        batch.draw(tapToJump, 320, 80);
        if (changeColorInstructionIsShown & !isPaused) {
            batch.draw(changeColor, 965, 100);
            batch.draw(here, (Level.mainCamera.position.x - MD.CAMERA_W/2) + 55, 205);
        }
        if (canChangeColorToWalkThrough & !isPaused) {
            batch.draw(here, (Level.mainCamera.position.x - MD.CAMERA_W/2) + 105, 205);
            batch.draw(here, (Level.mainCamera.position.x - MD.CAMERA_W/2) + 5, 205);
        }
    }

    @Override
    public void renderOnForeground() {
        super.renderOnForeground();
        if (canWalkOnColorInstructionIsShown) {
            batch.draw(canChangeColor, Level.mainCamera.position.x - MD.CAMERA_W/2, Level.mainCamera.position.y - MD.CAMERA_H/2);
            btnOkay.setPosition(Level.mainCamera.position.x - 20, Level.mainCamera.position.y - 70);
            btnOkay.draw(batch);
        }
        if (canWalkThrough) {
            batch.draw(passingThroughWalls, Level.mainCamera.position.x - MD.CAMERA_W/2, Level.mainCamera.position.y - MD.CAMERA_H/2);
            btnOkay.setPosition(Level.mainCamera.position.x - 70, Level.mainCamera.position.y - 70);
            btnOkay.draw(batch);
        }
    }

    @Override
    public void setGestureHandler() {

        GestureDetector gd = new GestureDetector(new TutorialGestureHandler());
        Gdx.input.setInputProcessor(gd);

    }

    @Override
    public void setCollisionHandler() {

        super.ch = new TutorialCollisionHandler();
        super.world.setContactListener(super.ch);

    }
}
