package parser;

import java.awt.Color;
import java.util.HashSet;

import lexer.Lexer;
import symbolic.Block;
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
 * The Parser generates an object representation of a runnable pseudocode program described by a String.
 * 
 * @author  Keshav Saharia
 *			keshav@techlabeducation.com
 */
public class Parser {
		
	private Lexer lexer;				// the Lexer object for lexing the input text
	private String[] tokens;			// an array of string tokens that this parser has lexed
	private int index = 0;				// the current index of the parser in the token stream
	private HashSet <String> symbols;	// The HashSet containing all symbol names
	
	private Block algorithm;			// Reference to the algorithm currently being parsed.
	
	private String[] drawType = { "circle", "square", "rectangle", "oval", "line" };
	
	/**
	 * Constructs the Parser object.
	 */
	public Parser() {
		lexer = new Lexer();
		symbols = new HashSet <String> ();
	}
	
	/**
	 * Parses the text and returns a Block object representing a runnable pseudocode program.
	 * @param text
	 * @return
	 */
	public Block parse(String text) {
		// Lex the input text and reset the parser
		tokens = lexer.lex(text);
		index = 0;
		symbols.clear();
		
		// Start the high-level parsing routine
		return parseBlock(null);
	}

	/**
	 * Highest level of the recursive descent parser. Parses an algorithm and returns an
	 * Block object representing the parsed algorithm.
	 * 
	 * @return a Block object representing the pseudocode algorithm
	 */
	private Block parseBlock(Block parent) {
		// Create an algorithm and store it to the object reference
		Block block = null;
		
		// Create a root block or attach to the root block
		if (parent == null) {
			block = new Block();
			algorithm = block;
		}
		else block = new Block(algorithm);
		
		// While there are tokens left in the token stream, parse an instruction from the token stream
		while (hasNext()) {
			Instruction instruction = parseInstruction();
			
			// If an instruction was successfully parsed
			if (instruction != null) {
				block.add(instruction);
			}
			// Otherwise skip the current token so the stream is gradually consumed
			else skipNext();
		}
		
		// Return the resulting algorithm object
		return block;
	}
	
	/**
	 * 
	 * @return
	 */
	private Instruction parseInstruction() {
		
		// Repeat instructions forever
		if (getNext("forever")) {
			return new ForeverInstruction(parseBlock(algorithm));
		}
		
		// Set the background instruction
		else if (getNext("set background", "set the background")) {
			return parseBackgroundInstruction();
		}
		
		// Draw a shape instruction
		else if (getNext("draw", "create", "place")) {
			return parseDrawInstruction();
		}
		
		// Put keyword can be used for drawing or for assigning to a symbol
		else if (getNext("put")) {
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
		else if (getNext("set")) {
			if (peekSymbol()) {
				Symbol symbol = parseSymbol();
				if (getNext("to", "as") && peekExpression()) {
					return new AssignInstruction(symbol, parseExpression());
				}
			}
		}
		
		// Increment, decrement, change
		else if (peekNext("increment", "decrement", "change")) {
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
		getNext("to", "as");
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
	
	/**
	 * Parses an operator.
	 * @return
	 */
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
	
	/**
	 * Returns true if there is a valid symbol after thi
	 * @return
	 */
	private boolean peekSymbol() {
		return peekNext().matches("[a-zA-Z][a-zA-Z0-9\\_]*");
	}
	
	/**
	 * Returns true if an existing symbol that the parser has already encountered is the next token
	 * in the input stream.
	 * @return
	 */
	private boolean peekExistingSymbol() {
		return peekSymbol() && symbols.contains(peekNext());
	}
	
	/**
	 * Parses a symbol from the input stream and returns a Symbol object that represents it.
	 * @return a Symbol object representing the next symbol in the input stream.
	 */
	private Symbol parseSymbol() {
		String symbolName = getNext();
		symbols.add(symbolName);
		return new Symbol(symbolName);
	}
	
	/**
	 * Returns true if the next token can be parsed as a terminal.
	 * @return true if the next token is a terminal, false otherwise.
	 */
	private boolean peekTerminal() {
		return peekNext().matches("\\d+(|\\.\\d*)");
	}
	
	/**
	 * Returns a Terminal object representing the next terminal value (currently only for numbers)
	 * in the token input stream.
	 * 
	 * @return a Terminal object representing the next token as a number
	 */
	private Terminal parseTerminal() {
		String next = getNext();
		try {
			return new Terminal(Double.parseDouble(next));
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Returns true if any of the given Strings match the tokens at the current
	 * index in the input stream.
	 * 
	 * @param matches - any number of Strings to match
	 * @return true if there was a match, false otherwise
	 */
	private boolean peekNext(String ... matches) {
		for (String match : matches) {
			if (peekNext(match)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if any of the given Strings match the tokens at the current index
	 * in the input stream, and moves the index to the tokens after the first match.
	 * 
	 * @param matches - any number of Strings to match
	 * @return true if there was a match, false otherwise 
	 */
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
	
	/**
	 * Skips the next matches in the input stream.
	 * @param matches - any number of Strings to ignore.
	 */
	private void skipNext(String ... matches) {
		getNext(matches);
	}
	
	/**
	 * Returns true if the String match occurs in the next tokens in the input stream.
	 * @param match - a String match that may contain spaces containing the tokens 
	 * 				  to match against the input stream at the current position
	 * 
	 * @return true if the match occurs in the next tokens, false otherwise
	 */
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
	
	/**
	 * Returns the next token in the input stream without moving to the next token.
	 * @return the next token in the token stream
	 */
	private String peekNext() {
		if (index < tokens.length)
			return tokens[index];
		else return "";
	}
	
	/**
	 * Gets the next token from the input stream and updates the current token index to the next token.
	 * @return the next token in the token stream
	 */
	private String getNext() {
		if (index < tokens.length) {
			String t = tokens[index];
			index++;
			return t;
		}
		else return "";
	}
	
	/**
	 * Skips the next token in the input stream.
	 */
	private void skipNext() {
		index++;
	}
	
	/**
	 * Returns true if the parser has another token in its input stream.
	 * @return
	 */
	private boolean hasNext() {
		return index < tokens.length;
	}
	
	/**
	 * Returns true if the parser is currently at a delimiter.
	 */
	private boolean atDelimiter() {
		return index >= tokens.length || tokens[index].equals("\n") || tokens[index].equals(".");
	}
}
