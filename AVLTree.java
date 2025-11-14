/**
 * @author Khalil El-abbassi, Benjamin Gutowski, Robert Ngo, Timothy Posley
 */
public class AVLTree {

    public static class Node {

        public int orderID;
        public String name;
        public Node left;
        public Node right;

        public Node(int orderID, String name) {
            this.orderID = orderID;
            this.name = name;
        }
    }

    private Node root;
    private int nodes;

    private int findHeight(Node n) {
        if (n == null) {
            return -1;
        } else {
            return 1 + Math.max(findHeight(n.left), findHeight(n.right));
        }
    }

    private int findBF(Node n) {
        return findHeight(n.left) - findHeight(n.right);
    }

    private void printTreeData() {
        System.out.println("Tree height: " + (1 + findHeight(this.root)));
        System.out.println("Number of nodes: " + nodes + "\n");
    }

    private Node leftRotation(Node n) {
        Node newRoot = n.right;
        Node displacedNode = newRoot.left;
        newRoot.left = n;
        n.right = displacedNode;
        return newRoot;
    }

    private Node rightRotation(Node n) {
        Node newRoot = n.left;
        Node displacedNode = newRoot.right;
        newRoot.right = n;
        n.left = displacedNode;
        return newRoot;
    }

    private Node balance(Node n) {
        if (n == null) {
            return n;
        }
        int bf = findBF(n);
        if (bf < -1) {
            if (findBF(n.right) > 0) {
                n.right = rightRotation(n.right);
            }
            n = leftRotation(n);
        } else if (bf > 1) {
            if (findBF(n.left) < 0) {
                n.left = leftRotation(n.left);
            }
            n = rightRotation(n);
        }
        return n;
    }

    public void addBookOrder(String name, int orderID) {
        if (orderID < 0) {
            System.out.println("Couldn't add (" + orderID + ")- " + name + ": Order number can't be negative!\n");
        } else if (name.length() > 150) {
            System.out.println("Couldn't add (" + orderID + ")- " + name + ": Order's book name can't be over 150 characters!\n");
        } else if (nodes >= 100) {
            System.out.println("Couldn't add (" + orderID + ")- " + name + ": Order storage is at maximum capacity (100 orders)\n");
        } else {
            this.root = addBookOrder(name, orderID, this.root);
        }
        printTreeData();
    }

    private Node addBookOrder(String name, int orderID, Node n) {
        if (n == null) {
            n = new Node(orderID, name);
            System.out.println("Successfully added book order: " + orderID + "- " + name + "\n");
            nodes++;
        } else if (n.orderID > orderID) {
            n.left = addBookOrder(name, orderID, n.left);
        } else if (n.orderID < orderID) {
            n.right = addBookOrder(name, orderID, n.right);
        } else {
            System.out.println("Couldn't add (" + orderID + ")- " + name + ": Order number already exists!\n");
        }
        n = balance(n);
        return n;
    }

    public void removeBookOrder(int orderID) {
        this.root = removeBookOrder(orderID, root);
        printTreeData();
    }

    private Node removeBookOrder(int orderID, Node n) {
        if (n == null) {
            System.out.println("Couldn't remove Order Number " + orderID + ": It doesn't exist!\n");
            return null;
        }
        if (n.orderID == orderID) {
            String curName = n.name;
            if (n.left != null && n.right != null) {
                Node rightmost = n.left;
                while (rightmost.right != null) {
                    rightmost = rightmost.right;
                }
                n.left = removeRightmostNode(n.left);
                Node leftTemp = n.left;
                Node rightTemp = n.right;
                n = rightmost;
                n.left = leftTemp;
                n.right = rightTemp;
            } else if (n.left != null) {
                n = n.left;
            } else {
                n = n.right;
            }
            nodes--;
            System.out.println("Successfully removed the book order: " + orderID + "- " + curName + "\n");
        } else if (n.orderID > orderID) {
            n.left = removeBookOrder(orderID, n.left);
        } else if (n.orderID < orderID) {
            n.right = removeBookOrder(orderID, n.right);
        }
        n = balance(n);
        return n;
    }

    private Node removeRightmostNode(Node n) {
        if (n.right != null) {
            n.right = removeRightmostNode(n.right);
        } else {
            return n.left;
        }
        return n;
    }

    public void getName(int orderID) {
        String orderName = getName(orderID, this.root);
        if (orderName == null) {
            System.out.println("Order Number " + orderID + " doesn't exist!\n");
        } else {
            System.out.println("Order Number " + orderID + " is named: " + orderName + "\n");
        }
        printTreeData();
    }

    private String getName(int orderID, Node n) {
        if (n == null) {
            return null;
        } else if (n.orderID == orderID) {
            return n.name;
        } else if (n.orderID < orderID) {
            return getName(orderID, n.right);
        } else {
            return getName(orderID, n.left);
        }
    }

    public void printBookOrders() {
        if (this.root == null) {
            System.out.println("There are no book orders!\n");
        } else {
            System.out.println("LIST OF ORDERS:");
            printBookOrders(this.root);
            System.out.println();
        }
        printTreeData();
    }

    private void printBookOrders(Node root) {
        if (root == null) {
            return;
        }
        printBookOrders(root.left);
        System.out.println(root.orderID + "- " + root.name);
        printBookOrders(root.right);
    }

    public void findOldestBookOrder() {
        Node curOrder = this.root;
        if (curOrder == null) {
            System.out.println("There are no book orders!\n");
        } else {
            while (curOrder.left != null) {
                curOrder = curOrder.left;
            }
            System.out.println("Oldest book order:");
            System.out.println(curOrder.orderID + "- " + curOrder.name + "\n");
        }
        printTreeData();
    }

    public void findLatestBookOrder() {
        Node curOrder = this.root;
        if (curOrder == null) {
            System.out.println("There are no book orders!\n");
        } else {
            while (curOrder.right != null) {
                curOrder = curOrder.right;
            }
            System.out.println("Latest book order:");
            System.out.println(curOrder.orderID + "- " + curOrder.name + "\n");
        }
        printTreeData();
    }

}
