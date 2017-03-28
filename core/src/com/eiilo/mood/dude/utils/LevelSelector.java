package com.eiilo.mood.dude.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.Box2DVars;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.handlers.LevelSelectorCollisionHandler;
import com.eiilo.mood.dude.handlers.LevelSelectorGestureHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.eiilo.mood.dude.env.Box2DVars.JUMP_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.LIMIT_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.NORMAL_GROUND_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.NORMAL_GROUND_LABEL;
import static com.eiilo.mood.dude.env.Box2DVars.PLAYER_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.PPM;
import static com.eiilo.mood.dude.env.PlayerVars.BEIGE_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.BLUE_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.GREEN_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.RED_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.YELLOW_LABEL;

public class LevelSelector implements Screen {

    private int pageIndex;

    public static MoodDude game;
    private SpriteBatch batch;

    public static OrthographicCamera mainCamera;
    private OrthographicCamera b2dCamera;

    private static ArrayList<Texture> levelsTextures;
    public static ArrayList<Sprite> levelsSprites;

    private TiledMap map;
    private TiledMapRenderer mapRenderer;

    public static float elapsedTime = 0;

    private Body playerBody;

    private World world;

    protected HashMap<String,TextureAtlas> walkingTextureAtlas;
    protected HashMap<String,Animation> walkingAnimation;

    private static ArrayList<Texture> indexTextures;
    public static ArrayList<Sprite> indexSprites;

    private static ArrayList<Texture> btnTextures;
    public static ArrayList<Sprite> btnSprites;

    private Sprite player = new Sprite();
    public static String currentColor = GREEN_LABEL;
    public static String facing;
    public static String[] colors = {
      GREEN_LABEL, BLUE_LABEL, RED_LABEL, YELLOW_LABEL, BEIGE_LABEL
    };
    public static int currentIndex = 0;

    private void placeBodies(String type) {
        MapLayer layer = map.getLayers().get(type);

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        for (MapObject mo : layer.getObjects()){

            bDef.type = BodyDef.BodyType.StaticBody;

            float x = (Float) mo.getProperties().get("x") / PPM;
            float y = (Float) mo.getProperties().get("y") / PPM;

            float hx = (Float) mo.getProperties().get("width") / PPM;
            float hy = (Float) mo.getProperties().get("height") / PPM;

            bDef.position.set(x, y);

            PolygonShape box = new PolygonShape();
            box.setAsBox(hx, hy);

            fDef.shape = box;
            fDef.isSensor = true;
            fDef.friction = 0;

            if (type == "limits")
                fDef.filter.categoryBits = Box2DVars.LIMIT_BIT;
            if (type == "jump")
                fDef.filter.categoryBits = Box2DVars.JUMP_BIT;

            fDef.filter.maskBits = PLAYER_BIT;
            world.createBody(bDef).createFixture(fDef).setUserData(type);
        }
    }

    private void placePlayers (int number) {
        if (number > 6) return;
        for (int i = 0; i < number; i++) {

            BodyDef bDef = new BodyDef();
            FixtureDef fDef = new FixtureDef();

            bDef.type = BodyDef.BodyType.DynamicBody;
            float x = 0;
            while (x < 50)
                x = (float) Math.random() * (MD.CAMERA_W - 50);
            x /= PPM;
            float y = 70/PPM;

            bDef.position.set(x, y);

            PolygonShape box = new PolygonShape();
            box.setAsBox(PlayerVars.WIDTH/4/PPM, PlayerVars.HEIGHT/4/PPM);

            fDef.shape = box;
            fDef.friction = 0;

            fDef.filter.categoryBits = Box2DVars.PLAYER_BIT;

            fDef.filter.maskBits = NORMAL_GROUND_BIT | LIMIT_BIT | JUMP_BIT;

            playerBody = world.createBody(bDef);
            playerBody.setLinearVelocity(1, 0);
            playerBody.createFixture(fDef).setUserData("player");
        }
    }

