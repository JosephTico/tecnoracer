package road;

import com.badlogic.gdx.graphics.Texture;
import helpers.ScreenManager;

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
