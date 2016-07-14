package expression;

import instruction.Block;

/**This Terminal is used to find the square root of an inputted number 
 * @author Anoop Balakrishnan
 */

public class SquareRootTerminal extends Terminal {
	
	Expression num;
	
	/** This constructor is used only if the user doesn't input any number to take 
	 * the square root of
	 */
	public SquareRootTerminal(){
		num = new Terminal(0);
	}
	/**This constructor takes in an inputed number that will then have the square
	 * root taken of it
	 * @param num
	 */
	public SquareRootTerminal(Expression num) {
		this.num = num;
	}
	
	/** The evaluate function interprets the number that the user inputs and then 
	 * takes the square root of it
	 * The square root of num
	 */
	public double evaluate(Block block) {
		double numValue = num.evaluate(block);
		return Math.sqrt(numValue);
	}
}
