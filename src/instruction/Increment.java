package instruction;

import java.awt.Graphics;

import expression.Expression;
import expression.Operator;
import expression.Symbol;
import expression.Terminal;

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
	
	public String toString() {
		return symbol.toString() + " += " + change.toString() + ";";
	}

	@Override
	public void execute(Graphics graphics, Block block) {
		block.assign(symbol, new Expression(symbol, Operator.Add, change));
	}
	
	/**
	 * Returns true if this instruction object is equivalent to another in the parse tree.
	 */
	public boolean equals(Instruction instruction, Block block) {
		if (instruction instanceof Increment) {
			Increment other = (Increment) instruction;
			return other.symbol.equals(this.symbol) &&
				   other.change.equals(this.change);
		}
		return false;
	}
}
