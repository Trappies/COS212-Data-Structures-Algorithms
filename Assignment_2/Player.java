public abstract class Player {
    protected PlayerMap gameMap;

    public Player(PlayerMap gameMap) {
        this.gameMap = gameMap;
    }

    public abstract void takeTurn();

    public abstract void selectArea();

    public abstract void close();
}
