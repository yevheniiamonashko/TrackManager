package collection;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxSearchable;
import nl.saxion.cds.collection.ValueNotFoundException;
import nl.saxion.cds.custom_data_structures.MyDoublyLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyDoublyLinkedList {
    private MyDoublyLinkedList<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new MyDoublyLinkedList<>();
    }


    @Test
    public void GivenNewDoublyLinkedList_WhenCheckingIfEmpty_ThenListIsEmpty() {

        assertTrue(list.isEmpty());

    }

    @Test
    public void GivenNewDoublyLinkedList_WhenCheckingIfContainsSpecificElement_ThenListDoesNotContainThisElement() {
        assertTrue(list.isEmpty());
        assertFalse(list.contains(1));
    }


    @Test
    public void GivenDoublyLinkedListWithElements_WhenCheckingIfEmpty_ThenListIsNotEmptyAndStoreAllElementsCorrectly() {

        list.addFirst(1);
        list.addAt(1, 2);
        list.addAt(2, 3);

        assertEquals(3, list.size());
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertTrue(list.contains(3));
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void GivenLargeNumberOfElements_WhenAddingToDoublyLinkedList_ThenTheAllElementsArePresentAndStoredCorrectly() {
        int numElements = 1000;


        for (int i = 1; i <= numElements; i++) {
            list.addLast(i);
        }
        assertEquals(1, list.get(0));
        assertEquals(numElements, list.get(numElements - 1));

        assertEquals(numElements, list.size());


        for (int i = 0; i < numElements; i++) {
            assertEquals(i + 1, list.get(i));
        }

    }

    @Test
    public void GivenListLargeNumberOfElements_WhenRemovingAllElementsSequentially_ThenTheListBecomesEmptyAndHeadUpdatesCorrectly() {
        int numElements = 1000;
        for (int i = 0; i < numElements; i++) {
            list.addLast(i);
        }
        assertEquals(numElements, list.size());
        assertEquals(0, list.get(0));
        assertEquals(numElements - 1, list.get(numElements - 1));


        for (int i = 0; i < numElements; i++) {
            assertEquals(i, list.removeFirst());

            if (i < numElements - 1) {
                assertEquals(i + 1, list.get(0));
            }
            assertEquals(numElements - i - 1, list.size());
        }
        assertTrue(list.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> list.get(0));
    }

    @Test
    public void GivenListWithLargeNumberOfElements_WhenIterating_ThenAllElementsAreAccessedInOrder() {
        int numElements = 1000;
        for (int i = 1; i <= numElements; i++) {
            list.addLast(i);
        }

        Iterator<Integer> iterator = list.iterator();
        int count = 1;
        while (iterator.hasNext()) {
            assertEquals(count, iterator.next());
            count++;
        }
        assertEquals(numElements, count - 1);
    }


    @Test
    public void GivenDoublyLinkedListSizeOfThree_WhenRemoveTheElement_ThenListSizeUpdatedCorrectly() {

        list.addFirst(1);
        list.addAt(1, 2);
        list.addAt(2, 3);


        assertEquals(3, list.size());
        list.remove(1);
        assertEquals(2, list.size());
    }

    @Test
    public void GivenDoublyLinkedListSizeOfThree_WhenRemoveAllElements_ThenListIsEmpty() {

        list.addFirst(1);
        list.addAt(1, 2);
        list.addAt(2, 3);


        assertEquals(3, list.size());

        list.remove(1);
        list.remove(2);
        list.remove(3);

        assertTrue(list.isEmpty());

    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenAddFirst_ThenTheHeadValueIsUpdatedCorrectly() {

        list.addFirst(1);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddFirst_ThenTheHeadValueIsUpdatedCorrectly() {

        list.addFirst(1);
        list.addAt(1, 2);


        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.addFirst(3);
        assertEquals(3, list.get(0));
        assertEquals(3, list.size());
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenAddLast_ThenTheTailValueIsUpdatedCorrectly() {

        list.addLast(1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(list.size() - 1));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddLast_ThenTheTailValueIsUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);


        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.addLast(3);
        assertEquals(3, list.get(list.size() - 1));
        assertEquals(3, list.size());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddAtSpecificIndex_ThenTheValuesAreUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);


        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.addAt(1, 5);
        assertEquals(1, list.get(0));
        assertEquals(5, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddAtIndexZero_ThenTheValuesAreUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);


        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.addAt(0, 5);
        assertEquals(5, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddAtIndexEqualToSize_ThenTheValuesAreUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.addAt(list.size(), 5);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(5, list.get(2));
        assertEquals("1, 2, 5", list.get(0) + ", " + list.get(1) + ", " + list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddAtIndexLessThanZero_ThenIndexOutOfBoundsExceptionIsThrown() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(-1, 5));
        assertEquals("Invalid index", exception.getMessage());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenAddAtIndexGreaterThanSize_ThenIndexOutOfBoundsExceptionIsThrown() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(3, 5));
        assertEquals("Invalid index", exception.getMessage());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenGetElementAtIndexEqualSize_ThenIndexOutOfBoundsExceptionIsThrown() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
        assertEquals("Invalid index", exception.getMessage());
    }

    @Test
    public void GivenDoublyLinkedListWithTwoElements_WhenRemoveTheHeadElement_ThenTheHeadValueUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.removeAt(0);
        assertEquals(2, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    public void GivenDoublyLinkedListWithOneElement_WhenRemoveTheLastElement_ThenTheValueSetToNull() {

        list.addFirst(1);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(1, list.size());

        Integer removedValue = list.removeLast();
        assertEquals(Integer.valueOf(1), removedValue);

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenRemoveTheLastElement_ThenEmptyCollectionExceptionIsThrown() {

        assertTrue(list.isEmpty());
        assertThrows(EmptyCollectionException.class, list::removeLast);
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenRemoveTheFirstElement_ThenEmptyCollectionExceptionIsThrown() {

        assertTrue(list.isEmpty());
        assertThrows(EmptyCollectionException.class, list::removeFirst);
    }

    @Test
    public void GivenDoublyLinkedListWithThreeElements_WhenRemoveTheTailElement_ThenTheTailValueUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list.size());

        list.removeAt(list.size() - 1);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.get(list.size() - 1));
        assertEquals(2, list.size());
    }

    @Test
    public void GivenDoublyLinkedListWithFiveElements_WhenRemoveElementsAtIndex1AndIndex3_ThenSizeUpdatedAndElementsPlacedCorrectly() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.addLast(5);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
        assertEquals(5, list.size());

        list.removeAt(1);
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(4, list.get(2));
        assertEquals(5, list.get(3));
        assertEquals(4, list.size());

        list.removeAt(3);
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(4, list.get(2));
        assertEquals(3, list.size());
        assertEquals("1, 3, 4", list.get(0) + ", " + list.get(1) + ", " + list.get(2));
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenTryRemoveElement_ThenEmptyCollectionExceptionIsThrown() {

        assertTrue(list.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> list.remove(1));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenTryRemovePresentedElement_ThenValueIsRemovedAndElementsPlacedCorrectly() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list.size());

        list.remove(3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenTryRemoveNonExistentElement_ThenValueNotFoundExceptionIsThrown() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);

        assertFalse(list.isEmpty());
        ValueNotFoundException exception = assertThrows(ValueNotFoundException.class, () -> list.remove(4));
        assertEquals("Value \"" + 4 + "\" is not found.", exception.getMessage());
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenTrySetElement_ThenEmptyCollectionExceptionIsThrown() {

        assertTrue(list.isEmpty());
        assertThrows(EmptyCollectionException.class, () -> list.set(0, 2));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenTrySetElementAtNegativeIndex_ThenIndexOutOfBoundsExceptionIsThrown() {

        list.addFirst(1);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(1, list.size());

        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, 2));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenTrySetElementWithIndexGreaterThanSize_ThenIndexOutOfBoundsExceptionIsThrown() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, 4));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenTrySetElementWithIndexEqualToSize_ThenIndexOutOfBoundsExceptionIsThrown() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        assertThrows(IndexOutOfBoundsException.class, () -> list.set(2, 3));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenTrySetElementAtValidIndex_ThenElementAtSpecifiedIndexIsUpdatedCorrectly() {

        list.addFirst(1);
        list.addLast(2);

        assertFalse(list.isEmpty());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(2, list.size());

        list.set(1, 3);
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenIterating_ThenElementsReturnedInOrder() {

        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        Iterator<Integer> iterator = list.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenCheckingHasNext_ThenReturnsFalse() {

        Iterator<Integer> iterator = list.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenCallingNext_ThenNoSuchElementExceptionIsThrown() {

        Iterator<Integer> iterator = list.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void GivenNonEmptyDoublyLinkedListWithNullAndNonNullValues_WhenCheckIfListContainsNull_ThenReturnsTrue() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(null);

        assertFalse(list.isEmpty());
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertNull(list.get(2));
        assertTrue(list.contains(null));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedListWithOneNullAndNonNullValues_WhenRemoveNullElement_ThenTheElementCorrectlyRemoved() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(null);

        assertFalse(list.isEmpty());
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertNull(list.get(2));
        assertTrue(list.contains(null));
        list.remove(null);
        assertEquals(2, list.size());
        assertFalse(list.contains(null));
    }

    @Test
    public void GivenNonEmptyDoublyLinkedListWithNullAndNonNullValues_WhenRemoveNullElement_ThenFirstNullElementIsRemoved() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(null);
        list.addLast(null);


        assertFalse(list.isEmpty());
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertNull(list.get(2));
        assertNull(list.get(3));
        assertTrue(list.contains(null));



        list.remove(null);
        assertEquals(3, list.size());
        assertNull(list.get(2));
        assertTrue(list.contains(null));
    }

    @Test
    public void GivenDoublyLinkedList_WhenSearchingForExistingElementUsingLinearSearch_ThenCorrectIndexIsReturned() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addLast(40);


        assertEquals(0, list.linearSearch(10));
        assertEquals(1, list.linearSearch(20));
        assertEquals(2, list.linearSearch(30));
        assertEquals(3, list.linearSearch(40));
    }

    @Test
    public void GivenDoublyLinkedList_WhenSearchingForNonExistingElementUsingLinearSearch_ThenNotFoundIsReturned() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);


        assertEquals(SaxSearchable.NOT_FOUND, list.linearSearch(40));
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenSearchingUsingLinearSearch_ThenNotFoundIsReturned() {

        assertEquals(SaxSearchable.NOT_FOUND, list.linearSearch(10));
    }

    @Test
    public void GivenDoublyLinkedList_WhenSearchingForNullElementUsingLinearSearch_ThenCorrectIndexIsReturned() {
        list.addLast(10);
        list.addLast(null);
        list.addLast(20);


        assertEquals(1, list.linearSearch(null));
    }


    @Test
    public void GivenDoublyLinkedListWithSeveralNullValues_WhenSearchingForNullElementUsingLinearSearch_ThenIndexOfFirstAppearedNullValueIsReturned() {
        list.addLast(10);
        list.addLast(null);
        list.addLast(20);
        list.addLast(30);
        list.addLast(null);
        list.addLast(40);
        list.addLast(50);
        list.addLast(null);
        list.addLast(60);


        assertEquals(1, list.linearSearch(null));
    }


    @Test
    public void GivenDoublyLinkedList_WhenUsingBinarySearch_ThenUnsupportedOperationExceptionIsThrown() {
        for (int i = 1; i <= 50; i++) {
            list.addLast(i);
        }


        Comparator<Integer> comparator = Integer::compareTo;


        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> list.binarySearch(comparator, 20));

        assertEquals("Doubly Linked List does not has binary search implementation.", exception.getMessage());
    }


    @Test
    public void GivenNonEmptyDoublyLinkedList_WhenUseGraphVizMethod_ThenElementsAreDisplayedCorrectly() {

        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);

        assertFalse(list.isEmpty());
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));

        String dotGraph = list.graphViz("DoublyLinkedList");

        String expectedDot = """
                digraph "DoublyLinkedList" {
                rankdir=LR;   node [shape=record];
                  node0 [label="{<prev> | 1 | <next> }"];
                  node1 [label="{<prev> | 2 | <next> }"];
                  node2 [label="{<prev> | 3 | <next> }"];
                  node0:next -> node1:prev;
                  node1:prev -> node0:next;
                  node1:next -> node2:prev;
                  node2:prev -> node1:next;
                }
                """;
        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenNonEmptyDoublyLinkedListWithOneElement_WhenUseGraphVizMethod_ThenElementIsDisplayedCorrectly() {

        list.addFirst(1);

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));

        String dotGraph = list.graphViz("DoublyLinkedList");

        String expectedDot = """
                digraph "DoublyLinkedList" {
                rankdir=LR;   node [shape=record];
                  node0 [label="{<prev> | 1 | <next> }"];
                }
                """;


        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenEmptyDoublyLinkedList_WhenUseGraphVizMethod_ThenEmptyStateDisplayedCorrectly() {


        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        String dotGraph = list.graphViz("DoublyLinkedList");

        String expectedDot = """
                digraph "DoublyLinkedList" {
                  node [shape=plaintext];
                  emptyNode [label="Empty List"];
                }
                """;


        assertEquals(expectedDot, dotGraph);
    }


}
