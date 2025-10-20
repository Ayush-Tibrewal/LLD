import java.util.*;

class KeyAndValue<K, V> {
    K key;
    V value;

    KeyAndValue(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

class MyHashMap<K, V> {
    ArrayList<LinkedList<KeyAndValue<K, V>>> list;
    int size = 0;
    float lf = 0.5f; // load factor

    MyHashMap() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new LinkedList<>());
        }
    }

    void put(K key, V value) {
        int hash = Math.abs(key.hashCode() % list.size());
        LinkedList<KeyAndValue<K, V>> bucket = list.get(hash);

        for (KeyAndValue<K, V> pair : bucket) {
            if (pair.key.equals(key)) {
                pair.value = value;
                return;
            }
        }

        // Rehash if load factor exceeded
        if ((float) size / list.size() > lf) {
            rehash();
            hash = Math.abs(key.hashCode() % list.size());
            bucket = list.get(hash); // recompute after resize
        }

        bucket.add(new KeyAndValue<>(key, value));
        size++;
    }

    V get(K key) {
        int hash = Math.abs(key.hashCode() % list.size());
        LinkedList<KeyAndValue<K, V>> bucket = list.get(hash);

        for (KeyAndValue<K, V> pair : bucket) {
            if (pair.key.equals(key)) {
                return pair.value;
            }
        }
        return null;
    }

    private void rehash() {
        ArrayList<LinkedList<KeyAndValue<K, V>>> old = list;
        list = new ArrayList<>();
        size = 0;

        for (int i = 0; i < old.size() * 2; i++) {
            list.add(new LinkedList<>());
        }

        for (LinkedList<KeyAndValue<K, V>> bucket : old) {
            for (KeyAndValue<K, V> pair : bucket) {
                put(pair.key, pair.value);
            }
        }
    }
}
    
    
    
   

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("A", "Apple");
        map.put("B", "Banana");
        map.put("C", "Cat");

        System.out.println(map.get("A")); // Apple
        System.out.println(map.get("C")); // Cat
        System.out.println(map.get("Z")); // null
    }
}
