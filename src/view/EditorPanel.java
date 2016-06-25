package view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EditorPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	// References to the text area and parent frame.
	private JTextArea area;
	private PseudocodeFrame pseudocode;
	
	/**
	 * Constructs the editor.
	 * @param pseudocode
	 */
	public EditorPanel(PseudocodeFrame pseudocode) {
		this.pseudocode = pseudocode;
		
		// Set the size and layout manager for this panel.
		setSize(PseudocodeFrame.EDITOR_WIDTH, PseudocodeFrame.HEIGHT);
		setLayout(new BorderLayout());
		
		// Create a text area in a scroll pane 
		area = new JTextArea();
		JScrollPane pane = new JScrollPane(area);
		area.addKeyListener(this);
		add(pane);
	}

	/**
	 * When a key is typed, the key event character is parsed and the 
	 * pseudocode frame is updated.
	 */
	public void keyTyped(KeyEvent e) {
		pseudocode.update(area.getText());
	}
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
