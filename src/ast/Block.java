package ast;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a block of pseudocode instructions.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 */
public class Block {
	
	// Reference to the parent block. If this is not null, the block references the
	// symbol table of the root block, otherwise it creates a new hashmap for storing
	// the values of symbols.
	private Block parent;
	private HashMap <String, Double> symbol;
	
	// A list of instructions in the block.
	private ArrayList <Instruction> instructions;
	
	// Integers for storing the progress through the block and its indent level
	private int programCounter = 0;
	private int indentLevel = 0;
	
	/**
	 * Creates the root block.
	 */
	public Block() {
		this(null);
	}
	
	/**
	 * Creates a block within the given parent block.
	 * @param parent
	 */
	public Block(Block parent) {
		this.parent = parent;
		instructions = new ArrayList <Instruction> ();
		
		// If this is the root block
		if (parent == null)
			symbol = new HashMap <String, Double> ();
		
		// Otherwise take the symbol table from the root block
		else { 
			symbol = parent.symbol;
			indentLevel = parent.indentLevel + 1;
		}
	}
	
	/**
	 * Add an instruction to this block.
	 * @param instruction - an Instruction object
	 */
	public void add(Instruction instruction) {
		instructions.add(instruction);
	}
	
	/**
	 * Returns true if there is an assigned value to the given symbol.
	 * @param symbol - the name of the symbol
	 * @return true if the symbol exists, false otherwise
	 */
	public boolean hasSymbol(Symbol symbol) {
		return this.symbol.containsKey(symbol.getName());
	}
	
	/**
	 * Returns the value assigned to the given symbol name.
	 * @param symbol - the name of the symbol
	 * @return the value assigned to the symbol, or 0 if no value was assigned
	 */
	public double get(Symbol symbol) {
		if (hasSymbol(symbol))
			return this.symbol.get(symbol.getName());
		else return 0;
	}
	
	/**
	 * Assigns the given symbol name to the value of the given expression.
	 * @param symbol - the symbol name
	 * @param expression - the Expression object representing the assigned value
	 */
	public void assign(Symbol symbol, Expression expression) {
		this.symbol.put(symbol.getName(), expression.evaluate(this));
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
	
	public void paint(Graphics graphics, Block algorithm) {
		if (! isComplete()) {
			if (currentInstruction().shouldExecute(algorithm)) {
				currentInstruction().paint(graphics, algorithm);
				if (! currentInstruction().isLoop())
					nextInstruction();
			}
			else nextInstruction();
		}
	}
	
	public String toString() {
		if (instructions.size() > 0) {
			StringBuilder builder = new StringBuilder();
			// Append the open block syntax
			for (int i = 0 ; i < indentLevel - 1 ; i++)
				builder.append("\t");
			builder.append("{\n");
			
			// Append every instruction
			for (Instruction i : instructions) {
				for (int j = 0 ; j < indentLevel ; j++)
					builder.append("\t");
				builder.append(i.toString());
				builder.append("\n");
			}
			
			// Append the closing block syntax
			for (int i = 0 ; i < indentLevel - 1 ; i++)
				builder.append("\t");
			builder.append("}");
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
	
	public int getIndentLevel() {
		return indentLevel;
	}

	public Instruction currentInstruction() {
		return instructions.get(programCounter);
	}

	public int length() {
		return instructions.size();
	}
}
