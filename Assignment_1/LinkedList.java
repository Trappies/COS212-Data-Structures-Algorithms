public class LinkedList {
    public CoordinateNode head;

    public LinkedList() {
        this.head = null;
    }

    public LinkedList(int x, int y) {
        this.head = new CoordinateNode(x, y);
    }

    private void append(CoordinateNode current, int x, int y) {
        if (current.next == null) {
            current.next = new CoordinateNode(x, y);
        } else {
            append(current.next, x, y);
        }
    }

    public void append(int x, int y) {
        if (head == null) {
            head = new CoordinateNode(x, y);
        } else {
            append(head, x, y);
        }
    }

    public void appendList(LinkedList other) {
        if (other.head != null) {
            appendListHelper(other.head, this.head);
        }
    }

    private void appendListHelper(CoordinateNode current, CoordinateNode currentHead) {
        if (current != null) {
            if (!this.contains(current.x, current.y)) {
                append(current.x, current.y);
            }
            appendListHelper(current.next, currentHead);
        }
    }

    private boolean contains(CoordinateNode current, int x, int y) {
        if (current == null) {
            return false;
        } else if (current.x == x && current.y == y) {
            return true;
        } else {
            return contains(current.next, x, y);
        }
    }

    public boolean contains(int x, int y) {
        return contains(head, x, y);
    }

    private String recToString(CoordinateNode node) {
        if (node.next == null) {
            return node.toString();
        } else {
            return node.toString() + " -> " + recToString(node.next);
        }
    }

    @Override
    public String toString() {
        if (head == null) {
            return "Empty List";
        } else {
            return recToString(this.head);
        }
    }

    private int length(CoordinateNode current) {
        if (current == null) {
            return 0;
        } else {
            return 1 + length(current.next);
        }
    }

    public int length() {
        return length(head);
    }

    private void reversed(CoordinateNode current, LinkedList reversedList) {
        if (current != null) {
            reversed(current.next, reversedList);
            reversedList.append(current.x, current.y);
        }
    }

    public LinkedList reversed() {
        LinkedList reversedList = new LinkedList();
        reversed(head, reversedList);
        return reversedList;
    }
}
