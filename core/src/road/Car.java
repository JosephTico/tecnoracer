package road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.tecno.racer.GameParameters;
import helpers.ScreenManager;

import java.util.Random;

/**
 * Created by Cookie on 03/05/2014.
 */
public class Car {

	protected Texture texture;

	protected float offset;
	protected int z;
	protected float speed;

	public Car(float offset, int z, float speed) {
		this.offset = offset;
		this.z = z;
		this.speed = speed;
		Random rand = new Random();
		int randNum = rand.nextInt(3) + 1;
		texture = ScreenManager.getInstance().assetManager.get("sprites/car" + intToString(randNum, 2) + ".png");
	}

	public static String intToString(int num, int digits) {
		String output = Integer.toString(num);
		while (output.length() < digits) output = "0" + output;
		return output;
	}

	public void dispose() {
		texture.dispose();
		speed = 0;
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
		return texture.getHeight();
	}

	public int getWidth() {
		return texture.getWidth();
	}

	public void updateCar(int z, float offset) {
		this.z = z;
		this.offset = offset;
	}

	public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
		batch.draw(texture, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);
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