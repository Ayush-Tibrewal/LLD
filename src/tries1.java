class Node{
    Node[] links; 
    boolean flag;
    Node(){
        links = new Node[26];
        flag = false;
    }
    boolean containskey(char ch){
        return links[ch-'a']!=null;
        }
        void setEnd(){
            this.flag = true;
        }
        Node get(char ch){
            return links[ch-'a'];
        }
        void put(char ch , Node node){
            links[ch-'a'] = node;
        }

        boolean isEnd(){
            return flag;
        }
    }

class Trie {
    Node root;
    public Trie() {
        root = new Node();
    }
    
    public void insert(String word) {
        Node node = root;
        for(int i = 0; i< word.length() ; i++){
            char ch = word.charAt(i);
            if(!node.containskey(ch)){
                node.put(ch , new Node());
            }
            node = node.get(ch);
        }
        node.setEnd();
    }
    
    public boolean search(String word) {
        int n = word.length();
        Node node = root;
        for(int i = 0; i< n ; i++){
            char ch = word.charAt(i);
            if(!node.containskey(ch)){
                return false;
            }
            node = node.get(ch);
        }
        return node.isEnd();
    }
    
    public boolean startsWith(String prefix) {
        int n = prefix.length();
        Node node = root;
        for(int i = 0 ; i<n; i++){
            char ch = prefix.charAt(i);
            if(!node.containskey(ch)) return false;
            node = node.get(ch);
        }
        return true;
    }
}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */
