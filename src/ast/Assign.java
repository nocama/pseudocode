package ast;

import java.awt.Graphics;

public class Assign extends Instruction {
	
	Symbol symbol;
	Expression expression;
	
	public Assign(Symbol symbol, Expression expression) {
		this.symbol = symbol;
		this.expression = expression;
	}
	
	public void execute(Block block) {
		block.assign(symbol, expression);
	}
	
	public String toString() {
		return symbol.toString() + " = " + expression.toString();
	}
}
