package com.eiilo.mood.dude.utils;

import static com.eiilo.mood.dude.env.Box2DVars.FOOT_UD;
import static com.eiilo.mood.dude.env.Box2DVars.NORMAL_GROUND_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.PLAYER_BIT;
import static com.eiilo.mood.dude.env.Box2DVars.PLAYER_UD;
import static com.eiilo.mood.dude.env.Box2DVars.SIDE_UD;

import static com.eiilo.mood.dude.env.MD.isWon;
import static com.eiilo.mood.dude.env.PlayerVars.CURRENT_COLOR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.eiilo.mood.dude.MoodDude;
import com.eiilo.mood.dude.env.Box2DVars;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.env.TiledVars;
import com.eiilo.mood.dude.handlers.CollisionHandler;
import com.eiilo.mood.dude.handlers.GestureHandler;
import com.eiilo.mood.dude.handlers.StandardLevelExtenderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.eiilo.mood.dude.env.PlayerVars.BEIGE_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.RED_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.GREEN_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.YELLOW_LABEL;
import static com.eiilo.mood.dude.env.PlayerVars.BLUE_LABEL;

import static com.eiilo.mood.dude.env.Box2DVars.PPM;
import static com.eiilo.mood.dude.env.PlayerVars.isDead;
import static com.eiilo.mood.dude.env.PlayerVars.isDying;
import static com.eiilo.mood.dude.env.PlayerVars.isJumping;

import static com.eiilo.mood.dude.env.MD.isPaused;

public class Level implements Screen, StandardLevelExtenderHelper {

    //TODO Organize code
    //TODO

    public static Sound jumpSound, deathSound, moodSound;

    private float elapsedTime;

    private int levelWidth;
    private int levelHeight;

    public static boolean animatePlayer;

    //A reference to the Game class and the current level index number
    public static MoodDude game;
    public static int levelIndex;
    protected SpriteBatch batch;

    public Sprite enemySprite;
    public Sprite movingObjectSprite;

    private Texture movingObjectTexture;

    private float playerLinearSpeed;

    protected BodyDef playerBodyDef;
    public static Body playerBody;
    protected Vector2 playerInitialPosition;

    public static CollisionHandler ch;

    //Player animations and sprites
    protected HashMap<String,TextureAtlas> walkingTextureAtlas;
    protected HashMap<String,Animation> walkingAnimation;

    protected HashMap<String,Texture> hurtTextures;
    protected HashMap<String,Texture> idleTextures;
    protected HashMap<String,Texture> jumpingTextures;

    protected HashMap<String,Texture> moodsTextures;
    public static HashMap<String,Sprite> moodsSprites;

    protected HashMap<String,Vector2> boostsPositions;
    public static HashMap<String,Boolean> boostsAreTaken;
    protected HashMap<String,Texture> boostsTextures;

    protected static Texture pauseTexture;
    public static Sprite pauseSprite;

    protected static Texture resumeTexture;
    public static Sprite resumeSprite;

    protected Texture paused;
    protected Texture lost;
    protected Texture won;

    private boolean deadSoundPlayed;

    public static Texture retryButtonTexture;
    public static Texture homeButtonTexture;
    public static Texture retrySmallButtonTexture;
    public static Texture nextButtonTexture;

    public static Sprite retryButton;
    public static Sprite homeButton;
    public static Sprite retrySmallButton;
    public static Sprite nextButton;

    TextureAtlas enemyTextureAtlas;
    Animation enemyAnimation;
    Texture dyingEnemyTexture;

    private ArrayList<LimitsX> movingObjectsLimits;
    private ArrayList<Body> movingObjects;

    //Maps names and location (combined they make the path of the maps) in the assets file
    private static final String MAPS_LOCATION = "levels_media/";
    private static final String[] MAPS_FILE_NAMES = {
            "tutorial.tmx",
            "level1.tmx",
            "level2.tmx",
            "level3.tmx",
            "level4.tmx",
            "level5.tmx",
            "level6.tmx",
            "level7.tmx",
            "level8.tmx",
            "level9.tmx",
            "level10.tmx",
            "level11.tmx",
            "level12.tmx",
            "level13.tmx",
            "level14.tmx",
            "level15.tmx",
            "level16.tmx",
            "level17.tmx",
            "level18.tmx"
    };

    private Sprite flagSprite;
    private TextureAtlas flag;
    private Vector2 flagPos;
    private Animation flagAnimation;

    //MAP variables
    protected TiledMap map;
    protected TiledMapRenderer mapRenderer;

    //Box2D variables
    protected World world;
    Box2DDebugRenderer b2dr;

    //Cameras
    public static OrthographicCamera mainCamera;
    protected OrthographicCamera b2dCamera;

    //Enemies
    public static int enemyNumber;
    public static ArrayList<String> enemyIsFacing;
    public static ArrayList<Body> enemiesBodies;

    int movingObjectsNumber = 0;


