class TreeNode {
    int value;
    TreeNode left, right;

    TreeNode(int value) {
        this.value = value;
        left = right = null;
    }
}

class BinaryTree {
    TreeNode root;

    // Insert a value (simple binary search tree insertion)
    void insert(int value) {
        root = insertRec(root, value);
    }

    private TreeNode insertRec(TreeNode node, int value) {
        if (node == null) return new TreeNode(value);
        if (value < node.value) node.left = insertRec(node.left, value);
        else node.right = insertRec(node.right, value);
        return node;
    }


     private TreeNode insertRec(TreeNode node, int value) {
        if (node == null)
            return new TreeNode(value);

        // Try to fill left first
        if (node.left == null)
            node.left = insertRec(node.left, value);
        else if (node.right == null)
            node.right = insertRec(node.right, value);
        else {
            // Both sides filled — go deeper on the left side
            node.left = insertRec(node.left, value);
        }

        return node;
    }

    void insert(int value) {
        TreeNode newNode = new TreeNode(value);
        if (root == null) {
            root = newNode;
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode temp = queue.poll();

            if (temp.left == null) {
                temp.left = newNode;
                break;
            } else queue.add(temp.left);

            if (temp.right == null) {
                temp.right = newNode;
                break;
            } else queue.add(temp.right);
        }
    }

    // Search for a value
    boolean search(int value) {
        return searchRec(root, value);
    }

    private boolean searchRec(TreeNode node, int value) {
        if (node == null) return false;
        if (node.value == value) return true;
        if (value < node.value) return searchRec(node.left, value);
        return searchRec(node.right, value);
    }

    // In-order traversal
    void inOrder() {
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(TreeNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }

    // Pre-order traversal
    void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(TreeNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    // Post-order traversal
    void postOrder() {
        postOrderRec(root);
        System.out.println();
    }

    private void postOrderRec(TreeNode node) {
        if (node != null) {
            postOrderRec(node.left);
            postOrderRec(node.right);
            System.out.print(node.value + " ");
        }
    }

    // Height of the tree
    int height() {
        return heightRec(root);
    }

    private int heightRec(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }
}

public class Main {
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        System.out.print("In-order: ");
        tree.inOrder();

        System.out.print("Pre-order: ");
        tree.preOrder();

        System.out.print("Post-order: ");
        tree.postOrder();

        System.out.println("Search 60: " + tree.search(60));
        System.out.println("Tree height: " + tree.height());
    }
}
