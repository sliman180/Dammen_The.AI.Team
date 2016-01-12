package be.kdg.ai.checkers.domain;

/**
 * This class represents a player.
 */
public class Player {
    private final String name;
    private boolean myTurn;

    public Player(String name) {
        this.name = name;
        myTurn = true;
    }

    public void toggleTurn()
    {
        myTurn = !myTurn;
    }

    public String getName() {
        return name;
    }

    public boolean isMyTurn() {
        return myTurn;
    }
}
