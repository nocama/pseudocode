package symbolic;

import java.awt.Graphics;

public class IncrementInstruction extends Instruction {

	Symbol symbol;
	Expression change = new Terminal(1);
	
	public IncrementInstruction(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public IncrementInstruction(Symbol symbol, Expression change) {
		this.symbol = symbol;
		this.change = change;
	}
	
	public void execute(Algorithm algorithm) {
		algorithm.putSymbol(symbol.getName(), new Terminal(algorithm.getSymbol(symbol.getName()) + change.evaluate(algorithm)));
	}
	
	public String toString() {
		return symbol.getName() + " += " + change.toString();
	}
}
