package instruction;

import java.awt.Graphics;

import expression.Expression;

public class Print extends Instruction {
	
	Expression expression;
	
	public Print(Expression expression) {
		this.expression = expression;
	}
	
	public void execute(Graphics graphics, Block algorithm) {
		System.out.println(expression.evaluate(algorithm));
	}
	
	public String toString() {
		return "System.out.println( " + expression.toString() + " );";
	}

	@Override
	public boolean equals(Instruction instruction, Block block) {
		if (instruction instanceof Print) {
			Print other = (Print) instruction;
			return other.expression.equals(this.expression);
		}
		return false;
	}
}
