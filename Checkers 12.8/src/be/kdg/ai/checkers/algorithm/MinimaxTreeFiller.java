package be.kdg.ai.checkers.algorithm;

import be.kdg.ai.checkers.command.IMoveCommand;
import be.kdg.ai.checkers.command.MoveListener;
import be.kdg.ai.checkers.command.NoMoveException;
import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.board.Position;
import be.kdg.ai.checkers.logger.Logger;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * A class that fills the node that's going to be used for the minimax algorithm
 */

public class MinimaxTreeFiller implements TreeFiller, MoveListener {
    private BoardState currentNode;
    private final IMoveCommand moveCommand;
    private final int max_depth;
    private static final Logger logger = Logger.getInstance();


    public MinimaxTreeFiller(BoardState boardState, IMoveCommand moveCommand, int max_depth) {
        this.moveCommand = moveCommand;
        this.max_depth = max_depth;
        currentNode = boardState;
        moveCommand.addListener(this);
    }

    @Override
    public void updateBoardState(BoardState newBoardState, boolean isTreeFiller) {
        // First we check if the update is intended for the minimaxTreefiller,
        // if it's not, then we do not execute this method.
        if (isTreeFiller){
            // The depth of the current node
            int depth = currentNode.getLevel();
            // The received boardState is added to the current node.
            currentNode.add(newBoardState);

            // get all nodes and put them in an Enumeration, then we go through every element of the enumeration and check
            // if the element equals the received boardState. If that is the case then we set the current node to that
            // level so that we can search for further nodes for that node.
            Enumeration nodes = currentNode.depthFirstEnumeration();
            while (nodes.hasMoreElements()){
                BoardState boardState = (BoardState) nodes.nextElement();
                if (boardState.equals(newBoardState) && depth < max_depth) {
//                    String logMessage = String.format("Depth level: %d\n%s\n", depth, boardState.getBoard().toString());
//                    logger.log(Logger.LogType.INFO, logMessage);

                    currentNode = boardState;
                    break;
                }
            }

            depth = currentNode.getLevel(); //nodig?
            // If the depth is smaller than the maximum depth then we can fill the tree for the current node.
            // Else we will return to the parent node so that we can find further child nodes for it.
            if (depth < max_depth) fillTree();
            else currentNode = (BoardState) currentNode.getParent();
        }
    }

    @Override
    public void endGame() {

    }

    @Override
    public BoardState getCurrentNode() {
        return currentNode;
    }

    @Override
    public void fillTree(){
        // The positions where we're going to do movements for are placed in this list.
        List<Position> positions = new ArrayList<>();

        // The list is filled with positions where the Pieces are according to the turn. If it's the turn
        // of the black player, then all positions for the black player are placed in the list.
        if (!currentNode.getPlayer().isMyTurn()){
            for (int row = 0; row < currentNode.getBoard().getSize(); row++) {
                for (int col = 0; col < currentNode.getBoard().getSize(); col++) {
                    if (currentNode.getBoard().getPieces()[row][col] == Piece.BLACK ||
                            currentNode.getBoard().getPieces()[row][col] == Piece.BLACK_KING)
                        positions.add(new Position(row, col));
                }
            }
        }
        else {
            for (int row = 0; row < currentNode.getBoard().getSize(); row++) {
                for (int col = 0; col < currentNode.getBoard().getSize(); col++) {
                    if (currentNode.getBoard().getPieces()[row][col] == Piece.WHITE ||
                            currentNode.getBoard().getPieces()[row][col] == Piece.WHITE_KING)
                        positions.add(new Position(row, col));
                }
            }
        }

        // For every position in the list, all possible destination positions are tried. If a destination position is
        // correct then the movement will be made, and the resulting boardState will be caught by the method
        // updateBoardState() above.
        for (Position currentPosition: positions){
            for (int row = 0; row < currentNode.getBoard().getSize(); row++) {
                for (int col = 0; col < currentNode.getBoard().getSize(); col++) {
                    Piece piece = currentNode.getBoard().getPieces()[currentPosition.getRow()][currentPosition.getColumn()];

                    if ((piece == Piece.BLACK || piece == Piece.BLACK_KING) &&
                            currentNode.getPlayer().isMyTurn())
                        currentNode.getPlayer().toggleTurn();
                    else if ((piece == Piece.WHITE || piece == Piece.WHITE_KING) && !currentNode.getPlayer().isMyTurn()){
                        currentNode.getPlayer().toggleTurn();
                    }
                    try {
                        moveCommand.move(currentNode, currentPosition, new Position(row, col));
                    } catch (NoMoveException e){
                        //no need to log this, it will trigger a lot
                    }
                }
            }
        }

        // If we have calculated every possible move for the current node, we return to the parent node so that we can
        // search for further children of it.
        if (currentNode.getParent() != null) {
            currentNode = (BoardState) currentNode.getParent();
        }
    }


}
