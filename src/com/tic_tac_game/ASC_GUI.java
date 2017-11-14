package com.tic_tac_game;

import java.io.IOException;

public class ASC_GUI extends Thread{
	
	private static final String LINE = "-";
	private static final String STRIP = "|";
	private static final int TYPE_CIRCLE = 1;
	private static final int TYPE_CROSS = 0;
	private static final String SHAPE_CIRCLE = "O";
	private static final String SHAPE_CROSS = "X";
	public int type;
	private final int PROTOCOL, POSITION, TYPE;
	private boolean started;
	private boolean active;
	private Client client;
	
	private String[] chessboard= {  LINE,LINE,LINE, 
									LINE,LINE,LINE,
									LINE,LINE,LINE};
	
	
	public ASC_GUI() throws IOException{
		client = new Client(this);
		client.start();
		this.type = -1;
		PROTOCOL = 0;
		POSITION = 1;
		TYPE = 2;
		started = true;
		active = true;
	}
	
	public void run() {
		ASC_GUI aui;
		try {
			aui = new ASC_GUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(int position) throws IOException {
		if (started && active) {
			int[] data = { Protocol.GAME_UPDATE, position, type};
			client.send(data);
		}
	}
	
	
	
	public void update(int[] data) {
		int position = data[0];
		int type = data[1];
		if(type == TYPE_CIRCLE) {
			chessboard[position] = SHAPE_CIRCLE;
		}
		else {
			chessboard[position] = SHAPE_CROSS;
		}
		show();
	}
	
	
	public void show() {
		for(int i=0 ; i<chessboard.length; i++) {
			if(i == chessboard.length-1) {
				System.out.print(chessboard[i]);
				break;
			}
			if((i+1)%3 == 0) {
				System.out.print(chessboard[i]+"\n");
				continue;
			}
			System.out.print(chessboard[i]+STRIP);
		}
	}
}
