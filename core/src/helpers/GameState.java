package helpers;

import com.badlogic.gdx.audio.Music;
import player.Player;
import road.Road;

public class GameState {
	public GameState() {}

	public int position = 0;
	public int trackLength = 500;

	public Music music;

	public Player player = new Player();

	public Road road = new Road(this);
}
