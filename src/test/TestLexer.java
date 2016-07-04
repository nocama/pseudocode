package test;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.Test;

import ast.Expression;
import ast.Terminal;
import lexer.Lexer;

public class TestLexer {

	static Lexer lexer = new Lexer();
	
	@Test
	public void testSimpleLex() {
		assertTrue(test("draw", "draw"));
		assertTrue(test("draw ", "draw"));
		assertTrue(test("draw a", "draw", "a"));
		assertTrue(test("draw a ", "draw", "a"));
		assertTrue(test("draw a circle at", "draw", "a", "circle", "at"));
	}
	
	@Test
	public void testExpressionLex() {
		assertTrue(test("1", "1"));
		assertTrue(test("-5 ", "-", "5"));
		assertTrue(test("1-5", "1", "-", "5"));
		assertTrue(test("1 + 2 * 3-4", "1", "+", "2", "*", "3", "-", "4"));
		assertTrue(test("draw a circle at", "draw", "a", "circle", "at"));
	}
	
	public boolean test(String ... lex) {
		if (lex.length > 0) {
			String[] tokens = lexer.lex(lex[0]);
			if (tokens.length != lex.length - 1) {
				System.out.println("LENGTH MISMATCH: got " + tokens.length + " tokens, expected " + (lex.length - 1));
				print(tokens);
				print(lex, 1);
				return false;
			}
			for (int i = 0 ; i < tokens.length ; i++) {
				if (! tokens[i].equals(lex[i + 1])) {
					System.out.println("MISMATCH: got " + tokens[i] + ", expected " + lex[i + 1]);
					print(tokens, 0, i);
					print(lex, 1, i + 1);
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void print(String[] tokens) {
		print(tokens, 0, -1);
	}
	
	public void print(String[] tokens, int offset) {
		print(tokens, offset, -1);
	}
	
	/**
	 * Prints out the token stream.
	 * @param tokens
	 */
	public void print(String[] tokens, int offset, int error) {
		// For each token
		PrintStream stream;
		for (int i = offset ; i < tokens.length ; i++) {
			stream = System.out;
			if (i == error) stream = System.err;
			
			stream.print("[");
			// Print out the token
			if (tokens[i].equals("\n"))
				stream.print("\\n");
			else if (tokens[i].equals("\t"))
				stream.print("\\t");
			else
				stream.print(tokens[i]);
			
			stream.print("] ");
		}
		System.out.println();
	}

}
