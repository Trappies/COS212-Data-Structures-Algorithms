public abstract class GameObject implements Comparable<GameObject> {
    public int objectNumber;
    public boolean visited;

    public GameObject(int objectNumber) {
        this.objectNumber = objectNumber;
        this.visited = false;
    }

    public abstract String visit();

    @Override
    public int compareTo(GameObject o) {
        if (o.objectNumber == this.objectNumber) {
            return 0;
        } else {
            if (o.objectNumber < this.objectNumber) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public String toString() {
        if (this.visited) {
            return this.objectNumber + "";
        }
        return "?";

    }
}
