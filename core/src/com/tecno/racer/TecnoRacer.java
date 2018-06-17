package com.tecno.racer;

import com.badlogic.gdx.Game;
import helpers.ScreenEnum;
import helpers.ScreenManager;

public class TecnoRacer extends Game {

	@Override
	public void create () {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen( ScreenEnum.LOADING);

	}

	@Override
	public void render () {
		super.render();
	}
}
