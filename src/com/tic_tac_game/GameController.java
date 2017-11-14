package com.tic_tac_game;

import java.io.IOException;
import java.util.Scanner;


public class GameController{

	private Client client;
	private ASC_GUI aui;
	
	
	public GameController() throws IOException{
		aui = new ASC_GUI();
	}
	
	
	public void send(int position) throws IOException {
		aui.send(position);
	}
	
	public static void main(String[] args) throws IOException {
		GameController gc = new GameController();
		System.out.println("please input position:");
		Scanner scan = new Scanner(System.in);
		int position = scan.nextInt();
		gc.send(position);
	}
}
