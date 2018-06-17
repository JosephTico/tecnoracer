package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;
import helpers.AbstractScreen;
import helpers.ScreenManager;

public class GameStage extends AbstractScreen {

	GameState state;
	BitmapFont font;
	private TextureAtlas atlas;
	protected Skin skin;
	Table table;
	Label label;
	Label debugLabel;

	public GameStage() {
		super();
	}

	@Override
	public void buildStage() {
		atlas = new TextureAtlas("skin/terra-mother-ui.atlas");
		skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"), atlas);
		state = new GameState();
		state.road.resetRoad();
		state.music = ScreenManager.getInstance().assetManager.get("music/racer.mp3", Music.class);
		addActor(state.player);
		state.music.play();

		table = new Table(skin);
		table.setBackground("window-c");
		table.setHeight(60f);
		table.setWidth(150f);
		table.setX(10);
		table.setY(GameParameters.HEIGHT - table.getHeight() - 10);
		addActor(table);

		label = new Label("Speed", skin);
		table.add(label).expandX().center();

		Table table2;
		table2 = new Table(skin);
		table2.setBackground("window-c");
		table2.setHeight(150f);
		table2.setWidth(200f);
		table2.setX(GameParameters.WIDTH - table2.getWidth() - 10);
		table2.setY(GameParameters.HEIGHT - table2.getHeight() - 10);
		addActor(table2);

		debugLabel = new Label("Speed", skin);
		table2.add(debugLabel).expandX().center();

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
		getBatch().begin();
		label.setText("Speed: " + Math.round(state.player.getSpeed() / GameParameters.MAX_SPEED  * 180) + "kph");
		debugLabel.setText("X: " + state.player.getX() +  "\nPos: " + state.position +  "\nLength: " + state.trackLength + "\nSegment: " + Math.round(state.position / GameParameters.SEGMENT_LENGTH) + " of " + Math.round(state.trackLength / GameParameters.SEGMENT_LENGTH));
		getBatch().end();
	}

	@Override
	public void dispose() {
		state.music.dispose();
		super.dispose();
	}

}
