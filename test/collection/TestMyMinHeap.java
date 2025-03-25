package collection;

import custom_data_structures.MyMinHeap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyMinHeap {

    private MyMinHeap<Integer> heap;

    @BeforeEach
    public void setUp() {
        heap = new MyMinHeap<>();
    }

    private void setUpBasicHeap() {
        heap.enqueue(10);
        heap.enqueue(20);
        heap.enqueue(30);
        heap.enqueue(40);
        heap.enqueue(50);
        heap.enqueue(5);
    }


    @Test
    public void GivenEmptyHeap_WhenCheckIfIsEmpty_ThenTheTrueValueReturned() {
        assertTrue(heap.isEmpty());
    }

    @Test
    public void GivenNonEmptyHeap_WhenAddElements_ThenTheElementsPresentedInTheHeap() {
        setUpBasicHeap();
        assertFalse(heap.isEmpty());
        assertEquals(6, heap.size());
        assertEquals(5, heap.peek());
    }

    @Test
    public void GivenNonEmptyHeap_WhenTryingToDequeue_ThenTheInitialRootElementIsRemovedFromTheHeap() {
        setUpBasicHeap();
        assertFalse(heap.isEmpty());
        assertEquals(6, heap.size());
        assertEquals(5, heap.dequeue());

        assertEquals(5, heap.size());
        assertEquals(10, heap.peek());
    }

    @Test
    public void GivenHeapWithOneElement_WhenTryingToDequeue_ThenTheElementIsReturnedAndTheHeapIsEmpty() {
        heap.enqueue(10);
        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());

        assertEquals(10, heap.dequeue());
        assertTrue(heap.isEmpty());
    }

    @Test
    public void GivenHeap_WhenPercolateUp_ThenSmallestElementAtTop() {
        heap.enqueue(30);
        heap.enqueue(20);
        heap.enqueue(10);
        assertEquals(10, heap.peek());
    }

    @Test
    public void GivenHeap_WhenPercolateDown_ThenHeapPropertyMaintainedAfterRemoval() {
        heap.enqueue(10);
        heap.enqueue(20);
        heap.enqueue(30);
        assertEquals(10, heap.dequeue());
        assertEquals(20, heap.peek());
    }

    @Test
    public void GivenHeap_WhenEnqueueAndDequeueManyElements_ThenHeapStructureMaintainsCorrectly() {
        for (int i = 1; i <= 1000; i++) {
            heap.enqueue(i);
        }
        assertEquals(1, heap.peek());

        for (int i = 1; i <= 1000; i++) {
            assertEquals(i, heap.dequeue());
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    public void GivenEmptyHeap_WhenGraphViz_TheNullLabelIsShown() {
        assertTrue(heap.isEmpty());
        String graphDot = heap.graphViz("EmptyMinHeap");
        String expectedGraph = """
                digraph EmptyMinHeap {
                    null [shape=point];
                }
                """;
        assertEquals(expectedGraph, graphDot);
    }

    @Test
    public void GivenNonEmptyHeap_WhenGraphViz_TheAllElementsDisplayedCorrectly() {
        setUpBasicHeap();
        String graphDot = heap.graphViz("MinHeap");

        String expectedGraph = """
                digraph MinHeap {
                    "0" [label="5"];
                    "0" -> "1";
                    "1" [label="20"];
                    "1" -> "3";
                    "3" [label="40"];
                    "1" -> "4";
                    "4" [label="50"];
                    "0" -> "2";
                    "2" [label="10"];
                    "2" -> "5";
                    "5" [label="30"];
                }
                """;
        assertEquals(expectedGraph, graphDot);
    }


    @Test
    public void GivenNonEmptyHeap_WhenUseInvalidChildIndexForGetParentIndex_ThenTheIllegalArgumentExceptionIsThrown() throws NoSuchMethodException {
        setUpBasicHeap();
        Method getParentIndex = MyMinHeap.class.getDeclaredMethod("getParentIndex", int.class);
        getParentIndex.setAccessible(true);
        int invalidIndex = -1;
        int invalidIndex1 = heap.size();

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> getParentIndex.invoke(heap, invalidIndex));
        InvocationTargetException exception1 = assertThrows(InvocationTargetException.class, () -> getParentIndex.invoke(heap, invalidIndex1));


        assertEquals("Invalid child index: " + invalidIndex, exception.getTargetException().getMessage());
        assertEquals("Invalid child index: " + invalidIndex1, exception1.getTargetException().getMessage());

    }

    @Test
    public void GivenNonEmptyHeap_WhenUseInvalidParentIndexForGetLeftChildIndex_ThenTheIllegalArgumentExceptionIsThrown() throws NoSuchMethodException {
        setUpBasicHeap();
        Method getLeftChildIndex = MyMinHeap.class.getDeclaredMethod("getLeftChildIndex", int.class);
        getLeftChildIndex.setAccessible(true);
        int invalidIndex = -1;
        int invalidIndex1 = heap.size();

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> getLeftChildIndex.invoke(heap, invalidIndex));
        InvocationTargetException exception1 = assertThrows(InvocationTargetException.class, () -> getLeftChildIndex.invoke(heap, invalidIndex1));


        assertEquals("Invalid parent index: " + invalidIndex, exception.getTargetException().getMessage());
        assertEquals("Invalid parent index: " + invalidIndex1, exception1.getTargetException().getMessage());

    }

    @Test
    public void GivenNonEmptyHeap_WhenUseInvalidParentIndexForGetRightChildIndex_ThenTheIllegalArgumentExceptionIsThrown() throws NoSuchMethodException {
        setUpBasicHeap();
        Method getRightChildIndex = MyMinHeap.class.getDeclaredMethod("getRightChildIndex", int.class);
        getRightChildIndex.setAccessible(true);
        int invalidIndex = -1;
        int invalidIndex1 = heap.size();

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> getRightChildIndex.invoke(heap, invalidIndex));
        InvocationTargetException exception1 = assertThrows(InvocationTargetException.class, () -> getRightChildIndex.invoke(heap, invalidIndex1));


        assertEquals("Invalid parent index: " + invalidIndex, exception.getTargetException().getMessage());
        assertEquals("Invalid parent index: " + invalidIndex1, exception1.getTargetException().getMessage());

    }


    @Test
    public void GivenEmptyHeap_WhenTryingToPeekTheElement_ThenTheEmptyCollectionExceptionIsThrown() {
        assertThrows(EmptyCollectionException.class, () -> heap.peek());
    }

    @Test
    public void GivenEmptyHeap_WhenTryingToDequeue_ThenTheEmptyCollectionExceptionIsThrown() {
        assertThrows(EmptyCollectionException.class, () -> heap.dequeue());
    }
}
