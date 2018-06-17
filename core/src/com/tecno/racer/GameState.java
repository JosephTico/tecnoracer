package com.tecno.racer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import player.Player;
import road.Road;

public class GameState {
	public GameState() {}

	public int position = 0;
	public int trackLength = 500;

	public float skyOffset = 0;
	public float hillOffset = 0;
	public float treeOffset = 0;

	public AssetManager manager = new AssetManager();

	public Music music;

	public Player player = new Player();

	public Road road = new Road(this);
}
