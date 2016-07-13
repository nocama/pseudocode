package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Console extends JFrame {

	private Interpreter interpreter;
	private JTextPane area;
	
	private static Color background = new Color(20, 20, 20);
	private static Color foreground = new Color(250, 250, 250);

	public Console(Interpreter interpreter) {
		super("Console");
		this.interpreter = interpreter;
		setSize(Pseudocode.SIZE, Pseudocode.SIZE / 2);

		// Create a text area in a scroll pane 
		area = new JTextPane();
		area.setFont(new Font("Menlo", 0, 18));
		area.setBackground(background);
		area.setForeground(foreground);
		area.setEditable(false);

		JPanel areaPanel = new JPanel( new BorderLayout() );
		areaPanel.add(area);
		JScrollPane pane = new JScrollPane(areaPanel);
		add(pane);
	}

	public void reset() {
		area.setText("");
	}
}
