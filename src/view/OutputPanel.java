package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import ast.Block;

/**
 * An OutputPanel is a JPanel that draws the output of an algorithm.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class OutputPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	// The underlying algorithm being run by this output view.
	private Block algorithm;
	// Flag for resetting the output view.
	private boolean reset = true;

	// BufferedImages for double buffering output
	private BufferedImage front;
	private BufferedImage back;
	private boolean frontBuffer = true;
	
	// Static values for storing the mouse and keyboard inputs
	public static boolean mouseClicked = false;
	public static int mouseX = 0, mouseY = 0;

	/**
	 * Constructs an OutputPanel object that will be displayed in the given PseudocodeFrame frame.
	 * @param frame - the frame that the output panel would be placed in
	 */
	public OutputPanel(Pseudocode frame) {
		
		// Sets the width and height of the panel
		setSize(frame.getWidth() / 2, frame.getHeight());
		
		// Create a timer that continuously repaints the window
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				repaint();
			}
		}, 0, 20);
		
		// 
		setVisible(true);
		repaint();
	}

	/**
	 * Resets the buffers and double buffering.
	 */
	public void reset() {
		// Refreshes the buffers
		front = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		back =  new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		
		// Reset the front buffer
		front.getGraphics().setColor(Color.WHITE);
		front.getGraphics().fillRect(0, 0, getWidth(), getHeight());
		
		frontBuffer = true;
	}

	public void paint(Graphics g) {
		if (reset) {
			reset = false;
			reset();
			//g.drawImage(front, 0, 0, null);
		}
		if (algorithm != null) {
			((frontBuffer) ? back : front).getGraphics()
				.drawImage(((frontBuffer) ? front : back), 0, 0, null);
			
			algorithm.paint(((frontBuffer) ? back : front).getGraphics(), algorithm);
			g.drawImage(((frontBuffer) ? back : front), 0, 0, null);
			
			frontBuffer = ! frontBuffer;
		}
	}

	public void update(Block algorithm) {
		// Cancel repaint for same algorithm
		//if (this.algorithm != null && this.algorithm.equals(algorithm))
		//	return;
		
		System.out.println("----------------------------------------------------------------------");
		algorithm.reset();
		this.algorithm = algorithm;
		System.out.println(algorithm);
		System.out.println("----------------------------------------------------------------------");
		//System.out.println(algorithm);
		reset = true;

		repaint();

	}
	
	
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}


	@Override
	public void mousePressed(MouseEvent e) {
		mouseClicked = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseClicked = false;
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
