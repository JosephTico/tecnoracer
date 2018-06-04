package player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import helpers.GameInfo;
import helpers.GameState;

public class Player extends Actor {

	float speed = 0;
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

	private float increase(float start, float increment, float max) {
		var result = start + increment;
		while (result >= max)
			result -= max;
		while (result < 0)
			result += max;
		return result;
	};

	private float limit(float value, float min, float max) {
		return Math.max(min, Math.min(value, max));
	}

	public void update(float delta, GameState state) {
		state.position = increase(state.position, delta * speed, state.trackLength);

		float dx = delta * 2 * (this.speed / GameInfo.maxSpeed);

		if (this.keyLeft) {
			this.playerX = this.playerX - dx;
		} else if (this.keyRight) {
			this.playerX = this.playerX + dx;
		}

		if (this.keyFaster) {
			this.speed = accelerate(this.speed, GameInfo.accel, delta);
		} else if (this.keySlower) {
			this.speed = accelerate(this.speed, GameInfo.breaking, delta);
		} else {
			this.speed = accelerate(this.speed, GameInfo.decel, delta);
		}

		if (((this.playerX < -1) || (this.playerX > 1)) && (this.speed > GameInfo.offRoadLimit)) {
			this.speed = accelerate(this.speed, GameInfo.offRoadDecel, delta);
		}

		this.playerX = limit(this.playerX, -2, 2);     // dont ever let player go too far out of bounds
		this.speed   = limit(this.speed, 0, GameInfo.maxSpeed); // or exceed maxSpeed*/

		System.out.format("X %f\n", this.playerX);
		System.out.format("Velocidad %f\n", this.speed);


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