package com.tecno.racer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tecno.racer.TecnoRacer;
import com.tecno.racer.GameParameters;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "TecnoRacer";
		config.width = GameParameters.WIDTH;
		config.height = GameParameters.HEIGHT;
		//config.fullscreen = true;


		new LwjglApplication(new TecnoRacer(), config);
	}
}
