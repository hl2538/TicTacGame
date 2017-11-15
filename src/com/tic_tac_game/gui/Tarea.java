package com.tic_tac_game.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JPanel;

import com.tic_tac_game.Protocol;

public class Tarea extends JPanel implements MouseListener{
	
	
	protected final int ID;
	protected int type;
	protected Color borderColor, backgroundColor;
	protected ASC_GUI aui;
	
	Tarea(int id, ASC_GUI parent) {
		this.ID = id;
		type = Protocol.TYPE_NONE;
		aui = parent;
		borderColor = new Color(120, 120, 120);
		backgroundColor = new Color(200, 200, 200);
		addMouseListener(this);
	}
	
	public void setType(int type) {
		this.type = type;
		
	}
	

	
	public void mouseReleased(MouseEvent arg0) {
		if (type == Protocol.TYPE_NONE) {
			try {
				borderColor = new Color(150, 150, 150);
				backgroundColor = new Color(210, 210, 210);
				aui.send(ID);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		repaint();
	}
	
	public void mouseExited(MouseEvent arg0) {
		if (type == Protocol.TYPE_NONE) {
			borderColor = new Color(120, 120, 120);
			backgroundColor = new Color(200, 200, 200);
		}

		repaint();
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0) {
		if (type == Protocol.TYPE_NONE) {
			borderColor = new Color(150, 150, 150);
			backgroundColor = new Color(210, 210, 210);
		}

		repaint();
	}
	
	public void mousePressed(MouseEvent arg0) {
		if (type == Protocol.TYPE_NONE) {
			borderColor = new Color(100, 100, 100);
			backgroundColor = new Color(90, 90, 90);
		}

		repaint();
	}

	
}
