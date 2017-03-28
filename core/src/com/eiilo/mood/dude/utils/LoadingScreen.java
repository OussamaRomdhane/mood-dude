package com.eiilo.mood.dude.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.eiilo.mood.dude.MainMenu;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.MD;

public class LoadingScreen implements Screen {

    private MoodDude game;

    private float time;

    private boolean loaded;

    private Batch batch;

    private OrthographicCamera camera;

    private Texture background;

    private Texture eiiloLogoTexture;
    private Sprite eiiloLogoSprite;

    private Texture kenneyLogoTexture;
    private Sprite kenneyLogoSprite;

    private Texture musicLogoTexture;
    private Sprite musicLogoSprite;

    public LoadingScreen (MoodDude game) {

        batch = new SpriteBatch();

        camera = new OrthographicCamera(MD.CAMERA_W, MD.CAMERA_H);
        camera.position.set(new Vector3(MD.CAMERA_W / 2, MD.CAMERA_H / 2, 0));
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        eiiloLogoTexture = new Texture(Gdx.files.internal("eiilo.png"));
        eiiloLogoSprite = new Sprite(eiiloLogoTexture);

        kenneyLogoTexture = new Texture(Gdx.files.internal("kenney.png"));
        kenneyLogoSprite = new Sprite(kenneyLogoTexture);

        musicLogoTexture = new Texture(Gdx.files.internal("credits/music.png"));
        musicLogoSprite = new Sprite(musicLogoTexture);

        this.game = game;

        game.assets.load("level_loading.tmx", TiledMap.class);

        game.assets.load("levels_media/brickWall.png", Texture.class);

        game.assets.load("ui_media/icons/pause.png", Texture.class);
        game.assets.load("ui_media/icons/resume.png", Texture.class);

        game.assets.load("ui_media/paused.png", Texture.class);
        game.assets.load("ui_media/lost.png", Texture.class);
        game.assets.load("ui_media/won.png", Texture.class);

        game.assets.load("ui_media/icons/retry.png", Texture.class);
        game.assets.load("ui_media/icons/home.png", Texture.class);
        game.assets.load("ui_media/icons/retry_small.png", Texture.class);
        game.assets.load("ui_media/icons/next_level.png", Texture.class);

        game.assets.load("players_media/enemy/enemy.atlas", TextureAtlas.class);
        game.assets.load("players_media/enemy/dying.png", Texture.class);

        game.assets.load("levels_media/flag/flag.atlas", TextureAtlas.class);

        game.assets.load("players_media/hurt/blue.png", Texture.class);
        game.assets.load("players_media/hurt/red.png", Texture.class);
        game.assets.load("players_media/hurt/beige.png", Texture.class);
        game.assets.load("players_media/hurt/yellow.png", Texture.class);
        game.assets.load("players_media/hurt/green.png", Texture.class);

        game.assets.load("players_media/walking/blue.atlas", TextureAtlas.class);
        game.assets.load("players_media/walking/red.atlas", TextureAtlas.class);
        game.assets.load("players_media/walking/green.atlas", TextureAtlas.class);
        game.assets.load("players_media/walking/yellow.atlas", TextureAtlas.class);
        game.assets.load("players_media/walking/beige.atlas", TextureAtlas.class);

        game.assets.load("players_media/still/blue.png", Texture.class);
        game.assets.load("players_media/still/red.png", Texture.class);
        game.assets.load("players_media/still/green.png", Texture.class);
        game.assets.load("players_media/still/beige.png", Texture.class);
        game.assets.load("players_media/still/yellow.png", Texture.class);

        game.assets.load("players_media/jump/blue.png", Texture.class);
        game.assets.load("players_media/jump/red.png", Texture.class);
        game.assets.load("players_media/jump/green.png", Texture.class);
        game.assets.load("players_media/jump/beige.png", Texture.class);
        game.assets.load("players_media/jump/yellow.png", Texture.class);

        game.assets.load("players_media/boosts/blue.png", Texture.class);
        game.assets.load("players_media/boosts/red.png", Texture.class);
        game.assets.load("players_media/boosts/green.png", Texture.class);
        game.assets.load("players_media/boosts/beige.png", Texture.class);
        game.assets.load("players_media/boosts/yellow.png", Texture.class);

        game.assets.load("players_media/boosts/blue_blank.png", Texture.class);
        game.assets.load("players_media/boosts/red_blank.png", Texture.class);
        game.assets.load("players_media/boosts/green_blank.png", Texture.class);
        game.assets.load("players_media/boosts/beige_blank.png", Texture.class);
        game.assets.load("players_media/boosts/yellow_blank.png", Texture.class);

        game.assets.load("levels_media/boosts/blue.png", Texture.class);
        game.assets.load("levels_media/boosts/red.png", Texture.class);
        game.assets.load("levels_media/boosts/beige.png", Texture.class);
        game.assets.load("levels_media/boosts/yellow.png", Texture.class);

        game.assets.load("ui_media/deleting.png", Texture.class);

        game.assets.load("ui_media/main-menu.png", Texture.class);
        game.assets.load("ui_media/main-menu-title.png", Texture.class);

        game.assets.load("ui_media/aboutBtn.png", Texture.class);
        game.assets.load("ui_media/playBtn.png", Texture.class);
        game.assets.load("ui_media/settingsBtn.png", Texture.class);

        game.assets.load("ui_media/deleteAll.png", Texture.class);
        game.assets.load("ui_media/musicOn.png", Texture.class);
        game.assets.load("ui_media/soundOn.png", Texture.class);
        game.assets.load("ui_media/mute.png", Texture.class);
        game.assets.load("ui_media/deleteAllGrey.png", Texture.class);

        game.assets.load("ui_media/yesBtn.png", Texture.class);
        game.assets.load("ui_media/noBtn.png", Texture.class);

        game.assets.load("about.png", Texture.class);

        game.assets.load("credits/developed.png", Texture.class);
        game.assets.load("credits/music.png", Texture.class);
        game.assets.load("credits/art.png", Texture.class);


    }

    @Override
    public void show() {

        loaded = false;
        time = 0;
        if (!game.mainMenuBackgroundMusic.isPlaying() && MD.musicOn) game.mainMenuBackgroundMusic.play();
        background = new Texture(Gdx.files.internal("ui_media/main-menu.png"));

    }

    @Override
    public void render(float delta) {

        time += Gdx.graphics.getDeltaTime();

        //Clearing the screen
        Gdx.gl.glClearColor(255, 250, 250, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
            if (time >= 3.5f) {
                batch.draw(background, 0, 0);
            }
            if (time < 3.5f)
                eiiloLogoSprite.draw(batch);
            else if (time < 5.5f)
                kenneyLogoSprite.draw(batch);
            else
                musicLogoSprite.draw(batch);
        batch.end();

        if(game.assets.update() ){
            loaded = true;
        }

        if (loaded && time > 7f) game.setScreen(new MainMenu(game));
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
        kenneyLogoTexture.dispose();
        musicLogoTexture.dispose();
        eiiloLogoTexture.dispose();
    }

}
