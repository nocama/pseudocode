package parser;

import java.awt.Color;
import java.util.HashSet;

import lexer.Lexer;
import symbolic.Algorithm;
import symbolic.AssignInstruction;
import symbolic.BackgroundInstruction;
import symbolic.DrawInstruction;
import symbolic.Expression;
import symbolic.ForeverInstruction;
import symbolic.IncrementInstruction;
import symbolic.Instruction;
import symbolic.Operator;
import symbolic.Symbol;
import symbolic.Terminal;

/**
 * The Parser generates an Algorithm object from a given pseudocode String.
 * 
 * @author  Keshav Saharia
 *			keshav@techlabeducation.com
 */
public class Parser {
		
	private Lexer lexer;				// the Lexer object for lexing the input text
	private String[] tokens;			// an array of string tokens that this parser has lexed
	private int index = 0;				// the current index of the parser in the token stream
	private HashSet <String> symbols;	// The HashSet containing all symbol names
	
	private String[] drawType = { "circle", "square", "rectangle", "oval", "line" };
	
	/**
	 * Constructs the Parser object.
	 */
	public Parser() {
		lexer = new Lexer();
		symbols = new HashSet <String> ();
	}
	
	/**
	 * Parses the text and returns an algorithm object representing a runnable pseudocode program.
	 * @param text
	 * @return
	 */
	public Algorithm parse(String text) {
		// Lex the input text and reset the parser
		tokens = lexer.lex(text);
		index = 0;
		symbols.clear();
		
		// Start the high-level parsing routine
		return parseAlgorithm();
	}

	/**
	 * Highest level of the recursive descent parser. Parses an algorithm and returns an
	 * Algorithm object representing the parsed algorithm.
	 * 
	 * @return an Algorithm object representing the pseudocode algorithm
	 */
	private Algorithm parseAlgorithm() {
		// Create an algorithm and store it to the object reference
		Algorithm algorithm = new Algorithm();
		
		// While there are tokens left in the token stream, parse an instruction from the token stream
		while (hasNext()) {
			Instruction instruction = parseInstruction();
			
			// If an instruction was successfully parsed
			if (instruction != null) {
				algorithm.add(instruction);
			}
			// Otherwise skip the current token so the stream is gradually consumed
			else skipNext();
		}
		
		// Return the resulting algorithm object
		return algorithm;
	}
	
	/**
	 * 
	 * @return
	 */
	private Instruction parseInstruction() {
		
		// Repeat instructions forever
		if (getNext("forever")) {
			return new ForeverInstruction(parseAlgorithm());
		}
		
		// Set the background instruction
		if (getNext("set background")) {
			return parseBackgroundInstruction();
		}
		
		// Draw a shape instruction
		if (getNext("draw", "create", "place")) {
			return parseDrawInstruction();
		}
		
		// Put keyword can be used for drawing or for assigning to a symbol
		if (getNext("put")) {
			skipNext("a", "an");
			
			// Put in the context of a drawing instruction
			if (peekNext(drawType))
				return parseDrawInstruction();
			
			// Checks if there is an expression
			else if (peekExpression()) {
				Expression expr = parseExpression();
				
				// If there is a symbol to assign it into
				if (getNext("in", "into") && peekSymbol()) {
					return new AssignInstruction(parseSymbol(), expr);
				}
			}
		}
		
		// The set keyword 
		if (getNext("set")) {
			if (peekSymbol()) {
				Symbol symbol = parseSymbol();
				if (getNext("to", "as") && peekExpression()) {
					return new AssignInstruction(symbol, parseExpression());
				}
			}
		}
		
		// Increment, decrement, change
		if (peekNext("increment", "decrement", "change")) {
			// The direction in which the change will happen (increment and change are both positive)
			int direction = (getNext().equals("decrement")) ? -1 : 1;
			
			if (peekSymbol()) {
				Symbol symbol = parseSymbol();
				
				// Give an explicit amount to increment by
				if (getNext("by") && peekExpression())
					return new IncrementInstruction(symbol, parseExpression());
				else 
					return new IncrementInstruction(symbol, new Terminal(direction));
			}
		}
		
		return null;
	}
	