    private void placeEnemies () {

        enemyAnimation = new Animation(1/10f, enemyTextureAtlas.getRegions());

        enemyIsFacing = new ArrayList<String>();

        for (int i = 0; i < enemyNumber; i++) {
            enemyIsFacing.add("right");
            placeEnemy(i);
        }

    }


    private void placeMovingObjects () {

        for (int i = 0; i < movingObjectsNumber; i++) {
            placeMovingObject(i);
        }

    }


    private void placeMovingObject (int number) {
        MapLayer layerBegin = map.getLayers().get("begin-moving-object-" + number);
        MapLayer layerEnd = map.getLayers().get("end-moving-object-" + number);

        if (layerBegin == null || layerEnd == null) {
            return;
        }

        MapObject begin = layerBegin.getObjects().get(0);
        MapObject end = layerEnd.getObjects().get(0);

        float bx = (Float) begin.getProperties().get("x") / PPM;
        float by = (Float) begin.getProperties().get("y") / PPM;

        float ex = (Float) end.getProperties().get("x") / PPM;
        float ey = (Float) end.getProperties().get("y") / PPM;

        Vector2 beginCords = new Vector2(bx, by);
        Vector2 endCords = new Vector2(ex, ey);

        BodyDef movingObjectBodyDef = new BodyDef();
        FixtureDef movingObjectFixtureDef = new FixtureDef();

        movingObjectBodyDef.type = BodyDef.BodyType.KinematicBody;
        movingObjectBodyDef.position.set(endCords.x, beginCords.y);

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(58/ PPM, 15/ PPM);

        movingObjectFixtureDef.shape = ps;
        movingObjectFixtureDef.filter.categoryBits = NORMAL_GROUND_BIT;
        movingObjectFixtureDef.filter.maskBits = PLAYER_BIT;

        Body movingObjectBody = world.createBody(movingObjectBodyDef);
        movingObjectBody.setLinearVelocity(1f, 0);
        movingObjectBody.createFixture(movingObjectFixtureDef).setUserData("mo");

        movingObjectsLimits.add(new LimitsX(bx, ex));
        movingObjects.add(movingObjectBody);

    }

    private void placeEnemy (int number) {
        MapLayer layerBegin = map.getLayers().get("begin-enemy-" + number);
        MapLayer layerEnd = map.getLayers().get("end-enemy-" + number);

        if (layerBegin == null || layerEnd == null) {
            return;
        }

        MapObject begin = layerBegin.getObjects().get(0);
        MapObject end = layerEnd.getObjects().get(0);

        float bx = (Float) begin.getProperties().get("x") / PPM;
        float by = (Float) begin.getProperties().get("y") / PPM;

        float ex = (Float) end.getProperties().get("x") / PPM;
        float ey = (Float) end.getProperties().get("y") / PPM;

        Vector2 beginCords = new Vector2(bx, by);
        Vector2 endCords = new Vector2(ex, ey);

        BodyDef enemyBodyDef = new BodyDef();
        FixtureDef enemyFixtureDef = new FixtureDef();

        enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBodyDef.position.set(endCords.x, beginCords.y);

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(20/ PPM, 16/ PPM);

        enemyFixtureDef.shape = ps;
        enemyFixtureDef.filter.categoryBits = NORMAL_GROUND_BIT;
        enemyFixtureDef.filter.maskBits = PLAYER_BIT | NORMAL_GROUND_BIT;

        Body enemyBody = world.createBody(enemyBodyDef);
        enemyBody.setLinearVelocity(1f, 0);
        enemyBody.setGravityScale(0);
        enemyBody.createFixture(enemyFixtureDef).setUserData("the-enemy-" + number);
        enemiesBodies.add(enemyBody);

        BodyDef limitsBodyDef = new BodyDef();
        FixtureDef limitsFixtureDef = new FixtureDef();

        limitsBodyDef.type = BodyDef.BodyType.StaticBody;
        limitsBodyDef.position.set(beginCords.x, beginCords.y);

        ps = new PolygonShape();
        ps.setAsBox(10/ PPM, 20/ PPM);

        limitsFixtureDef.shape = ps;
        limitsFixtureDef.isSensor = true;
        limitsFixtureDef.filter.categoryBits = NORMAL_GROUND_BIT;
        limitsFixtureDef.filter.maskBits = NORMAL_GROUND_BIT;

        Body limitsBody = world.createBody(limitsBodyDef);
        limitsBody.createFixture(limitsFixtureDef).setUserData("limit-begin");

        limitsBodyDef.position.set(endCords.x, endCords.y);
        limitsBody = world.createBody(limitsBodyDef);
        limitsBody.createFixture(limitsFixtureDef).setUserData("limit-end");

    }

