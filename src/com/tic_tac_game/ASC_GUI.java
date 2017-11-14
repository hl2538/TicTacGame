package com.tic_tac_game;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;



public class ASC_GUI extends JFrame{
	
	public int type;
	private Tarea[] ta;
	private JLabel[] jls;
	private final int PROTOCOL, POSITION, TYPE, MODE;
	private boolean started;
	private boolean active;
	private Client client;
	private Integer mode;
	private JPanel board;
	private JFrame frame;
	private JTextArea textArea;

	
	
	public ASC_GUI(Integer mode){
		this.mode = mode;
		board = new JPanel();
		board.setLayout(new GridLayout(3, 3));
		board.setBorder(new TitledBorder("Chessboard"));
		
		ta = new Tarea[9];
		jls = new JLabel[9];
		for (int i = 0; i < 9; i++) {
			ta[i] = new Tarea(i, this);

			JLabel jl = new JLabel();
			jls[i] = jl;
			jl.setText("-");
			jl.setFont(new Font("",Font.ITALIC, 50));
			ta[i].add(jl);
			board.add(ta[i]);
		}
		
		add(board, BorderLayout.CENTER);
		
		setTitle("Tic-Tac-Toe");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(true);
		
		JPanel textPanel = new JPanel();
		textArea = new JTextArea("Waiting");
		textArea.setColumns(5);
		textArea.setFont(Font.getFont(Font.SANS_SERIF));
		textPanel.setBorder(new TitledBorder("Game status"));
		textPanel.add(textArea, BorderLayout.CENTER);
		add(textPanel, BorderLayout.SOUTH);
		
		client = new Client(this, mode);
		client.start();

		PROTOCOL = 0;
		POSITION = 1;
		TYPE = 2;
		MODE = 3;
		type = -1;
		started = true;
		active = true;

	}
	
	
	public void send(int position) throws IOException {
		if (started && active) {
			int[] data = { Protocol.GAME_UPDATE, position, type, mode};
			client.send(data);
		}
	}
	
	
	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		System.out.println("Please choose play mode! \n "
//				+ "0 is human vs machine;\n"
//				+ " 1 is machine vs machine");
//		Integer mode = scan.nextInt();
		
		ASC_GUI aui = new ASC_GUI(0);
		aui.frame.setVisible(true);
	}
	
	public void updateGUI(int[] data) {
		int protocol = data[0];
		int position = data[1];
		int type = data[2];
		int winner = data[3];
		
		if(protocol == Protocol.GAME_JOIN) {
			this.type = type;
		}
		else if(protocol == Protocol.GAME_UPDATE) {
				if(type == 8) {
					jls[position].setText("O");
				}
				else {
					jls[position].setText("X");
				}
				repaint();
		}
		else if(protocol == Protocol.GAME_RESULT){
			if (type == Protocol.GAME_WIN && winner == Protocol.WINNER_HUMAN) {
				textArea.setText("Win");
				JOptionPane.showMessageDialog(null,
						"You win!!\nCongratulations!!", "Tic-Tac-Toe",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (type == Protocol.GAME_TIE && winner == Protocol.WINNER_NOBODY) {
				textArea.setText("Tie");
				JOptionPane.showMessageDialog(null,
						"You and your opponent are well-matched!!\nHope you come back again soon!!", "Tic-Tac-Toe",
						JOptionPane.INFORMATION_MESSAGE);
			} else if(type == Protocol.GAME_WIN && winner == Protocol.WINNER_MACHINE) {
				textArea.setText("Lose");
				JOptionPane.showMessageDialog(null,
						"You lose!!\nBut you are still good!!", "Tic-Tac-Toe",
						JOptionPane.INFORMATION_MESSAGE);
			}
			client.setActive(false);
			this.dispose();
		}
	}
	
}
