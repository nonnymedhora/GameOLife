/**
 * 
 */
package org.bawaweb.ui.try2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class GameOfLifeFrame extends JFrame {

	private static final long serialVersionUID = 5799223161673015113L;
	
	private GameOfLifePanel golPanel;
	private final int 		percent 			= 45;
//	private final int 		rate 				= 200;
	private int 			movesPerSec 		= 2;
	
	final JButton startPauseBtn 	= new JButton("Start >>");
	final JButton autofillBtn 		= new JButton("AutoFill");
	final JButton frameRtBtn		= new JButton("Rate");
	final JButton stepPauseBtn 		= new JButton("Step ||>");
	final JButton resetBtn 			= new JButton("Reset");
	final JButton exitBtn 			= new JButton("Exit");
	
	
	
	private Thread theGame;

	private JPanel butPanel;
	
	public GameOfLifeFrame() {
		super();
		initComponents();
	}

	private void initComponents() {
		Container cp;
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

	    cp.add(new JLabel("BaWaZ GameOfLife: "), BorderLayout.NORTH);
	    
	    this.golPanel = resetGame();

	    cp.add(this.golPanel, BorderLayout.CENTER);
	    
	    this.startPauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStartPauseCommand();				
			}	    	
	    });
	    
	    this.autofillBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAutoFillCommand();				
			}	    	
	    });
	    
	    this.frameRtBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doFrameRateCommand();				
			}	    	
	    });
	    
	    this.stepPauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStepPauseCommand();
			}	    	
	    });
	    
	    this.resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doResetCommand();
			}	    	
	    });
	    
	    this.exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doExitCommand();
			}	    	
	    });
	    
	    
	    this.butPanel = new JPanel();
	    this.butPanel.setLayout(new BoxLayout(this.butPanel,BoxLayout.X_AXIS)); 
	    this.butPanel.add(startPauseBtn);
	    this.butPanel.add(autofillBtn);
	    this.butPanel.add(frameRtBtn);
	    this.butPanel.add(stepPauseBtn);
	    this.butPanel.add(resetBtn);
	    this.butPanel.add(exitBtn);
		cp.add(this.butPanel, BorderLayout.SOUTH);
	    
	    this.pack();
	    repaint();
	   	    
		this.setVisible(true);
		
		
	}

	protected void doResetCommand() {
//		this.golPanel = resetGame();
//		repaint();
		
	}

	private GameOfLifePanel resetGame() {
		return new GameOfLifePanel(this.percent, 1000/this.movesPerSec);
	}

	protected void doExitCommand() {
		this.dispose();
		System.exit(0);
		
	}

	protected void doStepPauseCommand() {
		doStartPauseCommand();
		this.theGame.interrupt();
		
	}

	protected void doFrameRateCommand() {
		// Put up an options panel to change the number of moves per second
        final JFrame f_options = new JFrame();
        f_options.setTitle("Options");
        f_options.setSize(300,60);
        f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth())/2, 
            (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight())/2);
        f_options.setResizable(false);
        JPanel p_options = new JPanel();
        p_options.setOpaque(false);
        f_options.add(p_options);
        p_options.add(new JLabel("Number of moves per second:"));
        Integer[] secondOptions = {1,2,3,4,5,10,15,20};
        final JComboBox cb_seconds = new JComboBox(secondOptions);
        p_options.add(cb_seconds);
        cb_seconds.setSelectedItem(movesPerSec);
        cb_seconds.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                movesPerSec = (Integer)cb_seconds.getSelectedItem();
                golPanel.setFrameRate(1000/movesPerSec);
                repaint();
                f_options.dispose();
            }
        });
        f_options.setVisible(true); 
		
	}

	protected void doAutoFillCommand() {
		final JFrame f_autoFill = new JFrame();
        f_autoFill.setTitle("Autofill");
        f_autoFill.setSize(360, 60);
        f_autoFill.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_autoFill.getWidth())/2, 
            (Toolkit.getDefaultToolkit().getScreenSize().height - f_autoFill.getHeight())/2);
        f_autoFill.setResizable(false);
        JPanel p_autoFill = new JPanel();
        p_autoFill.setOpaque(false);
        f_autoFill.add(p_autoFill);
        p_autoFill.add(new JLabel("What percentage should be filled? "));
        Object[] percentageOptions = {"Select",5,10,15,20,25,30,40,50,60,70,80,90,95};
        final JComboBox cb_percent = new JComboBox(percentageOptions);
        p_autoFill.add(cb_percent);
        cb_percent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cb_percent.getSelectedIndex() > 0) {  
//                	golPanel.setPercent((Integer)cb_percent.getSelectedItem());
//                	repaint();
                    f_autoFill.dispose();
                }
            }
        });
        f_autoFill.setVisible(true);
		
	}

	protected void doStartPauseCommand() {
		boolean running = this.golPanel.isRunning();
		
		 if (!running) {
			this.golPanel.setRunning(true);
			this.theGame = new Thread(this.golPanel);
			this.theGame.start();
			this.startPauseBtn.setText("Pause ||");
		} else {
			this.golPanel.setRunning(false);
			this.theGame.interrupt();
			this.startPauseBtn.setText("Start |>");
		}
	}
	
	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		        final GameOfLifeFrame frame = new GameOfLifeFrame();
		        frame.setTitle("Bawaz _ GameO'Life");
		        frame.setSize(500, 500);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setResizable(false);
		        frame.setVisible(true);
		      }
		    });

	}

}
