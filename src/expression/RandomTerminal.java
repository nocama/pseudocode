package expression;

import instruction.Block;

public class RandomTerminal extends Terminal {
	
	Expression min;
	Expression max;
	
	public RandomTerminal(Expression min, Expression max){
		this.min = min;
		this.max = max;
	}
	public RandomTerminal() {
		
	}
	public RandomTerminal(Expression max) {
		this.max = max;
	}
	public double evaluate(Block block) {
		if (min == null){
			if(max == null)
				return Math.random() * 600;
			double maxValue = max.evaluate(block);
			return (double) ((int) (Math.random() * (maxValue - 0)) + 0);
		}	
		double minValue = min.evaluate(block);
		double maxValue = max.evaluate(block);
		double r = (double) ((int) (Math.random() * (maxValue - minValue)) + minValue);
		return r;
	}
	
}
