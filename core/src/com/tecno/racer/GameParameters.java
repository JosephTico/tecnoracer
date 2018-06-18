package com.tecno.racer;

public final class GameParameters {

	private GameParameters() {
		// non-public constructor
	}

	public static final int WIDTH = 1280;

	public static final int HEIGHT = 720;

	public static final int fps = 60;

	public static final float step  = 1.0f / (float) fps;

	public static final int ROAD_WIDTH = 2000;                    // actually half the roads width, easier math if the road spans from -ROAD_WIDTH to +ROAD_WIDTH
	public static final int SEGMENT_LENGTH = 200;                     // length of a single segment
	public static final int RUMBLE_LENGTH = 3;                       // number of segments per red/white rumble strip
	public static final int LANES = 3;                       // number of LANES
	public static final int FIELD_OF_VIEW   = 100;                     // angle (degrees) for field of view
	public static final int CAMERA_HEIGHT = 1000;                    // z height of camera
	public static final float CAMERA_DEPTH = (float) (1 / Math.tan((FIELD_OF_VIEW / 2) * Math.PI / 180));                    // z distance camera is from screen (computed)
	public static final int DRAW_DISTANCE = 300;                     // number of segments to draw
	public static final float PLAYER_Z = CAMERA_HEIGHT * GameParameters.CAMERA_DEPTH; // player relative z distance from camera (computed)
	public static final int fogDensity    = 5;                       // exponential fog density
	public static final float MAX_SPEED = SEGMENT_LENGTH /step;      // top speed (ensure we can't move more than 1 segment in a single frame to make collision detection easier)
	public static final float ACCEL =  MAX_SPEED /8.0f;             // acceleration rate - tuned until it 'felt' right
	public static final float BREAKING = -MAX_SPEED;               // deceleration rate when braking
	public static final float DECEL = -MAX_SPEED /5.0f;             // 'natural' deceleration rate when neither accelerating, nor braking
	public static final float OFF_ROAD_DECEL = -MAX_SPEED /2.0f;             // off road deceleration is somewhere in between
	public static final float OFF_ROAD_LIMIT =  MAX_SPEED /4.0f;             // limit when off road deceleration no longer applies (e.g. you can always go at least this speed even when off road)

	public static final float SPRITE_SCALE = 0.3F * (1 / 320F); //320 is the players car width
	public static final float ROAD_SCALE_FACTOR = 3 * WIDTH / 2 * (SPRITE_SCALE * ROAD_WIDTH);
	public static final float CENTRIFUGAL = 0.3F;

	public static final int OTHER_PLAYER_Z_FIX = SEGMENT_LENGTH * 4 + 48;

	public static final float SKY_SPEED = 0.001f; // background sky layer scroll speed when going around curve (or up hill)
	public static final float HILL_SPEED = 0.002f; // background hill layer scroll speed when going around curve (or up hill)
	public static final float TREE_SPEED = 0.003f; // background tree layer scroll speed when going around curve (or up hill)
}