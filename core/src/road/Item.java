package road;

import com.badlogic.gdx.graphics.Texture;
import helpers.ScreenManager;

public class Item extends Car {
	Types type;
	private boolean active = true;

	public Item(float offset, int z, Types type) {
		super(offset, z, 0);
		this.type = type;
		if (type == Types.BOOST) {
			texture = ScreenManager.getInstance().assetManager.get("sprites/bush1.png", Texture.class);
		} else if (type == Types.LIFE) {
			texture = ScreenManager.getInstance().assetManager.get("sprites/bush2.png", Texture.class);
		}
	}

	public boolean isActive() {
		return active;
	}

	;

	public void setActive(boolean active) {
		this.active = active;
	}

	public enum Types {BOOST, LIFE}
}
