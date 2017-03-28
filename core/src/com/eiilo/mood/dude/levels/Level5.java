package com.eiilo.mood.dude.levels;

import com.badlogic.gdx.math.Vector2;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.utils.Level;

import static com.eiilo.mood.dude.env.MD.isPaused;
import static com.eiilo.mood.dude.env.MD.isWon;
import static com.eiilo.mood.dude.env.PlayerVars.isDead;

public class Level5 extends Level {

    public Level5(MoodDude game) {
        super(game, new Vector2(30, 100), 5);
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if (!isPaused && !isDead && !isWon)
            world.step(delta, 6, 2);
    }

}