	private Instruction parseBackgroundInstruction() {
		getNext("to");
		Color color = parseColor();
		
		BackgroundInstruction background = new BackgroundInstruction();
		if (color != null)
			background.setColor(color);
		
		return background;
	}

	private Instruction parseDrawInstruction() {
		// Skip the keyword "a" and "an"
		skipNext("a", "an");
		Color color = parseColor();
		
		while (! atDelimiter() && ! peekNext(drawType))
			skipNext();
		
		// Any of the standard shapes should produce a draw instruction
		if (peekNext(drawType)) {
			 DrawInstruction draw = new DrawInstruction(getNext());
			 
			 if (color != null)
				 draw.setColor(color);
			 
			 if (draw.isLine()) {
				 if (getNext("from")) {
					 if (peekExpression()) draw.setX(parseExpression());
					 skipNext(",");
					 if (peekExpression()) draw.setY(parseExpression());
					 
					 if (getNext("to")) {
						 if (peekExpression()) draw.setEndX(parseExpression());
						 skipNext(",");
						 if (peekExpression()) draw.setEndY(parseExpression());
					 }
				 }
			 }
			 else while (peekNext("at", "with") && ! atDelimiter()) {
				// Set the coordinates with the "at" keyword
				 if (getNext("at")) {
					 // Try to parse one number
					 if (peekExpression()) draw.setX(parseExpression());
					 skipNext(",");
					 // If another number can be parsed
					 if (peekExpression()) draw.setY(parseExpression());
				 }
				 // Set properties with the "with" keyword
				 while (getNext("with")) {
					 // Keep parsing properties
					 while (peekNext("width", "height", "radius", "size", "diameter")) {
						 // If the next token is setting the width
						 if (getNext("width")) {
							 if (peekExpression())
								 draw.setWidth(parseExpression());
							 else if (getNext("and height") && peekExpression())
								 draw.setSize(parseExpression());
							 
						 }
						 // If the next token is setting the height
						 else if (getNext("height")) {
							 if (peekExpression())
								 draw.setHeight(parseExpression());
							 else if (getNext("and width") && peekExpression())
								 draw.setSize(parseExpression());
						 }
						 // If the next token is setting the radius
						 else if (getNext("radius")) {
							 if (peekExpression())
								 draw.setRadius(parseExpression());
						 }
						 // If the next token is setting the diameter
						 else if (getNext("diameter")) {
							 if (peekExpression())
								 draw.setDiameter(parseExpression());
						 }
						 // If the next token is setting the general size parameter.
						 else if (getNext("size")) {
							 if (peekExpression())
								 draw.setSize(parseExpression());
						 }
						 // Skip and tokens between phrases
						 skipNext("and");
						 skipNext("with");
					 }
					 
					 // Skip and tokens and keep trying to check for "and with" instructions
					 skipNext("and");
				 }
			 }
			 
			 return draw;
		}
		return null;
	}
	
	/**
	 * Parses a color.
	 * TODO: use dictionary of all Window colors
	 * @return
	 */
	private Color parseColor() {
		if (getNext("red")) return Color.RED;
		if (getNext("blue")) return Color.BLUE;
		if (getNext("yellow")) return Color.YELLOW;
		if (getNext("green")) return Color.GREEN;
		if (getNext("orange")) return Color.ORANGE;
		if (getNext("magenta")) return Color.MAGENTA;
		if (getNext("black")) return Color.BLACK;
		if (getNext("cyan")) return Color.CYAN;
		if (getNext("dark gray")) return Color.DARK_GRAY;
		if (getNext("gray")) return Color.GRAY;
		if (getNext("orange")) return Color.ORANGE;
		if (getNext("light gray")) return Color.LIGHT_GRAY;
		if (getNext("pink")) return Color.PINK;
		if (getNext("white")) return Color.WHITE;
		return null;
	}
	
