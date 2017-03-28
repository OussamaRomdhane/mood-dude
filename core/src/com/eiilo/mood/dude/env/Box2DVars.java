package com.eiilo.mood.dude.env;

public class Box2DVars {

    /*
        *
        * This class contains all of the Box2D constants we'll need to use, mostly the filtering bits
        *
     */

    //Scaling unit for the game, PPM is for Pixels Per Meter as Box2D uses KMS units we will scale accordingly
    public static final float PPM = 100;

    //UserData for the different components
    public static final String FOOT_UD = "foot";
    public static final String SIDE_UD = "side";
    public static final String PLAYER_UD = "player";

    //The bit mask for the player
    public static final short PLAYER_BIT = 2;

    public static final short LIMIT_BIT = 8192;
    public static final short JUMP_BIT = 16384;

    public static final short WIN_BIT = 8192;

    //Bit masks for the different types of grounds
    public static final short GREEN_GROUND_BIT = 4;
    public static final short BEIGE_GROUND_BIT = 8;
    public static final short BLUE_GROUND_BIT = 16;
    public static final short RED_GROUND_BIT = 32;
    public static final short YELLOW_GROUND_BIT = 64;
    public static final short NORMAL_GROUND_BIT = 128;

    //Bit masks for the different types of boosts
    public static final short BEIGE_BOOST_BIT = 512;
    public static final short BLUE_BOOST_BIT = 1024;
    public static final short RED_BOOST_BIT = 2048;
    public static final short YELLOW_BOOST_BIT = 4096;

    //Label for the player
    public static final String PLAYER_LABEL = "PLAYER";

    //Labels for the different types of ground
    public static final String GREEN_GROUND_LABEL = "GREEN_GROUND";
    public static final String BLUE_GROUND_LABEL = "BLUE_GROUND";
    public static final String RED_GROUND_LABEL = "RED_GROUND";
    public static final String NORMAL_GROUND_LABEL = "NORMAL_GROUND";

    public static final String WIN_LABEL = "WIN";

    //Labels for the different types of boosts
    public static final String BEIGE_BOOST_LABEL = "BEIGE_BOOST";
    public static final String BLUE_BOOST_LABEL = "BLUE_BOOST";
    public static final String RED_BOOST_LABEL = "RED_BOOST";
    public static final String YELLOW_BOOST_LABEL = "YELLOW_BOOST";

}
