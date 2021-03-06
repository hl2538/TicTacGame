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
	
	public void send(int[] data) throws IOException {
		output.writeByte(1); 
		output.write(data.length);
		for(int i=0; i<data.length; i++) {
			output.write(data[i]);
		}
		output.flush();
	}
	
	public void setActive(boolean b) {
		active = b;
	}
	
	public void run() {
		try {
			byte connectionTest;
			while(active) {
				if((connectionTest = input.readByte()) == -1) {
					System.out.println("Client disconnected");
					active = false;
				}
				else {
					if(connectionTest > (byte) 0) {
						int[] data = new int[input.read()];
						for(int i=0; i<data.length; i++) {
							data[i] = input.read();
						}
						try {
							server.receive(data);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			socket.close();
			server.remove(this);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			server.remove(this);
		}
	}
	

}
