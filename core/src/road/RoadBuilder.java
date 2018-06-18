package road;

import com.tecno.racer.GameParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cookie on 03/05/2014.
 */
public class RoadBuilder {


	private final List<RoadSegment> roadSegments = new ArrayList<RoadSegment>();

	public enum Length {

		NONE(0),
		SHORT(25),
		MEDIUM(50),
		LONG(100);

		private int size;

		Length(int size) {
			this.size = size;
		}
	}

	public enum Curve {

		NONE(0),
		LIGHT(2),
		MEDIUM(4),
		TIGHT(6);

		private int size;

		Curve(int size) {
			this.size = size;
		}
	}

	public enum Hills {

		NONE(0),
		LOW(20),
		MEDIUM(40),
		HIGH(60);

		private int size;

		Hills(int size) {
			this.size = size;
		}
	}

	public RoadBuilder addRightCurve(Length length, Curve curve, Hills hills) {
		addRoad(length.size, length.size, length.size, curve.size, hills.size);
		return this;
	}

	public RoadBuilder addLeftCurve(Length length, Curve curve, Hills hills) {
		addRoad(length.size, length.size, length.size, -curve.size, hills.size);
		return this;
	}

	public RoadBuilder addHill(Length length, Hills hills) {
		addRoad(length.size, length.size, length.size, 0, hills.size);
		return this;
	}

	public RoadBuilder addDip(Length length, Hills hills) {
		addRoad(length.size, length.size, length.size, 0, -hills.size);
		return this;
	}

	public RoadBuilder addStraight(Length length) {
		addRoad(length.size, 0, 0, 0, 0);
		return this;
	}

	public RoadBuilder addFinishLine(Length length) {
		addRoad(length.size, 0, 0, 0, 0, true);
		return this;
	}

	public List<RoadSegment> build() {
		return Collections.unmodifiableList(roadSegments);
	}

	private void addRoad(int enter, int hold, int leave, int curve, float y, boolean finish) {
		float startY = lastY();
		float endY = startY + (y * GameParameters.SEGMENT_LENGTH);
		int n;
		float total = enter + hold + leave;
		for (n = 0; n < enter; n++) {
			addSegment(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total), true);
		}
		for (n = 0; n < hold; n++) {
			addSegment(curve, easeInOut(startY, endY, (enter + n) / total), true);
		}
		for (n = 0; n < leave; n++) {
			addSegment(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total), true);
		}
	}


	private void addRoad(int enter, int hold, int leave, int curve, float y) {
		float startY = lastY();
		float endY = startY + (y * GameParameters.SEGMENT_LENGTH);
		int n;
		float total = enter + hold + leave;
		for (n = 0; n < enter; n++) {
			addSegment(easeIn(0, curve, n / (float) enter), easeInOut(startY, endY, n / total),false);
		}
		for (n = 0; n < hold; n++) {
			addSegment(curve, easeInOut(startY, endY, (enter + n) / total), false);
		}
		for (n = 0; n < leave; n++) {
			addSegment(easeInOut(curve, 0, n / (float) leave), easeInOut(startY, endY, (enter + hold + n) / total), false);
		}
	}

	private void addSegment(float curve, float y, boolean finish) {

		int index = roadSegments.size();
		Point p1 = new Point();
		p1.world.z = index * GameParameters.SEGMENT_LENGTH;
		p1.world.y = lastY();
		Point p2 = new Point();
		p2.world.z = (index + 1) * GameParameters.SEGMENT_LENGTH;
		p2.world.y = y;
		if (finish) {
			roadSegments.add(
					Math.floor(index / GameParameters.RUMBLE_LENGTH) % 2 == 1 ?
							RoadSegment.createFinishRoadSegment(index, p1, p2, curve) :
							RoadSegment.createFinishRoadSegment(index, p1, p2, curve)
			);
		} else {
			roadSegments.add(
			Math.floor(index / GameParameters.RUMBLE_LENGTH) % 2 == 1 ?
					RoadSegment.createDarkRoadSegment(index, p1, p2, curve) :
					RoadSegment.createLightRoadSegment(index, p1, p2, curve)
			);
		}
	}

	private float lastY() {
		return (roadSegments.size() == 0) ? 0 : roadSegments.get(roadSegments.size() - 1).getP2().world.y;
	}

	private float easeIn(float a, float b, float percent) {
		return (float) (a + (b - a) * Math.pow(percent, 2));
	}

	private float easeOut(float a, float b, float percent) {
		return (float) (a + (b - a) * (1 - Math.pow(1 - percent, 2)));
	}

	private float easeInOut(float a, float b, float percent) {
		return (float) (a + (b - a) * ((-Math.cos(percent * Math.PI) / (double) 2) + 0.5d));
	}


}