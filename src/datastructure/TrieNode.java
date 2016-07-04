package datastructure;

import java.util.HashMap;

/**
 * Represents a single node in a Trie. This node may refer to a value and/or have
 * a mapping to other TrieNode objects. 
 * 
 * @author  Keshav Saharia
 * 			keshav@techlabeducation.com
 * 
 * @license MIT
 */

public class TrieNode <T> {
	// The underlying value at this node.
	private T value;
	// The key of this node.
	private char key;
	// A mapping of child nodes by using their character keys as the hash key,
	// and storing a reference to the node as the value.
	private HashMap<Character, TrieNode<T>> children;
	
	/**
	 * Creates a TrieNode with the given key.
	 * @param key - the character key for this node
	 */
	public TrieNode(char key) {
		this.key = key;
		// Initialize the HashMap of child nodes.
		this.children = new HashMap<Character, TrieNode<T>> ();
	}
	
	/**
	 * Sets the value of this node to the given value.
	 * @param value - the value referenced by this node
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * Returns the value of this node.
	 * @return the value referenced by this node.
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Returns true if this node has a value.
	 * @return true if the node has a value, false otherwise.
	 */
	public boolean hasValue() {
		return value != null;
	}
	
	/**
	 * Returns true if this node has a child with the given key.
	 * @param childKey - the character key of the child node
	 * @return true if the child node is a child of this one, false otherwise
	 */
	public boolean hasChild(char childKey) {
		return children.containsKey(childKey);
	}
	
	/**
	 * Gets the child node with the given key.
	 * @param childKey - the character key of this child node
	 * @return
	 */
	public TrieNode <T> getChild(char childKey) {
		return children.get(childKey);
	}
	
	public HashMap <Character, TrieNode <T>> getChildren() {
		return children;
	}
	
	/**
	 * Adds the given node as a child of this one.
	 * 
	 * @param node - the node to add as a child.
	 */
	public void addChild(TrieNode <T> node) {
		children.put(node.key, node);
	}
}
