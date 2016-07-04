package ast;

import java.awt.Graphics;

public class Branch extends Instruction {
	
	Expression expression;
	Block block;

	public Branch(Expression expression, Block block) {
		this.expression = expression;
		this.block = block;
	}
	
	public void paint(Graphics g, Block algorithm) {
		block.reset();
		while (! block.isComplete())
			block.paint(g, algorithm);
	}
	
	@Override
	public boolean shouldExecute(Block algorithm) {
		return expression.evaluate(algorithm) >= 1;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("if( ");
		sb.append(expression.toString());
		sb.append(") ");
		sb.append(block.toString());
		return sb.toString();
	}
}
