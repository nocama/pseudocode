package symbolic;

import java.awt.Graphics;

public class ForeverInstruction extends Instruction {
	
	Algorithm block;
	Thread repaintThread;
	
	public ForeverInstruction(Algorithm block) {
		this.block = block;
	}
	
	public void paint(Graphics g, Algorithm algorithm) {
		System.out.println(block);
		block.reset();
		block.paint(g, algorithm);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {}
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
