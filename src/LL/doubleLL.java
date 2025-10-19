
class Node {
    int data;
    Node next;
    Node prev;

    Node(int data) {
        this.data = data;
    }
}

class DoublyLinkedList {
    Node head, tail;

    void insert(int data) {
        Node node = new Node(data);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    void insertAtStart(int data) {
        Node node = new Node(data);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    void insertAt(int index, int data) {
        if (index == 0) {
            insertAtStart(data);
            return;
        }
        Node temp = head;
        for (int i = 0; i < index - 1 && temp != null; i++) temp = temp.next;
        if (temp == null || temp.next == null) {
            insert(data);
            return;
        }
        Node node = new Node(data);
        node.next = temp.next;
        node.prev = temp;
        temp.next.prev = node;
        temp.next = node;
    }

    void deleteAt(int index) {
        if (head == null) return;
        if (index == 0) {
            if (head.next != null) head.next.prev = null;
            head = head.next;
            if (head == null) tail = null;
            return;
        }
        Node temp = head;
        for (int i = 0; i < index && temp != null; i++) temp = temp.next;
        if (temp == null) return;
        if (temp.prev != null) temp.prev.next = temp.next;
        if (temp.next != null) temp.next.prev = temp.prev;
        if (temp == tail) tail = temp.prev;
    }

    void showForward() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    void showBackward() {
        Node temp = tail;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.prev;
        }
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {
        DoublyLinkedList list = new DoublyLinkedList();
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insertAtStart(5);
        list.insertAt(2, 15);

        System.out.print("Forward: ");
        list.showForward();

        System.out.print("Backward: ");
        list.showBackward();

        list.deleteAt(2);
        System.out.print("After Deletion (Forward): ");
        list.showForward();
    }
}
