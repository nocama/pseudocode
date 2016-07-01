package symbolic;

import java.awt.Graphics;

public class ForeverInstruction extends Instruction {
	
	Block block;
	Thread repaintThread;
	
	public ForeverInstruction(Block block) {
		this.block = block;
	}
	
	public void paint(Graphics g, Block algorithm) {
		block.reset();
		while (! block.isComplete())
			block.paint(g, algorithm);
	}
	
	public boolean shouldExecute() {
		return true;
	}
	
	public boolean isLoop() {
		return true;
	}
	
	public void terminate() {
		if (repaintThread != null)
			repaintThread.interrupt();
	}
}
