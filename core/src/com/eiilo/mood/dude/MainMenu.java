package com.eiilo.mood.dude;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.eiilo.mood.dude.env.Box2DVars;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.handlers.MainMenuCollisionHandler;
import com.eiilo.mood.dude.handlers.MainMenuGestureHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.eiilo.mood.dude.env.Box2DVars.NORMAL_GROUND_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.PLAYER_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.PPM;

public class MainMenu implements Screen {

    private Texture isDeletingTexture;

    private static ArrayList<Texture> yesNoButtonsTextures;
    public static ArrayList<Sprite> yesNoButtonsSprites;

    public static boolean settingsClicked;

    private static ArrayList<Texture> buttonsTextures;
    public static ArrayList<Sprite> buttonsSprites;

    private static ArrayList<Texture> buttonsSettingsTextures;
    public static ArrayList<Sprite> buttonsSettingsSprites;

    private static ArrayList<Texture> dudesTextures;

    public static HashMap<String, Body> bodies;

    public static boolean isDeleting;

    private SpriteBatch batch;

    private Texture background, title;

    public static MoodDude game;

    public static World world;
    Box2DDebugRenderer b2dr;

    public static OrthographicCamera mainCamera;
    private OrthographicCamera b2dCamera;

    private void puttBodies () {
            BodyDef bDef = new BodyDef();
            FixtureDef fDef = new FixtureDef();

            bDef.type = BodyDef.BodyType.DynamicBody;
            float x = 0;
            x = (float) Math.random() * (MD.CAMERA_W ) - 100;
            x /= PPM;
            float y = 350/PPM;

            bDef.position.set(x, y);

            PolygonShape box = new PolygonShape();
            box.setAsBox(PlayerVars.WIDTH/4/PPM, PlayerVars.HEIGHT/4/PPM);

            fDef.shape = box;
            fDef.friction = 0;

            fDef.filter.categoryBits = Box2DVars.PLAYER_BIT;

            fDef.filter.maskBits = NORMAL_GROUND_BIT;
            Body player = world.createBody(bDef);
            player.setLinearVelocity(1, 0);
            java.util.Date date= new java.util.Date();
            String tmp = "player" + new Timestamp(date.getTime());
            player.createFixture(fDef).setUserData(tmp);

            bodies.put(tmp, player);
    }

    public MainMenu (MoodDude game) {
        this.game = game;

        isDeleting = false;

        settingsClicked = false;

        isDeletingTexture = game.assets.get("ui_media/deleting.png", Texture.class);

        buttonsSprites = new ArrayList<Sprite>();
        buttonsTextures = new ArrayList<Texture>();

        buttonsSettingsSprites = new ArrayList<Sprite>();
        buttonsSettingsTextures = new ArrayList<Texture>();

        yesNoButtonsSprites = new ArrayList<Sprite>();
        yesNoButtonsTextures = new ArrayList<Texture>();

        background = game.assets.get("ui_media/main-menu.png", Texture.class);
        title = game.assets.get("ui_media/main-menu-title.png", Texture.class);

        buttonsTextures.add(game.assets.get("ui_media/aboutBtn.png", Texture.class));
        buttonsTextures.add(game.assets.get("ui_media/playBtn.png", Texture.class));
        buttonsTextures.add(game.assets.get("ui_media/settingsBtn.png", Texture.class));

        buttonsSprites.add(new Sprite(buttonsTextures.get(0)));
        buttonsSprites.get(0).setPosition(80, 30);
        buttonsSprites.add(new Sprite(buttonsTextures.get(1)));
        buttonsSprites.get(1).setPosition(145, 30);
        buttonsSprites.add(new Sprite(buttonsTextures.get(2)));
        buttonsSprites.get(2).setPosition(350, 30);

        buttonsSettingsTextures.add(game.assets.get("ui_media/deleteAll.png", Texture.class));
        buttonsSettingsTextures.add(game.assets.get("ui_media/musicOn.png", Texture.class));
        buttonsSettingsTextures.add(game.assets.get("ui_media/soundOn.png", Texture.class));
        buttonsSettingsTextures.add(game.assets.get("ui_media/mute.png", Texture.class));
        buttonsSettingsTextures.add(game.assets.get("ui_media/deleteAllGrey.png", Texture.class));

        buttonsSettingsSprites.add(new Sprite(buttonsSettingsTextures.get(0)));
        buttonsSettingsSprites.get(0).setPosition(355, 38);
        buttonsSettingsSprites.add(new Sprite(buttonsSettingsTextures.get(1)));
        buttonsSettingsSprites.get(1).setPosition(405, 38);
        buttonsSettingsSprites.add(new Sprite(buttonsSettingsTextures.get(2)));
        buttonsSettingsSprites.get(2).setPosition(405, 38);
        buttonsSettingsSprites.add(new Sprite(buttonsSettingsTextures.get(3)));
        buttonsSettingsSprites.get(3).setPosition(405, 38);
        buttonsSettingsSprites.add(new Sprite(buttonsSettingsTextures.get(4)));
        buttonsSettingsSprites.get(4).setPosition(355, 38);

        bodies = new HashMap<String, Body>();
        dudesTextures = new ArrayList<Texture>();

        dudesTextures.add(game.assets.get("players_media/jump/blue.png", Texture.class));
        dudesTextures.add(game.assets.get("players_media/jump/red.png", Texture.class));
        dudesTextures.add(game.assets.get("players_media/jump/green.png", Texture.class));
        dudesTextures.add(game.assets.get("players_media/jump/beige.png", Texture.class));
        dudesTextures.add(game.assets.get("players_media/jump/yellow.png", Texture.class));
        dudesTextures.add(game.assets.get("players_media/jump/blue.png", Texture.class));

        yesNoButtonsTextures.add(game.assets.get("ui_media/yesBtn.png", Texture.class));
        yesNoButtonsTextures.add(game.assets.get("ui_media/noBtn.png", Texture.class));

        yesNoButtonsSprites.add(new Sprite(yesNoButtonsTextures.get(0)));
        yesNoButtonsSprites.add(new Sprite(yesNoButtonsTextures.get(1)));

        yesNoButtonsSprites.get(0).setPosition(160, 117);
        yesNoButtonsSprites.get(1).setPosition(250, 117);
    }

