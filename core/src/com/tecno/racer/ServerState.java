package com.tecno.racer;

import jexxus.Client;

public class ServerState {
	private  boolean multiplayer;
	private static ServerState ourInstance = new ServerState();
	private Client client;
	private String host;
	private int port;
	private boolean multiplayerReady;

	private ServerState() {

	}

	public static ServerState getInstance() {
		return ourInstance;
	}

	public static void resetInstance() {
		if (ourInstance.getClient().isConnected())
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
		System.out.println(data);
		setMultiplayerReady(true);
	}
}
