package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import symbolic.Algorithm;

public class OutputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Algorithm algorithm;
	private boolean reset = true;
	
	private BufferedImage output;
	
	public OutputPanel(PseudocodeFrame frame) {
		setSize(frame.getWidth() / 2, frame.getHeight());
		output = new BufferedImage(frame.getWidth() / 2, frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				repaint();
			}
			
		}, 0, 20);
		setVisible(true);
		repaint();
	}
	
	public void paint(Graphics g) {
		if (reset) {
			output.getGraphics().setColor(Color.BLACK);
			output.getGraphics().fillRect(0, 0, getWidth(), getHeight());
			reset = false;
			g.drawImage(output, 0, 0, null);
		}
		if (algorithm != null) {
			algorithm.paint(output.getGraphics(), algorithm);
			g.drawImage(output, 0, 0, null);
		}
	}

	public void update(Algorithm algorithm) {
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
