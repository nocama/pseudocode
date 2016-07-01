package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import symbolic.Block;

/**
 * An OutputPanel is a JPanel that draws the output of an algorithm.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class OutputPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// The underlying algorithm being run by this output view.
	private Block algorithm;
	// Flag for resetting the output view.
	private boolean reset = true;

	// BufferedImages for double buffering output
	private BufferedImage front;
	private BufferedImage back;
	private boolean frontBuffer = true;

	/**
	 * Constructs an OutputPanel object that will be displayed in the given PseudocodeFrame frame.
	 * @param frame - the frame that the output panel would be placed in
	 */
	public OutputPanel(PseudocodeFrame frame) {
		
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

		algorithm.reset();
		this.algorithm = algorithm;
		//System.out.println(algorithm);
		reset = true;

		repaint();

	}

}
