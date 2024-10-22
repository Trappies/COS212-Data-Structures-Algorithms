public class Empty extends GameObject {

    public Empty(int objectNumber) {
        super(objectNumber);
    }

    @Override
    public String visit() {
        this.visited = true;
        return "An empty space, silent and still.";
    }
}
