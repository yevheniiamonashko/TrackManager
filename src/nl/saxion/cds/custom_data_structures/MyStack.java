package nl.saxion.cds.custom_data_structures;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxStack;

public class MyStack<V> implements SaxStack<V> {
    /**
     * A doubly linked list is used as underlying structure for stack implementation.
     **/
    private final MyDoublyLinkedList<V> list;

    public MyStack() {
        list = new MyDoublyLinkedList<>();
    }

    /**
     * Checks if the stack is empty.
     *
     * @return {@code true} if the stack is empty, {@code false} otherwise.
     */

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return The size of the stack.
     */

    @Override
    public int size() {
        return list.size();
    }

    /**
     * This method generates a GraphViz DOT format representation of the stack.
     * This representation shows the elements from top to bottom.
     * If the stack is empty, it displays a single node indicating an empty stack.
     *
     * @param name The name of the GraphViz graph.
     * @return A DOT format string representing the stack's structure.
     */

    @Override
    public String graphViz(String name) {

        StringBuilder dot = new StringBuilder();

        if (isEmpty()) {
            dot.append("digraph \"").append(name).append("\" {\n");
            dot.append("  node [shape=plaintext];\n");
            dot.append("  emptyNode [label=\"Empty Stack\"];\n");
            dot.append("}\n");
            return dot.toString();
        }

        dot.append("digraph \"").append(name).append("\" {\n");
        dot.append("rankdir=BT; ");  // Bottom-to-top direction for the stack visualization
        dot.append("  node [shape=record];\n");

        int nodeId = 0;

        // Iteration in reverse order, from top of the stack to bottom
        for (int i = list.size() - 1; i >= 0; i--) {
            dot.append("  node").append(nodeId)
                    .append(" [label=\"{ ").append(list.get(i))
                    .append(" }\"];\n");
            nodeId++;
        }

        // Creation of edges between nodes
        for (int i = 0; i < nodeId - 1; i++) {
            dot.append("  node").append(i)
                    .append(" -> node").append(i + 1)
                    .append(";\n");
        }

        dot.append("}\n");

        return dot.toString();
    }

    /**
     * Pushes an element onto the stack. The element is added to the end of the doubly linked list.
     *
     * @param value The element to push onto the stack.
     */

    @Override
    public void push(V value) {
        list.addLast(value);

    }

    /**
     * Removes and returns the element at the top of the stack (last element in doubly linked list).
     *
     * @return The element removed from the top of the stack.
     * @throws EmptyCollectionException if the stack is empty.
     */

    @Override
    public V pop() throws EmptyCollectionException {
        if (list.isEmpty()) {
            throw new EmptyCollectionException();
        }
        return list.removeLast();
    }

    /**
     * Returns the element at the top of the stack (the last element in the doubly linked list).
     *
     * @return The element at the top of the stack.
     * @throws EmptyCollectionException if the stack is empty.
     */

    @Override
    public V peek() throws EmptyCollectionException {
        if (list.isEmpty()) {
            throw new EmptyCollectionException();
        }

        return list.get(list.size() - 1);
    }
}
