package symbolic;

import java.awt.Graphics;
import java.util.ArrayList;

public class Algorithm implements Pseudocode {
	private ArrayList <Instruction> instructions;
	private int programCounter = 0;
	
	public Algorithm() {
		instructions = new ArrayList <Instruction> ();
		programCounter = 0;
	}
	
	public void add(Instruction instruction) {
		instructions.add(instruction);
	}
	
	public void execute() {
		programCounter = 0;
	}

	@Override
	public void paint(Graphics g) {
		if (programCounter < instructions.size()) {
			instructions.get(programCounter).paint(g);
			programCounter++;
		}
	}
	
	public String toString() {
		if (instructions.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("algorithm:\n");
			for (Instruction i : instructions) {
				builder.append(i.toString());
				builder.append("\n");
			}
			builder.append("end algorithm");
			return builder.toString();
		}
		else return "No algorithm";
		
	}
}
