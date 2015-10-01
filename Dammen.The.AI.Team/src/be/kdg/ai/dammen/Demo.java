package be.kdg.ai.dammen;

import be.kdg.ai.dammen.engine.GameEngine;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Demo {
    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.createBoard();
        gameEngine.showBoard();
    }
}
