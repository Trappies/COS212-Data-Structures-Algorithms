public class BTree<T extends Comparable<T>> {
    public BTreeNode<T> root;
    public int m;

    public BTree(int m) {
        this.m = m;
        this.root = null;
    }

    public void insert(T data) {
        if (root == null) {
            root = new BTreeNode<>(m);
            root.nodeData[0] = data;
            return;
        }
    
        BTreeNode<T> current = root;
        BTreeNode<T> parent = null;
    
        while (current != null) {
            if (current.nodeChildren[0] == null) {
                int i = 0;
                while (i < current.nodeData.length && current.nodeData[i] != null
                        && current.nodeData[i].compareTo(data) < 0) {
                    i++;
                }
                for (int j = current.nodeData.length - 1; j > i; j--) {
                    current.nodeData[j] = current.nodeData[j - 1];
                }
                current.nodeData[i] = data;
                if (current.nodeData[m - 1] != null) {
                    split(parent, current);
                }
                return;
            } else {
                parent = current;
                int i = 0;
                while (i < current.nodeData.length && current.nodeData[i] != null
                        && current.nodeData[i].compareTo(data) < 0) {
                    i++;
                }
                current = current.descend(i);
            }
        }
    }
    

    private void split(BTreeNode<T> parent, BTreeNode<T> node) {
        int midIndex = node.size / 2;
        T midValue = (T) node.nodeData[midIndex];
        BTreeNode<T> left = new BTreeNode<>(m);
        BTreeNode<T> right = new BTreeNode<>(m);

        System.arraycopy(node.nodeData, 0, left.nodeData, 0, midIndex);
        System.arraycopy(node.nodeData, midIndex + 1, right.nodeData, 0, m - midIndex - 1);

        if (node.nodeChildren[0] != null) {
            System.arraycopy(node.nodeChildren, 0, left.nodeChildren, 0, midIndex + 1);
            System.arraycopy(node.nodeChildren, midIndex + 1, right.nodeChildren, 0, m - midIndex);
        }

        if (parent == null) {
            root = new BTreeNode<>(m);
            root.nodeData[0] = midValue;
            root.nodeChildren[0] = left;
            root.nodeChildren[1] = right;
            left.parent = root;
            right.parent = root;
        } else {
            int i = 0;
            while (i < parent.nodeData.length && parent.nodeData[i] != null
                    && parent.nodeData[i].compareTo(midValue) < 0) {
                i++;
            }
            System.arraycopy(parent.nodeData, i, parent.nodeData, i + 1, parent.size - i - 1);
            System.arraycopy(parent.nodeChildren, i + 1, parent.nodeChildren, i + 2, parent.size - i - 1);
            parent.nodeData[i] = midValue;
            parent.nodeChildren[i] = left;
            parent.nodeChildren[i + 1] = right;

            left.parent = parent;
            right.parent = parent;

            if (i >= parent.size - 1) {
                split(parent.parent, parent);
            }
        }
    }

    public String printPath(T key) {
        StringBuilder path = new StringBuilder();
        BTreeNode<T> current = root;

        while (current != null) {
            int i = 0;
            while (i < current.nodeData.length && current.nodeData[i] != null
                    && current.nodeData[i].compareTo(key) < 0) {
                i++;
            }
            if (i < current.nodeData.length && current.nodeData[i] != null && current.nodeData[i].compareTo(key) == 0) {
                path.append(current.nodeData[i]).append(" ");
                return path.toString().trim();
            }
            if (i < current.nodeData.length && current.nodeData[i] != null) {
                path.append(current.nodeData[i]).append(" -> ");
            } else if (i > 0 && current.nodeData[i - 1] != null) {
                path.append(current.nodeData[i - 1]).append(" -> ");
            }
            if (i < current.nodeChildren.length && current.nodeChildren[i] != null) {
                current = current.nodeChildren[i];
            } else {
                path.append("Null");
                break;
            }
        }

        return path.toString().trim();

    }

    /* -------------------------------------------------------------------------- */
    /* Please don't change this toString, I tried to make it pretty. */
    /* -------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------- */
    /* Also we may test against it */
    /* -------------------------------------------------------------------------- */

    @Override
    public String toString() {
        if (root == null) {
            return "The B-Tree is empty";
        }
        StringBuilder builder = new StringBuilder();
        buildString(root, builder, "", true);
        return builder.toString();
    }

    private void buildString(BTreeNode<T> node, StringBuilder builder, String prefix, boolean isTail) {
        if (node == null)
            return;

        builder.append(prefix).append(isTail ? "└── " : "├── ");
        for (int i = 0; i < node.nodeData.length; i++) {
            if (node.nodeData[i] != null) {
                builder.append(node.nodeData[i]);
                if (i < node.nodeData.length - 1 && node.nodeData[i + 1] != null) {
                    builder.append(", ");
                }
            }

        }
        if (node.parent != null)
            builder.append("\t(p:" + node.parent.nodeData[0].toString() + ")");
        builder.append("\n");

        int numberOfChildren = m;
        for (int i = 0; i < numberOfChildren; i++) {

            BTreeNode<T> child = node.descend(i);
            buildString(child, builder, prefix + (isTail ? "    " : "│   "), i == numberOfChildren - 1);
        }
    }
}
