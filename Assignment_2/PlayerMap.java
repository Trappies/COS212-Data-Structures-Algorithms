import java.util.Random;

public class PlayerMap {
    public String currentAreaName;
    public Hashmap<String, BTree<GameObject>> areas;
    public BTree<GameObject> currentArea;
    public BTreeNode<GameObject> currentNode;
    public int currentIndexInNode;
    public int moves;
    public Player player;

    public enum Action {
        N, P, A, D, J
    }

    public enum PlayerType {
        HUMAN, AUTO, RANDOM
    }

    public PlayerMap(int roughSize, long seed, PlayerType type) {

        this.moves = 0;
        this.areas = new Hashmap<>();
        this.currentAreaName = "";
        Random random = new Random(seed);

        // Define a list of area names
        String[] areaNames = { "swamp", "forest", "cave", "ruins" };
        String treasureArea = areaNames[random.nextInt(areaNames.length)]; // Randomly select the area for the treasure

        int treasureId = random.nextInt(10000); // Generates a number between 0 and 9999
        System.out.println(treasureId);
        for (String areaName : areaNames) {
            BTree<GameObject> area = new BTree<>(5);

            // Insert a mix of Empty objects and clues
            for (int i = 0; i < roughSize; i++) {
                // Randomly decide to insert an Empty object or a Clue
                if (random.nextBoolean()) {
                    area.insert(new Empty(random.nextInt(10000))); // Use a random ID for Empty objects
                } else {
                    if (random.nextBoolean()) {
                        for (String string : areaNames) {
                            // Generate clues about the treasure area
                            String clueText = "Location is: not " + string + ".";
                            if (string.equals(treasureArea)) {
                                clueText = "Location is: " + string + ".";
                            }
                            area.insert(new Clue(random.nextInt(10000), treasureId, clueText));
                        }
                    } else {
                        // Generate clues about the treasure key
                        for (int j = 0; j < 5; j++) {
                            area.insert(new Clue(random.nextInt(10000), treasureId));
                        }
                    }

                }
            }

            // If this is the treasure area, insert the treasure
            if (areaName.equals(treasureArea)) {
                area.insert(new Treasure(treasureId));
            }

            areas.insert(areaName, area);
        }
        switch (type) {
            case HUMAN:
                this.player = new HumanPlayer(this);
                break;
            case AUTO:
                this.player = new AutomatedPlayer(this);
                break;
            case RANDOM:
                this.player = new SemiRandomPlayer(this, seed + 1);
                break;
        }

    }

    public void play() {
        boolean playing = true;

        // Basic introduction
        System.out.println(
                "Exploring the area. Use (N)EXT, (P)REVIOUS, (A)SCEND, (D)ESCEND, (J)UMP to navigate or QUIT to exit.\nAreas available are:");
        for (Object name : areas.getKeys()) {
            System.out.println("-> " + (String) name);
        }

        while (playing) {
            if (currentNode != null) {
                GameObject currentObject = (GameObject) currentNode.nodeData[currentIndexInNode];
                if (currentObject instanceof Treasure) {
                    System.out.println(currentObject.visit());
                    System.out.println("Congratulations!");
                    break;
                }
            }

            player.takeTurn();
        }
        player.close();
    }

    public void selectArea(String areaName) {
        BTree<GameObject> retVal = areas.get(areaName);
        if (retVal == null) {
            System.out.println("No such area");
            return;
        }
        this.currentAreaName = areaName;
        this.currentArea = retVal;
        this.currentNode = currentArea.root;
        this.currentIndexInNode = 0;
    }

    public void takeAction(Action action) {
        moves++;
        switch (action) {
            case N:
                if (currentNode != null && currentIndexInNode + 1 < currentNode.nodeChildren.length - 1) {
                    currentIndexInNode++;
                } else {
                    System.out.println("Action currently invalid");
                }
                break;
            case P:
                if (currentNode != null && currentIndexInNode - 1 >= 0) {
                    currentIndexInNode--;
                } else {
                    System.out.println("Action currently invalid");
                }
                break;
            case A:
                if (currentNode != null && currentNode.parent != null) {
                    this.currentNode = currentNode.parent;
                    this.currentIndexInNode = 0;
                } else {
                    System.out.println("Action currently invalid");
                }
                break;
            case D:
                if (currentNode != null && currentNode.descend(currentIndexInNode) != null) {
                    this.currentNode = currentNode.descend(currentIndexInNode);
                    this.currentIndexInNode = 0;
                } else {
                    System.out.println("Action currently invalid");
                }
                break;
            case J:
                player.selectArea();
                break;

            default:
                break;
        }
    }

}
