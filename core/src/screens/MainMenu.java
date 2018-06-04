package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import helpers.AbstractScreen;
import player.Player;

public class MainMenu extends AbstractScreen {

	Player player;

	public MainMenu() {
		super();
	}

	@Override
	public void buildStage() {
		player = new Player();
		addActor(player);
		setKeyboardFocus(player);
	}


	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.act(delta);
		super.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
