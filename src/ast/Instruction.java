package ast;

import java.awt.Graphics;

public class Instruction {
	
	public void paint(Graphics graphics, Block block) {
		execute(block);
	}
	
	/**
	 * Executes the given instruction.
	 * @param block
	 */
	public void execute(Block block) {
		
	}
	
	public void terminate() {
		
	}
	
	public boolean shouldExecute(Block block) { 
		return true; 
	}
	
	public boolean isLoop() { 
		return false; 
	}
	
}
