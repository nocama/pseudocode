package parser;

import lexer.Lexer;
import symbolic.Algorithm;
import symbolic.DrawInstruction;
import symbolic.Instruction;

public class Parser {
	
	private String[] tokens;
	private int index = 0;
	private Lexer lexer;
	
	public Parser() {
		lexer = new Lexer();
	}
	
	public Algorithm parse(String text) {
		tokens = lexer.lex(text);
		index = 0;
		
		return parseAlgorithm();
	}

	private Algorithm parseAlgorithm() {
		Algorithm algorithm = new Algorithm();
		
		while (hasNext()) {
			Instruction instruction = parseInstruction();
			if (instruction != null) {
				algorithm.add(instruction);
			}
			else skipNext();
		}
		
		return algorithm;
	}

	private Instruction parseInstruction() {
		if (getNext("draw", "create")) {
			return parseDrawInstruction();
		}
		return null;
	}
	
	private Instruction parseDrawInstruction() {
		// Skip the keyword "a"
		getNext("a");
		// Draw a circle
		if (peekNext("circle", "square", "oval", "rectangle")) {
			 DrawInstruction draw = new DrawInstruction(getNext());
			 
			 // Set the coordinate
			 if (getNext("at")) {
				 if (peekNumber()) {
					 draw.setX(getNumber());
				 }
				 getNext(",");
				 if (peekNumber()) {
					 draw.setY(getNumber());
				 }
			 }
			 
			 return draw;
		}
		return null;
	}

	private boolean peekNext(String ... matches) {
		for (String match : matches) {
			if (peekNext().equals(match)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean getNext(String ... matches) {
		for (String match : matches) {
			if (peekNext().equals(match)) {
				index++;
				return true;
			}
		}
		return false;
	}
	
	private String peekNext() {
		if (index < tokens.length)
			return tokens[index];
		else return "";
	}
	
	private String getNext() {
		if (index < tokens.length) {
			String t = tokens[index];
			index++;
			return t;
		}
		else return "";
	}
	
	private boolean peekNumber() {
		return peekNext().matches("\\d+(|\\.\\d*)");
	}
	
	private double getNumber() {
		String next = getNext();
		try {
			return Double.parseDouble(next);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private void skipNext() {
		index++;
	}
	
	private boolean hasNext() {
		return index < tokens.length;
	}
}
