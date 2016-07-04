package ast;

import java.awt.Graphics;

public class Increment extends Instruction {

	Symbol symbol;
	Expression change = new Terminal(1);
	
	public Increment(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public Increment(Symbol symbol, Expression change) {
		this.symbol = symbol;
		this.change = change;
	}
	
	public void execute(Block block) {
		block.assign(symbol, new Expression(symbol, Operator.Add, change));
	}
	
	public String toString() {
		return symbol.getName() + " += " + change.toString();
	}
}
