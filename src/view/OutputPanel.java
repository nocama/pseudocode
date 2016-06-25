package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import symbolic.Algorithm;

public class OutputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Algorithm algorithm;

	public OutputPanel(PseudocodeFrame frame) {
		setSize(PseudocodeFrame.WIDTH - PseudocodeFrame.EDITOR_WIDTH, PseudocodeFrame.HEIGHT);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.WHITE);
		if (algorithm != null) {
			algorithm.paint(g);
		}
	}

	public void update(Algorithm algorithm) {
		algorithm.execute();
		System.out.println(algorithm);
		this.algorithm = algorithm;
		repaint();
	}
	
}
