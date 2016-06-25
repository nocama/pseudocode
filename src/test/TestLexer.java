package test;

import static org.junit.Assert.*;

import org.junit.Test;

import lexer.Lexer;

public class TestLexer {

	@Test
	public void testSimpleLex() {
		Lexer lexer = new Lexer();
		// TODO: automate testing
		print(lexer.lex("draw "));
		print(lexer.lex("draw a"));
		print(lexer.lex("draw a circle ldfk11, fdd"));
		print(lexer.lex("draw a circle at 1, 1"));
	}
	
	public void print(String[] tokens) {
		System.out.print("[");
		for (int i = 0 ; i < tokens.length ; i++) {
			System.out.print(tokens[i]);
			if (i + 1 < tokens.length)
				System.out.print(", ");
		}
		System.out.println("]");
	}

}
