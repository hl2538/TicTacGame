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
		socket = new ServerSocket(8081);
		clients = new LinkedList<ClientHandler>();
		active = true;
		close = false;
	}

	/**
	 * Decides Server can accept connection from client or not.
	 * 
	 * @param b
	 *            - true if can accept connection, false if cannot accept
	 *            connection.
	 */
	public void setActive(boolean b) {
		active = b;
	}

	/**
	 * Closes this server.
	 */
	public void close() {
		close = true;
	}

	/**
	 * Receives data from Client Handler and pass it to GameManager for
	 * calculation.
	 * 
	 * @param data
	 *            - the data to be calculated.
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void receive(int[] data) throws IOException, InterruptedException {
		gameManager.calculate(data);
	}

	/**
	 * Broadcasts data to ClientHandler.
	 * 
	 * @param id
	 *            - the identification number of ClientHandler. -1 for
	 *            "don't care", otherwise, send response to specific
	 *            ClientHandler.
	 * @param protocol
	 *            - the protocol corresponding to the value
	 * @param value
	 *            - The data to be broadcasted
	 * @throws IOException
	 */
	public void broadcast(int[] data) throws IOException {
		System.out.println("Server broadcasted:"+ "\tprotocol = "
				+ data[0] + "\tposition = " + data[1] + "\ttype = " + data[2]
				+ "\n");
		clients.get(0).send(data);

	}


	/**
	 * Main part of this Thread Keep receiving data from Client while this
	 * Server is active Finish and close this Server while the close boolean is
	 * true
	 */
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
	

	/**
	 * Entry of program.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
	}
	
	


}
