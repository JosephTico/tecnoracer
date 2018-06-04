package com.tecno.racer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tecno.racer.TecnoRacer;
import helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "TecnoRacer";
		config.width = GameInfo.WIDTH;
		config.height = GameInfo.HEIGHT;
		//config.fullscreen = true;


		new LwjglApplication(new TecnoRacer(), config);
	}
}
