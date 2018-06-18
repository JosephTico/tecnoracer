package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tecno.racer.ServerState;
import helpers.AbstractScreen;
import helpers.DigitFilter;
import helpers.ScreenEnum;
import helpers.ScreenManager;

public class MainMenu extends AbstractScreen {

	private TextureAtlas atlas;
	protected Skin skin;


	public MainMenu() {
		super();
	}

	@Override
	public void buildStage() {
		atlas = new TextureAtlas("skin/terra-mother-ui.atlas");
		skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"), atlas);

	}

	@Override
	public void show() {
		ServerState.resetInstance();

		//Stage should controll input:
		Gdx.input.setInputProcessor(this);

		Table root = new Table();
		root.setFillParent(true);
		//root.setBackground(skin.getTiledDrawable("tile").tint(Color.CYAN));
		root.setBackground(skin.getTiledDrawable("tile-a"));
		addActor(root);

		Image image = new Image(new Texture("sprites/logo.jpg"));
		root.add(image).width(419/2).height(300/2).padBottom(20f);

		root.row();
		Table content = new Table();
		root.add(content);



		Table right = new Table();
		content.add(right).padLeft(25.0f);


		Table table = new Table(skin);
		table.setBackground("window-c");
		right.add(table).height(60.0f).width(200.0f).padBottom(30);

		Label label = new Label("Configuration", skin);
		table.add(label).expandX().center();

		right.row();
		table = new Table(skin);
		table.setBackground("window-c");
		right.add(table).colspan(2).height(90.0f).padBottom(10f);

		label = new Label("Server IP:", skin);
		table.add(label);

		table.row();
		TextField hostField = new TextField("192.168.1.15", skin);
		table.add(hostField).center();
		setKeyboardFocus(hostField);
		hostField.setCursorPosition(hostField.getText().length());

		right.row();
		table = new Table(skin);
		table.setBackground("window-c");
		right.add(table).colspan(2).height(90.0f).padBottom(10f);

		label = new Label("Port:", skin);
		table.add(label);

		table.row();
		TextField portField = new TextField("1973", skin);
		portField.setTextFieldFilter(new DigitFilter());
		table.add(portField).center();

		content.row();
		table = new Table(skin);
		table.setBackground("window-c");
		content.add(table).colspan(2).growX().minWidth(500f).minHeight(60.0f).padTop(15.0f);

		label = new Label("Start game:", skin);
		table.add(label);

		ImageTextButton imageTextButton = new ImageTextButton("Single-player", skin);
		table.add(imageTextButton).expandX().center();
		//Add listeners to buttons
		imageTextButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerState.getInstance().setMultiplayer(false);
				ScreenManager.getInstance().showScreen( ScreenEnum.LOADING);
			}
		});

		imageTextButton = new ImageTextButton("Multi-player", skin);
		table.add(imageTextButton).expandX().center();
		//Add listeners to buttons
		imageTextButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ServerState.getInstance().setMultiplayer(true);
				ServerState.getInstance().setHost(hostField.getText());
				ServerState.getInstance().setPort(Integer.parseInt(portField.getText()));
				ScreenManager.getInstance().showScreen( ScreenEnum.LOADING);
			}
		});

		imageTextButton = new ImageTextButton("Exit", skin);
		table.add(imageTextButton).expandX().center();
		//Add listeners to buttons
		imageTextButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ServerState.getInstance().getClient() != null)
					ServerState.getInstance().getClient().exit();
				Gdx.app.exit();
			}
		});

		/*container = new Container(new Label("3", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container);*/

	}



	@Override
	public void render(float delta) {

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.act(delta);
		super.draw();

		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
