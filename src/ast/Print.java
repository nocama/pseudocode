package ast;

public class Print extends Instruction {
	
	Expression expression;
	
	public Print(Expression expression) {
		this.expression = expression;
	}
	
	public void execute(Block algorithm) {
		System.out.println(expression.evaluate(algorithm));
	}
	
	public String toString() {
		return "print( " + expression.toString() + " )";
	}
}