    private void placeBoosts(String boost) {
        MapLayer layer = map.getLayers().get(boost.toLowerCase());

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        for (MapObject mo : layer.getObjects()){

            bDef.type = BodyDef.BodyType.StaticBody;

            float x = (Float) mo.getProperties().get("x") / PPM;
            float y = (Float) mo.getProperties().get("y") / PPM;

            bDef.position.set(x, y);

            CircleShape cs = new CircleShape();
            cs.setRadius(16 / PPM);

            fDef.shape = cs;
            fDef.isSensor = true;

            if (boost.equals(Box2DVars.RED_BOOST_LABEL))
                fDef.filter.categoryBits = Box2DVars.RED_BOOST_BIT;

            if (boost.equals(Box2DVars.BLUE_BOOST_LABEL))
                fDef.filter.categoryBits = Box2DVars.BLUE_BOOST_BIT;

            if (boost.equals(Box2DVars.YELLOW_BOOST_LABEL))
                fDef.filter.categoryBits = Box2DVars.YELLOW_BOOST_BIT;

            fDef.filter.maskBits = PLAYER_BIT;
            world.createBody(bDef).createFixture(fDef).setUserData(boost);

            boostsPositions.put(boost, new Vector2(x * PPM,y  * PPM));
            boostsAreTaken.put(boost, false);
        }
    }

    //Place bodies places bodies in the position of the tiles of a given layer
    private void placeWon (){
        //Getting the layer we need
        MapLayer layer =  map.getLayers().get(TiledVars.LAYER_WIN);

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        for (MapObject mo : layer.getObjects()){

            bDef.type = BodyDef.BodyType.StaticBody;

            float x = (Float) mo.getProperties().get("x") / PPM;
            float y = (Float) mo.getProperties().get("y") / PPM;

            float hx = (Float) mo.getProperties().get("width") / PPM;
            float hy = (Float) mo.getProperties().get("height") / PPM;

            bDef.position.set(x, y);

            PolygonShape ps = new PolygonShape();
            ps.setAsBox(hx, hy);

            fDef.shape = ps;
            fDef.isSensor = true;

            fDef.filter.categoryBits = Box2DVars.WIN_BIT;

            fDef.filter.maskBits = PLAYER_BIT;

            world.createBody(bDef).createFixture(fDef).setUserData(Box2DVars.WIN_LABEL);

        }
    }

    private void drawFlag () {
        flagSprite = new Sprite(flagAnimation.getKeyFrame(elapsedTime, true));
        flagSprite.setPosition(flagPos.x, flagPos.y);
        flagSprite.draw(batch);
    }

    //Place bodies places bodies in the position of the tiles of a given layer
    private void placeBodies (String layerName, short categoryBit, short maskBit, boolean isSensor, String tag){
        //Getting the layer we need
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);

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
                fixtureDef.isSensor = isSensor;
                fixtureDef.filter.categoryBits = categoryBit;
                fixtureDef.filter.maskBits = maskBit;

