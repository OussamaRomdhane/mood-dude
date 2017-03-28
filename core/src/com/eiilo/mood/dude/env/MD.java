package com.eiilo.mood.dude.env;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;

public class MD {

    public static boolean isHUD;

    public static boolean isPaused;
    public static boolean isWon;

    public static final String TITLE = "The Mood Dude";
    public static final int CAMERA_W = 480;
    public static final int CAMERA_H = 320;

    public static final int LEVELS_COUNT = 18;

    public static int lastLevel;

    public static boolean musicOn;
    public static boolean soundsOn;
    public static boolean isFirstTime;

    public static void resetGame () {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        prefs.putInteger("level", 0);
        prefs.flush();
    }

    public static boolean isFirstTime () {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        return prefs.getBoolean("first_time", true);
    }

    public static int getLevel () {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        return prefs.getInteger("level", 0);
    }

    public static void setLevel (int levelIndex) {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        prefs.putInteger("level", levelIndex);
        prefs.flush();
    }

    public static boolean getSound () {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        return prefs.getBoolean("sound", true);
    }

    public static void setSound (boolean onOff) {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        prefs.putBoolean("sound", onOff);
        prefs.flush();
    }

    public static boolean getMusic () {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        return prefs.getBoolean("music", true);
    }

    public static void setMusic (boolean onOff) {
        Preferences prefs = Gdx.app.getPreferences("md_preferences");
        prefs.putBoolean("music", onOff);
        prefs.flush();
    }

}
