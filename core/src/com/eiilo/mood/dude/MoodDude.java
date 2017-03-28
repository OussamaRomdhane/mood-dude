package com.eiilo.mood.dude;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.env.PlayerVars;
import com.eiilo.mood.dude.handlers.LevelLoader;
import com.eiilo.mood.dude.utils.LoadingScreen;

import java.util.ArrayList;

public class MoodDude extends Game {

	public AssetManager assets;

	public static Music mainBackgroundMusic, mainMenuBackgroundMusic, creditsBackgroundMusic;

	@Override
	public void create() {

		mainMenuBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/lor.mp3"));
		mainBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/ts.mp3"));
		creditsBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/sm.mp3"));
		mainBackgroundMusic.setLooping(true);
		mainMenuBackgroundMusic.setLooping(true);
		creditsBackgroundMusic.setLooping(true);
		mainBackgroundMusic.setVolume(0.075f);
		mainMenuBackgroundMusic.setVolume(0.1f);
		creditsBackgroundMusic.setVolume(0.075f);
		if (mainBackgroundMusic.isPlaying()) mainBackgroundMusic.pause();
		if (mainMenuBackgroundMusic.isPlaying()) {
			if (mainBackgroundMusic.getPosition() > 0) mainBackgroundMusic.stop();
			mainMenuBackgroundMusic.stop();
		}
		if (MD.musicOn) mainMenuBackgroundMusic.play();
		//TODO Go to loading screen then main menu stage select etc.
		//TODO if the level = 0 go to tutorial directly no level selector
		assets = new AssetManager();
		assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

		MD.isFirstTime = MD.isFirstTime();
		MD.lastLevel = MD.getLevel();
		MD.musicOn = MD.getMusic();
		MD.soundsOn = MD.getSound();

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void pause() {
		super.pause();
		if (! MD.isHUD && !MD.isWon && !PlayerVars.isDead) {
			MD.isPaused = true;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		mainBackgroundMusic.dispose();
		mainMenuBackgroundMusic.dispose();
		creditsBackgroundMusic.dispose();
		assets.dispose();
	}
}
