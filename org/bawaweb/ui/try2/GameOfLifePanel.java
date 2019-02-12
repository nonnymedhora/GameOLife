
package org.bawaweb.ui.try2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Navroz
 * 
 * 1 -- Any live cell with fewer than two live neighbors dies, as if by underpopulation.
 * 2 -- Any live cell with two or three live neighbors lives on to the next generation.
 * 3 -- Any live cell with more than three live neighbors dies, as if by overpopulation.
 * 4 -- Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 *
 */
public class GameOfLifePanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 6547381L;
	private final int DIM = 10;
	
	private Cell[][] theCells = new Cell[DIM][DIM];
	private List<Cell[][]> genHistory = new ArrayList<Cell[][]>();;
	private int percent;
	private int frameRate;
	
	private volatile int l_Cells = 0;
	private volatile int d_Cells = 0;
	
	private int currentGeneration = 0;
	
//	private boolean hasGameStarted = false;
	
	private boolean running = false;
	
	public GameOfLifePanel(int percentFilled, int rate) {
		super();
		this.setBackground(Color.BLACK);
		this.percent= percentFilled;
		this.frameRate = rate;
		this.setLayout(new GridLayout(DIM,DIM));
		
		randomlyFillupCells(this.percent);
		this.setSize(300,300);
		this.setBorder(new BevelBorder(0));

	}

	private void randomlyFillupCells(int thePerCent) {
		int liveCells = 0;
		int deadCells = 0;

		for(int r = 0; r < DIM; r++) {
			for (int c = 0; c < DIM; c++) {
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
		genHistory.add(theCells);
		currentGeneration = genHistory.size();
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
			if(currentGeneration == genHistory.size()) {
				boolean[][] nxtGenStatusArr = new boolean[DIM][DIM];
				Cell[][] nxtGenCells = new Cell[DIM][DIM];
				for (int r = 0; r < DIM; r++) {
					for (int c = 0; c < DIM; c++) {
						boolean nextGenState = getNextGenState(theCells[r][c]);
						nxtGenStatusArr[r][c] = nextGenState;
						if(nextGenState) {
							nxtGenCells[r][c] = new Cell(r, c, "alive");
							living++;
						} else {
							nxtGenCells[r][c] = new Cell(r, c, "dead");
							dead++;
						}
	
						/*if (nextGenState) {
							theCells[r][c].setStatus(true);
							living++;
						} else {
							theCells[r][c].setStatus(false);
							dead++;
						}*/
					}
				}
				
				//swap the generations
				for(int i = 0; i < DIM; i++) {
					for(int j = 0; j < DIM; j++) {
						theCells[i][j].setStatus(nxtGenStatusArr[i][j]);
					}
				}
				
				// add 2 History
				//@TODO
				genHistory.add(nxtGenCells);
				currentGeneration = genHistory.size();
				System.out.println("Processed generations == " + currentGeneration);
				this.l_Cells = living;
				this.d_Cells = dead;
				System.out.println("There are now " + living + " LIVE cells and " + dead + " dead cells");
			
			} else {
				int nxtGen = currentGeneration+1;
				Cell[][] cells2Display = nxtGen < genHistory.size() ? genHistory.get(nxtGen) : null;
				if (cells2Display != null) {
					//swap the generations
					for (int i = 0; i < DIM; i++) {
						for (int j = 0; j < DIM; j++) {
							theCells[i][j].setStatus(cells2Display[i][j].isAlive());
						}
					}
					System.out.println("Displaying generation " + nxtGen);
					currentGeneration = nxtGen;
					repaint();
				}
			}//
				
				try {
					Thread.sleep(frameRate);
	//				repaint();
					run();
				} catch (InterruptedException ex) {
				} 
		}

	}

	private boolean  getNextGenState(Cell cell) {
		boolean nxtGenState = false;
		boolean currentState = cell.isAlive();
		
		final int cellRow = cell.getRow();
		int rowMin = cellRow - 1 < 0 ? cellRow : cellRow - 1;
		int rowMax = cellRow + 1 >= DIM ? cellRow : cellRow + 1;
		
		final int cellCol = cell.getColumn();
		int colMin = cellCol - 1 < 0 ? cellCol : cellCol - 1;
		int colMax = cellCol + 1 >= DIM ? cellCol : cellCol + 1;
		
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
			numSurrLiveCells--;					// discard reference of the existing live cell
			if (numSurrLiveCells <= 3) {
				if(numSurrLiveCells < 2) {
					nxtGenState = false;		// 1 -- Any live cell with fewer than two live neighbors dies, as if by underpopulation.
				}
				nxtGenState = true;				// 2 -- Any live cell with two or three live neighbors lives on to the next generation.
			} else {
				nxtGenState = false;			// 3 -- Any live cell with more than three live neighbors dies, as if by overpopulation.
			}
		} else {
			// cell is dead
			if (numSurrLiveCells == 3) {
				nxtGenState = true;				// 4 -- Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
			} else {
				nxtGenState = false;
			}
		}
		
		return nxtGenState;
	}
	


	public void goBack() {
		int prvGen = currentGeneration - 1; 
		Cell[][] lastGenCells = prvGen < 0 ? null : genHistory.get(prvGen);
		int living = 0;
		int dead = 0;
		if (lastGenCells != null) {
			//swap the generations
			for (int i = 0; i < DIM; i++) {
				for (int j = 0; j < DIM; j++) {
					boolean status = lastGenCells[i][j].isAlive();
					if ( status ) 
						living++;
					else
						dead++;
					theCells[i][j].setStatus(status);
				}
			}
			System.out.println("Displaying generation " + prvGen);
			System.out.println("There are " + living + " LIVING cells and " + dead + " dead cells");
			currentGeneration = prvGen;
			repaint();
		}
		
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
