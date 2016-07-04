package ast;

public class Symbol extends Expression {
	private String symbol;
	
	public Symbol(String symbol) {
		this.symbol = symbol;
	}
	
	public double evaluate(Block block) {
		return block.get(this);
	}
	
	@Override
	public int getPrecedence() {
		return 0;
	}

	public String getName() {
		return symbol;
	}
	
	public String toString() {
		return symbol;
	}
}
