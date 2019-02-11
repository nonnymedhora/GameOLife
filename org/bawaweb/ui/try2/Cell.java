/**
 * 
 */
package org.bawaweb.ui.try2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * @author Navroz
 *
 */
public class Cell extends JComponent {
	
	private static final int height 	= 30;
	private static final int width 		= 30;
	
	private int 		row;
	private int 		column;
	private boolean 	status;
	
	private Color 	color;

	public Cell(int aRow, int aColumn, Color aColor) {
		super();
		this.row 		= aRow;
		this.column 	= aColumn;
		this.color 		= aColor;
	}
	
	public Cell(int aRow, int aColumn, String state) {
		super();
		this.row = aRow;
		this.column = aColumn;
		if (state != null) {
			if (state.equalsIgnoreCase("alive")) {
				this.setStatus(true);
				this.color = Color.BLUE;
			} else if (state.equalsIgnoreCase("dead")) {
				this.setStatus(false);
				this.color = Color.RED;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(this.color);
		
		g2.fillRect(row, column, width, height);
	}
	

	public int getRow() {
		return this.row;
	}

	public int getColumn() {
		return this.column;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isAlive() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
		
		if(this.status) {
			this.color = Color.BLUE;
		} else {
			this.color = Color.RED;
		}
		
		repaint();
	}
	
	

}
