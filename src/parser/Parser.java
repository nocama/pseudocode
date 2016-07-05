package parser;

import java.awt.Color;
import java.util.HashSet;

import expression.*;
import instruction.*;

/**
 * The Parser generates an object representation of a runnable pseudocode program described by a String.
 * 
 * @author  Keshav Saharia
 *			keshav@techlabeducation.com
 *
 * @license MIT
 */
public class Parser {

	private Lexer lexer;				// the Lexer object for lexing the input text
	private String[] tokens;			// an array of string tokens that this parser has lexed
	private int index = 0;				// the current index of the parser in the token stream
	private Block rootBlock;			// The root block being parsed

	// The list of shapes that can be drawn.
	private String[] drawType = { "circle", "square", "rectangle", "oval", "line", "background" };
	private String[] builtInExpression = { "mouse", "random" };
	private static HashSet <String> reservedWords;
	
	/**
	 * Constructs the Parser object.
	 */
	public Parser() {
		lexer = new Lexer();
		if (reservedWords == null) {
			reservedWords = new HashSet <String> ();
			reservedWords.add("width");
			reservedWords.add("height");
			reservedWords.add("mousex");
			reservedWords.add("mousey");
			reservedWords.add("mouseclicked");
		}
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
		Block block = new Block(parent);
		if (parent == null)
			rootBlock = block;

		// While there are tokens left in the token stream, parse an instruction from the token stream
		while (hasNext()) {
			if (getNext("\n")) continue;
			
			// Count the indent level, and break if it doesn't match this block level
			int indent = countIndent();
			if (indent < block.getIndentLevel())
				break;
			// Otherwise take all tabs off the stream
			else while (getNext("\t"));
			
			Instruction instruction = parseInstruction(block);
			
			// If an instruction was successfully parsed
			if (instruction != null) {
				block.add(instruction);
				getNext("\n");
			}
			// Otherwise skip the current token so the stream is gradually consumed
			else skipNext();
		}

		// Return the resulting algorithm object
		return block;
	}

	/**
	 * Parses an instruction in the context of the given block, and returns an Instruction
	 * object if one could be parsed (or null otherwise).
	 * 
	 * @return an Instruction object if one could be parsed, null otherwise
	 */
	private Instruction parseInstruction(Block block) {

		// Repeat instructions forever
		if (getNext("forever"))
			return new Forever(parseBlock(block));
		
		// Parse an if statement
		else if (getNext("if") && peekExpression()) {
			return parseIf(block);
		}
		
		// Parse an else-if or else statement
//		else if (getNext("else", "otherwise")) {
//			if (getNext("if") && peekExpression())
//				return parseElseIf(block);
//			else
//				return new ElseBlock(parseBlock(block));
//		}
		
		// Prints a given value to the standard output.
		else if (getNext("print") && peekExpression()) {
			return parsePrint();
		}

		// Sets the background to the given color
		else if (getNext("background", "set background", "set the background")) {
			return parseBackground();
		}

		// Draw something on the screen
		else if (getNext("draw", "create", "place")) {
			return parseDraw();
		}

		// Put keyword can be used for drawing or for assigning to a symbol
		else if (getNext("put")) {
			return parsePut();
		}

		// The set keyword 
		else if (getNext("set")) {
			return parseAssign();
		}

		// Increment, decrement, change
		else if (peekNext("increment", "decrement", "change")) {
			return parseIncrement();
		}

		return null;
	}
	
	/**
	 * Parses an instruction that begins with the "put" keyword.
	 * @return
	 */
	private Instruction parsePut() {
		skipNext("a", "an");

		// Put in the context of a drawing instruction
		if (peekNext(drawType))
			return parseDraw();

		// Checks if there is an expression
		else if (peekExpression()) {
			Expression expr = parseExpression();

			// If there is a symbol to assign it into
			if (getNext("in", "into") && peekSymbolTerminal()) {
				return new Assign(parseSymbolTerminal(), expr);
			}
		}
		
		return null;
	}

	private Instruction parseElseIf(Block parentBlock) {
		Expression condition = parseExpression();
		if (condition != null) {
			Block elseIfBlock = parseBlock(parentBlock);
			return new ElseIfBlock(condition, elseIfBlock);
		}
		return null;
	}

	private Instruction parseIf(Block parentBlock) {
		Expression condition = parseExpression();
		if (condition != null) {
			Block ifBlock = parseBlock(parentBlock);
			return new IfBlock(condition, ifBlock);
		}
		return null;
	}

