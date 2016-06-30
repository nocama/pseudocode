package symbolic;

public class Symbol extends Expression {
	private String symbol;
	
	public Symbol(String symbol) {
		this.symbol = symbol;
	}
	
	public double evaluate(Algorithm algorithm) {
		if (algorithm.hasSymbol(symbol))
			return algorithm.getSymbol(symbol);
		return 0;
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
