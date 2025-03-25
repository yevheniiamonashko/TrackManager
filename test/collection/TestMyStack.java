package collection;

import custom_data_structures.MyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyStack {
    MyStack<String> stack;

    @BeforeEach
    public void setUp() {
        stack = new MyStack<>();
    }

    @Test
    public void GivenNewStack_WhenPushThreeElements_ThenSizeAndPeekAreCorrect() {
        stack.push("apple");
        stack.push("milk");
        stack.push("bread");
        stack.push("cheese");
        stack.push("butter");
        stack.push("eggs");
        stack.push("yogurt");

        assertEquals(7, stack.size());
        assertEquals("yogurt", stack.peek());
    }

    @Test
    public void GivenStackWithElements_WhenPopElement_ThenSizeAndPeekAreUpdatedCorrectly() {
        stack.push("apple");
        stack.push("milk");
        stack.push("bread");
        stack.push("cheese");
        stack.push("butter");

        assertEquals(5, stack.size());
        assertEquals("butter", stack.peek());


        assertEquals("butter", stack.pop());
        assertEquals(4, stack.size());
        assertEquals("cheese", stack.peek());

        assertEquals("cheese", stack.pop());
        assertEquals(3, stack.size());
        assertEquals("bread", stack.peek());
    }

    @Test
    public void GivenStackWithLargeNumberOfElements_WhenPushAndPopMultipleElements_ThenStackBehavesCorrectly() {

        for (int i = 1; i <= 100; i++) {
            stack.push("Item " + i);
        }

        assertEquals(100, stack.size());
        assertEquals("Item 100", stack.peek());


        for (int i = 100; i > 90; i--) {
            assertEquals("Item " + i, stack.pop());
        }
        assertEquals(90, stack.size());
        assertEquals("Item 90", stack.peek());
    }

    @Test
    public void GivenEmptyStack_WhenTryPopElement_ThenEmptyCollectionExceptionIsThrown() {
        assertTrue(stack.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> stack.pop());
    }

    @Test
    public void GivenEmptyStack_WhenTryPeekElement_ThenEmptyCollectionExceptionIsThrown() {
        assertTrue(stack.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> stack.peek());
    }
    @Test
    public void GivenNonEmptyStack_WhenUseGraphVizMethod_ThenElementsAreDisplayedCorrectly() {

        stack.push("1");
        stack.push("2");
        stack.push("3");


        String dotGraph = stack.graphViz("Stack");


        String expectedDot = """
                digraph "Stack" {
                rankdir=BT;   node [shape=record];
                  node0 [label="{ 3 }"];
                  node1 [label="{ 2 }"];
                  node2 [label="{ 1 }"];
                  node0 -> node1;
                  node1 -> node2;
                }
                """;


        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenNonEmptyStackWithOneElement_WhenUseGraphVizMethod_ThenElementIsDisplayedCorrectly() {

        stack.push("1");

        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());


        String dotGraph = stack.graphViz("Stack");


        String expectedDot = """
                digraph "Stack" {
                rankdir=BT;   node [shape=record];
                  node0 [label="{ 1 }"];
                }
                """;

        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenEmptyStack_WhenUseGraphVizMethod_ThenEmptyStateDisplayedCorrectly() {
        MyStack<Integer> stack = new MyStack<>();

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());


        String dotGraph = stack.graphViz("Stack");


        String expectedDot = """
                digraph "Stack" {
                  node [shape=plaintext];
                  emptyNode [label="Empty Stack"];
                }
                """;

        assertEquals(expectedDot, dotGraph);
    }


}
