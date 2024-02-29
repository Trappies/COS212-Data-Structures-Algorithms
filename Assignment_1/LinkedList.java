public class LinkedList {
    public CoordinateNode head;

    public LinkedList() {
        this.head = null;
    }

    public LinkedList(int x, int y) {
        this.head = new CoordinateNode(x, y);
    }

    public void append(int x, int y) {
        if (this.head == null) {
            this.head = new CoordinateNode(x, y);
        }
        else
        {
            recAppend(this.head, x, y);
        }
    }

    private void recAppend(CoordinateNode node, int x, int y) {
        if (node.next == null) {
            node.next = new CoordinateNode(x, y);
        }
        else
        {
            recAppend(node.next, x, y);
        }
    }

    public void appendList(LinkedList other) {
        if (other.head != null) {
            recAppendList(this.head, other.head);
        }
    }

    private void recAppendList(CoordinateNode node, CoordinateNode otherNode) {
        if (node.next == null) {
            node.next = new CoordinateNode(otherNode.x, otherNode.y);
        }
        else
        {
            recAppendList(node.next, otherNode);
        }
    }

    public boolean contains(int x, int y) {
       return recContains(this.head, x, y);
    }

    private boolean recContains(CoordinateNode node, int x, int y) {
        if (node == null) {
            return false;
        }
        if (node.x == x && node.y == y) {
            return true;
        }
        return recContains(node.next, x, y);
    }

    @Override
    public String toString() {
        if (head == null) {
            return "Empty List";
        }
        else 
        {
            return recToString(this.head);
        }
    }

    private String recToString(CoordinateNode node) {
        if (node.next == null) {
            // Possibly add []
            return node.toString();
        }
        else
        {
            return node.toString() + " -> " + recToString(node.next);
        }
    }

    public int length() {
        return recLength(this.head);
    }

    private int recLength(CoordinateNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + recLength(node.next);
    }

    public LinkedList reversed() {
        LinkedList reversed = new LinkedList();
        reversed.head = recReverse(this.head);
        return reversed;
    }

    private CoordinateNode recReverse(CoordinateNode node) {
        if (node == null || node.next == null) {
            return node;
        }

        CoordinateNode revHead = recReverse(node.next);
        node.next.next = node;
        node.next = null;
        return revHead;
    }

    public void removeLast() {
        if (head == null) {
            return; // List is empty, nothing to remove
        }
        head = removeLastRecursive(head);
    }
    
    private CoordinateNode removeLastRecursive(CoordinateNode currentNode) {
        if (currentNode.next == null) {
            return null; // Reached the last node, return null to indicate removal
        }
        currentNode.next = removeLastRecursive(currentNode.next);
        return currentNode;
    }
    
}
