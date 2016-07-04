package instruction;

import java.awt.Graphics;

public class Forever extends Instruction {
	
	// Reference to the block of instructions being repeated forever.
	private Block block;
	
	
	public Forever(Block block) {
		this.block = block;
	}
	
	public void execute(Graphics graphics, Block algorithm) {
		block.reset();
		while (! block.isComplete())
			block.execute(graphics, algorithm);
	}
	
	public boolean shouldExecute() {
		return true;
	}
	
	public boolean shouldRepeat() {
		return true;
	}
	
	/**
	 * Returns the String representation of this infinite loop.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("while(true) ");
		sb.append(block.toString());
		return sb.toString();
	}
	
	/**
	 * Returns true if this instruction object is equivalent to another in the parse tree.
	 */
	public boolean equals(Instruction instruction, Block block) {
		if (instruction instanceof IfBlock) {
			Forever other = (Forever) instruction;
			return other.block.equals(this.block);
		}
		return false;
	}
}
