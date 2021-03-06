package com.tic_tac_game;

public final class Protocol {
	private Protocol() {
		
	}
	
	public static final int GAME_JOIN = 1;
	public static final int GAME_STARTED = 2;
	public static final int GAME_RESULT = 3;
	public static final int GAME_WIN = 4;
	public static final int GAME_LOSE = 5;
	public static final int GAME_TIE = 14;
	
	public static final int TYPE = 6;
	public static final int TYPE_NONE = 7;
	public static final int TYPE_CIRCLE = 8;
	public static final int TYPE_CROSS = 9;
	
	public static final int POSITION = 10;
	
	public static final int GAME_UPDATE = 11;
	public static final int GAME_TOKEN = 13;
	
	public static final int WINNER_HUMAN = 100;
	public static final int WINNER_MACHINE = -100;
	public static final int WINNER_NOBODY = 50;
}
