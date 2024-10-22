public class BTreeNode<T extends Comparable<T>> {
    public Comparable<T>[] nodeData;
    public BTreeNode<T>[] nodeChildren;
    public BTreeNode<T> parent;
    public int size;

    @SuppressWarnings("unchecked")
    public BTreeNode(int size) {
        this.size = size;
        this.nodeData = new Comparable[size];
        this.nodeChildren = new BTreeNode[size + 1];
        this.parent = null;

    }

    public Comparable<T> getIndex(int i) {
        if (i < 0 || i >= nodeData.length) {
            return null;
        }
        return nodeData[i];
    }

    public BTreeNode<T> ascend() {
        return this.parent;
    }

    public BTreeNode<T> descend(int i) {
        if (i < 0 || i >= nodeChildren.length) {
            return null;
        }
        return nodeChildren[i];
    }


    /* -------------------------------------------------------------------------- */
    /* Helpers */
    /* -------------------------------------------------------------------------- */

    public String toString() {
        String out = "|";
        for (int i = 0; i < nodeData.length; i++) {
            if (nodeData[i] == null) {
                out += "null|";
            } else {
                out += nodeData[i].toString() + "|";
            }

        }
        return out;
    }

}
