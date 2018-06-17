package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import helpers.AbstractScreen;
import helpers.ScreenEnum;
import helpers.ScreenManager;

public class Loading extends AbstractScreen {

	protected Skin skin;
	Label loadingLabel;
	boolean error = false;
	private TextureAtlas atlas;

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

		loadingLabel = new Label("Loading... (0%)", skin);
		table.add(loadingLabel).expandX().center();

		loadAssets();
	}

	private void loadAssets() {
		AssetManager assMan = ScreenManager.getInstance().assetManager;
		assMan.load("music/racer.mp3", Music.class);
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


	}


	@Override
	public void render(float delta) {
		try {
			if (ScreenManager.getInstance().assetManager.update() && !error) {
				// we are done loading, let's move to another screen!
				ScreenManager.getInstance().showScreen(ScreenEnum.GAME_STAGE);
			}
		} catch (Exception e) {
			loadingLabel.setText("Error: files missing");
			error = true;
		}

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!error) {
			loadingLabel.setText("Loading (" + Math.round(ScreenManager.getInstance().assetManager.getProgress() * 100) + "%)");
		}

		super.act(delta);
		super.draw();

	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
