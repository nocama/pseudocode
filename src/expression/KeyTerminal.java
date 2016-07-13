package expression;

import instruction.Block;

public class KeyTerminal extends Terminal {
	
	private String key;
	private boolean pressed;
	
	public KeyTerminal(String key) {
		this.key = key;
		pressed = true;
	}
	
	public KeyTerminal(String key, boolean pressed) {
		this.key = key;
		this.pressed = pressed;
	}
	
	/**
	 * Evaluates in the context of a given block. Since this is a numeric value, it always
	 * evaluates to its double value.
	 */
	public double evaluate(Block block) {
		if (pressed)
			return block.get(key + " pressed");
		else
			return (block.hasSymbol(key + " pressed") && 
					block.get(key + " pressed") == 0) ? 1 : 0;
	}
	
}
