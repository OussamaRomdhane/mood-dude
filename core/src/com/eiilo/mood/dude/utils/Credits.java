package com.eiilo.mood.dude.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eiilo.mood.dude.MainMenu;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.MD;

public class Credits implements Screen {

    private final int CREDITS_SPRITES_COUNT = 3;
    private final float speed = 1.5f;

    private Batch batch;

    private MoodDude game;

    private TiledMap background;
    private MapRenderer renderer;

    private OrthographicCamera camera;

    private Vector2[] creditsSpritesPosition;

    private Array<Texture> creditsTexture;
    private Array<Sprite> creditsSprites;

    public Credits (MoodDude game) {

        creditsTexture = new Array<Texture>();
        creditsSprites = new Array<Sprite>();

        creditsTexture.add(game.assets.get("credits/developed.png", Texture.class));
        creditsTexture.add(game.assets.get("credits/art.png", Texture.class));
        creditsTexture.add(game.assets.get("credits/music.png", Texture.class));

        creditsSpritesPosition = new Vector2[CREDITS_SPRITES_COUNT];

        creditsSprites.add(new Sprite(creditsTexture.get(0)));
        creditsSpritesPosition[0] = new Vector2(MD.CAMERA_W/2 - creditsSprites.get(0).getWidth()/2, 0 - creditsSprites.get(0).getHeight() - 10);
        creditsSprites.get(0).setPosition(creditsSpritesPosition[0].x, creditsSpritesPosition[0].y);
        creditsSprites.add(new Sprite(creditsTexture.get(1)));
        creditsSpritesPosition[1] = new Vector2(MD.CAMERA_W/2 - creditsSprites.get(1).getWidth()/2, 0 - (creditsSprites.get(0).getHeight() + creditsSprites.get(1).getHeight()) - 20);
        creditsSprites.get(1).setPosition(creditsSpritesPosition[1].x, creditsSpritesPosition[1].y);

        creditsSprites.add(new Sprite(creditsTexture.get(2)));
        creditsSpritesPosition[2] = new Vector2(MD.CAMERA_W/2 - creditsSprites.get(2).getWidth()/2, 0 - (creditsSprites.get(0).getHeight() + creditsSprites.get(1).getHeight() + creditsSprites.get(2).getHeight()) - 20);
        creditsSprites.get(2).setPosition(creditsSpritesPosition[2].x, creditsSpritesPosition[2].y);

        background = game.assets.get("level_loading.tmx", TiledMap.class);

        this.game = game;

        camera = new OrthographicCamera(MD.CAMERA_W, MD.CAMERA_H);
        camera.position.x = MD.CAMERA_W/2;
        camera.position.y = MD.CAMERA_H/2;
        camera.update();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        renderer = new OrthogonalTiledMapRenderer(background);
        renderer.setView(camera);
    }

    @Override
    public void show() {

        if (MoodDude.mainBackgroundMusic.isPlaying()) MoodDude.mainBackgroundMusic.stop();
        if (MoodDude.mainMenuBackgroundMusic.isPlaying()) MoodDude.mainMenuBackgroundMusic.stop();
        if (MD.musicOn) MoodDude.creditsBackgroundMusic.play();

    }

    @Override
    public void render(float delta) {

        //Clearing the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        batch.begin();
            creditsSprites.get(0).setPosition(creditsSprites.get(0).getX(), creditsSprites.get(0).getY() + 1 * speed);
            creditsSprites.get(1).setPosition(creditsSprites.get(1).getX(), creditsSprites.get(1).getY() + 1 * speed);
            creditsSprites.get(2).setPosition(creditsSprites.get(2).getX(), creditsSprites.get(2).getY() + 1 * speed);
            creditsSprites.get(0).draw(batch);
            creditsSprites.get(1).draw(batch);
            creditsSprites.get(2).draw(batch);
        batch.end();

        if (creditsSprites.get(CREDITS_SPRITES_COUNT-1).getY() >= MD.CAMERA_H + 5) {
            if (MoodDude.creditsBackgroundMusic.isPlaying()) MoodDude.creditsBackgroundMusic.stop();
            game.setScreen(new MainMenu(game));
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
        batch.dispose();
        background.dispose();
        for (Texture t : creditsTexture)
            t.dispose();
    }
}
