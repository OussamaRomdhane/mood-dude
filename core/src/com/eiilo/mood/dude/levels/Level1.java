package com.eiilo.mood.dude.levels;

import com.badlogic.gdx.math.Vector2;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.utils.Level;

import static com.eiilo.mood.dude.env.MD.isPaused;
import static com.eiilo.mood.dude.env.MD.isWon;
import static com.eiilo.mood.dude.env.PlayerVars.isDead;

public class Level1 extends Level {

    public Level1(MoodDude game) {
        super(game, new Vector2(30, 70), 1);
    }

    @Override
    public void show(){
        super.show();

    }

    @Override
    public void render(float delta){
        super.render(delta);
        if (!isPaused && !isDead && !isWon)
            world.step(delta, 6, 2);
    }

}
