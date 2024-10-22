public class Treasure extends GameObject {

    public Treasure(int objectNumber) {
        super(objectNumber);
    }

    @Override
    public String visit() {
        this.visited = true;
        return "The treasure! (" + objectNumber + ")";
    }
}
