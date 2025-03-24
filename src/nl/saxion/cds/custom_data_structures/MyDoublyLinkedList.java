package nl.saxion.cds.custom_data_structures;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.collection.SaxSearchable;
import nl.saxion.cds.collection.ValueNotFoundException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyDoublyLinkedList<V> implements SaxList<V>, SaxSearchable<V> {

    /**
     * Inner class for representing a node in a doubly linked list.
     * Each node stores a value and references to the previous and next nodes in the list.
     */
    private class DLNode {
        private V value;
        private DLNode prev;
        private DLNode next;

        public DLNode(V value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }

    }

    private DLNode head;
    private DLNode tail;
    private int size;


    public MyDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public int linearSearch(V element) {
        DLNode current = head;
        int index = 0;
        while (current != null) {
            if ((element == null && current.value == null) || (element != null && element.equals(current.value))) {
                return index;
            }
            current = current.next;
            index++;
        }
        return SaxSearchable.NOT_FOUND;
    }

    @Override
    public int binarySearch(Comparator<V> comparator, V element) {
        throw new UnsupportedOperationException("Doubly Linked List does not has binary search implementation.");
    }


    @Override
    public boolean contains(V value) {
        DLNode current = head;
        while (current != null) {
            if ((value == null && current.value == null) || (value != null && value.equals(current.value))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public V get(int index) throws IndexOutOfBoundsException {

        DLNode nodeAtIndex = getNodeAtSpecificIndex(index);
        return nodeAtIndex.value;
    }

    @Override
    public void addLast(V value) {
        DLNode newNode = new DLNode(value);
        if (head != null) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;

        } else {
            head = tail = newNode;
        }
        size++;

    }

    @Override
    public void addFirst(V value) {
        DLNode newNode = new DLNode(value);
        if (head != null) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else head = tail = newNode;
        size++;
    }


    @Override
    public void addAt(int index, V value) throws IndexOutOfBoundsException {

        if (index == 0) {
            addFirst(value);
        } else if (index == size) {
            addLast(value);
        } else {
            DLNode newNode = new DLNode(value);
            DLNode current = getNodeAtSpecificIndex(index);
            insertBetween(current.prev, current, newNode);
            size++;
        }
    }

    @Override
    public void set(int index, V value) throws IndexOutOfBoundsException {
        DLNode nodeAtIndex = getNodeAtSpecificIndex(index);
        nodeAtIndex.value = value;
    }

    @Override
    public V removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        V value = tail.value;
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return value;
    }

    @Override
    public V removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        V value = head.value;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return value;
    }

    @Override
    public V removeAt(int index) throws IndexOutOfBoundsException {

        DLNode node = getNodeAtSpecificIndex(index);
        V value = node.value;
        removeNode(node);
        return value;
    }

    @Override
    public void remove(V value) throws ValueNotFoundException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        DLNode current = head;
        while (current != null) {
            if ((value == null && current.value == null) || (value != null && value.equals(current.value))) {
                removeNode(current);
                return;
            }
            current = current.next;
        }
        throw new ValueNotFoundException("" + value);

    }

    @Override
    public Iterator<V> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<V> {
        private DLNode current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            V value = current.value;
            current = current.next;
            return value;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Retrieves the node at a specified index in the list.
     * Traversal in this method is optimized by selecting the nearest end of the list
     * (either the head or the tail) based on the index, minimizing the number of elements to traverse.
     * If the index is in the first half of the list, traversal starts from the head
     * and iterates forward.Otherwise, traversal starts from the tail and iterates backward.
     *
     * @param index the index of the node to retrieve
     * @return the node at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws EmptyCollectionException if the list is empty
     */

    private DLNode getNodeAtSpecificIndex(int index) {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        DLNode current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current;
    }

    /**
     * Inserts a new node between two specified nodes in the list.
     * Updates the `prev` and `next` references of the surrounding nodes to include the new node.
     *
     * @param previous the node after which the new node is to be inserted, or null if inserting at the head
     * @param next the node before which the new node is to be inserted, or null if inserting at the tail
     * @param newNode the new node to insert between `previous` and `next`
     */

    private void insertBetween(DLNode previous, DLNode next, DLNode newNode) {
        newNode.prev = previous;
        newNode.next = next;
        if (previous != null) {
            previous.next = newNode;
        }
        if (next != null) {
            next.prev = newNode;
        }
    }

    /**
     * Removes a specified node from the list.
     * If the node is the head, the method calls `removeFirst()` to remove it.
     * If the node is the tail, the method calls `removeLast()` to remove it.
     * Otherwise, the method updates the `next` reference of the previous node
     * and the `prev` reference of the next node to link them directly to each other,removing the specified node.
     *
     * @param node the node to be removed from the list
     */
    private void removeNode(DLNode node) {
        if (node == head) {
            removeFirst();
        } else if (node == tail) {
            removeLast();
        } else {
            DLNode prev = node.prev;
            DLNode next = node.next;
            prev.next = next;
            next.prev = prev;
            size--;
        }

    }
    /**
     * Creates a graphViz string representing the structure of the doubly linked list.
     * If the list is empty, the output shows a single "Empty List" node.
     * Otherwise, each node displays its value and links to the previous and next nodes.
     *
     * @param name the name of the GraphViz graph
     * @return a DOT format string representing the list
     */

    @Override
    public String graphViz(String name) {
        StringBuilder dot = new StringBuilder();

        if (isEmpty()) {
            dot.append("digraph \"").append(name).append("\" {\n");
            dot.append("  node [shape=plaintext];\n");
            dot.append("  emptyNode [label=\"Empty List\"];\n");
            dot.append("}\n");
            return dot.toString();
        }


        dot.append("digraph \"").append(name).append("\" {\n");
        dot.append("rankdir=LR; ");
        dot.append("  node [shape=record];\n");


        DLNode current = head;
        int nodeId = 0;


        while (current != null) {
            dot.append("  node").append(nodeId)
                    .append(" [label=\"{<prev> | ").append(current.value)
                    .append(" | <next> }\"];\n");
            nodeId++;
            current = current.next;
        }


        current = head;
        nodeId = 0;
        while (current != null && current.next != null) {
            dot.append("  node").append(nodeId)
                    .append(":next -> node").append(nodeId + 1)
                    .append(":prev;\n");

            dot.append("  node").append(nodeId + 1)
                    .append(":prev -> node").append(nodeId)
                    .append(":next;\n");

            nodeId++;
            current = current.next;
        }

        dot.append("}\n");

        return dot.toString();
    }
}
