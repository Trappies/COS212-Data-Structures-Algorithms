
public class Clue extends GameObject {
    public String clueText;
    public int treasureObjectNumber;

    public Clue(int objectNumber, int treasureObjectNumber) {
        super(objectNumber);
        this.treasureObjectNumber = treasureObjectNumber;
        this.clueText = generateClue();
    }

    public Clue(int objectNumber, int treasureObjectNumber, String clueText) {
        super(objectNumber);
        this.treasureObjectNumber = treasureObjectNumber;
        this.clueText = clueText;
    }

    private String generateClue() {
        return generateRangeClue();
    }

    private String generateRangeClue() {
        return "The treasure is: " + ((treasureObjectNumber > this.objectNumber) ? ">" : "<=") + " "
                + this.objectNumber;

    }

    @Override
    public String visit() {
        this.visited = true;
        return "A clue: " + clueText;
    }

}
