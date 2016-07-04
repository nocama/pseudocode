package ast;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents an instruction in an algorithm to set the background to a given color.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class Background extends Instruction {

	Color color;
	
	public Background() {
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
		if (instruction instanceof Background) {
			Background other = (Background) instruction;
			return other.color == this.color;
		}
		return false;
	}
	
	public String toString() {
		return "background(" + color + ")";
	}
}
