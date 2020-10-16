package tp2;

import java.util.Iterator;

public class HashMap<KeyType, DataType> implements Iterable<KeyType> {

    private static final int DEFAULT_CAPACITY = 20;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final int CAPACITY_INCREASE_FACTOR = 2;

    private Node<KeyType, DataType>[] map;
    private int size = 0;
    private int capacity;
    private final float loadFactor; // Compression factor

    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY,
                DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, float loadFactor) {
        capacity = initialCapacity;
        this.loadFactor = 1 / loadFactor;
        map = new Node[capacity];
    }

    /**
     * Finds the index attached to a particular key
     * This is the hashing function ("Fonction de dispersement")
     *
     * @param key Value used to access to a particular instance of a DataType within map
     * @return Index value where this key should be placed in attribute map
     */
    private int hash(KeyType key) {
        int keyHash = key.hashCode() % capacity;
        return Math.abs(keyHash);
    }

    /**
     * @return if map should be rehashed
     */
    private boolean needRehash() {
        return size * loadFactor > capacity;
    }

    /**
     * @return Number of elements currently in the map
     */
    public int size() {
        return size;
    }

    /**
     * @return Current reserved space for the map
     */
    public int capacity() {
        return capacity;
    }

    /**
     * @return if map does not contain any element
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * TODO Average Case : O(1)
     * Find the next prime after increasing the capacity by CAPACITY_INCREASE_FACTOR (multiplication)
     */
    private void increaseCapacity() {
        int n = this.capacity * this.CAPACITY_INCREASE_FACTOR + 1;
        while (!ispremier(n)) {
            n += 2;
        }
        this.capacity = n;
    }

    private boolean ispremier(int n) {
        if (n < 2) {
            return false;
        } else {
            int r = (int) Math.floor(Math.sqrt(n));
            int f = 3;
            while (f <= r) {
                if ((n % f) == 0) {
                    return false;
                }
                f += 2;
            }
            return true;
        }
    }

    /**
     * TODO Average Case : O(n)
     * Increases capacity by CAPACITY_INCREASE_FACTOR (multiplication) and
     * reassigns all contained values within the new map
     */
    private void rehash() {
        this.increaseCapacity();
        HashMap tempo = new HashMap(this.capacity);

        for (Node node : this.map) {
            if (node != null) {
                tempo.put(node.key, node.data);
            }
        }
        this.map = tempo.map;
    }


    /**
     * TODO Average Case : O(1)
     * Finds if map contains a key
     *
     * @param key Key which we want to know if exists within map
     * @return if key is already used in map
     */
    public boolean containsKey(KeyType key) {
        return this.get(key) != null;
    }

    /**
     * TODO Average Case : O(1)
     * Finds the value attached to a key
     *
     * @param key Key which we want to have its value
     * @return DataType instance attached to key (null if not found)
     */
    public DataType get(KeyType key) {
        Node<KeyType, DataType> node = this.map[this.hash(key)];
        if (node != null) {
            do {
                if (node.key.equals(key)) {
                    return node.data;
                }
                node = node.next;
            } while (node != null);

        }
        return null;
    }

    /**
     * TODO Average Case : O(1) , Worst case : O(n)
     * Assigns a value to a key
     *
     * @param key Key which will have its value assigned or reassigned
     * @return Old DataType instance at key (null if none existed)
     */
    public DataType put(KeyType key, DataType value) {

        Node<KeyType, DataType> node = this.map[this.hash(key)];
        if (node != null) {
            DataType old = node.data;
            boolean smKey = (node.key.equals(key));
            while (node.next != null && !smKey) {
                if (node.key.equals(key)) {
                    smKey = true;
                } else {
                    node = node.next;
                }
            }

            if (smKey) {
                node.data = value;
            } else {
                node.next = new Node<>(key, value);
            }
            return old;


        } else {
            size++;
            if (this.needRehash()) {
                this.rehash();
            }
            this.map[this.hash(key)] = new Node<>(key, value);
            return null;
        }

    }

    /**
     * TODO Average Case : O(1)
     * Removes the node attached to a key
     *
     * @param key Key which is contained in the node to remove
     * @return Old DataType instance at key (null if none existed)
     */
    public DataType remove(KeyType key) {
        Node<KeyType, DataType> node = this.map[this.hash(key)];
        Node<KeyType, DataType> prev = null;

        if (node != null && node.key.equals(key)) {
            this.map[this.hash(key)] = node.next;
            size--;
            return node.data;
        }

        while (node != null && !node.key.equals(key)) {
            prev = node;
            node = node.next;
        }

        if (node == null) {
            return null;
        }

        prev.next = node.next;
        size--;
        return node.data;
    }

    /**
     * TODO Worst Case : O(1)
     * Removes all nodes contained within the map
     */
    public void clear() {
        this.map = new Node[this.capacity];
        size = 0;
    }

    static class Node<KeyType, DataType> {
        final KeyType key;
        DataType data;
        Node<KeyType, DataType> next; // Pointer to the next node within a Linked List

        Node(KeyType key, DataType data) {
            this.key = key;
            this.data = data;
            next = null;
        }
    }

    @Override
    public Iterator<KeyType> iterator() {
        return new HashMapIterator();
    }

    //"L'itérateur est utilisé dans les tests. Il ne faut pas l'utiliser dans la classe HashMap"
    // Iterators are used to iterate over collections like so:
    // for (Key key : map) { doSomethingWith(key); }
    private class HashMapIterator implements Iterator<KeyType> {
        // TODO: Add any relevant data structures to remember where we are in the list.
        private int posNode = 0;//ajout
        private int posMap = 0;//ajout

        /**
         * TODO Worst Case : O(n)
         * Determine if there is a new element remaining in the hashmap.
         */
        public boolean hasNext() {
            Node<KeyType, DataType> node = map[posMap];
            do {
                node = node.next;
                posNode++;
            } while (node != null);
            System.out.println("itérateur ne fonctionne pas");
            return true;
        }

        /**
         * TODO Worst Case : O(n)
         * Return the next new key in the hashmap.
         */
        public KeyType next() {
            Node<KeyType, DataType> node = map[posMap];
            for (int i = 0; i < posNode; i++) {
                node = node.next;
            } //while (node != null);
            return null;
        }
    }
}