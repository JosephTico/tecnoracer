package com.tecno.racer;

import helpers.ScreenEnum;
import helpers.ScreenManager;
import jexxus.Client;
import org.json.*;

public class ServerState {
	private boolean multiplayer;
	private static ServerState ourInstance = new ServerState();
	private Client client;
	private String host;
	private int port;
	private boolean multiplayerReady = false;

	public int getId() {
		return id;
	}

	private int id;

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
		} catch (JSONException e) {
			System.err.println("Invalid message: " + e);
		}
	}

	public void setId(int id) {
		this.id = id;
		setMultiplayerReady(true);
	}
}