	private Operator parseOperator() {
		// Math operators
		if (getNext("+")  || getNext("plus")) return Operator.Add;
		if (getNext("-")  || getNext("minus")) return Operator.Subtract;
		if (getNext("*")  || getNext("times")) return Operator.Multiply;
		if (getNext("/")  || getNext("divided by") || getNext("over")) return Operator.Divide;
		
		// Logic operators
		if (getNext("&&") || getNext("and")) return Operator.And;
		if (getNext("||") || getNext("or")) return Operator.Or;
		
		// Equality testing
		if (getNext("==") || getNext("is")) return Operator.Equal;
		if (getNext("!=") || getNext("is not")) return Operator.NotEqual;
		
		// Comparison operators
		if (getNext(">")  || getNext("greater than") || getNext("is greater than")) return Operator.GreaterThan;
		if (getNext("<")  || getNext("less than") || getNext("is less than")) return Operator.LessThan;
		if (getNext(">=") || getNext("greater than or equal to") || getNext("is greater than or equal to")) return Operator.GreaterThanOrEqual;
		if (getNext("<=") || getNext("less than or equal to") || getNext("is less than or equal to")) return Operator.LessThanOrEqual;
		return null;
	}
	
	private boolean peekExpression() {
		return peekTerminal() || peekNext("(", ")") || peekExistingSymbol();
	}
	
	private Expression parseExpression() {
		if (getNext("("))
			return parseExpression();
		else {
			Expression value = null;
			if (peekTerminal())
				value = parseTerminal();
			else if (peekSymbol())
				value = parseSymbol();
			
			if (peekNext(")"))
				return value;
			
			Operator op = parseOperator();
			if (op != null)
				return parseExpressionTail(value, op);
			
			return value;
		}
	}
	
	private Expression parseExpressionTail(Expression head, Operator operator) {
		if (getNext("("))
			return new Expression(head, operator, parseExpression());
		
		Expression tail = null;
		if (peekTerminal())
			tail = parseTerminal();
		else if (peekSymbol())
			tail = parseSymbol();
		
		if (tail != null) {
			Operator nextOperator = parseOperator();
			if (nextOperator != null) {
				if (Expression.getPrecedence(operator) < Expression.getPrecedence(nextOperator))
					return new Expression(head, operator, parseExpressionTail(tail, nextOperator));
				else
					return parseExpressionTail(new Expression(head, operator, tail), nextOperator);
			}
			else return new Expression(head, operator, tail);
		}
		
		return head;
	}
	
	private boolean peekSymbol() {
		return peekNext().matches("[a-zA-Z][a-zA-Z0-9\\_]*");
	}
	
	private boolean peekExistingSymbol() {
		return peekSymbol() && symbols.contains(peekNext());
	}
	
	private Symbol parseSymbol() {
		String symbolName = getNext();
		symbols.add(symbolName);
		return new Symbol(symbolName);
	}
	
	private boolean peekTerminal() {
		return peekNext().matches("\\d+(|\\.\\d*)");
	}
	
	private Terminal parseTerminal() {
		String next = getNext();
		try {
			return new Terminal(Double.parseDouble(next));
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	private boolean peekNext(String ... matches) {
		for (String match : matches) {
			if (peekNext(match)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean getNext(String ... matches) {
		for (String match : matches) {
			if (peekNext(match)) {
				if (match.indexOf(' ') > 0)
					index += match.split(" ").length;
				else 
					index++;
				return true;
			}
		}
		return false;
	}
	
	private void skipNext(String ... matches) {
		getNext(matches);
	}
	
	private boolean peekNext(String match) {
		if (match.indexOf(' ') > 0) {
			String[] parts = match.split(" ");
			for (int i = 0 ; i < parts.length ; i++) {
				if (index + i >= tokens.length || ! parts[i].equals(tokens[index + i]))
					return false;
			}
			return true;
		}
		else if (index < tokens.length)
			return tokens[index].equals(match);
		else return false;
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
	
	private void skipNext() {
		index++;
	}
	
	private boolean hasNext() {
		return index < tokens.length;
	}
	
	private boolean atDelimiter() {
		return index >= tokens.length || tokens[index].equals("\n") || tokens[index].equals(".");
	}
}
