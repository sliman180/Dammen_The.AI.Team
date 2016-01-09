package be.kdg.ai.checkers;

import be.kdg.ai.checkers.algorithm.Algorithm;
import be.kdg.ai.checkers.algorithm.MinimaxAlgorithm;
import be.kdg.ai.checkers.command.IMoveCommand;
import be.kdg.ai.checkers.command.MoveCommand;
import be.kdg.ai.checkers.controller.Controller;
import be.kdg.ai.checkers.controller.GuiController;
import be.kdg.ai.checkers.domain.Player;
import be.kdg.ai.checkers.gui.Gui;
import be.kdg.ai.checkers.gui.SwingGui;
import be.kdg.ai.checkers.manager.GameManager;

/**
 * Created by Sayu on 24/11/2015.
 */
public class Demo {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        Gui swingGui = new SwingGui();
        Controller guiController = new GuiController();
        Player me = new Player("tralala");
        IMoveCommand moveCommand = new MoveCommand(false);
        Algorithm minimax = new MinimaxAlgorithm();


        //autowiren
        guiController.setGui(swingGui);
        guiController.addListener(gameManager);
        gameManager.setController(guiController);
        gameManager.setMoveCommand(moveCommand);
        gameManager.setAlgorithm(minimax);
        swingGui.addListeners(guiController);
        moveCommand.addListener(gameManager);

        gameManager.setMaxDepthForAI(3);
        gameManager.start(me, 8);
    }
}
