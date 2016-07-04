package test;

import static org.junit.Assert.*;

import org.junit.Test;

import expression.*;

public class TestExpression {

	@Test
	public void testTerminal() {
		Terminal one = new Terminal(1);
		Terminal anotherOne = new Terminal(1);
		Terminal two = new Terminal(2);
		
		assertFalse(one.evaluate(null) == two.evaluate(null));
		assertTrue(one.evaluate(null) == anotherOne.evaluate(null));
	}

}
