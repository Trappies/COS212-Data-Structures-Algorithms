public class Main {
    public static void main(String[] args) throws Exception {
        // humanTester();
        // automaticTester();
        // semiRandomTester();

        BTree<Integer> bTree = new BTree<>(3);
        bTree.insert(50);
        bTree.insert(30);
        bTree.insert(20);
        bTree.insert(40);
        bTree.insert(70);
        bTree.insert(60);
        bTree.insert(80);
        System.out.println(bTree.toString());

        System.out.println("Path to key 15: " + bTree.printPath(15));
        System.out.println("Path to key 25: " + bTree.printPath(25));
        System.out.println("Path to key 30: " + bTree.printPath(30));
    }

    public static void humanTester() {
        System.out.println("TESTING HUMAN PLAYER\n");
        PlayerMap playerMap = new PlayerMap(10, 1234L, PlayerMap.PlayerType.HUMAN);
        playerMap.play();
        System.out.println("Moves taken: " + playerMap.moves);
        System.out.println("Scenario testing completed.\n");
        System.out.println("\n\n");
    }

    public static void semiRandomTester() {
        System.out.println("TESTING SEMI-RANDOM PLAYER\n");
        PlayerMap playerMap = new PlayerMap(10, 1234L, PlayerMap.PlayerType.RANDOM);
        playerMap.play();
        System.out.println("Moves taken: " + playerMap.moves);
        System.out.println("Scenario testing completed.\n");
        System.out.println("\n\n");
    }

    public static void automaticTester() {
        System.out.println("TESTING AUTOMATIC PLAYER\n");
        PlayerMap playerMap = new PlayerMap(10, 1234L, PlayerMap.PlayerType.AUTO);
        playerMap.play();
        System.out.println("Moves taken: " + playerMap.moves);
        System.out.println("Scenario testing completed.\n");
        System.out.println("\n\n");
    }
}

// public class Main {

//     public static void main(String[] args) {
//         // testScenario(PlayerMap.PlayerType.AUTO);
//         // testScenario(PlayerMap.PlayerType.RANDOM);
//         testScenario(PlayerMap.PlayerType.HUMAN);
//     }

//     private static void testScenario(PlayerMap.PlayerType playerType) {
//         System.out.println("Testing scenario with player type: " + playerType);
//         PlayerMap playerMap = new PlayerMap(10, 1234L, playerType);
//         playerMap.play();
//         System.out.println("Moves taken: " + playerMap.moves);
//         System.out.println("Scenario testing completed.\n");
//     }
// }
