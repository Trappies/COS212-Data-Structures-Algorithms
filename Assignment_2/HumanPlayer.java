import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(PlayerMap gameMap) {
        super(gameMap);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void takeTurn() {
        if (gameMap.currentNode != null && gameMap.currentNode.nodeData.length > gameMap.currentIndexInNode
                && gameMap.currentNode.nodeData[gameMap.currentIndexInNode] != null) {
            GameObject currentObject = (GameObject) gameMap.currentNode.nodeData[gameMap.currentIndexInNode];
            String text = currentObject.visit();
            System.out.println("Current Area:\n" + gameMap.currentArea);
            System.out.println("Current Node:\t" + gameMap.currentNode);
            System.out.println("Current Index:\t" + gameMap.currentIndexInNode);
            System.out.println("Current Object:\t" + currentObject + "\nYou found:\t" + text);
        } else {
            System.out.println("Current Area:\t" + gameMap.currentArea);
            System.out.println("Current Node:\t" + gameMap.currentNode);
            System.out.println("Current Index:\t" + gameMap.currentIndexInNode);
            System.out.println("Current Object:\tNothing here");
        }
        System.out.print("Your action: ");
        String actionString = scanner.nextLine().trim().toUpperCase();
        try {
            if (!"QUIT".equals(actionString)) {
                PlayerMap.Action action = PlayerMap.Action.valueOf(actionString);
                gameMap.takeAction(action);
            } else {
                System.exit(0);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid action. Try again.");
        }
    }

    @Override
    public void selectArea() {
        System.out.print("WHERE: ");
        String actionString = scanner.nextLine().trim();
        gameMap.selectArea(actionString);
    }

    @Override
    public void close() {
        // Cleanup anything you use here
        // HumanPlayer uses this to close its scanner
        scanner.close();
    }
}
