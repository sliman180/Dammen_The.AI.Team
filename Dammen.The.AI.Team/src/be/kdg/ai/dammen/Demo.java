package be.kdg.ai.dammen;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.engine.GameEngine;
import be.kdg.ai.dammen.gui.ConsoleGui;
import be.kdg.ai.dammen.gui.Gui;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Demo {
    private static final int DIMENSION = 10;
    public static void main(String[] args) {
        Board[][] board = new Board[DIMENSION][DIMENSION];
        GameEngine gameEngine = new GameEngine();
        gameEngine.setBoard(board);
        gameEngine.createBoard();

        Gui consoleGui = new ConsoleGui();
        consoleGui.showBoard(board);

    }
}
