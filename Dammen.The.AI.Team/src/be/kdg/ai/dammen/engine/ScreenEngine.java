package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.gui.Gui;
import be.kdg.ai.dammen.gui.GuiListener;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;
import be.kdg.ai.dammen.player.Player;

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
    private Player playerBlack = null;
    private Player playerWhite = null;

    public void setGui(Gui gui){
        this.gui = gui;
    }
    public void setGameEngine(GameEngine gameEngine){
        this.gameEngine = gameEngine;
        playerBlack = new Player("Black-Player", TypePiece.Status.BLACK);
        playerWhite = new Player("White-Player", TypePiece.Status.WHITE);
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
            if(currentSelectedPiece.getStatus() == TypePiece.Status.WHITE){
                gameEngine.move(currentSelectedPiece, selectedDestination,playerWhite);
            }
            if(currentSelectedPiece.getStatus() == TypePiece.Status.BLACK){
                gameEngine.move(currentSelectedPiece, selectedDestination,playerBlack);
            }
            selected = false;
        }
    }

    @Override
    public void newGame() {
        gameEngine.initializeGame();
    }
}
