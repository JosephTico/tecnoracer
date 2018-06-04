package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import helpers.AbstractScreen;
import helpers.GameState;
import player.Player;

public class GameStage extends AbstractScreen {

	Player player;
	float position;
	GameState state;

	public GameStage() {
		super();
	}

	@Override
	public void buildStage() {
		state = new GameState();
		player = new Player();
		addActor(player);
		setKeyboardFocus(player);
	}


	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.update(delta, state);
		super.act(delta);
		super.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
