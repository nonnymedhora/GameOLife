
package org.bawaweb.ui.try2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Navroz
 *
 */
public class GameOfLifePanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 6547381L;
	
	private Cell[][] theCells = new Cell[10][10];
	private int percent;
	private int frameRate;
	
	private volatile int l_Cells = 0;
	private volatile int d_Cells = 0;
	
//	private boolean hasGameStarted = false;
	
	private boolean running = false;
	
	public GameOfLifePanel(int percentFilled, int rate) {
		super();
		this.setBackground(Color.BLACK);
		this.percent= percentFilled;
		this.frameRate = rate;
		this.setLayout(new GridLayout(10,10));
		
		randomlyFillupCells(this.percent);
		this.setSize(300,300);
		this.setBorder(new BevelBorder(0));

	}

	private void randomlyFillupCells(int thePerCent) {
		int liveCells = 0;
		int deadCells = 0;

		for(int r = 0; r < 10; r++) {
			for (int c = 0; c < 10; c++) {
				if ( Math.random() * 100 <= thePerCent) {
					liveCells++;
					theCells[r][c] = new Cell(r, c, "alive");//Color.BLUE);// "alive");
				} else {
					theCells[r][c] = new Cell(r, c, "dead");//Color.RED);//"dead");
					deadCells++;
				}
				
				this.add( theCells[r][c] );
			}
		}
		this.l_Cells = liveCells;
		this.d_Cells = deadCells;
		System.out.println("There are " + liveCells + " liveCells");

		System.out.println("There are " + deadCells + " deadCells");
		
		System.out.println("Total " + (liveCells + deadCells));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	@Override
	public void run() {
		/*if(!hasGameStarted) {
			hasGameStarted = true;
			new Thread(this).start();
		}*/
		int living = 0;
		int dead = 0;
		if (running) {
			for (int r = 0; r < 10; r++) {
				for (int c = 0; c < 10; c++) {
					boolean nextGenState = getNextGenState(theCells[r][c]);

					if (nextGenState) {
						theCells[r][c].setStatus(true);
						living++;
					} else {
						theCells[r][c].setStatus(false);
						dead++;
					}
				}
			}
			try {
				Thread.sleep(frameRate);
				//            repaint();

				run();
			} catch (InterruptedException ex) {
			} 
		}

		this.l_Cells = living;
		this.d_Cells = dead;
		System.out.println("There are now " + living + " LIVE cells");
		System.out.println("There are now " + dead + " dead cells");
		
	}

	private boolean  getNextGenState(Cell cell) {
		boolean nxtGenState = false;
		boolean currentState = cell.isAlive();
		
		int rowMin = cell.getRow() - 1 < 0 ? cell.getRow() : cell.getRow() - 1;
		int rowMax = cell.getRow() + 1 >= 10 ? cell.getRow() : cell.getRow() + 1;
		
		int colMin = cell.getColumn() - 1 < 0 ? cell.getColumn() : cell.getColumn() - 1;
		int colMax = cell.getColumn() + 1 >= 10 ? cell.getColumn() : cell.getColumn() + 1;
		
		int numSurrLiveCells = 0;
		int numSurrDeadCells = 0;
		
		for (int r = rowMin; r <= rowMax; r++) {
			for(int c = colMin; c <= colMax; c++) {
				if ( this.theCells[r][c].isAlive()) {
					numSurrLiveCells++;
				} else {
					numSurrDeadCells++;
				}
			}
		}
		
		if (currentState) {
			// cell is alive
			if (numSurrLiveCells <= 3) {
				nxtGenState = true;
			} else {
				nxtGenState = false;
			}
		} else {
			// cell is dead
			if (numSurrLiveCells >= 3) {
				nxtGenState = true;
			} else {
				nxtGenState = false;
			}
		}
		
		return nxtGenState;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean run) {
		this.running = run;
	}

	public void setPercent(int percent) {
		this.percent = percent;
		this.randomlyFillupCells(this.percent);
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

}
