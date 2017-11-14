package com.tic_tac_game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class Client extends Thread {
	
	private boolean active;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private GUI gui;
	private ASC_GUI aui;
	private GameController gc;
	private Integer mode;
	
	public Client(GUI g, Integer mode) throws IOException {
		this.mode = mode;
		active = true;
		socket = new Socket();
		gui = g;
	}
	
	public Client() throws IOException{
		active = true;
		socket = new Socket();
	}

	
	public Client(ASC_GUI aui) {
		this.aui = aui;
		active = true;
		socket = new Socket();
	}
	/**
	 * Sets is client to active or not If this client is inactive, then this
	 * thread will end.
	 * 
	 * @param b
	 *            - active or inactive
	 */
	public void setActive(boolean b) {
		active = b;
	}

	/**
	 * Send request to Server
	 * 
	 * @param data
	 *            - the request to be sent.
	 * @throws IOException
	 */
	public void send(int[] data) throws IOException {
		output.writeByte(1); // Connection test
		output.write(data.length);
		System.out.println(data.length);
		for (int i = 0; i < data.length; i++) { // Main data
			output.write(data[i]);
		}
		output.flush();
	}


	public void run() {
		try {
			init();
			byte connectionTest;
			while (active) {
				if ((connectionTest = input.readByte()) == -1) { // Connection
																	// test
					JOptionPane.showMessageDialog(null,
							"Connection to Server lost!", "Tic-Tac-Toe",
							JOptionPane.ERROR_MESSAGE);
					gui.dispose();
					active = false;
				} else {
					if (connectionTest > (byte) 0) { // Pass value to GUI for
														// updating
						System.out.println("c");
						int[] data = new int[input.read()];
						for (int i = 0; i < data.length; i++) {
							data[i] = input.read();
						}
						//gui.update(data);
						aui.update(data);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Try to connect to Server.
	 * 
	 * @throws IOException
	 */
	private void init() throws IOException {
		socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8081));
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
		input = new DataInputStream(socket.getInputStream());
		System.out.println("Connected to Server");
		
		int[] gameJoinRequest = { Protocol.GAME_JOIN, -1, -1, mode};
		send(gameJoinRequest);
	}


}
