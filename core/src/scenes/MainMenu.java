package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tecno.racer.TecnoRacer;
import helpers.GameInfo;

public class MainMenu implements Screen {

	private TecnoRacer game;
	Texture img;


	public MainMenu(TecnoRacer game) {
		this.game = game;
	}

	@Override
	public void show() {
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getBatch().begin();
		game.getBatch().draw(img, GameInfo.WIDTH / 2 - img.getWidth() / 2, GameInfo.HEIGHT / 2 - img.getHeight() / 2);
		game.getBatch().end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		game.getBatch().dispose();
		img.dispose();
	}
}
