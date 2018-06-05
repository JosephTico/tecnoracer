package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import helpers.AbstractScreen;
import helpers.GameInfo;
import helpers.GameState;
import player.Player;

public class GameStage extends AbstractScreen {

	GameState state;

	public GameStage() {
		super();
	}

	@Override
	public void buildStage() {
		state = new GameState();
		state.road.resetRoad();
		state.music = Gdx.audio.newMusic(Gdx.files.internal("music/racer.mp3"));
		addActor(state.player);
		setKeyboardFocus(state.player);
		state.music.play();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		state.player.update(delta, state);
		state.road.render(getCamera());
		super.act(delta);
		state.music.setVolume(state.player.getSpeed() / (GameInfo.MAX_SPEED * 3f));
		super.draw();
	}

	@Override
	public void dispose() {
		state.music.dispose();
		super.dispose();
	}

}
