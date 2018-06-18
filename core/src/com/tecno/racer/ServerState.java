package com.tecno.racer;

import helpers.ScreenEnum;
import helpers.ScreenManager;
import jexxus.Client;
import org.json.*;
import road.Car;

import java.util.ArrayList;
import java.util.List;

public class ServerState {
	private static ServerState ourInstance = new ServerState();
	private boolean multiplayer;
	private Client client;
	private String host;
	private int port;
	private boolean multiplayerReady = false;
	private List<Car> cars = new ArrayList<Car>();
	private int id = 1;

	private ServerState() {

	}

	public static ServerState getInstance() {
		return ourInstance;
	}

	public static void resetInstance() {
		if (ourInstance.getClient() != null && ourInstance.getClient().isConnected())
			ourInstance.getClient().exit();
		ourInstance = new ServerState();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		setMultiplayerReady(true);
	}

	public void newClient(String ip) {
		this.client = new Client(ip);
	}

	public Client getClient() {
		return client;
	}

	public  boolean isMultiplayer() {
		return multiplayer;
	}

	public void setMultiplayer(boolean multiplayer) {
		this.multiplayer = multiplayer;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isMultiplayerReady() {
		return multiplayerReady;
	}

	public void setMultiplayerReady(boolean multiplayerReady) {
		this.multiplayerReady = multiplayerReady;
	}

	public void proccessInput(String data) {
		data = data.substring(0, data.length() - 1);
		System.out.println(data);

		try {
			JSONObject json = new JSONObject(data);
			if (json.has("id")) {
				setId(json.getInt("id"));
			}
			if (json.has("players")) {
				setPlayers(json.getJSONArray("players"));
			}
		} catch (JSONException e) {
			System.out.println("Mensaje invalido ignorado");
		}
	}

	private void setPlayers(JSONArray players) {
		cars.clear();
		for (int i=0; i < players.length(); i++) {
			JSONObject player = players.getJSONObject(i);
			int pos = player.getInt("position") + GameParameters.SEGMENT_LENGTH * 4 + 48;
			float x = player.getFloat("x");
			int id = player.getInt("id");

			if (id == ServerState.getInstance().getId() || id == 0)
				continue;

			Car tempCar = new Car(x,pos,0, id);
			System.out.println("CAR " + id + " " +tempCar);

			cars.add(tempCar);
		}
	}

	public List<Car> getCars() {
		return cars;
	}
}
