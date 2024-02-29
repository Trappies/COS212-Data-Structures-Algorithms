public class CoordinateNode {
    public int x;
    public int y;
    public CoordinateNode next;

    CoordinateNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.next = null;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
