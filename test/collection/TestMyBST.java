package collection;

import nl.saxion.cds.collection.DuplicateKeyException;
import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.custom_data_structures.MyArrayList;
import nl.saxion.cds.custom_data_structures.MyBinarySearchTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyBST {


    MyBinarySearchTree<Integer, String> tree;
    MyBinarySearchTree<String, String> wordTree;

    @BeforeEach
    public void setUp() {
        tree = new MyBinarySearchTree<>();
        wordTree = new MyBinarySearchTree<>();
    }


    private void setUpStandardTree() {
        tree.add(10, "Ten");
        tree.add(5, "Five");
        tree.add(20, "Twenty");
    }


    private void setUpComplexTree() {
        tree.add(10, "Ten");
        tree.add(5, "Five");
        tree.add(15, "Fifteen");
        tree.add(3, "Three");
        tree.add(7, "Seven");
        tree.add(12, "Twelve");
        tree.add(18, "Eighteen");
    }

    private void setUpWordTree() {
        wordTree.add("apple", "Apple");
        wordTree.add("apricot", "Apricot");
        wordTree.add("applet", "Applet");
        wordTree.add("application", "Application");
        wordTree.add("apply", "Apply");


        wordTree.add("cat", "Cat");
        wordTree.add("caterpillar", "Caterpillar");
        wordTree.add("car", "Car");
        wordTree.add("carbon", "Carbon");
        wordTree.add("carnation", "Carnation");
        wordTree.add("cargo", "Cargo");


        wordTree.add("blueberry", "Blueberry");
        wordTree.add("black", "Black");
        wordTree.add("blossom", "Blossom");
        wordTree.add("blanket", "Blanket");
        wordTree.add("blister", "Blister");


        wordTree.add("grape", "Grape");
        wordTree.add("grass", "Grass");
        wordTree.add("green", "Green");
        wordTree.add("gradient", "Gradient");
        wordTree.add("graphite", "Graphite");
        wordTree.add("gravity", "Gravity");


        wordTree.add("ant", "Ant");
        wordTree.add("antelope", "Antelope");
        wordTree.add("antenna", "Antenna");
        wordTree.add("angle", "Angle");
        wordTree.add("answer", "Answer");


        wordTree.add("bus", "Bus");
        wordTree.add("butter", "Butter");
        wordTree.add("buffalo", "Buffalo");
        wordTree.add("bubble", "Bubble");
        wordTree.add("building", "Building");
        wordTree.add("bulb", "Bulb");


    }

    @Test
    public void GivenTreeWithStringKeys_WhenGetByPrefixMatchesMultiplyElements_ThenTheListWithAllValuesIsReturned() {
       setUpWordTree();
        MyArrayList<String> result = wordTree.getByPrefix("ap");
        assertEquals(5, result.size());
        assertTrue(result.contains("Apple"));
        assertTrue(result.contains("Apricot"));
        assertTrue(result.contains("Applet"));
        assertTrue(result.contains("Application"));
        assertTrue(result.contains("Apply"));

    }

    @Test
    public void GivenTreeWithStringKeys_WhenGetByPrefixMatchesOnlyOneElement_ThenTheListWithOneElementIsReturned() {
       setUpWordTree();
        MyArrayList<String> result = wordTree.getByPrefix("graph");
        assertEquals(1, result.size());
        assertTrue(result.contains("Graphite"));


    }

    @Test
    public void GivenTreeWithStringKeys_WhenGetByPrefixDoesNotMatchAnyElement_ThenTheEmptyListIsReturned() {
        setUpWordTree();
        MyArrayList<String> result = wordTree.getByPrefix("wi");
        assertTrue(result.isEmpty());


    }
     @Test
    public void GivenEmptyTreeWithStringKeys_WhenGetByPrefix_ThenTheEmptyListIsReturned() {

        MyArrayList<String> result = wordTree.getByPrefix("a");
        assertTrue(result.isEmpty());


    }




    @Test
    public void GivenEmptyTree_WhenAddingUniqueKeys_TheCorrectValuesAreReturned() {
        setUpStandardTree();
        assertEquals("Ten", tree.get(10));
        assertEquals("Twenty", tree.get(20));
        assertEquals("Five", tree.get(5));
    }

    @Test
    public void GivenEmptyTree_WhenCheckingIfTreeIsEmpty_TheTrueIsReturned() {
        assertTrue(tree.isEmpty());
    }

    @Test
    public void GivenNonEmptyTree_WhenCheckingIfTreeIsEmpty_TheFalseIsReturned() {
        tree.add(10, "Ten");
        assertFalse(tree.isEmpty());
    }

    @Test
    public void GivenNonEmptyTree_WhenCheckingIfTreeContainsAddedKey_TheTrueIsReturned() {
        setUpStandardTree();
        assertTrue(tree.contains(10));
    }

    @Test
    public void GivenNonEmptyTree_WhenInsertNewNonDuplicateValue_TheTreeContainsAddedElementAndSizeCorrectlyIncreased() {
        setUpStandardTree();
        assertEquals(3, tree.size());
        tree.add(15, "Fifteen");
        assertEquals("Fifteen", tree.get(15));
        assertEquals(4, tree.size());
    }

    @Test
    public void GivenNonEmptyTree_WhenRemovingPresentElement_TheElementIsRemovedAndTheCorrectValueIsReturned() {
        setUpStandardTree();
        assertEquals(3, tree.size());
        assertEquals("Five", tree.remove(5));
        assertEquals(2, tree.size());
        assertFalse(tree.contains(5));
    }

    @Test
    public void GivenNonEmptyTree_WhenGettingTheListOfKeys_TheElementsInListPlacedInOrder() {
        setUpStandardTree();
        MyArrayList<Integer> keys = tree.getKeys();
        assertEquals(3, keys.size());
        assertEquals(5, keys.get(0));
        assertEquals(10, keys.get(1));
        assertEquals(20, keys.get(2));
    }

    @Test
    public void GivenEmptyTree_WhenGettingTheListOfKeys_TheEmptyListIsReturned() {
        MyArrayList<Integer> keys = tree.getKeys();
        assertEquals(0, keys.size());
    }

    @Test
    public void GivenNonEmptyTree_WhenDeletingLeafNode_TheTreeRemovesTheNodeCorrectly() {
        setUpComplexTree();
        assertTrue(tree.contains(3));
        assertEquals("Three", tree.remove(3));
        assertFalse(tree.contains(3));
        assertEquals(6, tree.size());
    }

    @Test
    public void GivenNonEmptyTree_WhenDeletingNodeWithOneChild_TheTreeRemovesTheNodeCorrectly() {
        setUpComplexTree();
        tree.add(13, "Thirteen");
        assertEquals("Twelve", tree.remove(12));
        assertFalse(tree.contains(12));
        assertTrue(tree.contains(13));
        assertEquals(7, tree.size());
    }

    @Test
    public void GivenNonEmptyTree_WhenDeletingNodeWithTwoChildren_TheTreeReplacesWithInOrderSuccessor() {
        setUpComplexTree();
        assertEquals("Ten", tree.remove(10));
        assertFalse(tree.contains(10));
        assertTrue(tree.contains(12));  // In-order successor
        assertEquals(6, tree.size());
    }

    @Test
    public void GivenNonEmptyStandardTree_WhenUsingGraphViz_TheElementsPlacedCorrectly() {
        setUpStandardTree();
        String dotGraph = tree.graphViz("SimpleTree");
        String expectedDot = """
                digraph SimpleTree {
                    "10" [label="10: Ten"];
                    "10" -> "5";
                    "5" [label="5: Five"];
                    "10" -> "20";
                    "20" [label="20: Twenty"];
                }
                """;
        assertEquals(expectedDot, dotGraph);
    }

    @Test
    public void GivenEmptyTree_WhenUsingGraphViz_TheGraphContainsOnlyNullLabel() {

        String dotGraph = tree.graphViz("EmptyTree");
        String expectedDot = """
                digraph EmptyTree {
                    null [shape=point];
                }
                """;
        assertEquals(expectedDot, dotGraph);

    }

    @Test
    public void GivenNonEmptyComplexTree_WhenUsingGraphViz_TheElementsPlacedCorrectly() {
        setUpComplexTree();
        String dotGraph = tree.graphViz("ComplexTree");
        String expectedDot = """
                digraph ComplexTree {
                    "10" [label="10: Ten"];
                    "10" -> "5";
                    "5" [label="5: Five"];
                    "5" -> "3";
                    "3" [label="3: Three"];
                    "5" -> "7";
                    "7" [label="7: Seven"];
                    "10" -> "15";
                    "15" [label="15: Fifteen"];
                    "15" -> "12";
                    "12" [label="12: Twelve"];
                    "15" -> "18";
                    "18" [label="18: Eighteen"];
                }
                """;
        assertEquals(expectedDot, dotGraph);

    }

    @Test
    public void GivenEmptyTree_WhenInsertingInLeftHeavyManner_ThenElementsPlacedCorrectly() {

        tree.add(10, "Ten");
        tree.add(5, "Five");
        tree.add(2, "Two");


        MyArrayList<Integer> keys = tree.getKeys();
        assertEquals(3, keys.size());
        assertEquals(2, keys.get(0));
        assertEquals(5, keys.get(1));
        assertEquals(10, keys.get(2));
    }

    @Test
    public void GivenEmptyTree_WhenInsertingInRightHeavyManner_ThenElementsPlacedCorrectly() {

        tree.add(10, "Ten");
        tree.add(12, "Twelve");
        tree.add(15, "Fifteen");


        MyArrayList<Integer> keys = tree.getKeys();
        assertEquals(3, keys.size());
        assertEquals(10, keys.get(0));
        assertEquals(12, keys.get(1));
        assertEquals(15, keys.get(2));
    }


    @Test
    public void GivenNonEmptyTree_WhenAddingDuplicatedKey_TheDuplicateKeyExceptionIsThrown() {
        tree.add(10, "Ten");
        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> tree.add(10, "Ten"));
        assertEquals("a duplicate key \"" + 10 + "\" is not allowed.", exception.getMessage());
    }

    @Test
    public void GivenNonEmptyTree_WhenTryingToGetNotPresentElement_TheKeyNotFoundExceptionIsThrown() {
        setUpStandardTree();
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> tree.get(30));
        assertEquals("Key \"" + 30 + "\" is not found.", exception.getMessage());
    }

    @Test
    public void GivenNonEmptyTree_WhenRemovingNonPresentElement_TheKeyNotFoundExceptionIsThrown() {
        setUpStandardTree();
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> tree.remove(30));
        assertEquals("Key \"" + 30 + "\" is not found.", exception.getMessage());
    }

    @Test
    public void GivenEmptyTree_WhenDeletingNode_ThenKeyNotFoundExceptionIsThrown() {
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> tree.remove(10));
        assertEquals("Key \"" + 10 + "\" is not found.", exception.getMessage());
    }


}




