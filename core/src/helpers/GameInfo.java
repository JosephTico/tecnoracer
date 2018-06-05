package helpers;

public final class GameInfo {

	private GameInfo() {
		// non-public constructor
	}

	public static final int WIDTH = 1280;

	public static final int HEIGHT = 720;

	public static final int fps = 60;

	public static final float step  = 1.0f / (float) fps;

	public static final int ROAD_WIDTH = 2000;                    // actually half the roads width, easier math if the road spans from -ROAD_WIDTH to +ROAD_WIDTH
	public static final int SEGMENT_LENGTH = 200;                     // length of a single segment
	public static final int RUMBLE_LENGTH = 3;                       // number of segments per red/white rumble strip
	//public static final int trackLength   = null;                    // z length of entire track (computed)
	public static final int LANES = 3;                       // number of LANES
	public static final int FIELD_OF_VIEW   = 100;                     // angle (degrees) for field of view
	public static final int CAMERA_HEIGHT = 1000;                    // z height of camera
	public static final float CAMERA_DEPTH = (float) (1 / Math.tan((FIELD_OF_VIEW / 2) * Math.PI / 180));                    // z distance camera is from screen (computed)
	public static final int DRAW_DISTANCE = 300;                     // number of segments to draw
	public static final int playerX       = 0;                       // player x offset from center of road (-1 to 1 to stay independent of ROAD_WIDTH)
	public static final float PLAYER_Z = CAMERA_HEIGHT * GameInfo.CAMERA_DEPTH; // player relative z distance from camera (computed)
	public static final int fogDensity    = 5;                       // exponential fog density
	public static final int position      = 0;                       // current camera Z position (add playerZ to get player's absolute Z position)
	public static final int speed         = 0;                       // current speed
	public static final float MAX_SPEED = SEGMENT_LENGTH /step;      // top speed (ensure we can't move more than 1 segment in a single frame to make collision detection easier)
	public static final float ACCEL =  MAX_SPEED /5.0f;             // acceleration rate - tuned until it 'felt' right
	public static final float BREAKING = -MAX_SPEED;               // deceleration rate when braking
	public static final float DECEL = -MAX_SPEED /5.0f;             // 'natural' deceleration rate when neither accelerating, nor braking
	public static final float OFF_ROAD_DECEL = -MAX_SPEED /2.0f;             // off road deceleration is somewhere in between
	public static final float OFF_ROAD_LIMIT =  MAX_SPEED /4.0f;             // limit when off road deceleration no longer applies (e.g. you can always go at least this speed even when off road)


}