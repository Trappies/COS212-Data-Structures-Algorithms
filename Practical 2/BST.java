public class BST<T extends Comparable<T>> {

    public BinaryNode<T> root;

    public BST() {
        this.root = null;
    }

    public void delete(T data) {
        this.root = recDelete(this.root, data);
    }

    private BinaryNode<T> recDelete(BinaryNode<T> node, T data) {
        if (node == null) {
            return null;
        }

        int res = data.compareTo(node.data);
        if (res < 0) {
            node.left = recDelete(node.left, data);
        } else if (res > 0) {
            node.right = recDelete(node.right, data);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            node.data = findMinDelete(node.right).data;
            node.right = recDelete(node.right, node.data);
        }
        return node;
    }

    private BinaryNode<T> findMinDelete(BinaryNode<T> node) {
        if (node.left == null) {
            return node;
        }
        return findMinDelete(node.left);
    }

    public boolean contains(T data) {
        return recContains(this.root, data);
    }

    private boolean recContains(BinaryNode<T> node, T data) {
        if (node == null) {
            return false;
        }

        int res = data.compareTo(node.data);
        if (res < 0) {
            return recContains(node.left, data);
        } else if (res > 0) {
            return recContains(node.right, data);
        } else {
            return true;
        }
    }

    public void insert(T data) {
        this.root = recInsert(this.root, data);
    }

    private BinaryNode<T> recInsert(BinaryNode<T> node, T data) {
        if (node == null) {
            return new BinaryNode<>(data);
        }

        int res = data.compareTo(node.data);
        if (res < 0) {
            node.left = recInsert(node.left, data);
        } else if (res > 0) {
            node.right = recInsert(node.right, data);
        }
        return node;
    }

    public int getHeight() {
        return recGetHeight(this.root);
    }

    private int recGetHeight(BinaryNode<T> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = recGetHeight(node.left);
        int rightHeight = recGetHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public String printSearchPath(T data) {
        StringBuilder pathBuilder = new StringBuilder();
        if (findPath(this.root, data, pathBuilder)) {
            return pathBuilder.toString();
        } else {
            pathBuilder.setLength(0);
            return appendNullPath(this.root, data, pathBuilder).toString();
        }
    }

    private boolean findPath(BinaryNode<T> node, T data, StringBuilder pathBuilder) {
        if (node == null) {
            return false;
        }

        if (pathBuilder.length() > 0) {
            pathBuilder.append(" -> ");
        }
        pathBuilder.append(node.data);

        if (node.data.equals(data)) {
            return true;
        }

        if (node.data.compareTo(data) < 0 && findPath(node.right, data, pathBuilder)) {
            return true;
        }

        if (node.data.compareTo(data) > 0 && findPath(node.left, data, pathBuilder)) {
            return true;
        }

        int lastArrowIndex = pathBuilder.lastIndexOf(" -> ");
        if (lastArrowIndex >= 0) {
            pathBuilder.setLength(lastArrowIndex);
        }

        return false;
    }

    private StringBuilder appendNullPath(BinaryNode<T> node, T data, StringBuilder pathBuilder) {
        if (node == null) {
            return pathBuilder.append(" -> Null");
        }

        if (pathBuilder.length() > 0) {
            pathBuilder.append(" -> ");
        }
        pathBuilder.append(node.data);

        if (node.data.compareTo(data) < 0
                && appendNullPath(node.right, data, pathBuilder).toString().endsWith("Null")) {
            return pathBuilder;
        }

        if (node.data.compareTo(data) > 0 && appendNullPath(node.left, data, pathBuilder).toString().endsWith("Null")) {
            return pathBuilder;
        }

        int lastArrowIndex = pathBuilder.lastIndexOf(" -> ");
        if (lastArrowIndex >= 0) {
            pathBuilder.setLength(lastArrowIndex);
        }

        return pathBuilder;
    }

    public int getNumLeaves() {
        return recGetNumLeaves(this.root);
    }

    private int recGetNumLeaves(BinaryNode<T> node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return recGetNumLeaves(node.left) + recGetNumLeaves(node.right);
    }

    public BST<T> extractBiggestSuperficiallyBalancedSubTree() {
        BST<T> subtree = new BST<>();
        subtree.root = recExtractBiggestSubTree(this.root);
        return subtree;
    }

    private BinaryNode<T> recExtractBiggestSubTree(BinaryNode<T> node) {
        if (node == null) {
            return null;
        }

        int countLeft = countNodes(node.left);
        int countRight = countNodes(node.right);

        if (countLeft == countRight) {
            return node;
        }

        BinaryNode<T> leftST = recExtractBiggestSubTree(node.left);
        BinaryNode<T> rightST = recExtractBiggestSubTree(node.right);

        return leftST != null && countNodes(leftST) >= countNodes(rightST) ? leftST : rightST;
    }

    public BinaryNode<T> getNode(T data) {
        return recGetNode(this.root, data);
    }

    private BinaryNode<T> recGetNode(BinaryNode<T> node, T data) {
        if (node == null || node.data.equals(data)) {
            return node;
        }
        int res = data.compareTo(node.data);
        if (res < 0) {
            return recGetNode(node.left, data);
        } else {
            return recGetNode(node.right, data);
        }
    }

    public boolean isSuperficiallyBalanced() {
        return recIsBalanced(this.root);
    }

    private boolean recIsBalanced(BinaryNode<T> node) {
        if (node == null) {
            return true;
        }

        int countLeft = countNodes(node.left);
        int countRight = countNodes(node.right);

        return countLeft == countRight && recIsBalanced(node.left) && recIsBalanced(node.right);
    }

    private int countNodes(BinaryNode<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public BinaryNode<T> findMax() {
        return recFindMax(this.root);
    }

    private BinaryNode<T> recFindMax(BinaryNode<T> node) {
        if (node == null) {
            return null;
        }
        if (node.right == null) {
            return node;
        }
        return recFindMax(node.right);
    }

    public BinaryNode<T> findMin() {
        return recFindMin(this.root);
    }

    private BinaryNode<T> recFindMin(BinaryNode<T> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node;
        }
        return recFindMin(node.left);
    }

    ///////////////

    private StringBuilder toString(BinaryNode<T> node, StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if (node.right != null) {
            toString(node.right, new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.data.toString()).append("\n");
        if (node.left != null) {
            toString(node.left, new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return root == null ? "Empty tree" : toString(root, new StringBuilder(), true, new StringBuilder()).toString();
    }

}
