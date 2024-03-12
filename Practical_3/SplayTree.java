public class SplayTree {
    public Node root;
    /*
     * The functions below this line was given
     */

    @Override
    public String toString() {
        return (root == null ? "Empty Tree" : toString(root, "", true));
    }

    public String toString(Node node, String prefix, boolean end) {
        String res = "";
        if (node.right != null) {
            res += toString(node.right, prefix + (end ? "│   " : "    "), false);
        }
        res += prefix + (end ? "└── " : "┌── ") + node.toString() + "\n";
        if (node.left != null) {
            res += toString(node.left, prefix + (end ? "    " : "│   "), true);
        }
        return res;
    }

    public String toStringOneLine() {
        return (root == null ? "Empty Tree" : "{" + toStringOneLine(root) + "}");
    }

    public String toStringOneLine(Node node) {
        return node.toString()
                + (node.left == null ? "{}" : "{" + toStringOneLine(node.left) + "}")
                + (node.right == null ? "{}" : "{" + toStringOneLine(node.right) + "}");
    }

    public SplayTree() {
        root = null;
    }

    /*
     * The functions above this line was given
     */

     public SplayTree(String input) {
        if (input.equals("Empty Tree")) {
            root = null;
        } else {
            root = splayTreeHelper(input.substring(1, input.length() - 1));
        }
    }

    private Node splayTreeHelper(String nodes) {
        if (nodes.isEmpty()) {
            return null;
        }
    
        int start = nodes.indexOf('{');
        int end = nodes.lastIndexOf('}');

        if (start == -1 || start == end - 1) {
            return null;
        }
    
        String nodeData = nodes.substring(1, start - 1);
        String childData = nodes.substring(start, end + 1);
    
        String[] nodeParts = nodeData.split(":");
        Integer studentNumber = Integer.parseInt(nodeParts[0].substring(1));
        Integer mark = null;
        if (!nodeParts[1].equals("{}")) {
            String markString = nodeParts[1].substring(0, nodeParts[1].length() - 1);
            if (!markString.isEmpty()) {
                if (markString.equals("null")){
                    mark = null;
                }
                else {
                mark = Integer.parseInt(markString);
                }
            }
        }

        Node node = new Node(studentNumber, mark);
    
        if (!childData.equals("{}{}")) {
            int separator = findSeparator(childData);
            node.left = splayTreeHelper(childData.substring(1, separator));
            node.right = splayTreeHelper(childData.substring(separator + 2, childData.length() - 1));
        }
    
        return node;
    }
    

    private int findSeparator(String childData) {
        int balance = 0;
        for (int i = 0; i < childData.length(); i++) {
            if (childData.charAt(i) == '{') {
                balance++;
            } else if (childData.charAt(i) == '}') {
                balance--;
            }
            if (balance == 0) {
                return i;
            }
        }
        return balance;
    }

    public Node access(int studentNumber) {
        return access(studentNumber, null);
    }

    public Node access(int studentNumber, Integer mark) {
        Node node = findNode(root, studentNumber);
        if (node == null) {
            node = new Node(studentNumber, mark);
            root = insertNode(root, node);
        } else if (mark != null) {
            node.mark = mark;
        }
        root = treeSplay(root, studentNumber);
        return node;
    }

    public Node findNode(Node node, int studentNumber) {
        if (node == null) {
            return null;
        }

        int cmp = Integer.compare(studentNumber, node.studentNumber);
        if (cmp < 0) {
            return findNode(node.left, studentNumber);
        } else if (cmp > 0) {
            return findNode(node.right, studentNumber);
        } else {
            return node;
        }
    }

    private Node insertNode(Node node, Node newNode) {
        if (node == null) {
            return newNode;
        }
        int cmp = Integer.compare(newNode.studentNumber, node.studentNumber);
        if (cmp < 0) {
            node.left = insertNode(node.left, newNode);
        } else {
            node.right = insertNode(node.right, newNode);
        }
        return node;
    }

    private Node treeSplay(Node root, int key) {
        if (root == null || root.studentNumber == key)
            return root;

        if (root.studentNumber > key) {
            if (root.left == null)
                return root;
            if (root.left.studentNumber > key) {
                root.left.left = treeSplay(root.left.left, key);
                root = rotateRight(root);
            } else if (root.left.studentNumber < key) {
                root.left.right = treeSplay(root.left.right, key);
                if (root.left.right != null)
                    root.left = rotateLeft(root.left);
            }
            return (root.left == null) ? root : rotateRight(root);
        } else {
            if (root.right == null)
                return root;
            if (root.right.studentNumber > key) {
                root.right.left = treeSplay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rotateRight(root.right);
            } else if (root.right.studentNumber < key) {
                root.right.right = treeSplay(root.right.right, key);
                root = rotateLeft(root);
            }
            return (root.right == null) ? root : rotateLeft(root);
        }
    }

    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    public Node remove(int studentNumber) {
        Node node = access(studentNumber);
    
        if (node == null) {
            return null;
        }
    
        Node leftTree = root.left;
        Node rightTree = root.right;
    
        if (leftTree != null) {
            leftTree = treeSplay(leftTree, largestElInLeftSubTree(leftTree).studentNumber);
            leftTree.right = rightTree;
        }
    
        root = leftTree;
        return node;
    }
    

    private Node largestElInLeftSubTree(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public String sortByStudentNumber() {
        if (root == null) {
            return "Empty Tree";
        }

        StringBuilder sb = new StringBuilder();
        studentNumberSortHelper(root, sb);

        return sb.toString();
    }

    private void studentNumberSortHelper(Node node, StringBuilder sb) {
        if (node != null) {
            studentNumberSortHelper(node.left, sb);
            sb.append(node.toString());
            studentNumberSortHelper(node.right, sb);
        }
    }

    public String sortByMark() {
        if (root == null) {
            return "Empty Tree";
        }

        Node minMarkNode = findSmallestMark(root);
        StringBuilder result = new StringBuilder();
        int ret = 0;
        while (minMarkNode != null) {
            result.append(minMarkNode.toString());
            remove(minMarkNode.studentNumber);
            minMarkNode = findSmallestMark(root);
            ret++;
        }

        return result.toString();
    }

    private Node findSmallestMark(Node node) {
        if (node == null) {
            return null;
        }

        Node left = findSmallestMark(node.left);
        Node right = findSmallestMark(node.right);

        Node minNode = node;

        if (left != null && (left.mark < minNode.mark
                || (left.mark == minNode.mark && left.studentNumber < minNode.studentNumber))) {
            minNode = left;
        }

        if (right != null && (right.mark < minNode.mark
                || (right.mark == minNode.mark && right.studentNumber < minNode.studentNumber))) {
            minNode = right;
        }

        return minNode;
    }

}
