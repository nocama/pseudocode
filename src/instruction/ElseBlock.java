package instruction;

import java.awt.Graphics;

import expression.Expression;

public class ElseBlock extends Instruction {
	
	Block block;

	/**
	 * Constructs an else block that performs the given block if the previous IfBlock instruction
	 * or ElseIf block instruction evaluated to false.
	 * @param block
	 */
	public ElseBlock(Block block) {
		this.block = block;
	}
	
	/**
	 * Assumes that the shouldExecute method returned true to the block parent of this instruction.
	 * Executes this instruction by first resetting the statement's block, then running every instruction.
	 */
	public void execute(Graphics graphics, Block algorithm) {
		block.reset();
		while (! block.isComplete())
			block.execute(graphics, algorithm);
	}
	
	/**
	 * Evaluates the branch instruction, and returns true if it evaluates to a positive non-zero number.
	 */
	public boolean shouldExecute(Block block) {
		Instruction previous = parentBlock.previousInstruction();
		if (previous != null && (previous instanceof IfBlock)) {
			IfBlock parentIf = (IfBlock) previous;
			if (parentIf.expression.evaluate(block) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the String representation of this block.
	 */
	public String toString() {
		return "else " + block.toString();
	}

	/**
	 * Returns true if this instruction object is equivalent to another in the parse tree.
	 */
	public boolean equals(Instruction instruction, Block block) {
		if (instruction instanceof ElseBlock) {
			ElseBlock other = (ElseBlock) instruction;
			return other.block.equals(this.block);
		}
		return false;
	}
}
