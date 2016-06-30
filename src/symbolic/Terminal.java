package symbolic;

public class Terminal extends Expression {
	
	// The underlying value of this terminal expression
	private double value;
	
	/**
	 * Constructs a terminal object with the default value of 0.
	 */
	public Terminal() {
		value = 0;
	}
	
	/**
	 * Constructs a terminal object with the given value.
	 * @param value - the numeric value of this terminal object.
	 */
	public Terminal(double value) {
		this.value = value;
	}
	
	@Override
	public int getPrecedence() {
		return 0;
	}
	
	@Override
	public double evaluate(Algorithm algorithm) {
		return value;
	}
	
	@Override
	public String toString() {
		return "" + value;
	}
	
	@Override
	public boolean equals(Object terminal) {
		if (terminal instanceof Terminal) {
			Terminal other = (Terminal) terminal;
			return value == other.value;
		}
		return false;
	}
}
