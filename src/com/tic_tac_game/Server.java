package com.tic_tac_game;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;



public class Server extends Thread {
	
	private boolean active;
	private boolean close;
	private ServerSocket socket;
	private GameManager gameManager;
	private LinkedList<ClientHandler> clients;

	Server() throws IOException {
		gameManager = new GameManager(this);
		socket = new ServerSocket(707);
		clients = new LinkedList<ClientHandler>();
		active = true;
		close = false;
	}

	public void setActive(boolean b) {
		active = b;
	}

	public void close() {
		close = true;
	}

	public void receive(int[] data) throws IOException, InterruptedException {
		gameManager.calculate(data);
	}


	public void broadcast(int[] data) throws IOException {
		System.out.println("Server broadcasted:"+ "\tprotocol = "
				+ data[0] + "\tposition = " + data[1] + "\ttype = " + data[2]
				+ "\n");
		clients.get(0).send(data);

	}

	public void run() {
		try {
			while (!close) {
				if (active) {
					Socket s = socket.accept();
					ClientHandler client = new ClientHandler(this, s);
					clients.add(client);
					client.start();
					System.out.println("Accepted a Client");
				}
			}
			for (int i = 0; i < clients.size(); i++) {
				clients.get(i).setActive(false);
			}

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	
	public void remove(ClientHandler c) {
		clients.remove(c);
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
	}
	
	


}
