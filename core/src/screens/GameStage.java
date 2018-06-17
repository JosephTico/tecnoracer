package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;
import helpers.AbstractScreen;
import helpers.ScreenManager;

import java.util.concurrent.TimeUnit;

public class GameStage extends AbstractScreen {

	GameState state;
	private long startTime;
	private TextureAtlas atlas;
	protected Skin skin;
	Table leftTable;
	Label leftLabel;
	Label rightLabel;
	Label debugLabel;
	int frameCounter = 0;

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

		leftTable = new Table(skin);
		leftTable.setBackground("window-c");
		leftTable.setHeight(80f);
		leftTable.setWidth(150f);
		leftTable.setX(10);
		leftTable.setY(GameParameters.HEIGHT - leftTable.getHeight() - 10);
		addActor(leftTable);

		leftLabel = new Label("Speed", skin);
		leftTable.add(leftLabel).expandX().center();


		Table rightTable;
		rightTable = new Table(skin);
		rightTable.setBackground("window-c");
		rightTable.setHeight(50f);
		rightTable.setWidth(100f);
		rightTable.setX(GameParameters.WIDTH - rightTable.getWidth() - 10);
		rightTable.setY(GameParameters.HEIGHT - rightTable.getHeight() - 10);
		addActor(rightTable);

		Table debugTable;
		debugTable = new Table(skin);
		debugTable.setBackground("window-c");
		debugTable.setHeight(150f);
		debugTable.setWidth(200f);
		debugTable.setX(GameParameters.WIDTH - debugTable.getWidth() - 10);
		debugTable.setY(GameParameters.HEIGHT - debugTable.getHeight() - rightTable.getHeight() - 30);
		addActor(debugTable);

		debugLabel = new Label("Speed", skin);
		debugTable.add(debugLabel).expandX().center();

		rightLabel = new Label("Lives: 3", skin);
		rightTable.add(rightLabel);

		startTime = TimeUtils.millis();


	}


	@Override
	public void render(float delta) {
		frameCounter++;
		long elapsedTime = TimeUtils.timeSinceMillis(startTime);
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (frameCounter == 30) {
			state.score++;
			frameCounter = 0;
		}


		String time = String.format("%02dm%02ds",
				TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
				TimeUnit.MILLISECONDS.toSeconds(elapsedTime) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
		);
		leftLabel.setText("Speed: " + Math.round(state.player.getSpeed() / GameParameters.MAX_SPEED  * 180) + "kph\nScore: " + String.format("%08d", state.score) +"\nTime: " + time);
		rightLabel.setText("Lives: " + state.lives);
		debugLabel.setText("X: " + state.player.getX() +  "\nPos: " + state.position +  "\nLength: " + state.trackLength + "\nSegment: " + Math.round(state.position / GameParameters.SEGMENT_LENGTH) + " of " + Math.round(state.trackLength / GameParameters.SEGMENT_LENGTH));
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
