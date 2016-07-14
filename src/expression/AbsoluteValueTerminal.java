package expression;

import instruction.Block;

/**This Terminal is used to find the Absolute Value of an inputted number 
 * @author Anoop Balakrishnan
 */

public class AbsoluteValueTerminal extends Terminal {
	
	private Expression num;
	
	/** This constructor is used only if the user doesn't input any number to take 
	 * the absolute value of
	 */
	public AbsoluteValueTerminal(){
		num = new Terminal(0);
	}
	/**This constructor takes in an inputed number that will then have the absolute
	 * value taken of it
	 * @param num
	 */
	public AbsoluteValueTerminal(Expression num) {
		this.num = num;
	}
	
	/** The evaluate function interprets the number that the user inputs and then 
	 * takes the absolute value of it
	 */
	public double evaluate(Block block) {
		double numValue = num.evaluate(block);
		return Math.abs(numValue);
	}
}
