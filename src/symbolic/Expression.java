package symbolic;

public class Expression {
	private Expression left;
	private Operator operator;
	private Expression right;
	
	public Expression() { }
	
	public Expression(double number) {
		left = new Terminal(number);
	}
	
	public Expression(Expression left) {
		this.left = left;
	}
	
	public Expression(Expression left, Operator operator, Expression right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	public int getPrecedence() {
		if (operator == null) return 0;
		return getPrecedence(operator);
	}
	
	public static int getPrecedence(Operator op) {
		switch (op) {
		case And: 		
		case Or:
		case Not: 				return 1;
		case Equal:
		case NotEqual:			return 2;
		case GreaterThan:
		case GreaterThanOrEqual:
		case LessThan:
		case LessThanOrEqual:	return 3;
		case Add: 
		case Subtract:		 	return 4;
		case Multiply:
		case Divide:			return 5;
		default:				return 0;
		}
	}
	
	public static String getString(Operator op) {
		switch (op) {
		case And: 				return "&&";
		case Or:				return "||";
		case Not: 				return "!";
		case Equal:				return "==";
		case NotEqual:			return "!=";
		case GreaterThan:		return ">";
		case GreaterThanOrEqual:return ">=";
		case LessThan:			return "<";
		case LessThanOrEqual:	return "<=";
		case Add: 				return "+";
		case Subtract:		 	return "-";
		case Multiply:			return "*";
		case Divide:			return "/";
		default:				return "";
		}
	}
	
	public double evaluate(Block algorithm) {
		if (left != null) {
			if (operator != null) {
				// Check for unary operator (only "not")
				if (operator != Operator.Not && right == null)
					return 0;
				
				switch(operator) {
				// Basic math operators
				case Add:			return left.evaluate(algorithm) + right.evaluate(algorithm);
				case Subtract: 		return left.evaluate(algorithm) - right.evaluate(algorithm);
				case Multiply: 		return left.evaluate(algorithm) * right.evaluate(algorithm);
				case Divide:		return left.evaluate(algorithm) / right.evaluate(algorithm);
				
				// Equality and inequality operators
				case Equal:			return (left.evaluate(algorithm) == right.evaluate(algorithm)) ? 1 : 0;
				case NotEqual: 		return (left.evaluate(algorithm) != right.evaluate(algorithm)) ? 1 : 0;
				
				// Comparison operators
				case GreaterThan:			return (left.evaluate(algorithm) > right.evaluate(algorithm)) ? 1 : 0;
				case GreaterThanOrEqual:	return (left.evaluate(algorithm) >= right.evaluate(algorithm)) ? 1 : 0;
				case LessThan:				return (left.evaluate(algorithm) < right.evaluate(algorithm)) ? 1 : 0;
				case LessThanOrEqual:		return (left.evaluate(algorithm) <= right.evaluate(algorithm)) ? 1 : 0;
				
				// Logic operators
				case And:		return (left.evaluate(algorithm) == 1 && right.evaluate(algorithm) == 1) ? 1 : 0;
				case Or: 		return (left.evaluate(algorithm) == 1 || right.evaluate(algorithm) == 1) ? 1 : 0;
				case Not: 		return (left.evaluate(algorithm) == 0) ? 1 : 0;
				
				// Return 0 by default.
				default: return 0;
				}
			}
			// Terminal value
			else return left.evaluate(algorithm);
		}
		return 0;
	}
	
	public String toString() {
		if (left != null) {
			if (operator != null) {
				if (right != null)
					return left.toString() + " " + getString(operator) + " " + right.toString();
				else
					return getString(operator) + left.toString();
			}
			else return left.toString();
		}
		return "";
	}
	
	public boolean equals(Object expression) {
		if (expression instanceof Expression) {
			Expression other = (Expression) expression;
			if (left != null) {
				if (operator != null)
					return left.equals(other.left) && 
						   operator == other.operator && 
						  (right == null || right.equals(other.right));
				else return left.equals(other.left);
			}
			return false;
		}
		return false;
	}
}
