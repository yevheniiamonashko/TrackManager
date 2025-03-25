package collection;

import custom_data_structures.MyAVLBinarySearchTree;
import custom_data_structures.MyArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyAVLBST {
    private MyAVLBinarySearchTree<Integer, String> avlTree;
    @BeforeEach
    public void setUp(){
        avlTree = new MyAVLBinarySearchTree<>();
    }


    @Test
    public void GivenEmptyAVLTree_WhenLeftLeftCaseOfInsert_TheThreePerformsTheRightRotation(){

        avlTree.add(30, "Thirty");
        avlTree.add(20, "Twenty");
        avlTree.add(10, "Ten");

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(10, keys.get(0));
        assertEquals(20, keys.get(1));
        assertEquals(30, keys.get(2));
    }
    @Test
    public void GivenEmptyAVLTree_WhenRightRightCaseOfInsert_TheThreePerformsTheLeftRotation(){
        avlTree.add(30, "Thirty");
        avlTree.add(40, "Forty");
        avlTree.add(50, "Fifty");

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(30, keys.get(0));
        assertEquals(40, keys.get(1));
        assertEquals(50, keys.get(2));
    }

    @Test
    public void GivenEmptyAVLTree_WhenLeftRightCaseOfInsert_TheTreePerformsTheLRRotation(){
        // Left-Right Case
        avlTree.add(30, "Thirty");
        avlTree.add(10, "Ten");
        avlTree.add(20, "Twenty");

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(10, keys.get(0));
        assertEquals(20, keys.get(1));
        assertEquals(30, keys.get(2));
    }

    @Test
    public void GivenEmptyAVLTree_WhenRightLeftCaseOfInsert_TheTreePerformsTheRLRotation(){

        avlTree.add(10, "Ten");
        avlTree.add(30, "Thirty");
        avlTree.add(20, "Twenty");

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(10, keys.get(0));
        assertEquals(20, keys.get(1));
        assertEquals(30, keys.get(2));
    }

    @Test
    public void GivenAVLTree_WhenLeftLeftCaseOfDelete_TheTreePerformsTheRightRotation() {
        avlTree.add(50, "Fifty");
        avlTree.add(40, "Forty");
        avlTree.add(30, "Thirty");
        avlTree.add(20, "Twenty");
        avlTree.add(10, "Ten");

        avlTree.remove(50);  // Causes a LL case rotation

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(10, keys.get(0));
        assertEquals(20, keys.get(1));
        assertEquals(30, keys.get(2));
        assertEquals(40, keys.get(3));
    }

    @Test
    public void GivenAVLTree_WhenRightRightCaseOfDelete_TheTreePerformsTheLeftRotation() {
        avlTree.add(10, "Ten");
        avlTree.add(20, "Twenty");
        avlTree.add(30, "Thirty");
        avlTree.add(40, "Forty");
        avlTree.add(50, "Fifty");

        avlTree.remove(10);  // Causes a RR case rotation

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(20, keys.get(0));
        assertEquals(30, keys.get(1));
        assertEquals(40, keys.get(2));
        assertEquals(50, keys.get(3));
    }

    @Test
    public void GivenAVLTree_WhenLeftRightCaseOfDelete_TheTreePerformsTheLRRotation() {
        avlTree.add(40, "Forty");
        avlTree.add(20, "Twenty");
        avlTree.add(30, "Thirty");

        avlTree.remove(40);  // Causes LR case rotation

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(20, keys.get(0));
        assertEquals(30, keys.get(1));
    }

    @Test
    public void GivenAVLTree_WhenRightLeftCaseOfDelete_TheTreePerformsTheRLRotation() {
        avlTree.add(10, "Ten");
        avlTree.add(50, "Fifty");
        avlTree.add(30, "Thirty");

        avlTree.remove(10);  // Causes a RL case rotation

        MyArrayList<Integer> keys = avlTree.getKeys();
        assertEquals(30, keys.get(0));
        assertEquals(50, keys.get(1));
    }

    @Test
    public void GivenEmptyAVLTree_WhenCheckingBalance_ItShouldBeBalanced() {


       assertEquals(0, avlTree.getBalance(avlTree.getRoot()));
        assertTrue(avlTree.isEmpty());
    }
    @Test
    public void GivenAVLTree_WhenLeftHeavy_BalanceFactorShouldBeOne() {
        avlTree.add(30, "Thirty");
        avlTree.add(20, "Twenty");


        assertEquals(1, avlTree.getBalance(avlTree.getRoot()));
    }

    @Test
    public void GivenAVLTree_WhenRightHeavy_BalanceFactorShouldBeMinusOne() {
        avlTree.add(30, "Thirty");
        avlTree.add(40, "Forty");


        assertEquals(-1, avlTree.getBalance(avlTree.getRoot()));
    }

    @Test
    public void GivenNonEmptyTree_WhenAddingDuplicatedKey_TheDuplicateKeyExceptionIsThrown() {
        avlTree.add(12, "Twelve");
        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> avlTree.add(12, "Twelve"));
        assertEquals("a duplicate key \"" + 12 + "\" is not allowed.", exception.getMessage());
    }

    @Test
    public void GivenNonEmptyTree_WhenTryingToGetNotPresentElement_TheKeyNotFoundExceptionIsThrown() {
        avlTree.add(12, "Twelve");
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> avlTree.get(30));
        assertEquals("Key \"" + 30 + "\" is not found.", exception.getMessage());
    }





}



