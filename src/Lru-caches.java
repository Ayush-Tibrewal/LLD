import java.util.*;

class Node {
    Node next, prev;
    int key, val;

    Node(int key, int val) {
        this.key = key;
        this.val = val;
    }
}

class LRUCache {
    Node head = new Node(0, 0);
    Node tail = new Node(0, 0);
    HashMap<Integer, Node> map;
    int cap;

    public LRUCache(int capacity) {
        cap = capacity;
        map = new HashMap<>();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            delete(node);
            insert(node);
            return node.val;
        }
        return -1;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            delete(map.get(key));
        }
        if (map.size() == cap) {
            delete(tail.prev);
        }
        insert(new Node(key, value));
    }

    void insert(Node node) {
        map.put(node.key, node);
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    void delete(Node node) {
        map.remove(node.key);
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
