/**
 *
 */
package com.allendowney.thinkdast;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;

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
	 *
	 * @param target
	 */
	private Node findNode(Object target) {
		// some implementations can handle null as a key, but not this one
		if (target == null) {
			throw new IllegalArgumentException();
		}

		// something to make the compiler happy
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) target;
	
		
		
		Node node = root; 
		while(node != null) {
			// k가 작으면 음수.
			int com = k.compareTo(node.key);
			
			if(com < 0) {
				node = node.left;
			} else if (com > 0 ) {
				node =node.right;
			} else {
				return node;
			}
			
			
		}
		// TODO: FILL THIS IN!
		return null;
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

	//모든 노드를 검색해야한다.
	@Override
	public boolean containsValue(Object target) {
		return containsValueHelper(root, target);
	}

	private boolean containsValueHelper(Node node, Object target) {
		// TODO: FILL THIS IN!
		if(node == null) 
			return false;
		
		if(node.value == target) {
			return true;
		}

		if(containsValueHelper(node.left, target)) {
			return true;
		}
		
		if(containsValueHelper(node.right, target)) {
			return true;
		}
		
		return false;
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
		//일련의 키들을 반환 탐색 BFS나 DFS로
		Set<K> set = new LinkedHashSet<K>();
		// TODO: FILL THIS IN!
		addInOrder(root, set);
		return set;
	}
	
	public void addInOrder(Node node,Set<K> set) {
		
		if(node==null) return;
		addInOrder(node.left,set);
		set.add(node.key);
		addInOrder(node.right, set);
		
	}
	
	//dfs는 연습용으로 만들었음.
	public LinkedHashSet<K> dfs() {
		Set<K> set = new LinkedHashSet<K>();
		dfsR(root,set);
//		System.out.println(set.size());
		return (LinkedHashSet<K>) set;
	}
	
	public void dfsR(Node node,Set<K> set) {
		if(node != null ) {
		set.add(node.key);
		dfsR(node.left,set);
		dfsR(node.right,set);
		}
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

	private V putHelper(Node node, K key, V value) {
		// TODO: FILL THIS IN!
		// 어디에 넣을것인지? 루트부터 탐색해서 value 위치를 찾는다.
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		// 음수면 k가 작은 것임.
		int com = k.compareTo(node.key);
		
		// 왼쪽으로 탐색
		if(com < 0) {
			if(node.left == null) {
				node.left = new Node(key, value);
				size++;
				return null;
			} else {
			putHelper(node.left, key, value);
			}
		} else if ( com > 0) {
			if(node.right == null) {
				node.right = new Node(key, value);
				size++;
				return null;
			}
			putHelper(node.right, key, value);
		}
		
		
			V oldValue = node.value;
			node.key = key;
			node.value = value;
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
		// OPTIONAL TODO: FILL THIS IN!
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
