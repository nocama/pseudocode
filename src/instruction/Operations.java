package instruction;

import java.awt.Graphics;

import expression.Expression;
import expression.Operator;
import expression.SymbolTerminal;
import expression.Terminal;

public class Operations extends Instruction {
	
	// The variable being incremented
	SymbolTerminal symbol;
	// The change in the variable (default is 1)
	Expression change = new Terminal(1);
	String operator;
	/**
	 * Increments the given symbol by the value of the given expression.
	 * @param symbol - the symbol to increment
	 * @param change - the expression describing how the symbol should change
	 */
	public Operations(SymbolTerminal symbol, Expression change, String operator) {
		this.symbol = symbol;
		this.change = change;
		this.operator = operator;
	}

	@Override
	public void execute(Graphics graphics, Block block) {
		// TODO Auto-generated method stub
		if (operator.equals("add")){
			block.assign(symbol.toString(), new Expression(symbol, Operator.Add, change));
		}
		else if (operator.equals("subtract")){
			block.assign(symbol.toString(), new Expression(symbol, Operator.Subtract, change));
		}
		else if (operator.equals("multiply")){
			block.assign(symbol.toString(), new Expression(symbol, Operator.Multiply, change));
		}
		else if (operator.equals("divide")){
			block.assign(symbol.toString(), new Expression(symbol, Operator.Divide, change));
		}
		
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return symbol.toString() + " += " + change.toString() + ";";
		
	}

	@Override
	public boolean equals(Instruction instruction, Block block) {
		if (instruction instanceof Operations) {
			Operations other = (Operations) instruction;
			return other.symbol.equals(this.symbol) &&
				   other.change.equals(this.change);
		}
		return false;
	}
	

}
