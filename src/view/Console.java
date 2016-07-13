package view;

import javax.swing.JFrame;

public class Console extends JFrame {
	
	private Interpreter interpreter;
	
	public Console(Interpreter interpreter) {
		super("Console");
		this.interpreter = interpreter;
		setSize(500, 300);
	}
	
}
