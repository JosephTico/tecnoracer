package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;
import com.tecno.racer.ServerState;
import helpers.AbstractScreen;
import helpers.ScreenManager;
import road.Car;

import java.util.Iterator;
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
	long accelId;
	Label posLabel;


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
		state.music.setLooping(true);
		state.accel = ScreenManager.getInstance().assetManager.get("music/accel.wav", Sound.class);
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


		if (ServerState.getInstance().isMultiplayer()) {
			Table centerTable;
			centerTable = new Table(skin);
			centerTable.setBackground("window-c");
			centerTable.setWidth(60f);
			centerTable.setHeight(60f);
			centerTable.setY(GameParameters.HEIGHT - centerTable.getHeight() - 10);
			centerTable.setX(GameParameters.WIDTH / 2 - centerTable.getWidth() / 2);
			addActor(centerTable);

			posLabel = new Label("#1", skin);
			centerTable.add(posLabel);

		}


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

		accelId = state.accel.loop(0f);


	}


	@Override
	public void render(float delta) {
		state.music.setVolume(0f);
		state.accel.setPitch(accelId, state.player.getSpeed() / GameParameters.MAX_SPEED * 2f);
		state.accel.setPan(accelId, state.player.getX()/3, state.player.getSpeed() / (GameParameters.MAX_SPEED));

		frameCounter++;
		long elapsedTime = TimeUtils.timeSinceMillis(startTime);
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (frameCounter == 30) {
			state.score++;
			state.score += Math.round(state.player.getSpeed()/GameParameters.MAX_SPEED) * 10;
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


		state.posMultiplayer = 1;
		if (ServerState.getInstance().isMultiplayer()) {
			synchronized(ServerState.getInstance().getCars()) {
				for (Car car : ServerState.getInstance().getCars()) {
					if (car.getZ() > state.position + GameParameters.OTHER_PLAYER_Z_FIX) {
						state.posMultiplayer++;
					}
				}
			}

			posLabel.setText("#" + state.posMultiplayer);
		}

		state.player.update(delta, state);
		state.road.update(delta);
		state.road.render(getCamera());
		super.act(delta);
		super.draw();
			}

	@Override
	public void dispose() {
		super.dispose();
		state.music.setVolume(0);
		state.accel.setVolume(accelId, 0);
	}

}
