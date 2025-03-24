package collection;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.custom_data_structures.MyQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyQueue {
    MyQueue<String> queue;

    @BeforeEach
    public void setUp() {
        queue = new MyQueue<>();
    }

    @Test
    public void GivenNewQueue_WhenEnqueueThreeElements_ThenSizeAndPeekAreCorrect() {

        queue.enqueue("apple");
        queue.enqueue("milk");
        queue.enqueue("bread");
        queue.enqueue("butter");
        queue.enqueue("cheese");
        queue.enqueue("jam");
        queue.enqueue("orange");
        queue.enqueue("grape");

        assertEquals(8, queue.size());
        assertEquals("apple", queue.peek());
    }

    @Test
    public void GivenQueueWithElements_WhenDequeueElement_ThenSizeAndPeekAreUpdatedCorrectly() {
        queue.enqueue("apple");
        queue.enqueue("milk");
        queue.enqueue("bread");
        queue.enqueue("butter");
        queue.enqueue("cheese");
        queue.enqueue("jam");
        queue.enqueue("orange");
        queue.enqueue("grape");

        assertEquals(8, queue.size());
        assertEquals("apple", queue.peek());

        assertEquals("apple", queue.dequeue());
        assertEquals(7, queue.size());
        assertEquals("milk", queue.peek());

        assertEquals("milk", queue.dequeue());
        assertEquals(6, queue.size());
        assertEquals("bread", queue.peek());
    }

    @Test
    public void GivenEmptyQueue_WhenTryDequeueElement_ThenEmptyCollectionExceptionIsThrown() {
        assertTrue(queue.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> queue.dequeue());
    }

    @Test
    public void GivenEmptyQueue_WhenTryPeekElement_ThenEmptyCollectionExceptionIsThrown() {
        assertTrue(queue.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> queue.peek());
    }
    @Test
    public void GivenNonEmptyQueue_WhenUseGraphVizMethod_ThenElementsAreDisplayedCorrectly() {



        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");


        String dotGraph = queue.graphViz("Queue");


        String expectedDot = """
                digraph "Queue" {
                rankdir=LR;   node [shape=record];
                  node0 [label="{ 1 }"];
                  node1 [label="{ 2 }"];
                  node2 [label="{ 3 }"];
                  node0 -> node1;
                  node1 -> node2;
                }
                """;


        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenNonEmptyQueueWithOneElement_WhenUseGraphVizMethod_ThenElementIsDisplayedCorrectly() {



        queue.enqueue("1");


        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());


        String dotGraph = queue.graphViz("Queue");


        String expectedDot = """
                digraph "Queue" {
                rankdir=LR;   node [shape=record];
                  node0 [label="{ 1 }"];
                }
                """;


        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenEmptyQueue_WhenUseGraphVizMethod_ThenEmptyStateDisplayedCorrectly() {
        MyQueue<Integer> queue = new MyQueue<>();


        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());


        String dotGraph = queue.graphViz("Queue");


        String expectedDot = """
                digraph "Queue" {
                  node [shape=plaintext];
                  emptyNode [label="Empty Queue"];
                }
                """;


        assertEquals(expectedDot, dotGraph);
    }

}