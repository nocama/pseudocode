package symbolic;

import java.awt.Graphics;

public class Instruction extends Pseudocode {

	@Override
	public void paint(Graphics g, Algorithm a) {
		execute(a);
	}
	
	public void execute(Algorithm a) {};
	public boolean shouldExecute() { return true; };
	public boolean isLoop() { return false; };
	
}
