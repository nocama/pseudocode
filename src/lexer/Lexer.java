package lexer;

import java.util.ArrayList;

public class Lexer {
	
	ArrayList<String> tokens;
	StringBuilder token;
	
	private static final int WORD = 1;
	private static final int NUMBER = 2;
	
	// For number parsing.
	boolean hasDecimal = false;
	
	/**
	 * Creates the lexer.
	 */
	public Lexer() {
		tokens = new ArrayList <String> ();
		token = new StringBuilder();
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public String[] lex(String text) {
		tokens.clear();
		
		int state = 0;
		
		for (int index = 0 ; index < text.length() ; index++) {
			char c = text.charAt(index);
			
			// Space characters terminate current token.
			if (c == ' ') {
				pushToken();
			}
			else if (state == WORD && Character.isAlphabetic(c)) {
				token.append(c);
			}
			else if (state == NUMBER && Character.isDigit(c)) {
				token.append(c);
			}
			else if (state == NUMBER && !hasDecimal && c == '.') {
				token.append(c);
				hasDecimal = true;
			}
			// Negative numbers
//			else if (index + 1 < text.length() && c == '-' && Character.isDigit(text.charAt(index + 1))) {
//				token.append(c);
//				state = NUMBER;
//			}
			else if (index + 1 < text.length() && isDouble(c, text.charAt(index + 1))) {
				pushToken();
				pushToken("" + c + text.charAt(index + 1));
			}
			else if (isSingle(c)) {
				pushToken();
				pushToken(c);
			}
			else {
				if (Character.isAlphabetic(c)) {
					token.append(c);
					state = WORD;
				}
				else if (Character.isDigit(c)) {
					token.append(c);
					state = NUMBER;
				}
			}
		}
		pushToken();
		
		return tokens.toArray(new String[tokens.size()]);
	}
	
	public boolean isSingle(char c) {
		return c == '\n' || c == '\t' || c == ',' || c == '.' || isOperator(c);
	}
	
	public boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>';
	}
	
	public boolean isDouble(char c, char d) {
		return (c == '>' || c == '<') && d == '=';
	}
	
	public void pushToken(char token) {
		tokens.add(("" + token).toLowerCase());
	}
	
	public void pushToken(String token) {
		tokens.add(token.toLowerCase());
	}
	
	public void pushToken() {
		if (token.length() > 0) {
			tokens.add(token.toString().toLowerCase());
			hasDecimal = false;
			clearToken();
		}
	}
	
	public void clearToken() {
		token.delete(0, token.length());
		hasDecimal = false;
	}
}
