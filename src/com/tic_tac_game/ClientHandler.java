package com.tic_tac_game;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	private DataInputStream input;
	private DataOutputStream output;
	private boolean active;
	private Socket socket;
	private Server server;
	
	
	ClientHandler(Server server, Socket s) throws IOException {
		socket = s;
		active = true;
		input = new DataInputStream(socket.getInputStream());		
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
		this.server = server;
	}
	
	
	/**
	 * Sends data to Client.
	 * @param data - data to be sent.
	 * @throws IOException
	 */
	public void send(int[] data) throws IOException {
		output.writeByte(1); //Connection test byte
		output.write(data.length);
		for(int i=0; i<data.length; i++) { //Main data
			output.write(data[i]);
		}
		output.flush();
	}
	
	/**
	 * Decides this ClientHandler is active or not.
	 * If this ClientHandler is inactive, this Thread will finish and remove itself from Server.
	 * @param b - true for active, false for inactive
	 */
	public void setActive(boolean b) {
		active = b;
	}
	
	/**
	 * Main part of this thread.
	 * If this Handler is active and the Client is not disconnected,
	 * this Handler will keep receiving and pass data to Server, else,
	 * this thread will finish and delete itself from Server.
	 */
	public void run() {
		try {
			byte connectionTest;
			while(active) {
				if((connectionTest = input.readByte()) == -1) { //Connection test
					System.out.println("Client disconnected");
					active = false;
				}
				else {
					if(connectionTest > (byte) 0) { //Read main data
						int[] data = new int[input.read()];
						for(int i=0; i<data.length; i++) {
							data[i] = input.read();
						}
						server.receive(data);
					}
				}
			}
			socket.close();
			server.remove(this);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			server.remove(this);
		}
	}
	

}