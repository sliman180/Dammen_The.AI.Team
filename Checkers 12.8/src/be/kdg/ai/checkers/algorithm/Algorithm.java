package be.kdg.ai.checkers.algorithm;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;

/**
 * The algortihm that's going to be used to find the best move for the AI.
 */
public interface Algorithm {
    int NO_PIECES_LEFT = 0;

    BoardState search(BoardState node, boolean maximasingPlayer);

    Position[] getBestMove(BoardState currentBoardState, BoardState node);

    default double calculateHeuristicValue(BoardState node) {
        if (node.getPlayer().isMyTurn() && (node.getBoard().getBlackLeft() == NO_PIECES_LEFT)) {
            return Double.NEGATIVE_INFINITY;
        } else if ((!node.getPlayer().isMyTurn()) && (node.getBoard().getWhiteLeft() == NO_PIECES_LEFT)) {
            return Double.POSITIVE_INFINITY;
        }
        if (node.getPlayer().isMyTurn())
            return node.getBoard().getWhiteLeft() - node.getBoard().getBlackLeft();
        else
            return node.getBoard().getBlackLeft() - node.getBoard().getWhiteLeft();
    }

    default BoardState getMax(BoardState best, BoardState child){
        if (best.getHeuristicValue() > child.getHeuristicValue())
            return best;
        else
            return child;
    }

    default BoardState getMin(BoardState best, BoardState child){
        if (best.getHeuristicValue() < child.getHeuristicValue())
            return best;
        else
            return child;
    }
}
