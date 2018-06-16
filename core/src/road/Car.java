package road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.tecno.racer.GameParameters;

/**
 * Created by Cookie on 03/05/2014.
 */
public class Car {

	private static final Texture TEXTURE = new Texture("sprites/player_straight.png");

	private float offset;
	private int z;
	private final float speed;

	public Car(float offset, int z, float speed) {
		this.offset = offset;
		this.z = z;
		this.speed = speed;
	}

	public float getOffset() {
		return offset;
	}

	public int getZ() {
		return z;
	}

	public float getSpeed() {
		return speed;
	}

	public int getHeight() {
		return TEXTURE.getHeight();
	}

	public int getWidth() {
		return TEXTURE.getWidth();
	}

	public void updateCar(int z, float offset) {
		this.z = z;
		this.offset = offset;
	}

	public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
		batch.draw(TEXTURE, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);
	}

	public void render(PolygonSpriteBatch polygonSpriteBatch, float scale, float destX, float destY, float clipY) {

		float destW = (getWidth() * scale) * GameParameters.ROAD_SCALE_FACTOR;
		float destH = (getHeight() * scale) * GameParameters.ROAD_SCALE_FACTOR;

		destX = destX + (destW * -0.5F);

		float clipH = Math.max(0, destY + destH - clipY);
		float amountToChop = clipH / destH;
		float clamped = MathUtils.clamp(amountToChop, 0, 1);

		if (clipH > 0) {
			draw(polygonSpriteBatch, destX, destY, destW, destH, clamped);
		}
	}
}