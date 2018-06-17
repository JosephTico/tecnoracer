package road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.tecno.racer.GameParameters;
import helpers.ScreenManager;

import java.util.Random;

public class Bomb extends Car {

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private boolean active = true;

	public Bomb(float offset, int z) {
		super(offset, z, 0);
		texture = ScreenManager.getInstance().assetManager.get("sprites/bomb.png", Texture.class);
	}
}