    @Override
    public void show() {

        if (game.mainBackgroundMusic.isPlaying()) game.mainBackgroundMusic.pause();
        if (!game.mainMenuBackgroundMusic.isPlaying() && MD.musicOn) game.mainMenuBackgroundMusic.play();

        //Setting the main camera
        mainCamera = new OrthographicCamera(MD.CAMERA_W,MD.CAMERA_H);
        mainCamera.position.x = MD.CAMERA_W/2;
        mainCamera.position.y = MD.CAMERA_H/2;
        mainCamera.update();

        //Box2D camera
        b2dCamera = new OrthographicCamera(MD.CAMERA_W/PPM,MD.CAMERA_H/PPM);
        b2dCamera.position.x = MD.CAMERA_W/PPM/2;
        b2dCamera.position.y = MD.CAMERA_H/PPM/2;
        b2dCamera.update();

        batch = new SpriteBatch();

        batch.setProjectionMatrix(mainCamera.combined);

        //Setting up the world with earth gravity
        world = new World(new Vector2(0,-9.81f),true);

        //Setting up the world renderer
        b2dr = new Box2DDebugRenderer();

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.StaticBody;
        float x = MD.CAMERA_W/2/PPM;
        float y = -100/PPM;

        bDef.position.set(x, y);

        PolygonShape box = new PolygonShape();
        box.setAsBox(300/PPM, 10/PPM);

        fDef.shape = box;
        fDef.friction = 0;

        fDef.filter.categoryBits = Box2DVars.NORMAL_GROUND_BIT;

        fDef.filter.maskBits = PLAYER_BIT;
        Body player = world.createBody(bDef);
        player.createFixture(fDef).setUserData("ground");

        world.setContactListener(new MainMenuCollisionHandler());

        GestureDetector gd = new GestureDetector(new MainMenuGestureHandler());
        Gdx.input.setInputProcessor(gd);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        puttBodies();

        batch.begin();

        //b2dr.render(world, b2dCamera.combined);

        batch.draw(background, 0, 0);

        for (Map.Entry<String, Body> en : bodies.entrySet()) {
            batch.draw(dudesTextures.get((int) Math.round(Math.random() * 5)), en.getValue().getPosition().x * PPM, en.getValue().getPosition().y * PPM);
        }

        batch.draw(title, 0, 0);
        int counter = 0;
        for (Sprite s : buttonsSprites) {
            if (counter < 2)
                s.draw(batch);
            else
                if (! settingsClicked)
                    s.draw(batch);

            counter ++;
        }
        counter = 0;
        if (settingsClicked)
            for (Sprite s : buttonsSettingsSprites) {
                if (counter == 0) {
                    if (MD.lastLevel > 0)
                        s.draw(batch);
                }
                else if (counter == 1) {
                    if (MD.musicOn) {
                        s.draw(batch);
                    }
                }
                else if (counter == 2) {
                    if (MD.soundsOn && !MD.musicOn)
                        s.draw(batch);
                }
                else if (counter == 3) {
                    if (!MD.soundsOn && !MD.musicOn)
                        s.draw(batch);
                }
                else if (counter == 4) {
                    if (MD.lastLevel == 0)
                        s.draw(batch);
                }
                counter ++;
            }

            if (isDeleting) {
                batch.draw(isDeletingTexture, 0, 0);
                yesNoButtonsSprites.get(0).draw(batch);
                yesNoButtonsSprites.get(1).draw(batch);
            }

        batch.end();
        world.step(delta, 6, 2);

        Array<Body> cBodies = new Array<Body>();
        world.getBodies(cBodies);

        for (Body b : cBodies){
            if (b.getFixtureList().get(0).getUserData() == "delete") world.destroyBody(b);
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
        background.dispose();
        for (Texture t : buttonsTextures) {
            t.dispose();
        }
        for (Texture t : yesNoButtonsTextures) {
            t.dispose();
        }
        batch.dispose();
        title.dispose();
    }
}
