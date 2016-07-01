package symbolic;

import java.awt.Graphics;

public class Instruction extends Pseudocode {

	@Override
	public void paint(Graphics g, Block a) {
		execute(a);
	}
	
	public void execute(Block a) {};
	public boolean shouldExecute() { return true; };
	public boolean isLoop() { return false; };
	
}
