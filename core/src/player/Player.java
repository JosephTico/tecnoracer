package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;

public class Player extends Actor {

	private float speed = 0;
	float playerX = 0;
	public boolean keyRight = false;
	public boolean keyLeft = false;
	public boolean keyFaster = false;
	public boolean keySlower = false;

	Sprite sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));

	public Player() {

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

		float dx = delta * 2 * (this.speed / GameParameters.MAX_SPEED);

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && Gdx.input.getX() < GameParameters.WIDTH / 2)) {
			this.playerX = this.playerX - dx;
		} else if (this.keyRight  || (Gdx.input.isTouched() && Gdx.input.getX() >= GameParameters.WIDTH / 2)) {
			this.playerX = this.playerX + dx;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isTouched()) {
			this.speed = accelerate(this.speed, GameParameters.ACCEL, delta);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.speed = accelerate(this.speed, GameParameters.BREAKING, delta);
		} else {
			this.speed = accelerate(this.speed, GameParameters.DECEL, delta);
		}

		if (((this.playerX < -1) || (this.playerX > 1)) && (this.speed > GameParameters.OFF_ROAD_LIMIT)) {
			this.speed = accelerate(this.speed, GameParameters.OFF_ROAD_DECEL, delta);
		}

		playerX = MathUtils.clamp(playerX, -2, 2);
		speed = MathUtils.clamp(speed, 0, GameParameters.MAX_SPEED);

		this.setPosition(playerX, 0);



	}


	public float getSpeed() {
		return speed;
	}

	@Override
	protected void positionChanged() {
		sprite.setPosition(getX(), getY());
		super.positionChanged();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
}