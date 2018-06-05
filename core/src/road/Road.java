package road;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import helpers.GameInfo;
import helpers.GameState;

import java.util.ArrayList;

public class Road {
	private GameState state;
	private ArrayList<RoadSegment> roadSegments = new ArrayList<>();
	private PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();

	private static final Texture BACKGROUND_HILLS = new Texture("background/hills.png");
	private static final Texture BACKGROUND_SKY = new Texture("background/sky.png");
	private static final Texture BACKGROUND_TREES = new Texture("background/trees.png");

	public Road(GameState state) {
		this.state = state;
	}

	public void resetRoad() {
		roadSegments.clear();

		for (int i = 0; i < 500; i++) {
			var p1 = new Point();
			p1.world.z = i * GameInfo.SEGMENT_LENGTH;
			var p2 = new Point();
			p2.world.z = (i+1) * GameInfo.SEGMENT_LENGTH;

			if (Math.floor(i / GameInfo.RUMBLE_LENGTH) % 2 == 1) {
				this.roadSegments.add(RoadSegment.createDarkRoadSegment(i, p1, p2));
			} else {
				this.roadSegments.add(RoadSegment.createLightRoadSegment(i, p1, p2));
			}
		}

		this.state.trackLength = this.roadSegments.size() * GameInfo.SEGMENT_LENGTH;
	}

	public RoadSegment findSegment(int z) {
		return this.roadSegments.get((int) Math.floor(z / GameInfo.SEGMENT_LENGTH) % roadSegments.size());
	}

	public void render(Camera camera) {
		polygonSpriteBatch.setProjectionMatrix(camera.combined);
		polygonSpriteBatch.begin();

		polygonSpriteBatch.draw(BACKGROUND_SKY, 0, 0, BACKGROUND_SKY.getWidth(), GameInfo.HEIGHT);
		polygonSpriteBatch.draw(BACKGROUND_HILLS, 0, 0, BACKGROUND_HILLS.getWidth(), GameInfo.HEIGHT);
		polygonSpriteBatch.draw(BACKGROUND_TREES, 0, 0, BACKGROUND_TREES.getWidth(), GameInfo.HEIGHT);

		RoadSegment playerSegment = findSegment(Math.round(state.position + GameInfo.PLAYER_Z));
		RoadSegment baseSegment = findSegment(state.position);

		float playerPercent = percentRemaining(state.position + GameInfo.PLAYER_Z, GameInfo.SEGMENT_LENGTH);
		float playerY = interpolate(playerSegment.getP1().world.y, playerSegment.getP2().world.y, playerPercent);

		float maxY = 0;
		float x = 0;
		float basePercent = (state.position % GameInfo.SEGMENT_LENGTH) / (float) GameInfo.SEGMENT_LENGTH;
		float dx = -(0 * 0); // TODO: CURVAS FUTURO


		for (int n = 0; n < Math.min(GameInfo.DRAW_DISTANCE, roadSegments.size()); n++) {

			RoadSegment segment = roadSegments.get((baseSegment.getIndex() + n) % roadSegments.size());
			segment.setClip(maxY);
			boolean segmentLooped = segment.getIndex() < baseSegment.getIndex();
			project(segment.getP1(), state.player.getX() * GameInfo.ROAD_WIDTH - x, playerY + GameInfo.CAMERA_HEIGHT, state.position - (segmentLooped ? state.trackLength : 0), GameInfo.CAMERA_DEPTH, GameInfo.WIDTH, GameInfo.HEIGHT, GameInfo.ROAD_WIDTH);
			project(segment.getP2(), state.player.getX() * GameInfo.ROAD_WIDTH - x - dx, playerY + GameInfo.CAMERA_HEIGHT, state.position - (segmentLooped ? state.trackLength : 0), GameInfo.CAMERA_DEPTH, GameInfo.WIDTH, GameInfo.HEIGHT, GameInfo.ROAD_WIDTH);

			x = x + dx;
			dx = dx + 0; //TODO: Curva en 0

			if (
					segment.getP1().camera.z <= GameInfo.CAMERA_DEPTH // Behind us
							|| (segment.getP2().screen.y <= segment.getP1().screen.y)  // back face cull
							|| (segment.getP2().screen.y < maxY)) { // clip by (already rendered) segment
				continue;
			}
			segment.draw(polygonSpriteBatch);
			maxY = segment.getP1().screen.y;
		}

		for (int n = (Math.min(GameInfo.DRAW_DISTANCE, roadSegments.size()) - 1); n > 0; n--) {
			RoadSegment segment = roadSegments.get((baseSegment.getIndex() + n) % roadSegments.size());
			if (segment == playerSegment) {
				//renderPlayer(speed / maxSpeed, CAMERA_DEPTH / PLAYER_Z, WIDTH / 2);
			}
		}


		polygonSpriteBatch.end();


	}

	private float percentRemaining(float n, float total) {
		return (n % total) / total;
	}

	private float interpolate(float a, float b, float percent) {
		return a + (b - a) * percent;
	}

	private void project(Point p, float cameraX, float cameraY, float cameraZ, float cameraDepth, float width, float height, float roadWidth) {
		p.camera.x = p.world.x - cameraX;
		p.camera.y = p.world.y - cameraY;
		p.camera.z = p.world.z - cameraZ;
		p.screen.scale = cameraDepth / p.camera.z;
		p.screen.x = Math.round((width / 2) + (p.screen.scale * p.camera.x * width / 2));
		p.screen.y = Math.round((height / 2) - (p.screen.scale * p.camera.y * -height / 2));
		p.screen.w = Math.round((p.screen.scale * roadWidth * width / 2));
	}

}