    //Place bodies places bodies in the position of the tiles of a given layer
    private void drawGround (){
        //Getting the layer we need
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");

        //Getting the tileSize (in our case width = height so there is only need for one variable)
        float tileSize = layer.getTileWidth();

        for (int row = 0; row < layer.getHeight(); row ++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                //Getting the current cell
                TiledMapTileLayer.Cell cell = layer.getCell(col,row);

                //Checking weather the cell is empty or not
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                //Setting up the bodyDef for the object
                BodyDef bodyDef = new BodyDef();
                bodyDef.position.set(
                        (col + .5f) * tileSize /PPM ,
                        (row + .5f) * tileSize /PPM
                );
                bodyDef.type = BodyDef.BodyType.StaticBody;

                //Creating a chain shape to border the current tiled map tile
                ChainShape cs = new ChainShape();
                Vector2[] points = new Vector2[3];
                points[0] = new Vector2(-tileSize / 2 / PPM, -tileSize /2 /PPM);
                points[1] = new Vector2(-tileSize / 2 / PPM, tileSize /2 /PPM);
                points[2] = new Vector2(tileSize / 2 / PPM, tileSize /2 /PPM);
                cs.createChain(points);

                //Setting up the fixture definition
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = cs;
                fixtureDef.density = 0f;
                fixtureDef.isSensor = false;
                fixtureDef.filter.categoryBits = NORMAL_GROUND_BIT;
                fixtureDef.filter.maskBits = PLAYER_BIT;

                //Adding the body to the world
                world.createBody(bodyDef).createFixture(fixtureDef).setUserData(NORMAL_GROUND_LABEL);
            }
        }
    }

    public LevelSelector (MoodDude game, int pageIndex) {
        this.pageIndex = pageIndex;

        MD.isHUD = true;

        this.game = game;

        facing = "RIGHT";

        btnTextures = new ArrayList<Texture>();
        btnSprites = new ArrayList<Sprite>();

        levelsTextures = new ArrayList<Texture>();
        levelsSprites = new ArrayList<Sprite>();

        indexTextures = new ArrayList<Texture>();
        indexSprites = new ArrayList<Sprite>();

        walkingTextureAtlas = new HashMap<String, TextureAtlas>();
        walkingAnimation = new HashMap<String, Animation>();

        btnTextures.add(new Texture(Gdx.files.internal("ui_media/homebtn.png")));

        btnSprites.add(new Sprite(btnTextures.get(0)));

        btnSprites.get(0).setPosition(5, MD.CAMERA_H - 40);

        walkingTextureAtlas.put(BLUE_LABEL, new TextureAtlas(Gdx.files.internal("players_media/walking/blue.atlas")));
        walkingTextureAtlas.put(RED_LABEL, new TextureAtlas(Gdx.files.internal("players_media/walking/red.atlas")));
        walkingTextureAtlas.put(GREEN_LABEL, new TextureAtlas(Gdx.files.internal("players_media/walking/green.atlas")));
        walkingTextureAtlas.put(YELLOW_LABEL, new TextureAtlas(Gdx.files.internal("players_media/walking/yellow.atlas")));
        walkingTextureAtlas.put(BEIGE_LABEL, new TextureAtlas(Gdx.files.internal("players_media/walking/beige.atlas")));

        walkingAnimation.put(BLUE_LABEL, new Animation(1/10f, walkingTextureAtlas.get(BLUE_LABEL).getRegions()));
        walkingAnimation.put(RED_LABEL, new Animation(1/10f, walkingTextureAtlas.get(RED_LABEL).getRegions()));
        walkingAnimation.put(GREEN_LABEL, new Animation(1/10f, walkingTextureAtlas.get(GREEN_LABEL).getRegions()));
        walkingAnimation.put(YELLOW_LABEL, new Animation(1 / 10f, walkingTextureAtlas.get(YELLOW_LABEL).getRegions()));
        walkingAnimation.put(BEIGE_LABEL, new Animation(1 / 10f, walkingTextureAtlas.get(BEIGE_LABEL).getRegions()));

        indexTextures.add(new Texture(Gdx.files.internal("levels_media/bricks/grey_boxTick.png")));
        indexTextures.add(new Texture(Gdx.files.internal("levels_media/bricks/grey_circle.png")));

        if (pageIndex == 1) {
            indexSprites.add(new Sprite(indexTextures.get(0)));
            indexSprites.add(new Sprite(indexTextures.get(1)));
            indexSprites.add(new Sprite(indexTextures.get(1)));
        }
        else if (pageIndex == 2) {
            indexSprites.add(new Sprite(indexTextures.get(1)));
            indexSprites.add(new Sprite(indexTextures.get(0)));
            indexSprites.add(new Sprite(indexTextures.get(1)));
        }
        else if (pageIndex == 3) {
            indexSprites.add(new Sprite(indexTextures.get(1)));
            indexSprites.add(new Sprite(indexTextures.get(1)));
            indexSprites.add(new Sprite(indexTextures.get(0)));
        }

        indexSprites.get(0).setPosition(192, 0);
        indexSprites.get(1).setPosition(224, 0);
        indexSprites.get(2).setPosition(256, 0);

        int startIndex = 1;

        switch (this.pageIndex) {
            case 1 :
                startIndex = 1;
                break;
            case 2 :
                startIndex = 7;
                break;
            case 3 :
                startIndex = 13;
                break;
        }

        System.out.println("YO : " + startIndex);

        Sprite sprite = new Sprite();
        String tmp = "";

        if (startIndex <= MD.lastLevel)
            tmp = "ui_media/levels/" + startIndex + ".png";
        else
            tmp = "ui_media/levels/l" + pageIndex + ".png";
        levelsTextures.add(new Texture(Gdx.files.internal(tmp)));
        sprite = new Sprite(levelsTextures.get(0));
        sprite.setPosition(40, 200);
        levelsSprites.add(sprite);

        if ((startIndex + 1) <= MD.lastLevel)
            tmp = "ui_media/levels/" + (startIndex + 1) + ".png";
        else
            tmp = "ui_media/levels/l" + pageIndex + ".png";
        levelsTextures.add(new Texture(Gdx.files.internal(tmp)));
        sprite = new Sprite(levelsTextures.get(1));
        sprite.setPosition(200, 200);
        levelsSprites.add(sprite);

        if ((startIndex + 2) <= MD.lastLevel)
            tmp = "ui_media/levels/" + (startIndex + 2) + ".png";
        else
            tmp = "ui_media/levels/l" + pageIndex + ".png";
        levelsTextures.add(new Texture(Gdx.files.internal(tmp)));
        sprite = new Sprite(levelsTextures.get(2));
        sprite.setPosition(360, 200);
        levelsSprites.add(sprite);

        if ((startIndex + 3) <= MD.lastLevel)
            tmp = "ui_media/levels/" + (startIndex + 3) + ".png";
        else
            tmp = "ui_media/levels/l" + pageIndex + ".png";
        levelsTextures.add(new Texture(Gdx.files.internal(tmp)));
        sprite = new Sprite(levelsTextures.get(3));
        sprite.setPosition(40, 80);
        levelsSprites.add(sprite);

        if ((startIndex + 4) <= MD.lastLevel)
            tmp = "ui_media/levels/" + (startIndex + 4) + ".png";
        else
            tmp = "ui_media/levels/l" + pageIndex + ".png";
        levelsTextures.add(new Texture(Gdx.files.internal(tmp)));
        sprite = new Sprite(levelsTextures.get(4));
        sprite.setPosition(200, 80);
        levelsSprites.add(sprite);

        if ((startIndex + 5) <= MD.lastLevel)
            tmp = "ui_media/levels/" + (startIndex + 5) + ".png";
        else
            tmp = "ui_media/levels/l" + pageIndex + ".png";
        levelsTextures.add(new Texture(Gdx.files.internal(tmp)));
        sprite = new Sprite(levelsTextures.get(5));
        sprite.setPosition(360, 80);
        levelsSprites.add(sprite);
    }

    @Override
    public void show() {
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

        //Loading the map
        map = new TmxMapLoader().load("levels_media/levels"+pageIndex+".tmx");
        MapProperties prop = map.getProperties();
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(mainCamera);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(mainCamera.combined);

        //Setting up the world with earth gravity
        world = new World(new Vector2(0,-9.81f), true);

        drawGround();
        placeBodies("limits");
        placeBodies("jump");
        placePlayers(1);

        world.setContactListener(new LevelSelectorCollisionHandler());

        GestureDetector gd = new GestureDetector(new LevelSelectorGestureHandler(this, pageIndex));
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += Gdx.graphics.getDeltaTime();

        mapRenderer.render();

        batch.begin();
        if (currentColor == GREEN_LABEL) {
            player = new Sprite(walkingAnimation.get(GREEN_LABEL).getKeyFrame(elapsedTime, true));
        }
        else if (currentColor == YELLOW_LABEL) {
            player = new Sprite(walkingAnimation.get(YELLOW_LABEL).getKeyFrame(elapsedTime, true));
        }
        else if (currentColor == BEIGE_LABEL) {
            player = new Sprite(walkingAnimation.get(BEIGE_LABEL).getKeyFrame(elapsedTime, true));
        }
        else if (currentColor == RED_LABEL) {
            player = new Sprite(walkingAnimation.get(RED_LABEL).getKeyFrame(elapsedTime, true));
        }
        else if (currentColor == BLUE_LABEL) {
            player = new Sprite(walkingAnimation.get(BLUE_LABEL).getKeyFrame(elapsedTime, true));
        }
        player.setPosition(playerBody.getPosition().x * PPM - 20, playerBody.getPosition().y * PPM - 28);
        if (facing == "LEFT")
            player.flip(true, false);
        player.draw(batch);
        for (Sprite sp : levelsSprites){
            sp.draw(batch);
        }
        for (Sprite sp : indexSprites) {
            sp.draw(batch);
        }
        for (Sprite sp : btnSprites) {
            sp.draw(batch);
        }

        batch.end();

        world.step(delta, 6, 2);
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
        map.dispose();
        world.dispose();

        for (Texture texture : levelsTextures){
            texture.dispose();
        }
        for (Texture texture : indexTextures) {
            texture.dispose();
        }
        for (Map.Entry<String, TextureAtlas> me : walkingTextureAtlas.entrySet()) {
            me.getValue().dispose();
        }
        for (Texture t : btnTextures) {
            t.dispose();
        }
        levelsTextures.clear();
        levelsSprites.clear();
    }
}
