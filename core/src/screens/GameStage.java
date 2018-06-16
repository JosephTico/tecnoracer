package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;
import helpers.AbstractScreen;
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
		state.music.play();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		state.player.update(delta, state);
		state.road.update(delta);
		state.road.render(getCamera());
		super.act(delta);
		state.music.setVolume(state.player.getSpeed() / (GameParameters.MAX_SPEED * 3f));
		super.draw();
	}

	@Override
	public void dispose() {
		state.music.dispose();
		super.dispose();
	}

}
