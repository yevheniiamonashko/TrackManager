package nl.saxion.cds.custom_data_structures;

import nl.saxion.cds.collection.DuplicateKeyException;
import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxBinaryTree;

public class MyBinarySearchTree<K extends Comparable<K>, V> implements SaxBinaryTree<K, V> {


    public static class Node<K extends Comparable<K>, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        int height; //used for an AVL binary search tree

        protected Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }

    }

    public Node<K, V> root;
    protected int size;

    /**
     * Returns the root node of the tree.
     *
     * @return the root node
     */
    public Node<K, V> getRoot() {
        return root;
    }

    public MyBinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Checks if the tree contains a node with the specified key.
     *
     * @param key the key to search for
     * @return true if the key is found, false otherwise
     */

    @Override
    public boolean contains(K key) {
        return search(root, key) != null;
    }

    /**
     * Retrieves the value associated with the specified key in the tree.
     *
     * @param key the key whose corresponding value will be returned
     * @return the value associated with the specified key
     * @throws KeyNotFoundException if the key is not found in the tree
     */

    @Override
    public V get(K key) throws KeyNotFoundException {
        Node<K, V> node = search(root, key);
        if (node == null) {
            throw new KeyNotFoundException("" + key);
        }
        return node.value;
    }


    /**
     * Retrieves all values associated with keys that start with the specified prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of values whose keys match the prefix
     */

    public MyArrayList<V> getByPrefix(String prefix) {
        MyArrayList<V> results = new MyArrayList<>();
        searchByPrefix(this.root, prefix.toLowerCase(), results);
        return results;
    }

    /**
     * Adds a new node to the tree.
     * If a node with the given key already exists, throws a DuplicateKeyException.
     *
     * @param key   the key of node to be added
     * @param value the value of the node associated with the key
     * @throws DuplicateKeyException if the key already exists in the tree
     */

    @Override
    public void add(K key, V value) throws DuplicateKeyException {
        root = insert(root, key, value);

    }

    /**
     * Inserts a new node into the tree with the given key and value using recursion.
     * The tree is traversed to find the correct position for the new node.
     *
     * @param node  the current node in the traversal (initially the root node)
     * @param key   the key of the node to be inserted
     * @param value the value associated with the inserted key
     * @return the updated node after the insertion, which will be the new subtree root
     * @throws DuplicateKeyException if a node with the given key already exists in the tree
     */
    protected Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);


        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            node.left = insert(node.left, key, value);
        } else if (compare > 0) {
            node.right = insert(node.right, key, value);
        } else {
            throw new DuplicateKeyException("" + key);
        }
        return node;
    }

    /**
     * Removes a node with the given key from the tree.
     * If the key is found, the node is removed, and its value is returned.
     * If the key is not found, a KeyNotFoundException is thrown.
     *
     * @param key the key of the node to remove
     * @return the value of the removed node
     * @throws KeyNotFoundException if no node with the given key exists in the tree
     */

    @Override
    public V remove(K key) throws KeyNotFoundException {
        Node<K, V> nodeToRemove = search(root, key);
        if (nodeToRemove == null) {
            throw new KeyNotFoundException("" + key);
        }
        V value = nodeToRemove.value;
        root = delete(root, key);
        size--;
        return value;
    }


    /**
     * Deletes a node with the given key from the tree using recursion.
     * If the node has two children, it finds the minimum node in the right subtree
     * (in-order successor), replaces the current node's key and value with the successor's,
     * and removes the successor node.
     * If the node has one or no children, the node is simply replaced by its child or just deleted if it is a leaf node.
     *
     * @param node the current node in the traversal (initially the root node)
     * @param key  the key of the node to delete
     * @return the updated node after the deletion, which will be the new subtree root
     */

    protected Node<K, V> delete(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            node.left = delete(node.left, key);
        } else if (compare > 0) {
            node.right = delete(node.right, key);

        } else {

            // Node with only one child or a leaf node
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }


            //Node with two children
            Node<K, V> minNode = getMin(node.right);

            node.key = minNode.key;
            node.value = minNode.value;

            node.right = delete(node.right, minNode.key);

        }


        return node;
    }

    /**
     * Finds the leftmost node (in-order successor) in a given subtree.
     *
     * @param node the root node of the subtree to search
     * @return the smallest node in the subtree
     */

    private Node<K, V> getMin(Node<K, V> node) {

        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    /**
     * Retrieves all keys in the tree in sorted order using an in-order traversal.
     *
     * @return a list containing all keys of the tree, in sorted order
     */
    @Override
    public MyArrayList<K> getKeys() {
        MyArrayList<K> keys = new MyArrayList<>();
        if (root == null) {
            return keys;
        }
        inOrder(root, keys);
        return keys;
    }

    /**
     * Checks if the tree is empty.
     *
     * @return true if the tree contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Retrieves the number of elements in the tree.
     *
     * @return the size of the tree
     */
    @Override
    public int size() {
        return size;
    }


    /**
     * Search for a node with the given key in the tree using recursion.
     * This method recursively traverses the tree starting from the provided node,
     * typically called with the root node as the starting point.
     *
     * @param node the current node in the traversal (initially the root node)
     * @param key  the key to search for
     * @return the node with the provided key if found, otherwise return null
     */

    private Node<K, V> search(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        if (node.key.equals(key)) {
            return node;
        }

        if (key.compareTo(node.key) < 0) {
            return search(node.left, key);

        } else {
            return search(node.right, key);
        }


    }

    /**
     * Recursively searches for values whose keys start with the specified prefix.
     *
     * @param node    the current node in the traversal (initially the root node)
     * @param prefix  the prefix to match with node keys
     * @param results the list to store values whose keys match the prefix
     */

    private void searchByPrefix(Node<K, V> node, String prefix, MyArrayList<V> results) {
        if (node == null) {
            return;
        }

        String nodeKeyLower = node.key.toString().toLowerCase();


        if (nodeKeyLower.startsWith(prefix)) {
            results.addLast(node.value);
        }


        searchByPrefix(node.left, prefix, results);
        searchByPrefix(node.right, prefix, results);


    }


    /**
     * Performs an in-order traversal of the tree and adds all keys to the provided list in sorted order.
     *
     * @param node the current node in the traversal (starting from the root)
     * @param keys the list to store the keys in
     */
    private void inOrder(Node<K, V> node, MyArrayList<K> keys) {
        if (node != null) {
            inOrder(node.left, keys);
            keys.addLast(node.key);
            inOrder(node.right, keys);
        }
    }
    /**
     * Generates a GraphViz Dot string representation of the binary search tree.
     *
     * @param name The name of the graph, used as the label in the DOT representation.
     * @return A string in DOT format that represents the binary search tree structure.
     */

    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ").append(name).append(" {\n");

        if (root == null) {
            sb.append("    null [shape=point];\n");
        } else {
            buildGraphViz(root, sb);
        }

        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Recursively builds the GraphViz DOT representation of the binary search tree.
     * Each node is represented as a labeled node, and edges are added to show
     * relationships between parent and child nodes.
     *
     * @param node The current node being processed in the traversal.
     * @param sb   The StringBuilder that accumulates the DOT representation.
     */

    private void buildGraphViz(Node<K, V> node, StringBuilder sb) {
        if (node != null) {

            sb.append("    \"").append(node.key).append("\" [label=\"").append(node.key).append(": ").append(node.value).append("\"];\n");

            if (node.left != null) {

                sb.append("    \"").append(node.key).append("\" -> \"").append(node.left.key).append("\";\n");
                buildGraphViz(node.left, sb);
            }

            if (node.right != null) {

                sb.append("    \"").append(node.key).append("\" -> \"").append(node.right.key).append("\";\n");
                buildGraphViz(node.right, sb);
            }
        }
    }


}
