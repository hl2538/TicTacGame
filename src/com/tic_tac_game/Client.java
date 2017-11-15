package com.tic_tac_game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.tic_tac_game.gui.ASC_GUI;


public class Client extends Thread {
	
	private boolean active;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ASC_GUI aui;
	private Integer mode;
	
	public Client() throws IOException{
		active = true;
		socket = new Socket();
	}
	
	
	public Client(ASC_GUI aui, Integer mode) {
		this.aui = aui;
		active = true;
		socket = new Socket();
		this.mode = mode;
	}
	
	
	public void setActive(boolean b) {
		active = b;
	}

	public void send(int[] data) throws IOException {
		output.writeByte(1);
		output.write(data.length);
		System.out.println(data.length);
		for (int i = 0; i < data.length; i++) {
			output.write(data[i]);
		}
		output.flush();
	}


	public void run() {
		try {
			init();
			byte connectionTest;
			while (active) {
				if ((connectionTest = input.readByte()) == -1) {
					JOptionPane.showMessageDialog(null,
							"Connection to Server lost!", "Tic-Tac-Toe",
							JOptionPane.ERROR_MESSAGE);
					active = false;
				} else {
					if (connectionTest > (byte) 0) { 
						int[] data = new int[input.read()];
						for (int i = 0; i < data.length; i++) {
							data[i] = input.read();
						}
						aui.updateGUI(data);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 707));
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
		input = new DataInputStream(socket.getInputStream());
		System.out.println("Connected to Server");
		
		int[] gameJoinRequest = { Protocol.GAME_JOIN, -1, -1, mode};
		send(gameJoinRequest);
	}


}
