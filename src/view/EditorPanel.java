package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class EditorPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	// References to the text area and parent frame.
	private JTextPane area;
	private PseudocodeFrame pseudocode;
	
	/**
	 * Constructs the editor.
	 * @param pseudocode
	 */
	public EditorPanel(PseudocodeFrame pseudocode) {
		this.pseudocode = pseudocode;
		
		// Set the size and layout manager for this panel.
		setSize(pseudocode.getWidth() / 2, pseudocode.getHeight());
		setLayout(new BorderLayout());
		
		// Create a text area in a scroll pane 
		area = new JTextPane();
		area.setFont(new Font("Monaco", 0, 16));
		JScrollPane pane = new JScrollPane(area);
		area.addKeyListener(this);
		add(pane);
	}

	/**
	 * When a key is released, the pseudocode frame is updated.
	 */
	public void keyReleased(KeyEvent e) {
		pseudocode.update(area.getText());
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
}