                //Adding the body to the world
                world.createBody(bodyDef).createFixture(fixtureDef).setUserData(tag);
            }
        }
    }

    public Level(MoodDude game, Vector2 playerPosition, int levelIndex){

        movingObjectTexture = game.assets.get("levels_media/brickWall.png", Texture.class);

        pauseTexture = game.assets.get("ui_media/icons/pause.png", Texture.class);
        resumeTexture = game.assets.get("ui_media/icons/resume.png", Texture.class);

        paused = game.assets.get("ui_media/paused.png", Texture.class);
        lost = game.assets.get("ui_media/lost.png", Texture.class);
        won = game.assets.get("ui_media/won.png", Texture.class);

        retryButtonTexture = game.assets.get("ui_media/icons/retry.png", Texture.class);
        homeButtonTexture = game.assets.get("ui_media/icons/home.png", Texture.class);
        retrySmallButtonTexture = game.assets.get("ui_media/icons/retry_small.png", Texture.class);
        nextButtonTexture = game.assets.get("ui_media/icons/next_level.png", Texture.class);

        enemyTextureAtlas = game.assets.get("players_media/enemy/enemy.atlas", TextureAtlas.class);
        dyingEnemyTexture = game.assets.get("players_media/enemy/dying.png", Texture.class);

        retryButton = new Sprite(retryButtonTexture);
        homeButton = new Sprite(homeButtonTexture);
        retrySmallButton = new Sprite(retrySmallButtonTexture);
        nextButton = new Sprite(nextButtonTexture);

        walkingTextureAtlas = new HashMap<String,TextureAtlas>();
        walkingAnimation = new HashMap<String,Animation>();

        hurtTextures = new HashMap<String,Texture>();
        idleTextures = new HashMap<String,Texture>();
        jumpingTextures = new HashMap<String,Texture>();

        moodsTextures = new HashMap<String,Texture>();
        moodsSprites = new HashMap<String,Sprite>();

        boostsPositions = new HashMap<String,Vector2>();
        boostsAreTaken = new HashMap<String,Boolean>();
        boostsTextures = new HashMap<String,Texture>();

        pauseSprite = new Sprite(pauseTexture);
        resumeSprite = new Sprite(resumeTexture);

        movingObjectsLimits = new ArrayList<LimitsX>();
        movingObjects = new ArrayList<Body>();

        MD.isHUD = false;

        flag = game.assets.get("levels_media/flag/flag.atlas", TextureAtlas.class);
        flagSprite = new Sprite();
        flagAnimation = new Animation(1/8f, flag.getRegions());

        isDying = false;
        isJumping = false;
        isDead = false;
        isPaused = false;
        MD.isWon = false;

        animatePlayer = true;

        elapsedTime = 0f;

        this.game = game;
        this.levelIndex = levelIndex;

        playerLinearSpeed = 1;

        retryButton.setPosition(-200, -200);
        homeButton.setPosition(-100, -100);
        retrySmallButton.setPosition(-200, -200);
        nextButton.setPosition(-200, -200);

        PlayerVars.AVAILABLE_COLORS.clear();
        PlayerVars.AVAILABLE_COLORS.add(GREEN_LABEL);
        CURRENT_COLOR = GREEN_LABEL;

        if (levelIndex == 0) playerLinearSpeed = 1.2f;
        else if (levelIndex == 1) playerLinearSpeed = 1.1f;
        else if (levelIndex > 1 && levelIndex < 4) playerLinearSpeed = 1 + (0.2f * levelIndex);
        else playerLinearSpeed = 1.8f;

        batch = new SpriteBatch();

        //Setting the main camera
        mainCamera = new OrthographicCamera(MD.CAMERA_W, MD.CAMERA_H);
        mainCamera.position.x = MD.CAMERA_W/2;
        mainCamera.position.y = MD.CAMERA_H/2;
        mainCamera.update();

        //Box2D camera
        b2dCamera = new OrthographicCamera(MD.CAMERA_W/PPM,MD.CAMERA_H/PPM);
        b2dCamera.position.x = MD.CAMERA_W/PPM/2;
        b2dCamera.position.y = MD.CAMERA_H/PPM/2;
        b2dCamera.update();

        //Loading the map
        map = game.assets.get(MAPS_LOCATION + MAPS_FILE_NAMES[levelIndex], TiledMap.class);
        MapProperties prop = map.getProperties();
        levelHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
        levelWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //Setting up the world with earth gravity
        world = new World(new Vector2(0,-9.81f),true);

        //Setting up the world renderer
        b2dr = new Box2DDebugRenderer();

        setCollisionHandler ();

        hurtTextures.put(BLUE_LABEL, game.assets.get("players_media/hurt/blue.png", Texture.class));
        hurtTextures.put(RED_LABEL, game.assets.get("players_media/hurt/red.png", Texture.class));
        hurtTextures.put(BEIGE_LABEL, game.assets.get("players_media/hurt/beige.png", Texture.class));
        hurtTextures.put(YELLOW_LABEL, game.assets.get("players_media/hurt/yellow.png", Texture.class));
        hurtTextures.put(GREEN_LABEL, game.assets.get("players_media/hurt/green.png", Texture.class));

        walkingTextureAtlas.put(BLUE_LABEL, game.assets.get("players_media/walking/blue.atlas", TextureAtlas.class));
        walkingTextureAtlas.put(RED_LABEL, game.assets.get("players_media/walking/red.atlas", TextureAtlas.class));
        walkingTextureAtlas.put(GREEN_LABEL, game.assets.get("players_media/walking/green.atlas", TextureAtlas.class));
        walkingTextureAtlas.put(YELLOW_LABEL, game.assets.get("players_media/walking/yellow.atlas", TextureAtlas.class));
        walkingTextureAtlas.put(BEIGE_LABEL, game.assets.get("players_media/walking/beige.atlas", TextureAtlas.class));

        walkingAnimation.put(BLUE_LABEL, new Animation(1/10f, walkingTextureAtlas.get(BLUE_LABEL).getRegions()));
        walkingAnimation.put(RED_LABEL, new Animation(1/10f, walkingTextureAtlas.get(RED_LABEL).getRegions()));
        walkingAnimation.put(GREEN_LABEL, new Animation(1/10f, walkingTextureAtlas.get(GREEN_LABEL).getRegions()));
        walkingAnimation.put(YELLOW_LABEL, new Animation(1/10f, walkingTextureAtlas.get(YELLOW_LABEL).getRegions()));
        walkingAnimation.put(BEIGE_LABEL, new Animation(1/10f, walkingTextureAtlas.get(BEIGE_LABEL).getRegions()));

        idleTextures.put(BLUE_LABEL, game.assets.get("players_media/still/blue.png", Texture.class));
        idleTextures.put(RED_LABEL, game.assets.get("players_media/still/red.png", Texture.class));
        idleTextures.put(GREEN_LABEL, game.assets.get("players_media/still/green.png", Texture.class));
        idleTextures.put(BEIGE_LABEL, game.assets.get("players_media/still/beige.png", Texture.class));
        idleTextures.put(YELLOW_LABEL, game.assets.get("players_media/still/yellow.png", Texture.class));

        jumpingTextures.put(BLUE_LABEL, game.assets.get("players_media/jump/blue.png", Texture.class));
        jumpingTextures.put(RED_LABEL, game.assets.get("players_media/jump/red.png", Texture.class));
        jumpingTextures.put(GREEN_LABEL, game.assets.get("players_media/jump/green.png", Texture.class));
        jumpingTextures.put(BEIGE_LABEL, game.assets.get("players_media/jump/beige.png", Texture.class));
        jumpingTextures.put(YELLOW_LABEL, game.assets.get("players_media/jump/yellow.png", Texture.class));

        moodsTextures.put(BLUE_LABEL, game.assets.get("players_media/boosts/blue.png", Texture.class));
        moodsTextures.put(RED_LABEL, game.assets.get("players_media/boosts/red.png", Texture.class));
        moodsTextures.put(GREEN_LABEL,game.assets.get("players_media/boosts/green.png", Texture.class));
        moodsTextures.put(BEIGE_LABEL, game.assets.get("players_media/boosts/beige.png", Texture.class));
        moodsTextures.put(YELLOW_LABEL, game.assets.get("players_media/boosts/yellow.png", Texture.class));

        moodsTextures.put(BLUE_LABEL+"_blank", game.assets.get("players_media/boosts/blue_blank.png", Texture.class));
        moodsTextures.put(RED_LABEL+"_blank", game.assets.get("players_media/boosts/red_blank.png", Texture.class));
        moodsTextures.put(GREEN_LABEL+"_blank", game.assets.get("players_media/boosts/green_blank.png", Texture.class));
        moodsTextures.put(BEIGE_LABEL+"_blank", game.assets.get("players_media/boosts/beige_blank.png", Texture.class));
        moodsTextures.put(YELLOW_LABEL + "_blank", game.assets.get("players_media/boosts/yellow_blank.png", Texture.class));

        moodsSprites.put(BLUE_LABEL, new Sprite(moodsTextures.get(BLUE_LABEL)));
        moodsSprites.put(RED_LABEL, new Sprite(moodsTextures.get(RED_LABEL)));
        moodsSprites.put(GREEN_LABEL, new Sprite(moodsTextures.get(GREEN_LABEL)));
        moodsSprites.put(BEIGE_LABEL, new Sprite(moodsTextures.get(BEIGE_LABEL)));
        moodsSprites.put(YELLOW_LABEL, new Sprite(moodsTextures.get(YELLOW_LABEL)));

        moodsSprites.put(BLUE_LABEL + "_blank", new Sprite(moodsTextures.get(BLUE_LABEL + "_blank")));
        moodsSprites.put(RED_LABEL + "_blank", new Sprite(moodsTextures.get(RED_LABEL + "_blank")));
        moodsSprites.put(GREEN_LABEL + "_blank", new Sprite(moodsTextures.get(GREEN_LABEL + "_blank")));
        moodsSprites.put(BEIGE_LABEL + "_blank", new Sprite(moodsTextures.get(BEIGE_LABEL + "_blank")));
        moodsSprites.put(YELLOW_LABEL + "_blank", new Sprite(moodsTextures.get(YELLOW_LABEL + "_blank")));

        boostsTextures.put(Box2DVars.BLUE_BOOST_LABEL, game.assets.get("levels_media/boosts/blue.png", Texture.class));
        boostsTextures.put(Box2DVars.RED_BOOST_LABEL, game.assets.get("levels_media/boosts/red.png", Texture.class));
        boostsTextures.put(Box2DVars.YELLOW_BOOST_LABEL, game.assets.get("levels_media/boosts/yellow.png", Texture.class));

        PlayerVars.WIDTH = idleTextures.get(BLUE_LABEL).getWidth();
        PlayerVars.HEIGHT = idleTextures.get(BLUE_LABEL).getHeight();

        this.playerInitialPosition = playerPosition;

        //Setting the flag position

        MapLayer layer = map.getLayers().get(TiledVars.LAYER_FLAG);

        for (MapObject mo : layer.getObjects()){

            float x = (Float) mo.getProperties().get("x");
            float y = (Float) mo.getProperties().get("y");

            flagPos = new Vector2(x, y);

            //System.out.println("hmmm : " + flagPos);
        }

        enemiesBodies = new ArrayList<Body>();

    }

    public Level (MoodDude game, Vector2 playerPosition, int levelIndex, int enemyNumber) {

        this (game,playerPosition,levelIndex);
        this.enemyNumber = enemyNumber;
        placeEnemies();

    }

    public Level (MoodDude game, Vector2 playerPosition, int levelIndex, int enemyNumber, int movingObjectsNumber) {

        this (game,playerPosition,levelIndex,enemyNumber);
        this.movingObjectsNumber = movingObjectsNumber;
        placeMovingObjects();

    }

    public void setCollisionHandler() {

        ch = new CollisionHandler();
        world.setContactListener(ch);

    }

    @Override
    public void show() {

        deadSoundPlayed = false;

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/death.wav"));
        moodSound = Gdx.audio.newSound(Gdx.files.internal("sounds/mood.mp3"));

        if (game.mainMenuBackgroundMusic.isPlaying()) game.mainMenuBackgroundMusic.stop();
        if (!game.mainBackgroundMusic.isPlaying() && MD.musicOn) game.mainBackgroundMusic.play();

        //Placing bodies for the grounds
        placeBodies(TiledVars.LAYER_BLUE, Box2DVars.BLUE_GROUND_BIT, Box2DVars.PLAYER_BIT, false, Box2DVars.BLUE_GROUND_LABEL);
        placeBodies(TiledVars.LAYER_RED, Box2DVars.RED_GROUND_BIT, Box2DVars.PLAYER_BIT, false, Box2DVars.RED_GROUND_LABEL);
        placeBodies(TiledVars.LAYER_GREEN, Box2DVars.GREEN_GROUND_BIT, Box2DVars.PLAYER_BIT, false, Box2DVars.GREEN_GROUND_LABEL);
        placeBodies(TiledVars.LAYER_NORMAL, Box2DVars.NORMAL_GROUND_BIT, Box2DVars.PLAYER_BIT, false, Box2DVars.NORMAL_GROUND_LABEL);

        placeWon();

        placeBoosts(Box2DVars.BLUE_BOOST_LABEL);
        placeBoosts(Box2DVars.RED_BOOST_LABEL);

        placePlayer(playerInitialPosition.x, playerInitialPosition.y);

        setGestureHandler();

    }

    public void setGestureHandler() {

        GestureDetector gd = new GestureDetector(new GestureHandler());
        Gdx.input.setInputProcessor(gd);

    }

    @Override
    public void render(float delta) {

        if (playerBody.getPosition().y < -32/PPM)
            isDead = true;

        if (!isPaused && !isDead && !isWon && animatePlayer)
            elapsedTime += Gdx.graphics.getDeltaTime();

        //Clearing the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Updating the cameras' positions
        updateCamera();

        //Rendering the tile map
        mapRenderer.setView(mainCamera);
        mapRenderer.render();

        //Rendering the Box2D world
        //b2dr.render(world, b2dCamera.combined);

        batch.setProjectionMatrix(mainCamera.combined);
        batch.begin();

        drawFlag();

        renderBeforePlayer();

        short index =  0;
        for (Body b : enemiesBodies) {
            Vector2 pos = b.getPosition();
            if (b.getUserData() == "dying"){
                enemySprite = new Sprite(dyingEnemyTexture);
            }
            else {
                enemySprite = new Sprite(enemyAnimation.getKeyFrame(elapsedTime, true));
            }
            if (enemyIsFacing.get(index) ==  "right")
                enemySprite.flip(true, false);
            enemySprite.setX(pos.x * PPM - 35);
            enemySprite.setY(pos.y * PPM - 15);
            enemySprite.draw(batch);
            index ++;
        }

        index = 0;

        for (Body b : movingObjects) {
            movingObjectSprite = new Sprite(movingObjectTexture);
            Vector2 pos = b.getPosition();
            movingObjectSprite.setPosition(pos.x * PPM - 61, pos.y * PPM - 17);
            movingObjectSprite.draw(batch);
            if (pos.x > movingObjectsLimits.get(index).ex)
                b.setLinearVelocity(-1,0);
            else if (pos.x < movingObjectsLimits.get(index).bx)
                b.setLinearVelocity(1, 0);
            index ++;
        }

        Sprite sprite = new Sprite();

        if (isJumping) {
            if (CURRENT_COLOR == GREEN_LABEL) {
                sprite = new Sprite(jumpingTextures.get(GREEN_LABEL));
            }
            else if (CURRENT_COLOR == YELLOW_LABEL) {
                sprite = new Sprite(jumpingTextures.get(YELLOW_LABEL));
            }
            else if (CURRENT_COLOR == BEIGE_LABEL) {
                sprite = new Sprite(jumpingTextures.get(BEIGE_LABEL));
            }
            else if (CURRENT_COLOR == RED_LABEL) {
                sprite = new Sprite(jumpingTextures.get(RED_LABEL));
            }
            else if (CURRENT_COLOR == BLUE_LABEL) {
                sprite = new Sprite(jumpingTextures.get(BLUE_LABEL));
            }
        }
        else {
            if (isDying) {
                if (CURRENT_COLOR == GREEN_LABEL) {
                    sprite = new Sprite(hurtTextures.get(GREEN_LABEL));
                }
                else if (CURRENT_COLOR == RED_LABEL) {
                    sprite = new Sprite(hurtTextures.get(RED_LABEL));
                }
                else if (CURRENT_COLOR == BLUE_LABEL) {
                    sprite = new Sprite(hurtTextures.get(BLUE_LABEL));
                }
            }
            else {
                if (CURRENT_COLOR == GREEN_LABEL) {
                    sprite = new Sprite(walkingAnimation.get(GREEN_LABEL).getKeyFrame(elapsedTime, true));
                }
                else if (CURRENT_COLOR == RED_LABEL) {
                    sprite = new Sprite(walkingAnimation.get(RED_LABEL).getKeyFrame(elapsedTime, true));
                }
                else if (CURRENT_COLOR == BLUE_LABEL) {
                    sprite = new Sprite(walkingAnimation.get(BLUE_LABEL).getKeyFrame(elapsedTime, true));
                }
            }
        }

        renderAfterPlayer();

        for (Map.Entry<String, Boolean> me : boostsAreTaken.entrySet()) {
            if (me.getValue() == false){
                Vector2 pos = boostsPositions.get(me.getKey());
                batch.draw(boostsTextures.get(me.getKey()),pos.x - 16,pos.y - 16);
            }
        }

        sprite.setX(playerBody.getPosition().x * PPM - PlayerVars.WIDTH / 2);
        sprite.setY(playerBody.getPosition().y * PPM - (PlayerVars.HEIGHT / 2) - 2.5f);
        sprite.draw(batch);

        if (!isDead && !isPaused && !isWon)
            drawMoodSwitchers();

        if (isPaused)
            popUpPausedScreen();

        if (isWon)
            popUpWonScreen();

        if (isDead) {
            popUpDeadScreen();
            if (! deadSoundPlayed) {
                deadSoundPlayed = true;
                deathSound.play();
            }
        }

        if (!isPaused && !isDead && !isWon) {
            pauseSprite.setPosition(mainCamera.position.x + (MD.CAMERA_W / 2) - 60, mainCamera.position.y + (MD.CAMERA_H / 2) - (45 + 10));
            pauseSprite.draw(batch);
        }

        if (isPaused && !isDead && !isWon) {
            resumeSprite.setPosition(mainCamera.position.x + (MD.CAMERA_W / 2) - 60, mainCamera.position.y + (MD.CAMERA_H / 2) - (45 + 10));
            resumeSprite.draw(batch);
        }

        renderOnForeground ();

        batch.end();
    }

    private void drawMoodSwitchers() {
        moodsSprites.get(BLUE_LABEL+"_blank").setX(mainCamera.position.x - (MD.CAMERA_W / 2) + 10);
        moodsSprites.get(BLUE_LABEL+"_blank").setY(mainCamera.position.y + (MD.CAMERA_H / 2) - (47 + 10));
        moodsSprites.get(BLUE_LABEL).setX(mainCamera.position.x - (MD.CAMERA_W / 2) + 10);
        moodsSprites.get(BLUE_LABEL).setY(mainCamera.position.y + (MD.CAMERA_H / 2) - (47 + 10));

        moodsSprites.get(RED_LABEL+"_blank").setX(mainCamera.position.x - (MD.CAMERA_W / 2) + 47 + 15);
        moodsSprites.get(RED_LABEL+"_blank").setY(mainCamera.position.y + (MD.CAMERA_H / 2) - (47 + 10));
        moodsSprites.get(RED_LABEL).setX(mainCamera.position.x - (MD.CAMERA_W / 2) + 47 + 15);
        moodsSprites.get(RED_LABEL).setY(mainCamera.position.y + (MD.CAMERA_H / 2) - (47 + 10));

        moodsSprites.get(GREEN_LABEL+"_blank").setX(mainCamera.position.x - (MD.CAMERA_W / 2) + 94 + 20);
        moodsSprites.get(GREEN_LABEL+"_blank").setY(mainCamera.position.y + (MD.CAMERA_H / 2) - (47 + 10));
        moodsSprites.get(GREEN_LABEL).setX(mainCamera.position.x - (MD.CAMERA_W / 2) + 94 + 20);
        moodsSprites.get(GREEN_LABEL).setY(mainCamera.position.y + (MD.CAMERA_H / 2) - (47 + 10));

        if (PlayerVars.AVAILABLE_COLORS.contains(BLUE_LABEL))
            moodsSprites.get(BLUE_LABEL).draw(batch);
        else
            moodsSprites.get(BLUE_LABEL+"_blank").draw(batch);

        if (PlayerVars.AVAILABLE_COLORS.contains(RED_LABEL))
            moodsSprites.get(RED_LABEL).draw(batch);
        else
            moodsSprites.get(RED_LABEL+"_blank").draw(batch);

        if (PlayerVars.AVAILABLE_COLORS.contains(GREEN_LABEL))
            moodsSprites.get(GREEN_LABEL).draw(batch);
        else
            moodsSprites.get(GREEN_LABEL+"_blank").draw(batch);

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
        b2dr.dispose();
        world.dispose();

        jumpSound.dispose();
        deathSound.dispose();
        moodSound.dispose();

        movingObjectTexture.dispose();

        paused.dispose();
        won.dispose();
        lost.dispose();

        retryButtonTexture.dispose();
        homeButtonTexture.dispose();
        pauseTexture.dispose();
        resumeTexture.dispose();

        enemyTextureAtlas.dispose();

        flag.dispose();

        Iterator it = idleTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }
        it = jumpingTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }
        it = walkingTextureAtlas.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((TextureAtlas) pair.getValue()).dispose();
        }
        it = moodsTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }
        it = boostsTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }
        it = hurtTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }
        it = idleTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }
        it = jumpingTextures.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((Texture) pair.getValue()).dispose();
        }

    }

    public void placePlayer(float x, float y){
        playerBodyDef = new BodyDef();
        playerBodyDef.position.set(
                (x/PPM),
                (y/PPM)
        );
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef playerFixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(PlayerVars.WIDTH/2/PPM, PlayerVars.HEIGHT/2/PPM);
        playerFixtureDef.shape = box;
        playerFixtureDef.density = 1f;
        playerFixtureDef.friction = 0;
        playerFixtureDef.filter.categoryBits = Box2DVars.PLAYER_BIT;
        playerFixtureDef.filter.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.GREEN_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT | Box2DVars.WIN_BIT;
        playerBody = world.createBody(playerBodyDef);
        playerBody.setLinearVelocity(playerLinearSpeed, 0);
        playerBody.setFixedRotation(true);
        playerBody.createFixture(playerFixtureDef).setUserData(PLAYER_UD);

        FixtureDef sensorsFixture = new FixtureDef();
        box.setAsBox((PlayerVars.WIDTH / 2 / PPM) - .035f, PlayerVars.HEIGHT / 8 / PPM, new Vector2(0, -.24f), 0);
        sensorsFixture.isSensor = true;
        playerFixtureDef.density = 0f;
        playerFixtureDef.friction = 0;
        sensorsFixture.shape = box;
        sensorsFixture.filter.categoryBits = Box2DVars.PLAYER_BIT;
        sensorsFixture.filter.maskBits = Box2DVars.RED_BOOST_BIT | Box2DVars.BLUE_BOOST_BIT | Box2DVars.YELLOW_BOOST_BIT
                | Box2DVars.BEIGE_BOOST_BIT | Box2DVars.GREEN_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        playerBody.createFixture(sensorsFixture).setUserData(FOOT_UD);

        box.setAsBox(PlayerVars.WIDTH / 8 / PPM, (PlayerVars.HEIGHT / 2 / PPM) - .05f, new Vector2(.15f, 0), 0);
        sensorsFixture.shape = box;
        sensorsFixture.filter.maskBits = Box2DVars.GREEN_GROUND_BIT | Box2DVars.NORMAL_GROUND_BIT;
        playerBody.createFixture(sensorsFixture).setUserData(SIDE_UD);

    }
    public void updateCamera(){

        if ((playerBody.getPosition().x*PPM) - 30 <= 0) return;
        if ((playerBody.getPosition().x*PPM) - 30 + MD.CAMERA_W >= levelWidth) return;
        mainCamera.position.set(new Vector3(((playerBody.getPosition().x * PPM) + MD.CAMERA_W / 2) - 30, MD.CAMERA_H / 2, 0));
        b2dCamera.position.set(new Vector3(((playerBody.getPosition().x) + MD.CAMERA_W / PPM / 2) - 30 / PPM, MD.CAMERA_H / PPM / 2, 0));
        b2dCamera.update();
        mainCamera.update();
    }

    public void popUpPausedScreen(){
        batch.draw(paused, mainCamera.position.x - MD.CAMERA_W / 2, mainCamera.position.y - MD.CAMERA_H / 2);
        retryButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 130, 0);
        retryButton.draw(batch);
        homeButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 240, 50);
        homeButton.draw(batch);
    }

    public void popUpDeadScreen(){
        batch.draw(lost, mainCamera.position.x - MD.CAMERA_W / 2, mainCamera.position.y - MD.CAMERA_H / 2);
        retryButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 130, 0);
        retryButton.draw(batch);
        homeButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 240, 50);
        homeButton.draw(batch);
    }

    public void popUpWonScreen(){
        batch.draw(won, mainCamera.position.x - MD.CAMERA_W / 2, mainCamera.position.y - MD.CAMERA_H / 2);
        nextButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 130, 0);
        nextButton.draw(batch);
        homeButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 240, 50);
        homeButton.draw(batch);
        retrySmallButton.setPosition((mainCamera.position.x - MD.CAMERA_W / 2) + 295, 50);
        retrySmallButton.draw(batch);
    }

    @Override
    public void renderBeforePlayer() {

    }

    @Override
    public void renderAfterPlayer() {

    }

    @Override
    public void renderOnForeground() {

    }

    private class LimitsX {
        public float bx, ex;
        public LimitsX (float bx, float ex) {
            this.bx = bx;
            this.ex = ex;
        }
    }
}
