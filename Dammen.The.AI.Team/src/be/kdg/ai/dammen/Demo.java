package be.kdg.ai.dammen;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.board.BoardFactory;
import be.kdg.ai.dammen.engine.GameEngine;
import be.kdg.ai.dammen.engine.ScreenEngine;
import be.kdg.ai.dammen.gui.ConsoleGui;
import be.kdg.ai.dammen.gui.Gui;
import be.kdg.ai.dammen.gui.StandaardGui;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;
import be.kdg.ai.dammen.player.Player;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Demo {

    public static void main(String[] args) {
        Player playerBlack = new Player("Black-Player", TypePiece.Status.BLACK);
        Player playerWhite = new Player("White-Player", TypePiece.Status.WHITE);

        BoardFactory boardFactory = new BoardFactory();
        Gui consoleGui = new ConsoleGui();
        GameEngine gameEngine = new GameEngine();
        ScreenEngine screenEngine = new ScreenEngine();
        gameEngine.setScreenEngine(screenEngine);
        screenEngine.setGameEngine(gameEngine);
        Gui standaardGui = new StandaardGui();

        screenEngine.setGui(standaardGui);
        standaardGui.addListeners(screenEngine);

        gameEngine.setBoardFactory(boardFactory);
        boardFactory.addListeners(gameEngine);

        gameEngine.initializeGame();

        System.out.println("Row=" + gameEngine.getPiece(3, 0).getRow() + " Column=" + gameEngine.getPiece(3, 0)
                .getColumn());

        Piece currentPiece = gameEngine.getPiece(3,0);
        Piece destination = gameEngine.getPiece(4,1);

       // gameEngine.move(currentPiece, destination, playerWhite);

    /*    System.out.println("Row=" + gameEngine.getPiece(3, 0).getRow() + " Column=" + gameEngine.getPiece(3, 0)
                .getColumn());
        System.out.println("Row=" + gameEngine.getPiece(4, 1).getRow() + " Column=" + gameEngine.getPiece(4, 1)
                .getColumn());*/
    //    gameEngine.doeZet("B07", "C06", 1);
    //    gameEngine.doeZet("A04","B05",2);
    //    gameEngine.doeZet("C06","A04",1);




    }
}
