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
import helpers.GameInfo;
import helpers.GameState;

public class Player extends Actor {

	private float speed = 0;
	float playerX = 0;
	boolean keyRight = false;
	boolean keyLeft = false;
	boolean keyFaster = false;
	boolean keySlower = false;

	Sprite sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));

	public Player() {

		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
		setTouchable(Touchable.enabled);

		addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
					case Input.Keys.RIGHT:
						Player.this.keyRight = true;
						break;
					case Input.Keys.LEFT:
						Player.this.keyLeft = true;
						break;
					case Input.Keys.DOWN:
						Player.this.keySlower = true;
						break;
					case Input.Keys.UP:
						Player.this.keyFaster = true;
						break;
				}
				return true;
			};

			public boolean keyUp(InputEvent event, int keycode) {
				switch (keycode) {
					case Input.Keys.RIGHT:
						Player.this.keyRight = false;
						break;
					case Input.Keys.LEFT:
						Player.this.keyLeft = false;
						break;
					case Input.Keys.DOWN:
						Player.this.keySlower = false;
						break;
					case Input.Keys.UP:
						Player.this.keyFaster = false;
						break;
				}
				return true;
			}
		});
	}

	private float accelerate(float v, float accel, float dt) {
		return v + (accel * dt);
	}

	private float increase(float start, float increment, int max) {
		var result = start + increment;
		while (result >= max)
			result -= max;
		while (result < 0)
			result += max;
		return result;
	};

	public void update(float delta, GameState state) {
		state.position = Math.round(increase(state.position, delta * speed, state.trackLength));

		float dx = delta * 2 * (this.speed / GameInfo.MAX_SPEED);

		if (this.keyLeft) {
			this.playerX = this.playerX - dx;
		} else if (this.keyRight) {
			this.playerX = this.playerX + dx;
		}

		if (this.keyFaster) {
			this.speed = accelerate(this.speed, GameInfo.ACCEL, delta);
		} else if (this.keySlower) {
			this.speed = accelerate(this.speed, GameInfo.BREAKING, delta);
		} else {
			this.speed = accelerate(this.speed, GameInfo.DECEL, delta);
		}

		if (((this.playerX < -1) || (this.playerX > 1)) && (this.speed > GameInfo.OFF_ROAD_LIMIT)) {
			this.speed = accelerate(this.speed, GameInfo.OFF_ROAD_DECEL, delta);
		}

		playerX = MathUtils.clamp(playerX, -2, 2);
		speed = MathUtils.clamp(speed, 0, GameInfo.MAX_SPEED);

		this.setPosition(playerX, 0);

		System.out.format("X %f\n", this.playerX);
		System.out.format("Velocidad %f\n", this.speed);


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