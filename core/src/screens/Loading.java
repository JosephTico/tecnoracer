package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tecno.racer.ServerState;
import helpers.AbstractScreen;
import helpers.ScreenEnum;
import helpers.ScreenManager;
import jexxus.Server;
import sockets.DatoSocket;

import java.util.concurrent.*;

public class Loading extends AbstractScreen {

	protected Skin skin;
	Label loadingLabel;
	boolean error = false;
	private TextureAtlas atlas;
	boolean isConnecting = false;
	Future<Integer> future;
	private boolean serverSetupAlready = false;

	@Override
	public void buildStage() {
		atlas = new TextureAtlas("skin/terra-mother-ui.atlas");
		skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"), atlas);

	}

	@Override
	public void show() {
		//Stage should controll input:
		Gdx.input.setInputProcessor(this);

		Table root = new Table();
		root.setFillParent(true);
		//root.setBackground(skin.getTiledDrawable("tile").tint(Color.CYAN));
		root.setBackground(skin.getTiledDrawable("tile-a"));
		addActor(root);

		Image image = new Image(new Texture("sprites/logo.jpg"));
		root.add(image).width(419).height(300).padBottom(20f);

		root.row();
		Table content = new Table();
		root.add(content);

		Table table = new Table(skin);
		table.setBackground("window-c");
		table.setHeight(60f);
		table.setWidth(150f);
		content.add(table);

		loadingLabel = new Label("Getting ready...", skin);
		table.add(loadingLabel).expandX().center();

		loadAssets();

		Gdx.graphics.setContinuousRendering(true);
	}

	private void loadAssets() {
		AssetManager assMan = ScreenManager.getInstance().assetManager;
		assMan.load("music/racer.mp3", Music.class);
		assMan.load("music/explosion.mp3", Sound.class);
		assMan.load("sprites/billboard01.png", Texture.class);
		assMan.load("sprites/billboard02.png", Texture.class);
		assMan.load("sprites/billboard03.png", Texture.class);
		assMan.load("sprites/billboard04.png", Texture.class);
		assMan.load("sprites/billboard05.png", Texture.class);
		assMan.load("sprites/billboard06.png", Texture.class);
		assMan.load("sprites/billboard07.png", Texture.class);
		assMan.load("sprites/billboard08.png", Texture.class);
		assMan.load("sprites/billboard09.png", Texture.class);
		assMan.load("sprites/car01.png", Texture.class);
		assMan.load("sprites/car02.png", Texture.class);
		assMan.load("sprites/car03.png", Texture.class);
		assMan.load("sprites/car04.png", Texture.class);
		assMan.load("sprites/player_left.png", Texture.class);
		assMan.load("sprites/player_right.png", Texture.class);
		assMan.load("sprites/player_straight.png", Texture.class);
		assMan.load("sprites/tree1.png", Texture.class);
		assMan.load("sprites/tree2.png", Texture.class);

		assMan.load("background/dark_grass.jpg", Texture.class);
		assMan.load("background/dark_road.jpg", Texture.class);
		assMan.load("background/hills.png", Texture.class);
		assMan.load("background/light_grass.jpg", Texture.class);
		assMan.load("background/light_road.jpg", Texture.class);
		assMan.load("background/red_rumble.jpg", Texture.class);
		assMan.load("background/sky.png", Texture.class);
		assMan.load("background/trees.png", Texture.class);
		assMan.load("background/white_lines.jpg", Texture.class);
		assMan.load("background/white_rumble.jpg", Texture.class);

		assMan.load("sprites/bomb.png", Texture.class);
		assMan.load("sprites/explosion.gif", Texture.class);
		assMan.load("sprites/bush1.png", Texture.class);
		assMan.load("sprites/bush2.png", Texture.class);
		assMan.load("sprites/light.png", Texture.class);




	}

	Callable<Integer> connectServer = () -> {
		try {
			ServerState.getInstance().newClient(ServerState.getInstance().getHost());
			ServerState.getInstance().getClient().port(ServerState.getInstance().getPort());
			Integer result = ServerState.getInstance().getClient().connect() ? 1: 0;
			System.out.println("Connected to server");
			return result;
		}
		catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	};

	public void setupServer() {
		if (!serverSetupAlready) {
			System.out.println("SETUP");
			ServerState.getInstance().getClient().onMessage(data -> {
				ServerState.getInstance().proccessInput(new String(data));
			});
			serverSetupAlready = true;
		}
	}


	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (ServerState.getInstance().isMultiplayer() && !ServerState.getInstance().isMultiplayerReady() && ServerState.getInstance().getClient() != null) {
			setupServer();
		}

		// If its a multiplayer game and already connected
		if (!ServerState.getInstance().isMultiplayer() || ServerState.getInstance().isMultiplayerReady()) {
			try {
				if (ScreenManager.getInstance().assetManager.update() && !error) {
					// we are done loading, let's move to another screen!
					ScreenManager.getInstance().showScreen(ScreenEnum.GAME_STAGE);
				}
			} catch (Exception e) {
				loadingLabel.setText("Error: files missing");
				error = true;
			}
			if (!error) {
				loadingLabel.setText("Loading (" + Math.round(ScreenManager.getInstance().assetManager.getProgress() * 100) + "%)");
			}
		} else {

			if (isConnecting && future.isDone()) {
				isConnecting = false;
				try {
					error = future.get() != 1;
				} catch (Exception e) {
					error = true;
					e.printStackTrace();
				}
			}

			// If not try to connect
			loadingLabel.setText("Connecting...");
			if (!error && !isConnecting && ServerState.getInstance().getClient() == null) {
				try {
					ExecutorService executor = Executors.newFixedThreadPool(1);
					future = executor.submit(connectServer);
					isConnecting = true;
				} catch (Exception e) {
					error = true;
					isConnecting = false;
				}
			} else if (!isConnecting && error) {
				loadingLabel.setText("Connection error");
			}

		}



		super.act(delta);
		super.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
