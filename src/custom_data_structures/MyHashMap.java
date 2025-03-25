package custom_data_structures;

import collection.DuplicateKeyException;
import collection.KeyNotFoundException;
import collection.SaxHashMap;


public class MyHashMap<K, V> implements SaxHashMap<K, V> {
    private final static int INITIAL_CAPACITY = 16;
    private final static float LOAD_FACTOR = 0.75f;


    private MyDoublyLinkedList<Entry<K, V>>[] table;
    private int size;


    /**
     * The inner class represents a key-value pair entry in the hash map.
     *
     * @param <K> The type of the key.
     * @param <V> The type of the value.
     */
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new MyDoublyLinkedList[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Checks if the hash map is empty.
     *
     * @return {@code true} if the hash map contains no key-value mappings, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Returns the number of key-value mappings in the hash map.
     *
     * @return The size of the hash map.
     */
    @Override
    public int size() {
        return size;
    }


    /**
     * Generates a GraphViz DOT format string representation of the hash map.
     * Each bucket is represented, showing the keys and values within each bucket.
     *
     * @param name The name of the GraphViz graph.
     * @return A string in DOT format representing the hash map's structure.
     */
    @Override
    public String graphViz(String name) {

        StringBuilder dot = new StringBuilder();

        // Starting the Graph
        dot.append("digraph \"").append(name).append("\" {\n");
        dot.append("rankdir=LR;\n");
        dot.append("node [shape=record];\n");


        for (int i = 0; i < table.length; i++) {
            MyDoublyLinkedList<Entry<K, V>> bucket = table[i];


            dot.append("bucket").append(i).append(" [label=\"Bucket ").append(i).append("\"];\n");

            if (bucket != null && !bucket.isEmpty()) {

                int entryId = 0;
                Entry<K, V> previousEntry = null;

                for (Entry<K, V> entry : bucket) {

                    dot.append("entry").append(i).append("_").append(entryId)
                            .append(" [label=\"{<key> Key: ").append(entry.key)
                            .append(" | <value> Value: ").append(entry.value).append("}\"];\n");


                    if (entryId == 0) {
                        dot.append("bucket").append(i).append(" -> entry").append(i).append("_").append(entryId).append(";\n");
                    }


                    if (previousEntry != null) {
                        dot.append("entry").append(i).append("_").append(entryId - 1)
                                .append(" -> entry").append(i).append("_").append(entryId).append(";\n");
                    }

                    previousEntry = entry;
                    entryId++;
                }

            } else {

                dot.append("bucket").append(i).append(" -> empty").append(i).append(";\n");
                dot.append("empty").append(i).append(" [label=\"Empty\"];\n");
            }
        }


        dot.append("}\n");

        return dot.toString();
    }

    /**
     * Checks if the specified key exists in the hash map.
     *
     * @param key The key to be checked.
     * @return {@code true} if the key exists, {@code false} otherwise.
     */
    @Override
    public boolean contains(K key) {
        int index = getBucketIndex(key);
        MyDoublyLinkedList<Entry<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key.
     * @throws KeyNotFoundException if the key does not exist in the map.
     */
    @Override
    public V get(K key) {
        int index = getBucketIndex(key);
        MyDoublyLinkedList<Entry<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        throw new KeyNotFoundException("" + key);
    }

    /**
     * Adds a new key-value pair to the hash map.
     * If the current size exceeds the load factor threshold after insertion,
     * the hash map will resize to maintain performance.
     *
     * @param key   The key to add.
     * @param value The value associated with the key.
     * @throws DuplicateKeyException if the key already exists in the hash map.
     */

    @Override
    public void add(K key, V value) throws DuplicateKeyException {
        if (contains(key)) {
            throw new DuplicateKeyException("" + key);
        }
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = new MyDoublyLinkedList<>();

        }
        table[index].addLast(new Entry<>(key, value));
        size++;
        if ((float) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    /**
     * Removes the entry for the specified key and returns the associated value.
     *
     * @param key The key of the entry to be removed.
     * @return The value associated with the removed key.
     * @throws KeyNotFoundException if the key does not exist in the map.
     */

    @Override
    public V remove(K key) throws KeyNotFoundException {
        int index = getBucketIndex(key);
        MyDoublyLinkedList<Entry<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    V value = entry.value;
                    bucket.remove(entry);
                    size--;
                    return value;
                }
            }

        }

        throw new KeyNotFoundException("" + key);
    }

    /**
     * Returns a list of all keys in the hash map.
     *
     * @return A MyArraylist of all keys in the hash map.
     */

    @Override
    public MyArrayList<K> getKeys() {
        MyArrayList<K> keys = new MyArrayList<>();
        for (MyDoublyLinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    keys.addLast(entry.key);
                }
            }
        }
        return keys;
    }

    /**
     * Computes the index of the bucket where a given key should be placed,
     * based on the key's hash code and the current table length.
     * The bucket index is calculated by taking the hash code of the key,
     * applying the modulo operation with the table length to ensure
     * the index falls within the bounds of the table.
     * The `Math.abs` used
     * handle negative hash codes.
     *
     *
     * @param key The key to be hashed.
     * @return The non-negative index of the bucket for the specified key.
     */

    private int getBucketIndex(K key) {

        return Math.abs(key.hashCode() % table.length);
    }

    /**
     * Resizes the hash map by doubling its capacity and rehashing all entries.
     * This method is triggered when the load factor exceeds 0.75.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        MyDoublyLinkedList<Entry<K, V>>[] oldTable = table;
        table = new MyDoublyLinkedList[oldTable.length * 2];
        size = 0;
        for (MyDoublyLinkedList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    add(entry.key, entry.value);
                }
            }
        }


    }

}
