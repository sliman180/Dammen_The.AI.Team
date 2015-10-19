package be.kdg.ai.dammen;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.board.BoardFactory;
import be.kdg.ai.dammen.engine.GameEngine;
import be.kdg.ai.dammen.gui.ConsoleGui;
import be.kdg.ai.dammen.gui.Gui;
import be.kdg.ai.dammen.gui.StandaardGui;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Demo {

    public static void main(String[] args) {
        BoardFactory boardFactory = new BoardFactory();
        Gui consoleGui = new ConsoleGui();
        Gui standaardGui = new StandaardGui();
        GameEngine gameEngine = new GameEngine();



        gameEngine.setBoardFactory(boardFactory);
        boardFactory.addListeners(gameEngine);
        gameEngine.setGui(standaardGui);
       // gameEngine.setGui(consoleGui);






        gameEngine.initializeGame();


        gameEngine.doeZet("B07", "C06", 1);
        gameEngine.doeZet("A04","B05",2);
        gameEngine.doeZet("C06","A04",1);




    }
}
