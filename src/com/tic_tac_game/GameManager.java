package com.tic_tac_game;


import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;



public class GameManager {
	
	
	private Server server;
	private int[] chessboard;
	int[] responseData;
	private final int PROTOCOL, POSITION, TYPE, MODE;
	private boolean gameStarted;
	
	
	public GameManager(Server server) {
		PROTOCOL = 0;
		POSITION = 1;
		TYPE = 2;
		MODE = 3;
		this.server = server;
		chessboard = new int[9];
		for (int i = 0; i < 9; i++) {
			chessboard[i] = Protocol.TYPE_NONE;
		}
		gameStarted = false;
		responseData = new int[3];
	}

	
	
	/**
	 * human versus machine controller
	 * @param data
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void calculate(int[] data) throws IOException, InterruptedException {
		if(data[MODE] == 0) {
			switch (data[PROTOCOL]) {
			case Protocol.GAME_JOIN:
					responseData[PROTOCOL] = Protocol.GAME_STARTED;
					responseData[POSITION] = -1;
					responseData[TYPE] = -1;
					gameStarted = true;
					server.broadcast(responseData);
				break;
			case Protocol.GAME_UPDATE:
				if (chessboard[data[POSITION]] == Protocol.TYPE_NONE) {
					chessboard[data[POSITION]] = data[TYPE];
					responseData[PROTOCOL] = data[PROTOCOL];
					responseData[POSITION] = data[POSITION];
					responseData[TYPE] = 9;
					server.broadcast(responseData);
				}
				if(isFull()) {
					int result = checkGameResult();
					responseData[PROTOCOL] = Protocol.GAME_RESULT;
					responseData[POSITION] = -1;
					
					if (result == 1) { // Check whether there's a winner or not
						responseData[TYPE] = Protocol.GAME_WIN;
						server.broadcast(responseData);

						responseData[TYPE] = Protocol.GAME_LOSE;
						
						server.broadcast(responseData);
					}
					else if (result == 2 && data[PROTOCOL] != Protocol.GAME_JOIN) {
						
						System.out.println("TIE");
						responseData[TYPE] = Protocol.GAME_TIE;
						
						server.broadcast(responseData);
						
					}
					break;
				}
				while(true) {
					Integer machinePosition = pickPosition(9);
					if(chessboard[machinePosition] == Protocol.TYPE_NONE) {
						
						chessboard[machinePosition] = 8;
						
						responseData[PROTOCOL] = Protocol.GAME_UPDATE;
						responseData[POSITION] = machinePosition;
						responseData[TYPE] = 8;
						server.broadcast(responseData);
						int result = checkGameResult();
						responseData[PROTOCOL] = Protocol.GAME_RESULT;
						responseData[POSITION] = -1;
						
						if (result == 1) { // Check whether there's a winner or not
							responseData[TYPE] = Protocol.GAME_WIN;
							server.broadcast(responseData);

							responseData[TYPE] = Protocol.GAME_LOSE;
							
							server.broadcast(responseData);
						}
						else if (result == 2 && data[PROTOCOL] != Protocol.GAME_JOIN) {
							
							System.out.println("TIE");
							responseData[TYPE] = Protocol.GAME_TIE;
							
							server.broadcast(responseData);
							
						}
						break;
					}
				}
				break;
			default:
				for (int i = 0; i < responseData.length; i++) {
					responseData[i] = -1;
				}
				System.out.println("Default");
				break;
			}

			

		}
		else {
			switch (data[PROTOCOL]) {
			case Protocol.GAME_JOIN:
					responseData[PROTOCOL] = Protocol.GAME_STARTED;
					responseData[POSITION] = -1;
					responseData[TYPE] = -1;
					gameStarted = true;
					server.broadcast(responseData);
					
					boolean turn = true;
					int shape = -1;
					LinkedList<Integer> positionList = new LinkedList<>();
					Integer firstPosition = pickPosition(9);
					positionList.add(firstPosition);
					int index = 0;
					while(!isFull()) {
						shape = turn ==true? 9:8;
						if(isEmpty()) {
							chessboard[firstPosition] = shape;
							responseData[PROTOCOL] = Protocol.GAME_UPDATE;
							responseData[POSITION] = firstPosition;
							responseData[TYPE] = shape;
							turn = !turn;
							positionList.add(firstPosition);
							index++;
						}
						else {
							Integer position = machineStrategy(positionList.get(index));
							if(positionList.contains(position))continue;
							chessboard[position] = shape;
							responseData[PROTOCOL] = Protocol.GAME_UPDATE;
							responseData[POSITION] = position;
							responseData[TYPE] = shape;
							turn = !turn;
							positionList.add(position);
							index++;
						}
						
						server.broadcast(responseData);
						Thread.sleep(1000);
						
						int result = checkGameResult();
						responseData[PROTOCOL] = Protocol.GAME_RESULT;
						responseData[POSITION] = -1;
						
						if (result == 1) { // Check whether there's a winner or not
							responseData[TYPE] = Protocol.GAME_WIN;
							server.broadcast(responseData);

							responseData[TYPE] = Protocol.GAME_LOSE;
							
							server.broadcast(responseData);
						}
						else if (result == 2 && data[PROTOCOL] != Protocol.GAME_JOIN) {
							
							System.out.println("TIE");
							responseData[TYPE] = Protocol.GAME_TIE;
							
							server.broadcast(responseData);
							
						}
					}
				break;
			default:
				for (int i = 0; i < responseData.length; i++) {
					responseData[i] = -1;
				}
				System.out.println("Default");
				break;
			}
		}
	}
	
	


	/**
	 * Checks whether there's anyone has win this game or not.
	 * 
	 * @throws IOException
	 */
	private int checkGameResult() throws IOException {
		boolean tie = true;
		
		if (chessboard[0] != Protocol.TYPE_NONE
				&& chessboard[0] == chessboard[4]
				&& chessboard[4] == chessboard[8]) {
			return 1;
		} else if (chessboard[2] != Protocol.TYPE_NONE
				&& chessboard[2] == chessboard[4]
				&& chessboard[4] == chessboard[6]) {
			return 1;
		} else {
			for (int i = 0; i < 2; i++) {
				if (chessboard[i * 3] != Protocol.TYPE_NONE
						&& chessboard[i * 3] == chessboard[i * 3 + 1]
						&& chessboard[i * 3 + 1] == chessboard[i * 3 + 2]) {
					return 1;
				} else {
					if (chessboard[i] != Protocol.TYPE_NONE
							&& chessboard[i] == chessboard[i + 3]
							&& chessboard[i + 3] == chessboard[i + 6]) {
						return 1;
					}
				}
			}
		}
		
		for (int i = 0; i < 9; i ++) {
			if (chessboard[i] == Protocol.TYPE_NONE) {
				tie = false;
			}
		}
		
		if (tie) {
			return 2;
		} else {
			return 0;
		}
	}

