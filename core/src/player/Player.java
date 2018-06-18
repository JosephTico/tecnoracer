package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;
import com.tecno.racer.ServerState;
import helpers.ScreenEnum;
import helpers.ScreenManager;
import road.RoadSegment;

public class Player extends Actor {

	private float speed = 0;
	float playerX = 0;
	public boolean keyRight = false;
	public boolean keyLeft = false;
	public boolean keyFaster = false;
	public boolean keySlower = false;

	private static Texture SPRITE_RIGHT;
	private static Texture SPRITE_LEFT;
	private static Texture SPRITE_STRAIGHT;

	private Texture currentTexture;

	private Sprite sprite;

	public Player() {
		SPRITE_RIGHT = ScreenManager.getInstance().assetManager.get("sprites/player_right" + ServerState.getInstance().getId() + ".png",  Texture.class);
		SPRITE_LEFT = ScreenManager.getInstance().assetManager.get("sprites/player_left" + ServerState.getInstance().getId() + ".png",  Texture.class);
		SPRITE_STRAIGHT = ScreenManager.getInstance().assetManager.get("sprites/player_straight" + ServerState.getInstance().getId() + ".png",  Texture.class);

		currentTexture = SPRITE_STRAIGHT;

		sprite = new Sprite(currentTexture);

		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
		setTouchable(Touchable.enabled);
	}

	private float accelerate(float v, float accel, float dt) {
		return v + (accel * dt);
	}

	private float increase(float start, float increment, int max) {
		float result = start + increment;
		while (result >= max)
			result -= max;
		while (result < 0)
			result += max;
		return result;
	};

	public void update(float delta, GameState state) {
		state.position = Math.round(increase(state.position, delta * speed, state.trackLength));

		float speedPercent = speed / GameParameters.MAX_SPEED;
		float dx = delta * speedPercent * 2; // at top speed, should be able to cross from left to right (-1 to 1) in 1 second


		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && Gdx.input.getX() < GameParameters.WIDTH / 2)) {
			playerX = playerX - dx;
			if (speed > 0) {
				sprite.setTexture(SPRITE_LEFT);
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)  || (Gdx.input.isTouched() && Gdx.input.getX() >= GameParameters.WIDTH / 2)) {
			playerX = playerX + dx;
			if (speed > 0) {
				sprite.setTexture(SPRITE_RIGHT);
			}
		} else {
			sprite.setTexture(SPRITE_STRAIGHT);
		}

		RoadSegment playerSegment = state.road.findSegment(Math.round(state.position + GameParameters.PLAYER_Z));

		playerX = playerX - (dx * speedPercent * playerSegment.getCurve() * GameParameters.CENTRIFUGAL);


		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			if (ServerState.getInstance().isMultiplayer() && ServerState.getInstance().getClient() != null)
				ServerState.getInstance().getClient().exit();
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isTouched()) {
			speed = accelerate(speed, GameParameters.ACCEL, delta);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			speed = accelerate(speed, GameParameters.BREAKING, delta);
		} else {
			speed = accelerate(speed, GameParameters.DECEL, delta);
		}

		if (((playerX < -1) || (playerX > 1)) && (speed > GameParameters.OFF_ROAD_LIMIT)) {
			speed = accelerate(speed, GameParameters.OFF_ROAD_DECEL, delta);
		}

		playerX = MathUtils.clamp(playerX, -2, 2);
		speed = MathUtils.clamp(speed, 0, GameParameters.MAX_SPEED);

		this.setPosition(playerX, 0);



	}


	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	protected void positionChanged() {
		sprite.setPosition(getX(), getY());
		super.positionChanged();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {




	}


	public float getWidth() {
		return SPRITE_STRAIGHT.getWidth();
	}
	public float getHeight() {
		return SPRITE_STRAIGHT.getHeight();
	}
	public void draw(Batch batch, float x, float y, float width, float height) {
		draw(batch, x, y, width, height, 1);
	}

	public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
		batch.draw(sprite.getTexture(), x, y, width, height);
	}

	public void render(PolygonSpriteBatch polygonSpriteBatch, float speedPercent, float scale, float destX) {

		float bounce = (float) (1.5 * Math.random() * speedPercent) * MathUtils.random(-1, 1);

		float destW = (getWidth() * scale) * GameParameters.ROAD_SCALE_FACTOR;
		float destH = (getHeight() * scale) * GameParameters.ROAD_SCALE_FACTOR;

		destX = destX + (destW * -0.5F);

		draw(polygonSpriteBatch, destX, bounce, destW, destH);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
}