package be.kdg.ai.checkers.controller;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.board.Position;
import be.kdg.ai.checkers.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller class controls input and output (especially for SwingGui)
 */
public class GuiController implements Controller {
    private Gui gui;
    private List<ControllerListener> listeners;
    private BoardState currentBoardState;
    private Position current, destination;

    public GuiController() {
        this.listeners = new ArrayList<ControllerListener>();
        clearPositionClicked();
    }

    private void clearPositionClicked() {
        current = null;
        destination = null;
    }

    @Override
    public void sendBoardStateToGui(BoardState boardState) {
        this.currentBoardState = boardState;
        gui.showBoard(boardState);
    }

    @Override
    public void addListener(ControllerListener listenerToAdd) {
        listeners.add(listenerToAdd);
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void gameWon(String player) {

        gui.showMessage(player + " won the game");
    }


    private void tryMove(int row, int column) {
        if (current == null) {
            current = new Position(row, column);
            gui.changeColor(current, Color.RED);
        } else {
            destination = new Position(row, column);
            for (ControllerListener listener : listeners) {
                if (checkPlayerTurn()){
                    listener.move(currentBoardState, current, destination);
                }else if(checkAITurn()){
                    listener.move(currentBoardState, current, destination);
                }
            }
            gui.changeColor(current, Color.LIGHT_GRAY);
            clearPositionClicked();
        }
    }

    private boolean checkPlayerTurn() {
        return (currentBoardState.getBoard().getPieces()[current.getRow()][current.getColumn()] == Piece.WHITE
                || currentBoardState.getBoard().getPieces()[current.getRow()][current.getColumn()] == Piece.WHITE_KING)
                && currentBoardState.getPlayer().isMyTurn();
    }
    private boolean checkAITurn() {
        return (currentBoardState.getBoard().getPieces()[current.getRow()][current.getColumn()] == Piece.BLACK
                || currentBoardState.getBoard().getPieces()[current.getRow()][current.getColumn()] == Piece.BLACK_KING)
                && !currentBoardState.getPlayer().isMyTurn();
    }

    @Override
    public void hover(int row, int column) {
        //System.out.println("r: "+row+"c: "+column);
    }

    @Override
    public void clicked(int row, int column) {
        boolean turn = currentBoardState.getPlayer().isMyTurn();
        System.out.println("turn:"+currentBoardState.getPlayer().isMyTurn());
        if (turn){
            tryMove(row, column);
        }
    }


    @Override
    public void newGame() {

    }
}