	public Integer pickPosition(Integer range) {
			Random random = new Random();
			Integer position = random.nextInt(range);
			return position;
	}
	
	public Integer machineStrategy(Integer position) {
		if(position ==-1) {
			return pickPosition(9);
		}
		Random random = new Random();
		if(belong(position) == 1) { //row1
			Integer p ;
			while(true) {
				p = random.nextInt(3);
				if(p!=position)break;
			}
			return p;
		}
		else if(belong(position)==2) { //row2
			Integer p ;
			while(true) {
				p = random.nextInt(3)+3;
				if(p!=position)break;
			}
			return p;
		}
		else {//row3
			Integer p ;
			while(true) {
				p = random.nextInt(3)+6;
				if(p!=position)break;
			}
			return p;
		}
	}
	
	public Integer belong(Integer position) {
		if(position>=0 && position<=2)return 1;
		else if(position>=3 && position<=5)return 2;
		else return 3;
	}
	
	public boolean isFull() {
		for(int i =0; i<chessboard.length; i++) {
			if(chessboard[i] == Protocol.TYPE_NONE) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isEmpty() {
		for(int i =0; i<chessboard.length; i++) {
			if(chessboard[i] != Protocol.TYPE_NONE) {
				return false;
			}
		}
		return true;
	}
}
