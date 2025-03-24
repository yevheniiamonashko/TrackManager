package nl.saxion.cds.custom_data_structures;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxHeap;

public class MyMinHeap<V extends Comparable<V>> implements SaxHeap<V> {

    private final MyArrayList<V> elements;

    private final int IS_LEAF_INDEX = -1;

    public MyMinHeap() {
        elements = new MyArrayList<>();
    }

    /**
     * Checks if the heap is empty.
     *
     * @return true if the heap is empty; false otherwise.
     */

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public int size() {
        return elements.size();
    }



    @Override
    public void enqueue(V value) {
      elements.addLast(value);
      percolateUp(elements.size() - 1);
    }

    /**
     * Removes and returns the minimum element from the heap (root element).
     *
     * @return the minimum element in the heap.
     * @throws EmptyCollectionException if the heap is empty.
     */

    @Override
    public V dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        V rootElement = elements.get(0);
        if(elements.size() > 1) {
            elements.set(0, elements.removeLast());
            percolateDown(0);
        } else {
            elements.removeLast();
        }
        return rootElement;

    }
    /**
     * Returns the minimum element of the heap (the root element in the min heap).
     *
     * @return the minimum element in the heap.
     * @throws EmptyCollectionException if the heap is empty.
     */

    @Override
    public V peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        return elements.get(0);
    }

    /**
     * Moves the element at the specified index up the heap to restore the min-heap property.
     * This operation is called after insertion to ensure the smallest element is placed at the root.
     * The process compares the current element with its parent.
     * If the current element is smaller
     * than its parent, they are swapped, and the comparison continues recursively up the heap
     * until the min-heap property is restored or the root is reached.
     *
     * @param index the index of the element to move up in the heap.
     */
    private void percolateUp(int index) {
        if (index == 0) return;
        int parentIndex = getParentIndex(index);
        V indexValue = elements.get(index);
        V parentValue = elements.get(parentIndex);
        if (indexValue.compareTo(parentValue) >= 0) return;
        elements.set(parentIndex, indexValue);
        elements.set(index, parentValue);
        percolateUp(parentIndex);

    }

    /**
     * Moves the element at the specified index down the heap to restore the min-heap property.
     * This operation is called after removing the root element to ensure the smallest element
     * stored at the root and the heap structure is maintained.

     * The process compares the current element with its children.
     * If the current element is greater
     * than the smallest of its children, it is swapped with that child.
     * This comparison continues
     * recursively down the heap until the min-heap property is restored or a leaf node is reached.
     *
     * @param index the index of the element to move down in the heap.
     *              This is initially the index of the new root after removal.
     */

    private void percolateDown(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        if (leftChildIndex == IS_LEAF_INDEX) {
            return;
        }

        V indexValue = elements.get(index);
        V leftChildValue = elements.get(leftChildIndex);

        int rightChildIndex = getRightChildIndex(index);
        int smallestIndex = leftChildIndex;
        V smallestValue = leftChildValue;


        if (rightChildIndex != IS_LEAF_INDEX) {
            V rightChildValue = elements.get(rightChildIndex);
            if (rightChildValue.compareTo(leftChildValue) < 0) {
                smallestIndex = rightChildIndex;
                smallestValue = rightChildValue;
            }
        }


        if (indexValue.compareTo(smallestValue) > 0) {
            elements.set(smallestIndex, indexValue);
            elements.set(index, smallestValue);
            percolateDown(smallestIndex);
        }
    }



    /**
     * Gets the parent index for the given child index.
     *
     * @param childIndex the index of the child.
     * @return the index of the parent.
     * @throws IllegalArgumentException if the childIndex is less than 1 or out of bounds.
     */

    private int getParentIndex(int childIndex) {
        if (childIndex < 1 || childIndex >= size()) {
            throw new IllegalArgumentException("Invalid child index: " + childIndex);
        }
        return ((childIndex - 1) / 2);

    }

    /**
     * Gets the left child index for the given parent index.
     *
     * @param parentIndex the index of the parent.
     * @return the index of the left child, or IS_LEAF_INDEX if there is no left child.
     * @throws IllegalArgumentException if the parentIndex is out of bounds.
     */

    private int getLeftChildIndex(int parentIndex) {
        if (parentIndex < 0 || parentIndex >= size()) {
            throw new IllegalArgumentException("Invalid parent index: " + parentIndex);
        }
        int leftChildIndex = 2 * parentIndex + 1;
        return leftChildIndex >= size() ? IS_LEAF_INDEX : leftChildIndex;
    }


    /**
     * Gets the right child index for the given parent index.
     *
     * @param parentIndex the index of the parent.
     * @return the index of the right child, or IS_LEAF_INDEX if there is no right child.
     * @throws IllegalArgumentException if the parentIndex is out of bounds.
     */

    private int getRightChildIndex(int parentIndex) {
        if (parentIndex < 0 || parentIndex >= size()) {
            throw new IllegalArgumentException("Invalid parent index: " + parentIndex);
        }
        int rightChildIndex = 2 * parentIndex + 2;
        return rightChildIndex >= size() ? IS_LEAF_INDEX : rightChildIndex;
    }

    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ").append(name).append(" {\n");


        if (isEmpty()) {
            sb.append("    null [shape=point];\n");
        } else {
            buildGraphViz(0, sb);
        }

        sb.append("}\n");
        return sb.toString();
    }

    private void buildGraphViz(int index, StringBuilder sb) {
        if (index >= size()) {
            return;
        }

        sb.append("    \"").append(index).append("\" [label=\"").append(elements.get(index)).append("\"];\n");


        int leftChildIndex = getLeftChildIndex(index);
        if (leftChildIndex != IS_LEAF_INDEX) {
            sb.append("    \"").append(index).append("\" -> \"").append(leftChildIndex).append("\";\n");
            buildGraphViz(leftChildIndex, sb);
        }

        int rightChildIndex = getRightChildIndex(index);
        if (rightChildIndex != IS_LEAF_INDEX) {
            sb.append("    \"").append(index).append("\" -> \"").append(rightChildIndex).append("\";\n");
            buildGraphViz(rightChildIndex, sb);
        }
    }

}
