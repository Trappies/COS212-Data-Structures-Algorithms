import java.util.Random;

public class SemiRandomPlayer extends Player {
    private Random random;
    private Hashmap<String, String> possibleAreas;
    private String jumpIntent;

    public SemiRandomPlayer(PlayerMap gameMap, long seed) {
        super(gameMap);
        this.random = new Random(seed);
        this.possibleAreas = new Hashmap<>();
        this.jumpIntent = "";
        for (Object o : gameMap.areas.getKeys()) {
            this.possibleAreas.insert((String) o, (String) o);
        }
    }

    @Override
    public void takeTurn() {
        if (gameMap.currentNode != null && gameMap.currentNode.nodeData.length > gameMap.currentIndexInNode
                && gameMap.currentNode.nodeData[gameMap.currentIndexInNode] != null) {

            GameObject currentObject = (GameObject) gameMap.currentNode.nodeData[gameMap.currentIndexInNode];
            String message = currentObject.visit();
            processMessage(message);
        }

        // Decision making for the next action based on processed clues
        decideNextAction();
    }

    private void processMessage(String message) {
        if (message.startsWith("A clue: Location is: not")) {
            // Remove this area from possible areas
            String area = message.substring(25, message.length() - 1);
            possibleAreas.delete(area);
        } else if (message.startsWith("A clue: Location is:")) {
            // Narrow down to this area
            String area = message.substring(21, message.length() - 1);
            possibleAreas.clear();
            possibleAreas.insert(area, area);
            this.jumpIntent = area;
        }
    }

    private void decideNextAction() {
        // If we have narrowed down to one area then select it
        if (possibleAreas.numValues == 1 && !gameMap.currentAreaName.equals(this.jumpIntent)) {

            gameMap.takeAction(PlayerMap.Action.J);
            return; // Stop here to avoid making another move in the same turn
        } else if (gameMap.currentAreaName.equals("")) {
            // If we're not in any area, jump to a default area
            this.jumpIntent = "swamp"; // Default starting area or choose randomly
            gameMap.takeAction(PlayerMap.Action.J);
            return; // Stop here to avoid making another move in the same turn
        }
        PlayerMap.Action[] actions = PlayerMap.Action.values();
        PlayerMap.Action action = null;
        boolean valid = false;
        while (!valid) {
            valid = true;
            int actionIndex = random.nextInt(actions.length - 1);
            action = actions[actionIndex];
            switch (action) {
                case N:
                    valid = canMoveNext();
                    break;
                case P:
                    valid = canMovePrev();
                    break;
                case A:
                    valid = canAscend();
                    break;
                case D:
                    valid = canDescend();
                    break;
                case J:
                    valid = false;
                    break;
            }
        }
        // Execute the action
        gameMap.takeAction(action);
    }

    private boolean canMoveNext() {
        return gameMap.currentNode != null && gameMap.currentIndexInNode + 1 < gameMap.currentNode.nodeData.length;
    }

    private boolean canMovePrev() {
        return gameMap.currentNode != null && gameMap.currentIndexInNode - 1 >= 0;
    }

    private boolean canAscend() {
        return gameMap.currentNode != null && gameMap.currentNode.parent != null;
    }

    private boolean canDescend() {
        return gameMap.currentNode != null && gameMap.currentNode.descend(gameMap.currentIndexInNode) != null;
    }

    @Override
    public void selectArea() {
        gameMap.selectArea(jumpIntent);
    }

    @Override
    public void close() {
        // Cleanup anything you use here
        // HumanPlayer uses this to close its scanner
    }

}
