class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList {
    Node head;

    void insert(int data) {
        Node node = new Node(data);
        if (head == null) {
            head = node;
        } else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = node;
        }
    }

    void insertAtStart(int data) {
        Node node = new Node(data);
        node.next = head;
        head = node;
    }

    void insertAt(int index, int data) {
        if (index == 0) {
            insertAtStart(data);
            return;
        }
        Node node = new Node(data);
        Node temp = head;
        for (int i = 0; i < index - 1; i++) {
            if (temp == null) return; // index out of range
            temp = temp.next;
        }
        node.next = temp.next;
        temp.next = node;
    }

    void deleteAt(int index) {
        if (head == null) return;
        if (index == 0) {
            head = head.next;
            return;
        }
        Node temp = head;
        for (int i = 0; i < index - 1; i++) {
            if (temp == null) return; // index out of range
            temp = temp.next;
        }
        if (temp.next == null) return;
        temp.next = temp.next.next;
    }

    void show() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insertAtStart(5);
        list.insertAt(2, 15);
        list.show();
        list.deleteAt(3);
        list.show();
    }
}
