class Node {
    Node[] links = new Node[26]; // har character ke liye ek link (a-z)
    int countPrefix = 0; // kitni baar ye prefix aaya
    int countEnd = 0;    // kitni baar word yahan khatam hua

    boolean containsKey(char ch) {
        return links[ch - 'a'] != null;
    }

    Node get(char ch) {
        return links[ch - 'a'];
    }

    void put(char ch, Node node) {
        links[ch - 'a'] = node;
    }
}

class Trie {
    private Node root;

    public Trie() {
        root = new Node();
    }

    // Insert a word
    public void insert(String word) {
        Node node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsKey(ch)) {
                node.put(ch, new Node());
            }
            node = node.get(ch);
            node.countPrefix++; // har prefix ke liye count badha do
        }
        node.countEnd++; // word yahan khatam hua
    }

    // Count how many times a word exactly appears
    public int countWordsEqualTo(String word) {
        Node node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsKey(ch)) return 0;
            node = node.get(ch);
        }
        return node.countEnd;
    }

    // Count how many words start with a prefix
    public int countWordsStartingWith(String prefix) {
        Node node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.containsKey(ch)) return 0;
            node = node.get(ch);
        }
        return node.countPrefix;
    }

    // Erase one occurrence of a word
    public void erase(String word) {
        Node node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsKey(ch)) return; // word not found
            node = node.get(ch);
            node.countPrefix--; // prefix count kam karo
        }
        node.countEnd--; // end count bhi kam karo
    }
}

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();

        trie.insert("apple");
        trie.insert("apple");
        trie.insert("app");

        System.out.println(trie.countWordsEqualTo("apple")); // 2
        System.out.println(trie.countWordsStartingWith("app")); // 3

        trie.erase("apple");
        System.out.println(trie.countWordsEqualTo("apple")); // 1
        System.out.println(trie.countWordsStartingWith("app")); // 2
    }
}
