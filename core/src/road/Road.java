package road;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.tecno.racer.GameParameters;
import com.tecno.racer.GameState;
import com.tecno.racer.ServerState;
import helpers.ScreenManager;
import jexxus.Server;

import java.util.ArrayList;
import java.util.List;

import static com.tecno.racer.GameParameters.DRAW_DISTANCE;
import static com.tecno.racer.GameParameters.SPRITE_SCALE;

public class Road {
	private GameState state;
	private List<RoadSegment> roadSegments = new ArrayList<RoadSegment>();
	private PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();
	private List<Car> cars = new ArrayList<Car>();
	private List<Bomb> bombs = new ArrayList<Bomb>();
	private List<Item> items = new ArrayList<Item>();
	private boolean serverReady = false;

	private static final Texture BACKGROUND_HILLS = ScreenManager.getInstance().assetManager.get("background/hills.png", Texture.class);
	private static final Texture BACKGROUND_SKY = ScreenManager.getInstance().assetManager.get("background/sky.png", Texture.class);
	private static final Texture BACKGROUND_TREES = ScreenManager.getInstance().assetManager.get("background/trees.png", Texture.class);

	public Road(GameState state) {
		this.state = state;
		BACKGROUND_HILLS.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		BACKGROUND_SKY.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		BACKGROUND_TREES.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
	}

