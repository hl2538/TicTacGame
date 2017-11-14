package com.tic_tac_game;

import java.io.IOException;
import java.util.Scanner;


public class GameController{

	private ASC_GUI aui;
	private final int PROTOCOL, POSITION, TYPE;
	private boolean started;
	private boolean active;
	private Client client;
	
	public GameController() throws IOException{
		PROTOCOL = 0;
		POSITION = 1;
		TYPE = 2;
		started = true;
		active = true;
	}
	
	
	public void send(int position) throws IOException {
		if (started && active) {
			int[] data = { Protocol.GAME_UPDATE, position, aui.type};
			client.send(data);
		}
	}
	
	public static void main(String[] args) throws IOException {

		GameController gc = new GameController();
		Client client = new Client(gc,0);
		client.start();
	}
}
