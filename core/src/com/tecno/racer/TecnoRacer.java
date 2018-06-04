package com.tecno.racer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import scenes.MainMenu;

public class TecnoRacer extends Game {
	private SpriteBatch batch;

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenu(this));

	}

	@Override
	public void render () {
		super.render();
	}
}
