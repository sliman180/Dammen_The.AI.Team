package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.gui.Gui;
import be.kdg.ai.dammen.gui.GuiListener;
import be.kdg.ai.dammen.piece.Piece;

import java.awt.*;

/**
 * Created by Sliman on 1-10-2015.
 */
public class ScreenEngine implements GuiListener {
    private Gui gui;
    private Board board;
    private GameEngine gameEngine;
    private boolean selected = false;
    private Piece currentSelectedPiece = null;
    private Piece selectedDestination = null;

    public void setGui(Gui gui){
        this.gui = gui;
    }
    public void setGameEngine(GameEngine gameEngine){
        this.gameEngine = gameEngine;
    }

    protected void sendBoard(Board board){
        this.board = board;
        gui.showBoard(board);
    }
    @Override
    public void hover(int row, int column) {
        System.out.println("Row"+row +" Column"+column);
        Piece tijdelijk = board.getPieces()[row][column];
        System.out.println(tijdelijk.getStatus());
        System.out.println(tijdelijk.getRank());
    }

    @Override
    public void clicked(int row, int column) {
        System.out.println("Row"+row +" Column"+column);

        if (!selected)
        {
            System.out.println("selected");
            currentSelectedPiece = board.getPieces()[row][column];
            gui.changeColor(currentSelectedPiece, Color.RED);
            System.out.println("cur:"+currentSelectedPiece.getStatus());
            System.out.println("currank:"+currentSelectedPiece.getRank());
            selected = true;
        }else
        {
            System.out.println("destination");
            selectedDestination = board.getPieces()[row][column];
            gui.changeColor(currentSelectedPiece,Color.LIGHT_GRAY);
            System.out.println("dest:"+selectedDestination.getStatus());
            System.out.println("destrank:"+selectedDestination.getRank());
            gameEngine.move(currentSelectedPiece, selectedDestination);
            selected = false;
        }
    }
}
