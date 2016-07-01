package symbolic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import view.PseudocodeFrame;

/**
 * Represents an algorithm written in pseudocode.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class Block extends Pseudocode {
	private Block parent;
	private ArrayList <Instruction> instructions;
	private HashMap <String, Double> symbol; 
	private int programCounter = 0;
	
	public Block() {
		this(null);
	}
	
	public Block(Block parent) {
		this.parent = parent;
		instructions = new ArrayList <Instruction> ();
		if (parent == null)
			symbol = new HashMap <String, Double> ();
		else
			symbol = parent.symbol;
		programCounter = 0;
	}
	
	public void add(Instruction instruction) {
		instructions.add(instruction);
	}
	
	public boolean hasSymbol(String symbol) {
		return this.symbol.containsKey(symbol);
	}
	
	public double getSymbol(String symbol) {
		return this.symbol.get(symbol);
	}
	
	public void assign(String symbol, Expression value) {
		this.symbol.put(symbol, value.evaluate((parent != null) ? parent : this));
		
		System.out.println(symbol + " is now " + this.symbol.get(symbol));
	}
	
	public void reset() {
		programCounter = 0;
	}
	
	public void terminate() {
		for (Instruction instruction : instructions) {
			instruction.terminate();
		}
	}
	
	public void nextInstruction() {
		programCounter++;
	}

	@Override
	public void paint(Graphics g, Block algorithm) {
		if (! isComplete()) {
			if (currentInstruction().shouldExecute()) {
				currentInstruction().paint(g, algorithm);
				if (! currentInstruction().isLoop())
					nextInstruction();
			}
			else nextInstruction();
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
	
	public boolean equals(Object algorithm) {
		if (algorithm instanceof Block) {
			Block other = (Block) algorithm;
			if (this.instructions.size() != other.instructions.size())
				return false;
			
			for (int i = 0 ; i < this.instructions.size() ; i++)
				if (! this.instructions.get(i).equals(other.instructions.get(i)))
					return false;
			
			return true;
		}
		return false;
	}

	public boolean isComplete() {
		return programCounter >= instructions.size();
	}

	public Instruction currentInstruction() {
		return instructions.get(programCounter);
	}
}
