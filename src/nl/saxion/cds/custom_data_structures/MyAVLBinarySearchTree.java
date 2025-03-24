package nl.saxion.cds.custom_data_structures;

import nl.saxion.cds.collection.*;

public class MyAVLBinarySearchTree<K extends Comparable<K>, V> extends MyBinarySearchTree<K,V> {




    /**
     * Calculates the height of a node.
     *
     * @param node the node whose height will be calculated
     * @return the height of the node, or 0 if the node is null
     */
    private int height(Node<K, V> node) {
        return node != null ? node.height : 0;
    }


    /**
     * Calculates the balance factor of a node, which is the difference
     * between the heights of the left and right subtrees.
     *
     * @param node the node whose balance factor is to be calculated
     * @return the balance factor of the node, or 0 if the node is null
     */

    private int getBalanceFactor(Node<K, V> node) {
        return node != null ? height(node.left) - height(node.right) : 0;
    }


    /**
     * Public method to get the balance factor of a node.
     *
     * @param node the node for which to calculate the balance factor
     * @return the balance factor of the specified node
     */
    public int getBalance(Node<K, V> node) {
        return getBalanceFactor(node);
   }

    /**
     * Inserts a new node with the specified key and value into the AVL tree.
     * Balances the tree and updates heights after insertion.
     *
     * @param node the current node in the traversal (initially the root)
     * @param key  the key of the node to be inserted
     * @param value the value associated with the inserted key
     * @return the updated subtree root after insertion and balancing
     * @throws DuplicateKeyException if a node with the given key already exists
     */

    @Override
    protected Node<K, V> insert(Node<K, V> node, K key, V value) throws DuplicateKeyException {

        node = super.insert(node, key, value);


        updateHeight(node);


        return balance(node);
    }



    /**
     * Balances the AVL tree by checking the balance factor of the given node (subtree root).
     * Depending on the balance factor, it performs the necessary rotation to restore balance.
     * Handles four cases: Left-Left (LL), Left-Right (LR), Right-Right (RR), and Right-Left (RL).
     *
     * @param node the root node of the subtree to check for balancing
     * @return the new root of the balanced subtree
     */

    private Node<K, V> balance(Node<K, V> node) {
        int balanceFactor = getBalanceFactor(node);

        // Left-Left (LL) case
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left-Right (LR) case
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Right (RR) case
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right-Left (RL) case
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Deletes a node with the specified key from the AVL tree.
     * Balances the tree and updates heights after deletion.
     *
     * @param node the current node in the traversal (initially the root)
     * @param key the key of the node to be deleted
     * @return the updated subtree root after deletion and balancing
     */

    @Override
    protected Node<K, V> delete(Node<K, V> node, K key) {

        node = super.delete(node, key);

        if (node == null) {
            return null;
        }


        updateHeight(node);
        return balance(node);
    }





    /**
     * Performs a right rotation on the given subtree.
     * The left child becomes the new root of the subtree, and the original root becomes the right child.
     *
     * @param currentRoot the root node of the subtree to be rotated
     * @return the new root of the subtree after the right rotation
     */

    private Node<K, V> rotateRight(Node<K, V> currentRoot) {
        Node<K, V> newRoot = currentRoot.left;
        Node<K, V> middleSubtree = newRoot.right;

        // Perform rotation
        newRoot.right = currentRoot;
        currentRoot.left = middleSubtree;

        // Update heights
        updateHeight(currentRoot);
        updateHeight(newRoot);

        return newRoot;
    }



    /**
     * Performs a left rotation on the given subtree.
     * The right child becomes the new root of the subtree, and the original root becomes the left child.
     *
     * @param currentRoot the root node of the subtree to be rotated
     * @return the new root of the subtree after the left rotation
     */

    private Node<K, V> rotateLeft(Node<K, V> currentRoot) {
        Node<K, V> newRoot = currentRoot.right;
        Node<K, V> middleSubtree = newRoot.left;

        // Perform rotation
        newRoot.left = currentRoot;
        currentRoot.right = middleSubtree;

        // Update heights
        updateHeight(currentRoot);
        updateHeight(newRoot);


        return newRoot;
    }


    /**
     * Updates the height of the given node based on the heights of its left and right children.
     *
     * @param node the node whose height is to be updated
     */
    private void updateHeight(Node<K, V> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }







}
