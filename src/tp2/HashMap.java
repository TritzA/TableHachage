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
        } else if (n < 9) {
            return true;
        } else if (n % 3 == 0) {
            return false;
        } else {
            int r = (int) Math.floor(Math.sqrt(n));
            int f = 5;
            while (f <= r) {
                if ((n % f) == 0) {
                    return false;
                }
                if ((n % (f + 2)) == 0) {
                    return false;
                }
                f += 6;
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
        HashMap map = new HashMap(this.capacity);

        System.out.println(this.capacity);
        for (Node node : this.map) {
            if (node != null)
                map.put(node.key, node.data);
        }

        this.map = map.map;
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
        Node<KeyType, DataType> t = this.map[this.hash(key)];
        if (t != null) {
            do {
                if (t.key.equals(key)) {
                    return t.data;
                }
                t = t.next;
            } while (t != null);
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
        Node<KeyType, DataType> t = this.map[this.hash(key)]; // obtient le premier noeud grace à la clé
        if (t != null) {//collision ou deux clé égale
            DataType old = t.data;

            while (t.next != null && !t.key.equals(key)) { // aller jusqu'au dernier noeud non-vide ou jusqu'à un noeuc ayant la mm clé
                t = t.next;
            }

            // node vaut mtn le denier noeud non-null
            if (t.key.equals(key)) {
                t.data = value;
            } else {
                t.next = new Node<>(key, value);
            }
            return old;
        } else {//pas collision
            size++;
            if (this.needRehash()) {
                this.rehash();
            }
            this.map[this.hash(key)] = new Node<>(key, value);// faire fonctionner sans gérer les collisions
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
        Node<KeyType, DataType> t = this.map[this.hash(key)];
        Node<KeyType, DataType> p = null;

        if (t != null && t.key.equals(key)) {
            this.map[this.hash(key)] = t.next;
            size--;
            return t.data;
        }

        while (t != null && !t.key.equals(key)) {
            p = t;
            t = t.next;
        }

        if (t == null)
            return null;

        p.next = t.next;
        size--;
        return t.data;
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

    // Iterators are used to iterate over collections like so:
    // for (Key key : map) { doSomethingWith(key); }
    private class HashMapIterator implements Iterator<KeyType> {
        // TODO: Add any relevant data structures to remember where we are in the list.
        int posmap = 0;
        private Node<KeyType, DataType> current = map[0];
        private int expectedModCount = size;

        /**
         * TODO Worst Case : O(n)
         * Determine if there is a new element remaining in the hashmap.
         */
        public boolean hasNext() {
            if (current == null) {
                while (current == null && posmap < capacity - 1) {
                    posmap++;
                    current = map[posmap];
                }

                if (current == null)
                    return false;

                if (posmap < size)
                    return true;

            } else{
                if (current.next == null) {
                    do{
                        posmap++;
                        current = map[posmap];
                    }while (current == null && posmap < capacity - 1);

                    if (current == null)
                        return false;

                    if (posmap < size)
                        return true;

                }
            }
            return true;
        }

        /**
         * TODO Worst Case : O(n)
         * Return the next new key in the hashmap.
         */
        public KeyType next() {
            if (size != expectedModCount)
                throw new java.util.ConcurrentModificationException();

            if (!hasNext())
                throw new java.util.NoSuchElementException();

            if (current.next == null) {
                do{
                    posmap++;
                    current = map[posmap];
                }while (current == null);

                return current.key;
            }

            KeyType nextItem = current.key;
            current = current.next;
            return nextItem;
        }
    }
}
