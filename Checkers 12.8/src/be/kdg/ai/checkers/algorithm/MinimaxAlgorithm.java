package be.kdg.ai.checkers.algorithm;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.board.Position;

import java.util.Enumeration;

/**
 * A minimax algorithm that works as an AI for the game. This will determine
 */
public class MinimaxAlgorithm implements Algorithm {
    @Override
    public BoardState search(BoardState node, boolean maximasingPlayer) {
        BoardState bestNode;

        if (node.getChildCount() == 0) {
            bestNode = node;
            bestNode.setHeuristicValue(calculateHeuristicValue(bestNode));
        } else if (maximasingPlayer) {
            bestNode = node;
            bestNode.setHeuristicValue(Double.NEGATIVE_INFINITY);

            // Recurse for all children of the currentnode
            for (int i = 0; i < node.getChildCount(); i++){
                BoardState childValue = search((BoardState) node.getChildAt(i), false);
                bestNode.setHeuristicValue(getMax(bestNode, childValue).getHeuristicValue());
            }
        }
        else {
            bestNode = node;
            bestNode.setHeuristicValue(Double.POSITIVE_INFINITY);

            // Recurse for all children of the currentnode
            for (int i = 0; i < node.getChildCount(); i++){
                BoardState childValue = search((BoardState) node.getChildAt(i), true);
                bestNode.setHeuristicValue(getMin(bestNode, childValue).getHeuristicValue());
            }
        }
        return bestNode;
    }

    @Override
    public Position[] getBestMove(BoardState currentBoardState, BoardState node) {
        Position[] bestPositions = new Position[2];
        Enumeration children = node.children();

        BoardState bestPossibleBoardState = (BoardState)children.nextElement();
        while (children.hasMoreElements()){
            BoardState childBoardState = (BoardState)children.nextElement();
            if (bestPossibleBoardState != null &&
                    bestPossibleBoardState.getHeuristicValue() < childBoardState.getHeuristicValue())
                bestPossibleBoardState = childBoardState;
        }

        if (bestPossibleBoardState != null) {
            for (int row = 0; row < bestPossibleBoardState.getBoard().getSize(); row++) {
                for (int col = 0; col < bestPossibleBoardState.getBoard().getSize(); col++) {
                    if (!bestPossibleBoardState.getBoard().getPieces()[row][col].equals
                            (currentBoardState.getBoard().getPieces()[row][col])){
                        if((currentBoardState.getBoard().getPieces()[row][col] == Piece.BLACK ||
                                currentBoardState.getBoard().getPieces()[row][col] == Piece.BLACK_KING) &&
                                bestPossibleBoardState.getBoard().getPieces()[row][col] == Piece.EMPTY){
                            bestPositions[0] = new Position(row, col);
                        }else if ((bestPossibleBoardState.getBoard().getPieces()[row][col] == Piece.BLACK ||
                                bestPossibleBoardState.getBoard().getPieces()[row][col] == Piece.BLACK_KING) &&
                                currentBoardState.getBoard().getPieces()[row][col] == Piece.EMPTY){
                            bestPositions[1] = new Position(row, col);
                        }
                    }
                }
            }
        }
        return bestPositions;
    }

}
