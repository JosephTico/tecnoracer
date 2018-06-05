package road;

public class Point {

	public World world = new World();
	public Camera camera = new Camera();
	public Screen screen = new Screen();

	public class World {
		public float x = 0;
		public float y = 0;
		public float z = 0;

		@Override
		public String toString() {
			return "World{" +
					"x=" + x +
					", y=" + y +
					", z=" + z +
					'}';
		}
	}

	public class Camera {
		public float x = 0;
		public float y = 0;
		public float z = 0;

		@Override
		public String toString() {
			return "Camera{" +
					"x=" + x +
					", y=" + y +
					", z=" + z +
					'}';
		}
	}

	public class Screen {
		public float x = 0;
		public float y = 0;
		public float w = 0;
		public float scale = 0;

		@Override
		public String toString() {
			return "Screen{" +
					"x=" + x +
					", y=" + y +
					", w=" + w +
					", scale=" + scale +
					'}';
		}
	}

	@Override
	public String toString() {
		return "Point{" +
				"world=" + world +
				", camera=" + camera +
				", screen=" + screen +
				'}';
	}
}