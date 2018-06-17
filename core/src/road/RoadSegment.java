package road;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tecno.racer.GameParameters;
import helpers.ScreenManager;

import java.util.ArrayList;
import java.util.List;

import static com.tecno.racer.GameParameters.LANES;

public class RoadSegment {
	private static final TextureRegion DARK_ROAD = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/dark_road.jpg", Texture.class));
	private static final TextureRegion LIGHT_ROAD = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/light_road.jpg", Texture.class));
	private static final TextureRegion WHITE_LINE = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/white_lines.jpg", Texture.class));
	private static final TextureRegion RED_RUMBLE = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/red_rumble.jpg", Texture.class));
	private static final TextureRegion WHITE_RUMBLE = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/white_rumble.jpg", Texture.class));
	private static final TextureRegion DARK_GRASS = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/dark_grass.jpg", Texture.class));
	private static final TextureRegion LIGHT_GRASS = new TextureRegion(ScreenManager.getInstance().assetManager.get("background/light_grass.jpg", Texture.class));

	private final Point p1;
	private final Point p2;
	private final int index;
	private final TextureRegion roadTexture;
	private final boolean hasLines;
	private final TextureRegion grassTexture;
	private final TextureRegion rumbleTexture;
	private final float curve;

	private List<Car> cars = new ArrayList<>();
	private List<Scenery> sceneries = new ArrayList<>();



	private float clip;

	public static RoadSegment createLightRoadSegment(int index, Point p1, Point p2, float curve) {
		return new RoadSegment(index, p1, p2, curve, LIGHT_ROAD, LIGHT_GRASS, WHITE_RUMBLE, true);
	}

	public static RoadSegment createDarkRoadSegment(int index, Point p1, Point p2, float curve) {
		return new RoadSegment(index, p1, p2, curve, DARK_ROAD, DARK_GRASS, RED_RUMBLE, false);
	}


	public RoadSegment(int index, Point p1, Point p2, float curve, TextureRegion roadTexture, TextureRegion grassTexture, TextureRegion rumbleTexture, boolean hasLines) {
		this.p1 = p1;
		this.p2 = p2;
		this.curve = curve;
		this.index = index;
		this.roadTexture = roadTexture;
		this.grassTexture = grassTexture;
		this.rumbleTexture = rumbleTexture;
		this.hasLines = hasLines;
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public int getIndex() {
		return index;
	}

	public void setClip(float clip) {
		this.clip = clip;
	}

	public float getClip() {
		return clip;
	}

	public float getCurve() {
		return curve;
	}

	public void addScenery(Scenery scenery) {
		this.sceneries.add(scenery);
	}

	public List<Scenery> getScenery() {
		return sceneries;
	}

	public void draw(PolygonSpriteBatch polygonSpriteBatch) {

		Point.Screen screen1 = p1.screen;
		Point.Screen screen2 = p2.screen;

		float x1 = screen1.x;
		float y1 = screen1.y;
		float w1 = screen1.w;

		float x2 = screen2.x;
		float y2 = screen2.y;
		float w2 = screen2.w;

		float lanew1, lanew2, lanex1, lanex2, lane;

		float r1 = rumbleWidth(w1, LANES);
		float r2 = rumbleWidth(w2, LANES);

		float l1 = laneMarkerWidth(w1, LANES);
		float l2 = laneMarkerWidth(w2, LANES);

		polygonSpriteBatch.draw(grassTexture, 0, screen2.y, GameParameters.ROAD_WIDTH, screen1.y - screen2.y);

		polygon(polygonSpriteBatch, x1 - w1 - r1, y1, x1 - w1, y1, x2 - w2, y2, x2 - w2 - r2, y2, rumbleTexture);
		polygon(polygonSpriteBatch, x1 + w1 + r1, y1, x1 + w1, y1, x2 + w2, y2, x2 + w2 + r2, y2, rumbleTexture);
		polygon(polygonSpriteBatch, x1 - w1, y1, x1 + w1, y1, x2 + w2, y2, x2 - w2, y2, roadTexture);


		if (hasLines) {
			lanew1 = w1 * 2 / LANES;
			lanew2 = w2 * 2 / LANES;
			lanex1 = x1 - w1 + lanew1;
			lanex2 = x2 - w2 + lanew2;
			for (lane = 1; lane < LANES; lanex1 += lanew1, lanex2 += lanew2, lane++) {
				polygon(polygonSpriteBatch, lanex1 - l1 / 2, y1, lanex1 + l1 / 2, y1, lanex2 + l2 / 2, y2, lanex2 - l2 / 2, y2, WHITE_LINE);
			}
		}
	}

	private void polygon(PolygonSpriteBatch polygonSpriteBatch,
	                     float x1, float y1,
	                     float x2, float y2,
	                     float x3, float y3,
	                     float x4, float y4,
	                     TextureRegion textureRegion) {

		float dx2 = x2 - x1;
		float dy2 = y2 - y1;

		float dx3 = x3 - x1;
		float dy3 = y3 - y1;

		float dx4 = x4 - x1;
		float dy4 = y4 - y1;

		PolygonRegion polygonRegion = new PolygonRegion(textureRegion,
				new float[]{
						0, 0,
						dx2, dy2,
						dx3, dy3,
						0, 0,
						dx4, dy4,
						dx3, dy3
				},
				new short[]{0, 1, 2, 3, 4, 5}
		);

		polygonSpriteBatch.draw(polygonRegion, x1, y1);
	}

	private float rumbleWidth(float projectedRoadWidth, float lanes) {
		return projectedRoadWidth / Math.max(32, 2 * lanes);
	}

	private float laneMarkerWidth(float projectedRoadWidth, float lanes) {
		return projectedRoadWidth / Math.max(32, 8 * lanes);
	}

	public void addCar(Car car) {
		this.cars.add(car);
	}

	public List<Car> getCars() {
		return cars;
	}

}
