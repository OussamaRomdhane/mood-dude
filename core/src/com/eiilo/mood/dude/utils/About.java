package com.eiilo.mood.dude.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.handlers.AboutGestureHandler;
import com.eiilo.mood.dude.handlers.GestureHandler;

public class About implements Screen {

    private Batch batch;

    private MoodDude game;

    private Texture texture;

    private TiledMap background;
    private MapRenderer renderer;

    public static OrthographicCamera camera;

    private Texture aboutTexture;
    private Sprite aboutSprite;

    private Texture homeBtnTexture;
    public static Sprite homeBtnSprite;

    public static Sprite eiiloSprite;
    public static Sprite kenneySprite;
    public static Sprite music1Sprite;
    public static Sprite music2Sprite;

    public About(MoodDude game) {

        this.game = game;

        texture = new Texture(Gdx.files.internal("levels_media/bg_castle.png"));

        batch = new SpriteBatch();

        background = game.assets.get("level_loading.tmx", TiledMap.class);

        camera = new OrthographicCamera(MD.CAMERA_W, MD.CAMERA_H);
        camera.position.x = MD.CAMERA_W / 2;
        camera.position.y = MD.CAMERA_H / 2;
        camera.update();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        renderer = new OrthogonalTiledMapRenderer(background);
        renderer.setView(camera);

        aboutTexture = game.assets.get("about.png", Texture.class);

        aboutSprite = new Sprite(aboutTexture);

        homeBtnTexture = new Texture(Gdx.files.internal("ui_media/homebtn.png"));
        homeBtnSprite = new Sprite(homeBtnTexture);
        homeBtnSprite.setPosition(5, MD.CAMERA_H - 40);

        GestureDetector gd = new GestureDetector(new AboutGestureHandler(game));
        Gdx.input.setInputProcessor(gd);

        eiiloSprite = new Sprite(texture, 0, 0, 410, 65);
        kenneySprite = new Sprite(texture, 0, 0, 230, 70);
        music1Sprite = new Sprite(texture, 0, 0, 230, 35);
        music2Sprite = new Sprite(texture, 0, 0, 260, 30);

    }

    @Override
    public void show() {

        eiiloSprite.setPosition(25, 200);
        kenneySprite.setPosition(100, 100);
        music1Sprite.setPosition(60, 50);
        music2Sprite.setPosition(60, 10);

    }

    @Override
    public void render(float delta) {

        //Clearing the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        batch.begin();
        aboutSprite.draw(batch);
        homeBtnSprite.draw(batch);
        batch.end();

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

        background.dispose();
        batch.dispose();
        texture.dispose();
        aboutTexture.dispose();
        homeBtnTexture.dispose();

    }

}
