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
        int n = this.capacity * this.CAPACITY_INCREASE_FACTOR;
        if (n % 2 == 0) {
            n++;
        }
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
        HashMap map = new HashMap(this.capacity);

        map.increaseCapacity();

        for (Node node : this.map) {
            if (node != null)
                map.put(node.key, node.data);
        }
        this.capacity = map.capacity;
        this.map = map.map;
        System.out.println(this.map);
    }

    /**
     * TODO Average Case : O(1)
     * Finds if map contains a key
     *
     * @param key Key which we want to know if exists within map
     * @return if key is already used in map
     */
    public boolean containsKey(KeyType key) {
        Node<KeyType, DataType> node = this.map[this.hash(key)]; // obtient le premier noeud grace à la clé
        if (node != null) {
            boolean mmCle = (node.key == key);
            if (mmCle == false) {//si ne trouve pas la même clé dès le début
                do {
                    if (node.key.equals(key)) {
                        return true;
                    }
                    node = node.next;
                } while (node != null);
            } else {
                return true;//si la clé est retrouvé dès le début, retourner true
            }
        }
        return false; // si ne la trouve pas au début ou dans le parcours
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
        if (!containsKey(key)) {
            size++;
            //System.out.println(size * loadFactor);
            if (this.needRehash()) {
                System.out.println("On est la");
                this.rehash();
            }
            size--;
        }
        Node<KeyType, DataType> node = this.map[this.hash(key)]; // obtient le premier noeud grace à la clé
        if (node != null) {//collision ou deux clé égale
            DataType old = node.data;
            boolean mmCle = (node.key == key);

            while (node.next != null && !mmCle) { // aller jusqu'au dernier noeud non-vide ou jusqu'à un noeuc ayant la mm clé
                if (node.key == key) {
                    mmCle = true;
                } else {
                    node = node.next;
                }
            }

            // node vaut mtn le denier noeud non-null
            if (mmCle == true) {
                node.data = value;
            } else {
                node.next = new Node<>(key, value);
            }
            return old;
        } else {//pas collision
            this.map[this.hash(key)] = new Node<>(key, value);// faire fonctionner sans gérer les collisions
            size++;
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
        private int position = 0;//ajout
        private int posMap = 0;//ajout

        /**
         * TODO Worst Case : O(n)
         * Determine if there is a new element remaining in the hashmap.
         */
        public boolean hasNext() {
            while (map[posMap].next != null) {

            }
            return position < size;
        }

        /**
         * TODO Worst Case : O(n)
         * Return the next new key in the hashmap.
         */
        public KeyType next() {
            if (!hasNext()) {
                return null;
            }
            return null;
        }
    }
}