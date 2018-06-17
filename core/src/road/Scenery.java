package road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.tecno.racer.GameParameters;
import helpers.ScreenManager;

import java.util.Random;

/**
 * Created by Cookie on 04/05/2014.
 */
public class Scenery {
	//    private static final Texture texture = new Texture("assets/billboard01.png");
	protected Texture texture;

	protected float offset;
	protected int z;

	public Scenery(float offset, int z) {
		this.offset = offset;
		this.z = z;
		Random rand = new Random();
		int randNum = rand.nextInt(8) + 1;
		texture = ScreenManager.getInstance().assetManager.get("sprites/billboard" + intToString(randNum, 2) + ".png", Texture.class);
	}

	public void dispose() {
		texture.dispose();
	}

	public float getOffset() {
		return offset;
	}

	public int getZ() {
		return z;
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

	public static String intToString(int num, int digits) {
		String output = Integer.toString(num);
		while (output.length() < digits) output = "0" + output;
		return output;
	}

	public void draw(Batch batch, float x, float y, float width, float height, float percentageToDraw) {
		batch.draw(texture, x, y + height * (1 - percentageToDraw), width, height * percentageToDraw, 0, percentageToDraw, 1, 0);
	}

	@Override
	public String toString() {
		return "Scenery{" +
				"offset=" + offset +
				", z=" + z +
				'}';
	}

	public void render(PolygonSpriteBatch polygonSpriteBatch, float scale, float destX, float destY, float clipY) {


		float destW = (getWidth() * scale) * GameParameters.ROAD_SCALE_FACTOR;
		float destH = (getHeight() * scale) * GameParameters.ROAD_SCALE_FACTOR;

		destX = destX + (destW * (getOffset() < 0 ? -1 : 0));

		float clipH = Math.max(0, destY + destH - clipY);
		float amountToChop = clipH / destH;
		float percentageToDraw = MathUtils.clamp(amountToChop, 0, 1);

		draw(polygonSpriteBatch, destX, destY, destW, destH, percentageToDraw);
	}

}