package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 *
 * @license MIT
 */
public class EditorPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	// References to the text area and parent frame.
	private JTextPane area;
	private Pseudocode pseudocode;
	
	/**
	 * Constructs the editor.
	 * @param pseudocode
	 */
	public EditorPanel(Pseudocode pseudocode) {
		this.pseudocode = pseudocode;
		
		// Set the size and layout manager for this panel.
		setSize(pseudocode.getWidth() / 2, pseudocode.getHeight());
		setLayout(new BorderLayout());
		
		// Create a text area in a scroll pane 
		area = new JTextPane();
		area.setFont(new Font("Menlo", 0, 18));
		initializeFormatting();
		
		JPanel areaPanel = new JPanel( new BorderLayout() );
		areaPanel.add(area);
		JScrollPane pane = new JScrollPane(areaPanel);
		area.addKeyListener(this);
		add(pane);
	}
	
	public void updateText(String text) {
		area.setText(text);
	}
	
	public void format() {
		StyledDocument document = area.getStyledDocument();
		document.setCharacterAttributes(0, area.getText().length(), area.getStyle("base"), true);
		document.setCharacterAttributes(0, 3, area.getStyle("keyword"), true);
	}
	
	/**
	 * Initializes the styles used by the editor to format text.
	 * @see NXTalkFormatter
	 */
	private void initializeFormatting() {
		Style base = area.addStyle("base", null);
		StyleConstants.setFontSize(base, 18);
		StyleConstants.setFontFamily(base, "Menlo");
		StyleConstants.setForeground(base, new Color(0, 0, 0));
		
		Style keyword = area.addStyle("keyword", null);
		StyleConstants.setFontSize(keyword, 18);
		StyleConstants.setFontFamily(keyword, "Menlo");
		StyleConstants.setBold(keyword, true);
	}
//		
//		Style style;
//		for (int syntax = 0 ; syntax <= NXTalk.SyntaxTypes ; syntax++) {
//			style = editor.addStyle("" + syntax, base);
//			if (NXTalk.isSyntaxBold(syntax))
//				StyleConstants.setBold(style, true);
//			if (NXTalk.isSyntaxItalic(syntax))
//				StyleConstants.setItalic(style, true);
//			StyleConstants.setForeground(style, NXTalk.getSyntaxColor(syntax));
//		}
	
//	public synchronized void refreshFormatting() {
//		model.synchronize(editor);
//		int caret = editor.getCaretPosition();
//		NXTalkFormatterOutput output = model.formatText(editor.getCaretPosition());
//		editor.setText(output.getFormattedText());
//		editor.setCaretPosition(caret);
//		StyledDocument doc = editor.getStyledDocument();
//		while (output.hasNextFormatType())
//			doc.setCharacterAttributes(
//					output.getNextPosition(), 
//					output.getNextLength(),
//					editor.getStyle(output.getNextFormatType()), true);
//	}

	/**
	 * When a key is released, the pseudocode frame is updated.
	 */
	public void keyReleased(KeyEvent e) {
		// Auto-indentation
		if (e.getKeyCode() == 0) {
			// Get the current line and caret position
			int caret = area.getCaretPosition();
			String text = area.getText();
			String[] lines = text.substring(0, caret).split("\n");
			int line = lines.length;
			
			if (line > 0) {
				String indent = "";
				while (indent.length() < lines[line - 1].length() && 
					   lines[line - 1].charAt(indent.length()) == '\t')
					indent += "\t";
				
				if (indent.length() < lines[line - 1].length()) {
					String last = lines[line - 1].substring(indent.length());
					if (last.startsWith("if") || last.startsWith("forever") || last.startsWith("otherwise")) {
						indent += "\t";
					}
				}
				
				if (indent.length() > 0) {
					area.setText(text + indent);
					area.setCaretPosition(caret + indent.length());
				}
			}
		}
		else {
			pseudocode.update(area.getText());
		}
		format();
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
}
