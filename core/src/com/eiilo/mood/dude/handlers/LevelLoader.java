package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.levels.Level1;
import com.eiilo.mood.dude.levels.Level10;
import com.eiilo.mood.dude.levels.Level11;
import com.eiilo.mood.dude.levels.Level12;
import com.eiilo.mood.dude.levels.Level13;
import com.eiilo.mood.dude.levels.Level14;
import com.eiilo.mood.dude.levels.Level15;
import com.eiilo.mood.dude.levels.Level16;
import com.eiilo.mood.dude.levels.Level17;
import com.eiilo.mood.dude.levels.Level18;
import com.eiilo.mood.dude.levels.Level2;
import com.eiilo.mood.dude.levels.Level3;
import com.eiilo.mood.dude.levels.Level4;
import com.eiilo.mood.dude.levels.Level5;
import com.eiilo.mood.dude.levels.Level6;
import com.eiilo.mood.dude.levels.Level7;
import com.eiilo.mood.dude.levels.Level8;
import com.eiilo.mood.dude.levels.Level9;
import com.eiilo.mood.dude.levels.Tutorial;

import java.util.ArrayList;

public class LevelLoader implements Screen {

    private Batch batch;
    private boolean assetsLoaded;

    private NinePatch empty, full;
    private Texture container, filler;

    private int levelIndex = 1;
    private float elapsedTime;
    private MoodDude game;

    private ArrayList<Texture> levelNumberTextures;
    private Sprite levelNumberSprite;

    private TiledMap background;
    private MapRenderer renderer;

    private OrthographicCamera camera;

    public LevelLoader (int levelIndex, MoodDude game){
        elapsedTime = 0;
        this.levelIndex =  levelIndex;
        this.game = game;

        assetsLoaded = false;

        container = new Texture(Gdx.files.internal("loading/container.png"));
        filler = new Texture(Gdx.files.internal("loading/filler.png"));
        empty = new NinePatch(new TextureRegion(container,24,24),8,8,8,8);
        full = new NinePatch(new TextureRegion(filler,24,24),8,8,8,8);

        levelNumberTextures = new ArrayList<Texture>();
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level0.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level1.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level2.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level3.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level4.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level5.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level6.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level7.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level8.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level9.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level10.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level11.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level12.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level13.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level14.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level15.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level16.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level17.png")));
        levelNumberTextures.add(new Texture(Gdx.files.internal("levels_numbers/level18.png")));

        levelNumberSprite = new Sprite(levelNumberTextures.get(levelIndex));
        levelNumberSprite.setScale(1.5f);
        levelNumberSprite.setPosition(MD.CAMERA_W/2 -140, MD.CAMERA_H/2 -50);

        background = game.assets.get("level_loading.tmx", TiledMap.class);
        renderer = new OrthogonalTiledMapRenderer(background);

        camera = new OrthographicCamera(MD.CAMERA_W, MD.CAMERA_H);
        camera.position.x = MD.CAMERA_W/2;
        camera.position.y = MD.CAMERA_H/2;
        camera.update();

        renderer.setView(camera);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
        if (levelIndex == 0)
            game.assets.load("levels_media/tutorial.tmx", TiledMap.class);
        else
            game.assets.load("levels_media/level"+levelIndex+".tmx", TiledMap.class);
    }

    @Override
    public void render(float delta) {

        //Clearing the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(game.assets.update()){
            assetsLoaded = true;
        }

        elapsedTime += Gdx.graphics.getDeltaTime();
        renderer.render();

        batch.begin();
        levelNumberSprite.draw(batch);
        empty.draw(batch, 15, MD.CAMERA_H/2 - 50, 450, 20);
        full.draw(batch, 15, MD.CAMERA_H/2 - 50, game.assets.getProgress()*450, 20);
        batch.end();

        switch (levelIndex){
            case 0 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Tutorial(game));
                break;
            case 1 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level1(game));
                break;
            case 2 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level2(game));
                break;
            case 3 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level3(game));
                break;
            case 4 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level4(game));
                break;
            case 5 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level5(game));
                break;
            case 6 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level6(game));
                break;
            case 7 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level7(game));
                break;
            case 8 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level8(game));
                break;
            case 9 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level9(game));
                break;
            case 10 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level10(game));
                break;
            case 11 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level11(game));
                break;
            case 12 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level12(game));
                break;
            case 13 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level13(game));
                break;
            case 14 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level14(game));
                break;
            case 15 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level15(game));
                break;
            case 16 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level16(game));
                break;
            case 17 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level17(game));
                break;
            case 18 :
                if (assetsLoaded && elapsedTime>= 2)
                    game.setScreen(new Level18(game));
                break;
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
