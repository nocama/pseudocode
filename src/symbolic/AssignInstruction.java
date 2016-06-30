package symbolic;

import java.awt.Graphics;

public class AssignInstruction extends Instruction {
	
	Symbol symbol;
	Expression expression;
	
	public AssignInstruction(Symbol symbol, Expression expression) {
		this.symbol = symbol;
		this.expression = expression;
	}

	@Override
	public void paint(Graphics g, Algorithm algorithm) {
		algorithm.putSymbol(symbol.getName(), expression);
	}
	
}
