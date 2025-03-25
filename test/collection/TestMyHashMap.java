package collection;

import custom_data_structures.MyArrayList;
import custom_data_structures.MyHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyHashMap {
    MyHashMap<Integer, String> map;

    @BeforeEach
    public void setUp() {
        map = new MyHashMap<>();
    }

    private void addDefaultEntries() {
        map.add(1, "Value 1");
        map.add(2, "Value 2");
        map.add(3, "Value 3");
        map.add(4, "Value 4");
    }

    @Test
    public void givenEmptyHashMap_whenCheckIfIsEmpty_thenTrueIsReturned() {
        assertTrue(map.isEmpty());
    }

    @Test
    public void GivenKeys_WhenCalculateTheBucketIndex_TheCorrectIndexIsReturned() throws Exception {
        Method getBucketIndex = MyHashMap.class.getDeclaredMethod("getBucketIndex", Object.class);
        getBucketIndex.setAccessible(true);
        int initialCapacity = 16;

        int index1 = (int) getBucketIndex.invoke(map, 1);
        int index17 = (int) getBucketIndex.invoke(map, 17);


        int expectedIndex1 = Math.abs(1 % initialCapacity);
        int expectedIndex17 = Math.abs(17 % initialCapacity);

        assertEquals(expectedIndex1, index1);
        assertEquals(expectedIndex17, index17);
    }

    @Test
    public void GivenKeysAfterResize_WhenCalculateTheBucketIndex_ThenCorrectIndexIsReturned() throws Exception {
        Method getBucketIndex = MyHashMap.class.getDeclaredMethod("getBucketIndex", Object.class);
        getBucketIndex.setAccessible(true);


        for (int i = 1; i <= 13; i++) {
            map.add(i, "Value " + i);
        }


        int resizedCapacity = 32;

        int index1 = (int) getBucketIndex.invoke(map, 1);
        int index17 = (int) getBucketIndex.invoke(map, 17);

        int expectedIndex1 = Math.abs(1 % resizedCapacity);
        int expectedIndex17 = Math.abs(17 % resizedCapacity);

        assertEquals(expectedIndex1, index1);
        assertEquals(expectedIndex17, index17);
    }


    @Test
    public void givenNonEmptyHashMap_whenCheckIfIsEmpty_thenFalseIsReturned() {
        map.add(1, "One");
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());
        assertEquals("One", map.get(1));
    }

    @Test
    public void givenNonEmptyHashMap_whenAddElements_thenHashMapContainsAllKeysAndValues() {
        addDefaultEntries();

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());
        for (int i = 1; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }
    }

    @Test
    public void givenNonEmptyHashMap_whenCheckIfContainsNonExistentElement_thenFalseIsReturned() {
        addDefaultEntries();

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());

        for (int i = 1; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }
        assertFalse(map.contains(5));
    }

    @Test
    public void givenNonEmptyHashMap_whenAddDuplicateKey_thenDuplicateKeyExceptionIsThrown() {
        addDefaultEntries();

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());

        for (int i = 1; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }

        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> map.add(4, "4"));
        assertEquals("a duplicate key \"" + 4 + "\" is not allowed.", exception.getMessage());
    }

    @Test
    public void givenNonEmptyHashMap_whenGetNonExistentKey_thenKeyNotFoundExceptionIsThrown() {
        addDefaultEntries();

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());

        for (int i = 1; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }

        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> map.get(5));
        assertEquals("Key \"" + 5 + "\" is not found.", exception.getMessage());
    }

    @Test
    public void givenNonEmptyHashMap_whenRemoveNonExistentKey_thenKeyNotFoundExceptionIsThrown() {
        addDefaultEntries();

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());

        for (int i = 1; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }

        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> map.remove(5));
        assertEquals("Key \"" + 5 + "\" is not found.", exception.getMessage());
    }
    @Test
    public void givenEmptyHashMap_whenRemoveKey_thenThrowsKeyNotFoundException() {
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> map.remove(1));
        assertEquals("Key \"1\" is not found.", exception.getMessage());
    }

    @Test
    public void givenEmptyHashMap_whenGetKey_thenThrowsKeyNotFoundException() {
        KeyNotFoundException exception = assertThrows(KeyNotFoundException.class, () -> map.get(1));
        assertEquals("Key \"" + 1 + "\" is not found.", exception.getMessage());
    }

    @Test
    public void givenNonEmptyHashMap_whenRemoveExistentEntry_thenEntryIsRemovedCorrectly() {
        addDefaultEntries();

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());

        for (int i = 1; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }

        assertEquals("Value 1", map.remove(1));
        assertEquals(3, map.size());

        assertFalse(map.contains(1));

        for (int i = 2; i <= 4; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }
    }

    @Test
    public void givenNonEmptyHashMap_whenGetAllKeys_thenCorrectListOfKeysIsReturned() {
        map.add(1, "One");
        map.add(2, "Two");
        map.add(3, "Three");
        map.add(4, "Four");

        assertFalse(map.isEmpty());
        assertEquals(4, map.size());

        MyArrayList<Integer> expectedKeys = new MyArrayList<>();
        expectedKeys.addLast(1);
        expectedKeys.addLast(2);
        expectedKeys.addLast(3);
        expectedKeys.addLast(4);

        MyArrayList<Integer> actualKeys = map.getKeys();

        assertEquals(expectedKeys.size(), actualKeys.size());

        for (int i = 0; i < expectedKeys.size(); i++) {
            assertEquals(expectedKeys.get(i), actualKeys.get(i));
        }
    }

    @Test
    public void givenHashMap_whenAddElementsAboveLoadFactor_thenMapExpandsToStoreAllElements() {
        for (int i = 1; i <= 13; i++) {
            map.add(i, "Value " + i);
        }

        assertFalse(map.isEmpty());
        assertEquals(13, map.size());

        for (int i = 1; i <= 13; i++) {
            assertTrue(map.contains(i));
            assertEquals("Value " + i, map.get(i));
        }


        map.add(14, "Value 14");
        map.add(15, "Value 15");
        map.add(16, "Value 16");
        map.add(17, "Value 17");
        assertEquals(17, map.size());

        for (int i = 1; i <= 17; i++) {
            assertEquals("Value " + i, map.get(i));
        }
    }

    @Test
    public void givenNullKey_whenAdd_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> map.add(null, "Null Value"));
    }

    @Test
    public void givenEmptyHashMap_whenGetKeys_thenReturnsEmptyList() {
        MyArrayList<Integer> keys = map.getKeys();
        assertTrue(keys.isEmpty());
    }

    @Test
    public void givenHashMap_whenKeysCauseCollision_thenHandlesCollisionsCorrectly() {

        map.add(1, "Value 1");
        map.add(17, "Value 17");

        assertTrue(map.contains(1));
        assertTrue(map.contains(17));
        assertEquals("Value 1", map.get(1));
        assertEquals("Value 17", map.get(17));
    }


    @Test
    public void givenEmptyMap_whenGraphViz_thenReturnsEmptyStateGraph() {
        String graph = map.graphViz("MyHashMap");

        String expectedGraph = """
                digraph "MyHashMap" {
                rankdir=LR;
                node [shape=record];
                bucket0 [label="Bucket 0"];
                bucket0 -> empty0;
                empty0 [label="Empty"];
                bucket1 [label="Bucket 1"];
                bucket1 -> empty1;
                empty1 [label="Empty"];
                bucket2 [label="Bucket 2"];
                bucket2 -> empty2;
                empty2 [label="Empty"];
                bucket3 [label="Bucket 3"];
                bucket3 -> empty3;
                empty3 [label="Empty"];
                bucket4 [label="Bucket 4"];
                bucket4 -> empty4;
                empty4 [label="Empty"];
                bucket5 [label="Bucket 5"];
                bucket5 -> empty5;
                empty5 [label="Empty"];
                bucket6 [label="Bucket 6"];
                bucket6 -> empty6;
                empty6 [label="Empty"];
                bucket7 [label="Bucket 7"];
                bucket7 -> empty7;
                empty7 [label="Empty"];
                bucket8 [label="Bucket 8"];
                bucket8 -> empty8;
                empty8 [label="Empty"];
                bucket9 [label="Bucket 9"];
                bucket9 -> empty9;
                empty9 [label="Empty"];
                bucket10 [label="Bucket 10"];
                bucket10 -> empty10;
                empty10 [label="Empty"];
                bucket11 [label="Bucket 11"];
                bucket11 -> empty11;
                empty11 [label="Empty"];
                bucket12 [label="Bucket 12"];
                bucket12 -> empty12;
                empty12 [label="Empty"];
                bucket13 [label="Bucket 13"];
                bucket13 -> empty13;
                empty13 [label="Empty"];
                bucket14 [label="Bucket 14"];
                bucket14 -> empty14;
                empty14 [label="Empty"];
                bucket15 [label="Bucket 15"];
                bucket15 -> empty15;
                empty15 [label="Empty"];
                }
                """;
        assertEquals(expectedGraph, graph);
    }


    @Test
    public void givenNonEmptyHashMap_whenUseGraphVizMethod_thenElementsAreDisplayedCorrectly() throws DuplicateKeyException {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();

        map.add(1, 100);
        map.add(2, 200);
        map.add(3, 300);

        String dotGraph = map.graphViz("MyHashMap");
        String expectedDot = """
                digraph "MyHashMap" {
                rankdir=LR;
                node [shape=record];
                bucket0 [label="Bucket 0"];
                bucket0 -> empty0;
                empty0 [label="Empty"];
                bucket1 [label="Bucket 1"];
                entry1_0 [label="{<key> Key: 1 | <value> Value: 100}"];
                bucket1 -> entry1_0;
                bucket2 [label="Bucket 2"];
                entry2_0 [label="{<key> Key: 2 | <value> Value: 200}"];
                bucket2 -> entry2_0;
                bucket3 [label="Bucket 3"];
                entry3_0 [label="{<key> Key: 3 | <value> Value: 300}"];
                bucket3 -> entry3_0;
                bucket4 [label="Bucket 4"];
                bucket4 -> empty4;
                empty4 [label="Empty"];
                bucket5 [label="Bucket 5"];
                bucket5 -> empty5;
                empty5 [label="Empty"];
                bucket6 [label="Bucket 6"];
                bucket6 -> empty6;
                empty6 [label="Empty"];
                bucket7 [label="Bucket 7"];
                bucket7 -> empty7;
                empty7 [label="Empty"];
                bucket8 [label="Bucket 8"];
                bucket8 -> empty8;
                empty8 [label="Empty"];
                bucket9 [label="Bucket 9"];
                bucket9 -> empty9;
                empty9 [label="Empty"];
                bucket10 [label="Bucket 10"];
                bucket10 -> empty10;
                empty10 [label="Empty"];
                bucket11 [label="Bucket 11"];
                bucket11 -> empty11;
                empty11 [label="Empty"];
                bucket12 [label="Bucket 12"];
                bucket12 -> empty12;
                empty12 [label="Empty"];
                bucket13 [label="Bucket 13"];
                bucket13 -> empty13;
                empty13 [label="Empty"];
                bucket14 [label="Bucket 14"];
                bucket14 -> empty14;
                empty14 [label="Empty"];
                bucket15 [label="Bucket 15"];
                bucket15 -> empty15;
                empty15 [label="Empty"];
                }
                """;
        assertEquals(expectedDot, dotGraph);
    }

}