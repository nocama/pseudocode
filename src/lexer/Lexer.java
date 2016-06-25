package lexer;

import java.util.ArrayList;

public class Lexer {
	
	ArrayList<String> tokens;
	StringBuilder token;
	
	private static final int WORD = 1;
	private static final int NUMBER = 2;
	
	// For number parsing.
	boolean hasDecimal = false;
	
	public Lexer() {
		tokens = new ArrayList <String> ();
		token = new StringBuilder();
	}
	
	public String[] lex(String text) {
		tokens.clear();
		
		int state = 0;
		
		for (int index = 0 ; index < text.length() ; index++) {
			char c = text.charAt(index);
			
			// Space characters terminate current token.
			if (c == ' ') {
				pushToken();
			}
			else if (isSingle(c)) {
				pushToken();
				pushToken(c);
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
		return c == '\n' || c == '\t' || c == ',';
	}
	
	public void pushToken(char token) {
		tokens.add("" + token);
	}
	
	public void pushToken(String token) {
		tokens.add(token);
	}
	
	public void pushToken() {
		if (token.length() > 0) {
			tokens.add(token.toString());
			hasDecimal = false;
			clearToken();
		}
	}
	
	public void clearToken() {
		token.delete(0, token.length());
		hasDecimal = false;
	}
}
