package com.tecno.racer.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tecno.racer.GameParameters;
import com.tecno.racer.TecnoRacer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("TecnoRacer");
		config.setWindowSizeLimits(GameParameters.WIDTH, GameParameters.HEIGHT, GameParameters.WIDTH, GameParameters.HEIGHT);

		new Lwjgl3Application(new TecnoRacer(), config);
	}
}