	private Instruction parsePrint() {
		Expression expression = parseExpression();
		if (expression != null) {
			return new Print(expression);
		}
		return null;
	}

	private Instruction parseAssign() {
		if (peekSymbolTerminal()) {
			SymbolTerminal symbol = parseSymbolTerminal();
			if (getNext("to", "as") && peekExpression()) {
				return new Assign(symbol, parseExpression());
			}
		}
		return null;
	}

	private Instruction parseIncrement() {
		// The direction in which the change will happen (increment and change are both positive)
		int direction = (getNext().equals("decrement")) ? -1 : 1;

		if (peekSymbolTerminal()) {
			SymbolTerminal symbol = parseSymbolTerminal();

			// Give an explicit amount to increment by
			if (getNext("by") && peekExpression())
				return new Increment(symbol, parseExpression());
			else 
				return new Increment(symbol, new Terminal(direction));
		}
		return null;
	}

	/**
	 * Parses an instruction to set the background color.
	 * @return the Instruction object representing a background draw
	 */
	private Instruction parseBackground() {
		getNext("to", "as");
		Color color = parseColor();

		Background background = new Background();
		if (color != null)
			background.setColor(color);

		return background;
	}

	/**
	 * Parses a drawing command instruction.
	 * @return an Instruction object representing the drawing command.
	 */
	private Instruction parseDraw() {
		// Caches size input for later
		Terminal size = null;
		
		skipNext("a", "an");
		
		if (getNext("big"))
			size = Constant.BIG;
		if (getNext("small"))
			size = Constant.SMALL;
		
		// Try parsing a color
		Color color = parseColor();
		// Keep looking for a drawing type until the end of the current instruction
		while (! atDelimiter() && ! peekNext(drawType))
			skipNext();
		
		if (getNext("background")) {
			return parseBackground();
		}
		
		// Any of the standard shapes should produce a draw instruction
		else if (peekNext(drawType)) {
			Draw draw = new Draw(getNext());

			if (color != null)
				draw.setColor(color);
			
			if (size != null)
				draw.setSize(size);

			// If drawing a line
			if (draw.isLine()) {
				// First get the from coordinate
				if (getNext("from", "going from", "starting at", "starting from")) {
					if (peekExpression()) draw.setX(parseExpression());
					skipNext(",");
					if (peekExpression()) draw.setY(parseExpression());
					
					// Then get the "to" coordinate
					if (getNext("to", "upto", "ending at", "and going to")) {
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
				else while (getNext("with")) {
					// Keep parsing properties
					while (peekNext("width", "height", "radius", "size", "diameter")) {
						// If the next token is setting the width
						if (getNext("width")) {
							skipNext("of");
							if (peekExpression())
								draw.setWidth(parseExpression());
							else if (getNext("and height") && peekExpression())
								draw.setSize(parseExpression());

						}
						// If the next token is setting the height
						else if (getNext("height")) {
							skipNext("of");
							if (peekExpression())
								draw.setHeight(parseExpression());
							else if (getNext("and width") && peekExpression())
								draw.setSize(parseExpression());
						}
						// If the next token is setting the radius
						else if (getNext("radius")) {
							skipNext("of");
							if (peekExpression())
								draw.setRadius(parseExpression());
						}
						// If the next token is setting the diameter
						else if (getNext("diameter")) {
							skipNext("of");
							if (peekExpression())
								draw.setDiameter(parseExpression());
						}
						// If the next token is setting the general size parameter.
						else if (getNext("size")) {
							skipNext("of");
							if (peekExpression())
								draw.setSize(parseExpression());
						}
						// Skip and tokens between phrases
						skipNext("and");
						skipNext("with");
						skipNext("a");
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
		if (peekNext().startsWith("#") && peekNext().length() == 7 && 
			peekNext().matches("\\#[0-9a-f]+")) {
			
			// Parse hex code
		}
		
		// Try 1 word colors
		if (RGB.hasColor(peekNext())) {
			return RGB.getColor(getNext());
		}
		// Try 2 word colors
		else if (RGB.hasColor(peekNext(2))) {
			return RGB.getColor(getNext() + getNext());
		}
		
		// Standard RGB colors
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
		return peekNumberTerminal() || peekNext("(", ")") || peekExistingSymbolTerminal() 
			|| peekNext("mouse") || peekNext("random");
	}

	private Expression parseExpression() {
		if (getNext("mouse")) {
			return parseMouseTerminal();
		}
		
		if (getNext("("))
			return parseExpression();
		else {
			Expression value = parseTerminal();

			if (peekNext(")"))
				return value;

			Operator op = parseOperator();
			if (op != null)
				return parseExpressionTail(value, op);

			return value;
		}
	}

	private Expression parseExpressionTail(Expression head, Operator operator) {
		
		// Start parsing a parenthesized expression from the expression parsing routine
		if (getNext("("))
			return new Expression(head, operator, parseExpression());
		
		// Get the tail expression
		Expression tail = parseTerminal();
		
		// If a tail to this expression was parsed
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
		// Prevents the text "and" for combining instructions to conflict with expression "and"
//		else if (operator == Operator.And) {
//			goBack();
//		}
		
		// Returns the expression head
		return head;
	}
	
	private Expression parseMouseTerminal() {
		if (getNext("x"))
			return new SymbolTerminal("mousex");
		else if (getNext("y"))
			return new SymbolTerminal("mousey");
		else if (getNext("clicked"))
			return new SymbolTerminal("mouseclicked");
		
		return new SymbolTerminal("mouseclicked");
	}
	
	private boolean peekTerminal() {
		return peekNumberTerminal() ||
			   peekStringTerminal() ||
			   peekExistingSymbolTerminal();
	}
	
	/**
	 * Tries to parse a terminal value and return it.
	 * @return a Terminal object representing a parsed value if there was one.
	 */
	private Terminal parseTerminal() {
		if (peekNumberTerminal())
			return parseNumberTerminal();
		if (peekStringTerminal())
			return parseStringTerminal();
		if (peekExistingSymbolTerminal())
			return parseSymbolTerminal();
		return null;
	}

	/**
	 * Returns true if there is a valid symbol at the parsing index
	 * @return
	 */
	private boolean peekSymbolTerminal() {
		return peekNext().matches("[a-zA-Z][a-zA-Z0-9\\_]*");
	}

	/**
	 * Returns true if an existing symbol that the parser has already encountered is the next token in the input stream.
	 * @return true if the next symbol is an existing symbol or a reserved word.
	 */
	private boolean peekExistingSymbolTerminal() {
		return peekSymbolTerminal() && (rootBlock.hasSymbol(peekNext()) || reservedWords.contains(peekNext()));
	}

	/**
	 * Parses a symbol from the input stream and returns a Symbol object that represents it.
	 * @return a Symbol object representing the next symbol in the input stream.
	 */
	private SymbolTerminal parseSymbolTerminal() {
		String symbolName = getNext();
		rootBlock.assign(symbolName, new Terminal(0));
		return new SymbolTerminal(symbolName);
	}

	/**
	 * Returns true if the next token can be parsed as a literal number.
	 * @return true if the next token is a numeric terminal, false otherwise.
	 */
	private boolean peekNumberTerminal() {
		return peekNext().matches("\\d+(|\\.\\d*)") ||
			   (index + 1 < tokens.length && peekNext().equals("-") && tokens[index + 1].matches("\\d+(|\\.\\d*)"));
	}

	/**
	 * Returns a Terminal object representing the next numeric terminal value in the token input stream.
	 * @return a Terminal object representing the next token as a number
	 */
	private Terminal parseNumberTerminal() {
		boolean negative = getNext("-");
		
		String next = getNext();
		try {
			return new Terminal(Double.parseDouble(next) * ((negative) ? -1 : 1));
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Returns true if the next token can be parsed as a string terminal.
	 * @return true if the next token is a string terminal, false otherwise.
	 */
	private boolean peekStringTerminal() {
		return peekNext().startsWith("\"") && peekNext().endsWith("\"");
	}

	/**
	 * Returns a Terminal object representing the next terminal value (currently only for numbers)
	 * in the token input stream.
	 * 
	 * @return a Terminal object representing the next token as a number
	 */
	private Terminal parseStringTerminal() {
		String next = getNext();
		return new StringTerminal(next.substring(1, next.length() - 1));
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
	 * Returns the next n tokens in the input stream.
	 */
	private String peekNext(int n) {
		String next = "";
		for (int i = 0 ; i < n && index + i < tokens.length ; i++)
			next += tokens[index + i] + " ";
		return next.trim();
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
	
	/**
	 * Returns the number of incoming tokens that are tabs.
	 */
	private int countIndent() {
		int indent = 0;
		while (index + indent < tokens.length && tokens[index + indent].equals("\t"))
			indent++;
		return indent;
	}
}
