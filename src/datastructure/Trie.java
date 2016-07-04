package datastructure;

/**
 * Trie is a Java implementation of a trie data structure.
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 * 
 * @license MIT
 */

public class Trie <T> {
	TrieNode <T> root;
	int maxKeyLength = 0;
	
	/**
	 * Create a new Trie object.
	 */
	public Trie() {
		root = new TrieNode <T> ((char) 0);
	}
	
	/**
	 * Adds a word with the specified index to the trie.
	 * @param word - word to be added
	 * @param index - the index associated with the word, returned with contains(word). Must be greater than 0.
	 */
	
	public void add(String word, T index) {
		if (! word.equals("")) {
			add(root, word, 0, index);
			
			// Store the maximum key length.
			if (word.length() > maxKeyLength)
				maxKeyLength = word.length();
		}
	}
	
	/**
	 * Recursively adds the given word to the given TrieNode object from the given wordIndex onwards.
	 * The base case of this recursive call is when the wordIndex goes beyond the last valid index, in which
	 * case the current node is assigned the given value.
	 * 
	 * @param node
	 * @param word
	 * @param wordIndex
	 * @param value
	 */
	private void add(TrieNode <T> node, String word, int wordIndex, T value) {
		// If the index is currently at the end of the word, set it as a terminal value and return.
		if (wordIndex == word.length()) {
			node.setValue(value);
			return;
		} 
		
		// Get the current character and build/traverse the tree with it.
		char current = word.charAt(wordIndex);
		
		// Add a new node for this character if there is not one already.
		if (! node.hasChild(current))
			node.addChild( new TrieNode <T> (current) );
		
		// Recursively add on the child node of this one with the rest of the word.
		add( node.getChild(current), word , wordIndex + 1, value );
	}
	
	/**
	 * Returns the index associated with the word, if it exists. Returns a 0 if a substring of the word is found
	 * @param word
	 * @return index associated with the word, if any
	 */
	
	public T get(String word) {
		return get(root, word, 0);
	}
	
	private T get(TrieNode <T> node, String word, int wordIndex) {
		// If the end of the word has been reached, return the root value.
		if (wordIndex >= word.length())
			return node.getValue();
		
		// Stop if the current node does not have the current character as a key. 
		if (! node.hasChild( word.charAt(wordIndex) ))
			return null;
		
		// Get the value for the child node with the given key character.
		return get(node.getChild(word.charAt(wordIndex)) , word, wordIndex + 1); 
	}
	
	/**
	 * Returns true if the given word is in the trie.
	 * @param word
	 * @return true if the word is in the trie, false otherwise.
	 */
	public boolean contains(String word) {
		return get(root, word, 0) != null;
	}
	
	/**
	 * Returns the nearest terminal node to the given node.
	 * @param word
	 * @return
	 */
	public T getNearest(String word) {
		return getNearest(root, word, 0, maxKeyLength);
	}
	
	/**
	 * Get the closest node to the given node with the given word and wordIndex.
	 * @param node
	 * @param word
	 * @param wordIndex
	 * @param depth
	 * @return
	 */
	private T getNearest(TrieNode <T> node, String word, int wordIndex, int depth) {
		if (wordIndex >= word.length() || ! node.hasChild(word.charAt(wordIndex))) {
			// If this is a terminal node, return its value.
			if (node.hasValue())
				return node.getValue();
			
			// If the depth has not yet been set, iterate over depths until one is found.
			if (wordIndex == word.length()) {
				for (int i = 1 ; i < maxKeyLength - wordIndex ; i++) {
					T nearest = getNearest(node, word, wordIndex + 1, i);
					if (nearest != null)
						return nearest;
				}
				return null;
			}
			
			// If this node is searching for the nearest node.
			else if (depth > 0) {
				// Try all child nodes.
				for (Character key : node.getChildren().keySet()) {
					// Find the closest key to the node within the given depth restriction.
					T closestToKey = getNearest(node.getChild(key), word, wordIndex, depth - 1);
					if (closestToKey != null)
						return closestToKey;
				}
				return null;
			}
			
			// If the max depth has been reached.
			else return null;
		}
		
		// Keep traversing the tree until a non-contained word is found.
		return getNearest(node.getChild(word.charAt(wordIndex)), word, wordIndex + 1, depth - 1);
	}
}