package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import helpers.AbstractScreen;
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
		//Stage should controll input:
		Gdx.input.setInputProcessor(this);

		Table root = new Table();
		root.setFillParent(true);
		//root.setBackground(skin.getTiledDrawable("tile").tint(Color.CYAN));
		root.setBackground(skin.getTiledDrawable("tile-a"));
		addActor(root);

		Image image = new Image(skin, "label-title");
		root.add(image);

		root.row();
		Table content = new Table();
		root.add(content);

		Table left = new Table();
		content.add(left);

		image = new Image(skin, "pc-1");
		left.add(image);

		Table table = new Table(skin);
		table.setBackground("window-c");
		left.add(table).height(60.0f).width(100.0f);

		Label label = new Label("Nesh", skin);
		table.add(label).expandX().left();

		left.row();
		image = new Image(skin, "pc-2");
		left.add(image);

		table = new Table(skin);
		table.setBackground("window-c");
		left.add(table).height(60.0f).width(100.0f);

		label = new Label("Pola", skin);
		table.add(label).expandX().left();

		left.row();
		image = new Image(skin, "pc-3");
		left.add(image);

		table = new Table(skin);
		table.setBackground("window-c");
		left.add(table).height(60.0f).width(100.0f);

		label = new Label("Geoff", skin);
		table.add(label).expandX().left();

		left.row();
		image = new Image(skin, "pc-4");
		left.add(image);

		table = new Table(skin);
		table.setBackground("window-c");
		left.add(table).height(60.0f).width(100.0f);

		label = new Label("Po", skin);
		table.add(label).expandX().left();

		Table right = new Table();
		content.add(right).padLeft(25.0f);

		image = new Image(skin, "cat");
		right.add(image);

		table = new Table(skin);
		table.setBackground("window-c");
		right.add(table).height(60.0f).width(100.0f);

		label = new Label("Queen", skin);
		table.add(label).expandX().left();

		right.row();
		table = new Table(skin);
		table.setBackground("window-c");
		right.add(table).colspan(2).height(90.0f);

		label = new Label("Favorite food:", skin);
		table.add(label);

		table.row();
		TextField textField = new TextField("Steak", skin);
		table.add(textField).right();

		right.row();
		table = new Table(skin);
		table.setBackground("window-c");
		right.add(table).colspan(2).height(90.0f);

		label = new Label("Coolest thing:", skin);
		table.add(label);

		table.row();
		textField = new TextField("Rockin", skin);
		table.add(textField).right();
		setKeyboardFocus(textField);
		textField.setCursorPosition(textField.getText().length());

		content.row();
		table = new Table(skin);
		table.setBackground("window-c");
		content.add(table).colspan(2).growX().minHeight(60.0f).padTop(15.0f);

		label = new Label("Are you sure?", skin);
		table.add(label);

		ImageTextButton imageTextButton = new ImageTextButton("Yep", skin);
		table.add(imageTextButton).expandX().right();
		//Add listeners to buttons
		imageTextButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().showScreen( ScreenEnum.LOADING);
			}
		});

		imageTextButton = new ImageTextButton("Nope", skin);
		table.add(imageTextButton).padLeft(10.0f);

		root.row();
		table = new Table(skin);
		table.setBackground("window-c");
		root.add(table).width(400.0f).minHeight(60.0f).padTop(15.0f);

		image = new Image(skin, "smash");
		table.add(image).expand().top().left();

		root.row();
		table = new Table(skin);
		table.setBackground("window-c");
		root.add(table).width(400.0f).minHeight(60.0f).padTop(15.0f);

		label = new Label("Balance: $100", skin);
		table.add(label);

		table.row();
		textField = new TextField("999", skin, "number");
		textField.setAlignment(Align.right);
		table.add(textField).width(100);

		root.row();
		Stack stack = new Stack();
		root.add(stack).width(125.0f).height(150.0f).padTop(15.0f);

		image = new Image(skin.getTiledDrawable("tile-b"));
		Container container = new Container(image);
		container.pad(5.0f);
		container.fill();
		stack.add(container);

		table = new Table(skin);
		table.setBackground("window-player-c");
		stack.add(table);

		label = new Label("Nesh", skin, "year199x");
		table.add(label).expandX().left().colspan(4).padLeft(10.0f);

		table.row();
		image = new Image(skin, "label-hp");
		table.add(image).expandX().right().padRight(10.0f);

		container = new Container(new Label("3", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container);

		container = new Container(new Label("0", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container);

		container = new Container(new Label("0", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container).padRight(10.0f);

		table.row();
		image = new Image(skin, "label-pp");
		table.add(image).expandX().right().padRight(10.0f);

		container = new Container(new Label("1", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container);

		container = new Container(new Label("0", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container);

		container = new Container(new Label("0", skin, "black"));
		container.setBackground(skin.getDrawable("digit-box"));
		table.add(container).padRight(10.0f);
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
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
