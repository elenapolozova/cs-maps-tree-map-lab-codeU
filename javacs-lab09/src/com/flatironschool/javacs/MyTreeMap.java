/**
 * Plan: implement map interface with a BST
 */
// why is hashmap not always the way to go? 
// ---> Sometimes hashing can be slow
// ---> The keys in a hashmap aren't stored in any particular order, and sometimes we want them to be
package com.flatironschool.javacs;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a Map using a binary search tree.
 * 
 * @param <K>
 * @param <V>
 *
 */
public class MyTreeMap<K, V> implements Map<K, V> {

	private int size = 0;
	private Node root = null;

	/**
	 * Represents a node in the tree.
	 *
	 */
	protected class Node {
		public K key;
		public V value;
		public Node left = null;
		public Node right = null;
		
		/**
		 * @param key
		 * @param value
		 * @param left
		 * @param right
		 */
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
		
	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public boolean containsKey(Object target) {
		return findNode(target) != null;
	}

	/**
	 * Returns the entry that contains the target key, or null if there is none. 
	 * (note that target key is not allowed to be null)
	 * @param target
	 */
	private Node findNode(Object target) {
		// some implementations can handle null as a key, but not this one
		if (target == null) {
            throw new NullPointerException();
	    }
		
		// something to make the compiler happy
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) target;

		Node currentNode = root;
		// while there's tree left to search and we still haven't found it, keep searching
		while (currentNode != null && !(k.compareTo(currentNode.key) == 0)){
			if (k.compareTo(currentNode.key) < 0){ 
				currentNode = currentNode.left; // if targ is smaller than currentNode, move left
			}
			else {
				currentNode = currentNode.right; // if targ is larger than currentNode, move right
			}
		}
		return currentNode;
		// the actual search
        // TODO: Fill this in.
        //return findNodeHelper(root, k);
	}

	/**
	 * Compares two keys or two values, handling null correctly.
	 * 
	 * @param target
	 * @param obj
	 * @return
	 */
	private boolean equals(Object target, Object obj) {
		if (target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}

	// search entire tree to find out whether or not certain value is in it
	@Override
	public boolean containsValue(Object target) {
		boolean valFound = false;
		return containsValueHelper(target, root);
	}

	private boolean containsValueHelper(Object target, Node n){
		boolean valFound = false;
		if (!(n == null)){
			valFound = equals(n.value, target) || containsValueHelper(target, n.left) || containsValueHelper(target, n.right);
		}
		return valFound;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		Node node = findNode(key);
		if (node == null) {
			return null;
		}
		return node.value;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<K>();
		set = keySetHelper(root);
		return set;
	}

	// returns the in-order traversal of the subtree rooted at n
	public LinkedHashSet<K> keySetHelper(Node n) {
		LinkedHashSet<K> set = new LinkedHashSet<K>();
		if (n != null){
			LinkedHashSet<K> left = keySetHelper(n.left);
			LinkedHashSet<K> right = keySetHelper(n.right);
			for (K k : left){
				set.add(k);
			}
			set.add(n.key);
			for (K k : right){
				set.add(k);
			}
		}
		return set;
	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (root == null) {
			root = new Node(key, value);
			size++;
			return null;
		}
		return putHelper(root, key, value);
	}

	// if key is already in the tree, replaces the old value with the new and returns the old
	// if key is not in the tree, creates a new (key, value) node and adds it to the right place
	private V putHelper(Node node, K key, V value) {
		// something to make the compiler happy
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;

		V oldValue = null;

		if (equals(node.key, k)){
			oldValue = node.value;
			node.value = value; // if found the right key, update its value
		}
		else { 
			if (k.compareTo(node.key) < 0){ // if k is less, add on left
				if (node.left == null){
					node.left = new Node(key, value); // if the appropriate space is empty, add it!
					size++;
				} // end if
				else {
					oldValue = putHelper(node.left, key, value);

				} // end else
			}
			else{ // if k is greater, add on right
				if (node.right == null){
					node.right = new Node(key, value); // if the appropriate space is empty, add it!
					size++;
				} // end if
				else {
					oldValue = putHelper(node.right, key, value);
				} // end else
			} // end else
		} // end else
        return oldValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<V>();
		Deque<Node> stack = new LinkedList<Node>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			if (node == null) continue;
			set.add(node.value);
			stack.push(node.left);
			stack.push(node.right);
		}
		return set;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyTreeMap<String, Integer>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		Integer value = map.get("Word1");
		System.out.println(value);
		
		for (String key: map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	/**
	 * Makes a node.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public MyTreeMap<K, V>.Node makeNode(K key, V value) {
		return new Node(key, value);
	}

	/**
	 * Sets the instance variables.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @param node
	 * @param size
	 */
	public void setTree(Node node, int size ) {
		this.root = node;
		this.size = size;
	}

	/**
	 * Returns the height of the tree.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @return
	 */
	public int height() {
		return heightHelper(root);
	}

	private int heightHelper(Node node) {
		if (node == null) {
			return 0;
		}
		int left = heightHelper(node.left);
		int right = heightHelper(node.right);
		return Math.max(left, right) + 1;
	}
}
