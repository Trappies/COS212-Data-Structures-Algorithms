public class RedBlackTree<T extends Comparable<T>> {

    /*
     * Sentinel is not the root. Go check the text book if this doesn't make sense
     */
    public RedBlackNode<T> SENTINEL;
    public RedBlackNode<T> NULL_NODE;

    public static final int RED = 1;
    public static final int BLACK = 0;

    public RedBlackTree() {
        SENTINEL = new RedBlackNode<>(null);
        NULL_NODE = new RedBlackNode<>(null);
        SENTINEL.right = NULL_NODE;
    }

    public RedBlackNode<T> getRoot() {
        if (SENTINEL.right == NULL_NODE) {
            return NULL_NODE;
        } else {
            return SENTINEL.right;
        }
    }

    public void bottomUpInsert(T data) {
        RedBlackNode<T> newNode = new RedBlackNode<>(data);

        RedBlackNode<T> parent = null;
        RedBlackNode<T> current = SENTINEL.right;

        while (current != NULL_NODE) {
            parent = current;
            int cmp = data.compareTo(current.data);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return;
            }
        }
        newNode.colour = RED;
        newNode.left = NULL_NODE;
        newNode.right = NULL_NODE;
        newNode.parent = parent;

        if (parent == null) {
            SENTINEL.right = newNode;
        } else {
            int cmp = data.compareTo(parent.data);
            if (cmp < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }

        fixColours(newNode);
    }

    private void fixColours(RedBlackNode<T> node) {
        while (node != SENTINEL.right && node.parent.colour == RED) {
            if (node.parent == node.parent.parent.left) {
                RedBlackNode<T> uncle = node.parent.parent.right;
                if (uncle.colour == RED) {
                    node.parent.colour = BLACK;
                    uncle.colour = BLACK;
                    node.parent.parent.colour = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        leftRotations(node);
                    }
                    node.parent.colour = BLACK;
                    node.parent.parent.colour = RED;
                    rightRotations(node.parent.parent);
                }
            } else {
                RedBlackNode<T> uncle = node.parent.parent.left;
                if (uncle.colour == RED) {
                    node.parent.colour = BLACK;
                    uncle.colour = BLACK;
                    node.parent.parent.colour = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotations(node);
                    }
                    node.parent.colour = BLACK;
                    node.parent.parent.colour = RED;
                    leftRotations(node.parent.parent);
                }
            }
        }
        SENTINEL.right.colour = BLACK;
    }

    private void leftRotations(RedBlackNode<T> node) {
        RedBlackNode<T> rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != NULL_NODE) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            SENTINEL.right = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rightRotations(RedBlackNode<T> node) {
        RedBlackNode<T> leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != NULL_NODE) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            SENTINEL.right = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    public boolean isValidRedBlackTree() {
        return isValidRedBlackTreeHelper(getRoot()) != -1;
    }

    private int isValidRedBlackTreeHelper(RedBlackNode<T> node) {
        int t = 0;
        int f = -1;

        if (node == NULL_NODE) {
            return t;
        }

        if (node.colour != RED && node.colour != BLACK) {
            return f;
        }

        if (node == getRoot() && node.colour != BLACK) {
            return f;
        }

        if (node.colour == RED && (node.left.colour != BLACK || node.right.colour != BLACK)) {
            return f;
        }

        int leftSTHeight = isValidRedBlackTreeHelper(node.left);
        int rightSTHeight = isValidRedBlackTreeHelper(node.right);

        if (leftSTHeight == -1 || rightSTHeight == -1 || leftSTHeight != rightSTHeight) {
            return f;
        }

        int blackTreeHeight = (node.colour == BLACK) ? leftSTHeight + 1 : leftSTHeight;

        return blackTreeHeight;
    }

    public void topDownDelete(T data) {
        RedBlackNode<T> nodeToDelete = searchForNode(data);
    if (nodeToDelete == NULL_NODE) {
        return;
    }

    RedBlackNode<T> child = (nodeToDelete.right == NULL_NODE) ? nodeToDelete.left : nodeToDelete.right;

    if (nodeToDelete.left != NULL_NODE && nodeToDelete.right != NULL_NODE) {
        RedBlackNode<T> successor = findMinimum(nodeToDelete.right);
        nodeToDelete.data = successor.data;
        nodeToDelete = successor;
        child = nodeToDelete.right != NULL_NODE ? nodeToDelete.right : nodeToDelete.left;
    }
    child.parent = nodeToDelete.parent;
    nodeVervang(nodeToDelete, child);

    if (nodeToDelete.colour == BLACK) {
        if (child.colour == RED) {
            child.colour = BLACK;
        } else {
            fixDeletionViolations(child);
        }
    }
    }

    private RedBlackNode<T> searchForNode(T data) {
        RedBlackNode<T> current = SENTINEL.right;

        while (current != NULL_NODE) {
            int cmp = data.compareTo(current.data);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return NULL_NODE;
    }

    private RedBlackNode<T> findMinimum(RedBlackNode<T> node) {
        while (node.left != NULL_NODE) {
            node = node.left;
        }
        return node;
    }

    private void fixDeletionViolations(RedBlackNode<T> node) {
        int count = 0;
        while (node != SENTINEL.right && node.colour == BLACK) {
            count++;
            if (node == node.parent.left) {
                RedBlackNode<T> sibling = node.parent.right;
                if (sibling.colour == RED) {
                    sibling.colour = BLACK;
                    node.parent.colour = RED;
                    leftRotations(node.parent);
                    sibling = node.parent.right;
                }
                if (sibling.left.colour == BLACK && sibling.right.colour == BLACK) {
                    sibling.colour = RED;
                    node = node.parent;
                } else {
                    if (sibling.right.colour == BLACK) {
                        sibling.left.colour = BLACK;
                        sibling.colour = RED;
                        rightRotations(sibling);
                        sibling = node.parent.right;
                    }
                    sibling.colour = node.parent.colour;
                    node.parent.colour = BLACK;
                    sibling.right.colour = BLACK;
                    leftRotations(node.parent);
                    node = SENTINEL.right;
                }
            } else {
                RedBlackNode<T> sibling = node.parent.left;
                if (sibling.colour == RED) {
                    sibling.colour = BLACK;
                    node.parent.colour = RED;
                    rightRotations(node.parent);
                    sibling = node.parent.left;
                }

                if (sibling.right.colour == BLACK && sibling.left.colour == BLACK) {
                    sibling.colour = RED;
                    node = node.parent;
                } else {
                    if (sibling.left.colour == BLACK) {
                        sibling.right.colour = BLACK;
                        sibling.colour = RED;
                        leftRotations(sibling);
                        sibling = node.parent.left;
                    }

                    sibling.colour = node.parent.colour;
                    node.parent.colour = BLACK;
                    sibling.left.colour = BLACK;
                    rightRotations(node.parent);
                    node = SENTINEL.right;
                }
            }
        }
        node.colour = BLACK;
    }

    private void nodeVervang(RedBlackNode<T> node, RedBlackNode<T> child) {
        if (node.parent == null) {
            SENTINEL.right = child;
        } else if (node == node.parent.left) {
            node.parent.left = child;
        } else {
            node.parent.right = child;
        }
        child.parent = node.parent;
    }

    /* -------------------------------------------------------------------------- */
    /* Private methods, which shouldn't be called from outside the class */
    /* -------------------------------------------------------------------------- */

    /* -------------------------------------------------------------------------- */
    /* Please don't change this toString, I worked really hard to make it pretty. */
    /* Also, it matches the website. -------------------------------------------- */
    /* Also, also, we might test against it ;) ---------------------------------- */
    /* -------------------------------------------------------------------------- */
    private StringBuilder toString(RedBlackNode<T> node, StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if (node.right != NULL_NODE) {
            toString(node.right, new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.toString()).append("\n");
        if (node.left != NULL_NODE) {
            toString(node.left, new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return SENTINEL.right == NULL_NODE ? "Empty tree"
                : toString(SENTINEL.right, new StringBuilder(), true, new StringBuilder()).toString();
    }

    public String toVis() {
        return toVisHelper(getRoot());
    }

    private String toVisHelper(RedBlackNode<T> node) {
        if (node == NULL_NODE) {
            return "{}";
        }
        String leftStr = toVisHelper(node.left);
        String rightStr = toVisHelper(node.right);
        return "{" + node.toString() + leftStr + rightStr + "}";
    }

}
