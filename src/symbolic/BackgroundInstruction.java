package symbolic;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents an instruction in an algorithm to set the background to a given color.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class BackgroundInstruction extends Instruction {

	Color color;
	
	public BackgroundInstruction() {
		color = Color.WHITE;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public void paint(Graphics g, Block algorithm) {
		g.setColor(color);
		g.fillRect(0, 0, 1000, 1000);
	}
	
	@Override
	public boolean equals(Object instruction) {
		if (instruction instanceof BackgroundInstruction) {
			BackgroundInstruction other = (BackgroundInstruction) instruction;
			return other.color == this.color;
		}
		return false;
	}
	
	public String toString() {
		return "Set background";
	}
}
