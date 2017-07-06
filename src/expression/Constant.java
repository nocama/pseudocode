package expression;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Stores references to constant values that are referred to by the parser, certain
 * expressions, the lexer, and the instructions.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 * 
 * @license MIT
 */
@SuppressWarnings("serial")
public class Constant {

	// Size constants
	public static final Terminal BIG = new Terminal(50);
	public static final Terminal MEDIUM = new Terminal(20);
	public static final Terminal SMALL = new Terminal(10);

	// A set of reserved keywords
	public static HashSet <String> keyword = new HashSet <String> () {{
		add("draw");
		add("draw image");
		add("set");
		add("to");
		add("do");
		add("set background");
		add("increment");
		add("decrement");
		add("increase");
		add("decrease");
		add("change");
		add("invert");
		add("if");
		add("forever");
		add("else if");
		add("otherwise if");
		add("else");
		add("otherwise");
		add("print");
		add("add");
		add("subtract");
		add("multiply");
		add("subtract");
		add("is less than");
		add("is greater than");
		add("is equal to");
		add("is not equal to");
		add("is not");
		add("is");
	}};

	// A set of reserved keywords
	public static HashSet <String> attribute = new HashSet <String> () {{
		add("with");
		add("radius");
		add("width");
		add("height");
		add("size");
		add("random");
		add("color");
		add("randomly");
		add("colored");
		add("number");
	}};

	public static HashSet <String> property = new HashSet <String> () {{
		add("width");
		add("height");
		add("size");
		add("radius");
	}};

	public static HashSet <String> operator = new HashSet <String> () {{
		add("+");
		add("-");
		add("*");
		add("/");
		add("and");
		add("or");
		add("not");
		add("to");
		add("by");
		add("<");
		add(">");
		add("<=");
		add(">=");
		add("==");
		add("is");
		add("!=");
	}};
}
