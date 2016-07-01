package symbolic;

import java.awt.Graphics;

public abstract class Pseudocode {
	
	public abstract void paint(Graphics g, Block algorithm);
	
	public void reset() {};
	public void terminate() {};
	
}
