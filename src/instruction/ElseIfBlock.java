package instruction;

import java.awt.Graphics;

import expression.Expression;

public class ElseIfBlock extends Instruction {
	
	Expression expression;
	Block block;

	/**
	 * An else if block performs the given block if its expression evaluates to true and no 
	 * previous if block evaluated to true.
	 * @param expression
	 * @param block
	 */
	public ElseIfBlock(Expression expression, Block block) {
		this.expression = expression;
		this.block = block;
	}
	
	/**
	 * Assumes that the shouldExecute method returned true to the block parent of this instruction.
	 * Executes this instruction by first resetting the statement's block, then running every instruction.
	 */
	public void execute(Graphics graphics, Block rootBlock) {
		block.reset();
		while (! block.isComplete())
			block.execute(graphics, rootBlock);
	}
	
	/**
	 * Evaluates the branch instruction, and returns true if it evaluates to a positive non-zero number.
	 */
	public boolean shouldExecute(Block block) {
		return expression.evaluate(block) >= 1;
	}
	
	/**
	 * Returns the String representation of this block.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("if( ");
		sb.append(expression.toString());
		sb.append(" ) ");
		sb.append(block.toString());
		return sb.toString();
	}

	/**
	 * Returns true if this instruction object is equivalent to another in the parse tree.
	 */
	public boolean equals(Instruction instruction, Block block) {
		if (instruction instanceof ElseIfBlock) {
			ElseIfBlock other = (ElseIfBlock) instruction;
			return other.expression.equals(this.expression) &&
				   other.block.equals(this.block);
		}
		return false;
	}
}
