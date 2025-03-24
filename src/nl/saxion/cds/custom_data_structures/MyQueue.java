package nl.saxion.cds.custom_data_structures;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxQueue;

import java.util.Iterator;

public class MyQueue<V> implements SaxQueue<V> {
    /**
     * A doubly linked list is used as underlying structure for queue implementation.
     **/
    private  final MyDoublyLinkedList<V> list;

   public MyQueue(){
       list= new MyDoublyLinkedList<>();
   }

    /**
     * Checks if the queue is empty.
     *
     * @return {@code true} if the queue is empty, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return The size of the queue.
     */
    @Override
    public int size() {
        return list.size();
    }


    /**
     * This method generates a GraphViz DOT format representation of the queue.
     * This representation shows the elements from left to right.
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
            dot.append("  emptyNode [label=\"Empty Queue\"];\n");
            dot.append("}\n");
            return dot.toString();
        }

        dot.append("digraph \"").append(name).append("\" {\n");
        dot.append("rankdir=LR; ");  // left-to-right direction for the queue
        dot.append("  node [shape=record];\n");

        // Use the iterator to traverse the elements in the queue
        Iterator<V> iterator = list.iterator();
        int nodeId = 0;

        // Node creation for each element in the queue
        while (iterator.hasNext()) {
            V currentValue = iterator.next();
            dot.append("  node").append(nodeId)
                    .append(" [label=\"{ ").append(currentValue)
                    .append(" }\"];\n");
            nodeId++;
        }

        // Creation of links between nodes
        for (int i = 0; i < nodeId - 1; i++) {
            dot.append("  node").append(i)
                    .append(" -> node").append(i + 1)
                    .append(";\n");
        }

        dot.append("}\n");

        return dot.toString();
    }

    /**
     * Adds an element to the end of the queue.
     *
     * @param value The element to be added to the queue.
     */
    @Override
    public void enqueue(V value) {
        list.addLast(value);
    }

    /**
     * Removes and returns the first element in the queue.
     *
     * @return The element at the front of the queue.
     * @throws EmptyCollectionException if the queue is empty.
     */
    @Override
    public V dequeue() throws EmptyCollectionException {
        if (list.isEmpty()) {
            throw new EmptyCollectionException();
        }
        return list.removeFirst();
    }

    /**
     * Returns the element at the front of the queue.
     *
     * @return The element at the front of the queue.
     * @throws EmptyCollectionException if the queue is empty.
     */
    @Override
    public V peek() throws EmptyCollectionException {
        if (list.isEmpty()) {
            throw new EmptyCollectionException();
        }
        return list.get(0);
    }
}
