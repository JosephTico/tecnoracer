package com.tecno.racer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import player.Player;
import road.Road;

public class GameState {
	public int score;

	public int lives = 3;
	public Sound accel;
	public int posMultiplayer = 4;

	public GameState() {}

	public int position = 0;
	public int trackLength = 500;

	public float skyOffset = 0;
	public float hillOffset = 0;
	public float treeOffset = 0;

	public Music music;

	public Player player = new Player();

	public Road road = new Road(this);
}
