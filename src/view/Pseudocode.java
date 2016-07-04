package view;

import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import instruction.Block;
import parser.Parser;

public class Pseudocode extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Create an instance of this JFrame on execution.
	 */
	public static void main(String[] args) { new Pseudocode(); };
	
	// Display constants
	public static final int SIZE = 600;
	
	// Editor and output panel reference.
	EditorPanel editor;
	OutputPanel output;
	
	// Parser
	Parser parser;
	Block parsed;
	
	/**
	 * Constructs this pseudocode editor.
	 */
	public Pseudocode() {
		super("Pseudocode");
		setSize(SIZE * 2, SIZE + 44);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(new PseudocodeMenuBar(this));
		
		// Creates a JPanel to contain the editor and output panel.
		JPanel container = new JPanel();
		editor = new EditorPanel(this);
		output = new OutputPanel(this);
		parser = new Parser();
		
		this.addComponentListener(new ComponentListener() {

			public void componentResized(ComponentEvent e) {
				output.setSize(getWidth() / 2, getHeight());
				if (parsed != null) {
					output.update(parsed);
				}
			}
			
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
			
		});
		
		// Add the panels to the container, and add the container to the JFrame.
		container.setLayout(new GridLayout(1, 2));
		container.add(editor);
		container.add(output);
		add(container);
		
		//setResizable(false);
		setVisible(true);
	}

	/**
	 * Called whenever the text in the editor is updated.
	 */
	public void update(String text) {
		parsed = parser.parse(text);
		output.update(parsed);
	}
	
}