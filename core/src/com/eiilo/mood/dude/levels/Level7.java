package com.eiilo.mood.dude.levels;

import com.badlogic.gdx.math.Vector2;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.utils.Level;

import static com.eiilo.mood.dude.env.MD.isPaused;
import static com.eiilo.mood.dude.env.MD.isWon;
import static com.eiilo.mood.dude.env.PlayerVars.isDead;

/**
 * Created by Oussama on 8/23/2015.
 */
public class Level7 extends Level {

    public Level7(MoodDude game) {
        super(game, new Vector2(30, 70), 7, 2);
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if (!isPaused && !isDead && !isWon)
            world.step(delta, 6, 2);
    }

}