	public void resetRoad() {
		roadSegments.clear();

		RoadBuilder roadBuilder = new RoadBuilder()
				.addStraight(RoadBuilder.Length.LONG)
				.addRightCurve(RoadBuilder.Length.SHORT, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.NONE)
				.addLeftCurve(RoadBuilder.Length.SHORT, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.HIGH)
				.addRightCurve(RoadBuilder.Length.MEDIUM, RoadBuilder.Curve.LIGHT, RoadBuilder.Hills.HIGH)
				.addLeftCurve(RoadBuilder.Length.MEDIUM, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.HIGH)
				.addRightCurve(RoadBuilder.Length.LONG, RoadBuilder.Curve.LIGHT, RoadBuilder.Hills.MEDIUM)
				.addLeftCurve(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.MEDIUM)
				.addDip(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
				.addHill(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
				.addHill(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
				.addDip(RoadBuilder.Length.SHORT, RoadBuilder.Hills.HIGH)
				.addStraight(RoadBuilder.Length.MEDIUM)
				.addRightCurve(RoadBuilder.Length.LONG, RoadBuilder.Curve.LIGHT, RoadBuilder.Hills.NONE)
				.addLeftCurve(RoadBuilder.Length.LONG, RoadBuilder.Curve.TIGHT, RoadBuilder.Hills.NONE)
				.addStraight(RoadBuilder.Length.LONG);

		roadSegments = roadBuilder.build();

		this.state.trackLength = this.roadSegments.size() * GameParameters.SEGMENT_LENGTH;

		resetCars();
		resetScenery();
		resetBombs();
		resetItems();
	}

	private void resetScenery() {
		Scenery scenery;
		float offset;
		for (int n = 0; n < roadSegments.size(); n++) {
			if (MathUtils.randomBoolean(0.1F)) {
				RoadSegment roadSegment = roadSegments.get(n);
				offset = MathUtils.randomBoolean() ? -1F - (float) (Math.random() * 1F) : 1F + (float) (Math.random() * 1F);
				scenery = new Scenery(offset, 0);
				roadSegment.addScenery(scenery);
			}
		}
	}

	public void resetCars() {
		Car car;
		RoadSegment segment;
		float offset;
		int z;
		float speed;
		for (int n = 0; n < 250; n++) {
			offset = (float) (Math.random() * MathUtils.random(-1F, 1F));
			z = (int) (Math.floor(Math.random() * roadSegments.size()) * GameParameters.SEGMENT_LENGTH);
			speed = 1000F + (float) (MathUtils.random(1000, 5000));//(float) (maxSpeed / 4 + Math.random() * maxSpeed / 2);//(sprite == SPRITES.SEMI ? 4 : 2);
			car = new Car(offset, z, speed);
			segment = findSegment(car.getZ());
			segment.addCar(car);
			cars.add(car);
		}

	}

	private void resetBombs() {
		Bomb bomb;
		RoadSegment segment;
		float offset;
		int z;
		for (int n = 0; n < 50; n++) {
			offset = (float) (Math.random() * MathUtils.random(-1F, 1F));
			z = (int) (Math.floor(Math.random() * roadSegments.size()) * GameParameters.SEGMENT_LENGTH);
			bomb = new Bomb(offset, z);
			segment = findSegment(bomb.getZ());
			segment.addBomb(bomb);
			bombs.add(bomb);
		}
	}

	private void resetItems() {
		Item item;
		RoadSegment segment;
		float offset;
		int z;
		for (int n = 0; n < 10; n++) {
			offset = (float) (Math.random() * MathUtils.random(-1F, 1F));
			z = (int) (Math.floor(Math.random() * roadSegments.size()) * GameParameters.SEGMENT_LENGTH);
			item = new Item(offset, z, Item.Types.BOOST);
			segment = findSegment(item.getZ());
			segment.addItem(item);
			items.add(item);
		}
	}

	public RoadSegment findSegment(int z) {
		return this.roadSegments.get((int) Math.floor(z / GameParameters.SEGMENT_LENGTH) % roadSegments.size());
	}

	private void updateCars(float delta, RoadSegment playerSegment, float playerW) {
		Car car;
		RoadSegment oldSegment;
		RoadSegment newSegment;
		for (int n = 0; n < cars.size(); n++) {
			car = cars.get(n);
			oldSegment = findSegment(car.getZ());
			float offset = car.getOffset() + updateCarOffset(car, oldSegment, playerSegment, playerW);
			int z = Math.round(increase(car.getZ(), delta * car.getSpeed(), state.trackLength));
			car.updateCar(z, offset);
			newSegment = findSegment(car.getZ());
			if (oldSegment != newSegment) {
				oldSegment.getCars().remove(car);
				newSegment.getCars().add(car);
			}
		}
	}


	private float updateCarOffset(Car car, RoadSegment carSegment, RoadSegment playerSegment, float playerW) {

		float dir;
		RoadSegment segment;
		Car otherCar;
		float otherCarW;
		int lookahead = 20;
		float carW = car.getWidth() * SPRITE_SCALE * 6;
		float playerX = state.player.getX();

		// optimization, dont bother steering around other cars when 'out of sight' of the player
		if ((carSegment.getIndex() - playerSegment.getIndex()) > DRAW_DISTANCE)
			return 0;

		for (int i = 1; i < lookahead; i++) {
			segment = roadSegments.get((carSegment.getIndex() + i) % roadSegments.size());

			if ((segment == playerSegment) && (car.getSpeed() > state.player.getSpeed()) && (overlap(playerX, playerW, car.getOffset(), carW, 1.2F))) {
				if (playerX > 0.5)
					dir = -1;
				else if (playerX < -0.5)
					dir = 1;
				else
					dir = (car.getOffset() > playerX) ? 1 : -1;
				return dir * 1 / i * (car.getSpeed() - state.player.getSpeed()) / GameParameters.MAX_SPEED; // the closer the cars (smaller i) and the greated the speed ratio, the larger the offset
			}

			for (int j = 0; j < segment.getCars().size(); j++) {
				otherCar = segment.getCars().get(j);
				otherCarW = otherCar.getWidth() * SPRITE_SCALE;
				if ((car.getSpeed() > otherCar.getSpeed()) && overlap(car.getOffset(), carW, otherCar.getOffset(), otherCarW, 1.2F)) {
					if (otherCar.getOffset() > 0.5)
						dir = -1;
					else if (otherCar.getOffset() < -0.5)
						dir = 1;
					else
						dir = (car.getOffset() > otherCar.getOffset()) ? 1 : -1;
					return dir * 1 / i * (car.getSpeed() - otherCar.getSpeed()) / GameParameters.MAX_SPEED;
				}
			}
		}

		// if no cars ahead, but I have somehow ended up off road, then steer back on
		if (car.getOffset() < -0.9)
			return 0.1F;
		else if (car.getOffset() > 0.9)
			return -0.1F;
		else
			return 0;
	}

	private boolean overlap(float x1, float w1, float x2, float w2) {
		return overlap(x1, w1, x2, w2, 1);
	}

	private boolean overlap(float x1, float w1, float x2, float w2, float percent) {
		float half = percent / 2;
		float min1 = x1 - (w1 * half);
		float max1 = x1 + (w1 * half);
		float min2 = x2 - (w2 * half);
		float max2 = x2 + (w2 * half);
		return !((max1 < min2) || (min1 > max2));
	}

	public void readyServer() {
		/*ServerState.getInstance().getClient().onMessage(data -> {
			System.out.println("Client received: " + new String(data));
			ServerState.getInstance().getClient().send("Client says hi!\0".getBytes());
		});*/
		serverReady = true;
	}

	public void updateServer() {
		if (!serverReady)
			readyServer();

		try {
			String data = "{ \"id\": 12, \"x\": 56, \"position\": 34, \"speed\": 78, \"life\": 90 }\0";
			ServerState.getInstance().getClient().send(data.getBytes());
		} catch (Exception e) {
			ServerState.getInstance().setMultiplayer(false);
			System.out.println("Desconectado");
		}
	}

	public void update(float delta) {

		if (ServerState.getInstance().isMultiplayer())
			updateServer();

		RoadSegment playerSegment = findSegment(Math.round(state.position + GameParameters.PLAYER_Z));
		float playerX = state.player.getX();

		float playerW = state.player.getWidth() * GameParameters.SPRITE_SCALE;

		float speedPercent = state.player.getSpeed() / GameParameters.MAX_SPEED;

		state.skyOffset  = increase(state.skyOffset,  GameParameters.SKY_SPEED  * playerSegment.getCurve() * speedPercent, 1);
		state.hillOffset = increase(state.hillOffset, GameParameters.HILL_SPEED * playerSegment.getCurve() * speedPercent, 1);
		state.treeOffset = increase(state.treeOffset, GameParameters.TREE_SPEED * playerSegment.getCurve() * speedPercent, 1);

		updateCars(delta, playerSegment, playerW);

		if (((playerX < -1) || (playerX > 1)) && (state.player.getSpeed() > GameParameters.OFF_ROAD_LIMIT)) {
			for (int n = 0; n < playerSegment.getScenery().size(); n++) {
				Scenery scenery = playerSegment.getScenery().get(n);
				float sceneryW = scenery.getWidth() * SPRITE_SCALE;
				float sceneryX = scenery.getOffset() + sceneryW / 2 * (scenery.getOffset() > 0 ? 1 : -1);
				if (overlap(playerX, playerW, sceneryX, sceneryW)) {
					state.player.setSpeed(GameParameters.MAX_SPEED / 100);
					state.position = Math.round(increase(playerSegment.getP1().world.z, -GameParameters.PLAYER_Z, state.trackLength)); // stop in front of sprite (at front of segment)
					break;
				}
			}
		}

		for (int n = 0; n < playerSegment.getCars().size(); n++) {
			Car car = playerSegment.getCars().get(n);
			float carW = car.getWidth() * SPRITE_SCALE * 7;
			if (state.player.getSpeed() > car.getSpeed()) {
				if (overlap(playerX, playerW, car.getOffset(), carW, 0.8F)) {
					state.player.setSpeed(car.getSpeed() * (car.getSpeed() / state.player.getSpeed()));
					state.position = Math.round(increase(car.getZ(), -GameParameters.PLAYER_Z, state.trackLength));
					break;
				}
			}
		}

		// Update bombs
		for (int n = 0; n < playerSegment.getBombs().size(); n++) {
			Bomb bomb = playerSegment.getBombs().get(n);
			float bombW = bomb.getWidth() * SPRITE_SCALE * 6;
			if (state.player.getSpeed() > 0 && bomb.isActive()) {
				if (overlap(playerX, playerW, bomb.getOffset(), bombW, 0.8F)) {
					ScreenManager.getInstance().assetManager.get("music/explosion.mp3", Sound.class).play(0.3f);
					state.player.setSpeed(0);
					state.position = Math.round(increase(bomb.getZ(), -GameParameters.PLAYER_Z, state.trackLength));
					bomb.setActive(false);
					bomb.texture =  ScreenManager.getInstance().assetManager.get("sprites/explosion.gif", Texture.class);
					state.lives--;
					break;
				}
			}
		}

		// Update Items
		for (int n = 0; n < playerSegment.getItems().size(); n++) {
			Item item = playerSegment.getItems().get(n);
			float itemW = item.getWidth() * SPRITE_SCALE * 6;
			if (state.player.getSpeed() > 0 && item.isActive()) {
				if (overlap(playerX, playerW, item.getOffset(), itemW, 0.8F)) {
					item.setActive(false);
					if (item.type == Item.Types.LIFE) {
						state.score += 300;
						state.lives++;
					} else if (item.type == Item.Types.BOOST) {
						state.score += 200;
						state.player.setSpeed(GameParameters.MAX_SPEED * 1.125f);
					}
					break;
				}
			}
		}
	}

	public void renderBackground(Texture texture, float rotation, float offset) {
		float imageW = texture.getWidth()/2;
		float imageH = texture.getHeight();

		int sourceX = Math.round(texture.getWidth() * rotation);
		int sourceY = texture.getHeight();
		int sourceW = Math.round(Math.min(imageW, texture.getWidth()-sourceX));
		int sourceH = Math.round(imageH);

		float destX = 0;
		float destY = Math.round(offset);
		float destW = Math.round(GameParameters.WIDTH * (sourceW/imageW));
		float destH = GameParameters.HEIGHT;


		polygonSpriteBatch.draw(texture,destX,destY, 0, 0, destW, destH, 1f, 1f, 0f, sourceX, sourceY, sourceW, sourceH, false, false);
		if (sourceW < imageW) {
			polygonSpriteBatch.draw(texture,destW - 1,destY, 0, 0, GameParameters.WIDTH - destW, destH, 1f, 1f, 0f, 0, sourceY, Math.round(imageW - sourceW), sourceH, false, false);
		}
	}


	public void render(Camera camera) {
		polygonSpriteBatch.setProjectionMatrix(camera.combined);
		polygonSpriteBatch.begin();




		RoadSegment playerSegment = findSegment(Math.round(state.position + GameParameters.PLAYER_Z));
		RoadSegment baseSegment = findSegment(state.position);

		float playerPercent = percentRemaining(state.position + GameParameters.PLAYER_Z, GameParameters.SEGMENT_LENGTH);
		float playerY = interpolate(playerSegment.getP1().world.y, playerSegment.getP2().world.y, playerPercent);

		renderBackground(BACKGROUND_SKY, state.skyOffset, GameParameters.HEIGHT / 480 * GameParameters.SKY_SPEED * playerY);
		renderBackground(BACKGROUND_HILLS, state.hillOffset, GameParameters.HEIGHT / 480 * GameParameters.HILL_SPEED * playerY);
		renderBackground(BACKGROUND_TREES, state.treeOffset, GameParameters.HEIGHT / 480 * GameParameters.TREE_SPEED * playerY);

		float maxY = 0;
		float x = 0;
		float basePercent = (state.position % GameParameters.SEGMENT_LENGTH) / (float) GameParameters.SEGMENT_LENGTH;
		float dx = -(baseSegment.getCurve()  * basePercent);


		for (int n = 0; n < Math.min(DRAW_DISTANCE, roadSegments.size()); n++) {

			RoadSegment segment = roadSegments.get((baseSegment.getIndex() + n) % roadSegments.size());
			segment.setClip(maxY);
			boolean segmentLooped = segment.getIndex() < baseSegment.getIndex();
			project(segment.getP1(), state.player.getX() * GameParameters.ROAD_WIDTH - x, playerY + GameParameters.CAMERA_HEIGHT, state.position - (segmentLooped ? state.trackLength : 0), GameParameters.CAMERA_DEPTH, GameParameters.WIDTH, GameParameters.HEIGHT, GameParameters.ROAD_WIDTH);
			project(segment.getP2(), state.player.getX() * GameParameters.ROAD_WIDTH - x - dx, playerY + GameParameters.CAMERA_HEIGHT, state.position - (segmentLooped ? state.trackLength : 0), GameParameters.CAMERA_DEPTH, GameParameters.WIDTH, GameParameters.HEIGHT, GameParameters.ROAD_WIDTH);

			x = x + dx;
			dx = dx + segment.getCurve();

			if (
					segment.getP1().camera.z <= GameParameters.CAMERA_DEPTH // Behind us
							|| (segment.getP2().screen.y <= segment.getP1().screen.y)  // back face cull
							|| (segment.getP2().screen.y < maxY)) { // clip by (already rendered) segment
				continue;
			}
			segment.draw(polygonSpriteBatch);
			maxY = segment.getP1().screen.y;
		}

		// back to front painters algorithm
		for (int n = (Math.min(DRAW_DISTANCE, roadSegments.size()) - 1); n > 0; n--) {
			RoadSegment segment = roadSegments.get((baseSegment.getIndex() + n) % roadSegments.size());

			// render scenary
			for (int i = 0; i < segment.getScenery().size(); i++) {
				Scenery scenery = segment.getScenery().get(i);
				float spriteScale = segment.getP1().screen.scale;
				float spriteX = segment.getP1().screen.x + (spriteScale * scenery.getOffset() * GameParameters.ROAD_WIDTH * GameParameters.WIDTH / 2);
				float spriteY = segment.getP1().screen.y;
				scenery.render(polygonSpriteBatch, spriteScale, spriteX, spriteY, segment.getClip());
			}


			List<Car> carDerivatives = new ArrayList<Car>();
			carDerivatives.addAll(segment.getCars());
			carDerivatives.addAll(segment.getBombs());
			carDerivatives.addAll(segment.getItems());

			// render other cars
			for (int i = 0; i < carDerivatives.size(); i++) {
				Car car = carDerivatives.get(i);
				float carPercent = percentRemaining(car.getZ(), GameParameters.SEGMENT_LENGTH);
				float spriteScale = interpolate(segment.getP1().screen.scale, segment.getP2().screen.scale, carPercent);
				float spriteX = interpolate(segment.getP1().screen.x, segment.getP2().screen.x, carPercent) + (spriteScale * car.getOffset() * GameParameters.ROAD_WIDTH * GameParameters.WIDTH / 2);
				float spriteY = interpolate(segment.getP1().screen.y, segment.getP2().screen.y, carPercent);
				car.render(polygonSpriteBatch, spriteScale, spriteX, spriteY, segment.getClip());
			}



			if (segment == playerSegment) {
				state.player.render(polygonSpriteBatch,state.player.getSpeed() / GameParameters.MAX_SPEED, GameParameters.CAMERA_DEPTH / GameParameters.PLAYER_Z, GameParameters.WIDTH / 2);
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

	private float increase(float start, float increment, int max) {
		float result = start + increment;
		while (result >= max) {
			result -= max;
		}
		while (result < 0) {
			result += max;
		}
		return result;
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
