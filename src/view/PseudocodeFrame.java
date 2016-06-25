package view;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import parser.Parser;
import symbolic.Algorithm;

public class PseudocodeFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Create an instance of this JFrame on execution.
	 */
	public static void main(String[] args) { new PseudocodeFrame(); };
	
	// Display constants
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600;
	public static final int EDITOR_WIDTH = 400;
	
	// Editor and output panel reference.
	EditorPanel editor;
	OutputPanel output;
	
	// Parser
	Parser parser;
	
	/**
	 * Constructs this pseudocode editor.
	 */
	public PseudocodeFrame() {
		super("Pseudocode");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Creates a JPanel to contain the editor and output panel.
		JPanel container = new JPanel();
		editor = new EditorPanel(this);
		output = new OutputPanel(this);
		parser = new Parser();
		
		// Add the panels to the container, and add the container to the JFrame.
		container.setLayout(new GridLayout(1, 2));
		container.add(editor);
		container.add(output);
		add(container);
		
		setVisible(true);
	}

	/**
	 * Called whenever the text in the editor is updated.
	 */
	public void update(String text) {
		output.update(parser.parse(text));
	}
	
}
